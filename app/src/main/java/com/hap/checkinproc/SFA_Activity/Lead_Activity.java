package com.hap.checkinproc.SFA_Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Lead_Adapter;
import com.hap.checkinproc.SFA_Adapter.Outlet_Info_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class Lead_Activity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView route_text, todayoutlets, TotalOutlets, reachedoutlets;
    List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> Retailer_Modal_List;
    Shared_Common_Pref sharedCommonPref;
    Common_Model Model_Pojo;
    String Route_id;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();
    CustomListViewDialog customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_);
        sharedCommonPref = new Shared_Common_Pref(Lead_Activity.this);
        recyclerView = findViewById(R.id.outletrecyclerview);
        route_text = findViewById(R.id.route_text);
        reachedoutlets = findViewById(R.id.reachedoutlets);
        todayoutlets = findViewById(R.id.todayoutlets);
        TotalOutlets = findViewById(R.id.TotalOutlets);
        common_class = new Common_Class(this);
        route_text.setOnClickListener(this);
        reachedoutlets.setOnClickListener(this);
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
        System.out.println("OUTLETLIST" + sharedCommonPref.getvalue(Shared_Common_Pref.Outlet_List));
        Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
        GetJsonData(sharedCommonPref.getvalue(Shared_Common_Pref.Todaydayplanresult), "1");
        GetJsonData(sharedCommonPref.getvalue(Shared_Common_Pref.Rout_List), "2");
        TotalOutlets.setText("Total Outlets:" + "\t" + Retailer_Modal_List.size());
        todayoutlets.setText("Today Outlets:" + "\t" + Retailer_Modal_List.size());
        recyclerView.setAdapter(new Lead_Adapter(Retailer_Modal_List, R.layout.lead_recyclerview, getApplicationContext(), new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {

            }
        }));
    }
    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();

            Route_id = myDataset.get(position).getId();
            route_text.setText(myDataset.get(position).getName());


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reachedoutlets:
                sharedCommonPref.save("RouteSelect", Route_id);
                sharedCommonPref.save("RouteName", route_text.getText().toString());
                common_class.CommonIntentwithoutFinish(New_Outlet_Map_creations.class);
                break;
            case R.id.route_text:
                customDialog = new CustomListViewDialog(Lead_Activity.this, FRoute_Master, 3);
                Window windowww = customDialog.getWindow();
                windowww.setGravity(Gravity.CENTER);
                windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
        }
    }

    private void GetJsonData(String jsonResponse, String type) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                String ETabs = jsonObject1.optString("ETabs");
                Model_Pojo = new Common_Model(id, name, flag);
                if (type.equals("1")) {
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                } else if (String.valueOf(jsonObject1.optString("Button_Access")).indexOf("D") > -1) {
                    route_text.setText(jsonObject1.optString("ClstrName"));
                    Route_id = jsonObject1.optString("cluster");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}