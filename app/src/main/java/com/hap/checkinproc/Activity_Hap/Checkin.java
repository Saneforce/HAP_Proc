package com.hap.checkinproc.Activity_Hap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.ShiftListItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Checkin extends AppCompatActivity {

    private JsonArray ShiftItems = new JsonArray();
    private RecyclerView recyclerView;
    private ShiftListItem mAdapter;
    String ODFlag, onDutyPlcID, onDutyPlcNm, vstPurpose;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            ODFlag = extras.getString("ODFlag");
            onDutyPlcID = extras.getString("onDutyPlcID");
            onDutyPlcNm = extras.getString("onDutyPlcNm");
            vstPurpose = extras.getString("vstPurpose");
        }


        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String Scode = (shared.getString("Sfcode", "null"));
        String Dcode = (shared.getString("Divcode", "null"));
        spinnerValue("get/Shift_timing", Dcode, Scode);

    }

    public void SetShitItems() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ShiftListItem(ShiftItems, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void spinnerValue(String a, String dc, String sc) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> shiftCall = apiInterface.shiftTime(a, dc, sc);
        shiftCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.e("ShiftTime", String.valueOf(response.body()));
                ShiftItems = response.body();
                SetShitItems();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }
}