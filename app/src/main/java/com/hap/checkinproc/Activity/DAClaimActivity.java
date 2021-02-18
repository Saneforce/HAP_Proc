package com.hap.checkinproc.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DAClaimActivity extends AppCompatActivity {

    String DaClaim = "";
    TextView dailyAllowance, modeAmount,DrvBrdAmt,Datotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_a_claim);
        dailyAllowance = findViewById(R.id.txt_daily_allowance_mode);
        modeAmount = findViewById(R.id.txt_BrdAmt);
        DrvBrdAmt = findViewById(R.id.txt_DrvBrdAmt);
        Datotal = findViewById(R.id.txt_totDA);
        DaClaim = String.valueOf(getIntent().getSerializableExtra("DaAllowance"));
        Log.v("JSONARRA_Y", DaClaim);
        try {
            JSONArray jsonArray = new JSONArray(DaClaim);
            Log.v("JSONARRAY", jsonArray.toString());


            for(int i=0;i<jsonArray.length();i++){
                JSONObject js = (JSONObject) jsonArray.get(i);
                dailyAllowance.setText(""+js.getString("all_name"));
                modeAmount.setText("Rs."+js.getString("brd_amt")+".00");
                DrvBrdAmt.setText("Rs."+js.getString("drvBrdAmt")+".00");
                DrvBrdAmt.setText("Rs."+js.getString("drvBrdAmt")+".00");
                Datotal.setText("Rs."+js.getString("da_amount")+".00");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}