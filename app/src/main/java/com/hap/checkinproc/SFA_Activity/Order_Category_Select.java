package com.hap.checkinproc.SFA_Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Category_Select extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    GridView categorygrid;
    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Order_Outlet_Filter;
    List<com.hap.checkinproc.SFA_Model_Class.RegularQty_Modal> RegularQty_Modal;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Trans_Order_Details_Offline> InvoiceorderDetails_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    TextView takeorder, ok, back, Out_Let_Name, Category_Nametext, totalqty, totalvalue, orderbutton, netamount,
            tvOtherBrand, tvQPS, tvPOP, tvCoolerInfo;
    /* @Inject
     Retrofit retrofit;*/
    private RecyclerView recyclerView;
    LinearLayout lin_orderrecyclerview, lin_gridcategory, totalorderbottom, linnetamount, linnercashdiscount;
    public boolean gobackflag = false;
    Common_Class common_class;
    JSONObject ProductJson_Object;
    JSONObject Activity_Report_APP_Object, Activity_Outlet_Report_object, Product_Details_Object,
            eventCapturesObjectArray, pendingBillObjectArray, ComProductObjectArray, Input_Report, Trans_Order_Details_Object;
    JSONArray sendtoserverArray;
    String Ukey;
    String[] strLoc;
    String Convert_Json_toString, Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "", mDCRMode;
    Shared_Common_Pref sharedCommonPref;
    EditText cashdiscount;
    Prodct_Adapter mProdct_Adapter;
    Pay_Adapter mPay_Adapter;


    String TAG = "Order_Category_Select";
    DatabaseHandler db;
    private int selectedPos = 0;

    RelativeLayout rlCategoryItemSearch;
    ImageView ivClose;
    EditText etCategoryItemSearch;
    private TextView tvTotalAmount;
    private double totalvalues, taxVal, totCGST, totSGST, totIGST;
    private Integer totalQty;
    private TextView tvBillTotItem;
    int cashDiscount;


    NumberFormat formatter = new DecimalFormat("##0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_order__category__select);
            //  ((MyApplication) getApplication()).getNetComponent().inject(this);
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(Order_Category_Select.this);
            common_class = new Common_Class(this);
            categorygrid = findViewById(R.id.category);
            takeorder = findViewById(R.id.takeorder);
            orderbutton = findViewById(R.id.orderbutton);
            netamount = findViewById(R.id.netamount);
            ok = findViewById(R.id.ok);
            back = findViewById(R.id.back);
            mDCRMode = sharedCommonPref.getvalue(Shared_Common_Pref.DCRMode);
            //   GetJsonData(sharedCommonPref.getvalue(Shared_Common_Pref.Todaydayplanresult), "6");
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);
            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6");
            lin_orderrecyclerview = findViewById(R.id.lin_orderrecyclerview);
            totalorderbottom = findViewById(R.id.totalorderbottom);
            cashdiscount = findViewById(R.id.cashdiscount);
            linnetamount = findViewById(R.id.linnetamount);
            linnercashdiscount = findViewById(R.id.linnercashdiscount);
            lin_gridcategory = findViewById(R.id.lin_gridcategory);
            totalqty = findViewById(R.id.totalqty);
            totalvalue = findViewById(R.id.totalvalue);
            Out_Let_Name = findViewById(R.id.outlet_name);
            Category_Nametext = findViewById(R.id.Category_Nametext);
            rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
            ivClose = findViewById(R.id.ivClose);

            tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
            tvPOP = (TextView) findViewById(R.id.tvPOP);
            tvQPS = (TextView) findViewById(R.id.tvQPS);
            tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
            etCategoryItemSearch = findViewById(R.id.searchView);


            Out_Let_Name.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            Product_ModalSetAdapter = new ArrayList<>();
            gson = new Gson();
            ok.setOnClickListener(this);
            takeorder.setOnClickListener(this);
            back.setOnClickListener(this);
            orderbutton.setOnClickListener(this);
            rlCategoryItemSearch.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            Ukey = Common_Class.GetEkey();
            Out_Let_Name.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            recyclerView = findViewById(R.id.orderrecyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            //  GetJsonData(sharedCommonPref.getvalue(Shared_Common_Pref.Category_List), "1");
            GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1");
            // String OrdersTable = sharedCommonPref.getvalue(Shared_Common_Pref.Product_List);
            String OrdersTable = String.valueOf(db.getMasterData(Constants.Product_List));
            userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
            }.getType();
            Product_Modal = gson.fromJson(OrdersTable, userType);
            //156
            if (Shared_Common_Pref.Invoicetoorder == null || Shared_Common_Pref.Invoicetoorder.equals("0")) {
                //  Get_regularqty();
            }

            LinearLayout llGridParent = findViewById(R.id.lin_gridcategory);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) llGridParent.getLayoutParams();
