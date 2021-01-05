package com.hap.checkinproc.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DeviationEntry extends AppCompatActivity {

    TextView DeviationTypeEntry;
    EditText chooseDate, remarks;
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    DatePickerDialog picker1;
    String maxDate, minDate;
    String maxYear, maxMonth, maxDay, minYear, minMonth, minDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviation_entry);

        getToolbar();

        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

        DeviationTypeEntry = findViewById(R.id.deviation_type);
        chooseDate = (EditText) findViewById(R.id.choose_date);
        remarks = findViewById(R.id.remarks);


        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog.


                picker1 = new DatePickerDialog(DeviationEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                chooseDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                calendarmin.set(Integer.parseInt(minYear), Integer.parseInt(minMonth) - 1, Integer.parseInt(minDay));
                picker1.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                picker1.show();

            }

        });
    }


    public void getToolbar() {
        MaxMinDate();
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });

        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });


    }


    public void openHome() {
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");

        if (CheckIn == true) {
            Intent Dashboard = new Intent(DeviationEntry.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }


    public void MaxMinDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println("Current_DATE_FORMAT" + formatter.format(date));

        String strMinDate = formatter.format(date);
        minDate = strMinDate;
        /*Min Date*/
        String[] separated1 = minDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();

        minYear = separated1[0];
        minMonth = separated1[1];
        minDay = separated1[2];
        Log.e("Sresdfsd", minYear);
        Log.e("Sresdfsd", minMonth);
        Log.e("Sresdfsd", minDay);

    }

}