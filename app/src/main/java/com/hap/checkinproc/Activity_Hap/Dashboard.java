package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hap.checkinproc.R;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    TextView username;
    String pic;
    ImageView profileImage;
    Integer type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        username=findViewById(R.id.username);
        CardView cardview1 = findViewById(R.id.cardview1);
        CardView cardview2 = findViewById(R.id.sec_card_view);
        CardView cardview3 = findViewById(R.id.cardview3);
        CardView cardview4 = findViewById(R.id.cardview4);
        CardView cardview8 = findViewById(R.id.cardview8);
        CardView cardview5 = findViewById(R.id.cardview5);
        CardView cardview9 = findViewById(R.id.cardview9);
        CardView tourplan = findViewById(R.id.tourplan);



//profile image set
        /*profileImage=(ImageView)findViewById(R.id.profile_image);
        Intent intent = getIntent();
        pic = intent.getStringExtra("photo");
        Log.e("avra",pic );
        Glide.with(this).load(pic).into(profileImage);
*/
        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        type = (shared.getInt("CheckCount", 0));


        if(type == 0){

            cardview8.setVisibility(View.GONE);

        }else {

        }

        cardview1.setOnClickListener(this);
        cardview2.setOnClickListener(this);
        cardview3.setOnClickListener(this);
        cardview4.setOnClickListener(this);
        cardview5.setOnClickListener(this);
        cardview8.setOnClickListener(this);
        cardview9.setOnClickListener(this);
        tourplan.setOnClickListener(this);



        }

    @Override
    public void onBackPressed() {
            Toast.makeText(Dashboard.this,"There is no back action",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        Intent  I;
        switch (view.getId()){

            case R.id.cardview1:

                /*Intent  i = new Intent(this, Check_in.class);

            startActivity(i);*/
            break;
            case R.id.sec_card_view:
               /* Intent i1 = new Intent(this, Ocheck_in.class);
                startActivity(i1);*/

                Toast.makeText(this, "Checking", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cardview3:

                  I = new Intent(this, Leave_Dashboard.class);

                startActivity(I);
                break;


            case R.id.cardview4:

                I = new Intent(this, Travel_Allowance.class);

                startActivity(I);
                break;

            case R.id.cardview5:

                I = new Intent(this, Reports.class);

                startActivity(I);
                break;

            case R.id.cardview8:

                I = new Intent(this, Approvals.class);

                startActivity(I);
                break;
            case R.id.cardview9:

                I = new Intent(this, Mydayplan_Activity.class);

                startActivity(I);
                break;

            case R.id.tourplan:

                I = new Intent(this, Tp_Month_Select.class);

                startActivity(I);
                break;


            default:
                break;

        }

    }
}
