package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;

import java.util.List;

public class Invoice_History_Adapter extends RecyclerView.Adapter<Invoice_History_Adapter.MyViewHolder> {

    Context context;
    List<OutletReport_View_Modal> mDate;
    AdapterOnClick mAdapterOnClick;

    public Invoice_History_Adapter(Context context, List<OutletReport_View_Modal> mDate, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @NonNull
    @Override
    public Invoice_History_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.invoice_history_recyclerview, null, false);

        return new Invoice_History_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(Invoice_History_Adapter.MyViewHolder holder, int position) {
        if (mDate.get(position).getInvoice_Flag().equals("1")) {
            holder.Statusinvoice.setText("COMPLETED");
            holder.ivStatus.setImageResource(R.drawable.ic_round_done_outline_24);

            // holder.parent_layout.setBackgroundResource(R.color.white);
        } else {
            holder.Statusinvoice.setText("PENDING");
            holder.ivStatus.setImageResource(R.drawable.ic_round_pending_24);

            //  holder.parent_layout.setBackgroundResource(R.color.greeninvoicecolor);
        }
        holder.txtOrderDate.setText("" + mDate.get(position).getOrderDate());
        holder.txtOrderID.setText(mDate.get(position).getOrderNo());
        holder.txtValue.setText("" + mDate.get(position).getOrderValue());
        holder.Itemcountinvoice.setText("" + mDate.get(position).getNo_Of_items());
        holder.txtType.setText("" + mDate.get(position).getStatus());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Statusinvoice, txtOrderDate, txtOrderID, txtValue, Itemcountinvoice, txtType;
        LinearLayout linearLayout;
        RelativeLayout parent_layout;
        ImageView ivStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txt_order);
            Statusinvoice = itemView.findViewById(R.id.Statusinvoice);
            txtOrderDate = itemView.findViewById(R.id.txt_date);
            txtValue = itemView.findViewById(R.id.txt_total);
            linearLayout = itemView.findViewById(R.id.row_report);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            Itemcountinvoice = itemView.findViewById(R.id.Itemcountinvoice);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            txtType = itemView.findViewById(R.id.txt_type);


        }
    }
}
