package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class POPStatusAdapter extends RecyclerView.Adapter<POPStatusAdapter.MyViewHolder> {
    Context context;

    Shared_Common_Pref shared_common_pref;


    POPMaterialAdapterSample popMaterialAdapter;

    JSONArray jsonArray;

    public POPStatusAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
        shared_common_pref = new Shared_Common_Pref(context);

    }

    @NonNull
    @Override
    public POPStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_popstatus_layout, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(POPStatusAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject itm = jsonArray.getJSONObject(position);

            holder.sNo.setText("" + itm.getString("SlNo"));
            holder.requestNo.setText("" + itm.getString("Requset_No"));
            holder.bookingDate.setText("" + itm.getString("Booking_Date"));


            List<QPS_Modal> statusList = new ArrayList<>();
            JSONArray arr = itm.getJSONArray("Details");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(position);
                statusList.add(new QPS_Modal(obj.getString("POP_ID"), obj.getString("POP_Status"), obj.getString("Received_Date"), obj.getString("Images")
                        , obj.getString("POP_Req_ID"), obj.getString("Trans_Sl_No")));
            }

//            holder.receivedDate.setText("" + itm.getString("Received_Date"));
//            holder.status.setText("" + itm.getString("POP_Status"));
//            holder.materialName.setText("" + itm.getString("POP_ID"));
            //    String images = itm.getString("Images");
            popMaterialAdapter = new POPMaterialAdapterSample(context, statusList);

            //  popMaterialAdapter = new POPMaterialAdapter(context, itm.getJSONArray("Details"));

//            popMaterialAdapter = new POPMaterialAdapter(context, itm.getJSONArray("Details"));

            holder.rvMaterials.setAdapter(popMaterialAdapter);


        } catch (Exception e) {
            Log.e("POPStatusAdapter:", e.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sNo, requestNo, bookingDate;
        RecyclerView rvMaterials;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sNo = itemView.findViewById(R.id.tvQpsSno);
            requestNo = itemView.findViewById(R.id.tvQPSReqNo);
            bookingDate = itemView.findViewById(R.id.tvBookingDate);
            rvMaterials = itemView.findViewById(R.id.rvMaterials);

        }
    }
}