package com.hap.checkinproc.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hap.checkinproc.Interface.ChildListInterface;
import com.hap.checkinproc.Interface.ParentListInterface;
import com.hap.checkinproc.Model_Class.HeaderCat;
import com.hap.checkinproc.Model_Class.HeaderName;
import com.hap.checkinproc.Model_Class.Product;
import com.hap.checkinproc.Model_Class.Product_Array;
import com.hap.checkinproc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ParentListAdapter extends RecyclerView.Adapter<ParentListAdapter.MyViewHolder> implements Filterable {

    //private List<Movie> moviesList;
    List<HeaderName> eventInfor;
    List<HeaderName> evenParent;

    private Activity activity;
    ArrayList<String> stringValue;
    ArrayList<Product_Array> dataValue;
    String gTotal;

    ArrayList<String> productName, productCode, productQuantity, productTotalDetails;
    HeaderCat headercat;

    List<Product> eventsArrayList;
    ParentListInterface itemClick;
    String getIdFromChild;

    Integer getPositionValue = 0;
    String productNameValue, productCodeValue;
    Integer productQuantityValue;
    ArrayList<String> mString;
    String catIma, catNam;


    public ParentListAdapter(HeaderCat headercat, List<HeaderName> eventInfor, List<Product> eventsArrayList, Activity activity, ArrayList<String> mString, ParentListInterface itemClick) {
        this.eventInfor = eventInfor;
        this.headercat = headercat;
        this.activity = activity;
        stringValue = new ArrayList<String>();

        this.eventsArrayList = eventsArrayList;
        this.mString = mString;
        this.evenParent = eventInfor;
        this.itemClick = itemClick;
        dataValue = new ArrayList<>();
        productTotalDetails = new ArrayList<>();
        productCode = new ArrayList<>();
        productName = new ArrayList<>();
        productQuantity = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_header_sub_category, null);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClickParentInter(gTotal, 0, getIdFromChild, getPositionValue, productNameValue, productCodeValue, productQuantityValue, catIma, catNam);
            }
        });
        itemView.setClickable(false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        List<Product> mProducts = evenParent.get(position).getProduct();

        Picasso.with(activity)
                .load(evenParent.get(position).getCatImage())
                .error(R.drawable.no_prod)
                .into(holder.subProdcutImage);

        holder.subProdcutName.setText(evenParent.get(position).getName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
        holder.subProductChild.setLayoutManager(layoutManager);
        holder.subProductChild.setHasFixedSize(true);
        holder.subProductChild.setNestedScrollingEnabled(false);
        holder.subProductChild.setItemAnimator(new DefaultItemAnimator());
      ChildListAdapter eventListChildAdapter = new ChildListAdapter(this.activity, mProducts, new ChildListInterface() {

            @Override
            public void onClickInterface(String value, int totalValue, String itemID, Integer positionValue, String productName, String productCode, Integer productQuantiy) {
                /*PRODUCT_ITEM_ID_VALUE*/
                getIdFromChild = itemID;
                getPositionValue = positionValue;
                productNameValue = productName;
                productCodeValue = productCode;
                productQuantityValue = productQuantiy;
                holder.subProdcutRate.setText(value);
                catIma = evenParent.get(position).getCatImage();
                catNam = evenParent.get(position).getName();

                itemClick.onClickParentInter(value, 0, getIdFromChild, getPositionValue, productNameValue, productCodeValue, productQuantityValue, catIma, catNam);


            }


        });

        holder.subProductChild.setAdapter(eventListChildAdapter);


    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return evenParent.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView subProdcutImage;
        TextView subProdcutName, subProdcutRate;
        RecyclerView subProductChild;

        public MyViewHolder(View view) {
            super(view);
            subProdcutImage = (ImageView) itemView.findViewById(R.id.product_header_image);
            subProdcutName = (TextView) itemView.findViewById(R.id.product_header_Name);
            subProdcutRate = (TextView) itemView.findViewById(R.id.product_row_total);
            subProductChild = (RecyclerView) itemView.findViewById(R.id.recycler_child);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    evenParent = eventInfor;
                } else {
                    List<HeaderName> filteredList = new ArrayList<>();
                    for (HeaderName row : eventInfor) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    evenParent = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = evenParent;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Log.e("FILTER_LIST", String.valueOf(filterResults.values));
                mString = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public void filterList(ArrayList<HeaderName> filterdNames) {
        this.eventInfor = filterdNames;
        notifyDataSetChanged();
    }

}