package com.hap.checkinproc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.FruitViewHolder> implements Filterable {

    Master_Interface updateUi;
    private List<Common_Model> contactList;
    private List<Common_Model> contactListFiltered;
    int typeName;

    public DataAdapter(List<Common_Model> myDataset, Context context, int type) {
        contactList = myDataset;
        typeName = type;
        contactListFiltered = myDataset;
        if (type != 1000)
            updateUi = ((Master_Interface) context);
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        return new FruitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FruitViewHolder fruitViewHolder, final int position) {
        if (position >= contactListFiltered.size()) return;
        final Common_Model contact = contactListFiltered.get(position);
        fruitViewHolder.mTextName.setText(contact.getName());
        String getAddress = contact.getAddress();
        String getPhone = contact.getPhone();

        if (!isNullOrEmpty(getAddress)) {
            fruitViewHolder.mTextAddress.setText(contact.getAddress());
            fruitViewHolder.mTextAddress.setVisibility(View.VISIBLE);
        } else {
            fruitViewHolder.mTextAddress.setVisibility(View.GONE);
        }
        if (!isNullOrEmpty(getPhone)) {
            fruitViewHolder.mTextPhone.setText(contact.getPhone());
            fruitViewHolder.mTextPhone.setVisibility(View.VISIBLE);
        } else {
            fruitViewHolder.mTextPhone.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<Common_Model> filteredList = new ArrayList<>();
                for (Common_Model row : contactList) {
                    if (row.getName().toLowerCase().trim().replaceAll("\\s", "").contains(charString.toLowerCase().trim().replaceAll("\\s", ""))) {
                        filteredList.add(row);
                    }
                }
                contactListFiltered = filteredList;
                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Common_Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextName, mTextPhone, mTextAddress, Checkboxname;
        LinearLayout checkboxLin, linear_row;
        CheckBox checkBox_select;

        public FruitViewHolder(View v) {
            super(v);
            mTextName = v.findViewById(R.id.txt_name);
            Checkboxname = v.findViewById(R.id.Checkboxname);
            checkBox_select = v.findViewById(R.id.checkBox_select);
            mTextPhone = v.findViewById(R.id.txt_phone);
            mTextAddress = v.findViewById(R.id.txt_address);
            checkboxLin = v.findViewById(R.id.checkboxLin);
            linear_row = v.findViewById(R.id.linear_row);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            updateUi.OnclickMasterType(contactListFiltered, this.getAdapterPosition(), typeName);
        }
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

}
