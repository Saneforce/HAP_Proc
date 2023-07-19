package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;

public class AdapterOutletApprovalCategory extends RecyclerView.Adapter<AdapterOutletApprovalCategory.ViewHolder> {
    Context context;
    JSONArray array;

    public AdapterOutletApprovalCategory(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public AdapterOutletApprovalCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterOutletApprovalCategory.ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_for_approval, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOutletApprovalCategory.ViewHolder holder, int position) {
        try {
            if (array.getJSONObject(holder.getBindingAdapterPosition()).optString("ReqId").isEmpty()) {
                holder.background_ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.background_ll.setBackgroundColor(Color.parseColor("#FFCDCD"));
            }
            holder.group.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("OutletCat_Type"));
            holder.category.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("Category_Name"));
            holder.subCategory.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("Sub_Category_Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView group, category, subCategory;
        LinearLayout background_ll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group = itemView.findViewById(R.id.group);
            category = itemView.findViewById(R.id.category);
            subCategory = itemView.findViewById(R.id.subCategory);
            background_ll = itemView.findViewById(R.id.background_ll);
        }
    }
}
