package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Activity.FlightBooking_Status_Activity;
import com.hap.checkinproc.Status_Model_Class.Leave_Status_Model;

import java.util.List;

public class FlightBooking_Status_Adapter extends RecyclerView.Adapter<FlightBooking_Status_Adapter.MyViewHolder> {
    private List<Leave_Status_Model> Leave_Status_ModelsList;
    private int rowLayout;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvStatus, tvNoOfTraveler, tvbookedBy;


        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvStatus = view.findViewById(R.id.tvFBStatus);
            tvNoOfTraveler = view.findViewById(R.id.tvTravelerCount);
            tvbookedBy = view.findViewById(R.id.tvBookedBy);

        }
    }


    public FlightBooking_Status_Adapter(List<Leave_Status_Model> Leave_Status_ModelsList, Context context) {
        this.Leave_Status_ModelsList = Leave_Status_ModelsList;
        this.context = context;

    }

    @Override
    public FlightBooking_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.flight_booking_status_listitem, null, false);
        return new FlightBooking_Status_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FlightBooking_Status_Adapter.MyViewHolder holder, int position) {
        Leave_Status_Model Leave_Status_Model = Leave_Status_ModelsList.get(position);
        holder.tvDate.setText(Leave_Status_Model.getCreatedDate());
        holder.tvStatus.setText("" + Leave_Status_Model.getSFNm());
        holder.tvNoOfTraveler.setText("" + Leave_Status_Model.getReason());
        holder.tvbookedBy.setText("" + Leave_Status_Model.getLeaveType());

        if (Leave_Status_Model.getSFNm().equalsIgnoreCase("APPROVED")) {
            holder.tvStatus.setBackgroundResource(R.drawable.button_green);
        } else if ((Leave_Status_Model.getSFNm().equalsIgnoreCase("CANCEL"))) {
            holder.tvStatus.setBackgroundResource(R.drawable.button_red);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.button_yellows);
        }

        holder.tvStatus.setPadding(20, 5, 20, 5);

        holder.tvNoOfTraveler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlightBooking_Status_Activity.activity.showTravelersDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Leave_Status_ModelsList.size();
    }
}