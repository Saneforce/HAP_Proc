package com.hap.checkinproc.SFA_Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Approve_Outlets_Adapter;
import com.hap.checkinproc.SFA_Model_Class.ModelApproveOutlets;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Created by RAGU on 27/01/2023
public class ApproveOutletsActivity extends AppCompatActivity {
    ImageView home;
    TextView outletsCount;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    String sfCode, stockistCode;

    ArrayList<ModelApproveOutlets> list = new ArrayList<>();
    Approve_Outlets_Adapter adapter;

    Context context = this;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_outlets);

        home = findViewById(R.id.toolbar_home_ApproveOutletsActivity);
        outletsCount = findViewById(R.id.count_ApproveOutletsActivity);
        recyclerView = findViewById(R.id.recyclerview_ApproveOutletsActivity);
        progressBar = findViewById(R.id.progressbar_ApproveOutletsActivity);

        sfCode = getIntent().getStringExtra("sfCode");
        stockistCode = getIntent().getStringExtra("stockistCode");

        common_class = new Common_Class(context);
        home.setOnClickListener(v -> {
            common_class.gotoHomeScreen(context, v);
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(this::StartLoading);
    }

    private void StartLoading() {
        list.clear();
        Map<String, String> params = new HashMap<>();
        params.put("sfCode", sfCode);
        params.put("stockistCode", stockistCode);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getPendingOutlets("gets_pendings_outlets", params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("success")) {
                            JSONArray array = object.getJSONArray("response");
                            for (int i = 0; i < array.length(); i++) {
                                String customerName = array.getJSONObject(i).getString("OutletName");
                                String customerID = array.getJSONObject(i).getString("OutletCode");
                                String customerMobile = array.getJSONObject(i).getString("OutletMobile");
                                String customerAddress = array.getJSONObject(i).getString("OutletAddress");
                                String listedDrCode = array.getJSONObject(i).getString("ListedDrCode");
                                list.add(new ModelApproveOutlets(customerName, customerID, customerMobile, customerAddress, listedDrCode));
                            }
                            String c = "No. of Outlets\n" + list.size();
                            runOnUiThread(() -> {
                                outletsCount.setText(c);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                adapter = new Approve_Outlets_Adapter(list, context);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            });
                        } else {
                            Toast.makeText(context, "List is empty", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Error: Response Failed", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}