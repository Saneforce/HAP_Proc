package com.hap.checkinproc.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.R;

import java.util.ArrayList;

public class DailyExpenseAdapter extends BaseAdapter implements View.OnCreateContextMenuListener {
    Context context;
    ArrayList<SelectionModel> array=new ArrayList<>();
    TextView txt_exp,tx_MxExp;
    static UpdateUi updateUi;
    LinearLayout lay_multi;

    public DailyExpenseAdapter(Context context, ArrayList<SelectionModel> array) {
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
        view= LayoutInflater.from(context).inflate(R.layout.row_item_daily_exp,viewGroup,false);
        txt_exp=view.findViewById(R.id.txt_exp);
        tx_MxExp=view.findViewById(R.id.txt_mxexp);
        EditText edt_exp=view.findViewById(R.id.edt_exp);
        ImageView img_attach=view.findViewById(R.id.img_attach);
        lay_multi=view.findViewById(R.id.lay_multi);
        SelectionModel m=array.get(i);
        txt_exp.setText(m.getTxt()+"s");
        tx_MxExp.setText(m.getMax());
        Log.v("toxt_exp_val",m.getTxt());
        createDynamicRowITem("hello",11,0);
        img_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUi.update(00,i);
            }
        });
        edt_exp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                m.setValue(editable.toString());
            }
        });
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    }
    public static void bindUpdateListener(UpdateUi up){
        updateUi=up;
    }

    public void createDynamicRowITem(String name,int id,int x) {
        CardView cardview = new CardView(context);
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        cardview.setLayoutParams(layoutparams);

        cardview.setRadius(5);

        cardview.setPadding(18, 18, 18, 18);

        cardview.setCardBackgroundColor(Color.GRAY);

        cardview.setUseCompatPadding(true);
        //cardview.setMaxCardElevation(2);
        cardview.setCardElevation(5);

        /*  cardview.setMaxCardElevation(6);*/
        cardview.setRadius(8);

        LinearLayout ll = new LinearLayout(context);
        ll.setId(id);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutparams_1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ll.setLayoutParams(layoutparams_1);
        ll.setBackgroundColor(Color.parseColor("#ffffff"));
        layoutparams_1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        EditText txt = new EditText(context);
        txt.setLayoutParams(layoutparams_1);
        txt.setText(name);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setTextSize(15f);
        txt.setBackground(null);
        txt.setPadding(28, 28, 28, 28);
        ll.addView(txt);
/*
        for(int i=0;i<2;i++){
            EditText txt = new EditText(context);
            txt.setLayoutParams(layoutparams_1);
            txt.setText(name);
            txt.setTextColor(Color.parseColor("#000000"));
            txt.setTextSize(15f);
            txt.setBackground(null);
            txt.setPadding(28, 28, 28, 28);
            ll.addView(txt);
        }
*/
        cardview.addView(ll);
        lay_multi.addView(cardview);
    }


}
