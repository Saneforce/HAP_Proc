package com.hap.checkinproc.SFA_Adapter;

import static com.hap.checkinproc.SFA_Activity.QPSActivity.qpsActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.Activity_Hap.AttachementActivity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.OnAttachmentDelete;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.QPSActivity;
import com.hap.checkinproc.common.FileUploadService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QPSAdapter extends RecyclerView.Adapter<QPSAdapter.MyViewHolder> {
    Context context;
    List<QPS_Modal> mData;

    Shared_Common_Pref shared_common_pref;
    Gson gson = new Gson();

    List<QPS_Modal> qpsModalList = new ArrayList<>();

    QPSFilesAdapter qpsFilesAdapter;
    private String key = "";

    public QPSAdapter(Context context, List<QPS_Modal> mData) {
        this.context = context;
        this.mData = mData;
        shared_common_pref = new Shared_Common_Pref(context);

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
        try {
            holder.sNo.setText("" + mData.get(position).getsNo());
            holder.requestNo.setText("" + mData.get(position).getRequestNo());
            holder.gift.setText("" + mData.get(position).getGift());
            holder.bookingDate.setText("" + mData.get(position).getBookingDate());
            holder.duration.setText("" + mData.get(position).getDuration());
            holder.receivedDate.setText("" + mData.get(position).getReceivedDate());
            holder.status.setText("" + mData.get(position).getStatus());

            qpsFilesAdapter = new QPSFilesAdapter(mData.get(position).getFileUrls(), R.layout.adapter_qps_files_layout, context);

            holder.rvFile.setAdapter(qpsFilesAdapter);


            getCurrentList();

            if (mData.get(position).getStatus().equalsIgnoreCase("Approved")) {
                holder.btnComplete.setVisibility(View.GONE);
                holder.ivCaptureImg.setVisibility(View.GONE);
                holder.ivAttachImg.setVisibility(View.GONE);
            } else {
                holder.btnComplete.setVisibility(View.VISIBLE);
                holder.ivCaptureImg.setVisibility(View.VISIBLE);
                holder.ivAttachImg.setVisibility(View.VISIBLE);
            }


            holder.ivCaptureImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCurrentList();
                    if (isCheckExceed(mData.get(position).getsNo() + "~key")) {
                        AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                            @Override
                            public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {


                                qpsModalList.add(new QPS_Modal(fullPath, FileName, (mData.get(position).getsNo() + "~key" + System.currentTimeMillis())));

                                shared_common_pref.save(Constants.QPS_LOCALPICLIST, gson.toJson(qpsModalList));
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
                    stat.putExtra("qps_localData", mData.get(position).getsNo() + "~key");
                    context.startActivity(stat);
                }
            });
            //working
            holder.btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    key = mData.get(position).getsNo() + "~key";
                    SaveOrder(holder.requestNo.getText().toString());

                    for (int i = 0; i < qpsModalList.size(); i++) {
                        if (qpsModalList.get(i).getFileKey().contains(key)) {

                            Intent mIntent = new Intent(context, FileUploadService.class);
                            mIntent.putExtra("mFilePath", qpsModalList.get(i).getFilePath());
                            mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
                            mIntent.putExtra("FileName", qpsModalList.get(i).getFileName());
                            //   mIntent.putExtra("Mode", "ExpClaim;" + qpsModalList.get(i).getFileKey());
                            mIntent.putExtra("Mode", "QPS");
                            FileUploadService.enqueueWork(context, mIntent);


                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e("QPSAdapter:", e.getMessage());
        }

    }


    private void SaveOrder(String reqNo) {

        JSONArray data = new JSONArray();
        JSONObject ActivityData = new JSONObject();


        try {
            JSONObject HeadItem = new JSONObject();
            HeadItem.put("divisionCode", Shared_Common_Pref.Div_Code);
            HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
            HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);

            HeadItem.put("distributorcode", Shared_Common_Pref.DistributorCode);

            HeadItem.put("date", Common_Class.GetDatewothouttime());

            ActivityData.put("QPS_Header", HeadItem);
            JSONArray Order_Details = new JSONArray();
            for (int z = 0; z < qpsModalList.size(); z++) {
                if (qpsModalList.get(z).getFileKey().contains(key)) {
                    JSONObject ProdItem = new JSONObject();
                    ProdItem.put("qps_filename", qpsModalList.get(z).getFileName());
                    ProdItem.put("qps_reqNo", reqNo);
                    Order_Details.put(ProdItem);
                }
            }
            ActivityData.put("file_Details", Order_Details);
            data.put(ActivityData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> responseBodyCall = apiInterface.approveQPSEntry(data.toString());
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                        String san = jsonObjects.getString("success");
                        Log.e("Success_Message", san);

                        if (jsonObjects.getBoolean("success")) {
                            Toast.makeText(context, jsonObjects.getString("Msg"), Toast.LENGTH_SHORT).show();
                            qpsActivity.common_class.getDb_310Data(Constants.QPS_STATUS, qpsActivity);
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("SUBMIT_VALUE", "ERROR");
            }
        });


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


    void getCurrentList() {
        qpsModalList.clear();
        if (shared_common_pref.getvalue(Constants.QPS_LOCALPICLIST).equals(""))
            qpsModalList = new ArrayList<>();
        else {
            String strQPS = shared_common_pref.getvalue(Constants.QPS_LOCALPICLIST);

            Type userType = new TypeToken<ArrayList<QPS_Modal>>() {
            }.getType();
            qpsModalList = gson.fromJson(strQPS, userType);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sNo, requestNo, gift, bookingDate, duration, receivedDate, status;
        Button btnComplete;
        ImageView ivCaptureImg, ivAttachImg;
        RecyclerView rvFile;

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
            rvFile = itemView.findViewById(R.id.rvFiles);

        }
    }
}