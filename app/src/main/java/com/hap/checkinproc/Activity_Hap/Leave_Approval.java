package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.Model_Class.Approval;
import com.hap.checkinproc.Model_Class.Leave_Approval_Model;
import com.hap.checkinproc.Model_Class.Tp_Approval_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.Leave_Approval_Adapter;
import com.hap.checkinproc.adapters.Tp_Approval_Adapter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class Leave_Approval extends AppCompatActivity {
    String Scode;
    String Dcode;
    String Rf_code;
    List<Leave_Approval_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave__approval);
        recyclerView = findViewById(R.id.leaverecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        getleavedetails();
    }

    public void getleavedetails() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Object> mCall = apiInterface.GetTPObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "vwLeave", routemaster);

        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                userType = new TypeToken<ArrayList<Leave_Approval_Model>>() {
                }.getType();
                approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);

                recyclerView.setAdapter(new Leave_Approval_Adapter(approvalList, R.layout.leave_approval_layout, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }
}