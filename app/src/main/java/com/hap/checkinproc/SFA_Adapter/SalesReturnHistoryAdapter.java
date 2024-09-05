package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.HAPApp;
import com.hap.checkinproc.SFA_Activity.Print_Invoice_Activity;
import com.hap.checkinproc.SFA_Activity.SalesReturnHistoryActivity;
import com.hap.checkinproc.SFA_Model_Class.SalesReturnHistoryModel;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class SalesReturnHistoryAdapter extends RecyclerView.Adapter<SalesReturnHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<SalesReturnHistoryModel> list;
    String orderType="";

    public SalesReturnHistoryAdapter(Context context, ArrayList<SalesReturnHistoryModel> list) {
        this.context = context;
        this.list = list;
    }
    public void setOrderType(String orderTypes){
        orderType=orderTypes;
    }

    @NonNull
    @Override
    public SalesReturnHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesReturnHistoryAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.sales_return_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReturnHistoryAdapter.ViewHolder holder, int position) {
        SalesReturnHistoryModel model = list.get(position);
        holder.return_id.setText(model.getReturnID());
        holder.return_date.setText(model.getReturnDate());
        String value = HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(model.getReturnTotal());
        holder.return_value.setText(value);

        holder.itemView.setOnClickListener(v -> {
            Shared_Common_Pref.TransSlNo = model.getReturnID();
            Intent intent = new Intent(context, Print_Invoice_Activity.class);
            Shared_Common_Pref sharedCommonPref = new Shared_Common_Pref(context);
            if(orderType.equalsIgnoreCase("CounterSales")) {
                sharedCommonPref.save(Constants.FLAG, "COUNTERSALES RETURN");
            }else if(Shared_Common_Pref.SFA_MENU.equalsIgnoreCase("VanSalesDashboardRoute")){
                sharedCommonPref.save(Constants.FLAG, "VANSALES RETURN");
            }else {
                sharedCommonPref.save(Constants.FLAG, "SALES RETURN");
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView return_id, return_date, return_value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            return_id = itemView.findViewById(R.id.return_id);
            return_date = itemView.findViewById(R.id.return_date);
            return_value = itemView.findViewById(R.id.return_value);
        }
    }
}
