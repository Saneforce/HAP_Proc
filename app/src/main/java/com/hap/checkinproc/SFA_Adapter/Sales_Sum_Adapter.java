package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;
import org.json.JSONArray;
import org.json.JSONObject;

public class Sales_Sum_Adapter extends RecyclerView.Adapter<Sales_Sum_Adapter.ViewHolder>  {
    private static final String TAG = "RecycleItem";
    private JSONArray list = new JSONArray();
    private Context Context;
    ViewHolder holder;

    public Sales_Sum_Adapter(JSONArray list, Context mContext) {
        this.list =list;
        this.Context =mContext;
    }

    @NonNull
    @Override
    public Sales_Sum_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_sum_adapter, parent, false);
        Sales_Sum_Adapter.ViewHolder holder = new Sales_Sum_Adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Sales_Sum_Adapter.ViewHolder holder, int position) {
        JSONObject itm = null;
        try
        {
            itm = list.getJSONObject(position);
            holder.val.setText(itm.getString("GrpName"));
            holder.grpname.setText(itm.getString("Val"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount()
    {
        return list.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView val,grpname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            val=itemView.findViewById(R.id.tv_val);
            grpname=itemView.findViewById(R.id.tv_grpname);


        }
    }
}
