package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.util.List;

public class Outlet_Info_Adapter extends RecyclerView.Adapter<Outlet_Info_Adapter.MyViewHolder> {

    private List<Retailer_Modal_List> Retailer_Modal_Listitem;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, status, invoice, outletAddress, textId, clsdRmks, txCustStatus;
        //ImageView ;
        public LinearLayout retStaBdg, icAC;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.retailername);
            textId = view.findViewById(R.id.txCustID);
            clsdRmks = view.findViewById(R.id.txClsdRmks);
            outletAddress = view.findViewById(R.id.ShopAddr);
            txCustStatus = view.findViewById(R.id.txCustStatus);
            retStaBdg = view.findViewById(R.id.retStaBdg);
            status = view.findViewById(R.id.status);
            invoice = view.findViewById(R.id.invoice);
            icAC = view.findViewById(R.id.icAC);
        }
    }


    public Outlet_Info_Adapter(List<Retailer_Modal_List> Retailer_Modal_Listitem, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Retailer_Modal_Listitem = Retailer_Modal_Listitem;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public Outlet_Info_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Outlet_Info_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Outlet_Info_Adapter.MyViewHolder holder, int position) {
        Retailer_Modal_List Retailer_Modal_List = Retailer_Modal_Listitem.get(position);
        String typ = Retailer_Modal_List.getType() == null ? "0" : Retailer_Modal_List.getType();

        holder.textviewname.setText("" + Retailer_Modal_List.getName().toUpperCase());
        // holder.textId.setText("" + Retailer_Modal_List.getId());
        holder.textId.setText("" + Retailer_Modal_List.getERP_Code());
        holder.outletAddress.setText("" + Retailer_Modal_List.getListedDrAddress1());
        holder.clsdRmks.setText("" + Retailer_Modal_List.getClosedRemarks());
        holder.icAC.setVisibility(View.INVISIBLE);
        if (Retailer_Modal_List.getDelivType() != null && Retailer_Modal_List.getDelivType().equalsIgnoreCase("AC")) {
            holder.icAC.setVisibility(View.VISIBLE);
        }
        holder.txCustStatus.setText((typ.equalsIgnoreCase("2") ? "Closed" : (typ.equalsIgnoreCase("1") ? "Service" : "Universal")));
        if (typ.equalsIgnoreCase("2")) {
            holder.retStaBdg.setBackground(context.getDrawable(R.drawable.round_status_blue));
        } else if (typ.equalsIgnoreCase("1")) {
            holder.retStaBdg.setBackground(context.getDrawable(R.drawable.button_green));
        } else {
            holder.retStaBdg.setBackground(context.getDrawable(R.drawable.button_blueg));
        }
        holder.textviewname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (Retailer_Modal_Listitem != null) {
            return Retailer_Modal_Listitem.size();
        } else {
            return 0;
        }
    }
}