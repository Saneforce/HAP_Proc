package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Model_Class.Permission_Status_Model;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Permission_Status_Adapter extends RecyclerView.Adapter<Permission_Status_Adapter.MyViewHolder> {
    private List<Permission_Status_Model> Permission_Status_ModelsList;
    private int rowLayout;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sf_name, permission_date, Ptime, Preason, Papplieddate, PStatus, Papproved;
        public MyViewHolder(View view) {
            super(view);
            sf_name = view.findViewById(R.id.sf_name);
            permission_date = view.findViewById(R.id.permission_date);
            Ptime = view.findViewById(R.id.Ptime);
            Preason = view.findViewById(R.id.Preason);
            Papplieddate = view.findViewById(R.id.Papplieddate);
            PStatus = view.findViewById(R.id.PStatus);
            Papproved = view.findViewById(R.id.Papproved);
        }
    }


    public Permission_Status_Adapter(List<Permission_Status_Model> Permission_Status_ModelsList, int rowLayout, Context context) {
        this.Permission_Status_ModelsList = Permission_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public Permission_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Permission_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Permission_Status_Adapter.MyViewHolder holder, int position) {
        Permission_Status_Model Permission_Status_Model = Permission_Status_ModelsList.get(position);
        Log.e("SF_NAME", "" + Permission_Status_Model.getSFNm());
        // holder.sf_name.setText(""+Permission_Status_Model.getSFNm());
        holder.permission_date.setText("" + Permission_Status_Model.getPermissiondate());
        holder.Ptime.setText("" + Permission_Status_Model.getFromTime() + " to " + Permission_Status_Model.getToTime());
        holder.Preason.setText(Permission_Status_Model.getReason());
        holder.Papplieddate.setText("Applied: " + Permission_Status_Model.getCreatedDate());
        holder.PStatus.setText(Permission_Status_Model.getPStatus());
        if (Permission_Status_Model.getApprovalFlag() == 0) {
            holder.PStatus.setBackgroundResource(R.drawable.button_green);
        } else if (Permission_Status_Model.getApprovalFlag() == 2) {
            holder.PStatus.setBackgroundResource(R.drawable.button_yellows);
        } else {
            holder.PStatus.setBackgroundResource(R.drawable.button_red);
        }

        if (Permission_Status_Model.getApprovalFlag() == 1) {
            holder.Papproved.setVisibility(View.VISIBLE);
            holder.Papproved.setText("Reject:" + Permission_Status_Model.getApproveddate());
        } else if (Permission_Status_Model.getApprovalFlag() == 0) {
            holder.Papproved.setVisibility(View.VISIBLE);
            holder.Papproved.setText("Approved:" + Permission_Status_Model.getApproveddate());
        } else {
            holder.Papproved.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Permission_Status_ModelsList.size();
    }
}