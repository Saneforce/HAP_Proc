package com.hap.checkinproc.SFA_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.MapDirectionActivity;
import com.hap.checkinproc.SFA_Activity.MyTeamActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyTeamMapAdapter extends RecyclerView.Adapter<MyTeamMapAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray array;
    Context context;
    String laty, lngy;
    JSONObject json;
    public static String TAG = "MyTeamMapAdapter";

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
            holder.tvSfName.setText(MyTeamActivity.myTeamActivity.mType.equalsIgnoreCase("ALL") ?
                    json.getString("Sf_Name") + "(" + json.getString("shortname") + ")" : json.getString("Sf_Name"));
            holder.tvDesig.setText(json.getString("Designation_Name"));
            holder.tvMobile.setText(json.getString("SF_Mobile"));

        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.llDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    json = array.getJSONObject(position);
                    String url = MyTeamActivity.myTeamActivity.common_class.getDirectionsUrl(json.getString("Lat") + "," + json.getString("Lon"));
                    Intent intent = new Intent(context, MapDirectionActivity.class);
                    intent.putExtra(Constants.MAP_ROUTE, url);
                    intent.putExtra(Constants.DEST_LAT, json.getString("Lat"));
                    intent.putExtra(Constants.DEST_LNG, json.getString("Lon"));
                    intent.putExtra(Constants.DEST_NAME, json.getString("HQ_Name"));
                    context.startActivity(intent);


                } catch (Exception e) {
                    Log.v(TAG, e.getMessage());
                }

            }
        });

        holder.llMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common_Class common_class = new Common_Class(MyTeamActivity.myTeamActivity);
                common_class.showCalDialog(MyTeamActivity.myTeamActivity, "Do you want to Call this Outlet?", holder.tvMobile.getText().toString().replaceAll(",", ""));
            }
        });


    }

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
