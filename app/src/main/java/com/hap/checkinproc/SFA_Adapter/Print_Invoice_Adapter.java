package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;

import org.json.JSONArray;

import java.text.DecimalFormat;

public class Print_Invoice_Adapter extends RecyclerView.Adapter<Print_Invoice_Adapter.MyViewHolder> {
    Context context;
    JSONArray mDate;

    public Print_Invoice_Adapter(Context context, JSONArray mDate) {
        this.context = context;
        this.mDate = mDate;
    }

    @NonNull
    @Override
    public Print_Invoice_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.print_invoice_recyclerview, null, false);
        return new Print_Invoice_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(Print_Invoice_Adapter.MyViewHolder holder, int position) {
        try {
            holder.productname.setText("" + mDate.getJSONObject(position).getString("Product_Name"));
            holder.productqty.setText("" + mDate.getJSONObject(position).getInt("Quantity"));
            holder.productUOM.setText("" + mDate.getJSONObject(position).getString("UOM"));
            holder.productrate.setText("" + new DecimalFormat("##0.00").format(mDate.getJSONObject(position).getDouble("Rate")));
            holder.producttotal.setText("" + new DecimalFormat("##0.00").format(mDate.getJSONObject(position).getDouble("value")));
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return mDate.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productname, productqty, productrate, producttotal, productUOM;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productname = itemView.findViewById(R.id.productname);
            productqty = itemView.findViewById(R.id.productqty);
            productrate = itemView.findViewById(R.id.productrate);
            producttotal = itemView.findViewById(R.id.producttotal);
            productUOM = itemView.findViewById(R.id.productUom);

        }
    }
}