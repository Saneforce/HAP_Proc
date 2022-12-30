package com.hap.checkinproc.Activity_Hap;

import static com.hap.checkinproc.Activity_Hap.Approvals.CheckInfo;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Tp_Approval_FF_Modal;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.Tp_ApprovalStatus_Adapter;
import com.hap.checkinproc.adapters.Tp_Approval_Adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TP_Approval_Status extends AppCompatActivity {

    Gson gson;
    Type userType;
    List<Tp_Approval_FF_Modal> Tp_Approval_Model;
    List<Tp_Approval_FF_Modal> filteredList= new ArrayList<>();;

    private RecyclerView recyclerView;
    TextView tvName,date,remarks,worktype,hqName;
    Common_Class common_class;
    private Toolbar toolbar;
    private  String name="",d="",r="",wt="",hq="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp_approval_status);

        gson = new Gson();
//        common_class = new Common_Class(this);
//        recyclerView = findViewById(R.id.tp_status_recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvName=findViewById(R.id.tpApprovalStatusName);
        date=findViewById(R.id.date);
        remarks=findViewById(R.id.remarks);
        worktype=findViewById(R.id.workType);
        hqName=findViewById(R.id.hq);



        name=getIntent().getStringExtra("FieldForceName");
        tvName.setText(name);
        d=getIntent().getStringExtra("date");
        date.setText(d);
        r=getIntent().getStringExtra("remarks");
        remarks.setText(r);
        wt=getIntent().getStringExtra("Work_Type");
        worktype.setText(wt);
        hq=getIntent().getStringExtra("HQ");
        hqName.setText(hq);

        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
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

//        gettp_Details();



        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    TP_Approval_Status.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

//    private void gettp_Details() {
//        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//
//        Call<Object> mCall = apiInterface.GetTPObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "vwtourplan", routemaster);
//        mCall.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                Log.e("GetTPDetailsList", String.valueOf(response.body().toString()));
////                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
//                userType = new TypeToken<ArrayList<Tp_Approval_FF_Modal>>() {
//                }.getType();
//                Tp_Approval_Model = gson.fromJson(new Gson().toJson(response.body()), userType);
//
//                if (Tp_Approval_Model != null && Tp_Approval_Model.size() > 0) {
//                    for (Tp_Approval_FF_Modal filterlist : Tp_Approval_Model) {
//                        if (name.equalsIgnoreCase(filterlist.getFieldForceName())) {
//
//                            filteredList.add(filterlist);
//                        }
//                    }
//                }
//
//                Log.d("wwwwe","filteredList"+ filteredList.toString());
//                recyclerView.setAdapter(new Tp_ApprovalStatus_Adapter(filteredList, R.layout.tp_approval_status_layout, getApplicationContext()));
////                        , new AdapterOnClick() {
////                    @Override
////                    public void onIntentClick(int Name) {
//////
//////                        common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", Tp_Approval_Model.get(Name).getTmonth());
//////                        Shared_Common_Pref.Tp_Approvalflag = "1";
//////                        Shared_Common_Pref.Tp_SFCode = Tp_Approval_Model.get(Name).getSfCode();
//////                        Shared_Common_Pref.Tp_Sf_name = Tp_Approval_Model.get(Name).getFieldForceName();
//////                        Shared_Common_Pref.Tp_Monthname = Tp_Approval_Model.get(Name).getTmonth();
////
//////                        Intent intent = new Intent(Tp_Approval.this, Tp_Month_Select.class);
////
////
//////                        intent.putExtra("Emp_Code", Tp_Approval_Model.get(Name).getEmpCode());
//////                        intent.putExtra("HQ", Tp_Approval_Model.get(Name).getHQ());
//////                        intent.putExtra("Designation", Tp_Approval_Model.get(Name).getDesignation());
//////                        intent.putExtra("MobileNumber", Tp_Approval_Model.get(Name).getSFMobile());
//////                        intent.putExtra("Plan_Date", Tp_Approval_Model.get(Name).getStartDate());
//////                        intent.putExtra("Work_Type", Tp_Approval_Model.get(Name).getWorktypeName());
//////                        intent.putExtra("Route", Tp_Approval_Model.get(Name).getRouteName());
//////                        intent.putExtra("Distributor", Tp_Approval_Model.get(Name).getWorkedWithName());
//////                        intent.putExtra("Sf_Code", Tp_Approval_Model.get(Name).getSFCode());
//////                        intent.putExtra("Remarks", Tp_Approval_Model.get(Name).getRemarks());
//////                        intent.putExtra("workedwithname", Tp_Approval_Model.get(Name).getJointWorkName());
//////                        intent.putExtra("TPHqname", Tp_Approval_Model.get(Name).getTourHQName());
//////                        intent.putExtra("ShiftType", Tp_Approval_Model.get(Name).getTypename());
//////                        intent.putExtra("ChillCentreName", Tp_Approval_Model.get(Name).getCCentreName());
//////                        intent.putExtra("FromDate", Tp_Approval_Model.get(Name).getFromdate());
//////                        intent.putExtra("Worktype_Flag", Tp_Approval_Model.get(Name).getWorktypeFlag());
//////                        intent.putExtra("ToDate", Tp_Approval_Model.get(Name).getTodate());
//////                        intent.putExtra("DeptType", Tp_Approval_Model.get(Name).getDeptType());
//////                        intent.putExtra("MOT", Tp_Approval_Model.get(Name).getMOT());
//////                        intent.putExtra("DA_Type", Tp_Approval_Model.get(Name).getDA_Type());
//////                        intent.putExtra("Da", Tp_Approval_Model.get(Name).getDriver_Allow());
//////                        intent.putExtra("From_Place", Tp_Approval_Model.get(Name).getFrom_Place());
//////                        intent.putExtra("To_Place", Tp_Approval_Model.get(Name).getTo_Place());
////                    }
////                }));
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//
//            }
//        });
//    }
}