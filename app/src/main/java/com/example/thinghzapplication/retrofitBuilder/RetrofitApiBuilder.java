package com.example.thinghzapplication.retrofitBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitApiBuilder  {
    private final String baseUrl = "https://api.thinghz.com/";
    private  Retrofit retrofit = null;
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();
    public RetrofitApiBuilder(Retrofit retrofit) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        this.retrofit = retrofit;
    }

    public Retrofit getRetrofitClient(){
        return retrofit;
    }
}

