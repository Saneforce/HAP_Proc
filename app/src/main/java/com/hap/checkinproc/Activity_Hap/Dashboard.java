package com.hap.checkinproc.Activity_Hap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity.AllowanceActivity;
import com.hap.checkinproc.Activity.TAClaimActivity;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private static String Tag = "HAP_Check-In";
    SharedPreferences sharedPreferences;
    SharedPreferences UserDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String MyPREFERENCES = "MyPrefs";

    TextView username;
    TextView lblUserName, lblEmail;
    LinearLayout linMyday, linCheckin, linRequstStaus, linReport, linOnDuty, linTaClaim, linExtShift, linTourPlan, linExit, lin_check_in, linHolidayWorking;
    Integer type;
    Common_Class common_class;
    TextView approvalcount;
    RelativeLayout linApprovals;
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        username = findViewById(R.id.username);
        lblUserName = (TextView) findViewById(R.id.lblUserName);
        lblEmail = (TextView) findViewById(R.id.lblEmail);
        Get_MydayPlan(1, "check/mydayplan");
        sharedPreferences = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        type = (shared.getInt("CheckCount", 0));
        common_class = new Common_Class(this);

        String eMail = UserDetails.getString("email", "");
        String sSFName = UserDetails.getString("SfName", "");
        String sSFType = UserDetails.getString("Sf_Type", "");
        lblUserName.setText(sSFName);
        lblEmail.setText(eMail);

        linMyday = (findViewById(R.id.lin_myday_plan)); linMyday.setVisibility(View.GONE); if(sSFType=="1") linMyday.setVisibility(View.VISIBLE);


        linCheckin = (findViewById(R.id.lin_check_in));
        linRequstStaus = (findViewById(R.id.lin_request_status));
        linReport = (findViewById(R.id.lin_report));
        linOnDuty = (findViewById(R.id.lin_onduty)); /*linOnDuty.setVisibility(View.GONE); if(sSFType=="0") linOnDuty.setVisibility(View.VISIBLE);*/
        linApprovals = (findViewById(R.id.lin_approvals));
        linTaClaim = (findViewById(R.id.lin_ta_claim));
        linExtShift = (findViewById(R.id.lin_extenden_shift)); linExtShift.setVisibility(View.GONE); if(sSFType=="0") linExtShift.setVisibility(View.VISIBLE);
        linTourPlan = (findViewById(R.id.lin_tour_plan));
        linHolidayWorking = findViewById(R.id.lin_holiday_working);
        linExit = (findViewById(R.id.lin_exit));
        approvalcount = findViewById(R.id.approvalcount);

        shared_common_pref = new Shared_Common_Pref(this);
        if (shared_common_pref.getvalue(Shared_Common_Pref.CHECK_COUNT).equals("0")) {
            linApprovals.setVisibility(View.GONE);
        } else {
            linApprovals.setVisibility(View.VISIBLE);
        }
        FlexboxLayout flexboxLayout=findViewById(R.id.flxlayut);
        View flxlastChild=null;
        int flg=0;
        for(int il=0;il<flexboxLayout.getChildCount();il++){
            if(flexboxLayout.getChildAt(il).getVisibility()==View.VISIBLE) {
                flxlastChild = flexboxLayout.getChildAt(il);
                if (flg == 1) flg = 0;
                else flg = 1;
            }
        }
        if(flg==1) {
            FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) flxlastChild.getLayoutParams();
            lp.setFlexBasisPercent(100);
            //lp.setOrder(-1);
            //lp.setFlexGrow(2);
            flxlastChild.setLayoutParams(lp);
        }
        linMyday.setOnClickListener(this);
        linCheckin.setOnClickListener(this);
        linRequstStaus.setOnClickListener(this);
        linReport.setOnClickListener(this);
        linOnDuty.setOnClickListener(this);
        linApprovals.setOnClickListener(this);
        linTaClaim.setOnClickListener(this);
        linExtShift.setOnClickListener(this);
        linTourPlan.setOnClickListener(this);
        linHolidayWorking.setOnClickListener(this);
        linExit.setOnClickListener(this);
        getcountdetails();
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
                Intent i = new Intent(this, Checkin.class);
                startActivity(i);
                break;

            case R.id.lin_request_status:
                startActivity(new Intent(this, Leave_Dashboard.class));
                break;

            case R.id.lin_ta_claim:
                startActivity(new Intent(this, TAClaimActivity.class)); //Travel_Allowance
                break;

            case R.id.lin_report:
                Intent Dashboard = new Intent(this, Dashboard_Two.class);
                Dashboard.putExtra("Mode", "RPT");
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

            case R.id.lin_holiday_working:
                AlertDialogBox.showDialog(Dashboard.this, "HAP Check-In", "Are you sure want to Check-in with Hoilday Entry", "YES", "NO", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {

                        common_class.CommonIntentwithoutFinishputextra(Checkin.class, "Mode", "holidayentry");
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.lin_onduty:
                startActivity(new Intent(this, On_Duty_Activity.class));
                break;
            case R.id.lin_exit:
                SharedPreferences.Editor editor = UserDetails.edit();
                editor.putBoolean("Login", false);
                editor.apply();
                finishAffinity();

                break;
            case R.id.lin_extenden_shift:
                Get_MydayPlan(2, "ValidateExtended");
                break;
            default:
                break;
        }

    }

    private void Get_MydayPlan(int flag, String Name) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", Name);
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
        Log.e("LOG_NAME", Name);
        Log.e("Log_Tp_SELECT", jsonArray.toString());
        Log.e("Log_FLAG", String.valueOf(flag));
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    // Log.e("GettodayResult", "response Tp_View: " + jsonObject.getString("success"));
                    if (flag == 1) {
                        JSONArray jsoncc = jsonObject.getJSONArray("Checkdayplan");
                        Log.e("LENGTH_Checkin", String.valueOf(jsoncc));
                        Log.e("LENGTH_Checkin", String.valueOf(jsoncc.length()));

                        //Log.e("TB_MyDAy_Plan",String.valueOf(jsoncc.getJSONObject(0).get("remarks")));
                        Log.e("MyDAY_LENGTH", String.valueOf(jsoncc.length()));
                        if (jsoncc.length() > 0) {
                            Log.e("LENGTH_FOR_LOOP", String.valueOf(jsoncc.length()));
                            linMyday.setVisibility(View.GONE);
                            linCheckin.setVisibility(View.VISIBLE);
                        } else {
                            linCheckin.setVisibility(View.GONE);
                            linMyday.setVisibility(View.VISIBLE);
                        }
                    } else {
                        String success = jsonObject.getString("success");
                        String Msg = jsonObject.getString("msg");
                        if (!Msg.equals("")) {
                            AlertDialogBox.showDialog(Dashboard.this, "HAP Check-In", Msg, "OK", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {

                                }
                            });
                        } else {
                            AlertDialogBox.showDialog(Dashboard.this, "HAP Check-In", Msg, "YES", "NO", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    common_class.CommonIntentwithoutFinishputextra(Checkin.class, "Mode", "extended");
                                    /*Intent intent = new Intent(getApplicationContext(), Checkin.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Extended_Flag", "extended");
                                    startActivity(intent);*/
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            // Toast.makeText(Dashboard.this, "Send To Checkin", Toast.LENGTH_SHORT).show();
                        }
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


    public void getcountdetails() {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "ViewAllCount");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("desig", "MGR");
        String commonworktype = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, commonworktype);

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSEcount", "response Tp_View: " + new Gson().toJson(response.body()));
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    // int TC=Integer.parseInt(jsonObject.getString("leave")) + Integer.parseInt(jsonObject.getString("Permission")) + Integer.parseInt(jsonObject.getString("vwOnduty")) + Integer.parseInt(jsonObject.getString("vwmissedpunch")) + Integer.parseInt(jsonObject.getString("TountPlanCount")) + Integer.parseInt(jsonObject.getString("vwExtended"));
                    //jsonObject.getString("leave"))
                    Log.e("TOTAl_COUNT", String.valueOf(Integer.parseInt(jsonObject.getString("leave")) + Integer.parseInt(jsonObject.getString("Permission")) + Integer.parseInt(jsonObject.getString("vwOnduty")) + Integer.parseInt(jsonObject.getString("vwmissedpunch")) + Integer.parseInt(jsonObject.getString("TountPlanCount")) + Integer.parseInt(jsonObject.getString("vwExtended"))));
                    //count = count +

                    Shared_Common_Pref.TotalCountApproval = Integer.parseInt(jsonObject.getString("leave")) + Integer.parseInt(jsonObject.getString("Permission")) + Integer.parseInt(jsonObject.getString("vwOnduty")) + Integer.parseInt(jsonObject.getString("vwmissedpunch")) + Integer.parseInt(jsonObject.getString("TountPlanCount")) + Integer.parseInt(jsonObject.getString("vwExtended"));
                    approvalcount.setText(String.valueOf(Shared_Common_Pref.TotalCountApproval));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common_class.ProgressdialogShow(2, "");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Get_MydayPlan(1, "check/mydayplan");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Get_MydayPlan(1, "check/mydayplan");
    }
}
