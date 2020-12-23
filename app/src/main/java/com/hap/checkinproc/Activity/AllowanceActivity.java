package com.hap.checkinproc.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.material.imageview.ShapeableImageView;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity_Hap.Dashboard;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.AdapterForSelectionList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllowanceActivity extends AppCompatActivity {
    RelativeLayout pic,rlay_pic,lay_km,lay_to,lay_From,lay_det,lay_fare;
    Uri outputFileUri;
    ShapeableImageView capture_img;
    CardView card_travel,card_to,card_typ;
    ApiInterface apiInterface;
    Button btn_submit,btn_ta;
    String filepath_final="";
    String mode,url,hq_code,typ_code;
    EditText edt_km,edt_rmk,edt_frm,edt_to,edt_fare;
    TextView txt_mode,txt_hq,txt_typ;
    Common_Class common_class;
    boolean updateMode=false;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    String SF_code="",div="";
    ArrayList<SelectionModel> array_hq=new ArrayList<>();
    RelativeLayout lay_hq,lay_typ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowance);
        pic=findViewById(R.id.pic);
        common_class = new Common_Class(this);
        rlay_pic=findViewById(R.id.rlay_pic);
        lay_From=findViewById(R.id.lay_From);
        lay_to=findViewById(R.id.lay_to);
        lay_km=findViewById(R.id.lay_km);
        lay_det=findViewById(R.id.lay_det);
        lay_fare=findViewById(R.id.lay_fare);
        capture_img=findViewById(R.id.capture_img);
        card_travel=findViewById(R.id.card_travel);
        card_to=findViewById(R.id.card_to);
        card_typ=findViewById(R.id.card_typ);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        btn_submit=findViewById(R.id.btn_submit);
        btn_ta=findViewById(R.id.btn_ta);
        edt_km=findViewById(R.id.edt_km);
        edt_rmk=findViewById(R.id.edt_rmk);
        edt_frm=findViewById(R.id.edt_frm);
        edt_to=findViewById(R.id.edt_to);
        edt_fare=findViewById(R.id.edt_fare);
        txt_mode=findViewById(R.id.txt_mode);
        lay_hq=findViewById(R.id.lay_hq);
        txt_hq=findViewById(R.id.txt_hq);
        lay_typ=findViewById(R.id.lay_typ);
        txt_typ=findViewById(R.id.txt_typ);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code=UserDetails.getString("Sfcode","");
        div=UserDetails.getString("Divcode","");
        getTravelMode();
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                outputFileUri = FileProvider.getUriForFile(AllowanceActivity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), "pickImageResult"+System.currentTimeMillis()+".jpeg"));
                Log.v("priniting_uri",outputFileUri.toString()+" output "+outputFileUri.getPath()+" raw_msg "+getExternalCacheDir().getPath());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, 12);
            }
        });
        card_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupSpinner();
            }
        });
        btn_ta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AllowanceActivity.this,TAClaimActivity.class);
                startActivity(i);
            }
        });
        lay_hq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupSpinnerHq();
            }
        });
        lay_typ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupSpinnerType();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(txt_mode.getText().toString())){
                    Toast.makeText(AllowanceActivity.this,"Select the mode of travels ",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (rlay_pic.getVisibility() == View.VISIBLE) {
                        getMulipart(filepath_final, 0);
                    } else {
                        submitData();
                    }
                }
            }
        });
    }

    public void popupSpinner() {
        final Dialog dialog = new Dialog(AllowanceActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup=(ImageView)dialog.findViewById(R.id.iv_close_popup);
        Button   ok=(Button)dialog.findViewById(R.id.ok);
        ArrayList<SelectionModel> array=new ArrayList<>();
        array.add(new SelectionModel("Bike",false,"12"));
        array.add(new SelectionModel("Bus",false,"11"));
        TextView tv_todayplan_popup_head=(TextView)dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText("Mode of Travel");
        AdapterForSelectionList adapt=new AdapterForSelectionList(AllowanceActivity.this,array,0);
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
/*
                if(isEmpty) {
                    if (array_selection.contains(new SelectionModel(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            SelectionModel m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
*/

                dialog.dismiss();
                //commonFun();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array.contains(new SelectionModel(true))) {
                    for (int i = 0; i < array.size(); i++) {
                        SelectionModel m = array.get(i);
                        if(m.isClick()){
                            mode=m.getCode();
                            txt_mode.setText(m.getTxt());
                            enableFields();
                        }
                    }
                }


                dialog.dismiss();
                capture_img.setImageURI(null);

            }
        });


    }
    public void popupSpinnerType() {
        final Dialog dialog = new Dialog(AllowanceActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup=(ImageView)dialog.findViewById(R.id.iv_close_popup);
        Button   ok=(Button)dialog.findViewById(R.id.ok);
        ArrayList<SelectionModel> array=new ArrayList<>();
        array.add(new SelectionModel("Metro",false,"MT"));
        array.add(new SelectionModel("Major",false,"MJ"));
        array.add(new SelectionModel("Others",false,"OT"));
        TextView tv_todayplan_popup_head=(TextView)dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText("Travel Type");
        AdapterForSelectionList adapt=new AdapterForSelectionList(AllowanceActivity.this,array,0);
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
/*
                if(isEmpty) {
                    if (array_selection.contains(new SelectionModel(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            SelectionModel m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
*/
                dialog.dismiss();
                //commonFun();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array.contains(new SelectionModel(true))) {
                    for (int i = 0; i < array.size(); i++) {
                        SelectionModel m = array.get(i);
                        if(m.isClick()){
                            typ_code=m.getCode();
                            txt_typ.setText(m.getTxt());

                        }
                    }
                }
                dialog.dismiss();

            }
        });


    }
    public void popupSpinnerHq() {
        final Dialog dialog = new Dialog(AllowanceActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup=(ImageView)dialog.findViewById(R.id.iv_close_popup);
        Button   ok=(Button)dialog.findViewById(R.id.ok);

        TextView tv_todayplan_popup_head=(TextView)dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText("Select Headquater");
        AdapterForSelectionList adapt=new AdapterForSelectionList(AllowanceActivity.this,array_hq,0);
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
/*
                if(isEmpty) {
                    if (array_selection.contains(new SelectionModel(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            SelectionModel m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
*/
                dialog.dismiss();
                //commonFun();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_hq.contains(new SelectionModel(true))) {
                    for (int i = 0; i < array_hq.size(); i++) {
                        SelectionModel m = array_hq.get(i);
                        if(m.isClick()){
                            hq_code=m.getCode();
                            txt_hq.setText(m.getTxt());
                            if(hq_code.equalsIgnoreCase("-1")){
                                card_to.setVisibility(View.VISIBLE);
                                card_typ.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                dialog.dismiss();

            }
        });


    }

    public void enableFields(){
        if(mode.equalsIgnoreCase("11")){
            rlay_pic.setVisibility(View.VISIBLE);
            lay_det.setVisibility(View.GONE);
            lay_From.setVisibility(View.VISIBLE);
            lay_to.setVisibility(View.VISIBLE);
            card_to.setVisibility(View.GONE);
            card_typ.setVisibility(View.GONE);
            lay_fare.setVisibility(View.VISIBLE);
            txt_mode.setText("Bus");
        }
        else{
            rlay_pic.setVisibility(View.VISIBLE);
            lay_km.setVisibility(View.VISIBLE);
            lay_det.setVisibility(View.VISIBLE);
            lay_From.setVisibility(View.GONE);
            lay_to.setVisibility(View.GONE);
            card_to.setVisibility(View.GONE);
            card_typ.setVisibility(View.GONE);
            lay_fare.setVisibility(View.GONE);
            txt_mode.setText("Bike");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == Activity.RESULT_OK)
        {

            String finalPath="/storage/emulated/0";
            String filePath=outputFileUri.getPath();
            filePath=filePath.substring(1);
            filePath=finalPath+filePath.substring(filePath.indexOf("/"));
            Log.v("printing__file_path",filePath);


            if(filePath==null)
                Toast.makeText(AllowanceActivity.this, "This file format not supported" , Toast.LENGTH_LONG).show();
            else{
                capture_img.setVisibility(View.VISIBLE);
                capture_img.setImageURI(outputFileUri);
                filepath_final=filePath;
            }

        }

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
                            url=js.getString("url");
                            submitData();
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

    public void submitData(){
        try{
            JSONObject jj=new JSONObject();
            jj.put("km",edt_km.getText().toString());
            jj.put("rmk",edt_rmk.getText().toString());
            jj.put("mod",mode);
            jj.put("sf",SF_code);
            jj.put("div",div);
            jj.put("url",url);
            jj.put("from",edt_frm.getText().toString());
            jj.put("to",edt_to.getText().toString());
            jj.put("fare",edt_fare.getText().toString());
            //saveAllowance
            Log.v("printing_allow",jj.toString());
            Call<ResponseBody>  Callto;
            if(updateMode)
                Callto = apiInterface.updateAllowance(jj.toString());
            else
                Callto = apiInterface.saveAllowance(jj.toString());

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
                            JSONObject js=new JSONObject(jsonData);
                            if(js.getString("success").equalsIgnoreCase("true")){
                                Toast.makeText(AllowanceActivity.this," Submitted successfully ",Toast.LENGTH_SHORT).show();
                                common_class.CommonIntentwithFinish(Dashboard.class);
                            }
                            else
                                Toast.makeText(AllowanceActivity.this," Cannot submitted the data ",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){}
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch(Exception e){}
    }
    public void getTravelMode(){
        try{
            JSONObject jj=new JSONObject();

            jj.put("sf",SF_code);
            jj.put("div",div);
            //jj.put("url",url);
            //saveAllowance
            Log.v("printing_allow",jj.toString());
            Call<ResponseBody>  Callto = apiInterface.getTravelMode(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {


                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("get_mode_Res",jsonData);
                            JSONArray ja=new JSONArray(jsonData);
                            if(ja.length()!=0){
                                JSONObject js=ja.getJSONObject(0);
                                mode=js.getString("MOT");
                                enableFields();
                                updateMode=true;
                            }
                            getHeadQuaters();
                           /* JSONObject js=new JSONObject(jsonData);
                            if(js.getString("success").equalsIgnoreCase("true")){
                                Toast.makeText(AllowanceActivity.this," Submitted successfully ",Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(AllowanceActivity.this," Cannot submitted the data ",Toast.LENGTH_SHORT).show();*/
                        }
                    }catch (Exception e){}
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch(Exception e){}
    }
    public void getHeadQuaters(){
        try{
            JSONObject jj=new JSONObject();

            jj.put("sfCode",SF_code);
            jj.put("divisionCode",div);
            //jj.put("url",url);
            //saveAllowance
            Log.v("printing_allow",jj.toString());
            Call<ResponseBody>  Callto = apiInterface.gethq(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {


                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("get_mode_Res",jsonData);
                            JSONArray ja=new JSONArray(jsonData);
                            for(int i=0;i<ja.length();i++){
                                JSONObject jjson=ja.getJSONObject(i);
                                array_hq.add(new SelectionModel(jjson.getString("name"),false,jjson.getString("id")));
                            }
                            array_hq.add(new SelectionModel("Others",false,"-1"));
                           /* if(ja.length()!=0){
                                card_travel.setEnabled(false);
                                JSONObject js=ja.getJSONObject(0);
                                mode=js.getString("MOT");
                                enableFields();
                                updateMode=true;
                            }*/
                           /* JSONObject js=new JSONObject(jsonData);
                            if(js.getString("success").equalsIgnoreCase("true")){
                                Toast.makeText(AllowanceActivity.this," Submitted successfully ",Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(AllowanceActivity.this," Cannot submitted the data ",Toast.LENGTH_SHORT).show();*/
                        }
                    }catch (Exception e){}
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch(Exception e){}
    }

}

