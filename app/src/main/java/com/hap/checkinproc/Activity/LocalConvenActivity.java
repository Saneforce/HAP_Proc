package com.hap.checkinproc.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocalConvenActivity extends AppCompatActivity {

    LinearLayout linlocalCon;
    TextView editTexts, txtLCAmnt;
    EditText edtLcFare, edt;
    LinearLayout  Dynamicallowance;
    String LCClaim = "";
    JSONArray jsonArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_conven);
        startService(new Intent(this, TimerService.class));
        linlocalCon = findViewById(R.id.lin_dyn_local_con);
        txtLCAmnt = findViewById(R.id.txt_local);
        LCClaim = String.valueOf(getIntent().getSerializableExtra("LCAllowance"));
        txtLCAmnt.setText("Rs." + String.valueOf(getIntent().getSerializableExtra("LCAll_Total")) + ".00");
        Log.v("JSON_LC", LCClaim);

        try {
            jsonArray = new JSONArray(LCClaim);
            localConDraft(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void localConDraft(JSONArray lcDraft) {


        JSONArray jsonAddition = null;
        for (int i = 0; i < lcDraft.length(); i++) {
            JSONObject lcdraftJson = null;
            try {
                lcdraftJson = (JSONObject) lcDraft.get(i);
                jsonAddition = lcdraftJson.getJSONArray("Additional");
                String expCode = String.valueOf(lcdraftJson.get("Exp_Code"));
                expCode = expCode.replaceAll("^[\"']+|[\"']+$", "");
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = null;

                LinearLayout.LayoutParams layoutParams = new LinearLayout
                        .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(15, 15, 15, 15);
                rowView = inflater.inflate(R.layout.local_claim_approval, null);

                linlocalCon.addView(rowView, layoutParams);
                int lcS = linlocalCon.getChildCount() - 1;

                View view = linlocalCon.getChildAt(i);
                editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                edtLcFare = (EditText) (view.findViewById(R.id.edt_la_fare));

                Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);

                localConDisplay(expCode, jsonAddition, lcS);
                editTexts.setText(expCode);
                edtLcFare.setText("" + lcdraftJson.get("Exp_Amt").toString());
                edtLcFare.setEnabled(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


    @SuppressLint("ResourceType")
    public void localConDisplay(String modeName, JSONArray jsonAddition, int position) {

        JSONObject jsonObjectAdd = null;
        for (int l = 0; l < jsonAddition.length(); l++) {
            try {
                jsonObjectAdd = (JSONObject) jsonAddition.get(l);
                String edtValueb = String.valueOf(jsonObjectAdd.get("Ref_Value"));
                RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_START);
                layoutparams_3.setMargins(20, -10, 0, 0);
                edt = new EditText(getApplicationContext());
                edt.setLayoutParams(layoutparams_3);
                edt.setText(edtValueb.replaceAll("^[\"']+|[\"']+$", ""));
                edt.setId(12345);
                edt.setTextSize(13);
                edt.setBackground(null);
                edt.setEnabled(false);
                childRel.addView(edt);
                View view = linlocalCon.getChildAt(position);
                Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
                Dynamicallowance.addView(childRel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onPause() {
        super.onPause();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(new Intent(this, TimerService.class));
    }
}