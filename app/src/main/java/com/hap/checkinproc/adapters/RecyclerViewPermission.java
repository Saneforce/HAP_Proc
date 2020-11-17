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
import com.hap.checkinproc.Model_Class.Permission;
import com.hap.checkinproc.R;

import java.util.List;

public class RecyclerViewPermission extends RecyclerView.Adapter<RecyclerViewPermission.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private List<Permission> permissionList ;
    private Context mContext;

    public RecyclerViewPermission (List<Permission> permissionList, Context mContext) {

        this.permissionList = permissionList;
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
    public void onBindViewHolder(@NonNull RecyclerViewPermission.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(permissionList.get(position).getFieldForceName());
        holder.applied_date.setText(permissionList.get(position).getApplieddate());
        holder.shift_time.setText(permissionList.get(position).getNoofHours());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(mContext, Ta_approval.class);
                i.putExtra("FieldForcename",permissionList.get(position).getFieldForceName());
                i.putExtra("applieddate",permissionList.get(position).getApplieddate());
                i.putExtra("Nofdays",permissionList.get(position).getNoofHours());
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {

        return permissionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
