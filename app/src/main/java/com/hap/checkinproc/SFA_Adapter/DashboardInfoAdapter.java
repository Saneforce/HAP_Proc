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

import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class DashboardInfoAdapter extends RecyclerView.Adapter<DashboardInfoAdapter.MyViewHolder> {
    Context context;
    JSONArray mDate;
    private View listItem;
    int rowlayout;

    String DistName = "";


    public DashboardInfoAdapter(Context context, JSONArray mDate, int rowlayout) {
        this.context = context;
        this.mDate = mDate;
        this.rowlayout = rowlayout;

    }


    @NonNull
    @Override
    public DashboardInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        listItem = layoutInflater.inflate(rowlayout, null, false);
        return new DashboardInfoAdapter.MyViewHolder(listItem);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(DashboardInfoAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject mObj = mDate.getJSONObject(position);

            holder.tvOutletName.setText(mObj.getString("OutletName"));

            if (DistName.contains(mObj.getString("FranchiseName")))
                holder.tvDistName.setVisibility(View.GONE);
            else {
                DistName = DistName + mObj.getString("FranchiseName");

                holder.tvDistName.setText(mObj.getString("FranchiseName"));
            }

            if (Shared_Common_Pref.SALES_MODE.equals("noorder")) {
                holder.tvStatus.setText(mObj.getString("Remarks"));
                holder.tvAmount.setVisibility(View.GONE);
                holder.tvId.setVisibility(View.GONE);
            } else {
                holder.tvStatus.setVisibility(View.GONE);
                holder.tvAmount.setText("₹ " + new DecimalFormat("##0.00").format(mObj.getDouble("TransactionAmt")));
                holder.tvId.setText(mObj.getString("TransactionNo"));
            }


        } catch (Exception e) {
            Log.e("History_Adapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDate.length();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvStatus, tvAmount, tvOutletName, tvDistName;
        ImageView ivStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOutletName = itemView.findViewById(R.id.retailername);
            tvId = itemView.findViewById(R.id.tvOrderId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvDistName = itemView.findViewById(R.id.tvDistributer);


        }
    }
}