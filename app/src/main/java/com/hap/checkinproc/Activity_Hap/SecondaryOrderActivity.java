package com.hap.checkinproc.Activity_Hap;

import android.content.DialogInterface;
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
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.Leave_Type;
import com.hap.checkinproc.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondaryOrderActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    LinearLayout OrderType, RetailerType, linearCategory;
    TextView toolHeader, txtOrder, txtRetailer, txtAddRetailer;
    CustomListViewDialog customDialog;

    ImageView imgBack;
    EditText toolSearch;
    Button mSubmit;
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelleaveType = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    private ArrayList<String> temaplateList;

    Gson gson;
    List<Leave_Type> leavetypelist;
    Type userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_order);
        initilaize();
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
                RetailerType();
            }
        });


        linearCategory = findViewById(R.id.prm_linear_orders);
        linearCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondaryOrderActivity.this, OrderCategoryActivity.class));
            }
        });
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



    public void RetailerType() {
        modelleaveType.clear();
        String commonLeaveType = "{\"tableName\":\"vwLeaveType\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                userType = new TypeToken<ArrayList<Leave_Type>>() {
                }.getType();
                leavetypelist = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < leavetypelist.size(); i++) {
                    String id = String.valueOf(leavetypelist.get(i).getId());
                    String name = leavetypelist.get(i).getName();
                    String address = leavetypelist.get(i).getLeaveSName();
                    mCommon_model_spinner = new Common_Model(id, name, "flag",address);
                    Log.e("LeaveType_Request", id);
                    Log.e("LeaveType_Request", name);
                    modelleaveType.add(mCommon_model_spinner);
                }
                customDialog = new CustomListViewDialog(SecondaryOrderActivity.this, modelleaveType, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
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
