package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hap.checkinproc.Activity_Hap.Leave_Approval_Reject;
import com.hap.checkinproc.Activity_Hap.Tp_Approval_Reject;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Model_Class.Leave_Approval_Model;
import com.hap.checkinproc.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Leave_Approval_Adapter extends RecyclerView.Adapter<Leave_Approval_Adapter.MyViewHolder> {

    private List<Leave_Approval_Model> Leave_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    Integer dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, leavedays;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);
            leavedays = view.findViewById(R.id.leavedays);
        }
    }


    public Leave_Approval_Adapter(List<Leave_Approval_Model> Leave_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Leave_Approval_ModelsList = Leave_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.mAdapterOnClick = mAdapterOnClick;
        this.context = context;
    }

    @Override
    public Leave_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });

        return new Leave_Approval_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Leave_Approval_Adapter.MyViewHolder holder, int position) {
        Leave_Approval_Model Leave_Approval_Model = Leave_Approval_ModelsList.get(position);
        holder.textviewname.setText(Leave_Approval_Model.getFieldForceName());
        holder.leavedays.setText("" + Leave_Approval_Model.getLeaveDays());
        holder.textviewdate.setText(Leave_Approval_Model.getApplieddate());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Leave_Approval_ModelsList.size();
    }
}