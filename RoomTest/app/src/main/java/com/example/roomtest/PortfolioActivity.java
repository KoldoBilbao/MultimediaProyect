package com.example.roomtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class PortfolioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PortfolioAdapter portfolioAdapter;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        recyclerView = findViewById(R.id.portfolioRecyclerView);
        Button addButton = findViewById(R.id.addButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appDatabase = AppDatabase.getInstance(this);

        loadPortfolio();

        addButton.setOnClickListener(v -> showAddCryptoDialog());

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadPortfolio() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Portfolio> portfolioList = appDatabase.portfolioDao().getAllPortfolioItems();
            runOnUiThread(() -> {
                portfolioAdapter = new PortfolioAdapter(
                        portfolioList,
                        this::showEditCryptoDialog, // Para editar
                        this::showDeleteConfirmationDialog // Para eliminar
                );
                recyclerView.setAdapter(portfolioAdapter);
            });
        });
    }

    private void showAddCryptoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_crypto, null);
        builder.setView(dialogView);

        EditText symbolInput = dialogView.findViewById(R.id.symbolInput);
        EditText quantityInput = dialogView.findViewById(R.id.quantityInput);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            String symbol = symbolInput.getText().toString().trim();
            String quantityStr = quantityInput.getText().toString().trim();

            if (symbol.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            double quantity = Double.parseDouble(quantityStr);

            fetchCryptoDetailsAndSave(symbol, quantity, dialog);
        });

        dialog.show();
    }

    private void fetchCryptoDetailsAndSave(String symbol, double quantity, AlertDialog dialog) {
        CryptoApi cryptoApi = RetrofitClient.getInstance().create(CryptoApi.class);
        cryptoApi.getTopCryptos("usd", "market_cap_desc", 100, 1).enqueue(new retrofit2.Callback<List<Crypto>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Crypto>> call, retrofit2.Response<List<Crypto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Crypto crypto : response.body()) {
                        if (crypto.getSymbol().equalsIgnoreCase(symbol)) {
                            Portfolio portfolio = new Portfolio(
                                    crypto.getId(),
                                    crypto.getName(),
                                    crypto.getSymbol(),
                                    quantity,
                                    crypto.getCurrent_price()
                            );
                            savePortfolioItem(portfolio);
                            dialog.dismiss();
                            return;
                        }
                    }
                    Toast.makeText(PortfolioActivity.this, "Crypto symbol not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Crypto>> call, Throwable t) {
                Toast.makeText(PortfolioActivity.this, "Failed to fetch crypto details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePortfolioItem(Portfolio portfolio) {
        Executors.newSingleThreadExecutor().execute(() -> {
            appDatabase.portfolioDao().insertPortfolio(portfolio);
            runOnUiThread(() -> {
                Toast.makeText(this, "Crypto added to portfolio", Toast.LENGTH_SHORT).show();
                loadPortfolio();
            });
        });
    }

    private void showEditCryptoDialog(Portfolio portfolio) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_crypto, null);
        builder.setView(dialogView);

        EditText symbolInput = dialogView.findViewById(R.id.symbolInput);
        EditText quantityInput = dialogView.findViewById(R.id.quantityInput);
        Button saveButton = dialogView.findViewById(R.id.saveButton);
        Button deleteButton = dialogView.findViewById(R.id.deleteButton);

        symbolInput.setText(portfolio.getSymbol());
        quantityInput.setText(String.valueOf(portfolio.getQuantity()));

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            String symbol = symbolInput.getText().toString().trim();
            String quantityStr = quantityInput.getText().toString().trim();

            if (symbol.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            double quantity = Double.parseDouble(quantityStr);
            portfolio.setSymbol(symbol);
            portfolio.setQuantity(quantity);

            Executors.newSingleThreadExecutor().execute(() -> {
                appDatabase.portfolioDao().updatePortfolio(portfolio);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Crypto updated successfully", Toast.LENGTH_SHORT).show();
                    loadPortfolio();
                    dialog.dismiss();
                });
            });
        });

        deleteButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                appDatabase.portfolioDao().deletePortfolio(portfolio);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Crypto deleted successfully", Toast.LENGTH_SHORT).show();
                    loadPortfolio();
                    dialog.dismiss();
                });
            });
        });

        dialog.show();
    }

    private void showDeleteConfirmationDialog(Portfolio portfolio) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Cryptocurrency")
                .setMessage("Are you sure you want to delete this cryptocurrency from your portfolio?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        appDatabase.portfolioDao().deletePortfolio(portfolio);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Cryptocurrency deleted", Toast.LENGTH_SHORT).show();
                            loadPortfolio();
                        });
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }



}
