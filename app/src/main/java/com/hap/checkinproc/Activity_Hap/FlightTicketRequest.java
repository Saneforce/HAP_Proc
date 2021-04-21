package com.hap.checkinproc.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

import java.util.Calendar;

import static com.hap.checkinproc.Activity_Hap.Login.CheckInDetail;

public class FlightTicketRequest extends AppCompatActivity {

    RadioGroup radioGrp;
    RadioButton radioOne, radioRound;
    LinearLayout LinearReturn, LinearHome;
    EditText edtDepature, edtReturn;
    TextView Name;
    String tominYear = "", tominMonth = "", tominDay = "", maxTWoDate = "", SFName = "";
    SharedPreferences CheckInDetails, UserDetails, sharedpreferences;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_ticket_request);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        sharedpreferences = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        radioGrp = findViewById(R.id.radio_ticket);
        radioOne = findViewById(R.id.radio_oneway);
        radioRound = findViewById(R.id.radio_twoway);
        LinearReturn = findViewById(R.id.lin_return);
        LinearHome = findViewById(R.id.lin_name);
        edtDepature = findViewById(R.id.edt_dep);
        edtReturn = findViewById(R.id.edt_return);
        Name = findViewById(R.id.txt_name);
        radioGrp.clearCheck();
        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                Name.setText(Shared_Common_Pref.Sf_Name);
                if (null != rb && checkedId > -1) {
                    LinearHome.setVisibility(View.VISIBLE);
                    if (rb.getText().toString().equalsIgnoreCase("One way")) {
                        LinearReturn.setVisibility(View.INVISIBLE);
                    } else {
                        LinearReturn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


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
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
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
            Intent Dashboard = new Intent(FlightTicketRequest.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }


    public void DepDtePker(View v) {
        hideKeyBoard();
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(FlightTicketRequest.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edtDepature.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        maxTWoDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        MaxMinDateTo(maxTWoDate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void RetunDtePker(View v) {
        hideKeyBoard();
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        if (!maxTWoDate.equalsIgnoreCase("")) {
            // date picker dialog
            DatePickerDialog picker = new DatePickerDialog(FlightTicketRequest.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            edtReturn.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, year, month, day);
            Calendar calendarmin = Calendar.getInstance();

            calendarmin.set(Integer.parseInt(tominYear), Integer.parseInt(tominMonth) - 1, Integer.parseInt(tominDay));
            picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
            picker.show();
        } else {
            Toast.makeText(this, "Please choose depature date", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void MaxMinDateTo(String strMinDate) {
        String[] separated1 = strMinDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();
        tominYear = separated1[0];
        tominMonth = separated1[1];
        tominDay = separated1[2];
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    FlightTicketRequest.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onPause() {
        super.onPause();

        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(new Intent(this, TimerService.class));
    }

}
