package com.hap.checkinproc.Activity_Hap;

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
import android.widget.Toast;

import com.hap.checkinproc.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Leave_Request extends AppCompatActivity {

    private EditText leavetype;
    private ListPopupWindow statusPopupList;
    //date
    DatePickerDialog picker;
    EditText eText;
    EditText etext2;
    EditText etext3;
    int daysBetween;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave__request);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        leavetype = findViewById(R.id.leave_type);
        setPopupList();
        //we need to show the list when clicking on the field
        //difference();
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
                                difference();
                                Toast.makeText(Leave_Request.this, "hii", Toast.LENGTH_SHORT).show();


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

    public void difference()  {

        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateBeforeString = "01/31/2014";
        String dateAfterString = "02/02/2014";
        etext3=(EditText) findViewById(R.id.no_of_days);
        System.out.println("dateBeforeString "+dateBeforeString);
        System.out.println("dateBeforeString1 "+eText.getText().toString());
        System.out.println("dateAfterString "+dateAfterString);
        System.out.println("dateAfterString1 "+eText.getText().toString());
        try {
            Date dateBefore = myFormat.parse(eText.getText().toString());
            Date dateAfter = myFormat.parse(etext2.getText().toString());
            long difference = dateAfter.getTime() - dateBefore.getTime();
          /*  float daysBetween = (difference / (1000*60*60*24));*/

            daysBetween = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);

            System.out.println("Number of Days between dates: "+(daysBetween+1));

          etext3.setText(""+(daysBetween+1));
         // etext3.setText(String.valueOf(daysBetween+1));


        } catch (Exception e) {
            e.printStackTrace();
        }
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