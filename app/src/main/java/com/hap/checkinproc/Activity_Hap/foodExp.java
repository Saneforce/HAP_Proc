package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.HAPListItem;
import com.hap.checkinproc.adapters.adFoodexp;

import org.json.JSONArray;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class foodExp extends AppCompatActivity {
    public static final String UserDetail = "MyPrefs";
    SharedPreferences UserDetails;
    RecyclerView mRecyclerView;
    TextView txtempid,txtempName,txtHQ,txtTot;
    ImageView btMyQR,btHome;
    adFoodexp lsExp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_exp);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

        txtempid=findViewById(R.id.empId);
        txtempName=findViewById(R.id.empName);
        txtHQ=findViewById(R.id.empHQ);
        txtTot=findViewById(R.id.TotAmt);

        btMyQR=findViewById(R.id.myQR);
        btHome=findViewById(R.id.toolbar_home);


        txtempid.setText(UserDetails.getString("EmpId",""));
        txtempName.setText(UserDetails.getString("SfName",""));
        txtHQ.setText(UserDetails.getString("SFHQ",""));
        mRecyclerView = findViewById(R.id.foodExpList);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        btMyQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(foodExp.this,CateenToken.class);
                startActivity(intent);
                finish();
            }
        });

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> rptCall = apiInterface.getDataArrayListA("get/foodexp",
                UserDetails.getString("Divcode", ""),
                UserDetails.getString("Sfcode", ""), "", "", null);
        rptCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray res = response.body();
                Log.d("Res Data:",res.toString());
                if (res.size() < 1) {
                    Toast.makeText(getApplicationContext(), "No Records Today", Toast.LENGTH_LONG).show();
                    return;
                }

                lsExp = new adFoodexp(res, foodExp.this);
                mRecyclerView.setAdapter(lsExp);
                Double amt=0.0;
                for(int il=0;il<res.size();il++){
                    JsonObject item=res.get(il).getAsJsonObject();
                    amt+=item.get("amount").getAsDouble();
                }

                txtTot.setText("Rs. "+ new DecimalFormat("##0.00").format(amt));

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }
}