package com.hap.checkinproc.SFA_Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvRemainAmt, etDate,tvRetailorName;
    Button btnSubmit;
    EditText etRefNo, etAmtRec;
    CheckBox cbCash, cbUPI, cbOnlineTrans;
    private DatePickerDialog fromDatePickerDialog;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        common_class = new Common_Class(this);
        shared_common_pref=new Shared_Common_Pref(this);

        init();


        tvRetailorName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));
        cbCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    cbUPI.setChecked(false);
                    cbOnlineTrans.setChecked(false);
                }
            }
        });

        cbUPI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbCash.setChecked(false);
                    cbOnlineTrans.setChecked(false);
                }
            }
        });
        cbOnlineTrans.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbUPI.setChecked(false);
                    cbCash.setChecked(false);
                }
            }
        });

        etAmtRec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().equals("")) {
                        tvRemainAmt.setText("₹500.00");
                    } else {
                        int remainAmt = 500;
                        int val;
                        if (remainAmt < Integer.parseInt(s.toString())) {
                            val = 0;
                        } else
                            val = 500 - Integer.parseInt(s.toString());

                        tvRemainAmt.setText("₹" + val + ".00");
                    }
                } catch (Exception e) {
                    Log.e("paymentAct:etAmtRec ", e.getMessage());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    void init() {
        tvRemainAmt = findViewById(R.id.tvPayRemainAmt);
        btnSubmit = findViewById(R.id.btnPaySubmit);
        etDate = findViewById(R.id.etPayDate);
        etRefNo = findViewById(R.id.etPayRefNo);
        etAmtRec = findViewById(R.id.etPayRecAmt);
        cbCash = findViewById(R.id.cbPayCash);
        cbUPI = findViewById(R.id.cbPayUPI);
        cbOnlineTrans = findViewById(R.id.cbPayOnlineTrans);
        tvRetailorName=findViewById(R.id.retailername);

        btnSubmit.setOnClickListener(this);
        etDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPaySubmit:

                if ((!cbCash.isChecked() && !cbUPI.isChecked() && !cbOnlineTrans.isChecked())) {
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
}