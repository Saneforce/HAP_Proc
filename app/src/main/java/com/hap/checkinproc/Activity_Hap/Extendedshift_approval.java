package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Extended;
import com.hap.checkinproc.Model_Class.Missed;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.RecyclerViewExtended;
import com.hap.checkinproc.adapters.RecyclerViewMissedpun;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Extendedshift_approval extends AppCompatActivity {

    String Scode;
    String Dcode;
    String Rf_code;

    List<Extended> extendedList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extendedshift_approval);

        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Scode = (shared.getString("Sfcode", "null"));
        Dcode=(shared.getString("Divcode","null"));
        Rf_code = Scode;

        RecyclerView recyclerView = findViewById(R.id.recycler_view5);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Call<List<Extended>>  extending  = apiInterface.extended("vwExtended",Dcode,Scode,Rf_code,"24");
        extending.enqueue(new Callback<List<Extended>>() {
            @Override
            public void onResponse(Call<List<Extended>> call, Response<List<Extended>> response) {
                extendedList=response.body();

                RecyclerViewExtended adapter = new RecyclerViewExtended(extendedList,Extendedshift_approval.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Extended>> call, Throwable t) {

            }
        });
    }
}