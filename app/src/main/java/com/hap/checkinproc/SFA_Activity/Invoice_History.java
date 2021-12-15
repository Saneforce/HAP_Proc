package com.hap.checkinproc.SFA_Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
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
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Invoice_History extends AppCompatActivity implements Master_Interface, View.OnClickListener, UpdateResponseUI {

    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";

    TextView outlet_name, lastinvoice, tvOtherBrand, tvQPS, tvPOP, tvCoolerInfo, tvOrder, txRmkTmplSpinn, txRmksNoOrd, tvOutstanding, txPrvBal, txSalesAmt, txPayment;
    LinearLayout lin_order, lin_repeat_order, lin_invoice, lin_repeat_invoice, lin_noOrder, linNoOrderRmks, linPayment, linRpt;
    Common_Class common_class;
    List<OutletReport_View_Modal> OutletReport_View_Modal = new ArrayList<>();
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    Type userType;
    Gson gson;
    Invoice_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;
    private String[] strLoc;
    private String Worktype_code;
    Location mlocation;
    List<Common_Model> ldgRemarks = new ArrayList<>();
    Button btnSbmtNOrd;
    ImageView btnRmkClose;
    public static String TAG = "Invoice_History";

    //Updateed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_invoice__history);
            db = new DatabaseHandler(this);
            gson = new Gson();
            sharedCommonPref = new Shared_Common_Pref(Invoice_History.this);
            common_class = new Common_Class(this);

            new LocationFinder(getApplication(), new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    mlocation = location;
                }
            });

            CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
            common_class.getProductDetails(this);
            lin_order = findViewById(R.id.lin_order);
            outlet_name = findViewById(R.id.outlet_name);
            outlet_name.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            lin_repeat_order = findViewById(R.id.lin_repeat_order);
            lin_invoice = findViewById(R.id.lin_invoice);
            lin_repeat_invoice = findViewById(R.id.lin_repeat_invoice);
            lastinvoice = findViewById(R.id.lastinvoice);
            lin_noOrder = findViewById(R.id.lin_noOrder);
            linNoOrderRmks = findViewById(R.id.linNoOrderRmks);
            btnSbmtNOrd = findViewById(R.id.btnSbmtNOrd);
            tvOrder = (TextView) findViewById(R.id.tvOrder);

            tvOrder.setVisibility(View.GONE);
            tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
            tvPOP = (TextView) findViewById(R.id.tvPOP);
            tvQPS = (TextView) findViewById(R.id.tvQPS);
            tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
            txRmkTmplSpinn = (TextView) findViewById(R.id.txRmkTmplSpinn);
            txRmksNoOrd = (TextView) findViewById(R.id.txRmksNoOrd);
            btnRmkClose = (ImageView) findViewById(R.id.btnRmkClose);
            linPayment = (LinearLayout) findViewById(R.id.lin_payment);
            linRpt = (LinearLayout) findViewById(R.id.llRpt);
            tvOutstanding = findViewById(R.id.txOutstanding);
            txPrvBal = findViewById(R.id.PrvOutAmt);
            txSalesAmt = findViewById(R.id.SalesAmt);
            txPayment = findViewById(R.id.PaymentAmt);


            lin_noOrder.setOnClickListener(this);
            lastinvoice.setOnClickListener(this);
            lin_order.setOnClickListener(this);
            tvOtherBrand.setOnClickListener(this);
            tvQPS.setOnClickListener(this);
            tvPOP.setOnClickListener(this);
            tvOrder.setOnClickListener(this);
            tvCoolerInfo.setOnClickListener(this);
            linPayment.setOnClickListener(this);
            linRpt.setOnClickListener(this);

            loadNoOrdRemarks();
            btnRmkClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linNoOrderRmks.setVisibility(View.GONE);
                }
            });
            txRmkTmplSpinn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.showCommonDialog(ldgRemarks, 1, Invoice_History.this);
                }
            });
            btnSbmtNOrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txRmksNoOrd.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(Invoice_History.this, "Select the Reason", Toast.LENGTH_LONG).show();
                        return;
                    }
                    linNoOrderRmks.setVisibility(View.GONE);
                    SaveOrder();
                }
            });
            invoicerecyclerview = (RecyclerView) findViewById(R.id.invoicerecyclerview);

            lin_invoice.setOnClickListener(this);

            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6");

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


            common_class.getDataFromApi(Constants.GetTodayOrder_List, this, false);
            common_class.getDb_310Data(Constants.TAXList, this);
            common_class.getDb_310Data(Constants.FreeSchemeDiscList, this);
            //common_class.getDb_310Data(Constants.OUTSTANDING, this);

            getOutstanding();

            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE))
                findViewById(R.id.orderTypesLayout).setVisibility(View.GONE);
        } catch (Exception e) {

        }

    }

    private void getOutstanding() {
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", UserDetails.getString("Sfcode", ""));
            jParam.put("Stk", sharedCommonPref.getvalue(Constants.Distributor_Id));
            jParam.put("Cus", Shared_Common_Pref.OutletCode);
            jParam.put("div", UserDetails.getString("Divcode", ""));

            ApiClient.getClient().create(ApiInterface.class)
                    .getDataArrayList("get/outstanding", jParam.toString())
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            try {
                                JsonArray res = response.body();
                                JsonObject jItem = res.get(0).getAsJsonObject();
                                txPrvBal.setText("₹" + new DecimalFormat("##0.00").format(jItem.get("pBal").getAsDouble()));
                                txSalesAmt.setText("₹" + new DecimalFormat("##0.00").format(jItem.get("Debit").getAsDouble()));
                                txPayment.setText("₹" + new DecimalFormat("##0.00").format(jItem.get("Credit").getAsDouble()));
                                tvOutstanding.setText("₹" + new DecimalFormat("##0.00").format(jItem.get("Balance").getAsDouble()));

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

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        if (type == 1) {
            txRmksNoOrd.setText(myDataset.get(position).getName());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llRpt:
                common_class.CommonIntentwithoutFinish(PayLedgerActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.lin_payment:
                common_class.CommonIntentwithoutFinish(PaymentActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.tvOtherBrand:
                common_class.CommonIntentwithFinish(OtherBrandActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.tvQPS:
                common_class.CommonIntentwithFinish(QPSActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.tvPOP:
                common_class.CommonIntentwithFinish(POPActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.tvCoolerInfo:
                common_class.CommonIntentwithFinish(CoolerInfoActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;

            case R.id.lin_order:
                Shared_Common_Pref.Invoicetoorder = "0";
                common_class.getDb_310Data(Constants.PreOrderQtyList, this);

                //Shared_Common_Pref.TransSlNo = "0";
                break;
            case R.id.lin_repeat_order:
                break;
            case R.id.lin_invoice:
                Shared_Common_Pref.Invoicetoorder = "2";
                //getInvoiceOrderQty();
                common_class.CommonIntentwithFinish(Invoice_Category_Select.class);
                overridePendingTransition(R.anim.in, R.anim.out);

                break;
            case R.id.lin_repeat_invoice:
                break;
            case R.id.lastinvoice:
                common_class.CommonIntentwithoutFinish(HistoryInfoActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                // common_class.CommonIntentwithoutFinish(More_Info_Activity.class);

                break;
            case R.id.lin_noOrder:
                if (mlocation == null) {
                    new LocationFinder(getApplication(), new LocationEvents() {
                        @Override
                        public void OnLocationRecived(Location location) {
                            strLoc = (location.getLatitude() + ":" + location.getLongitude()).split(":");

                            linNoOrderRmks.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    strLoc = (mlocation.getLatitude() + ":" + mlocation.getLongitude()).split(":");

                    linNoOrderRmks.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tvOrder:
                common_class.getDb_310Data(Constants.PreOrderQtyList, this);
                break;
        }
    }

    public void loadNoOrdRemarks() {
        db = new DatabaseHandler(this);
        try {
            JSONArray HAPRmks = db.getMasterData("HAPNoOrdRmks");
            if (HAPRmks != null) {
                for (int li = 0; li < HAPRmks.length(); li++) {
                    JSONObject jItem = HAPRmks.getJSONObject(li);
                    Common_Model item = new Common_Model(jItem.getString("id"), jItem.getString("name"), jItem);
                    ldgRemarks.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void GetJsonData(String jsonResponse, String type) {

        //type =1 product category data values
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (type.equals("1")) {

                } else {
                    Worktype_code = jsonObject1.optString("wtype");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {

            AlertDialogBox.showDialog(Invoice_History.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
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
                        HeadItem.put("Daywise_Remarks", txRmksNoOrd.getText().toString());
                        HeadItem.put("UKey", Common_Class.GetEkey());
                        HeadItem.put("orderValue", "0");
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                        OutletItem.put("orderValue", "0");
                        OutletItem.put("CashDiscount", "0");
                        OutletItem.put("NetAmount", "0");
                        OutletItem.put("No_Of_items", "0");
                        OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("TransSlNo", Shared_Common_Pref.TransSlNo);
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "no order");
                        OutletItem.put("reason", txRmksNoOrd.getText().toString());
                        if (strLoc.length > 0) {
                            OutletItem.put("Lat", strLoc[0]);
                            OutletItem.put("Long", strLoc[1]);
                        } else {
                            OutletItem.put("Lat", "");
                            OutletItem.put("Long", "");
                        }
                        ActivityData.put("Activity_Doctor_Report", OutletItem);
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
                                    linNoOrderRmks.setVisibility(View.GONE);
                                    common_class.showMsg(Invoice_History.this, jsonObjects.getString("Msg"));

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


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {
                    case Constants.OUTSTANDING:
                        JSONObject jsonObjectOutStd = new JSONObject(apiDataResponse);
                        if (jsonObjectOutStd.getBoolean("success")) {

                            JSONArray jsonArray = jsonObjectOutStd.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tvOutstanding.setText("₹" + new DecimalFormat("##0.00").format(
                                        jsonArray.getJSONObject(i).getDouble("Outstanding")));

                            }

                        } else {

                            tvOutstanding.setText("₹" + 0.00);
                        }
                        break;
                    case Constants.FreeSchemeDiscList:
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

                            sharedCommonPref.save(Constants.FreeSchemeDiscList, gson.toJson(product_details_modalArrayList));


                        } else {
                            sharedCommonPref.clear_pref(Constants.FreeSchemeDiscList);

                        }
                        break;
                    case Constants.TAXList:
                        JSONObject jsonObjectTax = new JSONObject(apiDataResponse);
                        Log.v("TAX_PRIMARY:", apiDataResponse);

                        if (jsonObjectTax.getBoolean("success")) {
                            sharedCommonPref.save(Constants.TAXList, apiDataResponse);

                        } else {
                            sharedCommonPref.clear_pref(Constants.TAXList);

                        }
                        break;
                    case Constants.GetTodayOrder_List:
                        userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                        }.getType();
                        OutletReport_View_Modal = gson.fromJson(apiDataResponse, userType);
                        if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
                            for (OutletReport_View_Modal filterlist : OutletReport_View_Modal) {
                                if (filterlist.getOutletCode().equals(Shared_Common_Pref.OutletCode)) {
                                    FilterOrderList.add(filterlist);
                                }
                            }
                        }


                        mReportViewAdapter = new Invoice_History_Adapter(Invoice_History.this, FilterOrderList, new AdapterOnClick() {
                            @Override
                            public void onIntentClick(int position) {
                                Log.e("TRANS_SLNO", FilterOrderList.get(position).getTransSlNo());
                                Shared_Common_Pref.TransSlNo = FilterOrderList.get(position).getTransSlNo();
                                Shared_Common_Pref.Invoicetoorder = "1";
                                Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                                sharedCommonPref.save(Constants.FLAG, FilterOrderList.get(position).getStatus());
                                Log.e("Sub_Total", String.valueOf(FilterOrderList.get(position).getOrderValue() + ""));
                                intent.putExtra("Order_Values", FilterOrderList.get(position).getOrderValue() + "");
                                intent.putExtra("Invoice_Values", FilterOrderList.get(position).getInvoicevalues());
                                intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
                                intent.putExtra("Invoice_Date", FilterOrderList.get(position).getOrderDate());
                                intent.putExtra("NetAmount", FilterOrderList.get(position).getNetAmount());
                                intent.putExtra("Discount_Amount", FilterOrderList.get(position).getDiscount_Amount());
                                startActivity(intent);
                                overridePendingTransition(R.anim.in, R.anim.out);


                            }
                        });
                        invoicerecyclerview.setAdapter(mReportViewAdapter);

                        break;

                    case Constants.PreOrderQtyList:
                        JSONObject jsonObjectPreOrder = new JSONObject(apiDataResponse);
                        if (jsonObjectPreOrder.getBoolean("success")) {
                            sharedCommonPref.save(Constants.PreOrderQtyList, apiDataResponse);
                            common_class.CommonIntentwithFinish(Order_Category_Select.class);
                            overridePendingTransition(R.anim.in, R.anim.out);
                        } else {
                            sharedCommonPref.clear_pref(Constants.PreOrderQtyList);
                            Log.v("PreOrderList: ", "" + "not success");
                        }
                        break;
                }

            }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            common_class.CommonIntentwithFinish(Dashboard_Route.class);
            overridePendingTransition(R.anim.in, R.anim.out);

            return true;
        }
        return false;
    }

}