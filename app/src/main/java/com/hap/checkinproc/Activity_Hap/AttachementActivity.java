package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.R;

import java.util.ArrayList;

public class AttachementActivity extends AppCompatActivity {
    ArrayList<String> intentValue;
    private LinearLayout parentLinearLayout;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachement);
        intentValue = new ArrayList<String>();
        intentValue = (ArrayList<String>) getIntent().getSerializableExtra("Data");

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);

        for (int i = 1; i <= intentValue.size(); i++) {

         /*   LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(15, 15, 15, 15);

            rowView = inflater.inflate(R.layout.activity_layout_img_preview, null);

            parentLinearLayout.addView(rowView, layoutParams);



                View childView = parentLinearLayout.getChildAt(i);

                ImageView taAttach = (ImageView) (childView.findViewById(R.id.img_preview));
                taAttach.setImageURI(Uri.parse((intentValue.get(i-1))));*/


            Log.e("array_length", String.valueOf(intentValue.get(i-1)));
            RelativeLayout childRel = new RelativeLayout(this);
            RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ImageView edt = new ImageView(this);
            edt.setLayoutParams(layoutparams_3);
            edt.setImageURI(Uri.parse((intentValue.get(i-1))));
            childRel.addView(edt);

            parentLinearLayout.addView(childRel, parentLinearLayout.getChildCount());
        }


    }
}