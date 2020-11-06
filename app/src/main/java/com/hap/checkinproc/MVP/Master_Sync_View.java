package com.hap.checkinproc.MVP;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Route_Master;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Master_Sync_View implements Main_Model.GetRoutemastersyncResult {

    String commonworktype;

    @Override
    public void GetRouteResult(Main_Model.GetRoutemastersyncResult.OnFinishedListenerroute onFinishedListener) {
        /** Create handle for the RetrofitInstance interface*/

        ///SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        for (int i = 0; i < 3; i++) {

            if (i == 0) {
                commonworktype = "{\"tableName\":\"mas_worktype\",\"coloumns\":\"[\\\"type_code as id\\\", \\\"Wtype as name\\\"]\",\"where\":\"[\\\"isnull(Active_flag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                //commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

            } else if (i == 1) {
                commonworktype = "{\"tableName\":\"vwstockiest_Master_APP\",\"coloumns\":\"[\\\"distributor_code as id\\\", \\\"stockiest_name as name\\\",\\\"town_code\\\",\\\"town_name\\\",\\\"Addr1\\\",\\\"Addr2\\\",\\\"City\\\",\\\"Pincode\\\",\\\"GSTN\\\",\\\"lat\\\",\\\"long\\\",\\\"addrs\\\",\\\"Tcode\\\",\\\"Dis_Cat_Code\\\"]\",\"where\":\"[\\\"isnull(Stockist_Status,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

            } else if (i == 2) {
                commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            }

            int ii = i;
            Log.e("Print_REquest", commonworktype);
            Log.e("SF_DETAILS", Shared_Common_Pref.Div_Code);
            Call<Object> call = service.GetRouteObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, "24", commonworktype);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Log.e("MAsterSyncView_Result", response.body() + "");
                    System.out.println("Route_Matser" + response.body().toString());
                    Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                    //approvalList=response.body();
                    onFinishedListener.onFinishedrouteObject(response.body(), ii);
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    onFinishedListener.onFailure(t);
                }
            });
        }


    }
}

