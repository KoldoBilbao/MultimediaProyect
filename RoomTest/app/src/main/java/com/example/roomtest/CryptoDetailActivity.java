package com.example.roomtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CryptoDetailActivity extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_detail);

        // Vincular vistas
        ImageView cryptoImage = findViewById(R.id.crypto_image);
        TextView cryptoName = findViewById(R.id.crypto_name);
        TextView cryptoSymbol = findViewById(R.id.crypto_symbol);
        TextView cryptoPrice = findViewById(R.id.crypto_price);
        TextView cryptoMarketCap = findViewById(R.id.crypto_market_cap);
        TextView cryptoMaxSupply = findViewById(R.id.crypto_max_supply);
        TextView cryptoCirculatingSupply = findViewById(R.id.crypto_circulating_supply);
        TextView cryptoSupplyPercentage = findViewById(R.id.crypto_supply_percentage);
        Button backButton = findViewById(R.id.back_button);
        lineChart = findViewById(R.id.line_chart);

        // Obtener datos pasados por el Intent
        Intent intent = getIntent();
        if (intent != null) {
            String coinId = intent.getStringExtra("coinId");
            String image = intent.getStringExtra("image");
            String name = intent.getStringExtra("name");
            String symbol = intent.getStringExtra("symbol");
            double price = intent.getDoubleExtra("price", 0.0);
            long marketCap = intent.getLongExtra("market_cap", 0);
            double maxSupply = intent.getDoubleExtra("max_supply", -1);
            double circulatingSupply = intent.getDoubleExtra("circulating_supply", 0);

            // Configurar datos en vistas
            Glide.with(this).load(image).placeholder(R.drawable.placeholder).error(R.drawable.error_image).into(cryptoImage);
            cryptoName.setText(name);
            cryptoSymbol.setText("Symbol: " + symbol);
            cryptoPrice.setText("Price: $" + new DecimalFormat("#,###").format(price));
            cryptoMarketCap.setText("Market Cap: $" + new DecimalFormat("#,###").format(marketCap));
            cryptoMaxSupply.setText(maxSupply > 0 ? "Max Supply: " + new DecimalFormat("#,###").format(maxSupply) : "Max Supply: ∞");
            cryptoCirculatingSupply.setText("Circulating Supply: " + new DecimalFormat("#,###").format(circulatingSupply));

            // Calcular porcentaje de suministro liberado
            if (maxSupply > 0) {
                double supplyPercentage = (circulatingSupply / maxSupply) * 100;
                cryptoSupplyPercentage.setText("Supply Released: " + String.format(Locale.getDefault(), "%.2f%%", supplyPercentage));
            } else {
                cryptoSupplyPercentage.setText("Supply Released: ∞");
            }

            // Cargar datos históricos para el gráfico
            fetchWeeklyData(coinId);
        }

        // Configurar botón de volver
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchWeeklyData(String coinId) {
        // Reiniciar la lista de entradas cada vez que se cargan nuevos datos
        List<Entry> entries = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 4; i++) { // 4 semanas
            String date = dateFormat.format(calendar.getTime());
            int finalIndex = i;

            new Handler().postDelayed(() -> {
                fetchDailyData(coinId, date, entries, finalIndex);
            }, i * 2000); // Incrementar delay a 2 segundos por solicitud

            calendar.add(Calendar.DAY_OF_MONTH, -7); // Retroceder una semana
        }
    }

    private void fetchDailyData(String coinId, String date, List<Entry> entries, int index) {
        CryptoApi cryptoApi = RetrofitClient.getInstance().create(CryptoApi.class);

        Call<JsonObject> call = cryptoApi.getHistoricalData(coinId, date);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject marketData = response.body().getAsJsonObject("market_data");
                    if (marketData != null) {
                        try {
                            float price = marketData.getAsJsonObject("current_price").get("usd").getAsFloat();
                            entries.add(new Entry(index, price));
                            Log.d("API Success", "Data added for date: " + date + ", price: " + price);

                            // Mostrar gráfico cuando se tengan los 5 puntos
                            if (entries.size() == 5) {
                                displayChart(entries);
                            }
                        } catch (Exception e) {
                            Log.e("API Error", "Data missing for date: " + date);
                        }
                    } else {
                        Log.e("API Error", "Market data is null for date: " + date);
                    }
                } else {
                    Log.e("API Error", "Error fetching data for date: " + date);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API Error", "Error fetching data: " + t.getMessage());
            }
        });
    }

    private void displayChart(List<Entry> entries) {
        if (entries.isEmpty()) {
            Toast.makeText(this, "No data available for the chart", Toast.LENGTH_SHORT).show();
            return;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Price (USD)");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setLineWidth(2f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Configuración del eje X para mostrar índices correctamente
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setGranularity(1f); // Asegurar que los puntos sean equidistantes
        lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int weekNumber = Math.round(value) + 1; // Redondear y convertir a entero
                return "Week " + weekNumber;
            }
        });

        lineChart.invalidate(); // Refrescar gráfico
    }

}
