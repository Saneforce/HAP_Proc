package com.hap.checkinproc.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity.PdfViewerActivity;
import com.hap.checkinproc.Activity_Hap.FlightBookingApproval;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Activity.FlightBooking_Status_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightBooking_Approval_Adapter extends RecyclerView.Adapter<FlightBooking_Approval_Adapter.MyViewHolder> {
    private JSONArray mArr;

    private Context context;
    private String sSF;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvStatus, tvNoOfTraveler, tvbookedBy, tvViewSta,
        txFrmPlc,txToPlc,txTrvDate,txRetTrvDate,txRetFrmPlc,txRetToPlc,txRet;
        Button btnApproval,btnReject,btnCancel,btnSvReject;
        public View vwReject;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvStatus = view.findViewById(R.id.tvFBStatus);
            tvNoOfTraveler = view.findViewById(R.id.tvTravelerCount);
            tvbookedBy = view.findViewById(R.id.tvBookedBy);
            tvViewSta = view.findViewById(R.id.tvViewSta);
            txFrmPlc = view.findViewById(R.id.tvFrom);
            txToPlc = view.findViewById(R.id.tvTo);
            txRetFrmPlc = view.findViewById(R.id.tvRetFrom);
            txRetToPlc = view.findViewById(R.id.tvRetTo);
            txRet = view.findViewById(R.id.tvReturn);
            vwReject = view.findViewById(R.id.vwReject);
            txTrvDate = view.findViewById(R.id.tvTrvDate);
            txRetTrvDate = view.findViewById(R.id.tvRetTrvDate);

            btnApproval= view.findViewById(R.id.Approvetkt);
            btnReject= view.findViewById(R.id.rejecttkt);
            btnSvReject= view.findViewById(R.id.btnSvReject);
            btnCancel= view.findViewById(R.id.canceltkt);
        }
    }


    public FlightBooking_Approval_Adapter(JSONArray arr, Context context, String mSF) {
        this.mArr = arr;
        this.context = context;
        this.sSF=mSF;
    }

    @Override
    public FlightBooking_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.flight_booking_status_listitem, null, false);
        return new FlightBooking_Approval_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FlightBooking_Approval_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject obj = mArr.getJSONObject(position);
            holder.tvDate.setText("" + obj.getString("RequestDate"));
            holder.tvStatus.setText("" + obj.getString("BookingStatus"));
            holder.tvNoOfTraveler.setText("" + obj.getString("Travellers"));
            holder.tvbookedBy.setText("" + obj.getString("RequestedBy"));

            holder.txFrmPlc.setText("" + obj.getString("BookFR_Plc"));
            holder.txToPlc.setText("" + obj.getString("BookTo_Plc"));
            holder.txRet.setText("" + obj.getString("Ret"));

            holder.txRetFrmPlc.setText("" + obj.getString("BookRetFR_Plc"));
            holder.txRetToPlc.setText("" + obj.getString("BookRetTo_Plc"));
            holder.txTrvDate.setText("" + obj.getString("BookDt") + " - "+ obj.getString("BookSes"));
            holder.txRetTrvDate.setText("" + obj.getString("BookRetDt") + " - "+ obj.getString("BookRetSes"));

            holder.btnApproval.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.GONE);

            if (obj.getString("BookingStatus").equalsIgnoreCase("Booked")) {
                holder.tvStatus.setBackgroundResource(R.drawable.button_green);
            } else {
                holder.tvStatus.setBackgroundResource(R.drawable.button_yellows);
            }

            holder.vwReject.setVisibility(View.GONE);
            holder.tvStatus.setPadding(20, 5, 20, 5);

            holder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        holder.vwReject.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                    }
                }
            });
            holder.tvNoOfTraveler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        FlightBookingApproval.activity.showTravelersDialog(mArr.getJSONObject(position).getJSONArray("TrvDetails"));
                    } catch (Exception e) {
                    }
                }
            });
            holder.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogBox.showDialog(context, "HAP Check-In", String.valueOf(Html.fromHtml("Do You Submit Flight Booking Request.")), "Yes", "No", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                            JSONObject jObj=new JSONObject();
                            try {
                                jObj.put("SF",sSF);
                                jObj.put("BookID",obj.getString("BookID"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            apiInterface.JsonSave("cancel/flightbook",jObj.toString()).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    JsonObject Res=response.body();
                                    String Msg= Res.get("Msg").getAsString();
                                    if(!Msg.equalsIgnoreCase("")){
                                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                                .setTitle("HAP Check-In")
                                                .setMessage(Html.fromHtml(Msg))
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                })
                                                .show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });


                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });



                }
            });
            holder.tvViewSta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent stat = new Intent(context, PdfViewerActivity.class);
                        stat.putExtra("PDF_ONE", mArr.getJSONObject(position).getString("Attachment").replaceAll("http:", "https:"));
                        stat.putExtra("PDF_FILE", "Web");
                        context.startActivity(stat);
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return mArr.length();
    }
}