package com.hap.checkinproc.SFA_Activity;

import static com.hap.checkinproc.Common_Class.Common_Class.getDateYearMonthFormat;
import static com.hap.checkinproc.SFA_Activity.HAPApp.CurrencySymbol;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.Model_Class.VanInvTransactionModel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Adapter.VanInvTransactionAdapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VanTransactionActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface, UpdateResponseUI {
    TextView  tvRetailorName, tvRetailorPhone, retaileAddress;
    final Handler handler = new Handler();
    LinearLayout llCalMob;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    public static TextView tvStartDate, tvEndDate;
    private DatePickerDialog fromDatePickerDialog;
    List<VanInvTransactionModel> vanInvTransactionModelList = new ArrayList<>();
    Type userType;
    Gson gson;
    RecyclerView rvInvoiceView;
    TextView tv_no_data;
    VanInvTransactionAdapter vanInvTransactionAdapter;
    TextView tvOutStandAmt,tv_out_top,tv_out_label;
    View view1;
    LinearLayout ll_totout;
    NumberFormat formatter = new DecimalFormat("##0.00");
    String entryBy="";
    LinearLayout ll_retailer_det;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_van_transaction);
        init();
    }

    public void init(){
        tvRetailorName = findViewById(R.id.retailername);
        retaileAddress = findViewById(R.id.retaileAddress);
        tvRetailorPhone = findViewById(R.id.retailePhoneNum);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        llCalMob = findViewById(R.id.btnCallMob);
        rvInvoiceView=findViewById(R.id.rvInvoiceView);
        tv_no_data=findViewById(R.id.tv_no_data);
        tvOutStandAmt=findViewById(R.id.tvOutStandAmt);
        tv_out_top=findViewById(R.id.tv_out_top);
        tv_out_label=findViewById(R.id.tv_out_label);
        view1=findViewById(R.id.view1);
        ll_totout=findViewById(R.id.ll_totout);
        ll_retailer_det=findViewById(R.id.ll_retailer_det);


        llCalMob.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);

        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        gson = new Gson();
        Intent intent=getIntent();
        if(intent.hasExtra("EntryBy")){
            entryBy=intent.getStringExtra("EntryBy");
        }
        tvRetailorName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));
        if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.Retailor_PHNo)))
            llCalMob.setVisibility(View.GONE);
        else
            tvRetailorPhone.setText(shared_common_pref.getvalue(Constants.Retailor_PHNo));
        retaileAddress.setText(shared_common_pref.getvalue(Constants.Retailor_Address));

        tvStartDate.setText(Common_Class.GetDatewothouttimenew());
        tvEndDate.setText(Common_Class.GetDatewothouttimenew());
        ll_retailer_det.setVisibility(View.VISIBLE);
        if(entryBy.equalsIgnoreCase("CounterSale")) {
            common_class.getDataFromApi(Constants.CounterSale_Transaction_History, this, false);
            ll_retailer_det.setVisibility(View.GONE);
        }else if(Shared_Common_Pref.SFA_MENU.equalsIgnoreCase("VanSalesDashboardRoute")) {
            common_class.getDataFromApi(Constants.Van_Inv_Transaction_History, this, false);
        }else{
            common_class.getDataFromApi(Constants.Sec_Inv_Transaction_History, this, false);
        }
    }
    void showDatePickerDialog(int pos, TextView tv) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(VanTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String date = ("" + year + "-" + month + "-" + dayOfMonth);
                String datenew = ("" +  dayOfMonth+ "-" + month + "-" +year );

                if (common_class.checkDates(pos == 0 ? date : getDateYearMonthFormat(tvStartDate.getText().toString()), pos == 1 ? date : getDateYearMonthFormat(tvEndDate.getText().toString()), VanTransactionActivity.this)) {
                    tv.setText(datenew);

                    if(entryBy.equalsIgnoreCase("CounterSale")) {
                        common_class.getDataFromApi(Constants.CounterSale_Transaction_History, VanTransactionActivity.this, false);
                    }else if(Shared_Common_Pref.SFA_MENU.equalsIgnoreCase("VanSalesDashboardRoute")) {
                        common_class.getDataFromApi(Constants.Van_Inv_Transaction_History, VanTransactionActivity.this, false);
                    }else{
                        common_class.getDataFromApi(Constants.Sec_Inv_Transaction_History, VanTransactionActivity.this, false);
                    }



                } else {
                    common_class.showMsg(VanTransactionActivity.this, "Please select valid date");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvStartDate:
                showDatePickerDialog(0, tvStartDate);
                break;
            case R.id.tvEndDate:
                showDatePickerDialog(1, tvEndDate);
                break;
            case R.id.btnCallMob:
                common_class.showCalDialog(VanTransactionActivity.this, "Do you want to Call this Outlet?", tvRetailorPhone.getText().toString().replaceAll(",", ""));
                break;
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {

    }
    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {
                    case Constants.Van_Inv_Transaction_History:
                        setHistoryAdapter(apiDataResponse);
                        break;
                    case Constants.Sec_Inv_Transaction_History:
                        setHistoryAdapter(apiDataResponse);
                        break;
                        case Constants.CounterSale_Transaction_History:
                        setHistoryAdapter(apiDataResponse);
                        break;
                }
            }
        }catch (Exception e){

        }
    }
    private void setHistoryAdapter(String apiDataResponse) {
        vanInvTransactionModelList.clear();
        userType = new TypeToken<ArrayList<VanInvTransactionModel>>() {
        }.getType();
        vanInvTransactionModelList = gson.fromJson(apiDataResponse, userType);

        if(vanInvTransactionModelList.size()==0){
            tv_no_data.setVisibility(View.VISIBLE);
            rvInvoiceView.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            ll_totout.setVisibility(View.GONE);
            tv_out_label.setVisibility(View.GONE);
            tv_out_top.setVisibility(View.GONE);

        }else{
            tv_no_data.setVisibility(View.GONE);
            rvInvoiceView.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            ll_totout.setVisibility(View.VISIBLE);
            tv_out_label.setVisibility(View.VISIBLE);
            tv_out_top.setVisibility(View.VISIBLE);
            double invAmt = 0;
            double outAmt=0;
            for (int i=0 ;i<vanInvTransactionModelList.size();i++){
                invAmt+=vanInvTransactionModelList.get(i).getInvoiceVal();
                if(vanInvTransactionModelList.get(i).getTransactionList().size()>0){
                   for(int j=0;j<vanInvTransactionModelList.get(i).getTransactionList().size();j++){
                       outAmt+=vanInvTransactionModelList.get(i).getTransactionList().get(j).getRecAmt();
                   }
                }
            }

            tvOutStandAmt.setText(""+CurrencySymbol+" -"+formatter.format(invAmt-outAmt));
            tv_out_top.setText(""+CurrencySymbol+" -"+formatter.format(invAmt-outAmt));
        }


        vanInvTransactionAdapter = new VanInvTransactionAdapter(VanTransactionActivity.this, vanInvTransactionModelList);
        rvInvoiceView.setAdapter(vanInvTransactionAdapter);
    }
}
