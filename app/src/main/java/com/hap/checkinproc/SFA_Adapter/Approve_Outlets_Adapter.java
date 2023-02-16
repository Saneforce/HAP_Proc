package com.hap.checkinproc.SFA_Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outlet_info_recyclerview_two, parent, false);
        return new Approve_Outlets_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Approve_Outlets_Adapter.ViewHolder holder, int position) {
        ModelApproveOutlets model = list.get(position);
        holder.name.setText(model.getCustomerName());
        holder.id.setText(model.getCustomerID());
        holder.mobile.setText(model.getCustomerMobile());
        holder.address.setText(model.getCustomerAddress());

        holder.mobile.setOnClickListener(v -> {
            new Common_Class(context).makeCall(Integer.valueOf(model.getCustomerMobile()));
        });

        holder.view.setOnClickListener(v -> {
            Toast.makeText(context, "View Clicked", Toast.LENGTH_SHORT).show();
        });

        holder.approve.setOnClickListener(v -> {
            Toast.makeText(context, "Approve Clicked", Toast.LENGTH_SHORT).show();
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Approving...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            String listedDrCode = model.getListedDrCode();
            Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(context);
            String sfCode = shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code, "");
            Map<String, String> params = new HashMap<>();
            params.put("type", "0"); // 0 means approve, 1 means reject, 2 means pending
            params.put("listedDrCode", listedDrCode);
            params.put("approvedBy", sfCode);
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
                                Toast.makeText(context, "Outlet Approved Successfully", Toast.LENGTH_SHORT).show();
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
        });

        holder.reject.setOnClickListener(v -> {
            Toast.makeText(context, "Reject Clicked", Toast.LENGTH_SHORT).show();
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
        TextView name, id, mobile, address;
        Button view, approve, reject;

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