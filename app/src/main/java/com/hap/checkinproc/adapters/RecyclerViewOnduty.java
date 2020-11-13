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
import com.hap.checkinproc.Model_Class.Approval;
import com.hap.checkinproc.Model_Class.Onduty;
import com.hap.checkinproc.R;

import java.util.List;

public class RecyclerViewOnduty extends RecyclerView.Adapter<RecyclerViewOnduty.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private List<Onduty> onduties ;
    private Context mContext;

    public RecyclerViewOnduty ( List<Onduty> onduties, Context mContext) {

        this.onduties = onduties;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerViewOnduty.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_missedpunch, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewOnduty.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(onduties.get(position).getFieldForceName());
        holder.applied_date.setText(onduties.get(position).getLoginDate());


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(mContext, Ta_approval.class);
                i.putExtra("FieldForcename",onduties.get(position).getFieldForceName());
                i.putExtra("applieddate",onduties.get(position).getLoginDate());

                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {

        return onduties.size();
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

