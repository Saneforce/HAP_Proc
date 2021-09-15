package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hap.checkinproc.Activity_Hap.Cumulative_Order_Model;
import com.hap.checkinproc.R;

import java.util.List;

public class OutletDashboardInfoAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    ImageView ivCategoryIcon;
    TextView tvDesc, tvValue;
    private List<Cumulative_Order_Model> listt;

    public OutletDashboardInfoAdapter(Context applicationContext, List<Cumulative_Order_Model> list) {
        try {


            this.context = applicationContext;
            this.listt = list;
            inflter = (LayoutInflater.from(applicationContext));
        } catch (Exception e) {
            Log.e("adapter:construct ", e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return listt.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.outlet_dashboardinfo_recyclerview, null); // inflate the layout

        try {

            tvDesc = view.findViewById(R.id.tvDesc);
            tvValue = view.findViewById(R.id.tvValue);


            tvDesc.setText("" + listt.get(i).getDesc());
            tvValue.setText("" + listt.get(i).getValue());
        } catch (Exception e) {
            Log.e("adaptergetView: ", e.getMessage());
        }
        return view;
    }
}
