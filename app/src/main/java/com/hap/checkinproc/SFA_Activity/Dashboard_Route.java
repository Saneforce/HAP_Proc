package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Tp_View_Master;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Dashboard_View_Adapter;
import com.hap.checkinproc.SFA_Adapter.Route_View_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Dashboard_View_Model;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.adapters.Leave_Approval_Adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard_Route extends AppCompatActivity implements View.OnClickListener {

    List<Retailer_Modal_List> Retailer_Modal_List;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView headtext, textViewname, Alltextclick, Completeclick, Pendingclick;
    View Alltextview, completeview, pendingview;

    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__route);
        recyclerView = findViewById(R.id.leaverecyclerview);
        shared_common_pref = new Shared_Common_Pref(this);
        headtext = findViewById(R.id.headtext);
        textViewname = findViewById(R.id.textViewname);
        Alltextclick = findViewById(R.id.Alltextclick);
        Completeclick = findViewById(R.id.Completeclick);
        Pendingclick = findViewById(R.id.Pendingclick);
        Alltextview = findViewById(R.id.Alltextview);
        completeview = findViewById(R.id.completeview);
        pendingview = findViewById(R.id.pendingview);
        Alltextview.setVisibility(View.VISIBLE);
        completeview.setVisibility(View.INVISIBLE);
        pendingview.setVisibility(View.INVISIBLE);

        Alltextclick.setOnClickListener(this);
        Completeclick.setOnClickListener(this);
        Pendingclick.setOnClickListener(this);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        GetAllDetails();

    }

    public void GetAllDetails() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        String commonworktype = "{\"tableName\":\"vwDoctor_Master_APP\",\"coloumns\":\"[\\\"doctor_code as id\\\", \\\"doctor_name as name\\\",\\\"town_code\\\",\\\"town_name\\\",\\\"lat\\\",\\\"long\\\",\\\"addrs\\\",\\\"ListedDr_Address1\\\",\\\"ListedDr_Sl_No\\\",\\\"Mobile_Number\\\",\\\"Doc_cat_code\\\",\\\"ContactPersion\\\",\\\"Doc_Special_Code\\\"]\",\"where\":\"[\\\"isnull(Doctor_Active_flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        Call<Object> call = service.GetRouteObject(QueryString, commonworktype);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("MAsterSyncView_Result", response.body() + "");
                Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
                }.getType();
                Retailer_Modal_List = gson.fromJson(new Gson().toJson(response.body()), userType);
                recyclerView.setAdapter(new Route_View_Adapter(Retailer_Modal_List, R.layout.route_dashboard_recyclerview, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int position) {
                        Shared_Common_Pref.OutletName = Retailer_Modal_List.get(position).getName().toUpperCase() + "~" + Retailer_Modal_List.get(position).getId();
                        Shared_Common_Pref.OutletCode = Retailer_Modal_List.get(position).getId();
                        common_class.CommonIntentwithFinish(Route_Product_Info.class);

                    }
                }));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Alltextclick:
                Alltextview.setVisibility(View.VISIBLE);
                completeview.setVisibility(View.INVISIBLE);
                pendingview.setVisibility(View.INVISIBLE);
                break;
            case R.id.Completeclick:
                Alltextview.setVisibility(View.INVISIBLE);
                completeview.setVisibility(View.VISIBLE);
                pendingview.setVisibility(View.INVISIBLE);
                break;
            case R.id.Pendingclick:
                Alltextview.setVisibility(View.INVISIBLE);
                completeview.setVisibility(View.INVISIBLE);
                pendingview.setVisibility(View.VISIBLE);
                break;


        }
    }
}