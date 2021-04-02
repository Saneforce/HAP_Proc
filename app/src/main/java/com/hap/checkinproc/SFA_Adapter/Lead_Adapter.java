package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.util.List;

public class Lead_Adapter extends RecyclerView.Adapter<Lead_Adapter.MyViewHolder> {
    private List<Retailer_Modal_List> Retailer_Modal_Listitem;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView outletname, Outlet_Code, Productcategory, Productnamedate, outletAddress, invoice;
        public MyViewHolder(View view) {
            super(view);
            outletname = view.findViewById(R.id.outletname);
            Outlet_Code = view.findViewById(R.id.Outlet_Code);
            Productcategory = view.findViewById(R.id.Productcategory);
            Productnamedate = view.findViewById(R.id.Productnamedate);
            outletAddress = view.findViewById(R.id.outletAddress);
        }
    }


    public Lead_Adapter(List<Retailer_Modal_List> Retailer_Modal_Listitem, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Retailer_Modal_Listitem = Retailer_Modal_Listitem;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public Lead_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Lead_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Lead_Adapter.MyViewHolder holder, int position) {
        Retailer_Modal_List Retailer_Modal_List = Retailer_Modal_Listitem.get(position);
        holder.outletname.setText("" + Retailer_Modal_List.getName().toUpperCase());
        holder.Outlet_Code.setText("Outlet Code:" + "\t\t" + Retailer_Modal_List.getId());
        holder.Productcategory.setText("PRODUCT:" + "\t\t" + "FRESH");
        holder.Productnamedate.setText("" + Retailer_Modal_List.getId());
        holder.outletAddress.setText("" + Retailer_Modal_List.getListedDrAddress1());
    }

    @Override
    public int getItemCount() {
        return Retailer_Modal_Listitem.size();
    }
}
