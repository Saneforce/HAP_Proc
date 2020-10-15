package com.hap.checkinproc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Leave_Dashboard extends AppCompatActivity  implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_dashboard);
        CardView cardview1 = findViewById(R.id.leave_request);
        CardView cardview2 = findViewById(R.id.permission_request);
        CardView cardview3 = findViewById(R.id.missed_punch);
        CardView cardview4 = findViewById(R.id.weekly_off);


        cardview1.setOnClickListener(this);
        cardview2.setOnClickListener(this);
        cardview3.setOnClickListener(this);
        cardview4.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.leave_request:

                Intent i = new Intent(this, Leave_Request.class);

                startActivity(i);
                break;
            case R.id.weekly_off:

                Intent i2 = new Intent(this, Weekly_Off.class);

                startActivity(i2);
                break;
            case R.id.permission_request:

                Intent i3 = new Intent(this, Permission_Request.class);

                startActivity(i3);
                break;
            case R.id.missed_punch:

                Intent i4 = new Intent(this,Missed_Punch.class);

                startActivity(i4);
                break;
        }
    }
}