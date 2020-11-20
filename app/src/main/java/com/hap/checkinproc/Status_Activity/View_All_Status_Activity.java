package com.hap.checkinproc.Status_Activity;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.Approvals;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Adapter.Permission_Status_Adapter;
import com.hap.checkinproc.Status_Adapter.ViewAll_Status_Adapter;
import com.hap.checkinproc.Status_Adapter.WeekOff_Status_Adapter;
import com.hap.checkinproc.Status_Model_Class.Permission_Status_Model;
import com.hap.checkinproc.Status_Model_Class.View_All_Model;
import com.hap.checkinproc.Status_Model_Class.WeekOff_Status_Model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View_All_Status_Activity extends AppCompatActivity {
    List<View_All_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    Intent i;
    String AMOD = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__all__status_);
        recyclerView = findViewById(R.id.viewallstatus);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gson = new Gson();
        getWeekOffStatus();
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }

    private void getWeekOffStatus() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        common_class.ProgressdialogShow(1, "Status");
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "get/AttnStatus");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("Status", "");
        QueryString.put("Priod","-1" );
        String commonworktype = "[]";

        Call<Object> mCall = apiInterface.Getwe_Status("-1", Shared_Common_Pref.Sf_Code,"get/AttnStatus","","[]");
        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                common_class.ProgressdialogShow(2, "Onduty Status");
                userType = new TypeToken<ArrayList<View_All_Model>>() {
                }.getType();
                approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);
                recyclerView.setAdapter(new ViewAll_Status_Adapter(approvalList, R.layout.view_all_status_listitem, getApplicationContext(),AMOD));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("ONfailureSTATUS",QueryString.toString());
                common_class.ProgressdialogShow(2, "Permission Status");
            }
        });


    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    View_All_Status_Activity.super.onBackPressed();
                }
            });


    @Override
    public void onBackPressed() {

    }}