package com.hap.checkinproc.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.hap.checkinproc.Activity_Hap.ImageCapture;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

import java.io.File;

public class AllowanceActivityTwo extends AppCompatActivity {
    String ModeOfTravel="", StartedKm="", StartedImage="", EndedImageName="";
    Shared_Common_Pref mShared_common_pref;
    TextView TextModeTravel, TextStartedKm;
    ImageView StartedKmImage, EndedKmImage;
    Button takeEndedPhoto, submitAllowance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowance_two);
        mShared_common_pref = new Shared_Common_Pref(this);
        ModeOfTravel = mShared_common_pref.getvalue("mode_of_travel");
        StartedKm = mShared_common_pref.getvalue("Started_km");
        StartedImage = mShared_common_pref.getvalue("Started_Image");


        TextModeTravel = findViewById(R.id.txt_mode_travel);
        TextStartedKm = findViewById(R.id.txt_started_km);
        StartedKmImage = findViewById(R.id.img_started_km);
        EndedKmImage = findViewById(R.id.img_ended_km);
        takeEndedPhoto = findViewById(R.id.btn_take_photo);
        submitAllowance = findViewById(R.id.submit_allowance);

        TextModeTravel.setText(ModeOfTravel);
        TextStartedKm.setText(StartedKm);
        StartedKmImage.setImageURI(Uri.parse(StartedImage));
        StartedKmImage.setRotation((float) 90);
        takeEndedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EndedImageName = "EndedImageName.jpeg";
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(getExternalCacheDir().getPath(), EndedImageName);
                Uri uri = FileProvider.getUriForFile(AllowanceActivityTwo.this, getApplicationContext().getPackageName() + ".provider", file);
                m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(m_intent, 2);

                Log.e("Started_km", "Click is working");

            }
        });

        submitAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhoto = new Intent(AllowanceActivityTwo.this, ImageCapture.class);
                takePhoto.putExtra("Mode", "COUT");
                startActivity(takePhoto);
            }
        });




    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {

                    EndedImageName = "EndedImageName.jpeg";
                    File file = new File(getExternalCacheDir().getPath(), EndedImageName);
                    Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                    EndedKmImage.setImageURI(uri);
                    EndedKmImage.setRotation((float) 90);

                    Log.e("Started_km", String.valueOf(uri));

                }
                break;
        }
    }

}