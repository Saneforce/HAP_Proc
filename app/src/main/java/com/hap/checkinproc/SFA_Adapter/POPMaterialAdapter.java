package com.hap.checkinproc.SFA_Adapter;

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
import com.hap.checkinproc.SFA_Activity.POPActivity;
import com.hap.checkinproc.common.FileUploadService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POPMaterialAdapter extends RecyclerView.Adapter<POPMaterialAdapter.MyViewHolder> {
    Context context;

    JSONArray jsonArray;
    Shared_Common_Pref shared_common_pref;
    Gson gson = new Gson();

    List<QPS_Modal> qpsModalList = new ArrayList<>();

    FilesAdapter filesAdapter;
    ArrayList<List<String>> fileList = new ArrayList<>();
    private String key = "";
    Common_Class common_class;

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public POPMaterialAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
        shared_common_pref = new Shared_Common_Pref(context);
        common_class = new Common_Class(context);

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itm = jsonArray.getJSONObject(i);

                String images = itm.getString("Images");
                List<String> items = new ArrayList<>();
                String[] res = images.split("[,]", 0);
                for (String myStr : res) {
                    if (!Common_Class.isNullOrEmpty(myStr)) {
                        items.add(myStr);
                    }

                }

                fileList.add(items);
            }
        } catch (Exception e) {

        }


    }

    @NonNull
    @Override
    public POPMaterialAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_popmaterial_layout, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(POPMaterialAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject itm = jsonArray.getJSONObject(position);

            holder.receivedDate.setText("" + itm.getString("Received_Date"));
            holder.status.setText("" + itm.getString("POP_Status"));
            holder.materialName.setText("" + itm.getString("POP_Name"));


            getCurrentList();

            if (itm.getString("POP_Status").equalsIgnoreCase("Approved")) {
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
                    try {
                        getCurrentList();

                        key = itm.getString("POP_Req_ID") + "~POPkey";
                        if (isCheckExceed(key)) {
                            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                                @Override
                                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {


                                    qpsModalList.add(new QPS_Modal(fullPath, FileName, (key + System.currentTimeMillis())));

                                    shared_common_pref.save(Constants.QPS_LOCALPICLIST, gson.toJson(qpsModalList));
                                }
                            });
                            Intent intent = new Intent(context, AllowancCapture.class);
                            intent.putExtra("allowance", "TAClaim");
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Limit Exceed...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }


                }
            });

            holder.ivAttachImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        getCurrentList();
                        AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                            @Override
                            public void OnImageDelete(String Mode, int ImgCount) {

                            }
                        });
                        key = itm.getString("POP_Req_ID") + "~POPkey";


                        Intent stat = new Intent(context, AttachementActivity.class);
                        stat.putExtra("qps_localData", key);
                        context.startActivity(stat);
                    } catch (Exception e) {

                    }
                }
            });

            //working
            holder.btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        key = itm.getString("POP_Req_ID") + "~POPkey";

                        SaveOrder(itm.getString("Trans_Sl_No"), itm.getString("POP_Req_ID"));

                        for (int i = 0; i < qpsModalList.size(); i++) {
                            if (qpsModalList.get(i).getFileKey().contains(key)) {

                                Intent mIntent = new Intent(context, FileUploadService.class);
                                mIntent.putExtra("mFilePath", qpsModalList.get(i).getFilePath());
                                mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
                                mIntent.putExtra("FileName", qpsModalList.get(i).getFileName());
                                //   mIntent.putExtra("Mode", "ExpClaim;" + qpsModalList.get(i).getFileKey());
                                mIntent.putExtra("Mode", "POP");
                                FileUploadService.enqueueWork(context, mIntent);


                            }
                        }
                    } catch (Exception e) {

                    }
                }
            });


            if (fileList != null && fileList.size() > position && fileList.get(position).size() > 0) {
                filesAdapter = new FilesAdapter(fileList.get(position), R.layout.adapter_qps_files_layout, context);

                holder.rvFile.setAdapter(filesAdapter);
            }
        } catch (Exception e) {
            Log.e("POPMAterialAdaptr:", e.getMessage());
        }

    }


    private void SaveOrder(String transNo, String popId) {

        JSONArray data = new JSONArray();
        JSONObject ActivityData = new JSONObject();


        try {
            JSONObject HeadItem = new JSONObject();
            HeadItem.put("divisionCode", Shared_Common_Pref.Div_Code);
            HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
            HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);

            HeadItem.put("distributorcode", shared_common_pref.getvalue(Constants.Distributor_Id));

            HeadItem.put("date", Common_Class.GetDatewothouttime());

            HeadItem.put("pop_reqId", popId);
            HeadItem.put("pop_TransNo", transNo);


            ActivityData.put("POP_Header", HeadItem);
            JSONArray Order_Details = new JSONArray();
            for (int z = 0; z < qpsModalList.size(); z++) {
                if (qpsModalList.get(z).getFileKey().contains(key)) {
                    JSONObject ProdItem = new JSONObject();
                    ProdItem.put("pop_filename", qpsModalList.get(z).getFileName());

                    Order_Details.put(ProdItem);
                }
            }
            ActivityData.put("file_Details", Order_Details);
            data.put(ActivityData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> responseBodyCall = apiInterface.approvePOPEntry(data.toString());
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObjects = new JSONObject(response.body().toString());

                        if (jsonObjects.getBoolean("success")) {
                            common_class.getDb_310Data(Constants.POP_ENTRY_STATUS, POPActivity.popActivity);
                        }
                        Toast.makeText(context, jsonObjects.getString("Msg"), Toast.LENGTH_SHORT).show();

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
        return jsonArray.length();
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
        TextView requestNo, receivedDate, status, materialName;
        Button btnComplete;
        ImageView ivCaptureImg, ivAttachImg;
        RecyclerView rvFile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            receivedDate = itemView.findViewById(R.id.tvReceivedDate);
            status = itemView.findViewById(R.id.tvStatus);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            ivCaptureImg = itemView.findViewById(R.id.ivQPSCaptureImg);
            ivAttachImg = itemView.findViewById(R.id.ivQPSPreviewImg);
            rvFile = itemView.findViewById(R.id.rvFiles);
            materialName = itemView.findViewById(R.id.tvPOPMaterial);

        }
    }
}