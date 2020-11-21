package com.hap.checkinproc.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.FruitViewHolder> implements Filterable {
    List<Common_Model> contactList;
    Master_Interface updateUi;
    int typeName;
    private List<Common_Model> contactListFiltered;

    public DataAdapter(List<Common_Model> myDataset, Context context, int type) {
        contactList = myDataset;
        typeName = type;
        contactListFiltered = myDataset;
        updateUi = ((Master_Interface) context);
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        FruitViewHolder vh = new FruitViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder fruitViewHolder, int i) {
        final Common_Model contact = contactListFiltered.get(i);
        fruitViewHolder.mTextViewOne.setText(contact.getName());
        String getAddress = contact.getAddress();
        if (isNullOrEmpty(getAddress)) {
            fruitViewHolder.mTextViewTwo.setVisibility(View.GONE);
        } else {
            fruitViewHolder.mTextViewTwo.setText(contact.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        int siz = 0;

        siz = contactListFiltered.size();

        return siz;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.e("FIlter_VAlues", charString);
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {

                    List<Common_Model> filteredList = new ArrayList<>();
                    for (Common_Model row : contactList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        Log.e("Thirumalaivasan", row.getName().toLowerCase());
                        if (row.getName().toLowerCase().trim().replaceAll("\\s", "").contains(charString.toLowerCase().trim().replaceAll("\\s", ""))) {
                            filteredList.add(row);
                            Log.e("FIlter_Rowvalues", String.valueOf(row.getName().toLowerCase()));
                        }
                    }

                    contactListFiltered = filteredList;


                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Common_Model>) filterResults.values;
                Log.e("FILTERED_RESULT", String.valueOf(contactListFiltered.size()));
                notifyDataSetChanged();
            }
        };

    }

    public class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextViewOne, mTextViewTwo;

        public FruitViewHolder(View v) {
            super(v);
            mTextViewOne = v.findViewById(R.id.txt_one);
            mTextViewTwo = v.findViewById(R.id.txt_two);
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
