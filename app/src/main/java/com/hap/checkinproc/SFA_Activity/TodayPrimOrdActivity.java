package com.hap.checkinproc.SFA_Activity;

import static com.hap.checkinproc.Common_Class.Constants.Rout_List;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.PrimaryOrder_History_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TodayPrimOrdActivity extends AppCompatActivity implements Master_Interface, View.OnClickListener, UpdateResponseUI {

    TextView tvStartDate, tvEndDate, distributor_text, route_text;
    Common_Class common_class;

    PrimaryOrder_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;
    public static TodayPrimOrdActivity mTdPriAct;
    public static String stDate = "", endDate = "";
    DatePickerDialog fromDatePickerDialog;
    String date = "";
    static SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    LinearLayout llDistributor, btnCmbRoute;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    Common_Model Model_Pojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_today_primorder_history);
            mTdPriAct = this;
            sharedCommonPref = new Shared_Common_Pref(TodayPrimOrdActivity.this);
            common_class = new Common_Class(this);

            tvStartDate = findViewById(R.id.tvStartDate);
            tvEndDate = findViewById(R.id.tvEndDate);
            invoicerecyclerview = (RecyclerView) findViewById(R.id.invoicerecyclerview);
            distributor_text = findViewById(R.id.distributor_text);
            llDistributor = findViewById(R.id.llDistributor);
            btnCmbRoute = findViewById(R.id.btnCmbRoute);
            route_text = findViewById(R.id.route_text);

            tvStartDate.setOnClickListener(this);
            tvEndDate.setOnClickListener(this);
            llDistributor.setOnClickListener(this);
            btnCmbRoute.setOnClickListener(this);

            stDate = Common_Class.GetDatewothouttime();
            endDate = Common_Class.GetDatewothouttime();
            tvStartDate.setText(stDate);
            tvEndDate.setText(endDate);

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, this, false);


            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                btnCmbRoute.setVisibility(View.GONE);
                findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
                distributor_text.setText("Hi! " + sharedCommonPref.getvalue(Constants.Distributor_name, ""));
            } else {
                if (!sharedCommonPref.getvalue(Constants.Distributor_Id).equals("")) {
                    common_class.getDb_310Data(Rout_List, this);
                    distributor_text.setText(/*"Hi! " +*/ sharedCommonPref.getvalue(Constants.Distributor_name, ""));
                } else {
                    btnCmbRoute.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {

        }

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 2:
                route_text.setText("");
                sharedCommonPref.save(Constants.Route_name, "");
                sharedCommonPref.save(Constants.Route_Id, "");
                // btnCmbRoute.setVisibility(View.VISIBLE);
                distributor_text.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Distributor_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Distributor_Id, myDataset.get(position).getId());
                sharedCommonPref.save(Constants.DistributorERP, myDataset.get(position).getCont());
                sharedCommonPref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
                sharedCommonPref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
                common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, TodayPrimOrdActivity.this, false);
                common_class.getDb_310Data(Rout_List, this);
                common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

                break;
            case 3:
                route_text.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
                common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                selectDate(1);
                break;
            case R.id.tvEndDate:
                selectDate(2);
                break;
            case R.id.btnCmbRoute:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    common_class.showCommonDialog(FRoute_Master, 3, this);
                }
                break;
            case R.id.llDistributor:
                common_class.showCommonDialog(common_class.getDistList(), 2, this);
                break;


        }
    }

    void selectDate(int val) {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(TodayPrimOrdActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (checkDates(date, tvEndDate.getText().toString()) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();
                        common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, TodayPrimOrdActivity.this, false);
                    } else
                        common_class.showMsg(TodayPrimOrdActivity.this, "Please select valid date");
                } else {
                    if (checkDates(tvStartDate.getText().toString(), date) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
                        common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, TodayPrimOrdActivity.this, false);

                    } else
                        common_class.showMsg(TodayPrimOrdActivity.this, "Please select valid date");

                }


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();


    }

    public boolean checkDates(String stDate, String endDate) {
        boolean b = false;
        try {

            Date date1 = dfDate.parse(stDate);
            Date date2 = dfDate.parse(endDate);
            long diff = date2.getTime() - date1.getTime();
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 90) {
                if (dfDate.parse(stDate).before(dfDate.parse(endDate))) {
                    b = true;//If start date is before end date
                } else if (dfDate.parse(stDate).equals(dfDate.parse(endDate))) {
                    b = true;//If two dates are equal
                } else {
                    b = false; //If start date is after the end date
                }

            } else {
                Toast.makeText(TodayPrimOrdActivity.this, "You can see only minimum 3 Months records", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    public void loadroute() {

        if (FRoute_Master.size() == 1) {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);
            route_text.setText(FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_name, FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_Id, FRoute_Master.get(0).getId());

        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {
                    case Rout_List:
                        JSONArray routeArr = new JSONArray(apiDataResponse);
                        FRoute_Master.clear();
                        for (int i = 0; i < routeArr.length(); i++) {
                            JSONObject jsonObject1 = routeArr.getJSONObject(i);
                            String id = String.valueOf(jsonObject1.optInt("id"));
                            String name = jsonObject1.optString("name");
                            String flag = jsonObject1.optString("FWFlg");
                            Model_Pojo = new Common_Model(id, name, flag);
                            Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                            FRoute_Master.add(Model_Pojo);

                        }
                        loadroute();
                        break;

                    case Constants.GetTodayPrimaryOrder_List:

                        JSONArray arr = new JSONArray(apiDataResponse);

                        mReportViewAdapter = new PrimaryOrder_History_Adapter(TodayPrimOrdActivity.this, arr, apiDataResponse, new AdapterOnClick() {
                            @Override
                            public void onIntentClick(int position) {
                                try {
                                    JSONObject obj = arr.getJSONObject(position);
                                    Shared_Common_Pref.TransSlNo = obj.getString("Trans_Sl_No");
                                    Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                                    sharedCommonPref.save(Constants.FLAG, "Primary Order");
                                    intent.putExtra("Order_Values", obj.getString("Order_Value"));
                                    intent.putExtra("Invoice_Values", obj.getString("invoicevalues"));
                                    //intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
                                    intent.putExtra("Invoice_Date", obj.getString("Order_Date"));
                                    intent.putExtra("NetAmount", obj.getString("NetAmount"));
                                    intent.putExtra("Discount_Amount", obj.getString("Discount_Amount"));
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.in, R.anim.out);
                                } catch (Exception e) {
                                    Log.v("primary:", e.getMessage());
                                }

                            }
                        });
                        invoicerecyclerview.setAdapter(mReportViewAdapter);

                        break;


                }

            }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE))
                finish();
            else
                common_class.CommonIntentwithFinish(FPPrimaryOrderActivity.class);
            overridePendingTransition(R.anim.in, R.anim.out);

            return true;
        }
        return false;
    }

    public void updateData(String orderNo, String cutoff_time) {
        try {
            if (Common_Class.isNullOrEmpty(cutoff_time)) {
                common_class.showMsg(this, "Time UP...");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date d1 = sdf.parse(Common_Class.GetTime());
                Date d2 = sdf.parse(cutoff_time);
                long elapsed = d2.getTime() - d1.getTime();
                if (elapsed >= 0) {
                    Intent intent = new Intent(this, PrimaryOrderActivity.class);
                    intent.putExtra(Constants.ORDER_ID, orderNo);
                    Shared_Common_Pref.TransSlNo = orderNo;
                    startActivity(intent);
                    overridePendingTransition(R.anim.in, R.anim.out);
                } else {
                    common_class.showMsg(this, "Time UP...");
                }
            }
        } catch (Exception e) {
            Log.v("TDPrimActivity:Edit:", e.getMessage());
        }

    }
}