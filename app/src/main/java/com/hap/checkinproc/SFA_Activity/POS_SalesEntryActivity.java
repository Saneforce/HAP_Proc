package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;
import com.hap.checkinproc.common.DatabaseHandler;

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

public class POS_SalesEntryActivity extends AppCompatActivity implements View.OnClickListener{

    List<Category_Universe_Modal> list=new ArrayList<>();

    RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    public static final String MyPREFERENCES = "MyPrefs";
    Type userType;
    Gson gson;
    DatabaseHandler db;
    final Handler handler = new Handler();
    public static final String UserDetail = "MyPrefs";
    SharedPreferences UserDetails;
    Button btnSubmit;
    ApiInterface apiInterface;

    com.hap.checkinproc.Activity_Hap.Common_Class DT = new com.hap.checkinproc.Activity_Hap.Common_Class();


    private DatePickerDialog fromDatePickerDialog;

    public static TextView tvStartDate, tvEndDate, currDate, totalExpense;
    public static String stDate = "", endDate = "";
    String date = "", SF_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_sales_entry);

        db = new DatabaseHandler(this);
        sharedCommonPref = new Shared_Common_Pref(POS_SalesEntryActivity.this);
        common_class = new Common_Class(this);


        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");

        ImageView ivToolbarHome = findViewById(R.id.posentry_toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);


        currDate=findViewById(R.id.tvDate);
        currDate.setText("" + DT.getDateWithFormat(new Date(), "dd-MMM-yyyy"));

        totalExpense = findViewById(R.id.totalExpense);
        btnSubmit = findViewById(R.id.btn_posentry_submit);
        recyclerView = findViewById(R.id.pos_entrysalesRecyclerview);

        tvStartDate = findViewById(R.id.tvPosEntryStartDate);
        tvEndDate = findViewById(R.id.tvPosEntryEndDate);

        stDate = Common_Class.GetDatewothouttime();
        endDate = Common_Class.GetDatewothouttime();
        tvStartDate.setText(stDate);
        tvEndDate.setText(endDate);

        GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void submitData() {

        JSONObject jObj = new JSONObject();


        try {

            jObj.put("SFCode",SF_code);
            jObj.put("currentDate",currDate.getText().toString());
            jObj.put("fromDate",stDate);
            jObj.put("toDate",endDate);
            jObj.put("totalExpense", totalExpense.getText().toString());

            for (int i = 0; i < list.size(); i++) {
                JSONObject obj1 = new JSONObject();
                obj1.put("productID", list.get(i).getId());
                obj1.put("productName",list.get(i).getName());
                obj1.put("productValue",list.get(i).getValue());
                jObj.accumulate("POSEntryData" , obj1);
            }
            Log.d("hjj","ghkj"+jObj.toString());

            apiInterface.JsonSave("save/posCounterSalesEntry", jObj.toString()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    Toast.makeText(POS_SalesEntryActivity.this,"POS Counter sales entry submitted Successfully",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void GetJsonData(String jsonResponse) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String name = jsonObject1.optString("pname");
                    String value = jsonObject1.optString("pvalue");
                    String id = jsonObject1.optString("pid");

                list.add(new Category_Universe_Modal(id,name, value ));
            }


                PosEntrySalesAdapter customAdapteravail = new PosEntrySalesAdapter(getApplicationContext(), list);
                recyclerView.setAdapter(customAdapteravail);
                customAdapteravail.notifyDataSetChanged();

        } catch (Exception e) {
        }
    }



    void showDatePickerDialog(int val) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(POS_SalesEntryActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), POS_SalesEntryActivity.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();

                        Log.v("sdatefd",stDate);
//                            common_class.getDb_310Data(Constants.VAN_STOCK, VanStockViewActivity.this);
                    } else
                        common_class.showMsg(POS_SalesEntryActivity.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, POS_SalesEntryActivity.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
                        Log.v("sdatefd",endDate);

//                            common_class.getDb_310Data(Constants.VAN_STOCK, VanStockViewActivity.this);

                    } else
                        common_class.showMsg(POS_SalesEntryActivity.this, "Please select valid date");

                }

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPosEntryStartDate:
                showDatePickerDialog(1);
                break;
            case R.id.tvPosEntryEndDate:
                showDatePickerDialog(2);
                break;

        }
    }


}