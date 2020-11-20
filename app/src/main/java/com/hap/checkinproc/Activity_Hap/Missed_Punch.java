package com.hap.checkinproc.Activity_Hap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
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

public class Missed_Punch extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    EditText checkOutTime, checkIn, reasonMP;
    SearchableSpinner missedDate;
    EditText shiftType;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear;
    String DateNTime;
    Gson gson1;
    List<MissedPunch> leavetypelist;
    Type userType;
    String missedDates, missedShift, missedCHeckin, missedCheckOut;
    Button missedSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed__punch);

        gson1 = new Gson();


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


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        missedDate = (SearchableSpinner) findViewById(R.id.missed_date);

        shiftType = (EditText) findViewById(R.id.missed_shift);
        checkIn = (EditText) findViewById(R.id.missed_checkin);
        leaveTypeMethod();

        reasonMP = (EditText) findViewById(R.id.reason_missed);
        missedSubmit = (Button) findViewById(R.id.submit_missed);
        missedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missedPunchSubmit();
            }
        });
        Bundle params=getIntent().getExtras();
        missedDates = params.getString("name");
        missedShift = params.getString("name1");
        missedCHeckin = params.getString("CInTm");
        checkIn.setText(missedCHeckin);
        shiftType.setText(missedShift);
    }


    private void DistributorTypeAdapter() {

        ArrayList<String> worktype = new ArrayList<>();
        for (int i = 0; i < leavetypelist.size(); i++) {
            worktype.add(leavetypelist.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Missed_Punch.this, R.layout.spinner_search_item, R.id.text_item, worktype);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        missedDate.setAdapter(arrayAdapter);

        missedDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                missedDates = leavetypelist.get(position).getName();
                missedShift = leavetypelist.get(position).getName1();
                missedCHeckin = leavetypelist.get(position).getCheckinTime();
                checkIn.setText(leavetypelist.get(position).getCheckinTime());
                shiftType.setText(leavetypelist.get(position).getName1());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                userType = new TypeToken<ArrayList<MissedPunch>>() {
                }.getType();
                Log.e("LeaveTypeList", String.valueOf(userType));

                leavetypelist = gson1.fromJson(new Gson().toJson(response.body()), userType);
                Log.e("LeaveTypeList", String.valueOf(leavetypelist));
                DistributorTypeAdapter();
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



    /*Submit Missed punch*/

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
                startActivity(new Intent(Missed_Punch.this, Dashboard.class));
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


}
