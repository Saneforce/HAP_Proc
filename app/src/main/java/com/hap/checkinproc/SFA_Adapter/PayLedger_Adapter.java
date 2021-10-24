package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.R;

import org.json.JSONArray;

import java.util.List;

public class PayLedger_Adapter extends RecyclerView.Adapter<PayLedger_Adapter.MyViewHolder> {

    Context context;
    List<Common_Model> mArr;

    public PayLedger_Adapter(Context context, List<Common_Model> arr) {
        this.context = context;
        this.mArr = arr;
    }

    @NonNull
    @Override
    public PayLedger_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_ledger_layout, null, false);

        return new PayLedger_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(PayLedger_Adapter.MyViewHolder holder, int position) {

        holder.tvDate.setText(""+mArr.get(position).getId());
        holder.tvDebit.setText(""+mArr.get(position).getName());
        holder.tvCredit.setText(""+mArr.get(position).getFlag());

        holder.tvBal.setText(""+mArr.get(position).getCheckouttime());

    }

    @Override
    public int getItemCount() {
        return mArr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDebit, tvCredit, tvBal;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDebit = itemView.findViewById(R.id.tvDebit);
            tvCredit = itemView.findViewById(R.id.tvCredit);
            tvBal = itemView.findViewById(R.id.tvBalance);

        }
    }
}
