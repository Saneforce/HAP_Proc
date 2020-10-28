package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Approval;
import com.hap.checkinproc.Model_Class.Location;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Leave_Approval extends AppCompatActivity {


    String Scode;
    String Dcode;
    String Rf_code;

    List<Approval>approvalList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave__approval);

        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Scode = (shared.getString("Sfcode", "null"));
        Dcode=(shared.getString("Divcode","null"));
        Rf_code = Scode;

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        System.out.println("Dcode"+Dcode+"Scode"+Scode);

        Call<List<Approval>> approvals = apiInterface.approval("vwLeave",Dcode,Scode,Rf_code,"24");
        Log.e("approval", String.valueOf(Rf_code));
        approvals.enqueue(new Callback<List<Approval>>() {
            @Override
            public void onResponse(Call<List<Approval>> call, Response<List<Approval>> response) {
               approvalList=response.body();
                Log.e("approv", response.body().toString());
                Log.e("approvals", approvalList.toString());
            }

            @Override
            public void onFailure(Call<List<Approval>> call, Throwable t) {

            }
        });




    }


}