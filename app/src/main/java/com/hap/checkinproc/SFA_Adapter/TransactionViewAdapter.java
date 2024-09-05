package com.hap.checkinproc.SFA_Adapter;

import static com.hap.checkinproc.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Model_Class.VanInvTransactionModel;
import com.hap.checkinproc.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class TransactionViewAdapter extends RecyclerView.Adapter<TransactionViewAdapter.MyViewHolder>{
    Context context;
    List<VanInvTransactionModel.Transaction> transactionList;
    NumberFormat formatter = new DecimalFormat("##0.00");
    public TransactionViewAdapter(Context context1,List<VanInvTransactionModel.Transaction> transactionListNew){
        context=context1;
        transactionList=transactionListNew;
    }
    @NonNull
    @Override
    public TransactionViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_van_transaction_detail, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewAdapter.MyViewHolder holder, int position) {
     VanInvTransactionModel.Transaction transaction=transactionList.get(position);
     holder.tvRecAmt.setText(""+CurrencySymbol+" "+formatter.format(transaction.getRecAmt()));
     holder.tvBalAmt.setText(""+CurrencySymbol+" "+formatter.format(transaction.getBalanceAmt()));
     holder.tvPayDate.setText(""+transaction.getPayDate());
     holder.tvPayMode.setText(""+transaction.getPayMode());

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecAmt,tvPayDate,tvPayMode,tvBalAmt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecAmt=itemView.findViewById(R.id.tvRecAmt);
            tvPayDate=itemView.findViewById(R.id.tvPayDate);
            tvPayMode=itemView.findViewById(R.id.tvPayMode);
            tvBalAmt=itemView.findViewById(R.id.tvBalAmt);
        }
    }
}
