package com.hap.checkinproc.SFA_Activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import java.util.List;

public class OtherBrandAdapter extends RecyclerView.Adapter<OtherBrandAdapter.MyViewHolder> {
    private List<Product_Details_Modal> Product_Details_Modalitem;
    private int rowLayout;
    private Context context;
    int selectdPos = -1;
    AlertDialog.Builder builder;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productname, Rate, Amount, Disc, lblAddQty;

        EditText Qty, etPrice, Free, sku;
        RelativeLayout rlOtherBrand;

        ImageView rlDeleteBrand;


        public MyViewHolder(View view) {
            super(view);
            productname = view.findViewById(R.id.productname);
            Qty = view.findViewById(R.id.Qty);
            etPrice = view.findViewById(R.id.etPrice);
            Amount = view.findViewById(R.id.Amount);
            Free = view.findViewById(R.id.Free);
            sku = view.findViewById(R.id.sku);
            rlDeleteBrand = view.findViewById(R.id.rlDeleteBrand);
            rlOtherBrand = view.findViewById(R.id.rlOtherBrand);


        }
    }


    public OtherBrandAdapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context) {
        this.Product_Details_Modalitem = Product_Details_Modalitem;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }


    public void notifyData(List<Product_Details_Modal> Product_Details_Modalitem) {
        this.Product_Details_Modalitem = Product_Details_Modalitem;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {

            builder = new AlertDialog.Builder(context);

            Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(position);


            holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
            holder.Amount.setText("₹ " + Product_Details_Modal.getAmount());
            holder.etPrice.setText("" + Product_Details_Modal.getPrice());
            holder.Free.setText("" + Product_Details_Modal.getScheme());
            holder.sku.setText("" + Product_Details_Modal.getSku());
            holder.Qty.setText("" + Product_Details_Modal.getQty());


            holder.Qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence charSequence, int start,
                                          int before, int count) {


                    if (!charSequence.toString().equals("")) {

                        int price = 0;


                        if (!holder.etPrice.getText().toString().equals(""))
                            price = Integer.parseInt(holder.etPrice.getText().toString());


                        holder.Amount.setText("₹ " + (Integer.parseInt(holder.Qty.getText().toString()) *
                                price));


                        Double amount = Double.valueOf(Integer.parseInt(holder.Qty.getText().toString()) *
                                price);

                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setQty(Integer.valueOf(charSequence.toString()));
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount(amount);

                    } else {
                        holder.Amount.setText("₹ 0");
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setQty(0);
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount(0.0);

                    }


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            holder.rlOtherBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        selectdPos = position;
                        OtherBrandActivity.otherBrandActivity.showBrandDialog(position);


                    } catch (Exception e) {
                        Log.e("otherbrandAdapter: ", e.getMessage());
                    }
                }
            });

            holder.etPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (!charSequence.toString().equals("")) {
                        int qty = 0;

                        if (!holder.Qty.getText().toString().equals(""))
                            qty = Integer.parseInt(holder.Qty.getText().toString());


                        holder.Amount.setText("₹ " + (qty * Integer.parseInt(holder.etPrice.getText().toString())));

                        Double amount = Double.valueOf(qty *
                                Integer.parseInt(holder.etPrice.getText().toString()));
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount(amount);
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setPrice(Integer.parseInt(charSequence.toString()));

                    } else {
                        holder.Amount.setText("₹ 0");
                        holder.etPrice.setHint("0");
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setPrice(0);
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setAmount(0.0);

                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.Free.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                    Product_Details_Modalitem.get(holder.getAdapterPosition()).setScheme((charSequence.toString()));


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.sku.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                    if (charSequence.toString().equals("")) {

                    } else {
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setSku((charSequence.toString()));
                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.rlDeleteBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(holder.getAdapterPosition());

                }
            });
        } catch (Exception e) {
            Log.e("OTHERBRAND_Adapter ", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return Product_Details_Modalitem.size();
    }


    private void deleteItem(int pos) {

        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage("").setTitle("");

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to delete this brand ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Product_Details_Modalitem.remove(pos);

                        if (Product_Details_Modalitem.size() == 0) {
                            Product_Details_Modalitem.add(new Product_Details_Modal("", "Select the Other Brand", "", 0, 0, 0, ""));
                        }

                        notifyDataSetChanged();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("AlertDialogExample");
        alert.show();
    }


}

