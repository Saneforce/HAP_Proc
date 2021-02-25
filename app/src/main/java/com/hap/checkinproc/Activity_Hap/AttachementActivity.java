package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
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
    int position;
    RelativeLayout allRelative;
    Shared_Common_Pref shared_common_pref;
    Uri uri;


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
        /* intentValue = (ArrayList<String>) getIntent().getSerializableExtra("Data");*/


        Log.v("ATTACHMENT", String.valueOf(intentValue.size()));
        Log.v("ATTACHMENT", String.valueOf(getIntent().getSerializableExtra("position")));
        Log.v("ATTACHMENT", String.valueOf(getIntent().getSerializableExtra("headTravel")));
        Log.v("ATTACHMENT", String.valueOf(getIntent().getSerializableExtra("mode")));
        Log.v("ATTACHMENT", String.valueOf(getIntent().getSerializableExtra("date")));

        allImage(String.valueOf(getIntent().getSerializableExtra("position")),
                String.valueOf(getIntent().getSerializableExtra("headTravel")),
                String.valueOf(getIntent().getSerializableExtra("mode")),
                String.valueOf(getIntent().getSerializableExtra("date")));



        parentLinearLayout = (GridLayout) findViewById(R.id.parent_linear_layout);

        parentLinearLayout.setRowCount(4);
        parentLinearLayout.setColumnCount(4);

       /* for (int i = 1; i <= intentValue.size(); i++) {
            position = i;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            rowView = inflater.inflate(R.layout.activity_layout_img_preview, null);

            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

            int size = parentLinearLayout.getChildCount();
            for (int j = 0; j < size; j++) {


                View childView = parentLinearLayout.getChildAt(j);
                ImageView taAttach = (ImageView) (childView.findViewById(R.id.img_preview));
                deleteImage = (ImageView) childView.findViewById(R.id.img_delete);
                taAttach.setImageURI(Uri.parse((intentValue.get(j))));

            }


        }*/


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

                for (int m = 0; m < jsonArray.size(); m++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(m);

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View rowView = null;

                    rowView = inflater.inflate(R.layout.activity_layout_img_preview, null);
                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

                    View childView = parentLinearLayout.getChildAt(m);
                    ImageView taAttach = (ImageView) (childView.findViewById(R.id.img_preview));
                    deleteImage = (ImageView) childView.findViewById(R.id.img_delete);

                    Picasso.with(AttachementActivity.this)
                            .load(jsonObject.get("Imageurl").getAsString())
                            .into(taAttach);
                }



            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });


    }


    public void DeleteLayout(View v) {
        finish();
    }

    public void OnItemDelete(View v) {
        int size = parentLinearLayout.getChildCount();


        Log.d("PARENT_COUNT", String.valueOf(size));
        parentLinearLayout.removeView((View) v.getParent());
        int ps = 0;
        for (int p = 0; p < size; p++) {
            ps = p;

            //    intentValue.remove();
        }

        Log.v("ATT_DELETE", String.valueOf(ps));
        intentValue.remove(ps);


    }
}