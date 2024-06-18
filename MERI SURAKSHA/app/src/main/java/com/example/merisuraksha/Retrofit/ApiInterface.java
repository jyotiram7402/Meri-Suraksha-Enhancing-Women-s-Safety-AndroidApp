package com.example.merisuraksha.Retrofit;



import com.example.merisuraksha.Domain.POJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    final String BASE_URL = "https://newsapi.org/v2/";

    @GET("top-headlines")
    Call<POJO> getNews(
            @Query("country") String country,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String api
    );

    @GET("top-headlines")
    Call<POJO> getCateNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String api
    );
}
