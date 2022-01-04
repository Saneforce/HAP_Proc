package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Model_Class.Leave_Status_Model;

import java.util.List;

public class FlightBooking_TravelerDetail_Adapter extends RecyclerView.Adapter<FlightBooking_TravelerDetail_Adapter.MyViewHolder> {
    private List<Leave_Status_Model> Leave_Status_ModelsList;
    private int rowLayout;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvName, tvCompany, tvMobile, tvtype;


        public MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tvEMPId);
            tvName = view.findViewById(R.id.tvName);
            tvCompany = view.findViewById(R.id.tvCompany);
            tvMobile = view.findViewById(R.id.txMobile);
            tvtype = view.findViewById(R.id.tvType);

        }
    }


    public FlightBooking_TravelerDetail_Adapter(List<Leave_Status_Model> Leave_Status_ModelsList, Context context) {
        this.Leave_Status_ModelsList = Leave_Status_ModelsList;
        this.context = context;

    }

    @Override
    public FlightBooking_TravelerDetail_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.flight_travelers_listitem, null, false);
        return new FlightBooking_TravelerDetail_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FlightBooking_TravelerDetail_Adapter.MyViewHolder holder, int position) {
        Leave_Status_Model Leave_Status_Model = Leave_Status_ModelsList.get(position);
        holder.tvId.setText("000" + (position + 1));
        holder.tvName.setText(Leave_Status_Model.getCreatedDate());
        holder.tvCompany.setText("" + Leave_Status_Model.getSFNm());
        holder.tvMobile.setText("" + Leave_Status_Model.getReason());
        holder.tvtype.setText("" + Leave_Status_Model.getLeaveType());


    }

    @Override
    public int getItemCount() {
        return Leave_Status_ModelsList.size();
    }
}