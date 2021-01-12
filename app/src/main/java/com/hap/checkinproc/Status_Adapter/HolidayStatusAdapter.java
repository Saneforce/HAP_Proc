package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.LeaveCancelReason;
import com.hap.checkinproc.Model_Class.HolidayEntryModel;
import com.hap.checkinproc.R;

import java.util.List;

public class HolidayStatusAdapter extends RecyclerView.Adapter<HolidayStatusAdapter.MyViewHolder> {
    private List<HolidayEntryModel> holiday_status_modelist;
    private int rowLayout;
    private Context context;
    String AMod;
    LeaveCancelReason mLeaveCancelRea;
    String EditextReason = "";
    Integer count = 0;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }

    public HolidayStatusAdapter(List<HolidayEntryModel> holiday_status_modelist, int rowLayout, Context context) {
        this.holiday_status_modelist = holiday_status_modelist;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.HolidayDate.setText(holiday_status_modelist.get(position).getSubmissionDate());
        holder.HolidayStatus.setText(holiday_status_modelist.get(position).getOStatus());
        holder.HolidayShitTime.setText(holiday_status_modelist.get(position).getShiftName());
        holder.HolidayInTime.setText(holiday_status_modelist.get(position).getStartTime());
        holder.HolidayOutTime.setText(holiday_status_modelist.get(position).getEndTime());
        holder.HolidayGeoIN.setText(holiday_status_modelist.get(position).getCheckin());
        holder.HolidayGeoOut.setText(holiday_status_modelist.get(position).getCheckout());
        holder.HolidayEntryDate.setText(holiday_status_modelist.get(position).getSubmissionDate());


    }

    @Override
    public int getItemCount() {
        return holiday_status_modelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView HolidayDate, HolidayStatus, HolidayEntry, HolidayShitTime, HolidayInTime, HolidayOutTime, HolidayGeoIN, HolidayGeoOut, HolidayEntryDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            HolidayDate = itemView.findViewById(R.id.holiday_date);
            HolidayStatus = itemView.findViewById(R.id.holiday_status);
            HolidayShitTime = itemView.findViewById(R.id.shift_timing);
            HolidayInTime = itemView.findViewById(R.id.holiday_in_time);
            HolidayOutTime = itemView.findViewById(R.id.holiday_out_time);
            HolidayGeoIN = itemView.findViewById(R.id.geo_in);
            HolidayGeoOut = itemView.findViewById(R.id.geo_out);
            HolidayEntryDate = itemView.findViewById(R.id.holiday_entry_date);

        }
    }
}
