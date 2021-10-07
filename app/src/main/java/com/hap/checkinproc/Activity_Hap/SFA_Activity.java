package com.hap.checkinproc.Activity_Hap;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Dashboard_Order_Reports;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route;
import com.hap.checkinproc.SFA_Activity.Dist_Locations;
import com.hap.checkinproc.SFA_Activity.Lead_Activity;
import com.hap.checkinproc.SFA_Activity.Offline_Sync_Activity;
import com.hap.checkinproc.SFA_Activity.Outlet_Info_Activity;
import com.hap.checkinproc.SFA_Activity.PrimaryOrderActivity;
import com.hap.checkinproc.SFA_Activity.Reports_Outler_Name;
import com.hap.checkinproc.SFA_Activity.SFA_Dashboard;
import com.hap.checkinproc.SFA_Adapter.OutletDashboardInfoAdapter;
import com.hap.checkinproc.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SFA_Activity extends AppCompatActivity implements View.OnClickListener /*,Main_Model.MasterSyncView*/ {
    LinearLayout Lin_Route, Lin_DCR, Lin_Lead, Lin_Dashboard, Lin_Outlet, DistLocation, Logout, lin_Reports, SyncButon, linorders, linPrimary;
    Gson gson;
    Type userType;
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;

    ImageView ivLogout, ivCalendar;

    LinearLayout llGridParent;

    OutletDashboardInfoAdapter cumulativeInfoAdapter;
    private List<Cumulative_Order_Model> cumulative_order_modelList = new ArrayList<>();
    GridView recyclerView;
    TextView tvServiceOutlet, tvUniverseOutlet, tvNewSerOutlet, tvTotSerOutlet, tvExistSerOutlet, tvDate, tvTodayCalls, tvProCalls, tvCumTodayCalls, tvNewTodayCalls, tvCumProCalls, tvNewProCalls, tvAvgNewCalls, tvAvgTodayCalls, tvAvgCumCalls;
    private DatePickerDialog fromDatePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfactivity);
        db = new DatabaseHandler(this);
        sharedCommonPref = new Shared_Common_Pref(SFA_Activity.this);
        ivLogout = findViewById(R.id.toolbar_home);
        Lin_Route = findViewById(R.id.Lin_Route);
        SyncButon = findViewById(R.id.SyncButon);
        DistLocation = findViewById(R.id.DistLocation);
        Lin_Lead = findViewById(R.id.Lin_Lead);
        Lin_DCR = findViewById(R.id.Lin_DCR);
        Lin_Dashboard = findViewById(R.id.Lin_Dashboard);
        Lin_Outlet = findViewById(R.id.Lin_Outlet);
        linorders = findViewById(R.id.linorders);
        lin_Reports = findViewById(R.id.lin_Reports);
        Logout = findViewById(R.id.Logout);
        linPrimary = findViewById(R.id.Lin_primary);

        common_class = new Common_Class(this);
        SyncButon.setOnClickListener(this);
        Lin_Route.setOnClickListener(this);
        Lin_DCR.setOnClickListener(this);
        Lin_Lead.setOnClickListener(this);
        lin_Reports.setOnClickListener(this);
        Lin_Dashboard.setOnClickListener(this);
        Lin_Outlet.setOnClickListener(this);
        DistLocation.setOnClickListener(this);
        linorders.setOnClickListener(this);
        Logout.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
        linPrimary.setOnClickListener(this);
        gson = new Gson();


        ivLogout.setImageResource(R.drawable.ic_baseline_logout_24);


        init();
        setOnClickListener();
        getNoOrderRemarks();
        recyclerView = findViewById(R.id.gvOutlet);

        llGridParent = findViewById(R.id.lin_gridOutlet);


        showDashboardData();

    }


    private void setOnClickListener() {
        ivCalendar.setOnClickListener(this);
    }

    private void getCumulativeDataFromAPI() {

        try {

            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calobj = Calendar.getInstance();
                String dateTime = df.format(calobj.getTime());


                JSONObject HeadItem = new JSONObject();
                HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
                HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                HeadItem.put("dt", dateTime);


                Call<ResponseBody> call = service.getCumulativeValues(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }


                                JSONObject jsonObject = new JSONObject(is.toString());

                                if (jsonObject.getBoolean("success")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    int todayCall = 0, cumTodayCall = 0, newTodayCall = 0, proCall = 0, cumProCall = 0, newProCall = 0;

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        todayCall = jsonObject1.getInt("TC");
                                        cumTodayCall = jsonObject1.getInt("CTC");
                                        newTodayCall = jsonObject1.getInt("NTC");
                                        proCall = jsonObject1.getInt("PC");
                                        cumProCall = jsonObject1.getInt("CPC");
                                        newProCall = jsonObject1.getInt("NPC");

                                        tvTodayCalls.setText("" + todayCall);
                                        tvCumTodayCalls.setText("" + cumTodayCall);
                                        tvNewTodayCalls.setText("" + newTodayCall);
                                        tvProCalls.setText("" + proCall);
                                        tvCumProCalls.setText("" + cumProCall);
                                        tvNewProCalls.setText("" + newProCall);


                                    }


                                    if (todayCall > 0 || proCall > 0)
                                        tvAvgTodayCalls.setText("" + (todayCall + proCall) / 2);

                                    if (cumTodayCall > 0 || cumProCall > 0)
                                        tvAvgCumCalls.setText("" + (cumTodayCall + cumProCall) / 2);
                                    if (newTodayCall > 0 || newProCall > 0)
                                        tvAvgNewCalls.setText("" + (newTodayCall + newProCall) / 2);

                                }


                            }

                        } catch (Exception e) {

                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>2", t.toString());


                    }
                });
            } else {
                common_class.showMsg(this, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }


    private void getServiceOutletSummary() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);


                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calobj = Calendar.getInstance();
                String dateTime = df.format(calobj.getTime());


                JSONObject HeadItem = new JSONObject();
                HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
                HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                HeadItem.put("dt", dateTime);


                Call<ResponseBody> call = service.getServiceOutletsummary(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }


                                JSONObject jsonObject = new JSONObject(is.toString());


                                //   {"success":true,"Data":[{"CTC":31,"CPC":28,"TC":0,"PC":0,"NTC":0,"NPC":0}]}

                                if (jsonObject.getBoolean("success")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        tvTotSerOutlet.setText("" + jsonObject1.getInt("totalcnt"));
                                        tvNewSerOutlet.setText("" + jsonObject1.getInt("newcnt"));

                                        tvExistSerOutlet.setText("" +
                                                (jsonObject1.getInt("totalcnt") - jsonObject1.getInt("newcnt")));

                                    }


                                }


                            }

                        } catch (Exception e) {

                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>2", t.toString());


                    }
                });
            } else {
                common_class.showMsg(this, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }

    private void getNoOrderRemarks() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                JSONObject HeadItem = new JSONObject();
                HeadItem.put("Div", Shared_Common_Pref.Div_Code);
                service.getDataArrayList("get/noordrmks", HeadItem.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        db.deleteMasterData("HAPNoOrdRmks");
                        db.addMasterData("HAPNoOrdRmks", response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });

            } else {
                common_class.showMsg(this, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }

    private void getOutletSummary() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);


                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calobj = Calendar.getInstance();
                String dateTime = df.format(calobj.getTime());


                JSONObject HeadItem = new JSONObject();
                HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
                HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                HeadItem.put("dt", dateTime);


                Call<ResponseBody> call = service.getOutletsummary(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }


                                JSONObject jsonObject = new JSONObject(is.toString());
                                if (jsonObject.getBoolean("success")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        tvServiceOutlet.setText("" + jsonObject1.getInt("ServiceOutlets"));
                                        tvUniverseOutlet.setText("" + jsonObject1.getInt("UniverseOutlets"));

                                    }


                                }


                            }

                        } catch (Exception e) {

                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>2", t.toString());


                    }
                });
            } else {
                common_class.showMsg(this, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }


    private void getDashboarddata() {
        try {
            if (common_class.isNetworkAvailable(this)) {

                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);


                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calobj = Calendar.getInstance();
                String dateTime = df.format(calobj.getTime());


                JSONObject HeadItem = new JSONObject();
                HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
                HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                HeadItem.put("dt", dateTime);


                Call<ResponseBody> call = service.getDashboardData(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }


                                JSONObject jsonObject = new JSONObject(is.toString());
                                if (jsonObject.getBoolean("success")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    cumulative_order_modelList.clear();


                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                        cumulative_order_modelList.add(new Cumulative_Order_Model(jsonObject1.getString("Doc_Special_SName"),
                                                jsonObject1.getInt("cnt")));


                                    }


                                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llGridParent.getLayoutParams();
// Changes the height and width to the specified *pixels*
                                    params.height = 180;
                                    params.width = cumulative_order_modelList.size() * 150;
                                    params.gravity = Gravity.CENTER_HORIZONTAL;
                                    llGridParent.setLayoutParams(params);


                                    cumulativeInfoAdapter = new OutletDashboardInfoAdapter(SFA_Activity.this,
                                            cumulative_order_modelList);
                                    recyclerView.setNumColumns(cumulative_order_modelList.size());

                                    recyclerView.setAdapter(cumulativeInfoAdapter);


                                }


                            }

                        } catch (Exception e) {


                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Log.v("fail>>2", t.toString());


                    }
                });
            } else {
                common_class.showMsg(this, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }


    public void init() {
//visitData
        tvTodayCalls = findViewById(R.id.tvTodayCalls);
        tvCumTodayCalls = findViewById(R.id.tvCumTodayCalls);
        tvNewTodayCalls = findViewById(R.id.tvNewTodayCalls);

        tvProCalls = findViewById(R.id.tvProCalls);
        tvCumProCalls = findViewById(R.id.tvCumProCalls);
        tvNewProCalls = findViewById(R.id.tvNewProCalls);

        tvAvgTodayCalls = findViewById(R.id.tvAvgTodayCalls);
        tvAvgCumCalls = findViewById(R.id.tvAvgCumCalls);
        tvAvgNewCalls = findViewById(R.id.tvAvgNewCalls);


        //outlet

        ivCalendar = (ImageView) findViewById(R.id.ivSFACalendar);
        tvDate = (TextView) findViewById(R.id.tvSFADate);


        tvServiceOutlet = (TextView) findViewById(R.id.tvServiceOutlet);
        tvUniverseOutlet = (TextView) findViewById(R.id.tvUniverseOutlet);

        tvNewSerOutlet = (TextView) findViewById(R.id.tvNewServiceOutlet);
        tvTotSerOutlet = (TextView) findViewById(R.id.tvTotalServiceOutlet);
        tvExistSerOutlet = (TextView) findViewById(R.id.tvExistServiceOutlet);

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);


//        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
//        TableRow tbrow0 = new TableRow(this);
//        TextView tv0 = new TextView(this);
//        tv0.setText(" Description ");
//        tv0.setTextColor(Color.WHITE);
//        tbrow0.addView(tv0);
//        TextView tv1 = new TextView(this);
//        tv1.setText(" Existing ");
//        tv1.setTextColor(Color.WHITE);
//        tbrow0.addView(tv1);
//        TextView tv2 = new TextView(this);
//        tv2.setText(" New ");
//        tv2.setTextColor(Color.WHITE);
//        tbrow0.addView(tv2);
//        TextView tv3 = new TextView(this);
//        tv3.setText(" Total Milk \n Litres ");
//        tv3.setTextColor(Color.WHITE);
//        tbrow0.addView(tv3);
//
//        TextView tv4 = new TextView(this);
//        tv4.setText(" New Milk Orders \n Litres ");
//        tv4.setTextColor(Color.WHITE);
//        tbrow0.addView(tv4);
//
//
//        stk.addView(tbrow0);
//        for (int i = 0; i < 25; i++) {
//            TableRow tbrow = new TableRow(this);
//            TextView t1v = new TextView(this);
//            t1v.setText("" + i);
//            t1v.setTextColor(Color.WHITE);
//            t1v.setGravity(Gravity.CENTER);
//            tbrow.addView(t1v);
//            TextView t2v = new TextView(this);
//            t2v.setText("Product " + i);
//            t2v.setTextColor(Color.WHITE);
//            t2v.setGravity(Gravity.CENTER);
//            tbrow.addView(t2v);
//            TextView t3v = new TextView(this);
//            t3v.setText("Rs." + i);
//            t3v.setTextColor(Color.WHITE);
//            t3v.setGravity(Gravity.CENTER);
//            tbrow.addView(t3v);
//            TextView t4v = new TextView(this);
//            t4v.setText("" + i * 15 / 32 * 10);
//            t4v.setTextColor(Color.WHITE);
//            t4v.setGravity(Gravity.CENTER);
//            tbrow.addView(t4v);
//            stk.addView(tbrow);
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Lin_primary:
                common_class.CommonIntentwithNEwTask(PrimaryOrderActivity.class);

                break;
            case R.id.ivSFACalendar:

                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        tvDate.setText("" + year + "-" + month + "-" + dayOfMonth);

                        showDashboardData();

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();

                break;
            case R.id.Lin_Dashboard:
                common_class.CommonIntentwithNEwTask(SFA_Dashboard.class);
                break;
            case R.id.Lin_DCR:
                //  common_class.CommonIntentwithNEwTask(SFADCRActivity.class);
                sharedCommonPref.save(Shared_Common_Pref.DCRMode, "SC");
                Intent intent = new Intent(SFA_Activity.this, Dashboard_Route.class);
                startActivity(intent);
                break;

            case R.id.Lin_Outlet:
                common_class.CommonIntentwithNEwTask(Outlet_Info_Activity.class);
                break;
            case R.id.DistLocation:
                common_class.CommonIntentwithNEwTask(Dist_Locations.class);
                break;
            case R.id.lin_Reports:
                common_class.CommonIntentwithNEwTask(Reports_Outler_Name.class);
                break;

            case R.id.linorders:
                common_class.CommonIntentwithNEwTask(Dashboard_Order_Reports.class);
                break;
            case R.id.Lin_Lead:
                common_class.CommonIntentwithNEwTask(Lead_Activity.class);
                break;
            case R.id.toolbar_home:
                AlertDialogBox.showDialog(SFA_Activity.this, "HAP SFA", "Are You Sure Want to Logout?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        sharedCommonPref.save("ActivityStart", "false");
                        Intent intent = new Intent(SFA_Activity.this, Dashboard_Two.class);
                        intent.putExtra("Mode", "CIN");
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                break;


            case R.id.Lin_Route:
                sharedCommonPref.save(sharedCommonPref.DCRMode, "");
                common_class.CommonIntentwithNEwTask(Dashboard_Route.class);
                break;

            case R.id.SyncButon:
                Shared_Common_Pref.Sync_Flag = "10";
                common_class.CommonIntentwithNEwTask(Offline_Sync_Activity.class);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        Log.v("CHECKING", "CHECKING");
    }


    void showDashboardData() {
        getCumulativeDataFromAPI();

       // common_class.getDb_310Data(Constants.CUMULATIVEDATA, this);

        getServiceOutletSummary();
        getOutletSummary();

        getDashboarddata();

    }

}

  /*  private void getDashboardDataFromAPI() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);


                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calobj = Calendar.getInstance();
                String dateTime = df.format(calobj.getTime());


                JSONObject HeadItem = new JSONObject();
                HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
                HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                HeadItem.put("dt", dateTime);


                Call<ResponseBody> call = service.getDashboardValues(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }


                                JSONObject jsonObject = new JSONObject(is.toString());


                                //   {"success":true,"Data":[{"CTC":31,"CPC":28,"TC":0,"PC":0,"NTC":0,"NPC":0}]}

                                if (jsonObject.getBoolean("success")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                    }


                                }


//                            popMaterialList.clear();
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                                popMaterialList.add(new Common_Model(jsonObject1.getString("POP_Code"), jsonObject1.getString("POP_Name"),
//                                        jsonObject1.getString("POP_UOM")));
//                            }


                            }

                        } catch (Exception e) {

                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>2", t.toString());


                    }
                });
            } else {
                common_class.showMsg(this, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }*/
