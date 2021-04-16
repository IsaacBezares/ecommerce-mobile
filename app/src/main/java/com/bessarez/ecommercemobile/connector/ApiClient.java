package com.bessarez.ecommercemobile.connector;

import com.bessarez.ecommercemobile.interfaces.EcommerceApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://ib-ecommerce.herokuapp.com";

    private static Retrofit getApiConnection() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static EcommerceApi getApiService(){
        return getApiConnection().create(EcommerceApi.class);
    }
}
