package com.hap.checkinproc.Activity_Hap;

import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.TAApprListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TACumulativeApproval extends AppCompatActivity {

    private RecyclerView recyclerView;
    TAApprListItem mAdapter;
    Shared_Common_Pref sharedCommonPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ta_cumulative_approval);
            sharedCommonPref = new Shared_Common_Pref(this);

            TextView txtHelp = findViewById(R.id.toolbar_help);
            ImageView imgHome = findViewById(R.id.toolbar_home);
            txtHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), Help_Activity.class));
                }
            });
            TextView txtErt = findViewById(R.id.toolbar_ert);
            TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

            txtErt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ERT.class));
                }
            });
            txtPlaySlip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
                }
            });


            ObjectAnimator textColorAnim;
            textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
            textColorAnim.setDuration(500);
            textColorAnim.setEvaluator(new ArgbEvaluator());
            textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
            textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
            textColorAnim.start();
            imgHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                    Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                    if (CheckIn == true) {
                        Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                        Dashboard.putExtra("Mode", "CIN");
                        startActivity(Dashboard);
                    } else
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }
            });

//            JSONArray dyRpt = new JSONArray();
//            //for (int il = 0; il < 10; il++) {
//            JSONObject newItem = new JSONObject();
//            try {
//                newItem.put("name", "Kumar S");
//                newItem.put("value", "SDM - 2345");
//                newItem.put("Link", true);
//                newItem.put("color", "#dddddd");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            dyRpt.put(newItem);
//            newItem = new JSONObject();
//            try {
//                newItem.put("name", "Ram Prabhu");
//                newItem.put("value", "SDE - 5521");
//                newItem.put("Link", true);
//                newItem.put("color", "#dddddd");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            dyRpt.put(newItem);
//            newItem = new JSONObject();
//            try {
//                newItem.put("name", "Siva kumar");
//                newItem.put("value", "SDE - 3294");
//                newItem.put("Link", true);
//                newItem.put("color", "#dddddd");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            dyRpt.put(newItem);
//            newItem = new JSONObject();
//            try {
//                newItem.put("name", "Selvam");
//                newItem.put("value", "SDE - 3235");
//                newItem.put("Link", true);
//                newItem.put("color", "#dddddd");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            dyRpt.put(newItem);
//
//            newItem = new JSONObject();
//            try {
//                newItem.put("name", "Manikandan K");
//                newItem.put("value", "SDE-56763");
//                newItem.put("Link", true);
//                newItem.put("color", "#dddddd");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            dyRpt.put(newItem);
            //}

            JSONObject obj = new JSONObject(sharedCommonPref.getvalue(Constants.WEEKLY_EXPENSE));

            recyclerView = (RecyclerView) findViewById(R.id.rclyApprLst);
            mAdapter = new TAApprListItem(obj.getJSONArray("Data")
                    , TACumulativeApproval.this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            ImageView backView = findViewById(R.id.imag_back);
//        backView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), Approvals.class));
//            }
//        });

        } catch (Exception e) {
        }
    }

}