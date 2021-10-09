package com.hap.checkinproc.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Nearby_Outlets;
import com.hap.checkinproc.SFA_Activity.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreMapAdapter extends RecyclerView.Adapter<ExploreMapAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray array;
    Context context;
    String laty, lngy;
    JSONObject json;
    StringBuilder bu;
    Type userType;
    List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> Retailer_Modal_List;


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

    public void notifyData(JSONArray array) {
        this.array = array;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            json = array.getJSONObject(position);
            holder.txt_dr.setText(json.getString("name"));
            holder.txt_add.setText(json.getString("vicinity"));

            String place_id = json.getString("place_id");

            // DatabaseHandler db = new DatabaseHandler(context);

            Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(context);

            Retailer_Modal_List = new ArrayList<>();
            // String outletserializableob = String.valueOf(db.getMasterData(Constants.Retailer_OutletList));
            String outletserializableob = shared_common_pref.getvalue(Constants.Retailer_OutletList);

            Gson gson = new Gson();

            userType = new TypeToken<ArrayList<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List>>() {
            }.getType();
            Retailer_Modal_List = gson.fromJson(outletserializableob, userType);


           /* for (int i = 0; i < Retailer_Modal_List.size(); i++) {
                Retailer_Modal_List.get(i).setPlace_id("ChIJ6fBt_tVnUjoRVxxz1mgBipI");
                if (Retailer_Modal_List.get(i).getPlace_id().equals(place_id)) {
                  //  holder.btnAddToList.setText("Marked");
                    holder.btnAddToList.setBackgroundResource(R.drawable.button_greenbg);
                    Log.v("ExploreAdapter: ", position + ":Marked");
                    break;
                } else {
                   // holder.btnAddToList.setText("Direction");
                    holder.btnAddToList.setBackgroundResource(R.drawable.button_blueg);
                    Log.v("ExploreAdapter: ", position + ":Direction");
                }

            }*/


            if (json.has("photos")) {
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
            } else {
                Glide.with(context)
                        .load(R.drawable.profile_img) // image url
                        .placeholder(R.drawable.profile_img) // any placeholder to load at start
                        .error(R.drawable.profile_img)  // any image in case of error
                        .override(200, 200) // resizing
                        .centerCrop()
                        .into(holder.shopPhoto);
            }

        } catch (Exception e) {


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


            }
        });


        holder.ivMarked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitMarkedPlace(json.getString("place_id"));
                } catch (Exception e) {

                }

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

    private void submitMarkedPlace(String placeId) {
        Common_Class common_class = new Common_Class(context);
        try {
            if (common_class.isNetworkAvailable(context)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                JSONObject HeadItem = new JSONObject();

                HeadItem.put("placeId", placeId);
                HeadItem.put("lat", Shared_Common_Pref.Outletlat);
                HeadItem.put("lng", Shared_Common_Pref.Outletlong);
                HeadItem.put("date", Common_Class.GetDate());


                Call<ResponseBody> call = service.submitMarkedData(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }

                                JSONObject jsonObject = new JSONObject(is.toString());

                                if (jsonObject.getBoolean("success")) {

                                }

                            }

                        } catch (Exception e) {


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>", t.toString());


                    }
                });
            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }


    @Override
    public int getItemCount() {
        return array.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView shopPhoto;
        TextView txt_dr, txt_add;
        LinearLayout rl_popup;
        // Button btnAddToList;

        ImageView btnAddToList, ivMarked;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_dr = (TextView) itemView.findViewById(R.id.ShopName);
            txt_add = (TextView) itemView.findViewById(R.id.ShopAddr);
            shopPhoto = (ImageView) itemView.findViewById(R.id.ShopPhoto);
            rl_popup = (LinearLayout) itemView.findViewById(R.id.rl_popup);
            btnAddToList = (ImageView) itemView.findViewById(R.id.btnAddtoList);
            ivMarked = (ImageView) itemView.findViewById(R.id.ivMarked);
            // btn_route=(Button)itemView.findViewById(R.id.btn_route);
           /* btn_visit=(Button)itemView.findViewById(R.id.btn_visit);
            img_profile=(ImageView)itemView.findViewById(R.id.img_profile);*/


        }
    }
}
