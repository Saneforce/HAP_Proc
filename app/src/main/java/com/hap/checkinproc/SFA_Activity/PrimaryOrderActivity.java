package com.hap.checkinproc.SFA_Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
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
import java.util.Date;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrimaryOrderActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {

    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Product_Details_Modal> freeQty_Array_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    CircularProgressButton takeorder;
    TextView Out_Let_Name, Category_Nametext,
            tvTimer;
    LinearLayout lin_orderrecyclerview, lin_gridcategory, rlAddProduct, llTdPriOrd;
    Common_Class common_class;
    String Ukey;
    String[] strLoc;
    String Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "";
    Shared_Common_Pref sharedCommonPref;
    Prodct_Adapter mProdct_Adapter;
    String TAG = "PRIMARY_ORDER";
    DatabaseHandler db;
    RelativeLayout rlCategoryItemSearch;
    ImageView ivClose;
    EditText etCategoryItemSearch;
    int cashDiscount;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private RecyclerView recyclerView, categorygrid, freeRecyclerview;
    private int selectedPos = 0;
    private TextView tvTotalAmount,tvACBal;
    private double totalvalues, taxVal;
    private Integer totalQty;
    private TextView tvBillTotItem;
    double ACBalance=0.0;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_primary_order_layout);
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(PrimaryOrderActivity.this);
            common_class = new Common_Class(this);

            categorygrid = findViewById(R.id.category);
            takeorder = findViewById(R.id.takeorder);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);
            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6");
            lin_orderrecyclerview = findViewById(R.id.lin_orderrecyclerview);
            lin_gridcategory = findViewById(R.id.lin_gridcategory);
            Out_Let_Name = findViewById(R.id.outlet_name);
            Category_Nametext = findViewById(R.id.Category_Nametext);
            rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
            rlAddProduct = findViewById(R.id.rlAddProduct);
            ivClose = findViewById(R.id.ivClose);
            llTdPriOrd = findViewById(R.id.llTodayPriOrd);
            tvACBal = findViewById(R.id.tvACBal);
            Out_Let_Name.setText("Hi! "+sharedCommonPref.getvalue(Constants.Distributor_name,""));
            getACBalance(0);

            etCategoryItemSearch = findViewById(R.id.searchView);
            tvTimer = findViewById(R.id.tvTimer);


            Product_ModalSetAdapter = new ArrayList<>();
            gson = new Gson();
            takeorder.setOnClickListener(this);
            rlCategoryItemSearch.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            rlAddProduct.setOnClickListener(this);
            llTdPriOrd.setOnClickListener(this);
            Category_Nametext.setOnClickListener(this);
            Ukey = Common_Class.GetEkey();
            recyclerView = findViewById(R.id.orderrecyclerview);
            freeRecyclerview = findViewById(R.id.freeRecyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categorygrid.setLayoutManager(layoutManager);

            common_class.getDb_310Data(Constants.Category_List, this);
            common_class.getDb_310Data(Constants.Product_List, this);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            common_class.getDb_310Data(Constants.PRIMARY_SCHEME, this);

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


        } catch (Exception e) {
            Log.v(TAG, " order oncreate: " + e.getMessage());

        }
    }
    private void getACBalance(int Mode) {
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("StkERP", sharedCommonPref.getvalue(Constants.DistributorERP));

            ApiClient.getClient().create(ApiInterface.class)
                    .getDataArrayList("get/custbalance", jParam.toString())
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            try {
                                JsonArray res = response.body();
                                JsonObject jItem = res.get(0).getAsJsonObject();

                                ACBalance=jItem.get("LC_BAL").getAsDouble();
                                if(ACBalance<=0) ACBalance=Math.abs(ACBalance); else ACBalance=0-ACBalance;
                                tvACBal.setText("₹" + new DecimalFormat("##0.00").format(ACBalance));
                                if(Mode==1){
                                    SubmitPrimaryOrder();
                                }
                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {

                            Log.d("InvHistory", String.valueOf(t));
                        }
                    });
        } catch (JSONException e) {

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

            if (Product_Modal.get(pm).getQty() > 0) {
                Getorder_Array_List.add(Product_Modal.get(pm));


            }
        }

        if (Getorder_Array_List.size() == 0)
            Toast.makeText(getApplicationContext(), "Order is empty", Toast.LENGTH_SHORT).show();
        else
            FilterProduct();

    }
