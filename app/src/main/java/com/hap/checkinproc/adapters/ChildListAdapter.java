package com.hap.checkinproc.adapters;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.hap.checkinproc.Interface.ChildListInterface;
import com.hap.checkinproc.Model_Class.Product;
import com.hap.checkinproc.Model_Class.Product_Array;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.MyViewHolder> {

    private List<Product> eventsArrayList;
    private Activity activity;
    ArrayList<Integer> stringValue;
    private long intSum;
    private ChildListInterface itemClick;
    Integer subTotalRate = 0;
    Integer editValue = 0;
    String getItemID, productNameValue, productCodeValue;
    Integer productQuantityValue;
    String listItemId;
    Integer getPositionValue = 0;
    ArrayList<Product_Array> dataValue;
    Product_Array product_array;
    ArrayList<String> productNameList;
    ArrayList<String> productCodeList;
    ArrayList<String> productQuantityList;

    public ChildListAdapter(Activity activity, List<Product> eventsArrayList, ChildListInterface itemClick) {
        this.eventsArrayList = eventsArrayList;
        this.activity = activity;
        this.itemClick = itemClick;
        stringValue = new ArrayList<>();
        productCodeList = new ArrayList<>();
        productNameList = new ArrayList<>();
        productQuantityList = new ArrayList<>();
        dataValue = new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_child_sub_category, null, false);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemClick.onClickInterface(String.valueOf(intSum), 0, listItemId, getPositionValue, productNameValue, productCodeValue, productQuantityValue);

            }
        });
        listItem.setClickable(false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildListAdapter.MyViewHolder holder, int position) {


        Log.e("KARTHIC", String.valueOf(eventsArrayList.get(position).getProductCatCode().toString().length()));


        Product events = eventsArrayList.get(position);
        holder.subProdcutChildName.setText(eventsArrayList.get(position).getName());
        holder.subProdcutChildRate.setText("Rs:" + eventsArrayList.get(position).getProductCatCode() + ".00");
        /*   holder.productEdt.setText("" + events.getmQuantity());*/


        holder.productEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (holder.productEdt.getText().toString().equals("")) {
                        holder.productEdt.setText("0");
                    }
                } else {
                    if (holder.productEdt.getText().toString().equals("0")) {
                        holder.productEdt.setText("");
                    }
                }
            }
        });

        holder.productEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!holder.productEdt.getText().toString().equals("")) {
                    editValue = Integer.valueOf(holder.productEdt.getText().toString());
                    Log.e("EDIT_VALUE", String.valueOf(editValue));
                    events.setmQuantity(editValue);
                    eventsArrayList.get(position).setmQuantity(Integer.parseInt("" + holder.productEdt.getText()));
                    subTotalRate = eventsArrayList.get(position).getProductCatCode() * events.getmQuantity();


                } else {
                    if (holder.productEdt.getText().toString().equals("0")) {
                        holder.productEdt.setText("");
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                getItemID = eventsArrayList.get(position).getId();
                productCodeValue = String.valueOf(eventsArrayList.get(position).getProductCatCode());
                productQuantityValue = eventsArrayList.get(position).getmQuantity();

                itemClick.onClickInterface(String.valueOf(subTotalRate),
                        0, getItemID, 0, productNameValue, productCodeValue, productQuantityValue);
                Log.e("djfkgsd", "" + String.valueOf(subTotalRate));

            }
        });


        holder.productPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.productEdt.clearFocus();
                events.addToQuantity();
                holder.productEdt.setText("" + events.getmQuantity());
                getItemID = eventsArrayList.get(position).getId();

                Log.e("GET_ID_VALUE", getItemID);
                productNameValue = eventsArrayList.get(position).getName();

                productCodeValue = String.valueOf(eventsArrayList.get(position).getProductCatCode());

                productQuantityValue = eventsArrayList.get(position).getmQuantity();

                if (dataValue.size() == 0) {
                    subTotalRate = productQuantityValue * Integer.parseInt(productCodeValue);

                    dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, productQuantityValue * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));
                    Log.e("Parent_product_value", "" + subTotalRate);
                    Log.e("Parent_product_value", "" + dataValue.size());
                } else {
                    System.out.println("PRODUCT_Array_SIzeElse" + dataValue.size());
                    int Total_Size = dataValue.size();
                    for (int i = 0; i < Total_Size; i++) {
                        product_array = dataValue.get(i);
                        if (getItemID == product_array.getProductcode()) {
                            System.out.println("Product_Code" + getItemID);
                            System.out.println("Existing_Code" + product_array.getProductcode());
                            System.out.println("Position_Count" + i);
                            dataValue.remove(i);
                            System.out.println("PRODUCT_Array_SIZE_REMOVE" + dataValue.size());
                            Total_Size = Total_Size - 1;
                            System.out.println("AlreadyExist" + product_array.getProductcode());
                        }

                    }
                    dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, productQuantityValue * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));

                }


                int sum = 0;

                Log.e("PRODUCT_ARRAY_SIZE", String.valueOf(dataValue));
                for (int i = 0; i < dataValue.size(); i++) {
                    sum = sum + dataValue.get(i).getSampleqty();
                    System.out.println("Final_Name" + dataValue.get(i).getProductname() + "Qty" + dataValue.get(i).getSampleqty() + "SampleQty" + dataValue.get(i).getSampleqty());
                    Log.e("PARENT_SUM", String.valueOf(sum));

                }
                Log.e("PARENT_TOTAL_SUM", String.valueOf(sum));


                itemClick.onClickInterface(String.valueOf(sum), 0, getItemID, getPositionValue, productNameValue, productCodeValue, productQuantityValue);
                notifyDataSetChanged();
            }
        });

        holder.proudctMinus.setEnabled(true);
        holder.proudctMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.productEdt.clearFocus();
                events.removeFromQuantity();
                holder.productEdt.setText("" + events.getmQuantity());

                getItemID = eventsArrayList.get(position).getId();

                productNameValue = eventsArrayList.get(position).getName();

                productCodeValue = String.valueOf(eventsArrayList.get(position).getProductCatCode());

                productQuantityValue = eventsArrayList.get(position).getmQuantity();

                if (holder.productEdt.getText().toString().equals("0")) {
                    Log.e("ZERO", "ZERO");
                    holder.proudctMinus.setEnabled(false);
                } else {

                    holder.proudctMinus.setEnabled(true);
                    Log.e("ZERO", "NOT ZERO");


                    if (dataValue.size() == 0) {
                        subTotalRate = events.getmQuantity() * Integer.parseInt(productCodeValue);
                        dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, events.getmQuantity() * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));
                        Log.e("Parent_product_value", "" + subTotalRate);
                        Log.e("Parent_product_value", "" + dataValue.size());
                    } else {
                        System.out.println("PRODUCT_Array_SIzeElse" + dataValue.size());
                        int Total_Size = dataValue.size();
                        for (int i = 0; i < Total_Size; i++) {
                            product_array = dataValue.get(i);
                            if (getItemID == product_array.getProductcode()) {
                                System.out.println("Product_Code" + getItemID);
                                System.out.println("Existing_Code" + product_array.getProductcode());
                                System.out.println("Position_Count" + i);
                                dataValue.remove(i);
                                System.out.println("PRODUCT_Array_SIZE_REMOVE" + dataValue.size());
                                Total_Size = Total_Size - 1;
                                System.out.println("AlreadyExist" + product_array.getProductcode());
                            }

                        }
                        dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, events.getmQuantity() * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));

                    }


                    int sum = 0;

                    Log.e("PRODUCT_ARRAY_SIZE", String.valueOf(dataValue));
                    for (int i = 0; i < dataValue.size(); i++) {
                        sum = sum + dataValue.get(i).getSampleqty();
                        System.out.println("Final_Name" + dataValue.get(i).getProductname() + "Qty" + dataValue.get(i).getSampleqty() + "SampleQty" + dataValue.get(i).getSampleqty());
                        Log.e("PARENT_SUM", String.valueOf(sum));

                    }
                    Log.e("PARENT_TOTAL_SUM", String.valueOf(sum));


                    itemClick.onClickInterface(String.valueOf(sum), 0, getItemID, getPositionValue, productNameValue, productCodeValue, productQuantityValue);
                    notifyDataSetChanged();
                }
            }
        });

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
        return eventsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subProdcutChildName, subProdcutChildRate, productCount, productItem;
        LinearLayout proudctMinus, productPlus;

        EditText productEdt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subProdcutChildName = (TextView) itemView.findViewById(R.id.child_product_name);
            subProdcutChildRate = (TextView) itemView.findViewById(R.id.child_product_price);
            productCount = (TextView) itemView.findViewById(R.id.product_count_increment);
            proudctMinus = (LinearLayout) itemView.findViewById(R.id.image_minus);
            productPlus = (LinearLayout) itemView.findViewById(R.id.image_plus);
            productEdt = (EditText) itemView.findViewById(R.id.edt_product_count_inc_dec);
        }
    }
}