package com.hap.checkinproc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;

public class RetailerViewAdapter extends RecyclerView.Adapter<RetailerViewAdapter.MyViewHolder> {

    /*  List<ReportModel> mDate;
      ViewReport mViewReport;
      String produtId, productDate;*/

    public RetailerViewAdapter() {
    }

    @NonNull
    @Override
    public RetailerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_list_reatiler_details, null, false);

        return new RetailerViewAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RetailerViewAdapter.MyViewHolder holder, int position) {
        holder.txtProductName.setText("ABC");
        holder.txtProductQty.setText("123");
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtProductName, txtProductQty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txt_product_name);
            txtProductQty = itemView.findViewById(R.id.txt_product_qty);

        }
    }
}
