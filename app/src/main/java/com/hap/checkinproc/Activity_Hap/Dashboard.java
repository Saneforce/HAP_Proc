package com.hap.checkinproc.Activity_Hap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity.AllowanceActivity;
import com.hap.checkinproc.Activity.TAClaimActivity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    TextView username;
    LinearLayout linMyday, linCheckin, linRequstStaus, linReport, linOnDuty, linApprovals, linTaClaim, linExtShift, linTourPlan, linExit, lin_check_in;
    Integer type;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        username = findViewById(R.id.username);

        Get_MydayPlan();
        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        type = (shared.getInt("CheckCount", 0));
        common_class = new Common_Class(this);

        linMyday = (findViewById(R.id.lin_myday_plan));

        linCheckin = (findViewById(R.id.lin_check_in));
        linRequstStaus = (findViewById(R.id.lin_request_status));
        linReport = (findViewById(R.id.lin_report));
        linOnDuty = (findViewById(R.id.lin_onduty));
        linApprovals = (findViewById(R.id.lin_approvals));
        linTaClaim = (findViewById(R.id.lin_ta_claim));
        linExtShift = (findViewById(R.id.lin_extenden_shift));
        linTourPlan = (findViewById(R.id.lin_tour_plan));
        linExit = (findViewById(R.id.lin_exit));


        linMyday.setOnClickListener(this);
        linCheckin.setOnClickListener(this);
        linRequstStaus.setOnClickListener(this);
        linReport.setOnClickListener(this);
        linOnDuty.setOnClickListener(this);
        linApprovals.setOnClickListener(this);
        linTaClaim.setOnClickListener(this);
        linExtShift.setOnClickListener(this);
        linTourPlan.setOnClickListener(this);
        linExit.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(Dashboard.this, "There is no back action", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        Intent I;
        switch (view.getId()) {

            case R.id.lin_check_in:
                Intent  i = new Intent(this, Checkin.class);
                startActivity(i);
                break;

            case R.id.lin_request_status:
                startActivity(new Intent(this, Leave_Dashboard.class));
                break;

            case R.id.lin_ta_claim:
                startActivity(new Intent(this, TAClaimActivity.class)); //Travel_Allowance
                break;

            case R.id.lin_report:
                Intent Dashboard=new Intent(this, Dashboard_Two.class);
                Dashboard.putExtra("Mode","RPT");
                startActivity(Dashboard);
                break;

            case R.id.lin_approvals:
                startActivity(new Intent(this, Approvals.class));
                break;
            case R.id.lin_myday_plan:
                startActivity(new Intent(this, Mydayplan_Activity.class));

                //common_class.CommonIntentwithFinish(Mydayplan_Activity.class);
                break;

            case R.id.lin_tour_plan:
                startActivity(new Intent(this, Tp_Month_Select.class));
                break;


            default:
                break;
        }


    }

    private void Get_MydayPlan() {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "check/mydayplan");
        QueryString.put("Sf_code", Shared_Common_Pref.Sf_Code);
        QueryString.put("Date", common_class.GetDate());
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        Log.e("Log_TpQuerySTring", QueryString.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Log.e("GettodayResult", "response Tp_View: " + jsonObject.getJSONArray("Checkdayplan"));
                    JSONArray jsoncc = jsonObject.getJSONArray("Checkdayplan");
                    Log.e("LENGTH", String.valueOf(jsoncc.length()));
                    //Log.e("TB_MyDAy_Plan",String.valueOf(jsoncc.getJSONObject(0).get("remarks")));
                    Log.e("MyDAY_LENGTH", String.valueOf(jsoncc.length()));
                    if (jsoncc.length() > 0) {
                        linCheckin.setVisibility(View.VISIBLE);
                        linMyday.setVisibility(View.GONE);
                    } else {
                        linCheckin.setVisibility(View.GONE);
                        linMyday.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}
