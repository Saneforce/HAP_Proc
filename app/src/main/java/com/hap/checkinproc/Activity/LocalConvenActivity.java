package com.hap.checkinproc.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.R;

public class LocalConvenActivity extends AppCompatActivity {

    LinearLayout linlocalCon;
    TextView editTexts;
    EditText edtLcFare;
    LinearLayout  linLocalSpinner,Dynamicallowance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_conven);
        linlocalCon = findViewById(R.id.lin_dyn_local_con);
    }



    public void localConDraft(JsonArray lcDraft) {


        JsonArray jsonAddition = null;
        for (int i = 0; i < lcDraft.size(); i++) {
            JsonObject lcdraftJson = (JsonObject) lcDraft.get(i);

            jsonAddition = lcdraftJson.getAsJsonArray("Additional");
            String expCode = String.valueOf(lcdraftJson.get("Exp_Code"));

            expCode = expCode.replaceAll("^[\"']+|[\"']+$", "");
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(15, 15, 15, 15);

            rowView = inflater.inflate(R.layout.activity_local_convenyance, null);


            linlocalCon.addView(rowView, layoutParams);

            int lcS = linlocalCon.getChildCount() - 1;

            View view = linlocalCon.getChildAt(i);
            editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
            edtLcFare = (EditText) (view.findViewById(R.id.edt_la_fare));
            linLocalSpinner = (LinearLayout) view.findViewById(R.id.lin_loc_spiner);
            Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);



         /*   editTexts.setText(expCode);
            edtLcFare.setText("" + lcdraftJson.get("Exp_Amt").toString());*/

        }

    }

}