package com.hap.checkinproc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Model_Class.Tp_Approval_Model;
import com.hap.checkinproc.R;

import java.util.List;

public class Tp_Approval_Adapter extends RecyclerView.Adapter<Tp_Approval_Adapter.MyViewHolder> {

    private List<Tp_Approval_Model> Tp_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open;

        public MyViewHolder(View view) {
            super(view);
            textviewname = (TextView) view.findViewById(R.id.textviewname);
            textviewdate = (TextView) view.findViewById(R.id.textviewdate);
            open = (TextView) view.findViewById(R.id.open);
        }
    }


    public Tp_Approval_Adapter(List<Tp_Approval_Model> Tp_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Tp_Approval_ModelsList = Tp_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });*/
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tp_Approval_Model Tp_Approval_Model = Tp_Approval_ModelsList.get(position);
        holder.textviewname.setText(Tp_Approval_Model.getFieldForceName());
        holder.textviewdate.setText(Tp_Approval_Model.getDate());

        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAdapterOnClick.onIntentClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return Tp_Approval_ModelsList.size();
    }
}