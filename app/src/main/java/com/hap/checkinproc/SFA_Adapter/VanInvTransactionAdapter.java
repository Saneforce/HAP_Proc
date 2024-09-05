package com.hap.checkinproc.SFA_Adapter;

import static com.hap.checkinproc.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Model_Class.VanInvTransactionModel;
import com.hap.checkinproc.R;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class VanInvTransactionAdapter extends RecyclerView.Adapter<VanInvTransactionAdapter.MyViewHolder>{
    Context context;
    List<VanInvTransactionModel> vanInvTransactionModelList;
    NumberFormat formatter = new DecimalFormat("##0.00");

    public VanInvTransactionAdapter(Context context1,List<VanInvTransactionModel> vanInvTransactionList){
        context=context1;
        vanInvTransactionModelList=vanInvTransactionList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_van_transaction, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VanInvTransactionModel vanModel=vanInvTransactionModelList.get(position);
        holder.tvInvDate.setText(""+vanModel.getInvoiceDate());
        holder.tvInvNo.setText(""+vanModel.getInvoiceNo());
        holder.tvInvVal.setText(""+CurrencySymbol+" "+formatter.format(vanModel.getInvoiceVal()));
        if(vanModel.getTransactionList().size()>0){
            holder.ll_head.setVisibility(View.VISIBLE);
            holder.rvTransactionDet.setVisibility(View.VISIBLE);
            holder.view1.setVisibility(View.VISIBLE);
            holder.view2.setVisibility(View.VISIBLE);
           holder.view3.setVisibility(View.VISIBLE);
           double amt=0;
           for( int i=0;i<vanModel.getTransactionList().size();i++){
               amt+=vanModel.getTransactionList().get(i).getRecAmt();
           }
           holder.tvoutStand.setText(""+CurrencySymbol+"-"+formatter.format(vanModel.getInvoiceVal()-amt));

            TransactionViewAdapter adapter = new TransactionViewAdapter(context, vanModel.getTransactionList());
            holder.rvTransactionDet.setAdapter(adapter);
        }else{
            holder.ll_head.setVisibility(View.GONE);
            holder.rvTransactionDet.setVisibility(View.GONE);
            holder.view1.setVisibility(View.INVISIBLE);
          //  holder.view1.setVisibility(View.GONE);
            holder.view2.setVisibility(View.GONE);
            holder.view3.setVisibility(View.VISIBLE);
            holder.tvoutStand.setText(""+CurrencySymbol+"-"+formatter.format(vanModel.getInvoiceVal()));
        }

    }

    @Override
    public int getItemCount() {
        return vanInvTransactionModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvDate,tvInvNo,tvInvVal,tvoutStand;
        LinearLayout ll_head;
        RecyclerView rvTransactionDet;
        View view1,view2,view3;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            tvInvDate=itemView.findViewById(R.id.tvInvDate);
            tvInvNo=itemView.findViewById(R.id.tvInvNo);
            tvInvVal=itemView.findViewById(R.id.tvInvVal);
            ll_head=itemView.findViewById(R.id.ll_head);
            rvTransactionDet=itemView.findViewById(R.id.rvTransactionView);
            view1=itemView.findViewById(R.id.view1);
            view2=itemView.findViewById(R.id.view2);
            view3=itemView.findViewById(R.id.view3);
            tvoutStand=itemView.findViewById(R.id.tvoutStand);
        }
    }
}
