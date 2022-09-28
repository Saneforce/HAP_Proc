package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;

public class InshopCheckoutActivity extends AppCompatActivity {

    TextView checkoutTunTime, checkinTime, retailerName;
    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_checkout);

        checkoutTunTime = findViewById(R.id.tvCheckoutRunTime);
        checkinTime = findViewById(R.id.tvCheckinTime);
        retailerName = findViewById(R.id.ischeckoutRetName);

        checkinTime.setText(InshopCheckinActivity.getValue());

        /*SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("value","");
        checkinTime.setText(value);*/

        retailerName.setText(InshopCheckinActivity.getValue());

        handler.postDelayed(new Runnable() {
            public void run() {
                checkoutTunTime.setText(Common_Class.GetRunTime());
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }
}