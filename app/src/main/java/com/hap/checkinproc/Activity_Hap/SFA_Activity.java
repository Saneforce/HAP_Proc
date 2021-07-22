package com.hap.checkinproc.Activity_Hap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Dashboard_Order_Reports;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route;
import com.hap.checkinproc.SFA_Activity.Dist_Locations;
import com.hap.checkinproc.SFA_Activity.Lead_Activity;
import com.hap.checkinproc.SFA_Activity.Offline_Sync_Activity;
import com.hap.checkinproc.SFA_Activity.Outlet_Info_Activity;
import com.hap.checkinproc.SFA_Activity.Reports_Outler_Name;
import com.hap.checkinproc.SFA_Activity.SFADCRActivity;
import com.hap.checkinproc.SFA_Activity.SFA_Dashboard;
import com.hap.checkinproc.common.DatabaseHandler;

import org.json.JSONArray;

import java.lang.reflect.Type;

public class SFA_Activity extends AppCompatActivity implements View.OnClickListener /*,Main_Model.MasterSyncView*/ {
    LinearLayout Lin_Route, Lin_DCR, Lin_Lead, Lin_Dashboard, Lin_Outlet, DistLocation, Logout, lin_Reports, SyncButon, linorders;
    Gson gson;
    Type userType;
    Common_Class common_class;
    private Main_Model.presenter presenter;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfactivity);
        db = new DatabaseHandler(this);
        sharedCommonPref = new Shared_Common_Pref(SFA_Activity.this);
        Lin_Route = findViewById(R.id.Lin_Route);
        SyncButon = findViewById(R.id.SyncButon);
        DistLocation = findViewById(R.id.DistLocation);
        Lin_Lead = findViewById(R.id.Lin_Lead);
        Lin_DCR = findViewById(R.id.Lin_DCR);
        Lin_Dashboard = findViewById(R.id.Lin_Dashboard);
        Lin_Outlet = findViewById(R.id.Lin_Outlet);
        linorders = findViewById(R.id.linorders);
        lin_Reports = findViewById(R.id.lin_Reports);
        Logout = findViewById(R.id.Logout);
        common_class = new Common_Class(this);
        SyncButon.setOnClickListener(this);
        Lin_Route.setOnClickListener(this);
        Lin_DCR.setOnClickListener(this);
        Lin_Lead.setOnClickListener(this);
        lin_Reports.setOnClickListener(this);
        Lin_Dashboard.setOnClickListener(this);
        Lin_Outlet.setOnClickListener(this);
        DistLocation.setOnClickListener(this);
        linorders.setOnClickListener(this);
        Logout.setOnClickListener(this);
        //presenter = new MasterSync_Implementations(this, new Offline_SyncView());
        gson = new Gson();
        // presenter.requestDataFromServer();

/*
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });*/

        if (sharedCommonPref.getvalue(Constants.HAVE_VALUE, "").equals(""))
            common_class.getDataFromApi(Constants.GetTodayOrder_List, this, false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Lin_Dashboard:
                common_class.CommonIntentwithNEwTask(SFA_Dashboard.class);
                break;
            case R.id.Lin_DCR:
                common_class.CommonIntentwithNEwTask(SFADCRActivity.class);
                break;
            case R.id.Lin_Route:
                sharedCommonPref.save(sharedCommonPref.DCRMode, "");
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
            case R.id.SyncButon:
                Shared_Common_Pref.Sync_Flag = "10";
                common_class.CommonIntentwithNEwTask(Offline_Sync_Activity.class);
                break;
            case R.id.linorders:
                common_class.CommonIntentwithNEwTask(Dashboard_Order_Reports.class);
                break;
            case R.id.Lin_Lead:
                common_class.CommonIntentwithNEwTask(Lead_Activity.class);
                break;
            case R.id.Logout:
                AlertDialogBox.showDialog(SFA_Activity.this, "HAP SFA", "Are You Sure Want to Logout?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        sharedCommonPref.save("ActivityStart", "false");
                        Intent intent = new Intent(SFA_Activity.this, Dashboard_Two.class);
                        intent.putExtra("Mode", "CIN");
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }


    boolean checkValueStore() {
        try {
            JSONArray storeData = db.getMasterData(Constants.Outlet_Total_Orders);
            if (storeData != null && storeData.length() > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

   /* @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void setDataToRoute(ArrayList<Route_Master> noticeArrayList) {

    }

    @Override
    public void setDataToRouteObject(Object responsebody, int position) {
        Log.e("Calling Position", String.valueOf(position));
        // Toast.makeText(this, "Position" + position, Toast.LENGTH_SHORT).show();
        Log.e("ResponseFromServer", String.valueOf(responsebody));
        String serializedData = gson.toJson(responsebody);
        switch (position) {
            case (0):
                //Outlet_List
                System.out.println("GetTodayOrder_All" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Outlet_List, serializedData);
                break;
            case (1):
                //Distributor_List
                //  System.out.println("Distributor_List" + serializedData);
                //  sharedCommonPref.save(Shared_Common_Pref.Distributor_List, serializedData);
                break;
            case (2):
                //Category_List
                System.out.println("Category_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Category_List, serializedData);
                break;
            case (3):
                //Product_List
                System.out.println("Product_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Product_List, serializedData);
                break;
            case (4):
                //GetTodayOrder_List
                System.out.println("GetTodayOrder_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.GetTodayOrder_List, serializedData);
                break;
            default:
                System.out.println("Compititor_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Compititor_List, serializedData);
        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }*/

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            common_class.CommonIntentwithFinish(Dashboard.class);
            return true;
        }
        return false;
    }
*/

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });


    @Override
    public void onBackPressed() {
        Log.v("CHECKING", "CHECKING");
    }
}