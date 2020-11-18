package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hap.checkinproc.Activity_Hap.Leave_Approval_Reject;

import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Model_Class.Leave_Status_Model;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Leave_Status_Adapter extends RecyclerView.Adapter<Leave_Status_Adapter.MyViewHolder> {

    private List<Leave_Status_Model> Leave_Status_ModelsList;
    private int rowLayout;
    private Context context;
    String AMod;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fromdatetodate, leavetype, leavedays, leavereason, applieddate, LStatus, SfName;
        RelativeLayout sf_namelayout;

        public MyViewHolder(View view) {
            super(view);
            fromdatetodate = view.findViewById(R.id.fromdatetodate);
            leavetype = view.findViewById(R.id.leavetype);
            leavedays = view.findViewById(R.id.leavedays);
            leavereason = view.findViewById(R.id.leavereason);
            applieddate = view.findViewById(R.id.applieddate);
            LStatus = view.findViewById(R.id.LStatus);
            SfName = view.findViewById(R.id.SfName);
            sf_namelayout = view.findViewById(R.id.sf_namelayout);


        }
    }


    public Leave_Status_Adapter(List<Leave_Status_Model> Leave_Status_ModelsList, int rowLayout, Context context, String AMod) {
        this.Leave_Status_ModelsList = Leave_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @Override
    public Leave_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Leave_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Leave_Status_Adapter.MyViewHolder holder, int position) {
        Leave_Status_Model Leave_Status_Model = Leave_Status_ModelsList.get(position);
        Log.e("SF_MODE", AMod);
        holder.fromdatetodate.setText(Leave_Status_Model.getFromDate() + " TO " + Leave_Status_Model.getToDate());
        holder.leavetype.setText("" + Leave_Status_Model.getLeaveType());
        holder.leavedays.setText("" + Leave_Status_Model.getNoOfDays());
        holder.leavereason.setText(Leave_Status_Model.getReason());
        holder.applieddate.setText("Applied: " + Leave_Status_Model.getCreatedDate());
        holder.LStatus.setText(Leave_Status_Model.getLStatus());
        if (Leave_Status_Model.getLeaveActiveFlag() == 0) {
            holder.LStatus.setBackgroundResource(R.drawable.button_green);
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Leave_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#008000"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }

        } else if (Leave_Status_Model.getLeaveActiveFlag() == 2) {
            holder.LStatus.setBackgroundResource(R.drawable.button_yellows);
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Leave_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff9819"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
        } else {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Leave_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff3700"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.LStatus.setBackgroundResource(R.drawable.button_red);
        }
    }

    @Override
    public int getItemCount() {
        return Leave_Status_ModelsList.size();
    }
}