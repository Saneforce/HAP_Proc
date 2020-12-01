package com.hap.checkinproc.Interface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://hap.sanfmcg.com/server/";
    public static final String BASE_URL2 = "http://www.fmcg.sanfmcg.com/server/";
    private static Retrofit retrofit = null;



    private static Retrofit retrofit1 = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
           return retrofit;
    }

    public static Retrofit getClient2() {
        if (retrofit1==null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
           return retrofit1;
    }



}
