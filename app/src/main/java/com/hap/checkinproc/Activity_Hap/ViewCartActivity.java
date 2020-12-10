package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.viewProduct;
import com.hap.checkinproc.Model_Class.Product_Array;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.CustomViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCartActivity extends AppCompatActivity {
    TextView toolHeader;
    ImageView imgBack;
    List<Product_Array> carsList;
    String SF_CODE, DIVISION_CODE, CUTT_OFF_CODE, WORK_TYPE, Town_code,
            reatilerID, retailerName,
            distributorId, distributorName, totalValueString;
    Integer orderType, totalOrderValue;
    CustomViewAdapter adapter;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    /* Submit button */
    String locationValue, dateTime, checkInTime, keyEk = "EK", KeyDate, KeyHyp = "-", keyCodeValue, checkOutTime;

    String time;
    SimpleDateFormat simpleDateFormat;
    Calendar calander;

    JSONObject person1;
    JSONArray sendArray;
    JSONObject stockReportObjectArray, reportObjectArray, PersonObjectArray, sampleReportObjectArray, eventCapturesObjectArray, pendingBillObjectArray, ComProductObjectArray, ActivityInputReport;
    ArrayList<String> listV = new ArrayList<>();

    EditText toolSearch;
    RecyclerView viewRecyclerview;
    Button mSubmit;
    Shared_Common_Pref shared_common_pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        shared_common_pref = new Shared_Common_Pref(this);

        SF_CODE = shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code);
        WORK_TYPE = shared_common_pref.getvalue("work_type_code");
        WORK_TYPE = "'" + WORK_TYPE + "'";
        Town_code = shared_common_pref.getvalue("town_code");
        Town_code = "'" + Town_code + "'";

        orderType = Integer.valueOf(shared_common_pref.getvalue("Phone_order_type"));

        totalOrderValue = Integer.valueOf(shared_common_pref.getvalue("Total_amount"));

        reatilerID = shared_common_pref.getvalue("Retailer_id");
        reatilerID = "'" + reatilerID + "'";
        retailerName = shared_common_pref.getvalue("Retailer_name");
        retailerName = "'" + retailerName + "'";

        distributorId = "'" + shared_common_pref.getvalue("distributor_id") + "'";
        distributorName = "'" + shared_common_pref.getvalue("distributor_name") + "'";

        Log.e("View_Cart_Activity", SF_CODE);
        Log.e("View_Cart_Activity", "asd" + WORK_TYPE);


        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));

            }
        });


        getToolbar();

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        mSubmit = findViewById(R.id.submit_button);


        @SuppressLint("WrongConstant")
        SharedPreferences sh = getSharedPreferences("MyPrefs", MODE_APPEND);
        //  SF_CODE = sh.getString("Sf_Code", "");
        DIVISION_CODE = sh.getString("Division_Code", "");
        CUTT_OFF_CODE = sh.getString("Cut_Off_Time", "");

        Log.e("SHARED_PERFERNCE", SF_CODE);
        Log.e("SHARED_PERFERNCE", DIVISION_CODE);
        Log.e("SHARED_PERFERNCE", CUTT_OFF_CODE);


        String carListAsString = getIntent().getStringExtra("list_as_string");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Product_Array>>() {
        }.getType();
        carsList = gson.fromJson(carListAsString, type);
    /*for (Product_Array cars : carsList){
            Log.i("Car__Data", cars.getProductname()+"-"+cars.getProductqty()+"__"+cars.getCatName()+"-"+cars.getProductcode());
        }*/
        viewRecyclerview = (RecyclerView) findViewById(R.id.report_list);
        viewRecyclerview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        viewRecyclerview.setLayoutManager(layoutManager);


        DateTime();
        currentTime();
        locationInitialize();
        startLocationUpdates();


        adapter = new CustomViewAdapter(this, carsList, new viewProduct() {

            @Override
            public void onViewItemClick(String itemID, String productName, String catName, String catImg, Integer productQty, Integer productRate) {

                System.out.println("Ka_Product_Code" + itemID);
                System.out.println("Ka_Product_Code" + productName);
                System.out.println("Ka_Product_Code" + productQty);
                System.out.println("Ka_Product_Code" + productRate);


                for (int i = 0; i < carsList.size(); i++) {
                    Log.e("Ka_Product_Code", "" + carsList.get(i).getCatName());
                    if (itemID == carsList.get(i).getProductcode()) {
                        System.out.println("Ka_Product_Code" + carsList.get(i).getProductname());
                        carsList.remove(i);

                        carsList.add(new Product_Array(itemID, productName, productQty, productQty, productRate, catImg, catName));
                        for (int j = 0; j < carsList.size(); j++) {
                            if (itemID == carsList.get(j).getProductcode()) {
                                Log.e("New_Qty_Value", "" + String.valueOf(carsList.get(j).getProductqty()));
                            }
                        }
                    }
                }


                /*ActivityReport*/
                DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar calobjw = Calendar.getInstance();
                Log.e("DATEFORMAT", String.valueOf(dfw.format(calobjw.getTime())));

                KeyDate = SF_CODE;
                keyCodeValue = keyEk + KeyDate + dfw.format(calobjw.getTime()).hashCode();

                Log.e("KEY_CODE_HASH", keyCodeValue);

                JSONObject reportObject = new JSONObject();

                reportObjectArray = new JSONObject();

                String sFCODE = "'" + SF_CODE + "'";

                try {
                    reportObject.put("Worktype_code", WORK_TYPE);
                    reportObject.put("Town_code", Town_code);
                    reportObject.put("RateEditable", "''");
                    reportObject.put("dcr_activity_date", dateTime);
                    reportObject.put("Daywise_Remarks", "''");
                    reportObject.put("eKey", keyCodeValue);
                    reportObject.put("rx", "'1'");
                    reportObject.put("rx_t", "''");
                    reportObject.put("DataSF", sFCODE);
                    reportObjectArray.put("Activity_Report_APP", reportObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }




                /*StockListReport*/
                JSONObject stockReportObject = new JSONObject();
                stockReportObjectArray = new JSONObject();
                JSONObject fkeyStock = new JSONObject();

                try {
                    stockReportObject.put("Doctor_POB", 0);
                    stockReportObject.put("Worked_With", sFCODE);
                    stockReportObject.put("Doc_Meet_Time", dateTime);
                    stockReportObject.put("modified_time", dateTime);
                    stockReportObject.put("net_weight_value", 0);
                    stockReportObject.put("stockist_code", distributorId);
                    stockReportObject.put("stockist_name", distributorName);
                    stockReportObject.put("superstockistid", "''");
                    stockReportObject.put("Discountpercent", 0);
                    stockReportObject.put("CheckinTime", checkInTime);
                    stockReportObject.put("CheckoutTime", checkOutTime);
                    stockReportObject.put("location", "''");
                    stockReportObject.put("geoaddress", "");
                    stockReportObject.put("PhoneOrderTypes", orderType);
                    stockReportObject.put("Order_Stk", distributorId);
                    stockReportObject.put("Order_No", "''");
                    stockReportObject.put("rootTarget", "0");
                    stockReportObject.put("orderValue", totalOrderValue);
                    stockReportObject.put("rateMode", "free");
                    stockReportObject.put("discount_price", 0);
                    stockReportObject.put("doctor_code", reatilerID);
                    stockReportObject.put("doctor_name", retailerName);
                    stockReportObject.put("f_key", fkeyStock);
                    fkeyStock.put("Activity_Report_Code", "'Activity_Report_APP'");
                    stockReportObjectArray.put("Activity_Doctor_Report", stockReportObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String stockReport = stockReportObjectArray.toString();

                System.out.println(" Activity_Stockist_Report" + stockReport);


                /*Activity_Trans_Order_Details*/
                JSONArray sampleReportArray = new JSONArray();
                sampleReportObjectArray = new JSONObject();

                try {

                    sampleReportObjectArray.put("Trans_Order_Details", sampleReportArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String sampleReport = sampleReportObjectArray.toString();
                System.out.println(" Trans_Order_Details" + sampleReport);



                /*Activity_Input_Report*/
                JSONArray Activity_Input_Report = new JSONArray();
                ActivityInputReport = new JSONObject();

                try {
                    ActivityInputReport.put("Activity_Input_Report", Activity_Input_Report);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String inputReport = ActivityInputReport.toString();

                System.out.println(" Activity_Input_Report" + inputReport);




                /*Activity_Event_Captures*/

                JSONArray eventCapturesArray = new JSONArray();
                eventCapturesObjectArray = new JSONObject();


                try {

                    eventCapturesObjectArray.put("Activity_Event_Captures", eventCapturesArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String EventCap = eventCapturesObjectArray.toString();
                System.out.println("Activity_Event_Captures" + EventCap);


                /*PENDING_Bills*/
                JSONArray pendingBillArray = new JSONArray();
                pendingBillObjectArray = new JSONObject();

                try {
                    pendingBillObjectArray.put("PENDING_Bills", pendingBillArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String PendingBill = pendingBillObjectArray.toString();
                System.out.println(" PENDING_Bills" + PendingBill);


                /*Compititor_Product*/
                JSONArray ComProductArray = new JSONArray();
                ComProductObjectArray = new JSONObject();

                try {
                    ComProductObjectArray.put("Compititor_Product", ComProductArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String ComProduct = ComProductObjectArray.toString();

                System.out.println(" Compititor_Product" + ComProduct);


                /*product_order_list*/
                List<JSONObject> myJSONObjects = new ArrayList<JSONObject>(carsList.size());


                JSONArray personarray = new JSONArray();
                PersonObjectArray = new JSONObject();
                JSONObject fkeyprodcut = new JSONObject();

                Integer product_total;
                for (int z = 0; z < carsList.size(); z++) {
                    person1 = new JSONObject();


                    try {
                        product_total = carsList.get(z).getProductqty() * carsList.get(z).getProductRate();
                        //adding items to first json object
                        person1.put("product_code", carsList.get(z).getProductcode());
                        person1.put("product_Name", carsList.get(z).getProductname());
                        person1.put("Product_Rx_Qty", carsList.get(z).getProductqty());
                        person1.put("Rate", carsList.get(z).getProductRate());
                        person1.put("Product_Sample_Qty", product_total);
                        person1.put("f_key", fkeyprodcut);
                        fkeyprodcut.put("Activity_MSL_Code", "Activity_Doctor_Report");


                        myJSONObjects.add(person1);
                        listV.add(String.valueOf((person1)));
                        personarray.put(person1);
                        PersonObjectArray.put("Activity_Sample_Report", personarray);
                        String JsonData = PersonObjectArray.toString();

                        System.out.println("Activity_Sample_Report: " + JsonData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                sendArray = new JSONArray();
                sendArray.put(reportObjectArray);
                sendArray.put(stockReportObjectArray);
                sendArray.put(PersonObjectArray);
                sendArray.put(sampleReportObjectArray);
                sendArray.put(ActivityInputReport);
                sendArray.put(eventCapturesObjectArray);
                sendArray.put(pendingBillObjectArray);
                sendArray.put(ComProductObjectArray);
                totalValueString = sendArray.toString();

            }
        });

        viewRecyclerview.setAdapter(adapter);


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveProduct();
            }
        });

    }


    /*Toolbar*/
    public void getToolbar() {


    }


    /*Date and Time Format*/
    public void DateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calobj = Calendar.getInstance();
        dateTime = "'" + df.format(calobj.getTime()) + "'";
        System.out.println("Date_and_Time" + dateTime);
    }


    /*Current Time for Cuttoff Process*/
    public void currentTime() {
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(calander.getTime());
        checkInTime = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
        Log.e("CHECK_TIME", checkInTime);
    }



    /*Location Initialize*/

    public void locationInitialize() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();

                getLoactionValue();
            }
        };


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /*Displaying Location Result*/
    private void getLoactionValue() {
        if (mCurrentLocation != null) {
            Log.e("Current_Location_Value", "Lat: " + mCurrentLocation.getLatitude() + ", " +
                    "Lng: " + mCurrentLocation.getLongitude());


            locationValue = String.valueOf(mCurrentLocation.getLatitude());
            locationValue = locationValue.concat(":");
            locationValue = "'" + locationValue.concat(String.valueOf(mCurrentLocation.getLongitude())) + "'";


        }
    }


    /*Getting Location Result*/
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        getLoactionValue();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(ViewCartActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {

                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        }
                        getLoactionValue();
                    }
                });
    }



    /*Save to server*/

    public void SaveProduct() {
        /*ActivityReport*/
        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calobjw = Calendar.getInstance();
        KeyDate = SF_CODE;
        keyCodeValue = keyEk + KeyDate + dfw.format(calobjw.getTime()).hashCode();

        Log.e("KEY_CODE_HASH", keyCodeValue);

        JSONObject reportObject = new JSONObject();

        reportObjectArray = new JSONObject();

        String sFCODE = "'" + SF_CODE + "'";





        /*Check out timing*/
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(calander.getTime());
        checkOutTime = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());


        try {
            reportObject.put("Worktype_code", WORK_TYPE);
            reportObject.put("Town_code", Town_code);
            reportObject.put("RateEditable", "''");
            reportObject.put("dcr_activity_date", dateTime);
            reportObject.put("Daywise_Remarks", "''");
            reportObject.put("eKey", keyCodeValue);
            reportObject.put("rx", "'1'");
            reportObject.put("rx_t", "''");
            reportObject.put("DataSF", sFCODE);
            reportObjectArray.put("Activity_Report_APP", reportObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String Activity_Report_APP = reportObjectArray.toString();

        System.out.println(" Activity_Report_APP" + Activity_Report_APP);


        /*StockListReport*/
        JSONObject stockReportObject = new JSONObject();
        stockReportObjectArray = new JSONObject();
        JSONObject fkeyStock = new JSONObject();


        try {

            stockReportObject.put("Doctor_POB", 0);
            stockReportObject.put("Worked_With", sFCODE);
            stockReportObject.put("Doc_Meet_Time", dateTime);
            stockReportObject.put("modified_time", dateTime);
            stockReportObject.put("net_weight_value", 0);
            stockReportObject.put("stockist_code", distributorId);
            stockReportObject.put("stockist_name", distributorName);
            stockReportObject.put("superstockistid", "''");
            stockReportObject.put("Discountpercent", 0);
            stockReportObject.put("CheckinTime", checkInTime);
            stockReportObject.put("CheckoutTime", checkOutTime);
            stockReportObject.put("location", "''");
            stockReportObject.put("geoaddress", "");
            stockReportObject.put("PhoneOrderTypes", orderType);
            stockReportObject.put("Order_Stk", distributorId);
            stockReportObject.put("Order_No", "''");
            stockReportObject.put("rootTarget", "0");
            stockReportObject.put("orderValue", totalOrderValue);
            stockReportObject.put("rateMode", "free");
            stockReportObject.put("discount_price", 0);
            stockReportObject.put("doctor_code", reatilerID);
            stockReportObject.put("doctor_name", retailerName);
            stockReportObject.put("f_key", fkeyStock);
            fkeyStock.put("Activity_Report_Code", "'Activity_Report_APP'");
            stockReportObjectArray.put("Activity_Doctor_Report", stockReportObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        String stockReport = stockReportObjectArray.toString();

        System.out.println(" Activity_Stockist_Report" + stockReport);


        /*Activity_Trans_Order_Details*/
        JSONArray sampleReportArray = new JSONArray();
        sampleReportObjectArray = new JSONObject();

        try {

            sampleReportObjectArray.put("Trans_Order_Details", sampleReportArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sampleReport = sampleReportObjectArray.toString();
        System.out.println(" Trans_Order_Details" + sampleReport);



        /*Activity_Input_Report*/
        JSONArray Activity_Input_Report = new JSONArray();
        ActivityInputReport = new JSONObject();

        try {
            ActivityInputReport.put("Activity_Input_Report", Activity_Input_Report);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String inputReport = ActivityInputReport.toString();

        System.out.println(" Activity_Input_Report" + inputReport);




        /*Activity_Event_Captures*/

        JSONArray eventCapturesArray = new JSONArray();
        eventCapturesObjectArray = new JSONObject();


        try {

            eventCapturesObjectArray.put("Activity_Event_Captures", eventCapturesArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String EventCap = eventCapturesObjectArray.toString();
        System.out.println("Activity_Event_Captures" + EventCap);


        /*PENDING_Bills*/
        JSONArray pendingBillArray = new JSONArray();
        pendingBillObjectArray = new JSONObject();

        try {
            pendingBillObjectArray.put("PENDING_Bills", pendingBillArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String PendingBill = pendingBillObjectArray.toString();
        System.out.println(" PENDING_Bills" + PendingBill);


        /*Compititor_Product*/
        JSONArray ComProductArray = new JSONArray();
        ComProductObjectArray = new JSONObject();

        try {
            ComProductObjectArray.put("Compititor_Product", ComProductArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String ComProduct = ComProductObjectArray.toString();

        System.out.println(" Compititor_Product" + ComProduct);


        /*product_order_list*/
        List<JSONObject> myJSONObjects = new ArrayList<JSONObject>(carsList.size());


        JSONArray personarray = new JSONArray();
        PersonObjectArray = new JSONObject();
        JSONObject fkeyprodcut = new JSONObject();


        Integer product_total;
        for (int z = 0; z < carsList.size(); z++) {
            person1 = new JSONObject();


            try {
                product_total = carsList.get(z).getProductqty() * carsList.get(z).getProductRate();
                //adding items to first json object
                person1.put("product_code", carsList.get(z).getProductcode());
                person1.put("product_Name", carsList.get(z).getProductname());
                person1.put("Product_Rx_Qty", carsList.get(z).getProductqty());
                person1.put("Rate", carsList.get(z).getProductRate());
                person1.put("Product_Sample_Qty", product_total);
                person1.put("f_key", fkeyprodcut);
                fkeyprodcut.put("Activity_MSL_Code", "Activity_Doctor_Report");


                myJSONObjects.add(person1);
                listV.add(String.valueOf((person1)));
                personarray.put(person1);
                PersonObjectArray.put("Activity_Sample_Report", personarray);
                String JsonData = PersonObjectArray.toString();

                System.out.println("Activity_Sample_Report: " + JsonData);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        sendArray = new JSONArray();

        sendArray.put(reportObjectArray);
        sendArray.put(stockReportObjectArray);
        sendArray.put(PersonObjectArray);
        sendArray.put(sampleReportObjectArray);
        sendArray.put(ActivityInputReport);
        sendArray.put(eventCapturesObjectArray);
        sendArray.put(pendingBillObjectArray);
        sendArray.put(ComProductObjectArray);


        totalValueString = sendArray.toString();


        Log.e("SUBMIT_VALUE", totalValueString);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> responseBodyCall = apiInterface.submitValue(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), SF_CODE, totalValueString);

        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.e("Success", response.body().toString());
                    Log.e("totalValueString", totalValueString);
                    try {
                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                        String san = jsonObjects.getString("success");
                        Log.e("StateCodeSet", san);
                        startActivity(new Intent(ViewCartActivity.this, OrderDashBoard.class));
                        Toast.makeText(ViewCartActivity.this, "Your order submitted successfully", Toast.LENGTH_SHORT).show();

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


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    AlertDialogBox.showDialog(ViewCartActivity.this, "", "Do you want to exit?", "Yes", "NO", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            onSuperBackPressed();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                        }
                    });
                }
            });

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {

    }
}