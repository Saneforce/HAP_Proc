package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

public class MainActivity extends AppCompatActivity      {
    private static int SPLASH_SCREEN = 3000;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent tmrIntent = new Intent(MainActivity.this, TimerService.class);
                //bindService(tmrIntent, mServiceConection, Context.BIND_AUTO_CREATE);
                startService(tmrIntent);

                SharedPreferences sharedpreferences;

                sharedpreferences = getSharedPreferences(mypreference,
                        Context.MODE_PRIVATE);
                if(sharedpreferences.getString("nameKey","")=="") {
                    Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                finish();

            }
        },SPLASH_SCREEN);
    }


}