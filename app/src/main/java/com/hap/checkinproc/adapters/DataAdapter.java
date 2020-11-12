package com.hap.checkinproc.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.Distributor_Master;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.Model_Class.Work_Type_Model;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.FruitViewHolder> implements Filterable {
    List<Work_Type_Model> contactList;
    Master_Interface updateUi;
    int typeName;
    private List<Work_Type_Model> contactListFiltered;
    private List<Distributor_Master> distributor_Master;
    private List<Distributor_Master> Filterdb;
    private List<Route_Master> Route_Master;
    private List<Route_Master> Filterroute;

    public DataAdapter(List<Work_Type_Model> myDataset, Context context, int type, List<Distributor_Master> Distributor_Master, List<Route_Master> route_Master) {
        contactList = myDataset;
        typeName = type;
        contactListFiltered = myDataset;
        distributor_Master = Distributor_Master;
        Filterdb = Distributor_Master;
        Route_Master = route_Master;
        Filterroute = route_Master;
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


        if (typeName == 1) {
            final Work_Type_Model contact = contactListFiltered.get(i);
            fruitViewHolder.mTextView.setText(contact.getName());

        } else if (typeName == 2) {
            final Distributor_Master dbfilter = Filterdb.get(i);
            fruitViewHolder.mTextView.setText(dbfilter.getName());
        } else {
            final Route_Master routefilter = Filterroute.get(i);
            fruitViewHolder.mTextView.setText(routefilter.getName());
        }

    }

    @Override
    public int getItemCount() {
        int siz = 0;
        if (typeName == 1) {
            siz = contactListFiltered.size();
        } else if (typeName == 2) {
            siz = Filterdb.size();
        } else {
            siz = Filterroute.size();
        }
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
                    if (typeName == 1) {
                        contactListFiltered = contactList;
                    } else if (typeName == 2) {
                        Filterdb = distributor_Master;
                    } else {
                        Filterroute = Route_Master;
                    }

                } else {
                    if (typeName == 1) {
                        List<Work_Type_Model> filteredList = new ArrayList<>();
                        for (Work_Type_Model row : contactList) {
                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            Log.e("Thirumalaivasan", row.getName().toLowerCase());

                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                                Log.e("FIlter_Rowvalues", String.valueOf(row.getName().toLowerCase()));
                            }
                        }

                        contactListFiltered = filteredList;
                    } else if (typeName == 2) {
                        List<Distributor_Master> filteredList = new ArrayList<>();
                        for (Distributor_Master row : distributor_Master) {
                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            Log.e("Thirumalaivasan", row.getName().toLowerCase());
                            if (row.getName().toLowerCase().trim().replaceAll("\\s", "").contains(charString.toLowerCase().trim().replaceAll("\\s", ""))) {
                                filteredList.add(row);
                                Log.e("FIlter_Rowvalues", String.valueOf(row.getName().toLowerCase()));
                            }
                        }

                        Filterdb = filteredList;
                    } else {
                        List<Route_Master> filteredList = new ArrayList<>();
                        for (Route_Master row : Route_Master) {
                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            Log.e("Thirumalaivasan", row.getName().toLowerCase());
                            if (row.getName().toLowerCase().trim().replaceAll("\\s", "").contains(charString.toLowerCase().trim().replaceAll("\\s", ""))) {
                                filteredList.add(row);
                                Log.e("FIlter_Rowvalues", String.valueOf(row.getName().toLowerCase()));
                            }
                        }

                        Filterroute = filteredList;
                    }


                }
                if (typeName == 1) {
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = contactListFiltered;
                    return filterResults;
                } else if (typeName == 2) {
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = Filterdb;
                    return filterResults;
                } else {
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = Filterroute;
                    return filterResults;
                }

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                if (typeName == 1) {
                    contactListFiltered = (ArrayList<Work_Type_Model>) filterResults.values;
                } else if (typeName == 2) {
                    Filterdb = (ArrayList<Distributor_Master>) filterResults.values;
                } else {
                    Filterroute = (ArrayList<Route_Master>) filterResults.values;
                }
                Log.e("FILTERED_RESULT", String.valueOf(contactListFiltered.size()));
                notifyDataSetChanged();
            }
        };

    }

    public class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public FruitViewHolder(View v) {
            super(v);
            mTextView =  v.findViewById(R.id.textView);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            updateUi.OnclickMasterType(contactListFiltered, this.getAdapterPosition(), Filterdb, Filterroute, typeName);


        }
    }


}
