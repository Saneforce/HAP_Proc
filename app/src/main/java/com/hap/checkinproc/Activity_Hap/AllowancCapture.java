package com.hap.checkinproc.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Activity.AllowanceActivity;
import com.hap.checkinproc.Activity.AllowanceActivityTwo;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AllowancCapture extends AppCompatActivity implements SurfaceHolder.Callback {
    Button button;
    TextureView textureView;
    String imagePath;
    String imageFileName;
    Camera mCamera;
    int mCamId = 1;
    private File file;
    SurfaceView preview;
    SurfaceHolder mHolder;
    private int noOfCameras;
    Button btnRtPrv, btnOkPrv;

    Intent intents, intev;
    String mode = "", allowance = "", StartedKM = "", FromKm = "", ToKm = "", Fare = "", Closing = "";

    Shared_Common_Pref mShared_common_pref;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowanc_capture);
        StartSelfiCamera();

        intents = getIntent();
        if (getIntent().getExtras() != null) {
            allowance = intents.getStringExtra("allowance");
            mode = intents.getStringExtra("Mode");
            StartedKM = intents.getStringExtra("Started");
            FromKm = intents.getStringExtra("FromKm");
            ToKm = intents.getStringExtra("ToKm");
            Fare = intents.getStringExtra("Fare");
        }
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mShared_common_pref = new Shared_Common_Pref(this);
        textureView = (TextureView) findViewById(R.id.ImagePreview);
        button = (Button) findViewById(R.id.button_capture);

        btnRtPrv = (Button) findViewById(R.id.btnRtPrv);
        btnOkPrv = (Button) findViewById(R.id.btnOkPrv);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePicture();

            }
        });


        btnRtPrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseImgPreview();
            }
        });
        btnOkPrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImgPreview();
            }
        });
    }


    public void takePicture() {
        long tsLong = System.currentTimeMillis() / 1000;
        imageFileName = Long.toString(tsLong) + ".jpg";

        //file  = new File(Environment.getExternalStorageDirectory() + "/"+ts+".jpg");
        imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + imageFileName;
        file = new File(imagePath);
        try {
            mCamera.takePicture(null, null,
                    new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] bytes, Camera camera) {
                            try {
                                save(bytes);
                                ShowImgPreview();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void ShowImgPreview() {
        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        vwPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageURI(Uri.fromFile(file));

        if (mCamId == 1) {
            imgPreview.setRotation((float) -90.0);
        } else {
            imgPreview.setRotation((float) 90.0);
        }

    }

    private void CloseImgPreview() {
        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        vwPreview.setVisibility(View.GONE);


        if (preview != null) {

            preview = null;
            mHolder.removeCallback(AllowancCapture.this);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        preview = (SurfaceView) findViewById(R.id.PREVIEW);
        mHolder = preview.getHolder();
        mHolder.addCallback(AllowancCapture.this);
        setDefaultCameraId("back");
        mCamera = Camera.open(mCamId);
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCameraDisplayOrientation();
        mCamera.startPreview();


        Log.e("mCAmer_id", String.valueOf(mCamId));
    }


    private void saveImgPreview() {
        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        vwPreview.setVisibility(View.GONE);
        // imgPreview.setImageURI(Uri.fromFile(file));
        String filePath = String.valueOf(file);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        imgPreview.setImageBitmap(bitmap);

        Log.e("Image_Capture", Uri.fromFile(file).toString());
        Log.e("Image_Capture", "IAMGE     " + bitmap);
        if (allowance.equals("One")) {

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("SharedImage", Uri.fromFile(file).toString());
            editor.putString("Sharedallowance", "One");
            editor.putString("SharedMode", mode);
            editor.putString("StartedKM", StartedKM);
            editor.putString("SharedFromKm", FromKm);
            editor.putString("SharedToKm", ToKm);
            editor.putString("SharedFare", Fare);
            editor.commit();

            Log.e("SHARE_MODE", mode);
            startActivity(new Intent(AllowancCapture.this, AllowanceActivity.class));
        } else if (allowance.equals("Two")) {

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("SharedImages", Uri.fromFile(file).toString());
            editor.commit();
            startActivity(new Intent(AllowancCapture.this, AllowanceActivityTwo.class));

        }


    }


    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }

    private void StartSelfiCamera() {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        preview = (SurfaceView) findViewById(R.id.PREVIEW);
        mHolder = preview.getHolder();
        mHolder.addCallback(AllowancCapture.this);
        setDefaultCameraId("back");
        mCamera = Camera.open(mCamId);

        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCameraDisplayOrientation();
        mCamera.startPreview();


        Log.e("mCAmer_id", String.valueOf(mCamId));
    }


    private void setDefaultCameraId(String cam) {
        noOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < noOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamId = i;
            }
        }
    }

    public void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCamId, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        if (surfaceHolder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        if (mCamera != null) {
            // Stop if preview surface is already running.
            mCamera.stopPreview();
            try {
                // Set preview display
                mCamera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            mCamera.startPreview();
        }

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onBackPressed() {

    }
}