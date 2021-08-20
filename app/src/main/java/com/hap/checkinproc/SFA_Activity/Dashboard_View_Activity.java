package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.Approvals;
import com.hap.checkinproc.Activity_Hap.Leave_Approval;
import com.hap.checkinproc.Activity_Hap.Leave_Approval_Reject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Dashboard_View_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Dashboard_View_Model;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard_View_Activity extends AppCompatActivity {
    String Scode;
    String Dcode;
    String Rf_code;
    List<Dashboard_View_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView headtext, textViewname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__view_);

        recyclerView = findViewById(R.id.leaverecyclerview);
        headtext = findViewById(R.id.headtext);
        textViewname = findViewById(R.id.textViewname);
        common_class = new Common_Class(this);
        headtext.setText(common_class.getintentValues("Name"));
        textViewname.setText(common_class.getintentValues("Name"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();

        GetAllDetails(common_class.getintentValues("Servername"));

    }

    public void GetAllDetails(String name) {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> mCall = apiInterface.GetTPObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, name, routemaster);
        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                userType = new TypeToken<ArrayList<Dashboard_View_Model>>() {
                }.getType();
                approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);
                recyclerView.setAdapter(new Dashboard_View_Adapter(approvalList, R.layout.dashboard_view_recyclerview, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int Name) {

                    }
                }));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }
}