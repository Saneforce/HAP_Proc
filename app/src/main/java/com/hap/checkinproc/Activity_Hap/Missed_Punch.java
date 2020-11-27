package com.hap.checkinproc.Activity_Hap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.MissedPunch;
import com.hap.checkinproc.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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

public class Missed_Punch extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener, Master_Interface {

    String Tag = "HAP_Missed_Punch";
    EditText checkOutTime, checkIn, reasonMP;

    EditText shiftType;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear;
    String DateNTime;
    Gson gson1;
    List<MissedPunch> leavetypelist;
    Type userType;
    String missedDates, missedShift, missedCHeckin, missedCheckOut;
    Button missedSubmit;
    List<Common_Model> missed_punch = new ArrayList<>();
    Common_Model Model_Pojo;
    TextView misseddateselect;
    LinearLayout misseddatelayout;
    CustomListViewDialog customDialog;
    Button mButtonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed__punch);
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
        gson1 = new Gson();
        misseddatelayout = findViewById(R.id.misseddatelayout);
        misseddateselect = findViewById(R.id.misseddateselect);
        checkOutTime = (EditText) findViewById(R.id.missed_checkout);


        checkOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Missed_Punch.this, Missed_Punch.this, year, month, day);
                datePickerDialog.show();
            }
        });

        misseddatelayout.setOnClickListener(this);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        shiftType = (EditText) findViewById(R.id.missed_shift);
        checkIn = (EditText) findViewById(R.id.missed_checkin);
        leaveTypeMethod();

        reasonMP = (EditText) findViewById(R.id.reason_missed);

        Bundle params = getIntent().getExtras();

        if (!(params == null)) {
            missedDates = params.getString("EDt");
            missedShift = params.getString("Shift");
            missedCHeckin = params.getString("CInTm");
            missedCheckOut = params.getString("COutTm");

            checkIn.setText(missedCHeckin);
            shiftType.setText(missedShift);
            misseddateselect.setText(missedDates);
            checkOutTime.setText(missedCheckOut);
        }
        Log.d(Tag, String.valueOf(params));


        mButtonSubmit = (Button) findViewById(R.id.submit_missed);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!misseddateselect.getText().toString().matches("") && !reasonMP.getText().toString().matches("")) {
                    missedPunchSubmit();
                } else if (misseddateselect.getText().toString().matches("")) {
                    Toast.makeText(Missed_Punch.this, "Enter Shite time", Toast.LENGTH_SHORT).show();
                } else if (reasonMP.getText().toString().matches("")) {
                    Toast.makeText(Missed_Punch.this, "Enter Remarks", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void leaveTypeMethod() {

        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.missedPunch(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e("RESPONSE_LOG", response.body().toString());
                GetJsonData(new Gson().toJson(response.body()), "0");

                //DistributorTypeAdapter();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        Log.e("DATE_AND_TIME", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        DateNTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

        TimePickerDialog timePickerDialog = new TimePickerDialog(Missed_Punch.this, Missed_Punch.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();


    }

    @Override
    public void onTimeSet(TimePicker tp, int hour, int sMinute) {

        DateNTime = DateNTime + " " + hour + ":" + sMinute;
        Log.e("DATE_AND_TIME", DateNTime);
        Log.e("DATE_AND_TIME", hour + ":" + sMinute);
        checkOutTime.setText(DateNTime);
        missedCheckOut = DateNTime;
    }

    /*
        missedDates = leavetypelist.get(position).getName();
        missedShift = leavetypelist.get(position).getName1();
        missedCHeckin = leavetypelist.get(position).getCheckinTime();
                    checkIn.setText(leavetypelist.get(position).getCheckinTime());
                    shiftType.setText(leavetypelist.get(position).getName1());
    */
    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        //id="Shift";
        //name="date";
        //flag="checkintime";
        missedDates = myDataset.get(position).getName();
        missedShift = myDataset.get(position).getId();
        missedCHeckin = myDataset.get(position).getFlag();
        checkIn.setText(myDataset.get(position).getFlag());
        shiftType.setText(myDataset.get(position).getId());
        misseddateselect.setText(myDataset.get(position).getName());
        checkOutTime.setText(myDataset.get(position).getAddress());
    }

    /*Submit Missed punch*/
    private void GetJsonData(String jsonResponse, String type) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String date = jsonObject1.optString("name");
                String shift = jsonObject1.optString("name1");
                String Checkin_Time = jsonObject1.optString("Checkin_Time");
                String COutTime = jsonObject1.optString("COutTime");

                Model_Pojo = new Common_Model(shift, date, Checkin_Time, COutTime);
                missed_punch.add(Model_Pojo);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void missedPunchSubmit() {

        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("missed_date", missedDates);
            jsonleaveType.put("Shift_Name", missedShift);
            jsonleaveType.put("checkouttime", missedCheckOut);
            jsonleaveType.put("checkinTime", missedCHeckin);
            jsonleaveType.put("reason", reasonMP.getText().toString());
            jsonleaveTypeS.put("MissedPunchEntry", jsonleaveType);
            jsonArray1.put(jsonleaveTypeS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String leaveCap1 = jsonArray1.toString();
        System.out.println("Activity" + leaveCap1);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Name);
        Log.e("SF_Name", Shared_Common_Pref.Div_Code);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Code);
        Log.e("SF_Name", Shared_Common_Pref.StateCode);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.SubmitmissedPunch(Shared_Common_Pref.Sf_Name, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "MGR", leaveCap1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObjecta = response.body();
                Log.e("TOTAL_REPOSNEaaa", String.valueOf(jsonObjecta));
                String Msg = jsonObjecta.get("Msg").getAsString();
                if(!Msg.equalsIgnoreCase("")){
                    AlertDialogBox.showDialog(Missed_Punch.this, "HAP Check-In", Msg, "OK", "", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            if(jsonObjecta.get("success").getAsBoolean()==true)
                                startActivity(new Intent(Missed_Punch.this, Leave_Dashboard.class));//openHome();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });
                }
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
                    Missed_Punch.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.misseddatelayout:
                customDialog = new CustomListViewDialog(Missed_Punch.this, missed_punch, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;

        }
    }
}