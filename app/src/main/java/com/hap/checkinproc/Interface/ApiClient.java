package com.hap.checkinproc.Interface;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.SFA_Activity.HAPApp;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String BASE_URL = "http://hapapps.sanfmcg.com/server/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (Shared_Common_Pref.LOGINTYPE.equalsIgnoreCase(Constants.DISTRIBUTER_TYPE)) {
            BASE_URL="http://hapsfa.sanfmcg.com/server/";
        }
        Log.d("BaseURL",BASE_URL);
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
