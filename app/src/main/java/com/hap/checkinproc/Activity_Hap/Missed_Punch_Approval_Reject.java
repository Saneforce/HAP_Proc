package com.hap.checkinproc.Activity_Hap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Missed_Punch_Approval_Reject extends AppCompatActivity implements View.OnClickListener {
    TextView name, empcode, hq, mobilenumber, designation, MPreject, misseddate, applieddate, shiftonduty, MPreason, checkintime, checkouttime, MPapprovebutton, MP_rejectsave;
    String Sf_Code, Sl_No;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    LinearLayout Approvereject, rejectonly;
    EditText reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed__punch__approval__reject);
        name = findViewById(R.id.name);
        MPapprovebutton = findViewById(R.id.MPapprovebutton);
        empcode = findViewById(R.id.empcode);
        reason = findViewById(R.id.reason);
        hq = findViewById(R.id.hq);
        designation = findViewById(R.id.designation);
        mobilenumber = findViewById(R.id.mobilenumber);
        Approvereject = findViewById(R.id.Approvereject);
        rejectonly = findViewById(R.id.rejectonly);
        MP_rejectsave = findViewById(R.id.mp_rejectsave);

        MPreason = findViewById(R.id.MPreason);
        MPreject = findViewById(R.id.MPreject);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        MPreject = findViewById(R.id.MPreject);
        applieddate = findViewById(R.id.applieddate);
        misseddate = findViewById(R.id.misseddate);
        shiftonduty = findViewById(R.id.shiftonduty);
        checkintime = findViewById(R.id.checkintime);
        checkouttime = findViewById(R.id.checkouttime);
        MPapprovebutton.setOnClickListener(this);
        MPreject.setOnClickListener(this);
        MP_rejectsave.setOnClickListener(this);
        Intent i = getIntent();
        name.setText("" + i.getExtras().getString("Username"));
        empcode.setText(":" + i.getExtras().getString("Emp_Code"));
        hq.setText("" + i.getExtras().getString("HQ"));
        designation.setText("" + i.getExtras().getString("Designation"));
        mobilenumber.setText("" + i.getExtras().getString("MobileNumber"));
        MPreason.setText("" + i.getExtras().getString("Reason"));
        applieddate.setText("" + i.getExtras().getString("AppliedDate"));
        misseddate.setText("" + i.getExtras().getString("MissedPunchDate"));
        shiftonduty.setText("" + i.getExtras().getString("Shiftonduty"));
        checkintime.setText("" + i.getExtras().getString("CheckinTime"));
        checkouttime.setText("" + i.getExtras().getString("CheckinTime"));
        Sf_Code = i.getExtras().getString("Sf_Code");
        Sl_No = i.getExtras().getString("Sl_No");

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }


    private void SendtpApproval(String Name, int flag) {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "dcr/save");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.Div_Code);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("Missed_Id", Sl_No);
        QueryString.put("Sf_Code", Sf_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            sp.put("Sf_Code", Sf_Code);
            if (flag == 2) {
                common_class.ProgressdialogShow(1, "Rejection for Missed Punch");
                sp.put("reason", common_class.addquote(reason.getText().toString()));
            } else {
                common_class.ProgressdialogShow(1, "Approval for Missed Punch");
            }
            jsonObject.put(Name, sp);

            Log.e("JSON_OBJECT", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        Log.e("Log_TpQuerySTring", QueryString.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                try {
                    common_class.CommonIntentwithFinish(Missed_punch_Approval.class);
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (flag == 1) {
                        common_class.ProgressdialogShow(2, "");
                        Toast.makeText(getApplicationContext(), "Missed Punch  Approved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        common_class.ProgressdialogShow(2, "");
                        Toast.makeText(getApplicationContext(), "Missed Punch Rejected  Successfully", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common_class.ProgressdialogShow(2, "");
            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.MPapprovebutton:
                SendtpApproval("MissedApproval", 1);
                break;

            case R.id.MPreject:
                rejectonly.setVisibility(View.VISIBLE);
                Approvereject.setVisibility(View.INVISIBLE);
                break;
            case R.id.mp_rejectsave:
                if (reason.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter The Reason", Toast.LENGTH_SHORT).show();
                } else {
                    SendtpApproval("MissedApprovalR", 2);
                }
                break;
        }
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Missed_Punch_Approval_Reject.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}


