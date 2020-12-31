package com.hap.checkinproc.Activity_Hap;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.hap.checkinproc.BuildConfig;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageCapture extends AppCompatActivity implements SurfaceHolder.Callback {
    Button button;
    TextureView textureView;
    ImageView btnFlash;
    ImageView btnSwchCam;
    ListView lstFlashMode;
    LinearLayout lstModalFlash;
    SeekBar skBarBright;

    String imagePath;
    String imageFileName;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;

    Camera mCamera;
    int mCamId = 0;
    String[] flashModes = {"OFF", "Auto", "ON", "Torch"};
    private File file;

    SurfaceView preview;
    SurfaceHolder mHolder;
    private int noOfCameras;

    JSONObject CheckInInf;

    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    Common_Class DT = new Common_Class();

    String mMode, WrkType, onDutyPlcID, onDutyPlcNm, vstPurpose;
    com.hap.checkinproc.Common_Class.Common_Class common_class;

    public static final String sCheckInDetail = "CheckInDetail";
    public static final String sUserDetail = "MyPrefs";

    Button btnRtPrv, btnOkPrv;


    //Variable to store brightness value
    private int brightness;
    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window
    private Window window;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);

        CheckInInf = new JSONObject();
        CheckInDetails = getSharedPreferences(sCheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(sUserDetail, Context.MODE_PRIVATE);
        common_class = new com.hap.checkinproc.Common_Class.Common_Class(this);

        Bundle params = getIntent().getExtras();
        try {
            mMode = params.getString("Mode");


            CheckInInf.put("Mode", mMode);
            CheckInInf.put("Divcode", UserDetails.getString("Divcode", ""));
            CheckInInf.put("sfCode", UserDetails.getString("Sfcode", ""));
            WrkType = "0";
            if (mMode.equals("onduty")) {
                WrkType = "1";
            }
            Log.e("Checkin_Mode", mMode);
            String SftId = params.getString("ShiftId");
            if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty")) {
                if (!(SftId.isEmpty() || SftId.equalsIgnoreCase(""))) {
                    CheckInInf.put("Shift_Selected_Id", SftId);
                    CheckInInf.put("Shift_Name", params.getString("ShiftName"));
                    CheckInInf.put("ShiftStart", params.getString("ShiftStart"));
                    CheckInInf.put("ShiftEnd", params.getString("ShiftEnd"));
                    CheckInInf.put("ShiftCutOff", params.getString("ShiftCutOff"));
                    CheckInInf.put("App_Version", Common_Class.Version_Name);
                    CheckInInf.put("WrkType", WrkType);
                    CheckInInf.put("CheckDutyFlag", "0");
                    CheckInInf.put("CheckDutyFlag", "0");
                    CheckInInf.put("PlcID", onDutyPlcID);
                    CheckInInf.put("PlcNm", onDutyPlcNm);
                    CheckInInf.put("vstRmks", vstPurpose);
                }
            }

            if (mMode.equalsIgnoreCase("extended")) {
                if (!(SftId.isEmpty() || SftId.equalsIgnoreCase(""))) {
                    DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Calendar calobjw = Calendar.getInstance();
                    CheckInInf.put("Shift_Selected_Id", SftId);
                    CheckInInf.put("Shift_Name", params.getString("ShiftName"));
                    CheckInInf.put("ShiftStart", params.getString("ShiftStart"));
                    CheckInInf.put("ShiftEnd", params.getString("ShiftEnd"));
                    CheckInInf.put("ShiftCutOff", params.getString("ShiftCutOff"));
                    CheckInInf.put("App_Version", Common_Class.Version_Name);
                    CheckInInf.put("Ekey", "EK" + UserDetails.getString("Sfcode", "") + dfw.format(calobjw.getTime()).hashCode());
                    CheckInInf.put("update", "0");
                    CheckInInf.put("WrkType", "0");
                    CheckInInf.put("CheckDutyFlag", "0");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!checkPermission()) {
            requestPermissions();

        } else {
            StartSelfiCamera();
        }


        textureView = (TextureView) findViewById(R.id.ImagePreview);
        button = (Button) findViewById(R.id.button_capture);

        btnRtPrv = (Button) findViewById(R.id.btnRtPrv);
        btnOkPrv = (Button) findViewById(R.id.btnOkPrv);
        btnFlash = (ImageView) findViewById(R.id.button_flash);
        btnSwchCam = (ImageView) findViewById(R.id.button_switchCam);
        lstModalFlash = (LinearLayout) findViewById(R.id.lstMFlash);
        lstFlashMode = (ListView) findViewById(R.id.lstFlashMode);

        preview = (SurfaceView) findViewById(R.id.PREVIEW);
        ArrayAdapter simpleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, flashModes);
        lstFlashMode.setAdapter(simpleAdapter);//sets the adapter for listView

        //perform listView item click event

        if (mCamId == 1) {
            lstFlashMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    lstModalFlash.setVisibility(View.GONE);
                    try {
                        Camera.Parameters params = mCamera.getParameters();
                        //params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                        params.set("flash-mode", flashModes[i].toLowerCase());
                        mCamera.setParameters(params);


                        Log.e("POSITION", String.valueOf(i));
                        Log.e("POSITION", String.valueOf(l));
                        if (i == 0) {
                            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                            try {
                                String cameraId = cameraManager.getCameraIdList()[0];
                                cameraManager.setTorchMode(cameraId, false);
                                Log.e("ON_ITEM_CLICK", "False");
                            } catch (CameraAccessException e) {
                            }
                        } else if (i == 1) {

                        } else if (i == 2) {
                            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                            try {
                                String cameraId = cameraManager.getCameraIdList()[0];
                                cameraManager.setTorchMode(cameraId, true);
                                Log.e("ON_ITEM_CLICK", "TRUE");
                            } catch (CameraAccessException e) {
                            }
                        } else if (i == 4) {

                        }

                    } catch (Exception e) {
                    }
                }
            });

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePicture();

            }
        });
        btnSwchCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSelfiCamera();
            }
        });
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lstModalFlash.setVisibility(View.VISIBLE);
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
        skBarBright = (SeekBar) findViewById(R.id.skBarBright);

        cResolver = getContentResolver();

        //Get the current window
        window = getWindow();

        //Set the seekbar range between 0 and 255
        //seek bar settings//
        //sets the range between 0 and 255
        skBarBright.setMax(255);
        //set the seek bar progress to 1
        skBarBright.setKeyProgressIncrement(1);

        try {
            //Get the current system brightness
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            //Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        //Set the progress of the seek bar based on the system's brightness
        skBarBright.setProgress(brightness);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean settingsCanWrite = Settings.System.canWrite(getApplicationContext());

            if (!settingsCanWrite) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                startActivity(intent);
            } else {
                skBarBright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        if (progress <= 20) {
                            //Set the brightness to 20
                            brightness = 20;
                        } else //brightness is greater than 20
                        {
                            //Set brightness variable based on the progress bar
                            brightness = progress;
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        //Set the system brightness using the brightness variable value
                        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                        //Get the current window attributes
                        ViewGroup.LayoutParams layoutpars = window.getAttributes();
                        //Set the brightness of this window
                        ((WindowManager.LayoutParams) layoutpars).screenBrightness = brightness / (float) 255;
                        //Apply attribute changes to this window
                        window.setAttributes((WindowManager.LayoutParams) layoutpars);
                    }
                });
            }
        }
    }

    private void StartSelfiCamera() {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        preview = (SurfaceView) findViewById(R.id.PREVIEW);
        mHolder = preview.getHolder();
        mHolder.addCallback(ImageCapture.this);
        setDefaultCameraId((mCamId == 0) ? "front" : "back");
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

    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    //Location service part
    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA);
        if (shouldProvideRationale) {
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(ImageCapture.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else
            ActivityCompat.requestPermissions(ImageCapture.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    // Permission was not granted.
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    //mLUService.requestLocationUpdates();
                    StartSelfiCamera();
                } else {
                    // Permission denied.
                    Snackbar.make(
                            findViewById(R.id.activity_main),
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
        }
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

        if (!checkPermission()) {
            requestPermissions();

        } else {

            if (preview != null) {


                preview = null;
                mHolder.removeCallback(ImageCapture.this);
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            preview = (SurfaceView) findViewById(R.id.PREVIEW);
            mHolder = preview.getHolder();
            mHolder.addCallback(ImageCapture.this);
            setDefaultCameraId((mCamId == 1) ? "front" : "back");
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

        saveCheckIn();
    }

    private void saveCheckIn() {
        try {
            // LocationFinder locationFinder=new LocationFinder(this);

            Location location = Common_Class.location;//locationFinder.getLocation();
            String CTime = DT.GetDateTime(getApplicationContext(), "HH:mm:ss");
            String CDate = DT.GetDateTime(getApplicationContext(), "yyyy-MM-dd");

            CheckInInf.put("eDate", CDate + " " + CTime);
            CheckInInf.put("eTime", CTime);
            double lat = 0, lng = 0;
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
            CheckInInf.put("lat", lat);
            CheckInInf.put("long", lng);
            CheckInInf.put("Lattitude", lat);
            CheckInInf.put("Langitude", lng);

            CheckInInf.put("iimgSrc", imagePath);
            CheckInInf.put("slfy", imageFileName);
            CheckInInf.put("Rmks", "");

            Log.e("Image_Capture", imagePath);
            Log.e("Image_Capture", imageFileName);


            if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty")) {
                SharedPreferences.Editor editor = CheckInDetails.edit();
                editor.putString("Shift_Selected_Id", CheckInInf.getString("Shift_Selected_Id"));
                editor.putString("Shift_Name", CheckInInf.getString("Shift_Name"));
                editor.putString("ShiftStart", CheckInInf.getString("ShiftStart"));
                editor.putString("ShiftEnd", CheckInInf.getString("ShiftEnd"));
                editor.putString("ShiftCutOff", CheckInInf.getString("ShiftCutOff"));
                if (CheckInDetails.getString("FTime", "").equalsIgnoreCase(""))
                    editor.putString("FTime", CTime);
                editor.putString("Logintime", CTime);
                editor.putBoolean("CheckIn", true);
                editor.apply();

                JSONArray jsonarray = new JSONArray();
                JSONObject paramObject = new JSONObject();
                paramObject.put("TP_Attendance", CheckInInf);
                Log.e("CHECK_IN_DETAILS", String.valueOf(paramObject));
                jsonarray.put(paramObject);


                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save",
                        UserDetails.getString("Divcode", ""),
                        UserDetails.getString("Sfcode", ""), "", "", jsonarray.toString());
                modelCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if (response.isSuccessful()) {
                            JsonObject itm = response.body().getAsJsonObject();
                            String mMessage = "Your Check-In Submitted Successfully";
                            try {
                                mMessage = itm.get("Msg").getAsString();
                            } catch (Exception e) {
                            }

                            AlertDialogBox.showDialog(ImageCapture.this, "HAP Check-In", String.valueOf(Html.fromHtml(mMessage)), "Ok", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    Intent Dashboard = new Intent(ImageCapture.this, Dashboard_Two.class);
                                    Dashboard.putExtra("Mode", "CIN");
                                    ImageCapture.this.startActivity(Dashboard);

                                    ((AppCompatActivity) ImageCapture.this).finish();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("HAP_receive", "");
                    }
                });
            } else if (mMode.equalsIgnoreCase("extended")) {
                JSONArray jsonarray = new JSONArray();
                JSONObject paramObject = new JSONObject();
                paramObject.put("extended_entry", CheckInInf);
                jsonarray.put(paramObject);
                Log.e("CHECK_IN_DETAILS", String.valueOf(jsonarray));

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save",
                        UserDetails.getString("Divcode", ""),
                        UserDetails.getString("Sfcode", ""), "", "", jsonarray.toString());
                modelCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e("RESPONSE_FROM_SERVER", String.valueOf(response.body().getAsJsonObject()));
                        if (response.isSuccessful()) {
                            JsonObject itm = response.body().getAsJsonObject();
                            String mMessage = "Your Extended Submitted Successfully";
                            try {
                                mMessage = itm.get("Msg").getAsString();


                            } catch (Exception e) {
                            }

                            AlertDialog alertDialog = new AlertDialog.Builder(ImageCapture.this)
                                    .setTitle("HAP Check-In")
                                    .setMessage(Html.fromHtml(mMessage))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent Dashboard = new Intent(ImageCapture.this, Dashboard_Two.class);
                                            Dashboard.putExtra("Mode", "extended");
                                            ImageCapture.this.startActivity(Dashboard);
                                            ((AppCompatActivity) ImageCapture.this).finish();
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("HAP_receive", "");
                    }
                });
            } else {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonObject> modelCall = apiInterface.JsonSave("get/logouttime",
                        UserDetails.getString("Divcode", ""),
                        UserDetails.getString("Sfcode", ""), "", "", CheckInInf.toString());
                modelCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if (response.isSuccessful()) {
                            Log.e("TOTAL_REPOSNEaaa", String.valueOf(response.body()));

                            SharedPreferences.Editor editor = CheckInDetails.edit();
                            editor.putString("Logintime", "");
                            editor.putBoolean("CheckIn", false);
                            editor.apply();

                            JsonObject itm = response.body().getAsJsonObject();
                            String mMessage = "Check in Time  : " + CheckInDetails.getString("FTime", "") + "<br>" +
                                    "Check Out Time : " + CTime;

                            try {
                                mMessage = itm.get("Msg").getAsString();
                            } catch (Exception e) {
                            }


                            AlertDialogBox.showDialog(ImageCapture.this, "HAP Check-In", String.valueOf(Html.fromHtml(mMessage)), "Ok", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    Intent Dashboard = new Intent(ImageCapture.this, Login.class);
                                    startActivity(Dashboard);

                                    ((AppCompatActivity) ImageCapture.this).finish();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {


                                }
                            });


                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("HAP_receive", "");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }

    private void setDefaultCameraId(String cam) {
        noOfCameras = Camera.getNumberOfCameras();
        int facing = cam.equalsIgnoreCase("front") ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < noOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
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
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
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


    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            // TODO Auto-generated method stub
            Log.e("Auto_Focus", "Auto_FOcus");
        }
    };

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


}