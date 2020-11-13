package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hap.checkinproc.Activity_Hap.Onduty_Approval_Reject;
import com.hap.checkinproc.Activity_Hap.Permission_Approval_Reject;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Model_Class.Onduty_Approval_Model;
import com.hap.checkinproc.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Onduty_Approval_Adapter extends RecyclerView.Adapter<Onduty_Approval_Adapter.MyViewHolder> {

    private List<Onduty_Approval_Model> Onduty_Approval_ModelsList;
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

        }
    }


    public Onduty_Approval_Adapter(List<Onduty_Approval_Model> Onduty_Approval_ModelsList, int rowLayout, Context context,AdapterOnClick mAdapterOnClick) {
        this.Onduty_Approval_ModelsList = Onduty_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.mAdapterOnClick = mAdapterOnClick;
        this.context = context;
    }

    @Override
    public Onduty_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });
        return new Onduty_Approval_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Onduty_Approval_Adapter.MyViewHolder holder, int position) {
        Onduty_Approval_Model Onduty_Approval_Model = Onduty_Approval_ModelsList.get(position);
        holder.textviewname.setText(Onduty_Approval_Model.getFieldForceName());
        holder.textviewdate.setText("" + Onduty_Approval_Model.getLoginDate());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Onduty_Approval_ModelsList.size();
    }
}