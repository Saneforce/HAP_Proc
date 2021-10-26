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
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.DashboardInfoAdapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardInfoActivity extends AppCompatActivity implements View.OnClickListener,
        UpdateResponseUI {

    RecyclerView rvDashboard;
    Shared_Common_Pref shared_common_pref;
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    Common_Class common_class;
    TextView tvDistributor, tvHeaderName;
    private String sts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_info);
        init();
        tvDistributor.setText(shared_common_pref.getvalue(Constants.Distributor_name));
        if(sts==null)
        tvHeaderName.setText(getIntent().getStringExtra("type") + " Info");
        else
            tvHeaderName.setText("No order Info");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {

                JSONObject jsonObject = new JSONObject(apiDataResponse);

                switch (key) {
                    case Constants.DASHBOARD_TYPE_INFO:
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
        common_class.getDb_310Data(Constants.DASHBOARD_TYPE_INFO, DashboardInfoActivity.this);
         sts = getIntent().getStringExtra("status");
    }


    void setAdapter(String response) {
        try {
            FilterOrderList.clear();
            JSONObject invoiceObj = new JSONObject(response);

            String type = getIntent().getStringExtra("type");
            if (type.equals("Orders") || type.equals("Invoice")) {

                JSONArray jsonArray = invoiceObj.getJSONArray(type);

                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int pm = 0; pm < jsonArray.length(); pm++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(pm);
                        List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();


                        if (!jsonObject1.getString("Status").equals("No Order")) {
                            JSONArray detailsArray = jsonObject1.getJSONArray("Details");


                            for (int da = 0; da < detailsArray.length(); da++) {

                                JSONObject daObj = detailsArray.getJSONObject(da);


                                product_details_modalArrayList.add(new Product_Details_Modal(daObj.getString("Product_Code"),
                                        daObj.getString("Product_Name"), "", Integer.parseInt(daObj.getString("Quantity")), ""));


                            }
                        }

                        if (type.equals("Invoice")) {
                            FilterOrderList.add(new OutletReport_View_Modal(0, jsonObject1.getString("InvoiceID"), "",
                                    jsonObject1.getString("ListedDr_Name"),
                                    jsonObject1.getString("Date"), (jsonObject1.getDouble("Order_Value")),
                                    jsonObject1.getString("Status"), product_details_modalArrayList));
                        } else if ((sts != null && sts.equals("No Order") && jsonObject1.getString("Status").equals("No Order")) || (sts == null && !jsonObject1.getString("Status").equals("No Order"))) {
                            FilterOrderList.add(new OutletReport_View_Modal(0, jsonObject1.getString("OrderID"), "",
                                    jsonObject1.getString("OutletName"),
                                    jsonObject1.getString("Date"), (jsonObject1.getDouble("Order_Value")),
                                    jsonObject1.getString("Status"), product_details_modalArrayList));

                        }
                    }


                }

            }


            rvDashboard.setAdapter(new DashboardInfoAdapter(this, FilterOrderList, R.layout.db_type_info_adapter_layout, 1, new AdapterOnClick() {
                @Override
                public void onIntentClick(int Name) {


                }
            }));

        } catch (Exception e) {
            Log.e("HistoryorderFrag:", e.getMessage());

        }
    }


}