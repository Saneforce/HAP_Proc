package com.hap.checkinproc.Interface;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //public static String BASE_URL = "http://ff.hap.in/server/";
//    public static String BASE_URL = "http://Checkin.hap.in/server/";//Non field  6.0.11
//    public static String BASE_URL = "http://hsfa.sanfmcg.com/server/";//field
    public static String BASE_URL = "http://primary.hap.in/server/";//distri 8.1.1
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        // if (Shared_Common_Pref.LOGINTYPE.equalsIgnoreCase(Constants.DISTRIBUTER_TYPE)) {
        //    BASE_URL = "http://primary.hap.in/server/";
        //   }
        //  BASE_URL = "http://hapins.sanfmcg.com/server/";
        //   BASE_URL = "https://checkin.hap.in/";

        Log.d("BaseURL", BASE_URL);
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
