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

public class Nearby_Outlets extends AppCompatActivity implements View.OnClickListener {
    String Scode;
    String Dcode;
    String Rf_code;
    List<Dashboard_View_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView Createoutlet, latitude, longitude, availableoutlets;
    List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> Retailer_Modal_List;
    List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> ShowRetailer_Modal_List;
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby__outlets);
        shared_common_pref = new Shared_Common_Pref(this);

        recyclerView = findViewById(R.id.outletrecyclerview);
        Createoutlet = findViewById(R.id.Createoutlet);
        availableoutlets = findViewById(R.id.availableoutlets);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        latitude.setText("Latitude : " + Shared_Common_Pref.Outletlat);
        longitude.setText("Latitude : " + Shared_Common_Pref.Outletlong);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        //GetAllDetails();

        gson = new Gson();
        userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
        }.getType();
        String OrdersTable = shared_common_pref.getvalue(Shared_Common_Pref.Outlet_List);
        Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
        System.out.println("DISTANCE_CHECKING_Lat" + "---" + Shared_Common_Pref.Outletlat + "----->");
        System.out.println("DISTANCE_CHECKING_Long" + "---" + Shared_Common_Pref.Outletlong + "----->");
        ShowRetailer_Modal_List = new ArrayList<>();
        ShowRetailer_Modal_List.clear();
        for (Retailer_Modal_List rml : Retailer_Modal_List) {
            if (rml.getLat() != null && rml.getLat() != "") {
                System.out.println("DISTANCE_CHECKING" + "---" + rml.getId() + "----->" + String.valueOf(Common_Class.Check_Distance(Common_Class.ParseDouble(rml.getLat()), Common_Class.ParseDouble(rml.getLong()), Shared_Common_Pref.Outletlat, Shared_Common_Pref.Outletlong)));
                if (Common_Class.Check_Distance(Common_Class.ParseDouble(rml.getLat()), Common_Class.ParseDouble(rml.getLong()), Shared_Common_Pref.Outletlat, Shared_Common_Pref.Outletlong) < 0.5) {
                    ShowRetailer_Modal_List.add(rml);
                }
            }
        }
        availableoutlets.setText("Available Outlets:" + "\t" + ShowRetailer_Modal_List.size());
        recyclerView.setAdapter(new Outlet_Info_Adapter(ShowRetailer_Modal_List, R.layout.outlet_info_recyclerview, getApplicationContext(), new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {
                Shared_Common_Pref.Outler_AddFlag = "0";
                Shared_Common_Pref.OutletName = ShowRetailer_Modal_List.get(position).getName().toUpperCase() ;
                Shared_Common_Pref.OutletCode = ShowRetailer_Modal_List.get(position).getId();
                common_class.CommonIntentwithFinish(Route_Product_Info.class);
                common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
            }
        }));
        Createoutlet.setOnClickListener(this);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Createoutlet:
                Shared_Common_Pref.Outler_AddFlag = "1";
                common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                break;
        }
    }
}