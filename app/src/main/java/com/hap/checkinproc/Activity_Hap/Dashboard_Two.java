package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hap.checkinproc.R;

public class Dashboard_Two extends AppCompatActivity implements View.OnClickListener {

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



    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(Dashboard_Two.this,"There is no back action",Toast.LENGTH_LONG).show();
        return;
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

