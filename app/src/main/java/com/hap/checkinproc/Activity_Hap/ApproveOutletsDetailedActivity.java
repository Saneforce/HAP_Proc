package com.hap.checkinproc.Activity_Hap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.ApproveOutletsActivity;
import com.hap.checkinproc.SFA_Adapter.AdapterOutletApprovalCategory;
import com.hap.checkinproc.SFA_Adapter.AdapterOutletApprovalFreezer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Created by RAGU on 27/01/2023
public class ApproveOutletsDetailedActivity extends AppCompatActivity {
    LinearLayout name_ll, outletType_ll, gst_ll, deliveryType_ll, categoryType_ll, freezer_ll;
    RecyclerView categoryTypeRV, freezerRV;
    Button approveBtn, rejectBtn;
    TextView outletName, outletNameNew, outletType, outletTypeNew, gst, gstNew, deliveryType, deliveryTypeNew;

    String OUTLET_ID = "", UPDATED_BY = "", UPDATED_ON = "", OUTLET_NAME = "", OUTLET_NAME_NEW = "", OUTLET_TYPE = "", OUTLET_TYPE_NEW = "", GST = "", GST_NEW = "", DELIVERY_TYPE = "", DELIVERY_TYPE_NEW = "";
    TextView OutletName, OutletCode, OutletMobile, OutletAddress;
    String LISTED_DR_CODE = "", OUTLET_NAMES = "", CUSTOMER_CODES = "", OUTLET_MOBILES = "", OUTLET_ADDRESSS = "";

    Context context = this;
    com.hap.checkinproc.Common_Class.Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    ProgressDialog progressDialog;

