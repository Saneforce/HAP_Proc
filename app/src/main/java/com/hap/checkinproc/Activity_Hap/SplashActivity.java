package com.hap.checkinproc.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 3000;
    public static final String mypreference = "mypref";
    Shared_Common_Pref shared_common_pref;
    //ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_main);
            shared_common_pref = new Shared_Common_Pref(this);
           // ivLogo = findViewById(R.id.ivLogo);


//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    SharedPreferences sharedpreferences;
//                    sharedpreferences = getSharedPreferences(mypreference,
//                            Context.MODE_PRIVATE);
//                    if (sharedpreferences.getString("nameKey", "") == "") {
//                        Intent intent = new Intent(SplashActivity.this, PrivacyPolicy.class);
//                        startActivity(intent);
//                    } else {
//
//
//                        Intent intent = new Intent(SplashActivity.this, Login.class);
//                        startActivity(intent);
//                    }
//                    finish();
//
//                }
//            }, SPLASH_SCREEN);
        } catch (Exception e) {
            Log.v("MAINACTIVITY:", e.getMessage());
        }
    }


}