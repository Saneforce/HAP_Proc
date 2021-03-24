package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.ImageCapture;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Activity.View_All_Status_Activity;

public class HomeRptRecyler  extends RecyclerView.Adapter<HomeRptRecyler.ViewHolder>{
    private static final String TAG = "Home Dashboard reports";
    private JsonArray mArrayList = new JsonArray();
    private Context mContext;

    public HomeRptRecyler(JsonArray arrayList, Context mContext) {
        this.mArrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HomeRptRecyler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homerptrecyler, parent, false);
        HomeRptRecyler.ViewHolder holder= new HomeRptRecyler.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        JsonObject itm=mArrayList.get(position).getAsJsonObject();
        holder.txtLable.setText(itm.get("name").getAsString());
        holder.txtValue.setText(Html.fromHtml(itm.get("value").getAsString()));
        Log.d(TAG, "onBindViewHolder: ColorCd "+itm.get("color").getAsString());
        if(!itm.get("color").getAsString().equalsIgnoreCase(""))
        holder.txtValue.setTextColor(Color.parseColor(itm.get("color").getAsString()));
        holder.txtValue.setMovementMethod(LinkMovementMethod.getInstance());
        if(itm.get("Link")!=null){
        if(itm.get("Link").getAsBoolean()==true){
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, View_All_Status_Activity.class);
                    intent.putExtra("Priod", itm.get("Priod").getAsString());
                    intent.putExtra("Status", holder.txtLable.getText());
                    mContext.startActivity(intent);
                }
            });
        }
        }
        try {
            if (itm.get("type").getAsString().equalsIgnoreCase("geo")) {
                holder.txtValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=" + holder.txtValue.getText()));
                        mContext.startActivity(browserIntent);
                    }
                });
            }
        }
        catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {

        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtLable,txtValue;
        LinearLayout parentLayout;
        //CardView secondarylayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLable=itemView.findViewById(R.id.txtlable);
            txtValue=itemView.findViewById(R.id.txtval);
            parentLayout=itemView.findViewById(R.id.parent_layout);
            //secondarylayout=itemView.findViewById(R.id.secondary_layout);
        }
    }
}