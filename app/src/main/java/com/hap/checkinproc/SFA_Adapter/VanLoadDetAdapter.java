package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VanLoadDetAdapter extends RecyclerView.Adapter<VanLoadDetAdapter.MyViewHolder>{
    Context context;
    private JSONArray arr;
    private int rowLayout;


    public VanLoadDetAdapter(JSONArray arr, int rowLayout, Context context) {
        this.arr = arr;
        this.rowLayout = rowLayout;
        this.context = context;


    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JSONObject objNew = arr.getJSONObject(position);
            int serialNo=position+1;
            holder.tvSerialNo.setText("Det: "+serialNo);
        holder.tvsalesManName.setText(""+objNew.getString("salesManName"));
        holder.tvVehicleNo.setText(""+objNew.getString("vehNo"));
        double srtKm=objNew.getDouble("srtKm");
        double endKm=objNew.getDouble("endKm");
        if(srtKm>0) {holder.tvSrtKm.setText("" +srtKm );}else{holder.tvSrtKm.setText("-");}
        if(endKm>0) {
            holder.tvEndKm.setText("" +endKm );
            holder.tvCheckOutDate.setText(""+objNew.getString("vanDate"));
            holder.tvCheckOutTime.setText(""+objNew.getString("checkoutTime"));
        }else{
            holder.tvEndKm.setText("-");
            holder.tvCheckOutDate.setText("-");
            holder.tvCheckOutTime.setText("-");
        }
        holder.tvCheckInDate.setText(""+objNew.getString("vanDate"));
        holder.tvCheckInTime.setText(""+objNew.getString("checkInTime"));
        if(endKm==0){
            holder.tvTotalKm.setText("Closing Not Done");
        }else{
            double totkm=endKm-srtKm;
            if(totkm<0) {
                holder.tvTotalKm.setText("Closing Not Done");
            }else {
                holder.tvTotalKm.setText("" + totkm);
            }
        }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return arr.length();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvsalesManName,tvVehicleNo,tvSrtKm,tvCheckInDate,tvCheckInTime,tvEndKm,tvCheckOutDate,tvCheckOutTime,tvTotalKm,tvSerialNo;

        public MyViewHolder(View view) {
            super(view);

            tvsalesManName = itemView.findViewById(R.id.tvsalesManName);
            tvVehicleNo=itemView.findViewById(R.id.tvVehicleNo);
            tvSrtKm = itemView.findViewById(R.id.tvSrtKm);
            tvCheckInDate= itemView.findViewById(R.id.tvCheckInDate);
            tvCheckInTime = itemView.findViewById(R.id.tvCheckInTime);
            tvEndKm= itemView.findViewById(R.id.tvEndKm);
            tvCheckOutDate= itemView.findViewById(R.id.tvCheckOutDate);
            tvCheckOutTime= itemView.findViewById(R.id.tvCheckOutTime);
            tvTotalKm= itemView.findViewById(R.id.tvTotalKm);
            tvSerialNo=itemView.findViewById(R.id.tvSerialNo);

        }
    }
}
