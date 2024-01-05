package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.ApproveOutletsActivity;
import com.hap.checkinproc.SFA_Model_Class.ModelPendingOutletsCategory;

import java.util.ArrayList;

public class AdapterPendingOutletsCategory extends RecyclerView.Adapter<AdapterPendingOutletsCategory.ViewHolder> {

    ArrayList<ModelPendingOutletsCategory> list;
    Context context;
    ArrayList<ModelPendingOutletsCategory> listNew;
    public AdapterPendingOutletsCategory(ArrayList<ModelPendingOutletsCategory> list, Context context) {
        this.list = list;
        this.context = context;
        this.listNew=list;
    }

    @NonNull
    @Override
    public AdapterPendingOutletsCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_outlets_category, parent, false);
        return new AdapterPendingOutletsCategory.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPendingOutletsCategory.ViewHolder holder, int position) {
        ModelPendingOutletsCategory model = list.get(position);
        holder.sfName.setText(model.getSfName());
        holder.stockistName.setText(model.getStockistName()+" - "+model.getErpCode());
        holder.count.setText("Outlets: " + model.getListedDrCount());

        holder.open.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApproveOutletsActivity.class);
            intent.putExtra("stockistCode", model.getStockistCode());
            intent.putExtra("sfCode", model.getSfCode());
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApproveOutletsActivity.class);
            intent.putExtra("stockistCode", model.getStockistCode());
            intent.putExtra("sfCode", model.getSfCode());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase().trim().replaceAll("\\s", "");
                ArrayList<ModelPendingOutletsCategory> filteredList = new ArrayList<>();
                ArrayList<ModelPendingOutletsCategory> filteredany = new ArrayList<>();
                for (ModelPendingOutletsCategory row : listNew) {
                    String sName = row.getStockistName().toLowerCase().trim().replaceAll("\\s", "");
                    String getSfName = (row.getSfName()!= null) ? row.getSfName().toLowerCase().trim().replaceAll("\\s", "") : "";
                    String getErpCode = (row.getErpCode()!= null) ? row.getErpCode().toLowerCase().trim().replaceAll("\\s", "") : "";
                    //String getPhone = (row.getCustomerMobile() != null) ? row.getCustomerMobile().toLowerCase().trim().replaceAll("\\s", "") : "";
                    if ((";" + sName).contains(";" + charString) || (";" + getSfName).contains(";" + charString)||(";" + getErpCode).contains(";" + charString)) {
                        filteredList.add(row);
                    } else if (sName.contains(charString) || getSfName.contains(charString)||getErpCode.contains(charString)) {
                        filteredany.add(row);
                    }
                }
                filteredList.addAll(filteredany);
                list = filteredList;
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<ModelPendingOutletsCategory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sfName, stockistName, count, open;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sfName = itemView.findViewById(R.id.sf_name);
            stockistName = itemView.findViewById(R.id.stockist_name);
            count = itemView.findViewById(R.id.outlets_count);
            open = itemView.findViewById(R.id.open);
        }
    }
}
