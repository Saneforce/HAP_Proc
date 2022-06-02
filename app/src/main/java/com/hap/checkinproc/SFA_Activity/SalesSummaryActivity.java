package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Sales_Sum_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvStartDate, tvEndDate,calls,order,invoice,tvpop,tvqps,tvotherbrand,tvcoderinfo;
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    RecyclerView recyclerView;

    public static String stDate = "", endDate = "";
    DatePickerDialog fromDatePickerDialog;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_summary_activity);
        recyclerView=findViewById(R.id.recycler_view);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        calls=findViewById(R.id.calls);
        order=findViewById(R.id.tvOrders);
        invoice=findViewById(R.id.tv_invoice);
        tvqps=findViewById(R.id.tv_Qps);
        tvpop=findViewById(R.id.tv_pop);
        tvotherbrand=findViewById(R.id.other_brand);
        tvcoderinfo=findViewById(R.id.tv_coder_info);

        sharedCommonPref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);

        tvStartDate.setText(Common_Class.GetDatewothouttime());
        tvEndDate.setText(Common_Class.GetDatewothouttime());
        getSalSum();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvStartDate:
                selectDate(1);
                break;
            case R.id.tvEndDate:
                selectDate(2);
                break;
        }
    }
    void selectDate(int val) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(SalesSummaryActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), SalesSummaryActivity.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();
                        getSalSum();
                    } else
                        common_class.showMsg(SalesSummaryActivity.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, SalesSummaryActivity.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
                        getSalSum();

                    } else
                        common_class.showMsg(SalesSummaryActivity.this, "Please select valid date");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }
    void getSalSum(){
        if (common_class.isNetworkAvailable(SalesSummaryActivity.this)) {
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("distributorid", sharedCommonPref.getvalue(Constants.Distributor_Id));
                jParam.put("FDT", tvStartDate.getText().toString());
                jParam.put("TDT", tvEndDate.getText().toString());
                Log.v("DATAA:",jParam.toString());
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getDataArrayList("get/disributorwisesales", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {
                            Log.d("Projec_grp_List", response.body().toString());
                            JSONArray routeArr = new JSONArray( response.body().toString());
                            for (int i = 0; i < routeArr.length(); i++) {
                                JSONObject jsonObject1 = routeArr.getJSONObject(i);

                                calls.setText(""+jsonObject1.getString("Calls"));
                                order.setText(""+jsonObject1.getString("Orders"));
                                invoice.setText(""+jsonObject1.getString("Invoices"));
                                tvqps.setText(""+jsonObject1.getString("QPS"));
                                tvpop.setText(""+jsonObject1.getString("POP"));
                                tvotherbrand.setText(""+jsonObject1.getString("Otherbrand"));
                                tvcoderinfo.setText(""+jsonObject1.getString("Cooler"));

                                JSONArray jsonArray =  jsonObject1.getJSONArray("CategorySales");
                                recyclerView.setAdapter(new Sales_Sum_Adapter(jsonArray,SalesSummaryActivity.this));
                                Log.v("DATA1:",jsonArray.toString());
                            }
                        }
                        catch (Exception e)
                        {
                            Log.v("Error:",e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
            }
            catch (Exception e)
            {
                Log.v("Projec_Product_List_ex", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}