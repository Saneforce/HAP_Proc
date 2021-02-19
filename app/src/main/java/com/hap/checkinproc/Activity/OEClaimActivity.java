package com.hap.checkinproc.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OEClaimActivity extends AppCompatActivity {

    LinearLayout LinearOtherAllowance, otherExpenseLayout;
    TextView oeEditext;
    EditText edtOE, edt;
    ImageView delete_lc;
    LinearLayout linLocalSpinner, Dynamicallowance;
    String OEClaim = "";
    JSONArray jsonArray = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_e_claim);

        LinearOtherAllowance = findViewById(R.id.lin_dyn_other_Expense);
        otherExpenseLayout = findViewById(R.id.lin_total_other);

        OEClaim = String.valueOf(getIntent().getSerializableExtra("OEAllowance"));

        Log.v("JSON_LC", OEClaim);

        try {
            jsonArray = new JSONArray(OEClaim);
            OeDraft(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void OeDraft(JSONArray oEDraft) {

        for (int i = 0; i < oEDraft.length(); i++) {

            JSONObject oejsonArray = null;
            try {
                oejsonArray = (JSONObject) oEDraft.get(i);
                String expCode = oejsonArray.getString("Exp_Code");

                expCode = expCode.replaceAll("^[\"']+|[\"']+$", "");


                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = null;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                rowView = inflater.inflate(R.layout.oe_claim_approval, null);


                LinearOtherAllowance.addView(rowView, layoutParams);

                View childView = LinearOtherAllowance.getChildAt(i);
                otherExpenseLayout.setVisibility(View.VISIBLE);
                oeEditext = (TextView) (childView.findViewById(R.id.other_enter_mode));
                edtOE = (EditText) (childView.findViewById(R.id.oe_fre_amt));

                oeEditext.setText(expCode);
                edtOE.setText(oejsonArray.getString("Exp_Amt"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }
}