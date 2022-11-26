package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.Activity_Hap.ProductImageView;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InshopCheckoutActivity extends AppCompatActivity {

    TextView checkoutTunTime, checkinTime, retailerName,tvDate,tvCheckout;
    final Handler handler = new Handler();
    Button checkout;
    String checkoutTime;
    ImageView attachedImage;
    CardView attach;

    ApiInterface apiInterface;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;

    String SF_code = "", div = "", State_Code = "", date="",UserInfo = "MyPrefs",imageSet = "", imageServer = "",imageConvert = "";

    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_checkout);

        sharedCommonPref = new Shared_Common_Pref(InshopCheckoutActivity.this);

        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

        common_class = new Common_Class(this);

        checkoutTunTime = findViewById(R.id.tvCheckoutRunTime);
        checkinTime = findViewById(R.id.tvCheckinTime);
        retailerName = findViewById(R.id.ischeckoutRetName);
        tvDate = findViewById(R.id.iscoutDate);
        checkout = findViewById(R.id.btnInshopCheckout);
        tvCheckout = findViewById(R.id.tvCheckoutTime);
        attachedImage = findViewById(R.id.cout_attachedImage);
        attach = findViewById(R.id.galleryCard);

        retailerName.setText(InshopCheckinActivity.getName());
        checkinTime.setText(InshopCheckinActivity.getCheckinTime());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.format(today);
        tvDate.setText(date);

        handler.postDelayed(new Runnable() {
            public void run() {
                checkoutTunTime.setText(Common_Class.GetRunTime());
                handler.postDelayed(this, 1000);
            }
        }, 1000);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutTime = checkoutTunTime.getText().toString().trim();
                Log.v("checkoutTime",checkoutTime);

                checkoutData();

//                Toast.makeText(InshopCheckoutActivity.this,"Checkout Successfully",Toast.LENGTH_SHORT).show();
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPermission cameraPermission = new CameraPermission(InshopCheckoutActivity.this, getApplicationContext());

                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                } else {

                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
//                            Photo_Name = FileName;
//                            imageConvert=fullPath;
//                            EndedImage="file://"+fullPath;
//                            EndedKmImage.setImageBitmap(image);

                            UploadPhoto(fullPath, UserDetails.getString("Sfcode", ""), FileName, "Travel", image);

                        }
                    });
                    Intent intent = new Intent(InshopCheckoutActivity.this, AllowancCapture.class);
                    intent.putExtra("allowance", "Two");
                    startActivity(intent);

                }

            }
        });

        attachedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                intent.putExtra("ImageUrl", imageSet);
                startActivity(intent);
            }
        });

    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        } catch (Exception e) {
        }
        return yy;
    }

    private void UploadPhoto(String path, String SF, String FileName, String Mode, Bitmap image) {
        try {
            common_class.ProgressdialogShow(1, "");

            MultipartBody.Part imgg;
            if (path != null && (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg"))) {
                imgg = convertimg("file", path);

            } else {
                common_class.ProgressdialogShow(0, "");
                common_class.showMsg(this, "Image file only supported");
                return;
            }


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseBody> mCall = apiInterface.onTAFileUpload(SF, FileName, Mode, imgg);

            Log.e("SEND_IMAGE_SERVER", mCall.request().toString());

            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {


                            JSONObject js = new JSONObject(response.body().string());
                            Log.v("Res", js.toString());

                            if (js.getBoolean("success")) {

                                if (image != null) {

                                    imageServer=FileName;
                                    imageConvert = path;
                                    imageSet = "file://" + path;
                                    attachedImage.setImageBitmap(image);
                                    attachedImage.setVisibility(View.VISIBLE);

                                }


                                common_class.ProgressdialogShow(0, "");

                                common_class.showMsg(InshopCheckoutActivity.this, "File uploading successful ");
                            } else {
                                common_class.ProgressdialogShow(0, "");
                                common_class.showMsg(InshopCheckoutActivity.this, "Failed.Try Again...");
                            }
                        } else {

                            common_class.ProgressdialogShow(0, "");
                            common_class.showMsg(InshopCheckoutActivity.this, "Failed.Try Again...");

                        }

                    } catch (Exception e) {
                        common_class.ProgressdialogShow(0, "");
                        common_class.showMsg(InshopCheckoutActivity.this, "Failed.Try Again...");

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    common_class.ProgressdialogShow(0, "");
                    common_class.showMsg(InshopCheckoutActivity.this, "Failed.Try Again...");

                    Log.e("SEND_IMAGE_Response", "ERROR");
                }
            });


        } catch (Exception e) {
            Log.e("TAClaim:", e.getMessage());
        }
    }

    private void checkoutData() {

        JSONObject jObj = new JSONObject();
        try {

            jObj.put("SFCode",SF_code);
            jObj.put("inshopCheckinTime",InshopCheckinActivity.getCheckinTime());
            jObj.put("inshopCheckoutTime",checkoutTime);
            jObj.put("inshopCheckoutDate",date);
            jObj.put("DivCode",div);
            jObj.put("Statecode",State_Code);
            jObj.put("inshopRetailerName", retailerName.getText().toString());
            jObj.put("inshopCheckoutImage",imageSet);
            jObj.put("c_flag",0);

            Log.d("isCheckouthjj","ghkj"+jObj.toString());



            apiInterface.JsonSave("save/inshopCheckout", jObj.toString()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    Toast.makeText(InshopCheckoutActivity.this,"Inshop Checkout Successfully",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}