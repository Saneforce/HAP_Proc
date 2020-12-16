package com.hap.checkinproc.Activity_Hap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Location;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;

public class Travel_Allowance extends AppCompatActivity {
    Spinner spinner3;
    DatePickerDialog picker;
    EditText eText;

    String Scode;
    String Dcode;

    List<Location> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel__allowance);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));



            }
        });

        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Scode = (shared.getString("Sfcode", "null"));
        Dcode = (shared.getString("Divcode", "null"));


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Location>> location = apiInterface.location("get/FieldForce_HQ", Dcode, Scode);
        location.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                locationList = response.body();
                Log.e("azxs", String.valueOf(locationList.size()));

                spinnerLoad();

            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {

            }
        });


        eText = (EditText) findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Travel_Allowance.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


    }

    private void spinnerLoad() {


        spinner3 = findViewById(R.id.spinner3);

        ArrayList<String> areaName = new ArrayList<>();

        for (int i = 0; i < locationList.size(); i++) {


            areaName.add(locationList.get(i).getName());


        }
        spinner3.setAdapter(new ArrayAdapter<>(Travel_Allowance.this, android.R.layout.simple_spinner_dropdown_item, areaName));
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sNumber = parent.getItemAtPosition(position).toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Travel_Allowance.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}