package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QPSFilesAdapter extends RecyclerView.Adapter<QPSFilesAdapter.MyViewHolder> {
    List<String> AryDta = new ArrayList<>();
    private Context context;
    int salRowDetailLayout;

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
                String itm = AryDta.get(position);
                Picasso.with(context)
                        .load(itm)
                        .into(holder.ivFile);
            } else {
                holder.ivFile.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return AryDta.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFile;

        public MyViewHolder(View view) {
            super(view);
            ivFile = view.findViewById(R.id.ivFile);

        }
    }
}
