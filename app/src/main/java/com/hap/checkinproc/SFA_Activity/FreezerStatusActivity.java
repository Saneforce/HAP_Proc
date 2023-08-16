package com.hap.checkinproc.SFA_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.AdapterFreezerStatusList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreezerStatusActivity extends AppCompatActivity {
    LottieAnimationView somethingWentWrong, progressbar, noRecordsFound;
    LinearLayout error_layout;
    RecyclerView recyclerView;
    TextView error_info;
    ImageView toolbar_home;

    String outletCode, distCode;

    Context context = this;
    AdapterFreezerStatusList adapterFreezerStatusList;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freezer_status);

        somethingWentWrong = findViewById(R.id.somethingWentWrong);
        progressbar = findViewById(R.id.progressbar);
        noRecordsFound = findViewById(R.id.noRecordsFound);
        error_layout = findViewById(R.id.error_layout);
        recyclerView = findViewById(R.id.recyclerView);
        error_info = findViewById(R.id.error_info);
        toolbar_home = findViewById(R.id.toolbar_home);

        outletCode = getIntent().getStringExtra("outletCode");
        distCode = getIntent().getStringExtra("distCode");

        common_class = new Common_Class(this);
        common_class.gotoHomeScreen(context, toolbar_home);
        
        getFreezerRequests();
    }

    private void getFreezerRequests() {
        progressbar.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
        noRecordsFound.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_pending_freezers");
        params.put("outletCode", outletCode);
        params.put("distCode", distCode);
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray array = jsonObject.getJSONArray("response");
                            if (array.length() > 0) {
                                resultOK();
                                adapterFreezerStatusList = new AdapterFreezerStatusList(context, array);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                recyclerView.setAdapter(adapterFreezerStatusList);
                            } else {
                                setNoRecordsFound();
                            }
                        } else {
                            resultError(jsonObject.getString("msg"));
                        }
                    } catch (Exception e) {
                        resultError(e.getMessage());
                    }
                } else {
                    resultError("Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                resultError(t.getMessage());
            }
        });
    }

    private void resultOK() {
        progressbar.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
        noRecordsFound.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void resultError(String msg) {
        error_info.setText(msg);
        error_layout.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noRecordsFound.setVisibility(View.GONE);
    }

    private void setNoRecordsFound() {
        error_layout.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noRecordsFound.setVisibility(View.VISIBLE);
    }
}