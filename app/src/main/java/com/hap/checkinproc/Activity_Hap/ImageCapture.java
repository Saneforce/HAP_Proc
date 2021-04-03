package com.hap.checkinproc.Activity_Hap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.LocationFinder;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    private ProgressDialog mProgress;
    public static RelativeLayout vwPreview;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    static OnImagePickListener imagePickListener;
    Camera mCamera;
    int mCamId = 1;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);
        startService(new Intent(this, TimerService.class));

        CheckInInf = new JSONObject();
        CheckInDetails = getSharedPreferences(sCheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(sUserDetail, Context.MODE_PRIVATE);
        common_class = new com.hap.checkinproc.Common_Class.Common_Class(this);

        Bundle params = getIntent().getExtras();
        try {
            mMode = params.getString("Mode");

            if(!mMode.equalsIgnoreCase("PF")) {
                CheckInInf.put("Mode", mMode);
                CheckInInf.put("Divcode", UserDetails.getString("Divcode", ""));
                CheckInInf.put("sfCode", UserDetails.getString("Sfcode", ""));
                WrkType = "0";
                if (mMode.equals("onduty")) {
                    WrkType = "1";
                }
                Log.e("Checkin_Mode", mMode);
                String SftId = params.getString("ShiftId");
                if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty") || mMode.equalsIgnoreCase("holidayentry")) {
                    if (!(SftId.isEmpty() || SftId.equalsIgnoreCase(""))) {
                        CheckInInf.put("Shift_Selected_Id", SftId);
                        CheckInInf.put("Shift_Name", params.getString("ShiftName"));
                        CheckInInf.put("ShiftStart", params.getString("ShiftStart"));
                        CheckInInf.put("ShiftEnd", params.getString("ShiftEnd"));
                        CheckInInf.put("ShiftCutOff", params.getString("ShiftCutOff"));
                    }
                    CheckInInf.put("App_Version", Common_Class.Version_Name);
                    CheckInInf.put("WrkType", WrkType);
                    CheckInInf.put("CheckDutyFlag", "0");
                    CheckInInf.put("On_Duty_Flag", WrkType);
                    CheckInInf.put("PlcID", onDutyPlcID);
                    CheckInInf.put("PlcNm", onDutyPlcNm);
                    CheckInInf.put("vstRmks", vstPurpose);
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CameraPermission cameraPermission = new CameraPermission(ImageCapture.this, getApplicationContext());

        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cameraPermission.requestPermission();
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
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


        ArrayAdapter simpleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, flashModes);
        lstFlashMode.setAdapter(simpleAdapter);//sets the adapter for listView

        //perform listView item click event

        if (mCamId == 0) {
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
                CameraPermission cameraPermission = new CameraPermission(ImageCapture.this, getApplicationContext());

                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                    Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                } else {
                    Log.v("PERMISSION", "PERMISSION");

                    mCamId=(mCamId == 1) ? 0 : 1;
                    StartSelfiCamera();

                }


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
        setDefaultCameraId((mCamId == 1) ? "front" : "back");
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
    public static void setOnImagePickListener(OnImagePickListener mImagePickListener){
        imagePickListener=mImagePickListener;
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
                            Bitmap bm=null;
                            try {
                                if (bytes != null) {
                                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                                    int screenHeight = getResources().getDisplayMetrics().heightPixels;
                                    bm = BitmapFactory.decodeByteArray(bytes, 0, (bytes != null) ? bytes.length : 0);

                                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                        // Notice that width and height are reversed
                                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
                                        int w = scaled.getWidth();
                                        int h = scaled.getHeight();
                                        w=bm.getWidth();
                                        h=bm.getHeight();
                                        // Setting post rotate to 90
                                        Matrix mtx = new Matrix();

                                        int CameraEyeValue = setPhotoOrientation(ImageCapture.this, mCamId); // CameraID = 1 : front 0:back
                                        if(mCamId==1) { // As Front camera is Mirrored so Fliping the Orientation
                                            if (CameraEyeValue == 270) {
                                                mtx.postRotate(90);
                                            } else if (CameraEyeValue == 90) {
                                                mtx.postRotate(270);
                                            }
                                        }else{
                                            mtx.postRotate(CameraEyeValue); // CameraEyeValue is default to Display Rotation
                                        }
                                        bm=applyMatrix(bm,mtx);
                                       // bm = Bitmap.createBitmap(bm, 0, 0, w, h, mtx, true);
                                    }else{// LANDSCAPE MODE
                                        //No need to reverse width and height
                                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                                        bm=scaled;
                                    }
                                }

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] byteArray = stream.toByteArray();

                                save(byteArray);
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
    public int setPhotoOrientation(Activity activity, int cameraId) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }
    public static Bitmap applyMatrix(Bitmap source, Matrix matrix) {
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
    private void ShowImgPreview() {
        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        vwPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageURI(Uri.fromFile(file));
        button.setVisibility(View.GONE);
        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));

        vwPreview.setBackground(drawableBitmap);

        Log.v("CAMERA_FOCUS_Preview", String.valueOf(mCamId));

        if (mCamId == 1) {
            imgPreview.setRotation((float) -90.0);
        } else if (mCamId == 2) {
            imgPreview.setRotation((float) 90.0);
        } else {
            imgPreview.setRotation((float) 270.0);
        }

    }

    private void CloseImgPreview() {
        vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        vwPreview.setVisibility(View.GONE);
        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));
        button.setVisibility(View.VISIBLE);
        vwPreview.setBackground(drawableBitmap);
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

        Log.v("mCamId_VALUE", String.valueOf(mCamId));
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


    private void saveImgPreview() {
        vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));

        vwPreview.setBackground(drawableBitmap);
        String filePath = String.valueOf(file);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        imgPreview.setImageBitmap(bitmap);


        Log.e("Image_Capture", Uri.fromFile(file).toString());
        Log.e("Image_Capture", "IAMGE     " + bitmap);
        if(mMode.equalsIgnoreCase("PF")){
            imagePickListener.OnImagePick(bitmap);
            finish();
        }else{
            mProgress = new ProgressDialog(this);
            String titleId = "Submiting";
            mProgress.setTitle(titleId);
            mProgress.setMessage("Preparing Please Wait...");
            mProgress.show();
            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    Common_Class.location = location;

                    mProgress.setMessage("Submiting Please Wait...");
                    vwPreview.setVisibility(View.GONE);
                    // imgPreview.setImageURI(Uri.fromFile(file));
                    button.setVisibility(View.GONE);
                    saveCheckIn();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    try {
                        Common_Class.location = location;

                        ImageCapture.vwPreview.setVisibility(View.GONE);
                        // imgPreview.setImageURI(Uri.fromFile(file));
                        button.setVisibility(View.GONE);
                        saveCheckIn();
                    }
                    catch (Exception e){}
                }
            });
        }
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


            if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty") || mMode.equalsIgnoreCase("holidayentry")) {

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

                        if (response.isSuccessful() ) {

                            JsonObject itm = response.body().getAsJsonObject();

                            mProgress.dismiss();
                            if(itm.get("success").getAsString().equalsIgnoreCase("true")) {
                                SharedPreferences.Editor editor = CheckInDetails.edit();
                                try {
                                    if (mMode.equalsIgnoreCase("CIN")) {
                                        editor.putString("Shift_Selected_Id", CheckInInf.getString("Shift_Selected_Id"));
                                        editor.putString("Shift_Name", CheckInInf.getString("Shift_Name"));
                                        editor.putString("ShiftStart", CheckInInf.getString("ShiftStart"));
                                        editor.putString("ShiftEnd", CheckInInf.getString("ShiftEnd"));
                                        editor.putString("ShiftCutOff", CheckInInf.getString("ShiftCutOff"));
                                    }
                                    if (CheckInDetails.getString("FTime", "").equalsIgnoreCase(""))
                                        editor.putString("FTime", CTime);
                                    editor.putString("Logintime", CTime);

                                    if (mMode.equalsIgnoreCase("onduty"))
                                        editor.putString("On_Duty_Flag", "1");
                                    else
                                        editor.putString("On_Duty_Flag", "0");
                                    editor.putBoolean("CheckIn", true);
                                    editor.apply();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            String mMessage = "Your Check-In Submitted Successfully";
                            try {
                                mMessage = itm.get("Msg").getAsString();
                            } catch (Exception e) {
                            }

                            AlertDialogBox.showDialog(ImageCapture.this, "HAP Check-In", String.valueOf(Html.fromHtml(mMessage)), "Yes", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {

                                    if(itm.get("success").getAsString().equalsIgnoreCase("true")) {
                                        Intent Dashboard = new Intent(ImageCapture.this, Dashboard_Two.class);
                                        Dashboard.putExtra("Mode", "CIN");
                                        ImageCapture.this.startActivity(Dashboard);
                                    }

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
                        mProgress.dismiss();
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
                        mProgress.dismiss();
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
                        mProgress.dismiss();
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

                        mProgress.dismiss();
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
                        mProgress.dismiss();
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
        Log.v("CAMERA_FOCUS", String.valueOf(facing));
        mCamId = facing;

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < noOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
                //mCamId = i;
            }
        }
    }

    public void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCamId, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setRotation(-rotation);
        mCamera.setParameters(parameters);
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
        try {
            // open the camera
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mCamera.setDisplayOrientation(90);
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = mCamera.getParameters();

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        param.setRotation(-rotation);
        // modify parameter
        /*        param.setPreviewSize(352, 288);*/
        mCamera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
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