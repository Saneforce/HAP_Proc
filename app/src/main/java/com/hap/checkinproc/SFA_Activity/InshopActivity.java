package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hap.checkinproc.Activity_Hap.SFA_Activity;
import com.hap.checkinproc.R;

public class InshopActivity extends AppCompatActivity {
    CardView checkin,checkout,inshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop);

        checkin = findViewById(R.id.checkinLay);
        checkout = findViewById(R.id.checkoutLay);
        inshop = findViewById(R.id.inshopLay);

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InshopActivity.this, InshopCheckinActivity.class));

            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InshopActivity.this, InshopCheckoutActivity.class));

            }
        });
        inshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InshopActivity.this, InshopOutletActivity.class));

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(InshopActivity.this, SFA_Activity.class));
        super.onBackPressed();
    }
}