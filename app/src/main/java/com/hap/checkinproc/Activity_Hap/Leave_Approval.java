package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.Model_Class.Approval;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

public class Leave_Approval extends AppCompatActivity   {


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

        /*ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        System.out.println("Dcode"+Dcode+"Scode"+Scode);

        Call<List<Approval>> approvals = apiInterface.approval("vwLeave","3","MGR4762","MGR4762","24");
        Log.e("approval", String.valueOf(Rf_code));
        approvals.enqueue(new Callback<List<Approval>>() {
            @Override
            public void onResponse(Call<List<Approval>> call, Response<List<Approval>> response) {
               approvalList=response.body();
                Log.e("getApplieddate", String.valueOf(approvalList.get(0).getApplieddate()));
                Log.e("approvals", String.valueOf(response.code()));

            }

            @Override
            public void onFailure(Call<List<Approval>> call, Throwable t) {
                System.out.println("on failure");


            }
        });*/


    }



}