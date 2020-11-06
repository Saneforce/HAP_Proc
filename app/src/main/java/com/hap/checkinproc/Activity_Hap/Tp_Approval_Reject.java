package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hap.checkinproc.Model_Class.Tp_Approval_Model;
import com.hap.checkinproc.R;

public class Tp_Approval_Reject extends AppCompatActivity {
    TextView name, empcode, hq, designation, plandate, worktype, route, distributor, remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp__approval__reject);
        name = findViewById(R.id.name);
        empcode = findViewById(R.id.empcode);
        hq = findViewById(R.id.hq);
        designation = findViewById(R.id.designation);
        plandate = findViewById(R.id.plandate);
        worktype = findViewById(R.id.worktype);
        route = findViewById(R.id.route);
        distributor = findViewById(R.id.distributor);
        remarks = findViewById(R.id.remarks);
        Intent i = getIntent();
        name.setText(i.getExtras().getString("Username"));
        empcode.setText(i.getExtras().getString("Emp_Code"));
        hq.setText(i.getExtras().getString("HQ"));
        designation.setText(i.getExtras().getString("Designation"));
        name.setText(i.getExtras().getString("MobileNumber"));
        plandate.setText(i.getExtras().getString("Plan_Date"));
        worktype.setText(i.getExtras().getString("Work_Type"));
        route.setText(i.getExtras().getString("Route"));
        distributor.setText(i.getExtras().getString("Distributor"));

        remarks.setText(i.getExtras().getString("Remarks"));
        /*String username = i.getExtras().getString("Username");
        String Emp_Code = i.getExtras().getString("Emp_Code");
        String HQ = i.getExtras().getString("Emp_Code");
        String Designation = i.getExtras().getString("Designation");
        String MobileNumber = i.getExtras().getString("MobileNumber");
        String Plan_Date = i.getExtras().getString("Plan_Date");
        String Work_Type = i.getExtras().getString("Work_Type");
        String Route = i.getExtras().getString("Route");
        String Distributor = i.getExtras().getString("Distributor");
        String Remarks = i.getExtras().getString("Remarks");*/


    }
}
