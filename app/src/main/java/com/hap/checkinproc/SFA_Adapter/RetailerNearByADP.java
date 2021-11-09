package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RetailerNearByADP extends RecyclerView.Adapter<RetailerNearByADP.MyViewHolder> {
    JsonArray jLists;
    int RowLayout;
    Context context;
    JSONObject PreSales;
    AdapterOnClick mAdapterOnClick;

    public RetailerNearByADP(JsonArray jList, int rowLayout, Context mcontext, AdapterOnClick adapterOnClick) {
        jLists = jList;
        RowLayout = rowLayout;
        context = mcontext;
        mAdapterOnClick = adapterOnClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(RowLayout, parent, false);
        return new RetailerNearByADP.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JsonObject jItem = jLists.get(position).getAsJsonObject();
            String OutletId = jItem.get("Code").getAsString();
            holder.txRetailName.setText(jItem.get("Name").getAsString().toUpperCase());
            holder.txRetailCode.setText(OutletId);
            holder.txOwnerNm.setText(jItem.get("Owner_Name").getAsString().toUpperCase());
            holder.txMobile.setText(jItem.get("Mobile").getAsString().toUpperCase());
            holder.txAdd.setText(jItem.get("Add1").getAsString().toUpperCase());
            holder.txDistName.setText(jItem.get("Distributor").getAsString() + " - " + jItem.get("DistCode").getAsString());
            holder.txChannel.setText(jItem.get("Channel").getAsString());
            holder.txDistance.setText(jItem.get("Distance").getAsString());
            String InvFlag = jItem.get("InvFlag").getAsString();
            holder.txRetNo.setText(String.valueOf(position + 1));
            holder.icMob.setVisibility(View.VISIBLE);
            if (jItem.get("Mobile").getAsString().equalsIgnoreCase("")) {
                holder.icMob.setVisibility(View.GONE);
            }
            if (InvFlag.equalsIgnoreCase("0")) {
                holder.parent_layout.setBackgroundResource(R.color.white);
            } else if (InvFlag.equalsIgnoreCase("1")) {
                holder.parent_layout.setBackgroundResource(R.color.invoiceordercolor);
            } else {
                holder.parent_layout.setBackgroundResource(R.color.greeninvoicecolor);
            }
            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = holder.getAdapterPosition();
                    mAdapterOnClick.onIntentClick(jLists.get(posi).getAsJsonObject(), posi);
                    mAdapterOnClick.onIntentClick(posi);
                }
            });

            holder.txTodayTotQty.setText("0");
            holder.txTodayTotVal.setText("₹0.00");
            holder.txPreTotQty.setText("0");
            holder.txPreTotVal.setText("₹0.00");
            //PreSales
            Boolean DtaBnd = false;
            String jData = jItem.get("todaydata").getAsJsonArray().toString();
            JSONArray BindArry = new JSONArray(jData);
            holder.lstTdyView.setAdapter(new CatewiseSalesaAdapter(BindArry, R.layout.categorywise_sales_adp, context));
            DtaBnd = true;

            int iQty = 0;
            double iVal = 0.0;
            for (int il = 0; il < BindArry.length(); il++) {
                JSONObject itm = BindArry.getJSONObject(il);
                if (itm.length() > 0) {
                    iQty += itm.getInt("Qty");
                    iVal += itm.getDouble("Val");
                }
            }
            holder.txTodayTotQty.setText(String.valueOf(iQty));
            holder.txTodayTotVal.setText("₹" + new DecimalFormat("##0.00").format(iVal));


            getMnthlyDta(holder, jItem, holder.tvFirstMonth.getText().toString());
            holder.tvFirstMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvFirstMonth.setTypeface(null, Typeface.BOLD);
                    holder.tvFirstMonth.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

                    holder.tvSecondMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvSecondMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    holder.tvThirdMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvThirdMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    getMnthlyDta(holder, jItem, holder.tvFirstMonth.getText().toString());
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

                    getMnthlyDta(holder, jItem, holder.tvSecondMnth.getText().toString());
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

                    getMnthlyDta(holder, jItem, holder.tvThirdMnth.getText().toString());
                }
            });


        } catch (Exception e) {
            Log.v("RouteAdapter: ", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return jLists.size();
    }

    public void getMnthlyDta(RetailerNearByADP.MyViewHolder holder, JsonObject jItem, String Mnth) {

        JSONArray PrevSales = null;
        try {
            PrevSales = new JSONArray(jItem.get("monthdata").getAsJsonArray().toString());
            boolean DtaBnd = false;
            JSONArray BindArry = new JSONArray();
            for (int i = 0; i < PrevSales.length(); i++) {
                JSONObject item = PrevSales.getJSONObject(i);
                String str = item.getString("Mnth").substring(0, 3);
                if (Mnth.equals(str)) {
                    BindArry.put(item);
                }

            }
            holder.lstPreView.setAdapter(new CatewiseSalesaAdapter(BindArry, R.layout.categorywise_sales_mnth_adp, context));

            sumOfTotal(BindArry, holder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sumOfTotal(JSONArray AryDta, RetailerNearByADP.MyViewHolder holder) {
        try {
            int iQty = 0;
            double iVal = 0.0;
            for (int il = 0; il < AryDta.length(); il++) {
                JSONObject itm = AryDta.getJSONObject(il);
                iQty += itm.getInt("Qty");
                iVal += itm.getDouble("Val");
            }
            holder.txPreTotQty.setText(String.valueOf(iQty));
            holder.txPreTotVal.setText("₹" + new DecimalFormat("##0.00").format(iVal));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txRetailName, txRetailCode, txAdd, txOwnerNm, txMobile, txDistName, txChannel, txDistance, txTdyDt, txTodayTotQty, txTodayTotVal, txPreTotQty, txPreTotVal,
                tvFirstMonth, tvSecondMnth, tvThirdMnth, txRetNo;
        LinearLayout parent_layout;
        RecyclerView lstTdyView, lstPreView;
        ImageView icMob;
        ImageView ivEdit;

        public MyViewHolder(View view) {
            super(view);
            try {
                parent_layout = view.findViewById(R.id.parent_layout);

                txRetailName = view.findViewById(R.id.retailername);
                txRetailCode = view.findViewById(R.id.retailorCode);
                txRetNo = view.findViewById(R.id.txRetNo);
                txAdd = view.findViewById(R.id.txAdd);
                txOwnerNm = view.findViewById(R.id.txOwnerNm);
                txMobile = view.findViewById(R.id.txMobile);
                icMob = view.findViewById(R.id.icMob);
                txDistName = view.findViewById(R.id.txDistName);
                txChannel = view.findViewById(R.id.txChannel);
                txDistance = view.findViewById(R.id.txDistance);
                tvFirstMonth = view.findViewById(R.id.tvLMFirst);
                tvSecondMnth = view.findViewById(R.id.tvLMSecond);
                tvThirdMnth = view.findViewById(R.id.tvLMThree);
                txTdyDt = view.findViewById(R.id.tvDate);

                txTodayTotQty = view.findViewById(R.id.tvTodayTotQty);
                txTodayTotVal = view.findViewById(R.id.tvTodayTotVal);
                txPreTotQty = view.findViewById(R.id.tvPreTotQty);
                txPreTotVal = view.findViewById(R.id.tvPreTotVal);

                lstTdyView = view.findViewById(R.id.rvTodayData);
                lstPreView = view.findViewById(R.id.rvPreMnData);

                lstTdyView.setLayoutManager(new LinearLayoutManager(context));
                lstPreView.setLayoutManager(new LinearLayoutManager(context));
                ivEdit = view.findViewById(R.id.btnEditRet);


                ivEdit.setVisibility(View.GONE);
                //txRetNo.setVisibility(View.GONE);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd");
                String plantime = dpln.format(c.getTime());
                txTdyDt.setText("" + plantime);
            } catch (Exception e) {
                Log.e("RouteAdapter:holder ", e.getMessage());
            }
        }
    }
}
