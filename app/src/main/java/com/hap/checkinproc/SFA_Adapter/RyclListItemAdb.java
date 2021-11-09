package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.onListItemClick;
import com.hap.checkinproc.Interface.onPayslipItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.HAPListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RyclListItemAdb  extends RecyclerView.Adapter<RyclListItemAdb.ViewHolder> {
    private static final String TAG = "RecycleItem";
    private JSONArray mlist = new JSONArray();
    private Context mContext;
    static onListItemClick itemClick;
    public RyclListItemAdb(JSONArray mlist, Context mContext, onListItemClick mItemClick) {
        this.mlist = mlist;
        this.mContext = mContext;
        this.itemClick=mItemClick;
    }

    @NonNull
    @Override
    public RyclListItemAdb.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_order_horizantal_universe_gridview, parent, false);
        RyclListItemAdb.ViewHolder holder = new RyclListItemAdb.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RyclListItemAdb.ViewHolder holder, int position) {

        JSONObject itm = null;
        try {
            itm = mlist.getJSONObject(position);
            holder.shift_time.setText(itm.getString("name"));

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    JSONObject itm = null;
                    try {
                        itm = mlist.getJSONObject(holder.getAdapterPosition());
                        if(itemClick!=null) itemClick.onItemClick(itm);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return mlist.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shift_time;
        LinearLayout parentLayout;
        //CardView secondarylayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shift_time = itemView.findViewById(R.id.textView);
            parentLayout = itemView.findViewById(R.id.gridcolor);
            //secondarylayout=itemView.findViewById(R.id.secondary_layout);
        }
    }
}