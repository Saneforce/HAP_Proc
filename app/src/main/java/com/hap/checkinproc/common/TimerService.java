package com.hap.checkinproc.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.hap.checkinproc.HAPApp;
import com.hap.checkinproc.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    public static final int notify = 2000;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

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
                    if (sMsg != "") {
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
    }
}
