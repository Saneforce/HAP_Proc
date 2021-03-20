package com.hap.checkinproc.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.ReatilRouteModel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;

public class AddNewRetailer extends AppCompatActivity implements Master_Interface {
    TextView toolHeader;
    CustomListViewDialog customDialog;
    ImageView imgBack;
    EditText toolSearch, retailercode;
    Button mSubmit;
    ApiInterface service;
    LinearLayout linReatilerRoute, linReatilerClass, linReatilerChannel;
    TextView txtRetailerRoute, txtRetailerClass, txtRetailerChannel;
    Type userType;
    List<Common_Model> modelRetailClass = new ArrayList<>();
    List<Common_Model> modelRetailChannel = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    List<ReatilRouteModel> mRetailerDetailsModels;
    Gson gson;
    EditText addRetailerName, addRetailerAddress, addRetailerCity, addRetailerPhone, addRetailerEmail, CurrentLocationsAddress;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    JSONArray mainArray;
    JSONObject docMasterObject;
    String keyEk = "N", KeyDate, KeyHyp = "-", keyCodeValue;
    Integer routeId1, classId, channelID;
    String routeId;
    Shared_Common_Pref shared_common_pref;
    SharedPreferences CheckInDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_retailer);
        linReatilerRoute = findViewById(R.id.linear_Retailer);
        txtRetailerRoute = findViewById(R.id.retailer_type);
        retailercode = findViewById(R.id.retailercode);
        CurrentLocationsAddress = findViewById(R.id.CurrentLocationsAddress);
        gson = new Gson();
        service = ApiClient.getClient().create(ApiInterface.class);
        mSubmit = findViewById(R.id.submit_button);
        CurrentLocationsAddress.setText("" + Shared_Common_Pref.OutletAddress);
        if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
            mSubmit.setVisibility(View.VISIBLE);
        } else {
            Shared_Common_Pref.Outler_AddFlag = "0";
        }
        shared_common_pref = new Shared_Common_Pref(this);
        getRouteDetails();
        getRetailerClass();
        getRetailerChannel();
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });


        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        OnclickRoute();
        onClickRetailerClass();
        onClickRetailerChannel();
        addRetailerName = findViewById(R.id.edt_new_name);
        addRetailerAddress = findViewById(R.id.edt_new_address);
        addRetailerCity = findViewById(R.id.edt_new_city);
        addRetailerPhone = findViewById(R.id.edt_new_phone);
        addRetailerEmail = findViewById(R.id.edt_new_email);
        addRetailerName.clearFocus();
        Intent i = getIntent();
        if (i != null && i.getExtras() != null) {
            addRetailerName.setText("" + i.getExtras().getString("OutletName"));
            addRetailerAddress.setText("" + i.getExtras().getString("OutletAddress"));
            txtRetailerRoute.setText("" + i.getExtras().getString("OutletRoute"));
            addRetailerPhone.setText("" + i.getExtras().getString("OutletMobile"));
            retailercode.setText("" + i.getExtras().getString("OutletCode"));
        }

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtRetailerRoute.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Select route", Toast.LENGTH_SHORT).show();
                } else if (addRetailerName.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (addRetailerAddress.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter Address", Toast.LENGTH_SHORT).show();
                } else if (addRetailerCity.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_SHORT).show();
                } else if (addRetailerPhone.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter Phone", Toast.LENGTH_SHORT).show();
                } else if (addRetailerEmail.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (txtRetailerClass.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Select Class", Toast.LENGTH_SHORT).show();
                } else if (txtRetailerChannel.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Select Channel", Toast.LENGTH_SHORT).show();
                } else if (!addRetailerEmail.getText().toString().trim().matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                } else {
                    addNewRetailers();
                    Toast.makeText(AddNewRetailer.this, "New Retailer Added successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    /*Route Details*/
    public void getRouteDetails() {

        String routeMap = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", routeMap);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                Log.e("Route_response", response.body().toString());

                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String className = String.valueOf(jsonObject.get("name"));
                    String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                    Log.e("RETAILER_CLASS_NAME", retailerClass);
                    String id = String.valueOf(jsonObject.get("id"));
                    mCommon_model_spinner = new Common_Model(id, retailerClass, "flag");
                    Log.e("LeaveType_Request", retailerClass);
                    modelRetailDetails.add(mCommon_model_spinner);
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("Route_response", "ERROR");
            }
        });
    }

    /*Route Click*/
    public void OnclickRoute() {

        linReatilerRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog = new CustomListViewDialog(AddNewRetailer.this, modelRetailDetails, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
    }


    /*Route Class*/
    public void getRetailerClass() {
        String routeMap = "{\"tableName\":\"Mas_Doc_Class\",\"coloumns\":\"[\\\"Doc_ClsCode as id\\\", \\\"Doc_ClsSName as name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", routeMap);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();


                Log.e("RESPONSE_VALUE", String.valueOf(jsonArray));
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String className = String.valueOf(jsonObject.get("name"));
                    String id = String.valueOf(jsonObject.get("id"));
                    String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                    Log.e("RETAILER_CLASS_NAME", retailerClass);

                    mCommon_model_spinner = new Common_Model(id, retailerClass, "flag");
                    Log.e("LeaveType_Request", retailerClass);
                    modelRetailClass.add(mCommon_model_spinner);
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    /*Retailer Class Click*/
    public void onClickRetailerClass() {
        linReatilerClass = findViewById(R.id.linear_retailer_class);
        txtRetailerClass = findViewById(R.id.txt_retailer_class);
        linReatilerClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog = new CustomListViewDialog(AddNewRetailer.this, modelRetailClass, 9);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
    }

    /*Retailer Channel */
    public void getRetailerChannel() {
        String routeMap = "{\"tableName\":\"Doctor_Specialty\",\"coloumns\":\"[\\\"Specialty_Code as id\\\", \\\"Specialty_Name as name\\\"]\",\"where\":\"[\\\"isnull(Deactivate_flag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", routeMap);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                Log.e("RESPONSE_VALUE", String.valueOf(jsonArray));
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String className = String.valueOf(jsonObject.get("name"));
                    String id = String.valueOf(jsonObject.get("id"));
                    String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                    Log.e("RETAILER_CLASS_NAME", retailerClass);
                    mCommon_model_spinner = new Common_Model(id, retailerClass, "flag");
                    Log.e("LeaveType_Request", retailerClass);
                    modelRetailChannel.add(mCommon_model_spinner);
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    /*Retailer Channel Click*/
    public void onClickRetailerChannel() {
        linReatilerChannel = findViewById(R.id.linear_retailer_channel);
        txtRetailerChannel = findViewById(R.id.txt_retailer_channel);
        linReatilerChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(AddNewRetailer.this, modelRetailChannel, 10);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
    }


    /*Add New Retailer*/
    public void addNewRetailers() {


        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calobjw = Calendar.getInstance();
        KeyDate = shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code);
        keyCodeValue = keyEk + KeyHyp + KeyDate + dfw.format(calobjw.getTime()).hashCode();

        Log.e("KEY_CODE_HASH", keyCodeValue);


        JSONObject reportObject = new JSONObject();

        docMasterObject = new JSONObject();


        try {
            reportObject.put("town_code", "'" + routeId + "'");
            reportObject.put("wlkg_sequence", "null");
            reportObject.put("unlisted_doctor_name", "'" + addRetailerName.getText().toString() + "'");
            reportObject.put("unlisted_doctor_address", "'" + addRetailerAddress.getText().toString() + "'");
            reportObject.put("unlisted_doctor_phone", "'" + addRetailerPhone.getText().toString() + "'");
            reportObject.put("unlisted_doctor_cityname", "'" + addRetailerCity.getText().toString() + "'");
            reportObject.put("unlisted_doctor_landmark", "''");
            reportObject.put("lat", "''");
            reportObject.put("long", "''");
            reportObject.put("unlisted_doctor_areaname", "''");
            reportObject.put("unlisted_doctor_contactperson", "''");
            reportObject.put("unlisted_doctor_designation", "''");
            reportObject.put("unlisted_doctor_gst", "''");
            reportObject.put("unlisted_doctor_pincode", "''");
            reportObject.put("unlisted_doctor_phone2", "''");
            reportObject.put("unlisted_doctor_phone3", "''");
            reportObject.put("unlisted_doctor_contactperson2", "''");
            reportObject.put("unlisted_doctor_contactperson3", "''");
            reportObject.put("unlisted_doctor_designation2", "''");
            reportObject.put("unlisted_cat_code", "null");
            reportObject.put("unlisted_specialty_code", channelID);
            reportObject.put("unlisted_qulifi", "'samp'");
            reportObject.put("unlisted_class", classId);
            reportObject.put("DrKeyId", "'" + keyCodeValue + "'");
            docMasterObject.put("unlisted_doctor_master", reportObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mainArray = new JSONArray();
        mainArray.put(docMasterObject);
        String totalValueString = mainArray.toString();
        Log.e("TOTAL_VALUE_STRING", totalValueString);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        // addNewRetailer
        Call<JsonObject> call = apiInterface.addNewRetailer(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", "MGR", totalValueString);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                Log.e("Add_Retailer_details", String.valueOf(jsonObject));
                String success = String.valueOf(jsonObject.get("success"));
                if (success.equalsIgnoreCase("true") && Shared_Common_Pref.Outler_AddFlag.equals("0")) {
                    startActivity(new Intent(getApplicationContext(), SecondaryOrderActivity.class));
                } else if (success.equalsIgnoreCase("true") && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                    Shared_Common_Pref.Outler_AddFlag = "0";
                    startActivity(new Intent(getApplicationContext(), Dashboard_Route.class));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();

        if (type == 8) {
            txtRetailerRoute.setText(myDataset.get(position).getName());
            routeId = myDataset.get(position).getId();
            routeId = String.valueOf(routeId.subSequence(1, routeId.length() - 1));
            routeId1 = Integer.valueOf(routeId);
            Log.e("ASDFGHJ", "" + routeId);
        } else if (type == 9) {
            txtRetailerClass.setText(myDataset.get(position).getName());
            classId = Integer.valueOf(myDataset.get(position).getId());

        } else if (type == 10) {
            txtRetailerChannel.setText(myDataset.get(position).getName());
            channelID = Integer.valueOf(myDataset.get(position).getId());

        }
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {

                    finish();
                }
            });

    @Override
    public void onBackPressed() {

    }

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


}
