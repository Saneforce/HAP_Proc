package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Approval;
import com.hap.checkinproc.Model_Class.Permission;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.RecyclerViewLeaveApproval;
import com.hap.checkinproc.adapters.RecyclerViewPermission;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Permision_approval extends AppCompatActivity {

    String Scode;
    String Dcode;
    String Rf_code;

    List<Permission> permissionList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permision_approval);

        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Scode = (shared.getString("Sfcode", "null"));
        Dcode=(shared.getString("Divcode","null"));
        Rf_code = Scode;

        RecyclerView recyclerView = findViewById(R.id.recycler_view3);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Call<List<Permission>>permissions  = apiInterface.permission("ViewPermission",Dcode,Scode,Rf_code,"24");
        permissions.enqueue(new Callback<List<Permission>>() {
            @Override
            public void onResponse(Call<List<Permission>> call, Response<List<Permission>> response) {

                permissionList=response.body();

                RecyclerViewPermission adapter = new RecyclerViewPermission(permissionList, Permision_approval.this);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<Permission>> call, Throwable t) {

                System.out.println("on failure");
            }
        });

    }
}