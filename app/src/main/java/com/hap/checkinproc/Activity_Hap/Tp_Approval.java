package com.hap.checkinproc.Activity_Hap;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;

import com.hap.checkinproc.Model_Class.Tp_Approval_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.Tp_Approval_Adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Tp_Approval extends AppCompatActivity {
    Gson gson;
    Type userType;
    List<Tp_Approval_Model> Tp_Approval_Model;
    private RecyclerView recyclerView;
    TextView title;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp__approval);


        recyclerView = findViewById(R.id.tprecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        gettp_Details();

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

    }

    public void gettp_Details() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Object> mCall = apiInterface.GetTPObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "vwtourplan", routemaster);

        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                userType = new TypeToken<ArrayList<Tp_Approval_Model>>() {
                }.getType();
                Tp_Approval_Model = gson.fromJson(new Gson().toJson(response.body()), userType);

                recyclerView.setAdapter(new Tp_Approval_Adapter(Tp_Approval_Model, R.layout.tpapproval_layout, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(Integer Name) {
                        Intent intent = new Intent(Tp_Approval.this, Tp_Approval_Reject.class);
                        intent.putExtra("Username", Tp_Approval_Model.get(Name).getFieldForceName());
                        intent.putExtra("Emp_Code", Tp_Approval_Model.get(Name).getEmpCode());
                        intent.putExtra("HQ", Tp_Approval_Model.get(Name).getHQ());
                        intent.putExtra("Designation", Tp_Approval_Model.get(Name).getDesignation());
                        intent.putExtra("MobileNumber", Tp_Approval_Model.get(Name).getSFMobile());
                        intent.putExtra("Plan_Date", Tp_Approval_Model.get(Name).getStartDate());
                        intent.putExtra("Work_Type", Tp_Approval_Model.get(Name).getWorktypeName());
                        intent.putExtra("Route", Tp_Approval_Model.get(Name).getRouteName());
                        intent.putExtra("Distributor", Tp_Approval_Model.get(Name).getWorkedWithName());
                        intent.putExtra("Sf_Code", Tp_Approval_Model.get(Name).getSFCode());
                        intent.putExtra("Remarks", Tp_Approval_Model.get(Name).getRemarks());
                        startActivity(intent);
                    }
                }));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Tp_Approval.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}
