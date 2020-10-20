package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hap.checkinproc.R;

public class Leave_Dashboard extends AppCompatActivity  {

    String[] mobileArray = {"Leave Request","Permission Request","Missed Punch","Weekly Off"};
    String[] mobileArray2 = {"Leave Status","Permission Status","On-duty Status","Missed Punch Status","weekly-off Status","extended shift Status"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_dashboard);

        ArrayAdapter adapter = new ArrayAdapter<String>(Leave_Dashboard.this,R.layout.layout_leave_type,R.id.tv_element, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                switch (position) {
                    case 0:
                        startActivity(new Intent(Leave_Dashboard.this, Leave_Request.class));break;
                    case 1:
                        startActivity(new Intent(Leave_Dashboard.this, Permission_Request.class));break;
                    case 2:
                        startActivity(new Intent(Leave_Dashboard.this, Missed_Punch.class));break;
                    case 3:
                        startActivity(new Intent(Leave_Dashboard.this, Weekly_Off.class));break;


                }
            }
        });

        ArrayAdapter adapter2 = new ArrayAdapter<String>(Leave_Dashboard.this,R.layout.layout_leave_type,R.id.tv_element, mobileArray2);

        ListView listView2 = (ListView) findViewById(R.id.mobile_list2);
        listView2.setAdapter(adapter2);

/*
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
        }*/
    }
}