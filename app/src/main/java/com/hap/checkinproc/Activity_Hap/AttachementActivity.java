package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.Interface.OnAttachmentDelete;
import com.hap.checkinproc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttachementActivity extends AppCompatActivity {
    ArrayList<String> intentValue;
    private GridLayout parentLinearLayout;
    FrameLayout frameLayout;
    ImageView deleteImage;
    Integer position,ImgCount=0;
    RelativeLayout allRelative;
    Shared_Common_Pref shared_common_pref;
    String ImageUKey = "", ImageUrl = "", DateTime = "",sMode="";


    static OnAttachmentDelete deleteListener;

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
        allImage(String.valueOf(getIntent().getSerializableExtra("position")),
                String.valueOf(getIntent().getSerializableExtra("headTravel")),
                String.valueOf(getIntent().getSerializableExtra("mode")),
                String.valueOf(getIntent().getSerializableExtra("date")));
        sMode=String.valueOf(getIntent().getSerializableExtra("mode"));
        parentLinearLayout = (GridLayout) findViewById(R.id.parent_linear_layout);

        parentLinearLayout.setColumnCount(3);
        parentLinearLayout.setRowCount(4);
        ImageUKey = String.valueOf(getIntent().getSerializableExtra("Delete"));


    }

    public void allImage(String pos, String HeadTravel, String Mode, String Date) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonArray> mCall = apiInterface.allPreview(pos, HeadTravel, Mode, Date, shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
        Log.e("IMAGE_View_TRAN", mCall.request().toString());
        mCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                Log.e("JSON_ARRAY", jsonArray.toString());
                ImgCount=jsonArray.size();
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
    public void deleteImage(String ImageUKey, String ImageUrl, String DateTime,View view) {
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
                deleteListener.OnImageDelete(sMode,ImgCount);
//                if (jsonObject.get("success").getAsString().equals("true")) {
//                    finish();
//                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }

    public void DeleteLayout(View v) {
        finish();
    }
}