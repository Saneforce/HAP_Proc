package com.hap.checkinproc.Activity_Hap;

import static com.hap.checkinproc.Activity_Hap.Login.CheckInDetail;
import static com.hap.checkinproc.Common_Class.Constants.GroupFilter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.Interface.onListItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Dashboard_Order_Reports;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route;
import com.hap.checkinproc.SFA_Activity.GrnListActivity;
import com.hap.checkinproc.SFA_Activity.Lead_Activity;
import com.hap.checkinproc.SFA_Activity.MyTeamActivity;
import com.hap.checkinproc.SFA_Activity.Nearby_Outlets;
import com.hap.checkinproc.SFA_Activity.Offline_Sync_Activity;
import com.hap.checkinproc.SFA_Activity.Outlet_Info_Activity;
import com.hap.checkinproc.SFA_Activity.POSActivity;
import com.hap.checkinproc.SFA_Activity.PrimaryOrderActivity;
import com.hap.checkinproc.SFA_Activity.ProjectionCategorySelectActivity;
import com.hap.checkinproc.SFA_Activity.ReportsListActivity;
import com.hap.checkinproc.SFA_Activity.Reports_Distributor_Name;
import com.hap.checkinproc.SFA_Activity.SFA_Dashboard;
import com.hap.checkinproc.SFA_Activity.StockAuditCategorySelectActivity;
import com.hap.checkinproc.SFA_Activity.VanSalesDashboardRoute;
import com.hap.checkinproc.SFA_Adapter.RyclBrandListItemAdb;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationReceiver;
import com.hap.checkinproc.common.SANGPSTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SFA_Activity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI /*,Main_Model.MasterSyncView*/ {
    LinearLayout Lin_Route, Lin_Lead, Lin_Dashboard, Logout, SyncButon, linorders;
    Gson gson;

    private SANGPSTracker mLUService;
    private LocationReceiver myReceiver;
    private boolean mBound = false;

    public static final String UserDetail = "MyPrefs";
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    SharedPreferences UserDetails;
    DatabaseHandler db;

    ImageView ivLogout, ivCalendar;

    LinearLayout llGridParent;

    OutletDashboardInfoAdapter cumulativeInfoAdapter;
    private List<Cumulative_Order_Model> cumulative_order_modelList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView tvServiceOutlet, tvUniverseOutlet, tvNewSerOutlet, tvTotSerOutlet, tvExistSerOutlet, tvDate, tvTodayCalls, tvProCalls,
            tvCumTodayCalls, tvNewTodayCalls, tvCumProCalls, tvNewProCalls, tvAvgNewCalls, tvAvgTodayCalls, tvAvgCumCalls, tvUserName,
            tvPrimOrder, tvNoOrder, tvTotalValOrder, tvUpdTime;
    private DatePickerDialog fromDatePickerDialog;

    public static String sfa_date = "";

    MenuAdapter menuAdapter;
    RecyclerView rvMenu, rvPrimOrd;
    private List<Common_Model> menuList = new ArrayList<>();
    NumberFormat formatter = new DecimalFormat("##0.00");

    public static String updateTime = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfactivity);
        db = new DatabaseHandler(this);
        sharedCommonPref = new Shared_Common_Pref(SFA_Activity.this);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        common_class = new Common_Class(this);
        gson = new Gson();


        init();
        setOnClickListener();

        ivLogout.setImageResource(R.drawable.ic_baseline_logout_24);


        tvDate.setText("" + Common_Class.GetDatewothouttime());

        sfa_date = tvDate.getText().toString();
        String sUName = UserDetails.getString("SfName", "");
        tvUserName.setText("HI! " + sUName);
        tvUpdTime.setText("Last Updated On : " + updateTime);

        common_class.getProductDetails(this);
        getNoOrderRemarks();
        showDashboardData();
        getPrimaryData("All");
        common_class.getDb_310Data(Constants.GroupFilter, this);


        switch (sharedCommonPref.getvalue(Constants.LOGIN_TYPE)) {
            case Constants.CHECKIN_TYPE:
                menuList.add(new Common_Model("Primary Order", R.drawable.ic_outline_add_chart_48));
                menuList.add(new Common_Model("Secondary Order", R.drawable.ic_outline_assignment_48));
                menuList.add(new Common_Model("Van Sales", R.drawable.ic_outline_local_shipping_24));
                menuList.add(new Common_Model("Outlets", R.drawable.ic_baseline_storefront_24));
                menuList.add(new Common_Model("Nearby Outlets", R.drawable.ic_outline_near_me_24));
                menuList.add(new Common_Model("Reports", R.drawable.ic_reports));
                menuList.add(new Common_Model("Franchise", R.drawable.ic_franchise));
                menuList.add(new Common_Model("My Team", R.drawable.ic_baseline_groups_24));
                menuList.add(new Common_Model("Projection", R.drawable.ic_projection));
               // menuList.add(new Common_Model("Stock Audit", R.drawable.ic_stock_audit));
                if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_Id)))
                    common_class.getDb_310Data(Constants.Distributor_List, this);
                break;
            case Constants.DISTRIBUTER_TYPE:
                menuList.add(new Common_Model("Primary Order", R.drawable.ic_outline_add_chart_48));
                menuList.add(new Common_Model("Secondary Order", R.drawable.ic_outline_assignment_48));
                menuList.add(new Common_Model("Van Sales", R.drawable.ic_outline_local_shipping_24));
                menuList.add(new Common_Model("Outlets", R.drawable.ic_baseline_storefront_24));
                menuList.add(new Common_Model("Nearby Outlets", R.drawable.ic_outline_near_me_24));
                menuList.add(new Common_Model("Reports", R.drawable.ic_reports));
                menuList.add(new Common_Model("POS", R.drawable.ic_outline_assignment_48));
                menuList.add(new Common_Model("GRN", R.drawable.ic_outline_assignment_turned_in_24));

                common_class.getPOSProduct(this);
                common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);
                break;
            default:
                menuList.add(new Common_Model("Secondary Order", R.drawable.ic_outline_assignment_48));
                menuList.add(new Common_Model("Van Sales", R.drawable.ic_outline_local_shipping_24));
                menuList.add(new Common_Model("Outlets", R.drawable.ic_baseline_storefront_24));
                menuList.add(new Common_Model("Nearby Outlets", R.drawable.ic_outline_near_me_24));
                menuList.add(new Common_Model("Reports", R.drawable.ic_reports));
                menuList.add(new Common_Model("POS", R.drawable.ic_outline_assignment_48));
                menuList.add(new Common_Model("GRN", R.drawable.ic_outline_assignment_turned_in_24));
                break;
        }


        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 4);
        rvMenu.setLayoutManager(manager);
        //  rvMenu.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        menuAdapter = new MenuAdapter(this, menuList, new AdapterOnClick() {
            @Override
            public void CallMobile(String menuName) {
                switch (menuName) {
                    case "Nearby Outlets":
                        Intent intent = new Intent(SFA_Activity.this, Nearby_Outlets.class);
                        intent.putExtra("menu", "menu");
                        startActivity(intent);
                        break;
                    case "GRN":
                        common_class.CommonIntentwithNEwTask(GrnListActivity.class);
                        break;
                    case "POS":
                        common_class.CommonIntentwithNEwTask(POSActivity.class);
                        break;
                    case "Primary Order":
                        common_class.getDb_310Data(Constants.PrimaryTAXList, SFA_Activity.this);
                        break;
                    case "Secondary Order":
                        sharedCommonPref.save(Shared_Common_Pref.DCRMode, "SC");
                        startActivity(new Intent(SFA_Activity.this, Dashboard_Route.class));
                        break;

                    case "Sales Return":
                        sharedCommonPref.save(Shared_Common_Pref.DCRMode, "SR");
                        startActivity(new Intent(SFA_Activity.this, Dashboard_Route.class));
                        break;

                    case "Van Sales":
                        sharedCommonPref.save(Shared_Common_Pref.DCRMode, "Van Sales");
                        startActivity(new Intent(SFA_Activity.this, VanSalesDashboardRoute.class));
                        break;
                    case "Outlets":
                        common_class.CommonIntentwithNEwTask(Outlet_Info_Activity.class);
                        break;
                    case "Franchise":
                        common_class.CommonIntentwithNEwTask(Reports_Distributor_Name.class);
                        break;
                    case "Reports":
                        common_class.CommonIntentwithNEwTask(ReportsListActivity.class);
                        // common_class.CommonIntentwithNEwTask(Reports_Outler_Name.class);

                        break;
                    case "My Team":
                        common_class.CommonIntentwithNEwTask(MyTeamActivity.class);
                        break;
                    case "Projection":
                        getProjectionProductDetails(SFA_Activity.this);
                        break;
                    case "Stock Audit":
                      getStockAuditDetails(SFA_Activity.this);  break;


                }
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
        rvMenu.setAdapter(menuAdapter);


    }

    public void getProjectionProductDetails(Activity activity) {

        if (common_class.isNetworkAvailable(activity)) {
            UserDetails = activity.getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

            DatabaseHandler db = new DatabaseHandler(activity);
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("SF", UserDetails.getString("Sfcode", ""));
                jParam.put("Stk", sharedCommonPref.getvalue(Constants.Distributor_Id));
                // jParam.put("outletId", Shared_Common_Pref.OutletCode);
                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getDataArrayList("get/projectionprodgroup", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_grp_List", response.body().toString());
                        db.deleteMasterData(Constants.ProjectionProdGroups_List);
                        db.addMasterData(Constants.ProjectionProdGroups_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/projectionprodtypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_type_List", response.body().toString());
                        db.deleteMasterData(Constants.ProjectionProdTypes_List);
                        db.addMasterData(Constants.ProjectionProdTypes_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/projectionprodcate", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_cat_List", response.body().toString());
                        db.deleteMasterData(Constants.Projection_Category_List);
                        db.addMasterData(Constants.Projection_Category_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/projectionproddets", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_Product_List", response.body().toString());
                        db.deleteMasterData(Constants.Projection_Product_List);
                        db.addMasterData(Constants.Projection_Product_List, response.body());

                        common_class.CommonIntentwithNEwTask(ProjectionCategorySelectActivity.class);

                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.v("Projec_Product_fail", t.getMessage());

                    }
                });
            } catch (Exception e) {
                Log.v("Projec_Product_List_ex", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public void getStockAuditDetails(Activity activity) {

        if (common_class.isNetworkAvailable(activity)) {
            UserDetails = activity.getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

            DatabaseHandler db = new DatabaseHandler(activity);
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("SF", UserDetails.getString("Sfcode", ""));
                jParam.put("Stk", sharedCommonPref.getvalue(Constants.Distributor_Id));
                // jParam.put("outletId", Shared_Common_Pref.OutletCode);
                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getDataArrayList("get/auditprodgroup", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_grp_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_GroupsList);
                        db.addMasterData(Constants.StockAudit_GroupsList, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/auditprodtypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_type_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_Types_List);
                        db.addMasterData(Constants.StockAudit_Types_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/auditprodcate", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_cat_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_Category_List);
                        db.addMasterData(Constants.StockAudit_Category_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/auditproddets", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_Product_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_Product_List);
                        db.addMasterData(Constants.StockAudit_Product_List, response.body());

                        common_class.CommonIntentwithNEwTask(StockAuditCategorySelectActivity.class);

                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.v("Projec_Product_fail", t.getMessage());

                    }
                });
            } catch (Exception e) {
                Log.v("Projec_Product_List_ex", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    private void setOnClickListener() {
        ivCalendar.setOnClickListener(this);
        SyncButon.setOnClickListener(this);
        Lin_Route.setOnClickListener(this);
        Lin_Lead.setOnClickListener(this);
        Lin_Dashboard.setOnClickListener(this);
        linorders.setOnClickListener(this);
        Logout.setOnClickListener(this);
        ivLogout.setOnClickListener(this);

    }

    private void getCumulativeDataFromAPI(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

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

        recyclerView = findViewById(R.id.gvOutlet);

        llGridParent = findViewById(R.id.lin_gridOutlet);
        tvUserName = findViewById(R.id.tvUserName);

        ivLogout = findViewById(R.id.toolbar_home);
        Lin_Route = findViewById(R.id.Lin_Route);
        SyncButon = findViewById(R.id.SyncButon);
        Lin_Lead = findViewById(R.id.Lin_Lead);
        Lin_Dashboard = findViewById(R.id.Lin_Dashboard);
        linorders = findViewById(R.id.linorders);
        Logout = findViewById(R.id.Logout);
        rvMenu = findViewById(R.id.rvMenu);
        tvTotalValOrder = findViewById(R.id.tvTotValOrd);
        tvNoOrder = findViewById(R.id.tvNoOrd);
        tvPrimOrder = findViewById(R.id.tvPrimOrd);
        tvUpdTime = findViewById(R.id.tvUpdTime);
        rvPrimOrd = findViewById(R.id.rvPrimOrder);


        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.linorders:
                common_class.CommonIntentwithNEwTask(Dashboard_Order_Reports.class);
                break;

            case R.id.ivSFACalendar:
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        tvDate.setText("" + year + "-" + month + "-" + dayOfMonth);
                        sfa_date = tvDate.getText().toString();
                        showDashboardData();

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();

                break;
            case R.id.Lin_Dashboard:
                common_class.CommonIntentwithNEwTask(SFA_Dashboard.class);
                break;
            case R.id.Lin_Lead:
                common_class.CommonIntentwithNEwTask(Lead_Activity.class);
                break;
            case R.id.toolbar_home:
                AlertDialogBox.showDialog(SFA_Activity.this, "HAP SFA", "Are You Sure Want to Logout?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        sharedCommonPref.save("ActivityStart", "false");

                        sharedCommonPref.clear_pref(Constants.Distributor_name);
                        sharedCommonPref.clear_pref(Constants.Distributor_Id);
                        if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.CHECKIN_TYPE)) {
                            Intent intent = new Intent(SFA_Activity.this, Dashboard_Two.class);
                            intent.putExtra("Mode", "CIN");
                            startActivity(intent);
                            finish();
                        } else {
                            SharedPreferences CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = UserDetails.edit();
                            editor.putBoolean("Login", false);
                            editor.apply();
                            CheckInDetails.edit().clear().commit();
                            finishAffinity();
                        }
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
        common_class.getDb_310Data(Constants.CUMULATIVEDATA, this);
        //common_class.getDb_310Data(Constants.SERVICEOUTLET, this);
        common_class.getDb_310Data(Constants.OUTLET_SUMMARY, this);
        common_class.getDb_310Data(Constants.SFA_DASHBOARD, this);

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {

                    case GroupFilter:
                        Log.v(key, apiDataResponse);

                        JSONObject filterObj = new JSONObject(apiDataResponse);

                        JSONObject obj1 = new JSONObject();
                        obj1.put("name", "All");
                        obj1.put("GroupCode", "All");

                        JSONArray arr1 = new JSONArray();

                        arr1.put(obj1);

                        if (filterObj.getBoolean("success")) {


                            for (int i = 0; i < filterObj.getJSONArray("Data").length(); i++)
                                arr1.put(filterObj.getJSONArray("Data").getJSONObject(i));


                        }

                        rvPrimOrd.setAdapter(new RyclBrandListItemAdb(arr1, this, new onListItemClick() {

                            @Override

                            public void onItemClick(JSONObject item) {
                                try {
                                    getPrimaryData(item.getString("GroupCode"));
                                } catch (Exception e) {
                                    Log.v("primHist:", e.getMessage());
                                }
                            }

                        }));


                        break;
                    case Constants.PRIMARY_DASHBOARD:

                        Log.v(key + ":", apiDataResponse);
                        JSONObject obj = new JSONObject(apiDataResponse);
                        if (obj.getBoolean("success")) {
                            updateTime = Common_Class.GetDate();
                            tvUpdTime.setText("Last Updated On : " + updateTime);
                            JSONArray arr = obj.getJSONArray("Data");

                            tvPrimOrder.setText("" + arr.getJSONObject(0).getInt("Ordr"));
                            tvNoOrder.setText("" + arr.getJSONObject(0).getInt("NoOrdr"));
                            tvTotalValOrder.setText("" + formatter.format(arr.getJSONObject(0).getDouble("Val")));

                        }
                        break;
                    case Constants.PrimaryTAXList:
                        sharedCommonPref.save(Constants.PrimaryTAXList, apiDataResponse);
                        common_class.CommonIntentwithoutFinish(PrimaryOrderActivity.class);
                        overridePendingTransition(R.anim.in, R.anim.out);
                        break;

                    case Constants.CUMULATIVEDATA:
                        getCumulativeDataFromAPI(apiDataResponse);
                        break;
                    case Constants.SERVICEOUTLET:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tvTotSerOutlet.setText("" + jsonArray.getJSONObject(i).getInt("totalcnt"));
                                tvNewSerOutlet.setText("" + jsonArray.getJSONObject(i).getInt("newcnt"));
                                tvExistSerOutlet.setText("" +
                                        (jsonArray.getJSONObject(i).getInt("totalcnt") - jsonArray.getJSONObject(i).getInt("newcnt")));

                            }
                        }
                        break;

                    case Constants.OUTLET_SUMMARY:
                        JSONObject outletObj = new JSONObject(apiDataResponse);
                        if (outletObj.getBoolean("success")) {

                            JSONArray jsonArray = outletObj.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tvServiceOutlet.setText("" + jsonArray.getJSONObject(i).getInt("ServiceOutlets"));
                                tvUniverseOutlet.setText("" + jsonArray.getJSONObject(i).getInt("UniverseOutlets"));

                            }
                        }

                        break;
                    case Constants.SFA_DASHBOARD:
                        JSONObject sfaObj = new JSONObject(apiDataResponse);
                        if (sfaObj.getBoolean("success")) {

                            JSONArray jsonArray = sfaObj.getJSONArray("Data");
                            cumulative_order_modelList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                cumulative_order_modelList.add(new Cumulative_Order_Model(jsonObject1.getString("Doc_Special_SName"),
                                        jsonObject1.getInt("cnt")));


                            }


                            cumulativeInfoAdapter = new OutletDashboardInfoAdapter(SFA_Activity.this,
                                    cumulative_order_modelList);

                            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                            //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            recyclerView.setLayoutManager(layoutManager);


                            recyclerView.setAdapter(cumulativeInfoAdapter);


                        }
                        break;

                }
            }
        } catch (Exception e) {

        }
    }

    void getPrimaryData(String groupType) {
        JsonObject data = new JsonObject();
        data.addProperty("Grpcode", groupType);
        common_class.getDb_310Data(Constants.PRIMARY_DASHBOARD, SFA_Activity.this, data);
    }

    public class OutletDashboardInfoAdapter extends RecyclerView.Adapter<OutletDashboardInfoAdapter.MyViewHolder> {
        Context context;
        private List<Cumulative_Order_Model> listt;

        public OutletDashboardInfoAdapter(Context applicationContext, List<Cumulative_Order_Model> list) {
            this.context = applicationContext;
            this.listt = list;
        }

        @Override
        public OutletDashboardInfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.outlet_dashboardinfo_recyclerview, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(OutletDashboardInfoAdapter.MyViewHolder holder, int position) {
            try {
                try {
                    holder.tvDesc.setText("" + listt.get(position).getDesc());
                    holder.tvValue.setText("" + listt.get(position).getValue());
//                    holder.pbVisitCount.setMax(listt.get(position).getValue());
//                    holder.pbVisitCount.setProgress(position);
                } catch (Exception e) {
                    Log.e("adaptergetView: ", e.getMessage());
                }


            } catch (Exception e) {
            }


        }

        @Override
        public int getItemCount() {
            return listt.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvDesc, tvValue;
            ProgressBar pbVisitCount;

            public MyViewHolder(View view) {
                super(view);
                tvDesc = view.findViewById(R.id.tvDesc);
                tvValue = view.findViewById(R.id.tvValue);
                pbVisitCount = view.findViewById(R.id.pbVisitCount);


            }
        }


    }

    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
        Context context;
        private List<Common_Model> listt;
        AdapterOnClick adapterOnClick;

        public MenuAdapter(Context applicationContext, List<Common_Model> list, AdapterOnClick adapterOnClick) {
            this.context = applicationContext;
            this.listt = list;
            this.adapterOnClick = adapterOnClick;
        }

        @Override
        public MenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.sfa_menu_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(MenuAdapter.MyViewHolder holder, int position) {
            try {
                try {
                    holder.tvName.setText("" + listt.get(position).getName());
                    holder.ivIcon.setImageResource(listt.get(position).getIcon());

                    holder.llParent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapterOnClick.CallMobile(listt.get(position).getName());
                        }
                    });

                } catch (Exception e) {
                    Log.e("adaptergetView: ", e.getMessage());
                }


            } catch (Exception e) {
            }


        }

        @Override
        public int getItemCount() {
            return listt.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvName;
            ImageView ivIcon;
            LinearLayout llParent;

            public MyViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tvMenuName);
                ivIcon = view.findViewById(R.id.ivMenuIcon);
                llParent = view.findViewById(R.id.llMenu);

            }
        }


    }


    private final ServiceConnection mServiceConection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLUService = ((SANGPSTracker.LocationBinder) service).getLocationUpdateService(getApplicationContext());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLUService = null;
            mBound = false;
        }
    };
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
