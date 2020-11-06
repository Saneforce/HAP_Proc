package com.hap.checkinproc.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Bundle;

import com.hap.checkinproc.Activity.Util.ImageFilePath;
import com.hap.checkinproc.Activity.Util.ModelDynamicView;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.AdapterForDynamicView;
import com.hap.checkinproc.adapters.AdapterForSelectionList;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ViewActivity extends AppCompatActivity {
    boolean isEmpty=false;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;
    TimePickerDialog    timePickerDialog;
    ArrayList<ModelDynamicView> array_view=new ArrayList<>();
    ApiInterface apiService;
    AdapterForDynamicView adp_view;
    ProgressDialog progressDialog=null;
    ListView list;
    int pos_upload_file=0;
    Uri outputFileUri;
    public static int CAMERA_REQUEST=12;
    String filePathing="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        progressDialog = createProgressDialog(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        list=findViewById(R.id.list_view);
        callDynamicViewList();
        //commonFun();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(array_view.get(i).getViewid().equalsIgnoreCase("4")){
                    popupSpinner(0,array_view.get(i).getA_list(),i);
                }
                else if(array_view.get(i).getViewid().equalsIgnoreCase("8")){
                    datePick(i,8);
                }
                else    if( array_view.get(i).getViewid().equalsIgnoreCase("6")){
                    popupSpinner(1,array_view.get(i).getA_list(),i);
                }

            }
        });
        AdapterForDynamicView.bindListernerForDateRange(new UpdateUi() {
            @Override
            public void update(int value, int pos) {
                if(value==15){
                    pos_upload_file=pos;
                    uploadFile();
                }
                else if(value==16){
                    pos_upload_file=pos;
                    captureFile();
                }
                else if(value==17){
                    pos_upload_file=pos;
                    popupCapture();
                }
                else    if(value>5 && value<10){
                    datePick(pos,value);
                }
                else
                    timePicker(pos,value);
            }

            @Override
            public void Getjsoninterface(String Success) {

            }


        });
