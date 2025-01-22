package com.example.roomtest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.coingecko.com/api/v3/";
    private static Retrofit retrofitInstance;

    // Método para obtener una instancia única de Retrofit
    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            synchronized (RetrofitClient.class) { // Para asegurar que sea thread-safe
                if (retrofitInstance == null) {
                    retrofitInstance = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofitInstance;
    }

    // Método genérico para crear servicios de Retrofit
    public static <S> S createService(Class<S> serviceClass) {
        return getInstance().create(serviceClass);
    }
}