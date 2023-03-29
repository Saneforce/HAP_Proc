package com.hap.checkinproc.SFA_Activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Model_Class.ReportModel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class POSEntryViewAdapter extends RecyclerView.Adapter<POSEntryViewAdapter.MyViewHolder>  {
    Context context;
    List<Product_Details_Modal> listt;

    public POSEntryViewAdapter(Context applicationContext, List<Product_Details_Modal> list) {
        this.context = applicationContext;
        this.listt = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pos_entryview_listitems, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.rate.setText(""+listt.get(position).getTotal());
        holder.date.setText(listt.get(position).getEntryDate());

        Log.e("ertgyhujikl",listt.get(position).getSlno());
    }

    @Override
    public int getItemCount() {
        return listt.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date,rate;


        public MyViewHolder(View view) {
            super(view);
            date= itemView.findViewById(R.id.tvEntryDate);
            rate = itemView.findViewById(R.id.txtValue);

        }
    }
}

