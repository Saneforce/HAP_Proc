package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AdapterOutletApprovalFreezer extends RecyclerView.Adapter<AdapterOutletApprovalFreezer.ViewHolder> {
    Context context;
    JSONArray array;
    private FilesAdapter filesAdapter;

    public AdapterOutletApprovalFreezer(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public AdapterOutletApprovalFreezer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterOutletApprovalFreezer.ViewHolder(LayoutInflater.from(context).inflate(R.layout.freezer_item_for_approval, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOutletApprovalFreezer.ViewHolder holder, int position) {
        try {
            if (array.getJSONObject(holder.getBindingAdapterPosition()).optString("flag").equals("2")) {
                holder.background_ll.setBackgroundColor(Color.parseColor("#FFCDCD"));
            } else {
                holder.background_ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            if (array.getJSONObject(holder.getBindingAdapterPosition()).optString("frzStatus").contains("Own")) {
                holder.llExpecSalVal.setVisibility(View.GONE);
            } else {
                holder.llExpecSalVal.setVisibility(View.VISIBLE);
            }
            holder.txFreezerGroup.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("frzGrp"));
            holder.txFreezerStatus.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("frzStatus"));
            holder.edt_expectSaleVal.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("salesVal"));
            holder.edt_depositAmt.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("depAmt"));
            holder.freezerCapacityTV_Company.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("frzCap"));
            /*String frzImg = array.getJSONObject(holder.getBindingAdapterPosition()).optString("FrzImgs");
            JSONArray array = new JSONArray(frzImg);
            List<String> jAryDta = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                String sname = ApiClient.BASE_URL + "FreezerImages/" + array.optString(i);
                sname = sname.replaceAll("server/", "");
                jAryDta.add(sname);
            }
            holder.rvFreezerFiles.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            filesAdapter = new FilesAdapter(jAryDta, R.layout.adapter_local_files_layout, context);
            holder.rvFreezerFiles.setAdapter(filesAdapter);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txFreezerGroup, txFreezerStatus, edt_expectSaleVal, edt_depositAmt, freezerCapacityTV_Company;
        LinearLayout llExpecSalVal, frzCapLayout, background_ll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txFreezerGroup = itemView.findViewById(R.id.txFreezerGroup);
            txFreezerStatus = itemView.findViewById(R.id.txFreezerStatus);
            edt_expectSaleVal = itemView.findViewById(R.id.edt_expectSaleVal);
            edt_depositAmt = itemView.findViewById(R.id.edt_depositAmt);
            freezerCapacityTV_Company = itemView.findViewById(R.id.freezerCapacityTV_Company);
            llExpecSalVal = itemView.findViewById(R.id.llExpecSalVal);
            frzCapLayout = itemView.findViewById(R.id.frzCapLayout);
            background_ll = itemView.findViewById(R.id.background_ll);
        }
    }
}
