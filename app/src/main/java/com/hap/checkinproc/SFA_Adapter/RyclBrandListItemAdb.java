package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.onListItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Order_Category_Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RyclBrandListItemAdb extends RecyclerView.Adapter<RyclBrandListItemAdb.ViewHolder> {
    private static final String TAG = "RecycleItem";
    private JSONArray mlist = new JSONArray();
    private Context mContext;
    static onListItemClick itemClick;
    RyclBrandListItemAdb.ViewHolder pholder;

    public RyclBrandListItemAdb(JSONArray mlist, Context mContext, onListItemClick mItemClick) {
        this.mlist = mlist;
        this.mContext = mContext;
        this.itemClick = mItemClick;
    }

    @NonNull
    @Override
    public RyclBrandListItemAdb.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_order_types_ryclv, parent, false);
        RyclBrandListItemAdb.ViewHolder holder = new RyclBrandListItemAdb.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RyclBrandListItemAdb.ViewHolder holder, int position) {

        JSONObject itm = null;
        try {
            itm = mlist.getJSONObject(position);
            holder.icon.setText(itm.getString("name"));

            holder.gridcolor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    JSONObject itm = null;
                    try {
                        itm = mlist.getJSONObject(holder.getAdapterPosition());
                        if (itemClick != null) itemClick.onItemClick(itm);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (pholder != null) {
                        pholder.gridcolor.setBackground(mContext.getResources().getDrawable(R.drawable.cardbutton));
                        pholder.icon.setTextColor(mContext.getResources().getColor(R.color.black));
                        pholder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                    pholder = holder;
                    Order_Category_Select.order_category_select.brandPos = holder.getAdapterPosition();
                    //  showOrderItemList(holder.getAdapterPosition(), "");
                    holder.gridcolor.setBackground(mContext.getDrawable(R.drawable.cardbtnprimary));
                    holder.icon.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                }
            });

            if (position == Order_Category_Select.order_category_select.brandPos) {

                holder.gridcolor.setBackground(mContext.getResources().getDrawable(R.drawable.cardbtnprimary));
                holder.icon.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                pholder = holder;
            } else {
                holder.gridcolor.setBackground(mContext.getResources().getDrawable(R.drawable.cardbutton));
                holder.icon.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            }
            holder.iv.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return mlist.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView icon;
        LinearLayout gridcolor;
        ImageView iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.textView);
            gridcolor = itemView.findViewById(R.id.gridcolor);
            iv=itemView.findViewById(R.id.ivCategoryIcon);

        }
    }
}