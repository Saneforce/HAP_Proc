package com.hap.checkinproc.Activity.Util;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaApprovalDisplay extends AppCompatActivity {

    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_approval_display);

        shared_common_pref = new Shared_Common_Pref(this);
        callTaDateDisplay();
    }


    public void callTaDateDisplay() {


        try {
            JSONObject taJsObject = new JSONObject();
            taJsObject.put("sfCode", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            taJsObject.put("Ta_Date", "2021-02-05");
            Log.v("jsonObject", taJsObject.toString());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> call = apiInterface.getTaDateApproval(taJsObject.toString());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("TAG_TA_RESPONSE", response.body().toString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.v("TARESPONSE", "Failure_taResponse");
                }
            });

        } catch (JSONException e) {

        }


    }

}