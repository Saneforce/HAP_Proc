package com.hap.checkinproc.SFA_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
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

public class Invoice_History extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    TextView outlet_name, lastinvoice, tvOtherBrand, tvQPS, tvPOP, tvCoolerInfo, tvOrder;
    LinearLayout lin_order, lin_repeat_order, lin_invoice, lin_repeat_invoice, lin_noOrder;
    Common_Class common_class;
    List<OutletReport_View_Modal> OutletReport_View_Modal;
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    Type userType;
    Gson gson;
    Invoice_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;
    private String[] strLoc;
    private String Worktype_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_invoice__history);
            db = new DatabaseHandler(this);
            gson = new Gson();
            sharedCommonPref = new Shared_Common_Pref(Invoice_History.this);
            common_class = new Common_Class(this);


            common_class.getDataFromApi(Constants.GetTodayOrder_List, this, false);

            lin_order = findViewById(R.id.lin_order);
            outlet_name = findViewById(R.id.outlet_name);
            outlet_name.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            lin_repeat_order = findViewById(R.id.lin_repeat_order);
            lin_invoice = findViewById(R.id.lin_invoice);
            lin_repeat_invoice = findViewById(R.id.lin_repeat_invoice);
            lastinvoice = findViewById(R.id.lastinvoice);
            lin_noOrder = findViewById(R.id.lin_noOrder);
            tvOrder = (TextView) findViewById(R.id.tvOrder);


            tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
            tvPOP = (TextView) findViewById(R.id.tvPOP);
            tvQPS = (TextView) findViewById(R.id.tvQPS);
            tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);


            lin_noOrder.setOnClickListener(this);
            lastinvoice.setOnClickListener(this);
            lin_order.setOnClickListener(this);
            tvOtherBrand.setOnClickListener(this);
            tvQPS.setOnClickListener(this);
            tvPOP.setOnClickListener(this);
            tvOrder.setOnClickListener(this);
            tvCoolerInfo.setOnClickListener(this);
            invoicerecyclerview = (RecyclerView) findViewById(R.id.invoicerecyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            invoicerecyclerview.setLayoutManager(layoutManager);
            String DCRMode = sharedCommonPref.getvalue(Shared_Common_Pref.DCRMode);
//            lin_invoice.setVisibility(View.VISIBLE);
//            if (!DCRMode.equalsIgnoreCase("")) {
//                lin_invoice.setVisibility(View.GONE);
//            }
            // String OrdersTable = sharedCommonPref.getvalue(Shared_Common_Pref.GetTodayOrder_List);
            String OrdersTable = String.valueOf(db.getMasterData(Constants.GetTodayOrder_List));
            userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
            }.getType();
            OutletReport_View_Modal = gson.fromJson(OrdersTable, userType);
            System.out.println("Array_List_Size" + OrdersTable.toString());
            System.out.println("Array_List_Sizee" + OutletReport_View_Modal.size());
            System.out.println("Array_List_Outlet_Code" + Shared_Common_Pref.OutletCode);
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
//                    if (FilterOrderList.get(position).getStatus().equals("ORDER")) {
//                        Intent intent = new Intent(getBaseContext(), Order_Category_Select.class);
//                        startActivity(intent);
//                    } else {
                    Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                    Log.e("Sub_Total", String.valueOf(FilterOrderList.get(position).getOrderValue() + ""));
                    intent.putExtra("Order_Values", FilterOrderList.get(position).getOrderValue() + "");
                    intent.putExtra("Invoice_Values", FilterOrderList.get(position).getInvoicevalues());
                    intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
                    intent.putExtra("Invoice_Date", FilterOrderList.get(position).getOrderDate());
                    intent.putExtra("NetAmount", FilterOrderList.get(position).getNetAmount());
                    intent.putExtra("Discount_Amount", FilterOrderList.get(position).getDiscount_Amount());
                    startActivity(intent);
                    //}

                }
            });
            invoicerecyclerview.setAdapter(mReportViewAdapter);
            lin_invoice.setOnClickListener(this);

            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6");

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


            navigateOrderScreen();


        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

            case R.id.lin_order:
                Shared_Common_Pref.Invoicetoorder = "0";
                getPreOrderQty();

                //Shared_Common_Pref.TransSlNo = "0";
                break;
            case R.id.lin_repeat_order:
                break;
            case R.id.lin_invoice:
                Shared_Common_Pref.Invoicetoorder = "2";
                getInvoiceOrderQty();
                break;
            case R.id.lin_repeat_invoice:
                break;
            case R.id.lastinvoice:
                common_class.CommonIntentwithoutFinish(MoreInfoActivity.class);
                // common_class.CommonIntentwithoutFinish(More_Info_Activity.class);

                break;
            case R.id.lin_noOrder:
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

            case R.id.tvOrder:
                getPreOrderQty();
                break;
        }
    }

    private void GetJsonData(String jsonResponse, String type) {

        //type =1 product category data values
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            //Category_Modal.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (type.equals("1")) {
//                    String id = String.valueOf(jsonObject1.optInt("id"));
//                    String name = jsonObject1.optString("name");
//                    String Division_Code = jsonObject1.optString("Division_Code");
//                    String Cat_Image = jsonObject1.optString("Cat_Image");
//                    String sampleQty = jsonObject1.optString("sampleQty");
//                    String colorflag = jsonObject1.optString("colorflag");
//                    Category_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));
                } else {
//                    Route_Code = jsonObject1.optString("cluster");
//                    Dirtributor_Cod = jsonObject1.optString("stockist");
                    Worktype_code = jsonObject1.optString("wtype");
                    // Distributor_Name = jsonObject1.optString("StkName");
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

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Calendar calobj = Calendar.getInstance();
                    String dateTime = df.format(calobj.getTime());

                    try {
                        JSONObject HeadItem = new JSONObject();
                        HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("Worktype_code", Worktype_code);
                        HeadItem.put("Town_code", sharedCommonPref.getvalue(Constants.Route_Id));
                        HeadItem.put("dcr_activity_date", dateTime);
                        HeadItem.put("Daywise_Remarks", "");
                        HeadItem.put("UKey", Common_Class.GetEkey());
                        HeadItem.put("orderValue", "0");
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", Shared_Common_Pref.DistributorCode);
                        OutletItem.put("stockist_name", Shared_Common_Pref.DistributorName);
                        OutletItem.put("orderValue", "0");
                        OutletItem.put("CashDiscount", "0");
                        OutletItem.put("NetAmount", "0");
                        OutletItem.put("No_Of_items", "0");
                        OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("TransSlNo", Shared_Common_Pref.TransSlNo);
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "no order");
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
                                    String san = jsonObjects.getString("success");
                                    Log.e("Success_Message", san);
                                    if (san.equals("true")) {

                                        Toast.makeText(Invoice_History.this, "No Order Submitted Successfully", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onLoadFilterData(List<Retailer_Modal_List> retailer_modal_list) {

    }

    @Override
    public void onLoadTodayOrderList(List<com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal> outletReportViewModals) {
        if (outletReportViewModals != null) {

            OutletReport_View_Modal.clear();

            OutletReport_View_Modal = outletReportViewModals;

            FilterOrderList.clear();

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
//                    if (FilterOrderList.get(position).getStatus().equals("ORDER")) {
//                        getPreOrderQty();
//
//                    } else {
                    Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                    Log.e("Sub_Total", String.valueOf(FilterOrderList.get(position).getOrderValue() + ""));
                    intent.putExtra("Order_Values", FilterOrderList.get(position).getOrderValue() + "");
                    intent.putExtra("Invoice_Values", FilterOrderList.get(position).getInvoicevalues());
                    intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
                    intent.putExtra("Invoice_Date", FilterOrderList.get(position).getOrderDate());
                    intent.putExtra("NetAmount", FilterOrderList.get(position).getNetAmount());
                    intent.putExtra("Discount_Amount", FilterOrderList.get(position).getDiscount_Amount());
                    startActivity(intent);
                    //  }

                }
            });
            invoicerecyclerview.setAdapter(mReportViewAdapter);
        }

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse) {

    }

    public void navigateOrderScreen() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                JSONObject HeadItem = new JSONObject();

                HeadItem.put("sfCode", Shared_Common_Pref.DistributorCode);
                HeadItem.put("divisionCode", Shared_Common_Pref.Div_Code);


                Call<ResponseBody> call = service.getSecondaryscheme(HeadItem.toString());
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

                                    Gson gson = new Gson();
                                    List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();


                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        product_details_modalArrayList.add(new Product_Details_Modal(jsonObject1.getString("Product_Code"),
                                                jsonObject1.getString("Scheme"), jsonObject1.getString("Free"),
                                                jsonObject1.getString("Discount"), jsonObject1.getString("Discount_Type"),
                                                jsonObject1.getString("Package"), "0", jsonObject1.getString("Offer_Product"),
                                                jsonObject1.getString("Offer_Product_Name"), jsonObject1.getString("offer_product_unit")));


                                    }

                                    sharedCommonPref.save(Constants.FreeSchemeDiscList, gson.toJson(product_details_modalArrayList));


                                } else {
                                    sharedCommonPref.clear_pref(Constants.FreeSchemeDiscList);

                                }


                            }

                        } catch (Exception e) {


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>", t.toString());


                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }


    private void getPreOrderQty() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                common_class.ProgressdialogShow(1, "");
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                JSONObject HeadItem = new JSONObject();

                HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);
                HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);


                Call<ResponseBody> call = service.getPreOrderQty(HeadItem.toString());
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

                                    Gson gson = new Gson();
                                    List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();


                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    if (jsonArray != null && jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                            product_details_modalArrayList.add(new Product_Details_Modal(jsonObject1.getString("Product_Detail_Code"),
                                                    "", "", jsonObject1.getInt("Qty"), ""));


                                        }
                                    }

                                    sharedCommonPref.save(Constants.PreOrderQtyList, gson.toJson(product_details_modalArrayList));


                                    common_class.CommonIntentwithFinish(Order_Category_Select.class);


                                    common_class.ProgressdialogShow(0, "");
                                    Log.v("PreOrderList: ", "" + product_details_modalArrayList.size());

                                } else {
                                    sharedCommonPref.clear_pref(Constants.PreOrderQtyList);
                                    Log.v("PreOrderList: ", "" + "not success");
                                    common_class.ProgressdialogShow(0, "");


                                }


                            }

                        } catch (Exception e) {
                            common_class.ProgressdialogShow(0, "");


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>", t.toString());
                        common_class.ProgressdialogShow(0, "");


                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }


    private void getInvoiceOrderQty() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                common_class.ProgressdialogShow(1, "");
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                JSONObject HeadItem = new JSONObject();

                HeadItem.put("OrderID", Shared_Common_Pref.OutletCode);


                Call<ResponseBody> call = service.getInvoiceOrderQty(HeadItem.toString());
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

                                    Gson gson = new Gson();
                                    List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();


                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    if (jsonArray != null && jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                            product_details_modalArrayList.add(new Product_Details_Modal(jsonObject1.getString("Product_Detail_Code"),
                                                    "", "", jsonObject1.getInt("Qty"), ""));


                                        }
                                    }

                                    sharedCommonPref.save(Constants.InvoiceQtyList, gson.toJson(product_details_modalArrayList));


                                    common_class.CommonIntentwithFinish(Order_Category_Select.class);


                                    common_class.ProgressdialogShow(0, "");
                                    Log.v("PreOrderList: ", "" + product_details_modalArrayList.size());


                                    common_class.CommonIntentwithFinish(Invoice_Category_Select.class);


                                } else {
                                    sharedCommonPref.clear_pref(Constants.InvoiceQtyList);
                                    Log.v("PreOrderList: ", "" + "not success");
                                    common_class.ProgressdialogShow(0, "");


                                }


                            }

                        } catch (Exception e) {
                            common_class.ProgressdialogShow(0, "");


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>", t.toString());
                        common_class.ProgressdialogShow(0, "");


                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }




 /*   public void ViewDateReport() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code.replace(",", ""));
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("fromdate", Common_Class.GetDateOnly());
        QueryString.put("todate", Common_Class.GetDateOnly());
        QueryString.put("Outlet_Code", Shared_Common_Pref.OutletCode);
        Log.e("Report_ValuesMap", QueryString.toString());
        Call<Object> call = service.GetRouteObject(QueryString, "{\"tableName\":\"GetOutletViewReport\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("MAster_Product_Details", response.body() + "");
                System.out.println("GetOutletView" + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                }.getType();
                OutletReport_View_Modal = gson.fromJson(new Gson().toJson(response.body()), userType);
                if (OutletReport_View_Modal.size() == 0) {
                    Toast.makeText(Invoice_History.this, "Order Not Available!", Toast.LENGTH_SHORT).show();
                }

                System.out.println("Product_Details_Size" + OutletReport_View_Modal.size());
                mReportViewAdapter = new Outlet_Report_View_Adapter(Invoice_History.this, OutletReport_View_Modal, new ViewReport() {
                    @Override
                    public void reportCliick(String productId, String orderDate) {
                        Intent intnet = new Intent(Invoice_History.this, Outet_Report_Details.class);
                        intnet.putExtra("Order_ID", productId);
                        intnet.putExtra("OrderDate", orderDate);
                        startActivity(intnet);
                    }
                });

                invoicerecyclerview.setAdapter(mReportViewAdapter);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }*/
}