package com.hap.checkinproc.Interface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FreezerAPIClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor("PORTAL_API", "HAPAPI$&"))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://portal.hap.in/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
