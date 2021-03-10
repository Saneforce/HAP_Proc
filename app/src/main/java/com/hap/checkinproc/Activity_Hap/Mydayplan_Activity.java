package com.hap.checkinproc.Activity_Hap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity.AllowanceActivity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Joint_Work_Listner;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.MVP.MasterSync_Implementations;
import com.hap.checkinproc.MVP.Master_Sync_View;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.Joint_Work_Adapter;
import com.hap.checkinproc.common.TimerService;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Common_Class.Common_Class.addquote;

public class Mydayplan_Activity extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {

    List<Common_Model> worktypelist = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();
    List<Common_Model> FRoute_Master = new ArrayList<>();
    List<Common_Model> Jointworklistview = new ArrayList<>();
    List<Common_Model> ChillingCenter_List = new ArrayList<>();
    List<Common_Model> Savejointwork = new ArrayList<>();
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    List<Common_Model> Shift_Typelist = new ArrayList<>();
    LinearLayout worktypelayout, distributors_layout, route_layout, joint_work_Caption;
    List<Common_Model> distributor_master = new ArrayList<>();
    private Main_Model.presenter presenter;
    Common_Model Model_Pojo;
    Gson gson;
    EditText edt_remarks, empidedittext;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    String worktype_id, worktypeflag, distributorname, distributorid, routename, routeid, Fieldworkflag = "", Worktype_Button = "", hqid, shifttypeid, Chilling_Id;
    Button submitbutton, GetEmpId;
    CustomListViewDialog customDialog;
    ProgressBar progressbar;
    TextView worktype_text, distributor_text, route_text, text_tour_plancount, hq_text, shift_type, chilling_text, Remarkscaption;
    RecyclerView jointwork_recycler;
    Joint_Work_Adapter da;
    LinearLayout jointwork_layout, joint_work_Recyclerview, hqlayout, shiftypelayout, Procrumentlayout, chillinglayout;
    DatePickerDialog picker;
    Joint_Work_Adapter adapter;

