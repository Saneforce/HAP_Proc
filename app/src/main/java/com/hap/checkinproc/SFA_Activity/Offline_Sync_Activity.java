package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.hap.checkinproc.Activity_Hap.SFA_Activity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.MVP.MasterSync_Implementations;
import com.hap.checkinproc.MVP.Master_Sync_View;
import com.hap.checkinproc.MVP.Offline_SyncView;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Offline_Sync_Activity extends AppCompatActivity implements Main_Model.MasterSyncView {
    private Main_Model.presenter presenter;
    Shared_Common_Pref sharedCommonPref;
    Type userType;
    Gson gson;
    private ProgressDialog progress;
    Common_Class common_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline__sync_);
        sharedCommonPref = new Shared_Common_Pref(Offline_Sync_Activity.this);
        presenter = new MasterSync_Implementations(this, new Offline_SyncView());
        gson = new Gson();
        common_class = new Common_Class(this);
        presenter.requestDataFromServer();
        progress = new ProgressDialog(this);
        progress.setMessage("Data Is Syncing ");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while (jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 5;
                        progress.setProgress(jumpTime);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    @Override
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
        String serializedData = gson.toJson(responsebody);
        if (position == 0) {
            //Outlet_List
           //System.out.println("GetOutlet_All" + serializedData);
            sharedCommonPref.save(Shared_Common_Pref.Outlet_List, serializedData);
        } else if (position == 1) {
            //Distributor_List
           // System.out.println("Distributor_List" + serializedData);
            sharedCommonPref.save(Shared_Common_Pref.Distributor_List, serializedData);
        } else if (position == 2) {
            //Category_List
            //System.out.println("Category_List" + serializedData);
            sharedCommonPref.save(Shared_Common_Pref.Category_List, serializedData);
        } else if (position == 3) {
            //Product_List
           // System.out.println("Product_List" + serializedData);
            sharedCommonPref.save(Shared_Common_Pref.Product_List, serializedData);
        } else if (position == 4) {
            //GetTodayOrder_List
            //System.out.println("GetTodayOrder_List" + serializedData);
            sharedCommonPref.save(Shared_Common_Pref.GetTodayOrder_List, serializedData);
        } else if (position == 5) {
            //GetTodayOrder_List
            //System.out.println("GetTodayOrderDetails_List" + serializedData);
            sharedCommonPref.save(Shared_Common_Pref.TodayOrderDetails_List, serializedData);
        }
        else {
            progress.dismiss();
            System.out.println("Compititor_List" + serializedData);
            sharedCommonPref.save(Shared_Common_Pref.Compititor_List, serializedData);
            if ( sharedCommonPref.Sync_Flag!=null &&sharedCommonPref.Sync_Flag.equals("1")) {
                common_class.CommonIntentwithFinish( Dashboard_Route.class);
            } else if ( sharedCommonPref.Sync_Flag!=null && sharedCommonPref.Sync_Flag.equals("2")) {
                common_class.CommonIntentwithFinish(Invoice_History.class);
            } else if(sharedCommonPref.Sync_Flag!=null && sharedCommonPref.Sync_Flag.equals("0")) {
                common_class.CommonIntentwithFinish(SFA_Activity.class);
            }
        }

    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }
}