package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.ViewReport;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;


import java.util.List;

public class Outlet_Report_View_Adapter  extends RecyclerView.Adapter<Outlet_Report_View_Adapter.MyViewHolder> {

    Context context;
    List<OutletReport_View_Modal> mDate;
    ViewReport mViewReport;
    String produtId, productDate;

    public Outlet_Report_View_Adapter(Context context, List<OutletReport_View_Modal> mDate, ViewReport mViewReport) {
        this.context = context;
        this.mDate = mDate;
        this.mViewReport = mViewReport;
    }

    @NonNull
    @Override
    public Outlet_Report_View_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_report_list, null, false);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewReport.reportCliick(produtId, productDate);
            }
        });
        return new Outlet_Report_View_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(Outlet_Report_View_Adapter.MyViewHolder holder, int position) {

        holder.txtsNo.setText(mDate.get(position).getSlno());
        holder.txtOrderDate.setText(mDate.get(position).getOrderDate());
        holder.txtOrderID.setText(mDate.get(position).getTransSlNo());
        holder.txtValue.setText(""+mDate.get(position).getOrderValue());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewReport.reportCliick(mDate.get(position).getTransSlNo(), mDate.get(position).getOrderDate());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtsNo;
        TextView txtOrderDate;
        TextView txtOrderID;
        TextView txtValue;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtsNo = (TextView) itemView.findViewById(R.id.txt_serial);
            txtOrderID = (TextView) itemView.findViewById(R.id.txt_order);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtValue = (TextView) itemView.findViewById(R.id.txt_total);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.row_report);
        }
    }
}
