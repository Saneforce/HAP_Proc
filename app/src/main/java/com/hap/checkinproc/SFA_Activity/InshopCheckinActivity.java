package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InshopCheckinActivity extends AppCompatActivity {

    TextView checkinRunTime, checkedinTime, search, retailerName, tvDate;
    Button checkin;
    CardView searchLay;
    LinearLayout checkinLay, checkedinLay;
    final Handler handler = new Handler();
    String n="", m="";

    ApiInterface apiInterface;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;

    String SF_code = "", div = "", State_Code = "", date="";

    private static String name,checkinTime;
    public static String getName() {
        return name;
    }
    public static String getCheckinTime() {
        return checkinTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_checkin);

        checkinRunTime = findViewById(R.id.tvCheckInRunTime);
        checkin = findViewById(R.id.btnInshopCheckin);
        checkinLay = findViewById(R.id.inshopCheckinLay);
        checkedinLay = findViewById(R.id.inshopCheckedInLay);
        checkedinTime = findViewById(R.id.tvCheckedinTime);
        search = findViewById(R.id.tvInshopSearchRet);
        searchLay = findViewById(R.id.isSearchView);
        retailerName = findViewById(R.id.inshopRetName);
        tvDate = findViewById(R.id.ischeckinDate);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (getIntent().hasExtra("idData")) {
            n=getIntent().getStringExtra("idData");
            m=getIntent().getStringExtra("idData");
        }

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.format(today);
        tvDate.setText(date);


        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");

        search.setText(n);


        handler.postDelayed(new Runnable() {
            public void run() {
                checkinRunTime.setText(Common_Class.GetRunTime());
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkedinTime.setText(Common_Class.GetRunTime());
                checkedinLay.setVisibility(View.VISIBLE);
                checkinLay.setVisibility(View.GONE);

                retailerName.setText(m);

                name = retailerName.getText().toString().trim();
                checkinTime = checkedinTime.getText().toString().trim();
                
                checkinData();
                

            }
        });

        searchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InshopCheckinActivity.this, InshopRetailerActivity.class));

            }
        });
    }

    private void checkinData() {
        JSONObject jObj = new JSONObject();

        try {

            jObj.put("SFCode",SF_code);
            jObj.put("inshopCheckinTime",checkinTime);
            jObj.put("inshopCheckinDate",date);
            jObj.put("DivCode",div);
            jObj.put("Statecode",State_Code);
            jObj.put("inshopRetailerName", retailerName.getText().toString());
            jObj.put("c_flag",1);

            Log.d("isCheckinhjj","ghkj"+jObj.toString());

            if(jObj.getString("c_flag").equals("1")){
                checkinLay.setVisibility(View.GONE);
                checkedinLay.setVisibility(View.VISIBLE);
            }
            else{
                checkedinLay.setVisibility(View.GONE);
                checkin.setVisibility(View.VISIBLE);
            }

            apiInterface.JsonSave("save/inshopCheckin", jObj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Toast.makeText(InshopCheckinActivity.this,"Inshop CheckedIn Successfully",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(InshopCheckinActivity.this, InshopActivity.class));
        finish();
    }
}