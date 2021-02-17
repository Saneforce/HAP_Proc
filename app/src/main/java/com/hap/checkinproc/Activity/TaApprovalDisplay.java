package com.hap.checkinproc.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.Dashboard;
import com.hap.checkinproc.Activity_Hap.Dashboard_Two;
import com.hap.checkinproc.Activity_Hap.ERT;
import com.hap.checkinproc.Activity_Hap.Help_Activity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;

public class TaApprovalDisplay extends AppCompatActivity {

    TextView txtDate, txtName, txtTotalAmt, txtHQ, txtTrvlMode, txtDesig, txtDept, txtDA, txtTL, txtLA, txtLC, txtOE, txtfA, txtReject;
    Common_Class common_class;
    Shared_Common_Pref mShared_common_pref;
    String date = " ", SlStart = "", TotalAmt = "";
    LinearLayout linAccept, linReject;
    AppCompatEditText appCompatEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_approval_display);

        mShared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);

        date = String.valueOf(getIntent().getSerializableExtra("date"));
        Log.v("datedate", date);

        getTAList(date);

        txtDate = findViewById(R.id.txt_date);
        txtTotalAmt = findViewById(R.id.txt_amt);
        txtName = findViewById(R.id.txt_Name);
        txtHQ = findViewById(R.id.txt_hq);
        txtDesig = findViewById(R.id.txt_desg);
        txtDept = findViewById(R.id.txt_dep);
        txtTrvlMode = findViewById(R.id.txt_mde);
        linAccept = findViewById(R.id.lin_accp);
        linReject = findViewById(R.id.rejectonly);
        txtReject = findViewById(R.id.L_rejectsave);

        appCompatEditText = findViewById(R.id.reason);
        txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendtpApproval(2);
            }
        });

        txtDA = findViewById(R.id.txt_da);
        txtTL = findViewById(R.id.txt_tl);
        txtLA = findViewById(R.id.txt_la);
        txtLC = findViewById(R.id.txt_lc);
        txtOE = findViewById(R.id.txt_oe);
        txtfA = findViewById(R.id.txt_fa);

        txtDate.setText(String.valueOf(getIntent().getSerializableExtra("date")));

        txtName.setText(String.valueOf(getIntent().getSerializableExtra("name")));
        txtHQ.setText(String.valueOf(getIntent().getSerializableExtra("head_quaters")));
        txtDesig.setText(String.valueOf(getIntent().getSerializableExtra("desig")));
        txtDept.setText(String.valueOf(getIntent().getSerializableExtra("dept")));
        txtTrvlMode.setText(String.valueOf(getIntent().getSerializableExtra("travel_mode")));

        SlStart = String.valueOf(getIntent().getSerializableExtra("Sl_No"));


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
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });


    }

    public void DaApproval(View v) {
    }

    public void BoardingApproval(View v) {
    }

    public void DriverDaApproval(View v) {
    }

    public void FuelApproval(View v) {
    }


    public void getTAList(String Date) {
        Log.v("datedatefgdfgd", Date);
        JSONObject taReq = new JSONObject();
        try {
            taReq.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            taReq.put("divisionCode", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            taReq.put("Selectdate", Date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("JSON_VALUE", taReq.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> mTrave = apiInterface.getApprovalDisplay(taReq.toString());
        mTrave.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();

                for (int m = 0; m < jsonArray.size(); m++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(m);
                    Log.v("LIST", jsonObject.toString());

                    txtDA.setText("Rs." + jsonObject.get("Boarding_Amt").getAsString() + ".00");
                    txtTL.setText("Rs." + jsonObject.get("Ta_totalAmt").getAsString() + ".00");
                    txtLA.setText("Rs." + jsonObject.get("Ldg_totalAmt").getAsString() + ".00");
                    txtLC.setText("Rs." + jsonObject.get("Lc_totalAmt").getAsString() + ".00");
                    txtOE.setText("Rs." + jsonObject.get("Oe_totalAmt").getAsString() + ".00");
                    txtTotalAmt.setText("Rs." + jsonObject.get("Total_Amount").getAsString() + ".00");
                    TotalAmt = jsonObject.get("Total_Amount").getAsString();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    TaApprovalDisplay.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

    public void onApproval(View v) {
        SendtpApproval(1);
    }

    public void onReject(View v) {
        linAccept.setVisibility(View.GONE);
        linReject.setVisibility(View.VISIBLE);
    }


    private void SendtpApproval(int flag) {
        JSONObject taReq = new JSONObject();

        try {
            taReq.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            taReq.put("Flag", flag);
            taReq.put("Sl_No", SlStart);
            taReq.put("AAmount", TotalAmt);
            taReq.put("Reason", appCompatEditText.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TA_REQ", taReq.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.taApprove(taReq.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                try {
                    common_class.CommonIntentwithFinish(TAApprovalActivity.class);
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (flag == 1) {
                        Toast.makeText(getApplicationContext(), "TA  Approved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "TA Rejected  Successfully", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}