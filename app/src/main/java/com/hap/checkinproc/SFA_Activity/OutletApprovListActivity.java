package com.hap.checkinproc.SFA_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.OutletApprovalAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class OutletApprovListActivity extends AppCompatActivity {
    RecyclerView rvApproval;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_outlet_approval_list);
            rvApproval = findViewById(R.id.rvOutletApprov);
            tvStatus=findViewById(R.id.tvOutletSta);

            JSONObject student1 = new JSONObject();

            student1.put("name", "haptest5");
            student1.put("des", "BDE");
            student1.put("ho", "Salem");
            student1.put("date", "2022-01-16");


            JSONObject student2 = new JSONObject();

            student2.put("name", "ciadmin");
            student2.put("des", "BDE");
            student2.put("ho", "Madurai");
            student2.put("date", "2022-01-17");

            JSONArray jsonArray = new JSONArray();

            jsonArray.put(student1);
            jsonArray.put(student2);

            OutletApprovalAdapter adapter = new OutletApprovalAdapter(this, jsonArray, R.layout.adapter_outlet_approval);
            rvApproval.setAdapter(adapter);

            tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overridePendingTransition(R.anim.in, R.anim.out);
                    startActivity(new Intent(getApplicationContext(),Outlet_Status_Activity.class));
                }
            });

        } catch (Exception e) {
            Log.v("OutletApprovalActivity:", e.getMessage());
        }

    }
}
