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
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.Approvals;
import com.hap.checkinproc.Activity_Hap.Dashboard;
import com.hap.checkinproc.Activity_Hap.Dashboard_Two;
import com.hap.checkinproc.Activity_Hap.ERT;
import com.hap.checkinproc.Activity_Hap.Help_Activity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Adapter.Travel_Approval_Adapter;
import com.hap.checkinproc.common.TimerService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;

public class TAApprovalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref mShared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_approval);
        startService(new Intent(this, TimerService.class));
        mShared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        getTAList();
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
        recyclerView = findViewById(R.id.leaverecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Approvals.class);

            }
        });
    }

    public void getTAList() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> mTrave = apiInterface.getApprovalList(mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
        mTrave.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();

                Log.v("APPROVAL_LIST", jsonArray.toString());
                recyclerView.setAdapter(new Travel_Approval_Adapter(jsonArray, R.layout.leave_approval_layout, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int Name) {

                            JsonObject jsonObject = (JsonObject) jsonArray.get(Name);
                            Log.v("LIST", jsonObject.toString());
                            Intent intent = new Intent(TAApprovalActivity.this, TaApprovalDisplay.class);
                            intent.putExtra("date", jsonObject.get("id").getAsString());
                            intent.putExtra("name", jsonObject.get("Sf_Name").getAsString());
                            intent.putExtra("total_amount", jsonObject.get("Total_Amount").getAsString());
                            intent.putExtra("head_quaters", jsonObject.get("HQ").getAsString());
                            intent.putExtra("travel_mode", jsonObject.get("MOT_Name").getAsString());
                            intent.putExtra("desig", jsonObject.get("sf_Designation_Short_Name").getAsString());
                            intent.putExtra("dept", jsonObject.get("DeptName").getAsString());
                            intent.putExtra("Sl_No", jsonObject.get("Sl_NoStart").getAsString());
                            intent.putExtra("sfCode", jsonObject.get("Sf_code").getAsString());
                            intent.putExtra("SF_Mobile", jsonObject.get("SF_Mobile").getAsString());
                            intent.putExtra("sf_emp_id", jsonObject.get("sf_emp_id").getAsString());
                            startActivity(intent);

                            Log.e("sfCode", jsonObject.get("Sf_code").getAsString());
                            Log.e("Sl_No", jsonObject.get("Sl_NoStart").getAsString());
                            Log.e("total_amount", jsonObject.get("Total_Amount").getAsString());
                            Log.e("total_amount","fgd");
                        }

                }));
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
                    TAApprovalActivity.super.onBackPressed();
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