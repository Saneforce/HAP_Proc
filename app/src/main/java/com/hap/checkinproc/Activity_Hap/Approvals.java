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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity.TAApprovalActivity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Activity.Extended_Shift_Activity;
import com.hap.checkinproc.Status_Activity.Leave_Status_Activity;
import com.hap.checkinproc.Status_Activity.MissedPunch_Status_Activity;
import com.hap.checkinproc.Status_Activity.Onduty_Status_Activity;
import com.hap.checkinproc.Status_Activity.Permission_Status_Activity;
import com.hap.checkinproc.Status_Activity.WeekOff_Status_Activity;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Approvals extends AppCompatActivity implements View.OnClickListener {
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    LinearLayout LeaveRequest, PermissionRequest, OnDuty, MissedPunch, ExtendedShift, TravelAllowance, TourPlan, lin_leavecancel_histry, lin_leaveholidaystatus;
    LinearLayout LeaveStatus, DaExcptStaus, PermissionStatus, OnDutyStatus, MissedStatus, ExtdShift, lin_weekoff, linLeaveCancel, lin_DeviationApproval, lin_holidayentryApproval, linDaExceptionEntry;
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    SharedPreferences Setups;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    public static final String SetupsInfo = "MySettings";
    TextView countLeaveRequest, extendedcount, countPermissionRequest, countOnDuty, countMissedPunch,
            countTravelAllowance, countTourPlan, txt_holiday_count, txt_deviation_count, txt_leavecancel_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvals);
        shared_common_pref = new Shared_Common_Pref(this);
        startService(new Intent(this, TimerService.class));

        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        Setups = getSharedPreferences(SetupsInfo, Context.MODE_PRIVATE);
        lin_leavecancel_histry = findViewById(R.id.lin_leavecancel_histry);
        lin_leaveholidaystatus = findViewById(R.id.lin_leaveholidaystatus);
        linLeaveCancel = findViewById(R.id.lin_leave_cancel);
        linDaExceptionEntry = findViewById(R.id.lin_daExp_entry);
        DaExcptStaus = findViewById(R.id.lin_da_excep_status);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
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
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
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
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
                Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
                Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
                Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });


        common_class = new Common_Class(this);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        LeaveRequest = findViewById(R.id.lin_leave_req);
        PermissionRequest = findViewById(R.id.lin_per_req);
        OnDuty = findViewById(R.id.lin_on_duty);
        MissedPunch = findViewById(R.id.lin_miss_punch);
        ExtendedShift = findViewById(R.id.lin_ext_shift_status);
        TravelAllowance = findViewById(R.id.lin_travel_allow);
        TourPlan = findViewById(R.id.lin_tour_plan);
        /*Status Linear*/
        LeaveStatus = findViewById(R.id.lin_leav_sta);
        PermissionStatus = findViewById(R.id.lin_per_sta);
        OnDutyStatus = findViewById(R.id.lin_duty_sta);
        MissedStatus = findViewById(R.id.lin_miss_sta);
        ExtdShift = findViewById(R.id.lin_ext_shift);
        lin_weekoff = findViewById(R.id.lin_weekoff);
        /*Request Text*/
        extendedcount = findViewById(R.id.txt_week_off_count);
        countLeaveRequest = findViewById(R.id.txt_leave_req_count);
        countPermissionRequest = findViewById(R.id.txt_per_req_count);
        countOnDuty = findViewById(R.id.txt_on_duty_count);
        countMissedPunch = findViewById(R.id.txt_miss_punch_count);
        countTravelAllowance = findViewById(R.id.txt_trvl_all);
        countTourPlan = findViewById(R.id.txt_tour_plan);
        txt_holiday_count = findViewById(R.id.txt_holiday_count);
        txt_deviation_count = findViewById(R.id.txt_deviation_count);
        txt_leavecancel_count = findViewById(R.id.txt_leave_cancel_req_count);

        lin_holidayentryApproval = findViewById(R.id.lin_holidayentryApproval);
        lin_DeviationApproval = findViewById(R.id.lin_DeviationApproval);
        /*Status text*/
        /*SetOnClickListner*/
        LeaveRequest.setOnClickListener(this);
        PermissionRequest.setOnClickListener(this);
        OnDuty.setOnClickListener(this);
        MissedPunch.setOnClickListener(this);
        ExtendedShift.setOnClickListener(this);
        TravelAllowance.setOnClickListener(this);
        TourPlan.setOnClickListener(this);
        LeaveStatus.setOnClickListener(this);
        PermissionStatus.setOnClickListener(this);
        OnDutyStatus.setOnClickListener(this);
        MissedStatus.setOnClickListener(this);
        ExtdShift.setOnClickListener(this);
        lin_weekoff.setOnClickListener(this);
        lin_leavecancel_histry.setOnClickListener(this);
        lin_leaveholidaystatus.setOnClickListener(this);
        linLeaveCancel.setOnClickListener(this);
        lin_holidayentryApproval.setOnClickListener(this);
        lin_DeviationApproval.setOnClickListener(this);
        linDaExceptionEntry.setOnClickListener(this);
        DaExcptStaus.setOnClickListener(this);


        getcountdetails();
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
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    countLeaveRequest.setText(jsonObject.getString("leave"));
                    countPermissionRequest.setText(jsonObject.getString("Permission"));
                    countOnDuty.setText(jsonObject.getString("vwOnduty"));
                    countTravelAllowance.setText(jsonObject.getString("ExpList"));
                    countMissedPunch.setText(jsonObject.getString("vwmissedpunch"));
                    countTourPlan.setText(jsonObject.getString("TountPlanCount"));
                    extendedcount.setText(jsonObject.getString("vwExtended"));
                    txt_holiday_count.setText(jsonObject.getString("HolidayCount"));
                    txt_deviation_count.setText(jsonObject.getString("DeviationC"));
                    txt_leavecancel_count.setText(jsonObject.getString("CancelLeave"));
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lin_leave_req:
                startActivity(new Intent(Approvals.this, Leave_Approval.class));
                finish();
                break;

            case R.id.lin_leave_cancel:

                startActivity(new Intent(Approvals.this, Leave_Cancel_Approval.class));
                finish();
                break;

            case R.id.lin_per_req:
                startActivity(new Intent(Approvals.this, Permission_Approval.class));
                finish();
                break;

            case R.id.lin_on_duty:
                startActivity(new Intent(Approvals.this, Onduty_approval.class));
                finish();
                break;

            case R.id.lin_miss_punch:
                startActivity(new Intent(Approvals.this, Missed_punch_Approval.class));
                finish();
                break;

            case R.id.lin_ext_shift_status:
                startActivity(new Intent(Approvals.this, Extendedshift_approval.class));
                finish();
                break;

            case R.id.lin_travel_allow:
                startActivity(new Intent(Approvals.this, TAApprovalActivity.class));
                finish();
                break;

            case R.id.lin_tour_plan:
                startActivity(new Intent(Approvals.this, Tp_Approval.class));
                finish();
                break;

            case R.id.lin_daExp_entry:
                startActivity(new Intent(Approvals.this, DAExcApproval.class));
                finish();
                break;

            case R.id.lin_leav_sta:
                common_class.CommonIntentwithoutFinishputextra(Leave_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_per_sta:
                common_class.CommonIntentwithoutFinishputextra(Permission_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_duty_sta:
                common_class.CommonIntentwithoutFinishputextra(Onduty_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_miss_sta:
                common_class.CommonIntentwithoutFinishputextra(MissedPunch_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_ext_shift:
                common_class.CommonIntentwithoutFinishputextra(Extended_Shift_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_weekoff:
                common_class.CommonIntentwithoutFinishputextra(WeekOff_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_leavecancel_histry:
                common_class.CommonIntentwithoutFinishputextra(LeaveCancelRequestStatus.class, "AMod", "1");
                finish();
                break;
            case R.id.lin_leaveholidaystatus:
                common_class.CommonIntentwithoutFinishputextra(HolidayEntryStatus.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_da_excep_status:
                common_class.CommonIntentwithoutFinishputextra(DaExceptionStatus.class, "AMod", "1");
                finish();
                break;
            case R.id.lin_holidayentryApproval:
                startActivity(new Intent(Approvals.this, Holiday_Entry_Approval.class));
                finish();
                break;
            case R.id.lin_DeviationApproval:
                startActivity(new Intent(Approvals.this, Deviation_Entry_Approval.class));
                finish();
                break;

        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(new Intent(this, TimerService.class));
        getcountdetails();
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                    Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
                    Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
                    Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
                    Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");
                    if (CheckIn == true) {
                        Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                        Dashboard.putExtra("Mode", "CIN");
                        startActivity(Dashboard);
                    } else
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));

                }
            });


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onPause() {
        super.onPause();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }


}