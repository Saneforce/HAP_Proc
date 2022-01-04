package com.hap.checkinproc.Status_Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Adapter.FlightBooking_Status_Adapter;
import com.hap.checkinproc.Status_Adapter.FlightBooking_TravelerDetail_Adapter;
import com.hap.checkinproc.Status_Model_Class.Leave_Status_Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FlightBooking_Status_Activity extends AppCompatActivity implements View.OnClickListener {
    List<Leave_Status_Model> mList = new ArrayList<>();

    RecyclerView rv;
    FlightBooking_Status_Adapter adapter;
    TextView tvFromDate, tvToDate;
    private DatePickerDialog fromDatePickerDialog;
    Common_Class common_class;
    public static FlightBooking_Status_Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flight_booking_status);
        init();
        mList.add(new Leave_Status_Model("APPROVED", "03-01-2022 11:50:20", "2", "Mrs.MAHALAKSHMI"));
        mList.add(new Leave_Status_Model("PENDING", "04-01-2022 10:40:00", "3", "Mr.RAJA"));
        mList.add(new Leave_Status_Model("CANCEL", "01-01-2022 09:27:15", "3", "Mrs.AMUTHA"));

        adapter = new FlightBooking_Status_Adapter(mList, this);
        rv.setAdapter(adapter);

    }

    void init() {
        activity = this;
        common_class = new Common_Class(this);
        tvFromDate = findViewById(R.id.tvFDate);
        tvToDate = findViewById(R.id.tvTDate);
        rv = findViewById(R.id.rvFightBookSta);

        tvToDate.setOnClickListener(this);
        tvFromDate.setOnClickListener(this);

        tvFromDate.setText(Common_Class.GetDatewothouttime());
        tvToDate.setText(Common_Class.GetDatewothouttime());

    }

    public void showTravelersDialog(int posl) {
        try {
            LayoutInflater inflater = LayoutInflater.from(FlightBooking_Status_Activity.this);

            final View view = inflater.inflate(R.layout.flight_travelers_header, null);
            AlertDialog alertDialog = new AlertDialog.Builder(FlightBooking_Status_Activity.this).create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ImageView ivClose = view.findViewById(R.id.ivClose);


            RecyclerView rv = view.findViewById(R.id.rvFlightTravelers);
            List<Leave_Status_Model> mTravList = new ArrayList<>();

            mTravList.add(new Leave_Status_Model("SAN", "LAKSHMI R", "9876543210", "HAP"));
            mTravList.add(new Leave_Status_Model("XYZ", "RAJESH P", "8907651234", "OTHER"));
            mTravList.add(new Leave_Status_Model("ABC", "MANI M", "7890654321", "OTHER"));

            rv.setAdapter(new FlightBooking_TravelerDetail_Adapter(mTravList, this));

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });


            alertDialog.setView(view);
            alertDialog.show();
        } catch (Exception e) {
            Log.e("OrderAdapter:dialog ", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFDate:
                showDatePickerDialog(0, tvFromDate);
                break;
            case R.id.tvTDate:
                showDatePickerDialog(1, tvToDate);
                break;
        }
    }

    void showDatePickerDialog(int pos, TextView tv) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(FlightBooking_Status_Activity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String date = ("" + year + "-" + month + "-" + dayOfMonth);

                if (common_class.checkDates(pos == 0 ? date : tvFromDate.getText().toString(), pos == 1 ? date : tvToDate.getText().toString(), FlightBooking_Status_Activity.this)) {
                    tv.setText(date);
                } else {
                    common_class.showMsg(FlightBooking_Status_Activity.this, "Please select valid date");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

}