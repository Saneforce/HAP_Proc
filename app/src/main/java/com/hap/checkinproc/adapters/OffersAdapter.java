package com.hap.checkinproc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Interface.onListItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.RyclBrandListItemAdb;

import org.json.JSONArray;
import org.json.JSONException;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder>{
    private static final String TAG = "OfferItems";
    private JSONArray mList = new JSONArray();
    private Context mContext;
    static onListItemClick itemClick;
    RyclBrandListItemAdb.ViewHolder pholder;
    Common_Class common_class;
    public OffersAdapter(JSONArray mlist, Context mContext, onListItemClick mItemClick) {
        this.mList = mlist;
        this.mContext = mContext;
        this.itemClick = mItemClick;
        common_class = new Common_Class(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_ryclv_adb, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String sImgPath=mList.getJSONObject(position).getString("offerimg");
            if (sImgPath!="") {
                holder.ivOfferImg.clearColorFilter();
                Glide.with(mContext)
                        .load(sImgPath)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.ivOfferImg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.length();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOfferImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOfferImg = itemView.findViewById(R.id.ivOfferImg);

        }
    }
}
