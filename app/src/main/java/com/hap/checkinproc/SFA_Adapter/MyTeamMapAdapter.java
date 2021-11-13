package com.hap.checkinproc.SFA_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.MapDirectionActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyTeamMapAdapter extends RecyclerView.Adapter<MyTeamMapAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray array;
    Context context;
    String laty, lngy;
    JSONObject json;

    public MyTeamMapAdapter(Activity context, JSONArray array, String laty, String lngy) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.array = array;
        this.laty = laty;
        this.lngy = lngy;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.team_loc_detail_layout, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            json = array.getJSONObject(position);

            holder.tvSfName.setText(json.getString("Sf_Name"));
            holder.tvDesig.setText(json.getString("Designation_Name"));
            holder.tvMobile.setText(json.getString("SF_Mobile"));

        } catch (Exception e) {


            e.printStackTrace();
        }


        holder.llDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String url = getDirectionsUrl(array.getJSONObject(position).getString("Lat") + "," + array.getJSONObject(position).getString("Lon"));
                    Intent intent = new Intent(context, MapDirectionActivity.class);
                    intent.putExtra(Constants.MAP_ROUTE, url);
                    intent.putExtra(Constants.DEST_LAT, array.getJSONObject(position).getString("Lat"));
                    intent.putExtra(Constants.DEST_LNG, array.getJSONObject(position).getString("Lon"));
                    intent.putExtra(Constants.DEST_NAME, array.getJSONObject(position).getString("HQ_Name"));
                    context.startActivity(intent);


                } catch (Exception e) {

                }

            }
        });


    }

    private String getDirectionsUrl(String dest) {
        // Origin of route
        String str_origin = "origin=" + Shared_Common_Pref.Outletlat + "," + Shared_Common_Pref.Outletlong;
        // Destination of route
        String str_dest = "destination=" + dest;
        // Key
        String key = "key=" + context.getString(R.string.map_api_key);
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        return url;
    }
    //draw route


    @Override
    public int getItemCount() {
        return array.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvSfName, tvDesig, tvMobile;
        LinearLayout llDir, llMobile;


        public ViewHolder(View itemView) {
            super(itemView);

            tvSfName = (TextView) itemView.findViewById(R.id.tvSfName);
            tvDesig = (TextView) itemView.findViewById(R.id.tvDesig);
            tvMobile = (TextView) itemView.findViewById(R.id.txMobile);
            llMobile = (LinearLayout) itemView.findViewById(R.id.btnCallMob);
            llDir = (LinearLayout) itemView.findViewById(R.id.llDirection);
        }
    }


}
