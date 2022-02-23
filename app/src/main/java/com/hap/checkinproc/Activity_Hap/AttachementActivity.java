package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.OnAttachmentDelete;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.QPS_Modal;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttachementActivity extends AppCompatActivity {
    ArrayList<String> intentValue;
    private GridLayout parentLinearLayout;
    FrameLayout frameLayout;
    ImageView deleteImage;
    Integer position, ImgCount = 0;
    RelativeLayout allRelative;
    Shared_Common_Pref shared_common_pref;
    String ImageUKey = "", ImageUrl = "", DateTime = "", sMode = "";

    List<QPS_Modal> qpsModalList = new ArrayList<>();
    static OnAttachmentDelete deleteListener;
    private Gson gson;
    private String sfcode = "";

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_attachement);

        shared_common_pref = new Shared_Common_Pref(this);
        frameLayout = findViewById(R.id.frame_Layout_imag_preview);
        frameLayout.setBackgroundColor(Color.TRANSPARENT);

        allRelative = findViewById(R.id.re_Layout_imag_preview);
        allRelative.setBackgroundColor(Color.TRANSPARENT);

        intentValue = new ArrayList<String>();
        sfcode = String.valueOf(getIntent().getStringExtra("sfCode"));

        allImage(String.valueOf(getIntent().getSerializableExtra("position")),
                String.valueOf(getIntent().getSerializableExtra("headTravel")),
                String.valueOf(getIntent().getSerializableExtra("mode")),
                String.valueOf(getIntent().getSerializableExtra("date")));

        sMode = String.valueOf(getIntent().getSerializableExtra("mode"));
        parentLinearLayout = (GridLayout) findViewById(R.id.parent_linear_layout);

        parentLinearLayout.setColumnCount(3);
        parentLinearLayout.setRowCount(4);
        ImageUKey = String.valueOf(getIntent().getSerializableExtra("Delete"));


//        if (getIntent().getStringExtra("qps_localData") != null && !getIntent().getStringExtra("qps_localData").equals("")) {
//            showLocalImgList();
//        }


    }

    private void showLocalImgList() {
        try {
            gson = new Gson();
            String strQPS = shared_common_pref.getvalue(Constants.QPS_LOCALPICLIST);

            Type userType = new TypeToken<ArrayList<QPS_Modal>>() {
            }.getType();
            qpsModalList = gson.fromJson(strQPS, userType);

            List<QPS_Modal> filterList = new ArrayList<>();
            filterList.clear();

            for (int i = 0; i < qpsModalList.size(); i++) {
                if (qpsModalList.get(i).getFileKey().contains((getIntent().getStringExtra("qps_localData")))) {
                    filterList.add(qpsModalList.get(i));
                }
            }


            ImgCount = filterList.size();
            for (int m = 0; m < filterList.size(); m++) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View rowView = inflater.inflate(R.layout.activity_layout_img_preview, null);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

                View childView = parentLinearLayout.getChildAt(m);
                ImageView taAttach = (ImageView) (childView.findViewById(R.id.img_preview));

                File file = new File(filterList.get(m).getFilePath());
                Uri contentUri = Uri.fromFile(file);

                Picasso.with(AttachementActivity.this)
                        .load(contentUri)
                        .into(taAttach);

                position = parentLinearLayout.indexOfChild(rowView);
                View cv = parentLinearLayout.getChildAt(position);
                ImageView taAttachs = (ImageView) (cv.findViewById(R.id.img_preview));
                deleteImage = (ImageView) cv.findViewById(R.id.img_delete);
                if (ImageUKey.equals("1")) {
                    deleteImage.setVisibility(View.GONE);
                } else {
                    deleteImage.setVisibility(View.VISIBLE);
                }


                deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteImage(file, (View) v.getParent());

                    }
                });
                taAttachs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
//                        intent.putExtra("ImageUrl", jsonObject.get("Imageurl").getAsString());
//                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            Log.e("AttachQPS: ", e.getMessage());
        }


    }

    public void allImage(String pos, String HeadTravel, String Mode, String Date) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        //    Call<JsonArray> mCall = apiInterface.allPreview(pos, HeadTravel, Mode, Date, shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
        Call<JsonArray> mCall = apiInterface.allPreview(pos, HeadTravel, Mode, Date, Shared_Common_Pref.TravelAllowance == 0 ? shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) : sfcode);
        Log.e("IMAGE_View_TRAN", mCall.request().toString());
        mCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                Log.e("JSON_ARRAY", jsonArray.toString());
                ImgCount = jsonArray.size();
                for (int m = 0; m < jsonArray.size(); m++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(m);

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    final View rowView = inflater.inflate(R.layout.activity_layout_img_preview, null);
                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

                    View childView = parentLinearLayout.getChildAt(m);
                    ImageView taAttach = (ImageView) (childView.findViewById(R.id.img_preview));

                    Picasso.with(AttachementActivity.this)
                            .load(jsonObject.get("Imageurl").getAsString())
                            .into(taAttach);

                    position = parentLinearLayout.indexOfChild(rowView);
                    View cv = parentLinearLayout.getChildAt(position);
                    ImageView taAttachs = (ImageView) (cv.findViewById(R.id.img_preview));
                    deleteImage = (ImageView) cv.findViewById(R.id.img_delete);
                    if (ImageUKey.equals("1")) {
                        deleteImage.setVisibility(View.GONE);
                    } else {
                        deleteImage.setVisibility(View.VISIBLE);
                    }


                    int finalIndex = m;
                    deleteImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteImage(jsonObject.get("Img_U_key").getAsString(), jsonObject.get("lat").getAsString(), jsonObject.get("Insert_Date_Time").getAsString(), (View) v.getParent());

                        }
                    });
                    taAttachs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                            intent.putExtra("ImageUrl", jsonObject.get("Imageurl").getAsString());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public static void setOnAttachmentDeleteListener(OnAttachmentDelete mOnAttachmentDelete) {
        deleteListener = mOnAttachmentDelete;
    }

    public void deleteImage(String ImageUKey, String ImageUrl, String DateTime, View view) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> mCall = apiInterface.dltePrvws(ImageUrl, ImageUKey, DateTime, shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
        Log.e("IMAGE_DELETE_REPONSE", mCall.request().toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                Log.e("RESPONSE", jsonObject.get("success").getAsString());
                //parentLinearLayout.removeViewAt(Position);
                parentLinearLayout.removeView(view);
                ImgCount--;
                deleteListener.OnImageDelete(sMode, ImgCount);
//                if (jsonObject.get("success").getAsString().equals("true")) {
//                    finish();
//                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }


    public void deleteImage(File file, View view) {

        for (int i = 0; i < qpsModalList.size(); i++) {
            if (qpsModalList.get(i).getFilePath().equals(file.getAbsolutePath())) {
                qpsModalList.remove(i);
                shared_common_pref.save(Constants.QPS_LOCALPICLIST, gson.toJson(qpsModalList));
            }
        }

        parentLinearLayout.removeView(view);
        ImgCount--;
        if (file.exists())
            file.delete();

    }

    public void DeleteLayout(View v) {
        finish();
    }
}