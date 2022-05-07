package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity.TAViewStatus;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;


public class ViewTAStatusAdapter extends RecyclerView.Adapter<ViewTAStatusAdapter.MyViewHolder> {
    Context context;
    JsonArray taJsonArray;
    AdapterOnClick adapterOnClick;


    public ViewTAStatusAdapter(Context context, JsonArray taJsonArray, AdapterOnClick adapterOnClick) {
        this.context = context;
        this.taJsonArray = taJsonArray;
        this.adapterOnClick = adapterOnClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewStatus = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_ta_status, parent, false);
        return new ViewTAStatusAdapter.MyViewHolder(viewStatus);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        JsonObject jsonObject = (JsonObject) taJsonArray.get(position);
        Log.v("TaAmount", jsonObject.get("Total_Amount").getAsString());

        holder.taDate.setText(jsonObject.get("EDT").getAsString());
        holder.taStatus.setText(jsonObject.get("ApSTatus").getAsString());
        holder.taTotalAmt.setText(jsonObject.get("Total_Amount").getAsString());
        holder.taDaAmt.setText(jsonObject.get("Boarding_Amt").getAsString());
        holder.taTLAmt.setText(jsonObject.get("Ta_totalAmt").getAsString());
        holder.taFaAmt.setText(jsonObject.get("trv_lc_amt").getAsString());
        holder.taLaAmt.setText(jsonObject.get("Ldg_totalAmt").getAsString());
        holder.taLcAmt.setText(jsonObject.get("Lc_totalAmt").getAsString());
        holder.taOeAmt.setText(jsonObject.get("Oe_totalAmt").getAsString());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TAViewAct = new Intent(context, TAViewStatus.class);

                TAViewAct.putExtra("sfCode", Shared_Common_Pref.Sf_Code);
                TAViewAct.putExtra("TA_Date", jsonObject.get("Expdt").getAsString());
                TAViewAct.putExtra("TA_APPROVAL", "0");
                TAViewAct.putExtra("total_amount", jsonObject.get("Total_Amount").getAsString());
                context.startActivity(TAViewAct);
            }
        });
        holder.btnCancel.setVisibility(View.GONE);
        if (jsonObject.get("ApSTatus").getAsString().equalsIgnoreCase("Approval Pending"))
            holder.btnCancel.setVisibility(View.VISIBLE);

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterOnClick.onIntentClick(jsonObject, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return taJsonArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView taDate, taStatus, taTotalAmt, taDaAmt, taTLAmt, taFaAmt, taLaAmt, taLcAmt, taOeAmt;
        CardView mCardView;
        Button btnCancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taDate = (TextView) itemView.findViewById(R.id.ta_date);
            taStatus = (TextView) itemView.findViewById(R.id.ta_status);
            taTotalAmt = (TextView) itemView.findViewById(R.id.ta_amount);
            taDaAmt = (TextView) itemView.findViewById(R.id.txt_da);
            taTLAmt = (TextView) itemView.findViewById(R.id.txt_tvrl_amt);
            taFaAmt = (TextView) itemView.findViewById(R.id.txt_tl);
            taLaAmt = (TextView) itemView.findViewById(R.id.txt_la);
            taLcAmt = (TextView) itemView.findViewById(R.id.txt_lc);
            taOeAmt = (TextView) itemView.findViewById(R.id.txt_oe);
            mCardView = itemView.findViewById(R.id.ta_row_item);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
        }
    }

    public interface OnTAStatusClick {
        void onCancelClick(JsonObject obj);
    }
}
