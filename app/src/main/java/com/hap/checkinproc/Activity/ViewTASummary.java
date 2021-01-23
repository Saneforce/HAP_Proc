package com.hap.checkinproc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTASummary extends AppCompatActivity {
    Intent vwSumryInt;
    String dateTime = "", travelType = "";
    TextView chsDate,trvlType,startedTime,endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_t_a_summary);
        vwSumryInt = getIntent();
        dateTime = vwSumryInt.getStringExtra("DateofExpense");
        travelType = vwSumryInt.getStringExtra("travelMode");
        Log.e("View_TA_Date", dateTime);
        Log.e("View_TA_travel", travelType);
        chsDate = findViewById(R.id.txt_date);
        trvlType = findViewById(R.id.travel_type);
     //   startedTime= findViewById(R.id.)


    }



   /* public void displayTravelMode(String ChoosedDate) {
        Log.d("JSON_VALUE_O", ChoosedDate);

        ChoosedDate = ChoosedDate.replaceAll("^[\"']+|[\"']+$", "");

        Log.d("JSON_VALUE_N", ChoosedDate);
        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("divisionCode", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            jj.put("Selectdate", ChoosedDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getTAdateDetails(jj.toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();

                Log.d("JSON_VALUE", jsonArray.toString());
                Log.d("JSON_VALUE", "CHECKING");
                TravelBike.setVisibility(View.VISIBLE);
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    StartedKm = String.valueOf(jsonObject.get("Start_Km"));
                    ClosingKm = String.valueOf(jsonObject.get("End_Km"));
                    PersonalKm = String.valueOf(jsonObject.get("Personal_Km"));
                    StartedKm = StartedKm.replaceAll("^[\"']+|[\"']+$", "");
                    PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");
                    if (PersonalKm.equals("null")) {
                        PersonalKiloMeter.setText(PersonalKm);
                    }
                    PersonalKiloMeter.setText(PersonalKm);
                    TxtStartedKm.setText(StartedKm);

                    Integer S = Integer.valueOf(String.valueOf(TxtStartedKm.getText()));


                    if (ClosingKm != null && !ClosingKm.isEmpty() && !ClosingKm.equals("null") && !ClosingKm.equals("")) {
                        ClosingKm = ClosingKm.replaceAll("^[\"']+|[\"']+$", "");
                        TxtClosingKm.setText(ClosingKm);
                        C = Integer.valueOf(ClosingKm);
                        Log.e("TOTAL_KM", String.valueOf(C));
                        totalkm = C - S;
                        Log.e("TOTAL_Claim_KM", String.valueOf(totalPersonalKm));
                        TotalTravelledKm.setText(String.valueOf(totalkm));
                    }

                    if (PersonalKm != null && !PersonalKm.isEmpty() && !PersonalKm.equals("null") && !PersonalKm.equals("")) {
                        PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");
                        Pva = Integer.valueOf(PersonalKm);
                        totalPersonalKm = totalkm - Pva;
                        PersonalTextKM.setText(String.valueOf(totalPersonalKm));
                        Log.e("TOTAL_Claim_KM", (PersonalTextKM.getText().toString()));
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }
*/

}