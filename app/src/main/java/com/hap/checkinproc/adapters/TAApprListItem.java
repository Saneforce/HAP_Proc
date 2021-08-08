package com.hap.checkinproc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.onPayslipItemClick;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TAApprListItem extends RecyclerView.Adapter<TAApprListItem.ViewHolder> {
    private static final String TAG = "ShiftList";
    private JSONArray mlist = new JSONArray();
    private Context mContext;
    static onPayslipItemClick payClick;
    public TAApprListItem(JSONArray mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TAApprListItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ta_appr, parent, false);
        TAApprListItem.ViewHolder holder = new TAApprListItem.ViewHolder(view);
        return holder;

    }
    public static void SetPayOnClickListener(onPayslipItemClick mPayClick){
        payClick=mPayClick;
    }
    @Override
    public void onBindViewHolder(@NonNull TAApprListItem.ViewHolder holder, int position) {

        JSONObject itm = null;
        try {
            itm = mlist.getJSONObject(position);
            holder.txEMPNm.setText(itm.getString("name"));
            holder.txEMPDesig.setText(itm.getString("value"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return mlist.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txEMPNm,txEMPDesig;
        LinearLayout parentLayout;
        //CardView secondarylayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txEMPNm = itemView.findViewById(R.id.txEmpName);
            txEMPDesig = itemView.findViewById(R.id.txDesgName);
            parentLayout = itemView.findViewById(R.id.parent_layout);

            //secondarylayout=itemView.findViewById(R.id.secondary_layout);
        }
    }
}
