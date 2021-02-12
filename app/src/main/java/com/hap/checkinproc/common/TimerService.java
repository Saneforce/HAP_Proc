package com.hap.checkinproc.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.Block_Information;
import com.hap.checkinproc.Activity_Hap.Common_Class;
import com.hap.checkinproc.Activity_Hap.Login;
import com.hap.checkinproc.HAPApp;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerService extends Service {
    private static final String TAG = TimerService.class.getSimpleName();
    public static final int notify = 1000;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    private Boolean UpdtFlag=false;
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
public void startTimerService(){
    startService(new Intent(this, TimerService.class));
    }
    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Log.d("service is ","Destroyed");
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @SuppressLint("ResourceType")
                @Override
                public void run() {
                    Activity cAtivity=HAPApp.getActiveActivity();
                    String sMsg="";
                    Context context = getApplicationContext();
                    Connectivity cn=new Connectivity();
                    if(Connectivity.isConnected(context)==false){
                        sMsg="No Internet Connectivity detected!.Kindly check your Internet Data Settings";
                    }else if(Connectivity.isConnectedFast(context)==false){
                        sMsg="Poor internet connectivity detected,access will take more time.";
                    }
                    ViewGroup rootView = cAtivity.getWindow().getDecorView().findViewById(android.R.id.content);

                    try {
                        RelativeLayout el=rootView.findViewById(4231);
                        if(el.getVisibility()==View.VISIBLE){
                            rootView.removeView(el);
                        }
                    } catch(Exception e){}

                    if (context != null) {
                        DatabaseHandler db = new DatabaseHandler(context);
                        JSONArray locations=db.getAllPendingTrackDetails();
                        if(locations.length()>0){
                            try {
                                if(UpdtFlag==false) {
                                    UpdtFlag = true;
                                    JSONObject loc = locations.getJSONObject(0);

                                    loc.put("DvcID", "");
                                    JSONArray param = new JSONArray();
                                    param.put(loc);
                                    SharedPreferences UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                    if (UserDetails.getString("Sfcode", "") != "") {
                                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                        Call<JsonObject> call = apiInterface.JsonSave("save/track", "3", UserDetails.getString("Sfcode", ""), "", "", param.toString());
                                        call.enqueue(new Callback<JsonObject>() {
                                            @Override
                                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                // Get result Repo from response.body()
                                                db.deleteTrackDetails(loc);
                                                UpdtFlag=false;
                                                Log.d(TAG, "Local Location" + String.valueOf(response.body()));
                                            }

                                            @Override
                                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                                UpdtFlag=false;
                                                Log.d(TAG, "onFailure Local Location");
                                            }
                                        });
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (isTimeAutomatic(context) != true ) {
                            if(HAPApp.activeActivity.getClass()!=Block_Information.class){
                                Intent nwScr = new Intent(context, Block_Information.class);
                                nwScr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(nwScr);
                            }
                        }
                    }

                    SharedPreferences CheckInDetails = getSharedPreferences("CheckInDetail", Context.MODE_PRIVATE);
                    String ACutOff=CheckInDetails.getString("ShiftCutOff","");
                    if(!ACutOff.equalsIgnoreCase("")){

                        Common_Class Dt=new Common_Class();
                        Date CutOff=Dt.getDate(ACutOff);
                        String sDt=Dt.GetDateTime(getApplicationContext(),"yyyy-MM-dd HH:mm:ss");
                        Date Cdate=Dt.getDate(sDt);
                        if (Cdate.getTime()>=CutOff.getTime()){
                            Log.d("Cutoff","Time REached");
                            SharedPreferences UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = UserDetails.edit();
                            editor.putBoolean("Login", false);
                            editor.apply();
                            SharedPreferences.Editor cInEditor = CheckInDetails.edit();
                            cInEditor.remove("Shift_Selected_Id");
                            cInEditor.remove("Shift_Name");
                            cInEditor.remove("ShiftStart");
                            cInEditor.remove("ShiftEnd");
                            cInEditor.remove("ShiftCutOff");
                            cInEditor.remove("FTime");
                            cInEditor.remove("Logintime");
                            cInEditor.putBoolean("CheckIn", false);
                            cInEditor.apply();
                            Intent nwScr = new Intent(context, Login.class);
                            nwScr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(nwScr);
                            /*window.localStorage.removeItem("Sfift_End_Time");
                            window.localStorage.removeItem("LOGIN");
                            $state.go('signin');*/
                        }
                    }
                    if (!sMsg.equalsIgnoreCase("")) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                (RelativeLayout.LayoutParams.MATCH_PARENT), (RelativeLayout.LayoutParams.WRAP_CONTENT));
                        lp.setMargins(13, 13, 13, 13);

                        RelativeLayout relative = new RelativeLayout(getApplicationContext());
                        relative.setId(4231);
                        relative.setLayoutParams(lp);
                        relative.setBackgroundColor(Color.parseColor("#b75501"));

                        TextView tv = new TextView(getApplicationContext());
                        tv.setLayoutParams(lp);

                        tv.setText(sMsg);
                        tv.setTextColor(Color.parseColor("#ffffff"));
                        relative.addView(tv);

                        rootView.addView(relative);
                        Log.d("service is ", "running" + cAtivity.getClass().getName());
                    }

                }

            });
        }
        public boolean isTimeAutomatic(Context c) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
            } else {
                return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
            }
        }
    }
}
