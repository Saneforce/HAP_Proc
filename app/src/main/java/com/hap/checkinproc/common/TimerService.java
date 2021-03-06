package com.hap.checkinproc.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
            relative.setGravity(Gravity.CENTER);
            relative.setId(4231);
            relative.setLayoutParams(lp);
            relative.setBackgroundColor(Color.parseColor("#FFFFFF"));

            RelativeLayout.LayoutParams ImageRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ImageRel.addRule(RelativeLayout.CENTER_HORIZONTAL);

            ImageRel.setMargins(0, 0, 3, 0);
            ImageView img = new ImageView(this);
            img.setId(123);
            img.setImageResource(R.drawable.location);
            img.setLayoutParams(ImageRel);
            relative.addView(img);


            RelativeLayout.LayoutParams headRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            headRel.addRule(RelativeLayout.CENTER_IN_PARENT);
            headRel.addRule(RelativeLayout.BELOW, 123);
            headRel.setMargins(50, 70, 50, 50);
            TextView headTxt = new TextView(this);
            headTxt.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            headTxt.setGravity(Gravity.CENTER);
            headTxt.setLayoutParams(headRel);
            headTxt.setTextColor(Color.BLACK);
            headTxt.setId(12);
            headTxt.setTextSize(20);
            headTxt.setPadding(0, 10, 10, 10);
            headTxt.setTypeface(null, Typeface.BOLD);
            headTxt.setText("Location permission required");
            relative.addView(headTxt);


            RelativeLayout.LayoutParams subheadRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            subheadRel.addRule(RelativeLayout.CENTER_HORIZONTAL);
            subheadRel.addRule(RelativeLayout.BELOW, 12);
            subheadRel.setMargins(50, 10, 50, 50);
            TextView subheadTxt = new TextView(this);
            subheadTxt.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            subheadTxt.setGravity(Gravity.CENTER);
            subheadTxt.setLayoutParams(subheadRel);
            subheadTxt.setTextColor(Color.parseColor("#585858"));
            subheadTxt.setId(152);
            subheadTxt.setTextSize(16);
            subheadTxt.setPadding(0, 10, 10, 10);
            /* edt.setText("Please provide AllOW ALWAYS in the permission setting to access the Application");*/
            subheadTxt.setText("Allow Hap to automatically detect your current location for travel allowance");
            relative.addView(subheadTxt);


            RelativeLayout.LayoutParams enableRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            enableRel.addRule(RelativeLayout.CENTER_HORIZONTAL);
            enableRel.addRule(RelativeLayout.BELOW, 152);
            enableRel.setMargins(50, 50, 50, 50);
            TextView edt = new TextView(this);
            edt.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            edt.setGravity(Gravity.CENTER);
            edt.setLayoutParams(enableRel);
            edt.setTextColor(Color.BLACK);
            edt.setId(1520);
            edt.setTextSize(12);
            edt.setPadding(0, 10, 10, 10);
            /* edt.setText("Please provide AllOW ALWAYS in the permission setting to access the Application");*/
            edt.setText("To enable, go to 'Settings' and turn on Location permission 'Allow Always'");
            relative.addView(edt);


            RelativeLayout.LayoutParams btnRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100);

            btnRel.addRule(RelativeLayout.CENTER_HORIZONTAL);

            btnRel.addRule(RelativeLayout.BELOW, 1520);
            btnRel.setMargins(100, 50, 100, 50);

            Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(200,
                    100));
            btn.setGravity(Gravity.CENTER);
            btn.setLayoutParams(btnRel);
            btn.setTextColor(Color.WHITE);
            btn.setBackgroundResource(R.drawable.button_blueg);

            btn.setText("Open Setting");
            btn.setAllCaps(false);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            relative.addView(btn);


            rootView.addView(relative);
        }

        super.onTaskRemoved(rootIntent);
    }

}
