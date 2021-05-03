package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.ProductImageView;
import com.hap.checkinproc.Activity_Hap.TaFuelEdit;
import com.hap.checkinproc.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class FuelListAdapter extends RecyclerView.Adapter<FuelListAdapter.MyViewHolder> {
    Context context;
    JsonArray jsonArray;

    public FuelListAdapter(Context context, JsonArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ta_fuel_allowance, parent, false);
        return new FuelListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        JsonObject jsFuel = (JsonObject) jsonArray.get(position);

        Log.v("FUEL_REPSONE", jsFuel.toString());


        holder.txtTaClaim.setText(jsFuel.get("MOT_Name").getAsString());
        holder.TxtStartedKm.setText(jsFuel.get("Start_Km").getAsString());
        holder.TxtClosingKm.setText(jsFuel.get("End_Km").getAsString());


        if (!jsFuel.get("End_Km").getAsString().equalsIgnoreCase("")) {

            Integer start = Integer.valueOf(jsFuel.get("Start_Km").getAsString());
            Integer end = Integer.valueOf(jsFuel.get("End_Km").getAsString());

            if (end != 0) {
                String total = String.valueOf(end - start);
                holder.TotalTravelledKm.setText(total);
                holder.PersonalKiloMeter.setText(jsFuel.get("Personal_Km").getAsString());

                Integer Total = Integer.valueOf(total);
                Integer Personal = Integer.valueOf(jsFuel.get("Personal_Km").getAsString());
                String TotalPersonal = String.valueOf(Total - Personal);
                holder.PersonalTextKM.setText(TotalPersonal);
                Double FuelaAmt = Double.valueOf(jsFuel.get("FuelAmt").getAsString());
                holder.fuelAmount.setText(" Rs." + new DecimalFormat("##0.00").format(FuelaAmt) + " / KM ");

                holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TaFuelEdit.class);
                        intent.putExtra("SL_NO", jsFuel.get("Sl_No").getAsString());
                        intent.putExtra("MOT", jsFuel.get("MOT").getAsString());
                        intent.putExtra("Start", jsFuel.get("Start_Km").getAsString());
                        intent.putExtra("End", jsFuel.get("End_Km").getAsString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                });


                Double q = Double.valueOf(TotalPersonal);
                Double z = Double.valueOf(jsFuel.get("FuelAmt").getAsString());


                String qz = String.valueOf(q * z);

                holder.TextTotalAmount.setText("Rs. " + qz);
            }
        }


        if (!jsFuel.get("start_Photo").getAsString().equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(jsFuel.get("start_Photo").getAsString())
                    .into(holder.imgStart);

            holder.imgStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductImageView.class);
                    intent.putExtra("ImageUrl", jsFuel.get("start_Photo").getAsString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        if (!jsFuel.get("End_photo").getAsString().equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(jsFuel.get("End_photo").getAsString())
                    .into(holder.imgEnd);

            holder.imgEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductImageView.class);
                    intent.putExtra("ImageUrl", jsFuel.get("End_photo").getAsString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TxtStartedKm, TxtClosingKm, travelTypeMode, TotalTravelledKm, txtTaClaim, PersonalTextKM, PersonalKiloMeter,
                fuelAmount, TextTotalAmount;
        ImageView imgStart, imgEnd, imgEdit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTaClaim = itemView.findViewById(R.id.mode_name);
            TxtStartedKm = itemView.findViewById(R.id.txt_started_km);
            TxtClosingKm = itemView.findViewById(R.id.txt_ended_km);
            PersonalTextKM = itemView.findViewById(R.id.personal_km_text);
            TotalTravelledKm = itemView.findViewById(R.id.total_km);
            PersonalKiloMeter = itemView.findViewById(R.id.pers_kilo_meter);
            imgStart = itemView.findViewById(R.id.startkmimage);
            imgEnd = itemView.findViewById(R.id.endkmimage);
            imgEdit = itemView.findViewById(R.id.img_edit);
            fuelAmount = itemView.findViewById(R.id.fuel_amount);
            TextTotalAmount = itemView.findViewById(R.id.txt_total_amt);

        }
    }
}
