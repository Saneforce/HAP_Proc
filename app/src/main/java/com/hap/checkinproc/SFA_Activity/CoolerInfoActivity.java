package com.hap.checkinproc.SFA_Activity;

import static com.hap.checkinproc.SFA_Activity.QPSActivity.qpsActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.Activity_Hap.AttachementActivity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.OnAttachmentDelete;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.QPS_Modal;
import com.hap.checkinproc.common.FileUploadService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoolerInfoActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvOrder, tvOtherBrand, tvQPS, tvPOP, tvRetailorName, tvReceivedDate;
    EditText etTagNo, etMake, etCoolerType, etRemarks;
    Common_Class common_class;
    CheckBox cbPurity, cbFrontage, cbNoWrk, cbAvail;
    Button btnSubmit;
    private DatePickerDialog fromDatePickerDialog;

    ImageView ivPurityCapture, ivPurityPreview, ivFTCapture, ivFTPreview, ivNowrkCapture, ivNoWrkPreview, ivToolbarHome;
    Gson gson;
    List<QPS_Modal> qpsModalList = new ArrayList<>();
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooler_info_layout);

        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        gson = new Gson();
        init();

        tvRetailorName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));
        findViewById(R.id.tvCoolerInfo).setVisibility(View.GONE);
        common_class.gotoHomeScreen(this, ivToolbarHome);


        cbPurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    findViewById(R.id.purityCaptureOption).setVisibility(View.VISIBLE);
                else {
                    clearFiles("purity~key");
                    findViewById(R.id.purityCaptureOption).setVisibility(View.GONE);
                }
            }
        });

        cbFrontage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    findViewById(R.id.frontageCaptureOption).setVisibility(View.VISIBLE);
                else {
                    clearFiles("FT~key");
                    findViewById(R.id.frontageCaptureOption).setVisibility(View.GONE);
                }
            }
        });

        cbNoWrk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    findViewById(R.id.llNoWrkOpt).setVisibility(View.VISIBLE);
                else {
                    clearFiles("noWrk~key");
                    findViewById(R.id.llNoWrkOpt).setVisibility(View.GONE);
                }
            }
        });

    }

    void showPic(String key) {
        AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
            @Override
            public void OnImageDelete(String Mode, int ImgCount) {

            }
        });

        Intent stat = new Intent(CoolerInfoActivity.this, AttachementActivity.class);
        stat.putExtra("qps_localData", key);
        startActivity(stat);
    }

    void addPic(String key) {
        getCurrentList();
        if (isCheckExceed(key)) {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {

                    qpsModalList.add(new QPS_Modal(fullPath, FileName, (key + System.currentTimeMillis())));
                    shared_common_pref.save(Constants.QPS_LOCALPICLIST, gson.toJson(qpsModalList));
                }
            });
            Intent intent = new Intent(CoolerInfoActivity.this, AllowancCapture.class);
            intent.putExtra("allowance", "TAClaim");
            startActivity(intent);
        } else {
            Toast.makeText(CoolerInfoActivity.this, "Limit Exceed...", Toast.LENGTH_SHORT).show();
        }

    }

    void getCurrentList() {
        qpsModalList.clear();
        if (shared_common_pref.getvalue(Constants.QPS_LOCALPICLIST).equals(""))
            qpsModalList = new ArrayList<>();
        else {
            String strQPS = shared_common_pref.getvalue(Constants.QPS_LOCALPICLIST);

            Type userType = new TypeToken<ArrayList<QPS_Modal>>() {
            }.getType();
            qpsModalList = gson.fromJson(strQPS, userType);
        }
    }

    boolean isCheckExceed(String key) {
        if (qpsModalList.size() == 0)
            return true;
        else {
            int count = 0;
            for (int i = 0; i < qpsModalList.size(); i++) {
                if (qpsModalList.get(i).getFileKey().contains(key)) {
                    count += 1;
                }
            }

            if (count < 3)
                return true;
            else
                return false;

        }
    }

    void init() {
        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvQPS = (TextView) findViewById(R.id.tvQPS);
        tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
        tvPOP = (TextView) findViewById(R.id.tvPOP);
        cbPurity = (CheckBox) findViewById(R.id.cbPurity);
        cbFrontage = (CheckBox) findViewById(R.id.cbFrontage);
        cbNoWrk = (CheckBox) findViewById(R.id.cbNoWorking);
        cbAvail = (CheckBox) findViewById(R.id.cbNoAvail);
        tvReceivedDate = (TextView) findViewById(R.id.tvReceivedDate);
        ivPurityCapture = (ImageView) findViewById(R.id.ivPurityCapture);
        ivPurityPreview = (ImageView) findViewById(R.id.ivPurityPreview);
        ivFTCapture = (ImageView) findViewById(R.id.ivFTCapture);
        ivFTPreview = (ImageView) findViewById(R.id.ivFTPreview);
        ivNowrkCapture = (ImageView) findViewById(R.id.ivNoWrkCapture);
        ivNoWrkPreview = (ImageView) findViewById(R.id.ivNoWrkPreview);
        tvRetailorName = findViewById(R.id.Category_Nametext);
        ivToolbarHome = findViewById(R.id.toolbar_home);
        btnSubmit = findViewById(R.id.btnSubmit);
        etTagNo = findViewById(R.id.etTagNo);
        etMake = findViewById(R.id.etMake);
        etCoolerType = findViewById(R.id.etCoolerType);
        etRemarks = findViewById(R.id.edt_remarks);

        tvOrder.setOnClickListener(this);
        tvOtherBrand.setOnClickListener(this);
        tvQPS.setOnClickListener(this);
        tvPOP.setOnClickListener(this);
        tvReceivedDate.setOnClickListener(this);

        ivPurityCapture.setOnClickListener(this);
        ivPurityPreview.setOnClickListener(this);
        ivFTCapture.setOnClickListener(this);
        ivFTPreview.setOnClickListener(this);
        ivNowrkCapture.setOnClickListener(this);
        ivNoWrkPreview.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            shared_common_pref.clear_pref(Constants.QPS_LOCALPICLIST);
            common_class.CommonIntentwithFinish(Invoice_History.class);
            return true;
        }
        return false;
    }

    void clearFiles(String key) {
        if (qpsModalList != null && qpsModalList.size() > 0) {
            List<QPS_Modal> filterList = new ArrayList<>();
            for (int i = 0; i < qpsModalList.size(); i++) {
                if (qpsModalList.get(i).getFileKey().contains(key)) {
                    File file = new File(qpsModalList.get(i).getFileName());
                    file.delete();
                } else {
                    filterList.add(qpsModalList.get(i));
                }
            }
            qpsModalList.clear();
            qpsModalList = filterList;
            shared_common_pref.save(Constants.QPS_LOCALPICLIST, gson.toJson(qpsModalList));
        }
    }

    @Override
    public void onClick(View v) {
        Common_Class common_class = new Common_Class(this);
        switch (v.getId()) {
            case R.id.tvOrder:
                common_class.commonDialog(this, Order_Category_Select.class, "Order?");
                break;
            case R.id.tvOtherBrand:
                common_class.commonDialog(this, OtherBrandActivity.class, "Other Brand?");
                break;
            case R.id.tvQPS:
                common_class.commonDialog(this, QPSActivity.class, "QPS?");
                break;
            case R.id.tvPOP:
                common_class.commonDialog(this, POPActivity.class, "POP?");
                break;
            case R.id.ivPurityCapture:
                addPic("purity~key");
                break;
            case R.id.ivFTCapture:
                addPic("FT~key");
                break;
            case R.id.ivPurityPreview:
                showPic("purity~key");
                break;
            case R.id.ivFTPreview:
                showPic("FT~key");
                break;
            case R.id.ivNoWrkCapture:
                addPic("noWrk~key");
                break;
            case R.id.ivNoWrkPreview:
                showPic("noWrk~key");
                break;
            case R.id.tvReceivedDate:
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(CoolerInfoActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;

                        tvReceivedDate.setText("" + dayOfMonth + "/" + month + "/" + year);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
                break;
            case R.id.btnSubmit:

                if (etTagNo.getText().toString().equals(""))
                    common_class.showMsg(this, "Enter Tag No");
                else if (etMake.getText().toString().equals("")) {
                    common_class.showMsg(this, "Enter Make");
                } else if (etCoolerType.getText().toString().equals("")) {
                    common_class.showMsg(this, "Enter Cooler Type");
                } else if (tvReceivedDate.getText().toString().equals("")) {
                    common_class.showMsg(this, "Enter Received Date");
                } else {
                    submitData();
                    uploadFile();
                }
                break;
        }

    }

    void uploadFile() {
        for (int i = 0; i < qpsModalList.size(); i++) {
            Intent mIntent = new Intent(this, FileUploadService.class);
            mIntent.putExtra("mFilePath", qpsModalList.get(i).getFilePath());
            mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
            mIntent.putExtra("FileName", qpsModalList.get(i).getFileName());
            mIntent.putExtra("Mode", "cooler");
            FileUploadService.enqueueWork(this, mIntent);
        }
    }

    void submitData() {

        JSONArray data = new JSONArray();
        JSONObject ActivityData = new JSONObject();
        try {
            JSONObject HeadItem = new JSONObject();
            HeadItem.put("divisionCode", Shared_Common_Pref.Div_Code);
            HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
            HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);

            HeadItem.put("distributorcode", Shared_Common_Pref.DistributorCode);

            HeadItem.put("date", Common_Class.GetDatewothouttime());

            HeadItem.put("tagNo", Shared_Common_Pref.Div_Code);
            HeadItem.put("make", Shared_Common_Pref.Sf_Code);
            HeadItem.put("coolerType", Shared_Common_Pref.OutletCode);

            HeadItem.put("recDate", Shared_Common_Pref.DistributorCode);
            HeadItem.put("cbPurity", cbPurity.isChecked());

            HeadItem.put("cbFrontage", cbFrontage.isChecked());
            HeadItem.put("cbNotAvailable", cbAvail.isChecked());
            HeadItem.put("cbNotWorking", cbNoWrk.isChecked());
            HeadItem.put("remarks", etRemarks.getText().toString());

            ActivityData.put("Cooler_Header", HeadItem);
            JSONArray Order_Details = new JSONArray();
            for (int z = 0; z < qpsModalList.size(); z++) {
                JSONObject ProdItem = new JSONObject();
                ProdItem.put("cooler_filename", qpsModalList.get(z).getFileName());
                Order_Details.put(ProdItem);

            }
            ActivityData.put("file_Details", Order_Details);
            data.put(ActivityData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> responseBodyCall = apiInterface.approveCIEntry(data.toString());
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                        String san = jsonObjects.getString("success");
                        Log.e("Success_Message", san);

                        if (jsonObjects.getBoolean("success")) {
                           common_class.showMsg(CoolerInfoActivity.this, jsonObjects.getString("Msg"));
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("SUBMIT_VALUE", "ERROR");
            }
        });

    }
}