// Changes the height and width to the specified *pixels*
            params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            params.width = 210 * Category_Modal.size();
            // params.setMargins(5, 5, 5, 5);
            llGridParent.setLayoutParams(params);


            Order_Category_Select.CategoryAdapter customAdapteravail = new Order_Category_Select.CategoryAdapter(getApplicationContext(),
                    Category_Modal);
            //categorygrid.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

            categorygrid.setNumColumns(Category_Modal.size());

            categorygrid.setAdapter(customAdapteravail);


            if (Shared_Common_Pref.Invoicetoorder != null) {
                if (Shared_Common_Pref.Invoicetoorder.equals("1")) {
                    ok.setText("Edit");
                    // String orderlist = sharedCommonPref.getvalue(Shared_Common_Pref.TodayOrderDetails_List);
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
                          /*  Log.e("GETQTY", String.valueOf(PM.getQty()));
                            Log.e("GETREgular", String.valueOf(PM.getRegularQty()));
                            Log.e("Category_Universe_Modal", CM.getId());
                            Log.e("Product_Details_Modal", String.valueOf(PM.getProductCatCode()));*/
                                if (CM.getId().equals(String.valueOf(PM.getProductCatCode())) && (PM.getQty() > 0 || PM.getRegularQty() > 0)) {
                                    Category_Modal.get(jki).setColorFlag("1");
                                    Log.e("Category_Modal_CAT", Category_Modal.get(jki).getColorFlag());
                                }
                            }
                        }
                        jki++;

                    }
                    FilterProduct("invoice", true);
                } else {
                    ok.setText("Ok");
                }
            } else {
                ok.setText("OK");
            }

            cashdiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals("")) {
                        if (Double.valueOf(s.toString()) > 0) {
                            Double totalamount = Double.valueOf(totalvalue.getText().toString());
                            if (Double.valueOf(s.toString()) > Double.valueOf(totalvalue.getText().toString())) {
                                Toast.makeText(Order_Category_Select.this, "Discount Exceeded", Toast.LENGTH_SHORT).show();
                                cashdiscount.setText("");
                                netamount.setText("" + totalvalue.getText().toString());
                            } else {
                                Double discountvalues = totalamount - Double.valueOf(s.toString());
                                netamount.setText("" + discountvalues);
                            }
                        }
                    } else {
                        netamount.setText("" + totalvalue.getText().toString());
                    }
                }
            });


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


            showOrderItemList(0);

            tvOtherBrand.setOnClickListener(this);
            tvQPS.setOnClickListener(this);
            tvPOP.setOnClickListener(this);
            tvCoolerInfo.setOnClickListener(this);

            findViewById(R.id.tvOrder).setVisibility(View.GONE);


            etCategoryItemSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    showOrderFilterItem(selectedPos, s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            String preOrderList = sharedCommonPref.getvalue(Constants.PreOrderQtyList);

            Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
            }.getType();
            List<Product_Details_Modal> productList = gson.fromJson(preOrderList, type);


            for (int pm = 0; pm < Product_Modal.size(); pm++) {

                if (productList != null && productList.size() > 0) {

                    for (int k = 0; k < productList.size(); k++) {

                        if (Product_Modal.get(pm).getId().equals(productList.get(k).getId())) {

                            Product_Modal.get(pm).setRegularQty(productList.get(k).getQty());

                            Product_Modal.get(pm).setAmount(Double.valueOf(formatter.format(Product_Modal.get(pm).getRegularQty() * Product_Modal.get(pm).getRate())));


                            double enterQty = Product_Modal.get(pm).getRegularQty();
                            String strSchemeList = sharedCommonPref.getvalue(Constants.FreeSchemeDiscList);

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


                                                        Product_Modal.get(pm).setDiscount((Math.round(discountVal)));

                                                    } else {
                                                        //Rs
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (enterQty / highestScheme);

                                                            double freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());

                                                            Product_Modal.get(pm).setDiscount((Math.round(freeVal)));
                                                        } else {
                                                            int val = (int) (enterQty / highestScheme);
                                                            int freeVal = (int) (val * (product_details_modalArrayList.get(i).getDiscount()));
                                                            Product_Modal.get(pm).setDiscount((freeVal));
                                                        }
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


                        }
                    }
                }
            }


            String taxRes = sharedCommonPref.getvalue(Constants.TAXList);


            for (int pmTax = 0; pmTax < Product_Modal.size(); pmTax++) {
                double wholeTax = 0;
                if (!Common_Class.isNullOrEmpty(taxRes)) {
                    JSONObject jsonObject = new JSONObject(taxRes.toString());


                    JSONArray jsonArray = jsonObject.getJSONArray("Data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        if (jsonObject1.getString("Product_Detail_Code").equals(Product_Modal.get(pmTax).getId())) {

                            if (jsonObject1.getDouble("Tax_Val") > 0) {
                                double taxCal = Product_Modal.get(pmTax).getAmount() * ((jsonObject1.getDouble("Tax_Val") / 100));
                                wholeTax += taxCal;

                                switch (jsonObject1.getString("Tax_Type")) {
                                    case "CGST":
                                        Product_Modal.get(pmTax).setCGST(taxCal);
                                        break;
                                    case "SGST":
                                        Product_Modal.get(pmTax).setCGST(taxCal);
                                        break;
                                    case "IGST":
                                        Product_Modal.get(pmTax).setCGST(taxCal);
                                        break;
                                }


                            }
                        }
                    }


                    Product_Modal.get(pmTax).setAmount(Double.valueOf(formatter.format(Product_Modal.get(pmTax).getAmount()
                            + wholeTax)));

                    Product_Modal.get(pmTax).setTax(String.valueOf(formatter.format(wholeTax)));


                }
            }


        } catch (Exception e) {
            Log.v(TAG, " order oncreate: " + e.getMessage());

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
//        for (Product_Details_Modal pm : Product_Modal) {
//            System.out.println("Product_getQty" + pm.getQty());
//            System.out.println("Product_getQty" + pm.getRegularQty());
//            if (pm.getRegularQty() != null) {
//                if (pm.getQty() > 0 || pm.getRegularQty() > 0) {
//                    Getorder_Array_List.add(pm);
//
//                }
//            }
//        }


        for (int pm = 0; pm < Product_Modal.size(); pm++) {

            if (Product_Modal.get(pm).getRegularQty() != null) {
                if (Product_Modal.get(pm).getQty() > 0 || Product_Modal.get(pm).getRegularQty() > 0) {
                    Getorder_Array_List.add(Product_Modal.get(pm));

                }
            }
        }

        if (Getorder_Array_List.size() == 0)
            Toast.makeText(getApplicationContext(), "Order is empty", Toast.LENGTH_SHORT).show();
        else
            FilterProduct("order", false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Category_Nametext:
                findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
                findViewById(R.id.rlCategoryItemSearch).setVisibility(View.VISIBLE);
                break;
            case R.id.ivClose:
                findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
                findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);

                break;

            case R.id.tvOtherBrand:
                common_class.CommonIntentwithFinish(OtherBrandActivity.class);
                break;
            case R.id.tvQPS:
                common_class.CommonIntentwithFinish(QPSActivity.class);
                break;
            case R.id.tvPOP:
                common_class.CommonIntentwithFinish(POPActivity.class);
                break;
            case R.id.tvCoolerInfo:
                common_class.CommonIntentwithFinish(CoolerInfoActivity.class);
                break;

            case R.id.takeorder:

                if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
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
                    if (Shared_Common_Pref.Invoicetoorder != null) {
                        if (Shared_Common_Pref.Invoicetoorder.equals("1")) {
                            FilterProduct("invoice", true);
                        } else if (Shared_Common_Pref.Invoicetoorder.equals("2")) {
                            FilterProduct("invoice", true);
                        } else {
                            showOrderList();
                        }
                    } else {
                        showOrderList();
                    }
                }
                break;
            case R.id.ok:
                try {
                    lin_gridcategory.setVisibility(View.VISIBLE);
                    lin_orderrecyclerview.setVisibility(View.GONE);
                    totalorderbottom.setVisibility(View.GONE);
                    orderbutton.setVisibility(View.GONE);
                    gobackflag = false;
                    takeorder.setVisibility(View.VISIBLE);
                    findViewById(R.id.rlTakeOrder).setVisibility(View.VISIBLE);


                    Order_Category_Select.CategoryAdapter customAdapteravail = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
                    categorygrid.setAdapter(customAdapteravail);

                    ok.setVisibility(View.INVISIBLE);


                } catch (Exception e) {
                    Log.e(TAG + " ok button click: ", e.getMessage());
                }
                break;
            case R.id.back:
                if (gobackflag == false) {
                    common_class.CommonIntentwithFinish(Invoice_History.class);
                } else {
                    gobackflag = false;
                    lin_gridcategory.setVisibility(View.VISIBLE);
                    lin_orderrecyclerview.setVisibility(View.GONE);
                    totalorderbottom.setVisibility(View.GONE);
                    orderbutton.setVisibility(View.GONE);
                    ok.setVisibility(View.VISIBLE);
                    takeorder.setVisibility(View.VISIBLE);
                    findViewById(R.id.rlTakeOrder).setVisibility(View.VISIBLE);
                    Order_Category_Select.CategoryAdapter customAdapteravaill = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
                    categorygrid.setAdapter(customAdapteravaill);
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

    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {

            AlertDialogBox.showDialog(Order_Category_Select.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {

                    JSONArray data = new JSONArray();
                    JSONObject ActivityData = new JSONObject();

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar calobj = Calendar.getInstance();
                    String dateTime = df.format(calobj.getTime());

                    // String Cash_Discount = (cashdiscount.getText().toString().equals("") || cashdiscount.getText().toString() == null) ? "0" : cashdiscount.getText().toString();
                    try {
                        JSONObject HeadItem = new JSONObject();
                        HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("Worktype_code", Worktype_code);
                        HeadItem.put("Town_code", sharedCommonPref.getvalue(Constants.Route_Id));
                        HeadItem.put("dcr_activity_date", dateTime);
                        HeadItem.put("Daywise_Remarks", "");
                        HeadItem.put("UKey", Ukey);
                        HeadItem.put("orderValue", totalvalue.getText().toString());
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", Shared_Common_Pref.DistributorCode);
                        OutletItem.put("stockist_name", Shared_Common_Pref.DistributorName);
                        OutletItem.put("orderValue", totalvalue.getText().toString());
                        OutletItem.put("CashDiscount", cashDiscount);
                        OutletItem.put("CGST_TOT", totCGST);
                        OutletItem.put("SGST_TOT", totSGST);
                        OutletItem.put("IGST_TOT", totIGST);

                        //  OutletItem.put("TAXAmount", taxVal);

                        OutletItem.put("NetAmount", totalvalues);
                        OutletItem.put("No_Of_items", tvBillTotItem.getText().toString());
                        OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("TransSlNo", Shared_Common_Pref.TransSlNo);
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "order");

                        if (strLoc.length > 0) {
                            OutletItem.put("Lat", strLoc[0]);
                            OutletItem.put("Long", strLoc[1]);
                        } else {
                            OutletItem.put("Lat", "");
                            OutletItem.put("Long", "");
                        }
                        ActivityData.put("Activity_Doctor_Report", OutletItem);
                        JSONArray Order_Details = new JSONArray();
                        for (int z = 0; z < Getorder_Array_List.size(); z++) {
                            JSONObject ProdItem = new JSONObject();
                            ProdItem.put("product_Name", Getorder_Array_List.get(z).getName());
                            ProdItem.put("product_code", Getorder_Array_List.get(z).getId());
                            ProdItem.put("Product_Qty", Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_RegularQty", Getorder_Array_List.get(z).getRegularQty());
                            ProdItem.put("Product_Total_Qty", Getorder_Array_List.get(z).getQty() +
                                    Getorder_Array_List.get(z).getRegularQty());
                            ProdItem.put("Product_Amount", Getorder_Array_List.get(z).getAmount());
                            ProdItem.put("Rate", String.format("%.2f", Getorder_Array_List.get(z).getRate()));

                            ProdItem.put("free", Getorder_Array_List.get(z).getFree());
                            ProdItem.put("dis", Getorder_Array_List.get(z).getDiscount());
                            ProdItem.put("dis_value", Getorder_Array_List.get(z).getDiscount_value());
                            ProdItem.put("CGST", Getorder_Array_List.get(z).getCGST());
                            ProdItem.put("SGST", Getorder_Array_List.get(z).getSGST());
                            ProdItem.put("IGST", Getorder_Array_List.get(z).getIGST());
                            ProdItem.put("Off_Pro_code", Getorder_Array_List.get(z).getOff_Pro_code());
                            ProdItem.put("Off_Pro_name", Getorder_Array_List.get(z).getOff_Pro_name());
                            ProdItem.put("Off_Pro_Unit", Getorder_Array_List.get(z).getOff_Pro_Unit());
                            ProdItem.put("Off_Scheme_Unit", Getorder_Array_List.get(z).getScheme());
                            ProdItem.put("discount_type", Getorder_Array_List.get(z).getDiscount_type());


                            Order_Details.put(ProdItem);
                        }
                        ActivityData.put("Order_Details", Order_Details);
                        data.put(ActivityData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> responseBodyCall = apiInterface.saveCalls(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
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
                                        if (Shared_Common_Pref.Invoicetoorder.equals("0")) {
                                            Toast.makeText(Order_Category_Select.this, "Order Submitted Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Order_Category_Select.this, "Invoice Submitted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        Shared_Common_Pref.Sync_Flag = "2";
//                                    startActivity(new Intent(getApplicationContext(), Offline_Sync_Activity.class));

                                        startActivity(new Intent(getApplicationContext(), Invoice_History.class));
                                        finish();
                                    }

                                } catch (Exception e) {

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("SUBMIT_VALUE", "ERROR");
                        }
                    });

                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void FilterProduct(String StringFlag, boolean flag) {
        boolean checkavail = flag;
        if (StringFlag.equals("order")) {
            linnercashdiscount.setVisibility(View.GONE);
            linnetamount.setVisibility(View.GONE);
            if (mDCRMode.equalsIgnoreCase("SC")) {
                checkavail = true;
            } else {
                for (Category_Universe_Modal cl : listt) {
                    if (cl.getColorFlag().equals("1")) {
                        checkavail = true;
                    }
                }
            }
            //156
            // orderbutton.setText("ORDER");
            orderbutton.setText("SUBMIT");

            findViewById(R.id.orderTypesLayout).setVisibility(View.GONE);
            findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
            findViewById(R.id.rlSearchParent).setVisibility(View.GONE);


            findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
            findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);


            //156
        } else {
            Order_Category_Select.CategoryAdapter customAdapteravail = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
            categorygrid.setAdapter(customAdapteravail);
            linnercashdiscount.setVisibility(View.VISIBLE);
            linnetamount.setVisibility(View.VISIBLE);
            orderbutton.setText("INVOICE");
        }
        if (checkavail == false) {
            lin_gridcategory.setVisibility(View.VISIBLE);
            lin_orderrecyclerview.setVisibility(View.GONE);
            totalorderbottom.setVisibility(View.GONE);
            orderbutton.setVisibility(View.GONE);
            ok.setVisibility(View.VISIBLE);
            gobackflag = false;
            takeorder.setVisibility(View.VISIBLE);
            findViewById(R.id.rlTakeOrder).setVisibility(View.VISIBLE);

            Toast.makeText(this, "Enter The Qty", Toast.LENGTH_SHORT).show();
        } else {
            lin_gridcategory.setVisibility(View.GONE);
            lin_orderrecyclerview.setVisibility(View.VISIBLE);
//            totalorderbottom.setVisibility(View.VISIBLE);
//            orderbutton.setVisibility(View.VISIBLE);
            takeorder.setText("SUBMIT");
            ok.setVisibility(View.INVISIBLE);
            gobackflag = true;
            //takeorder.setVisibility(View.GONE);
            //findViewById(R.id.rlTakeOrder).setVisibility(View.GONE);
//            Category_Nametext.setText("");
//            Category_Nametext.setVisibility(View.GONE);


            orderbutton.setText("SUBMIT");

            findViewById(R.id.orderTypesLayout).setVisibility(View.GONE);
            findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
            findViewById(R.id.rlSearchParent).setVisibility(View.GONE);


            findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
            findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);


            int talqty = 0, totalvalues = 0;
            Getorder_Array_List = new ArrayList<>();
            Getorder_Array_List.clear();
            for (Product_Details_Modal pm : Product_Modal) {
                System.out.println("Product_getQty" + pm.getQty());
                System.out.println("Product_getQtyRegular" + pm.getRegularQty());
                if (pm.getRegularQty() != null) {
                    if (pm.getQty() > 0 || pm.getRegularQty() > 0) {
                        Getorder_Array_List.add(pm);
                        // talqty += pm.getQty() + pm.getRegularQty();
                        talqty += pm.getQty() + pm.getRegularQty();
                        totalvalues += pm.getAmount();
                    }
                }
            }
            totalvalue.setText("" + totalvalues);
            if (StringFlag.equals("order")) {
                netamount.setText("");
            } else {
                netamount.setText("" + totalvalues);
            }
            totalqty.setText("" + talqty);


            mPay_Adapter = new Pay_Adapter(Getorder_Array_List, R.layout.product_pay_recyclerview, getApplicationContext(), -1);
            recyclerView.setAdapter(mPay_Adapter);
            new Pay_Adapter(Getorder_Array_List, R.layout.product_pay_recyclerview, getApplicationContext(), 0).notifyDataSetChanged();
            recyclerView.setItemViewCacheSize(Product_Modal.size());


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

        totCGST = 0;
        totSGST = 0;
        totIGST = 0;


        for (int pm = 0; pm < Product_Modal.size(); pm++) {

            if (Product_Modal.get(pm).getRegularQty() != null) {
                if (Product_Modal.get(pm).getQty() > 0 || Product_Modal.get(pm).getRegularQty() > 0) {

                    cashDiscount += (int) Product_Modal.get(pm).getDiscount();

                    totalvalues += Product_Modal.get(pm).getAmount();

                    totalQty += Product_Modal.get(pm).getQty() + Product_Modal.get(pm).getRegularQty();

                    if (!Common_Class.isNullOrEmpty(Product_Modal.get(pm).getTax()))
                        taxVal += Double.parseDouble(Product_Modal.get(pm).getTax());


                    if (Product_Modal.get(pm).getCGST()!=null)
                        totCGST += Product_Modal.get(pm).getCGST();
                    if (Product_Modal.get(pm).getSGST() !=null)
                        totSGST += Product_Modal.get(pm).getSGST();
                    if (Product_Modal.get(pm).getIGST() !=null)
                        totIGST += Product_Modal.get(pm).getIGST();


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


        if (cashDiscount > 0) {
            tvSaveAmt.setVisibility(View.VISIBLE);
            tvSaveAmt.setText("You will save ₹ " + cashDiscount + " on this order");
        } else
            tvSaveAmt.setVisibility(View.GONE);


    }


    public void showOrderFilterItem(int categoryPos, String filterString) {
        categoryPos = selectedPos;
        Product_ModalSetAdapter.clear();
        for (Product_Details_Modal personNpi : Product_Modal) {
            if (personNpi.getProductCatCode().toString().equals(listt.get(categoryPos).getId())) {
                if (personNpi.getName().toLowerCase().contains(filterString.toLowerCase()))
                    Product_ModalSetAdapter.add(personNpi);
            }
        }
        // lin_gridcategory.setVisibility(View.GONE);
        lin_orderrecyclerview.setVisibility(View.VISIBLE);
        totalorderbottom.setVisibility(View.GONE);
        orderbutton.setVisibility(View.GONE);
        ok.setVisibility(View.VISIBLE);
        gobackflag = true;
        // takeorder.setVisibility(View.GONE);
        Category_Nametext.setVisibility(View.VISIBLE);
        Category_Nametext.setText(listt.get(categoryPos).getName());


        Order_Category_Select.CategoryAdapter customAdapteravail = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
        categorygrid.setAdapter(customAdapteravail);
        // customAdapteravail.updateUi(categoryPos);
        //
        mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_order_recyclerview, getApplicationContext(), categoryPos);

        recyclerView.setAdapter(mProdct_Adapter);
        new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_order_recyclerview, getApplicationContext(), categoryPos).notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(Product_ModalSetAdapter.size());


        Category_Nametext.setOnClickListener(this);
    }

    public void showOrderItemList(int categoryPos) {

        Product_ModalSetAdapter.clear();
        for (Product_Details_Modal personNpi : Product_Modal) {
            if (personNpi.getProductCatCode().toString().equals(listt.get(categoryPos).getId())) {
                Product_ModalSetAdapter.add(personNpi);
            }
        }
        // lin_gridcategory.setVisibility(View.GONE);
        lin_orderrecyclerview.setVisibility(View.VISIBLE);
        totalorderbottom.setVisibility(View.GONE);
        orderbutton.setVisibility(View.GONE);
        ok.setVisibility(View.VISIBLE);
        gobackflag = true;
        // takeorder.setVisibility(View.GONE);
        Category_Nametext.setVisibility(View.VISIBLE);
        Category_Nametext.setText(listt.get(categoryPos).getName());


        Order_Category_Select.CategoryAdapter customAdapteravail = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
        categorygrid.setAdapter(customAdapteravail);

        mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_order_recyclerview, getApplicationContext(), categoryPos);

        recyclerView.setAdapter(mProdct_Adapter);
        new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_order_recyclerview, getApplicationContext(), categoryPos).notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(Product_ModalSetAdapter.size());


        Category_Nametext.setOnClickListener(this);

    }

    @Override
    public void onLoadFilterData(List<Retailer_Modal_List> retailer_modal_list) {

    }

    @Override
    public void onLoadTodayOrderList(List<OutletReport_View_Modal> outletReportViewModals) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse) {

    }

    public class CategoryAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        ImageView ivCategoryIcon;
        TextView icon;

        public CategoryAdapter(Context applicationContext, List<Category_Universe_Modal> list) {
            this.context = applicationContext;
            listt = list;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return listt.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @SuppressLint("ResourceAsColor")
        public void updateUi(int pos) {
            for (int i = 0; i < listt.size(); i++) {

                if (i == pos) {

                    ivCategoryIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                    icon.setTextColor(R.color.colorPrimaryDark);
                    icon.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    ivCategoryIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_grey24);
                    icon.setTextColor(R.color.grey_500);
                    icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                }
            }

            notifyDataSetChanged();

        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.category_order_horizantal_universe_gridview, null); // inflate the layout
            icon = view.findViewById(R.id.textView);
            LinearLayout gridcolor = view.findViewById(R.id.gridcolor);
            ivCategoryIcon = view.findViewById(R.id.ivCategoryIcon);
            icon.setText(listt.get(i).getName());


//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) gridcolor.getLayoutParams();
//// Changes the height and width to the specified *pixels*
//            params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
//            params.width = 120;
//            gridcolor.setLayoutParams(params);


            gridcolor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectedPos = i;
                    showOrderItemList(i);


                }
            });


            if (i == selectedPos) {

                ivCategoryIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                icon.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                icon.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                ivCategoryIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_grey24);
                icon.setTextColor(getResources().getColor(R.color.grey_500));
                icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            }

            return view;
        }
    }


  /*  public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
        private int rowLayout;

        Context context;
        LayoutInflater inflter;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout gridcolor;
            TextView icon;
            ImageView ivCategoryIcon;


            public MyViewHolder(View view) {
                super(view);

                icon = view.findViewById(R.id.textView);
                gridcolor = view.findViewById(R.id.gridcolor);
                ivCategoryIcon = view.findViewById(R.id.ivCategoryIcon);


            }
        }


        public CategoryAdapter(Context applicationContext, List<Category_Universe_Modal> list) {
            this.context = applicationContext;
            listt = list;
        }

        @Override
        public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
//
//            return new MyViewHolder(view);

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view= layoutInflater.inflate(R.layout.category_order_horizantal_universe_gridview, parent, false);
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
        public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position) {
            try {


                holder.icon.setText(listt.get(position).getName());


                holder.gridcolor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectedPos = position;
                        showOrderItemList(position);


                    }
                });


                if (position == selectedPos) {

                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                    holder.icon.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    holder.ivCategoryIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_grey24);
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


    }*/


    public class Prodct_Adapter extends RecyclerView.Adapter<Prodct_Adapter.MyViewHolder> {
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;
        private int Categorycolor;

        Context context;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, Disc, Free, RegularQty, lblRQty, lblAddQty, productQty, preOrderVal, regularAmt,
                    QtyAmt, totalQty, tvTaxLabel;

            public LinearLayout lnRwEntry, lnlblRwEntry;
            EditText Qty;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                Rate = view.findViewById(R.id.Rate);
                Qty = view.findViewById(R.id.Qty);
                lblRQty = view.findViewById(R.id.status);
                lblAddQty = view.findViewById(R.id.lblAddQty);
                RegularQty = view.findViewById(R.id.RegularQty);
                Amount = view.findViewById(R.id.Amount);
                Disc = view.findViewById(R.id.Disc);
                Free = view.findViewById(R.id.Free);
                lnRwEntry = view.findViewById(R.id.lnRwEntry);
                lnlblRwEntry = view.findViewById(R.id.lnlblRwEntry);
                productQty = view.findViewById(R.id.productqty);
                preOrderVal = view.findViewById(R.id.tvPreOrderVal);
                regularAmt = view.findViewById(R.id.RegularAmt);
                QtyAmt = view.findViewById(R.id.qtyAmt);
                totalQty = view.findViewById(R.id.totalqty);

                tvTaxLabel = view.findViewById(R.id.tvTaxLabel);


            }
        }


        public Prodct_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int Categorycolor) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.Categorycolor = Categorycolor;

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


                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(position);


                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.Rate.setText("₹" + formatter.format(Product_Details_Modal.getRate()));
                holder.Amount.setText("₹" + new DecimalFormat("##0.00").format(Product_Details_Modal.getAmount()));
                holder.totalQty.setText("Total Qty : " + ((Product_Details_Modalitem.get(position).getRegularQty()) + (Product_Details_Modalitem.get(position).getQty())));


                holder.RegularQty.setText("" + Product_Details_Modal.getRegularQty());
                holder.regularAmt.setText("₹" + new DecimalFormat("##0.00").format(Product_Details_Modal.getRegularQty() * Product_Details_Modalitem.get(position).getRate()));

                holder.Qty.setText("" + Product_Details_Modal.getQty());
                holder.productQty.setText("" + Product_Details_Modal.getQty());

                holder.QtyAmt.setText("₹" + formatter.format(Product_Details_Modal.getRate() * Product_Details_Modal.getQty()));


                if (Common_Class.isNullOrEmpty(Product_Details_Modal.getFree()))
                    holder.Free.setText("0");
                else
                    holder.Free.setText("" + Product_Details_Modal.getFree());


                holder.Disc.setText("₹" + formatter.format(Product_Details_Modal.getDiscount()));


                if (Common_Class.isNullOrEmpty(Product_Details_Modal.getTax()))
                    holder.tvTaxLabel.setText("TAX :₹0.00");
                else
                    holder.tvTaxLabel.setText("TAX :₹" + Product_Details_Modal.getTax());


                holder.Qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence charSequence, int start,
                                              int before, int count) {
                        try {

                            double enterQty = 0;
                            if (!charSequence.toString().equals(""))
                                enterQty = Double.valueOf(charSequence.toString());

                            double totQty = (enterQty + Product_Details_Modalitem.get(position).getRegularQty());


                            Product_Details_Modalitem.get(position).setQty((int) enterQty);
                            holder.Amount.setText("₹" + new DecimalFormat("##0.00").format(totQty * Product_Details_Modalitem.get(position).getRate()));
                            Product_Details_Modalitem.get(position).setAmount(Double.valueOf(formatter.format(totQty *
                                    Product_Details_Modalitem.get(position).getRate())));

                            holder.QtyAmt.setText("₹" + formatter.format(enterQty * Product_Details_Modalitem.get(position).getRate()));
                            holder.totalQty.setText("Total Qty : " + totQty);


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

                                        Product_Details_Modalitem.get(position).setTax("0.00");
                                        Product_Details_Modalitem.get(position).setTax_value("0.00");
                                        Product_Details_Modalitem.get(position).setCGST(0.00);
                                        Product_Details_Modalitem.get(position).setSGST(0.00);
                                        Product_Details_Modalitem.get(position).setIGST(0.00);

                                        Product_Details_Modalitem.get(position).setOff_Pro_code(product_details_modalArrayList.get(i).getOff_Pro_code());
                                        Product_Details_Modalitem.get(position).setOff_Pro_name(product_details_modalArrayList.get(i).getOff_Pro_name());
                                        Product_Details_Modalitem.get(position).setOff_Pro_Unit(product_details_modalArrayList.get(i).getOff_Pro_Unit());
                                        Product_Details_Modalitem.get(position).setFree_val(product_details_modalArrayList.get(i).getFree());

                                        Product_Details_Modalitem.get(position).setDiscount_value(String.valueOf(product_details_modalArrayList.get(i).getDiscount()));
                                        Product_Details_Modalitem.get(position).setDiscount_type(product_details_modalArrayList.get(i).getDiscount_type());


                                        if (totQty >= schemeVal) {

                                            if (schemeVal > highestScheme) {
                                                highestScheme = schemeVal;


                                                if (!product_details_modalArrayList.get(i).getFree().equals("0")) {
                                                    if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                        double freePer = (totQty / highestScheme);

                                                        double freeVal = freePer * Double.parseDouble(product_details_modalArrayList.
                                                                get(i).getFree());

                                                        Product_Details_Modalitem.get(position).setFree(String.valueOf(Math.round(freeVal)));
                                                    } else {
                                                        int val = (int) (totQty / highestScheme);
                                                        int freeVal = val * Integer.parseInt(product_details_modalArrayList.get(i).getFree());
                                                        Product_Details_Modalitem.get(position).setFree(String.valueOf(freeVal));
                                                    }
                                                } else {

                                                    holder.Free.setText("0");
                                                    Product_Details_Modalitem.get(position).setFree("0");

                                                }


                                                if (product_details_modalArrayList.get(i).getDiscount() != 0) {

                                                    if (product_details_modalArrayList.get(i).getDiscount_type().equals("%")) {
                                                        double discountVal = totQty * (((product_details_modalArrayList.get(i).getDiscount()
                                                        )) / 100);


                                                        Product_Details_Modalitem.get(position).setDiscount((Math.round(discountVal)));

                                                    } else {
                                                        //Rs
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (totQty / highestScheme);

                                                            double freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());

                                                            Product_Details_Modalitem.get(position).setDiscount((Math.round(freeVal)));
                                                        } else {
                                                            int val = (int) (totQty / highestScheme);
                                                            int freeVal = (int) (val * (product_details_modalArrayList.get(i).getDiscount()));
                                                            Product_Details_Modalitem.get(position).setDiscount((freeVal));
                                                        }
                                                    }


                                                } else {
                                                    holder.Disc.setText("₹0.00");
                                                    Product_Details_Modalitem.get(position).setDiscount(0.00);

                                                }


                                            }

                                        } else {
                                            holder.Free.setText("0");
                                            Product_Details_Modalitem.get(position).setFree("0");

                                            holder.Disc.setText("₹0.00");
                                            Product_Details_Modalitem.get(position).setDiscount(0.00);


                                        }


                                    }

                                }


                            }

                            if (!haveVal) {
                                holder.Free.setText("0");
                                Product_Details_Modalitem.get(position).setFree("0");

                                holder.Disc.setText("₹0.00");
                                Product_Details_Modalitem.get(position).setDiscount(0.00);


                                Product_Details_Modalitem.get(position).setTax("0.00");
                                Product_Details_Modalitem.get(position).setTax_value("0.00");
                                Product_Details_Modalitem.get(position).setCGST(0.00);
                                Product_Details_Modalitem.get(position).setSGST(0.00);
                                Product_Details_Modalitem.get(position).setIGST(0.00);

                                Product_Details_Modalitem.get(position).setOff_Pro_code("");
                                Product_Details_Modalitem.get(position).setOff_Pro_name("");
                                Product_Details_Modalitem.get(position).setOff_Pro_Unit("");

                                Product_Details_Modalitem.get(position).setDiscount_value("0.00");
                                Product_Details_Modalitem.get(position).setDiscount_type("");


                            } else {

                                Product_Details_Modalitem.get(position).setAmount((Product_Details_Modalitem.get(position).getAmount()) -
                                        (Product_Details_Modalitem.get(position).getDiscount()));

                                holder.Free.setText("" + Product_Details_Modalitem.get(position).getFree());
                                holder.Disc.setText("₹" + formatter.format(Product_Details_Modalitem.get(position).getDiscount()));

                                holder.Amount.setText("₹" + formatter.format(Product_Details_Modalitem.get(position).getAmount()));


                            }


                            String taxRes = sharedCommonPref.getvalue(Constants.TAXList);

                            if (!Common_Class.isNullOrEmpty(taxRes)) {
                                JSONObject jsonObject = new JSONObject(taxRes.toString());


                                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                double wholeTax = 0;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    if (jsonObject1.getString("Product_Detail_Code").equals(Product_Details_Modalitem.get(position).getId())) {

                                        if (jsonObject1.getDouble("Tax_Val") > 0) {
                                            double taxCal = Product_Details_Modalitem.get(position).getAmount() * ((jsonObject1.getDouble("Tax_Val") / 100));

                                            wholeTax += taxCal;


                                            switch (jsonObject1.getString("Tax_Type")) {
                                                case "CGST":
                                                    Product_Details_Modalitem.get(position).setCGST(taxCal);
                                                    break;
                                                case "SGST":
                                                    Product_Details_Modalitem.get(position).setCGST(taxCal);
                                                    break;
                                                case "IGST":
                                                    Product_Details_Modalitem.get(position).setCGST(taxCal);
                                                    break;
                                            }


                                        }
                                    }
                                }


                                Product_Details_Modalitem.get(position).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(position).getAmount()
                                        + wholeTax)));

                                Product_Details_Modalitem.get(position).setTax(String.valueOf(formatter.format(wholeTax)));
                                holder.Amount.setText("₹" + formatter.format(Product_Details_Modalitem.get(position).getAmount()));


                                holder.tvTaxLabel.setText("TAX :₹" + Product_Details_Modal.getTax());


                            }


                            updateToTALITEMUI();
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


    public class Pay_Adapter extends RecyclerView.Adapter<Pay_Adapter.MyViewHolder> {
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;

        Context context;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, Disc, Free, RegularQty, productQty, totalQty, tvTaxLabel;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                Rate = view.findViewById(R.id.Rate);
                RegularQty = view.findViewById(R.id.RegularQty);
                Amount = view.findViewById(R.id.Amount);
                Free = view.findViewById(R.id.Free);
                productQty = view.findViewById(R.id.productqty);


            }
        }


        public Pay_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int Categorycolor) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;


        }

        @Override
        public Pay_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        public void onBindViewHolder(Pay_Adapter.MyViewHolder holder, int position) {
            try {


                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(position);


                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.Rate.setText("₹" + formatter.format(Product_Details_Modal.getRate()));
                holder.Amount.setText("₹" + formatter.format(Product_Details_Modal.getAmount()));

                holder.RegularQty.setText("" + Product_Details_Modal.getRegularQty());

                holder.productQty.setText("" + Product_Details_Modal.getQty());


                if (Common_Class.isNullOrEmpty(Product_Details_Modal.getFree()))
                    holder.Free.setText("0");
                else
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
                //                gobackflag = false;
                lin_gridcategory.setVisibility(View.VISIBLE);

                findViewById(R.id.orderTypesLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);

                findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);


                findViewById(R.id.llBillHeader).setVisibility(View.GONE);
                findViewById(R.id.llPayNetAmountDetail).setVisibility(View.GONE);


                takeorder.setText("PROCEED TO CART");
                Order_Category_Select.CategoryAdapter customAdapteravaill = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
                categorygrid.setAdapter(customAdapteravaill);


                showOrderItemList(selectedPos);


            } else {
                common_class.CommonIntentwithFinish(Invoice_History.class);
            }
            return true;
        }
        return false;
    }
}