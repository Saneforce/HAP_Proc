package com.hap.checkinproc.Activity_Hap;

import static com.hap.checkinproc.Common_Class.Constants.GroupFilter;
import static com.hap.checkinproc.Common_Class.Constants.VAN_STOCK;
import static com.hap.checkinproc.SFA_Activity.HAPApp.CurrencySymbol;
import static com.hap.checkinproc.SFA_Activity.HAPApp.activeActivity;
import static com.hap.checkinproc.SFA_Activity.HAPApp.getActiveActivity;
import static com.hap.checkinproc.SFA_Activity.HAPApp.setAppLogos;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity.ProcPrimaryOrderActivity;
import com.hap.checkinproc.Activity.Util.ListModel;
import com.hap.checkinproc.Activity.ViewActivity;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.OnLiveUpdateListener;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.Interface.onListItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Dashboard_Order_Reports;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route;
import com.hap.checkinproc.SFA_Activity.GrnListActivity;
import com.hap.checkinproc.SFA_Activity.InshopActivity;
import com.hap.checkinproc.SFA_Activity.Invoice_History;
import com.hap.checkinproc.SFA_Activity.Lead_Activity;
import com.hap.checkinproc.SFA_Activity.MyTeamActivity;
import com.hap.checkinproc.SFA_Activity.Nearby_Outlets;
import com.hap.checkinproc.SFA_Activity.Offline_Sync_Activity;
import com.hap.checkinproc.SFA_Activity.Outlet_Info_Activity;
import com.hap.checkinproc.SFA_Activity.POSActivity;
import com.hap.checkinproc.SFA_Activity.PendingOutletsCategory;
import com.hap.checkinproc.SFA_Activity.PrimaryOrderActivity;
import com.hap.checkinproc.SFA_Activity.ProjectionCategorySelectActivity;
import com.hap.checkinproc.SFA_Activity.ReportsListActivity;
import com.hap.checkinproc.SFA_Activity.Reports_Distributor_Name;
import com.hap.checkinproc.SFA_Activity.SFA_Dashboard;
import com.hap.checkinproc.SFA_Activity.StockAuditCategorySelectActivity;
import com.hap.checkinproc.SFA_Activity.VanSalesDashboardRoute;
import com.hap.checkinproc.SFA_Adapter.RyclBrandListItemAdb;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.adapters.OffersAdapter;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationReceiver;
import com.hap.checkinproc.common.SANGPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SFA_Activity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI, Master_Interface /*,Main_Model.MasterSyncView*/ {
    public static final String UserDetail = "MyPrefs";
    public static final String CheckInDetail = "CheckInDetail";
    public static String sfa_date = "";
    public static String updateTime = "";
    LinearLayout Lin_Route, Lin_Lead, Lin_Dashboard, Logout, SyncButon, linorders;
    Gson gson;
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    SharedPreferences UserDetails;
    SharedPreferences CheckInDetails;
    DatabaseHandler db;
    ImageView ivLogout, ivCalendar, ivProcureSync, btnCloseOffer;
    LinearLayout llGridParent, linOffer;
    OutletDashboardInfoAdapter cumulativeInfoAdapter;
    RecyclerView recyclerView;
    TextView tvServiceOutlet, tvUniverseOutlet, tvNewSerOutlet, tvTotSerOutlet, tvExistSerOutlet, tvDate, tvTodayCalls, tvProCalls,
            tvCumTodayCalls, tvNewTodayCalls, tvCumProCalls, tvNewProCalls, tvAvgNewCalls, tvAvgTodayCalls, tvAvgCumCalls, tvUserName,
            tvPrimOrder, tvNoOrder, tvTotalValOrder, tvUpdTime, lblSlideNo;
    MenuAdapter menuAdapter;
    RecyclerView rvMenu, rvPrimOrd, ryclOffers;
    NumberFormat formatter = new DecimalFormat("##0.00");
    ApiInterface apiService;
    int menuItem = 0;
    private SANGPSTracker mLUService;
    private LocationReceiver myReceiver;
    private boolean mBound = false;
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
    private List<Cumulative_Order_Model> cumulative_order_modelList = new ArrayList<>();
    private DatePickerDialog fromDatePickerDialog;
    private List<ListModel> menuList = new ArrayList<>();

    TextView tv_tot_sku,tv_tot_qty,tv_tot_value,tv_tot_saleret,tv_tot_norder,tv_tot_saleretval;
    TextView tv_vantot_sku,tv_vantot_qty,tv_vantot_value,tv_tot_vansale,tv_vantot_norder,tv_tot_vanorder,
            tv_vantot_saleretval,tv_vantot_saleret;
    TextView tv_postot_sku,tv_postot_qty,tv_postot_value,tv_postot_order,tv_postot_saleretval,tv_postot_saleret;
    CardView cv_secord_det,cv_vansale_det,cv_countersale_det;
    List<Common_Model> activityList=new ArrayList<>();
    RelativeLayout rlActivity;
    TextView tvActivity;
    LinearLayout ll_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfactivity);
        db = new DatabaseHandler(this);
        sharedCommonPref = new Shared_Common_Pref(SFA_Activity.this);
        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        common_class = new Common_Class(this);
        gson = new Gson();
        apiService = ApiClient.getClient().create(ApiInterface.class);


        linOffer = findViewById(R.id.linOffer);
        linOffer.setVisibility(View.GONE);
        ryclOffers = findViewById(R.id.ryclOffers);
        lblSlideNo = findViewById(R.id.lblSlideNo);
        btnCloseOffer = findViewById(R.id.btnCloseOffer);


        init();
        setOnClickListener();

        String sOffShown = sharedCommonPref.getvalue(Constants.DB_OfferShownOn, "");

        if (!Common_Class.GetDatewothouttime().equalsIgnoreCase(sOffShown)) {
            sharedCommonPref.clear_pref(Constants.DB_Offer_NOTIFY);
        }

        btnCloseOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linOffer.setVisibility(View.GONE);
            }
        });
        ivLogout.setImageResource(R.drawable.ic_baseline_logout_24);


        tvDate.setText("" + Common_Class.GetDatewothouttime());

        sfa_date = tvDate.getText().toString();
        String sUName = UserDetails.getString("SfName", "");
        tvUserName.setText("HI! " + sUName);
        tvUpdTime.setText("Last Updated On : " + updateTime);

        common_class.getProductDetails(this);
        getNoOrderRemarks();
        showDashboardData();
        getOfferNotify();
        getPrimaryData("All");
        common_class.getDb_310Data(Constants.GroupFilter, this);

        if (UserDetails.getString("DeptType", "").equalsIgnoreCase("1")) {
            ivProcureSync.setVisibility(View.VISIBLE);
            findViewById(R.id.cvOutletSummary).setVisibility(View.GONE);
            findViewById(R.id.cvTodayOrders).setVisibility(View.GONE);
            findViewById(R.id.cvSalesData).setVisibility(View.GONE);
            findViewById(R.id.cvCalls).setVisibility(View.GONE);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_Id)))
                common_class.getDb_310Data(Constants.Distributor_List, this);

            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.PROCUR_MENU)))
                callDynamicmenu();
            else {
                menuList.clear();
                Type userType = new TypeToken<ArrayList<ListModel>>() {
                }.getType();
                menuList = gson.fromJson(sharedCommonPref.getvalue(Constants.PROCUR_MENU), userType);
                setMenuAdapter();
            }


        } else {
            ivProcureSync.setVisibility(View.GONE);
            switch (sharedCommonPref.getvalue(Constants.LOGIN_TYPE)) {
                case Constants.CHECKIN_TYPE:
                    menuList.add(new ListModel("", "Primary Order", "", "", "", R.drawable.ic_outline_add_chart_48));
                    menuList.add(new ListModel("", "Secondary Order", "", "", "", R.drawable.ic_outline_assignment_48));
                    // menuList.add(new ListModel("", "Van Sales", "", "", "", R.drawable.ic_outline_local_shipping_24));
                    menuList.add(new ListModel("", "Outlets", "", "", "", R.drawable.ic_baseline_storefront_24));
                    if (UserDetails.getString("Sfcode", "").equals("MGR8171")) {
                        menuList.add(new ListModel("", "Approve Outlets", "", "", "", R.drawable.ic_approve_outlets));
                    }

                    menuList.add(new ListModel("", "Nearby Outlets", "", "", "", R.drawable.ic_outline_near_me_24));
                    menuList.add(new ListModel("", "Reports", "", "", "", R.drawable.ic_reports));
                    menuList.add(new ListModel("", "Franchise", "", "", "", R.drawable.ic_franchise));
                    menuList.add(new ListModel("", "My Team", "", "", "", R.drawable.ic_baseline_groups_24));
                    menuList.add(new ListModel("", "Projection", "", "", "", R.drawable.ic_projection));
                    // menuList.add(new ListModel("", "Stock Audit", "", "", "", R.drawable.ic_stock_audit));
                    menuList.add(new ListModel("", "Inshop", "", "", "", R.drawable.ic_inshop));
                    menuList.add(new ListModel("", "Van Sales", "", "", "", R.drawable.ic_outline_local_shipping_24));
                    if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_Id)))
                        common_class.getDb_310Data(Constants.Distributor_List, this);
                    break;
                case Constants.DISTRIBUTER_TYPE:
                    menuList.add(new ListModel("", "Primary Order", "", "", "", R.drawable.ic_outline_add_chart_48));
                    menuList.add(new ListModel("", "Secondary Order", "", "", "", R.drawable.ic_outline_assignment_48));
                    menuList.add(new ListModel("", "Van Sales", "", "", "", R.drawable.ic_outline_local_shipping_24));
                    menuList.add(new ListModel("", "Outlets", "", "", "", R.drawable.ic_baseline_storefront_24));
                    menuList.add(new ListModel("", "Nearby Outlets", "", "", "", R.drawable.ic_outline_near_me_24));
                    menuList.add(new ListModel("", "Reports", "", "", "", R.drawable.ic_reports));
                    menuList.add(new ListModel("", "Counter Sales", "", "", "", R.drawable.ic_outline_assignment_48));
                    menuList.add(new ListModel("", "GRN", "", "", "", R.drawable.ic_outline_assignment_turned_in_24));
