package com.hap.checkinproc.Activity_Hap;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.MaxMinDate;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Weekly_Off extends AppCompatActivity {

    DatePickerDialog picker;
    EditText eText, remark;
    Gson gson;
    Button weeklySubmit;
    List<MaxMinDate> maxMinDates;
    String maxDate, minDate;
    String maxYear, maxMonth, maxDay, minYear, minMonth, minDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly__off);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));

            }
        });
        eText = (EditText) findViewById(R.id.week_off_date);
        remark = (EditText) findViewById(R.id.remarks_week);
        weeklySubmit = (Button) findViewById(R.id.weeky_submit);
        gson = new Gson();

        Bundle params=getIntent().getExtras();
        if(params!=null){
            String[] stDt=params.getString("EDt").split("/");
            eText.setText(stDt[2]+"-"+stDt[1]+"-"+stDt[0]);
            eText.setEnabled(false);
        }

        MaxMinDate();
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Weekly_Off.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                calendarmin.set(Integer.parseInt(minYear), Integer.parseInt(minMonth) - 1, Integer.parseInt(minDay));
                Calendar calendarmax = Calendar.getInstance();
                calendarmax.set(Integer.parseInt(maxYear), Integer.parseInt(maxMonth) - 1, Integer.parseInt(maxDay));
                picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                picker.getDatePicker().setMaxDate(calendarmax.getTimeInMillis());

                picker.show();
            }
        });

        weeklySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eText.getText().toString().matches("") && !remark.getText().toString().matches("")) {
                    weekOff();
                } else if (eText.getText().toString().matches("")) {
                    Toast.makeText(Weekly_Off.this, "Enter Date", Toast.LENGTH_SHORT).show();
                } else if (remark.getText().toString().matches("")) {
                    Toast.makeText(Weekly_Off.this, "Enter Remarks  ", Toast.LENGTH_SHORT).show();
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

    }


    /*getMax and Min date*/
    public void MaxMinDate() {

        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.mmDate("wk", Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                Type userType1 = new TypeToken<ArrayList<MaxMinDate>>() {
                }.getType();
                maxMinDates = gson.fromJson(new Gson().toJson(response.body()), userType1);
                Log.e("REMAINING_LEAVES", String.valueOf(maxMinDates));

                for (int i = 0; i < maxMinDates.size(); i++) {
                    maxDate = maxMinDates.get(i).getMaxDate();
                    minDate = maxMinDates.get(i).getMinDate();
                    Log.e("MaxName", maxMinDates.get(i).getMaxDate());
                    Log.e("MaxName", maxMinDates.get(i).getMinDate());
                }

                /*MAx Date*/
                String[] separated = maxDate.split("-");
                separated[0] = separated[0].trim();
                separated[1] = separated[1].trim();
                separated[2] = separated[2].trim();

                maxYear = separated[0];
                maxMonth = separated[1];
                maxDay = separated[2];
                Log.e("Sresdfsd", maxYear);
                Log.e("Sresdfsd", maxMonth);
                Log.e("Sresdfsd", maxDay);

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

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });

    }


    /*Submit WeekOff*/
    public void weekOff() {


        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("WKDate", eText.getText().toString());
            jsonleaveType.put("reason", remark.getText().toString());

            jsonleaveTypeS.put("WeekofPunch", jsonleaveType);
            jsonArray1.put(jsonleaveTypeS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String leaveCap = "[{\"LeaveFormValidate\":{\"Leave_Type\":\"'9'\",\"From_Date\":\"2020-11-02\",\"To_Date\":\"'2020-11-05'\",\"Shift\":\"0\",\"PChk\":0}}]";
        String leaveCap = jsonArray1.toString();
        System.out.println("Activity_Event_Captures" + leaveCap);


        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.mmDates("wk", Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, leaveCap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                JsonObject jsonObjecta = response.body();

                Log.e("TOTAL_REPOSNE", String.valueOf(jsonObjecta));
                String Msg = jsonObjecta.get("msg").getAsString();

                AlertDialogBox.showDialog(Weekly_Off.this, "HAP Check-In", Msg, "OK", "", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivity(new Intent(Weekly_Off.this, Leave_Dashboard.class));
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {

                    }
                });

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Weekly_Off.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

}