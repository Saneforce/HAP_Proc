package com.hap.checkinproc.SFA_Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.QPSAdapter;
import com.hap.checkinproc.SFA_Adapter.QPS_Modal;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QPSActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit;
    TextView tvViewStatus;
    TextView tvOrder, tvOtherBrand, tvPOP, tvCoolerInfo;
    RecyclerView rvQps;

    QPSAdapter qpsAdapter;
    ArrayList<QPS_Modal> qpsModals = new ArrayList<>();

    Common_Class common_class;

    TextView etBookingDate;
    DatePickerDialog fromDatePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qps);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvViewStatus = findViewById(R.id.tvQPSViewStatus);
        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvPOP = (TextView) findViewById(R.id.tvPOP);
        tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
        tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
        rvQps = (RecyclerView) findViewById(R.id.rvQps);
        etBookingDate = (TextView) findViewById(R.id.etQPSBookingDate);


        common_class = new Common_Class(this);

        TextView tvRetailorName = findViewById(R.id.Category_Nametext);
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(this);

        tvRetailorName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));


        tvOrder.setOnClickListener(this);
        tvOtherBrand.setOnClickListener(this);
        tvPOP.setOnClickListener(this);
        tvCoolerInfo.setOnClickListener(this);


        tvViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvViewStatus.setVisibility(View.GONE);
                findViewById(R.id.llQPSStatus).setVisibility(View.GONE);
                findViewById(R.id.llQPSRequestStatus).setVisibility(View.VISIBLE);

                btnSubmit.setText("Completed");

            }
        });


        findViewById(R.id.tvQPS).setVisibility(View.GONE);
        qpsModals.add(new QPS_Modal("233", "236763", "Cooker", "30.8.2021", "-1 day", "10.9.2021"));
        qpsModals.add(new QPS_Modal("234", "236745", "Mobile", "25.8.2021", "-5 days", "10.9.2021"));

        qpsModals.add(new QPS_Modal("235", "236789", "Bag", "28.8.2021", "-3 days", "10.9.2021"));
        qpsAdapter = new QPSAdapter(this, qpsModals);
        rvQps.setAdapter(qpsAdapter);

        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);


        etBookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(QPSActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        etBookingDate.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
            }
        });


        getdataFromAPI();
    }

    private void getdataFromAPI() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JSONArray> call = service.getQPSData(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.OutletCode, Shared_Common_Pref.DistributorCode);

        call.enqueue(new Callback<JSONArray>() {
            @Override
            public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {


            }

            @Override
            public void onFailure(Call<JSONArray> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Common_Class common_class = new Common_Class(this);
        switch (v.getId()) {
            case R.id.tvOrder:
                common_class.CommonIntentwithFinish(Order_Category_Select.class);
                break;
            case R.id.tvOtherBrand:
                common_class.CommonIntentwithFinish(OtherBrandActivity.class);
                break;
            case R.id.tvPOP:
                common_class.CommonIntentwithFinish(POPActivity.class);
                break;
            case R.id.tvCoolerInfo:
                common_class.CommonIntentwithFinish(CoolerInfoActivity.class);
                break;
        }
    }
}