//                    menuList.add(new ListModel("", "Inshop", "", "", "", R.drawable.ic_inshop));

                    // Todo: For Testing Approve Outlets
                   //  menuList.add(new ListModel("", "Approve Outlets", "", "", "", R.drawable.ic_approve_outlets));


                    common_class.getPOSProduct(this);
                    common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);
                    break;
                default:
                    menuList.add(new ListModel("", "Secondary Order", "", "", "", R.drawable.ic_outline_assignment_48));
                    menuList.add(new ListModel("", "Van Sales", "", "", "", R.drawable.ic_outline_local_shipping_24));
                    menuList.add(new ListModel("", "Outlets", "", "", "", R.drawable.ic_baseline_storefront_24));
                    menuList.add(new ListModel("", "Nearby Outlets", "", "", "", R.drawable.ic_outline_near_me_24));
                    menuList.add(new ListModel("", "Reports", "", "", "", R.drawable.ic_reports));
                    menuList.add(new ListModel("", "POS", "", "", "", R.drawable.ic_outline_assignment_48));
                    menuList.add(new ListModel("", "GRN", "", "", "", R.drawable.ic_outline_assignment_turned_in_24));
//                    menuList.add(new ListModel("", "Inshop", "", "", "", R.drawable.ic_inshop));

                    break;
            }
            setMenuAdapter();
        }

        getNotify();

    }

    private void getNotify() {
        Shared_Common_Pref mShared_common_pref = new Shared_Common_Pref(this);
        if (com.hap.checkinproc.Activity_Hap.Common_Class.isNullOrEmpty(mShared_common_pref.getvalue(Constants.DB_TWO_GET_NOTIFY))) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> rptCall = apiInterface.getDataArrayListDist("get/dist_notify",
                    UserDetails.getString("Divcode", ""),
                    UserDetails.getString("Sfcode", ""), sharedCommonPref.getvalue(Constants.Distributor_Id), "", null);
            rptCall.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        JsonArray res = response.body();
                        assignGetNotify(res);
                        mShared_common_pref.save(Constants.DB_TWO_GET_NOTIFY, gson.toJson(response.body()));
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    call.cancel();
                }
            });
        } else {
            Type userType = new com.google.common.reflect.TypeToken<JsonArray>() {
            }.getType();
            JsonArray arr = (gson.fromJson(mShared_common_pref.getvalue(Constants.DB_TWO_GET_NOTIFY), userType));
            assignGetNotify(arr);
        }
    }

    void assignGetNotify(JsonArray res) {
        TextView txt = findViewById(R.id.MRQtxt);
        txt.setText("");
        txt.setVisibility(View.GONE);
        String sMsg = "";
        for (int il = 0; il < res.size(); il++) {
            JsonObject Itm = res.get(il).getAsJsonObject();
            sMsg += Itm.get("NtfyMsg").getAsString();
        }
        if (!sMsg.equalsIgnoreCase("")) {
            txt.setText(Html.fromHtml(sMsg));
            txt.setVisibility(View.VISIBLE);
            txt.setSelected(true);
        }
    }
    public void LoadingMaterials(){
        common_class.ProgressdialogShow(1, "");
        common_class.getPOSProduct(getActiveActivity(), new OnLiveUpdateListener() {
            @Override
            public void onUpdate(String mode) {
                common_class.CommonIntentwithNEwTask(POSActivity.class);
                activeActivity.overridePendingTransition(R.anim.in, R.anim.out);
                common_class.ProgressdialogShow(0, "");
            }

            @Override
            public void onError(String msg) {
                RetryLoadingProds();
                common_class.ProgressdialogShow(0, "");
            }
        });
    }
    public void RetryLoadingProds(){
        AlertDialogBox.showDialog(getApplicationContext(), "HAP SFA", "Product Loading Failed. Do you want to Retry ?", "Retry", "Cancel", false, new AlertBox() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                if (common_class.isNetworkAvailable(getApplicationContext())) {
                    LoadingMaterials();
                    dialog.dismiss();
                }
            }
            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }
    void setMenuAdapter() {
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 4);
        rvMenu.setLayoutManager(manager);
        //  rvMenu.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        menuAdapter = new MenuAdapter(this, menuList, new AdapterOnClick() {
            @Override
            public void onIntentClick(int pos) {
                switch (menuList.get(pos).getFormName()) {
                    case "Nearby Outlets":
                        Intent intent = new Intent(SFA_Activity.this, Nearby_Outlets.class);
                        intent.putExtra("menu", "menu");
                        startActivity(intent);
                        break;
                    case "GRN":
                        common_class.CommonIntentwithNEwTask(GrnListActivity.class);
                        break;
                    case "Counter Sales":
                        LoadingMaterials();
                       // common_class.CommonIntentwithNEwTask(POSActivity.class);
                        break;
                    case "Primary Order":
                        common_class.getDb_310Data(Constants.PrimaryTAXList, SFA_Activity.this);
                        break;
                    case "Secondary Order":
                        sharedCommonPref.save(Shared_Common_Pref.DCRMode, "SC");
                        Shared_Common_Pref.SFA_MENU = "Dashboard_Route";
                        startActivity(new Intent(SFA_Activity.this, Dashboard_Route.class));

                        break;

                    case "Sales Return":
                        sharedCommonPref.save(Shared_Common_Pref.DCRMode, "SR");
                        Shared_Common_Pref.SFA_MENU = "Dashboard_Route";
                        startActivity (new Intent(SFA_Activity.this, Dashboard_Route.class));

                        break;

                    case "Van Sales":
                      //  if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.VAN_STOCK_LOADING)))
                            common_class.getDb_310Data(Constants.VAN_STOCK, SFA_Activity.this);
                            sharedCommonPref.save(Shared_Common_Pref.DCRMode, "Van Sales");


                        break;
                    case "Outlets":
                        common_class.CommonIntentwithNEwTask(Outlet_Info_Activity.class);
                        break;
                    case "Approve Outlets":
                        common_class.CommonIntentwithNEwTask(PendingOutletsCategory.class);
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
                        getStockAuditDetails(SFA_Activity.this);
                        break;
                    case "Sync":
                        saveFormData(-1);
                        common_class.ProgressdialogShow(1, "");
                        break;
                    case "Inshop":

//                        Toast.makeText(SFA_Activity.this,"Inshop clicked",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SFA_Activity.this, InshopActivity.class));
                        break;

                    default:
                        if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(menuList.get(pos).getFormName())))
                            saveFormData(pos);
                        else
                            navigateFormScreen(pos);


                }
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
        rvMenu.setAdapter(menuAdapter);

    }

    void navigateFormScreen(int pos) {
        Intent ii = new Intent(SFA_Activity.this, ViewActivity.class);
        ii.putExtra("btn_need", menuList.get(pos).getTargetForm());
        ii.putExtra("frmid", menuList.get(pos).getFormid());
        ii.putExtra("frmname", menuList.get(pos).getFormName());
        startActivity(ii);

    }

    private void saveFormData(int pos) {
        try {


            ArrayList<Common_Model> formList = new ArrayList<>();

            for (int i = 0; i < menuList.size(); i++) {
                if (!menuList.get(i).getFormid().equalsIgnoreCase(""))
                    formList.add(new Common_Model(menuList.get(i).getFormName(), menuList.get(i).getFormid()));
            }

            formList.add(new Common_Model("New Farmer", "2"));

            formList.add(new Common_Model("Existing Farmer", "3"));
            formList.add(new Common_Model("Competitor Activity", "12"));
            formList.add(new Common_Model("General Activities", "8"));


            if (formList.size() == menuItem) {
                common_class.ProgressdialogShow(0, "");
                menuItem = 0;
                if (pos >= 0) {
                    navigateFormScreen(pos);
                }
                return;
            } else {
                String formid = formList.get(menuItem).getName();

                JSONObject json = new JSONObject();

                json.put("slno", formList.get(menuItem).getId());

                String formname = formList.get(menuItem).getName();


                Log.v("printing_sf_code", json.toString());
                Call<ResponseBody> approval = apiService.getView(json.toString());


                approval.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.v("procure:" + formname + ":" + formid + ":", response.body().byteStream() + "");

                            InputStreamReader ip = null;
                            StringBuilder is = new StringBuilder();
                            String line = null;
                            try {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);

                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                }

                                sharedCommonPref.save(formname, is.toString());

                                //   Log.v("PROC:" + formList.get(menuItem).getName() + ":", sharedCommonPref.getvalue(formid));
                                menuItem = menuItem + 1;
                                saveFormData(pos);
//
                                Log.v("sizeCheck:", "" + formList.size() + ":current:" + menuItem);


                            } catch (Exception e) {
                                common_class.ProgressdialogShow(0, "");

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        call.cancel();
                        common_class.ProgressdialogShow(0, "");

                    }
                });


            }


            //  }
        } catch (Exception e) {

        }
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
                        call.cancel();

                        Log.v("Projec_Product_List_ex", t.getMessage());
                        t.printStackTrace();
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
                        call.cancel();

                        Log.v("Projec_Product_List_ex", t.getMessage());
                        t.printStackTrace();
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
                        call.cancel();

                        Log.v("Projec_Product_List_ex", t.getMessage());
                        t.printStackTrace();
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
                        call.cancel();
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
            //  Map<String, String> jParam = new HashMap<>();

            //  JSONObject jParam = new JSONObject();
            try {
//                jParam.put("SF", UserDetails.getString("Sfcode", ""));
//                jParam.put("Stk", sharedCommonPref.getvalue(Constants.Distributor_Id));
//                // jParam.put("outletId", Shared_Common_Pref.OutletCode);
//                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getStockAudit("get/auditprodgroup", UserDetails.getString("Divcode", "")).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {
                            Log.v("stockAudit_grp_List", response.body().toString());
                            db.deleteMasterData(Constants.StockAudit_GroupsList);
                            db.addMasterData(Constants.StockAudit_GroupsList, response.body().toString());
                        } catch (Exception e) {
                            Log.v("StockAudit:Ex:catch", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        call.cancel();
                        Log.v("StockAudit:Ex", t.getMessage());
                    }
                });
                service.getStockAudit("get/auditprodtypes", UserDetails.getString("Divcode", "")).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("stockAudit_type_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_Types_List);
                        db.addMasterData(Constants.StockAudit_Types_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        call.cancel();

                    }
                });
                service.getStockAudit("get/auditprodcate", UserDetails.getString("Divcode", "")).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("stockAudit_cat_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_Category_List);
                        db.addMasterData(Constants.StockAudit_Category_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        call.cancel();

                    }
                });
                service.getStockAudit("get/auditproddets", UserDetails.getString("Divcode", "")).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("stockAudit_Product_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_Product_List);
                        db.addMasterData(Constants.StockAudit_Product_List, response.body());

                        common_class.CommonIntentwithNEwTask(StockAuditCategorySelectActivity.class);

                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        call.cancel();
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
        ivProcureSync.setOnClickListener(this);
        rlActivity.setOnClickListener(this);

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
                        call.cancel();

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
        ivProcureSync = (ImageView) findViewById(R.id.ivProcureSync);


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

        tv_tot_sku=findViewById(R.id.tv_tot_sku);
        tv_tot_qty=findViewById(R.id.tv_tot_qty);
        tv_tot_value=findViewById(R.id.tv_tot_value);
        tv_tot_saleret=findViewById(R.id.tv_tot_saleret);
        tv_tot_norder=findViewById(R.id.tv_tot_noorder);
        tv_tot_saleretval=findViewById(R.id.tv_tot_saleretval);

        tv_vantot_sku=findViewById(R.id.tv_vantot_sku);
        tv_vantot_qty=findViewById(R.id.tv_vantot_qty);
        tv_vantot_value=findViewById(R.id.tv_vantot_value);
        tv_tot_vansale=findViewById(R.id.tv_tot_vansale);
        tv_vantot_norder=findViewById(R.id.tv_vantot_noorder);
        tv_tot_vanorder=findViewById(R.id.tv_tot_vanorder);
        tv_vantot_saleret=findViewById(R.id.tv_vantot_saleret);
        tv_vantot_saleretval=findViewById(R.id.tv_vantot_saleretval);

        tv_postot_sku=findViewById(R.id.tv_postot_sku);
        tv_postot_qty=findViewById(R.id.tv_postot_qty);
        tv_postot_value=findViewById(R.id.tv_postot_value);
        tv_postot_order=findViewById(R.id.tv_tot_order);
        tv_postot_saleret=findViewById(R.id.tv_postot_saleret);
        tv_postot_saleretval=findViewById(R.id.tv_postot_saleretval);
        cv_secord_det=findViewById(R.id.cv_secondary_details);
        cv_vansale_det=findViewById(R.id.cv_vansales_details);
        cv_countersale_det=findViewById(R.id.cv_countersales_details);
        rlActivity=findViewById(R.id.rl_activity);
        tvActivity=findViewById(R.id.tvActivity);
        ll_activity=findViewById(R.id.ll_activity);




        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        getActivityDetails();

        if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.CHECKIN_TYPE)){
          /*  cv_countersale_det.setVisibility(View.VISIBLE);
            cv_secord_det.setVisibility(View.VISIBLE);
            cv_vansale_det.setVisibility(View.VISIBLE);
            getTodaySecondaryData();
            getTodayCounterSalesData();
            getTodayVanSalesData();*/
            cv_countersale_det.setVisibility(View.GONE);
            cv_secord_det.setVisibility(View.GONE);
            cv_vansale_det.setVisibility(View.GONE);
            ll_activity.setVisibility(View.VISIBLE);
        }else{
            cv_countersale_det.setVisibility(View.GONE);
            cv_secord_det.setVisibility(View.GONE);
            cv_vansale_det.setVisibility(View.GONE);
            ll_activity.setVisibility(View.GONE);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProcureSync:
                saveFormData(-1);
                common_class.ProgressdialogShow(1, "");

                break;

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
            case R.id.rl_activity:
                Log.e("cbvn","adss");
                common_class.showCommonDialog(activityList, 17, this);
                break;
            case R.id.toolbar_home:
                AlertDialogBox.showDialog(SFA_Activity.this, "HAP SFA", "Are You Sure Want to Logout?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        sharedCommonPref.save("ActivityStart", "false");

                        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                        sharedCommonPref.clear_pref(Constants.Distributor_name);
                        sharedCommonPref.clear_pref(Constants.Distributor_Id);
                        if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.CHECKIN_TYPE) && CheckIn) {
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
                            setAppLogos();
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
                Shared_Common_Pref.SFA_MENU = "Dashboard_Route";
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

    private void getOfferNotify() {
        if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.DB_Offer_NOTIFY))) {
            Map<String, String> QueryString = new HashMap<>();
            QueryString.put("axn", "get/offernotify");
            QueryString.put("CusCode", UserDetails.getString("Sfcode", ""));
            QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> rptCall = apiInterface.getDataArrayList(QueryString, null);
            rptCall.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        JsonArray res = response.body();
                        Log.d("getOfferNotify", String.valueOf(response.body()));
                        //  Log.d("NotifyMsg", response.body().toString());
                        JSONArray sArr = new JSONArray(String.valueOf(response.body()));
                        assignOffGetNotify(sArr);
                        sharedCommonPref.save(Constants.DB_Offer_NOTIFY, gson.toJson(response.body()));
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    call.cancel();

                    Log.d("Tag", String.valueOf(t));
                }
            });

        } else {
            try {
                JSONArray sArr = new JSONArray(String.valueOf(sharedCommonPref.getvalue(Constants.DB_Offer_NOTIFY)));
                //assignOffGetNotify(sArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void assignOffGetNotify(JSONArray res) {
        JSONArray fRes = res;
        if (fRes.length() > 0) {
            LinearLayoutManager TypgridlayManager = new LinearLayoutManager(this);
            TypgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ryclOffers.setLayoutManager(TypgridlayManager);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(ryclOffers);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ryclOffers.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        LinearLayoutManager layoutManager = ((LinearLayoutManager) ryclOffers.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        lblSlideNo.setText((firstVisiblePosition + 1) + "/" + fRes.length());
                    }
                });
            } else {
                ryclOffers.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        LinearLayoutManager layoutManager = ((LinearLayoutManager) ryclOffers.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        lblSlideNo.setText((firstVisiblePosition + 1) + "/" + fRes.length());
                    }
                });
            }
            OffersAdapter TyplistItems = new OffersAdapter(fRes, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {
                    try {
                        //GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", item.getString("id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            ryclOffers.setAdapter(TyplistItems);
            linOffer.setVisibility(View.VISIBLE);

            sharedCommonPref.save(Constants.DB_OfferShownOn, Common_Class.GetDatewothouttime());
        }
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
                    case VAN_STOCK:
                        try {
                            JSONObject stkObj = new JSONObject(apiDataResponse);

                            if (stkObj.getBoolean("success")) {
                                JSONArray arr = stkObj.getJSONArray("Data");
                                List<Product_Details_Modal> stkList = new ArrayList<>();
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    stkList.add(new Product_Details_Modal(obj.getString("PCode"), obj.getInt("Cr"), obj.getInt("Dr"), (obj.getInt("Bal"))));
                                }

                                sharedCommonPref.save(Constants.VAN_STOCK_LOADING, gson.toJson(stkList));
                                sharedCommonPref.save(Constants.VAN_STOCK_LOADING_TIME, Common_Class.GetDateOnly());

                            }
                            Shared_Common_Pref.SFA_MENU = "VanSalesDashboardRoute";
                            startActivity(new Intent(SFA_Activity.this, VanSalesDashboardRoute.class));
                            Log.v(key, apiDataResponse);
                        }catch (Exception e){

                        }
                        break;
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
                        if (UserDetails.getString("DeptType", "").equalsIgnoreCase("1"))
                            common_class.CommonIntentwithoutFinish(ProcPrimaryOrderActivity.class);
                        else
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
            Log.v(key + "Ex:", e.getMessage());

        }
    }

    void getPrimaryData(String groupType) {
        JsonObject data = new JsonObject();
        data.addProperty("Grpcode", groupType);
        common_class.getDb_310Data(Constants.PRIMARY_DASHBOARD, SFA_Activity.this, data);
    }

    public void callDynamicmenu() {
        JSONObject json = new JSONObject();
        try {
            json.put("div", UserDetails.getString("Divcode", ""));

            Log.v("printing_sf_code", json.toString());
            Call<ResponseBody> approval = apiService.getMenu(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            menuList.clear();
                            menuList.add(new ListModel("", "Primary Order", "", "", "", R.drawable.ic_outline_add_chart_48));
                            Log.v("printing_dynamic_menu", is.toString());
                            JSONArray js = new JSONArray(is.toString());
                            for (int i = 0; i < js.length(); i++) {
                                JSONObject jj = js.getJSONObject(i);
                                menuList.add(new ListModel(jj.getString("Frm_ID"), jj.getString("Frm_Name"), jj.getString("Frm_Table"), jj.getString("Targt_Frm"), jj.getString("Frm_Type"), R.drawable.ic_outline_assignment_48));
                            }
                            //  menuList.add(new ListModel("", "Sync", "", "", "", R.drawable.ic_round_sync_24));


                            sharedCommonPref.save(Constants.PROCUR_MENU, gson.toJson(menuList));

                            setMenuAdapter();

                        } catch (Exception e) {
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();

                }
            });

        } catch (Exception e) {
        }
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
        AdapterOnClick adapterOnClick;
        private List<ListModel> listt;

        public MenuAdapter(Context applicationContext, List<ListModel> list, AdapterOnClick adapterOnClick) {
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
                    holder.tvName.setText("" + listt.get(position).getFormName());
                    holder.ivIcon.setImageResource(listt.get(position).getIcon());

                    holder.llParent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapterOnClick.onIntentClick(position);
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

    private void  getTodaySecondaryData(){
        try {

            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                JSONObject HeadItem = new JSONObject();
                HeadItem.put("sfCode",Shared_Common_Pref.Sf_Code);
                String div_code = Shared_Common_Pref.Div_Code.replaceAll(",", "");
                HeadItem.put("divisionCode", div_code);



                Call<ResponseBody> call = service.getTotalSecondaryData(Shared_Common_Pref.Sf_Code,HeadItem.toString());
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


                                int sku=0;
                                int qty=0;
                                double value=0;
                                int  salretCnt=0;
                                double saleretValue=0;
                                int noOrderCnt=0;


                                JSONObject data = new JSONObject(is.toString());

                                JSONArray secInv = data.getJSONArray("SecInv");
                                if(secInv.length()>0) {
                                    JSONObject object= secInv.getJSONObject(0);
                                    if(object.has("Item")){
                                        sku=object.getInt("Item");
                                    }
                                    if(object.has("TotQty")){
                                        qty=object.getInt("TotQty");
                                    }
                                    if(object.has("TotValue")){
                                        value=object.getDouble("TotValue");
                                    }

                                }
                                JSONArray saleReturn = data.getJSONArray("SaleReturn");
                                if(saleReturn.length()>0){
                                    JSONObject saleReturnObj = saleReturn.getJSONObject(0);
                                    if(saleReturnObj.has("retCnt")){
                                        salretCnt=saleReturnObj.getInt("retCnt");
                                    }
                                    if(saleReturnObj.has("saleRetVal")){
                                        saleretValue=saleReturnObj.getDouble("saleRetVal");
                                    }

                                }


                                JSONArray noOrder = data.getJSONArray("NoOrder");
                                if(noOrder.length()>0) {
                                    JSONObject noOrderObj = noOrder.getJSONObject(0);
                                    if(noOrderObj.has("noOrderCnt")){
                                        noOrderCnt=noOrderObj.getInt("noOrderCnt");
                                    }
                                }

                                tv_tot_sku.setText("" + sku);
                                tv_tot_qty.setText("" + qty);
                                tv_tot_value.setText("" + CurrencySymbol + " " + formatter.format(value));
                                tv_tot_saleret.setText(""+salretCnt);
                                tv_tot_saleretval.setText("" + CurrencySymbol + " " + formatter.format(saleretValue));
                                tv_tot_norder.setText(""+noOrderCnt);
                                cv_secord_det.setVisibility(View.VISIBLE);
                                cv_countersale_det.setVisibility(View.GONE);
                                cv_vansale_det.setVisibility(View.GONE);
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
                common_class.showMsg(VanSalesDashboardRoute.dashboard_route, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }
    private void  getTodayCounterSalesData(){
        try {

            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                JSONObject HeadItem = new JSONObject();
                HeadItem.put("sfCode",Shared_Common_Pref.Sf_Code);
                String div_code = Shared_Common_Pref.Div_Code.replaceAll(",", "");
                HeadItem.put("divisionCode", div_code);



                Call<ResponseBody> call = service.getTotalCounterSalesData(Shared_Common_Pref.Sf_Code,HeadItem.toString());
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


                                int sku=0;
                                int qty=0;
                                double value=0;
                                int billCnt=0;
                                int  salretCnt=0;
                                double saleretValue=0;



                                JSONObject data = new JSONObject(is.toString());

                                JSONArray countSale = data.getJSONArray("CounterSale");
                                if(countSale.length()>0) {
                                    JSONObject object= countSale.getJSONObject(0);
                                    if(object.has("Item")){
                                        sku=object.getInt("Item");
                                    }
                                    if(object.has("TotQty")){
                                        qty=object.getInt("TotQty");
                                    }
                                    if(object.has("TotValue")){
                                        value=object.getDouble("TotValue");
                                    }
                                    if(object.has("billCnt")){
                                        billCnt=object.getInt("billCnt");
                                    }
                                }
                                JSONArray saleReturn = data.getJSONArray("PosReturn");
                                if(saleReturn.length()>0){
                                    JSONObject saleReturnObj = saleReturn.getJSONObject(0);
                                    if(saleReturnObj.has("retCnt")){
                                        salretCnt=saleReturnObj.getInt("retCnt");
                                    }
                                    if(saleReturnObj.has("saleRetVal")){
                                        saleretValue=saleReturnObj.getDouble("saleRetVal");
                                    }

                                }




                                tv_postot_sku.setText("" + sku);
                                tv_postot_qty.setText("" + qty);
                                tv_postot_value.setText("" + CurrencySymbol + " " + formatter.format(value));
                                tv_postot_order.setText(""+billCnt);
                                tv_postot_saleret.setText(""+salretCnt);
                                tv_postot_saleretval.setText("" + CurrencySymbol + " " + formatter.format(saleretValue));
                                cv_secord_det.setVisibility(View.GONE);
                                cv_countersale_det.setVisibility(View.VISIBLE);
                                cv_vansale_det.setVisibility(View.GONE);

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
                common_class.showMsg(VanSalesDashboardRoute.dashboard_route, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }
    private void  getTodayVanSalesData(){
        try {

            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                JSONObject HeadItem = new JSONObject();
                HeadItem.put("sfCode",Shared_Common_Pref.Sf_Code);
                String div_code = Shared_Common_Pref.Div_Code.replaceAll(",", "");
                HeadItem.put("divisionCode", div_code);



                Call<ResponseBody> call = service.getTotalVanSalesData(Shared_Common_Pref.Sf_Code,HeadItem.toString());
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


                                int sku=0;
                                int qty=0;
                                double value=0;
                                int  salretCnt=0;
                                double saleretValue=0;
                                int noOrderCnt=0;
                                int vanOrderCnt=0;
                                int vanInvCnt=0;


                                JSONObject data = new JSONObject(is.toString());

                                JSONArray secInv = data.getJSONArray("VanInv");
                                if(secInv.length()>0) {
                                    JSONObject object= secInv.getJSONObject(0);
                                    if(object.has("Item")){
                                        sku=object.getInt("Item");
                                    }
                                    if(object.has("TotQty")){
                                        qty=object.getInt("TotQty");
                                    }
                                    if(object.has("TotValue")){
                                        value=object.getDouble("TotValue");
                                    }
                                    if(object.has("billCnt")){
                                        vanInvCnt=object.getInt("billCnt");
                                    }

                                }
                                JSONArray saleReturn = data.getJSONArray("VanReturn");
                                if(saleReturn.length()>0){
                                    JSONObject saleReturnObj = saleReturn.getJSONObject(0);
                                    if(saleReturnObj.has("retCnt")){
                                        salretCnt=saleReturnObj.getInt("retCnt");
                                    }
                                    if(saleReturnObj.has("saleRetVal")){
                                        saleretValue=saleReturnObj.getDouble("saleRetVal");
                                    }

                                }


                                JSONArray noOrder = data.getJSONArray("NoOrder");
                                if(noOrder.length()>0) {
                                    JSONObject noOrderObj = noOrder.getJSONObject(0);
                                    if(noOrderObj.has("noOrderCnt")){
                                        noOrderCnt=noOrderObj.getInt("noOrderCnt");
                                    }
                                }

                                JSONArray vanOrder = data.getJSONArray("VanOrder");
                                if(vanOrder.length()>0) {
                                    JSONObject vanOrderObj = vanOrder.getJSONObject(0);
                                    if(vanOrderObj.has("ordCnt")){
                                        vanOrderCnt=vanOrderObj.getInt("ordCnt");
                                    }
                                }

                                tv_vantot_sku.setText("" + sku);
                                tv_vantot_qty.setText("" + qty);
                                tv_vantot_value.setText("" + CurrencySymbol + " " + formatter.format(value));
                                tv_vantot_saleret.setText(""+salretCnt);
                                tv_vantot_saleretval.setText("" + CurrencySymbol + " " + formatter.format(saleretValue));
                                tv_vantot_norder.setText(""+noOrderCnt);
                                tv_tot_vanorder.setText(""+vanOrderCnt);
                                tv_tot_vansale.setText(""+vanInvCnt);
                                cv_secord_det.setVisibility(View.GONE);
                                cv_countersale_det.setVisibility(View.GONE);
                                cv_vansale_det.setVisibility(View.VISIBLE);

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
                common_class.showMsg(VanSalesDashboardRoute.dashboard_route, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }
    public void getActivityDetails(){
        String orderList = "[{\"id\":\"1\",\"name\":\"Secondary Sales\"},{\"id\":\"2\",\"name\":\"VanSales\"},{\"id\":\"3\",\"name\":\"CounterSales\"}]";
        if (orderList != null && !orderList.equals("")) {
            activityList.clear();
            try {
                JSONArray jsonArray = new JSONArray(orderList);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Common_Model selectionModel = new Common_Model(jsonObject.getString("name"), jsonObject.getString("id"));
                    activityList.add(selectionModel);
                }

                Log.e( "getpaymentModeList: "  ,""+activityList.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 17:
                tvActivity.setText(myDataset.get(position).getName());
                if (Integer.parseInt(myDataset.get(position).getId()) == 1) {
                    getTodaySecondaryData();
                  /*  cv_secord_det.setVisibility(View.VISIBLE);
                    cv_countersale_det.setVisibility(View.GONE);
                    cv_vansale_det.setVisibility(View.GONE);*/
                } else if (Integer.parseInt(myDataset.get(position).getId()) == 2) {
                    getTodayVanSalesData();
                    /*cv_secord_det.setVisibility(View.GONE);
                    cv_countersale_det.setVisibility(View.GONE);
                    cv_vansale_det.setVisibility(View.VISIBLE);*/
                } else if (Integer.parseInt(myDataset.get(position).getId()) == 3) {
                    getTodayCounterSalesData();
                   /* cv_secord_det.setVisibility(View.GONE);
                    cv_countersale_det.setVisibility(View.VISIBLE);
                    cv_vansale_det.setVisibility(View.GONE);*/
                    break;

                }
        }
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
