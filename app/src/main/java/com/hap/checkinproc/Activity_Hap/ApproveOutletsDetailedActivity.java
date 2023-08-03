package com.hap.checkinproc.Activity_Hap;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    TextView outletID, updatedBy, updatedOn, outletName, outletNameNew, outletType, outletTypeNew, gst, gstNew, deliveryType, deliveryTypeNew;

    String OUTLET_ID, UPDATED_BY, UPDATED_ON, OUTLET_NAME, OUTLET_NAME_NEW, OUTLET_TYPE, OUTLET_TYPE_NEW, GST, GST_NEW, DELIVERY_TYPE, DELIVERY_TYPE_NEW;

    Context context = this;
    com.hap.checkinproc.Common_Class.Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    ProgressDialog progressDialog;

    JSONArray catArray, catArrayNew, freezerArray, freezerArrayNew;
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

        outletID = findViewById(R.id.outletID);
        updatedBy = findViewById(R.id.updatedBy);
        updatedOn = findViewById(R.id.updatedOn);
        outletName = findViewById(R.id.outletName);
        outletNameNew = findViewById(R.id.outletNameNew);
        outletType = findViewById(R.id.outletType);
        outletTypeNew = findViewById(R.id.outletTypeNew);
        gst = findViewById(R.id.gst);
        gstNew = findViewById(R.id.gstNew);
        deliveryType = findViewById(R.id.deliveryType);
        deliveryTypeNew = findViewById(R.id.deliveryTypeNew);

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(context);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        rejectBtn.setOnClickListener(v -> RejectOutlet());

        approveBtn.setOnClickListener(v -> ApproveOutlet());

        getApprovalData();
    }

    private void ApproveOutlet() {
        progressDialog.setMessage("Approving...");
        progressDialog.show();
    }

    private void RejectOutlet() {
        progressDialog.setMessage("Rejecting...");
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("axn", "reject_outlet");
        params.put("sfCode", Shared_Common_Pref.Sf_Code);
        params.put("outletCode", getIntent().getStringExtra("OutletCode"));
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
                            Toast.makeText(context, "Outlet rejected successfully", Toast.LENGTH_SHORT).show();
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

    private void getApprovalData() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_outlet_info_for_approval");
        params.put("sfCode", Shared_Common_Pref.Sf_Code);
        params.put("outletCode", getIntent().getStringExtra("OutletCode"));
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
                            Log.e("skfjgh", res.toString());

                            JSONObject Mas_ListedDr = res.getJSONObject("Mas_ListedDr");
                            OUTLET_NAME = Mas_ListedDr.optString("ListedDr_Name");
                            OUTLET_TYPE = String.valueOf(Mas_ListedDr.optInt("Outlet_Type"));
                            GST = Mas_ListedDr.optString("GST");
                            DELIVERY_TYPE = Mas_ListedDr.optString("Allowance_Type");

                            JSONObject Mas_OutletChangesReq = res.getJSONObject("Mas_OutletChangesReq");
                            UPDATED_ON = Mas_OutletChangesReq.optString("ReqDt");
                            UPDATED_BY = Mas_OutletChangesReq.optString("ReqBy");
                            OUTLET_ID = Mas_OutletChangesReq.optString("OutletId");
                            GST_NEW = Mas_OutletChangesReq.optString("GSTNo");
                            OUTLET_TYPE_NEW = Mas_OutletChangesReq.optString("OutletType");
                            DELIVERY_TYPE_NEW = Mas_OutletChangesReq.optString("DelvType");
                            OUTLET_NAME_NEW = Mas_OutletChangesReq.optString("OutletName");

                            outletID.setText(String.format("Outlet ID: %S", OUTLET_ID));
                            updatedBy.setText(String.format("Updated By: %S", UPDATED_BY));
                            updatedOn.setText(String.format("Updated On: %S", UPDATED_ON));

                            if (OUTLET_NAME.equalsIgnoreCase(OUTLET_NAME_NEW)) {
                                name_ll.setVisibility(View.GONE);
                            } else {
                                name_ll.setVisibility(View.VISIBLE);
                                outletName.setText(OUTLET_NAME);
                                outletNameNew.setText(OUTLET_NAME_NEW);
                            }

                            if (OUTLET_TYPE.equalsIgnoreCase(OUTLET_TYPE_NEW)) {
                                outletType_ll.setVisibility(View.GONE);
                            } else {
                                outletType_ll.setVisibility(View.VISIBLE);
                                outletType.setText(OUTLET_TYPE);
                                outletTypeNew.setText(OUTLET_TYPE_NEW);
                            }

                            if (GST.equalsIgnoreCase(GST_NEW)) {
                                gst_ll.setVisibility(View.GONE);
                            } else {
                                gst_ll.setVisibility(View.VISIBLE);
                                gst.setText(GST);
                                gstNew.setText(GST_NEW);
                            }

                            if (DELIVERY_TYPE.equalsIgnoreCase(DELIVERY_TYPE_NEW)) {
                                deliveryType_ll.setVisibility(View.GONE);
                            } else {
                                deliveryType_ll.setVisibility(View.VISIBLE);
                                deliveryType.setText(DELIVERY_TYPE);
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
                            }

                            freezerArray = res.getJSONArray("Outlet_Freezer_Mapping");
                            freezerArrayNew = res.getJSONArray("Mas_RetFreezerDetails");
                            for (int i = 0; i < freezerArrayNew.length(); i++) {
                                JSONObject frzObject = freezerArrayNew.getJSONObject(i);
                                freezerArray.put(frzObject);
                            }

                            if (freezerArray.length() == 0) {
                                freezer_ll.setVisibility(View.GONE);
                            }


                            categoryTypeRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            AdapterOutletApprovalCategory adapterOutletApprovalCategory = new AdapterOutletApprovalCategory(context, catArray);
                            categoryTypeRV.setAdapter(adapterOutletApprovalCategory);

                            freezerRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            AdapterOutletApprovalFreezer adapterOutletApprovalFreezer = new AdapterOutletApprovalFreezer(context, freezerArray);
                            freezerRV.setAdapter(adapterOutletApprovalFreezer);
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
