package com.hap.checkinproc.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.AttachementActivity;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FuelAllowance extends AppCompatActivity {


    LinearLayout LinearOtherAllowance, otherExpenseLayout;
    TextView oeEditext;
    EditText edtOE, edt;
    ImageView delete_lc;
    LinearLayout linLocalSpinner, Dynamicallowance;

    String FUClaim = "", div = "", State_Code = "", StartedKm = "", ClosingKm = "", ModeOfTravel = "", PersonalKm = "",
            DriverNeed = "", DateForAPi = "", DateTime = "", shortName = "", Exp_Name = "", Id = "", userEnter = "",
            attachment = "", maxAllowonce = "", strRetriveType = "", StrToEnd = "", StrBus = "", StrTo = "", StrDaName = "",
            OEdynamicLabel = "", strFuelAmount = "", StrModeValue = "", dynamicLabel = "", StrDailyAllowance = "", ldgEmpName = "",
            witOutBill = "", ValCd = "", fullPath = "", filePath = "", editMode = "", allowanceAmt = "", myldgEliAmt = "", myBrdEliAmt = "",
            drvldgEliAmt = "", drvBrdEliAmt = "", strGT = "", totLodgAmt = "", start_Image = "", End_Imge = "";
    Double tofuel = 0.0, ldgEliAmt = 0.0, ldgDrvEligi = 0.0, gTotal = 0.0, TotLdging = 0.0,
            GrandTotalAllowance = 0.0, fAmount = 0.0, doubleAmount = 0.0, myBrdAmt = 0.0, drvBrdAmt = 0.0,
            otherExp = 0.0, localCov = 0.0, sum = 0.0, sumsTotss = 0.0;

    double TotDA = 0.0, sTotal = 0.0, sums = 0.0;

    TextView txt_date, txt_ldg_type, TxtStartedKm, TxtClosingKm, modeTextView, travelTypeMode,
            TotalTravelledKm, txtBusFrom, txtBusTo, txtTaClaim, PersonalTextKM, PersonalKiloMeter,
            txtDailyAllowance, editText, ldg_cin, txtJNName, txtJNDesig, txtJNDept, txtJNHQ, txtJNMob,
            lblHdBill, lblHdBln, ldgWOBBal, ldgAdd, txtJNMyEli, txtMyEligi, txtDrivEligi, lbl_ldg_eligi, txt_totDA,
            fuelAmount, TextTotalAmount, editTexts,  localText, OeText, grandTotal, txtallamt, txt_BrdAmt,
            txt_DrvBrdAmt, txtJointAdd, txtJNEligi;

    Integer totalkm = 0, totalPersonalKm = 0, Pva, C = 0, S = 0, taCount = 0, oeCount = 0, editTextPositionss;

    int size = 0, lcSize = 0, OeSize = 0, pos = -1;
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