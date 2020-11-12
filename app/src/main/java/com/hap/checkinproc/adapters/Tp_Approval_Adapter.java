package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hap.checkinproc.Activity_Hap.Tp_Approval_Reject;
import com.hap.checkinproc.Model_Class.Tp_Approval_Model;
import com.hap.checkinproc.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Tp_Approval_Adapter extends RecyclerView.Adapter<Tp_Approval_Adapter.MyViewHolder> {

    private List<Tp_Approval_Model> Tp_Approval_ModelsList;
    private int rowLayout;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open;

        public MyViewHolder(View view) {
            super(view);
            textviewname = (TextView) view.findViewById(R.id.textviewname);
            textviewdate = (TextView) view.findViewById(R.id.textviewdate);
            open = (TextView) view.findViewById(R.id.open);
        }
    }


    public Tp_Approval_Adapter(List<Tp_Approval_Model> Tp_Approval_ModelsList, int rowLayout, Context context) {
        this.Tp_Approval_ModelsList = Tp_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tp_Approval_Model Tp_Approval_Model = Tp_Approval_ModelsList.get(position);
        holder.textviewname.setText(Tp_Approval_Model.getFieldForceName());
        holder.textviewdate.setText(Tp_Approval_Model.getDate());

        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Tp_Approval_Reject.class);
                intent.putExtra("Username", Tp_Approval_ModelsList.get(position).getFieldForceName());
                intent.putExtra("Emp_Code", Tp_Approval_ModelsList.get(position).getEmpCode());
                intent.putExtra("HQ", Tp_Approval_ModelsList.get(position).getHQ());
                intent.putExtra("Designation", Tp_Approval_ModelsList.get(position).getDesignation());
                intent.putExtra("MobileNumber", Tp_Approval_ModelsList.get(position).getSFMobile());
                intent.putExtra("Plan_Date", Tp_Approval_ModelsList.get(position).getStartDate());
                intent.putExtra("Work_Type", Tp_Approval_ModelsList.get(position).getWorktypeName());
                intent.putExtra("Route", Tp_Approval_ModelsList.get(position).getRouteName());
                intent.putExtra("Distributor", Tp_Approval_ModelsList.get(position).getWorkedWithName());
                intent.putExtra("Sf_Code", Tp_Approval_ModelsList.get(position).getSFCode());
                intent.putExtra("Remarks", Tp_Approval_ModelsList.get(position).getRemarks());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Tp_Approval_ModelsList.size();
    }
}