package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Onduty;
import com.hap.checkinproc.Model_Class.Permission;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.RecyclerViewOnduty;
import com.hap.checkinproc.adapters.RecyclerViewPermission;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Onduty_approval extends AppCompatActivity {


    String Scode;
    String Dcode;
    String Rf_code;

    List<Onduty> onduties ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onduty_approval);


        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Scode = (shared.getString("Sfcode", "null"));
        Dcode=(shared.getString("Divcode","null"));
        Rf_code = Scode;
        RecyclerView recyclerView = findViewById(R.id.recycler_view4);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Call<List<Onduty>> ondutys  = apiInterface.onduty("vwOnduty",Dcode,Scode,Rf_code,"24");
        ondutys.enqueue(new Callback<List<Onduty>>() {
            @Override
            public void onResponse(Call<List<Onduty>> call, Response<List<Onduty>> response) {
               onduties = response.body();
                RecyclerViewOnduty adapter = new RecyclerViewOnduty(onduties,Onduty_approval.this);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<Onduty>> call, Throwable t) {

                System.out.println("on failure");

            }
        });



    }
}