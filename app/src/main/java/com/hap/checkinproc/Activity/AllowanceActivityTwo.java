package com.hap.checkinproc.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.hap.checkinproc.Activity_Hap.ImageCapture;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

import java.io.File;

public class AllowanceActivityTwo extends AppCompatActivity {
    String ModeOfTravel = "", StartedKm = "", StartedImage = "", EndedImageName = "", AllowancePrefernce = "";
    Shared_Common_Pref mShared_common_pref;
    TextView TextModeTravel, TextStartedKm;
    ImageView StartedKmImage, EndedKmImage;
    Button takeEndedPhoto, submitAllowance;
    EditText EndedEditText;
    Integer stKM = 0, endKm = 0;
    String EndImageURi = " ";


    public static final String mypreference = "mypref";
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";

    String PrivacyScreen = "";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowance_two);
        mShared_common_pref = new Shared_Common_Pref(this);
        ModeOfTravel = mShared_common_pref.getvalue("mode_of_travel");
        StartedKm = mShared_common_pref.getvalue("Started_km");
        StartedImage = mShared_common_pref.getvalue("Started_Image");
        AllowancePrefernce = mShared_common_pref.getvalue("Allowance");

        TextModeTravel = findViewById(R.id.txt_mode_travel);
        TextStartedKm = findViewById(R.id.txt_started_km);
        StartedKmImage = findViewById(R.id.img_started_km);
        EndedEditText = findViewById(R.id.ended_km);
        EndedKmImage = findViewById(R.id.img_ended_km);
        takeEndedPhoto = findViewById(R.id.btn_take_photo);
        submitAllowance = findViewById(R.id.submit_allowance);


        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);


        Log.e("STARTED_KM", StartedKm);


        EndedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EndedEditText.getText().toString() != null && !EndedEditText.getText().toString().isEmpty() && !EndedEditText.getText().toString().equals("null")) {
                    stKM = Integer.valueOf(StartedKm);
                    if (!EndedEditText.getText().toString().equals("")) {
                        endKm = Integer.valueOf(EndedEditText.getText().toString());
                    }
                    Log.e("STARTED_KM", String.valueOf(endKm));
                    if (stKM < endKm) {
                        Log.e("STARTED_KM", "GREATER");
                    } else {
                        Log.e("STARTED_KM", "Not GREATER");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (StartedImage != null && !StartedImage.isEmpty() && !StartedImage.equals("null")) {
            StartedKmImage.setImageURI(Uri.parse(StartedImage));
            StartedKmImage.setRotation((float) 90);
        }

        TextModeTravel.setText("" + ModeOfTravel);
        TextStartedKm.setText("" + StartedKm);

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
                if (!EndImageURi.equals("") && !EndedEditText.getText().toString().equals("")) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove(Name);
                    editor.remove(MOT);
                    editor.commit();
                    stKM = Integer.valueOf(StartedKm);
                    endKm = Integer.valueOf(String.valueOf(EndedEditText.getText().toString()));
                    Log.e("STARTED_KM", String.valueOf(endKm));
                    if (stKM < endKm) {
                        Intent takePhoto = new Intent(AllowanceActivityTwo.this, ImageCapture.class);
                        takePhoto.putExtra("Mode", "COUT");
                        startActivity(takePhoto);

                    } else {
                        Toast.makeText(AllowanceActivityTwo.this, "Should be greater then Started Km", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AllowanceActivityTwo.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }


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
                    EndImageURi = String.valueOf(uri);

                }
                break;
        }
    }

}