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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;
import com.hap.checkinproc.Activity_Hap.ReportActivity;
import com.hap.checkinproc.Activity_Hap.TAHistory;
import com.hap.checkinproc.Activity_Hap.ViewReportActivity;
import com.hap.checkinproc.Activity_Hap.ViewTAStatus;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.Interface.ViewReport;
import com.hap.checkinproc.Model_Class.DateReport;
import com.hap.checkinproc.Model_Class.DateResult;
import com.hap.checkinproc.Model_Class.POSDataList;
import com.hap.checkinproc.Model_Class.ReportDataList;
import com.hap.checkinproc.Model_Class.ReportModel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Adapter.PosOrder_History_Adapter;
import com.hap.checkinproc.SFA_Adapter.Projection_History_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;
import com.hap.checkinproc.SFA_Model_Class.InshopModel;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.adapters.ReportViewAdapter;
import com.hap.checkinproc.adapters.TAApprovHistoryAdapter;
import com.hap.checkinproc.adapters.ViewTAStatusAdapter;
import com.hap.checkinproc.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class POSViewEntryActivity extends AppCompatActivity{

    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    public static TextView tvStartDate, tvEndDate;
    POSEntryViewAdapter mReportViewAdapter;
    RecyclerView posViewRecycler;
    public static String stDate = "", endDate = "",SF_code="";
    String fromDateString, dateTime, toDateString, SF_CODE;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ArrayList<Float> model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posview_entry);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        Log.v("sfcodeeee",SF_code);

        model = new ArrayList<>();

        tvStartDate = findViewById(R.id.tvPosViewStartDate);
        tvEndDate = findViewById(R.id.tvPosViewEndDate);
        posViewRecycler = findViewById(R.id.posViewRecyclerview);

        DateFormat df = new SimpleDateFormat("yyyy-MM-d");
        Calendar calobj = Calendar.getInstance();
        dateTime = df.format(calobj.getTime());
        System.out.println("Date_and_Time" + dateTime);

        tvStartDate.setText(dateTime);
        tvEndDate.setText(dateTime);

        fromDateString = dateTime;
        toDateString = dateTime;

        ViewDateReport();

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(POSViewEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tvStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                                Log.e("DATE_FROM", tvStartDate.getText().toString());

                                ViewDateReport();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(POSViewEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                toDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tvEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                Log.e("DATE_FROM", tvEndDate.getText().toString());
                                ViewDateReport();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        posViewRecycler.setLayoutManager(layoutManager);
    }

    public void ViewDateReport() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<POSDataList> call = apiInterface.getpos1(SF_code, fromDateString, toDateString);

        call.enqueue(new Callback<POSDataList>() {
            @Override
            public void onResponse(Call<POSDataList> call, Response<POSDataList> response) {


                POSDataList mReportActivities = response.body();
                List<Product_Details_Modal> mDReportModels = mReportActivities.getData();

                Log.e("MdReportModels", String.valueOf(mDReportModels.size()));

                for (int i = 0; i < mDReportModels.size(); i++) {
                    model.add(Float.valueOf(mDReportModels.get(i).getTotal()));
                }

                mReportViewAdapter = new POSEntryViewAdapter(POSViewEntryActivity.this,mDReportModels);
                posViewRecycler.setAdapter(mReportViewAdapter);
            }

            @Override
            public void onFailure(Call<POSDataList> call, Throwable t) {
                Log.e("MdReportModelsErr", t.toString());
            }
        });

    }

}

