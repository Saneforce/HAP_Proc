package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;

import java.util.List;

public class RetailorAdapter extends RecyclerView.Adapter<RetailorAdapter.FruitViewHolder> {

    Master_Interface updateUi;
    private List<Common_Model> contactList;
    private List<Common_Model> contactListFiltered;
    int typeName;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    Context mContext;

    Shared_Common_Pref shared_common_pref;


    public RetailorAdapter(List<Common_Model> myDataset, Context context) {
        contactList = myDataset;
        contactListFiltered = myDataset;
        mContext = context;


    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_firstmnth_info, parent, false);
        return new FruitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FruitViewHolder fruitViewHolder, final int position) {

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }


    public class FruitViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextName, mTextPhone, mTextAddress, Checkboxname;
        LinearLayout checkboxLin, linear_row;
        CheckBox checkBox_select;
        TextView cbTextName, tvPerDay;

        public FruitViewHolder(View v) {
            super(v);
//            mTextName = v.findViewById(R.id.txt_name);
//            Checkboxname = v.findViewById(R.id.Checkboxname);
//            checkBox_select = v.findViewById(R.id.checkBox_select);
//            mTextPhone = v.findViewById(R.id.txt_phone);
//            mTextAddress = v.findViewById(R.id.txt_address);
//            checkboxLin = v.findViewById(R.id.checkboxLin);
//            cbTextName = v.findViewById(R.id.Checkboxname);
//            linear_row = v.findViewById(R.id.linear_row);
//            tvPerDay = v.findViewById(R.id.txt_per_day);
//

        }


    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

}
