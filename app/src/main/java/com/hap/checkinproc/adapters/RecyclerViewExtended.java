package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity_Hap.Ta_approval;
import com.hap.checkinproc.Model_Class.Extended;
import com.hap.checkinproc.Model_Class.Missed;
import com.hap.checkinproc.R;

import java.util.List;

public class RecyclerViewExtended extends RecyclerView.Adapter<RecyclerViewExtended.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private List<Extended> extendedList ;
    private Context mContext;

    public RecyclerViewExtended( List<Extended> extendedList, Context mContext) {

        this.extendedList = extendedList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerViewExtended.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_missedpunch, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewExtended.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(extendedList.get(position).getSfName());
        holder.applied_date.setText(extendedList.get(position).getEntrydate());


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(mContext, Ta_approval.class);
                i.putExtra("FieldForcename",extendedList.get(position).getSfName());
                i.putExtra("applieddate",extendedList.get(position).getEntrydate());

                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {

        return extendedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView applied_date;
        LinearLayout parentLayout;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.odname);
            applied_date=itemView.findViewById(R.id.oddate);
            parentLayout=itemView.findViewById(R.id.onduty_lay);
            button=itemView.findViewById(R.id.buttonod);
        }
    }
}

