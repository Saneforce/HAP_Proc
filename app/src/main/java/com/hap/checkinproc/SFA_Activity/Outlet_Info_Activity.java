package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Outlet_Info_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Dashboard_View_Model;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
public class Outlet_Info_Activity extends AppCompatActivity {

    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView headtext, textViewname;
    List<Retailer_Modal_List> Retailer_Modal_List;
    Shared_Common_Pref shared_common_pref;
    Shared_Common_Pref sharedCommonPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet__info_);
        shared_common_pref = new Shared_Common_Pref(this);
        sharedCommonPref = new Shared_Common_Pref(Outlet_Info_Activity.this);
        recyclerView = findViewById(R.id.outletrecyclerview);
        headtext = findViewById(R.id.headtext);
        textViewname = findViewById(R.id.textViewname);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gson = new Gson();
        userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
        }.getType();
        String OrdersTable = sharedCommonPref.getvalue(Shared_Common_Pref.Outlet_List);
        Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
        recyclerView.setAdapter(new Outlet_Info_Adapter(Retailer_Modal_List, R.layout.outlet_info_recyclerview, getApplicationContext(), new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {
                Intent intent = new Intent(getApplicationContext(), AddNewRetailer.class);
                intent.putExtra("OutletCode", String.valueOf(Retailer_Modal_List.get(position).getId()));
                intent.putExtra("OutletName", Retailer_Modal_List.get(position).getName());
                intent.putExtra("OutletAddress", Retailer_Modal_List.get(position).getListedDrAddress1());
                intent.putExtra("OutletMobile", Retailer_Modal_List.get(position).getMobileNumber());
                intent.putExtra("OutletRoute", Retailer_Modal_List.get(position).getTownName());
                startActivity(intent);
            }
        }));
    }
}