package com.hap.checkinproc.SFA_Activity;

import static com.hap.checkinproc.SFA_Activity.HAPApp.CurrencySymbol;
import static com.hap.checkinproc.SFA_Activity.HAPApp.MRPCap;
import static com.hap.checkinproc.SFA_Activity.HAPApp.ProductsLoaded;
import static com.hap.checkinproc.SFA_Activity.HAPApp.StockCheck;
import static com.hap.checkinproc.SFA_Activity.HAPApp.getActiveActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.QRCodeScanner;
import com.hap.checkinproc.Activity_Hap.SFA_Activity;
import com.hap.checkinproc.BuildConfig;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.OnLiveUpdateListener;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.Interface.onListItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Dashboard_View_Adapter;
import com.hap.checkinproc.SFA_Adapter.RyclBrandListItemAdb;
import com.hap.checkinproc.SFA_Adapter.RyclListItemAdb;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;
import com.hap.checkinproc.SFA_Model_Class.Dashboard_View_Model;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferCounterActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI,  Master_Interface {
    final Handler handler = new Handler();
    public int selectedPos = 0, uomPos;
    //GridView categorygrid,Grpgrid,Brndgrid;
    SharedPreferences UserDetails;
    SharedPreferences CartDetails;
    Type userType;
    Gson gson;
    CircularProgressButton takeorder;
    TextView Category_Nametext, tvName, tvMRP, lblName, lblPhone, lblAddress, tvPosOrders, tvPayMode;
    LinearLayout lin_orderrecyclerview, lin_gridcategory, rlAddProduct, rlQtyParent;
    Common_Class common_class;
    String Ukey;
    String[] strLoc;
    String Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "";
    Shared_Common_Pref sharedCommonPref;
    Prodct_Adapter mProdct_Adapter;
    String TAG = "POSACTIVITY",UserInfo = "MyPrefs",OrderCart = "MyPrefs";
    DatabaseHandler db;
    RelativeLayout rlCategoryItemSearch;
    ImageView ivClose, ivMns, ivPlus, ImgVProd;
    EditText etCategoryItemSearch, etName, etPhone, etAddress, etQty, etRecAmt;
    EditText cashdiscount,etDiscPer,etDiscAmt;
    double cashDiscount;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private RecyclerView recyclerView,BndlGrid, Grpgrid, freeRecyclerview;
    private TextView tvTotalAmount, tvBalAmt, tvNetAmtTax, tvDate, tvDay,tvInvAmt,tvPayAmt;
    private double totalvalues,rDiscAmt,rDiscPer, InvAmt;
    private Integer totalQty;
    private TextView tvBillTotItem;
    private DatePickerDialog fromDatePickerDialog;
    private List<Product_Details_Modal> orderTotTax;
    private String scanProId = "",OrderTypId="",OrderTypNm="";
    private ArrayList<Common_Model> uomList;

    private final List<Common_Model> payList = new ArrayList<>();
    private double payAmt;
    private double totTax;
    RecyclerView rvCurrentStk;
    boolean CartView;
    String cusName="";
    BundleProdAdapter BundleProds;
    com.hap.checkinproc.Activity_Hap.Common_Class DT = new com.hap.checkinproc.Activity_Hap.Common_Class();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_offer_counter_layout);
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(this);
            common_class = new Common_Class(this);
            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
            CartDetails = getSharedPreferences(OrderCart, Context.MODE_PRIVATE);

            selectedPos = 0;
            rvCurrentStk = findViewById(R.id.rvCurntStk);
            Grpgrid = findViewById(R.id.PGroup);
            BndlGrid = findViewById(R.id.OrdOfferBund);
            takeorder = findViewById(R.id.takeorder);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            lin_orderrecyclerview = findViewById(R.id.lin_orderrecyclerview);
            lin_gridcategory = findViewById(R.id.lin_gridcategory);
            Category_Nametext = findViewById(R.id.Category_Nametext);
            rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
            rlQtyParent = findViewById(R.id.rlQtyParent);
            rlAddProduct = findViewById(R.id.rlAddProduct);
            ivClose = findViewById(R.id.ivClose);

            etCategoryItemSearch = findViewById(R.id.searchView);
            etName = findViewById(R.id.edt_name);
            etPhone = findViewById(R.id.edt_phone);
            etAddress = findViewById(R.id.edtAddress);
            lblName = findViewById(R.id.lbl_name);
            lblPhone = findViewById(R.id.lbl_phone);
            lblAddress = findViewById(R.id.lblAddress);

            tvName = findViewById(R.id.tvScanProName);
            tvMRP = findViewById(R.id.tvScanMRP);
            ivPlus = findViewById(R.id.ivScanQtyPls);
            ivMns = findViewById(R.id.ivScanQtyMns);
            etQty = findViewById(R.id.etScanQty);
            etRecAmt = findViewById(R.id.etRecAmt);
            ImgVProd = findViewById(R.id.ivAddShoppingCart);
            tvPosOrders = findViewById(R.id.tvPosOrders);
            tvPayMode = findViewById(R.id.tvPayMode);
            tvBalAmt = findViewById(R.id.tvBalance);
            tvDate = findViewById(R.id.tvDate);
            tvDay = findViewById(R.id.tvDay);
            tvInvAmt = findViewById(R.id.subtotal);
            tvPayAmt = findViewById(R.id.tvnetamount);

            tvPayMode.setOnClickListener(this);

            gson = new Gson();
            takeorder.setOnClickListener(this);
            rlCategoryItemSearch.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            rlAddProduct.setOnClickListener(this);
            tvPosOrders.setOnClickListener(this);
            Ukey = Common_Class.GetEkey();

            etDiscPer=findViewById(R.id.etdiscountPer);
            etDiscAmt=findViewById(R.id.etdiscountAmt);
            etDiscPer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    etDiscAmt.setText("0");
                    rDiscPer =0;
                    if (!Common_Class.isNullOrEmpty(s.toString())) {
                        rDiscPer = Double.parseDouble(s.toString());
                    }

                    InvAmt= Double.parseDouble( tvInvAmt.getText().toString().replace(CurrencySymbol+" " , ""));
                    rDiscAmt=InvAmt*(rDiscPer/100);
                    totalvalues=InvAmt - rDiscAmt;
                    if (rDiscAmt == 0) {
                        etDiscAmt.setText("");
                    } else {
                        etDiscAmt.setText(new DecimalFormat("##0.00").format(rDiscAmt));
                    }

                    //tvPayAmt.setText(CurrencySymbol+" " + formatter.format(totalvalues ));
                    ///////tvTotOutstanding.setText(CurrencySymbol+" "+ formatter.format(outstandAmt + (totalvalues - payAmt)));


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etDiscPer.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, 100)});
            etDiscAmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus=true){
                        InvAmt= Double.parseDouble( tvInvAmt.getText().toString().replace(CurrencySymbol+" " , ""));
                        if(!etDiscPer.getText().toString().equalsIgnoreCase("")){
                            etDiscPer.setText("");
                            etDiscAmt.setText("");
                            rDiscPer=0;
                            rDiscAmt=InvAmt*(rDiscPer/100);
                            totalvalues=InvAmt - rDiscAmt;
                            tvPayAmt.setText(CurrencySymbol+" " + formatter.format(totalvalues ));
                            //tvTotOutstanding.setText(CurrencySymbol+" "+ formatter.format(outstandAmt + (totalvalues - payAmt)));
                        }
                        etDiscAmt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        InputFilter limitFilter = new MinMaxInputFilter(0.0, InvAmt);
                        etDiscAmt.setFilters(new InputFilter[] { limitFilter });
                    }
                }
            });
            etDiscAmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    rDiscAmt=0;
                    if (!Common_Class.isNullOrEmpty(s.toString())) {
                        rDiscAmt = Double.parseDouble(s.toString());
                    }
                    InvAmt= Double.parseDouble( tvInvAmt.getText().toString().replace(CurrencySymbol+" " , ""));
                    totalvalues=InvAmt - rDiscAmt;

                    tvPayAmt.setText(CurrencySymbol+" " + formatter.format(totalvalues ));
                    //tvTotOutstanding.setText(CurrencySymbol+" "+ formatter.format(outstandAmt + (totalvalues - payAmt)));


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            tvDate.setText("" + DT.getDateWithFormat(new Date(), "dd-MMM-yyyy"));
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            tvDay.setText("" + sdf.format(new Date()));

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            Category_Nametext.setOnClickListener(this);
            etRecAmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        payAmt = 0;

                        if (!Common_Class.isNullOrEmpty(s.toString())) {
                            payAmt = Double.parseDouble(s.toString());
                        }

                        //tvBalAmt.setText(formatter.format((payAmt - totalvalues)));

                    } catch (Exception e) {

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            JSONArray ProdGroups = db.getMasterData(Constants.POS_ProdGroups_List);
            LinearLayoutManager GrpgridlayManager = new LinearLayoutManager(this);
            GrpgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            Grpgrid.setLayoutManager(GrpgridlayManager);

            RyclListItemAdb grplistItems = new RyclListItemAdb(ProdGroups, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {

                    try {
                        OrderTypId=item.getString("id");
                        OrderTypNm=item.getString("name");
                        common_class.brandPos = 0;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Grpgrid.setAdapter(grplistItems);
            OrderTypId=ProdGroups.getJSONObject(0).getString("id");
            OrderTypNm=ProdGroups.getJSONObject(0).getString("name");
            LoadingMaterials();

        } catch (Exception e) {
            Log.v(TAG, " order oncreate: " + e.getMessage());

        }
    }

    public void LoadingMaterials(){
        common_class.ProgressdialogShow(1, "Loading Material Details");
        common_class.getOffPOSProductDetails(getActiveActivity(), new OnLiveUpdateListener() {
            @Override
            public void onUpdate(String mode) {
                common_class.getDb_310Data(Constants.POS_TAXList, OfferCounterActivity.this);
                common_class.getDb_310Data(Constants.POS_SCHEME, OfferCounterActivity.this);

                common_class.ProgressdialogShow(0, "");
                initData();
                if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.POS_NETAMT_TAX)))
                    common_class.getDb_310Data(Constants.POS_NETAMT_TAX, OfferCounterActivity.this);

                common_class.getDb_310Data(Constants.CURRENT_STOCK, OfferCounterActivity.this);
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
    public void initData()
    {
        JSONArray Prods = new JSONArray();
        try {
             Prods = new JSONArray(CartDetails.getString("Cart","[]"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if(Prods.length()<1){
         Prods = db.getMasterData(Constants.OFFERPOS_Product_List);
        }
        BundleProds = new BundleProdAdapter(this,Prods);
        BndlGrid.setAdapter(BundleProds);
        updateToTALITEMUI();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    public void sumofTax(JSONObject jObj, int pos) {
        try {
            String taxRes = sharedCommonPref.getvalue(Constants.POS_TAXList);
            if (!Common_Class.isNullOrEmpty(taxRes)) {
                JSONObject jsonObject = new JSONObject(taxRes);
                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                double wholeTax = 0;
                JSONArray taxList = new JSONArray();

                double totTax = 0;

                JSONArray proTaxArr = new JSONArray();
                double rate = 0;

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.getString("Product_Detail_Code").equals(jObj.getString("id"))) {
                        if (jsonObject1.getDouble("Tax_Val") > 0) {
                            totTax = totTax + (jsonObject1.getDouble("Tax_Val"));

                            JSONObject totTaxObj = new JSONObject();
                            totTaxObj.put("Tax_Id", jsonObject1.getString("Tax_Id"));

                            totTaxObj.put("Tax_Type", jsonObject1.getString("Tax_Type"));
                            totTaxObj.put("Tax_Val", jsonObject1.getDouble("Tax_Val"));
                            proTaxArr.put(totTaxObj);
                        }
                    }
                }

                rate= ((Double.parseDouble(jObj.getString("MRP")) * 100) / (totTax + 100));
                wholeTax = (Double.parseDouble(jObj.getString("Qty")) *
                        (Double.parseDouble(jObj.getString("MRP")) - rate));
                Log.v("taxCalc:", "val:" + wholeTax + ":Rate:" + rate + ":totTax:" + totTax);

                for (int pTax = 0; pTax < proTaxArr.length(); pTax++) {
                    JSONObject jsonObject1 = proTaxArr.getJSONObject(pTax);
                    double itotTax = jsonObject1.getDouble("Tax_Val");
                    double irate= ((Double.parseDouble(jObj.getString("MRP")) * 100) / (itotTax + 100));

                    double taxCal = (Double.parseDouble(jObj.getString("Qty")) *
                            (Double.parseDouble(jObj.getString("MRP")) - irate));
                    JSONObject itm=new JSONObject();
                    itm.put("Tax_Id",jsonObject1.getString("Tax_Id"));
                    itm.put("Tax_Type",jsonObject1.getString("Tax_Type"));
                    itm.put("Tax_Val",jsonObject1.getDouble("Tax_Val"));
                    itm.put("Tax_Amt",taxCal);

                    taxList.put(itm);

                }

                jObj.put("TaxList",taxList);
                //Product_Details_Modalitem.get(pos).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(pos).getAmount())));
                jObj.put("Tax",Double.parseDouble(formatter.format(wholeTax)));
            }
        } catch (Exception e) {
            Log.v("taxCalc:ex", e.getMessage());
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
            }
        }, dely);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPayMode:
                common_class.getDb_310Data(Constants.PAYMODES, this);
                break;
            case R.id.tvPosOrders:
                startActivity(new Intent(getApplicationContext(), PosHistoryActivity.class));
                break;
            case R.id.ivScanner:
                Intent intent = new Intent(this, QRCodeScanner.class);
                intent.putExtra("scan", "scan");
                startActivity(intent);
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
                break;
            case R.id.takeorder:
                try {
                    if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                            Log.d("RepeatAni", String.valueOf(takeorder.isAnimating()));
                            if (takeorder.isAnimating()) return;
                            takeorder.startAnimation();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
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
                            }, 500);
                    }else {
                        CartView =true;
                        BundleProds.notifyDataSetChanged();
                        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);
                        takeorder.setText("SUBMIT");
                        ResetSubmitBtn(0);
                    }
                } catch (Exception e) {
                    Log.v(TAG, e.getMessage());
                }
                break;


        }
    }


    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {

           /* if(StockCheck.equalsIgnoreCase("1")) {
                for (int z = 0; z < Getorder_Array_List.size(); z++) {
                    double enterQty = Getorder_Array_List.get(z).getQty();
                    double totQty = (enterQty * Getorder_Array_List.get(z).getCnvQty());
                    if ((Getorder_Array_List.get(z).getBalance() - (int) totQty) < 0) {
                        Toast.makeText(this, "Low Stock", Toast.LENGTH_LONG).show();
                        ResetSubmitBtn(0);
                        return;
                    }
                }
            }
            */

            AlertDialogBox.showDialog(OfferCounterActivity.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    common_class.ProgressdialogShow(1, "");
                    JSONArray data = new JSONArray();
                    JSONObject ActivityData = new JSONObject();
                    try {
                        JSONObject HeadItem = new JSONObject();
                        HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("dcr_activity_date", Common_Class.GetDate());
                        HeadItem.put("UKey", Ukey);
                        HeadItem.put("AppVer", BuildConfig.VERSION_NAME);
                        ActivityData.put("Activity_Report_Head", HeadItem);
                        cusName=etName.getText().toString();
                        if(cusName.equalsIgnoreCase("")){
                            cusName="Customer";
                        }
                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                        OutletItem.put("name", cusName);
                        OutletItem.put("phoneNo", etPhone.getText().toString());
                        OutletItem.put("address", etAddress.getText().toString());
                        OutletItem.put("CashDiscount", cashDiscount);
                        OutletItem.put("NetAmount", formatter.format(totalvalues));
                        OutletItem.put("InvAmt", InvAmt);
                        OutletItem.put("No_Of_items", tvBillTotItem.getText().toString());
                        OutletItem.put("ordertype", "posoff");
                        OutletItem.put("payMode", tvPayMode.getText().toString());
                        OutletItem.put("totAmtTax", totTax);

                        sharedCommonPref.save(Constants.Retailor_Name_ERP_Code,cusName);
                        sharedCommonPref.save(Constants.Retailor_PHNo,etPhone.getText().toString());
                        OutletItem.put("RecAmt",
                                tvPayMode.getText().toString().equalsIgnoreCase("cash") ? etRecAmt.getText().toString() : "0");
                        OutletItem.put("Balance", tvPayMode.getText().toString().equalsIgnoreCase("cash") ?
                                tvBalAmt.getText().toString() : "0");

                        if (strLoc.length > 0) {
                            OutletItem.put("Lat", strLoc[0]);
                            OutletItem.put("Long", strLoc[1]);
                        } else {
                            OutletItem.put("Lat", "");
                            OutletItem.put("Long", "");
                        }

                        JSONArray totTaxArr = new JSONArray();
                        JSONArray Order_Details = new JSONArray(CartDetails.getString("Cart","[]"));
                        for (int i = 0; i < orderTotTax.size(); i++) {
                            JSONObject totTaxObj = new JSONObject();
                            totTaxObj.put("Tax_Type", orderTotTax.get(i).getTax_Type());
                            totTaxObj.put("Tax_Amt", formatter.format(orderTotTax.get(i).getTax_Amt()));
                            totTaxArr.put(totTaxObj);
                        }

                        OutletItem.put("TOT_TAX_details", totTaxArr);
                        ActivityData.put("Activity_Doctor_Report", OutletItem);
                        ActivityData.put("Order_Details", Order_Details);
                        data.put(ActivityData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> responseBodyCall = apiInterface.savePOSOffer(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                    responseBodyCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    common_class.ProgressdialogShow(0, "");
                                    Log.e("JSON_VALUES", response.body().toString());
                                    JSONObject jsonObjects = new JSONObject(response.body().toString());
                                    ResetSubmitBtn(1);
                                    String succ=jsonObjects.getString("success");


                                    if (jsonObjects.getString("success").equals("true")) {
                                        sharedCommonPref.clear_pref(Constants.LOC_POS_DATA);

                                        SharedPreferences.Editor editor = CartDetails.edit();
                                        editor.remove("Cart");
                                        editor.apply();

                                        Shared_Common_Pref.TransSlNo = jsonObjects.getString("OrderID");
                                        sharedCommonPref.save(Constants.FLAG, "POS INVOICE");ProductsLoaded=true;
                                        common_class.ProgressdialogShow(1, "Updating Material Details");
                                        common_class.getOffPOSProductDetails(OfferCounterActivity.this, new OnLiveUpdateListener() {
                                            @Override
                                            public void onUpdate(String mode) {
                                                Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                                                sharedCommonPref.save(Constants.FLAG, "POS INVOICE");
                                                sharedCommonPref.save(Constants.Retailor_Name_ERP_Code,cusName);
                                                sharedCommonPref.save(Constants.Retailor_PHNo,etPhone.getText().toString());
                                                intent.putExtra("NetAmount", String.valueOf(totalvalues));
                                                intent.putExtra("Discount_Amount", String.valueOf( cashDiscount));
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.in, R.anim.out);

                                                common_class.ProgressdialogShow(0, "");
                                            }
                                        });
                                        //common_class.CommonIntentwithFinish(Print_Invoice_Activity.class);

                                    }
                                    common_class.showMsg(OfferCounterActivity.this, jsonObjects.getString("Msg"));

                                } catch (Exception e) {
                                    common_class.ProgressdialogShow(0, "");
                                    ResetSubmitBtn(2);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            call.cancel();
                            common_class.ProgressdialogShow(0, "");
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
            Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
            ResetSubmitBtn(0);
        }
    }
    public void updateToTALITEMUI() {
        try {
            TextView tvTotalItems = findViewById(R.id.tvTotalItems);
            TextView tvTotLabel = findViewById(R.id.tvTotLabel);
            tvTotalAmount = findViewById(R.id.tvTotalAmount);
            tvNetAmtTax = findViewById(R.id.tvNetAmtTax);
            TextView tvTax = findViewById(R.id.tvTaxVal);
            TextView tvTaxLabel = findViewById(R.id.tvTaxLabel);
            TextView tvBillSubTotal = findViewById(R.id.subtotal);
            TextView tvSaveAmt = findViewById(R.id.tvSaveAmt);
            tvBillTotItem = findViewById(R.id.totalitem);
            TextView tvBillTotQty = findViewById(R.id.tvtotalqty);
            TextView tvBillToPay = findViewById(R.id.tvnetamount);
            TextView tvCashDiscount = findViewById(R.id.tvcashdiscount);

            orderTotTax = new ArrayList<>();
            orderTotTax.clear();
            totalvalues = 0;
            totalQty = 0;
            cashDiscount = 0;
            int NofItm=0;
            JSONArray Prods = new JSONArray();
            try {
                Prods = new JSONArray(CartDetails.getString("Cart","[]"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if(Prods.length()<1){
                Prods = db.getMasterData(Constants.OFFERPOS_Product_List);
            }
            for(int ij=0;ij<Prods.length();ij++){
               JSONObject jObj = Prods.getJSONObject(ij);
               JSONArray jProd= jObj.getJSONArray("Prods");
                for(int ik=0;ik<jProd.length();ik++){
                    JSONObject itm=jProd.getJSONObject(ik);
                    if(itm.getInt("Qty")>0){
                        NofItm++;
                        totalQty += itm.getInt("Qty");
                        totalvalues += itm.getDouble("Amount");
                        JSONArray jPTaxList=itm.getJSONArray("TaxList");
                        for (int tax = 0; tax < jPTaxList.length(); tax++) {
                            String label = jPTaxList.getJSONObject(tax).getString("Tax_Type");
                            Double amt = jPTaxList.getJSONObject(tax).getDouble("Tax_Amt");
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
            }

            tvTotalItems.setText("Items : " + NofItm + "   Qty : " + totalQty);
            tvTotalAmount.setText(CurrencySymbol+" " + formatter.format(totalvalues));
            tvTotLabel.setText("Price (" + NofItm + " items)");
            tvBillSubTotal.setText(CurrencySymbol+" " + formatter.format(totalvalues));
            tvBillTotItem.setText("" + NofItm);
            tvBillTotQty.setText("" + totalQty);
            Double subTotal=totalvalues/2;

            tvBillToPay.setText(CurrencySymbol+" " + formatter.format(subTotal));
            tvCashDiscount.setText(CurrencySymbol+" " + formatter.format(subTotal));
            cashDiscount=subTotal;
            InvAmt=subTotal/1.05;
            totTax=subTotal-InvAmt;
            tvNetAmtTax.setText(CurrencySymbol+" " + formatter.format(InvAmt));
            String label = "", amt = "";
            for (int i = 0; i < orderTotTax.size(); i++) {
                label = label + orderTotTax.get(i).getTax_Type() + "\n";
                //amt = amt + CurrencySymbol+" " + formatter.format(orderTotTax.get(i).getTax_Amt()) + "\n";
                amt = amt + CurrencySymbol+" " + formatter.format(totTax/2.0) + "\n";

                orderTotTax.get(i).setTax_Amt(totTax/2.0);
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

        } catch (Exception e) {

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            ResetSubmitBtn(0);
        }
    }
    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {


            switch (key) {
                case Constants.CURRENT_STOCK:
                    Log.v("POS_CURRENT_STK:", apiDataResponse);

                    JSONObject currStkObj = new JSONObject(apiDataResponse);
                    List<Dashboard_View_Model> approvalList = new ArrayList<>();

                    if (currStkObj.getBoolean("success")) {
                        JSONArray arr = currStkObj.getJSONArray("Data");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            String val = obj.toString();
                            String result = val.replaceAll("[(){}]", "");

                            List<String> list = Arrays.asList(result.split(","));

                            for (int l = 0; l < list.size(); l++) {
                                int split = list.get(l).indexOf(":");
                                String name = list.get(l).substring(1, split - 1);
                                String cnt = list.get(l).substring(split + 1, list.get(l).length());

                                if (name.equalsIgnoreCase("SoldValue"))
                                    cnt = CurrencySymbol+" " + cnt;
                                approvalList.add(new Dashboard_View_Model(name, cnt));
                            }

                        }
                    }


                    rvCurrentStk.setAdapter(new Dashboard_View_Adapter(approvalList, R.layout.adapter_current_stk_layout, getApplicationContext(), new AdapterOnClick() {
                        @Override
                        public void onIntentClick(int Name) {

                        }
                    }));
                    break;
                case Constants.POS_NETAMT_TAX:
                    Log.v("POS_NETAMT_TAX:", apiDataResponse);
                    break;
                case Constants.STOCK_DATA:
                    JSONObject stkObj = new JSONObject(apiDataResponse);
                    /*if (stkObj.getBoolean("success")) {
                        JSONArray arr = stkObj.getJSONArray("Data");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            for (int pm = 0; pm < Product_Modal.size(); pm++) {
                                if (obj.getString("ProdCode").equalsIgnoreCase(Product_Modal.get(pm).getId())) {
                                    Product_Modal.get(pm).setBalance(obj.getInt("Balance"));
                                    break;
                                }
                            }
                        }
                    }*/


                    break;
                case Constants.PAYMODES:
                    payList.clear();
                    JSONObject obj = new JSONObject(apiDataResponse);
                    if (obj.getBoolean("success")) {
                        JSONArray jsonArray = obj.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataObj = jsonArray.getJSONObject(i);
                            payList.add(new Common_Model(dataObj.getString("Name"), dataObj.getString("Code")));
                        }
                    } else {
                        common_class.showMsg(this, "No Records Found");
                    }
                    if (payList.size() > 0) {
                        common_class.showCommonDialog(payList, 20, this);
                    }

                    break;


                case Constants.POS_SCHEME:
                    Log.v(TAG + "scheme:", apiDataResponse);
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

                        sharedCommonPref.save(Constants.POS_SCHEME, gson.toJson(product_details_modalArrayList));


                    } else {
                        sharedCommonPref.clear_pref(Constants.POS_SCHEME);

                    }
                    break;
                case Constants.POS_TAXList:
                    JSONObject jsonObjectTax = new JSONObject(apiDataResponse);
                    if (jsonObjectTax.getBoolean("success")) {
                        sharedCommonPref.save(Constants.POS_TAXList, apiDataResponse);

                    } else {
                        sharedCommonPref.clear_pref(Constants.POS_TAXList);

                    }
                    break;
            }
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (!Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.SCAN_DATA))) {
         //   loadScanData();
        }
        if(ProductsLoaded==true){
            initData();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                moveProductScreen();
            } else {
                common_class.commonDialog(this, SFA_Activity.class, "POS?");

                SharedPreferences.Editor editor = CartDetails.edit();
                editor.remove("Cart");
                editor.apply();
            }
            return true;
        }
        return false;
    }
    void moveProductScreen() {
        lin_gridcategory.setVisibility(View.VISIBLE);
        findViewById(R.id.llUserDetail).setVisibility(View.VISIBLE);
        findViewById(R.id.llLblUserDetail).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);
        findViewById(R.id.llBillHeader).setVisibility(View.GONE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.GONE);
        takeorder.setText("PROCEED");

        CartView =false;
        BundleProds.notifyDataSetChanged();
    }
    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 1:

                break;
            case 20:
                tvPayMode.setText("" + myDataset.get(position).getName());
                if (myDataset.get(position).getName().equalsIgnoreCase("cash"))
                    findViewById(R.id.llPayAmtDetail).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.llPayAmtDetail).setVisibility(View.GONE);
                break;

        }
    }
    public class BundleProdAdapter extends RecyclerView.Adapter<BundleProdAdapter.MyViewHolder> {
        Context context;
        MyViewHolder pholder;
        JSONArray listt;
        public BundleProdAdapter(Context applicationContext, JSONArray list) {
            this.context = applicationContext;
            listt = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.product_offer_bundle, parent, false);
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
                holder.txName.setText(listt.getJSONObject(position).getString("name"));
                JSONArray orderList=listt.getJSONObject(position).getJSONArray("Prods");
                mProdct_Adapter = new Prodct_Adapter(getApplicationContext(),orderList);
                holder.rlcyProdlist.setAdapter(mProdct_Adapter);
                holder.rlcyProdlist.setVisibility(View.VISIBLE);
               if(CartView){
                   holder.rlcyProdlist.setVisibility(View.GONE);
               }
                holder.Qty.setText(listt.getJSONObject(position).optString("BQty"));
                holder.QtyPls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";
                        holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) + 1));
                        JSONArray orderList = null ;
                        try {

                            listt.getJSONObject(position).put("BQty",holder.Qty.getText());
                            orderList = listt.getJSONObject(position).getJSONArray("Prods");
                        for (int il = 0; il < orderList.length(); il++) {
                            JSONObject jItem=listt.getJSONObject(position).getJSONArray("Prods").getJSONObject(il);
                            listt.getJSONObject(position).getJSONArray("Prods").getJSONObject(il).put("Qty", holder.Qty.getText());
                            Double sAmt =Double.parseDouble(jItem.getString("Qty").toUpperCase())*Double.parseDouble(jItem.getString("MRP").toUpperCase());
                            listt.getJSONObject(position).getJSONArray("Prods").getJSONObject(il).put("Amount",sAmt);
                            sumofTax(jItem,position);
                            SharedPreferences.Editor editor = CartDetails.edit();
                            editor.putString("Cart", listt.toString());
                            editor.apply();
                        }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        mProdct_Adapter.notifyDataSetChanged();
                        updateToTALITEMUI();
                    }
                });
                holder.QtyMns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String sVal = holder.Qty.getText().toString();
                            if (sVal.equalsIgnoreCase("")) sVal = "0";
                            if (Integer.parseInt(sVal) > 0) {
                                holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) - 1));
                            }

                            JSONArray orderList = null ;
                            try {
                                listt.getJSONObject(position).put("BQty",holder.Qty.getText());
                                orderList = listt.getJSONObject(position).getJSONArray("Prods");
                                for (int il = 0; il < orderList.length(); il++) {
                                    JSONObject jItem=listt.getJSONObject(position).getJSONArray("Prods").getJSONObject(il);
                                    listt.getJSONObject(position).getJSONArray("Prods").getJSONObject(il).put("Qty", holder.Qty.getText());
                                    Double sAmt =Double.parseDouble(jItem.getString("Qty").toUpperCase())*Double.parseDouble(jItem.getString("MRP").toUpperCase());
                                    listt.getJSONObject(position).getJSONArray("Prods").getJSONObject(il).put("Amount",sAmt);
                                    sumofTax(jItem,position);

                                    SharedPreferences.Editor editor = CartDetails.edit();
                                    editor.putString("Cart", listt.toString());
                                    editor.apply();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            mProdct_Adapter.notifyDataSetChanged();
                            updateToTALITEMUI();
                        } catch (Exception e) {
                            Log.v(TAG + "QtyMns:", e.getMessage());
                        }
                    }
                });

                if (position == selectedPos) {
                    pholder = holder;
                }
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return listt.length();
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView QtyPls, QtyMns;
            EditText Qty;
            TextView txName;
            RecyclerView rlcyProdlist;
            public MyViewHolder(View view) {
                super(view);

                txName = view.findViewById(R.id.productname);
                QtyPls = view.findViewById(R.id.ivQtyPls);
                QtyMns = view.findViewById(R.id.ivQtyMns);
                Qty = view.findViewById(R.id.Qty);

                rlcyProdlist = view.findViewById(R.id.rlcyProdlist);
            }
        }
    }
    public class Prodct_Adapter extends RecyclerView.Adapter<Prodct_Adapter.MyViewHolder> {
        Context context;
        int CategoryType;
        private JSONArray listProd;
        private int rowLayout;

        public Prodct_Adapter( Context context,JSONArray mlistProd) {
            this.listProd = mlistProd;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_bundle_recyclerview, parent, false);
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
                JSONObject jItem=listProd.getJSONObject(position);

                holder.productname.setText(jItem.getString("name").toUpperCase());
                holder.erpCode.setText(jItem.getString("ERP_Code").toUpperCase());
                holder.tvUOM.setText(jItem.getString("Default_UOM_Name").toUpperCase());
                holder.Qty.setText(jItem.getString("Qty").toUpperCase());
                holder.Rate.setText(CurrencySymbol+" " + formatter.format(Double.parseDouble(jItem.getString("MRP").toUpperCase())));

                holder.tvStock.setText(jItem.getString("Balance").toUpperCase()+" "+jItem.getString("Default_UOM_Name").toUpperCase());
                holder.tvStock.setTextColor(getResources().getColor(R.color.green));

                double bal=Double.parseDouble(jItem.getString("Balance"));
                double totQty=0;
                if((bal - (int) totQty)<=0 ) {
                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                    holder.tvStock.setTextColor(getResources().getColor(R.color.color_red));
                }

                holder.Amount.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(Double.parseDouble(jItem.getString("Amount"))));
                holder.tvTaxLabel.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(Double.parseDouble(jItem.getString("Tax"))));
                /*
                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(holder.getBindingAdapterPosition());
                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.erpCode.setText("" + Product_Details_Modal.getERP_Code().toUpperCase());
                holder.Amount.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(Product_Details_Modal.getAmount()));
                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getUOM_Nm()))
                    holder.tvUOM.setText(Product_Details_Modal.getUOM_Nm());
                else {
                    holder.tvUOM.setText(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Nm(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Id("" + Product_Details_Modal.getDefaultUOM());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setCnvQty(Product_Details_Modal.getDefaultUOMQty());
                }

                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() == null)
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setBalance(0);


                holder.Rate.setText(CurrencySymbol+" " + formatter.format(Double.parseDouble(Product_Details_Modal.getMRP()) * Product_Details_Modal.getCnvQty()));

                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getBar_Code()))
                    Log.v(TAG, "name:" + Product_Details_Modal.getName() + " :code:" + Product_Details_Modal.getBar_Code());
                double totQty= Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                holder.tvStock.setText("" + String.format("%.2f", (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()/Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty())).replaceAll(".00","") + " " + holder.tvUOM.getText());

                holder.tvTknStock.setText("" + ((int) totQty) + " EA");
                holder.tvCLStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty) + " EA");

                holder.tvTknStock.setTextColor(getResources().getColor(R.color.green));
                holder.tvCLStock.setTextColor(getResources().getColor(R.color.green));
                if((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty)<0 && StockCheck.equalsIgnoreCase("1")) {
                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                    holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));
                    holder.tvCLStock.setTextColor(getResources().getColor(R.color.color_red));
                }
                //holder.tvBatchNo.setText("Batch : "+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBatchNo());
                holder.tvTknStock.setVisibility(View.GONE);
                holder.tvCLStock.setVisibility(View.GONE);

                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() > 0)
                    holder.tvStock.setTextColor(getResources().getColor(R.color.green));
                else
                    holder.tvStock.setTextColor(getResources().getColor(R.color.color_red));

                if (CategoryType >= 0) {


                    holder.totalQty.setText("Total Qty : " + ((int)
                            (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty())));

                    if (!Product_Details_Modal.getPImage().equalsIgnoreCase("")) {
                        holder.ImgVwProd.clearColorFilter();
                        Glide.with(this.context)
                                .load(Product_Details_Modal.getPImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.ImgVwProd);
                    } else {
                        holder.ImgVwProd.setImageDrawable(getResources().getDrawable(R.drawable.product_logo));
                        holder.ImgVwProd.setColorFilter(getResources().getColor(R.color.grey_500));
                    }


                    holder.regularAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty() * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP())));

                    holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(Double.parseDouble(Product_Details_Modal.getMRP()) * Product_Details_Modal.getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()));


                    holder.rlUOM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uomPos = position;
                            uomList = new ArrayList<>();

                            String uomids="";
                            if (Product_Details_Modal.getUOMList() != null && Product_Details_Modal.getUOMList().size() > 0) {
                                for (int i = 0; i < Product_Details_Modal.getUOMList().size(); i++) {
                                    com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal.UOM uom = Product_Details_Modal.getUOMList().get(i);

                                    if((";"+uomids).toLowerCase().indexOf(";"+uom.getUOM_Id().toLowerCase()+";")<0) {
                                        uomids += uom.getUOM_Id().toLowerCase() + ";";
                                        uomList.add(new Common_Model(uom.getUOM_Nm(), uom.getUOM_Id(), "", "", String.valueOf(uom.getCnvQty())));
                                    }
                                }
                                common_class.showCommonDialog(uomList, 1, OfferCounterActivity.this);
                            } else {
                                common_class.showMsg(OfferCounterActivity.this, "No Records Found.");
                            }
                        }
                    });

                }

                holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getTax()));
                if (Product_Details_Modal.getQty() > 0)
                    holder.Qty.setText("" + Product_Details_Modal.getQty());

                if (Common_Class.isNullOrEmpty(Product_Details_Modal.getFree()))
                    holder.Free.setText("0");
                else
                    holder.Free.setText("" + Product_Details_Modal.getFree());


                holder.Disc.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modal.getDiscount()));


                holder.QtyPls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";

                        int order = (int) ((Integer.parseInt(sVal) + 1) * Product_Details_Modal.getCnvQty());
                        int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
                        if(StockCheck.equalsIgnoreCase("1") && order > balance){
                            common_class.showMsg(OfferCounterActivity.this, "Can't exceed Stock");
                        }else{
                            if (Product_Details_Modal.getCheckStock() == 1)
                                holder.tvStock.setText("" + (int) (balance - order));
                            holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) + 1));
                        }
                    }
                });
                holder.QtyMns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String sVal = holder.Qty.getText().toString();
                            if (sVal.equalsIgnoreCase("")) sVal = "0";
                            if (Integer.parseInt(sVal) > 0) {
                                holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) - 1));

                                int order = (int) ((Integer.parseInt(sVal) - 1) * Product_Details_Modal.getCnvQty());
                                int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
                                if (Product_Details_Modal.getCheckStock() == 1)
                                    holder.tvStock.setText("" + (int) (balance - order));
                            }

                        } catch (Exception e) {
                            Log.v(TAG + "QtyMns:", e.getMessage());
                        }
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

                            double totQty = (enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty());


                            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() < totQty  && StockCheck.equalsIgnoreCase("1")) {
//                                totQty = 0;
//                                enterQty = 0;
//                                holder.Qty.setText("0");
                                // common_class.showMsg(POSActivity.this, "No stock");
                                //totQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                                //enterQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty();

                                common_class.showMsg(OfferCounterActivity.this, "Can't exceed stock");
                                //holder.Qty.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty());

                            }
                            //if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() > 0)
                            //    holder.tvStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()) + " EA");
                            holder.tvStock.setText("" + String.format("%.2f", (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()/Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty())).replaceAll(".00","") + " " + holder.tvUOM.getText());

                            holder.tvTknStock.setText("" + ((int) totQty) + " EA");
                            holder.tvCLStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty) + " EA");
                            holder.tvTknStock.setVisibility(View.GONE);
                            holder.tvCLStock.setVisibility(View.GONE);
                            // holder.tvTknStock.setTextColor(getResources().getColor(R.color.green));
                            //holder.tvCLStock.setTextColor(getResources().getColor(R.color.green));
                            holder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
                            if((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty)<0 && StockCheck.equalsIgnoreCase("1")) {
                                holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                                //   holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));
                                // holder.tvCLStock.setTextColor(getResources().getColor(R.color.color_red));
                            }

                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(totQty * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP())));
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                    Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP()))));
                            if (CategoryType >= 0) {
                                holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty() * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP())));
                                holder.totalQty.setText("Total Qty : " + (int) enterQty);
                            }


                            String strSchemeList = sharedCommonPref.getvalue(Constants.POS_SCHEME);

                            Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
                            }.getType();
                            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type);

                            double highestScheme = 0;
                            boolean haveVal = false;
                            if (totQty > 0 && product_details_modalArrayList != null && product_details_modalArrayList.size() > 0) {

                                for (int i = 0; i < product_details_modalArrayList.size(); i++) {

                                    if (Product_Details_Modal.getId().equals(product_details_modalArrayList.get(i).getId())) {

                                        haveVal = true;
                                        double schemeVal = Double.parseDouble(product_details_modalArrayList.get(i).getScheme());

                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_code(product_details_modalArrayList.get(i).getOff_Pro_code());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_name(product_details_modalArrayList.get(i).getOff_Pro_name());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_Unit(product_details_modalArrayList.get(i).getOff_Pro_Unit());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree_val(product_details_modalArrayList.get(i).getFree());

                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_value(String.valueOf(product_details_modalArrayList.get(i).getDiscount()));
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_type(product_details_modalArrayList.get(i).getDiscount_type());


                                        if (totQty >= schemeVal) {

                                            if (schemeVal > highestScheme) {
                                                highestScheme = schemeVal;


                                                if (!product_details_modalArrayList.get(i).getFree().equals("0")) {
                                                    if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                        double freePer = (totQty / highestScheme);

                                                        double freeVal = freePer * Double.parseDouble(product_details_modalArrayList.
                                                                get(i).getFree());

                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(Math.round(freeVal)));
                                                    } else {
                                                        int val = (int) (totQty / highestScheme);
                                                        int freeVal = val * Integer.parseInt(product_details_modalArrayList.get(i).getFree());
                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(freeVal));
                                                    }
                                                } else {

                                                    holder.Free.setText("0");
                                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                                }


                                                if (product_details_modalArrayList.get(i).getDiscount() != 0) {

                                                    if (product_details_modalArrayList.get(i).getDiscount_type().equals("%")) {
                                                        double discountVal = totQty * (((product_details_modalArrayList.get(i).getDiscount()
                                                        )) / 100);


                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((Math.round(discountVal)));

                                                    } else {
                                                        //Rs
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (totQty / highestScheme);

                                                            double freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());

                                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((Math.round(freeVal)));
                                                        } else {
                                                            int val = (int) (totQty / highestScheme);
                                                            double freeVal = val * (product_details_modalArrayList.get(i).getDiscount());
                                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((freeVal));
                                                        }
                                                    }


                                                } else {
                                                    holder.Disc.setText(CurrencySymbol+" 0.00");
                                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);

                                                }


                                            }

                                        } else {
                                            holder.Free.setText("0");
                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                            holder.Disc.setText(CurrencySymbol+" 0.00");
                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);


                                        }


                                    }

                                }


                            }

                            if (!haveVal) {
                                holder.Free.setText("0");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                holder.Disc.setText(CurrencySymbol+" 0.00");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_code("");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_name("");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_Unit("");

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_value("0.00");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_type("");


                            } else {

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()) -
                                        (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
                                holder.Free.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getFree());
                                holder.Disc.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
                                holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            }
                            sumofTax(Product_Details_Modalitem, holder.getBindingAdapterPosition());
                            holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()));
                            updateToTALITEMUI();

                            //hide code for del also unwanted edit scenario
//                            if (CategoryType == -1) {
//                                if (holder.Amount.getText().toString().equals("₹0.00")) {
//                                    Product_Details_Modalitem.remove(position);
//                                    notifyDataSetChanged();
//                                }
//                                showFreeQtyList();
//                            }

                        } catch (Exception e) {
                            Log.v(TAG, " orderAdapter:qty " + e.getMessage());
                        }


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

                            AlertDialogBox.showDialog(OfferCounterActivity.this, "HAP SFA",
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
                updateToTALITEMUI();*/
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        private void showUOMDialog() {

            LayoutInflater inflater = LayoutInflater.from(OfferCounterActivity.this);

            final View view = inflater.inflate(R.layout.fruit_item, null);
            AlertDialog alertDialog = new AlertDialog.Builder(OfferCounterActivity.this).create();
            alertDialog.setView(view);
            alertDialog.show();

        }

        @Override
        public int getItemCount() {
            return listProd.length();
        }

        private void showDialog(Product_Details_Modal product_details_modal) {
            try {


                LayoutInflater inflater = LayoutInflater.from(OfferCounterActivity.this);

                final View view = inflater.inflate(R.layout.edittext_price_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(OfferCounterActivity.this).create();
                alertDialog.setCancelable(false);

                final EditText etComments = view.findViewById(R.id.et_addItem);
                Button btnSave = view.findViewById(R.id.btn_save);
                Button btnCancel = view.findViewById(R.id.btn_cancel);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Common_Class.isNullOrEmpty(etComments.getText().toString())) {
                            common_class.showMsg(OfferCounterActivity.this, "Empty value is not allowed");
                        } else if (Double.valueOf(etComments.getText().toString()) > Double.valueOf(product_details_modal.getMRP())) {
                            common_class.showMsg(OfferCounterActivity.this, "Enter Rate is greater than "+MRPCap);

                        } else {
                            alertDialog.dismiss();
                            product_details_modal.setRate(Double.valueOf(etComments.getText().toString()));
                            etComments.setText("");
                            notifyDataSetChanged();

                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


                alertDialog.setView(view);
                alertDialog.show();
            } catch (Exception e) {
                Log.e("OrderAdapter:dialog ", e.getMessage());
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname,erpCode, Rate, Amount, Disc, Free, RegularQty, lblRQty, productQty, regularAmt,
                    QtyAmt, totalQty, tvTaxLabel, tvUOM, tvStock,tvTknStock,tvCLStock,tvBatchNo,Qty;
            ImageView ImgVwProd;

            LinearLayout llRegular;
            LinearLayout rlUOM;

            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                erpCode = view.findViewById(R.id.erpCode);
                Rate = view.findViewById(R.id.Rate);
                Qty = view.findViewById(R.id.Qty);
                Amount = view.findViewById(R.id.Amount);
                tvTaxLabel = view.findViewById(R.id.tvTaxTotAmt);
                llRegular = view.findViewById(R.id.llRegular);
                tvUOM = view.findViewById(R.id.tvUOM);
                tvStock = view.findViewById(R.id.tvStockBal);
                tvTknStock = view.findViewById(R.id.tvTknStock);
                tvCLStock = view.findViewById(R.id.tvCLStock);
                tvBatchNo= view.findViewById(R.id.tvBatchNo);

                ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);


            }
        }


    }

    public class MinMaxInputFilter implements InputFilter {
        private double mMinValue;
        private double mMaxValue;

        private static final double MIN_VALUE_DEFAULT = Double.MIN_VALUE;
        private static final double MAX_VALUE_DEFAULT = Double.MAX_VALUE;

        public MinMaxInputFilter(Double min, Double max) {
            this.mMinValue = (min != null ? min : MIN_VALUE_DEFAULT);
            this.mMaxValue = (max != null ? max : MAX_VALUE_DEFAULT);
        }

        public MinMaxInputFilter(Integer min, Integer max) {
            this.mMinValue = (min != null ? min : MIN_VALUE_DEFAULT);
            this.mMaxValue = (max != null ? max : MAX_VALUE_DEFAULT);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String replacement = source.subSequence(start, end).toString();
                String newVal = dest.subSequence(0, dstart).toString() + replacement
                        + dest.subSequence(dend, dest.length()).toString();

                // check if there are leading zeros
                if (newVal.matches("0\\d+.*"))
                    if (TextUtils.isEmpty(source))
                        return dest.subSequence(dstart, dend);
                    else
                        return "";

                // check range
                double input = Double.parseDouble(newVal);
                if (!isInRange(mMinValue, mMaxValue, input))
                    if (TextUtils.isEmpty(source))
                        return dest.subSequence(dstart, dend);
                    else
                        return "";

                return null;
            } catch (NumberFormatException nfe) {
                Log.e("inputfilter", "parse");
            }
            return "";
        }

        private boolean isInRange(double a, double b, double c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
    /*public class Prodct_Adapter extends RecyclerView.Adapter<Prodct_Adapter.MyViewHolder> {
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

        public void notify(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int categoryType) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.CategoryType = categoryType;
            notifyDataSetChanged();

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
                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(holder.getBindingAdapterPosition());
                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.erpCode.setText("" + Product_Details_Modal.getERP_Code().toUpperCase());
                holder.Amount.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(Product_Details_Modal.getAmount()));
                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getUOM_Nm()))
                    holder.tvUOM.setText(Product_Details_Modal.getUOM_Nm());
                else {
                    holder.tvUOM.setText(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Nm(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Id("" + Product_Details_Modal.getDefaultUOM());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setCnvQty(Product_Details_Modal.getDefaultUOMQty());
                }

                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() == null)
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setBalance(0);


                holder.Rate.setText(CurrencySymbol+" " + formatter.format(Double.parseDouble(Product_Details_Modal.getMRP()) * Product_Details_Modal.getCnvQty()));

                //  holder.RegularQty.setText("" + Product_Details_Modal.getRegularQty());

                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getBar_Code()))
                    Log.v(TAG, "name:" + Product_Details_Modal.getName() + " :code:" + Product_Details_Modal.getBar_Code());
                double totQty= Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                //holder.tvStock.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance());
                holder.tvStock.setText("" + String.format("%.2f", (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()/Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty())).replaceAll(".00","") + " " + holder.tvUOM.getText());

                holder.tvTknStock.setText("" + ((int) totQty) + " EA");
                holder.tvCLStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty) + " EA");

                holder.tvTknStock.setTextColor(getResources().getColor(R.color.green));
                holder.tvCLStock.setTextColor(getResources().getColor(R.color.green));
                if((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty)<0 && StockCheck.equalsIgnoreCase("1")) {
                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                    holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));
                    holder.tvCLStock.setTextColor(getResources().getColor(R.color.color_red));
                }
                //holder.tvBatchNo.setText("Batch : "+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBatchNo());
                holder.tvTknStock.setVisibility(View.GONE);
                holder.tvCLStock.setVisibility(View.GONE);

                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() > 0)
                    holder.tvStock.setTextColor(getResources().getColor(R.color.green));
                else
                    holder.tvStock.setTextColor(getResources().getColor(R.color.color_red));

                if (CategoryType >= 0) {


                    holder.totalQty.setText("Total Qty : " + ((int)
                            (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty()/*
                             * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()* *)));

                    if (!Product_Details_Modal.getPImage().equalsIgnoreCase("")) {
                        holder.ImgVwProd.clearColorFilter();
                        Glide.with(this.context)
                                .load(Product_Details_Modal.getPImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.ImgVwProd);
                    } else {
                        holder.ImgVwProd.setImageDrawable(getResources().getDrawable(R.drawable.product_logo));
                        holder.ImgVwProd.setColorFilter(getResources().getColor(R.color.grey_500));
                    }


                    holder.regularAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty() * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP())));

                    holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(Double.parseDouble(Product_Details_Modal.getMRP()) * Product_Details_Modal.getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()));


                    holder.rlUOM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uomPos = position;
                            uomList = new ArrayList<>();

                            String uomids="";
                            if (Product_Details_Modal.getUOMList() != null && Product_Details_Modal.getUOMList().size() > 0) {
                                for (int i = 0; i < Product_Details_Modal.getUOMList().size(); i++) {
                                    com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal.UOM uom = Product_Details_Modal.getUOMList().get(i);

                                    if((";"+uomids).toLowerCase().indexOf(";"+uom.getUOM_Id().toLowerCase()+";")<0) {
                                        uomids += uom.getUOM_Id().toLowerCase() + ";";
                                        uomList.add(new Common_Model(uom.getUOM_Nm(), uom.getUOM_Id(), "", "", String.valueOf(uom.getCnvQty())));
                                    }
                                }
                                common_class.showCommonDialog(uomList, 1, OfferCounterActivity.this);
                            } else {
                                common_class.showMsg(OfferCounterActivity.this, "No Records Found.");
                            }
                        }
                    });

                }

                holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getTax()));
                if (Product_Details_Modal.getQty() > 0)
                    holder.Qty.setText("" + Product_Details_Modal.getQty());

                if (Common_Class.isNullOrEmpty(Product_Details_Modal.getFree()))
                    holder.Free.setText("0");
                else
                    holder.Free.setText("" + Product_Details_Modal.getFree());


                holder.Disc.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modal.getDiscount()));


                holder.QtyPls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";

                        int order = (int) ((Integer.parseInt(sVal) + 1) * Product_Details_Modal.getCnvQty());
                        int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
                        if(StockCheck.equalsIgnoreCase("1") && order > balance){
                            common_class.showMsg(OfferCounterActivity.this, "Can't exceed Stock");
                        }else{
                            //if ((balance >= order) || Product_Details_Modal.getCheckStock() == 0) {
                            if (Product_Details_Modal.getCheckStock() == 1)
                                holder.tvStock.setText("" + (int) (balance - order));
                            holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) + 1));
//                            } else {
//                                common_class.showMsg(POSActivity.this, "Can't exceed stock");
//                            }
                        }
                    }
                });
                holder.QtyMns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String sVal = holder.Qty.getText().toString();
                            if (sVal.equalsIgnoreCase("")) sVal = "0";
                            if (Integer.parseInt(sVal) > 0) {
                                holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) - 1));

                                int order = (int) ((Integer.parseInt(sVal) - 1) * Product_Details_Modal.getCnvQty());
                                int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
                                if (Product_Details_Modal.getCheckStock() == 1)
                                    holder.tvStock.setText("" + (int) (balance - order));
                            }

                        } catch (Exception e) {
                            Log.v(TAG + "QtyMns:", e.getMessage());
                        }
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

                            double totQty = (enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty());


                            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() < totQty  && StockCheck.equalsIgnoreCase("1")) {
//                                totQty = 0;
//                                enterQty = 0;
//                                holder.Qty.setText("0");
                                // common_class.showMsg(POSActivity.this, "No stock");
                                //totQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                                //enterQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty();

                                common_class.showMsg(OfferCounterActivity.this, "Can't exceed stock");
                                //holder.Qty.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty());

                            }
                            //if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() > 0)
                            //    holder.tvStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()) + " EA");
                            holder.tvStock.setText("" + String.format("%.2f", (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()/Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty())).replaceAll(".00","") + " " + holder.tvUOM.getText());

                            holder.tvTknStock.setText("" + ((int) totQty) + " EA");
                            holder.tvCLStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty) + " EA");
                            holder.tvTknStock.setVisibility(View.GONE);
                            holder.tvCLStock.setVisibility(View.GONE);
                                // holder.tvTknStock.setTextColor(getResources().getColor(R.color.green));
                            //holder.tvCLStock.setTextColor(getResources().getColor(R.color.green));
                            holder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
                            if((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty)<0 && StockCheck.equalsIgnoreCase("1")) {
                            holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                             //   holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));
                                // holder.tvCLStock.setTextColor(getResources().getColor(R.color.color_red));
                            }

                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(totQty * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP())));
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                    Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP()))));
                            if (CategoryType >= 0) {
                                holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty() * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP())));
                                holder.totalQty.setText("Total Qty : " + (int) /*totQty* *enterQty);
                            }


                            String strSchemeList = sharedCommonPref.getvalue(Constants.POS_SCHEME);

                            Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
                            }.getType();
                            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type);

                            double highestScheme = 0;
                            boolean haveVal = false;
                            if (totQty > 0 && product_details_modalArrayList != null && product_details_modalArrayList.size() > 0) {

                                for (int i = 0; i < product_details_modalArrayList.size(); i++) {

                                    if (Product_Details_Modal.getId().equals(product_details_modalArrayList.get(i).getId())) {

                                        haveVal = true;
                                        double schemeVal = Double.parseDouble(product_details_modalArrayList.get(i).getScheme());

                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_code(product_details_modalArrayList.get(i).getOff_Pro_code());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_name(product_details_modalArrayList.get(i).getOff_Pro_name());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_Unit(product_details_modalArrayList.get(i).getOff_Pro_Unit());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree_val(product_details_modalArrayList.get(i).getFree());

                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_value(String.valueOf(product_details_modalArrayList.get(i).getDiscount()));
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_type(product_details_modalArrayList.get(i).getDiscount_type());


                                        if (totQty >= schemeVal) {

                                            if (schemeVal > highestScheme) {
                                                highestScheme = schemeVal;


                                                if (!product_details_modalArrayList.get(i).getFree().equals("0")) {
                                                    if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                        double freePer = (totQty / highestScheme);

                                                        double freeVal = freePer * Double.parseDouble(product_details_modalArrayList.
                                                                get(i).getFree());

                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(Math.round(freeVal)));
                                                    } else {
                                                        int val = (int) (totQty / highestScheme);
                                                        int freeVal = val * Integer.parseInt(product_details_modalArrayList.get(i).getFree());
                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(freeVal));
                                                    }
                                                } else {

                                                    holder.Free.setText("0");
                                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                                }


                                                if (product_details_modalArrayList.get(i).getDiscount() != 0) {

                                                    if (product_details_modalArrayList.get(i).getDiscount_type().equals("%")) {
                                                        double discountVal = totQty * (((product_details_modalArrayList.get(i).getDiscount()
                                                        )) / 100);


                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((Math.round(discountVal)));

                                                    } else {
                                                        //Rs
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (totQty / highestScheme);

                                                            double freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());

                                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((Math.round(freeVal)));
                                                        } else {
                                                            int val = (int) (totQty / highestScheme);
                                                            double freeVal = val * (product_details_modalArrayList.get(i).getDiscount());
                                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((freeVal));
                                                        }
                                                    }


                                                } else {
                                                    holder.Disc.setText(CurrencySymbol+" 0.00");
                                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);

                                                }


                                            }

                                        } else {
                                            holder.Free.setText("0");
                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                            holder.Disc.setText(CurrencySymbol+" 0.00");
                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);


                                        }


                                    }

                                }


                            }

                            if (!haveVal) {
                                holder.Free.setText("0");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                holder.Disc.setText(CurrencySymbol+" 0.00");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_code("");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_name("");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_Unit("");

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_value("0.00");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_type("");


                            } else {

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()) -
                                        (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
                                holder.Free.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getFree());
                                holder.Disc.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
                                holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            }
                            sumofTax(Product_Details_Modalitem, holder.getBindingAdapterPosition());
                            holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()));
                            updateToTALITEMUI();

                            //hide code for del also unwanted edit scenario
//                            if (CategoryType == -1) {
//                                if (holder.Amount.getText().toString().equals("₹0.00")) {
//                                    Product_Details_Modalitem.remove(position);
//                                    notifyDataSetChanged();
//                                }
//                                showFreeQtyList();
//                            }

                        } catch (Exception e) {
                            Log.v(TAG, " orderAdapter:qty " + e.getMessage());
                        }


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

                            AlertDialogBox.showDialog(OfferCounterActivity.this, "HAP SFA",
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


//                holder.Rate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showDialog(Product_Details_Modal);
//                    }
//                });

                updateToTALITEMUI();
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        private void showUOMDialog() {

            LayoutInflater inflater = LayoutInflater.from(OfferCounterActivity.this);

            final View view = inflater.inflate(R.layout.fruit_item, null);
            AlertDialog alertDialog = new AlertDialog.Builder(OfferCounterActivity.this).create();
//            alertDialog.setTitle("HAP Check-In");
//            alertDialog.setMessage(Html.fromHtml(mMessage));
//            alertDialog.setCancelable(false);

//            TextView btnOthers = (TextView) view.findViewById(R.id.tvOthers);
//            TextView btnWeekOFF = (TextView) view.findViewById(R.id.tvWeekOff);
//            TextView btnDeviation = (TextView) view.findViewById(R.id.tvDeviation);
            alertDialog.setView(view);
            alertDialog.show();

        }

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }

        private void showDialog(Product_Details_Modal product_details_modal) {
            try {


                LayoutInflater inflater = LayoutInflater.from(OfferCounterActivity.this);

                final View view = inflater.inflate(R.layout.edittext_price_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(OfferCounterActivity.this).create();
                alertDialog.setCancelable(false);

                final EditText etComments = view.findViewById(R.id.et_addItem);
                Button btnSave = view.findViewById(R.id.btn_save);
                Button btnCancel = view.findViewById(R.id.btn_cancel);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Common_Class.isNullOrEmpty(etComments.getText().toString())) {
                            common_class.showMsg(OfferCounterActivity.this, "Empty value is not allowed");
                        } else if (Double.valueOf(etComments.getText().toString()) > Double.valueOf(product_details_modal.getMRP())) {
                            common_class.showMsg(OfferCounterActivity.this, "Enter Rate is greater than "+MRPCap);

                        } else {
                            alertDialog.dismiss();
                            product_details_modal.setRate(Double.valueOf(etComments.getText().toString()));
                            etComments.setText("");
                            notifyDataSetChanged();

                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


                alertDialog.setView(view);
                alertDialog.show();
            } catch (Exception e) {
                Log.e("OrderAdapter:dialog ", e.getMessage());
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname,erpCode, Rate, Amount, Disc, Free, RegularQty, lblRQty, productQty, regularAmt,
                    QtyAmt, totalQty, tvTaxLabel, tvUOM, tvStock,tvTknStock,tvCLStock,tvBatchNo;
            ImageView ImgVwProd, QtyPls, QtyMns,ivDel;
            EditText Qty;

            LinearLayout llRegular;
            LinearLayout rlUOM;

            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                erpCode = view.findViewById(R.id.erpCode);
                QtyPls = view.findViewById(R.id.ivQtyPls);
                QtyMns = view.findViewById(R.id.ivQtyMns);
                Rate = view.findViewById(R.id.Rate);
                Qty = view.findViewById(R.id.Qty);
                RegularQty = view.findViewById(R.id.RegularQty);
                Amount = view.findViewById(R.id.Amount);
                Free = view.findViewById(R.id.Free);
                Disc = view.findViewById(R.id.Disc);
                tvTaxLabel = view.findViewById(R.id.tvTaxTotAmt);
                llRegular = view.findViewById(R.id.llRegular);
                tvUOM = view.findViewById(R.id.tvUOM);
                tvStock = view.findViewById(R.id.tvStockBal);
                tvTknStock = view.findViewById(R.id.tvTknStock);
                tvCLStock = view.findViewById(R.id.tvCLStock);
                tvBatchNo= view.findViewById(R.id.tvBatchNo);


                if (CategoryType >= 0) {
                    ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);
                    lblRQty = view.findViewById(R.id.status);
                    regularAmt = view.findViewById(R.id.RegularAmt);
                    QtyAmt = view.findViewById(R.id.qtyAmt);
                    totalQty = view.findViewById(R.id.totalqty);
                    rlUOM = view.findViewById(R.id.rlUOM);
                }
                else {
                    ivDel = view.findViewById(R.id.ivDel);
                }


            }
        }


    }*/
}