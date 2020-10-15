package com.hap.checkinproc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Leave_Request extends AppCompatActivity {

    private EditText leavetype;
    private ListPopupWindow statusPopupList;
    //date
    DatePickerDialog picker;
    EditText eText;
    EditText etext2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave__request);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        leavetype = findViewById(R.id.leave_type);
        setPopupList();
        //we need to show the list when clicking on the field

        setListeners();


        eText=(EditText) findViewById(R.id.from_date);
        eText.setInputType(InputType.TYPE_NULL);

        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Leave_Request.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        etext2=(EditText) findViewById(R.id.to_date);
        etext2.setInputType(InputType.TYPE_NULL);
        etext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Leave_Request.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etext2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }


    private void setListeners() {
        leavetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                statusPopupList.show();
            }
        });
    }

    private void setPopupList() {

        final List<String> status = new ArrayList<>();
        status.add("leave without pay");
        status.add("casual leave");
        status.add("corona symptoms");
        status.add("self quarantine");

        statusPopupList = new ListPopupWindow(Leave_Request.this);
        ArrayAdapter adapter = new ArrayAdapter<>(Leave_Request.this, R.layout.layout_leave_type, R.id.tv_element, status);
        statusPopupList.setAnchorView(leavetype); //this let as set the popup below the EditText
        statusPopupList.setAdapter(adapter);
        statusPopupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leavetype.setText(status.get(position));//we set the selected element in the EditText
                statusPopupList.dismiss();
            }
        });
    }
}