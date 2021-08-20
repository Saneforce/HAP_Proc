package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import java.util.List;

public class MoreInfoAdapter extends RecyclerView.Adapter<MoreInfoAdapter.MyViewHolder> {
    Context context;
    List<Product_Details_Modal> mDate;
    AdapterOnClick mAdapterOnClick;

    public MoreInfoAdapter(Context context, List<Product_Details_Modal> mDate, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @NonNull
    @Override
    public MoreInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.more_info_recyclerview, null, false);
        return new MoreInfoAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MoreInfoAdapter.MyViewHolder holder, int position) {
        holder.productname.setText("" + mDate.get(position).getName());
        holder.productqty.setText("" + mDate.get(position).getQty());
        holder.productrate.setText("" + mDate.get(position).getRate());
        holder.producttotal.setText("" + mDate.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productname, productqty, productrate, producttotal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productname = itemView.findViewById(R.id.productname);
            productqty = itemView.findViewById(R.id.productqty);
            productrate = itemView.findViewById(R.id.productrate);
            producttotal = itemView.findViewById(R.id.producttotal);

        }
    }
}