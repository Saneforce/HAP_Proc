package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.FreezerStatusModel;

import java.io.File;
import java.util.ArrayList;

public class FreezerStatusAdapter extends RecyclerView.Adapter<FreezerStatusAdapter.ViewHolder> {
    Context context;
    ArrayList<FreezerStatusModel> list;
    FilesAdapter filesAdapter;

    PerformClicks performClicks;

    public FreezerStatusAdapter(Context context, ArrayList<FreezerStatusModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setPerformClicks(PerformClicks performClicks) {
        this.performClicks = performClicks;
    }

    @NonNull
    @Override
    public FreezerStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FreezerStatusAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_freezer_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FreezerStatusAdapter.ViewHolder holder, int position) {
        holder.freezerGroup.setText(list.get(holder.getBindingAdapterPosition()).getFreezerGroup());
        holder.freezerStatus.setText(list.get(holder.getBindingAdapterPosition()).getFreezerStatus());
        holder.freezerMake.setText(list.get(holder.getBindingAdapterPosition()).getFreezerMake());
        holder.freezerTagNo.setText(list.get(holder.getBindingAdapterPosition()).getFreezerTagNo());
        holder.freezerCapacity.setText(list.get(holder.getBindingAdapterPosition()).getFreezerCapacity());
        holder.expectSaleVal.setText(list.get(holder.getBindingAdapterPosition()).getExpectedSalesValue());
        holder.depositAmt.setText(list.get(holder.getBindingAdapterPosition()).getDepositAmount());

        if (list.get(holder.getBindingAdapterPosition()).getFreezerStatus().equalsIgnoreCase("Company Provided")) {
            holder.llExpecSalVal.setVisibility(View.VISIBLE);
        } else {
            holder.llExpecSalVal.setVisibility(View.GONE);
        }

        filesAdapter = new FilesAdapter(list.get(holder.getBindingAdapterPosition()).getPhotoList(), R.layout.adapter_local_files_layout, context);
        holder.rvFreezerFiles.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvFreezerFiles.setAdapter(filesAdapter);

        holder.removeItem.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage("Are you sure you want to remove?");
            builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
            builder.setPositiveButton("YES", (dialog, which) -> {
                if (list.size() > 1) {
                    list.remove(holder.getBindingAdapterPosition());
                    notifyItemRemoved(holder.getBindingAdapterPosition());
                    notifyItemRangeChanged(holder.getBindingAdapterPosition(), list.size());
                } else if (list.size() == 1) {
                    Toast.makeText(context, "Minimum one freezer required", Toast.LENGTH_SHORT).show();
                    list.set(0, new FreezerStatusModel("", "", "", "", "", "", "", "", "", "", 0, new ArrayList<>()));
                    notifyItemChanged(0);
                }
            });
            builder.create().show();
        });
        holder.captureImage.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    File file = new File(fullPath);
                    Uri contentUri = Uri.fromFile(file);
                    list.get(holder.getBindingAdapterPosition()).getPhotoList().add(contentUri.toString());
                    filesAdapter.notifyItemInserted(list.get(holder.getBindingAdapterPosition()).getPhotoList().size());
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            intent.putExtra("allowance", "TAClaim");
            context.startActivity(intent);
        });
        holder.freezerGroup.setOnClickListener(v -> {
            if (performClicks != null) {
                performClicks.onGroupSelect(holder.getBindingAdapterPosition());
            }
        });
        holder.freezerStatus.setOnClickListener(v -> {
            if (performClicks != null) {
                performClicks.onStatusSelect(holder.getBindingAdapterPosition());
            }
        });
        holder.freezerCapacity.setOnClickListener(v -> {
            if (performClicks != null) {
                performClicks.onCapacitySelect(holder.getBindingAdapterPosition());
            }
        });

        holder.expectSaleVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.get(holder.getBindingAdapterPosition()).setExpectedSalesValue(s.toString());
            }
        });
        holder.depositAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.get(holder.getBindingAdapterPosition()).setDepositAmount(s.toString());
            }
        });
        holder.freezerMake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.get(holder.getBindingAdapterPosition()).setFreezerMake(s.toString());
            }
        });
        holder.freezerTagNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.get(holder.getBindingAdapterPosition()).setFreezerTagNo(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface PerformClicks {
        void onStatusSelect(int position);

        void onGroupSelect(int position);

        void onCapacitySelect(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView freezerGroup, freezerStatus, freezerCapacity, removeItem;
        EditText freezerMake, freezerTagNo, expectSaleVal, depositAmt;
        ImageView captureImage;
        RecyclerView rvFreezerFiles;
        LinearLayout llExpecSalVal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            freezerGroup = itemView.findViewById(R.id.txFreezerGroup);
            freezerStatus = itemView.findViewById(R.id.txFreezerStatus);
            freezerCapacity = itemView.findViewById(R.id.txFreezerCapacity);
            freezerMake = itemView.findViewById(R.id.edt_retailer_freezerMake);
            freezerTagNo = itemView.findViewById(R.id.edt_retailer_freezerTagNo);
            captureImage = itemView.findViewById(R.id.ivFreezerCapture);
            removeItem = itemView.findViewById(R.id.remove);
            rvFreezerFiles = itemView.findViewById(R.id.rvFreezerFiles);
            expectSaleVal = itemView.findViewById(R.id.edt_expectSaleVal);
            depositAmt = itemView.findViewById(R.id.edt_depositAmt);
            llExpecSalVal = itemView.findViewById(R.id.llExpecSalVal);
        }
    }
}
