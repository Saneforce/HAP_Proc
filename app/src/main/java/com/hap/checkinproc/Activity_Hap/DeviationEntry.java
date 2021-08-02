package com.hap.checkinproc.Activity_Hap;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.DeviationEntryModel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviationEntry extends AppCompatActivity implements View.OnClickListener, Master_Interface, LocationListener {

    TextView DeviationTypeEntry;
    EditText chooseDate, remarks;
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    DatePickerDialog picker1;
    String minDate, minYear, minMonth, minDay, dateInput;
    String Currentlocation = "";
    Button DeviationSubmit;

    /*Deviation Entry*/
    Gson gson;
    List<DeviationEntryModel> deviationEntry;
    Type userType;
    Common_Model Model_Pojo;
    List<Common_Model> modelleaveType = new ArrayList<>();
    CustomListViewDialog customDialog;
    LocationManager locationManager;
    Shared_Common_Pref mShared_common_pref;
    Location location;
    String deivationID = "";
    LinearLayout LinearDevaitaionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviation_entry);
        getToolbar();
        gson = new Gson();
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        mShared_common_pref = new Shared_Common_Pref(this);
        DeviationTypeEntry = findViewById(R.id.deviation_type);
        chooseDate = (EditText) findViewById(R.id.choose_date);
        remarks = findViewById(R.id.remarks);
        DeviationSubmit = findViewById(R.id.deviation_submit);
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog.

                picker1 = new DatePickerDialog(DeviationEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                chooseDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                dateInput = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                calendarmin.set(Integer.parseInt(minYear), Integer.parseInt(minMonth) - 1, Integer.parseInt(minDay));
                picker1.getDatePicker().setMaxDate(cldr.getTimeInMillis());
                picker1.show();
            }

        });

        DeviationTypeEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelleaveType.clear();
                getDevetionType();
            }
        });

        LinearDevaitaionType = findViewById(R.id.lin_deviation_entry);
        LinearDevaitaionType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelleaveType.clear();
                getDevetionType();
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        try {
            LocationManager enabledManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (enabledManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = enabledManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
                } else {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
                    location = new Location(String.valueOf(locationManager));
                    onLocationChanged(location);
                }
            }


        } catch (SecurityException e) {
            e.printStackTrace();
        }


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        DeviationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviationEntrySubmit();
            }
        });
    }


    public void getToolbar() {
        MaxMinDate();
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });

        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
            }
        });

        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });


    }


    public void openHome() {
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");

        if (CheckIn == true) {
            Intent Dashboard = new Intent(DeviationEntry.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }


    public void MaxMinDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println("Current_DATE_FORMAT" + formatter.format(date));

        String strMinDate = formatter.format(date);
        minDate = strMinDate;
        /*Min Date*/
        String[] separated1 = minDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();

        minYear = separated1[0];
        minMonth = separated1[1];
        minDay = separated1[2];
        Log.e("Sresdfsd", minYear);
        Log.e("Sresdfsd", minMonth);
        Log.e("Sresdfsd", minDay);

    }


    /*Deviation entry type*/
    public void getDevetionType() {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        String commonLeaveType = "{\"tableName\":\"vwdeviationtype\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObjects(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                userType = new TypeToken<ArrayList<DeviationEntryModel>>() {
                }.getType();
                deviationEntry = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < deviationEntry.size(); i++) {

                    String id = String.valueOf(deviationEntry.get(i).getId());
                    String name = deviationEntry.get(i).getName();
                    Model_Pojo = new Common_Model(id, name, "flag");

                    Log.e("LeaveType_Request", id);
                    Log.e("LeaveType_Request", name);
                    modelleaveType.add(Model_Pojo);
                }

                customDialog = new CustomListViewDialog(DeviationEntry.this, modelleaveType, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }

    /*Submit Deviation Entry*/
    public void deviationEntrySubmit() {


        Date currentTime = Calendar.getInstance().getTime();

        Log.e("CurrentTime", new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()));

        JSONObject deviationObject = new JSONObject();
        JSONObject deviationArray = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            deviationObject.put("Deviation_Type", deivationID);
            deviationObject.put("From_Date", dateInput);
            deviationObject.put("reason", "'" + remarks.getText().toString() + "'");
            deviationObject.put("Time", "'" + new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()) + "'");
            deviationObject.put("LatLng", "'" + Currentlocation + "'");
            deviationArray.put("DeviationEntry", deviationObject);
            jsonArray1.put(deviationArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String Dentry = jsonArray1.toString();

        Log.d("Devaition_ENTRY_SUB", Dentry);


        Log.e("SF_Name", Shared_Common_Pref.Sf_Name);
        Log.e("SF_Name", Shared_Common_Pref.Div_Code);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Code);
        Log.e("SF_Name", Shared_Common_Pref.StateCode);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.deviationSave(Shared_Common_Pref.Sf_Name, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "MGR", Dentry);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObjecta = response.body();

                Log.e("SDFDFD", String.valueOf(jsonObjecta));
                String Msg = jsonObjecta.get("Msg").getAsString();
                Log.e("SDFDFDDFF", String.valueOf(Msg.length()));
                if (!Msg.equals("")) {
                    AlertDialogBox.showDialog(DeviationEntry.this, "Confrimation", Msg, "OK", "", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            openHome();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });


                } else {
                    openHome();
                    Toast.makeText(DeviationEntry.this, "Deviation Entry Successfully", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }


    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 8) {
            DeviationTypeEntry.setText(myDataset.get(position).getName());
            Log.e("DeviationId", myDataset.get(position).getId());
            deivationID = myDataset.get(position).getId();
        }
    }

    @Override
    public void onClick(View v) {

    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
              finish();
                }
            });

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.e("LOCATION_LISTNER", String.valueOf(location.getLatitude()));
        Log.e("LOCATION_LISTNER", String.valueOf(location.getLongitude()));

        Currentlocation = location.getLatitude() + "," + location.getLongitude();

    }
}