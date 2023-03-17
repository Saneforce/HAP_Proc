package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Adapter.PosOrder_History_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class POSViewEntryActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {


    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;

    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static TextView tvStartDate, tvEndDate;
    Type userType;

    List<Product_Details_Modal> OutletReport_View_Modal = new ArrayList<>();
    List<Product_Details_Modal> FilterOrderList = new ArrayList<>();
    Common_Class common_class;

    Gson gson;
    POSEntryViewAdapter mReportViewAdapter;
    RecyclerView posViewRecycler;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;

    public static String TAG = "View_History";
    private DatePickerDialog fromDatePickerDialog;

    public static String stDate = "", endDate = "";
    String date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posview_entry);

        db = new DatabaseHandler(this);
        gson = new Gson();
        sharedCommonPref = new Shared_Common_Pref(POSViewEntryActivity.this);
        common_class = new Common_Class(this);

        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        common_class.getProductDetails(this);

        tvStartDate = findViewById(R.id.tvPosViewStartDate);
        tvEndDate = findViewById(R.id.tvPosViewEndDate);
        posViewRecycler = findViewById(R.id.posViewRecyclerview);

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);

        stDate = Common_Class.GetDatewothouttime();
        endDate = Common_Class.GetDatewothouttime();
        tvStartDate.setText(stDate);
        tvEndDate.setText(endDate);

        common_class.getDb_310Data(Constants.POS_Category_EntryList, POSViewEntryActivity.this);

        posViewRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    void showDatePickerDialog(int val) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(POSViewEntryActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), POSViewEntryActivity.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();

                        Log.v("sdatefd",stDate);
                        common_class.getDb_310Data(Constants.POS_Category_EntryList, POSViewEntryActivity.this);
                    } else
                        common_class.showMsg(POSViewEntryActivity.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, POSViewEntryActivity.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
                        Log.v("sdatefd",endDate);

                        common_class.getDb_310Data(Constants.POS_Category_EntryList, POSViewEntryActivity.this);

                    } else
                        common_class.showMsg(POSViewEntryActivity.this, "Please select valid date");
                }

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPosViewStartDate:
                showDatePickerDialog(1);
                break;
            case R.id.tvPosViewEndDate:
                showDatePickerDialog(2);
                break;
        }
    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {

                    case Constants.POS_Category_EntryList:
                        FilterOrderList.clear();
                        userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
                        }.getType();
                        OutletReport_View_Modal = gson.fromJson(apiDataResponse, userType);
                        if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
                            for (Product_Details_Modal filterlist : OutletReport_View_Modal) {

                                FilterOrderList.add(filterlist);
                            }
                        }

//                        Toast.makeText(this,"list item shown",Toast.LENGTH_SHORT).show();
                        mReportViewAdapter = new POSEntryViewAdapter(POSViewEntryActivity.this, FilterOrderList);
                        posViewRecycler.setAdapter(mReportViewAdapter);

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

