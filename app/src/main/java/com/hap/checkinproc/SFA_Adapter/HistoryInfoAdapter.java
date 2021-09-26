package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;

import java.util.List;

public class HistoryInfoAdapter extends RecyclerView.Adapter<HistoryInfoAdapter.MyViewHolder> {
    Context context;
    List<OutletReport_View_Modal> mDate;
    AdapterOnClick mAdapterOnClick;

    public HistoryInfoAdapter(Context context, List<OutletReport_View_Modal> mDate, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @NonNull
    @Override
    public HistoryInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.history_info_adapter_layout, null, false);
        return new HistoryInfoAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(HistoryInfoAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvName.setText("" + mDate.get(position).getNo_Of_items() + " items");
            holder.tvId.setText("" + mDate.get(position).getOrderNo());
            holder.tvDate.setText("" + mDate.get(position).getOrderDate());

            if (mDate.get(position).getInvoice_Flag().equals("1")) {
                holder.tvStatus.setText("COMPLETED");

                holder.ivStatus.setImageResource(R.drawable.ic_round_done_outline_24);

            } else {
                holder.tvStatus.setText("PENDING");
                holder.ivStatus.setImageResource(R.drawable.ic_round_pending_24);
            }
            holder.tvAmount.setText("₹ " + mDate.get(position).getOrderValue());

        } catch (Exception e) {
            Log.e("History_Adapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvDate, tvStatus, tvAmount, tvAddress, tvOutletName;
        ImageView ivStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOutletName = itemView.findViewById(R.id.retailername);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvId = itemView.findViewById(R.id.tvOrderId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvDate=itemView.findViewById(R.id.tvDate);

        }
    }
}