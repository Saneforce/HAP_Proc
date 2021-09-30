package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.Activity_Hap.AttachementActivity;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.OnAttachmentDelete;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.FileUploadService;

import java.util.ArrayList;
import java.util.List;

public class QPSAdapter extends RecyclerView.Adapter<QPSAdapter.MyViewHolder> {
    Context context;
    List<QPS_Modal> mData;
    private String imageServer = "", imageConvert = "";
    private String key = "";

    List<QPS_Modal> qpsModalList = new ArrayList<>();

    public QPSAdapter(Context context, List<QPS_Modal> mData) {
        this.context = context;
        this.mData = mData;

    }

    @NonNull
    @Override
    public QPSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_qps_layout, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(QPSAdapter.MyViewHolder holder, int position) {
        holder.sNo.setText("" + mData.get(position).getsNo());
        holder.requestNo.setText("" + mData.get(position).getRequestNo());
        holder.gift.setText("" + mData.get(position).getGift());
        holder.bookingDate.setText("" + mData.get(position).getBookingDate());
        holder.duration.setText("" + mData.get(position).getDuration());
        holder.receivedDate.setText("" + mData.get(position).getReceivedDate());
        holder.status.setText("" + mData.get(position).getStatus());

        if (mData.get(position).getStatus().equalsIgnoreCase("COMPLETED"))
            holder.btnComplete.setVisibility(View.GONE);
        else
            holder.btnComplete.setVisibility(View.VISIBLE);


        holder.ivCaptureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckExceed(mData.get(position).getsNo() + "key")) {
                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {

                            imageServer = FileName;
                            imageConvert = fullPath;

                            qpsModalList.add(new QPS_Modal(fullPath, FileName, (mData.get(position).getsNo() + "key" + System.currentTimeMillis())));

                        }
                    });
                    Intent intent = new Intent(context, AllowancCapture.class);
                    intent.putExtra("allowance", "TAClaim");
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Limit Exceed...", Toast.LENGTH_SHORT).show();
                }


            }
        });

        holder.ivAttachImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                    @Override
                    public void OnImageDelete(String Mode, int ImgCount) {

                    }
                });


                Intent stat = new Intent(context, AttachementActivity.class);
                Gson gson = new Gson();
                stat.putExtra("keyList", gson.toJson(qpsModalList));
                stat.putExtra("pos", mData.get(position).getsNo() + "key");
                context.startActivity(stat);
            }
        });

//working
       /* holder.ivCaptureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                    @Override
                    public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                        imageServer = FileName;
                        imageConvert = fullPath;
                        long nano_startTime = System.nanoTime();
                        String ImageUKey = "EK" + Shared_Common_Pref.Sf_Code + nano_startTime;


                        String sMode = "TL;" + Common_Class.GetDatewothouttime() + ";" + "EK" + Shared_Common_Pref.Sf_Code + "-" + mData.get(position).getsNo() + ";"
                                + "Bus" + ";" + ImageUKey;

                        if (!Common_Class.isNullOrEmpty(imageServer)) {

                            Intent mIntent = new Intent(context, FileUploadService.class);
                            mIntent.putExtra("mFilePath", fullPath);
                            mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
                            mIntent.putExtra("FileName", FileName);
                            mIntent.putExtra("Mode", "ExpClaim;" + sMode);
                            FileUploadService.enqueueWork(context, mIntent);
                        }
                    }
                });
                Intent intent = new Intent(context, AllowancCapture.class);
                intent.putExtra("allowance", "TAClaim");
                context.startActivity(intent);


            }
        });

        holder.ivAttachImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                key = "EK" + Shared_Common_Pref.Sf_Code + "-" + mData.get(position).getsNo();
                AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                    @Override
                    public void OnImageDelete(String Mode, int ImgCount) {
                        if (ImgCount < 1) {
                            key = "";
                        }
                    }
                });
                Intent stat = new Intent(context, AttachementActivity.class);
                stat.putExtra("position", "EK" + Shared_Common_Pref.Sf_Code + "-" + mData.get(position).getsNo());
                stat.putExtra("headTravel", "TL");
                stat.putExtra("mode", "Bus");
                stat.putExtra("date", Common_Class.GetDatewothouttime());
                context.startActivity(stat);
            }
        });*/


        holder.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < qpsModalList.size(); i++) {
                    if (qpsModalList.get(i).getFileKey().contains(mData.get(position).getsNo() + "key")) {
                        Intent mIntent = new Intent(context, FileUploadService.class);
                        mIntent.putExtra("mFilePath", qpsModalList.get(i).getFilePath());
                        mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
                        mIntent.putExtra("FileName", qpsModalList.get(i).getFileName());
                        mIntent.putExtra("Mode", "ExpClaim;" + qpsModalList.get(i).getFileKey());
                        FileUploadService.enqueueWork(context, mIntent);

                        
                    }
                }
            }
        });
        //working
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    boolean isCheckExceed(String key) {
        if (qpsModalList.size() == 0)
            return true;
        else {

            int count = 0;
            for (int i = 0; i < qpsModalList.size(); i++) {

                if (qpsModalList.get(i).getFileKey().contains(key)) {

                    count += 1;


                }
            }

            if (count < 3)
                return true;
            else
                return false;

        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sNo, requestNo, gift, bookingDate, duration, receivedDate, status;
        Button btnComplete;
        ImageView ivCaptureImg, ivAttachImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sNo = itemView.findViewById(R.id.tvQpsSno);
            requestNo = itemView.findViewById(R.id.tvQPSReqNo);
            gift = itemView.findViewById(R.id.tvQPSGift);
            bookingDate = itemView.findViewById(R.id.tvQPSBookDate);
            duration = itemView.findViewById(R.id.tvQPSDuration);
            receivedDate = itemView.findViewById(R.id.tvQPSReceivedDate);
            status = itemView.findViewById(R.id.tvStatus);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            ivCaptureImg = itemView.findViewById(R.id.ivQPSCaptureImg);
            ivAttachImg = itemView.findViewById(R.id.ivQPSPreviewImg);

        }
    }
}