package com.hap.checkinproc.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hap.checkinproc.Activity.Util.ModelDynamicView;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.AdapterForDynamicView;
import com.hap.checkinproc.adapters.FilterAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {


    FilterAdapter adpt;
    ListView list_filter;
    ApiInterface apiService;
    ArrayList<String> array=new ArrayList<>();
    FloatingActionButton fab;
    int value;
    String frmId="";
    String btnShow="V";
    String fab_value="0";
    TextView tool_header;
    ProgressDialog progressDialog=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        list_filter=findViewById(R.id.list_filter);
        progressDialog = createProgressDialog(this);
        //fab=findViewById(R.id.fab);
        Bundle extra=getIntent().getExtras();
        value=extra.getInt("btn_need");
        frmId=extra.getString("frmid");
        tool_header=findViewById(R.id.tool_header);
       /* if(value>0)
            fab.setVisibility(View.VISIBLE);
        else
            fab.setVisibility(View.GONE);*/
        /*ViewCompat.setNestedScrollingEnabled(list_filter, true);*/
        array.add("");
        array.add("");
        array.add("");
        array.add("");
        array.add("");
        array.add("");
        array.add("");

/*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(FilterActivity.this,ViewActivity.class);
                i.putExtra("frm_id",String.valueOf(value));
                i.putExtra("btn_need","0");
                startActivity(i);
            }
        });
*/
       /* */
        callDynamicViewList();
        //createTxtView();
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



    public void callDynamicViewList(){
        JSONObject json=new JSONObject();
        try {
            json.put("slno", frmId);

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
                            JSONObject jsonjk = jsonArray.getJSONObject(0);
                            if (!jsonjk.getString("Control_id").equalsIgnoreCase("19")) {
                                Intent i=new Intent(FilterActivity.this,ViewActivity.class);
                                i.putExtra("frmid",String.valueOf(frmId));
                                i.putExtra("btn_need","0");
                                startActivity(i);
                            }
                            else {
                                for(int i=0;i<jsonArray.length();i++){
                                    ArrayList<SelectionModel>  arr=new ArrayList<>();
                                    JSONObject json=jsonArray.getJSONObject(i);
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

                                    adpt=new FilterAdapter(FilterActivity.this,jarray,gettingfield,0,json.getString("Target_Form"));
                                    list_filter.setAdapter(adpt);
                                    fab_value=json.getString("type");
                                    btnShow=json.getString("target");
                                    tool_header.setText(json.getString("header"));
                                    if(Integer.parseInt(fab_value)>0) {
                                        //fab.setVisibility(View.VISIBLE);
                                        value=Integer.parseInt(fab_value);
                                    }
                                    /*if(btnShow.equalsIgnoreCase("T"))
                                        btn_save.setVisibility(View.VISIBLE);
                                    else
                                        btn_save.setVisibility(View.GONE);*/
                                    progressDialog.dismiss();

                               /*

                                String  para="";
                               /* if(json.getString("Control_Id").equalsIgnoreCase("11"))
                                    para=json.getString("Table_code");
                                else
                                    para=json.getString("Control_Para");*/
                                    //array_view.add(new ModelDynamicView(json.getString("Control_id"),"",json.getString("Fld_Name"),"",arr,json.getString("Fld_Length"),json.getString("Fld_ID"),json.getString("Frm_ID"),"",json.getString("Fld_Mandatory")));
                                }
                            }


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

}