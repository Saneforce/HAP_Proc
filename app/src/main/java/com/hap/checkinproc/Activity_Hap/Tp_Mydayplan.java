package com.hap.checkinproc.Activity_Hap;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.MVP.MasterSync_Implementations;
import com.hap.checkinproc.MVP.Master_Sync_View;
import com.hap.checkinproc.Model_Class.Distributor_Master;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.Model_Class.Work_Type_Model;
import com.hap.checkinproc.R;

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

import static com.hap.checkinproc.Common_Class.Common_Class.addquote;

public class Tp_Mydayplan extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {
    Spinner worktypespinner, worktypedistributor, worktyperoute;
    List<Common_Model> worktypelist = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();

    List<Common_Model> FRoute_Master = new ArrayList<>();
    LinearLayout worktypelayout, distributors_layout, route_layout;
    List<Common_Model> distributor_master = new ArrayList<>();
    private Main_Model.presenter presenter;
    Gson gson;
    Type userType;
    EditText edt_remarks;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    String worktype_id, worktypename, distributorname, distributorid, routename, routeid, Fieldworkflag = "";
    private TextClock tClock;
    Button submitbutton;
    CustomListViewDialog customDialog;
    ImageView backarow;
    ProgressBar progressbar;
    TextView worktype_text, distributor_text, route_text;
    TextView tourdate;
    Common_Model Model_Pojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp__mydayplan);
        progressbar = findViewById(R.id.progressbar);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        edt_remarks = findViewById(R.id.edt_remarks);

        gson = new Gson();
        tourdate = findViewById(R.id.tourdate);
        gson = new Gson();
        Log.e("TOuR_PLAN_DATE", common_class.getintentValues("TourDate"));
        tourdate.setText(common_class.getintentValues("TourDate"));
        route_text = findViewById(R.id.route_text);
        worktypelayout = findViewById(R.id.worktypelayout);
        //worktyperoute = findViewById(R.id.worktyperoute);
        distributors_layout = findViewById(R.id.distributors_layout);
        route_layout = findViewById(R.id.route_layout);
        submitbutton = findViewById(R.id.submitbutton);
        worktype_text = findViewById(R.id.worktype_text);
        distributor_text = findViewById(R.id.distributor_text);
        presenter = new MasterSync_Implementations(this, new Master_Sync_View());
        presenter.requestDataFromServer();
        // backarow.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        worktypelayout.setOnClickListener(this);
        distributors_layout.setOnClickListener(this);
        route_layout.setOnClickListener(this);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
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
    public void setDataToRouteObject(Object noticeArrayList, int position) {
        Log.e("Calling Position", String.valueOf(position));

        Log.e("ROUTE_MASTER_Object", String.valueOf(noticeArrayList));
        Log.e("TAG", "response Tbmydayplan: " + new Gson().toJson(noticeArrayList));
        if (position == 0) {
            Log.e("SharedprefrenceVALUES", new Gson().toJson(noticeArrayList));
            GetJsonData(new Gson().toJson(noticeArrayList), "0");
        } else if (position == 1) {
            GetJsonData(new Gson().toJson(noticeArrayList), "1");
        } else {
            GetJsonData(new Gson().toJson(noticeArrayList), "2");
        }

    }

    public void loadroute(String id) {
        Log.e("Select the Distributor", String.valueOf(id));
        if (common_class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select the Distributor", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < Route_Masterlist.size(); i++) {
            Log.e("Stockist_Id", Route_Masterlist.get(i).getFlag());
            if (Route_Masterlist.get(i).getFlag().contains(id)) {
                Log.e("Froutr", Route_Masterlist.get(i).getName());
                FRoute_Master.add(new Common_Model(Route_Masterlist.get(i).getId(), Route_Masterlist.get(i).getName(), Route_Masterlist.get(i).getFlag()));
            }

        }


    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 1) {
            worktype_text.setText(myDataset.get(position).getName());
            worktype_id = String.valueOf(myDataset.get(position).getId());
            Log.e("FIELD_WORK", myDataset.get(position).getFlag());
            Fieldworkflag = myDataset.get(position).getFlag();
            if (myDataset.get(position).getFlag().equals("F")) {
                distributors_layout.setVisibility(View.VISIBLE);
                route_layout.setVisibility(View.VISIBLE);
            } else {
                distributors_layout.setVisibility(View.GONE);
                route_layout.setVisibility(View.GONE);
            }
        } else if (type == 2) {
            routeid = null;
            routename = null;
            route_text.setText("");
            distributor_text.setText(myDataset.get(position).getName());
            distributorid = String.valueOf(myDataset.get(position).getId());
            Log.e("StockistID",myDataset.get(position).getId());
            loadroute(myDataset.get(position).getId());
        } else {
            route_text.setText(myDataset.get(position).getName());
            routename = myDataset.get(position).getName();
            routeid = myDataset.get(position).getId();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitbutton:
                if (vali()) {
                    common_class.ProgressdialogShow(1, "Tour  plan");
                    Calendar c = Calendar.getInstance();
                    String Dcr_Dste = new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date());
                    JSONArray jsonarr = new JSONArray();
                    JSONObject jsonarrplan = new JSONObject();
                    String remarks = edt_remarks.getText().toString();
                    try {
                        JSONObject jsonobj = new JSONObject();
                        jsonobj.put("worktype_code", addquote(worktype_id));
                        jsonobj.put("worktype_name", addquote(worktype_text.getText().toString()));
                        jsonobj.put("sfName", addquote(Shared_Common_Pref.Sf_Name));
                        jsonobj.put("RouteCode", addquote(routeid));
                        jsonobj.put("objective", addquote(remarks));
                        jsonobj.put("RouteName", addquote(routename));
                        jsonobj.put("Tour_Date", addquote(tourdate.getText().toString()));
                        jsonobj.put("Worked_with_Code", addquote(distributorid));
                        jsonobj.put("Worked_with_Name", addquote(distributorname));
                        jsonobj.put("Multiretailername", "''");
                        jsonobj.put("MultiretailerCode", "''");
                        jsonobj.put("worked_with", "''");
                        jsonobj.put("jointWorkCode", "''");
                        jsonobj.put("HQ_Code", "''");
                        jsonobj.put("HQ_Name", "''");
                        jsonarrplan.put("Tour_Plan", jsonobj);
                        jsonarr.put(jsonarrplan);
                        Log.d("Mydayplan_Object", jsonarr.toString());
                        Map<String, String> QueryString = new HashMap<>();
                        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
                        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
                        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
                        QueryString.put("desig", "MGR");
                        Log.d("QueryString", String.valueOf(QueryString));
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<Object> Callto = apiInterface.Tb_Mydayplan(QueryString, jsonarr.toString());
                        Callto.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Log.e("RESPONSE_FROM_SERVER", response.body().toString());
                                common_class.ProgressdialogShow(2, "Tour  plan");
                                if (response.code() == 200 || response.code() == 201) {
                                    common_class.CommonIntentwithFinish(Dashboard.class);
                                    Toast.makeText(Tp_Mydayplan.this, "Tour Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                common_class.ProgressdialogShow(2, "Tour  plan");
                                Log.e("Reponse TAG", "onFailure : " + t.toString());
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.worktypelayout:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, worktypelist, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                customDialog.show();

                break;
            case R.id.distributors_layout:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, distributor_master, 2);
                Window windoww = customDialog.getWindow();
                windoww.setGravity(Gravity.CENTER);
                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;
            case R.id.route_layout:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, FRoute_Master, 3);
                Window windowww = customDialog.getWindow();
                windowww.setGravity(Gravity.CENTER);
                windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;
        }
    }


    public boolean vali() {
        if (worktype_text.getText().toString() == null || worktype_text.getText().toString().isEmpty() || worktype_text.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Select The Worktype", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Fieldworkflag.equals("F") && (distributor_text.getText().toString() == null || distributor_text.getText().toString().isEmpty() || distributor_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Distributor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Fieldworkflag.equals("F") && (route_text.getText().toString() == null || route_text.getText().toString().isEmpty() || route_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Route", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Tp_Mydayplan.super.onBackPressed();
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
                String id = jsonObject1.optString("id");
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                Model_Pojo = new Common_Model(id, name, flag);
                if (type.equals("0")) {
                    worktypelist.add(Model_Pojo);
                } else if (type.equals("1")) {
                    distributor_master.add(Model_Pojo);
                } else {
                    Log.e("STOCKIST_CODE", jsonObject1.optString("stockist_code"));
                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
