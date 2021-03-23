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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Activity_Hap.Tp_Mydayplan;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.ViewReport;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.MVP.MasterSync_Implementations;
import com.hap.checkinproc.MVP.Master_Sync_View;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.Model_Class.Tp_View_Master;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Dashboard_View_Adapter;
import com.hap.checkinproc.SFA_Adapter.Outlet_Report_View_Adapter;
import com.hap.checkinproc.SFA_Adapter.Route_View_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Dashboard_View_Model;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.adapters.Leave_Approval_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Dashboard_Route extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {
    List<Retailer_Modal_List> Retailer_Modal_List;
    List<Retailer_Modal_List> Retailer_Modal_ListFilter;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView headtext, textViewname, Alltextclick, Completeclick, Pendingclick, ReachedOutlet, distributor_text, route_text;
    View Alltextview, completeview, pendingview;
    Common_Model Model_Pojo;
    Shared_Common_Pref shared_common_pref;
    private Main_Model.presenter presenter;
    List<Common_Model> distributor_master = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();
    CustomListViewDialog customDialog;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    List<OutletReport_View_Modal> OutletReport_View_Modal;

    String Route_id, Distributor_Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__route);
        recyclerView = findViewById(R.id.leaverecyclerview);
        GetAllDetails();
        ViewAllOutletOrder();
        presenter = new MasterSync_Implementations(this, new Master_Sync_View());
        presenter.requestDataFromServer();
        shared_common_pref = new Shared_Common_Pref(this);
        headtext = findViewById(R.id.headtext);
        route_text = findViewById(R.id.route_text);
        distributor_text = findViewById(R.id.distributor_text);
        textViewname = findViewById(R.id.textViewname);
        Alltextclick = findViewById(R.id.Alltextclick);
        Completeclick = findViewById(R.id.Completeclick);
        Pendingclick = findViewById(R.id.Pendingclick);
        Alltextview = findViewById(R.id.Alltextview);
        completeview = findViewById(R.id.completeview);
        ReachedOutlet = findViewById(R.id.ReachedOutlet);
        pendingview = findViewById(R.id.pendingview);
        Alltextview.setVisibility(View.VISIBLE);
        completeview.setVisibility(View.INVISIBLE);
        pendingview.setVisibility(View.INVISIBLE);
        Alltextclick.setOnClickListener(this);
        Completeclick.setOnClickListener(this);
        Pendingclick.setOnClickListener(this);
        ReachedOutlet.setOnClickListener(this);
        distributor_text.setOnClickListener(this);
        route_text.setOnClickListener(this);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();

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
                Retailer_Modal_ListFilter = gson.fromJson(new Gson().toJson(response.body()), userType);
                recyclerView.setAdapter(new Route_View_Adapter(Retailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int position) {
                        Shared_Common_Pref.Outler_AddFlag = "0";
                        Log.e("Route_Outlet_Info", Shared_Common_Pref.Outler_AddFlag);
                        Shared_Common_Pref.OutletName = Retailer_Modal_List.get(position).getName().toUpperCase() + "~" + Retailer_Modal_List.get(position).getId();
                        Shared_Common_Pref.OutletCode = Retailer_Modal_List.get(position).getId();
                        common_class.CommonIntentwithoutFinish(Route_Product_Info.class);

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
            case R.id.ReachedOutlet:
                if (Distributor_Id == null || Distributor_Id.equals("")) {
                    Toast.makeText(this, "Select The Distributor", Toast.LENGTH_SHORT).show();
                } else if (Route_id == null || Route_id.equals("")) {
                    Toast.makeText(this, "Select The Route", Toast.LENGTH_SHORT).show();
                } else {
                    shared_common_pref.save("RouteSelect", Route_id);
                    shared_common_pref.save("RouteName", route_text.getText().toString());
                    shared_common_pref.save("Distributor_ID", Distributor_Id);
                    common_class.CommonIntentwithoutFinish(New_Outlet_Map_creations.class);
                }
                break;
            case R.id.distributor_text:
                customDialog = new CustomListViewDialog(Dashboard_Route.this, distributor_master, 2);
                Window windoww = customDialog.getWindow();
                windoww.setGravity(Gravity.CENTER);
                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
            case R.id.route_text:
                customDialog = new CustomListViewDialog(Dashboard_Route.this, FRoute_Master, 3);
                Window windowww = customDialog.getWindow();
                windowww.setGravity(Gravity.CENTER);
                windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void setDataToRoute(ArrayList<Route_Master> noticeArrayList) {
        Log.e("ROUTE_MASTER", String.valueOf(noticeArrayList.size()));
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 2) {
            route_text.setText("");
            Distributor_Id = myDataset.get(position).getId();
            distributor_text.setText(myDataset.get(position).getName());
            loadroute(myDataset.get(position).getId());
        } else if (type == 3) {
            Route_id = myDataset.get(position).getId();
            route_text.setText(myDataset.get(position).getName());
           // OutletFilter(myDataset.get(position).getId());
        }

    }

    private void OutletFilter(String id) {
        Retailer_Modal_ListFilter.clear();
        for (int i = 0; i < Retailer_Modal_List.size(); i++) {
            if (Retailer_Modal_List.get(i).getTownCode().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
                Log.e("Route_Masterlist", String.valueOf(id) + "STOCKIST" + Route_Masterlist.get(i).getFlag());
                Retailer_Modal_ListFilter.add(new Retailer_Modal_List(Retailer_Modal_List.get(i).getId(), Retailer_Modal_List.get(i).getName(), Retailer_Modal_List.get(i).getTownCode(),
                        Retailer_Modal_List.get(i).getTownName(), Retailer_Modal_List.get(i).getLat(), Retailer_Modal_List.get(i).getLong(), Retailer_Modal_List.get(i).getAddrs(),
                        Retailer_Modal_List.get(i).getListedDrAddress1(), Retailer_Modal_List.get(i).getListedDrSlNo(), Retailer_Modal_List.get(i).getMobileNumber(), Retailer_Modal_List.get(i).getDocCatCode(), Retailer_Modal_List.get(i).getContactPersion(), Retailer_Modal_List.get(i).getDocSpecialCode()));
            }

        }
        recyclerView.setAdapter(new Route_View_Adapter(Retailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getApplicationContext(), new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {
                Shared_Common_Pref.OutletName = Retailer_Modal_List.get(position).getName().toUpperCase() + "~" + Retailer_Modal_List.get(position).getId();
                Shared_Common_Pref.OutletCode = Retailer_Modal_List.get(position).getId();
                common_class.CommonIntentwithFinish(Route_Product_Info.class);

            }
        }));

    }

    @Override
    public void setDataToRouteObject(Object noticeArrayList, int position) {
        Log.e("Calling Position", String.valueOf(position));
        Log.e("ROUTE_MASTER_Object", String.valueOf(noticeArrayList));
        if (position == 0) {
            Log.e("SharedprefrenceVALUES", new Gson().toJson(noticeArrayList));
            GetJsonData(new Gson().toJson(noticeArrayList), "0");
        } else if (position == 1) {
            GetJsonData(new Gson().toJson(noticeArrayList), "1");
        } else if (position == 2) {
            GetJsonData(new Gson().toJson(noticeArrayList), "2");
        } else if (position == 3) {
            GetJsonData(new Gson().toJson(noticeArrayList), "3");
        } else if (position == 4) {
            GetJsonData(new Gson().toJson(noticeArrayList), "4");
        } else if (position == 5) {
            GetJsonData(new Gson().toJson(noticeArrayList), "5");
        } else {

        }

    }

    @Override
    public void onResponseFailure(Throwable throwable) {


    }

    public void loadroute(String id) {

        if (common_class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select the Distributor", Toast.LENGTH_SHORT).show();
        }
        FRoute_Master.clear();
        for (int i = 0; i < Route_Masterlist.size(); i++) {
            if (Route_Masterlist.get(i).getFlag().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
                Log.e("Route_Masterlist", String.valueOf(id) + "STOCKIST" + Route_Masterlist.get(i).getFlag());
                FRoute_Master.add(new Common_Model(Route_Masterlist.get(i).getId(), Route_Masterlist.get(i).getName(), Route_Masterlist.get(i).getFlag()));
            }

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
                    distributor_master.add(Model_Pojo);
                } else if (type.equals("2")) {
                    Log.e("STOCKIST_CODE", jsonObject1.optString("stockist_code"));
                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                }

            }


            //spinner.setSelection(adapter.getPosition("select worktype"));
            //            parseJsonData_cluster(clustspin_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ViewAllOutletOrder() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-d");
        Calendar calobj = Calendar.getInstance();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code.replace(",", ""));
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("fromdate", df.format(calobj.getTime()));
        QueryString.put("todate", df.format(calobj.getTime()));
        QueryString.put("Outlet_Code", Shared_Common_Pref.OutletCode);
        Log.e("Report_ValuesMap", QueryString.toString());
        Call<Object> call = service.GetRouteObject(QueryString, "{\"tableName\":\"GetOutletViewReport\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("MAster_Product_Details", response.body() + "");
                System.out.println("GetOutletView" + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                }.getType();
                OutletReport_View_Modal = gson.fromJson(new Gson().toJson(response.body()), userType);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }
}