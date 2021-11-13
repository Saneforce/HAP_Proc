package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.MyTeamActivity;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;

import java.util.List;

public class MyTeamCategoryAdapter extends RecyclerView.Adapter<MyTeamCategoryAdapter.MyViewHolder> {
    List<Category_Universe_Modal> AryDta;
    private Context context;
    Common_Class common_class;
    int salRowDetailLayout;
    MyTeamCategoryAdapter.MyViewHolder pholder;

    public MyTeamCategoryAdapter(List<Category_Universe_Modal> jAryDta, int rowLayout, Context mContext) {
        AryDta = jAryDta;
        context = mContext;
        salRowDetailLayout = rowLayout;
        common_class = new Common_Class(context);
    }

    @NonNull
    @Override
    public MyTeamCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(salRowDetailLayout, parent, false);
        return new MyTeamCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTeamCategoryAdapter.MyViewHolder holder, int position) {
        try {
            Category_Universe_Modal itm = AryDta.get(position);
            holder.txCatname.setText(itm.getName());

            holder.llTeamType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JsonObject data = new JsonObject();
                        data.addProperty("sfcode", Shared_Common_Pref.Sf_Code);
                        data.addProperty("date", "2021-11-10");
                        data.addProperty("type", AryDta.get(position).getName());
                        common_class.getDb_310Data(Constants.MYTEAM_LOCATION, MyTeamActivity.myTeamActivity, data);

                        MyTeamActivity.selectedPos=position;
                        if (pholder != null) {
                            pholder.ivIcon.setColorFilter(context.getResources().getColor(R.color.grey_600));
                            pholder.txCatname.setTextColor(context.getResources().getColor(R.color.grey_800));
                            pholder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                        }
                        pholder = holder;
                        holder.ivIcon.setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark));
                        pholder.txCatname.setTextColor(context.getResources().getColor(R.color.black));
                        pholder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


                    } catch (Exception e) {

                    }
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return AryDta.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txCatname;
        LinearLayout llTeamType;
        ImageView ivIcon;

        public MyViewHolder(View view) {
            super(view);
            txCatname = view.findViewById(R.id.tvCategoryName);
            llTeamType = view.findViewById(R.id.llTeamType);
            ivIcon = view.findViewById(R.id.ivCategoryIcon);

        }
    }
}
