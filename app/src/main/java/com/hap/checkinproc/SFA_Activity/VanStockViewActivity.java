package com.hap.checkinproc.SFA_Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class VanStockViewActivity extends AppCompatActivity implements UpdateResponseUI {

    Common_Class common_class;
    TextView tvDt, tvLoadAmt, tvUnLoadAmt, tvTotVanSalQty, tvTotStkQty;
    NumberFormat formatter = new DecimalFormat("##0.00");
    RecyclerView rvVanSales;
    private double salAmt;
    private int totStk, totSal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_van_stockview);

        common_class = new Common_Class(this);

        tvLoadAmt = findViewById(R.id.tvLoadStkAmt);
        tvUnLoadAmt = findViewById(R.id.tvUnLoadStkAmt);
        tvDt = findViewById(R.id.tvVSPayDate);
        rvVanSales = findViewById(R.id.rvVanSal);
        tvTotVanSalQty = findViewById(R.id.tvTotSalQty);
        tvTotStkQty = findViewById(R.id.tvTotLoadQty);


        tvDt.setText("Date : " + Common_Class.GetDatemonthyearformat());
        tvLoadAmt.setText("₹" + formatter.format(getIntent().getDoubleExtra("stkLoadAmt", 0)));
        common_class.getDb_310Data(Constants.VAN_STOCK, this);

        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);

    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Constants.VAN_STOCK:
                    Log.v(key, apiDataResponse);
                    setHistoryAdapter(apiDataResponse);

                    break;

            }
        } catch (Exception e) {

        }
    }

    private void setHistoryAdapter(String apiDataResponse) {
        try {

            JSONObject stkObj = new JSONObject(apiDataResponse);
            salAmt = 0;
            totStk = 0;
            totSal = 0;
            JSONArray arr = stkObj.getJSONArray("Data");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                totStk += obj.getInt("Cr");
                totSal += obj.getInt("Dr");

            }
            tvTotVanSalQty.setText("" + totSal);
            tvTotStkQty.setText("" + totStk);


          //  tvUnLoadAmt.setText("₹" + formatter.format(getIntent().getDoubleExtra("stkLoadAmt", 0) - salAmt));
            rvVanSales.setAdapter(new Pay_Adapter(arr, R.layout.adapter_vansales_stockview, VanStockViewActivity.this));

        } catch (Exception e) {
            Log.v("adap:", e.getMessage());
        }

    }

    public class Pay_Adapter extends RecyclerView.Adapter<Pay_Adapter.MyViewHolder> {
        Context context;
        private JSONArray arr;
        private int rowLayout;


        public Pay_Adapter(JSONArray arr, int rowLayout, Context context) {
            this.arr = arr;
            this.rowLayout = rowLayout;
            this.context = context;


        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {


                JSONObject obj = arr.getJSONObject(position);

                try {
                    holder.tvSku.setText("" + (obj.getString("PName")));
                } catch (Exception e) {

                }

                holder.tvSalQty.setText(String.valueOf("" + obj.getInt("Dr")).toString().trim());

                holder.tvStkQty.setText("" + (obj.getInt("Cr")));


            } catch (Exception e) {
                Log.e("adapterProduct: ", e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return arr.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvSku, tvStkQty, tvSalQty;


            public MyViewHolder(View view) {
                super(view);
                tvSku = view.findViewById(R.id.tvSku);
                tvStkQty = view.findViewById(R.id.tvStockQty);
                tvSalQty = view.findViewById(R.id.tvSalQty);

            }
        }


    }

}