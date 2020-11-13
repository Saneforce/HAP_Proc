package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hap.checkinproc.Activity_Hap.Extended_Approval_Reject;
import com.hap.checkinproc.Activity_Hap.Onduty_Approval_Reject;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Model_Class.Extended_Approval_Model;
import com.hap.checkinproc.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Extended_Approval_Adapter extends RecyclerView.Adapter<Extended_Approval_Adapter.MyViewHolder> {

    private List<Extended_Approval_Model> Extended_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    Integer dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);

        }
    }

    public Extended_Approval_Adapter(List<Extended_Approval_Model> Extended_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Extended_Approval_ModelsList = Extended_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public Extended_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });
        return new Extended_Approval_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Extended_Approval_Adapter.MyViewHolder holder, int position) {
        Extended_Approval_Model Extended_Approval_Model = Extended_Approval_ModelsList.get(position);
        holder.textviewname.setText(Extended_Approval_Model.getSfName());
        holder.textviewdate.setText("" + Extended_Approval_Model.getEntrydate());

        Log.e("GET_Extended_Date", Extended_Approval_Model.getEntrydate());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAdapterOnClick.onIntentClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return Extended_Approval_ModelsList.size();
    }
}