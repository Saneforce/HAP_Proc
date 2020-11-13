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
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Model_Class.Permission_Approval_Model;
import com.hap.checkinproc.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Permission_Approval_Adapter extends RecyclerView.Adapter<Permission_Approval_Adapter.MyViewHolder> {

    private List<Permission_Approval_Model> Permission_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    Integer dummy;

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


    public Permission_Approval_Adapter(List<Permission_Approval_Model> Permission_Approval_ModelsList, int rowLayout, Context context,AdapterOnClick mAdapterOnClick) {
        this.Permission_Approval_ModelsList = Permission_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public Permission_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });

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

                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Permission_Approval_ModelsList.size();
    }
}