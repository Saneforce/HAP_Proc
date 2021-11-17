package com.hap.checkinproc.SFA_Activity;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.common.ConnectivityReceiver;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HAPApp extends Application {

    private ApiComponent mApiComponent;
    public static Activity activeActivity;
    private BroadcastReceiver mNetworkReceiver;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    SharedPreferences CommUserDetails;
    public static final String UserDetail = "MyPrefs";
    @Override
    public void onCreate() {
        super.onCreate();


      /*  mApiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule("https://hap.sanfmcg.com/server/"))
                .build();*/
        setupActivityListener();
        mNetworkReceiver=new ConnectivityReceiver();
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    private void setupActivityListener() {

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activeActivity = activity;
                CommUserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
                try
                {
                    if(!CommUserDetails.getString("Sfcode","").equalsIgnoreCase(""))
                    startService(new Intent(activeActivity, TimerService.class));
                }catch (Exception e){}
                Shared_Common_Pref.Sf_Code = CommUserDetails.getString("Sfcode", "");
                Shared_Common_Pref.Sf_Name = CommUserDetails.getString("SfName", "");
                Shared_Common_Pref.Div_Code = CommUserDetails.getString("Divcode", "");
                Shared_Common_Pref.StateCode = CommUserDetails.getString("State_Code", "");

            }

            @Override
            public void onActivityStarted(Activity activity) {


            }

            @Override
            public void onActivityResumed(Activity activity) {
                activeActivity = activity;
                try
                {
                    if(!CommUserDetails.getString("Sfcode","").equalsIgnoreCase(""))
                    startService(new Intent(activeActivity, TimerService.class));
                }catch (Exception e){}
            }

            @Override
            public void onActivityPaused(Activity activity) {
                //  activeActivity = null;
                Log.v("LOG_IN_LOCATION", "ONPAUSE");
                LocalBroadcastManager.getInstance(activeActivity).unregisterReceiver(mRegistrationBroadcastReceiver);

            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //unregisterReceiver(mNetworkReceiver);
            }
        });
    }

    public static Activity getActiveActivity() {
        return activeActivity;
    }
    public static void sendOFFlineLocations(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        DatabaseHandler db = new DatabaseHandler(activeActivity);
        JSONArray locations = db.getAllPendingTrackDetails();
        if (locations.length() > 0) {
            try {
                SharedPreferences UserDetails = activeActivity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                if (UserDetails.getString("Sfcode", "") != "") {
                    Call<JsonObject> call = apiInterface.JsonSave("save/trackall", "3", UserDetails.getString("Sfcode", ""), "", "", locations.toString());
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            // Get result Repo from response.body()
                            db.deleteAllTrackDetails();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("LocationUpdate", "onFailure Local Location"+t.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public ApiComponent getNetComponent() {
        return mApiComponent;
    }
}