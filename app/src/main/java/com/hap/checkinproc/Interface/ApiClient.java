package com.hap.checkinproc.Interface;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
     // public static String BASE_URL = "http://hapqc.sanfmcg.com/server/"; //field  9.1.1
    // public static String BASE_URL = "http://Checkin.hap.in/server/";  //Non field  6.1.2
   //  public static String BASE_URL = "http://hapqc.sanfmcg.com/server/"; //field  9.1.1//
      public static String BASE_URL = "http://primary.hap.in/server/";//distri 8.3.4
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        Log.d("BaseURL", BASE_URL);
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.writeTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES);

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
//        }

            OkHttpClient okHttpClient = builder.build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofit(){

        if(retrofit == null){
            retrofit= new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
