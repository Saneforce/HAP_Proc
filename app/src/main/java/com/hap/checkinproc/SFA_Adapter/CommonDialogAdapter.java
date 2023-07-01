package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.CoolerPositionModel;

import java.util.ArrayList;
import java.util.List;

public class CommonDialogAdapter extends RecyclerView.Adapter<CommonDialogAdapter.ViewHolder> {
    List<Common_Model> list;
    Context context;

    CommonDialogAdapter.ItemSelected itemSelected;

    public CommonDialogAdapter(List<Common_Model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setItemSelected(CommonDialogAdapter.ItemSelected itemSelected) {
        this.itemSelected = itemSelected;
    }

    @NonNull
    @Override
    public CommonDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommonDialogAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.common_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommonDialogAdapter.ViewHolder holder, int position) {
        Common_Model model = list.get(position);

        holder.title.setText(model.getName());

        holder.itemView.setOnClickListener(v -> {
            if (itemSelected != null) {
                itemSelected.onItemSelected(model, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemSelected {
        void onItemSelected(Common_Model model, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Name);
        }
    }
}
