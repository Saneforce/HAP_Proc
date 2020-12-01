package com.hap.checkinproc.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hap.checkinproc.R;

import java.util.ArrayList;


public class DesignActivity extends AppCompatActivity {
    RelativeLayout lay_header;
    LinearLayout lin_lay1,lin_lay2;
    boolean pic=true;
    boolean phone=true;
    ArrayList<String> header=new ArrayList<>();
    ArrayList<String> footer=new ArrayList<>();
    LinearLayout rlay;
    int ki=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_item_list);
        lay_header=findViewById(R.id.lay_header);
        lin_lay1=findViewById(R.id.lin_lay1);
        lin_lay2=findViewById(R.id.lin_lay2);
        String val="Sf_Name;;Y;0,SF_Mobile;SF Mobile;Y;P,Profile_Pic;Profile Pic;Y;LI,SF_Email;SF Email;N;0,Territory_Code;Territory Code;N;0,Sf_Joining_Date;Sf Joining Date;N;0";
        String[] splitByComma=val.split(",");
         rlay=new LinearLayout(this);
        rlay.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i<splitByComma.length;i++){
            String[] splitBysemi=splitByComma[i].split(";");
            if(splitBysemi[2].equalsIgnoreCase("Y")){
                header.add("hello");
            }
            else
                footer.add("hello");
        }
        //createDynamicView();
        //Sf_Name;Sf Name;Y;0,SF_Mobile;SF Mobile;Y;P,Profile_Pic;Profile Pic;Y;LI,SF_Email;SF Email;N;0,Territory_Code;Territory Code;N;0,Sf_Joining_Date;Sf Joining Date;N;0
    }

    @SuppressLint("ResourceType")
    public void createDynamicView(String type, String pic){

        if(type.equalsIgnoreCase("Y") && pic.equalsIgnoreCase("0") ){
            rlay.addView(createHeader(ki,"Warren Hemandez"));
            ki=9;
        }
        else if(type.equalsIgnoreCase("Y") && pic.equalsIgnoreCase("P")){
            ImageView img=new ImageView(this);
            img.setImageResource(R.drawable.call_icon);
            img.setId(899);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(30,30);
            params2.addRule(RelativeLayout.ALIGN_PARENT_END);
            params2.addRule(RelativeLayout.CENTER_VERTICAL);
            params2.setMargins(0,0,15,0);
            img.setLayoutParams(params2);
            lay_header.addView(img);
            rlay.addView(createHeader(ki,"Warren Hemandez"));
        }
        else if(type.equalsIgnoreCase("Y") && !pic.equalsIgnoreCase("0"))
        {
           /* CircleImageView img=new CircleImageView(this);
            img.setImageResource(R.drawable.profile);
            img.setId(89);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(80,80);
            params1.addRule(RelativeLayout.CENTER_VERTICAL);
            img.setLayoutParams(params1);
            lay_header.addView(img);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.WRAP_CONTENT,(int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.RIGHT_OF,img.getId());
            params2.addRule(RelativeLayout.CENTER_VERTICAL);
            params2.setMargins(10,0,0,0);
            rlay.setLayoutParams(params2);*/
        }

        lay_header.addView(rlay);


    }
    @SuppressLint("ResourceType")
    public TextView createHeader(int x, String value){
        TextView txt=new TextView(this);
        txt.setText(value);
        if(x==0){
            txt.setId(9);
            Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_bold);
            txt.setTypeface(typeface);
            txt.setTextSize(15f);
            txt.setTextColor(Color.BLACK);
        }
        else{
            txt.setId(99);
            Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto);
            txt.setTypeface(typeface);
            txt.setTextSize(13f);
            txt.setTextColor(Color.BLACK);
        }
        return txt;
    }

    public void createDynamicview(int x,String val){
        TextView txt=new TextView(this);
        Log.v("txt_str_view",val);
        txt.setText(val);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setTextSize(15f);
        txt.setSingleLine(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams.setMargins(10,5,0,0);
        txt.setLayoutParams(layoutParams);
        if(x==0)
            lin_lay1.addView(txt);
        else
            lin_lay2.addView(txt);
    }

}