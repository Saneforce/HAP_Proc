package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.OutletApprovActiviy;

import org.json.JSONArray;
import org.json.JSONObject;

public class OutletApprovalAdapter extends RecyclerView.Adapter<OutletApprovalAdapter.MyViewHolder> {
    Context context;
    JSONArray mArr;

    private View listItem;
    int rowlayout;


    public OutletApprovalAdapter(Context context, JSONArray mArr, int rowlayout) {
        this.context = context;
        this.mArr = mArr;
        this.rowlayout = rowlayout;


    }


    @NonNull
    @Override
    public OutletApprovalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        listItem = layoutInflater.inflate(rowlayout, null, false);
        return new OutletApprovalAdapter.MyViewHolder(listItem);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(OutletApprovalAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject obj = mArr.getJSONObject(position);
            holder.tvDate.setText("" + obj.getString("date"));
            holder.tvName.setText("" + obj.getString("name"));
            holder.tvHO.setText("" + obj.getString("ho"));
            holder.tvDes.setText("" + obj.getString("des"));

            holder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OutletApprovActiviy.class);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e("OutletApprovalAdapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mArr.length();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvName, tvDes, tvHO;
        Button btnView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvHO = itemView.findViewById(R.id.tvHO);
            btnView = itemView.findViewById(R.id.btn_View);

        }
    }
}