package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;

import java.util.List;

public class QPSAdapter extends RecyclerView.Adapter<QPSAdapter.MyViewHolder> {
    Context context;
    List<QPS_Modal> mData;

    public QPSAdapter(Context context, List<QPS_Modal> mData) {
        this.context = context;
        this.mData = mData;

    }

    @NonNull
    @Override
    public QPSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_qps_layout, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(QPSAdapter.MyViewHolder holder, int position) {
        holder.sNo.setText("" + mData.get(position).getsNo());
        holder.requestNo.setText("" + mData.get(position).getRequestNo());
        holder.gift.setText("" + mData.get(position).getGift());
        holder.bookingDate.setText("" + mData.get(position).getBookingDate());
        holder.duration.setText("" + mData.get(position).getDuration());
        holder.receivedDate.setText("" + mData.get(position).getReceivedDate());
        holder.status.setText("" + mData.get(position).getStatus());

        if (mData.get(position).getStatus().equalsIgnoreCase("COMPLETED"))
            holder.btnComplete.setVisibility(View.GONE);
        else
            holder.btnComplete.setVisibility(View.VISIBLE);


        holder.ivCaptureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sNo, requestNo, gift, bookingDate, duration, receivedDate, status;
        Button btnComplete;
        ImageView ivCaptureImg, ivAttachImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sNo = itemView.findViewById(R.id.tvQpsSno);
            requestNo = itemView.findViewById(R.id.tvQPSReqNo);
            gift = itemView.findViewById(R.id.tvQPSGift);
            bookingDate = itemView.findViewById(R.id.tvQPSBookDate);
            duration = itemView.findViewById(R.id.tvQPSDuration);
            receivedDate = itemView.findViewById(R.id.tvQPSReceivedDate);
            status = itemView.findViewById(R.id.tvStatus);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            ivCaptureImg=itemView.findViewById(R.id.ivQPSCaptureImg);
            ivAttachImg=itemView.findViewById(R.id.ivQPSPreviewImg);

        }
    }
}