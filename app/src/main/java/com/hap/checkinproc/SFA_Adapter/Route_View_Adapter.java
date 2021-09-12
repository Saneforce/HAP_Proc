package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.TabAdapter;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Route_View_Adapter extends RecyclerView.Adapter<Route_View_Adapter.MyViewHolder> {

    private List<Retailer_Modal_List> Retailer_Modal_Listitem;
    private List<Retailer_Modal_List> tdList = new ArrayList<>();
    private List<Retailer_Modal_List> preList = new ArrayList<>();

    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;
    private TabAdapter adapter;

    List<Retailer_Modal_List> common_models = new ArrayList<>();
    List<Retailer_Modal_List> Retailor_model_list_today = new ArrayList<>();

    private Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    Gson gson = new Gson();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, tvPreOtherVal, tvTdOtherVal, tvPreMilkVal, tvTdMilkVal, tvTdTotVal, tvPreTotVal, tvPreCurdVal, tvTdCurdVal,
                textviewdate, status, invoice, values, invoicedate, tvRetailorCode, tvFirstMonth, tvSecondMnth, tvThirdMnth;
        LinearLayout parent_layout, llTdParent, llPreParent, llDataParent;
        private com.ogaclejapan.smarttablayout.SmartTabLayout tabLayout;
        private ViewPager viewPager;

        LinearLayout cdParent;


        RecyclerView listView;


        public MyViewHolder(View view) {
            super(view);

            try {

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
                textviewdate = view.findViewById(R.id.tvDate);


                tvTdMilkVal = view.findViewById(R.id.tvTodayMilkVal);
                tvTdCurdVal = view.findViewById(R.id.tvTodayCurdVal);
                tvTdOtherVal = view.findViewById(R.id.tvTodayOtherVal);
                tvTdTotVal = view.findViewById(R.id.tvTodayTotVal);

                tvPreMilkVal = view.findViewById(R.id.tvPreMilkVal);
                tvPreCurdVal = view.findViewById(R.id.tvPreCurdVal);
                tvPreOtherVal = view.findViewById(R.id.tvPreOthersVal);
                tvPreTotVal = view.findViewById(R.id.tvPreTotVal);


                llTdParent = view.findViewById(R.id.llTdDataParent);
                llPreParent = view.findViewById(R.id.llPreDataParent);
                llDataParent = view.findViewById(R.id.llDataParent);
                cdParent = view.findViewById(R.id.cdParent);


                Calendar c = Calendar.getInstance();
                SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd");
                String plantime = dpln.format(c.getTime());
                textviewdate.setText("" + plantime);


             //   common_models.clear();


//                for (int rt = 0; rt < Retailer_Modal_Listitem.size(); rt++) {
//
//                    if (!shared_common_pref.getvalue(Constants.RetailorTodayData).equals("")) {
//                        if (tdList != null && tdList.size() > 0) {
//                            boolean isHaveToday = false;
//
//                            for (int i = 0; i < tdList.size(); i++) {
//
//
//                                if (tdList.get(i).getCust_Code().equals(Retailer_Modal_Listitem.get(rt).getId())) {
//                                    isHaveToday = true;
//
//
//                                }
//                            }
//
//                            if (!isHaveToday) {
//
//                            }
//
//
//                        }
//
//                    }
//
//                }


//                RetailorAdapter adapter = new RetailorAdapter(Retailer_Modal_Listitem, context);
//
//                listView.setAdapter(adapter);
//

            } catch (Exception e) {
                Log.e("RouteAdapter:holder ", e.getMessage());
            }
        }


    }


    public Route_View_Adapter(List<Retailer_Modal_List> Retailer_Modal_Listitem, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        try {
            this.Retailer_Modal_Listitem = Retailer_Modal_Listitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.mAdapterOnClick = mAdapterOnClick;
            shared_common_pref = new Shared_Common_Pref(context);

            String todayorderliost = shared_common_pref.getvalue(Constants.RetailorTodayData);

            String strPreList = shared_common_pref.getvalue(Constants.RetailorPreviousData);

            if (!todayorderliost.equals("")) {
                Type userTypeReport = new TypeToken<ArrayList<Retailer_Modal_List>>() {
                }.getType();
                tdList = gson.fromJson(todayorderliost, userTypeReport);
            }

            if (!strPreList.equals("")) {
                Type userTypeReport = new TypeToken<ArrayList<Retailer_Modal_List>>() {
                }.getType();
                preList = gson.fromJson(strPreList, userTypeReport);
            }
        } catch (Exception e) {
            Log.e("RouteAdapter:constr ", e.getMessage());
        }

    }

    @Override
    public Route_View_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Route_View_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Route_View_Adapter.MyViewHolder holder, int position) {
        try {
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


            boolean isHaveToday = false, isHavePre = false;

            if (!shared_common_pref.getvalue(Constants.RetailorTodayData).equals("")) {
                if (tdList != null && tdList.size() > 0) {

                    for (int i = 0; i < tdList.size(); i++) {
                        if (tdList.get(i).getCust_Code().equals(Retailer_Modal_List.getId())) {
                            isHaveToday = true;

                            holder.tvTdMilkVal.setText("" + tdList.get(i).getMilk() + "|₹" + tdList.get(i).getMilkVal());
                            holder.tvTdCurdVal.setText("" + tdList.get(i).getCurd() + "|₹" + tdList.get(i).getCurdVal());
                            holder.tvTdOtherVal.setText("" + tdList.get(i).getOthers() + "|₹" + tdList.get(i).getOthersVal());

                            holder.tvTdTotVal.setText("" + (tdList.get(i).getMilk() + tdList.get(i).getCurd() + tdList.get(i).getOthers()) + "|₹" + (
                                    tdList.get(i).getMilkVal() + tdList.get(i).getCurdVal() + tdList.get(i).getOthersVal()));
                        }
                    }

                    if (!isHaveToday) {
                        holder.tvTdMilkVal.setText("0|₹0");
                        holder.tvTdCurdVal.setText("0|₹0");
                        holder.tvTdOtherVal.setText("0|₹0");

                        holder.tvTdTotVal.setText("0|₹0");
                    } else {


                    }


                }

            }


            if (!shared_common_pref.getvalue(Constants.RetailorPreviousData).equals("") && holder.tvFirstMonth.getText().toString().equals("")) {
                if (preList != null && preList.size() > 0) {
                    for (int i = 0; i < preList.size(); i++) {
                        if (preList.get(i).getCust_Code().equals(Retailer_Modal_List.getId())) {
                            isHavePre = true;

                            Log.v("PreList: ", Retailer_Modal_List.getName());
                            String str = preList.get(i).getMnth();

                            if (holder.tvFirstMonth.getText().toString().equals("")) {
                                holder.tvPreMilkVal.setText("" + preList.get(i).getMilk() + "|₹" + preList.get(i).getMilkVal());
                                holder.tvPreCurdVal.setText("" + preList.get(i).getCurd() + "|₹" + preList.get(i).getCurdVal());
                                holder.tvPreOtherVal.setText("" + preList.get(i).getOthers() + "|₹" + preList.get(i).getOthersVal());

                                holder.tvPreTotVal.setText("" + (preList.get(i).getMilk() + preList.get(i).getCurd() +
                                        preList.get(i).getOthers()) + "|₹" + (
                                        preList.get(i).getMilkVal() + preList.get(i).getCurdVal() + preList.get(i).getOthersVal()));

                                holder.tvFirstMonth.setText("" + str.substring(0, 3));


                            } else if (holder.tvSecondMnth.getText().toString().equals("")) {
                                holder.tvSecondMnth.setText("" + str.substring(0, 3));


                            } else {
                                holder.tvThirdMnth.setText("" + str.substring(0, 3));
                            }


                        }
                    }

                    if (!isHavePre) {
                        holder.cdParent.setBackgroundColor(Color.WHITE);
                        holder.textviewname.setTextColor(context.getResources().getColor(R.color.black));
                        holder.tvFirstMonth.setText("");

                        holder.tvSecondMnth.setText("");
                        holder.tvThirdMnth.setText("");

                        holder.tvPreMilkVal.setText("0|₹0");
                        holder.tvPreCurdVal.setText("0|₹0");
                        holder.tvPreOtherVal.setText("0|₹0");

                        holder.tvPreTotVal.setText("0|₹0");
                    } else {
                        holder.cdParent.setBackgroundColor(context.getResources().getColor(R.color.greentrans));
                        holder.textviewname.setTextColor(context.getResources().getColor(R.color.greentext));

                    }

                }

            }


            holder.tvFirstMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvFirstMonth.setTypeface(null, Typeface.BOLD);
                    holder.tvFirstMonth.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

                    holder.tvSecondMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvSecondMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    holder.tvThirdMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvThirdMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    boolean isHavePre = false;

                    if (!shared_common_pref.getvalue(Constants.RetailorPreviousData).equals("")) {
                        if (preList != null && preList.size() > 0) {
                            for (int i = 0; i < preList.size(); i++) {
                                if (preList.get(i).getCust_Code().equals(Retailer_Modal_List.getId())) {
                                    isHavePre = true;

                                    String str = preList.get(i).getMnth().substring(0, 3);

                                    Log.v("PreList: ", Retailer_Modal_List.getName() + " mnth: " + str);


                                    if (holder.tvFirstMonth.getText().toString().equals(str)) {
                                        holder.tvPreMilkVal.setText("" + preList.get(i).getMilk() + "|₹" + preList.get(i).getMilkVal());
                                        holder.tvPreCurdVal.setText("" + preList.get(i).getCurd() + "|₹" + preList.get(i).getCurdVal());
                                        holder.tvPreOtherVal.setText("" + preList.get(i).getOthers() + "|₹" + preList.get(i).getOthersVal());

                                        holder.tvPreTotVal.setText("" + (preList.get(i).getMilk() + preList.get(i).getCurd() +
                                                preList.get(i).getOthers()) + "|₹" + (
                                                preList.get(i).getMilkVal() + preList.get(i).getCurdVal() + preList.get(i).getOthersVal()));


                                    }

                                }
                            }

                            if (!isHavePre) {
                                holder.tvFirstMonth.setText("");

                                holder.tvSecondMnth.setText("");
                                holder.tvThirdMnth.setText("");

                                holder.tvPreMilkVal.setText("0|₹0");
                                holder.tvPreCurdVal.setText("0|₹0");
                                holder.tvPreOtherVal.setText("0|₹0");

                                holder.tvPreTotVal.setText("0|₹0");
                            }

                        }

                    }

                    notifyDataSetChanged();


                }
            });
            holder.tvSecondMnth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvFirstMonth.setTypeface(null, Typeface.NORMAL);
                    holder.tvFirstMonth.setTextColor(context.getResources().getColor(R.color.black_80));

                    holder.tvSecondMnth.setTypeface(null, Typeface.BOLD);
                    holder.tvSecondMnth.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

                    holder.tvThirdMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvThirdMnth.setTextColor(context.getResources().getColor(R.color.black_80));


                    boolean isHavePre = false;

                    if (!shared_common_pref.getvalue(Constants.RetailorPreviousData).equals("")) {
                        if (preList != null && preList.size() > 0) {
                            for (int i = 0; i < preList.size(); i++) {
                                if (preList.get(i).getCust_Code().equals(Retailer_Modal_List.getId())) {
                                    isHavePre = true;

                                    String str = preList.get(i).getMnth().substring(0, 3);
                                    Log.v("PreList: ", Retailer_Modal_List.getName() + " mnth: " + str);


                                    if (holder.tvSecondMnth.getText().toString().equals(str)) {
                                        holder.tvPreMilkVal.setText("" + preList.get(i).getMilk() + "|₹" + preList.get(i).getMilkVal());
                                        holder.tvPreCurdVal.setText("" + preList.get(i).getCurd() + "|₹" + preList.get(i).getCurdVal());
                                        holder.tvPreOtherVal.setText("" + preList.get(i).getOthers() + "|₹" + preList.get(i).getOthersVal());

                                        holder.tvPreTotVal.setText("" + (preList.get(i).getMilk() + preList.get(i).getCurd() +
                                                preList.get(i).getOthers()) + "|₹" + (
                                                preList.get(i).getMilkVal() + preList.get(i).getCurdVal() + preList.get(i).getOthersVal()));


                                    }

                                }
                            }

                            if (!isHavePre) {
                                holder.tvFirstMonth.setText("");

                                holder.tvSecondMnth.setText("");
                                holder.tvThirdMnth.setText("");

                                holder.tvPreMilkVal.setText("0|₹0");
                                holder.tvPreCurdVal.setText("0|₹0");
                                holder.tvPreOtherVal.setText("0|₹0");

                                holder.tvPreTotVal.setText("0|₹0");
                            }

                        }

                    }

                    notifyDataSetChanged();


                }
            });
            holder.tvThirdMnth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvFirstMonth.setTypeface(null, Typeface.NORMAL);
                    holder.tvFirstMonth.setTextColor(context.getResources().getColor(R.color.black_80));
                    holder.tvSecondMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvSecondMnth.setTextColor(context.getResources().getColor(R.color.black_80));
                    holder.tvThirdMnth.setTypeface(null, Typeface.BOLD);
                    holder.tvThirdMnth.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    boolean isHavePre = false;

                    if (!shared_common_pref.getvalue(Constants.RetailorPreviousData).equals("")) {
                        if (preList != null && preList.size() > 0) {
                            for (int i = 0; i < preList.size(); i++) {
                                if (preList.get(i).getCust_Code().equals(Retailer_Modal_List.getId())) {
                                    isHavePre = true;

                                    String str = preList.get(i).getMnth().substring(0, 3);
                                    Log.v("PreList: ", Retailer_Modal_List.getName() + " mnth: " + str);


                                    if (holder.tvThirdMnth.getText().toString().equals(str)) {
                                        holder.tvPreMilkVal.setText("" + preList.get(i).getMilk() + "|₹" + preList.get(i).getMilkVal());
                                        holder.tvPreCurdVal.setText("" + preList.get(i).getCurd() + "|₹" + preList.get(i).getCurdVal());
                                        holder.tvPreOtherVal.setText("" + preList.get(i).getOthers() + "|₹" + preList.get(i).getOthersVal());

                                        holder.tvPreTotVal.setText("" + (preList.get(i).getMilk() + preList.get(i).getCurd() +
                                                preList.get(i).getOthers()) + "|₹" + (
                                                preList.get(i).getMilkVal() + preList.get(i).getCurdVal() + preList.get(i).getOthersVal()));


                                    }

                                }
                            }

                            if (!isHavePre) {
                                holder.tvFirstMonth.setText("");

                                holder.tvSecondMnth.setText("");
                                holder.tvThirdMnth.setText("");

                                holder.tvPreMilkVal.setText("0|₹0");
                                holder.tvPreCurdVal.setText("0|₹0");
                                holder.tvPreOtherVal.setText("0|₹0");

                                holder.tvPreTotVal.setText("0|₹0");
                            }

                        }

                    }

                    notifyDataSetChanged();


                }
            });


        } catch (Exception e) {
            Log.v("RouteAdapter: ", e.getMessage());
        }
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
    public int getItemCount() {
        if (Retailer_Modal_Listitem != null)
            return Retailer_Modal_Listitem.size();
        else
            return 0;
    }


}