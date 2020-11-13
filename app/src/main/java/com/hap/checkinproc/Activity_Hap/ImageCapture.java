package com.hap.checkinproc.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.SANGPSTracker;

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
    String SftId=params.getString("ShiftId");
    if(!(SftId.isEmpty() || SftId.equalsIgnoreCase(""))) {
        try {
            CheckInInf.put("Mode", params.getString("Mode"));
            CheckInInf.put("Divcode", UserDetails.getString("Divcode",""));
            CheckInInf.put("sfCode",UserDetails.getString("Sfcode",""));
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



        textureView =(TextureView)findViewById(R.id.ImagePreview);
        button =(Button)findViewById(R.id.button_capture);
        btnFlash =(ImageView)findViewById(R.id.button_flash);
        btnSwchCam =(ImageView)findViewById(R.id.button_switchCam);
        lstModalFlash=(LinearLayout) findViewById(R.id.lstMFlash);
        lstFlashMode=(ListView)findViewById(R.id.lstFlashMode);

        preview = (SurfaceView) findViewById(R.id.PREVIEW);
        mHolder = preview.getHolder();
        mHolder.addCallback(this);
        setDefaultCameraId("front");
        mCamera = Camera.open(mCamId);
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

    public void openDateTimeSetting() {
        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
        //this.webView.getContext().startActivity(intent);
    }

    public static boolean isTimeAutomatic(Context c) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }
    public String GetDateTime(String pattern) {
        if(isTimeAutomatic(getApplicationContext())==false){

            //this.webView.sendJavascript("blockApp('date')");
        }
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(c.getTime());

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
            SANGPSTracker sangpsTracker=new SANGPSTracker(getApplicationContext());
            Location location=sangpsTracker.getLocation();
            String CTime=GetDateTime("yyyy-MM-dd HH:mm:ss");

            CheckInInf.put("eDate", CTime);
            CheckInInf.put("eTime", GetDateTime("HH:mm:ss"));
            CheckInInf.put("lat", location.getLatitude());
            CheckInInf.put("long", location.getLongitude());
            CheckInInf.put("iimgSrc", imagePath);
            CheckInInf.put("slfy", imageFileName);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Shift_Selected_Id",CheckInInf.getString("Shift_Selected_Id"));
            editor.putString("Shift_Name",CheckInInf.getString("Shift_Name"));
            editor.putString("ShiftStart",CheckInInf.getString("ShiftStart"));
            editor.putString("ShiftEnd",CheckInInf.getString("ShiftEnd"));
            editor.putString("ShiftCutOff",CheckInInf.getString("ShiftCutOff"));
            editor.putString("Logintime",CTime);
            editor.apply();

            JSONArray jsonarray=new JSONArray();
            JSONObject paramObject = new JSONObject();

            paramObject.put("TP_Attendance",CheckInInf);
            jsonarray.put(paramObject);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save",
                    UserDetails.getString("Divcode",""),
                    UserDetails.getString("Sfcode",""),"","",jsonarray.toString());
            modelCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.isSuccessful()) {
                        Intent  i = new Intent(ImageCapture.this, Dashboard_Two.class);
                        startActivity(i);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
Log.d("HAP_receive","");
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
 /*
        var loc={}
        loc.Lat=_currentLocation.Latitude;
        loc.Long=_currentLocation.Longitude;
        loginData.Logintime=Logintime;
        $scope.End_TimeStore.Sfift_Login_Time=Logintime;
*/

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