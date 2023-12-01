package com.hap.checkinproc.SFA_Activity;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.MainActivity;
import com.hap.checkinproc.DefaultLauncherAlias;
import com.hap.checkinproc.DistributorLauncherAlias;
import com.hap.checkinproc.FFALauncherAlias;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
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

    public static String CurrencySymbol =  "₹";//₹ B$
    public static String MRPCap = "MRP";//₹ B$
    public static String MyAppID = "com.hap.checkinproc";
    public static String StockCheck = "1";//₹ B$
    SharedPreferences UserDetails;
    public static Boolean ProductsLoaded = false;
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
                Log.e("ActivityName", activeActivity.getClass().getSimpleName());
                CommUserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
                try
                {
                    if(!CommUserDetails.getString("Sfcode","").equalsIgnoreCase("")) {
                        startService(new Intent(activeActivity, TimerService.class));
                    }
                }catch (Exception e){}
                Shared_Common_Pref.Sf_Code = CommUserDetails.getString("Sfcode", "");
                Shared_Common_Pref.Sf_Name = CommUserDetails.getString("SfName", "");
                Shared_Common_Pref.Div_Code = CommUserDetails.getString("Divcode", "");
                Shared_Common_Pref.StateCode = CommUserDetails.getString("State_Code", "");

                StockCheck=CommUserDetails.getString("StockCheck", "1");
                CurrencySymbol = getActiveActivity().getResources().getString(R.string.Currency); //"₹";//₹
                MRPCap = getActiveActivity().getResources().getString(R.string.MRPCAP);//₹ B$
             //   if(Shared_Common_Pref.Sf_Code.equalsIgnoreCase("7951"))
                Shared_Common_Pref sharedCommonPref = new Shared_Common_Pref(HAPApp.this);
                if (sharedCommonPref.getIntValue(Constants.Dist_Export_Flag)==1){
                  //  CurrencySymbol = "B$"; //"₹";//₹ B$
                    CurrencySymbol=sharedCommonPref.getvalue(Constants.Export_Currency_Symbol);
                    MRPCap = sharedCommonPref.getvalue(Constants.Export_MRP);//₹ B$
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {


            }

            @Override
            public void onActivityResumed(Activity activity) {
                activeActivity = activity;
                try
                {
                    if(!CommUserDetails.getString("Sfcode","").equalsIgnoreCase("")) {
                        startService(new Intent(activeActivity, TimerService.class));
                    }
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
                            call.cancel();
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

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    public static void setAppLogos(){

        try {
            PackageManager manager = getActiveActivity().getPackageManager();
//            if (Shared_Common_Pref.LOGINTYPE.equalsIgnoreCase(Constants.DISTRIBUTER_TYPE)) {
//                 //enable old icon
//                manager.setComponentEnabledSetting(new ComponentName(activeActivity, DistributorLauncherAlias.class)
//                        , PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//                // disable new icon
//                manager.setComponentEnabledSetting(new ComponentName(activeActivity, FFALauncherAlias.class)
//                        , PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//                manager.setComponentEnabledSetting(new ComponentName(activeActivity, MainActivity.class)
//                        , PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//                Toast.makeText(activeActivity, "Enable " + Constants.DISTRIBUTER_TYPE + " Icon", Toast.LENGTH_LONG).show();
//            } else {
//
//                manager.setComponentEnabledSetting(new ComponentName(activeActivity, FFALauncherAlias.class)
//                        , PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//                // disable new icon
//                manager.setComponentEnabledSetting(new ComponentName(activeActivity, DistributorLauncherAlias.class)
//                        , PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//                manager.setComponentEnabledSetting(new ComponentName(activeActivity, MainActivity.class)
//                        , PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//                Toast.makeText(activeActivity, "Enable " + Constants.CHECKIN_TYPE + " Icon", Toast.LENGTH_LONG).show();
//
//
//            }

        } catch (Exception e) {
            Log.v("launcherIcon:", e.getMessage());
        }

    }
}