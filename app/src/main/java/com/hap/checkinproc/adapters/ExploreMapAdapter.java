package com.hap.checkinproc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Nearby_Outlets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExploreMapAdapter extends RecyclerView.Adapter<ExploreMapAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private final JSONArray array;
    Context context;
    String laty, lngy;
    JSONObject json;

    public ExploreMapAdapter(Activity context, JSONArray array, String laty, String lngy) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.array = array;
        this.laty = laty;
        this.lngy = lngy;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.explorelist, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            json = array.getJSONObject(position);
            holder.txt_dr.setText(json.getString("name"));
            holder.txt_add.setText(json.getString("vicinity"));
            JSONArray jsonA = json.getJSONArray("photos");
            if (jsonA.length() > 0) {
                JSONObject jo = jsonA.getJSONObject(0);
                JSONArray ja = jo.getJSONArray("html_attributions");

                StringBuilder bu = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?photoreference=");
                bu.append(jo.getString("photo_reference"));
                bu.append("&sensor=false");
                bu.append("&maxheight=" + jo.getString("height"));
                bu.append("&maxwidth=" + jo.getString("width"));
                bu.append("&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU");
                //https://maps.googleapis.com/maps/api/place/photo?photoreference=CmRaAAAAZGVKYoIApQ0FY9qrZcoSquyHJTmRzKG6cwIUAeDA7ARoddKmfSG9mb1KUYzBMkxj7IWR9efFGWhKLyyivm2gkWvdhWQZtqBc0GszOetFku_7MfjGyrhCJ5vgs2Il4U8vEhCwnDmXrtkrQWQYxUxKH_heGhSTiUC8g9jAdoZ0BJTMN5dEXXLJDA&sensor=false&maxheight=MAX_HEIGHT&maxwidth=MAX_WIDTH&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU

                Glide.with(context)
                        .load(bu.toString()) // image url
                        .placeholder(R.drawable.profile_img) // any placeholder to load at start
                        .error(R.drawable.profile_img)  // any image in case of error
                        .override(200, 200) // resizing
                        .centerCrop()
                        .into(holder.shopPhoto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.rl_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //productChangeListener.checkPosition(position);
            }
        });

        holder.btnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Nearby_Outlets.nearby_outlets.getPlaceIdValues(position);

//                Intent intent = new Intent(context, AddNewRetailer.class);
//                try {
//                    intent.putExtra(Constants.PLACE_ID, json.getString("place_id"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                context.startActivity(new Intent(context, AddNewRetailer.class));
            }
        });


       /* holder.btn_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_dr=array.get(position).getName();
                full_add=array.get(position).getAddr();
                getPlaceID(array.get(position).getPhno(),position);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView shopPhoto;
        TextView txt_dr, txt_add;
        LinearLayout rl_popup;
        Button btnAddToList;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_dr = (TextView) itemView.findViewById(R.id.ShopName);
            txt_add = (TextView) itemView.findViewById(R.id.ShopAddr);
            shopPhoto = (ImageView) itemView.findViewById(R.id.ShopPhoto);
            rl_popup = (LinearLayout) itemView.findViewById(R.id.rl_popup);
            btnAddToList = (Button) itemView.findViewById(R.id.btnAddtoList);
            // btn_route=(Button)itemView.findViewById(R.id.btn_route);
           /* btn_visit=(Button)itemView.findViewById(R.id.btn_visit);
            img_profile=(ImageView)itemView.findViewById(R.id.img_profile);*/


        }
    }
}
