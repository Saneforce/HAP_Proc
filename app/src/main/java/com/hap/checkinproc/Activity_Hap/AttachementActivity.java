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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.R;

import java.util.ArrayList;

public class AttachementActivity extends AppCompatActivity {
    ArrayList<String> intentValue;
    private GridLayout parentLinearLayout;
    FrameLayout frameLayout;
    ImageView deleteImage;
    int position;
    RelativeLayout allRelative;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_attachement);

        frameLayout = findViewById(R.id.frame_Layout_imag_preview);
        frameLayout.setBackgroundColor(Color.TRANSPARENT);

        allRelative= findViewById(R.id.re_Layout_imag_preview);
        allRelative.setBackgroundColor(Color.TRANSPARENT);

        intentValue = new ArrayList<String>();
        intentValue = (ArrayList<String>) getIntent().getSerializableExtra("Data");

        Log.v("ATTACHMENT", String.valueOf(intentValue.size()));
        Log.v("ATTACHMENT", intentValue.toString());
        parentLinearLayout = (GridLayout) findViewById(R.id.parent_linear_layout);

        parentLinearLayout.setRowCount(4);
        parentLinearLayout.setColumnCount(4);


        for (int i = 1; i <= intentValue.size(); i++) {
            position = i;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            rowView = inflater.inflate(R.layout.activity_layout_img_preview, null);

            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

            int size = parentLinearLayout.getChildCount();
            Log.v("ATTACHMENT_OUTER_SIZE", String.valueOf(size));
            for (int j = 0; j < size; j++) {

                Log.v("ATTACHMENT_SIZE", String.valueOf(size));
                Log.v("ATTACHMENT_POSITION", String.valueOf(position));
                View childView = parentLinearLayout.getChildAt(j);
                ImageView taAttach = (ImageView) (childView.findViewById(R.id.img_preview));
                deleteImage = (ImageView) childView.findViewById(R.id.img_delete);
                taAttach.setImageURI(Uri.parse((intentValue.get(j))));

            }


        }


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
            Log.v("ATTACHMENT_DELETE", String.valueOf(p));
            //    intentValue.remove();
        }

        Log.v("ATT_DELETE", String.valueOf(ps));
        intentValue.remove(ps);

        Log.v("ATTACHMENT_DE", String.valueOf(intentValue.size()));
    }
}