package com.hap.checkinproc.Activity_Hap;

import android.content.Intent;
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
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.RetailerDetailsModel;
import com.hap.checkinproc.Model_Class.RetailerViewDetails;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.RetailerViewAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondaryOrderActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    LinearLayout OrderType, RetailerType, linearCategory;
    String lastOrderAmount = "", mobileNumber = "";
    TextView txtOrder, txtRetailer, txtAddRetailer, txtRetailerChannel, txtClass, txtLastOrderAmount, txtModelOrderValue, txtLastVisited, txtReamrks, txtMobile, txtMobileTwo, txtTempalate, txtDistributor;
    CustomListViewDialog customDialog;
    EditText edtRemarks;
    List<Common_Model> modelRetailChannel = new ArrayList<>();
    Button mSubmit;
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    private ArrayList<String> temaplateList;
    List<RetailerViewDetails> mRetailerViewDetails;
    Shared_Common_Pref shared_common_pref;

    List<RetailerDetailsModel> mRetailerDetailsModels;
    Gson gson;
    LinearLayout mRetailerDetails;
    Type userType;
    String retailerId;
    Integer count;

    RetailerViewAdapter mRetailerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_secondary_order);
        gson = new Gson();
        shared_common_pref = new Shared_Common_Pref(this);

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
        initilaize();
        RetailerType();
        getTemaplte();
        onClickRetailerTemplate();

    }

    public void initilaize() {


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        mSubmit = findViewById(R.id.submit_button);
        mSubmit.setText("Submit");
        txtRetailer = findViewById(R.id.retailer_type);
        txtOrder = findViewById(R.id.order_type);
        txtAddRetailer = findViewById(R.id.txt_add_retailer);
        txtAddRetailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondaryOrderActivity.this, AddNewRetailer.class
                ));
            }
        });

        RetailerType = findViewById(R.id.linear_Retailer);
        OrderType = findViewById(R.id.linear_order);

        OrderType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderType.clear();
                OrderType();
            }
        });


        RetailerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(SecondaryOrderActivity.this, modelRetailDetails, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

            }
        });


        linearCategory = findViewById(R.id.prm_linear_orders);
        linearCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtOrder.getText().toString().matches("")) {
                    Toast.makeText(SecondaryOrderActivity.this, "Enter Text Order", Toast.LENGTH_SHORT).show();
                } else if (txtRetailer.getText().toString().matches("")) {
                    Toast.makeText(SecondaryOrderActivity.this, "Enter Text Retailer", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(SecondaryOrderActivity.this, OrderCategoryActivity.class));
                }
            }
        });


        txtRetailerChannel = findViewById(R.id.retailer_channel);
        txtClass = findViewById(R.id.txt_class);
        txtModelOrderValue = findViewById(R.id.model_order_vlaue);
        txtLastVisited = findViewById(R.id.txt_last_visited);
        txtLastOrderAmount = findViewById(R.id.txt_last_order_amount);
        txtReamrks = findViewById(R.id.txt_remarks);
        txtMobile = findViewById(R.id.txt_mobile);
        txtMobileTwo = findViewById(R.id.txt_mobile2);
        txtDistributor = findViewById(R.id.txt_distributor);
        mRetailerDetails = findViewById(R.id.linear_reatiler_details);


    }


    /*   Order Types*/
    public void OrderType() {
        temaplateList = new ArrayList<>();
        temaplateList.add("Phone Order");
        temaplateList.add("Field Order");

        for (int i = 0; i < temaplateList.size(); i++) {
            String id = String.valueOf(temaplateList.get(i));
            String name = temaplateList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            Log.e("LeaveType_Request", id);
            Log.e("LeaveType_Request", name);
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(SecondaryOrderActivity.this, listOrderType, 9);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();


    }


    /*Retailer Type*/
    public void RetailerType() {
        String RetailerDetails = "{\"tableName\":\"vwDoctor_Master_APP\",\"coloumns\":\"[\\\"doctor_code as id\\\", \\\"doctor_name as name\\\",\\\"town_code\\\",\\\"town_name\\\",\\\"lat\\\",\\\"long\\\",\\\"addrs\\\",\\\"ListedDr_Address1\\\",\\\"ListedDr_Sl_No\\\",\\\"Mobile_Number\\\",\\\"Doc_cat_code\\\",\\\"ContactPersion\\\",\\\"Doc_Special_Code\\\"]\",\"where\":\"[\\\"isnull(Doctor_Active_flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient2().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.retailerClass("4", "MR2408", "MR2408", "24", RetailerDetails);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String id = String.valueOf(jsonObject.get("id"));
                    String name = String.valueOf(jsonObject.get("name"));
                    String townName = String.valueOf(jsonObject.get("ListedDr_Address1"));
                    String phone = String.valueOf(jsonObject.get("Mobile_Number"));
                    String strName = String.valueOf(name.subSequence(1, name.length() - 1));
                    String strTownName = String.valueOf(townName.subSequence(1, townName.length() - 1));
                    String strPhone = String.valueOf(phone.subSequence(1, phone.length() - 1));
                    mCommon_model_spinner = new Common_Model(strName, id, "flag", strTownName, strPhone);
                    modelRetailDetails.add(mCommon_model_spinner);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }



    /*Retailer Details*/

    public void RetailerViewDetailsMethod() {
        mRetailerDetails.setVisibility(View.VISIBLE);
        ApiInterface apiInterface2 = ApiClient.getClient2().create(ApiInterface.class);

        Log.e("API_INTERFACE", apiInterface2.toString());
        Call<RetailerViewDetails> call = apiInterface2.retailerViewDetails(retailerId, "4", "MR2408");
        Log.e("Retailer_ID_DETAILS", retailerId);

        call.enqueue(new Callback<RetailerViewDetails>() {
            @Override
            public void onResponse(Call<RetailerViewDetails> call, Response<RetailerViewDetails> response) {

                RetailerViewDetails mRetailerViewDetail = response.body();


                txtRetailerChannel.setText(mRetailerViewDetail.getDrSpl());
                txtClass.setText(mRetailerViewDetail.getDrCat());
                lastOrderAmount = String.valueOf(mRetailerViewDetail.getLastorderAmount());
                txtLastOrderAmount.setText(lastOrderAmount);
                txtLastVisited.setText(mRetailerViewDetail.getLVDt());
                txtReamrks.setText(mRetailerViewDetail.getRmks());
                txtMobile.setText(mRetailerViewDetail.getPOTENTIAL().get(0).getListedDrPhone());
                mobileNumber = String.valueOf(mRetailerViewDetail.getPOTENTIAL().get(0).getListedDrMobile());
                txtMobileTwo.setText(mobileNumber);


             /*   for (int a = 0; a < mRetailerViewDetail.getStockistDetails().size(); a++) {
                    String sd = mRetailerViewDetail.getStockistDetails().get(a).getOrderDetails().get(a).getProductName();
                    Log.e("PRODUCT_NAME", sd);

                }*/

            }

            @Override
            public void onFailure(Call<RetailerViewDetails> call, Throwable t) {
                Log.d("Retailer_Details", "Error");
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 8) {
            txtRetailer.setText(myDataset.get(position).getName());
            retailerId = myDataset.get(position).getId();
            retailerId = String.valueOf(retailerId.subSequence(1, retailerId.length() - 1));
            RetailerViewDetailsMethod();

            shared_common_pref.save("Retailer_id", retailerId);
            shared_common_pref.save("Retailer_name", myDataset.get(position).getName());
            Log.e("Retailer_ID", myDataset.get(position).getName());
        } else if (type == 9) {
            txtOrder.setText(myDataset.get(position).getName());
            if (myDataset.get(position).getName().toString().matches("Phone Order")) {
                count = 0;
            } else {
                count = 1;
            }
            shared_common_pref.save("Phone_order_type", String.valueOf(count));
            Log.e("Phone_order_type", String.valueOf(count));


        } else if (type == 11) {
            edtRemarks.setText(myDataset.get(position).getName() + " ");
            edtRemarks.setSelection(edtRemarks.getText().length());
        }
    }


    /*Retailer Template */
    public void getTemaplte() {
        String routeMap = "{\"tableName\":\"vwRmksTemplate\",\"coloumns\":\"[\\\"id as id\\\", \\\"content as name\\\"]\",\"where\":\"[\\\"isnull(ActFlag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient2().create(ApiInterface.class);

        Call<JsonArray> call = apiInterface.retailerClass("4", "MR2408", "MR2408", "24", routeMap);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String className = String.valueOf(jsonObject.get("name"));
                    String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                    Log.e("RETAILER_CLASS_NAME", retailerClass);

                    mCommon_model_spinner = new Common_Model(retailerClass, retailerClass, "flag");
                    Log.e("LeaveType_Request", retailerClass);
                    modelRetailChannel.add(mCommon_model_spinner);
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    /*Retailer Template Click*/
    public void onClickRetailerTemplate() {
        txtTempalate = findViewById(R.id.txt_templates);
        edtRemarks = findViewById(R.id.remarks_week);
        txtTempalate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog = new CustomListViewDialog(SecondaryOrderActivity.this, modelRetailChannel, 11);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    onSuperBackPressed();
                }
            });

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {

    }
}
