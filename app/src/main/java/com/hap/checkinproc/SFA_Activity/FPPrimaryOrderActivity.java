package com.hap.checkinproc.SFA_Activity;

import static com.hap.checkinproc.Common_Class.Constants.Rout_List;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.SFA_Activity;
import com.hap.checkinproc.BuildConfig;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.Interface.onListItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.RyclBrandListItemAdb;
import com.hap.checkinproc.SFA_Adapter.RyclListItemAdb;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FPPrimaryOrderActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI, Master_Interface {

    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Product_Details_Modal> freeQty_Array_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    CircularProgressButton takeorder, btnRepeat;
    TextView distributor_text, Category_Nametext,
            tvTimer, txBalAmt, txAmtWalt, txAvBal, tvDistId, tvDate, tvDeliveryDate, route_text;
    LinearLayout lin_orderrecyclerview, lin_gridcategory, rlAddProduct, llTdPriOrd, btnRefACBal, llDistributor, btnCmbRoute;
    Common_Class common_class;
    String Ukey;
    String[] strLoc;
    String Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "";
    Shared_Common_Pref sharedCommonPref;
    Prodct_Adapter mProdct_Adapter;
    String TAG = "PRIMARY_ORDER";
    DatabaseHandler db;
    RelativeLayout rlCategoryItemSearch, balDetwin;
    ImageView ivClose, btnClose;
    EditText etCategoryItemSearch;
    double cashDiscount;
    boolean bRmRow = false;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private RecyclerView recyclerView, categorygrid, freeRecyclerview, Grpgrid, Brndgrid;
    private int selectedPos = 0;
    private TextView tvTotalAmount, tvACBal, tvTotalItems;
    private double totalvalues, taxVal;
    private Integer totalQty;
    private TextView tvBillTotItem;
    double ACBalance = 0.0;
    final Handler handler = new Handler();
    private DatePickerDialog fromDatePickerDialog;
    com.hap.checkinproc.Activity_Hap.Common_Class DT = new com.hap.checkinproc.Activity_Hap.Common_Class();
    public static final String UserDetail = "MyPrefs";
    SharedPreferences UserDetails;
    private ArrayList<Product_Details_Modal> orderTotTax;
    String orderId = "";
    Common_Model Model_Pojo;
    List<Common_Model> FRoute_Master = new ArrayList<>();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fp_primary_order_layout);
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(FPPrimaryOrderActivity.this);
            UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
            common_class = new Common_Class(this);
            common_class.getProductDetails(this);

            categorygrid = findViewById(R.id.category);
            Grpgrid = findViewById(R.id.PGroup);
            Brndgrid = findViewById(R.id.PBrnd);
            takeorder = findViewById(R.id.takeorder);
            tvTotalItems = findViewById(R.id.tvTotalItems);
            tvTotalAmount = findViewById(R.id.tvTotalAmount);

            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            lin_orderrecyclerview = findViewById(R.id.lin_orderrecyclerview);
            lin_gridcategory = findViewById(R.id.lin_gridcategory);
            distributor_text = findViewById(R.id.distributor_text);
            llDistributor = findViewById(R.id.llDistributor);
            btnCmbRoute = findViewById(R.id.btnCmbRoute);
            Category_Nametext = findViewById(R.id.Category_Nametext);
            rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
            rlAddProduct = findViewById(R.id.rlAddProduct);
            ivClose = findViewById(R.id.ivClose);
            llTdPriOrd = findViewById(R.id.llTodayPriOrd);
            tvACBal = findViewById(R.id.tvACBal);
            txAvBal = findViewById(R.id.txAvBal);
            txAmtWalt = findViewById(R.id.txAmtWalt);
            txBalAmt = findViewById(R.id.txBalAmt);
            btnRefACBal = findViewById(R.id.btnRefACBal);
            balDetwin = findViewById(R.id.balDetwin);
            btnClose = findViewById(R.id.btnClose);
            tvDistId = findViewById(R.id.tvDistId);
            tvDate = findViewById(R.id.tvDate);
            tvDeliveryDate = findViewById(R.id.tvDeliveryDate);
            btnRepeat = findViewById(R.id.btnRepeat);
            route_text = findViewById(R.id.route_text);

            getACBalance(0);
            etCategoryItemSearch = findViewById(R.id.searchView);
            tvTimer = findViewById(R.id.tvTimer);
            Product_ModalSetAdapter = new ArrayList<>();
            gson = new Gson();
            userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
            }.getType();
            takeorder.setOnClickListener(this);
            rlCategoryItemSearch.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            rlAddProduct.setOnClickListener(this);
            llTdPriOrd.setOnClickListener(this);
            Category_Nametext.setOnClickListener(this);
            tvDeliveryDate.setOnClickListener(this);
            btnRepeat.setOnClickListener(this);
            llDistributor.setOnClickListener(this);
            btnCmbRoute.setOnClickListener(this);

            Ukey = Common_Class.GetEkey();
            recyclerView = findViewById(R.id.orderrecyclerview);
            freeRecyclerview = findViewById(R.id.freeRecyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categorygrid.setLayoutManager(layoutManager);

            common_class.getDb_310Data(Constants.Primary_Product_List, this);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            common_class.getDb_310Data(Constants.PRIMARY_SCHEME, this);
            tvACBal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    balDetwin.setVisibility(View.VISIBLE);
                }
            });
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    balDetwin.setVisibility(View.GONE);
                }
            });
            btnRefACBal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getACBalance(0);
                }
            });
            etCategoryItemSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    showOrderItemList(selectedPos, s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            handler.postDelayed(new Runnable() {
                public void run() {
                    tvTimer.setText(Common_Class.GetTime() + "   /   " + sharedCommonPref.getvalue(Constants.CUTOFF_TIME));
                    handler.postDelayed(this, 1000);
                }
            }, 1000);

            tvDistId.setText("" + sharedCommonPref.getvalue(Constants.DistributorERP));
            tvDate.setText(DT.GetDateTime(getApplicationContext(), "dd-MMM-yyyy"));
            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6", "");
            orderId = getIntent().getStringExtra(Constants.ORDER_ID);

            if (!sharedCommonPref.getvalue(Constants.Distributor_Id).equals("")) {
                common_class.getDb_310Data(Rout_List, this);
                distributor_text.setText(/*"Hi! " +*/ sharedCommonPref.getvalue(Constants.Distributor_name, ""));

            } else {
                btnCmbRoute.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Log.v(TAG, " order oncreate: " + e.getMessage());
        }
    }

    private void getACBalance(int Mode) {
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("StkERP", sharedCommonPref.getvalue(Constants.DistributorERP));
            tvACBal.setText("₹0.00");
            txBalAmt.setText("₹0.00");
            txAmtWalt.setText("₹0.00");
            txAvBal.setText("₹0.00");

            ApiClient.getClient().create(ApiInterface.class)
                    .getDataArrayList("get/custbalance", jParam.toString())
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            try {
                                JsonArray res = response.body();
                                JsonObject jItem = res.get(0).getAsJsonObject();
                                double ActBAL = 0;
                                try {
                                    ActBAL = jItem.get("LC_BAL").getAsDouble();
                                } catch (Exception e) {
                                    common_class.showMsg(FPPrimaryOrderActivity.this, e.getMessage());

                                }
                                ACBalance = jItem.get("Balance").getAsDouble();
                                if (ACBalance <= 0) ACBalance = Math.abs(ACBalance);
                                else ACBalance = 0 - ACBalance;
                                if (ActBAL <= 0) ActBAL = Math.abs(ActBAL);
                                else ActBAL = 0 - ActBAL;
                                NumberFormat format1 = NumberFormat.getCurrencyInstance(new Locale("en", "in"));

                                // tvACBal.setText("₹" + new DecimalFormat("##0.00").format(ACBalance));
                                tvACBal.setText(format1.format(ACBalance));
                                txBalAmt.setText("₹" + new DecimalFormat("##0.00").format(ACBalance));
                                txAmtWalt.setText("₹" + new DecimalFormat("##0.00").format(jItem.get("Pending").getAsDouble()));
                                txAvBal.setText("₹" + new DecimalFormat("##0.00").format(ActBAL));
                                if (Mode == 1) {
                                    SubmitPrimaryOrder();
                                }
                            } catch (Exception e) {
                                common_class.showMsg(FPPrimaryOrderActivity.this, e.getMessage());
                                ResetSubmitBtn(0);
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {

                            common_class.showMsg(FPPrimaryOrderActivity.this, t.getMessage());
                            ResetSubmitBtn(0);
                            Log.d("InvHistory", String.valueOf(t));
                        }
                    });
        } catch (JSONException e) {

            ResetSubmitBtn(0);
        }
    }

    private void FilterTypes(String GrpID) {
        try {
            JSONArray TypGroups = new JSONArray();
            JSONArray tTypGroups = db.getMasterData(Constants.ProdTypes_List);
            LinearLayoutManager TypgridlayManager = new LinearLayoutManager(this);
            TypgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            Brndgrid.setLayoutManager(TypgridlayManager);
            for (int i = 0; i < tTypGroups.length(); i++) {

                JSONObject ritm = tTypGroups.getJSONObject(i);
                if (ritm.getString("GroupId").equalsIgnoreCase(GrpID)) {
                    TypGroups.put(ritm);
                }
            }

            String filterId = "";
            if (TypGroups.length() > 0)
                filterId = TypGroups.getJSONObject(0).getString("id");

            RyclBrandListItemAdb TyplistItems = new RyclBrandListItemAdb(TypGroups, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {
                    try {
                        GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", item.getString("id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            Brndgrid.setAdapter(TyplistItems);

            GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", filterId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetJsonData(String jsonResponse, String type, String filter) {

        //type =1 product category data values
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            Category_Modal.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (type.equals("1")) {
                    String id = String.valueOf(jsonObject1.optInt("id"));
                    String name = jsonObject1.optString("name");
                    String Division_Code = jsonObject1.optString("Division_Code");
                    String Cat_Image = jsonObject1.optString("Cat_Image");
                    String sampleQty = jsonObject1.optString("sampleQty");
                    String colorflag = jsonObject1.optString("colorflag");
                    String typeId = String.valueOf(jsonObject1.optInt("TypID"));
                    if (filter.equalsIgnoreCase(typeId))
                        Category_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));
                } else {
                    Route_Code = jsonObject1.optString("cluster");
                    Dirtributor_Cod = jsonObject1.optString("stockist");
                    Worktype_code = jsonObject1.optString("wtype");
                    Distributor_Name = jsonObject1.optString("StkName");
                }
            }

            if (type.equals("1")) {
                selectedPos = 0;
                FPPrimaryOrderActivity.CategoryAdapter customAdapteravail = new FPPrimaryOrderActivity.CategoryAdapter(getApplicationContext(),
                        Category_Modal);
                categorygrid.setAdapter(customAdapteravail);
                if (Common_Class.isNullOrEmpty(orderId)) {
                    showOrderItemList(selectedPos, "");
                } else {
                    orderId = "";
                    common_class.getDataFromApi(Constants.TodayPrimaryOrderDetails_List, this, false);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void showOrderList() {

        Getorder_Array_List = new ArrayList<>();
        Getorder_Array_List.clear();

        for (int pm = 0; pm < Product_Modal.size(); pm++) {

            if (Product_Modal.get(pm).getQty() > 0) {
                Getorder_Array_List.add(Product_Modal.get(pm));


            }
        }

        if (Getorder_Array_List.size() == 0)
            Toast.makeText(getApplicationContext(), "Order is empty", Toast.LENGTH_SHORT).show();
        else
            FilterProduct();

    }

    public void SubmitPrimaryOrder() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date d1 = sdf.parse(Common_Class.GetTime());
            Date d2 = sdf.parse(sharedCommonPref.getvalue(Constants.CUTOFF_TIME));
            long elapsed = d2.getTime() - d1.getTime();
            if (ACBalance >= totalvalues) {
                if (elapsed >= 0) {
                    String sLoc = sharedCommonPref.getvalue("CurrLoc");
                    if (sLoc.equalsIgnoreCase("")) {
                        new LocationFinder(getApplication(), new LocationEvents() {
                            @Override
                            public void OnLocationRecived(Location location) {
                                strLoc = (location.getLatitude() + ":" + location.getLongitude()).split(":");
                                SaveOrder();
                            }
                        });
                    } else {
                        strLoc = sLoc.split(":");
                        SaveOrder();
                    }

                } else {
                    ResetSubmitBtn(0);
                    common_class.showMsg(this, "Time UP...");
                }
            } else {
                ResetSubmitBtn(0);
                common_class.showMsg(this, "Low A/C Balance...");
            }
        } catch (Exception e) {
            common_class.showMsg(this, e.getMessage());
            ResetSubmitBtn(0);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedCommonPref.clear_pref(Constants.TodayPrimaryOrderDetails_List);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCmbRoute:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    common_class.showCommonDialog(FRoute_Master, 3, this);
                }
                break;
            case R.id.llDistributor:
                common_class.showCommonDialog(common_class.getDistList(), 2, this);
                break;
            case R.id.tvDeliveryDate:
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(FPPrimaryOrderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;

                        tvDeliveryDate.setText("" + dayOfMonth + "/" + month + "/" + year);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();

                break;
            case R.id.llTodayPriOrd:
                startActivity(new Intent(getApplicationContext(), TodayPrimOrdActivity.class));
                break;
            case R.id.rlAddProduct:
                moveProductScreen();
                break;

            case R.id.Category_Nametext:
                findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
                findViewById(R.id.rlCategoryItemSearch).setVisibility(View.VISIBLE);
                break;
            case R.id.ivClose:
                findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
                findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);
                etCategoryItemSearch.setText("");
                showOrderItemList(selectedPos, "");
                break;

            case R.id.takeorder:
                try {
                    bRmRow = false;
                    if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                        if (Getorder_Array_List != null
                                && Getorder_Array_List.size() > 0) {
                            if (takeorder.isAnimating()) return;
                            takeorder.startAnimation();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getACBalance(1);
                                }
                            }, 500);

                        } else {
                            common_class.showMsg(this, "Your Cart is empty...");
                        }
                    } else {
                        showOrderList();
                    }
                } catch (Exception e) {

                }
                break;
            case R.id.btnRepeat:
                if (btnRepeat.isAnimating()) return;
                btnRepeat.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        common_class.getDataFromApi(Constants.PrePrimaryOrderQty, FPPrimaryOrderActivity.this, false);
                    }
                }, 500);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            String sLoc = sharedCommonPref.getvalue("CurrLoc");
            if (sLoc.equalsIgnoreCase("")) {
                new LocationFinder(getApplication(), new LocationEvents() {
                    @Override
                    public void OnLocationRecived(Location location) {
                        strLoc = (location.getLatitude() + ":" + location.getLongitude()).split(":");
                        SaveOrder();
                    }
                });
            } else {
                strLoc = sLoc.split(":");
                SaveOrder();
            }
        }
    }

    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {
            AlertDialogBox.showDialog(FPPrimaryOrderActivity.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {

                    JSONArray data = new JSONArray();
                    JSONObject ActivityData = new JSONObject();
                    try {
                        JSONObject HeadItem = new JSONObject();
                        HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("Worktype_code", Worktype_code);
                        HeadItem.put("Town_code", sharedCommonPref.getvalue(Constants.Route_Id));
                        HeadItem.put("dcr_activity_date", Common_Class.GetDate());
                        HeadItem.put("Daywise_Remarks", "");
                        HeadItem.put("UKey", Ukey);
                        HeadItem.put("orderValue", formatter.format(totalvalues));
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("AppVer", BuildConfig.VERSION_NAME);
                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                        OutletItem.put("orderValue", formatter.format(totalvalues));
                        OutletItem.put("CashDiscount", cashDiscount);
                        OutletItem.put("NetAmount", formatter.format(totalvalues));
                        OutletItem.put("No_Of_items", tvBillTotItem.getText().toString());
                        OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("ordertype", "order");
                        OutletItem.put("deliveryDate", tvDeliveryDate.getText().toString());
                        OutletItem.put("orderId", getIntent().getStringExtra(Constants.ORDER_ID) == null ? "" : getIntent().getStringExtra(Constants.ORDER_ID));
                        OutletItem.put("mode", getIntent().getStringExtra(Constants.ORDER_ID) == null ? "new" : "edit");
                        OutletItem.put("cutoff_time", sharedCommonPref.getvalue(Constants.CUTOFF_TIME));

                        if (strLoc.length > 0) {
                            OutletItem.put("Lat", strLoc[0]);
                            OutletItem.put("Long", strLoc[1]);
                        } else {
                            OutletItem.put("Lat", "");
                            OutletItem.put("Long", "");
                        }
                        JSONArray Order_Details = new JSONArray();
                        JSONArray totTaxArr = new JSONArray();

                        for (int z = 0; z < Getorder_Array_List.size(); z++) {
                            JSONObject ProdItem = new JSONObject();
                            ProdItem.put("product_Name", Getorder_Array_List.get(z).getName());
                            ProdItem.put("product_code", Getorder_Array_List.get(z).getId());
                            ProdItem.put("Product_ERP", Getorder_Array_List.get(z).getERP_Code());
                            ProdItem.put("Product_Qty", Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_RegularQty", 0);
                            ProdItem.put("Product_Total_Qty", Getorder_Array_List.get(z).getQty()
                            );
                            ProdItem.put("Product_Amount", Getorder_Array_List.get(z).getAmount());
                            ProdItem.put("MRP", Getorder_Array_List.get(z).getMRP());
                            ProdItem.put("Rate", String.format("%.2f", Getorder_Array_List.get(z).getSBRate()));

                            ProdItem.put("free", Getorder_Array_List.get(z).getFree());
                            ProdItem.put("dis", Getorder_Array_List.get(z).getDiscount());
                            ProdItem.put("dis_value", Getorder_Array_List.get(z).getDiscount_value());
                            ProdItem.put("Off_Pro_code", Getorder_Array_List.get(z).getOff_Pro_code());
                            ProdItem.put("Off_Pro_name", Getorder_Array_List.get(z).getOff_Pro_name());
                            ProdItem.put("Off_Pro_Unit", Getorder_Array_List.get(z).getOff_Pro_Unit());
                            ProdItem.put("Off_Scheme_Unit", Getorder_Array_List.get(z).getScheme());
                            ProdItem.put("discount_type", Getorder_Array_List.get(z).getDiscount_type());
                            ProdItem.put("ConversionFactor", Getorder_Array_List.get(z).getConversionFactor());

                            JSONArray tax_Details = new JSONArray();

                            if (Getorder_Array_List.get(z).getProductDetailsModal() != null &&
                                    Getorder_Array_List.get(z).getProductDetailsModal().size() > 0) {

                                for (int i = 0; i < Getorder_Array_List.get(z).getProductDetailsModal().size(); i++) {
                                    JSONObject taxData = new JSONObject();

                                    String label = Getorder_Array_List.get(z).getProductDetailsModal().get(i).getTax_Type();
                                    Double amt = Getorder_Array_List.get(z).getProductDetailsModal().get(i).getTax_Amt();
                                    taxData.put("Tax_Id", Getorder_Array_List.get(z).getProductDetailsModal().get(i).getTax_Id());
                                    taxData.put("Tax_Val", Getorder_Array_List.get(z).getProductDetailsModal().get(i).getTax_Val());
                                    taxData.put("Tax_Type", label);
                                    taxData.put("Tax_Amt", amt);
                                    tax_Details.put(taxData);

                                }


                            }

                            ProdItem.put("TAX_details", tax_Details);

                            Order_Details.put(ProdItem);

                        }

                        for (int i = 0; i < orderTotTax.size(); i++) {
                            JSONObject totTaxObj = new JSONObject();

                            totTaxObj.put("Tax_Type", orderTotTax.get(i).getTax_Type());
                            totTaxObj.put("Tax_Amt", orderTotTax.get(i).getTax_Amt());
                            totTaxArr.put(totTaxObj);

                        }

                        OutletItem.put("TOT_TAX_details", totTaxArr);
                        ActivityData.put("Activity_Doctor_Report", OutletItem);
                        ActivityData.put("Order_Details", Order_Details);
                        data.put(ActivityData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> responseBodyCall = apiInterface.savePrimaryOrder(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                    responseBodyCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    Log.e("JSON_VALUES", response.body().toString());
                                    JSONObject jsonObjects = new JSONObject(response.body().toString());
                                    ResetSubmitBtn(1);
                                    common_class.showMsg(FPPrimaryOrderActivity.this, jsonObjects.getString("Msg"));
                                    if (jsonObjects.getString("success").equals("true")) {
                                        finish();
                                    }

                                } catch (Exception e) {
                                    ResetSubmitBtn(2);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("SUBMIT_VALUE", "ERROR");
                            ResetSubmitBtn(2);
                        }
                    });

                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    ResetSubmitBtn(0);
                }
            });
        } else {

            ResetSubmitBtn(0);
            Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void ResetSubmitBtn(int resetMode) {
        common_class.ProgressdialogShow(0, "");
        long dely = 10;
        if (resetMode != 0) dely = 1000;
        if (resetMode == 1) {
            takeorder.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        } else {
            takeorder.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                takeorder.stopAnimation();
                takeorder.revertAnimation();
                btnRepeat.stopAnimation();
                btnRepeat.revertAnimation();
            }
        }, dely);

    }

    private void FilterProduct() {

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }

        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);
        lin_gridcategory.setVisibility(View.GONE);
        lin_orderrecyclerview.setVisibility(View.VISIBLE);
        takeorder.setText("SUBMIT");
        takeorder.setVisibility(View.INVISIBLE);
        btnRepeat.setVisibility(View.GONE);
        //  sumofTax();
        mProdct_Adapter = new Prodct_Adapter(Getorder_Array_List, R.layout.adapter_primary_pay_layout, getApplicationContext(), -1);
        recyclerView.setAdapter(mProdct_Adapter);
        showFreeQtyList();
    }

    void showFreeQtyList() {
        freeQty_Array_List = new ArrayList<>();
        freeQty_Array_List.clear();

        for (Product_Details_Modal pm : Product_Modal) {


            if (!Common_Class.isNullOrEmpty(pm.getFree()) && !pm.getFree().equals("0")) {
                freeQty_Array_List.add(pm);

            }

        }
        if (freeQty_Array_List != null && freeQty_Array_List.size() > 0) {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.VISIBLE);
            Free_Adapter mFreeAdapter = new Free_Adapter(freeQty_Array_List, R.layout.product_free_recyclerview, getApplicationContext(), -1);
            freeRecyclerview.setAdapter(mFreeAdapter);

        } else {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);

        }

    }

    public void updateToTALITEMUI() {
        TextView tvTotLabel = findViewById(R.id.tvTotLabel);

        TextView tvTax = findViewById(R.id.tvTaxVal);

        TextView tvTaxLabel = findViewById(R.id.tvTaxLabel);

        TextView tvBillSubTotal = findViewById(R.id.subtotal);
        TextView tvSaveAmt = findViewById(R.id.tvSaveAmt);

        tvBillTotItem = findViewById(R.id.totalitem);
        TextView tvBillTotQty = findViewById(R.id.tvtotalqty);
        TextView tvBillToPay = findViewById(R.id.tvnetamount);
        TextView tvCashDiscount = findViewById(R.id.tvcashdiscount);


        Getorder_Array_List = new ArrayList<>();
        Getorder_Array_List.clear();
        totalvalues = 0;
        totalQty = 0;
        cashDiscount = 0;
        taxVal = 0;

        orderTotTax = new ArrayList<>();

        for (int pm = 0; pm < Product_Modal.size(); pm++) {

            if (Product_Modal.get(pm).getQty() > 0) {

                cashDiscount += (double) Product_Modal.get(pm).getDiscount();

                totalvalues += Product_Modal.get(pm).getAmount();

                totalQty += Product_Modal.get(pm).getQty();

                if (Product_Modal.get(pm).getTax() > 0)
                    taxVal += Product_Modal.get(pm).getTax();


                Getorder_Array_List.add(Product_Modal.get(pm));


            }

        }

        tvTotalAmount.setText("₹ " + formatter.format(totalvalues));
        tvTotalItems.setText("Items : " + Getorder_Array_List.size());

        if (Getorder_Array_List.size() == 1)
            tvTotLabel.setText("Price (1 item)");
        else
            tvTotLabel.setText("Price (" + Getorder_Array_List.size() + " items)");

        tvBillSubTotal.setText("₹ " + formatter.format(totalvalues));
        tvBillTotItem.setText("" + Getorder_Array_List.size());
        tvBillTotQty.setText("" + totalQty);
        tvBillToPay.setText("₹ " + formatter.format(totalvalues));
        tvCashDiscount.setText("₹ " + formatter.format(cashDiscount));
        tvTax.setText("₹ " + formatter.format(taxVal));


        if (cashDiscount > 0) {
            tvSaveAmt.setVisibility(View.VISIBLE);
            tvSaveAmt.setText("You will save ₹ " + formatter.format(cashDiscount) + " on this order.");
        } else
            tvSaveAmt.setVisibility(View.GONE);

        for (int l = 0; l < Getorder_Array_List.size(); l++) {
            if (Getorder_Array_List.get(l).getProductDetailsModal() != null) {
                for (int tax = 0; tax < Getorder_Array_List.get(l).getProductDetailsModal().size(); tax++) {
                    String label = Getorder_Array_List.get(l).getProductDetailsModal().get(tax).getTax_Type();
                    Double amt = Getorder_Array_List.get(l).getProductDetailsModal().get(tax).getTax_Amt();
                    if (orderTotTax.size() == 0) {
                        orderTotTax.add(new Product_Details_Modal(label, amt));
                    } else {

                        boolean isDuplicate = false;
                        for (int totTax = 0; totTax < orderTotTax.size(); totTax++) {
                            if (orderTotTax.get(totTax).getTax_Type().equals(label)) {
                                double oldAmt = orderTotTax.get(totTax).getTax_Amt();
                                isDuplicate = true;
                                orderTotTax.set(totTax, new Product_Details_Modal(label, oldAmt + amt));

                            }
                        }

                        if (!isDuplicate) {
                            orderTotTax.add(new Product_Details_Modal(label, amt));

                        }
                    }

                }
            }
        }

        String label = "";
        String amt = "";
        for (int i = 0; i < orderTotTax.size(); i++) {
            label = label + orderTotTax.get(i).getTax_Type() + "\n";
            amt = amt + "₹" + String.valueOf(formatter.format(orderTotTax.get(i).getTax_Amt())) + "\n";
        }

        tvTaxLabel.setText(label);
        tvTax.setText(amt);
        if (orderTotTax.size() == 0) {
            tvTaxLabel.setVisibility(View.INVISIBLE);
            tvTax.setVisibility(View.INVISIBLE);
        } else {
            tvTaxLabel.setVisibility(View.VISIBLE);
            tvTax.setVisibility(View.VISIBLE);

        }
    }

    public void sumofTax(List<Product_Details_Modal> Product_Details_Modalitem, int pos) {
        try {
            String taxRes = sharedCommonPref.getvalue(Constants.PrimaryTAXList);
            if (!Common_Class.isNullOrEmpty(taxRes)) {
                JSONObject jsonObject = new JSONObject(taxRes);


                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                double wholeTax = 0;
                List<Product_Details_Modal> taxList = new ArrayList<>();


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if (jsonObject1.getString("Product_Detail_Code").equals(Product_Details_Modalitem.get(pos).getId())) {

                        if (jsonObject1.getDouble("Tax_Val") > 0) {
                            double taxCal = Product_Details_Modalitem.get(pos).getAmount() *
                                    ((jsonObject1.getDouble("Tax_Val") / 100));

                            wholeTax += taxCal;

                            taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                    jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));


                        }
                    }
                }

                Product_Details_Modalitem.get(pos).setProductDetailsModal(taxList);


                Product_Details_Modalitem.get(pos).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(pos).getAmount()
                        + wholeTax)));

                Product_Details_Modalitem.get(pos).setTax(Double.parseDouble(formatter.format(wholeTax)));


            }

        } catch (Exception e) {

        }
    }

    public void showOrderItemList(int categoryPos, String filterString) {

        Product_ModalSetAdapter.clear();
        for (Product_Details_Modal personNpi : Product_Modal) {
            if (personNpi.getProductCatCode().toString().equals(listt.get(categoryPos).getId())) {
                if (Common_Class.isNullOrEmpty(filterString))
                    Product_ModalSetAdapter.add(personNpi);
                else if (personNpi.getName().toLowerCase().contains(filterString.toLowerCase()))
                    Product_ModalSetAdapter.add(personNpi);

            }
        }
        lin_orderrecyclerview.setVisibility(View.VISIBLE);
        Category_Nametext.setVisibility(View.VISIBLE);
        Category_Nametext.setText(listt.get(categoryPos).getName());


        if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
            lin_gridcategory.setVisibility(View.VISIBLE);
            findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);
            findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
            findViewById(R.id.llBillHeader).setVisibility(View.GONE);
            findViewById(R.id.llPayNetAmountDetail).setVisibility(View.GONE);
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
            btnRepeat.setVisibility(View.VISIBLE);
            takeorder.setText("PROCEED TO CART");
            takeorder.setVisibility(View.VISIBLE);
        }
        mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.adapter_primary_product, getApplicationContext(), categoryPos);
        recyclerView.setAdapter(mProdct_Adapter);

    }

    @Override
    public void onErrorData(String msg) {
        ResetSubmitBtn(2);
    }

    public void loadroute() {

        if (FRoute_Master.size() == 1) {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);
            route_text.setText(FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_name, FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_Id, FRoute_Master.get(0).getId());

        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Rout_List:
                    JSONArray routeArr = new JSONArray(apiDataResponse);
                    FRoute_Master.clear();
                    for (int i = 0; i < routeArr.length(); i++) {
                        JSONObject jsonObject1 = routeArr.getJSONObject(i);
                        String id = String.valueOf(jsonObject1.optInt("id"));
                        String name = jsonObject1.optString("name");
                        String flag = jsonObject1.optString("FWFlg");
                        Model_Pojo = new Common_Model(id, name, flag);
                        Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                        FRoute_Master.add(Model_Pojo);

                    }
                    loadroute();
                    break;
                case Constants.PrePrimaryOrderQty:
                    loadData(apiDataResponse);
                    break;
                case Constants.TodayPrimaryOrderDetails_List:
                    loadData(apiDataResponse);
                    // sharedCommonPref.save(Constants.TodayPrimaryOrderDetails_List, apiDataResponse);
                    break;
                case Constants.Primary_Product_List:
                    String OrdersTable = sharedCommonPref.getvalue(Constants.Primary_Product_List);
                    Product_Modal = gson.fromJson(OrdersTable, userType);
                    JSONArray ProdGroups = db.getMasterData(Constants.ProdGroups_List);
                    LinearLayoutManager GrpgridlayManager = new LinearLayoutManager(this);
                    GrpgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    Grpgrid.setLayoutManager(GrpgridlayManager);

                    RyclListItemAdb grplistItems = new RyclListItemAdb(ProdGroups, this, new onListItemClick() {
                        @Override
                        public void onItemClick(JSONObject item) {

                            try {
                                FilterTypes(item.getString("id"));
                                common_class.brandPos = 0;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Grpgrid.setAdapter(grplistItems);

                    FilterTypes(ProdGroups.getJSONObject(0).getString("id"));

                    break;

                case Constants.PRIMARY_SCHEME:
                    JSONObject jsonObject = new JSONObject(apiDataResponse);
                    if (jsonObject.getBoolean("success")) {
                        Gson gson = new Gson();
                        List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");

                        if (jsonArray != null && jsonArray.length() > 1) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                product_details_modalArrayList.add(new Product_Details_Modal(jsonObject1.getString("Product_Code"),
                                        jsonObject1.getString("Scheme"), jsonObject1.getString("Free"),
                                        Double.valueOf(jsonObject1.getString("Discount")), jsonObject1.getString("Discount_Type"),
                                        jsonObject1.getString("Package"), 0, jsonObject1.getString("Offer_Product"),
                                        jsonObject1.getString("Offer_Product_Name"), jsonObject1.getString("offer_product_unit")));


                            }
                        }

                        sharedCommonPref.save(Constants.PRIMARY_SCHEME, gson.toJson(product_details_modalArrayList));


                    } else {
                        sharedCommonPref.clear_pref(Constants.PRIMARY_SCHEME);

                    }


                    break;

            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                moveProductScreen();

            } else {
                common_class.CommonIntentwithFinish(SFA_Activity.class);

            }
            return true;
        }
        return false;
    }

    void moveProductScreen() {
        lin_gridcategory.setVisibility(View.VISIBLE);
        findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.GONE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.GONE);
        findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
        btnRepeat.setVisibility(View.VISIBLE);
        takeorder.setText("PROCEED TO CART");
        takeorder.setVisibility(View.VISIBLE);
        showOrderItemList(selectedPos, "");
    }

    void loadData(String apiDataResponse) {
        try {
            Product_Modal = gson.fromJson(sharedCommonPref.getvalue(Constants.Primary_Product_List), userType);

            JSONArray jsonArray1 = new JSONArray(apiDataResponse);
            if (jsonArray1 != null && jsonArray1.length() > 0) {
                for (int pm = 0; pm < Product_Modal.size(); pm++) {
                    for (int q = 0; q < jsonArray1.length(); q++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(q);
                        if (Product_Modal.get(pm).getId().equals(jsonObject1.getString("Product_Code"))) {
                            Product_Modal.get(pm).setQty(
                                    jsonObject1.getInt("Quantity"));

                            Product_Modal.get(pm).setAmount(Double.valueOf(formatter.format(Product_Modal.get(pm).getQty() *
                                    Product_Modal.get(pm).getSBRate())));


                            double enterQty = Product_Modal.get(pm).getQty();
                            String strSchemeList = sharedCommonPref.getvalue(Constants.PRIMARY_SCHEME);

                            Type type1 = new TypeToken<ArrayList<Product_Details_Modal>>() {
                            }.getType();
                            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type1);

                            double highestScheme = 0;
                            boolean haveVal = false;
                            if (product_details_modalArrayList != null && product_details_modalArrayList.size() > 0) {

                                for (int i = 0; i < product_details_modalArrayList.size(); i++) {

                                    if (Product_Modal.get(pm).getId().equals(product_details_modalArrayList.get(i).getId())) {

                                        haveVal = true;
                                        double schemeVal = Double.parseDouble(product_details_modalArrayList.get(i).getScheme());

                                        if (enterQty >= schemeVal) {

                                            if (schemeVal > highestScheme) {
                                                highestScheme = schemeVal;


                                                if (!product_details_modalArrayList.get(i).getFree().equals("0")) {
                                                    if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                        double freePer = (enterQty / highestScheme);

                                                        double freeVal = freePer * Double.parseDouble(product_details_modalArrayList.
                                                                get(i).getFree());

                                                        Product_Modal.get(pm).setFree(String.valueOf(Math.round(freeVal)));
                                                    } else {
                                                        int val = (int) (enterQty / highestScheme);
                                                        int freeVal = val * Integer.parseInt(product_details_modalArrayList.get(i).getFree());
                                                        Product_Modal.get(pm).setFree(String.valueOf(freeVal));
                                                    }
                                                } else {

                                                    Product_Modal.get(pm).setFree("0");

                                                }


                                                if (product_details_modalArrayList.get(i).getDiscount() != 0) {

                                                    if (product_details_modalArrayList.get(i).getDiscount_type().equals("%")) {
                                                        double discountVal = enterQty * (((product_details_modalArrayList.get(i).getDiscount()
                                                        )) / 100);
                                                        Product_Modal.get(pm).setDiscount(discountVal);

                                                    } else {
                                                        //Rs
                                                        double freeVal;
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (enterQty / highestScheme);
                                                            freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());

                                                        } else {
                                                            int val = (int) (enterQty / highestScheme);
                                                            freeVal = (double) (val * (product_details_modalArrayList.get(i).getDiscount()));
                                                        }
                                                        Product_Modal.get(pm).setDiscount(freeVal);


                                                    }

                                                } else {
                                                    Product_Modal.get(pm).setDiscount(0.00);

                                                }


                                            }

                                        } else {
                                            Product_Modal.get(pm).setFree("0");

                                            Product_Modal.get(pm).setDiscount(0.00);


                                        }


                                    }

                                }


                            }

                            if (!haveVal) {
                                Product_Modal.get(pm).setFree("0");

                                Product_Modal.get(pm).setDiscount(0.00);

                            } else {
                                Product_Modal.get(pm).setAmount((Product_Modal.get(pm).getAmount()) -
                                        Double.valueOf(Product_Modal.get(pm).getDiscount()));
                            }

                            sumofTax(Product_Modal, pm);
                        }

                    }
                }


            }
            ResetSubmitBtn(0);
            showOrderList();
        } catch (Exception e) {
            Log.v(TAG + ":loadData:", e.getMessage());
        }

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        if (type == 2) {
            // route_text.setText("");
            distributor_text.setText(myDataset.get(position).getName());
            tvTotalAmount.setText("₹ 0.00");
            tvTotalItems.setText("Items : 0");
            sharedCommonPref.save(Constants.Route_name, "");
            sharedCommonPref.save(Constants.Route_Id, "");
            // btnCmbRoute.setVisibility(View.VISIBLE);
            sharedCommonPref.save(Constants.Distributor_name, myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Distributor_Id, myDataset.get(position).getId());
            sharedCommonPref.save(Constants.DistributorERP, myDataset.get(position).getCont());
            sharedCommonPref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
            sharedCommonPref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());

            common_class.getProductDetails(this);
            common_class.getDb_310Data(Constants.Primary_Product_List, this);
            getACBalance(0);  // common_class.getDb_310Data(Rout_List, this);
            common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

        } else if (type == 3) {
            route_text.setText(myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
            common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

        }
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
        Context context;
        MyViewHolder pholder;

        public CategoryAdapter(Context applicationContext, List<Category_Universe_Modal> list) {
            this.context = applicationContext;
            listt = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.category_order_horizantal_universe_gridview, parent, false);
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
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                holder.icon.setText(listt.get(position).getName());
                if (!listt.get(position).getCatImage().equalsIgnoreCase("")) {
                    holder.ivCategoryIcon.clearColorFilter();
                    Glide.with(this.context)
                            .load(listt.get(position).getCatImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.ivCategoryIcon);
                } else {
                    holder.ivCategoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.product_logo));
                    holder.ivCategoryIcon.setColorFilter(getResources().getColor(R.color.grey_500));
                }

                holder.gridcolor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pholder != null) {
                            pholder.gridcolor.setBackground(getResources().getDrawable(R.drawable.cardbutton));
                            pholder.icon.setTextColor(getResources().getColor(R.color.black));
                            pholder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                            pholder.undrCate.setVisibility(View.GONE);

                        }
                        pholder = holder;
                        selectedPos = position;
                        showOrderItemList(position, "");
                        holder.gridcolor.setBackground(getResources().getDrawable(R.drawable.cardbtnprimary));
                        holder.icon.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                        holder.undrCate.setVisibility(View.VISIBLE);
                    }
                });


                if (position == selectedPos) {
                    holder.gridcolor.setBackground(getResources().getDrawable(R.drawable.cardbtnprimary));
                    holder.icon.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.undrCate.setVisibility(View.VISIBLE);
                    pholder = holder;
                } else {
                    holder.gridcolor.setBackground(getResources().getDrawable(R.drawable.cardbutton));
                    holder.icon.setTextColor(getResources().getColor(R.color.black));
                    holder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                }


            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return listt.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout gridcolor, undrCate;
            TextView icon;
            ImageView ivCategoryIcon;


            public MyViewHolder(View view) {
                super(view);

                icon = view.findViewById(R.id.textView);
                gridcolor = view.findViewById(R.id.gridcolor);
                ivCategoryIcon = view.findViewById(R.id.ivCategoryIcon);
                undrCate = view.findViewById(R.id.undrCate);

            }
        }


    }

    public class Prodct_Adapter extends RecyclerView.Adapter<Prodct_Adapter.MyViewHolder> {
        Context context;
        int CategoryType;
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;


        public Prodct_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int categoryType) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.CategoryType = categoryType;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
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
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {


                Product_Details_Modal ProductItem = Product_Details_Modalitem.get(holder.getAdapterPosition());
                holder.productname.setText("" + ProductItem.getName().toUpperCase());
                holder.Rate.setText("₹" + formatter.format(ProductItem.getSBRate()));// * (Integer.parseInt(Product_Details_Modal.getConversionFactor()))));
                holder.Amount.setText("₹" + new DecimalFormat("##0.00").format(ProductItem.getAmount()));

                int oQty = ProductItem.getQty();
                String sQty = ProductItem.getQty().toString();
                if (oQty <= 0) sQty = "";
                holder.Qty.setText(sQty);

                if (CategoryType >= 0) {

                    holder.tvMRP.setText("₹" + ProductItem.getMRP());
                    holder.totalQty.setText("Total Qty : " + oQty);//((Product_Details_Modalitem.get(holder.getAdapterPosition()).getQty() * (Integer.parseInt(Product_Details_Modal.getConversionFactor())))));

                    if (!ProductItem.getPImage().equalsIgnoreCase("")) {
                        holder.ImgVwProd.clearColorFilter();
                        Glide.with(this.context)
                                .load(ProductItem.getPImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.ImgVwProd);
                    } else {
                        holder.ImgVwProd.setImageDrawable(getResources().getDrawable(R.drawable.product_logo));
                        holder.ImgVwProd.setColorFilter(getResources().getColor(R.color.grey_500));
                    }


                    holder.QtyAmt.setText("₹" + formatter.format(oQty * ProductItem.getSBRate())); //* (Integer.parseInt(Product_Details_Modal.getConversionFactor())) * Product_Details_Modal.getQty()));


                }

                holder.tvTaxLabel.setText("₹" + formatter.format(ProductItem.getTax()));


                if (Common_Class.isNullOrEmpty(ProductItem.getFree()))
                    holder.Free.setText("0");
                else
                    holder.Free.setText("" + ProductItem.getFree());


                holder.Disc.setText("₹" + formatter.format(ProductItem.getDiscount()));


                holder.QtyPls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";
                        int iQty = Integer.parseInt(sVal) + 1;
                        sVal = "";
                        if (iQty > 0) sVal = String.valueOf(iQty);
                        holder.Qty.setText(sVal);
                        bRmRow = true;
                        //sumofTax();
                    }
                });
                holder.QtyMns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";
                        int iQty = Integer.parseInt(sVal) - 1;
                        sVal = "";
                        if (iQty > 0) sVal = String.valueOf(iQty);
                        holder.Qty.setText(sVal);
                        bRmRow = true;
                        // sumofTax();
                    }
                });
                holder.Qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence charSequence, int start,
                                              int before, int count) {
                        try {

                            double enterQty = 0;
                            if (!charSequence.toString().equals(""))
                                enterQty = Double.valueOf(charSequence.toString());

                            double totQty = enterQty;//* Double.valueOf(Product_Details_Modalitem.get(holder.getAdapterPosition()).getConversionFactor()));
                            double ProdAmt = totQty * Product_Details_Modalitem.get(holder.getAdapterPosition()).getSBRate();

                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText("₹" + new DecimalFormat("##0.00").format(ProdAmt));
                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount(ProdAmt);
                            if (CategoryType >= 0) {
                                holder.QtyAmt.setText("₹" + formatter.format(ProdAmt));
                                holder.totalQty.setText("Total Qty : " + (int) totQty);
                            }

                            String strSchemeList = sharedCommonPref.getvalue(Constants.PRIMARY_SCHEME);

                            Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
                            }.getType();
                            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type);

                            double highestScheme = 0;
                            boolean haveVal = false;
                            if (totQty > 0 && product_details_modalArrayList != null && product_details_modalArrayList.size() > 0) {

                                for (int i = 0; i < product_details_modalArrayList.size(); i++) {

                                    if (ProductItem.getId().equals(product_details_modalArrayList.get(i).getId())) {

                                        haveVal = true;
                                        double schemeVal = Double.parseDouble(product_details_modalArrayList.get(i).getScheme());

                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setOff_Pro_code(product_details_modalArrayList.get(i).getOff_Pro_code());
                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setOff_Pro_name(product_details_modalArrayList.get(i).getOff_Pro_name());
                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setOff_Pro_Unit(product_details_modalArrayList.get(i).getOff_Pro_Unit());
                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setFree_val(product_details_modalArrayList.get(i).getFree());

                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount_value(String.valueOf(product_details_modalArrayList.get(i).getDiscount()));
                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount_type(product_details_modalArrayList.get(i).getDiscount_type());


                                        if (totQty >= schemeVal) {

                                            if (schemeVal > highestScheme) {
                                                highestScheme = schemeVal;


                                                if (!product_details_modalArrayList.get(i).getFree().equals("0")) {
                                                    if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                        double freePer = (totQty / highestScheme);

                                                        double freeVal = freePer * Double.parseDouble(product_details_modalArrayList.
                                                                get(i).getFree());

                                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setFree(String.valueOf(Math.round(freeVal)));
                                                    } else {
                                                        int val = (int) (totQty / highestScheme);
                                                        int freeVal = val * Integer.parseInt(product_details_modalArrayList.get(i).getFree());
                                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setFree(String.valueOf(freeVal));
                                                    }
                                                } else {

                                                    holder.Free.setText("0");
                                                    Product_Details_Modalitem.get(holder.getAdapterPosition()).setFree("0");

                                                }


                                                if (product_details_modalArrayList.get(i).getDiscount() != 0) {

                                                    if (product_details_modalArrayList.get(i).getDiscount_type().equals("%")) {
                                                        double discountVal = totQty * (((product_details_modalArrayList.get(i).getDiscount()
                                                        )) / 100);


                                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount(discountVal);

                                                    } else {
                                                        //Rs
                                                        double freeVal;
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (totQty / highestScheme);
                                                            freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());
                                                        } else {
                                                            int val = (int) (totQty / highestScheme);
                                                            freeVal = (double) (val * (product_details_modalArrayList.get(i).getDiscount()));
                                                        }
                                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount(freeVal);

                                                    }


                                                } else {
                                                    holder.Disc.setText("₹0.00");
                                                    Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount(0.00);

                                                }


                                            }

                                        } else {
                                            holder.Free.setText("0");
                                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setFree("0");

                                            holder.Disc.setText("₹0.00");
                                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount(0.00);


                                        }


                                    }

                                }


                            }

                            if (!haveVal) {
                                holder.Free.setText("0");
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setFree("0");

                                holder.Disc.setText("₹0.00");
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount(0.00);
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setOff_Pro_code("");
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setOff_Pro_name("");
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setOff_Pro_Unit("");

                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount_value("0.00");
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount_type("");


                            } else {

                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount((Product_Details_Modalitem.get(holder.getAdapterPosition()).getAmount()) -
                                        (Product_Details_Modalitem.get(holder.getAdapterPosition()).getDiscount()));

                                holder.Free.setText("" + Product_Details_Modalitem.get(holder.getAdapterPosition()).getFree());
                                holder.Disc.setText("₹" + formatter.format(Product_Details_Modalitem.get(holder.getAdapterPosition()).getDiscount()));

                                holder.Amount.setText("₹" + formatter.format(Product_Details_Modalitem.get(holder.getAdapterPosition()).getAmount()));


                            }

                            sumofTax(Product_Details_Modalitem, holder.getAdapterPosition());
                            holder.Amount.setText("₹" + formatter.format(Product_Details_Modalitem.get(holder.getAdapterPosition()).getAmount()));
                            holder.tvTaxLabel.setText("₹" + formatter.format(Product_Details_Modalitem.get(holder.getAdapterPosition()).getTax()));

                            updateToTALITEMUI();


                            if (CategoryType == -1) {
                                if (holder.Amount.getText().toString().equals("₹0.00")) {

                                    Product_Details_Modalitem.remove(position);
                                    notifyDataSetChanged();

                                }

                                showFreeQtyList();
                            }

                        } catch (Exception e) {
                            Log.v(TAG, " orderAdapter:qty " + e.getMessage());
                        }

                        bRmRow = false;

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                if (CategoryType == -1) {
                    holder.ivDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialogBox.showDialog(FPPrimaryOrderActivity.this, "HAP SFA",
                                    "Do you want to remove " + Product_Details_Modalitem.get(position).getName().toUpperCase() + " from your cart?"
                                    , "OK", "Cancel", false, new AlertBox() {
                                        @Override
                                        public void PositiveMethod(DialogInterface dialog, int id) {
                                            Product_Details_Modalitem.get(position).setQty(0);
                                            Product_Details_Modalitem.remove(position);
                                            notifyDataSetChanged();
                                            updateToTALITEMUI();
                                        }

                                        @Override
                                        public void NegativeMethod(DialogInterface dialog, int id) {
                                            dialog.dismiss();

                                        }
                                    });

                        }
                    });
                }

                updateToTALITEMUI();
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, Disc, Free, lblRQty, productQty,
                    QtyAmt, totalQty, tvTaxLabel, tvMRP;
            ImageView ImgVwProd, QtyPls, QtyMns, ivDel;
            EditText Qty;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                QtyPls = view.findViewById(R.id.ivQtyPls);
                QtyMns = view.findViewById(R.id.ivQtyMns);
                Rate = view.findViewById(R.id.Rate);
                Qty = view.findViewById(R.id.Qty);
                Amount = view.findViewById(R.id.Amount);
                Free = view.findViewById(R.id.Free);
                Disc = view.findViewById(R.id.Disc);
                tvTaxLabel = view.findViewById(R.id.tvTaxTotAmt);


                if (CategoryType >= 0) {
                    ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);
                    lblRQty = view.findViewById(R.id.status);
                    QtyAmt = view.findViewById(R.id.qtyAmt);
                    totalQty = view.findViewById(R.id.totalqty);
                    tvMRP = view.findViewById(R.id.MrpRate);

                } else {
                    ivDel = view.findViewById(R.id.ivDel);
                }


            }
        }
    }

    public class Free_Adapter extends RecyclerView.Adapter<Free_Adapter.MyViewHolder> {
        Context context;
        private final List<Product_Details_Modal> Product_Details_Modalitem;
        private final int rowLayout;


        public Free_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int Categorycolor) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;


        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
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
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {


                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(position);
                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.Free.setText("" + Product_Details_Modal.getFree());
                updateToTALITEMUI();
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Free;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                Free = view.findViewById(R.id.Free);

            }
        }
    }
}