package com.hap.checkinproc.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hap.checkinproc.Activity.Util.ListModel;
import com.hap.checkinproc.Activity.Util.ModelDynamicView;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.AdapterForDynamicView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcurementDashboardActivity extends AppCompatActivity {
    ApiInterface apiService;
    ArrayList<ListModel> array_nav=new ArrayList<>();
    AdapterForNavigation nav_adapt;
    ListView list_nav;
    ImageView iv_nav;
    SharedPreferences share;
    SharedPreferences shareKey;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs" ;
    TextView txt_name,txt_mail,txt_head_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement_dashboard);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        list_nav=findViewById(R.id.list_nav);
        iv_nav=findViewById(R.id.iv_nav);
        txt_name=findViewById(R.id.txt_name);
        txt_mail=findViewById(R.id.txt_mail);
        txt_head_name=findViewById(R.id.txt_head_name);
        share=getSharedPreferences("existing",0);
        SharedPreferences.Editor edit=share.edit();
        edit.putString("exist","N");
        edit.putString("fab","1");
        edit.commit();
        shareKey=getSharedPreferences("key",0);
        SharedPreferences.Editor edit1=shareKey.edit();
        edit1.putString("keys","[]");
        edit1.putString("pk","");
        edit1.commit();
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        iv_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                    //super.onBackPressed();
                }
            }
        });
        txt_name.setText(UserDetails.getString("SfName",""));
        txt_mail.setText(UserDetails.getString("email",""));
        txt_head_name.setText(UserDetails.getString("SfName",""));


        callDynamicmenu();
    }

    public class AdapterForNavigation extends BaseAdapter{
        Context context;
        ArrayList<ListModel> arr=new ArrayList<>();

        public AdapterForNavigation(Context context, ArrayList<ListModel> arr) {
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
            view= LayoutInflater.from(context).inflate(R.layout.row_item_dash,viewGroup,false);
            RelativeLayout lay_row=view.findViewById(R.id.lay_row);
            TextView txt_name=view.findViewById(R.id.txt_name);
            txt_name.setText(arr.get(i).getFormName());
            lay_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if(arr.get(i).getFormType().equalsIgnoreCase("V")){
                        Intent ii=new Intent(ProcurementDashboardActivity.this,ViewActivity.class);
                        ii.putExtra("btn_need",arr.get(i).getTargetForm());
                        ii.putExtra("frmid",arr.get(i).getFormid());
                        startActivity(ii);
                    //}
                }
            });
            return view;
        }
    }

    public void callDynamicmenu(){
        JSONObject json=new JSONObject();
        try {
            json.put("div", "3");

            Log.v("printing_sf_code",json.toString());
            Call<ResponseBody> approval=apiService.getMenu(json.toString());

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

                            Log.v("printing_dynamic_menu",is.toString());
                            JSONArray js=new JSONArray(is.toString());
                            for(int i=0;i<js.length();i++){
                                JSONObject jj=js.getJSONObject(i);
                                array_nav.add(new ListModel(jj.getString("Frm_ID"),jj.getString("Frm_Name"),jj.getString("Frm_Table"),jj.getString("Targt_Frm"),jj.getString("Frm_Type")));
                            }
                            nav_adapt=new AdapterForNavigation(ProcurementDashboardActivity.this,array_nav);
                            list_nav.setAdapter(nav_adapt);
                            nav_adapt.notifyDataSetChanged();
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