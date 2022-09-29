package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InshopCheckoutActivity extends AppCompatActivity {

    TextView checkoutTunTime, checkinTime, retailerName,tvDate;
    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_checkout);

        checkoutTunTime = findViewById(R.id.tvCheckoutRunTime);
        checkinTime = findViewById(R.id.tvCheckinTime);
        retailerName = findViewById(R.id.ischeckoutRetName);
        tvDate = findViewById(R.id.iscoutDate);

        retailerName.setText(InshopCheckinActivity.getName());
        checkinTime.setText(InshopCheckinActivity.getDate());


        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateToStr = format.format(today);
        tvDate.setText(dateToStr);

//        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("retailername", Context.MODE_PRIVATE);
//
//        String name = sharedPreferences.getString("name","");
//        retailerName.setText(name);



        handler.postDelayed(new Runnable() {
            public void run() {
                checkoutTunTime.setText(Common_Class.GetRunTime());
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }
}