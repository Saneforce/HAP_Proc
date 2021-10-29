package com.hap.checkinproc.SFA_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.DashboardInfoAdapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardInfoActivity extends AppCompatActivity implements View.OnClickListener,
        UpdateResponseUI {

    RecyclerView rvDashboard;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    TextView tvDistributor, tvHeaderName;
    private String sts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_info);
        init();
        tvDistributor.setText(shared_common_pref.getvalue(Constants.Distributor_name));
        if (sts == null)
            tvHeaderName.setText(getIntent().getStringExtra("type") + " Info");
        else
            tvHeaderName.setText("No order Info");


        common_class.getDb_310Data(Constants.SALES_SUMMARY, this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {

                JSONObject jsonObject = new JSONObject(apiDataResponse);
                Log.v("SALES_SUMMA:", apiDataResponse);

                switch (key) {
                    case Constants.SALES_SUMMARY:
                        if (jsonObject.getBoolean("success")) {
                            setAdapter(apiDataResponse);
                        }
                        break;


                }
            }

        } catch (Exception e) {

        }

    }

    void init() {
        tvHeaderName = findViewById(R.id.headtext);
        rvDashboard = findViewById(R.id.rvDashboard);
        tvDistributor = findViewById(R.id.tvDistributer);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        sts = getIntent().getStringExtra("status");
    }


    void setAdapter(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getBoolean("success")) {
                JSONArray jsonArr = jsonObject.getJSONArray("data");
                tvDistributor.setText(jsonArr.getJSONObject(0).getString("Sf_Name"));
                rvDashboard.setAdapter(new DashboardInfoAdapter(this, jsonArr, R.layout.db_type_info_adapter_layout));
            }

        } catch (Exception e) {
            Log.e("HistoryorderFrag:", e.getMessage());

        }
    }


}