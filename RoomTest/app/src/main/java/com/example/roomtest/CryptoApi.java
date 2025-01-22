package com.example.roomtest;

import java.util.List;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CryptoApi {
    @GET("coins/markets")
    Call<List<Crypto>> getTopCryptos(
            @Query("vs_currency") String vsCurrency,
            @Query("order") String order,
            @Query("per_page") int perPage,
            @Query("page") int page
    );

    @GET("coins/{id}/history")
    Call<JsonObject> getHistoricalData(
            @Path("id") String coinId,
            @Query("date") String date
    );
}