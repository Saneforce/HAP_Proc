package com.hap.checkinproc.Activity_Hap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Example;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.hardware.camera2.CameraCaptureSession.StateCallback;

//import com.example.sanloginui.adapters.RecyclerViewAdapter;

public class Check_in extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private static final String TAG = "Check_in";

    //listView
    private EditText acetStatus;
    private ListPopupWindow statusPopupList;


    //    camera
    Button button;
    TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);

    }


    private String cameraId;
    CameraDevice cameraDevice;
    CameraCaptureSession cameraCaptureSession;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimensions;
    private ImageReader imageReader;
    private File file;
    Handler mBackgroundHandler;
    HandlerThread mBackgroundThread;


    List<Example> exampleList;
    //variable
    private ArrayList<String> mNames = new ArrayList<>();
    String Scode;
    String Dcode;

    String lat;
    String log;
    String ID;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Log.d(TAG, "onCreate: started.");
        //in itShift_Time();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        textureView = (TextureView) findViewById(R.id.texture);
        button = (Button) findViewById(R.id.button_capture);
        textureView.setSurfaceTextureListener(textureListener);

        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Scode = (shared.getString("Sfcode", "null"));
        Dcode = (shared.getString("Divcode", "null"));

        spinnerValue("get/Shift_timing", Dcode, Scode);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                send();
                try {
                    takePicture();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(Check_in.this, Dashboard_Two.class);

                startActivity(i);

            }

        });


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getResources().getColor(R.color.color_white));

        acetStatus = findViewById(R.id.acet_status);
        //setPopupList();
        //we need to show the list when clicking on the field

        setListeners();
    }

    private void setListeners() {
        acetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                statusPopupList.show();
            }
        });
    }

    private void spinnerValue(String a, String dc, String sc) {


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        //Call<List<Example>> shiftCall = apiInterface.shiftTime(a,dc,sc);
       /*  shiftCall.enqueue(new Callback<List<Example>>() {
          @Override
          public void onResponse(Call<List<Example>> call, Response<List<Example>> response) {

              exampleList =response.body();
              Log.e("aasds", String.valueOf(exampleList.size()));

              setPopupList();
          }

          @Override
          public void onFailure(Call<List<Example>> call, Throwable t) {
              Log.e("Check","check Null");
          }
      });*/

    }


    private void setPopupList() {

        final List<String> status = new ArrayList<>();

        for (int i = 0; i < exampleList.size(); i++) {

            status.add(exampleList.get(i).getName());
        }

        statusPopupList = new ListPopupWindow(Check_in.this);
        ArrayAdapter adapter = new ArrayAdapter<>(Check_in.this, R.layout.layout_checkin, R.id.tv_element, status);
        statusPopupList.setAnchorView(acetStatus); //this let as set the popup below the EditText
        statusPopupList.setAdapter(adapter);
        statusPopupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acetStatus.setText(status.get(position));//we set the selected element in the EditText
                statusPopupList.dismiss();
                ID = exampleList.get(position).getId();

            }
        });
    }


    public void send() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONArray jsonarray = new JSONArray();
        JSONObject paramObject = new JSONObject();
        try {
            JSONObject paramObjectdata = new JSONObject();
            paramObjectdata.put("lat", lat);
            paramObjectdata.put("long", log);
            paramObjectdata.put("eDate", "2020-9-26 12:26:32");
            paramObjectdata.put("slfy", "1601103391878.jpg");
            paramObjectdata.put("WrkType", Common_Class.Work_Type);//static
            paramObjectdata.put("sfCode", Scode);
            paramObjectdata.put("App_version", Common_Class.Version_Name);//static
            paramObjectdata.put("CheckDutyFlag", null);
            paramObjectdata.put("Shift_Selected_Id", ID);

            paramObject.put("TP_Attendance", paramObjectdata);
            jsonarray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("desd" + jsonarray);
        System.out.println("iddddd" + ID);
        Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save", Dcode, Scode, "24", "MGR", jsonarray.toString());
        modelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    System.out.println("THIRUSUCCESS" + response.body().toString());
                } else {


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("ARAVINDHERROR");
            }


        });
    }


    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {

            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int i) {
            cameraDevice.close();
            cameraDevice = null;

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture = textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
        Surface surface = new Surface(texture);
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);

        cameraDevice.createCaptureSession(Arrays.asList(surface), new StateCallback() {
            @Override
            public void onConfigured(@NotNull CameraCaptureSession session) {
                if (cameraDevice == null) {
                    return;
                }

                cameraCaptureSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onConfigureFailed(@NotNull CameraCaptureSession session) {
                Toast.makeText(getApplicationContext(), "configuration changed", Toast.LENGTH_LONG).show();

            }
        }, null);


    }

    @SuppressLint("NewApi")
    private void updatePreview() throws CameraAccessException {

        if (cameraDevice == null) {
            return;
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() throws CameraAccessException {

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        cameraId = manager.getCameraIdList()[1];

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        assert map != null;
        imageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Check_in.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            return;
        }

        manager.openCamera(cameraId, stateCallback, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void takePicture() throws CameraAccessException {

        if (cameraDevice == null) {
            return;
        }

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
        Size[] jpegSizes = null;

        jpegSizes = Objects.requireNonNull(characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(ImageFormat.JPEG);

        int width = 640;
        int height = 480;

        if (jpegSizes != null && jpegSizes.length > 0) {
            width = jpegSizes[0].getWidth();
            height = jpegSizes[0].getHeight();
        }

        ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
        List<Surface> outputSurfaces = new ArrayList<>(2);
        outputSurfaces.add(reader.getSurface());

        outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

        final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));


        long tsLong = System.currentTimeMillis() / 1000;
        String ts = Long.toString(tsLong);

        file = new File(Environment.getExternalStorageDirectory() + "/" + ts + ".jpg");

        ImageReader.OnImageAvailableListener readerListener = imageReader -> {

            Image image = reader.acquireLatestImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            try {
                save(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                image.close();
            }

        };

        reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

        final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {

            @Override
            public void onCaptureCompleted(@NotNull CameraCaptureSession session, @NotNull CaptureRequest request, @NotNull TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                Toast.makeText(getApplicationContext(), "SAVED", Toast.LENGTH_LONG).show();
                // createCameraPreview();

            }


        };

        cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {


            @Override
            public void onConfigured(CameraCaptureSession session) {
                try {
                    session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {

            }
        }, mBackgroundHandler);

    }


    private void save(byte[] bytes) throws IOException {

        OutputStream outputStream = null;
        outputStream = new FileOutputStream(file);

        outputStream.write(bytes);

        outputStream.close();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (textureView.isAvailable()) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

    }

    @Override
    protected void onPause() {
        try {

            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    private void stopBackgroundThread() throws InterruptedException {

        mBackgroundThread.quitSafely();
        mBackgroundThread.join();
        mBackgroundThread = null;
        mBackgroundHandler = null;

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Check_in.this);
                }
            }
        });

    }

   /* private void initShift_Time(){

        Log.d(TAG, "initShift_Time: preparing shift");
        mNames.add("10.00 am - 12.00 pm");
        mNames.add("01.00 pm - 04.00 pm");
        mNames.add("05.00 pm - 06.00 pm");
        mNames.add("01.00 pm - 04.00 pm");

       // initRecyclerView();
    }
 private void initRecyclerView(){
     Log.d(TAG, "initRecyclerView: init layout_product");
     RecyclerView recyclerView = findViewById(R.id.recycler_view);
     RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, this);
     recyclerView.setAdapter(adapter);
     recyclerView.setLayoutManager(new LinearLayoutManager(this));

 }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        lat = String.valueOf(currentLocation.getLatitude());
        log = String.valueOf(currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("I am here. ");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        googleMap.addMarker(markerOptions);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }
}
