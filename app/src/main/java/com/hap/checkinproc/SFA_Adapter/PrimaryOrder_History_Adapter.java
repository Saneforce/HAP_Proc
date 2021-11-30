package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.PrimaryOrderActivity;
import com.hap.checkinproc.SFA_Activity.TodayPrimOrdActivity;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;

import java.text.DecimalFormat;
import java.util.List;

public class PrimaryOrder_History_Adapter extends RecyclerView.Adapter<PrimaryOrder_History_Adapter.MyViewHolder> {

    Context context;
    List<OutletReport_View_Modal> mDate;
    AdapterOnClick mAdapterOnClick;
    String mResponse;

    public PrimaryOrder_History_Adapter(Context context, List<OutletReport_View_Modal> mDate, String mResponse, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
        this.mResponse = mResponse;
    }

    @NonNull
    @Override
    public PrimaryOrder_History_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.primaryorder_history_recyclerview, null, false);

        return new PrimaryOrder_History_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(PrimaryOrder_History_Adapter.MyViewHolder holder, int position) {

        holder.txtOrderDate.setText("" + mDate.get(position).getOrderDate());
        holder.txtOrderID.setText(mDate.get(position).getOrderNo());
        holder.txtValue.setText("" + new DecimalFormat("##0.00").format(mDate.get(position).getOrderValue()));
        holder.Itemcountinvoice.setText("" + mDate.get(position).getStatus());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(position);
            }
        });

        holder.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodayPrimOrdActivity.mTdPriAct.updateData(mDate.get(position).getOrderNo());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderDate, txtOrderID, txtValue, Itemcountinvoice;
        LinearLayout linearLayout, llEdit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txt_order);
            txtOrderDate = itemView.findViewById(R.id.txt_date);
            txtValue = itemView.findViewById(R.id.txt_total);
            linearLayout = itemView.findViewById(R.id.row_report);
            Itemcountinvoice = itemView.findViewById(R.id.Itemcountinvoice);
            llEdit = itemView.findViewById(R.id.llEdit);


        }
    }
}
