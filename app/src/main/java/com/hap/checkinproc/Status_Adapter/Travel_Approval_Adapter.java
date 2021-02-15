package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Model_Class.Leave_Approval_Model;
import com.hap.checkinproc.Model_Class.Travel_Approval_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.Leave_Approval_Adapter;

import java.util.List;

public class Travel_Approval_Adapter extends RecyclerView.Adapter<Travel_Approval_Adapter.MyViewHolder> {


    private List<Travel_Approval_Model> Travel_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;


    public Travel_Approval_Adapter(List<Travel_Approval_Model> leave_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        Travel_Approval_ModelsList = leave_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });

        return new Travel_Approval_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Travel_Approval_Model travel_approval_model = Travel_Approval_ModelsList.get(position);
        holder.textviewname.setText(travel_approval_model.getSfName());
        holder.textviewdate.setText(travel_approval_model.getId());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Travel_Approval_ModelsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, leavedays;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textviewname = itemView.findViewById(R.id.textviewname);
            textviewdate = itemView.findViewById(R.id.textviewdate);
            open = itemView.findViewById(R.id.open);
            leavedays = itemView.findViewById(R.id.leavedays);
        }
    }
}
