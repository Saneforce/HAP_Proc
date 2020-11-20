package com.hap.checkinproc.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hap.checkinproc.Activity.Util.ImageFilePath;
import com.hap.checkinproc.Activity.Util.ModelDynamicView;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.AdapterForDynamicView;
import com.hap.checkinproc.adapters.AdapterForSelectionList;
import com.hap.checkinproc.adapters.FilterAdapter;
import com.hap.checkinproc.adapters.FilterDemoAdapter;
import com.hap.checkinproc.adapters.FilterRecycler;

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
import java.util.HashMap;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    String frm_id;
    FilterDemoAdapter adpt;
    FloatingActionButton fab;
    int value;
    FilterRecycler fil_adpt;
    RecyclerView relist_view;
    Bundle extra;
    SharedPreferences share,shareKey;
    ImageView iv_dwnldmaster_back;
    String fab_value="0";
    TextView tool_header;
    String btnShow="V";
    Button btn_save;
    //String sfCode="sfcode";
    public static String key="",header="";
    SimpleDateFormat sdf;
    SimpleDateFormat sdf_or;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    String SF_code="";
    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*chrcking*/
        SharedPreferences.Editor edit=share.edit();
        edit.putString("fab","1");
        edit.commit();

        try {
            JSONArray jjj = new JSONArray(shareKey.getString("keys", "").toString());
            if (jjj.length()!=0) {

                Log.v("printing_json_je",jjj.toString());
                JSONArray ja=new JSONArray();
                for(int i=0;i<jjj.length();i++){
                    if(i!=jjj.length()-1){
                        JSONObject js=jjj.getJSONObject(i);
                        Log.v("printing_json_ke",js.toString());
                        ja.put(js);
                    }
                }
                 edit=shareKey.edit();
                Log.v("printing_json_fi",ja.toString());
                edit.putString("keys",ja.toString());
                edit.commit();
            }
        }catch(Exception e){}
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        header=tool_header.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
         extra=getIntent().getExtras();
        frm_id=extra.getString("frmid");
        value= Integer.parseInt(extra.getString("btn_need"));
        share=getSharedPreferences("existing",0);
        shareKey=getSharedPreferences("key",0);
        progressDialog = createProgressDialog(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        fab=findViewById(R.id.fab);
        list=findViewById(R.id.list_view);
        relist_view=findViewById(R.id.relist_view);
        iv_dwnldmaster_back=findViewById(R.id.iv_dwnldmaster_back);
        tool_header=findViewById(R.id.tool_header);
        btn_save=findViewById(R.id.btn_save);
        Log.v("printing_frm_id",frm_id+"");
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf_or = new SimpleDateFormat("dd-MM-yyyy");
        if(value>0)
            fab.setVisibility(View.VISIBLE);
        else
            fab.setVisibility(View.GONE);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code=UserDetails.getString("Sfcode","");
        iv_dwnldmaster_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit=share.edit();
                edit.putString("fab","0");
                edit.commit();
                try {
                    JSONArray jjj=new JSONArray(shareKey.getString("keys", ""));
                    if (jjj.length()==0) {
                        edit = shareKey.edit();
                        JSONObject jkey1=new JSONObject();
                        JSONArray jar=new JSONArray();
                        JSONObject js=new JSONObject();
                        js.put("PK_ID", ViewActivity.key);
                        js.put("FK_ID", "");
                        jkey1.put(ViewActivity.header, js);
                        jar.put(jkey1);
                        Log.v("printing_shareing23",jar.toString());
                        edit.putString("keys", jar.toString());
                        edit.putString("pk", ViewActivity.key);
                        edit.commit();
                    }
                    else{
                        edit = shareKey.edit();
                        JSONObject jkey1=new JSONObject();
                        JSONObject js=new JSONObject();
                        js.put("PK_ID", ViewActivity.key);
                        js.put("FK_ID", shareKey.getString("pk",""));
                        jkey1.put(ViewActivity.header, js);
                        jjj.put(jkey1);
                        Log.v("printing_shareing",jjj.toString());
                        edit.putString("keys", jjj.toString());
                        edit.putString("pk", ViewActivity.key);
                        edit.commit();
                    }
                }catch (Exception e){}

                Intent i=new Intent(ViewActivity.this,ViewActivity.class);
                i.putExtra("frmid",String.valueOf(value));
                i.putExtra("btn_need","0");
                startActivity(i);
            }
        });
        Log.v("printing_share_key",shareKey.getString("keys","")+" hello ");
        callDynamicViewList();
       /* if(!frm_id.equalsIgnoreCase("-1"))
        callDynamicViewList();
        else{
            ArrayList<SelectionModel> arr=new ArrayList<>();
            array_view.add(new ModelDynamicView("19", extra.getString("arr"), "", extra.getString("value"), arr, "", "", "", "", ""));
           // array_view.add(new ModelDynamicView("66", "", "", "", arr, "", "", "", "", ""));
            adp_view = new AdapterForDynamicView(ViewActivity.this, array_view);
            list.setAdapter(adp_view);
            adp_view.notifyDataSetChanged();
            progressDialog.dismiss();
        }*/
        //commonFun();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validationOfField()){
                SharedPreferences.Editor edit = shareKey.edit();
                try {
                    JSONArray jjj = new JSONArray(shareKey.getString("keys", ""));
                    if (jjj.length() == 0) {

                        JSONObject jkey1 = new JSONObject();
                        JSONArray jar = new JSONArray();
                        JSONObject js = new JSONObject();
                        js.put("PK_ID", ViewActivity.key);
                        js.put("FK_ID", "");
                        JSONArray jAA = new JSONArray();
                        for (int i = 0; i < array_view.size(); i++) {
                            JSONObject jk = new JSONObject();
                            if (!array_view.get(i).getViewid().equalsIgnoreCase("19")) {
                                String col = array_view.get(i).getFieldname().replace(" ", "_");
                                jk.put("id", array_view.get(i).getViewid());
                                if (array_view.get(i).getViewid().equalsIgnoreCase("8")) {
                                    Date d = (Date) sdf_or.parse(array_view.get(i).getValue());
                                    jk.put("value", sdf.format(d));
                                } else if (array_view.get(i).getViewid().equalsIgnoreCase("15") || array_view.get(i).getViewid().equalsIgnoreCase("16")
                                        || array_view.get(i).getViewid().equalsIgnoreCase("17")) {
                                    String pic = "";
                                    String[] picVal = array_view.get(i).getValue().split(",");
                                    for (int k = 0; k < picVal.length; k++) {
                                        Log.v("picVal_entry", picVal[k]);
                                        getMulipart(picVal[k], 0);
                                        pic = pic + picVal[k].substring(picVal[k].lastIndexOf("/"));
                                    }
                                    jk.put("value", pic);
                                } else
                                    jk.put("value", array_view.get(i).getValue());
                                jk.put("col", col);
                                jAA.put(jk);
                            }
                        }
                        js.put("ctrl", jAA);
                        jkey1.put(ViewActivity.header, js);
                        jar.put(jkey1);
                        Log.v("printing_shareing23", jar.toString());
                        edit.putString("keys", jar.toString());
                        edit.putString("pk", ViewActivity.key);
                        edit.commit();
                    } else {
                        JSONObject jkey1 = new JSONObject();
                        JSONObject js = new JSONObject();
                        js.put("PK_ID", ViewActivity.key);
                        js.put("FK_ID", shareKey.getString("pk", ""));
                        JSONArray jAA = new JSONArray();
                        for (int i = 0; i < array_view.size(); i++) {
                            JSONObject jk = new JSONObject();
                            if (!array_view.get(i).getViewid().equalsIgnoreCase("19")) {
                                String col = array_view.get(i).getFieldname().replace(" ", "_");
                                jk.put("id", array_view.get(i).getViewid());
                                if (array_view.get(i).getViewid().equalsIgnoreCase("8")) {
                                    Date d = (Date) sdf_or.parse(array_view.get(i).getValue());
                                    jk.put("value", sdf.format(d));
                                } else if (array_view.get(i).getViewid().equalsIgnoreCase("15") || array_view.get(i).getViewid().equalsIgnoreCase("16")
                                        || array_view.get(i).getViewid().equalsIgnoreCase("17")) {
                                    String pic = "";
                                    String[] picVal = array_view.get(i).getValue().split(",");
                                    for (int k = 0; k < picVal.length; k++) {
                                        Log.v("picVal_entry", picVal[k]);
                                        getMulipart(picVal[k], 0);
                                        pic = pic + picVal[k].substring(picVal[k].lastIndexOf("/") + 1) + ",";
                                    }
                                    jk.put("value", pic);
                                } else
                                    jk.put("value", array_view.get(i).getValue());
                                jk.put("col", col);
                                jAA.put(jk);
                            }
                        }
                        js.put("ctrl", jAA);
                        jkey1.put(ViewActivity.header, js);
                        jjj.put(jkey1);
                        Log.v("printing_shareing", jjj.toString());
                        edit.putString("keys", jjj.toString());
                        edit.putString("pk", ViewActivity.key);
                        edit.commit();
                    }
                    saveDynamicList();

                }catch (Exception e){}
                }
                else
                    Toast.makeText(ViewActivity.this,"Please fill the mandatory fields",Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(array_view.size()==0){

                }
                else {
                    if (array_view.get(i).getViewid().equalsIgnoreCase("4") || array_view.get(i).getViewid().equalsIgnoreCase("5")) {
                        popupSpinner(0, array_view.get(i).getA_list(), i);
                    } else if (array_view.get(i).getViewid().equalsIgnoreCase("8")) {
                        datePick(i, 8);
                    } else if (array_view.get(i).getViewid().equalsIgnoreCase("6") || array_view.get(i).getViewid().equalsIgnoreCase("7")) {
                        popupSpinner(1, array_view.get(i).getA_list(), i);
                    }
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
            json.put("slno", frm_id);

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

                            if(jsonArray.length()==0){
                                Toast.makeText(ViewActivity.this,"No controls available for this form ",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else {
                                JSONObject jsonjk = jsonArray.getJSONObject(0);
                                if (jsonjk.getString("Control_id").equalsIgnoreCase("19")) {
                                    callDynamicViewListView();
                                } else {
                                    if (share.getString("exist", "").equalsIgnoreCase("E") && share.getString("fab", "").equalsIgnoreCase("1")) {
                                        ArrayList<SelectionModel> arr = new ArrayList<>();
                                        array_view.add(new ModelDynamicView("19", share.getString("arr", ""), "", share.getString("value", ""), arr, "", "", "0", "", ""));

                                    }
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ArrayList<SelectionModel> arr = new ArrayList<>();
                                        JSONObject json = jsonArray.getJSONObject(i);
                                        JSONArray jarray = json.getJSONArray("input");

                                        String para = "";
                               /* if(json.getString("Control_Id").equalsIgnoreCase("11"))
                                    para=json.getString("Table_code");
                                else
                                    para=json.getString("Control_Para");*/

                                        Log.v("Printing_ctrl_id", json.getString("Control_id"));
                                        if (json.getString("Control_id").equalsIgnoreCase("23")) {
                                            String gettingfield = json.getString("Fld_Src_Field");
                                            array_view.add(new ModelDynamicView(json.getString("Control_id"), gettingfield, json.getString("Fld_Name"), "", arr, json.getString("Fld_Length"), json.getString("Fld_ID"), json.getString("Frm_ID"), "", json.getString("Fld_Mandatory")));
                                            fab_value=json.getString("type");
                                            btnShow=json.getString("target");
                                            tool_header.setText(json.getString("header"));
                                            header=json.getString("header");
                                        } else if (json.getString("Control_id").equalsIgnoreCase("19")) {
                                            String gettingfield = json.getString("Fld_Src_Field");
                                            array_view.add(new ModelDynamicView("19", jarray.toString(), "", gettingfield, arr, "", "", json.getString("Target_Form"), "", ""));
                                            fab_value=json.getString("type");
                                            btnShow=json.getString("target");
                                            tool_header.setText(json.getString("header"));
                                            header=json.getString("header");
                                        } else {
                                            if (jarray.length() != 0) {
                                                for (int m = 0; m < jarray.length(); m++) {
                                                    JSONObject jjss = jarray.getJSONObject(m);
                                                    Log.v("json_input_iss", jjss.getString(json.getString("code")));
                                                    arr.add(new SelectionModel(jjss.getString(json.getString("name")), false, jjss.getString(json.getString("code"))));
                                                }

                                            }
                                            fab_value=json.getString("type");
                                            btnShow=json.getString("target");
                                            tool_header.setText(json.getString("header"));
                                            header=json.getString("header");

                                            array_view.add(new ModelDynamicView(json.getString("Control_id"), "", json.getString("Fld_Name"), "", arr, json.getString("Fld_Length"), json.getString("Fld_ID"), json.getString("Frm_ID"), "", json.getString("Fld_Mandatory")));
                                        }
                                    }
                                /*
                                arr.add(new SelectionModel("Male", false, "0"));
                                arr.add(new SelectionModel("Female", false, "1"));
                                arr.add(new SelectionModel("Others", false, "2"));
                                arr.add(new SelectionModel("None", false, "3"));

                                array_view.add(new ModelDynamicView("22", "", "Gender", "", arr, "", "22", "1", "", "0"));*/
                                /*ArrayList<SelectionModel> arr = new ArrayList<>();
                                ArrayList<SelectionModel> arr1 = new ArrayList<>();
                                arr1.add(new SelectionModel("Male", false, "0"));
                                arr1.add(new SelectionModel("Female", false, "1"));
                                arr1.add(new SelectionModel("Others", false, "2"));
                                arr1.add(new SelectionModel("None", false, "3"));
                                array_view.add(new ModelDynamicView("23", "", "Gender", "", arr1, "", "23", "1", "", "0"));*/

                                    adp_view = new AdapterForDynamicView(ViewActivity.this, array_view);
                                    list.setAdapter(adp_view);
                                    adp_view.notifyDataSetChanged();
                                    if(Integer.parseInt(fab_value)>0) {
                                        fab.setVisibility(View.VISIBLE);
                                        value=Integer.parseInt(fab_value);
                                    }
                                    if(btnShow.equalsIgnoreCase("T"))
                                        btn_save.setVisibility(View.VISIBLE);
                                    else
                                        btn_save.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    key=SF_code+"_"+frm_id+"_"+System.currentTimeMillis();

                                  /*  if(TextUtils.isEmpty(shareKey.getString("key","")))
                                    {
                                        JSONObject jkey=new JSONObject();
                                        JSONObject jkey1=new JSONObject();
                                        jkey.put("pk",key);
                                        jkey.put("fk","");
                                        jkey1.put(tool_header.getText().toString(),jkey);
                                        SharedPreferences.Editor edit=shareKey.edit();
                                        edit.putString("key",jkey1.toString());
                                        edit.commit();
                                    }
                                    */
                                }
                                //commonFun();
                                Log.v("Printing_arr_view", array_view.size() + "");
                            }

                        } catch (Exception e) {
                            Log.v("Exception_fmcg",e.getMessage());
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}

    }
    public void saveDynamicList(){
        JSONObject json=new JSONObject();
        try {
            JSONArray ja=new JSONArray(shareKey.getString("keys",""));

            json.put("data",ja);

            Log.v("printing_sf_code",ja.toString());
            Call<ResponseBody> approval=apiService.saveView(ja.toString());

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
                            Log.v("printing_save_tp",is.toString());
                            JSONObject jj=new JSONObject(is.toString());
                            if(jj.getString("success").equalsIgnoreCase("true")){
                                Intent i=new Intent(ViewActivity.this,ProcurementDashboardActivity.class);
                                startActivity(i);
                            }

                        } catch (Exception e) {
                            Log.v("Exception_fmcg",e.getMessage());
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
    public  boolean validationOfField(){
        boolean val=true;
        for(int i=0;i<array_view.size();i++){
            ModelDynamicView   mm=array_view.get(i);
            if(mm.getMandatory().equalsIgnoreCase("0")&&(!mm.getViewid().equalsIgnoreCase("22"))&&(!mm.getViewid().equalsIgnoreCase("23"))&&(!mm.getViewid().equalsIgnoreCase("19")))
            {
                if(mm.getViewid().equalsIgnoreCase("12")||mm.getViewid().equalsIgnoreCase("9")){
                    if(TextUtils.isEmpty(mm.getTvalue())||TextUtils.isEmpty(mm.getValue()))
                        return  false;
                }
                else
                {
                    if(TextUtils.isEmpty(mm.getValue()))
                        return  false;
                }
            }
        }
        return val;
    }

    public void callDynamicViewListView(){
        JSONObject json=new JSONObject();
        try {
            json.put("slno", frm_id);

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
                            // array_view.clear();
                            Log.v("printing_dynamic_view",is.toString());
                            JSONArray jsonArray=new JSONArray(is.toString());

                         //   for(int i=0;i<jsonArray.length();i++){
                                ArrayList<SelectionModel>  arr=new ArrayList<>();
                                JSONObject json=jsonArray.getJSONObject(0);
                                String gettingfield=json.getString("Fld_Src_Field");
                                String[] splitbyComma=gettingfield.split(",");
                               /* for(int k=0;k<splitbyComma.length;k++){
                                    Log.v("printing_split_comma",splitbyComma[k]+" length "+splitbyComma[k].length()+" index "+splitbyComma[k].indexOf("/"));
                                }*/
                                JSONArray   jarray=json.getJSONArray("input");
                               /* RecyclerView.LayoutManager lay= new LinearLayoutManager(ViewActivity.this);
                                relist_view.setLayoutManager(lay);
                                relist_view.addItemDecoration(new DividerItemDecoration(ViewActivity.this,
                                        DividerItemDecoration.VERTICAL));
                                fil_adpt=new FilterRecycler(ViewActivity.this,jarray,gettingfield,0);
                                relist_view.setAdapter(fil_adpt);
                                progressDialog.dismiss();*/

                                adpt=new FilterDemoAdapter(ViewActivity.this,jarray,gettingfield,0,json.getString("Target_Form"));
                                list.setAdapter(adpt);
                                fab_value=json.getString("type");
                                btnShow=json.getString("target");
                                tool_header.setText(json.getString("header"));
                            header=json.getString("header");
                            key=SF_code+"_"+frm_id+"_"+System.currentTimeMillis();
                                if(Integer.parseInt(fab_value)>0) {
                                    fab.setVisibility(View.VISIBLE);
                                    value=Integer.parseInt(fab_value);
                                }
                                if(btnShow.equalsIgnoreCase("T"))
                                    btn_save.setVisibility(View.VISIBLE);
                                else
                                    btn_save.setVisibility(View.GONE);
                                progressDialog.dismiss();

                               /*

                                String  para="";
                               /* if(json.getString("Control_Id").equalsIgnoreCase("11"))
                                    para=json.getString("Table_code");
                                else
                                    para=json.getString("Control_Para");*/
                                //array_view.add(new ModelDynamicView(json.getString("Control_id"),"",json.getString("Fld_Name"),"",arr,json.getString("Fld_Length"),json.getString("Fld_ID"),json.getString("Frm_ID"),"",json.getString("Fld_Mandatory")));
                           // }


                          /*  adp_view=new AdapterForDynamicView(ViewActivity.this,array_view);
                            list.setAdapter(adp_view);
                            adp_view.notifyDataSetChanged();
                            progressDialog.dismiss();*/
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

        Callto = apiService.uploadProcPic(values,imgg);

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


}