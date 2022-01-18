package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Outlet_Info_Activity;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.util.List;

public class Outlet_Info_Adapter extends RecyclerView.Adapter<Outlet_Info_Adapter.MyViewHolder> {

    private List<Retailer_Modal_List> Retailer_Modal_Listitem;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;
    public static int size = 0;
    String activityName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, txRetNo, status, invoice, outletAddress, textId, clsdRmks, txCustStatus, lupdDt;
        public LinearLayout retStaBdg, icAC, layparent;
        Button btnSend;
        EditText etSNo;
        RelativeLayout rlSeqParent;

        public MyViewHolder(View view) {
            super(view);

            layparent = view.findViewById(R.id.layparent);
            textviewname = view.findViewById(R.id.retailername);
            textId = view.findViewById(R.id.txCustID);
            clsdRmks = view.findViewById(R.id.txClsdRmks);
            outletAddress = view.findViewById(R.id.ShopAddr);
            txCustStatus = view.findViewById(R.id.txCustStatus);
            retStaBdg = view.findViewById(R.id.retStaBdg);
            status = view.findViewById(R.id.status);
            invoice = view.findViewById(R.id.invoice);
            icAC = view.findViewById(R.id.icAC);
            lupdDt = view.findViewById(R.id.lupdDt);
            btnSend = view.findViewById(R.id.btnSend);
            etSNo = view.findViewById(R.id.etSNo);
            txRetNo = view.findViewById(R.id.txRetNo);
            rlSeqParent = view.findViewById(R.id.rlSequence);
        }
    }


    public Outlet_Info_Adapter(List<Retailer_Modal_List> Retailer_Modal_Listitem, int rowLayout, Context context,
                               String activityName, AdapterOnClick mAdapterOnClick) {
        this.Retailer_Modal_Listitem = Retailer_Modal_Listitem;
        this.rowLayout = rowLayout;
        this.context = context;
        this.activityName = activityName;
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

        if (activityName.equalsIgnoreCase("Outlets")) {
            holder.rlSeqParent.setVisibility(View.VISIBLE);
            if (size < Retailer_Modal_Listitem.size()) {
                size = Retailer_Modal_Listitem.size();
            }
        }

        holder.textviewname.setText("" + Retailer_Modal_List.getName().toUpperCase());
        // holder.textId.setText("" + Retailer_Modal_List.getId());
        holder.textId.setText("" + Retailer_Modal_List.getERP_Code());
        holder.outletAddress.setText("" + Retailer_Modal_List.getListedDrAddress1());
        holder.clsdRmks.setText("" + Retailer_Modal_List.getClosedRemarks());
        holder.lupdDt.setText((Retailer_Modal_List.getLastUpdt_Date().equalsIgnoreCase("")) ? "" : "Last Updated On " + Retailer_Modal_List.getLastUpdt_Date());
        holder.icAC.setVisibility(View.INVISIBLE);
        if (Retailer_Modal_List.getDelivType() != null && Retailer_Modal_List.getDelivType().equalsIgnoreCase("AC")) {
            holder.icAC.setVisibility(View.VISIBLE);
        }
        holder.txCustStatus.setText((typ.equalsIgnoreCase("3") ? "Duplicate" : typ.equalsIgnoreCase("2") ? "Closed" : (typ.equalsIgnoreCase("1") ? "Service" : "Non Service")));

        switch (typ) {
            case "1":
                holder.retStaBdg.setBackground(context.getDrawable(R.drawable.button_green));
                break;
            case "2":
                holder.retStaBdg.setBackground(context.getDrawable(R.drawable.round_status_blue));
                break;
            case "3":
                holder.retStaBdg.setBackground(context.getDrawable(R.drawable.button_yellows));
                break;
            default:
                holder.retStaBdg.setBackground(context.getDrawable(R.drawable.button_blueg));
                break;
        }

        holder.layparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(holder.getAdapterPosition());
            }
        });

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.etSNo.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(context, "Enter Delivery Sequence", Toast.LENGTH_SHORT).show();

                } else {
                    int val = Integer.parseInt(holder.etSNo.getText().toString());
                    if (val <= size)
                        Outlet_Info_Activity.outlet_info_activity.submitSeqNo(val, Retailer_Modal_List.getId());
                    else
                        Toast.makeText(context, "Invalid Delivery Sequence", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.txRetNo.setText("" + Retailer_Modal_List.getListedDrSlNo().toString());

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