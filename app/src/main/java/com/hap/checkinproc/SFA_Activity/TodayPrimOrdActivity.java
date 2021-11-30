package com.hap.checkinproc.SFA_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.PrimaryOrder_History_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodayPrimOrdActivity extends AppCompatActivity implements Master_Interface, View.OnClickListener, UpdateResponseUI {

    TextView outlet_name;
    Common_Class common_class;
    List<OutletReport_View_Modal> OutletReport_View_Modal = new ArrayList<>();
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    Type userType;
    Gson gson;
    PrimaryOrder_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;
    public static TodayPrimOrdActivity mTdPriAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_today_primorder_history);
            mTdPriAct = this;
            gson = new Gson();
            sharedCommonPref = new Shared_Common_Pref(TodayPrimOrdActivity.this);
            common_class = new Common_Class(this);

            outlet_name = findViewById(R.id.outlet_name);
            outlet_name.setText(sharedCommonPref.getvalue(Constants.Distributor_name, ""));

            invoicerecyclerview = (RecyclerView) findViewById(R.id.invoicerecyclerview);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


            common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, this, false);

        } catch (Exception e) {

        }

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {

                    case Constants.GetTodayPrimaryOrder_List:
                        userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                        }.getType();
                        OutletReport_View_Modal = gson.fromJson(apiDataResponse, userType);
                        if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
                            for (OutletReport_View_Modal filterlist : OutletReport_View_Modal) {
                                FilterOrderList.add(filterlist);

                            }
                        }


                        mReportViewAdapter = new PrimaryOrder_History_Adapter(TodayPrimOrdActivity.this, FilterOrderList, apiDataResponse, new AdapterOnClick() {
                            @Override
                            public void onIntentClick(int position) {
                                Log.e("TRANS_SLNO", FilterOrderList.get(position).getTransSlNo());
                                Shared_Common_Pref.TransSlNo = FilterOrderList.get(position).getTransSlNo();

                                Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                                sharedCommonPref.save(Constants.FLAG, "Primary Order");
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


                }

            }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.in, R.anim.out);

            return true;
        }
        return false;
    }

    public void updateData(String orderNo) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date d1 = sdf.parse(Common_Class.GetTime());
            Date d2 = sdf.parse(sharedCommonPref.getvalue(Constants.CUTOFF_TIME));
            long elapsed = d2.getTime() - d1.getTime();
            if (elapsed >= 0) {
                Intent intent = new Intent(this, PrimaryOrderActivity.class);
                intent.putExtra(Constants.ORDER_ID, orderNo);
                Shared_Common_Pref.TransSlNo = orderNo;
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
            } else {

                common_class.showMsg(this, "Time UP...");
            }
        } catch (Exception e) {

        }

    }
}