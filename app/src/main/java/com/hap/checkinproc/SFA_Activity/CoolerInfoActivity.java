package com.hap.checkinproc.SFA_Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

import java.util.Calendar;

public class CoolerInfoActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvOrder, tvOtherBrand, tvQPS, tvPOP;
    Common_Class common_class;

    CheckBox cbPurity, cbFrontage;

    TextView tvReceivedDate;
    private DatePickerDialog fromDatePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooler_info_layout);

        common_class = new Common_Class(this);


        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvQPS = (TextView) findViewById(R.id.tvQPS);
        tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
        tvPOP = (TextView) findViewById(R.id.tvPOP);

        cbPurity = (CheckBox) findViewById(R.id.cbPurity);
        cbFrontage = (CheckBox) findViewById(R.id.cbFrontage);
        tvReceivedDate = (TextView) findViewById(R.id.tvReceivedDate);

        TextView tvRetailorName = findViewById(R.id.Category_Nametext);
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(this);

        tvRetailorName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));


        tvOrder.setOnClickListener(this);
        tvOtherBrand.setOnClickListener(this);
        tvQPS.setOnClickListener(this);
        tvPOP.setOnClickListener(this);

        findViewById(R.id.tvCoolerInfo).setVisibility(View.GONE);


        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);


        cbPurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    findViewById(R.id.purityCaptureOption).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.purityCaptureOption).setVisibility(View.GONE);
            }
        });

        cbFrontage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    findViewById(R.id.frontageCaptureOption).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.frontageCaptureOption).setVisibility(View.GONE);
            }
        });


        tvReceivedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(CoolerInfoActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        tvReceivedDate.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            common_class.CommonIntentwithFinish(Invoice_History.class);

            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Common_Class common_class = new Common_Class(this);
        switch (v.getId()) {
            case R.id.tvOrder:
                common_class.CommonIntentwithFinish(Order_Category_Select.class);
                break;
            case R.id.tvOtherBrand:
                common_class.CommonIntentwithFinish(OtherBrandActivity.class);
                break;
            case R.id.tvQPS:
                common_class.CommonIntentwithFinish(QPSActivity.class);
                break;
            case R.id.tvPOP:
                common_class.CommonIntentwithFinish(POPActivity.class);
                break;
        }
    }
}