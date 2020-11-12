package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hap.checkinproc.Activity_Hap.Leave_Approval_Reject;
import com.hap.checkinproc.Activity_Hap.Permission_Approval_Reject;
import com.hap.checkinproc.Model_Class.Permission_Approval_Model;
import com.hap.checkinproc.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Permission_Approval_Adapter extends RecyclerView.Adapter<Permission_Approval_Adapter.MyViewHolder> {

    private List<Permission_Approval_Model> Permission_Approval_ModelsList;
    private int rowLayout;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, NoofHours;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);
            NoofHours = view.findViewById(R.id.noofhours);
        }
    }


    public Permission_Approval_Adapter(List<Permission_Approval_Model> Permission_Approval_ModelsList, int rowLayout, Context context) {
        this.Permission_Approval_ModelsList = Permission_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public Permission_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Permission_Approval_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Permission_Approval_Adapter.MyViewHolder holder, int position) {
        Permission_Approval_Model Permission_Approval_Model = Permission_Approval_ModelsList.get(position);
        holder.textviewname.setText(Permission_Approval_Model.getFieldForceName());
        holder.NoofHours.setText("" + Permission_Approval_Model.getNoofHours());
        holder.textviewdate.setText(Permission_Approval_Model.getApplieddate());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Permission_Approval_Reject.class);
                intent.putExtra("Sl_No", String.valueOf(Permission_Approval_ModelsList.get(position).getSlNo()));
                intent.putExtra("Username", Permission_Approval_ModelsList.get(position).getFieldForceName());
                intent.putExtra("Emp_Code", Permission_Approval_ModelsList.get(position).getEmpCode());
                intent.putExtra("HQ", Permission_Approval_ModelsList.get(position).getHQ());
                intent.putExtra("Designation", Permission_Approval_ModelsList.get(position).getDesignation());
                intent.putExtra("MobileNumber", Permission_Approval_ModelsList.get(position).getSFMobile());
                intent.putExtra("Reason", Permission_Approval_ModelsList.get(position).getReason());
                intent.putExtra("fromtime", Permission_Approval_ModelsList.get(position).getFromTime());
                intent.putExtra("totime", Permission_Approval_ModelsList.get(position).getToTime());
                intent.putExtra("Sf_Code", Permission_Approval_ModelsList.get(position).getSfCode());
                intent.putExtra("permissiondate", Permission_Approval_ModelsList.get(position).getPermissiondate());
                intent.putExtra("NoofHours", Permission_Approval_ModelsList.get(position).getNoofHours());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Permission_Approval_ModelsList.size();
    }
}