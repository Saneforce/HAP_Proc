package com.hap.checkinproc.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.viewProduct;
import com.hap.checkinproc.Model_Class.Product_Array;
import com.hap.checkinproc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.MyViewHolder> {

    Integer editCountValue = 0, quntaity, price, subTotalRate = 0, deletePosition;
    List<Product_Array> mProduct_arrays;
    viewProduct viewProd;
    Context context;
    AlertDialog.Builder builder;
    String productname;
    String catName;
    String catImg;
    String productID;
    String productcode;
    Integer productqty;
    Integer productRate;
    Product_Array newProductArray;
    ArrayList<Product_Array> Product_Array_List;

    public CustomViewAdapter(Context context, List<Product_Array> mProduct_arrays, viewProduct viewProd) {
        this.context = context;
        this.viewProd = viewProd;
        this.mProduct_arrays = mProduct_arrays;
        Product_Array_List = new ArrayList<Product_Array>();
    }


    @NonNull
    @Override
    public CustomViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_item_viewcart, null, false);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProd.onViewItemClick(productID, productname, catName, catImg, productqty, productRate);
            }
        });

        return new CustomViewAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(CustomViewAdapter.MyViewHolder holder, int position) {

        Product_Array mProductArray = mProduct_arrays.get(position);
        int positions = holder.getAdapterPosition();
        if (!mProductArray.getProductqty().toString().equals("0")) {

            quntaity = Integer.valueOf(mProduct_arrays.get(position).getProductqty());
            price = Integer.valueOf(mProduct_arrays.get(position).getProductRate());

            productqty = Integer.valueOf(mProduct_arrays.get(position).getProductqty());
            holder.txtName.setText(mProductArray.getProductname());
            holder.txtCatName.setText(mProductArray.getCatName());
            holder.txtPrice.setText("Total :" + mProductArray.getProductRate());
            holder.txtQty.setText("Qty :" + mProductArray.getProductqty());
            holder.totalAmount.setText("Total :" + mProduct_arrays.get(position).getProductqty() * mProduct_arrays.get(position).getProductRate());
            Picasso.with(context).load(mProductArray.getCatImage()).error(R.drawable.no_prod).into(holder.productImage);
            holder.editCount.setText("" + quntaity);
            holder.editCount.setSelection(holder.editCount.getText().length());
            holder.editCount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!holder.editCount.getText().toString().equals("")) {

                        /*Inteface Sending Value*/
                        productID = mProductArray.getProductcode();
                        productname = mProductArray.getProductname();
                        productcode = mProductArray.getProductcode();
                        productRate = mProductArray.getProductRate();
                        catName = mProductArray.getCatName();
                        catImg = mProductArray.getCatImage();
                        editCountValue = Integer.valueOf(holder.editCount.getText().toString());
                        productqty = editCountValue;
                        holder.txtQty.setText("Qty :" + editCountValue);
                        subTotalRate = editCountValue * price;
                        holder.totalAmount.setText("Total :" + subTotalRate);


                        viewProd.onViewItemClick(productID, productname, catName, catImg, productqty, productRate);
                        Log.e("PRODUCT_DETAILS_VALUE", "" + productID + " " + productname + "  " + productqty + " " + productRate);

                    } else {
                        holder.totalAmount.setText("Total :" + 0);
                        holder.txtQty.setText("Qty : " + 0
                        );
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogBox.showDialog(context, "", "Do you want to delete this product?", "Yes", "No", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {


                            // viewProd.onViewItemClick(productID, productname, catName, catImg, productqty, productRate);
                            mProduct_arrays.remove(position);
                            notifyItemRemoved(positions);
                            notifyItemRangeChanged(positions, mProduct_arrays.size());

                           // Product_Array newProduct = mProduct_arrays.get(position);

                            for (int l = 0; l < mProduct_arrays.size(); l++) {
                                Log.e("PRODUCT_NAME", ""+ mProduct_arrays.get(l).getCatName());
                                Log.e("PRODUCT_NAME",""+ mProduct_arrays.get(l).getProductname());
                                Log.e("PRODUCT_NAME",""+ mProduct_arrays.get(l).getCatImage());
                                Log.e("PRODUCT_NAME", ""+ String.valueOf(mProduct_arrays.get(l).getProductqty()));
                                Log.e("PRODUCT_NAME",""+ String.valueOf(mProduct_arrays.get(l).getProductRate()));
                            }


                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });
                }
            });
        }

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
        return mProduct_arrays.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtQty;
        TextView txtPrice;
        TextView totalAmount;
        TextView txtCatName;
        ImageView productImage;
        EditText editCount;
        ImageView deleteProduct;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.item_name);
            txtQty = (TextView) itemView.findViewById(R.id.item_qty);
            txtPrice = (TextView) itemView.findViewById(R.id.item_price);
            txtCatName = (TextView) itemView.findViewById(R.id.item_product_name);
            totalAmount = (TextView) itemView.findViewById(R.id.total_amount);
            productImage = (ImageView) itemView.findViewById(R.id.image_product);
            editCount = (EditText) itemView.findViewById(R.id.edit_qty);
            deleteProduct = (ImageView) itemView.findViewById(R.id.delete_product);

        }
    }
}