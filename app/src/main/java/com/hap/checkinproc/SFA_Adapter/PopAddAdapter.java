package com.hap.checkinproc.SFA_Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.POPActivity;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import java.util.Calendar;
import java.util.List;

public class PopAddAdapter extends RecyclerView.Adapter<PopAddAdapter.MyViewHolder> {
    private List<Product_Details_Modal> Product_Details_Modalitem;
    private int rowLayout;
    private Context context;
    int selectdPos = -1;
    AlertDialog.Builder builder;
    private DatePickerDialog fromDatePickerDialog;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        EditText Qty;
        TextView etBookingDate, etPopMaterial, etUOM;
        RelativeLayout rlOtherBrand;
        ImageView rlDeletePOP;


        public MyViewHolder(View view) {
            super(view);
            Qty = view.findViewById(R.id.etQty);
            etUOM = view.findViewById(R.id.tvUOM);
            etPopMaterial = view.findViewById(R.id.etPopMaterial);
            etBookingDate = view.findViewById(R.id.etBookingDate);
            rlDeletePOP = view.findViewById(R.id.rlDeletePOP);


        }
    }


    public PopAddAdapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context) {
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


            holder.etPopMaterial.setText("" + Product_Details_Modal.getName());
            holder.etUOM.setText("" + Product_Details_Modal.getUOM());
            holder.Qty.setText("" + Product_Details_Modal.getQty());
            holder.etBookingDate.setText("" + Product_Details_Modal.getBookingDate());


            holder.Qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().equals(""))
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setQty(0);
                    else
                        Product_Details_Modalitem.get(holder.getAdapterPosition()).setQty(Integer.parseInt(s.toString()));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


//            holder.sku.setText("" + Product_Details_Modal.getSku());
//            holder.Qty.setText("" + Product_Details_Modal.getQty());


//            holder.Qty.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void onTextChanged(CharSequence charSequence, int start,
//                                          int before, int count) {
//
//
//                    if (!charSequence.toString().equals("")) {
//                        holder.Amount.setText("₹ " + Integer.parseInt(holder.Qty.getText().toString()) *
//                                Integer.parseInt(holder.etPrice.getText().toString()));
//
//
//                        Double amount = Double.valueOf(Integer.parseInt(holder.Qty.getText().toString()) *
//                                Integer.parseInt(holder.etPrice.getText().toString()));
//
//                        Product_Details_Modalitem.get(position).setQty(Integer.valueOf(charSequence.toString()));
//                        Product_Details_Modalitem.get(position).setAmount(amount);
//
//                    } else {
//                        holder.Amount.setText("₹ 0");
//                        Product_Details_Modalitem.get(position).setQty(0);
//                        Product_Details_Modalitem.get(position).setAmount(0.0);
//
//                    }
//
//
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start,
//                                              int count, int after) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//
//
            holder.etPopMaterial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        selectdPos = position;
                        POPActivity.popActivity.showBrandDialog(position);


                    } catch (Exception e) {
                        Log.e("otherbrandAdapter: ", e.getMessage());
                    }
                }
            });
//
//            holder.etPrice.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                    if (!charSequence.toString().equals("")) {
//                        holder.Amount.setText("₹ " + Integer.parseInt(holder.Qty.getText().toString()) *
//                                Integer.parseInt(holder.etPrice.getText().toString()));
//
//                        Double amount = Double.valueOf(Integer.parseInt(holder.Qty.getText().toString()) *
//                                Integer.parseInt(holder.etPrice.getText().toString()));
//                        Product_Details_Modalitem.get(position).setAmount(amount);
//                        Product_Details_Modalitem.get(position).setPrice(Integer.parseInt(charSequence.toString()));
//
//                    } else {
//                        holder.Amount.setText("₹ 0");
//                        holder.etPrice.setHint("0");
//                        Product_Details_Modalitem.get(position).setPrice(0);
//                        Product_Details_Modalitem.get(position).setAmount(0.0);
//
//                    }
//
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//
//            holder.Free.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//                    Product_Details_Modalitem.get(position).setScheme((charSequence.toString()));
//
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//
//            holder.sku.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//                    Product_Details_Modalitem.get(position).setSku((charSequence.toString()));
//
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//

            holder.etBookingDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar newCalendar = Calendar.getInstance();
                    fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            holder.etBookingDate.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);

                            Product_Details_Modalitem.get(holder.getAdapterPosition()).setBookingDate(holder.etBookingDate.getText().toString());
                        }
                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    fromDatePickerDialog.show();
                }
            });

            holder.rlDeletePOP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);


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
        builder.setMessage("Do you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Product_Details_Modalitem.remove(pos);
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
        alert.show();
    }


}

