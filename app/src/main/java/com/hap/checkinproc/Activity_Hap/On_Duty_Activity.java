package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.Leave_Type;
import com.hap.checkinproc.Model_Class.RemainingLeave;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.LeaveRemaining;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class On_Duty_Activity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    LinearLayout haplocationtext, purposeofvisittext, haplocationbutton, otherlocationbutton, submitbutton, closebutton, exitclose, ondutylocations;
    EditText purposeofvisitedittext, ondutyedittext;
    int flag;
    Common_Model Model_Pojo;
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    TextView selecthaplocationss;
    CustomListViewDialog customDialog;
    String hapLocid;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on__duty_);
        gson = new Gson();
        haplocationtext = findViewById(R.id.haplocationtext);
        purposeofvisittext = findViewById(R.id.purposeofvisittext);
        otherlocationbutton = findViewById(R.id.otherlocationbutton);
        haplocationbutton = findViewById(R.id.haplocationbutton);
        ondutylocations = findViewById(R.id.ondutylocations);
        submitbutton = findViewById(R.id.submitbutton);
        selecthaplocationss = findViewById(R.id.selecthaplocationss);
        purposeofvisitedittext = findViewById(R.id.purposeofvisitedittext);
        ondutyedittext = findViewById(R.id.ondutyedittext);
        closebutton = findViewById(R.id.closebutton);
        exitclose = findViewById(R.id.exitclose);
        otherlocationbutton.setOnClickListener(this);
        haplocationbutton.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        closebutton.setOnClickListener(this);
        exitclose.setOnClickListener(this);
        selecthaplocationss.setOnClickListener(this);
        GetfieldforceHq();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.otherlocationbutton:
                flag = 1;
                ondutylocations.setVisibility(View.VISIBLE);
                purposeofvisittext.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.GONE);
                submitbutton.setVisibility(View.VISIBLE);
                otherlocationbutton.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.GONE);
                break;
            case R.id.haplocationbutton:
                flag = 0;
                haplocationtext.setVisibility(View.VISIBLE);
                purposeofvisittext.setVisibility(View.VISIBLE);
                submitbutton.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.GONE);
                ondutylocations.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.GONE);
                otherlocationbutton.setVisibility(View.GONE);
                break;
            case R.id.submitbutton:
                if (vali()) {
                    Intent intent = new Intent(this, Check_in.class);
                    Bundle extras = new Bundle();
                    extras.putString("ODFlag", String.valueOf(flag));
                    extras.putString("onDutyPlcID", hapLocid);
                    extras.putString("onDutyPlcNm", selecthaplocationss.getText().toString());
                    extras.putString("vstPurpose", purposeofvisitedittext.getText().toString());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
                break;

            case R.id.closebutton:
                haplocationtext.setVisibility(View.GONE);
                purposeofvisittext.setVisibility(View.GONE);
                submitbutton.setVisibility(View.GONE);
                closebutton.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.VISIBLE);
                otherlocationbutton.setVisibility(View.VISIBLE);
                ondutylocations.setVisibility(View.GONE);
                break;
            case R.id.exitclose:
                startActivity(new Intent(this, Dashboard.class));
                break;
            case R.id.selecthaplocationss:
                customDialog = new CustomListViewDialog(On_Duty_Activity.this, getfieldforcehqlist, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;
        }
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        selecthaplocationss.setText(myDataset.get(position).getName());
        hapLocid = String.valueOf(myDataset.get(position).getId());
    }

    public void GetfieldforceHq() {
        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.getFieldForce_HQ(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e("ROUTE_MASTER_Object", String.valueOf(response.body().toString()));
                GetJsonData(new Gson().toJson(response.body()), "0");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    private void GetJsonData(String jsonResponse, String type) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("ODFlag");

                if (flag.equals("1")) {
                    Model_Pojo = new Common_Model(id, name, flag);
                    getfieldforcehqlist.add(Model_Pojo);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean vali() {

        if (flag == 1 && (ondutyedittext.getText().toString() == null || ondutyedittext.getText().toString().isEmpty() || ondutyedittext.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Enter the ON-Duty Location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (flag == 0 && (selecthaplocationss.getText().toString() == null || selecthaplocationss.getText().toString().isEmpty() || selecthaplocationss.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The HAP Location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (purposeofvisitedittext.getText().toString() == null || purposeofvisitedittext.getText().toString().isEmpty() || purposeofvisitedittext.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter the Purpose of visit", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

}