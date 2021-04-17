package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.R;

public class FlightTicketRequest extends AppCompatActivity {

    RadioGroup radioGrp;
    RadioButton radioOne, radioRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_ticket_request);
        radioGrp = findViewById(R.id.radio_ticket);
        radioOne = findViewById(R.id.radio_oneway);
        radioRound = findViewById(R.id.radio_twoway);
        radioGrp.clearCheck();

        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(FlightTicketRequest.this, rb.getText() + "  " + checkedId, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}