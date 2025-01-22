package com.example.roomtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CryptoAdapter cryptoAdapter;
    private Button myPortfolioButton; // Botón My Portfolio

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Vincular vistas
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myPortfolioButton = view.findViewById(R.id.myPortfolioButton);

        // Configurar el botón My Portfolio
        myPortfolioButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PortfolioActivity.class);
            startActivity(intent);
        });

        // Cargar datos de las criptomonedas
        fetchCryptoData();

        return view;
    }

    private void fetchCryptoData() {
        CryptoApi cryptoApi = RetrofitClient.getInstance().create(CryptoApi.class);

        Call<List<Crypto>> call = cryptoApi.getTopCryptos("usd", "market_cap_desc", 20, 1);
        call.enqueue(new Callback<List<Crypto>>() {
            @Override
            public void onResponse(Call<List<Crypto>> call, Response<List<Crypto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Crypto> cryptoList = response.body();
                    cryptoAdapter = new CryptoAdapter(cryptoList);
                    recyclerView.setAdapter(cryptoAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Crypto>> call, Throwable t) {
                // Manejar errores aquí
            }
        });
    }
}
