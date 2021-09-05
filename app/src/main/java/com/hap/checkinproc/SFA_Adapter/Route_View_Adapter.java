package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.TabAdapter;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class Route_View_Adapter extends RecyclerView.Adapter<Route_View_Adapter.MyViewHolder> {

    private List<Retailer_Modal_List> Retailer_Modal_Listitem;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;
    private TabAdapter adapter;

    List<Common_Model> common_models = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, status, invoice, values, invoicedate, tvRetailorCode, tvFirstMonth, tvSecondMnth, tvThirdMnth;
        LinearLayout parent_layout;
        private com.ogaclejapan.smarttablayout.SmartTabLayout tabLayout;
        private ViewPager viewPager;


        RecyclerView listView;


        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.retailername);
            parent_layout = view.findViewById(R.id.parent_layout);
            status = view.findViewById(R.id.status);
            invoice = view.findViewById(R.id.invoice);
            values = view.findViewById(R.id.values);
            invoicedate = view.findViewById(R.id.invoicedate);
            tvRetailorCode = view.findViewById(R.id.retailorCode);
            viewPager = view.findViewById(R.id.viewpager);
            viewPager.setOffscreenPageLimit(3);
            tabLayout = view.findViewById(R.id.tabs);
            listView = view.findViewById(R.id.lvLastInvoice);
            tvFirstMonth = view.findViewById(R.id.tvLMFirst);
            tvSecondMnth = view.findViewById(R.id.tvLMSecond);
            tvThirdMnth = view.findViewById(R.id.tvLMThree);

            //  setupViewPager();
//             tabLayout.setupWithViewPager(viewPager);

//            setupViewPager(viewPager, tabLayout);
//
//            tabLayout.setViewPager(viewPager);

            common_models.clear();

            common_models.add(new Common_Model("Milk", "0"));


            RetailorAdapter adapter = new RetailorAdapter(common_models, context);

            listView.setAdapter(adapter);


            tvFirstMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvFirstMonth.setTypeface(null, Typeface.BOLD);

                    tvSecondMnth.setTypeface(null, Typeface.NORMAL);
                    tvThirdMnth.setTypeface(null, Typeface.NORMAL);

                }
            });
            tvSecondMnth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvFirstMonth.setTypeface(null, Typeface.NORMAL);
                    tvSecondMnth.setTypeface(null, Typeface.BOLD);
                    tvThirdMnth.setTypeface(null, Typeface.NORMAL);

                }
            });
            tvThirdMnth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvFirstMonth.setTypeface(null, Typeface.NORMAL);
                    tvSecondMnth.setTypeface(null, Typeface.NORMAL);
                    tvThirdMnth.setTypeface(null, Typeface.BOLD);

                }
            });


        }


    }

    private void setupViewPager(ViewPager viewPager, SmartTabLayout smartTabLayout) {

//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                Dashboard_Route.dashboard_route.getSupportFragmentManager(), FragmentPagerItems.with(context)
//                .add("Mar", RetailorFirstMnthFrag.class)
//               // .add(R.string.titleB, PageFragment.class)
//                .create());
//
//
//        viewPager.setAdapter(adapter);
//
//        smartTabLayout.setViewPager(viewPager);

//
//        RetailorAdapter adapter = new RetailorAdapter(Dashboard_Route.dashboard_route.getSupportFragmentManager());
//
//
//        adapter.addFragment(new RetailorFirstMnthFrag(context, "Mar"), "Mar");
//        adapter.addFragment(new RetailorFirstMnthFrag(context, "Feb"), "Feb");
//        adapter.addFragment(new RetailorFirstMnthFrag(context, "Jan"), "Jan");
//
//
//        viewPager.setAdapter(adapter);
//

//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        if (isGraphMode) {
//            adapter.resetFragment();
//            adapter.addFragment(new CombinedChartFragment(this, "Table4"), "T4");
//
//            findViewById(R.id.llPagerDots).setVisibility(View.GONE);
//
//        } else {
//            adapter.resetFragment();
//            adapter.addFragment(new DashboardTableDataFrag(this, "Table1"), "T1");
//            adapter.addFragment(new DashboardBarDataFrag("Table2"), "T2");
//            adapter.addFragment(new DashboardBarDataFrag("Table3"), "T3");
//            adapter.addFragment(new DashboardBarDataFrag("Table3"), "T3");
//
//            findViewById(R.id.llPagerDots).setVisibility(View.VISIBLE);
//            addBottomDots(0);
//
//        }
//        viewPager.setAdapter(adapter);
//        adapter.notifyDataSetChanged();


    }


    public Route_View_Adapter(List<Retailer_Modal_List> Retailer_Modal_Listitem, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Retailer_Modal_Listitem = Retailer_Modal_Listitem;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;


    }

    @Override
    public Route_View_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Route_View_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Route_View_Adapter.MyViewHolder holder, int position) {
        Retailer_Modal_List Retailer_Modal_List = Retailer_Modal_Listitem.get(position);
        holder.textviewname.setText("" + Retailer_Modal_List.getName().toUpperCase());
        holder.tvRetailorCode.setText("" + Retailer_Modal_List.getERP_Code());
        if (Retailer_Modal_List.getStatusname() != null) {
            holder.status.setText("Status :" + "\t\t" + Retailer_Modal_List.getStatusname().toUpperCase());
        } else {
            holder.status.setText("Status :" + "\t\t" + "");
        }

        holder.invoice.setText("Last inv value :" + "\t\t" + Retailer_Modal_List.getInvoiceValues());
        holder.values.setText("Value :" + "\t\t" + Retailer_Modal_List.getValuesinv());
        holder.invoicedate.setText("Last inv date :" + "\t\t" + Retailer_Modal_List.getInvoiceDate());
        Log.e("INVOICE_FLAG_Adapter", Retailer_Modal_List.getInvoice_Flag());
        if (Retailer_Modal_List.getInvoice_Flag().equals("0")) {
            holder.parent_layout.setBackgroundResource(R.color.white);
        } else if (Retailer_Modal_List.getInvoice_Flag().equals("1")) {
            holder.parent_layout.setBackgroundResource(R.color.invoiceordercolor);
        } else {
            holder.parent_layout.setBackgroundResource(R.color.greeninvoicecolor);
        }
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (Retailer_Modal_Listitem != null)
            return Retailer_Modal_Listitem.size();
        else
            return 0;
    }
}