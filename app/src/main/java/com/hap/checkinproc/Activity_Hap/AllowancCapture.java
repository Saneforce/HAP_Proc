package com.hap.checkinproc.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Activity.AllowanceActivity;
import com.hap.checkinproc.Activity.AllowanceActivityTwo;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AllowancCapture extends AppCompatActivity implements SurfaceHolder.Callback {
    Button button;
    String imagePath;
    String imageFileName;
    Camera mCamera;
    int mCamId = 1;
    private File file;
    SurfaceView preview;
    SurfaceHolder mHolder;
    private int noOfCameras;
    Button btnRtPrv, btnOkPrv;
    String mode = "", allowance = "", StartedKM = "", FromKm = "", ToKm = "", Fare = "", Closing = "";
    Shared_Common_Pref mShared_common_pref;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowanc_capture);
        startService(new Intent(this, TimerService.class));
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mShared_common_pref = new Shared_Common_Pref(this);

        button = (Button) findViewById(R.id.button_capture);
        allowance = String.valueOf(getIntent().getSerializableExtra("allowance"));
        Log.e("allowance", allowance);
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

        CameraPermission cameraPermission = new CameraPermission(AllowancCapture.this, getApplicationContext());

        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cameraPermission.requestPermission();
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
            StartSelfiCamera();
        }
    }


    public void takePicture() {
        long tsLong = System.currentTimeMillis() / 1000;
        imageFileName = Long.toString(tsLong) + ".jpg";
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

        button.setVisibility(View.GONE);

        imgPreview.setImageURI(Uri.fromFile(file));
        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));

        vwPreview.setBackground(drawableBitmap);

        if (mCamId == 1) {
            imgPreview.setRotation((float) -90.0);
        } else if (mCamId == 2) {
            imgPreview.setRotation((float) 90.0);
        } /*else {
            imgPreview.setRotation((float) 270.0);
        }
*/

    }

    private void CloseImgPreview() {
        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        vwPreview.setVisibility(View.GONE);
        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));
        button.setVisibility(View.VISIBLE);
        vwPreview.setBackground(drawableBitmap);

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
        setDefaultCameraId();
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
        imgPreview.setImageURI(Uri.fromFile(file));
        button.setVisibility(View.GONE);
        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));
        vwPreview.setBackground(drawableBitmap);

        if (allowance.equals("One")) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("SharedImage", Uri.fromFile(file).toString());
            editor.commit();

            startActivity(new Intent(AllowancCapture.this, AllowanceActivity.class));



        } else if (allowance.equals("three")) {

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
          //  startActivity(new Intent(AllowancCapture.this, On_Duty_Activity.class));

            Intent newIntent = new Intent(AllowancCapture.this, On_Duty_Activity.class);
            newIntent.putExtra("CHECKING",Uri.fromFile(file).toString());
            startActivity(newIntent);


        } else if (allowance.equals("Two")) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("SharedImages", Uri.fromFile(file).toString());
            editor.commit();
            startActivity(new Intent(AllowancCapture.this, AllowanceActivityTwo.class));
        }else if(allowance.equalsIgnoreCase("Missed")){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("SharedImages", Uri.fromFile(file).toString());
            editor.commit();
            startActivity(new Intent(AllowancCapture.this, Missed_Punch.class));
        }

    }


    private void save(byte[] bytes) throws IOException {
        FileOutputStream outputStream = null;
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
        mHolder.addCallback(this);
        setDefaultCameraId();
        mCamera = Camera.open(mCamId);

        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            e.printStackTrace();
        }
        setCameraDisplayOrientation();
        mCamera.startPreview();

        Log.e("mCAmer_id", String.valueOf(mCamId));
    }


    private void setDefaultCameraId() {
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
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onPause() {
        super.onPause();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(new Intent(this, TimerService.class));
    }
}