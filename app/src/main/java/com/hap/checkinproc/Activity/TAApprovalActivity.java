package com.hap.checkinproc.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Activity.Util.ApprovalModel;
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

public class TAApprovalActivity extends AppCompatActivity {
    ListView list;
    ApiInterface apiInterface;
    ArrayList<ApprovalModel> array=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_approval);
        list=findViewById(R.id.list);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        callApi("");
    }
    public void callApi(String date){
        try {
            JSONObject jj = new JSONObject();
            jj.put("Ta_Date", "2020-11-13");
            jj.put("div", "3");
            jj.put("sf", "MGR5246");
            jj.put("rSF", "MGR4762");
            jj.put("State_Code", "24");
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getTAAproval(jj.toString());
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
                            JSONArray ja=new JSONArray(jsonData);
                            for(int i=0;i<ja.length();i++){
                                JSONObject jj=ja.getJSONObject(i);
                                array.add(new ApprovalModel(jj.getString("name"),jj.getString("Start_date"),jj.toString(),jj.getJSONArray("value").toString()));
                            }
                          /*  JSONObject json=new JSONObject(jsonData);
                            JSONArray ja=json.getJSONArray("TAStartandEnd");
                            JSONArray ja1=json.getJSONArray("User_Expense");
                            String name,date;
                            for(int i=0;i<ja.length();i++){
                                JSONObject jjson=ja.getJSONObject(i);
                                name=jjson.getString("FieldForceName");
                                date=jjson.getString("Start_date");
                                array.add(new ApprovalModel(name,date,jjson.toString(),ja1.toString()));
                            }*/

                            AdapterForTAApproval adpt=new AdapterForTAApproval(TAApprovalActivity.this);
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

    public class AdapterForTAApproval extends BaseAdapter{
        Context context;

        public AdapterForTAApproval(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int i) {
            return array.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view= LayoutInflater.from(context).inflate(R.layout.row_item_approval,viewGroup,false);
            TextView txt_name=view.findViewById(R.id.txt_name);
            TextView txt_dt=view.findViewById(R.id.txt_dt);
            TextView txt_open=view.findViewById(R.id.txt_open);
            txt_name.setText(array.get(i).getName());
            txt_dt.setText(array.get(i).getDates());
            txt_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                                Intent ii=new Intent(TAApprovalActivity.this,ApprovalViewActivity.class);
                                ii.putExtra("head_val",array.get(i).getFirstdata());
                                startActivity(ii);




                    }catch (Exception e){}
                }
            });

            return view;
        }
    }
}