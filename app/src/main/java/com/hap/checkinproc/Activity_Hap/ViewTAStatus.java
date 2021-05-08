package com.hap.checkinproc.Activity_Hap;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.ViewTAStatusAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;

public class ViewTAStatus extends AppCompatActivity {

    RecyclerView mTaStatusList;
    ViewTAStatusAdapter viewTAStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_t_a_status);
        mTaStatusList = findViewById(R.id.ta_list);
        mTaStatusList.setLayoutManager(new LinearLayoutManager(this));


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

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TaListResponse();
    }

    @Override
    public void onBackPressed() {
    }

    public void TaListResponse() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> mTAStatus = apiInterface.getTaViewStatus(Shared_Common_Pref.Sf_Code);

        Log.v("TA_STATUS_REQUEST", mTAStatus.request().toString());
        mTAStatus.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonTa = response.body();
                Log.v("JSON_VIEW_Array", jsonTa.toString());

                viewTAStatusAdapter = new ViewTAStatusAdapter(ViewTAStatus.this, jsonTa);
                mTaStatusList.setAdapter(viewTAStatusAdapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }
}