package com.hap.checkinproc.SFA_Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.common.DatabaseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GrnListActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {

    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static TextView tvStartDate, tvEndDate;
    TextView outlet_name;

    Common_Class common_class;
    List<OutletReport_View_Modal> OutletReport_View_Modal = new ArrayList<>();
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    Type userType;
    Gson gson;
    Invoice_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;


    public static String TAG = "Invoice_History";
    private DatePickerDialog fromDatePickerDialog;


    //Updateed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_grn_list);
            db = new DatabaseHandler(this);
            gson = new Gson();
            sharedCommonPref = new Shared_Common_Pref(GrnListActivity.this);
            common_class = new Common_Class(this);

            CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
            common_class.getProductDetails(this);

            outlet_name = findViewById(R.id.outlet_name);
            outlet_name.setText("Hi! " + sharedCommonPref.getvalue(Constants.Distributor_name));

            tvStartDate = findViewById(R.id.tvStartDate);
            tvEndDate = findViewById(R.id.tvEndDate);


            tvStartDate.setOnClickListener(this);
            tvEndDate.setOnClickListener(this);


            invoicerecyclerview = (RecyclerView) findViewById(R.id.invoicerecyclerview);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            tvStartDate.setText(Common_Class.GetDatewothouttime());
            tvEndDate.setText(Common_Class.GetDatewothouttime());

            common_class.getDataFromApi(Constants.GetGrn_List, this, false);


        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }

    }

    void showDatePickerDialog(int pos, TextView tv) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(GrnListActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String date = ("" + year + "-" + month + "-" + dayOfMonth);

                if (common_class.checkDates(pos == 0 ? date : tvStartDate.getText().toString(), pos == 1 ? date : tvEndDate.getText().toString(), GrnListActivity.this)) {
                    tv.setText(date);
                    if (pos == 0)
                        tvStartDate.setText(date);
                    else
                        tvEndDate.setText(date);
                    common_class.getDataFromApi(Constants.GetGrn_List, GrnListActivity.this, false);
                } else {
                    common_class.showMsg(GrnListActivity.this, "Please select valid date");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                showDatePickerDialog(0, tvStartDate);
                break;
            case R.id.tvEndDate:
                showDatePickerDialog(1, tvEndDate);
                break;

        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {

                    case Constants.GetGrn_List:
                        FilterOrderList.clear();
                        userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                        }.getType();
                        OutletReport_View_Modal = gson.fromJson(apiDataResponse, userType);
                        if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
                            for (OutletReport_View_Modal filterlist : OutletReport_View_Modal) {
                                //if (filterlist.getOutletCode().equals(Shared_Common_Pref.OutletCode)) {
                                FilterOrderList.add(filterlist);
                                // }
                            }
                        }


                        mReportViewAdapter = new Invoice_History_Adapter(GrnListActivity.this, FilterOrderList, new AdapterOnClick() {
                            @Override
                            public void onIntentClick(int position) {
                                if (FilterOrderList.get(position).getStatus().equalsIgnoreCase("INVOICE")) {
                                    Log.e("TRANS_SLNO", FilterOrderList.get(position).getTransSlNo());
                                    Shared_Common_Pref.TransSlNo = FilterOrderList.get(position).getTransSlNo();
                                    Shared_Common_Pref.Invoicetoorder = "1";
                                    Intent intent = new Intent(getBaseContext(), Grn_Category_Select.class);
                                  //  sharedCommonPref.save(Constants.FLAG, FilterOrderList.get(position).getStatus());
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
                                } else {
                                    Log.e("TRANS_SLNO", FilterOrderList.get(position).getTransSlNo());
                                    Shared_Common_Pref.TransSlNo = FilterOrderList.get(position).getTransSlNo();
                                    Shared_Common_Pref.Invoicetoorder = "1";
                                    Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                                 //   sharedCommonPref.save(Constants.FLAG, FilterOrderList.get(position).getIndent());
                                    sharedCommonPref.save(Constants.FLAG, "GRN");
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
            return true;
        }
        return false;
    }

}