    JSONArray catArray, catArrayNew, catForApprove, freezerArray, freezerArrayNew;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_outlets_detailed);

        name_ll = findViewById(R.id.name_ll);
        outletType_ll = findViewById(R.id.outletType_ll);
        gst_ll = findViewById(R.id.gst_ll);
        deliveryType_ll = findViewById(R.id.deliveryType_ll);
        categoryType_ll = findViewById(R.id.categoryType_ll);
        freezer_ll = findViewById(R.id.freezer_ll);

        categoryTypeRV = findViewById(R.id.categoryTypeRV);
        freezerRV = findViewById(R.id.freezerRV);

        approveBtn = findViewById(R.id.approveBtn);
        rejectBtn = findViewById(R.id.rejectBtn);

        outletName = findViewById(R.id.outletName);
        outletNameNew = findViewById(R.id.outletNameNew);
        outletType = findViewById(R.id.outletType);
        outletTypeNew = findViewById(R.id.outletTypeNew);
        gst = findViewById(R.id.gst);
        gstNew = findViewById(R.id.gstNew);
        deliveryType = findViewById(R.id.deliveryType);
        deliveryTypeNew = findViewById(R.id.deliveryTypeNew);

        OutletName = findViewById(R.id.OutletName);
        OutletCode = findViewById(R.id.OutletCode);
        OutletMobile = findViewById(R.id.OutletMobile);
        OutletAddress = findViewById(R.id.OutletAddress);

        LISTED_DR_CODE = getIntent().getStringExtra("ListedDrCode");
        Log.e("mhfd", "LISTED_DR_CODE: " + LISTED_DR_CODE);
        OUTLET_NAMES = getIntent().getStringExtra("OutletName");
        CUSTOMER_CODES = getIntent().getStringExtra("CustomerCode");
        OUTLET_MOBILES = getIntent().getStringExtra("OutletMobile");
        OUTLET_ADDRESSS = getIntent().getStringExtra("OutletAddress");

        OutletName.setText(OUTLET_NAMES);
        OutletCode.setText(CUSTOMER_CODES);
        OutletMobile.setText(OUTLET_MOBILES);
        OutletAddress.setText(OUTLET_ADDRESSS);

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(context);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        String ss = shared_common_pref.getvalue(Constants.LOGIN_DATA);
        Log.e("mhfd", "LOGIN_DATA: " + ss);

        rejectBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to reject?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                RejectOutlet();
                dialog.dismiss();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });

        approveBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to approve?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                ApproveOutlet();
                dialog.dismiss();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });

        getApprovalData();
    }

    private void ApproveOutlet() {
        progressDialog.setMessage("Approving...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("axn", "approve_outlet");
        params.put("sfCode", Shared_Common_Pref.Sf_Code);
        params.put("outletCode", LISTED_DR_CODE);
        params.put("OutletName", OUTLET_NAME_NEW);
        params.put("OutletType", OUTLET_TYPE_NEW);
        params.put("GSTNo", GST_NEW);
        params.put("DelvType", DELIVERY_TYPE_NEW);

        params.put("distributorId", shared_common_pref.getvalue(Constants.Distributor_Id));
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String result = response.body().string();
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("success")) {
                            ApproveOutletsActivity.refresh = true;
                            Toast.makeText(context, "Outlet approved successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void RejectOutlet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_get_reason, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        EditText editText = view.findViewById(R.id.inputMessage);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView submit = view.findViewById(R.id.submit);
        cancel.setOnClickListener(v1 -> dialog.dismiss());
        submit.setOnClickListener(v2 -> {
            String message = editText.getText().toString().trim();
            if (TextUtils.isEmpty(message)) {
                Toast.makeText(context, "Reason for Rejection Required", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Rejecting...");
                progressDialog.show();
                Map<String, String> params = new HashMap<>();
                params.put("axn", "reject_outlet");
                params.put("sfCode", Shared_Common_Pref.Sf_Code);
                params.put("outletCode", LISTED_DR_CODE);
                params.put("distributorId", shared_common_pref.getvalue(Constants.Distributor_Id));
                params.put("message", message);
                Call<ResponseBody> call = apiInterface.getUniversalData(params);
                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String result = response.body().string();
                                JSONObject object = new JSONObject(result);
                                if (object.getBoolean("success")) {
                                    ApproveOutletsActivity.refresh = true;
                                    Toast.makeText(context, "Outlet rejected successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable e) {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setCancelable(true);
        dialog.show();
    }

    private void getApprovalData() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_outlet_info_for_approval");
        params.put("sfCode", Shared_Common_Pref.Sf_Code);
        params.put("outletCode", LISTED_DR_CODE);
        params.put("distributorId", shared_common_pref.getvalue(Constants.Distributor_Id));
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String result = response.body().string();
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("success")) {
                            JSONObject res = object.getJSONObject("response");

                            JSONObject Mas_ListedDr = res.getJSONObject("Mas_ListedDr");
                            OUTLET_NAME = Mas_ListedDr.optString("ListedDr_Name");
                            OUTLET_TYPE = String.valueOf(Mas_ListedDr.optInt("Outlet_Type"));
                            GST = Mas_ListedDr.optString("GST");
                            DELIVERY_TYPE = Mas_ListedDr.optString("Allowance_Type");

                            outletName.setText(OUTLET_NAME);
                            gst.setText(GST);
                            deliveryType.setText(DELIVERY_TYPE);

                            switch (OUTLET_TYPE) {
                                case "0":
                                    outletType.setText("Non Service");
                                    break;
                                case "1":
                                    outletType.setText("Service");
                                    break;
                                case "2":
                                    outletType.setText("Closed");
                                    break;
                                case "3":
                                    outletType.setText("Duplicate");
                                    break;
                                default:
                                    outletType.setText("");
                                    break;
                            }

                            JSONObject Mas_OutletChangesReq = res.optJSONObject("Mas_OutletChangesReq");
                            if (Mas_OutletChangesReq != null) {
                                UPDATED_ON = Mas_OutletChangesReq.optString("ReqDt");
                                UPDATED_BY = Mas_OutletChangesReq.optString("ReqBy");
                                OUTLET_ID = Mas_OutletChangesReq.optString("OutletId");
                                GST_NEW = Mas_OutletChangesReq.optString("GSTNo");
                                OUTLET_TYPE_NEW = Mas_OutletChangesReq.optString("OutletType");
                                DELIVERY_TYPE_NEW = Mas_OutletChangesReq.optString("DelvType");
                                OUTLET_NAME_NEW = Mas_OutletChangesReq.optString("OutletName");
                            }

                            if (OUTLET_NAME.equalsIgnoreCase(OUTLET_NAME_NEW) || OUTLET_NAME_NEW.isEmpty()) {
                                OUTLET_NAME_NEW = OUTLET_NAME;
                                outletNameNew.setVisibility(View.GONE);
                            } else {
                                outletNameNew.setText(OUTLET_NAME_NEW);
                            }

                            if (OUTLET_TYPE.equalsIgnoreCase(OUTLET_TYPE_NEW) || OUTLET_TYPE_NEW.isEmpty()) {
                                OUTLET_TYPE_NEW = OUTLET_TYPE;
                                outletTypeNew.setVisibility(View.GONE);
                            } else {
                                switch (OUTLET_TYPE_NEW) {
                                    case "0":
                                        outletTypeNew.setText("Non Service");
                                        break;
                                    case "1":
                                        outletTypeNew.setText("Service");
                                        break;
                                    case "2":
                                        outletTypeNew.setText("Closed");
                                        break;
                                    case "3":
                                        outletTypeNew.setText("Duplicate");
                                        break;
                                    default:
                                        outletTypeNew.setText("");
                                        break;
                                }
                            }

                            if (GST.equalsIgnoreCase(GST_NEW) || GST_NEW.isEmpty()) {
                                GST_NEW = GST;
                                gstNew.setVisibility(View.GONE);
                            } else {
                                gstNew.setText(GST_NEW);
                            }

                            if (DELIVERY_TYPE.equalsIgnoreCase(DELIVERY_TYPE_NEW) || DELIVERY_TYPE_NEW.isEmpty()) {
                                DELIVERY_TYPE_NEW = DELIVERY_TYPE;
                                deliveryTypeNew.setVisibility(View.GONE);
                            } else {
                                deliveryTypeNew.setText(DELIVERY_TYPE_NEW);
                            }

                            catArray = res.getJSONArray("Outlet_Category_Mapping");
                            catArrayNew = res.getJSONArray("Mas_RetCatChangesReq");
                            for (int i = 0; i < catArrayNew.length(); i++) {
                                JSONObject catObject = catArrayNew.getJSONObject(i);
                                catArray.put(catObject);
                            }

                            if (catArray.length() == 0) {
                                categoryType_ll.setVisibility(View.GONE);
                            } else {
                                categoryType_ll.setVisibility(View.VISIBLE);
                                categoryTypeRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                AdapterOutletApprovalCategory adapterOutletApprovalCategory = new AdapterOutletApprovalCategory(context, catArray);
                                categoryTypeRV.setAdapter(adapterOutletApprovalCategory);
                            }

                            freezerArray = res.getJSONArray("OurFreezers");
                            freezerArrayNew = res.getJSONArray("PendingFreezers");
                            for (int i = 0; i < freezerArrayNew.length(); i++) {
                                JSONObject frzObject = freezerArrayNew.getJSONObject(i);
                                freezerArray.put(frzObject);
                            }

                            if (freezerArray.length() == 0) {
                                freezer_ll.setVisibility(View.GONE);
                            } else {
                                freezer_ll.setVisibility(View.VISIBLE);
                                freezerRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                AdapterOutletApprovalFreezer adapterOutletApprovalFreezer = new AdapterOutletApprovalFreezer(context, freezerArray);
                                freezerRV.setAdapter(adapterOutletApprovalFreezer);
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        });
    }
}
