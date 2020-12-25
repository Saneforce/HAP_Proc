package com.hap.checkinproc.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import com.hap.checkinproc.Activity.Util.ImageFilePath;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
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
    ArrayList<SelectionModel> array = new ArrayList<>();
    ListView list;
    ArrayList<String> picPath = new ArrayList<>();
    ArrayList<String> finalpicPath = new ArrayList<>();
    Button btn_sub;
    String todayExp = "";
    Uri outputFileUri;
    int pos = -1;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    String SF_code = "", div = "", State_Code = "";
    LinearLayout lay_row;
    int cardViewCount = 70, rlayCount = 700;
    Shared_Common_Pref mShared_common_pref;

    String StartedKm = "", EndedKm = "", ModeOfTravel = "", FromPlace = "", ToPlace = "", Bus = "", StratedKmImage = "", EndedKmImage = "", BusFareImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_claim);
        txt_date = findViewById(R.id.txt_date);
        card_date = findViewById(R.id.card_date);
        img_attach = findViewById(R.id.img_attach);
        btn_sub = findViewById(R.id.btn_sub);
        list = findViewById(R.id.list);
        lay_row = findViewById(R.id.lay_row);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        txt_date.setText(Common_Class.GetDateOnly() + "-" + Common_Class.GetDay());
        String date = Common_Class.GetDate();
        callApi(date.substring(0, date.indexOf(" ")));

        mShared_common_pref = new Shared_Common_Pref(this);
        StartedKm = mShared_common_pref.getvalue("Started_km");
        ModeOfTravel = mShared_common_pref.getvalue("mode_of_travel");


        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < picPath.size(); i++) {
                    getMulipart(picPath.get(i), -1);
                }
                for (int i = 0; i < array.size(); i++) {
                    String[] imp = array.get(i).getImg_url().split(",");
                    for (int j = 0; j < imp.length - 1; j++) {
                        getMulipart(imp[j], i);
                    }
                }
                submitData();
            }
        });
        DailyExpenseAdapter.bindUpdateListener(new UpdateUi() {
            @Override
            public void update(int value, int pos1) {
                pos = pos1;
                popupCapture();
            }
        });
        img_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = -1;
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
            String filepathing = "";
            if (data.getData() == null) {
                Log.v("onactivity_result", data.getData() + " ");

                for (pickedImageCount = 0; pickedImageCount < mClipData.getItemCount();
                     pickedImageCount++) {
                    Log.v("picked_image_value", mClipData.getItemAt(pickedImageCount).getUri() + "");
                    ImageFilePath filepath = new ImageFilePath();
                    String fullPath = filepath.getPath(TAClaimActivity.this, mClipData.getItemAt(pickedImageCount).getUri());
                    Log.v("picked_fullPath", fullPath + "");
                    if (pos == -1)
                        picPath.add(fullPath);
                    else {
                        SelectionModel m = array.get(pos);
                        filepathing = m.getImg_url();
                        filepathing = filepathing + fullPath + ",";
                        m.setImg_url(filepathing);

                    }
                }
            } else {
                Log.v("data_pic_multiple", "is in empty");
                ImageFilePath filepath = new ImageFilePath();
                String fullPath = filepath.getPath(TAClaimActivity.this, data.getData());
                Log.v("data_pic_multiple11", fullPath);
                if (pos == -1)
                    picPath.add(fullPath);
                else {
                    SelectionModel m = array.get(pos);
                    filepathing = m.getImg_url();
                    filepathing = filepathing + fullPath + ",";
                    m.setImg_url(filepathing);

                }
            }

        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
           /* ModelDynamicView mm = array.get(pos_upload_file);
            if (!TextUtils.isEmpty(mm.getValue()))
                filePathing = mm.getValue();*/
            String finalPath = "/storage/emulated/0";
            String filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));
            Log.v("printing__file_path", filePath);
            if (pos == -1)
                picPath.add(filePath);
            else {
                SelectionModel m = array.get(pos);
                String filepathing = "";
                filepathing = m.getImg_url();
                filepathing = filepathing + filePath + ",";
                m.setImg_url(filepathing);
            }
            //filePathing = filePathing + filePath + ",";
        }
    }

    public void submitData() {
        try {
            JSONArray ja = new JSONArray();
            JSONArray ja1 = new JSONArray();
            JSONObject jjMain = new JSONObject();
            JSONObject jjMain1 = new JSONObject();
            for (int i = 0; i < array.size(); i++) {
                SelectionModel m = array.get(i);
                JSONObject jj = new JSONObject();
                jj.put("ID", m.getCode());
                jj.put("Name", m.getTxt());
                jj.put("amt", m.getValue());
                String[] imgUrl = m.getImg_url().split(",");
                String url_img = "";
                for (int j = 0; j < imgUrl.length; j++) {
                    url_img = url_img + imgUrl[j].substring(imgUrl[j].lastIndexOf("/") + 1) + ",";
                }
                jj.put("imgData", url_img);

                Log.v("total_size__log", m.getArray().size() + "");
                JSONArray jjarray = new JSONArray();
                for (int k = 0; k < m.getArray().size(); k++) {
                    for (int h = 0; h < m.getArray().get(k).getArray().size(); h++) {
                        JSONObject jjjson = new JSONObject();
                        jjjson.put("id", m.getArray().get(k).getArray().get(h).getCode());
                        jjjson.put("val", m.getArray().get(k).getArray().get(h).getValue());
                        jjjson.put("mainid", m.getCode());
                        jjarray.put(jjjson);
                    }


                }
                Log.v("printing_final_totl", jjarray.toString());
                jj.put("value", jjarray);
                ja.put(jj);
            }
            Log.v("printing_final", ja.toString());
            jjMain.put("sf", SF_code);
            jjMain.put("div", div);
            String dates = txt_date.getText().toString();
            jjMain.put("date", dates.substring(0, dates.lastIndexOf("-")) + " 00:00:00");
            jjMain.put("dailyExpense", ja);
            ja = new JSONArray();
            JSONObject jj = new JSONObject();
            JSONObject jjj = new JSONObject(todayExp);
            jj.put("LEOS", "");
            jj.put("MOT", jjj.getString("MOT"));
            jj.put("BusFare", "");
            jj.put("DateOfExp", txt_date.getText().toString());
            jj.put("Start_image", jjj.getString("Image_Url"));
            jj.put("Start_Km", jjj.getString("Start_Km"));
            jj.put("TodayworkRoute", "");
            jj.put("TodayworkName", "");
            jj.put("Workplace", "");
            jj.put("Workplace_Name", "");
            jj.put("Stop_image", jjj.getString("End_Image_Url"));
            jj.put("Stop_km", jjj.getString("End_Km"));
            jj.put("Ukey", "MGR4762" + System.currentTimeMillis());
            jj.put("EventcaptureUrl", "");
            ja.put(jj);
            jjMain.put("EA", ja);
            ja = new JSONArray();
            for (int i = 0; i < picPath.size(); i++) {
                JSONObject jjjj = new JSONObject();
                jjjj.put("imgurl", picPath.get(i).substring(picPath.get(i).lastIndexOf("/") + 1));
                jjjj.put("title", "");
                jjjj.put("remarks", "");
                ja.put(jjjj);
            }
            jjMain.put("ActivityCaptures", ja);
            //ja1.put(jjMain);
            Log.v("printing_final", jjMain.toString());
            Call<ResponseBody> submit = apiInterface.saveDailyAllowance(jjMain.toString());
            submit.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("printing_json", jsonData);
                        JSONObject json = new JSONObject(jsonData);
                        if (json.getString("success").equalsIgnoreCase("true")) {
                            Toast.makeText(TAClaimActivity.this, "Submitted Successfully ", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(TAClaimActivity.this, ViewTASummary.class);
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        Log.v("printing_excep_va", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


        } catch (Exception e) {
            Log.v("printing_exception_are", e.getMessage());
        }

    }

    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field("MR0417");
        CallApiImage(values, imgg, x);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        Log.v("full_profile", path);
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
        Log.v("full_profile", yy + "");
        return yy;
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));

        return xx;

    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }

    public void CallApiImage(HashMap<String, RequestBody> values, MultipartBody.Part imgg, final int x) {
        Call<ResponseBody> Callto;

        Callto = apiInterface.uploadimg(values, imgg);

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
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            Log.v("printing_dynamic_cou", js.getString("url"));
                            if (x == -1) {
                                finalpicPath.add(js.getString("url"));
                            } else {
                                String filepathing = array.get(x).getImg_url();
                                filepathing = filepathing + js.getString("url") + ",";
                                array.get(x).setImg_url(filepathing);
                            }

                            //submitData();

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

    public void datePick() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(TAClaimActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int mnth = monthOfYear + 1;
                Log.v("printing_date_format", dayOfMonth + "-" + mnth + "-" + year);

                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                String dayOfWeek = simpledateformat.format(date);
                Log.v("printing_date_fo_day", dayOfWeek);
                txt_date.setText(dayOfMonth + "-" + mnth + "-" + year + "-" + dayOfWeek);
                callApi(year + "-" + mnth + "-" + dayOfMonth);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    public void popupCapture() {
        final Dialog dialog = new Dialog(TAClaimActivity.this, R.style.AlertDialogCustom);
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
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
    }

    public void captureFile() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Uri outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), "pickImageResult.jpeg"));
        outputFileUri = FileProvider.getUriForFile(TAClaimActivity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), "pickImageResult" + System.currentTimeMillis() + ".jpeg"));
        Log.v("priniting_uri", outputFileUri.toString() + " output " + outputFileUri.getPath() + " raw_msg " + getExternalCacheDir().getPath());
        //content://com.saneforce.sbiapplication.fileprovider/shared_video/Android/data/com.saneforce.sbiapplication/cache/pickImageResult.jpeg
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 2);
    }

    public void callApi(String date) {
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
                            Log.v("response_data", jsonData);
                            array = new ArrayList<>();
                            lay_row.removeAllViews();
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray ja = js.getJSONArray("ExpenseWeb");
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject json_oo = ja.getJSONObject(i);
                                JSONObject json_o = json_oo.getJSONObject("value1");
                                ArrayList<SelectionModel> arr = new ArrayList<>();
                                ArrayList<SelectionModel> arr1 = new ArrayList<>();
                                JSONArray jjja = json_oo.getJSONArray("value");
                                for (int j = 0; j < jjja.length(); j++) {
                                    JSONObject json_in = jjja.getJSONObject(j);
                                    arr.add(new SelectionModel(json_in.getString("Ad_Fld_Name"), "", json_in.getString("Ad_Fld_ID"), "", ""));
                                }
                                arr1.add(new SelectionModel("", arr));
                                array.add(new SelectionModel(json_o.getString("Name"), "", json_o.getString("ID"), "", arr1, json_o.getString("user_enter"), json_o.getString("Attachemnt"), json_o.getString("Max_Allowance")));
                            }
                            JSONArray ja1 = js.getJSONArray("TodayExpense");
                            if (ja1.length() != 0)
                                todayExp = ja1.getJSONObject(0).toString();
                            Log.v("todayExp_val", todayExp);
                            DailyExpenseAdapter adpt = new DailyExpenseAdapter(TAClaimActivity.this, array);
                            list.setAdapter(adpt);

                            for (int i = 0; i < array.size(); i++) {
                                createDynamicViewForSingleRow(array.get(i).getTxt(), array.get(i).getArray(), i, array.get(i).getUser_enter(), array.get(i).getAttachment(), array.get(i).getMax());
                            }

                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }
    }

    @SuppressLint("ResourceType")
    public void createDynamicViewForSingleRow(String name, ArrayList<SelectionModel> array, int position, String userenter, String attachment, String max) {
        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutparams_1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rl.setLayoutParams(layoutparams_1);
        RelativeLayout.LayoutParams layoutparams_2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams_2.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutparams_2.setMargins(5, 0, 0, 0);
        TextView txt = new TextView(this);
        txt.setLayoutParams(layoutparams_2);
        txt.setText(name);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.basic);
        //txt.setTypeface(typeface,Typeface.BOLD);
        txt.setTypeface(typeface);
        txt.setTextSize(16f);
        txt.setTextColor(Color.BLACK);
        rl.addView(txt);
        RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_END);
        layoutparams_3.setMargins(0, 10, 0, 0);
        EditText edt = new EditText(this);
        edt.setLayoutParams(layoutparams_3);
        edt.setBackgroundResource(R.drawable.round_rect_with_blue_stroke);
        edt.setEms(5);
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (userenter.equalsIgnoreCase("0")) {
            edt.setEnabled(false);
            edt.setText(max);
        } else if (userenter.equalsIgnoreCase("1")) {
            edt.setFilters(new InputFilter[]{new InputFilterMinMax("0", max)});
        }
        edt.setPadding(9, 9, 9, 9);
        rl.addView(edt);
        RelativeLayout.LayoutParams layoutparams_4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams_4.addRule(RelativeLayout.ALIGN_PARENT_END);
        layoutparams_4.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutparams_4.setMargins(0, 0, 3, 0);
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.attach_icon);
        img.setId(899);
        if (attachment.equalsIgnoreCase("1")) {
            img.setVisibility(View.VISIBLE);
        } else
            img.setVisibility(View.INVISIBLE);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                popupCapture();
            }
        });
        //layoutparams_3.addRule(RelativeLayout.CENTER_VERTICAL);
        // layoutparams_3.setMargins(0,8,0,0);
        img.setLayoutParams(layoutparams_4);
        rl.addView(img);
        //layoutparams_3.addRule(RelativeLayout.CENTER_VERTICAL);
        lay_row.addView(rl);

        if (array.get(0).getArray().size() != 0) {
            LinearLayout r2 = new LinearLayout(this);
            LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            r2.setOrientation(LinearLayout.VERTICAL);
            r2.setLayoutParams(params_2);
            r2.setId(rlayCount);
            array.get(0).getArray().get(0).setTmp_url(String.valueOf(rlayCount));
            array.get(0).setTxt(String.valueOf(cardViewCount));
            rlayCount = rlayCount + 1;
            ArrayList<Integer> cardCountt = new ArrayList<>();
            r2.addView(generateView(0, array.get(0).getArray(), array, 0, cardCountt));
            lay_row.addView(r2);
        } else {
            View vv = new View(this);
            vv.setBackgroundColor(Color.BLACK);
            vv.setLayoutParams(layoutparams_1);
            lay_row.addView(vv);
        }
    }

    @SuppressLint("ResourceType")
    public CardView generateView(int x, ArrayList<SelectionModel> arr, ArrayList<SelectionModel> arrayList, int pos, ArrayList<Integer> cardPos) {
        CardView cardview = new CardView(this);
        cardview.setId(cardViewCount);
        cardPos.add(cardViewCount);
        cardViewCount = cardViewCount + 1;
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (x != 0) {
            // CardView cc=lay_row.findViewById(x);
            //layoutparams.addRule(RelativeLayout.BELOW,cc.getId());
        }
        cardview.setLayoutParams(layoutparams);

        cardview.setRadius(5);

        cardview.setPadding(18, 18, 18, 18);

        cardview.setCardBackgroundColor(Color.GRAY);

        cardview.setUseCompatPadding(true);
        //cardview.setMaxCardElevation(2);
        cardview.setCardElevation(5);

        /*  cardview.setMaxCardElevation(6);*/
        cardview.setRadius(8);
        LinearLayout lay = new LinearLayout(this);
        LinearLayout.LayoutParams params_3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lay.setBackgroundColor(Color.parseColor("#ffffff"));
        lay.setPadding(5, 5, 5, 5);
        params_3.setMargins(5, 6, 5, 3);
        lay.setOrientation(LinearLayout.VERTICAL);

        try {
            for (int i = 0; i < arr.size(); i++) {

                SelectionModel mm = arr.get(i);
                EditText edt1 = new EditText(this);
                edt1.setLayoutParams(params_3);
                edt1.setHint(arr.get(i).getTxt());
                edt1.setHintTextColor(Color.parseColor("#C0C0C0"));
                edt1.setBackgroundResource(R.drawable.round_rect_with_blue_stroke);
                edt1.setText("");
                edt1.setPadding(9, 9, 9, 9);
                lay.addView(edt1);
                edt1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        mm.setValue(editable.toString());
                    }
                });
            }
        } catch (Exception e) {

        }

       /* EditText edt2=new EditText(this);
        edt2.setLayoutParams(params_3);
        edt2.setBackgroundResource(R.drawable.round_rect_with_blue_stroke);
        edt2.setText("");
        edt2.setHint("Enter the value");
        edt2.setHintTextColor(Color.parseColor("#C0C0C0"));
        edt2.setPadding(9,9,9,9);

        lay.addView(edt2);*/
        LinearLayout.LayoutParams params_4 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params_4.setMargins(0, 5, 0, 0);
        RelativeLayout rlay_icon = new RelativeLayout(this);
        rlay_icon.setId(657);
        RelativeLayout.LayoutParams params_5 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //rlay_icon.setLayoutParams(params_5);
        params_5.addRule(RelativeLayout.ALIGN_PARENT_END);
        params_5.setMargins(0, 6, 0, 0);
        ImageView img1 = new ImageView(this);
        img1.setImageResource(R.drawable.circle_plus_icon);
        img1.setId(899);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countt = Integer.parseInt(arr.get(0).getTmp_url());
                ArrayList<SelectionModel> arr_new = new ArrayList<>();
                for (int l = 0; l < arr.size(); l++) {
                    SelectionModel mm = arr.get(l);
                    arr_new.add(new SelectionModel(mm.getTxt(), "", mm.getCode(), "", mm.getTmp_url()));
                }

                arrayList.add(new SelectionModel(String.valueOf(cardViewCount), arr_new));
                Log.v("arraylist_selection", arrayList.size() + "");
                LinearLayout rlays = lay_row.findViewById(countt);
                img1.setVisibility(View.INVISIBLE);
                rlays.addView(generateView(cardview.getId(), arr_new, arrayList, pos + 1, cardPos));
                // lay_row.addView(rlays);
            }
        });

        img1.setLayoutParams(params_5);
        rlay_icon.addView(img1);
        RelativeLayout.LayoutParams params_6 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //rlay_icon.setLayoutParams(params_5);

        params_6.addRule(RelativeLayout.LEFT_OF, img1.getId());
        params_6.setMargins(0, 6, 6, 0);
        ImageView img2 = new ImageView(this);
        img2.setImageResource(R.drawable.circle_minus_icon);
        img2.setId(500);

        //layoutparams_3.addRule(RelativeLayout.CENTER_VERTICAL);
        // layoutparams_3.setMargins(0,8,0,0);
        img2.setLayoutParams(params_6);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countt = Integer.parseInt(arr.get(0).getTmp_url());
                LinearLayout rlays = lay_row.findViewById(countt);
                Log.v("printing_pos_are ", pos + " end ");
                int pos = cardPos.indexOf(cardview.getId());
                Log.v("positon_vard", pos + " mock ");
                cardPos.remove(pos);
                rlays.removeView(cardview);
                arrayList.remove(pos);

                int cardCount = Integer.parseInt(arrayList.get(arrayList.size() - 1).getTxt());
                CardView card = rlays.findViewById(cardCount);
                RelativeLayout rlayay = card.findViewById(657);
                ImageView img = rlayay.findViewById(899);
                img.setVisibility(View.VISIBLE);


                // lay_row.addView(rlays);
            }
        });
        if (pos == 0) {
            img2.setVisibility(View.INVISIBLE);
        }
        rlay_icon.addView(img2);
        //lay.addView(edt1);
        lay.addView(rlay_icon);

        cardview.addView(lay);
        return cardview;
    }

    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}