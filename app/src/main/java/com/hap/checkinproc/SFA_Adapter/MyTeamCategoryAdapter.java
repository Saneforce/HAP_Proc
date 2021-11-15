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

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.MyTeamActivity;

import org.json.JSONArray;

public class MyTeamCategoryAdapter extends RecyclerView.Adapter<MyTeamCategoryAdapter.MyViewHolder> {
    JSONArray AryDta;
    private Context context;
    Common_Class common_class;
    int salRowDetailLayout;
    MyTeamCategoryAdapter.MyViewHolder pholder;

    public MyTeamCategoryAdapter(JSONArray jAryDta, int rowLayout, Context mContext) {
        AryDta = jAryDta;
        context = mContext;
        salRowDetailLayout = rowLayout;
        common_class = new Common_Class(context);
    }

    @NonNull
    @Override
    public MyTeamCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(salRowDetailLayout, parent, false);
        return new MyTeamCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTeamCategoryAdapter.MyViewHolder holder, int position) {
        try {
            String itm = AryDta.getString(position);
            holder.txCatname.setText(itm);

            holder.llTeamType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        MyTeamActivity.myTeamActivity.getTeamLoc(itm);


                        if (pholder != null) {
                            pholder.ivIcon.setColorFilter(context.getResources().getColor(R.color.grey_600));
                            pholder.txCatname.setTextColor(context.getResources().getColor(R.color.grey_800));
                            pholder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        }
                        pholder = holder;
                        MyTeamActivity.selectedPos = position;
                        holder.ivIcon.setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark));
                        holder.txCatname.setTextColor(context.getResources().getColor(R.color.black));
                        holder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                    } catch (Exception e) {

                    }
                }
            });

            if (position == MyTeamActivity.selectedPos) {

                holder.ivIcon.setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark));
                holder.txCatname.setTextColor(context.getResources().getColor(R.color.black));
                holder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                pholder = holder;
            } else {
                holder.ivIcon.setColorFilter(context.getResources().getColor(R.color.grey_600));
                holder.txCatname.setTextColor(context.getResources().getColor(R.color.grey_800));
                holder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUI(MyTeamCategoryAdapter.MyViewHolder holder, int pos) {
        if (pos == MyTeamActivity.selectedPos) {

            holder.ivIcon.setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.txCatname.setTextColor(context.getResources().getColor(R.color.black));
            holder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        } else {
            holder.ivIcon.setColorFilter(context.getResources().getColor(R.color.grey_600));
            holder.txCatname.setTextColor(context.getResources().getColor(R.color.grey_800));
            holder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        }

    }

    @Override
    public int getItemCount() {
        return AryDta.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txCatname;
        LinearLayout llTeamType;
        ImageView ivIcon;

        public MyViewHolder(View view) {
            super(view);
            txCatname = view.findViewById(R.id.tvCategoryName);
            llTeamType = view.findViewById(R.id.llTeamType);
            ivIcon = view.findViewById(R.id.ivCategoryIcon);

        }
    }
}
