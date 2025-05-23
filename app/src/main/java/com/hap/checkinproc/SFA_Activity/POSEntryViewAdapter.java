package com.hap.checkinproc.SFA_Activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.HistorySalesInfoAdapter;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Adapter.PosOrder_History_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class POSEntryViewAdapter extends RecyclerView.Adapter<POSEntryViewAdapter.MyViewHolder> {

    Context context;
    List<Product_Details_Modal> mDate;
    Shared_Common_Pref sharedCommonPref;

    public POSEntryViewAdapter(Context context, List<Product_Details_Modal> mDate){

        this.context = context;
        this.mDate = mDate;
        sharedCommonPref = new Shared_Common_Pref(context);
    }


    @Override
    public POSEntryViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.pos_entryview_listitems, null, false);

        return new POSEntryViewAdapter.MyViewHolder(listItem);
    }



    @Override
    public void onBindViewHolder(POSEntryViewAdapter.MyViewHolder holder, int position) {

        try {
            holder.productname.setText(mDate.get(position).getName());
            holder.rate.setText("" + new DecimalFormat("##0.00").format(mDate.get(position).getPrice()));
        } catch (Exception e) {
            Log.e("POSView_Adapter:", e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productname, rate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.txtProd);
            rate = itemView.findViewById(R.id.txtValue);

        }
    }



}
