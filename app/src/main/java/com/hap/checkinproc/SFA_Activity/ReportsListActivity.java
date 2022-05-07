package com.hap.checkinproc.SFA_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.ReportsLIstAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReportsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_reports_list);
            shared_common_pref = new Shared_Common_Pref(this);
            recyclerView = findViewById(R.id.rvReportsList);
            common_class = new Common_Class(this);

            ImageView backView = findViewById(R.id.imag_back);
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            JSONObject obj1 = new JSONObject();
            obj1.put("name", "Outlet Reports");
            obj1.put("des", "app");


            JSONObject obj2 = new JSONObject();
            obj2.put("name", "Primary Order");
            obj2.put("des", "web");

            JSONObject obj3 = new JSONObject();
            obj3.put("name", "Sales Summary");
            obj3.put("des", "web");
            JSONObject obj4 = new JSONObject();
            obj4.put("name", "New Outlet Follow up");
            obj4.put("des", "web");
            JSONObject obj5 = new JSONObject();
            obj5.put("name", "QPS Follow up");
            obj5.put("des", "web");


            JSONArray jsonArray = new JSONArray();
            jsonArray.put(obj1);
            jsonArray.put(obj2);
            jsonArray.put(obj3);
            jsonArray.put(obj4);
            jsonArray.put(obj5);


            recyclerView.setAdapter(new ReportsLIstAdapter(this, jsonArray, R.layout.adapter_report_list, new AdapterOnClick() {
                @Override
                public void onIntentClick(JSONObject obj, int position) {
                    try {
                        if (obj.getString("des").equalsIgnoreCase("App")) {
                            Intent intent = new Intent(getApplicationContext(), Reports_Outler_Name.class);
                            startActivity(intent);
                        } else {
//                            Intent intent = new Intent(getApplicationContext(), WebReportsActivity.class);
//                            startActivity(intent);
                        }
                    } catch (Exception e) {

                    }

                }
            }));
        } catch (Exception e) {

        }

    }

}