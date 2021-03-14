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
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
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
    String Scode;
    String Dcode;
    String Rf_code;
    List<Dashboard_View_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView headtext, textViewname;
    List<Retailer_Modal_List> Retailer_Modal_List;
    @Inject
    Retrofit retrofit;
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet__info_);
        shared_common_pref = new Shared_Common_Pref(this);
        ((HAPApp) getApplication()).getNetComponent().inject(this);
        recyclerView = findViewById(R.id.outletrecyclerview);
        headtext = findViewById(R.id.headtext);
        textViewname = findViewById(R.id.textViewname);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        GetAllDetails();
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void GetAllDetails() {
        ApiInterface service = retrofit.create(ApiInterface.class);
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

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


}