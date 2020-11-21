package com.hap.checkinproc.Activity_Hap;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.Leave_Type;
import com.hap.checkinproc.Model_Class.RemainingLeave;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.LeaveRemaining;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Leave_Request extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    DatePickerDialog picker;
    EditText eText;
    EditText etext2;
    EditText etext3;
    int daysBetween;
    Gson gson;
    List<Leave_Type> leavetypelist;
    Type userType;
    Button Submit;
    String leavetype_id = "";
    String fromData, toData;
    String daysDifferce;
    CheckBox mHalfCheck;
    private ArrayList<String> shitList, halfTypeList;
    LinearLayout mCheckHalf, mShitTiming, mHalfDayType;
    RecyclerView mRecycleLeaveRemaining;
    List<RemainingLeave> mRemainingLeaves;
    LeaveRemaining leaveRemaining;
    EditText reasonForLeave;
    String shiftTypeVal = "", halfTypeVal = "", halfChecked, getReason;
    Boolean oneTwo = false;

    TextView shitType,leaveType,halfType;
    CustomListViewDialog customDialog;
    List<Common_Model> modelShiftType = new ArrayList<>();
    List<Common_Model> modelleaveType = new ArrayList<>();
    List<Common_Model> modelhalfdayType = new ArrayList<>();
    Common_Model Model_Pojo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave__request);
        gson = new Gson();
         eText = (EditText) findViewById(R.id.from_date);
        eText.setInputType(InputType.TYPE_NULL);

        etext2 = (EditText) findViewById(R.id.to_date);
        etext2.setInputType(InputType.TYPE_NULL);

        Submit = (Button) findViewById(R.id.submitButton);

        Bundle params=getIntent().getExtras();
        if(params!=null){
            eText.setText(params.getString("EDt"));
            etext2.setText(params.getString("EDt"));

            String[] stDt=params.getString("EDt").split("/");
            fromData= stDt[2]+"-"+stDt[1]+"-"+stDt[0];
            toData= stDt[2]+"-"+stDt[1]+"-"+stDt[0];
            difference();
            //eText.setText(stDt[2]+"-"+stDt[1]+"-"+stDt[0]);
            eText.setEnabled(false);
            etext2.setEnabled(false);
        }

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        reasonForLeave = (EditText) findViewById(R.id.reason_leave);
        mCheckHalf = (LinearLayout) findViewById(R.id.check_half_linear);
        mShitTiming = (LinearLayout) findViewById(R.id.shit_linear);
        mHalfDayType = (LinearLayout) findViewById(R.id.half_day_linear);

        mHalfCheck = (CheckBox) findViewById(R.id.check_half);

        mRecycleLeaveRemaining = (RecyclerView) findViewById(R.id.leave_remaining);
        mRecycleLeaveRemaining.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycleLeaveRemaining.setLayoutManager(layoutManager);

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
                                difference();

                                fromData = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }, year, month, day);
                picker.show();
            }
        });

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
                                toData = "'" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "'";
                            }
                        }, year, month, day);
                picker.show();

            }
        });

        leaveReaming();
        // addingShiftToSpinner();
        shitType = findViewById(R.id.shift_timing);
        shitType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modelShiftType.clear();
                SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String Scode = (shared.getString("Sfcode", "null"));
                String Dcode = (shared.getString("Divcode", "null"));
                spinnerValue("get/Shift_timing", Dcode, Scode);


            }
        });

    /*adding spinner to leave type*/
        leaveType = findViewById(R.id.leave_type);
        leaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelleaveType.clear();
                leaveTypeMethod();
            }
        });


        /*adding spinner to half day*/
        addingHalfToSpinner();
        halfType = findViewById(R.id.half_day_type);
        halfType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelhalfdayType.clear();

                for (int i = 0; i < halfTypeList.size(); i++) {
                    String name = String.valueOf(halfTypeList.get(i));

                    Model_Pojo = new Common_Model("id", name, "flag");
                    Log.e("LeaveType_Request", "id");
                    Log.e("LeaveType_Request", name);

                    modelhalfdayType.add(Model_Pojo);
                }

                customDialog = new CustomListViewDialog(Leave_Request.this, modelhalfdayType, 6);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });




        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("halfType", leavetype_id);

                if (leaveType.getText().toString().matches("")) {
                    Toast.makeText(Leave_Request.this, "Enter Leave Type", Toast.LENGTH_SHORT).show();return;
                } else if (eText.getText().toString().matches("")) {
                    Toast.makeText(Leave_Request.this, "Enter From date", Toast.LENGTH_SHORT).show();return;
                } else if (etext2.getText().toString().matches("")) {
                    Toast.makeText(Leave_Request.this, "Enter To date", Toast.LENGTH_SHORT).show();return;
                } else if (reasonForLeave.getText().toString().matches((""))) {
                    Toast.makeText(Leave_Request.this, "Enter Reason", Toast.LENGTH_SHORT).show();return;
                }
                if (oneTwo == true) {
                    if (shitType.getText().toString().matches("")) {
                        Toast.makeText(Leave_Request.this, "Enter Shift Time", Toast.LENGTH_SHORT).show();return;
                    } else if (halfType.getText().toString().matches("")) {
                        Toast.makeText(Leave_Request.this, "Enter HalfDay Type", Toast.LENGTH_SHORT).show();return;
                    }
                }
                if (reasonForLeave.getText().toString().matches((""))) {
                    Toast.makeText(Leave_Request.this, "Enter Reason", Toast.LENGTH_SHORT).show();return;
                }
                LeaveSubmitOne();

            }
        });
    }



    public void addingHalfToSpinner() {
        halfTypeList = new ArrayList<>();
        halfTypeList.add("First Half");
        halfTypeList.add("Second Half");

    }

    public void difference() {

        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateBeforeString = "01/31/2014";
        String dateAfterString = "02/02/2014";
        etext3 = (EditText) findViewById(R.id.no_of_days);
        System.out.println("dateBeforeString " + dateBeforeString);
        System.out.println("dateBeforeString1 " + eText.getText().toString());
        System.out.println("dateAfterString " + dateAfterString);
        System.out.println("dateAfterString1 " + eText.getText().toString());
        try {
            Date dateBefore = myFormat.parse(eText.getText().toString());
            Date dateAfter = myFormat.parse(etext2.getText().toString());
            long difference = dateAfter.getTime() - dateBefore.getTime();
            /*  float daysBetween = (difference / (1000*60*60*24));*/
            daysBetween = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
            System.out.println("Number of Days between dates: " + (daysBetween + 1));
            etext3.setText("" + (daysBetween + 1));
            daysDifferce = String.valueOf((daysBetween + 1));
            Log.e("DIFFERNCE_DATE", daysDifferce);
            if (daysDifferce.equals("1")) {
                mCheckHalf.setVisibility(View.VISIBLE);
                mHalfCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (mHalfCheck.isChecked()) {
                            etext3.setText("" + 0.5);
                            halfChecked = "1";
                            mShitTiming.setVisibility(View.VISIBLE);
                            mHalfDayType.setVisibility(View.VISIBLE);

                            oneTwo = true;
                            Log.e("BOOLEAN_CHECK", String.valueOf(oneTwo));


                        } else {
                            halfChecked = "0";
                            etext3.setText("" + (daysBetween + 1));
                            mShitTiming.setVisibility(View.GONE);
                            mHalfDayType.setVisibility(View.GONE);
                            oneTwo = false;
                            Log.e("BOOLEAN_CHECK", String.valueOf(oneTwo));
                        }
                    }
                });

            } else {
                mCheckHalf.setVisibility(View.GONE);
                mShitTiming.setVisibility(View.GONE);
                mHalfDayType.setVisibility(View.GONE);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*Leave Type api call*/

    public void leaveTypeMethod() {

        String commonLeaveType = "{\"tableName\":\"vwLeaveType\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                userType = new TypeToken<ArrayList<Leave_Type>>() {
                }.getType();
                leavetypelist = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < leavetypelist.size(); i++) {
                    String id = String.valueOf(leavetypelist.get(i).getId());
                    String name = leavetypelist.get(i).getName();
                    Model_Pojo = new Common_Model(id, name, "flag");
                    Log.e("LeaveType_Request", id);
                    Log.e("LeaveType_Request", name);

                    modelleaveType.add(Model_Pojo);
                }

                customDialog = new CustomListViewDialog(Leave_Request.this, modelleaveType, 5);
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


    /*Submit api checking leave status*/

    public void LeaveSubmitOne() {

        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("Leave_Type", leavetype_id);
            jsonleaveType.put("From_Date", fromData);
            jsonleaveType.put("To_Date", toData);
            jsonleaveType.put("Shift", "0");
            jsonleaveType.put("PChk", 0);
            //  jsonleaveArrayType.put(jsonleaveType);
            jsonleaveTypeS.put("LeaveFormValidate", jsonleaveType);
            jsonArray1.put(jsonleaveTypeS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String leaveCap = "[{\"LeaveFormValidate\":{\"Leave_Type\":\"'9'\",\"From_Date\":\"2020-11-02\",\"To_Date\":\"'2020-11-05'\",\"Shift\":\"0\",\"PChk\":0}}]";
        String leaveCap = jsonArray1.toString();
        System.out.println("Activity_Event_Captures" + leaveCap);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Name);
        Log.e("SF_Name", Shared_Common_Pref.Div_Code);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Code);
        Log.e("SF_Name", Shared_Common_Pref.StateCode);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.leaveSubmit(Shared_Common_Pref.Sf_Name, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "MGR", leaveCap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObjecta = response.body();

                String Msg = String.valueOf(jsonObjecta.get("Msg"));
                Msg = Msg.replace("\"", "");
                Log.e("SDFDFD", String.valueOf(jsonObjecta));
                Log.e("SDFDFDDFF", String.valueOf(Msg.length()));


                if (Msg.length() == 0) {
                    // Toast.makeText(Leave_Request.this, "NULL VALUE", Toast.LENGTH_SHORT).show();

                    LeaveSubmitTwo();
                } else {
                    AlertDialogBox.showDialog(Leave_Request.this, "Confrimation", Msg, "OK", "", false, new AlertBox() {
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


    /*SUbmit Leave Request 2 */
    public void LeaveSubmitTwo() {
        getReason = "'" + reasonForLeave.getText().toString() + "'";

        String dateDiff = "" + daysDifferce + "";


        if (halfTypeVal == "First Half") {

            halfTypeVal = "1";
        } else if (halfTypeVal == "Second Half") {
            halfTypeVal = "2    ";
        } else {
            halfTypeVal = "3";
        }

        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("Leave_Type", leavetype_id);
            jsonleaveType.put("From_Date", fromData);
            jsonleaveType.put("To_Date", toData);
            jsonleaveType.put("Reason", getReason);
            jsonleaveType.put("address", "''");
            jsonleaveType.put("No_of_Days", dateDiff);
            jsonleaveType.put("HalfDay_Type", halfTypeVal);
            jsonleaveType.put("HalfDay", halfChecked);
            jsonleaveTypeS.put("LeaveForm", jsonleaveType);
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
                Log.e("TOTAL_REPOSNEaaa", String.valueOf(jsonObjecta));

                startActivity(new Intent(Leave_Request.this, Dashboard.class));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }


    /*Leave reamining*/
    public void leaveReaming() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.remainingLeave("2020", Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Type userType1 = new TypeToken<ArrayList<RemainingLeave>>() {
                }.getType();
                mRemainingLeaves = gson.fromJson(new Gson().toJson(response.body()), userType1);
                Log.e("REMAINING_LEAVES", String.valueOf(mRemainingLeaves));
                leaveRemaining = new LeaveRemaining(Leave_Request.this, mRemainingLeaves);
                mRecycleLeaveRemaining.setAdapter(leaveRemaining);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Leave_Request.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }


    private void spinnerValue(String a, String dc, String sc) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> shiftCall = apiInterface.shiftTime(a, dc, sc);
        shiftCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {


                String REPONSE = String.valueOf(response.body());


                //Log.e("ShiftTime_Leave_Request", REPONSE);
                try {
                    JSONArray jsonArray = new JSONArray(REPONSE);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.optString("id");
                        String name = jsonObject1.optString("name");
                        String flag = jsonObject1.optString("FWFlg");
                        Model_Pojo = new Common_Model(id, name, flag);
                        Log.e("ShiftTime_Leave_Request", id);
                        Log.e("ShiftTime_Leave_Request", name);
                        Log.e("ShiftTime_Leave_Request", flag);

                        modelShiftType.add(Model_Pojo);
                        Log.e("MODELSHIFTTYPE", String.valueOf(modelShiftType));


                    }
                    customDialog = new CustomListViewDialog(Leave_Request.this, modelShiftType, 4);
                    Window window = customDialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    customDialog.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if(type == 4) {
            shitType.setText(myDataset.get(position).getName());
        }else if(type == 5 ){
            leaveType.setText(myDataset.get(position).getName());
        }else if(type == 6){
            halfType.setText(myDataset.get(position).getName());
        }
    }
}