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
import com.hap.checkinproc.Model_Class.LeaveCancelStatusModel;
import com.hap.checkinproc.R;

import java.util.List;

public class LeaveCancelStatusAdapter extends RecyclerView.Adapter<LeaveCancelStatusAdapter.MyViewHolder> {
    private List<LeaveCancelStatusModel> holiday_status_modelist;
    private int rowLayout;
    private Context context;
    String AMod;
    LeaveCancelReason mLeaveCancelRea;
    String EditextReason = "";
    Integer count = 0;


    @NonNull
    @Override
    public LeaveCancelStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new LeaveCancelStatusAdapter.MyViewHolder(view);
    }

    public LeaveCancelStatusAdapter(List<LeaveCancelStatusModel> holiday_status_modelist, int rowLayout, Context context) {
        this.holiday_status_modelist = holiday_status_modelist;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveCancelStatusAdapter.MyViewHolder holder, int position) {

        holder.HolidayDate.setText(holiday_status_modelist.get(position).getCreatedDate());
        holder.HolidayStatus.setText(holiday_status_modelist.get(position).getLStatus());
        holder.HolidayEntry.setText(holiday_status_modelist.get(position).getLeaveType());
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

            HolidayDate = itemView.findViewById(R.id.leave_date);
            HolidayStatus = itemView.findViewById(R.id.leave_status);
            HolidayEntry = itemView.findViewById(R.id.leave_type);
            HolidayReason = itemView.findViewById(R.id.leave_reason);
            HolidayApplied = itemView.findViewById(R.id.leave_applied);
            HolidayReject = itemView.findViewById(R.id.leave_rejected);


        }
    }
}