package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.MapDirectionActivity;
import com.hap.checkinproc.SFA_Activity.Reports_Distributor_Name;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistributerListAdapter extends RecyclerView.Adapter<DistributerListAdapter.MyViewHolder> {
    JSONArray AryDta;
    private Context context;
    int salRowDetailLayout;
    private double ACBalance = 0.0;
    Shared_Common_Pref shared_common_pref;
    AdapterOnClick mAdapterOnClick;

    public DistributerListAdapter(JSONArray jAryDta, int rowLayout, Context mContext) {
        AryDta = jAryDta;
        context = mContext;
        salRowDetailLayout = rowLayout;
        shared_common_pref = new Shared_Common_Pref(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(salRowDetailLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JSONObject itm = AryDta.getJSONObject(position);
            holder.tvDistName.setText(itm.getString("name"));
            holder.tvDistAdd.setText(itm.getString("Addr1"));

            NumberFormat format1 = NumberFormat.getCurrencyInstance(new Locale("en", "in"));

            holder.rlRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject jParam = new JSONObject();
                        jParam.put("StkERP", AryDta.getJSONObject(position).getString("ERP_Code"));

                        ApiClient.getClient().create(ApiInterface.class)
                                .getDataArrayList("get/custbalance", jParam.toString())
                                .enqueue(new Callback<JsonArray>() {
                                    @Override
                                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                        try {

                                            JsonArray res = response.body();

                                            JsonObject jItem = res.get(0).getAsJsonObject();
                                            double ActBAL = 0;
                                            try {
                                                ActBAL = jItem.get("LC_BAL").getAsDouble();
                                            } catch (Exception e) {
                                                Log.v("newArr:lccatch:", e.getMessage());

                                            }
                                            ACBalance = jItem.get("Balance").getAsDouble();
                                            if (ACBalance <= 0) ACBalance = Math.abs(ACBalance);
                                            else ACBalance = 0 - ACBalance;
                                            if (ActBAL <= 0) ActBAL = Math.abs(ActBAL);
                                            else ActBAL = 0 - ActBAL;


                                            holder.tvbal.setText("" + format1.format(ACBalance));
                                            holder.tvAvailBal.setText("Available Balance:" + format1.format(ActBAL));
                                            holder.tvAmtBlk.setText("Amount Blocked:" + format1.format(jItem.get("Pending").getAsDouble()));
                                            holder.tvBalUpdTime.setText("Last Updated On " + Common_Class.GetDatemonthyearTimeformat());

                                            AryDta.getJSONObject(position).put("bal", format1.format(ACBalance));
                                            AryDta.getJSONObject(position).put("avail", format1.format(ActBAL));
                                            AryDta.getJSONObject(position).put("blk", jItem.get("Pending").getAsDouble());
                                            AryDta.getJSONObject(position).put("balUpdatedTime", Common_Class.GetDatemonthyearTimeformat());


                                            shared_common_pref.save(Constants.Distributor_List, AryDta.toString());
                                        } catch (Exception e) {
                                            Log.v("newArr:catch", e.getMessage());
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<JsonArray> call, Throwable t) {
                                        Log.v("newArr:fail", t.getMessage());

                                    }
                                });

                    } catch (Exception e) {
                    }
                }

            });


            try {
                holder.tvbal.setText("" + (itm.getString("bal")));
                holder.tvAmtBlk.setText("Amount Blocked:" + (itm.getString("blk")));
                holder.tvAvailBal.setText("Available Balance:" + itm.getString("avail"));
                holder.tvBalUpdTime.setText("Last Updated On " + itm.getString("balUpdatedTime"));
            } catch (Exception e) {
                // holder.tvBalUpdTime.setText("Last Updated On " + Common_Class.GetDatemonthyearTimeformat());

            }

            holder.llDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Common_Class common_class = new Common_Class(context);
                        JSONObject itm = AryDta.getJSONObject(position);
                        if (Common_Class.isNullOrEmpty(itm.getString("lat")) || Common_Class.isNullOrEmpty(itm.getString("lon"))) {
                            common_class.showMsg(Reports_Distributor_Name.reports_distributor_name, "No route is found");
                        } else {
                            Intent intent = new Intent(context, MapDirectionActivity.class);
                            intent.putExtra(Constants.DEST_LAT, itm.getString("lat"));
                            intent.putExtra(Constants.DEST_LNG, itm.getString("long"));
                            intent.putExtra(Constants.DEST_NAME, itm.getString("name"));
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {

                    }

                }
            });

            holder.tvDistName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
//                        JSONObject itm = AryDta.getJSONObject(position);
//
//                        Intent intent = new Intent(context, AddNewRetailer.class);
//                        Shared_Common_Pref.Outlet_Info_Flag = "1";
//                        Shared_Common_Pref.Editoutletflag = "1";
//                        Shared_Common_Pref.Outler_AddFlag = "0";
//                        Shared_Common_Pref.FromActivity = "Outlets";
//                    Shared_Common_Pref.OutletCode = String.valueOf(Retailer_Modal_ListFilter.get(position).getId());
//                    intent.putExtra("OutletCode", String.valueOf(Retailer_Modal_ListFilter.get(position).getId()));
//                    intent.putExtra("OutletName", Retailer_Modal_ListFilter.get(position).getName());
//                    intent.putExtra("OutletAddress", Retailer_Modal_ListFilter.get(position).getListedDrAddress1());
//                    intent.putExtra("OutletMobile", Retailer_Modal_ListFilter.get(position).getPrimary_No());
//                    intent.putExtra("OutletRoute", Retailer_Modal_ListFilter.get(position).getTownName());

                        //  context.startActivity(intent);
                    } catch (Exception e) {

                    }

                }
            });

            holder.llRefreshLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Reports_Distributor_Name.reports_distributor_name.updateDistlatLng(AryDta.getJSONObject(position).getInt("id"), position);
                    } catch (Exception e) {
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
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
        return AryDta.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDistName, tvbal, tvAvailBal, tvAmtBlk, tvBalUpdTime, tvDistAdd;
        RelativeLayout rlRefresh;
        LinearLayout llDirection, llParent, llRefreshLoc;
        ProgressBar pb;

        public MyViewHolder(View view) {
            super(view);
            tvDistName = view.findViewById(R.id.tvDistName);
            tvbal = view.findViewById(R.id.tvACBal);
            tvAvailBal = view.findViewById(R.id.tvAvailBal);
            tvAmtBlk = view.findViewById(R.id.tvAmtBlk);
            rlRefresh = view.findViewById(R.id.rlRefBal);
            llDirection = view.findViewById(R.id.llDirection);
            llParent = view.findViewById(R.id.layparent);
            tvBalUpdTime = view.findViewById(R.id.tvBalUpdTime);
            tvDistAdd = view.findViewById(R.id.tvDistAdd);
            llRefreshLoc = view.findViewById(R.id.llRefreshLoc);
            pb = view.findViewById(R.id.progressbar);

        }
    }
}