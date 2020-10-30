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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity_Hap.Ta_approval;
import com.hap.checkinproc.Model_Class.Approval;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewLeaveApproval extends RecyclerView.Adapter<RecyclerViewLeaveApproval.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private List<Approval> approvalList ;
    private Context mContext;

    public RecyclerViewLeaveApproval (List<Approval> approvalList, Context mContext) {

        this.approvalList = approvalList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_approval, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(approvalList.get(position).getFieldForceName());
        holder.applied_date.setText(approvalList.get(position).getApplieddate());
        holder.shift_time.setText(""+approvalList.get(position).getLeaveDays());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(mContext, Ta_approval.class);
                i.putExtra("FieldForcename",approvalList.get(position).getFieldForceName());
                i.putExtra("applieddate",approvalList.get(position).getApplieddate());
                i.putExtra("Nofdays",approvalList.get(position).getLeaveDays());
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {

        return approvalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView shift_time;
        TextView name;
        TextView applied_date;
        LinearLayout parentLayout;
      Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            applied_date=itemView.findViewById(R.id.applied_date);
            shift_time=itemView.findViewById(R.id.leave_days);
            parentLayout=itemView.findViewById(R.id.leaveapproval);
            button=itemView.findViewById(R.id.leave_open);
        }
    }
}
