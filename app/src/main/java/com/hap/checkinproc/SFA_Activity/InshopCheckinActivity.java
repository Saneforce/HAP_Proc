package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.R;

import java.util.Calendar;
import java.util.Date;

public class InshopCheckinActivity extends AppCompatActivity {

    TextView checkinRunTime, checkedinTime, search, retailerName;
    Button checkin;
    CardView searchLay;
    LinearLayout checkinLay, checkedinLay;
    final Handler handler = new Handler();
    String n="", m="";

    private static String name;
    public static String getValue() {
        return name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_checkin);

        checkinRunTime = findViewById(R.id.tvCheckInRunTime);
        checkin = findViewById(R.id.btnInshopCheckin);
        checkinLay = findViewById(R.id.inshopCheckinLay);
        checkedinLay = findViewById(R.id.inshopCheckedInLay);
        checkedinTime = findViewById(R.id.tvCheckedinTime);
        search = findViewById(R.id.tvInshopSearchRet);
        searchLay = findViewById(R.id.isSearchView);
        retailerName = findViewById(R.id.inshopRetName);

//        Bundle bundle = getIntent().getExtras();
//        String value = bundle.getString("idData");
        if (getIntent().hasExtra("idData")) {

            n=getIntent().getStringExtra("idData");
            m=getIntent().getStringExtra("idData");
        }



        search.setText(n);
 name = retailerName.getText().toString().trim();


        handler.postDelayed(new Runnable() {
            public void run() {
                checkinRunTime.setText(Common_Class.GetRunTime());
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkedinTime.setText(Common_Class.GetRunTime());
                checkedinLay.setVisibility(View.VISIBLE);
                checkinLay.setVisibility(View.GONE);

                retailerName.setText(m);

            }
        });

        searchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InshopCheckinActivity.this, InshopRetailerActivity.class));

            }
        });
    }

}