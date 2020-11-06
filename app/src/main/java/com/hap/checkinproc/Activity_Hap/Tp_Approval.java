package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;

import com.hap.checkinproc.Model_Class.Tp_Approval_Model;
import com.hap.checkinproc.Model_Class.Tp_View_Master;
import com.hap.checkinproc.Model_Class.Work_Type_Model;
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

                recyclerView.setAdapter(new Tp_Approval_Adapter(Tp_Approval_Model, R.layout.tpapproval_layout, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }
}