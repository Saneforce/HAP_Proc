package com.hap.checkinproc.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hap.checkinproc.Common_Class.LocationServices;
import com.hap.checkinproc.HAPApp;
import com.hap.checkinproc.R;

import java.util.Timer;

public class TimerService extends Service {
    private static final String TAG = TimerService.class.getSimpleName();
    public static final int notify = 1000;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    private Boolean UpdtFlag = false;

    @Override
    public IBinder onBind(Intent intent) {

        /*        *//*if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else*//*
        mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);*/
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        Log.d("service is ", "SERVICE IS CREATED");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service is ", "SERVICE IS DESTROYED");
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Intent inten = new Intent(this, TimerService.class);
        startService(inten);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);

        return START_STICKY;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Activity cAtivity = HAPApp.getActiveActivity();
        String sMsg = "";
        Context context = getApplicationContext();

        LocationServices locationServices = new LocationServices(cAtivity, context);

        if (locationServices.checkPermission() == false) {
            sMsg = "NO PERMISSION";
            Log.v("KARTHIC_KUMAR", sMsg);
        } else {
            /*   sMsg = "PERMISIN IS THERE";*/
            Log.v("KARTHIC_KUMAR", sMsg);
        }


        ViewGroup rootView = cAtivity.getWindow().getDecorView().findViewById(android.R.id.content);

        try {
            RelativeLayout el = rootView.findViewById(4231);
            if (el.getVisibility() == View.VISIBLE) {
                rootView.removeView(el);
            }
        } catch (Exception e) {
        }


        if (!sMsg.equalsIgnoreCase("")) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    (RelativeLayout.LayoutParams.MATCH_PARENT), (RelativeLayout.LayoutParams.MATCH_PARENT));

            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            RelativeLayout relative = new RelativeLayout(getApplicationContext());
            relative.setId(4231);
            relative.setLayoutParams(lp);
            relative.setBackgroundColor(Color.parseColor("#FFFFFF"));

            RelativeLayout.LayoutParams layoutparams_4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams_4.addRule(RelativeLayout.CENTER_IN_PARENT);

            layoutparams_4.setMargins(0, 0, 3, 0);
            ImageView img = new ImageView(this);
            img.setId(123);
            img.setImageResource(R.drawable.location);
            img.setLayoutParams(layoutparams_4);
            relative.addView(img);

            RelativeLayout.LayoutParams layoutparams_4s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            layoutparams_4s.addRule(RelativeLayout.CENTER_IN_PARENT);

            layoutparams_4s.addRule(RelativeLayout.BELOW, 123);
            TextView edt = new TextView(this);
            edt.setLayoutParams(layoutparams_4s);
            edt.setTextColor(Color.BLACK);
            /*edt.setPadding(150,10,10,10);*/
            edt.setText("Please provide AllOW ALWAYS in the permission setting to access the Application");
            relative.addView(edt);
            rootView.addView(relative);

        }

        super.onTaskRemoved(rootIntent);
    }

}
