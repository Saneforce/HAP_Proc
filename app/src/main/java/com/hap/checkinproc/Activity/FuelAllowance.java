package com.hap.checkinproc.Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.hap.checkinproc.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;


public class FuelAllowance extends AppCompatActivity {



    String FUClaim = "",  StartedKm = "", ClosingKm = "", PersonalKm = "", StrDaName = "", strFuelAmount = "";
    Double tofuel = 0.0,  fAmount = 0.0;
    TextView  TxtStartedKm, TxtClosingKm, travelTypeMode, TotalTravelledKm, txtTaClaim, PersonalTextKM, PersonalKiloMeter,
            fuelAmount, TextTotalAmount;
    Integer totalkm = 0, totalPersonalKm = 0, Pva, C = 0, S = 0;
    JSONArray jsonArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_allowance);
        FUClaim = String.valueOf(getIntent().getSerializableExtra("jsonTravDetai"));
        txtTaClaim = findViewById(R.id.mode_name);
        TxtStartedKm = findViewById(R.id.txt_started_km);
        TxtClosingKm = findViewById(R.id.txt_ended_km);
        travelTypeMode = findViewById(R.id.txt_type_travel);
        PersonalTextKM = findViewById(R.id.personal_km_text);
        TextTotalAmount = findViewById(R.id.txt_total_amt);
        TotalTravelledKm = findViewById(R.id.total_km);
        PersonalKiloMeter = findViewById(R.id.pers_kilo_meter);

        fuelAmount = findViewById(R.id.fuel_amount);
        try {
            jsonArray = new JSONArray(FUClaim);
            fuelAll(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("JSON_FUC", FUClaim);

    }



    public void fuelAll(JSONArray jsonArray){
        JSONObject jsonObject = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            int finalC = i;

            Log.e("JsonArray", String.valueOf(jsonArray.length()));

            try {
                jsonObject = (JSONObject) jsonArray.get(i);
                Log.e("jsonObject", String.valueOf(jsonObject.toString()));
                StartedKm = jsonObject.getString("Start_KM");
                ClosingKm = jsonObject.getString("End_KM");
                PersonalKm = jsonObject.getString("Personal_KM");
                StrDaName = jsonObject.getString("Mode_Of_Travel");
                strFuelAmount = jsonObject.getString("Fare");

                Log.v("StartedKm", StartedKm);
                Log.v("ClosingKm", ClosingKm);
                Log.v("PersonalKm", PersonalKm);

                fAmount = Double.valueOf(strFuelAmount);
                fuelAmount.setText(" Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM ");
/*                txtTaClaim.setText(StrDaName);*/

                StartedKm = StartedKm.replaceAll("^[\"']+|[\"']+$", "");
                if (StartedKm != null && !StartedKm.isEmpty() && !StartedKm.equals("null") && !StartedKm.equals("")) {
                    S = Integer.valueOf(StartedKm);
                    TxtStartedKm.setText(StartedKm);
                } else {

                }

                PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");

                if (PersonalKm.equals("null")) {
                    PersonalKiloMeter.setText("0");
                } else {
                    PersonalKiloMeter.setText(PersonalKm);
                }

                if (ClosingKm != null && !ClosingKm.isEmpty() && !ClosingKm.equals("null") && !ClosingKm.equals("")) {
                    ClosingKm = ClosingKm.replaceAll("^[\"']+|[\"']+$", "");
                    TxtClosingKm.setText(ClosingKm);
                    C = Integer.valueOf(ClosingKm);
                    totalkm = C - S;

                }

                if (PersonalKm != null && !PersonalKm.isEmpty() && !PersonalKm.equals("null") && !PersonalKm.equals("")) {
                    PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");
                    Pva = Integer.valueOf(PersonalKm);
                    totalPersonalKm = totalkm - Pva;
                    PersonalTextKM.setText(String.valueOf(totalPersonalKm));
                }

                Double totalAmount = Double.valueOf(strFuelAmount);
                tofuel = totalPersonalKm * totalAmount;
                TextTotalAmount.setText("Rs. " + new DecimalFormat("##0.00").format(tofuel));
                TotalTravelledKm.setText(String.valueOf(totalkm));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}