/*
        AdapterForDynamicView.bindListernerForDateRange(new TwoTypeparameter() {
            @Override
            public void update(int value, int pos) {
                if(value==5){
                    pos_upload_file=pos;
                    uploadFile();
                }else    if(value>5 && value<10){
                    datePick(pos,value);
                }
                else
                    timePicker(pos,value);
            }
        });
*/
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        filePathing="";
        try {
            // When an Image is picked
            if (requestCode == 2 && resultCode == RESULT_OK
                    && null != data && null != data.getClipData()) {

                ClipData mClipData = data.getClipData();
                ModelDynamicView    mm=array_view.get(pos_upload_file);
                if(!TextUtils.isEmpty(mm.getValue()))
                    filePathing=mm.getValue();
                Toast.makeText(ViewActivity.this, "You picked " +
                                (mClipData.getItemCount() > 1 ? mClipData.getItemCount() + "Images" :
                                        mClipData.getItemCount() + "Image"),
                        Toast.LENGTH_LONG).show();

                //pickedImageContainer.removeAllViews();

                int pickedImageCount;

                for (pickedImageCount = 0; pickedImageCount < mClipData.getItemCount();
                     pickedImageCount++) {
                    Log.v("picked_image_value",mClipData.getItemAt(pickedImageCount).getUri()+"");
                    ImageFilePath   filepath=new ImageFilePath();
                    String  fullPath=filepath.getPath(ViewActivity.this,mClipData.getItemAt(pickedImageCount).getUri());
                    Log.v("picked_image_value_path",fullPath+"");

                    filePathing=filePathing+fullPath+",";
                    /*ImageView productImageView =
                            new ImageView(getActivity());

                    productImageView.setAdjustViewBounds(true);

                    productImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    productImageView.setLayoutParams(new LinearLayout
                            .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));

                    pickedImageContainer.addView(productImageView);

                    Glide.with(getActivity())
                            .load(mClipData.getItemAt(pickedImageCount).getUri())
                            .fitCenter().placeholder(R.drawable.map_default)
                            .error(R.drawable.map_default)
                            .into(productImageView);*/
                }

                mm.setValue(filePathing);
                adp_view.notifyDataSetChanged();

            }
            else if(requestCode == 7 && resultCode == RESULT_OK){
                if(resultCode==RESULT_OK){
                    ModelDynamicView    mm=array_view.get(pos_upload_file);
                    if(!TextUtils.isEmpty(mm.getValue()))
                        filePathing=mm.getValue();
                    Uri uri=data.getData();
                    ImageFilePath   filepath=new ImageFilePath();
                    String  fullPath=filepath.getPath(ViewActivity.this,uri);
                    Log.v("file_path_are",fullPath+"print");
                    String PathHolder = data.getData().getPath();
                    filePathing=filePathing+fullPath+",";
                    /*String filePath = getImageFilePath(data);
                    Log.v("file_path_are",filePath);*/
                    if(fullPath==null)
                        Toast.makeText(ViewActivity.this, "This file format not supported" , Toast.LENGTH_LONG).show();
                    else{

                        mm.setValue(filePathing);
                        adp_view.notifyDataSetChanged();
                    }

                    //commonFun();

                }

            }
            else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
            {
                ModelDynamicView    mm=array_view.get(pos_upload_file);
                if(!TextUtils.isEmpty(mm.getValue()))
                    filePathing=mm.getValue();
                String finalPath="/storage/emulated/0";
                String filePath=outputFileUri.getPath();
                filePath=filePath.substring(1);
                filePath=finalPath+filePath.substring(filePath.indexOf("/"));
                Log.v("printing__file_path",filePath);
                filePathing=filePathing+filePath+",";
           /* String filePath = getImageFilePath(data);
            String photopath=getRealPathFromURI(outputFileUri);
            Log.v("printing_photopath",photopath);
            Log.v("printing_file_path_re",filePath);*/
/*
                try {
                    File compressedImageFile = new Compressor(getApplicationContext()).compressToFile(new File(filePath));
                    camera_img.setImageURI(Uri.parse(compressedImageFile.toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
*/
                //cameraPath.add(new ModelForStringBoolean(filePath,true));
                if(filePath==null)
                    Toast.makeText(ViewActivity.this, "This file format not supported" , Toast.LENGTH_LONG).show();
                else{

                    mm.setValue(filePathing);
                    adp_view.notifyDataSetChanged();
                }

            }

            else {
                Toast.makeText(this, "You haven't picked any Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: Something went wrong " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void selectMultiImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    public void uploadFile(){
        Intent    intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 7);
    }
    public void captureFile(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Uri outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), "pickImageResult.jpeg"));
        outputFileUri = FileProvider.getUriForFile(ViewActivity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), "pickImageResult"+System.currentTimeMillis()+".jpeg"));
        Log.v("priniting_uri",outputFileUri.toString()+" output "+outputFileUri.getPath()+" raw_msg "+getExternalCacheDir().getPath());
        //content://com.saneforce.sbiapplication.fileprovider/shared_video/Android/data/com.saneforce.sbiapplication/cache/pickImageResult.jpeg
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    public void popupSpinner(int type, final ArrayList<SelectionModel> array_selection, final int    pos){
        final Dialog dialog=new Dialog(ViewActivity.this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);
        TextView tv_todayplan_popup_head=(TextView)dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText(array_view.get(pos).getFieldname());
        ImageView iv_close_popup=(ImageView)dialog.findViewById(R.id.iv_close_popup);
        Button   ok=(Button)dialog.findViewById(R.id.ok);

        if (array_selection.contains(new SelectionModel(true))) {
            isEmpty=false;
        }
        else
            isEmpty=true;

        final AdapterForSelectionList adapt=new AdapterForSelectionList(ViewActivity.this,array_selection,type);
        popup_list.setAdapter(adapt);
        final SearchView search_view=(SearchView)dialog.findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.v("search_view_str",s);
                adapt.getFilter().filter(s);
                return false;
            }
        });

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty) {
                    if (array_selection.contains(new SelectionModel(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            SelectionModel m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
                dialog.dismiss();
                commonFun();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_selection.contains(new SelectionModel(true))) {
                    for (int i = 0; i < array_selection.size(); i++) {
                        SelectionModel m = array_selection.get(i);
                        if(m.isClick()){
                            array_view.get(pos).setValue(m.getTxt());
                            i=array_selection.size();
                            adp_view.notifyDataSetChanged();
                            break;
                        }
                    }

                }
                else {
                    array_view.get(pos).setValue("");
                    adp_view.notifyDataSetChanged();
                }
                dialog.dismiss();
                commonFun();
            }
        });

    }
    public void datePick(final int  pos, final int value){
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(ViewActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ModelDynamicView    mm=array_view.get(pos);
                int mnth=monthOfYear+1;
                if(mm.getViewid().equalsIgnoreCase("9")){
                    if(value==8){
                        if(TextUtils.isEmpty(mm.getTvalue()))
                            mm.setValue(dayOfMonth + "-" + mnth+ "-" +year);
                        else{
                            String val= dayOfMonth + "-" + mnth+ "-" +year;
                            if(dateDifference(val,mm.getTvalue())<0)
                                Toast.makeText(ViewActivity.this,"From date should be lesser",Toast.LENGTH_SHORT).show();
                            else
                                mm.setValue(dayOfMonth + "-" + mnth+ "-" +year);

                        }
                    }
                    else{
                        if(TextUtils.isEmpty(mm.getValue()))
                            Toast.makeText(ViewActivity.this,"Fill from date",Toast.LENGTH_SHORT).show();
                        else{
                            String val= dayOfMonth + "-" + mnth+ "-" +year;
                            if(dateDifference(mm.getValue(),val)<0)
                                Toast.makeText(ViewActivity.this,"To date should be greater",Toast.LENGTH_SHORT).show();
                            else
                                mm.setTvalue(dayOfMonth + "-" + mnth+ "-" +year);
                        }


                    }
                }
                else
                    mm.setValue(dayOfMonth + "-" + mnth+ "-" +year);

                adp_view.notifyDataSetChanged();
                commonFun();
           /* Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);*/
                // txt_fdate.setText(dateFormatter.format(newDate.getTime()));
                //fdate=txt_fdate.getText().toString();

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }
    public void timePicker(final int  pos,final int value){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(ViewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                ModelDynamicView mm=array_view.get(pos);
                if(mm.getViewid().equalsIgnoreCase("12")){
                    if(value==12){
                        if(TextUtils.isEmpty(mm.getTvalue()))
                            mm.setValue(selectedHour + ":" + selectedMinute);
                        else
                        {
                            int thr=spiltTime(mm.getTvalue());
                            int tmin=spiltMin(mm.getTvalue());
                            if(thr==selectedHour){
                                if(selectedMinute<tmin){
                                    mm.setValue(selectedHour + ":" + selectedMinute);
                                }
                                else
                                    Toast.makeText(ViewActivity.this,"From time should be lesser",Toast.LENGTH_SHORT).show();
                            }
                            else    if(thr>selectedHour){
                                mm.setValue(selectedHour + ":" + selectedMinute);
                            }
                            else
                                Toast.makeText(ViewActivity.this,"From time should be lesser",Toast.LENGTH_SHORT).show();

                        }
                    }
                    else {
                        if(TextUtils.isEmpty(mm.getValue()))
                            Toast.makeText(ViewActivity.this,"Fill the from  time",Toast.LENGTH_SHORT).show();
                        else
                        {
                            int fhr=spiltTime(mm.getValue());
                            int fmin=spiltMin(mm.getValue());
                            if(fhr==selectedHour){
                                if(selectedMinute>fmin){
                                    mm.setTvalue(selectedHour + ":" + selectedMinute);
                                }
                                else
                                    Toast.makeText(ViewActivity.this,"To time should be greater",Toast.LENGTH_SHORT).show();
                            }
                            else    if(fhr<selectedHour){
                                mm.setTvalue(selectedHour + ":" + selectedMinute);
                            }
                            else
                                Toast.makeText(ViewActivity.this,"To time should be greater",Toast.LENGTH_SHORT).show();

                        }
                    }

                }
                else
                    mm.setValue(selectedHour + ":" + selectedMinute);


                adp_view.notifyDataSetChanged();
                commonFun();

            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    public int spiltTime(String val){
        String  v=val.substring(0,val.indexOf(":"));
        return Integer.parseInt(v);
    }
    public int spiltMin(String val){
        String  v=val.substring(val.indexOf(":")+1);
        return Integer.parseInt(v);
    }

    public long dateDifference(String   d1,String   d2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        try {
            Date date1 = simpleDateFormat.parse(d1+" 00:00:00");
            Date date2 = simpleDateFormat.parse(d2+" 00:00:00");

            long different = date2.getTime() - date1.getTime();
            Log.v("priting_date_diffence",different+"");
            return  different;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  0;
    }
    public void callDynamicViewList(){
        JSONObject json=new JSONObject();
        try {
            json.put("slno", "1");

            Log.v("printing_sf_code",json.toString());
            Call<ResponseBody> approval=apiService.getView(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            array_view.clear();
                            Log.v("printing_dynamic_view",is.toString());
                            JSONArray jsonArray=new JSONArray(is.toString());

                            for(int i=0;i<jsonArray.length();i++){
                                ArrayList<SelectionModel>  arr=new ArrayList<>();
                                JSONObject json=jsonArray.getJSONObject(i);
                                JSONArray   jarray=json.getJSONArray("input");
                                if(jarray.length()!=0){
                                    for(int m=0;m<jarray.length();m++){
                                        JSONObject  jjss=jarray.getJSONObject(m);
                                        Log.v("json_input_iss",jjss.getString(json.getString("code")));
                                        arr.add(new SelectionModel(jjss.getString(json.getString("name")),false,jjss.getString(json.getString("code"))));
                                    }

                                }
                                String  para="";
                               /* if(json.getString("Control_Id").equalsIgnoreCase("11"))
                                    para=json.getString("Table_code");
                                else
                                    para=json.getString("Control_Para");*/
                                array_view.add(new ModelDynamicView(json.getString("Control_id"),"",json.getString("Fld_Name"),"",arr,para,json.getString("Fld_ID"),json.getString("Frm_ID"),"",json.getString("Fld_Mandatory")));
                            }
                            ArrayList<SelectionModel>  arr=new ArrayList<>();
                            arr.add(new SelectionModel("Male",false,"0"));
                            arr.add(new SelectionModel("Female",false,"1"));
                            arr.add(new SelectionModel("Others",false,"2"));
                            arr.add(new SelectionModel("None",false,"3"));

                            array_view.add(new ModelDynamicView("22","","Gender","",arr,"","22","1","","0"));
                            arr=new ArrayList<>();
                            ArrayList<SelectionModel>  arr1=new ArrayList<>();
                            arr1.add(new SelectionModel("Male",false,"0"));
                            arr1.add(new SelectionModel("Female",false,"1"));
                            arr1.add(new SelectionModel("Others",false,"2"));
                            arr1.add(new SelectionModel("None",false,"3"));
                            array_view.add(new ModelDynamicView("23","","Gender","",arr1,"","23","1","","0"));

                            adp_view=new AdapterForDynamicView(ViewActivity.this,array_view);
                            list.setAdapter(adp_view);
                            adp_view.notifyDataSetChanged();
                            progressDialog.dismiss();
                            //commonFun();

                        } catch (Exception e) {
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}

    }
    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_progress);
        // dialog.setMessage(Message);
        return dialog;
    }

    public void commonFun(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }
    public void popupCapture() {
        final Dialog dialog = new Dialog(ViewActivity.this, R.style.AlertDialogCustom);
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

}