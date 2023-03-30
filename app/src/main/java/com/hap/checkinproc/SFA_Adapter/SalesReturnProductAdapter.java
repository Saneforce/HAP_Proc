package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.HAPApp;
import com.hap.checkinproc.SFA_Model_Class.SalesReturnProductModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SalesReturnProductAdapter extends RecyclerView.Adapter<SalesReturnProductAdapter.ViewHolder> {
    Context context;
    ArrayList<SalesReturnProductModel> list;

    public SalesReturnProductAdapter(Context context, ArrayList<SalesReturnProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SalesReturnProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesReturnProductAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.sales_return_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReturnProductAdapter.ViewHolder holder, int position) {
        SalesReturnProductModel model = list.get(position);

        holder.productName.setText(model.getProductName());
        holder.materialCode.setText("Mat Code: " + model.getMaterialCode());
        holder.MRP.setText("MRP: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(Double.parseDouble(model.getMRP())));
        holder.rate.setText("Rate: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(Double.parseDouble(model.getRate())));
        holder.invUOM.setText(model.getInvUOM());
        holder.invQty.setText(model.getInvQty());
        holder.retUOM.setText(model.getRetUOM());
        holder.retQty.setText(model.getRetQty());
        holder.retType.setText(model.getRetType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, materialCode, MRP, rate, invUOM, invQty, retUOM, retType, retAmount;
        EditText retQty;
        ImageView retQtyMinus, retQtyPlus;
        RelativeLayout rl_retType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.ProductName);
            materialCode = itemView.findViewById(R.id.MatCode);
            MRP = itemView.findViewById(R.id.MRP);
            rate = itemView.findViewById(R.id.Rate);
            invUOM = itemView.findViewById(R.id.UOM);
            invQty = itemView.findViewById(R.id.invQty);
            retUOM = itemView.findViewById(R.id.retUOM);
            retQty = itemView.findViewById(R.id.retQty);
            retType = itemView.findViewById(R.id.tv_retType);
            retAmount = itemView.findViewById(R.id.retAmount);
            retQtyMinus = itemView.findViewById(R.id.retQtyMns);
            retQtyPlus = itemView.findViewById(R.id.retQtyPlus);
            rl_retType = itemView.findViewById(R.id.rl_retType);
        }
    }
}
