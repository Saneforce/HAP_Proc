package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hap.checkinproc.Activity_Hap.Onduty_Approval_Reject;
import com.hap.checkinproc.Activity_Hap.Permission_Approval_Reject;
import com.hap.checkinproc.Model_Class.Onduty_Approval_Model;
import com.hap.checkinproc.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Onduty_Approval_Adapter extends RecyclerView.Adapter<Onduty_Approval_Adapter.MyViewHolder> {

    private List<Onduty_Approval_Model> Onduty_Approval_ModelsList;
    private int rowLayout;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, NoofHours;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);

        }
    }


    public Onduty_Approval_Adapter(List<Onduty_Approval_Model> Onduty_Approval_ModelsList, int rowLayout, Context context) {
        this.Onduty_Approval_ModelsList = Onduty_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public Onduty_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
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
                Intent intent = new Intent(context, Onduty_Approval_Reject.class);
                intent.putExtra("Username", Onduty_Approval_ModelsList.get(position).getFieldForceName());
                intent.putExtra("Emp_Code", Onduty_Approval_ModelsList.get(position).getEmpCode());
                intent.putExtra("HQ", Onduty_Approval_ModelsList.get(position).getHQ());
                intent.putExtra("Designation", Onduty_Approval_ModelsList.get(position).getDesignation());
                intent.putExtra("Applieddate", Onduty_Approval_ModelsList.get(position).getLoginDate());
                intent.putExtra("MobileNumber", Onduty_Approval_ModelsList.get(position).getSFMobile());
                intent.putExtra("Odtype", Onduty_Approval_ModelsList.get(position).getOndutytype());
                intent.putExtra("POV", Onduty_Approval_ModelsList.get(position).getRmks());
                intent.putExtra("OdLocation", Onduty_Approval_ModelsList.get(position).getODLocName());
                intent.putExtra("Geocheckin", Onduty_Approval_ModelsList.get(position).getCheckin());
                intent.putExtra("geocheckout", Onduty_Approval_ModelsList.get(position).getCheckout());
                intent.putExtra("checkintime", Onduty_Approval_ModelsList.get(position).getStartTime());
                intent.putExtra("checkouttime", Onduty_Approval_ModelsList.get(position).getEndTime());
                intent.putExtra("Sf_Code", Onduty_Approval_ModelsList.get(position).getSfCode());
                intent.putExtra("duty_id", Onduty_Approval_ModelsList.get(position).getDutyId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Onduty_Approval_ModelsList.size();
    }
}