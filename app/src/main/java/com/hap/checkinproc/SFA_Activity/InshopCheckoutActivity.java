package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InshopCheckoutActivity extends AppCompatActivity {

    TextView checkoutTunTime, checkinTime, retailerName,tvDate,tvCheckout;
    final Handler handler = new Handler();
    Button checkout;
    String checkoutTime;

    ApiInterface apiInterface;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;

    String SF_code = "", div = "", State_Code = "", date="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_checkout);

        checkoutTunTime = findViewById(R.id.tvCheckoutRunTime);
        checkinTime = findViewById(R.id.tvCheckinTime);
        retailerName = findViewById(R.id.ischeckoutRetName);
        tvDate = findViewById(R.id.iscoutDate);
        checkout = findViewById(R.id.btnInshopCheckout);
        tvCheckout = findViewById(R.id.tvCheckoutTime);

        retailerName.setText(InshopCheckinActivity.getName());
        checkinTime.setText(InshopCheckinActivity.getCheckinTime());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.format(today);
        tvDate.setText(date);

        handler.postDelayed(new Runnable() {
            public void run() {
                checkoutTunTime.setText(Common_Class.GetRunTime());
                handler.postDelayed(this, 1000);
            }
        }, 1000);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutTime = checkoutTunTime.getText().toString().trim();
                Log.v("checkoutTime",checkoutTime);

                checkoutData();

//                Toast.makeText(InshopCheckoutActivity.this,"Checkout Successfully",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkoutData() {

        JSONObject jObj = new JSONObject();
        try {

            jObj.put("SFCode",SF_code);
            jObj.put("inshopCheckoutTime",checkoutTime);
            jObj.put("inshopCheckoutDate",date);
            jObj.put("DivCode",div);
            jObj.put("Statecode",State_Code);
            jObj.put("inshopRetailerName", retailerName.getText().toString());
            jObj.put("c_flag",0);

            Log.d("hjj","ghkj"+jObj.toString());



            apiInterface.JsonSave("save/inshopCheckout", jObj.toString()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    Toast.makeText(InshopCheckoutActivity.this,"Inshop Checkout Successfully",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}