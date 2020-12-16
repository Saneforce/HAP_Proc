package com.hap.checkinproc.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.R;

import java.util.ArrayList;

public class UpcomingFollow extends BaseAdapter {
    Context context;
    ArrayList<SelectionModel> array=new ArrayList<>();

    public UpcomingFollow(Context context, ArrayList<SelectionModel> array) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.row_item_follow_up,viewGroup,false);
        TextView txt_name=view.findViewById(R.id.txt_name);
        TextView txt_date=view.findViewById(R.id.txt_date);
        ImageView img_phone=view.findViewById(R.id.img_phone);
        SelectionModel mm=array.get(i);
        txt_name.setText(mm.getTxt());
        txt_date.setText(mm.getValue());
        img_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mm.getCode()) || mm.getCode().length()<10){
                    Toast.makeText(context," Invalid phone number ",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+mm.getCode()));
                    context.startActivity(callIntent);
                }
            }
        });

        return view;
    }
}
