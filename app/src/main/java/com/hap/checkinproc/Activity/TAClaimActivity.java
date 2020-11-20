package com.hap.checkinproc.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.hap.checkinproc.Activity.Util.ImageFilePath;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.DailyExpenseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TAClaimActivity extends AppCompatActivity {

    CardView card_date;
    TextView txt_date;
    ApiInterface apiInterface;
    DatePickerDialog fromDatePickerDialog;
    ImageView img_attach;
    ArrayList<SelectionModel> array=new ArrayList<>();
    ListView list;
    ArrayList<String> picPath=new ArrayList<>();
    ArrayList<String> finalpicPath=new ArrayList<>();
    Button btn_sub;
    String todayExp="";
    Uri outputFileUri;
    int pos=-1;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    String SF_code="",div="",State_Code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_claim);
        txt_date=findViewById(R.id.txt_date);
        card_date=findViewById(R.id.card_date);
        img_attach=findViewById(R.id.img_attach);
        btn_sub=findViewById(R.id.btn_sub);
        list=findViewById(R.id.list);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code=UserDetails.getString("Sfcode","");
        div=UserDetails.getString("Divcode","");
        State_Code=UserDetails.getString("State_Code","");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        txt_date.setText(Common_Class.GetDateOnly()+"-"+ Common_Class.GetDay());
        String date= Common_Class.GetDate();
        callApi(date.substring(0,date.indexOf(" ")));

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<picPath.size();i++){
                    getMulipart(picPath.get(i),-1);
                }
                for(int i=0;i<array.size();i++){
                    String[] imp=array.get(i).getImg_url().split(",");
                    for(int j=0;j<imp.length-1;j++){
                        getMulipart(imp[j],i);
                    }
                }
                submitData();
            }
        });
        DailyExpenseAdapter.bindUpdateListener(new UpdateUi() {
            @Override
            public void update(int value, int pos1) {
                pos=pos1;
                popupCapture();
            }
        });
        img_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos=-1;
            popupCapture();
            }
        });

        card_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            //TODO: action
            ClipData mClipData = data.getClipData();
            int pickedImageCount;

            String filepathing="";
            for (pickedImageCount = 0; pickedImageCount < mClipData.getItemCount();
                 pickedImageCount++) {
                Log.v("picked_image_value", mClipData.getItemAt(pickedImageCount).getUri() + "");
                ImageFilePath filepath = new ImageFilePath();
                String fullPath = filepath.getPath(TAClaimActivity.this, mClipData.getItemAt(pickedImageCount).getUri());
                Log.v("picked_fullPath", fullPath + "");
                if(pos==-1)
                picPath.add(fullPath);
                else{
                    SelectionModel m=array.get(pos);
                    filepathing=m.getImg_url();
                    filepathing=filepathing+fullPath+",";
                    m.setImg_url(filepathing);

                }
            }

        }
        else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
           /* ModelDynamicView mm = array.get(pos_upload_file);
            if (!TextUtils.isEmpty(mm.getValue()))
                filePathing = mm.getValue();*/
            String finalPath = "/storage/emulated/0";
            String filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));
            Log.v("printing__file_path", filePath);
            if(pos==-1)
                picPath.add(filePath);
            else{
                SelectionModel m=array.get(pos);
                String filepathing="";
                filepathing=m.getImg_url();
                filepathing=filepathing+filePath+",";
                m.setImg_url(filepathing);
            }
            //filePathing = filePathing + filePath + ",";
        }
    }

    public void submitData(){
        try{
            JSONArray ja=new JSONArray();
            JSONArray ja1=new JSONArray();
            JSONObject jjMain=new JSONObject();
            JSONObject jjMain1=new JSONObject();
            for(int i=0;i<array.size();i++){
                SelectionModel m=array.get(i);
                JSONObject jj=new JSONObject();
                jj.put("ID",m.getCode());
                jj.put("Name",m.getTxt());
                jj.put("amt",m.getValue());
                String[] imgUrl=m.getImg_url().split(",");
                String url_img="";
                for(int j=0;j<imgUrl.length;j++){
                    url_img=url_img+imgUrl[j].substring(imgUrl[j].lastIndexOf("/")+1)+",";
                }
                jj.put("imgData",url_img);
                ja.put(jj);
            }
            jjMain.put("sf",SF_code);
            jjMain.put("div",div);
            jjMain.put("dailyExpense",ja);
            ja=new JSONArray();
            JSONObject jj=new JSONObject();
            JSONObject jjj=new JSONObject(todayExp);
            jj.put("LEOS","");
            jj.put("MOT",jjj.getString("MOT"));
            jj.put("BusFare","");
            jj.put("DateOfExp",txt_date.getText().toString());
            jj.put("Start_image",jjj.getString("Image_Url"));
            jj.put("Start_Km",jjj.getString("Start_Km"));
            jj.put("TodayworkRoute","");
            jj.put("TodayworkName","");
            jj.put("Workplace","");
            jj.put("Workplace_Name","");
            jj.put("Stop_image",jjj.getString("End_Image_Url"));
            jj.put("Stop_km",jjj.getString("End_Km"));
            jj.put("Ukey","MGR4762"+System.currentTimeMillis());
            jj.put("EventcaptureUrl","");
            ja.put(jj);
            jjMain.put("EA",ja);
            ja=new JSONArray();
            for(int i=0;i<picPath.size();i++){
                JSONObject jjjj=new JSONObject();
                jjjj.put("imgurl",picPath.get(i).substring(picPath.get(i).lastIndexOf("/")+1));
                jjjj.put("title","");
                jjjj.put("remarks","");
                ja.put(jjjj);
            }
            jjMain.put("ActivityCaptures",ja);
            //ja1.put(jjMain);
            Log.v("printing_final",jjMain.toString());
            Call<ResponseBody> submit=apiInterface.saveDailyAllowance(jjMain.toString());
            submit.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("printing_json",jsonData);
                        JSONObject json=new JSONObject(jsonData);
                       if(json.getString("success").equalsIgnoreCase("true"))
                       {
                           Toast.makeText(TAClaimActivity.this,"Submitted Successfully ",Toast.LENGTH_SHORT).show();
                           Intent i=new Intent(TAClaimActivity.this,ViewTASummary.class);
                           startActivity(i);
                       }
                    }catch (Exception e){
                        Log.v("printing_excep_va",e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}

    }
    public void getMulipart(String  path,int  x){
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field("MR0417");
        CallApiImage(values,imgg,x);
    }
    public MultipartBody.Part convertimg(String tag, String path){
        MultipartBody.Part yy=null;
        Log.v("full_profile",path);
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if(path.contains(".png")||path.contains(".jpg")||path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        }catch (Exception e){}
        Log.v("full_profile",yy+"");
        return yy;
    }
    public HashMap<String, RequestBody> field(String val){
        HashMap<String,RequestBody> xx=new HashMap<String,RequestBody>();
        xx.put("data",createFromString(val));

        return xx;

    }
    private RequestBody createFromString(String txt)
    {
        return RequestBody.create(MultipartBody.FORM,txt);
    }
    public void CallApiImage(HashMap<String,RequestBody> values, MultipartBody.Part imgg, final int x){
        Call<ResponseBody> Callto;

        Callto = apiInterface.uploadimg(values,imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());
                //uploading.setText("Uploading "+String.valueOf(count)+"/"+String.valueOf(count_check));

                try {
                    if (response.isSuccessful()) {


                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject  js=new JSONObject(jsonData);
                        if(js.getString("success").equalsIgnoreCase("true")){
                            Log.v("printing_dynamic_cou",js.getString("url"));
                            if(x==-1){
                                finalpicPath.add(js.getString("url"));
                            }
                            else{
                                String filepathing=array.get(x).getImg_url();
                                filepathing=filepathing+js.getString("url")+",";
                                array.get(x).setImg_url(filepathing);
                            }

                            //submitData();

                        }

                    }

                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });
    }
    public void datePick(){
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog=new DatePickerDialog(TAClaimActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int mnth=monthOfYear+1;
                Log.v("printing_date_format",dayOfMonth + "-" + mnth+ "-" +year);

                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, monthOfYear, dayOfMonth-1);
                String dayOfWeek = simpledateformat.format(date);
                Log.v("printing_date_fo_day",dayOfWeek);
                txt_date.setText(dayOfMonth + "-" + mnth+ "-" +year+"-"+dayOfWeek);
                callApi(year+"-"+mnth+"-"+dayOfMonth);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }
    public void popupCapture() {
        final Dialog dialog = new Dialog(TAClaimActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_capture);
        dialog.show();
        TextView upload=dialog.findViewById(R.id.upload);
        TextView camera=dialog.findViewById(R.id.camera);
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
    public void selectMultiImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
    }
    public void captureFile(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Uri outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), "pickImageResult.jpeg"));
        outputFileUri = FileProvider.getUriForFile(TAClaimActivity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), "pickImageResult"+System.currentTimeMillis()+".jpeg"));
        Log.v("priniting_uri",outputFileUri.toString()+" output "+outputFileUri.getPath()+" raw_msg "+getExternalCacheDir().getPath());
        //content://com.saneforce.sbiapplication.fileprovider/shared_video/Android/data/com.saneforce.sbiapplication/cache/pickImageResult.jpeg
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 2);
    }
    public void callApi(String date){
        try {
            JSONObject jj = new JSONObject();
            jj.put("Ta_Date", date);
            jj.put("div", div);
            jj.put("sf", SF_code);
            jj.put("rSF", SF_code);
            jj.put("State_Code", State_Code);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getDailyAllowance(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {


                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data",jsonData);
                            array=new ArrayList<>();
                            JSONObject js=new JSONObject(jsonData);
                            JSONArray ja=js.getJSONArray("ExpenseWeb");
                            for(int i=0;i<ja.length();i++){
                                JSONObject json_o=ja.getJSONObject(i);
                                array.add(new SelectionModel(json_o.getString("Name"),"",json_o.getString("ID"),"",""));
                            }
                            JSONArray ja1=js.getJSONArray("TodayExpense");
                            if(ja1.length()!=0)
                            todayExp=ja1.getJSONObject(0).toString();
                            Log.v("todayExp_val",todayExp);
                            DailyExpenseAdapter adpt=new DailyExpenseAdapter(TAClaimActivity.this,array);
                            list.setAdapter(adpt);

                        }
                    }catch (Exception e){}
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}
    }
}