    public static final String mypreference = "mypref";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydayplan_);
        progressbar = findViewById(R.id.progressbar);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        edt_remarks = findViewById(R.id.edt_remarks);
        text_tour_plancount = findViewById(R.id.text_tour_plancount);
        jointwork_layout = findViewById(R.id.jointwork_layout);
        jointwork_layout.setOnClickListener(this);
        gson = new Gson();
        text_tour_plancount.setText("0");
        jointwork_recycler = findViewById(R.id.jointwork_recycler);
        hqlayout = findViewById(R.id.hqlayout);
        shiftypelayout = findViewById(R.id.shiftypelayout);
        hq_text = findViewById(R.id.hq_text);
        shift_type = findViewById(R.id.shift_type);
        common_class = new Common_Class(this);
        jointwork_recycler.setLayoutManager(new LinearLayoutManager(this));
        worktypelayout = findViewById(R.id.worktypelayout);
        distributors_layout = findViewById(R.id.distributors_layout);
        GetEmpId = findViewById(R.id.GetEmpId);
        route_layout = findViewById(R.id.route_layout);
        submitbutton = findViewById(R.id.mydaysubmitbutton);
        worktype_text = findViewById(R.id.worktype_text);
        route_text = findViewById(R.id.route_text);
        distributor_text = findViewById(R.id.distributor_text);
        presenter = new MasterSync_Implementations(this, new Master_Sync_View());
        presenter.requestDataFromServer();
        empidedittext = findViewById(R.id.empidedittext);
        Remarkscaption = findViewById(R.id.remarkscaption);
        chillinglayout = findViewById(R.id.chillinglayout);
        chilling_text = findViewById(R.id.chilling_text);
        Procrumentlayout = findViewById(R.id.Procrumentlayout);
        hqlayout = findViewById(R.id.hqlayout);
        shiftypelayout = findViewById(R.id.shiftypelayout);
        hq_text = findViewById(R.id.hq_text);
        shift_type = findViewById(R.id.shift_type);
        shiftypelayout.setOnClickListener(this);
        hqlayout.setOnClickListener(this);

        chillinglayout.setOnClickListener(this);
        worktypelayout.setOnClickListener(this);
        distributors_layout.setOnClickListener(this);
        route_layout.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        GetEmpId.setOnClickListener(this);
        common_class.ProgressdialogShow(1, "Day plan");
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        if (Shared_Common_Pref.Dept_Type.equals("1")) {
            jointwork_layout.setVisibility(View.GONE);
            distributors_layout.setVisibility(View.GONE);
            route_layout.setVisibility(View.GONE);
            Procrumentlayout.setVisibility(View.VISIBLE);
            edt_remarks.setHint("Enter the Purpose of Visit");
            Remarkscaption.setText("Visiting Purpose");
        } else {
            jointwork_layout.setVisibility(View.VISIBLE);
            distributors_layout.setVisibility(View.VISIBLE);
            route_layout.setVisibility(View.VISIBLE);
            Procrumentlayout.setVisibility(View.GONE);
            Remarkscaption.setText("Remarks");
            edt_remarks.setHint("Enter The Remarks");
        }

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

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


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setDataToRoute(ArrayList<Route_Master> noticeArrayList) {
    }

    @Override
    public void setDataToRouteObject(Object noticeArrayList, int position) {
        Log.e("Calling Position", String.valueOf(position));
        // Toast.makeText(this, "Position" + position, Toast.LENGTH_SHORT).show();
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
            GetJsonData(new Gson().toJson(noticeArrayList), "6");
            common_class.ProgressdialogShow(2, "Day plan");
            Get_MydayPlan();
        }

    }


    @Override
    public void onResponseFailure(Throwable throwable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mydaysubmitbutton:

                if (vali()) {
                    savemydayplan();
                }
                break;


            case R.id.worktypelayout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, worktypelist, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
            case R.id.distributors_layout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, distributor_master, 2);
                Window windoww = customDialog.getWindow();
                windoww.setGravity(Gravity.CENTER);
                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;
            case R.id.route_layout:
                Log.e("ROUTE_FILTER", String.valueOf(FRoute_Master.size()));
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, FRoute_Master, 3);
                Window windowww = customDialog.getWindow();
                windowww.setGravity(Gravity.CENTER);
                windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;
            case R.id.hqlayout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, getfieldforcehqlist, 4);
                Window windowhq = customDialog.getWindow();
                windowhq.setGravity(Gravity.CENTER);
                windowhq.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;

            case R.id.shiftypelayout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, Shift_Typelist, 5);
                Window windowstype = customDialog.getWindow();
                windowstype.setGravity(Gravity.CENTER);
                windowstype.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;


            case R.id.chillinglayout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, ChillingCenter_List, 6);
                Window chillwindow = customDialog.getWindow();
                chillwindow.setGravity(Gravity.CENTER);
                chillwindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
            case R.id.GetEmpId:

                if (empidedittext.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter the EMP_Id", Toast.LENGTH_SHORT).show();
                } else {
                    GetEmpList();
                }
                break;
        }
    }


    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 1) {
            worktype_text.setText(myDataset.get(position).getName());
            worktype_id = String.valueOf(myDataset.get(position).getId());
            Log.e("FIELD_WORK", myDataset.get(position).getFlag());
            Log.e("Button_Access", myDataset.get(position).getCheckouttime());
            Fieldworkflag = myDataset.get(position).getFlag();
            Worktype_Button = myDataset.get(position).getCheckouttime();
            Log.e("FIELD_Dept_Type", Shared_Common_Pref.Dept_Type);
            if (myDataset.get(position).getCheckouttime().indexOf("D") > -1) {
                distributors_layout.setVisibility(View.VISIBLE);
            } else {
                distributor_text.setText("");
                distributors_layout.setVisibility(View.GONE);
            }

            if (myDataset.get(position).getCheckouttime().indexOf("R") > -1) {
                route_layout.setVisibility(View.VISIBLE);
            } else {
                route_text.setText("");
                route_layout.setVisibility(View.GONE);
            }
            if (myDataset.get(position).getCheckouttime().indexOf("J") > -1) {
                jointwork_layout.setVisibility(View.VISIBLE);
            } else {
                jointwork_layout.setVisibility(View.GONE);

            }

        } else if (type == 2) {
            routeid = null;
            routename = null;
            route_text.setText("");
            distributor_text.setText(myDataset.get(position).getName());
            distributorid = String.valueOf(myDataset.get(position).getId());
            Log.e("StockistID", myDataset.get(position).getId());
            shared_common_pref.save("distributor_name", distributor_text.getText().toString());
            shared_common_pref.save("distributor_id", distributorid);
            Log.e("ORDER_SAVE", distributor_text.getText().toString());
            loadroute(myDataset.get(position).getId());
        } else if (type == 3) {
            route_text.setText(myDataset.get(position).getName());
            routename = myDataset.get(position).getName();
            routeid = myDataset.get(position).getId();
            shared_common_pref.save("town_code", routeid);
        } else if (type == 4) {
            hq_text.setText(myDataset.get(position).getName());
            hqid = myDataset.get(position).getId();
        } else if (type == 5) {
            shift_type.setText(myDataset.get(position).getName());
            shifttypeid = myDataset.get(position).getId();
        } else {
            chilling_text.setText(myDataset.get(position).getName());
            Chilling_Id = myDataset.get(position).getId();
        }


    }

    private void Get_MydayPlan() {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "Get/Tp_dayplan");
        QueryString.put("Sf_code", Shared_Common_Pref.Sf_Code);
        QueryString.put("Date", common_class.GetDate());
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        Log.e("Log_TpQuerySTring", QueryString.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Log.e("GettodayResult", "response Tp_View: " + jsonObject.getJSONArray("GettodayResult"));
                    JSONArray jsoncc = jsonObject.getJSONArray("GettodayResult");
                    Log.e("LENGTH", String.valueOf(jsoncc.length()));
                    if (jsoncc.length() > 0) {
                        worktype_id = String.valueOf(jsoncc.getJSONObject(0).get("worktype_code"));
                        edt_remarks.setText(String.valueOf(jsoncc.getJSONObject(0).get("remarks")));
                        Fieldworkflag = String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag"));
                        worktype_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")));
                        if (Shared_Common_Pref.Dept_Type.equals("1")) {
                            hq_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("TourHQ_Name")));
                            hqid = String.valueOf(jsoncc.getJSONObject(0).get("Hq_Id"));
                            shift_type.setText(String.valueOf(jsoncc.getJSONObject(0).get("Typename")));
                            shifttypeid = String.valueOf(jsoncc.getJSONObject(0).get("SHift_Type_Id"));
                            chilling_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("CCentre_Name")));
                            Chilling_Id = String.valueOf(jsoncc.getJSONObject(0).get("Chilling_Id"));
                            jointwork_layout.setVisibility(View.GONE);
                        } else {

                        }

                        if (String.valueOf(jsoncc.getJSONObject(0).get("submit_status")).equals("3")) {
                            submitbutton.setVisibility(View.GONE);
                        }
                        Worktype_Button = String.valueOf(jsoncc.getJSONObject(0).get("Button_Access"));
                        if (String.valueOf(jsoncc.getJSONObject(0).get("Button_Access")).indexOf("R") > -1) {
                            route_layout.setVisibility(View.VISIBLE);
                            routename = String.valueOf(jsoncc.getJSONObject(0).get("RouteName"));
                            route_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("RouteName")));
                            routeid = String.valueOf(jsoncc.getJSONObject(0).get("RouteCode"));
                        } else {
                            route_text.setText("");
                            route_layout.setVisibility(View.GONE);
                        }
                        if (String.valueOf(jsoncc.getJSONObject(0).get("Button_Access")).indexOf("D") > -1) {
                            distributors_layout.setVisibility(View.VISIBLE);
                            distributorid = String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Code"));
                            loadroute(String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Code")));
                            distributor_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Name")));
                        } else {
                            distributor_text.setText("");
                            distributors_layout.setVisibility(View.GONE);
                        }

                        if (String.valueOf(jsoncc.getJSONObject(0).get("Button_Access")).indexOf("J") > -1) {
                            jointwork_layout.setVisibility(View.VISIBLE);
                            Log.e("WORKTYPE_FLAG", Fieldworkflag);
                            String Jointworkcode = String.valueOf(jsoncc.getJSONObject(0).get("JointworkCode"));
                            String JointWork_Name = String.valueOf(jsoncc.getJSONObject(0).get("JointWork_Name"));
                            int jcount = 0;
                            String[] arrOfStr = Jointworkcode.split(",");
                            String[] arrOfname = JointWork_Name.split(",");
                            for (int i = 0; arrOfStr.length > i; i++) {
                                Model_Pojo = new Common_Model(arrOfname[i], arrOfStr[i], false);
                                Jointworklistview.add(Model_Pojo);
                            }
                            text_tour_plancount.setText(String.valueOf(arrOfStr.length));
                            adapter = new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                                @Override
                                public void onIntentClick(int position, boolean flag) {
                                    Jointworklistview.remove(position);
                                    text_tour_plancount.setText(String.valueOf(Jointworklistview.size()));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            jointwork_recycler.setAdapter(adapter);
                        } else {
                            jointwork_layout.setVisibility(View.GONE);
                        }

                    }
                    common_class.ProgressdialogShow(2, "Tour plan");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Tour Plan");
            }
        });
    }

    public boolean vali() {
        if (worktype_text.getText().toString() == null || worktype_text.getText().toString().isEmpty() || worktype_text.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Select The Worktype", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Shared_Common_Pref.Dept_Type.equals("1") && (hq_text.getText().toString() == null || hq_text.getText().toString().isEmpty() || hq_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Head Quarters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Shared_Common_Pref.Dept_Type.equals("1") && (chilling_text.getText().toString() == null || chilling_text.getText().toString().isEmpty() || chilling_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Chilling Center", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Shared_Common_Pref.Dept_Type.equals("1") && (shift_type.getText().toString() == null || shift_type.getText().toString().isEmpty() || shift_type.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Shift M/E", Toast.LENGTH_SHORT).show();
            return false;
        }

        if ( Worktype_Button.indexOf("D") > -1 && (distributor_text.getText().toString() == null || distributor_text.getText().toString().isEmpty() || distributor_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Distributor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Worktype_Button.indexOf("R") > -1 && (route_text.getText().toString() == null || route_text.getText().toString().isEmpty() || route_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Route", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void savemydayplan() {
        progressbar.setVisibility(View.VISIBLE);
        Calendar c = Calendar.getInstance();
        String Dcr_Dste = new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date());
        JSONArray jsonarr = new JSONArray();
        JSONObject jsonarrplan = new JSONObject();
        Savejointwork.addAll(common_class.getfilterList(Jointworklistview));
        Log.e("Savejointwork_SIZE", String.valueOf(Savejointwork.size()));
        String jointwork = "";
        for (int ii = 0; ii < Savejointwork.size(); ii++) {
            if (ii != 0) {
                jointwork = jointwork.concat(",");
            }
            Log.e("JOINT_WORK_SELECT_NAME", Savejointwork.get(ii).getName());
            jointwork = jointwork.concat(Savejointwork.get(ii).getId());
        }
        Log.e("JOINT_WORK", jointwork);
        String remarks = edt_remarks.getText().toString();
        try {
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("wtype", addquote(worktype_id));
            jsonobj.put("sf_member_code", addquote(Shared_Common_Pref.Sf_Code));
            jsonobj.put("stockist", addquote(distributorid));
            jsonobj.put("stkName", distributor_text.getText().toString());
            jsonobj.put("dcrtype", "APP");
            jsonobj.put("cluster", addquote(routeid));
            jsonobj.put("custid", "");
            jsonobj.put("address", "");
            jsonobj.put("remarks", addquote(remarks));
            jsonobj.put("OtherWors", "");
            jsonobj.put("FWFlg", addquote(Fieldworkflag));
            jsonobj.put("Button_Access", Worktype_Button);
            jsonobj.put("ClstrName", addquote(routename));
            jsonobj.put("AppVersion", "V_1.0.5");
            jsonobj.put("dcr_activity_date", Common_Class.GetDate());
            jsonobj.put("worked_with", addquote(jointwork));
            //PROCUREMENT
            jsonobj.put("HQid", addquote(hqid));
            jsonobj.put("Chilling_Id", addquote(Chilling_Id));
            jsonobj.put("Shift_Type_Id", addquote(shifttypeid));
            jsonobj.put("Fromdate", "''");
            jsonobj.put("Todate", "''");
            jsonarrplan.put("tbMyDayPlan", jsonobj);
            jsonarr.put(jsonarrplan);
            Log.d("Mydayplan_Object", jsonarr.toString());
            Map<String, String> QueryString = new HashMap<>();
            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
            QueryString.put("desig", "MGR");
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<Object> Callto = apiInterface.Tb_Mydayplan(QueryString, jsonarr.toString());
            Callto.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Log.e("RESPONSE_FROM_SERVER", response.body().toString());

                    Log.d("QueryString", String.valueOf(QueryString));
                    progressbar.setVisibility(View.GONE);
                    if (response.code() == 200 || response.code() == 201) {
                        /*common_class.CommonIntentwithFinish(Dashboard.class);*/


                        SharedPreferences.Editor editors = sharedpreferences.edit();
                        editors.remove("SharedMode");
                        editors.remove("SharedModeTypeVale");
                        editors.remove("StartedKM");
                        editors.remove("SharedDailyAllowance");
                        editors.remove("SharedFromKm");
                        editors.remove("SharedToKm");
                        editors.remove("ShareStrEnd");
                        editors.remove("SharedDriver");
                        editors.remove("ShareModeID");
                        editors.remove("StoreId");
                        editors.remove("SharedImage");
                        editors.commit();

                        Intent intent = new Intent(Mydayplan_Activity.this, AllowanceActivity.class);
                        intent.putExtra("My_Day_Plan", "One");
                        startActivity(intent);
                        finish();

                        Toast.makeText(Mydayplan_Activity.this, "Day Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    progressbar.setVisibility(View.GONE);

                    Log.e("Reponse TAG", "onFailure : " + t.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Mydayplan_Activity.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

    private void GetJsonData(String jsonResponse, String type) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                Model_Pojo = new Common_Model(id, name, flag);
                if (type.equals("0")) {
                    String ETabs = jsonObject1.optString("ETabs");
                    Model_Pojo = new Common_Model(id, name, flag, ETabs);
                    worktypelist.add(Model_Pojo);
                } else if (type.equals("1")) {
                    distributor_master.add(Model_Pojo);
                } else if (type.equals("2")) {
                    Log.e("STOCKIST_CODE", jsonObject1.optString("stockist_code"));
                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                } else if (type.equals("3")) {
                  /*  Model_Pojo = new Common_Model(name + "-" + jsonObject1.optString("desig"), id, false);
                    Jointworklistview.add(Model_Pojo);*/
                } else if (type.equals("4")) {
                    String Odflag = jsonObject1.optString("ODFlag");
                    Model_Pojo = new Common_Model(id, name, Odflag);
                    getfieldforcehqlist.add(Model_Pojo);
                } else if (type.equals("5")) {
                    Model_Pojo = new Common_Model(id, name, flag);
                    Shift_Typelist.add(Model_Pojo);
                } else {
                    Model_Pojo = new Common_Model(id, name, flag);
                    ChillingCenter_List.add(Model_Pojo);
                }

            }

            if (type.equals("3")) {
                jointwork_recycler.setAdapter(new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                    @Override
                    public void onIntentClick(int po, boolean flag) {
                        Jointworklistview.get(po).setSelected(flag);
                        int jcount = 0;
                        for (int i = 0; Jointworklistview.size() > i; i++) {
                            if (Jointworklistview.get(i).isSelected()) {
                                jcount = jcount + 1;
                            }

                        }
                        text_tour_plancount.setText(String.valueOf(jcount));
                    }
                }));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetEmpList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> Callto = apiInterface.getDataArrayList("get/Emp_IdName",
                Shared_Common_Pref.Div_Code,
                Shared_Common_Pref.Sf_Code, empidedittext.getText().toString(), "", "DateTime", null);
        Callto.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray res = response.body();
                if (res.size() < 1) {
                    Toast.makeText(getApplicationContext(), "Emp Code  Not Found!", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.e("EMP_ID_Details", String.valueOf(Jointworklistview.size()));
                JsonObject EmpDet = res.get(0).getAsJsonObject();
                Log.e("EMP_ID_Details", String.valueOf(Jointworklistview.indexOf(new Common_Model(EmpDet.get("Sf_Name").getAsString() + "-" + EmpDet.get("sf_Designation_Short_Name").getAsString(), EmpDet.get("Sf_Code").getAsString(), false))));
                Common_Model Model_Pojo = new Common_Model(EmpDet.get("Sf_Name").getAsString() + "-" + EmpDet.get("sf_Designation_Short_Name").getAsString(), EmpDet.get("Sf_Code").getAsString(), false);

                boolean flag = CheckContains(Jointworklistview, EmpDet.get("Sf_Code").getAsString());
                if (flag) {
                    Toast.makeText(getApplicationContext(), "Already Added SF Name!", Toast.LENGTH_LONG).show();
                } else {
                    Jointworklistview.add(Model_Pojo);
                }
                text_tour_plancount.setText(String.valueOf(Jointworklistview.size()));
                adapter = new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                    @Override
                    public void onIntentClick(int position, boolean flag) {
                        Jointworklistview.remove(position);
                        text_tour_plancount.setText(String.valueOf(Jointworklistview.size()));
                        adapter.notifyDataSetChanged();
                    }
                });
                jointwork_recycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("Error:", "Some Error" + t.getMessage());
            }
        });
    }

    private boolean CheckContains(List<Common_Model> jointworklistview, String Sf_Code) {
        boolean flag = false;
        for (int i = 0; jointworklistview.size() > i; i++) {
            if (jointworklistview.get(i).getId().equals(Sf_Code)) {
                flag = true;
            }

        }
        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();

        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onPause() {
        super.onPause();

        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(new Intent(this, TimerService.class));
    }

}


