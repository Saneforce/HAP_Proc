package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Model_Class.MissedPunch_Status_Model;

import java.util.List;

public class MissedPnch_Status_Adapter extends RecyclerView.Adapter<MissedPnch_Status_Adapter.MyViewHolder> {

    private List<MissedPunch_Status_Model> missedPunchStatusModelList;
    private int rowLayout;
    private Context context;


    public MissedPnch_Status_Adapter(List<MissedPunch_Status_Model> onduty_Status_ModelsList, int rowLayout, Context context) {
        missedPunchStatusModelList = onduty_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MissedPnch_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MissedPunch_Status_Model Onduty_Status_Model = missedPunchStatusModelList.get(position);
        holder.ondutydate.setText("" + missedPunchStatusModelList.get(position).getMissedPunchDate());
        holder.type.setText("" + missedPunchStatusModelList.get(position).getShiftName());
        holder.intime.setText("" + missedPunchStatusModelList.get(position).getCheckinTime());
        holder.outtime.setText("" + missedPunchStatusModelList.get(position).getCheckoutTme());
        holder.POV.setText("" + missedPunchStatusModelList.get(position).getReason());
        holder.OStatus.setText(Onduty_Status_Model.getMPStatus());
        if (Onduty_Status_Model.getMissedPunchFlag() == 0) {
            holder.OStatus.setBackgroundResource(R.drawable.button_yellows);
        } else if (Onduty_Status_Model.getMissedPunchFlag() == 2) {
            holder.OStatus.setBackgroundResource(R.drawable.button_green);
        } else {
            holder.OStatus.setBackgroundResource(R.drawable.button_red);
        }

    }

    @Override
    public int getItemCount() {
        return missedPunchStatusModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ondutydate, type, shift, odlocation, POV, intime, outtime, geoin, geoout, applieddate, OStatus, Papproved;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ondutydate = itemView.findViewById(R.id.ondutydate);
            type = itemView.findViewById(R.id.type);
            odlocation = itemView.findViewById(R.id.odlocation);
            POV = itemView.findViewById(R.id.text_reason);
            intime = itemView.findViewById(R.id.txt_in_time);
            outtime = itemView.findViewById(R.id.txt_out_time);
            geoin = itemView.findViewById(R.id.geoin);
            OStatus = itemView.findViewById(R.id.OStatus);
            geoout = itemView.findViewById(R.id.geoout);
            applieddate = itemView.findViewById(R.id.applieddate);
            Papproved = itemView.findViewById(R.id.applieddate);
        }
    }
}
