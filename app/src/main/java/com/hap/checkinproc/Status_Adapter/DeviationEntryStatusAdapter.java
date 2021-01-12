package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.LeaveCancelReason;
import com.hap.checkinproc.Model_Class.DeviationEntryStatusModel;
import com.hap.checkinproc.R;

import java.util.List;

public class DeviationEntryStatusAdapter extends RecyclerView.Adapter<DeviationEntryStatusAdapter.MyViewHolder> {
    private List<DeviationEntryStatusModel> holiday_status_modelist;
    private int rowLayout;
    private Context context;
    String AMod;
    LeaveCancelReason mLeaveCancelRea;
    String EditextReason = "";
    Integer count = 0;


    @NonNull
    @Override
    public DeviationEntryStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new DeviationEntryStatusAdapter.MyViewHolder(view);
    }

    public DeviationEntryStatusAdapter(List<DeviationEntryStatusModel> holiday_status_modelist, int rowLayout, Context context) {
        this.holiday_status_modelist = holiday_status_modelist;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviationEntryStatusAdapter.MyViewHolder holder, int position) {

        holder.HolidayDate.setText(holiday_status_modelist.get(position).getDeviationDate());
        holder.HolidayStatus.setText(holiday_status_modelist.get(position).getDStatus());
        holder.HolidayEntry.setText(holiday_status_modelist.get(position).getDeviationType());
        holder.HolidayReason.setText(holiday_status_modelist.get(position).getReason());
        holder.HolidayApplied.setText(holiday_status_modelist.get(position).getCreatedDate());
        holder.HolidayReject.setText(holiday_status_modelist.get(position).getLastUpdtDate());


    }

    @Override
    public int getItemCount() {
        return holiday_status_modelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView HolidayDate, HolidayStatus, HolidayEntry, HolidayReason, HolidayApplied, HolidayReject, HolidayGeoIN, HolidayGeoOut, HolidayEntryDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            HolidayDate = itemView.findViewById(R.id.deviation_date);
            HolidayStatus = itemView.findViewById(R.id.devitaion_status);
            HolidayEntry = itemView.findViewById(R.id.deviation_entry);
            HolidayReason = itemView.findViewById(R.id.deviation_reason);
            HolidayApplied = itemView.findViewById(R.id.deviation_applied);
            HolidayReject = itemView.findViewById(R.id.deviation_rejected);


        }
    }
}