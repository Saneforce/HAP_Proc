package com.hap.checkinproc.SFA_Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
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
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.SFA_Model_Class.Trans_Order_Details_Offline;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Invoice_Category_Select extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI, Master_Interface {
    NumberFormat formatter = new DecimalFormat("##0.00");
    //GridView categorygrid;
    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Order_Outlet_Filter;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Trans_Order_Details_Offline> InvoiceorderDetails_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    TextView  Out_Let_Name, Category_Nametext;

    CircularProgressButton takeorder;

    private RecyclerView recyclerView, categorygrid, freeRecyclerview;
    LinearLayout lin_gridcategory;
    Common_Class common_class;
    CustomListViewDialog customDialog;


    String Ukey;
    String[] strLoc;
    String Convert_Json_toString, Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "", mDCRMode;
    Shared_Common_Pref sharedCommonPref;
    EditText cashdiscount;
    Prodct_Adapter mProdct_Adapter;
    List<Product_Details_Modal> freeQty_Array_List;

    String TAG = "Invoice_Category_Select";
    DatabaseHandler db;
    private int selectedPos = 0;

    RelativeLayout rlCategoryItemSearch;
    ImageView ivClose;
    EditText etCategoryItemSearch, etRecAmt;
    private TextView tvTotalAmount;
    private double totalvalues;
    int cashDiscount;

    private Integer totalQty;
    private TextView tvBillTotItem, tvPayMode, tvDate, tvOutStanding, tvTotOutstanding, tvInvAmt, tvPayAmt;
    private double taxVal;

    RelativeLayout rlPayment;

    CheckBox cbCredit, cbCash;
    LinearLayout llPayMode;

    private List<Common_Model> payList = new ArrayList<>();

    String orderId = "";
    private LinearLayout rlAddProduct, rlCredit, rlCash;
    private int outstandAmt;
    private double payAmt;

    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_invoice__category__select_edit);
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(Invoice_Category_Select.this);
            common_class = new Common_Class(this);
            categorygrid = findViewById(R.id.category);
            takeorder = findViewById(R.id.takeorder);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categorygrid.setLayoutManager(layoutManager);

            mDCRMode = sharedCommonPref.getvalue(Shared_Common_Pref.DCRMode);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);
            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6");
            cashdiscount = findViewById(R.id.cashdiscount);
            lin_gridcategory = findViewById(R.id.lin_gridcategory);
            Out_Let_Name = findViewById(R.id.outlet_name);
            Category_Nametext = findViewById(R.id.Category_Nametext);
            rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
            ivClose = findViewById(R.id.ivClose);

            etCategoryItemSearch = findViewById(R.id.searchView);

            rlPayment = findViewById(R.id.rlPayMode);
            rlCredit = findViewById(R.id.rlPayTypeCredit);
            rlCash = findViewById(R.id.rlPayTypeCash);
            tvDate = findViewById(R.id.tvDate);
            tvPayMode = findViewById(R.id.tvPayMode);
            etRecAmt = findViewById(R.id.etRecAmt);
            cbCash = findViewById(R.id.cbCash);
            cbCredit = findViewById(R.id.cbCredit);
            llPayMode = findViewById(R.id.llPayMode);
            rlAddProduct = findViewById(R.id.rlAddProduct);
            tvOutStanding = findViewById(R.id.tvOutstanding);
            tvTotOutstanding = findViewById(R.id.tvTotOutstanding);
            tvInvAmt = findViewById(R.id.tvInvAMt);
            tvPayAmt = findViewById(R.id.tvPayAmt);

            Out_Let_Name.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            Product_ModalSetAdapter = new ArrayList<>();
            gson = new Gson();
            takeorder.setOnClickListener(this);
            rlCategoryItemSearch.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            rlPayment.setOnClickListener(this);
            rlCash.setOnClickListener(this);
            rlCredit.setOnClickListener(this);
            rlAddProduct.setOnClickListener(this);
            Category_Nametext.setOnClickListener(this);


            Ukey = Common_Class.GetEkey();
            Out_Let_Name.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            recyclerView = findViewById(R.id.orderrecyclerview);
            freeRecyclerview = findViewById(R.id.freeRecyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1");

            String OrdersTable = String.valueOf(db.getMasterData(Constants.Product_List));
            userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
            }.getType();
            Product_Modal = gson.fromJson(OrdersTable, userType);


            Invoice_Category_Select.CategoryAdapter customAdapteravail = new Invoice_Category_Select.CategoryAdapter(getApplicationContext(),
                    Category_Modal);

            categorygrid.setAdapter(customAdapteravail);


            if (Shared_Common_Pref.Invoicetoorder != null && Shared_Common_Pref.Invoicetoorder.equals("1")) {

                String orderlist = String.valueOf(db.getMasterData(Constants.TodayOrderDetails_List));
                userType = new TypeToken<ArrayList<Trans_Order_Details_Offline>>() {
                }.getType();
                InvoiceorderDetails_List = gson.fromJson(orderlist, userType);
                Log.e("ORDER_SL_NUmber", Shared_Common_Pref.TransSlNo);
                Order_Outlet_Filter = new ArrayList<>();
                for (Trans_Order_Details_Offline ivl : InvoiceorderDetails_List) {
                    if (ivl.getTransSlNo().equals(Shared_Common_Pref.TransSlNo)) {
                        Log.e("ORDER_SL_Loop", Shared_Common_Pref.TransSlNo);
                        Order_Outlet_Filter.add(new Product_Details_Modal(ivl.getProductCode(), ivl.getProductName(), 1, "1",
                                "1", "5", "i", 7.99, 1.8, ivl.getRate(), ivl.getQuantity(), ivl.getQty(), ivl.getValue()));
                    }

                }
                //Log.e("Product_Modal.size()", String.valueOf(Product_Modal.size()));
                for (int j = 0; Product_Modal.size() > j; j++) {
                    for (int i = 0; Order_Outlet_Filter.size() > i; i++) {
                        if (Product_Modal.get(j).getId().equals(Order_Outlet_Filter.get(i).getId())) {
                            Product_Modal.get(j).setQty(Order_Outlet_Filter.get(i).getQty());
                            // Log.e("SETQTY", String.valueOf(Order_Outlet_Filter.get(i).getQty()));
                            Product_Modal.get(j).setAmount(Order_Outlet_Filter.get(i).getAmount());
                            Product_Modal.get(j).setRegularQty(Order_Outlet_Filter.get(i).getRegularQty());
                        }
                    }
                }
                //GetJsonData(sharedCommonPref.getvalue(Shared_Common_Pref.Category_List), "2");
                int jki = 0;
                Log.e("Category_Before", String.valueOf(Category_Modal.size()));
                for (Category_Universe_Modal CM : Category_Modal) {
                    for (Product_Details_Modal PM : Product_Modal) {
                        if (PM.getQty() > 0 || PM.getRegularQty() > 0) {
                            if (CM.getId().equals(String.valueOf(PM.getProductCatCode())) && (PM.getQty() > 0 || PM.getRegularQty() > 0)) {
                                Category_Modal.get(jki).setColorFlag("1");
                                Log.e("Category_Modal_CAT", Category_Modal.get(jki).getColorFlag());
                            }
                        }
                    }
                    jki++;

                }
                FilterProduct("invoice", true);
            }


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


            showOrderItemList(0, "");


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


            tvDate.setText("" + Common_Class.GetDatewothouttime());


            cbCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbCredit.setChecked(false);
                        //  llPayMode.setVisibility(View.VISIBLE);


                    }
                }
            });


            cbCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbCash.setChecked(false);
                        //llPayMode.setVisibility(View.GONE);
                    }
                }
            });


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

                        tvTotOutstanding.setText("₹ " + formatter.format(outstandAmt + (totalvalues - payAmt)));

                    } catch (Exception e) {

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            if (Shared_Common_Pref.Invoicetoorder.equals("4")) {
                orderId = Shared_Common_Pref.TransSlNo;
                Shared_Common_Pref.Invoicetoorder = "2";


                String preOrderList = sharedCommonPref.getvalue(Constants.InvoiceQtyList);
                JSONObject invoiceObj = new JSONObject(preOrderList);


                if (invoiceObj.getBoolean("success")) {
                    JSONArray jsonArray = invoiceObj.getJSONArray("Data");

                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int pm = 0; pm < Product_Modal.size(); pm++) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                if (Product_Modal.get(pm).getId().equals(jsonObject1.getString("Product_Code"))) {
                                    Product_Modal.get(pm).setRegularQty(jsonObject1.getInt("Total_Qty"));
                                    Product_Modal.get(pm).setQty(jsonObject1.getInt("Total_Qty"));
                                    Product_Modal.get(pm).setAmount(jsonObject1.getDouble("Total_Amount"));
                                    Product_Modal.get(pm).setDiscount(jsonObject1.getInt("Discount"));
                                    Product_Modal.get(pm).setFree(String.valueOf(jsonObject1.getInt("free")));
                                    Product_Modal.get(pm).setRate((jsonObject1.getDouble("Rate")));


                                }


                            }
                        }
                    }


                }


                if (!Common_Class.isNullOrEmpty(preOrderList)) {
                    String taxRes = sharedCommonPref.getvalue(Constants.TAXList);


                    for (int pmTax = 0; pmTax < Product_Modal.size(); pmTax++) {
                        double wholeTax = 0;
                        if (!Common_Class.isNullOrEmpty(taxRes)) {
                            JSONObject jsonObject = new JSONObject(taxRes.toString());


                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            List<Product_Details_Modal> taxList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                if (jsonObject1.getString("Product_Detail_Code").equals(Product_Modal.get(pmTax).getId())) {

                                    if (jsonObject1.getDouble("Tax_Val") > 0) {
                                        double taxCal = ((Product_Modal.get(pmTax).getQty() * Product_Modal.get(pmTax).getRate()) - Product_Modal.get(pmTax).getDiscount()) * ((jsonObject1.getDouble("Tax_Val") / 100));
                                        wholeTax += taxCal;


                                        taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                                jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));

                                        Product_Modal.get(pmTax).setProductDetailsModal(taxList);


                                    }
                                }
                            }


                            Product_Modal.get(pmTax).setAmount(Double.valueOf(formatter.format(Product_Modal.get(pmTax).getAmount())));

                            Product_Modal.get(pmTax).setTax(String.valueOf(formatter.format(wholeTax)));


                        }
                    }
                }

            }


            common_class.getDb_310Data(Constants.OUTSTANDING, this);

        } catch (Exception e) {

            Log.e(TAG, " invoice oncreate: " + e.getMessage());

        }
    }

    private void GetJsonData(String jsonResponse, String type) {

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
                    Category_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));
                } else {
                    Route_Code = jsonObject1.optString("cluster");
                    Dirtributor_Cod = jsonObject1.optString("stockist");
                    Worktype_code = jsonObject1.optString("wtype");
                    Distributor_Name = jsonObject1.optString("StkName");
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

            if (Product_Modal.get(pm).getRegularQty() != null) {
                if (Product_Modal.get(pm).getQty() > 0) {
                    Getorder_Array_List.add(Product_Modal.get(pm));

                }
            }
        }

        if (Getorder_Array_List.size() == 0)
            Toast.makeText(getApplicationContext(), "Invoice is empty", Toast.LENGTH_SHORT).show();
        else
            FilterProduct("invoice", false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlAddProduct:
                moveProductScreen();
                break;

            case R.id.rlPayTypeCash:
                cbCash.setChecked(false);
                break;
            case R.id.rlPayTypeCredit:
                cbCredit.setChecked(false);
                break;

            case R.id.rlPayMode:

                common_class.getDb_310Data(Constants.PAYMODES, this);
                break;

            case R.id.Category_Nametext:
                findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
                findViewById(R.id.rlCategoryItemSearch).setVisibility(View.VISIBLE);
                break;
            case R.id.ivClose:
                findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
                findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);

                break;


            case R.id.takeorder:

                if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                    if (Getorder_Array_List != null && Getorder_Array_List.size() > 0) {
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
                        },100);
                    } else {
                        common_class.showMsg(this, "Your Cart is empty...");
                    }
                } else {

                    showOrderList();

                }
                break;

            case R.id.orderbutton:
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
                break;
        }
    }

    public void ResetSubmitBtn(int resetMode){
        common_class.ProgressdialogShow(0, "");
        long dely=10;
        if(resetMode!=0) dely=1000;
        if (resetMode==1){
            takeorder.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        }else {
            takeorder.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                takeorder.stopAnimation();
                takeorder.revertAnimation();
            }
        },dely);

    }
    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {

            AlertDialogBox.showDialog(Invoice_Category_Select.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {

                    JSONArray data = new JSONArray();
                    JSONObject ActivityData = new JSONObject();

                    // String Cash_Discount = (cashdiscount.getText().toString().equals("") || cashdiscount.getText().toString() == null) ? "0" : cashdiscount.getText().toString();
                    try {
                        JSONObject HeadItem = new JSONObject();
                        HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("Worktype_code", Worktype_code);
                        HeadItem.put("Town_code", sharedCommonPref.getvalue(Constants.Route_Id));
                        HeadItem.put("dcr_activity_date", Common_Class.GetDate());
                        HeadItem.put("Daywise_Remarks", "");
                        HeadItem.put("UKey", Ukey);
                        HeadItem.put("orderValue", totalvalues);
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", Shared_Common_Pref.DistributorCode);
                        OutletItem.put("stockist_name", Shared_Common_Pref.DistributorName);
                        OutletItem.put("orderValue", totalvalues);
                        OutletItem.put("CashDiscount", cashDiscount);

                        OutletItem.put("NetAmount", totalvalues);

                        OutletItem.put("No_Of_items", tvBillTotItem.getText().toString());
                        OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("TransSlNo", Shared_Common_Pref.TransSlNo);
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "invoice");
                        // OutletItem.put("outstandAmt", outstandAmt);

                        OutletItem.put("PAYAmount", etRecAmt.getText().toString());

                        if (cbCredit.isChecked())
                            OutletItem.put("payType", "Credit");
                        else
                            OutletItem.put("payType", "Cash");

                        // OutletItem.put("payType", tvPayMode.getText().toString());
                        OutletItem.put("orderId", orderId);


                        if (strLoc.length > 0) {
                            OutletItem.put("Lat", strLoc[0]);
                            OutletItem.put("Long", strLoc[1]);
                        } else {
                            OutletItem.put("Lat", "");
                            OutletItem.put("Long", "");
                        }
                        JSONArray Order_Details = new JSONArray();
                        JSONArray totTaxArr = new JSONArray();
                        ArrayList<Product_Details_Modal> totTaxList = new ArrayList<>();

                        for (int z = 0; z < Getorder_Array_List.size(); z++) {
                            JSONObject ProdItem = new JSONObject();
                            ProdItem.put("product_Name", Getorder_Array_List.get(z).getName());
                            ProdItem.put("product_code", Getorder_Array_List.get(z).getId());
                            ProdItem.put("Product_Qty", Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_RegularQty", Getorder_Array_List.get(z).getRegularQty());
                            ProdItem.put("Product_Total_Qty", Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_Amount", Getorder_Array_List.get(z).getAmount());
                            ProdItem.put("Rate", String.format("%.2f", Getorder_Array_List.get(z).getRate()));
                            ProdItem.put("free", Getorder_Array_List.get(z).getFree());
                            ProdItem.put("dis", Getorder_Array_List.get(z).getDiscount());
                            ProdItem.put("dis_value", Getorder_Array_List.get(z).getDiscount_value());
                            ProdItem.put("Off_Pro_code", Getorder_Array_List.get(z).getOff_Pro_code());
                            ProdItem.put("Off_Pro_name", Getorder_Array_List.get(z).getOff_Pro_name());
                            ProdItem.put("Off_Pro_Unit", Getorder_Array_List.get(z).getOff_Pro_Unit());
                            ProdItem.put("Off_Scheme_Unit", Getorder_Array_List.get(z).getScheme());
                            ProdItem.put("discount_type", Getorder_Array_List.get(z).getDiscount_type());
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


                                    if (totTaxList.size() == 0) {
                                        totTaxList.add(new Product_Details_Modal(label, amt));
                                    } else {

                                        boolean isDuplicate = false;
                                        for (int totTax = 0; totTax < totTaxList.size(); totTax++) {
                                            if (totTaxList.get(totTax).getTax_Type().equals(label)) {
                                                double oldAmt = totTaxList.get(totTax).getTax_Amt();
                                                isDuplicate = true;
                                                totTaxList.set(totTax, new Product_Details_Modal(label, oldAmt + amt));

                                            }
                                        }

                                        if (!isDuplicate) {
                                            totTaxList.add(new Product_Details_Modal(label, amt));

                                        }
                                    }


                                }


                            }

                            ProdItem.put("TAX_details", tax_Details);


                            Order_Details.put(ProdItem);
                        }
                        for (int i = 0; i < totTaxList.size(); i++) {
                            JSONObject totTaxObj = new JSONObject();


                            totTaxObj.put("Tax_Type", totTaxList.get(i).getTax_Type());
                            totTaxObj.put("Tax_Amt", totTaxList.get(i).getTax_Amt());
                            totTaxArr.put(totTaxObj);

                        }
                        OutletItem.put("TOT_TAX_details", totTaxArr);
                        ActivityData.put("Activity_Doctor_Report", OutletItem);
                        ActivityData.put("Order_Details", Order_Details);
                        data.put(ActivityData);
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.saveInvoice(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                        responseBodyCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    Log.e("JSON_VALUES", response.body().toString());
                                    JSONObject jsonObjects = new JSONObject(response.body().toString());
                                    String san = jsonObjects.getString("success");
                                    Log.e("Success_Message", san);
                                    if (san.equals("true")) {

                                        Toast.makeText(Invoice_Category_Select.this, "Invoice Submitted Successfully", Toast.LENGTH_SHORT).show();

                                        Shared_Common_Pref.Sync_Flag = "2";
//                                    startActivity(new Intent(getApplicationContext(), Offline_Sync_Activity.class));

                                        ResetSubmitBtn(1);
                                        startActivity(new Intent(getApplicationContext(), Invoice_History.class));
                                        finish();
                                    }

                                } catch (Exception e) {
                                    Log.e(TAG, "invcatch: " + e.getMessage());
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ResetSubmitBtn(2);
                    }
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();ResetSubmitBtn(0);
                }
            });
        } else {
            Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
            ResetSubmitBtn(0);
        }
    }

    private void FilterProduct(String StringFlag, boolean flag) {


        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);
        rlAddProduct.setVisibility(View.VISIBLE);
        lin_gridcategory.setVisibility(View.GONE);
        takeorder.setText("SUBMIT");

        mProdct_Adapter = new Prodct_Adapter(Getorder_Array_List, R.layout.invoice_pay_recyclerview_edit, getApplicationContext(), -1);
        recyclerView.setAdapter(mProdct_Adapter);
        new Prodct_Adapter(Getorder_Array_List, R.layout.invoice_pay_recyclerview_edit, getApplicationContext(), -1).notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(Product_Modal.size());


        showFreeQtyList();


    }

    void showFreeQtyList() {
        freeQty_Array_List = new ArrayList<>();
        freeQty_Array_List.clear();

        for (Product_Details_Modal pm : Product_Modal) {

            if (pm.getRegularQty() != null) {
                if (!Common_Class.isNullOrEmpty(pm.getFree()) && !pm.getFree().equals("0")) {
                    freeQty_Array_List.add(pm);

                }
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
        TextView tvTotalItems = findViewById(R.id.tvTotalItems);
        TextView tvTotLabel = findViewById(R.id.tvTotLabel);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        TextView tvTax = findViewById(R.id.tvTaxVal);


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

        for (int pm = 0; pm < Product_Modal.size(); pm++) {

            if (Product_Modal.get(pm).getRegularQty() != null) {
                if (Product_Modal.get(pm).getQty() > 0 /*|| Product_Modal.get(pm).getRegularQty() > 0*/) {


                    cashDiscount += (int) Product_Modal.get(pm).getDiscount();

                    totalvalues += Double.valueOf(formatter.format(Product_Modal.get(pm).getAmount()));

                    totalQty += Product_Modal.get(pm).getQty()/* + Product_Modal.get(pm).getRegularQty()*/;

                    if (!Common_Class.isNullOrEmpty(Product_Modal.get(pm).getTax()))
                        taxVal += Double.parseDouble(Product_Modal.get(pm).getTax());


                    Getorder_Array_List.add(Product_Modal.get(pm));


                }
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
        //  tvPayAmount.setText("" + (int) totalvalues);

        tvInvAmt.setText("₹ " + formatter.format(totalvalues));
        tvPayAmt.setText("₹ " + formatter.format(totalvalues + outstandAmt));

        tvTotOutstanding.setText("₹ " + formatter.format(outstandAmt + (totalvalues - payAmt)));


        if (cashDiscount > 0) {
            tvSaveAmt.setVisibility(View.GONE);
            tvSaveAmt.setText("You will save ₹ " + cashDiscount + " on this order");
        } else
            tvSaveAmt.setVisibility(View.GONE);


    }

    public void showOrderItemList(int categoryPos, String filterString) {

        Product_ModalSetAdapter.clear();
        for (Product_Details_Modal personNpi : Product_Modal) {
            if (personNpi.getProductCatCode().toString().equals(listt.get(categoryPos).getId())) {
                if (Common_Class.isNullOrEmpty(filterString))
                    Product_ModalSetAdapter.add(personNpi);
                else {
                    if (personNpi.getName().toLowerCase().contains(filterString.toLowerCase()))
                        Product_ModalSetAdapter.add(personNpi);

                }
            }
        }

        Category_Nametext.setText(listt.get(categoryPos).getName());

        mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_invoice_recyclerview, getApplicationContext(), categoryPos);

        recyclerView.setAdapter(mProdct_Adapter);
        new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_invoice_recyclerview, getApplicationContext(), categoryPos).notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(Product_ModalSetAdapter.size());

    }

    @Override
    public void onLoadFilterData(List<Retailer_Modal_List> retailer_modal_list) {

    }

    @Override
    public void onLoadTodayOrderList(List<OutletReport_View_Modal> outletReportViewModals) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            JSONObject jsonObject = new JSONObject(apiDataResponse);


            switch (key) {
                case Constants.OUTSTANDING:


                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            outstandAmt = jsonArray.getJSONObject(i).getInt("Outstanding");
                            tvOutStanding.setText("₹ " + formatter.format(jsonArray.getJSONObject(i).getInt("Outstanding")));
                        }

                    } else {

                        outstandAmt = 0;
                        tvOutStanding.setText("₹ " + 0.00);
                    }

                    tvTotOutstanding.setText("₹ " + tvOutStanding.getText().toString());
                    break;
                case Constants.PAYMODES:

                    payList.clear();

                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataObj = jsonArray.getJSONObject(i);
                            payList.add(new Common_Model(dataObj.getString("Name"), dataObj.getString("Code")));
                        }

                    } else {
                        common_class.showMsg(this, "No Records Found");
                    }
                    if (payList.size() > 0) {

                        customDialog = new CustomListViewDialog(Invoice_Category_Select.this, payList, 1);
                        Window windoww = customDialog.getWindow();
                        windoww.setGravity(Gravity.CENTER);
                        windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        customDialog.show();
                    }

                    break;

            }
        } catch (Exception e) {

        }

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        tvPayMode.setText("" + myDataset.get(position).getName());
    }

    public class CategoryAdapter extends RecyclerView.Adapter<Invoice_Category_Select.CategoryAdapter.MyViewHolder> {

        Context context;
        Invoice_Category_Select.CategoryAdapter.MyViewHolder pholder;

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


        public CategoryAdapter(Context applicationContext, List<Category_Universe_Modal> list) {
            this.context = applicationContext;
            listt = list;
        }

        @Override
        public Invoice_Category_Select.CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.category_order_horizantal_universe_gridview, parent, false);
            return new Invoice_Category_Select.CategoryAdapter.MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(Invoice_Category_Select.CategoryAdapter.MyViewHolder holder, int position) {
            try {


                holder.icon.setText(listt.get(holder.getAdapterPosition()).getName());
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
                            pholder.icon.setTextColor(getResources().getColor(R.color.grey_500));
                            pholder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                            pholder.undrCate.setVisibility(View.GONE);
                        }
                        pholder = holder;
                        selectedPos = holder.getAdapterPosition();
                        showOrderItemList(holder.getAdapterPosition(), "");

                        holder.icon.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                        holder.undrCate.setVisibility(View.VISIBLE);
                    }
                });


                if (position == selectedPos) {

                    holder.icon.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.undrCate.setVisibility(View.VISIBLE);
                    pholder = holder;
                } else {
                    holder.icon.setTextColor(getResources().getColor(R.color.grey_500));
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


    }

    public class Prodct_Adapter extends RecyclerView.Adapter<Prodct_Adapter.MyViewHolder> {
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;

        Context context;

        int CategoryType;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, Disc, Free, RegularQty, lblRQty, productQty, regularAmt,
                    QtyAmt, totalQty, tvTaxLabel;
            ImageView ImgVwProd, QtyPls, QtyMns;
            EditText Qty;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                QtyPls = view.findViewById(R.id.ivQtyPls);
                QtyMns = view.findViewById(R.id.ivQtyMns);
                Rate = view.findViewById(R.id.Rate);
                Qty = view.findViewById(R.id.Qty);
                RegularQty = view.findViewById(R.id.RegularQty);
                Amount = view.findViewById(R.id.Amount);
                Free = view.findViewById(R.id.Free);
                Disc = view.findViewById(R.id.Disc);
                tvTaxLabel = view.findViewById(R.id.tvTaxLabel);


                if (CategoryType >= 0) {
                    ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);
                    lblRQty = view.findViewById(R.id.status);
                    regularAmt = view.findViewById(R.id.RegularAmt);
                    QtyAmt = view.findViewById(R.id.qtyAmt);
                    totalQty = view.findViewById(R.id.totalqty);
                }


            }
        }


        public Prodct_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int categoryType) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.CategoryType = categoryType;

        }

        @Override
        public Prodct_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        public void onBindViewHolder(Prodct_Adapter.MyViewHolder holder, int position) {
            try {


                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(holder.getAdapterPosition());


                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.Rate.setText("₹" + formatter.format(Product_Details_Modal.getRate()));
                holder.Amount.setText("₹" + new DecimalFormat("##0.00").format(Product_Details_Modal.getAmount()));
                holder.RegularQty.setText("" + Product_Details_Modal.getRegularQty());


                if (CategoryType >= 0) {
                    holder.totalQty.setText("Total Qty : " + ((Product_Details_Modalitem.get(holder.getAdapterPosition()).getQty())));

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


                    holder.regularAmt.setText("₹" + new DecimalFormat("##0.00").format(Product_Details_Modal.getRegularQty() * Product_Details_Modalitem.get(holder.getAdapterPosition()).getRate()));

                    holder.QtyAmt.setText("₹" + formatter.format(Product_Details_Modal.getRate() * Product_Details_Modal.getQty()));


                }

                if (Common_Class.isNullOrEmpty(Product_Details_Modal.getTax()))
                    holder.tvTaxLabel.setText("₹0.00");
                else
                    holder.tvTaxLabel.setText("₹" + Product_Details_Modal.getTax());

                holder.Qty.setText("" + Product_Details_Modal.getQty());


                if (Common_Class.isNullOrEmpty(Product_Details_Modal.getFree()))
                    holder.Free.setText("0");
                else
                    holder.Free.setText("" + Product_Details_Modal.getFree());


                holder.Disc.setText("₹" + formatter.format(Product_Details_Modal.getDiscount()));


                holder.QtyPls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";
                        holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) + 1));
                    }
                });
                holder.QtyMns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";
                        if (Integer.parseInt(sVal) > 0) {
                            holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) - 1));
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

                            double totQty = (enterQty);


                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText("₹" + new DecimalFormat("##0.00").format(totQty * Product_Details_Modalitem.get(holder.getAdapterPosition()).getRate()));
                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                    Product_Details_Modalitem.get(holder.getAdapterPosition()).getRate())));
                            if (CategoryType >= 0) {
                                holder.QtyAmt.setText("₹" + formatter.format(enterQty * Product_Details_Modalitem.get(holder.getAdapterPosition()).getRate()));
                                holder.totalQty.setText("Total Qty : " + (int) totQty);
                            }


                            String strSchemeList = sharedCommonPref.getvalue(Constants.FreeSchemeDiscList);

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

                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setTax("0.00");
                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setTax_value("0.00");
                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setCGST(0.00);
                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setSGST(0.00);
                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setIGST(0.00);

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


                                                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount((Math.round(discountVal)));

                                                    } else {
                                                        //Rs
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (totQty / highestScheme);

                                                            double freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());

                                                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount((Math.round(freeVal)));
                                                        } else {
                                                            int val = (int) (totQty / highestScheme);
                                                            int freeVal = (int) (val * (product_details_modalArrayList.get(i).getDiscount()));
                                                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setDiscount((freeVal));
                                                        }
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


                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setTax("0.00");
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setTax_value("0.00");
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setCGST(0.00);
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setSGST(0.00);
                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setIGST(0.00);

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


                            String taxRes = sharedCommonPref.getvalue(Constants.TAXList);

                            if (!Common_Class.isNullOrEmpty(taxRes)) {
                                JSONObject jsonObject = new JSONObject(taxRes.toString());
                                List<Product_Details_Modal> taxList = new ArrayList<>();


                                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                double wholeTax = 0;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    if (jsonObject1.getString("Product_Detail_Code").equals(Product_Details_Modalitem.get(holder.getAdapterPosition()).getId())) {

                                        if (jsonObject1.getDouble("Tax_Val") > 0) {
                                            double taxCal = Product_Details_Modalitem.get(holder.getAdapterPosition()).getAmount() *
                                                    ((jsonObject1.getDouble("Tax_Val") / 100));

                                            wholeTax += taxCal;

                                            taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                                    jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));

                                        }
                                    }
                                }

                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setProductDetailsModal(taxList);

                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(holder.getAdapterPosition()).getAmount()
                                        + wholeTax)));

                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setTax(String.valueOf(formatter.format(wholeTax)));
                                holder.Amount.setText("₹" + formatter.format(Product_Details_Modalitem.get(holder.getAdapterPosition()).getAmount()));


                                holder.tvTaxLabel.setText("₹" + Product_Details_Modal.getTax());


                            }


                            updateToTALITEMUI();


                            if (CategoryType == -1) {
                                String amt = holder.Amount.getText().toString();
                                if (amt.equals("₹0.00")) {
                                    Product_Details_Modalitem.remove(position);
                                    notifyDataSetChanged();
                                }
                                showFreeQtyList();
                            }

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
                holder.Rate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(Product_Details_Modal);
                    }
                });

                updateToTALITEMUI();
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        private void showDialog(Product_Details_Modal product_details_modal) {
            try {


                LayoutInflater inflater = LayoutInflater.from(Invoice_Category_Select.this);

                final View view = inflater.inflate(R.layout.edittext_price_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(Invoice_Category_Select.this).create();
                alertDialog.setCancelable(false);

                final EditText etComments = (EditText) view.findViewById(R.id.et_addItem);
                Button btnSave = (Button) view.findViewById(R.id.btn_save);
                Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Common_Class.isNullOrEmpty(etComments.getText().toString())) {
                            common_class.showMsg(Invoice_Category_Select.this, "Empty value is not allowed");
                        } else if (Double.valueOf(etComments.getText().toString()) > Double.valueOf(product_details_modal.getMRP())) {
                            common_class.showMsg(Invoice_Category_Select.this, "Enter Rate is greater than MRP");

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

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }


    }

    public class Free_Adapter extends RecyclerView.Adapter<Free_Adapter.MyViewHolder> {
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;

        Context context;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, tvDisc, Free, RegularQty, productQty, totalQty, tvTax;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);

                Free = view.findViewById(R.id.Free);

            }
        }


        public Free_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int Categorycolor) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;


        }

        @Override
        public Free_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new Free_Adapter.MyViewHolder(view);
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
        public void onBindViewHolder(Free_Adapter.MyViewHolder holder, int position) {
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


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                moveProductScreen();
            } else {

                common_class.CommonIntentwithFinish(Invoice_History.class);
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
        rlAddProduct.setVisibility(View.GONE);
        findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
        takeorder.setText("PROCEED TO CART");
        showOrderItemList(selectedPos, "");


    }
}