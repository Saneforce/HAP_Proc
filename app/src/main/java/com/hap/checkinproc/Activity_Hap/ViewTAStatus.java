package com.hap.checkinproc.Activity_Hap;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.ViewTAStatusAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTAStatus extends AppCompatActivity {

    RecyclerView mTaStatusList;
    ViewTAStatusAdapter viewTAStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_t_a_status);
        mTaStatusList = findViewById(R.id.ta_list);
        mTaStatusList.setLayoutManager(new LinearLayoutManager(this));

        TaListResponse();
    }


    public void TaListResponse() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> mTAStatus = apiInterface.getTaViewStatus(Shared_Common_Pref.Sf_Code);

        Log.v("TA_STATUS_REQUEST",mTAStatus.request().toString());
        mTAStatus.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonTa = response.body();
                viewTAStatusAdapter = new ViewTAStatusAdapter(ViewTAStatus.this,jsonTa);
                mTaStatusList.setAdapter(viewTAStatusAdapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }
}