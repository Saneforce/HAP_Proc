package com.hap.checkinproc.Activity_Hap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.R;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    TextView username;
    LinearLayout linMyday, linCheckin, linRequstStaus, linReport, linOnDuty, linApprovals, linTaClaim, linExtShift, linTourPlan, linExit;
    Integer type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        username = findViewById(R.id.username);


        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        type = (shared.getInt("CheckCount", 0));


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
                break;

            case R.id.lin_request_status:
                startActivity(new Intent(this, Leave_Dashboard.class));
                break;

            case R.id.lin_ta_claim:
                startActivity(new Intent(this, Travel_Allowance.class));
                break;

            case R.id.lin_report:
                startActivity(new Intent(this, Reports.class));
                break;

            case R.id.lin_approvals:
                startActivity(new Intent(this, Approvals.class));
                break;
            case R.id.lin_myday_plan:
                startActivity(new Intent(this, Mydayplan_Activity.class));
                break;

            case R.id.lin_tour_plan:
                startActivity(new Intent(this, Tp_Month_Select.class));
                break;


            default:
                break;
        }


    }
}
