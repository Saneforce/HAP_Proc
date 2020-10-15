package com.hap.checkinproc.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hap.checkinproc.Activity.Util.ModelDynamicView;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.R;

import java.util.ArrayList;

public class AdapterForDynamicView extends BaseAdapter {
    Context context;
    ArrayList<ModelDynamicView> array=new ArrayList<>();
    RelativeLayout dyn_lay;
    static UpdateUi updateUi;
    Button btn_upload;

    public AdapterForDynamicView(Context context, ArrayList<ModelDynamicView> array) {
        this.context = context;
        this.array = array;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.row_item_fmcg_activity,viewGroup,false);
        TextView txt_label=(TextView)view.findViewById(R.id.txt_label);
        RelativeLayout rlay_spin=(RelativeLayout) view.findViewById(R.id.rlay_spin);
        RelativeLayout rlay_date=(RelativeLayout) view.findViewById(R.id.rlay_date);
        RelativeLayout rlay_currency=(RelativeLayout) view.findViewById(R.id.rlay_currency);
        RelativeLayout rlay_spin_fdt=(RelativeLayout) view.findViewById(R.id.rlay_spin_fdt);
        RelativeLayout rlay_spin_tdt=(RelativeLayout) view.findViewById(R.id.rlay_spin_tdt);
        dyn_lay=(RelativeLayout)view.findViewById(R.id.dyn_lay);
        //RelativeLayout rlay_head=(RelativeLayout) view.findViewById(R.id.rlay_head);
        RelativeLayout rlay_upload=(RelativeLayout) view.findViewById(R.id.rlay_upload);
        TextView spin_txt=(TextView)view.findViewById(R.id.spin_txt);
        TextView spin_txt_fdt=(TextView)view.findViewById(R.id.spin_txt_fdt);
        TextView spin_txt_tdt=(TextView)view.findViewById(R.id.spin_txt_tdt);
        TextView txt_currency=(TextView)view.findViewById(R.id.txt_currency);
        TextView txt_upload=(TextView)view.findViewById(R.id.txt_upload);
        EditText edt_field=(EditText) view.findViewById(R.id.edt_field);
        EditText edt_field_numeric=(EditText) view.findViewById(R.id.edt_field_numeric);
        EditText edt_feed=(EditText)view.findViewById(R.id.edt_feed);
        EditText edt_field_currency=(EditText)view.findViewById(R.id.edt_field_currency);
         btn_upload=(Button)view.findViewById(R.id.btn_upload);

