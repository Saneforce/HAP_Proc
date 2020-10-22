package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hap.checkinproc.R;

public class Approvals extends AppCompatActivity  {

  //  String[] mobileArray = {"Leave Request","Permission Request","Missed Punch","Weekly Off"};
    String[] mobileArray2 = {"Leave","Permission","On-duty","Missed Punch","Extended Shift","Travel Allowance","Tour plan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvals);

       /* ArrayAdapter adapter = new ArrayAdapter<String>(Approvals.this,R.layout.layout_leave_type,R.id.tv_element, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
*/
        ArrayAdapter adapter2 = new ArrayAdapter<String>(Approvals.this,R.layout.layout_leave_type,R.id.tv_element, mobileArray2);

        ListView listView2 = (ListView) findViewById(R.id.mobile_list2);
        listView2.setAdapter(adapter2);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                switch (position) {
                    case 0:
                        startActivity(new Intent(Approvals.this, Leave_Approval.class));break;
                  /*  case 1:
                        startActivity(new Intent(Leave_Dashboard.this, Permission_Request.class));break;
                    case 2:
                        startActivity(new Intent(Leave_Dashboard.this, Missed_Punch.class));break;
                    case 3:
                        startActivity(new Intent(Leave_Dashboard.this, Weekly_Off.class));break;
*/

                }
            }
        });



    }
}