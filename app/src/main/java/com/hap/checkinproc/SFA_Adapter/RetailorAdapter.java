package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.util.List;

public class RetailorAdapter extends RecyclerView.Adapter<RetailorAdapter.MyViewHolder> {
    private List<Retailer_Modal_List> Retailer_Modal_Listitem;

    Context mContext;

    Shared_Common_Pref shared_common_pref;
    private List<Retailer_Modal_List> tdList;


    public RetailorAdapter(List<Retailer_Modal_List> myDataset, Context context) {
        Retailer_Modal_Listitem = myDataset;
        mContext = context;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_firstmnth_info, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder fruitViewHolder, final int position) {

        Retailer_Modal_List Retailer_Modal_List = Retailer_Modal_Listitem.get(position);

        if (!shared_common_pref.getvalue(Constants.RetailorTodayDataDynamic).equals("")) {
            if (tdList != null && tdList.size() > 0) {
                boolean isHaveToday=false;

                for (int i = 0; i < tdList.size(); i++) {
                    if (tdList.get(i).getCust_Code().equals(Retailer_Modal_List.getId())) {
                        isHaveToday = true;

//                        holder.tvTdMilkVal.setText("" + tdList.get(i).getMilk() + "|₹" + tdList.get(i).getMilkVal());
//                        holder.tvTdCurdVal.setText("" + tdList.get(i).getCurd() + "|₹" + tdList.get(i).getCurdVal());
//                        holder.tvTdOtherVal.setText("" + tdList.get(i).getOthers() + "|₹" + tdList.get(i).getOthersVal());
//
//                        holder.tvTdTotVal.setText("" + (tdList.get(i).getMilk() + tdList.get(i).getCurd() + tdList.get(i).getOthers()) + "|₹" + (
//                                tdList.get(i).getMilkVal() + tdList.get(i).getCurdVal() + tdList.get(i).getOthersVal()));
                    }
                }

                if (!isHaveToday) {
//                    holder.tvTdMilkVal.setText("0|₹0");
//                    holder.tvTdCurdVal.setText("0|₹0");
//                    holder.tvTdOtherVal.setText("0|₹0");
//
//                    holder.tvTdTotVal.setText("0|₹0");
                } else {


                }


            }

        }





    }

    @Override
    public int getItemCount() {
        return Retailer_Modal_Listitem.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductValue;

        public MyViewHolder(View v) {
            super(v);
            tvProductName = v.findViewById(R.id.tvProductName);
            tvProductValue = v.findViewById(R.id.tvProductValue);

        }


    }



}
