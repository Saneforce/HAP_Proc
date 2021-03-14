package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route;
import com.hap.checkinproc.SFA_Activity.Dist_Locations;
import com.hap.checkinproc.SFA_Activity.Outlet_Info_Activity;
import com.hap.checkinproc.SFA_Activity.Reports_Outler_Name;
import com.hap.checkinproc.SFA_Activity.SFA_Dashboard;

import java.lang.reflect.Type;

public class SFA_Activity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout Lin_Route, Lin_Lead, Lin_Dashboard, Lin_Outlet, DistLocation, Logout, lin_Reports;
    Gson gson;
    Type userType;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfactivity);
        Lin_Route = findViewById(R.id.Lin_Route);
        DistLocation = findViewById(R.id.DistLocation);
        Lin_Lead = findViewById(R.id.Lin_Lead);
        Lin_Dashboard = findViewById(R.id.Lin_Dashboard);
        Lin_Outlet = findViewById(R.id.Lin_Outlet);
        lin_Reports = findViewById(R.id.lin_Reports);
        Logout = findViewById(R.id.Logout);
        common_class = new Common_Class(this);
        Lin_Route.setOnClickListener(this);
        Lin_Lead.setOnClickListener(this);
        lin_Reports.setOnClickListener(this);
        Lin_Dashboard.setOnClickListener(this);
        Lin_Outlet.setOnClickListener(this);
        DistLocation.setOnClickListener(this);
        Logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Lin_Dashboard:
                common_class.CommonIntentwithNEwTask(SFA_Dashboard.class);
                break;
            case R.id.Lin_Route:
                common_class.CommonIntentwithNEwTask(Dashboard_Route.class);
                break;
            case R.id.Lin_Outlet:
                common_class.CommonIntentwithNEwTask(Outlet_Info_Activity.class);
                break;

            case R.id.DistLocation:
                common_class.CommonIntentwithNEwTask(Dist_Locations.class);
                break;
            case R.id.lin_Reports:
                common_class.CommonIntentwithNEwTask(Reports_Outler_Name.class);
                break;


            case R.id.Logout:
                AlertDialogBox.showDialog(SFA_Activity.this, "HAP SFA", "Are You Sure Want to Logout?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        common_class.CommonIntentwithFinish(Dashboard.class);
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
                break;
        }
    }
}