package com.hap.checkinproc.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.R;

import java.util.ArrayList;

public class DailyExpenseAdapter extends BaseAdapter implements View.OnCreateContextMenuListener {
    Context context;
    ArrayList<SelectionModel> array=new ArrayList<>();
    TextView txt_exp;
    static UpdateUi updateUi;

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
        EditText edt_exp=view.findViewById(R.id.edt_exp);
        ImageView img_attach=view.findViewById(R.id.img_attach);
        SelectionModel m=array.get(i);
        txt_exp.setText(m.getTxt());
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
}
