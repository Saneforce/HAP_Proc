package com.hap.checkinproc.Activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovalViewActivity extends AppCompatActivity {
ArrayList<SelectionModel> array=new ArrayList<>();
ListView list_view;
EditText edt_amt;
TextView txt_name,txt_code,txt_hq,txt_des,txt_mob,txt_smod,txt_emod,txt_skm,txt_ekm;
String sf_code="",sl_no="";
Button approve,reject;
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_view);
        list_view=findViewById(R.id.list_view);
        Bundle extra=getIntent().getExtras();
        String head_val=extra.getString("head_val");
        edt_amt=findViewById(R.id.edt_amt);
        txt_name=findViewById(R.id.txt_name);
        txt_code=findViewById(R.id.txt_code);
        txt_hq=findViewById(R.id.txt_hq);
        txt_des=findViewById(R.id.txt_des);
        txt_mob=findViewById(R.id.txt_mob);
        txt_smod=findViewById(R.id.txt_smod);
        txt_emod=findViewById(R.id.txt_emod);
        txt_skm=findViewById(R.id.txt_skm);
        txt_ekm=findViewById(R.id.txt_ekm);
        approve=findViewById(R.id.approve);
        reject=findViewById(R.id.reject);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        try{
            JSONObject jj=new JSONObject(head_val);
            JSONArray jjj=jj.getJSONArray("value");
            Log.v("printing_totl_va",jjj.toString());
            for(int i=0;i<jjj.length();i++){
                JSONObject json=jjj.getJSONObject(i);
                array.add(new SelectionModel(json.getString("name"),json.getString("amt")));
            }

            txt_name.setText(": "+jj.getString("name"));
            sf_code=jj.getString("sf_code");
            sl_no=jj.getString("sl_no");
            txt_code.setText(": "+jj.getString("code"));
            txt_hq.setText(": "+jj.getString("hq"));
            txt_des.setText(": "+jj.getString("Designation"));
            txt_mob.setText(": "+jj.getString("mob"));
            txt_smod.setText(": "+jj.getString("smod"));
            txt_emod.setText(": "+jj.getString("emod"));
            txt_skm.setText(": "+jj.getString("skm"));
            txt_ekm.setText(": "+jj.getString("ekm"));

            AdapterForViewTA adpt=new AdapterForViewTA(this,array);
            list_view.setAdapter(adpt);

        }catch (Exception e){}

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupReject();
            }
        });
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi("");
            }
        });
    }
    public class AdapterForViewTA extends BaseAdapter{
        Context context;
        ArrayList<SelectionModel> arr=new ArrayList<>();

        public AdapterForViewTA(Context context, ArrayList<SelectionModel> arr) {
            this.context = context;
            this.arr = arr;
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int i) {
            return arr.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view= LayoutInflater.from(context).inflate(R.layout.row_item_ta_view,viewGroup,false);
            TextView txt_start=view.findViewById(R.id.txt_start);
            TextView txt_amt=view.findViewById(R.id.txt_amt);
            txt_start.setText(arr.get(i).getTxt());
            //Log.v("printing_txt ",arr.get(i).getTxt()+" val "+arr.get(i).getValue());
            txt_amt.setText(arr.get(i).getCode());
            txt_amt.setText("0");
            return view;
        }
    }
    public void callApi(String date){
        try {
            JSONObject jj = new JSONObject();
            jj.put("Sl_No", sl_no);
            jj.put("sf", "MGR5246");
            jj.put("AAmount", edt_amt.getText().toString());
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.saveTAApprove(jj.toString());
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


                        }
                    }catch (Exception e){}
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}
    }
    public void popupReject(){
        final Dialog dialog=new Dialog(ApprovalViewActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_reject_leave);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        RelativeLayout lay_yes=dialog.findViewById(R.id.lay_yes);
        RelativeLayout lay_cancel=dialog.findViewById(R.id.lay_cancel);
        EditText edt_feed=dialog.findViewById(R.id.edt_feed);
        lay_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiReject(edt_feed.getText().toString());
            }
        });
        lay_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    public void callApiReject(String value){
        try {
            JSONObject jj = new JSONObject();
            jj.put("Sl_No", sl_no);
            jj.put("sf", "MGR5246");
            jj.put("reason", value);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.rejectTAApprove(jj.toString());
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