package com.hap.checkinproc.SFA_Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.hap.checkinproc.SFA_Adapter.AdapterPendingOutletsCategory;
import com.hap.checkinproc.SFA_Model_Class.ModelPendingOutletsCategory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingOutletsCategory extends AppCompatActivity {
    ImageView home;
    TextView history;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    ArrayList<ModelPendingOutletsCategory> list = new ArrayList<>();
    AdapterPendingOutletsCategory adapter;
    Context context = this;
    Common_Class common_class;

    public static boolean refresh = false;
    EditText searchViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_outlets_category);

        home = findViewById(R.id.toolbar_home);
        history = findViewById(R.id.view_history);
        recyclerView = findViewById(R.id.recyclerview_PendingOutletsCategory);
        progressBar = findViewById(R.id.progressbar_PendingOutletsCategory);
        searchViews = findViewById(R.id.et_searchView);
        searchViews.setVisibility(View.GONE);
        common_class = new Common_Class(context);
        home.setOnClickListener(v -> {
            common_class.gotoHomeScreen(context, v);
        });
        searchViews.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }
        });

        history.setOnClickListener(v -> {
            startActivity(new Intent(this, ApprovalHistoryActivity.class));
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(this::StartLoading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refresh) {
            StartLoading();
            refresh = false;
        }
    }

    private void StartLoading() {
        list.clear();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        searchViews.setVisibility(View.GONE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getPendingOutletsCategory("get_pending_outlets_category");
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
                                String SF_Code = array.getJSONObject(i).getString("SF_Code");
                                String Sf_Name = array.getJSONObject(i).getString("Sf_Name");
                                String Stockist_Code = array.getJSONObject(i).getString("Stockist_Code");
                                String Stockist_Name = array.getJSONObject(i).getString("Stockist_Name");
                                String ListedDrCount = array.getJSONObject(i).getString("ListedDrCount");
                                String sf_Designation_Short_Name = array.getJSONObject(i).getString("sf_Designation_Short_Name");
                                String stockist_erpCode=array.getJSONObject(i).getString("ERP_Code");
                                list.add(new ModelPendingOutletsCategory(SF_Code, Sf_Name, Stockist_Code, Stockist_Name, ListedDrCount, sf_Designation_Short_Name,stockist_erpCode));
                            }
                            runOnUiThread(() -> {
                                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                adapter = new AdapterPendingOutletsCategory(list, context);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                searchViews.setVisibility(View.VISIBLE);
                            });
                        } else {
                            Toast.makeText(context, "List is empty", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(context, "Error: Response Failed", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}