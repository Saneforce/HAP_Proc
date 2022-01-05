package com.hap.checkinproc.Status_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity.PdfViewerActivity;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Activity.FlightBooking_Status_Activity;

import org.json.JSONArray;
import org.json.JSONObject;

public class FlightBooking_Status_Adapter extends RecyclerView.Adapter<FlightBooking_Status_Adapter.MyViewHolder> {
    private JSONArray mArr;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvStatus, tvNoOfTraveler, tvbookedBy, tvViewSta;


        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvStatus = view.findViewById(R.id.tvFBStatus);
            tvNoOfTraveler = view.findViewById(R.id.tvTravelerCount);
            tvbookedBy = view.findViewById(R.id.tvBookedBy);
            tvViewSta = view.findViewById(R.id.tvViewSta);

        }
    }


    public FlightBooking_Status_Adapter(JSONArray arr, Context context) {
        this.mArr = arr;
        this.context = context;

    }

    @Override
    public FlightBooking_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.flight_booking_status_listitem, null, false);
        return new FlightBooking_Status_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FlightBooking_Status_Adapter.MyViewHolder holder, int position) {
        try {
            JSONObject obj = mArr.getJSONObject(position);
            holder.tvDate.setText("" + obj.getString("RequestDate"));
            holder.tvStatus.setText("" + obj.getString("BookingStatus"));
            holder.tvNoOfTraveler.setText("" + obj.getString("Travellers"));
            holder.tvbookedBy.setText("" + obj.getString("RequestedBy"));

            if (obj.getString("BookingStatus").equalsIgnoreCase("Booked")) {
                holder.tvStatus.setBackgroundResource(R.drawable.button_green);
            } else {
                holder.tvStatus.setBackgroundResource(R.drawable.button_yellows);
            }

            holder.tvStatus.setPadding(20, 5, 20, 5);

            holder.tvNoOfTraveler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        FlightBooking_Status_Activity.activity.showTravelersDialog(mArr.getJSONObject(position).getJSONArray("TrvDetails"));
                    } catch (Exception e) {
                    }
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