public void SubmitPrimaryOrder(){
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date d1 = sdf.parse(Common_Class.GetTime());
        Date d2 = sdf.parse(sharedCommonPref.getvalue(Constants.CUTOFF_TIME));
        long elapsed = d2.getTime() - d1.getTime();
        if(ACBalance>=totalvalues) {
            //if (elapsed >= 0) {
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

            //} else {
             //   common_class.showMsg(this, "Time UP...");
            //}
        } else {
            common_class.showMsg(this, "Low A/C Balance...");
        }
    }catch (Exception e) {

    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                    if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {

                        if (Getorder_Array_List != null
                                && Getorder_Array_List.size() > 0) {
                            if(takeorder.isAnimating()) return;
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
                        if (Shared_Common_Pref.Invoicetoorder != null) {
                            if (Shared_Common_Pref.Invoicetoorder.equals("1")) {
                                FilterProduct();
                            } else if (Shared_Common_Pref.Invoicetoorder.equals("2")) {
                                FilterProduct();
                            } else {
                                showOrderList();
                            }
                        } else {
                            showOrderList();
                        }

                    }
                } catch (Exception e) {

                }
                break;


        }
    }

    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {

            AlertDialogBox.showDialog(PrimaryOrderActivity.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
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
                        OutletItem.put("TransSlNo", Shared_Common_Pref.TransSlNo);
                        OutletItem.put("ordertype", "order");

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
                            ProdItem.put("Product_ERP",Getorder_Array_List.get(z).getERP_Code());
                            ProdItem.put("Product_Qty", Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_RegularQty", 0);
                            ProdItem.put("Product_Total_Qty", Getorder_Array_List.get(z).getQty()
                            );
                            ProdItem.put("Product_Amount", Getorder_Array_List.get(z).getAmount());
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
                                    String san = jsonObjects.getString("success");
                                    Log.e("Success_Message", san);
                                    common_class.showMsg(PrimaryOrderActivity.this, jsonObjects.getString("Msg"));
                                    if (san.equals("true")) {
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

    private void FilterProduct() {


        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.GONE);


        findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);


        lin_gridcategory.setVisibility(View.GONE);
        lin_orderrecyclerview.setVisibility(View.VISIBLE);
        takeorder.setText("SUBMIT");

        Getorder_Array_List = new ArrayList<>();
        Getorder_Array_List.clear();
        for (Product_Details_Modal pm : Product_Modal) {

            if (pm.getQty() > 0) {
                Getorder_Array_List.add(pm);

            }

        }


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

            if (Product_Modal.get(pm).getQty() > 0) {

                cashDiscount += (int) Product_Modal.get(pm).getDiscount();

                totalvalues += Product_Modal.get(pm).getAmount();

                totalQty += Product_Modal.get(pm).getQty();

                if (!Common_Class.isNullOrEmpty(Product_Modal.get(pm).getTax()))
                    taxVal += Double.parseDouble(Product_Modal.get(pm).getTax());


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
                else if (personNpi.getName().toLowerCase().contains(filterString.toLowerCase()))
                    Product_ModalSetAdapter.add(personNpi);

            }
        }
        // lin_gridcategory.setVisibility(View.GONE);
        lin_orderrecyclerview.setVisibility(View.VISIBLE);
        Category_Nametext.setVisibility(View.VISIBLE);
        Category_Nametext.setText(listt.get(categoryPos).getName());


        mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.adapter_primary_product, getApplicationContext(), categoryPos);

        recyclerView.setAdapter(mProdct_Adapter);
        new Prodct_Adapter(Product_ModalSetAdapter, R.layout.adapter_primary_product, getApplicationContext(), categoryPos).notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(Product_ModalSetAdapter.size());


    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            switch (key) {
                case Constants.Category_List:
                    GetJsonData(sharedCommonPref.getvalue(Constants.Category_List), "1");
                    PrimaryOrderActivity.CategoryAdapter customAdapteravail = new PrimaryOrderActivity.CategoryAdapter(getApplicationContext(),
                            Category_Modal);
                    categorygrid.setAdapter(customAdapteravail);
                    break;
                case Constants.Product_List:
                    String OrdersTable = sharedCommonPref.getvalue(Constants.Product_List);
                    userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
                    }.getType();
                    Product_Modal = gson.fromJson(OrdersTable, userType);
                    showOrderItemList(0, "");
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
                                        jsonObject1.getString("Package"), "0", jsonObject1.getString("Offer_Product"),
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


        takeorder.setText("PROCEED TO CART");

        showOrderItemList(selectedPos, "");


    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
        Context context;
        LayoutInflater inflter;
        MyViewHolder pholder;
        private int rowLayout;

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
                            pholder.gridcolor.setBackground(getResources().getDrawable(R.drawable.cardbutton));
                            pholder.icon.setTextColor(getResources().getColor(R.color.black));
                            pholder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                            pholder.undrCate.setVisibility(View.GONE);

                        }
                        pholder = holder;
                        selectedPos = holder.getAdapterPosition();
                        showOrderItemList(holder.getAdapterPosition(), "");
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
        private final List<Product_Details_Modal> Product_Details_Modalitem;
        private final int rowLayout;


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


                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(holder.getAdapterPosition());


                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.Rate.setText("₹" + formatter.format(Product_Details_Modal.getSBRate()));
                holder.Amount.setText("₹" + new DecimalFormat("##0.00").format(Product_Details_Modal.getAmount()));


                if (CategoryType >= 0) {

                    holder.totalQty.setText("Total Qty : " + (
                            (Product_Details_Modalitem.get(holder.getAdapterPosition()).getQty())));

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


                    holder.QtyAmt.setText("₹" + formatter.format(Product_Details_Modal.getSBRate() * Product_Details_Modal.getQty()));


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

                            double totQty = (enterQty * Double.valueOf(Product_Details_Modalitem.get(position).getConversionFactor()));


                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText("₹" + new DecimalFormat("##0.00").format(totQty * Product_Details_Modalitem.get(holder.getAdapterPosition()).getSBRate()));
                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                    Product_Details_Modalitem.get(holder.getAdapterPosition()).getSBRate())));
                            if (CategoryType >= 0) {
                                holder.QtyAmt.setText("₹" + formatter.format(enterQty * Product_Details_Modalitem.get(holder.getAdapterPosition()).getSBRate()));
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


                            String taxRes = sharedCommonPref.getvalue(Constants.PrimaryTAXList);

                            if (!Common_Class.isNullOrEmpty(taxRes)) {
                                JSONObject jsonObject = new JSONObject(taxRes);


                                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                double wholeTax = 0;
                                List<Product_Details_Modal> taxList = new ArrayList<>();


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    if (jsonObject1.getString("Product_Detail_Code").equals(Product_Details_Modalitem.get(holder.getAdapterPosition()).getId())) {

                                        if (jsonObject1.getDouble("Tax_Val") > 0) {
                                            double taxCal = Product_Details_Modalitem.get(holder.getAdapterPosition()).getAmount() *
                                                    ((jsonObject1.getDouble("Tax_Val") / 100));

                                            wholeTax += taxCal;


                                            // List<Product_Details_Modal> taxList = new ArrayList<>();


                                            switch (jsonObject1.getString("Tax_Type")) {
                                                case "CGST":
                                                    Product_Details_Modalitem.get(holder.getAdapterPosition()).setCGST(taxCal);
                                                    break;
                                                case "SGST":
                                                    Product_Details_Modalitem.get(holder.getAdapterPosition()).setSGST(taxCal);
                                                    break;
                                                case "IGST":
                                                    Product_Details_Modalitem.get(holder.getAdapterPosition()).setIGST(taxCal);
                                                    break;
                                            }


                                            taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                                    jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));


                                        }
                                    }
                                }

                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setProductDetailsModal(taxList);


                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(holder.getAdapterPosition()).getAmount()
                                        + wholeTax)));

                                Product_Details_Modalitem.get(holder.getAdapterPosition()).setTax(formatter.format(wholeTax));
                                holder.Amount.setText("₹" + formatter.format(Product_Details_Modalitem.get(holder.getAdapterPosition()).getAmount()));


                                holder.tvTaxLabel.setText("₹" + Product_Details_Modal.getTax());


                            }


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


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, Disc, Free, lblRQty, productQty,
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
                Amount = view.findViewById(R.id.Amount);
                Free = view.findViewById(R.id.Free);
                Disc = view.findViewById(R.id.Disc);
                tvTaxLabel = view.findViewById(R.id.tvTaxLabel);


                if (CategoryType >= 0) {
                    ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);
                    lblRQty = view.findViewById(R.id.status);
                    QtyAmt = view.findViewById(R.id.qtyAmt);
                    totalQty = view.findViewById(R.id.totalqty);
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
            public TextView productname, Rate, Amount, Free, productQty, totalQty;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                Free = view.findViewById(R.id.Free);

            }
        }


    }
}