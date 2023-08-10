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
import org.json.JSONException;

public class AdapterFreezerStatusList extends RecyclerView.Adapter<AdapterFreezerStatusList.ViewHolder> {
    Context context;
    JSONArray array;

    public AdapterFreezerStatusList(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public AdapterFreezerStatusList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterFreezerStatusList.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_freezer_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFreezerStatusList.ViewHolder holder, int position) {
        try {
            holder.frzGr.setText(array.getJSONObject(holder.getBindingAdapterPosition()).getString("frzGrp"));
            holder.frzSt.setText(array.getJSONObject(holder.getBindingAdapterPosition()).getString("frzStatus"));
            holder.exSalVal.setText(array.getJSONObject(holder.getBindingAdapterPosition()).getString("salesVal"));
            holder.depAmt.setText(array.getJSONObject(holder.getBindingAdapterPosition()).getString("depAmt"));
            holder.frzCap.setText(array.getJSONObject(holder.getBindingAdapterPosition()).getString("frzCap"));
            holder.date.setText("Requested on: " + array.getJSONObject(holder.getBindingAdapterPosition()).getString("crDate"));
            int flag = array.getJSONObject(holder.getBindingAdapterPosition()).getInt("flag");
            if (flag == 0) {
                holder.approved.setVisibility(View.VISIBLE);
            } else if (flag == 1) {
                holder.rejected.setVisibility(View.VISIBLE);
            } else if (flag == 2) {
                holder.pending.setVisibility(View.VISIBLE);
            }
        } catch (JSONException ignored) { }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView frzGr, frzSt, exSalVal, depAmt, frzCap, date, approved, rejected, pending;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            frzGr = itemView.findViewById(R.id.frzGr);
            frzSt = itemView.findViewById(R.id.frzSt);
            exSalVal = itemView.findViewById(R.id.exSalVal);
            depAmt = itemView.findViewById(R.id.depAmt);
            frzCap = itemView.findViewById(R.id.frzCap);
            date = itemView.findViewById(R.id.date);
            approved = itemView.findViewById(R.id.approved);
            rejected = itemView.findViewById(R.id.rejected);
            pending = itemView.findViewById(R.id.pending);
        }
    }
}
