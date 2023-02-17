package com.hap.checkinproc.SFA_Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Activity_Hap.ApproveOutletsDetailedActivity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.ModelApproveOutlets;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Created by RAGU on 27/01/2023
public class Approve_Outlets_Adapter extends RecyclerView.Adapter<Approve_Outlets_Adapter.ViewHolder> {

    ArrayList<ModelApproveOutlets> list;
    Context context;

    OnButtonClickListener mListener;


    public Approve_Outlets_Adapter(ArrayList<ModelApproveOutlets> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Approve_Outlets_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Approve_Outlets_Adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.outlet_info_recyclerview_two, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Approve_Outlets_Adapter.ViewHolder holder, int position) {
        ModelApproveOutlets model = list.get(position);
        holder.name.setText(model.getCustomerName());
        holder.id.setText(model.getCustomerID());
        holder.mobile.setText(model.getCustomerMobile());
        holder.address.setText(model.getCustomerAddress());

        holder.mobile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("tel:91" + model.getCustomerMobile()));
            context.startActivity(intent);
        });

        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApproveOutletsDetailedActivity.class);
            Shared_Common_Pref.Outlet_Info_Flag = "2";
            Shared_Common_Pref.Editoutletflag = "1";
            Shared_Common_Pref.Outler_AddFlag = "2";
            Shared_Common_Pref.FromActivity = "Outlets";
            Shared_Common_Pref.OutletCode = String.valueOf(model.getCustomerID());
            intent.putExtra("OutletCode", String.valueOf(model.getCustomerID()));
            intent.putExtra("OutletName", model.getCustomerName());
            intent.putExtra("OutletAddress", model.getCustomerAddress());
            intent.putExtra("OutletMobile", model.getCustomerMobile());
            intent.putExtra("OutletRoute", "");
            context.startActivity(intent);
        });

        holder.approve.setOnClickListener(v -> {
            UpdateData(holder, model, "", "0"); // 0 means approve, 1 means reject, 2 means pending
        });

        holder.reject.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_get_reason, null);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            EditText editText = view.findViewById(R.id.inputMessage);
            TextView cancel = view.findViewById(R.id.cancel);
            TextView submit = view.findViewById(R.id.submit);
            cancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            submit.setOnClickListener(v2 -> {
                String message = editText.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(context, "Reason for Rejection Required", Toast.LENGTH_SHORT).show();
                } else {
                    UpdateData(holder, model, message, "1"); // 0 means approve, 1 means reject, 2 means pending
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            dialog.show();
        });
    }

    private void UpdateData(ViewHolder holder, ModelApproveOutlets model, String message, String type) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String listedDrCode = model.getListedDrCode();
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(context);
        String sfCode = shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code, "");
        Map<String, String> params = new HashMap<>();
        params.put("type", type); // 0 means approve, 1 means reject, 2 means pending
        params.put("listedDrCode", listedDrCode);
        params.put("ModifiedBy", sfCode);
        params.put("remarks", message);
        params.put("ModifiedOn", new Common_Class(context).GetDatemonthyearTimeformat());
        Call<ResponseBody> call = apiInterface.setOutletStatus("set_outlet_status", params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject js = new JSONObject(response.body().string());
                        if (js.getBoolean("success")) {
                            list.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyItemRangeChanged(0, list.size());
                            if (type.equals("0")) {
                                Toast.makeText(context, "Outlet Approved Successfully", Toast.LENGTH_SHORT).show();
                            } else if (type.equals("1")) {
                                Toast.makeText(context, "Outlet Rejected Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error: Response not successfull", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnButtonClickListener {
        void onButtonClick(ModelApproveOutlets model, int buttonNumber);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, id, mobile, address, view, approve, reject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.customerName_outletInfo);
            id = itemView.findViewById(R.id.customerId_outletInfo);
            mobile = itemView.findViewById(R.id.mobile_outletInfo);
            address = itemView.findViewById(R.id.address_outletInfo);
            view = itemView.findViewById(R.id.viewBtn);
            approve = itemView.findViewById(R.id.approveBtn);
            reject = itemView.findViewById(R.id.rejectBtn);
        }
    }
}