package com.hap.checkinproc.SFA_Adapter;

import static com.hap.checkinproc.SFA_Activity.HAPApp.CurrencySymbol;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.ModelPendingOrder;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterPendingOrder extends RecyclerView.Adapter<AdapterPendingOrder.ViewHolder> {
    Context context;
    ArrayList<ModelPendingOrder> list;

    ViewClicked viewClicked;

    public void setViewClicked(ViewClicked viewClicked) {
        this.viewClicked = viewClicked;
    }

    public AdapterPendingOrder(Context context, ArrayList<ModelPendingOrder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterPendingOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterPendingOrder.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pending_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPendingOrder.ViewHolder holder, int position) {
        ModelPendingOrder model = list.get(position);
        holder.address.setText(model.getAddress());
        holder.mobile.setText(model.getMobile());
        holder.title2.setText(model.getTitle2());
        holder.orderID.setText(model.getOrderID());
        holder.date.setText(model.getDate());
        holder.products.setText(model.getProducts());
        Log.e("status", model.getTotal());
        String amount = CurrencySymbol+" " + new DecimalFormat("##0.00").format(Double.parseDouble(model.getTotal()));
        holder.total.setText(amount);

        holder.viewBtn.setOnClickListener(v -> {
            if (viewClicked != null) {
                viewClicked.onClick(model, position);
            }
        });

        holder.mobileLayout.setOnClickListener(v -> new Common_Class(context).makeCall(model.getMobile()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView address, mobile, title2, orderID, date, products, total, viewBtn;
        LinearLayout mobileLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.tvDistAdd);
            mobile = itemView.findViewById(R.id.txMobile);
            title2 = itemView.findViewById(R.id.retailername);
            orderID = itemView.findViewById(R.id.tvOrderId);
            date = itemView.findViewById(R.id.tvDateTime);
            products = itemView.findViewById(R.id.tvProductName);
            total = itemView.findViewById(R.id.tvAmount);
            viewBtn = itemView.findViewById(R.id.view_order);
            mobileLayout = itemView.findViewById(R.id.btnCallMob);
        }
    }

    public interface ViewClicked {
        void onClick (ModelPendingOrder model, int position);
    }
}
