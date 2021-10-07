package com.hap.checkinproc.SFA_Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.PayModeAdapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {

    TextView tvRemainAmt, etDate, tvRetailorName, tvOutStandAmt;
    Button btnSubmit;
    EditText etRefNo, etAmtRec;
    private DatePickerDialog fromDatePickerDialog;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    RecyclerView rvPayMode;
    private List<Common_Model> payList = new ArrayList<>();
    PayModeAdapter payModeAdapter;
    NumberFormat formatter = new DecimalFormat("##0.00");

    Double outstandAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);

        init();


        tvRetailorName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));

        etAmtRec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().equals("")) {
                        tvRemainAmt.setText("₹" + outstandAmt);
                    } else {
                        double remainAmt = outstandAmt;
                        double val;
                        if (remainAmt < Integer.parseInt(s.toString())) {
                            val = 0;
                        } else
                            val = Double.parseDouble(formatter.format(outstandAmt - Double.parseDouble(s.toString())));

                        tvRemainAmt.setText("₹" + val);
                    }
                } catch (Exception e) {
                    Log.e("paymentAct:etAmtRec ", e.getMessage());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        common_class.getDb_310Data(Constants.OUTSTANDING, this);
        common_class.getDb_310Data(Constants.PAYMODES, this);

    }


    void init() {
        tvRemainAmt = findViewById(R.id.tvPayRemainAmt);
        btnSubmit = findViewById(R.id.btnPaySubmit);
        etDate = findViewById(R.id.etPayDate);
        etRefNo = findViewById(R.id.etPayRefNo);
        etAmtRec = findViewById(R.id.etPayRecAmt);
        tvRetailorName = findViewById(R.id.retailername);
        rvPayMode = findViewById(R.id.rvPayMode);
        tvOutStandAmt = findViewById(R.id.tvPayOutStandAmt);

        btnSubmit.setOnClickListener(this);
        etDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPaySubmit:

                if (!payModeAdapter.isModeSelected()) {
                    common_class.showMsg(this, "Please select any Payment Mode");
                } else if (etDate.getText().toString().equals("")) {
                    common_class.showMsg(this, "Please enter the Date of payment");
                } else if (etAmtRec.getText().toString().equals("")) {
                    common_class.showMsg(this, "Please enter the Amount Received");
                } else {
                    common_class.showMsg(this, "Submitted Successfully");
                    finish();
                }

                break;

            case R.id.etPayDate:
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(PaymentActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;

                        etDate.setText("" + dayOfMonth + "/" + month + "/" + year);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();


                break;


        }
    }

    @Override
    public void onLoadFilterData(List<Retailer_Modal_List> retailer_modal_list) {

    }

    @Override
    public void onLoadTodayOrderList(List<OutletReport_View_Modal> outletReportViewModals) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            JSONObject jsonObject = new JSONObject(apiDataResponse);


            switch (key) {
                case Constants.OUTSTANDING:


                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            outstandAmt = Double.valueOf(formatter.format(jsonArray.getJSONObject(i).getDouble("Outstanding")));
                            tvOutStandAmt.setText("₹" + outstandAmt);
                        }

                    } else {
                        outstandAmt = 0.00;
                        tvOutStandAmt.setText("₹" + 0.00);
                    }
                    break;
                case Constants.PAYMODES:

                    payList.clear();
                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataObj = jsonArray.getJSONObject(i);
                            payList.add(new Common_Model(dataObj.getString("Name"), dataObj.getString("Code")));
                        }

                    }

                    payModeAdapter = new PayModeAdapter(payList, R.layout.adapter_paymode_layout, PaymentActivity.this);
                    rvPayMode.setAdapter(payModeAdapter);

                    break;

            }
        } catch (Exception e) {

        }
    }
}