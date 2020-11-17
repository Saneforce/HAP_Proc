package com.hap.checkinproc.Activity_Hap;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Common_Class.Common_Class.addquote;

public class Mydayplan_Activity extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {
    Spinner worktypespinner, worktypedistributor, worktyperoute;
    List<Work_Type_Model> worktypelist;
    List<Route_Master> Route_Masterlist;
    List<Route_Master> FRoute_Master;
    LinearLayout worktypelayout, distributors_layout, route_layout;
    List<Distributor_Master> distributor_master;
    private Main_Model.presenter presenter;
    Gson gson;
    Type userType;
    EditText edt_remarks;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    String worktype_id, worktypeflag, distributorname, distributorid, routename, routeid, Fieldworkflag = "";
    private TextClock tClock;
    Button submitbutton;
    CustomListViewDialog customDialog;
    ImageView backarow;
    ProgressBar progressbar;
    TextView worktype_text, distributor_text, route_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydayplan_);
        progressbar = findViewById(R.id.progressbar);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        edt_remarks = findViewById(R.id.edt_remarks);
        backarow = findViewById(R.id.backarow);
        gson = new Gson();
        worktypelayout = findViewById(R.id.worktypelayout);
        distributors_layout = findViewById(R.id.distributors_layout);
        route_layout = findViewById(R.id.route_layout);
        submitbutton = findViewById(R.id.mydaysubmitbutton);
        worktype_text = findViewById(R.id.worktype_text);
        route_text = findViewById(R.id.route_text);
        distributor_text = findViewById(R.id.distributor_text);
        presenter = new MasterSync_Implementations(this, new Master_Sync_View());
        presenter.requestDataFromServer();
        backarow.setOnClickListener(this);
        worktypelayout.setOnClickListener(this);
        distributors_layout.setOnClickListener(this);
        route_layout.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        common_class.ProgressdialogShow(1, "Day plan");
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }


    public void loadroute(String id) {
        Log.e("Select the Distributor", String.valueOf(id));
        if (common_class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select the Distributor", Toast.LENGTH_SHORT).show();
        }
        FRoute_Master = new ArrayList<Route_Master>();
        ArrayList<String> route = new ArrayList<>();
        for (int i = 0; i < Route_Masterlist.size(); i++) {

            if (Route_Masterlist.get(i).getStockistCode().contains(id)) {
                route.add(Route_Masterlist.get(i).getName());
                FRoute_Master.add(new Route_Master(Route_Masterlist.get(i).getId(), Route_Masterlist.get(i).getName(), Route_Masterlist.get(i).getTarget(), Route_Masterlist.get(i).getMinProd(), Route_Masterlist.get(i).getFieldCode(), Route_Masterlist.get(i).getStockistCode()));

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
        Log.e("ROUTE_MASTER", String.valueOf(noticeArrayList.size()));
    }

    @Override
    public void setDataToRouteObject(Object noticeArrayList, int position) {
        Log.e("Calling Position", String.valueOf(position));
        Toast.makeText(this, "Position" + position, Toast.LENGTH_SHORT).show();
        Log.e("ROUTE_MASTER_Object", String.valueOf(noticeArrayList));
        Log.e("TAG", "response Tbmydayplan: " + new Gson().toJson(noticeArrayList));
        if (position == 0) {
            Log.e("SharedprefrenceVALUES", new Gson().toJson(noticeArrayList));
            userType = new TypeToken<ArrayList<Work_Type_Model>>() {
            }.getType();
            worktypelist = gson.fromJson(new Gson().toJson(noticeArrayList), userType);
        } else if (position == 1) {
            userType = new TypeToken<ArrayList<Distributor_Master>>() {
            }.getType();
            distributor_master = gson.fromJson(new Gson().toJson(noticeArrayList), userType);
            //loaddistriSpinner();
        } else {
            userType = new TypeToken<ArrayList<Route_Master>>() {
            }.getType();
            Route_Masterlist = gson.fromJson(new Gson().toJson(noticeArrayList), userType);
            FRoute_Master = Route_Masterlist;

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
            case R.id.backarow:
                common_class.CommonIntentwithFinish(Dashboard.class);
                break;
            case R.id.worktypelayout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, worktypelist, 1, distributor_master, Route_Masterlist);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                customDialog.show();

                break;
            case R.id.distributors_layout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, worktypelist, 2, distributor_master, Route_Masterlist);
                Window windoww = customDialog.getWindow();
                windoww.setGravity(Gravity.CENTER);
                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;
            case R.id.route_layout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, worktypelist, 3, distributor_master, Route_Masterlist);
                Window windowww = customDialog.getWindow();
                windowww.setGravity(Gravity.CENTER);
                windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;

        }
    }


    @Override
    public void OnclickMasterType(List<Work_Type_Model> myDataset, int position, List<Distributor_Master> Distributor_Master, List<Route_Master> route_Master, int type) {
        customDialog.dismiss();
        if (type == 1) {
            worktype_text.setText(myDataset.get(position).getName());
            worktype_id = String.valueOf(myDataset.get(position).getId());
            Log.e("FIELD_WORK", myDataset.get(position).getFWFlg());
            Fieldworkflag = myDataset.get(position).getFWFlg();
            if (myDataset.get(position).getFWFlg().equals("F")) {
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
            distributor_text.setText(Distributor_Master.get(position).getName());
            distributorid = String.valueOf(Distributor_Master.get(position).getId());
            loadroute(String.valueOf(Distributor_Master.get(position).getId()));
        } else {
            route_text.setText(route_Master.get(position).getName());
            routename = route_Master.get(position).getName();
            routeid = route_Master.get(position).getId();
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
                // locationList=response.body();

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Log.e("GettodayResult", "response Tp_View: " + jsonObject.getJSONArray("GettodayResult"));
                    JSONArray jsoncc = jsonObject.getJSONArray("GettodayResult");
                    Log.e("LENGTH", String.valueOf(jsoncc.length()));
                    if (jsoncc.length() > 0) {
                        if (String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")).equals("Meeting")) {
                            edt_remarks.setText(String.valueOf(jsoncc.getJSONObject(0).get("remarks")));
                            worktype_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")));
                            distributors_layout.setVisibility(View.GONE);
                            route_layout.setVisibility(View.GONE);
                            Fieldworkflag = "M";
                        } else {
                            routename = String.valueOf(jsoncc.getJSONObject(0).get("RouteName"));
                            route_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("RouteName")));
                            routeid = String.valueOf(jsoncc.getJSONObject(0).get("RouteCode"));
                            distributorname = String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Name"));
                            distributorid = String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Code"));
                            worktype_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")));
                            loadroute(String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Code")));
                            distributor_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Name")));
                            Fieldworkflag = "F";
                        }
                    } else {
                        Toast.makeText(Mydayplan_Activity.this, "Your Not done Tour Plan", Toast.LENGTH_SHORT).show();

                    }
                    common_class.ProgressdialogShow(2, "Day plan");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Day plan");
            }
        });
    }

    public boolean vali() {
        if (worktype_text.getText().toString() == null || worktype_text.getText().toString().isEmpty() || worktype_text.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Select The Worktype", Toast.LENGTH_SHORT).show();
            //worktype_text.setError("Select The Worktype");
            return false;
        }
        if (Fieldworkflag.equals("F") && (distributor_text.getText().toString() == null || distributor_text.getText().toString().isEmpty() || distributor_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Distributor", Toast.LENGTH_SHORT).show();
            //distributor_text.setError("Select The Distributor");
            return false;
        }
        if (Fieldworkflag.equals("F") && (route_text.getText().toString() == null || route_text.getText().toString().isEmpty() || route_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Route", Toast.LENGTH_SHORT).show();
            //route_text.setError("Select The Route");

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

        String remarks = edt_remarks.getText().toString();
        try {
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("wtype", addquote(worktype_id));
            jsonobj.put("sf_member_code", addquote(Shared_Common_Pref.Sf_Code));
            jsonobj.put("stockist", addquote(distributorid));
            jsonobj.put("stkName", distributorname);
            jsonobj.put("dcrtype", "APP");
            jsonobj.put("cluster", addquote(routeid));
            jsonobj.put("custid", "");
            jsonobj.put("address", "");
            jsonobj.put("remarks", addquote(remarks));
            jsonobj.put("OtherWors", "");
            jsonobj.put("FWFlg", addquote(worktypeflag));
            jsonobj.put("ClstrName", addquote(routename));
            jsonobj.put("AppVersion", "V_1.0.5");
            jsonobj.put("dcr_activity_date", Common_Class.GetDate());
            jsonobj.put("worked_with", "''");
            jsonarrplan.put("tbMyDayPlan", jsonobj);
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
                    progressbar.setVisibility(View.GONE);
                    if (response.code() == 200 || response.code() == 201) {
                        common_class.CommonIntentwithFinish(Dashboard.class);
                        Toast.makeText(Mydayplan_Activity.this, "Day Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    progressbar.setVisibility(View.GONE);

                    Log.e("Reponse TAG", "onFailure : " + t.toString());
//                    Toast.makeText(getActivity(), "Myday Plan Not Submitted ", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(Leave_Activity.this, MainActivity.class);
//                    startActivity(i);
//                    getFragmentManager().beginTransaction().replace(R.id.DCRMain_Frame, new Home_Fragment_Activity()).commit();
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
}


