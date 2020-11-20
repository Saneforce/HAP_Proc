package com.hap.checkinproc.Activity_Hap;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
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
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.hap.checkinproc.BuildConfig;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageCapture extends AppCompatActivity implements
    SurfaceHolder.Callback{
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
    int mCamId=0;
    String[] flashModes={"OFF","Auto","ON","Torch"};//animal names array
    private File file;

    SurfaceView preview;
    SurfaceHolder mHolder;
    private int noOfCameras;

    JSONObject CheckInInf;

    SharedPreferences sharedPreferences;
    SharedPreferences UserDetails;
    Common_Class DT= new Common_Class();

    String mMode;

    public static final String CheckInDetail = "CheckInDetail" ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);

        CheckInInf=new JSONObject();
        sharedPreferences = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Bundle params=getIntent().getExtras();
            try {
                mMode=params.getString("Mode");
                CheckInInf.put("Mode", mMode);
                CheckInInf.put("Divcode", UserDetails.getString("Divcode",""));
                CheckInInf.put("sfCode",UserDetails.getString("Sfcode",""));

                String SftId=params.getString("ShiftId");
                if (mMode.equalsIgnoreCase("CIN")) {
                    if(!(SftId.isEmpty() || SftId.equalsIgnoreCase(""))) {
                        CheckInInf.put("Shift_Selected_Id", SftId);
                        CheckInInf.put("Shift_Name", params.getString("ShiftName"));
                        CheckInInf.put("ShiftStart", params.getString("ShiftStart"));
                        CheckInInf.put("ShiftEnd", params.getString("ShiftEnd"));
                        CheckInInf.put("ShiftCutOff", params.getString("ShiftCutOff"));
                        CheckInInf.put("App_Version", Common_Class.Version_Name);
                        CheckInInf.put("ShiftCutOff", params.getString("ShiftCutOff"));
                        CheckInInf.put("WrkType", "0");
                        CheckInInf.put("CheckDutyFlag", "0");
                        CheckInInf.put("PlcID", "");
                        CheckInInf.put("PlcNm", "");
                        CheckInInf.put("vstRmks", "");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!checkPermission())
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestPermissions();
                }
            }
            else{
                StartSelfiCamera();
            }



        textureView =(TextureView)findViewById(R.id.ImagePreview);
        button =(Button)findViewById(R.id.button_capture);
        btnFlash =(ImageView)findViewById(R.id.button_flash);
        btnSwchCam =(ImageView)findViewById(R.id.button_switchCam);
        lstModalFlash=(LinearLayout) findViewById(R.id.lstMFlash);
        lstFlashMode=(ListView)findViewById(R.id.lstFlashMode);

        preview = (SurfaceView) findViewById(R.id.PREVIEW);
        ArrayAdapter simpleAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,flashModes);
        lstFlashMode.setAdapter(simpleAdapter);//sets the adapter for listView

        //perform listView item click event
        lstFlashMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lstModalFlash.setVisibility(View.GONE);
                try {
                    Camera.Parameters params = mCamera.getParameters();
                    //params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                    params.set("flash-mode", flashModes[i].toLowerCase());
                    mCamera.setParameters(params);
                }
                catch (Exception e){}
            }
        });

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

        skBarBright=(SeekBar)findViewById(R.id.skBarBright);
        skBarBright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void StartSelfiCamera(){
        /*mHolder = preview.getHolder();
        mHolder.addCallback(this);
        setDefaultCameraId("front");
        mCamera = Camera.open(mCamId);*/
        if (mCamera != null) {
            preview=null;
            mHolder.removeCallback(ImageCapture.this);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        preview = (SurfaceView) findViewById(R.id.PREVIEW);
        mHolder = preview.getHolder();
        mHolder.addCallback(ImageCapture.this);
        setDefaultCameraId((mCamId==0)? "front": "back");
        mCamera = Camera.open(mCamId);
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCameraDisplayOrientation();
        mCamera.startPreview();
    }
    private boolean checkPermission()
    {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        //PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    //Location service part
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestPermissions()
    {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA);
        if(shouldProvideRationale)
        {
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(ImageCapture.this,
                                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        }
        else
            ActivityCompat.requestPermissions(ImageCapture.this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
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

    public void takePicture(){
        long tsLong = System.currentTimeMillis()/1000;
        imageFileName = Long.toString(tsLong)+".jpg";

        //file  = new File(Environment.getExternalStorageDirectory() + "/"+ts+".jpg");
        imagePath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"+imageFileName;
        file  = new File(imagePath);
        mCamera.takePicture(null, null,
                new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        try {
                            save(bytes);
                            saveCheckIn();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void saveCheckIn(){
        try {
           // LocationFinder locationFinder=new LocationFinder(this);

            Location location= Common_Class.location;//locationFinder.getLocation();
            String CTime=DT.GetDateTime(getApplicationContext(),"HH:mm:ss");
            String CDate=DT.GetDateTime(getApplicationContext(),"yyyy-MM-dd");

            CheckInInf.put("eDate", CDate+" "+CTime);
            CheckInInf.put("eTime", CTime);
            CheckInInf.put("lat", location.getLatitude());
            CheckInInf.put("long", location.getLongitude());
            CheckInInf.put("iimgSrc", imagePath);
            CheckInInf.put("slfy", imageFileName);
            CheckInInf.put("Rmks", "");
            if(mMode.equalsIgnoreCase("CIN")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Shift_Selected_Id", CheckInInf.getString("Shift_Selected_Id"));
                editor.putString("Shift_Name", CheckInInf.getString("Shift_Name"));
                editor.putString("ShiftStart", CheckInInf.getString("ShiftStart"));
                editor.putString("ShiftEnd", CheckInInf.getString("ShiftEnd"));
                editor.putString("ShiftCutOff", CheckInInf.getString("ShiftCutOff"));
                if(sharedPreferences.getString("FTime","").equalsIgnoreCase(""))
                    editor.putString("FTime", CTime);
                editor.putString("Logintime", CTime);
                editor.putBoolean("CheckIn", true);
                editor.apply();

                JSONArray jsonarray = new JSONArray();
                JSONObject paramObject = new JSONObject();

                paramObject.put("TP_Attendance", CheckInInf);
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

                            AlertDialog alertDialog = new AlertDialog.Builder(ImageCapture.this)
                                    .setTitle("HAP Check-In")
                                    .setMessage(Html.fromHtml(mMessage))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent Dashboard = new Intent(ImageCapture.this, Dashboard_Two.class);
                                            Dashboard.putExtra("Mode", "CIN");
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
            }else {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save",
                        UserDetails.getString("Divcode", ""),
                        UserDetails.getString("Sfcode", ""), "", "", CheckInInf.toString());
                modelCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if (response.isSuccessful()) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Logintime", "");
                            editor.putBoolean("CheckIn", false);
                            editor.apply();

                            JsonObject itm = response.body().getAsJsonObject();
                            String mMessage = "Check in Time  : "+ sharedPreferences.getString("FTime","")+"<br>"+
                                            "Check Out Time : "+ CTime;

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
                                            Intent Dashboard = new Intent(ImageCapture.this, Login.class);
                                            startActivity(Dashboard);

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

    private void setDefaultCameraId(String cam){
        noOfCameras= Camera.getNumberOfCameras();
        int facing=cam.equalsIgnoreCase("front")? Camera.CameraInfo.CAMERA_FACING_FRONT:Camera.CameraInfo.CAMERA_FACING_BACK;
        Camera.CameraInfo cameraInfo=new Camera.CameraInfo();
        for(int i=0;i<noOfCameras;i++){
            Camera.getCameraInfo(i,cameraInfo);
            if(cameraInfo.facing==facing){
                mCamId=i;
            }
        }
    }
    public void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCamId, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
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
        //int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        mCamera.setDisplayOrientation(result);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
            setCameraDisplayOrientation();
        } catch (IOException e) {
            // left blank for now
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
            setCameraDisplayOrientation();
        } catch (Exception e) {
            // intentionally left blank for a test
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.release();
    }
}