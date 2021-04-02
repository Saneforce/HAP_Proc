package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Route_Product_Info;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.util.List;

public class Route_View_Adapter extends RecyclerView.Adapter<Route_View_Adapter.MyViewHolder> {

    private List<Retailer_Modal_List> Retailer_Modal_Listitem;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, status, invoice, values, invoicedate;
        LinearLayout parent_layout;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.retailername);
            parent_layout = view.findViewById(R.id.parent_layout);
            status = view.findViewById(R.id.status);
            invoice = view.findViewById(R.id.invoice);
            values = view.findViewById(R.id.values);
            invoicedate = view.findViewById(R.id.invoicedate);
        }
    }


    public Route_View_Adapter(List<Retailer_Modal_List> Retailer_Modal_Listitem, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Retailer_Modal_Listitem = Retailer_Modal_Listitem;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public Route_View_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Route_View_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Route_View_Adapter.MyViewHolder holder, int position) {
        Retailer_Modal_List Retailer_Modal_List = Retailer_Modal_Listitem.get(position);
        holder.textviewname.setText("" + Retailer_Modal_List.getName().toUpperCase() + "~" + Retailer_Modal_List.getId());
        if (Retailer_Modal_List.getStatusname() != null) {
            holder.status.setText("Status :" + "\t\t" + Retailer_Modal_List.getStatusname().toUpperCase());
        } else {
            holder.status.setText("Status :" + "\t\t" + "");
        }

        holder.invoice.setText("Last invoice value :" + "\t\t" + Retailer_Modal_List.getInvoiceValues());
        holder.values.setText("Value :" + "\t\t" + Retailer_Modal_List.getValuesinv());
        holder.invoicedate.setText("Last invoice date :" + "\t\t" + Retailer_Modal_List.getInvoiceDate());
        if (Retailer_Modal_List.getInvoice_Flag().equals("0")) {
            holder.parent_layout.setBackgroundResource(R.color.white);
        } else if (Retailer_Modal_List.getInvoice_Flag().equals("1")) {
            holder.parent_layout.setBackgroundResource(R.color.invoiceordercolor);
        } else {
            holder.parent_layout.setBackgroundResource(R.color.greeninvoicecolor);
        }
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Retailer_Modal_Listitem.size();
    }
}