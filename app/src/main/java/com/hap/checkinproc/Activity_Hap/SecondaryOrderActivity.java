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

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Model;
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
    String retailerChannel = "", retailerClass = "", modelSum = "", lastVisited = "", retailerRemarks = "";
    TextView txtOrder, txtRetailer, txtAddRetailer, txtRetailerChannel, txtClass, txtModelOrderValue, txtLastVisited, txtReamrks;
    CustomListViewDialog customDialog;


    EditText toolSearch;
    Button mSubmit;
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    private ArrayList<String> temaplateList;

    List<RetailerDetailsModel> mRetailerDetailsModels;
    Gson gson;
    LinearLayout mRetailerDetails;
    Type userType;
    String retailerId;

    RetailerViewAdapter mRetailerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_order);
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
    }

    public void initilaize() {

        gson = new Gson();

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
                startActivity(new Intent(SecondaryOrderActivity.this, OrderCategoryActivity.class));
            }
        });


        txtRetailerChannel = findViewById(R.id.retailer_channel);
        txtClass = findViewById(R.id.txt_class);
        txtModelOrderValue = findViewById(R.id.model_order_vlaue);
        txtLastVisited = findViewById(R.id.txt_last_visited);
        txtReamrks = findViewById(R.id.txt_remarks);
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
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObject("3", "MGR4762", "MGR4762", "24", RetailerDetails);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                userType = new TypeToken<ArrayList<RetailerDetailsModel>>() {
                }.getType();
                mRetailerDetailsModels = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < mRetailerDetailsModels.size(); i++) {
                    String id = String.valueOf(mRetailerDetailsModels.get(i).getId());
                    String name = mRetailerDetailsModels.get(i).getName();
                    String address = mRetailerDetailsModels.get(i).getListedDrAddress1();
                    mCommon_model_spinner = new Common_Model(name, id, "flag", address);
                    Log.e("LeaveType_Request", id);
                    Log.e("LeaveType_Request", name);
                    modelRetailDetails.add(mCommon_model_spinner);
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }



    /*Retailer Details*/

    public void RetailerViewDetailsMethod() {
        mRetailerDetails.setVisibility(View.VISIBLE);

        Log.e("Retailer_ID", retailerId);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<RetailerViewDetails> call = service.getRetailerDetails("3", "MGR4762", retailerId);
        call.enqueue(new Callback<RetailerViewDetails>() {
            @Override
            public void onResponse(Call<RetailerViewDetails> call, Response<RetailerViewDetails> response) {

                RetailerViewDetails retailerViewDetails = response.body();


                retailerChannel = retailerViewDetails.getDrSpl();
                Log.e("Retailer_channel", retailerChannel);
                txtRetailerChannel.setText(retailerChannel);

                retailerClass = retailerViewDetails.getDrCat();
                Log.e("Retailer_channel", retailerClass);
                txtClass.setText(retailerClass);

                modelSum = String.valueOf(retailerViewDetails.getMOV().get(0).getMorderSum());
                Log.e("Retailer_channel", modelSum);
                txtModelOrderValue.setText(modelSum);
/*

                lastVisited = String.valueOf(retailerViewDetails.getLVDt());
                Log.e("Retailer_channel", lastVisited);
                txtLastVisited.setText(lastVisited);

                retailerRemarks = retailerViewDetails.getRmks();
                Log.e("Retailer_channel", retailerRemarks);
                txtReamrks.setText(retailerRemarks);
*/


                mRetailerViewAdapter = new RetailerViewAdapter();


            }

            @Override
            public void onFailure(Call<RetailerViewDetails> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
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

            RetailerViewDetailsMethod();
            Log.e("Retailer_ID", retailerId);
        } else if (type == 9) {
            txtOrder.setText(myDataset.get(position).getName());
        }
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
