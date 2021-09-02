package com.hap.checkinproc.SFA_Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.PopAddAdapter;
import com.hap.checkinproc.SFA_Adapter.QPSAdapter;
import com.hap.checkinproc.SFA_Adapter.QPS_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import java.util.ArrayList;
import java.util.List;

public class POPActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvViewStatus;
    Button btnSubmit;
    TextView tvOrder, tvOtherBrand, tvQPS, tvCoolerInfo;

    RecyclerView rvQps;

    QPSAdapter qpsAdapter;
    ArrayList<QPS_Modal> qpsModals = new ArrayList<>();
    Common_Class common_class;

    RecyclerView rvPopAdd;

    PopAddAdapter popAddAdapter;

    TextView tvAdd;

    List<Product_Details_Modal> popAddList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_layout);
        common_class = new Common_Class(this);

        btnSubmit = findViewById(R.id.btnSubmit);
        tvViewStatus = findViewById(R.id.tvPOPViewStatus);

        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvQPS = (TextView) findViewById(R.id.tvQPS);
        tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
        tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
        rvQps = (RecyclerView) findViewById(R.id.rvPOP);
        rvPopAdd = (RecyclerView) findViewById(R.id.rvPOPAdd);
        tvAdd = (TextView) findViewById(R.id.tvAddPOP);

        TextView tvRetailorName = findViewById(R.id.Category_Nametext);
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(this);

        tvRetailorName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));


        tvOrder.setOnClickListener(this);
        tvOtherBrand.setOnClickListener(this);
        tvQPS.setOnClickListener(this);
        tvCoolerInfo.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvViewStatus.setVisibility(View.GONE);
                findViewById(R.id.llPOPStatus).setVisibility(View.GONE);
                findViewById(R.id.llPOPRequestStatus).setVisibility(View.VISIBLE);
                tvAdd.setVisibility(View.GONE);
                btnSubmit.setText("Completed");

            }
        });


        findViewById(R.id.tvPOP).setVisibility(View.GONE);


        qpsModals.add(new QPS_Modal("233", "236763", "Cooker", "30.8.2021", "-1 day", "10.9.2021"));
        qpsModals.add(new QPS_Modal("234", "236745", "Mobile", "25.8.2021", "-5 days", "10.9.2021"));

        qpsModals.add(new QPS_Modal("235", "236789", "Bag", "28.8.2021", "-3 days", "10.9.2021"));
        qpsAdapter = new QPSAdapter(this, qpsModals);
        rvQps.setAdapter(qpsAdapter);


        popAddList.add(new Product_Details_Modal("", "", 0));

        popAddAdapter = new PopAddAdapter(popAddList, R.layout.popup_add_recyclerview,
                this);

        rvPopAdd.setAdapter(popAddAdapter);


        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);
    }

    @Override
    public void onClick(View v) {
        Common_Class common_class = new Common_Class(this);
        switch (v.getId()) {
            case R.id.tvOrder:
                common_class.CommonIntentwithFinish(Order_Category_Select.class);
                break;
            case R.id.tvOtherBrand:
                common_class.CommonIntentwithFinish(OtherBrandActivity.class);
                break;
            case R.id.tvQPS:
                common_class.CommonIntentwithFinish(QPSActivity.class);
                break;
            case R.id.tvCoolerInfo:
                common_class.CommonIntentwithFinish(CoolerInfoActivity.class);
                break;
            case R.id.tvAddPOP:
                popAddList.add(new Product_Details_Modal("", "", 0));
                popAddAdapter.notifyData(popAddList);

                break;
        }
    }
}