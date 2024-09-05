package com.hap.checkinproc.SFA_Activity;

import static com.hap.checkinproc.SFA_Activity.HAPApp.CurrencySymbol;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Calendar;

public class VanSalPaymentActivity extends AppCompatActivity implements UpdateResponseUI, View.OnClickListener {

    Common_Class common_class;
   // TextView  tvLoadAmt, tvUnLoadAmt, tvTotVanSal,tvTotCollAmt,tvTotCredit;
   NumberFormat formatter = new DecimalFormat("##0.00");
    RecyclerView rvVanSales;
    private double invAmt,penAmt,recAmt,outstandAmt;
    private double totStkAmt;

    public static String stDate = "", endDate = "";
    LinearLayout ll_head,ll_total;
    TextView tv_nodata;
    public static  String date="";
    private DatePickerDialog fromDatePickerDialog;
    public static TextView tvDt;
    TextView tvTotInvVal,tvTotPenAmt,tvTotRecAmt,tvTotOutstand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_van_sal_payment);

        common_class = new Common_Class(this);

        //tvLoadAmt = findViewById(R.id.tvLoadStkAmt);
        //tvUnLoadAmt = findViewById(R.id.tvUnLoadStkAmt);
        tvDt = findViewById(R.id.tvVSPayDate);
        rvVanSales = findViewById(R.id.rvVanSal);
        //tvTotVanSal = findViewById(R.id.tvTotSal);
       // tvTotCollAmt = findViewById(R.id.tvTotCollAmt);
       // tvTotCredit=findViewById(R.id.tvTotCredit);
        ll_head=findViewById(R.id.ll_head);
        ll_total=findViewById(R.id.ll_total);
        tv_nodata=findViewById(R.id.tv_nodata);
        tvTotInvVal=findViewById(R.id.tvTotInvVal);
        tvTotRecAmt=findViewById(R.id.tvTotRecAmt);
        tvTotPenAmt=findViewById(R.id.tvTotPenAmt);
        tvTotOutstand=findViewById(R.id.tvTotOutstand);

        tvDt.setText("Date : " + Common_Class.GetDatemonthyearformat());
        tvDt.setOnClickListener(this);
        date=Common_Class.GetDatewothouttime();

        totStkAmt = getIntent().getDoubleExtra("stkLoadAmt",-1.00 );

        if (totStkAmt == -1) {
            common_class.getDb_310Data(Constants.VAN_STOCK_AMT, this);
        } else {
            //tvLoadAmt.setText(CurrencySymbol+" " + formatter.format(totStkAmt));

        }

        //common_class.getDataFromApi(Constants.VanSalOrderList, this, false);
        common_class.getDataFromApi(Constants.VanSalOrderListNew, this, false);

        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);


    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Constants.VanSalOrderList:
                    Log.v(key, apiDataResponse);
                    setHistoryAdapter(apiDataResponse);
                    break;
                case Constants.VanSalOrderListNew:
                    Log.v(key, apiDataResponse);
                    setHistoryAdapter(apiDataResponse);
                    break;
                case Constants.VAN_STOCK_AMT:
                    JSONObject stkObj = new JSONObject(apiDataResponse);
                    totStkAmt = 0;
                    if (stkObj.getBoolean("success")) {
                        JSONArray arr = stkObj.getJSONArray("Data");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            totStkAmt += obj.getDouble("CrAmt");

                        }
                    }

                    //tvLoadAmt.setText(CurrencySymbol+" " + formatter.format(totStkAmt));
                    //tvUnLoadAmt.setText(CurrencySymbol+" " + formatter.format(totStkAmt - salAmt));

                    break;

            }
        } catch (Exception e) {

        }
    }

    private void setHistoryAdapter(String apiDataResponse) {
        try {

            JSONArray arr = new JSONArray(apiDataResponse);

            if (!Common_Class.isNullOrEmpty(apiDataResponse) && !apiDataResponse.equalsIgnoreCase("[]")) {

                JSONArray filterArr = new JSONArray();
                invAmt=0;
                penAmt=0;
                recAmt=0;
                outstandAmt=0;

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    filterArr.put(obj);

                        invAmt += obj.getDouble("InvVal");
                        penAmt+= obj.getDouble("PenAmt");
                        recAmt+=obj.getDouble("RecAmt");
                        outstandAmt+=obj.getDouble("OutStand");


                }
                tvTotInvVal.setText(CurrencySymbol+" "+ formatter.format(invAmt));
                 tvTotPenAmt.setText(CurrencySymbol+" "+ formatter.format(penAmt));
                tvTotRecAmt.setText(CurrencySymbol+" "+ formatter.format(recAmt));
                tvTotOutstand.setText(CurrencySymbol+" " + formatter.format(outstandAmt));


                //tvTotVanSal.setText(CurrencySymbol+" "+ formatter.format(salAmt));
               // tvTotCollAmt.setText(CurrencySymbol+" "+ formatter.format(collectAmt));
                //tvTotCredit.setText(CurrencySymbol+" "+ formatter.format(creditAmt));
                //tvUnLoadAmt.setText(CurrencySymbol+" " + formatter.format(totStkAmt - salAmt));

                if(filterArr.length()>0){
                    tv_nodata.setVisibility(View.GONE);
                    ll_total.setVisibility(View.VISIBLE);
                    ll_head.setVisibility(View.VISIBLE);
                    rvVanSales.setVisibility(View.VISIBLE);
                    rvVanSales.setAdapter(new Pay_Adapter(filterArr, R.layout.adapter_vansales_pay, VanSalPaymentActivity.this));
                }else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    ll_total.setVisibility(View.GONE);
                    ll_head.setVisibility(View.GONE);
                    rvVanSales.setVisibility(View.GONE);
                }
               // rvVanSales.setAdapter(new Pay_Adapter(filterArr, R.layout.adapter_vansales_pay, VanSalPaymentActivity.this));
            }else {
                tv_nodata.setVisibility(View.VISIBLE);
                ll_total.setVisibility(View.GONE);
                ll_head.setVisibility(View.GONE);
                rvVanSales.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.v("adap:", e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvVSPayDate:
                showDatePickerDialog();
            break;
        }
    }
    void showDatePickerDialog() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(VanSalPaymentActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                date = ("" + year + "-" + month + "-" + dayOfMonth);
                String datenew=(""+dayOfMonth+"-"+month+"-"+year);
               tvDt.setText("Date : " +datenew);
              //  common_class.getDataFromApi(Constants.VanSalOrderList, VanSalPaymentActivity.this , false);
                common_class.getDataFromApi(Constants.VanSalOrderListNew, VanSalPaymentActivity.this , false);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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
        public Pay_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new Pay_Adapter.MyViewHolder(view);
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
        public void onBindViewHolder(Pay_Adapter.MyViewHolder holder, int position) {
            try {


                JSONObject obj = arr.getJSONObject(position);

                //holder.tvSNo.setText("" + (position + 1));
                holder.tvRetailerName.setText(""+(position + 1)+" "+obj.getString("CusName"));
                holder.tvInvNo.setText("" + obj.getString("InvNo"));
                holder.tvInvDate.setText(""+obj.getString("InvDate"));
                holder.tvInVal.setText("InvVal : "+CurrencySymbol+formatter.format(obj.getDouble("InvVal")));
                holder.tvPenAmt.setText("Pen.Amt : "+CurrencySymbol+formatter.format(obj.getDouble("PenAmt")));
                holder.tvRecAmt.setText("Rec.Amt : "+CurrencySymbol+formatter.format(obj.getDouble("RecAmt")));
                holder.tvPayMode.setText(""+obj.getString("PayMode"));
                holder.tvOutStand.setText("-"+formatter.format(obj.getDouble("OutStand")));


              /*  holder.tvAmt.setText(CurrencySymbol+" " + formatter.format(obj.getDouble("Order_Value")));
                holder.tvRetailerName.setText("" + obj.getString("Cus_Name"));
                holder.tvCollectedAmt.setText(CurrencySymbol+" " + formatter.format(obj.getDouble("Collect_Amt")));
                holder.tvCredit.setText(CurrencySymbol+" " + formatter.format(obj.getDouble("Order_Value") -obj.getDouble("Collect_Amt")));

                holder.tvPaytype.setText(""+obj.getString("Pay_Mode"));
                holder.tvRefNo.setText(""+obj.getString("Pay_Ref_no"));
                holder.tvBankName.setText(""+obj.getString("PaymentName"));*/


            } catch (Exception e) {
                Log.e("adapterProduct: ", e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return arr.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvSNo,tvRetailerName,tvInvNo,tvInvDate,tvInVal,tvPenAmt,tvPayMode,tvRecAmt,tvOutStand;
            //TextView tvPaytype,tvRefNo,tvBankName;


            public MyViewHolder(View view) {
                super(view);
                tvSNo = view.findViewById(R.id.tvSNo);
                tvInvNo = view.findViewById(R.id.tvInvNum);
                tvInvDate = view.findViewById(R.id.tvInvDate);
                tvRetailerName=view.findViewById(R.id.tvRetailerName);
                tvInVal=view.findViewById(R.id.tvInvValue);
                tvPenAmt=view.findViewById(R.id.tvPenAmt);
                tvPayMode=view.findViewById(R.id.tvPayMode);
                tvRecAmt=view.findViewById(R.id.tvRecAmt);
                tvOutStand=view.findViewById(R.id.tvOutStand);

            }
        }


    }

}