        final ModelDynamicView    mm=array.get(i);
        rlay_spin.setVisibility(View.GONE);
        edt_field.setVisibility(View.GONE);
        edt_field_numeric.setVisibility(View.GONE);
        edt_feed.setVisibility(View.GONE);
        rlay_date.setVisibility(View.GONE);
        rlay_currency.setVisibility(View.GONE);
        rlay_upload.setVisibility(View.GONE);
        dyn_lay.setVisibility(View.GONE);
        txt_upload.setVisibility(View.GONE);
//        rlay_head.setVisibility(View.GONE);
        txt_label.setText(mm.getFieldname());
        if(mm.getViewid().equalsIgnoreCase("1")){
            edt_field.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mm.getValue()))
                edt_field.setText(mm.getValue());
        }
        else    if(mm.getViewid().equalsIgnoreCase("2")){
            edt_feed.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mm.getValue()))
                edt_feed.setText(mm.getValue());
        }
        else    if(mm.getViewid().equalsIgnoreCase("3")){
            edt_field_numeric.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mm.getValue()))
                edt_field_numeric.setText(mm.getValue());
        }
        else    if(mm.getViewid().equalsIgnoreCase("4") ||  mm.getViewid().equalsIgnoreCase("5")
                ||  mm.getViewid().equalsIgnoreCase("6")    ||  mm.getViewid().equalsIgnoreCase("7")
                ||  mm.getViewid().equalsIgnoreCase("8")    ||  mm.getViewid().equalsIgnoreCase("11")){
            rlay_spin.setVisibility(View.VISIBLE);
            Log.v("inside_spinner",i+" value "+mm.getValue());
            if(!TextUtils.isEmpty(mm.getValue()))
                spin_txt.setText(mm.getValue());
        }
        else    if(mm.getViewid().equalsIgnoreCase("15") || mm.getViewid().equalsIgnoreCase("16")|| mm.getViewid().equalsIgnoreCase("17")){
            rlay_upload.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mm.getValue())){
                txt_upload.setVisibility(View.VISIBLE);
                String[] ar=mm.getValue().split(",");
                Log.v("printing_storage_val",mm.getValue()+" length "+ar.length);
                int arrs=ar.length;
                txt_upload.setText(String.valueOf(arrs));
            }
        }
        else  if(mm.getViewid().equalsIgnoreCase("9") ||  mm.getViewid().equalsIgnoreCase("12"))  {
            rlay_date.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mm.getValue()))
                spin_txt_fdt.setText(mm.getValue());
            if(!TextUtils.isEmpty(mm.getTvalue()))
                spin_txt_tdt.setText(mm.getTvalue());
        }

        else    if(mm.getViewid().equalsIgnoreCase("22")){
            dyn_lay.setVisibility(View.VISIBLE);
            createDynamicView(mm.getA_list());
        }
        else    if(mm.getViewid().equalsIgnoreCase("23")){
            dyn_lay.setVisibility(View.VISIBLE);
            createDynamicViewCheckbox(mm.getA_list());
        }

        edt_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss= String.valueOf(s);
                mm.setValue(ss);
            }
        });

        edt_feed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss= String.valueOf(s);
                mm.setValue(ss);
            }
        });
        edt_field_numeric.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss= String.valueOf(s);
                mm.setValue(ss);
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mm.getViewid().equalsIgnoreCase("15"))
                updateUi.update(15,i);
                else if(mm.getViewid().equalsIgnoreCase("16"))
                    updateUi.update(16,i);
                else
                    updateUi.update(17,i);
               /* else
                    popupCapture();*/
            }
        });
        rlay_spin_fdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mm.getViewid().equalsIgnoreCase("9"))
                    updateUi.update(8,i);
                else
                    updateUi.update(12,i);
            }
        });
        rlay_spin_tdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mm.getViewid().equalsIgnoreCase("9"))
                    updateUi.update(9,i);
                else
                    updateUi.update(13,i);
            }
        });

        return view;
    }
    public  static void bindListernerForDateRange(UpdateUi pp){
        updateUi=pp;
    }


    public void createDynamicView(final ArrayList<SelectionModel> arr){
        RadioGroup rg;
        RadioButton rb1,rb2,rb3,rb4;
        rg = new RadioGroup(context);
        for(int i=0;i<arr.size();i++){
            rb1 = new RadioButton(context);
            rb1.setId(i);

            if(arr.get(i).isClick())
                rb1.setChecked(true);

                rb1.setText(arr.get(i).getTxt());
                rg.addView(rb1);

        }
       /* rb1 = new RadioButton(context);
        rb2 = new RadioButton(context);
        rb3 = new RadioButton(context);
        rb4 = new RadioButton(context);
        rb1.setText("Malekkkkkkkkkkkkkkkkkkkkkk");
        rb2.setText("Femalekkkkkkkkkkkkkkkkkkkkkkkk");
        rb3.setText("Otherskkkkkkkkkkkkkkkkkkkkkkk");
        rb4.setText("Nonekkkkkkkkkkkkkkkkkkkkkkkkkkk");
        rg.addView(rb1);
        rg.addView(rb2);
        rg.addView(rb3);
        rg.addView(rb4);*/
        rg.setOrientation(RadioGroup.VERTICAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.WRAP_CONTENT,(int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin =150;
        params.topMargin = 5;
        rg.setLayoutParams(params);
        dyn_lay.addView(rg);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.v("radio_button_id",checkedId+"and value"+arr.get(checkedId).getTxt());
                for(int i=0;i<arr.size();i++){
                    if(checkedId==i)
                        arr.get(i).setClick(true);
                    else
                        arr.get(i).setClick(false);
                }

               /* RadioButton radioButton = (RadioButton) findViewById(checkedId);
                Toast.makeText(getApplicationContext(),radioButton.getText(),Toast.LENGTH_LONG).show();*/
            }
        });
    }
    public void createDynamicViewCheckbox(final ArrayList<SelectionModel> arr){
        LinearLayout ll=new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT,(int) LinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin =150;
        params.topMargin = 5;
        for(int i=0;i<arr.size();i++){


            final CheckBox cb = new CheckBox(context);
            cb.setId(i);
            if(arr.get(i).isClick())
                cb.setChecked(true);
            cb.setText(arr.get(i).getTxt());
            cb.setLayoutParams(params);
            ll.addView(cb);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.v("printing_checked",cb.getId()+" id");
                    if(!b) {
                        cb.setChecked(false);
                        arr.get(cb.getId()).setClick(false);
                    }
                    else{
                        cb.setChecked(true);
                        arr.get(cb.getId()).setClick(true);
                    }

                }
            });
        }
        dyn_lay.addView(ll);
       /* rb1 = new RadioButton(context);
        rb2 = new RadioButton(context);
        rb3 = new RadioButton(context);
        rb4 = new RadioButton(context);
        rb1.setText("Malekkkkkkkkkkkkkkkkkkkkkk");
        rb2.setText("Femalekkkkkkkkkkkkkkkkkkkkkkkk");
        rb3.setText("Otherskkkkkkkkkkkkkkkkkkkkkkk");
        rb4.setText("Nonekkkkkkkkkkkkkkkkkkkkkkkkkkk");
        rg.addView(rb1);
        rg.addView(rb2);
        rg.addView(rb3);
        rg.addView(rb4);*/

    }



}
