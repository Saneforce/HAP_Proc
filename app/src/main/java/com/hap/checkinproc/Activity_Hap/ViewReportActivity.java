
package com.hap.checkinproc.Activity_Hap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.hap.checkinproc.Model_Class.DateReport;
import com.hap.checkinproc.Model_Class.DateResult;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.DateReportAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewReportActivity extends AppCompatActivity {
    TextView toolHeader, txtProductId, txtProductDate;
    ImageView imgBack;
    EditText toolSearch;
    RecyclerView DateRecyclerView;
    String productId, orderDate;
    DateReportAdapter mDateReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);



        Intent intent = getIntent();
        productId = intent.getStringExtra("ProductID");
        orderDate = intent.getStringExtra("OrderDate");
        Log.e("productID1234567,", productId + "    " + orderDate);

        DateRecyclerView = (RecyclerView) findViewById(R.id.date_recycler);
        DateRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DateRecyclerView.setLayoutManager(layoutManager);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });



        txtProductId = (TextView) findViewById(R.id.txt_product_id);
        txtProductDate = (TextView) findViewById(R.id.txt_order_Date);

        txtProductId.setText(productId);
        txtProductDate.setText(orderDate);

        ViewDateReport();
    }





    public void ViewDateReport() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DateReport> responseBodyCall = apiInterface.dateReport(productId, Shared_Common_Pref.Sf_Code);
        responseBodyCall.enqueue(new Callback<DateReport>() {
            @Override
            public void onResponse(Call<DateReport> call, Response<DateReport> response) {

                DateReport mReportActivities = response.body();

                List<DateResult> mDReportModels = mReportActivities.getData();

                mDateReportAdapter = new DateReportAdapter(ViewReportActivity.this, mDReportModels);

                DateRecyclerView.setAdapter(mDateReportAdapter);
            }

            @Override
            public void onFailure(Call<DateReport> call, Throwable t) {

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