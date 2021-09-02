package com.hap.checkinproc.Activity_Hap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.OutletDashboardInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardOutletDataFrag extends Fragment {
    String mTabName = "";
    View view;
    Context mContext;
    GridView recyclerView;

    LinearLayout llGridParent;

    OutletDashboardInfoAdapter cumulativeInfoAdapter;
    private List<Cumulative_Order_Model> cumulative_order_modelList = new ArrayList<>();


    public DashboardOutletDataFrag(Context context, String TabName) {
        // Required empty public constructor
        this.mContext = context;
        this.mTabName = TabName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for mContext fragment
        view = inflater.inflate(R.layout.fragment_outletdata_info, container, false);

        recyclerView = view.findViewById(R.id.gvOutlet);

        llGridParent = view.findViewById(R.id.lin_gridOutlet);


        cumulative_order_modelList.clear();


        cumulative_order_modelList.add(new Cumulative_Order_Model("DSO", "800/10"));

        cumulative_order_modelList.add(new Cumulative_Order_Model("DCO", "30/190"));

        cumulative_order_modelList.add(new Cumulative_Order_Model("VS", "50/100"));
        cumulative_order_modelList.add(new Cumulative_Order_Model("MT", "80/100"));


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llGridParent.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = 500;
        params.width = cumulative_order_modelList.size() * 200;
        llGridParent.setLayoutParams(params);


        cumulativeInfoAdapter = new OutletDashboardInfoAdapter(getActivity(), cumulative_order_modelList);
        recyclerView.setNumColumns(cumulative_order_modelList.size());

        recyclerView.setAdapter(cumulativeInfoAdapter);


        return view;
    }

//    void init() {
//        TableLayout stk = (TableLayout) view.findViewById(R.id.table_main);
//        TableRow tbrow0 = new TableRow(mContext);
//        TextView tv0 = new TextView(mContext);
//        tv0.setText(" Desc ");
//        tv0.setTextColor(Color.BLACK);
//        tbrow0.addView(tv0);
//        TextView tv1 = new TextView(mContext);
//        tv1.setText(" Existing ");
//        tv1.setTextColor(Color.BLACK);
//        tbrow0.addView(tv1);
//        TextView tv2 = new TextView(mContext);
//        tv2.setText(" New ");
//        tv2.setTextColor(Color.BLACK);
//        tbrow0.addView(tv2);
//        TextView tv3 = new TextView(mContext);
//        tv3.setText(" Total Milk \n Ltrs. ");
//        tv3.setTextColor(Color.BLACK);
//        tbrow0.addView(tv3);
//
//        TextView tv4 = new TextView(mContext);
//        tv4.setText(" New Milk Orders \n Ltrs. ");
//        tv4.setTextColor(Color.BLACK);
//        tbrow0.addView(tv4);
//
//
//        stk.addView(tbrow0);
//        for (int i = 0; i < 5; i++) {
//            TableRow tbrow = new TableRow(mContext);
//            TextView t1v = new TextView(mContext);
//            t1v.setText("" + i);
//            t1v.setTextColor(Color.BLACK);
//            t1v.setGravity(Gravity.CENTER);
//            tbrow.addView(t1v);
//            TextView t2v = new TextView(mContext);
//            t2v.setText("Product " + i);
//            t2v.setTextColor(Color.BLACK);
//            t2v.setGravity(Gravity.CENTER);
//            tbrow.addView(t2v);
//            TextView t3v = new TextView(mContext);
//            t3v.setText("Rs." + i);
//            t3v.setTextColor(Color.BLACK);
//            t3v.setGravity(Gravity.CENTER);
//            tbrow.addView(t3v);
//            TextView t4v = new TextView(mContext);
//            t4v.setText("" + i * 15 / 32 * 10);
//            t4v.setTextColor(Color.BLACK);
//            t4v.setGravity(Gravity.CENTER);
//            tbrow.addView(t4v);
//            stk.addView(tbrow);
//        }
//
//    }

}