package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Missed;
import com.hap.checkinproc.Model_Class.Onduty;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.RecyclerViewMissedpun;
import com.hap.checkinproc.adapters.RecyclerViewOnduty;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Missedpunch_approval extends AppCompatActivity {

    String Scode;
    String Dcode;
    String Rf_code;

    List<Missed> missedList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missedpunch_approval);

        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Scode = (shared.getString("Sfcode", "null"));
        Dcode=(shared.getString("Divcode","null"));
        Rf_code = Scode;

        RecyclerView recyclerView = findViewById(R.id.recycler_view6);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Call<List<Missed>> misseds  = apiInterface.missed("vwmissedpunch",Dcode,Scode,Rf_code,"24");
        misseds.enqueue(new Callback<List<Missed>>() {
            @Override
            public void onResponse(Call<List<Missed>> call, Response<List<Missed>> response) {
                missedList=response.body();

                RecyclerViewMissedpun adapter = new RecyclerViewMissedpun(missedList,Missedpunch_approval.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Missed>> call, Throwable t) {

            }
        });




    }
}