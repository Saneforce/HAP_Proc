package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Model_Class.View_All_Model;

import java.util.List;

public class ViewAll_Status_Adapter extends RecyclerView.Adapter<ViewAll_Status_Adapter.MyViewHolder> {

    private List<View_All_Model> View_Status_ModelsList;
    private int rowLayout;
    private Context context;

    String AMod;

    public ViewAll_Status_Adapter(List<View_All_Model> View_Status_ModelsList, int rowLayout, Context context, String AMod) {
        this.View_Status_ModelsList = View_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @Override
    public ViewAll_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewAll_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewAll_Status_Adapter.MyViewHolder holder, int position) {
        View_All_Model View_Status_Model = View_Status_ModelsList.get(position);
        GradientDrawable drawable = (GradientDrawable) holder.wkstatus.getBackground();
        holder.weekofdate.setText(View_Status_ModelsList.get(position).getWrkDate());
        holder.wkstatus.setText(View_Status_ModelsList.get(position).getDayStatus());
        holder.shifttime.setText(View_Status_ModelsList.get(position).getSFTName().toString());
        holder.txt_in_time.setText(View_Status_ModelsList.get(position).getAttTm().toString());
        holder.txt_out_time.setText(View_Status_ModelsList.get(position).getET().toString());
        String color=View_Status_Model.getStusClr().replace("!important", "");
        drawable.setColor(Color.parseColor(color.trim()));
    }

    @Override
    public int getItemCount() {
        return View_Status_ModelsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView weekofdate, wkstatus, shifttime, txt_in_time, txt_out_time;

        public MyViewHolder(View view) {
            super(view);
            weekofdate = view.findViewById(R.id.weekofdate);
            wkstatus = view.findViewById(R.id.wkstatus);
            shifttime = view.findViewById(R.id.shifttime);
            txt_in_time = view.findViewById(R.id.txt_in_time);
            txt_out_time = view.findViewById(R.id.txt_out_time);

        }
    }

}