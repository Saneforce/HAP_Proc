package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Missed_Punch_Model;
import com.hap.checkinproc.Model_Class.Permission_Approval_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.Missed_Punch_Adapter;
import com.hap.checkinproc.adapters.Permission_Approval_Adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Missed_punch_Approval extends AppCompatActivity {
    String Scode;
    String Dcode;
    String Rf_code;

    List<Missed_Punch_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed_punch__approval);
        recyclerView = findViewById(R.id.Mpunchrecyclerview);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        common_class.ProgressdialogShow(1, "Missed Punch Approval");
        Rf_code = Scode;
        gson = new Gson();
        getmissedpunchdetails();

    }


    public void getmissedpunchdetails() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> mCall = apiInterface.GetTPObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "vwmissedpunch", routemaster);

        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                userType = new TypeToken<ArrayList<Missed_Punch_Model>>() {
                }.getType();
                approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);

                recyclerView.setAdapter(new Missed_Punch_Adapter(approvalList, R.layout.missed_punch_list_item, getApplicationContext()));
                common_class.ProgressdialogShow(2, "Missed Punch Approval");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Missed Punch Approval");
            }
        });

    }
}
