package com.hap.checkinproc.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.ShiftListItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;

public class Checkin extends AppCompatActivity {

    private static String Tag = "HAP_Check-In";
    SharedPreferences sharedPreferences;
    SharedPreferences CheckInDetails;
    public static final String spCheckIn = "CheckInDetail";
    public static final String MyPREFERENCES = "MyPrefs";
    private JsonArray ShiftItems = new JsonArray();
    private RecyclerView recyclerView;
    private ShiftListItem mAdapter;
    String ODFlag, onDutyPlcID, onDutyPlcNm, vstPurpose,Check_Flag;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        Check_Flag="CIN";
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });

        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));

            }
        });
        SharedPreferences CheckInDetails = getSharedPreferences(spCheckIn, MODE_PRIVATE);
        String SFTID = CheckInDetails.getString("Shift_Selected_Id", "");
        intent = getIntent();


        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            ODFlag = bundle.getString("ODFlag");
            onDutyPlcID = bundle.getString("onDutyPlcID");
            onDutyPlcNm = bundle.getString("onDutyPlcNm");
            vstPurpose = bundle.getString("vstPurpose");
            Check_Flag = bundle.getString("Mode");
            Log.e("CHECKIN_FLAG", Check_Flag);
            if (onDutyPlcID == null) {
                SFTID = "0";
                onDutyPlcID = "";
            }
        }


        if (SFTID != "") {
            Intent takePhoto = new Intent(this, ImageCapture.class);
            takePhoto.putExtra("Mode", Check_Flag);
            takePhoto.putExtra("ShiftId", SFTID);
            takePhoto.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
            takePhoto.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", ""));
            takePhoto.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", ""));
            takePhoto.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", ""));
            takePhoto.putExtra("ODFlag", ODFlag);
            takePhoto.putExtra("onDutyPlcID", onDutyPlcID);
            takePhoto.putExtra("onDutyPlcNm", onDutyPlcNm);
            takePhoto.putExtra("vstPurpose", vstPurpose);
            startActivity(takePhoto);
            finish();
        }
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String Scode = (shared.getString("Sfcode", "null"));
        String Dcode = (shared.getString("Divcode", "null"));
        spinnerValue("get/Shift_timing", Dcode, Scode);

    }

    public void SetShitItems() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ShiftListItem(ShiftItems, this, Check_Flag);
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