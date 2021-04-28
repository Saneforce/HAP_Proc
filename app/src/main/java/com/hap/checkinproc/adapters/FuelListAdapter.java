package com.hap.checkinproc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

        holder.txtTaClaim.setText(jsFuel.get("MOT_Name").getAsString());
        holder.TxtStartedKm.setText(jsFuel.get("Start_Km").getAsString());
        holder.TxtClosingKm.setText(jsFuel.get("End_Km").getAsString());


        if (!jsFuel.get("End_Km").getAsString().equalsIgnoreCase("")) {

            Integer start = Integer.valueOf(jsFuel.get("Start_Km").getAsString());
            Integer end = Integer.valueOf(jsFuel.get("End_Km").getAsString());
            String total = String.valueOf(end - start);
            holder.TotalTravelledKm.setText(total);
            holder.PersonalKiloMeter.setText(jsFuel.get("Personal_Km").getAsString());

            Integer Total = Integer.valueOf(total);
            Integer Personal = Integer.valueOf(jsFuel.get("Personal_Km").getAsString());
            String TotalPersonal = String.valueOf(Total - Personal);
            holder.PersonalTextKM.setText(TotalPersonal);
            Double FuelaAmt = Double.valueOf(jsFuel.get("FuelAmt").getAsString());
            holder.fuelAmount.setText(" Rs." + new DecimalFormat("##0.00").format(FuelaAmt) + " / KM ");


            Double q = Double.valueOf(TotalPersonal);
            Double z = Double.valueOf(jsFuel.get("FuelAmt").getAsString());


            String qz = String.valueOf(q * z);

            holder.TextTotalAmount.setText("Rs. " + qz);
        }


        Picasso.with(context)
                .load(jsFuel.get("start_Photo").getAsString())
                .into(holder.imgStart);

        Picasso.with(context)
                .load(jsFuel.get("End_photo").getAsString())
                .into(holder.imgEnd);


    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TxtStartedKm, TxtClosingKm, travelTypeMode, TotalTravelledKm, txtTaClaim, PersonalTextKM, PersonalKiloMeter,
                fuelAmount, TextTotalAmount;
        ImageView imgStart, imgEnd;


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
            fuelAmount = itemView.findViewById(R.id.fuel_amount);
            TextTotalAmount = itemView.findViewById(R.id.txt_total_amt);

        }
    }
}
