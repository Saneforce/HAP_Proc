package com.hap.checkinproc.SFA_Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.QPSAdapter;
import com.hap.checkinproc.SFA_Adapter.QPS_Modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QPSActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface {

    Button btnSubmit;
    TextView tvViewStatus;
    TextView tvOrder, tvOtherBrand, tvPOP, tvCoolerInfo;
    RecyclerView rvQps;

    QPSAdapter qpsAdapter;
    ArrayList<QPS_Modal> qpsModals = new ArrayList<>();

    Common_Class common_class;

    TextView etBookingDate;
    DatePickerDialog fromDatePickerDialog;

    TextView tvHapBrand, tvPeriod, tvGift, tvAvailble, tvTarget;
    ImageView ivEye;
    private CustomListViewDialog customDialog;

    EditText etNewOrder, etOtherBrand;
    TextView tvRetailorName;
    ImageView ivToolbarHome;

    private List<Common_Model> qpsComboList = new ArrayList<>();
    private String QPS_Code = "";
    Shared_Common_Pref shared_common_pref;
    public static QPSActivity qpsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qps);
        qpsActivity = this;

        init();

        tvRetailorName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));


        tvOrder.setOnClickListener(this);
        tvOtherBrand.setOnClickListener(this);
        tvPOP.setOnClickListener(this);
        tvCoolerInfo.setOnClickListener(this);
        ivEye.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvViewStatus.setOnClickListener(this);
        etBookingDate.setOnClickListener(this);


        findViewById(R.id.tvQPS).setVisibility(View.GONE);


        common_class.gotoHomeScreen(this, ivToolbarHome);

        etNewOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    float value, period;
                    if (s.toString().equals(""))
                        value = 0;
                    else
                        value = Float.parseFloat(s.toString());

                    if (tvPeriod.getText().toString().equals(""))
                        period = 0;
                    else
                        period = Float.parseFloat(tvPeriod.getText().toString());

                    tvAvailble.setText("" + value * period);

                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (common_class.isNetworkAvailable(this))
            getHapBrandFromAPI();
        else
            common_class.showMsg(this, "Please check your internet connection.");
    }


    private void submitQPSData() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                JSONObject HeadItem = new JSONObject();

                HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);
                HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
                HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                HeadItem.put("distributorCode", Shared_Common_Pref.DistributorCode);

                HeadItem.put("QPS_Code", QPS_Code);
                HeadItem.put("otherBrand", etOtherBrand.getText().toString());

                HeadItem.put("hapBrand", tvHapBrand.getText().toString());
                HeadItem.put("newOrder", etNewOrder.getText().toString());
                HeadItem.put("period", tvPeriod.getText().toString());
                HeadItem.put("gift", tvGift.getText().toString());
                HeadItem.put("acheive", tvAvailble.getText().toString());
                HeadItem.put("target", tvTarget.getText().toString());
                HeadItem.put("bookingDate", etBookingDate.getText().toString());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calobj = Calendar.getInstance();
                String dateTime = df.format(calobj.getTime());

                HeadItem.put("currentTime", dateTime);


                Call<ResponseBody> call = service.submitQPSData(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }

                                JSONObject jsonObject = new JSONObject(is.toString());

                                if (jsonObject.getBoolean("success")) {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("Msg"), Toast.LENGTH_SHORT).
                                            show();
                                    startActivity(new Intent(getApplicationContext(), Invoice_History.class));
                                    finish();
                                }

                            }

                        } catch (Exception e) {


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>", t.toString());


                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (tvViewStatus.getVisibility() == View.VISIBLE) {

                common_class.CommonIntentwithFinish(Invoice_History.class);
                shared_common_pref.clear_pref(Constants.QPS_LOCALPICLIST);
            } else {
                tvViewStatus.setVisibility(View.VISIBLE);
                findViewById(R.id.llQPSStatus).setVisibility(View.VISIBLE);
                findViewById(R.id.llQPSRequestStatus).setVisibility(View.GONE);

                btnSubmit.setVisibility(View.VISIBLE);
            }


            return true;
        }
        return false;
    }

    private void getHapBrandFromAPI() {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            JSONObject HeadItem = new JSONObject();

            HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);


            Call<ResponseBody> call = service.getHapBrand(HeadItem.toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        if (response.isSuccessful()) {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);
                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                                Log.v("Res>>", is.toString());
                            }

                            JSONObject jsonObject = new JSONObject(is.toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("Data");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                int val = jsonObject1.getInt("HapLtr");
                                tvHapBrand.setText("" + val);
                            }

                        }

                    } catch (Exception e) {


                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.v("fail>>", t.toString());


                }
            });
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }

    private void getdataFromAPI() {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            JSONObject HeadItem = new JSONObject();
            HeadItem.put("divisionCode", Shared_Common_Pref.Div_Code);
            HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
            HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);

            HeadItem.put("distributorcode", Shared_Common_Pref.DistributorCode);


            Call<ResponseBody> call = service.getQPSData(HeadItem.toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        if (response.isSuccessful()) {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);
                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                                Log.v("Res>>", is.toString());
                            }


                            JSONObject jsonObject = new JSONObject(is.toString());


                            if (jsonObject.getBoolean("success")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                qpsComboList.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    qpsComboList.add(new Common_Model(jsonObject1.getString("Days_Period")
                                            , jsonObject1.getInt("Total_Ltrs"), jsonObject1.getString("QPS_Name"), jsonObject1.getString("QPS_Code")));
                                }


                                customDialog = new CustomListViewDialog(QPSActivity.this, qpsComboList, 500);
                                Window windoww = customDialog.getWindow();
                                windoww.setGravity(Gravity.CENTER);
                                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                customDialog.show();
                            } else {
                                common_class.showMsg(QPSActivity.this, jsonObject.getString("Msg"));
                            }


                        }

                    } catch (Exception e) {

                        Log.v("fail>>1", e.getMessage());

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.v("fail>>2", t.toString());


                }
            });
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }


    public void getQPSStatus() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                JSONObject HeadItem = new JSONObject();
                HeadItem.put("retailerCode", Shared_Common_Pref.OutletCode);

                Call<ResponseBody> call = service.getQPSStatus(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }


                                JSONObject jsonObject = new JSONObject(is.toString());

                                qpsModals.clear();

                                if (jsonObject.getBoolean("success")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject arrObj = jsonArray.getJSONObject(i);


                                        JSONArray fileArray = arrObj.getJSONArray("Images");

                                        List<String> fileList = new ArrayList<>();

                                        if (fileArray != null && fileArray.length() > 0) {
                                            for (int fa = 0; fa < fileArray.length(); fa++) {
                                                if (!Common_Class.isNullOrEmpty(String.valueOf(fileArray.get(fa))))
                                                    fileList.add(String.valueOf(fileArray.get(fa)));
                                            }

                                            qpsModals.add(new QPS_Modal(arrObj.getString("SlNO"), arrObj.getString("Trans_sl_No"),
                                                    arrObj.getString("QPS_Name"), arrObj.getString("Booking_Date"), arrObj.getString("Duration"),
                                                    arrObj.getString("Received_Date"), arrObj.getString("Status"), fileList));
                                        }
                                    }

                                    tvViewStatus.setVisibility(View.GONE);
                                    findViewById(R.id.llQPSStatus).setVisibility(View.GONE);
                                    findViewById(R.id.llQPSRequestStatus).setVisibility(View.VISIBLE);

                                    // btnSubmit.setText("Completed");
                                    btnSubmit.setVisibility(View.GONE);


                                } else {

                                    qpsModals.clear();
                                    common_class.showMsg(QPSActivity.this, jsonObject.getString("Msg"));
                                }


                                qpsAdapter = new QPSAdapter(QPSActivity.this, qpsModals);
                                rvQps.setAdapter(qpsAdapter);


                            }

                        } catch (Exception e) {

                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>2", t.toString());


                    }
                });
            } else
                common_class.showMsg(this, "Please check your internet connection");
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }


    @Override
    public void onClick(View v) {
        Common_Class common_class = new Common_Class(this);
        switch (v.getId()) {
            case R.id.etQPSBookingDate:
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(QPSActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;

                        etBookingDate.setText("" + year + "-" + month + "-" + dayOfMonth);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
                break;
            case R.id.tvQPSViewStatus:

                getQPSStatus();
                break;
            case R.id.tvOrder:
                common_class.commonDialog(this,Order_Category_Select.class);
                break;
            case R.id.tvOtherBrand:
                common_class.commonDialog(this,OtherBrandActivity.class);
                break;
            case R.id.tvPOP:
                common_class.commonDialog(this,POPActivity.class);
                break;
            case R.id.tvCoolerInfo:
                common_class.commonDialog(this,CoolerInfoActivity.class);
                break;
            case R.id.ivQPSComboData:
                if (common_class.isNetworkAvailable(this)) {
                    getdataFromAPI();
                } else {
                    common_class.showMsg(this, "Please check your internet connection.");
                }
                break;
            case R.id.btnSubmit:
                if (isValiddata()) {

                    float acheive, target;
                    float hapBrand, newOrder;
                    float period = 0;

                    if (tvPeriod.getText().toString().equals(""))
                        period = 0;
                    else
                        period = Float.parseFloat(tvPeriod.getText().toString());

                    if (tvAvailble.getText().toString().equals(""))
                        acheive = 0;
                    else
                        acheive = Float.parseFloat(tvAvailble.getText().toString());
                    if (tvTarget.getText().toString().equals(""))
                        target = 0;
                    else
                        target = Float.parseFloat(tvTarget.getText().toString());

                    if (acheive >= target) {
                        submitQPSData();
                    } else {


//                        if (tvHapBrand.getText().toString().equals(""))
//                            hapBrand = 0;
//                        else
//                            hapBrand = Float.parseFloat(tvHapBrand.getText().toString());
                        if (etNewOrder.getText().toString().equals(""))
                            newOrder = 0;
                        else
                            newOrder = Float.parseFloat(etNewOrder.getText().toString());


                        float minVal = target / period;


                        // float expectVal = minVal - (newOrder);

                        Toast.makeText(getApplicationContext(), "Please given above " + minVal + " in New Order(ltr)", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private boolean isValiddata() {
        if (etOtherBrand.getText().toString().equals("")) {
            common_class.showMsg(this, "Enter Other Brand");
            return false;
        } else if (etNewOrder.getText().toString().equals("")) {
            common_class.showMsg(this, "Enter New Order");
            return false;
        } else if (tvPeriod.getText().toString().equals("") || tvGift.getText().toString().equals("") || tvTarget.getText().toString().equals("")) {
            common_class.showMsg(this, "Please choose any scheme from eye icon");
            return false;
        } else if (etBookingDate.getText().toString().equals("")) {
            common_class.showMsg(this, "Enter Booking Date");
            return false;
        }

        return true;
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {

        customDialog.dismiss();

        tvPeriod.setText("" + myDataset.get(position).getName());
        tvGift.setText("" + myDataset.get(position).getQPS_Name());
        tvTarget.setText("" + myDataset.get(position).getTotal_Ltrs());

        QPS_Code = myDataset.get(position).getQPS_Code();


    }


    void init() {
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);

        btnSubmit = findViewById(R.id.btnSubmit);
        tvViewStatus = findViewById(R.id.tvQPSViewStatus);
        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvPOP = (TextView) findViewById(R.id.tvPOP);
        tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
        tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
        rvQps = (RecyclerView) findViewById(R.id.rvQps);
        etBookingDate = (TextView) findViewById(R.id.etQPSBookingDate);
        tvHapBrand = (TextView) findViewById(R.id.tvQPSHapBrand);
        ivEye = (ImageView) findViewById(R.id.ivQPSComboData);

        tvPeriod = (TextView) findViewById(R.id.tvQPSPeriodDays);
        tvGift = (TextView) findViewById(R.id.tvQPSGift);
        tvTarget = (TextView) findViewById(R.id.tvQpsAcheive);
        tvAvailble = (TextView) findViewById(R.id.tvQpsCurrentAcheive);
        etNewOrder = (EditText) findViewById(R.id.etNewOrder);
        etOtherBrand = (EditText) findViewById(R.id.etQPSotherBrand);
        ivToolbarHome = findViewById(R.id.toolbar_home);
        tvRetailorName = findViewById(R.id.Category_Nametext);


    }
}
