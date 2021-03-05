package com.hap.checkinproc.Activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TL_cliam_Apprval extends AppCompatActivity {

    LinearLayout travelDynamicLoaction;
    TextView editText;
    EditText etrTaFr, etrTaTo, enterFrom, enterTo, enterFare;
    String TLClaim = "", StrToEnd = "0";
    JSONArray jsonArray = null;
    ImageView ImgPreview;
    Integer tlPos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_l_cliam__apprval);
        startService(new Intent(this, TimerService.class));
        travelDynamicLoaction = findViewById(R.id.lin_travel_dynamic_location);

        TLClaim = String.valueOf(getIntent().getSerializableExtra("TLAllowance"));
        StrToEnd = String.valueOf(getIntent().getSerializableExtra("strEnd"));


        Log.v("TLAllowance", TLClaim);
        Log.v("TLStrToEnd", StrToEnd);

        try {
            jsonArray = new JSONArray(TLClaim);
            if (StrToEnd.equals("0")) {
                trvldLocationBus(jsonArray);
            } else {
                trvldLocation(jsonArray);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void trvldLocation(JSONArray traveldLoc) {

        for (int i = 0; i < traveldLoc.length(); i++) {
            JSONObject tldraftJson = null;
            try {
                tldraftJson = (JSONObject) traveldLoc.get(i);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = null;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                rowView = inflater.inflate(R.layout.tl_claim_appro, null);
                travelDynamicLoaction.addView(rowView, layoutParams);
                View views = travelDynamicLoaction.getChildAt(i);

                etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);
                etrTaFr.setText(tldraftJson.getString("From_P"));
                etrTaTo.setText(tldraftJson.getString("To_P"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }


    public void trvldLocationBus(JSONArray traveldLoc) {

        for (int j = 0; j < traveldLoc.length(); j++) {

            Log.v("TravelldArray", String.valueOf(traveldLoc.length()));

            JSONObject tldraftJsons = null;
            try {
                tldraftJsons = (JSONObject) traveldLoc.get(j);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = null;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                rowView = inflater.inflate(R.layout.tl_claim_appro_one, null);

                travelDynamicLoaction.addView(rowView, layoutParams);


                View views = travelDynamicLoaction.getChildAt(j);
                LinearLayout lad = (LinearLayout) views.findViewById(R.id.linear_row_ad);
                editText = (TextView) views.findViewById(R.id.enter_mode);
                enterFrom = views.findViewById(R.id.enter_from);
                enterTo = views.findViewById(R.id.enter_to);
                enterFare = views.findViewById(R.id.enter_fare);
                ImgPreview = views.findViewById(R.id.img_prv);

                editText.setText("" + tldraftJsons.getString("Mode"));
                enterFrom.setText("" + tldraftJsons.getString("From_P"));
                enterTo.setText("" + tldraftJsons.getString("To_P"));
                enterFare.setText("" + tldraftJsons.getString("Fare"));

                tlPos = travelDynamicLoaction.indexOfChild(rowView);
                ImgPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View views = travelDynamicLoaction.getChildAt(tlPos);


                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }  @Override
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