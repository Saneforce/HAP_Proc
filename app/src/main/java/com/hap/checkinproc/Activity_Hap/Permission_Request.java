package com.hap.checkinproc.Activity_Hap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.AvalaibilityHours;
import com.hap.checkinproc.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Permission_Request extends AppCompatActivity {

    TimePickerDialog picker;
    EditText eText, eText2;
    String DateSelection;
    DatePickerDialog picker1;
    EditText eText1, takenHrs, hrsTwo, hrsThree, reasonPermission;
    private ArrayList<String> shitList;
    private SearchableSpinner permission_spinner;
    Date d1 = null;
    Date d2 = null;
    String clickedDate, fromTime = "", toTime = "", FTime, TTime, Clicked, TTTIme;
    Integer differnce;
    Button buttonSubmit;
    List<AvalaibilityHours> mAvalaibilityHours;
    Gson gson;
    String takenLeave = "";
    String ShiftName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission__request);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        takenHrs = (EditText) findViewById(R.id.hrs_one);
        hrsTwo = (EditText) findViewById(R.id.hrs_two);
        hrsThree = (EditText) findViewById(R.id.hrs_three);
        gson = new Gson();
        eText1 = (EditText) findViewById(R.id.permission_date);
        eText1.setInputType(InputType.TYPE_NULL);
        eText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker1 = new DatePickerDialog(Permission_Request.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                DateSelection = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                /*07/15/2016*/
                                clickedDate = "'" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "'";
                                Clicked = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                AvalaibilityHours();
                            }
                        }, year, month, day);
                picker1.show();
            }
        });

        eText = (EditText) findViewById(R.id.from_time);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR);
                int minutes = cldr.get(Calendar.MINUTE);
                int aa = cldr.get(Calendar.AM_PM);
                // time picker dialog
                picker = new TimePickerDialog(Permission_Request.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                eText.setText(sHour + ":" + sMinute);
                                fromTime(sHour, sMinute);
                                fromTime = String.valueOf(sHour);
                                FTime = sHour + ":" + sMinute;


                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        eText2 = (EditText) findViewById(R.id.to_time);
        eText2.setInputType(InputType.TYPE_NULL);
        eText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(Permission_Request.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                eText2.setText(sHour + ":" + sMinute);
                                toTime(sHour, sMinute);
                                toTime = String.valueOf(sHour);
                                TTime = "'" + sHour + ":" + sMinute + "'";
                                TTTIme = sHour + ":" + sMinute;
                                differ();
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        addingShiftToSpinner();
        permission_spinner = (SearchableSpinner) findViewById(R.id.permission_spinner);
        ShiftTypeAdapter(shitList);

        reasonPermission = (EditText) findViewById(R.id.reason_permission);


        buttonSubmit = (Button) findViewById(R.id.btn_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eText.getText().toString().matches("") && !eText1.getText().toString().matches("") && !eText2.getText().toString().matches("") && !reasonPermission.getText().toString().matches("") && !ShiftName.matches("")) {
                    PermissionRequestOne();
                } else if (eText1.getText().toString().matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter Date", Toast.LENGTH_SHORT).show();
                } else if (ShiftName.matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter Shite time", Toast.LENGTH_SHORT).show();
                } else if (eText.getText().toString().matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter From time", Toast.LENGTH_SHORT).show();
                } else if (eText2.getText().toString().matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter To Time", Toast.LENGTH_SHORT).show();
                } else if (reasonPermission.getText().toString().matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter Remarks  ", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void fromTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        fromTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        Log.e("TIME_WITH_AM_PM", fromTime);
    }

    private void toTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        toTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        Log.e("TIME_WITH_AM_PM", toTime);
    }


    private void differ() {


        Log.e("ClickedfromTime", fromTime);
        Log.e("ClickedtoTime", toTime);
        if (!fromTime.equals("") && !toTime.equals("")) {
            if (Integer.valueOf(fromTime) < Integer.valueOf(toTime)) {

                differnce = Integer.valueOf(toTime) - Integer.valueOf(fromTime);

                if (differnce > 2) {
                    hrsTwo.setText("2");
                    differnce = 2;

                } else {
                    hrsTwo.setText("" + differnce);

                }

            } else {
                Toast.makeText(this, "PLease choose higher than from time", Toast.LENGTH_SHORT).show();
                hrsTwo.setText("");
                eText2.setText("");
            }

        } else {
        }

    }


    private void ShiftTypeAdapter(ArrayList<String> arrayList) {
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Permission_Request.this, R.layout.spinner_search_item, R.id.text_item, arrayList);
        // Specify layout to be used when list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        permission_spinner.setAdapter(arrayAdapter);
        permission_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShiftName = parent.getItemAtPosition(position).toString();

                Log.e("HALF_TYPE", ShiftName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addingShiftToSpinner() {
        shitList = new ArrayList<>();
        shitList.add("Shift Time");
        shitList.add("08:30 to 10:30");
        shitList.add("10:00 to 11:30");
    }





    /*Checking availability with date*/

    public void AvalaibilityHours() {

        String datePermission = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.availabilityLeave(clickedDate, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, datePermission);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                Type userType1 = new TypeToken<ArrayList<AvalaibilityHours>>() {
                }.getType();
                mAvalaibilityHours = gson.fromJson(new Gson().toJson(response.body()), userType1);
                Log.e("REMAINING_LEAVES", String.valueOf(mAvalaibilityHours));
                for (int i = 0; i < mAvalaibilityHours.size(); i++) {
                    takenLeave = mAvalaibilityHours.get(i).getTknHrS();

                    Log.e("dsdsa", "" + takenLeave);
                    if (takenLeave != null) {
                        takenHrs.setText(takenLeave);

                    } else {
                        takenHrs.setText("0");
                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }



    /*Submit Hours*/

    public void PermissionRequestOne() {

        String hoursCount = "'" + String.valueOf(differnce) + "'";

        Log.e("ClieckedData", clickedDate);
        Log.e("ClieckedData", FTime);
        Log.e("ClieckedData", TTime);
        Log.e("ClieckedData", hoursCount);


        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("pdate", clickedDate);
            jsonleaveType.put("start_at", FTime);
            jsonleaveType.put("end_at", TTime);
            jsonleaveType.put("Noof_Count", hoursCount);
            jsonleaveType.put("Shift", "20");
            jsonleaveTypeS.put("PermissionFormValidate", jsonleaveType);
            jsonArray1.put(jsonleaveTypeS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String leaveCap1 = jsonArray1.toString();
        System.out.println("Activity_Event_Capturesaaa" + leaveCap1);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Name);
        Log.e("SF_Name", Shared_Common_Pref.Div_Code);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Code);
        Log.e("SF_Name", Shared_Common_Pref.StateCode);


        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.leaveSubmit(Shared_Common_Pref.Sf_Name, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "MGR", leaveCap1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObjecta = response.body();

                Log.e("TOTAL_REPOSNE_PER", String.valueOf(jsonObjecta));
                String Msg = String.valueOf(jsonObjecta.get("Msg"));
                String Psql = String.valueOf(jsonObjecta.get("SPSQl"));
                Msg = Msg.replace("\"", "");
                Log.e("SDFDFD", jsonObjecta.get("success").toString());
                Log.e("SDFDFDDFF", String.valueOf(Msg.length()));


                if (Msg.length() == 0) {
                    // Toast.makeText(Leave_Request.this, "NULL VALUE", Toast.LENGTH_SHORT).show();

                    PermissionRequestTwo();
                } else {
                    AlertDialogBox.showDialog(Permission_Request.this, "Confrimation", Msg, "OK", "", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }


    /*PermissionRequestTwo*/
    private void PermissionRequestTwo() {

        String hoursCount = String.valueOf(differnce);

        Log.e("ClieckedData", Clicked);
        Log.e("ClieckedData", FTime);
        Log.e("ClieckedData", TTTIme);
        Log.e("ClieckedData", hoursCount);


        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("pdate", Clicked);
            jsonleaveType.put("start_at", FTime);
            jsonleaveType.put("end_at", TTTIme);
            jsonleaveType.put("Reason", reasonPermission.getText().toString());
            jsonleaveType.put("No_of_Hrs", differnce);
            jsonleaveTypeS.put("PermissionEntry", jsonleaveType);
            jsonArray1.put(jsonleaveTypeS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String leaveCap1 = jsonArray1.toString();
        System.out.println("Activity_Event_Capturesaaa" + leaveCap1);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Name);
        Log.e("SF_Name", Shared_Common_Pref.Div_Code);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Code);
        Log.e("SF_Name", Shared_Common_Pref.StateCode);


        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.leaveSubmit(Shared_Common_Pref.Sf_Name, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, "MGR", leaveCap1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObjecta = response.body();

                Log.e("TOTAL_REPOSNE_PER", String.valueOf(jsonObjecta));
                String Msg = String.valueOf(jsonObjecta.get("Msg"));
                String Psql = String.valueOf(jsonObjecta.get("SPSQl"));
                Msg = Msg.replace("\"", "");
                Log.e("SDFDFD", jsonObjecta.get("success").toString());
                Log.e("SDFDFDDFF", String.valueOf(Msg.length()));

                startActivity(new Intent(Permission_Request.this, Dashboard.class));

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }
    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Permission_Request.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }


}