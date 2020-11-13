package com.hap.checkinproc.Activity_Hap;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hap.checkinproc.R;

public class  Dashboard_Two extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__two);


        CardView cardview3 = findViewById(R.id.cardview3);
        CardView cardview4 = findViewById(R.id.cardview4);
        CardView cardView5 = findViewById(R.id.cardview5);

        cardview3.setOnClickListener(this);
        cardview4.setOnClickListener(this);
        cardView5.setOnClickListener(this);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });




    }
    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Dashboard_Two.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){


            case R.id.cardview3:

                Intent  i2 = new Intent(this, Leave_Dashboard.class);

                startActivity(i2);
                break;


            case R.id.cardview4:

                Intent  i3 = new Intent(this, Travel_Allowance.class);

                startActivity(i3);
                break;

            case R.id.cardview5:

                Intent  i5 = new Intent(this, Reports.class);

                startActivity(i5);
                break;


            default:
                break;



        }

    }

    }

