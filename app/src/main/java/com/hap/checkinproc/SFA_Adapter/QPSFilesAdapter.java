package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity_Hap.ProductImageView;
import com.hap.checkinproc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QPSFilesAdapter extends RecyclerView.Adapter<QPSFilesAdapter.MyViewHolder> {
    List<String> AryDta = new ArrayList<>();
    private Context context;
    int salRowDetailLayout;
    String itm = "";

    public QPSFilesAdapter(List<String> jAryDta, int rowLayout, Context mContext) {
        AryDta = jAryDta;
        context = mContext;
        salRowDetailLayout = rowLayout;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(salRowDetailLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {


            if (AryDta != null && AryDta.size() > 0) {
                itm = AryDta.get(position);
                Picasso.with(context)
                        .load(itm)
                        .into(holder.ivFile);
            } else {
                holder.ivFile.setVisibility(View.GONE);
            }


            holder.ivFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductImageView.class);
                    intent.putExtra("ImageUrl", AryDta.get(position));
                    context.startActivity(intent);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (AryDta != null && AryDta.size() > 0)
            return AryDta.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFile;

        public MyViewHolder(View view) {
            super(view);
            ivFile = view.findViewById(R.id.ivFile);

        }
    }
}
