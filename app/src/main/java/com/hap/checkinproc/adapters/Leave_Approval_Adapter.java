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
import com.hap.checkinproc.Model_Class.Leave_Approval_Model;
import com.hap.checkinproc.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Leave_Approval_Adapter extends RecyclerView.Adapter<Leave_Approval_Adapter.MyViewHolder> {

    private List<Leave_Approval_Model> Leave_Approval_ModelsList;
    private int rowLayout;
    private Context context;

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


    public Leave_Approval_Adapter(List<Leave_Approval_Model> Leave_Approval_ModelsList, int rowLayout, Context context) {
        this.Leave_Approval_ModelsList = Leave_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public Leave_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
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
                Intent intent = new Intent(context, Leave_Approval_Reject.class);
                intent.putExtra("LeaveId", String.valueOf(Leave_Approval_ModelsList.get(position).getLeaveId()));
                intent.putExtra("Username", Leave_Approval_ModelsList.get(position).getFieldForceName());
                intent.putExtra("Emp_Code", Leave_Approval_ModelsList.get(position).getEmpCode());
                intent.putExtra("HQ", Leave_Approval_ModelsList.get(position).getHQ());
                intent.putExtra("Designation", Leave_Approval_ModelsList.get(position).getDesignation());
                intent.putExtra("MobileNumber", Leave_Approval_ModelsList.get(position).getSFMobile());
                intent.putExtra("Reason", Leave_Approval_ModelsList.get(position).getReason());
                intent.putExtra("Leavetype", Leave_Approval_ModelsList.get(position).getLeaveType());
                intent.putExtra("fromdate", Leave_Approval_ModelsList.get(position).getFromDate());
                intent.putExtra("todate", Leave_Approval_ModelsList.get(position).getToDate());
                intent.putExtra("leavedays", String.valueOf(Leave_Approval_ModelsList.get(position).getLeaveDays()));
                Log.e("LEAVE_APPROVAL_REJECT", String.valueOf(Leave_Approval_ModelsList.get(position).getLeaveDays()));
                intent.putExtra("Sf_Code", Leave_Approval_ModelsList.get(position).getSfCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Leave_Approval_ModelsList.size();
    }
}