package com.hap.checkinproc.SFA_Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.PayLedger_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PayLedgerActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    public TextView tvOutletName, tvStartDate, tvEndDate;
    DatePickerDialog fromDatePickerDialog;

    RecyclerView rvLedger;
    PayLedger_Adapter plAdapter;
    List<Common_Model> common_modelList = new ArrayList<>();
    private Common_Class common_class;
    static SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    private String date;

    public static String ledgerFDT = "", ledgerTDT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_stmt);
        init();

        JsonObject jParam=new JsonObject();
        jParam.addProperty("FDate",ledgerFDT);
        jParam.addProperty("TDate",ledgerTDT);
        common_class.getDb_310Data(Constants.LEDGER, this,jParam);


        //plAdapter = new PayLedger_Adapter(this, common_modelList);
        //rvLedger.setAdapter(plAdapter);
    }

    public void init() {
        common_class = new Common_Class(this);
        rvLedger = findViewById(R.id.rvLedger);
        tvOutletName = findViewById(R.id.retailername);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);


        tvStartDate.setText(Common_Class.GetDatewothouttime());
        tvEndDate.setText(Common_Class.GetDatewothouttime());

        ledgerFDT = String.valueOf(tvStartDate.getText());
        ledgerTDT = String.valueOf(tvEndDate.getText());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                selectDate(1);
                break;
            case R.id.tvEndDate:
                selectDate(2);
                break;
        }
    }

    public boolean checkDates(String stDate, String endDate) {
        boolean b = false;
        try {

            Date date1 = dfDate.parse(stDate);
            Date date2 = dfDate.parse(endDate);
            long diff = date2.getTime() - date1.getTime();
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 90) {
                if (dfDate.parse(stDate).before(dfDate.parse(endDate))) {
                    b = true;//If start date is before end date
                } else if (dfDate.parse(stDate).equals(dfDate.parse(endDate))) {
                    b = true;//If two dates are equal
                } else {
                    b = false; //If start date is after the end date
                }

            } else {
                Toast.makeText(PayLedgerActivity.this, "You can see only minimum 3 Months records", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    void selectDate(int val) {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(PayLedgerActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (checkDates(date, tvEndDate.getText().toString()) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        ledgerFDT = tvStartDate.getText().toString();
                        JsonObject jParam=new JsonObject();
                        jParam.addProperty("FDate",ledgerFDT);
                        jParam.addProperty("TDate",ledgerTDT);
                        common_class.getDb_310Data(Constants.LEDGER, PayLedgerActivity
                                .this,jParam);
                    } else
                        common_class.showMsg(PayLedgerActivity.this, "Please select valid date");
                } else {
                    if (checkDates(tvStartDate.getText().toString(), date) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        ledgerTDT = tvEndDate.getText().toString();

                        JsonObject jParam=new JsonObject();
                        jParam.addProperty("FDate",ledgerFDT);
                        jParam.addProperty("TDate",ledgerTDT);
                        common_class.getDb_310Data(Constants.LEDGER, PayLedgerActivity.this,jParam);

                    } else
                        common_class.showMsg(PayLedgerActivity.this, "Please select valid date");

                }


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();


    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {
                    case Constants.LEDGER:
                        JSONArray legList= new JSONArray(apiDataResponse);
                        /*common_modelList.clear();
                        for(int il=0;il<legList.length();il++){
                            JSONObject lItm=legList.getJSONObject(il);
                            common_modelList.add(new Common_Model(lItm.getString("LedgDate"), lItm.getString("Debit"), lItm.getString("Credit"), lItm.getString("Balance")));

                        }*/
                        plAdapter = new PayLedger_Adapter(this, legList);
                        rvLedger.setAdapter(plAdapter);
                        break;
                }
            }

        } catch (Exception e) {

        }
    }
}
