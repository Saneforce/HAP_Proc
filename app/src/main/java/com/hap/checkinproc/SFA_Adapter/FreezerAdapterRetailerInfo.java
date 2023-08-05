package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class FreezerAdapterRetailerInfo extends RecyclerView.Adapter<FreezerAdapterRetailerInfo.ViewHolder> {
    JSONArray myArray;
    Context context;

    public FreezerAdapterRetailerInfo(JSONArray myArray, Context context) {
        this.myArray = myArray;
        this.context = context;
    }

    @NonNull
    @Override
    public FreezerAdapterRetailerInfo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FreezerAdapterRetailerInfo.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_freezer_retailer_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FreezerAdapterRetailerInfo.ViewHolder holder, int position) {
        try {
            holder.Type.setText(myArray.getJSONObject(holder.getBindingAdapterPosition()).optString("Type"));
            holder.TagNumber.setText(myArray.getJSONObject(holder.getBindingAdapterPosition()).optString("TagNumber"));
            holder.Make.setText(myArray.getJSONObject(holder.getBindingAdapterPosition()).optString("Make"));
            holder.EquipmentNumber.setText(myArray.getJSONObject(holder.getBindingAdapterPosition()).optString("EquipmentNumber"));
            holder.FreezerReceivedDate.setText(myArray.getJSONObject(holder.getBindingAdapterPosition()).optString("FreezerReceivedDate"));
            holder.EquipmentType.setText(myArray.getJSONObject(holder.getBindingAdapterPosition()).optString("EquipmentType"));

            String image1 = myArray.getJSONObject(holder.getBindingAdapterPosition()).optString("FileAttachment1");
            String image2 = myArray.getJSONObject(holder.getBindingAdapterPosition()).optString("FileAttachment2");

            if (!image1.isEmpty()) {
                Picasso.get().load(image1).into(holder.FileAttachment1);
            } else {
                holder.FileAttachment1.setVisibility(View.GONE);
            }

            if (!image2.isEmpty()) {
                Picasso.get().load(image2).into(holder.FileAttachment2);
            } else {
                holder.FileAttachment2.setVisibility(View.GONE);
            }
        } catch (JSONException ignored) { }
    }

    @Override
    public int getItemCount() {
        return myArray.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Type, TagNumber, Make, EquipmentNumber, FreezerReceivedDate, EquipmentType;
        ImageView FileAttachment2, FileAttachment1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Type = itemView.findViewById(R.id.Type);
            TagNumber = itemView.findViewById(R.id.TagNumber);
            Make = itemView.findViewById(R.id.Make);
            EquipmentNumber = itemView.findViewById(R.id.EquipmentNumber);
            FreezerReceivedDate = itemView.findViewById(R.id.FreezerReceivedDate);
            EquipmentType = itemView.findViewById(R.id.EquipmentType);
            FileAttachment2 = itemView.findViewById(R.id.FileAttachment2);
            FileAttachment1 = itemView.findViewById(R.id.FileAttachment1);
        }
    }
}
