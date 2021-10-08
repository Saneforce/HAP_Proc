package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

public class POPStatusAdapter extends RecyclerView.Adapter<POPStatusAdapter.MyViewHolder> {
    Context context;
    List<QPS_Modal> mData;

    Shared_Common_Pref shared_common_pref;


    POPMaterialAdapter qpsFilesAdapter;

    public POPStatusAdapter(Context context, List<QPS_Modal> mData) {
        this.context = context;
        this.mData = mData;
        shared_common_pref = new Shared_Common_Pref(context);

    }

    @NonNull
    @Override
    public POPStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_popstatus_layout, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(POPStatusAdapter.MyViewHolder holder, int position) {
        try {
            holder.sNo.setText("" + mData.get(position).getsNo());
            holder.requestNo.setText("" + mData.get(position).getRequestNo());
            holder.bookingDate.setText("" + mData.get(position).getBookingDate());

            qpsFilesAdapter = new POPMaterialAdapter(context, mData);

            holder.rvFile.setAdapter(qpsFilesAdapter);


        } catch (Exception e) {
            Log.e("POPStatusAdapter:", e.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sNo, requestNo,  bookingDate;
        RecyclerView rvFile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sNo = itemView.findViewById(R.id.tvQpsSno);
            requestNo = itemView.findViewById(R.id.tvQPSReqNo);
            bookingDate=itemView.findViewById(R.id.tvBookingDate);
            rvFile = itemView.findViewById(R.id.rvMaterials);

        }
    }
}