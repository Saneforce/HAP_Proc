package com.hap.checkinproc.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.hap.checkinproc.Activity.Util.ImageFilePath;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaExceptionEntry extends AppCompatActivity implements View.OnClickListener, Master_Interface {

    EditText cardDate, edtActual, edtEarly, edtAmt;
    DatePickerDialog picker;
    String minDate, minYear, minMonth, minDay, fullPath = "", finalPath = "", filePath = "";
    ArrayList<String> travelTypeList;
    Common_Model mCommon_model_spinner;
    List<Common_Model> listOrderType = new ArrayList<>();
    CustomListViewDialog customDialog;
    CardView expType;
    TextView typeText, TxtActl, TxtErly, txtTotalAmt;
    LinearLayout dataVisiblity;
    ImageView imgAttach;
    Dialog dialog;
    Uri outputFileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_exception_entry);

        getTool();
        MaxMinDate();
        cardDate = findViewById(R.id.choose_date);
        expType = findViewById(R.id.exp_card_type);
        TxtActl = findViewById(R.id.actual_timer);
        TxtErly = findViewById(R.id.early_timer);
        typeText = findViewById(R.id.txt_crd_tpe);
        edtActual = findViewById(R.id.choose_time);
        edtEarly = findViewById(R.id.early_time);
        edtAmt = findViewById(R.id.edt_amt);
        txtTotalAmt = findViewById(R.id.total_amount);
        imgAttach = findViewById(R.id.da_exp_att);
        dataVisiblity = findViewById(R.id.linear_view);
        expType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderType.clear();
                OrderType();
            }
        });


        cardDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog.

                picker = new DatePickerDialog(DaExceptionEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cardDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                calendarmin.set(Integer.parseInt(minYear), Integer.parseInt(minMonth) - 1, Integer.parseInt(minDay));
                picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                picker.show();

            }

        });

        edtActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DaExceptionEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edtActual.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        edtEarly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DaExceptionEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edtEarly.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cameraPermission = new CameraPermission(DaExceptionEntry.this, getApplicationContext());
                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                    Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                } else {
                    Log.v("PERMISSION", "PERMISSION");
                    popupCapture();

                }
            }
        });


        edtAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtTotalAmt.setText("Rs. " + edtAmt.getText().toString() + ".00");
                Log.v("EDT_AMOUNT", edtAmt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void popupCapture() {
        dialog = new Dialog(DaExceptionEntry.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_capture);
        dialog.show();
        TextView upload = dialog.findViewById(R.id.upload);
        TextView camera = dialog.findViewById(R.id.camera);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMultiImage();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureFile();
            }
        });
    }

    public void selectMultiImage() {
        dialog.dismiss();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);

    }

    public void captureFile() {
        dialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = FileProvider.getUriForFile(DaExceptionEntry.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), Shared_Common_Pref.Sf_Code + "_" + System.currentTimeMillis() + ".jpeg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 1);

    }


    public void MaxMinDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println("Current_DATE_FORMAT" + formatter.format(date));

        String strMinDate = formatter.format(date);
        minDate = strMinDate;
        /*Min Date*/
        String[] separated1 = minDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();

        minYear = separated1[0];
        minMonth = separated1[1];
        minDay = separated1[2];
        Log.e("Sresdfsd", minYear);
        Log.e("Sresdfsd", minMonth);
        Log.e("Sresdfsd", minDay);

    }

    public void OrderType() {
        travelTypeList = new ArrayList<>();
        travelTypeList.add("EARLY CHECK-IN");
        travelTypeList.add("LATE CHECK-OUT");

        for (int i = 0; i < travelTypeList.size(); i++) {
            String id = String.valueOf(travelTypeList.get(i));
            String name = travelTypeList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(DaExceptionEntry.this, listOrderType, 101);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

    }

    public void getTool() {
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });

        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    DaExceptionEntry.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 101) {
            dataVisiblity.setVisibility(View.VISIBLE);
            typeText.setText(myDataset.get(position).getName());
            if (typeText.getText().toString().equals("EARLY CHECK-IN")) {
                TxtActl.setText("Actual Check-in Time");
                TxtErly.setText("Early Check-in Time");
            } else {
                TxtActl.setText("Actual Check-out Time");
                TxtErly.setText("Late Check-out Time");
            }
        }
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            if (resultCode == RESULT_OK) {
                if (requestCode == 2) {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            // display your images
                            ImageFilePath filepath = new ImageFilePath();
                            fullPath = filepath.getPath(DaExceptionEntry.this, mClipData.getItemAt(i).getUri());

                            Log.v("KARTHIC_DA_IMAGE_1", fullPath);
                            getMulipart(fullPath);
                        }
                    } else if (data.getData() != null) {
                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(DaExceptionEntry.this, item);
                        Log.v("KARTHIC_DA_IMAGE_2", fullPath);
                        getMulipart(fullPath);
                    }
                }
            }
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));
            Log.v("KARTHIC_DA_IMAGE_3", filePath);

            getMulipart(filePath);
        }
    }


    public void getMulipart(String path) {
        Log.v("PATH_IMAGE", path);
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field(Shared_Common_Pref.Sf_Code);
        Log.v("PATH_IMAGE_imgg", String.valueOf(imgg));
        sendImageToServer(values, imgg);
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));

        return xx;

    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }


    private void sendImageToServer(HashMap<String, RequestBody> values, MultipartBody.Part imgg) {
        Call<ResponseBody> Callto;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Callto = apiService.uploadProcPic(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());

                try {
                    if (response.isSuccessful()) {

                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            Log.v("printing_dynamic_cou", js.getString("url"));

                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
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

    public void DaException(View v) {
        daExpen();
    }


    public void daExpen() {

        JSONObject json = new JSONObject();
        try {
            json.put("da_type", typeText.getText().toString());
            json.put("da_date", cardDate.getText().toString());
            json.put("da_actua_time", edtActual.getText().toString());
            json.put("da_early_time", edtEarly.getText().toString());
            json.put("da_amt", edtAmt.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("Resquest_DAException", json.toString());


        Call<ResponseBody> Callto;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Callto = apiService.daExpen(Shared_Common_Pref.Sf_Code, json.toString());

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());

                try {
                    if (response.isSuccessful()) {

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });

    }


}