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
import com.hap.checkinproc.Model_Class.Missed;
import com.hap.checkinproc.Model_Class.Onduty;
import com.hap.checkinproc.R;

import java.util.List;

public class RecyclerViewMissedpun extends RecyclerView.Adapter<RecyclerViewMissedpun.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private List<Missed> missedList ;
    private Context mContext;

    public RecyclerViewMissedpun( List<Missed> missedList, Context mContext) {

        this.missedList = missedList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerViewMissedpun.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_missedpunch, parent, false);
       ViewHolder holder= new ViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewMissedpun.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(missedList.get(position).getSfName());
        holder.applied_date.setText(missedList.get(position).getMissedPunchDate());


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(mContext, Ta_approval.class);
                i.putExtra("FieldForcename",missedList.get(position).getSfName());
                i.putExtra("applieddate",missedList.get(position).getMissedPunchDate());

                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {

        return missedList.size();
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

