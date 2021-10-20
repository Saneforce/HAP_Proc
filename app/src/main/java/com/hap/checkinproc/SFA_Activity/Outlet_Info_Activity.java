package com.hap.checkinproc.SFA_Activity;

import static com.hap.checkinproc.Common_Class.Constants.Retailer_OutletList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Outlet_Info_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Outlet_Info_Activity extends AppCompatActivity implements View.OnClickListener, Master_Interface, UpdateResponseUI {
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView route_text, todayoutlets, TotalOutlets, reachedoutlets;
    List<Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();
    List<Retailer_Modal_List> Retailer_Modal_ListFilter = new ArrayList<>();
    Shared_Common_Pref sharedCommonPref;
    Common_Model Model_Pojo;
    String Route_id;
    EditText txSearchRet;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();
    List<Common_Model> distributor_master = new ArrayList<>();

    CustomListViewDialog customDialog;
    DatabaseHandler db;
    String TAG = "Lead_Activity:";
    private TextView distributor_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_outlet__info_);
            db = new DatabaseHandler(this);
            common_class = new Common_Class(this);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            sharedCommonPref = new Shared_Common_Pref(Outlet_Info_Activity.this);
            recyclerView = findViewById(R.id.outletrecyclerview);
            route_text = findViewById(R.id.route_text);
            reachedoutlets = findViewById(R.id.reachedoutlets);
            todayoutlets = findViewById(R.id.todayoutlets);
            TotalOutlets = findViewById(R.id.TotalOutlets);
            distributor_text = findViewById(R.id.distributor_text);

            txSearchRet = findViewById(R.id.txSearchRet);

            route_text.setOnClickListener(this);
            reachedoutlets.setOnClickListener(this);
            distributor_text.setOnClickListener(this);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            gson = new Gson();

            getDbstoreData(Constants.Distributor_List);
            getDbstoreData(Constants.Rout_List);

            ImageView backView = findViewById(R.id.imag_back);
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();
            String OrdersTable = sharedCommonPref.getvalue(Constants.Retailer_OutletList);
            // if (!OrdersTable.equals("")) {
            Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
            if (Retailer_Modal_List != null) {
                Retailer_Modal_ListFilter = gson.fromJson(OrdersTable, userType);
                GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "2");
                TotalOutlets.setText(String.valueOf(Retailer_Modal_List.size()));
            }


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            distributor_text.setText(sharedCommonPref.getvalue(Constants.Distributor_name));

            route_text.setText(sharedCommonPref.getvalue(Constants.Route_name));


            if (!sharedCommonPref.getvalue(Constants.Distributor_Id).equals("")) {
                OutletFilter(sharedCommonPref.getvalue(Constants.Distributor_Id), "1");
                findViewById(R.id.btnCmbRoute).setVisibility(View.VISIBLE);
                loadroute(sharedCommonPref.getvalue(Constants.Distributor_Id));

            } else {
                findViewById(R.id.btnCmbRoute).setVisibility(View.GONE);

            }

            txSearchRet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    SearchRetailers();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void SearchRetailers() {
        Retailer_Modal_ListFilter.clear();
        userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
        }.getType();
        String OrdersTable = sharedCommonPref.getvalue(Constants.Retailer_OutletList);
        Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
        for (int sr = 0; sr < Retailer_Modal_List.size(); sr++) {
            String itmname = Retailer_Modal_List.get(sr).getName().toUpperCase();
            String sSchText = txSearchRet.getText().toString().toUpperCase();
            if ((";" + itmname).indexOf(";" + sSchText) > -1) {
                Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(sr));
            }
        }
        TotalOutlets.setText(String.valueOf(Retailer_Modal_List.size()));
        reloadData();
    }

    public void reloadData() {
        if (Retailer_Modal_ListFilter != null) {
            recyclerView.setAdapter(new Outlet_Info_Adapter(Retailer_Modal_ListFilter, R.layout.outlet_info_recyclerview, getApplicationContext(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    Intent intent = new Intent(getApplicationContext(), AddNewRetailer.class);
                    Shared_Common_Pref.Outlet_Info_Flag = "1";
                    Shared_Common_Pref.Editoutletflag = "1";
                    Shared_Common_Pref.Outler_AddFlag = "0";
                    Shared_Common_Pref.FromActivity = "Outlets";
                    Shared_Common_Pref.OutletCode = String.valueOf(Retailer_Modal_ListFilter.get(position).getId());
                    intent.putExtra("OutletCode", String.valueOf(Retailer_Modal_ListFilter.get(position).getId()));
                    intent.putExtra("OutletName", Retailer_Modal_ListFilter.get(position).getName());
                    intent.putExtra("OutletAddress", Retailer_Modal_ListFilter.get(position).getListedDrAddress1());
                    intent.putExtra("OutletMobile", Retailer_Modal_ListFilter.get(position).getMobileNumber());
                    intent.putExtra("OutletRoute", Retailer_Modal_ListFilter.get(position).getTownName());
                    startActivity(intent);
                    finish();
                }
            }));
            //recyclerView.setAdapter(new Lead_Adapter(Retailer_Modal_ListFilter, R.layout.lead_recyclerview, getApplicationContext()));
            //recyclerView.setItemViewCacheSize(Retailer_Modal_List.size());
        }
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 2) {
            route_text.setText("");
            sharedCommonPref.save(Constants.Route_Id, "");
            distributor_text.setText(myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Distributor_name, myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Distributor_Id, myDataset.get(position).getId());
            sharedCommonPref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
            findViewById(R.id.btnCmbRoute).setVisibility(View.VISIBLE);
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("Stk", myDataset.get(position).getId());
                //jParam.put("div", UserDetails.getString("Divcode", ""));
            } catch (JSONException ex) {

            }
            ApiClient.getClient().create(ApiInterface.class)
                    .getDataArrayList("get/routelist", jParam.toString())
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            try {

                                db.deleteMasterData(Constants.Rout_List);
                                db.addMasterData(Constants.Rout_List, response.body().toString());
                                getDbstoreData(Constants.Rout_List);
                                loadroute(myDataset.get(position).getId());
                                OutletFilter(myDataset.get(position).getId(), "1");
                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.d("RouteList", String.valueOf(t));
                        }
                    });
        } else if (type == 3) {
            Route_id = myDataset.get(position).getId();
            route_text.setText(myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
            OutletFilter(myDataset.get(position).getId(), "0");
        }
    }

    public void loadroute(String id) {
        if (common_class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select the Distributor", Toast.LENGTH_SHORT).show();
        }
//        FRoute_Master.clear();
//        for (int i = 0; i < Route_Masterlist.size(); i++) {
//            if (Route_Masterlist.get(i).getFlag().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
//                Log.e("Route_Masterlist", String.valueOf(id) + "STOCKIST" + Route_Masterlist.get(i).getFlag());
//                FRoute_Master.add(new Common_Model(Route_Masterlist.get(i).getId(), Route_Masterlist.get(i).getName(), Route_Masterlist.get(i).getFlag()));
//            }
//        }

        if (FRoute_Master.size() == 1) {
            route_text.setText(FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_name, FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_Id, FRoute_Master.get(0).getId());
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);


        } else {
            route_text.setText("");
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reachedoutlets:
                sharedCommonPref.save("RouteSelect", Route_id);
                sharedCommonPref.save("RouteName", route_text.getText().toString());
                //common_class.CommonIntentwithoutFinish(New_Outlet_Map_creations.class);
                common_class.CommonIntentwithoutFinish(Nearby_Outlets.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.route_text:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    customDialog = new CustomListViewDialog(Outlet_Info_Activity.this, FRoute_Master, 3);
                    Window windowww = customDialog.getWindow();
                    windowww.setGravity(Gravity.CENTER);
                    windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    customDialog.show();
                }
                break;

            case R.id.distributor_text:
                customDialog = new CustomListViewDialog(Outlet_Info_Activity.this, distributor_master, 2);
                Window windoww = customDialog.getWindow();
                windoww.setGravity(Gravity.CENTER);
                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
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

    private void OutletFilter(String id, String flag) {


        try {

            if (flag.equals("0")) {

                Retailer_Modal_ListFilter = new ArrayList<>();

                for (int i = 0; i < Retailer_Modal_List.size(); i++) {
                    if (id.equals(Retailer_Modal_List.get(i).getTownCode()))
                        Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(i));
                }
                reloadData();
            } else {
                common_class.getDataFromApi(Retailer_OutletList, this, false);
            }


        } catch (Exception e) {
            Log.e("DR:RetailorFilter: ", e.getMessage());
        }

    }

    void getDbstoreData(String listType) {
        try {
            FRoute_Master.clear();
            JSONArray jsonArray = db.getMasterData(listType);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                String ETabs = jsonObject1.optString("ETabs");
                Model_Pojo = new Common_Model(id, name, flag);
                if (listType.equals(Constants.Distributor_List)) {
                    String Add2 = jsonObject1.optString("Addr2");
                    String Mob = jsonObject1.optString("Mobile");
                    Model_Pojo = new Common_Model(name, id, flag, Add2, Mob);
                    distributor_master.add(Model_Pojo);
                } else if (listType.equals(Constants.Rout_List)) {
                    Log.e("STOCKIST_CODE", jsonObject1.optString("stockist_code"));
                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                }

            }

        } catch (Exception e) {

        }


    }


    @Override
    public void onLoadFilterData(List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> retailer_modal_list) {
        if (retailer_modal_list != null) {
            Retailer_Modal_List.clear();
            Retailer_Modal_List = retailer_modal_list;
            Retailer_Modal_ListFilter.clear();
            Retailer_Modal_ListFilter = Retailer_Modal_List;
            reloadData();
        }


    }



    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {

    }


}
