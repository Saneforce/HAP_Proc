package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.ViewReport;
import com.hap.checkinproc.Model_Class.ReportDataList;
import com.hap.checkinproc.Model_Class.ReportModel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.ReportViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    TextView toolHeader, txtTotalValue, txtProductDate;
    ImageView imgBack;
    Button fromBtn, toBtn;
    EditText toolSearch;
    String fromDateString, dateTime, toDateString, SF_CODE;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ReportViewAdapter mReportViewAdapter;
    RecyclerView mReportList;
    ArrayList<Integer> mArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
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

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });



        mArrayList = new ArrayList<>();
        txtTotalValue = (TextView) findViewById(R.id.total_value);
        @SuppressLint("WrongConstant")
        SharedPreferences sh
                = getSharedPreferences("MyPrefs",
                MODE_APPEND);
        SF_CODE = sh.getString("Sf_Code", "");

        Log.e("SF_CODE", SF_CODE);
        fromBtn = (Button) findViewById(R.id.from_picker);
        toBtn = (Button) findViewById(R.id.to_picker);

        txtTotalValue.setText("0");
        DateFormat df = new SimpleDateFormat("yyyy-MM-d");
        Calendar calobj = Calendar.getInstance();
        dateTime = df.format(calobj.getTime());
        System.out.println("Date_and_Time" + dateTime);

        fromBtn.setText("" + dateTime);
        toBtn.setText("" + dateTime);
        fromDateString = dateTime;
        toDateString = dateTime;

        ViewDateReport();

        fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                fromBtn.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                ViewDateReport();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                toDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                toBtn.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                ViewDateReport();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        mReportList = (RecyclerView) findViewById(R.id.report_list);
        mReportList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mReportList.setLayoutManager(layoutManager);
    }



    public void ViewDateReport() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Log.e("FROMDATA", "" + fromDateString);
        Log.e("TODATA", "" + toDateString);
        Call<ReportDataList> responseBodyCall = apiInterface.reportValues(Shared_Common_Pref.Sf_Code, fromDateString, toDateString);
        responseBodyCall.enqueue(new Callback<ReportDataList>() {
            @Override
            public void onResponse(Call<ReportDataList> call, Response<ReportDataList> response) {

                ReportDataList mReportActivities = response.body();

                List<ReportModel> mDReportModels = mReportActivities.getData();
                for (int i = 0; i < mDReportModels.size(); i++) {
                    Log.e("data", mDReportModels.get(i).getOrderDate());
                    mArrayList.add(Integer.valueOf(mDReportModels.get(i).getOrderValue()));
                }
                long intSum = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    intSum = mArrayList.stream()
                            .mapToLong(Integer::longValue)
                            .sum();
                }

                txtTotalValue.setText("" + Integer.valueOf(String.valueOf(intSum)));

                mReportViewAdapter = new ReportViewAdapter(ReportActivity.this, mDReportModels, new ViewReport() {
                    @Override
                    public void reportCliick(String productId, String orderDate) {

                        Intent intnet = new Intent(ReportActivity.this, ViewReportActivity.class);

                        intnet.putExtra("ProductID", productId);
                        intnet.putExtra("OrderDate", orderDate);
                        startActivity(intnet);

                        Log.e("ProdutId", productId);
                        Log.e("OrderDate", orderDate);
                    }
                });

                mReportList.setAdapter(mReportViewAdapter);
            }

            @Override
            public void onFailure(Call<ReportDataList> call, Throwable t) {

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

