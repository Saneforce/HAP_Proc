package com.hap.checkinproc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Model_Class.Tp_Approval_FF_Modal;
import com.hap.checkinproc.R;

import java.util.List;

public class New_TP_Approval_Adapter extends RecyclerView.Adapter<New_TP_Approval_Adapter.MyViewHolder> {
    private List<Tp_Approval_FF_Modal> Tp_Approval_FF_ModalsList;
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


    public New_TP_Approval_Adapter(List<Tp_Approval_FF_Modal> Tp_Approval_FF_ModalsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Tp_Approval_FF_ModalsList = Tp_Approval_FF_ModalsList;
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
    public void onBindViewHolder(New_TP_Approval_Adapter.MyViewHolder holder, int position) {
        Tp_Approval_FF_Modal Tp_Approval_FF_Modal = Tp_Approval_FF_ModalsList.get(position);
        holder.textviewname.setText(Tp_Approval_FF_Modal.getFieldForceName());
        holder.textviewdate.setText(Tp_Approval_FF_Modal.getMonthnameexample()+"-"+Tp_Approval_FF_Modal.getTyear());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Tp_Approval_FF_ModalsList.size();
    }
}