package com.hap.checkinproc.SFA_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Outlet_Info_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.common.DatabaseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Outlet_Info_Activity extends AppCompatActivity implements View.OnClickListener {

    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView headtext, textViewname, homebutton;
    List<Retailer_Modal_List> Retailer_Modal_List;
    Shared_Common_Pref shared_common_pref;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;
    ImageView ivHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_outlet__info_);
            db = new DatabaseHandler(this);
            shared_common_pref = new Shared_Common_Pref(this);
            sharedCommonPref = new Shared_Common_Pref(Outlet_Info_Activity.this);
            recyclerView = findViewById(R.id.outletrecyclerview);
            headtext = findViewById(R.id.headtext);
            textViewname = findViewById(R.id.textViewname);
            homebutton = findViewById(R.id.homebutton);
            ivHome = findViewById(R.id.toolbar_home);
            homebutton.setOnClickListener(this);
            ivHome.setOnClickListener(this);
            common_class = new Common_Class(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            gson = new Gson();
      /*  ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
            gson = new Gson();
            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();
            // String OrdersTable = sharedCommonPref.getvalue(Shared_Common_Pref.Outlet_List);
            String OrdersTable = String.valueOf(db.getMasterData(Constants.Retailer_OutletList));

            Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
            recyclerView.setAdapter(new Outlet_Info_Adapter(Retailer_Modal_List, R.layout.outlet_info_recyclerview, getApplicationContext(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    Intent intent = new Intent(getApplicationContext(), AddNewRetailer.class);
                    Shared_Common_Pref.Outlet_Info_Flag = "1";
                    Shared_Common_Pref.Editoutletflag = "0";
                    Shared_Common_Pref.Outler_AddFlag = "0";
                    Shared_Common_Pref.OutletCode = String.valueOf(Retailer_Modal_List.get(position).getId());
                    intent.putExtra("OutletCode", String.valueOf(Retailer_Modal_List.get(position).getId()));
                    intent.putExtra("OutletName", Retailer_Modal_List.get(position).getName());
                    intent.putExtra("OutletAddress", Retailer_Modal_List.get(position).getListedDrAddress1());
                    intent.putExtra("OutletMobile", Retailer_Modal_List.get(position).getMobileNumber());
                    intent.putExtra("OutletRoute", Retailer_Modal_List.get(position).getTownName());
                    startActivity(intent);
                }
            }));
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homebutton:
                finish();
                break;
            case R.id.toolbar_home:
                finish();
                break;
        }
    }
}