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
            if (array.getJSONObject(holder.getBindingAdapterPosition()).optString("OutletCode").isEmpty()) {
                holder.background_ll.setBackgroundColor(Color.parseColor("#FFCDCD"));
            } else {
                holder.background_ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            if (array.getJSONObject(holder.getBindingAdapterPosition()).optString("FrzStatus").contains("Own")) {
                holder.llExpecSalVal.setVisibility(View.GONE);
            }

            holder.txFreezerGroup.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("FrzGrp"));
            holder.txFreezerStatus.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("FrzStatus"));
            holder.edt_expectSaleVal.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("SalesVal"));
            holder.edt_depositAmt.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("DeposiAmt"));
            holder.edt_retailer_freezerMake.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("FrzMak"));
            holder.edt_retailer_freezerTagNo.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("FrzID"));
            holder.txFreezerCapacity.setText(array.getJSONObject(holder.getBindingAdapterPosition()).optString("FrzCap"));
            String frzImg = array.getJSONObject(holder.getBindingAdapterPosition()).optString("FrzImgs");
            JSONArray array = new JSONArray(frzImg);
            List<String> jAryDta = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                String sname = ApiClient.BASE_URL + "FreezerImages/" + array.optString(i);
                sname = sname.replaceAll("server/", "");
                jAryDta.add(sname);
            }
            holder.rvFreezerFiles.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            filesAdapter = new FilesAdapter(jAryDta, R.layout.adapter_local_files_layout, context);
            holder.rvFreezerFiles.setAdapter(filesAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvFreezerFiles;
        TextView txFreezerGroup, txFreezerStatus, edt_expectSaleVal, edt_depositAmt, edt_retailer_freezerMake, edt_retailer_freezerTagNo, txFreezerCapacity;
        LinearLayout background_ll, llExpecSalVal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txFreezerGroup = itemView.findViewById(R.id.txFreezerGroup);
            txFreezerStatus = itemView.findViewById(R.id.txFreezerStatus);
            edt_expectSaleVal = itemView.findViewById(R.id.edt_expectSaleVal);
            edt_depositAmt = itemView.findViewById(R.id.edt_depositAmt);
            edt_retailer_freezerMake = itemView.findViewById(R.id.edt_retailer_freezerMake);
            edt_retailer_freezerTagNo = itemView.findViewById(R.id.edt_retailer_freezerTagNo);
            txFreezerCapacity = itemView.findViewById(R.id.txFreezerCapacity);
            rvFreezerFiles = itemView.findViewById(R.id.rvFreezerFiles);
            background_ll = itemView.findViewById(R.id.background_ll);
            llExpecSalVal = itemView.findViewById(R.id.llExpecSalVal);
        }
    }
}
