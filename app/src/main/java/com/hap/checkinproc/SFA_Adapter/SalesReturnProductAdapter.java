package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.HAPApp;
import com.hap.checkinproc.SFA_Model_Class.SalesReturnProductModel;
import com.hap.checkinproc.SFA_Model_Class.TaxModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SalesReturnProductAdapter extends RecyclerView.Adapter<SalesReturnProductAdapter.ViewHolder> {
    Context context;
    ArrayList<SalesReturnProductModel> list;
    Common_Class common_class;
    CalculateTotal calculateTotal;

    boolean isUpdating;

    public SalesReturnProductAdapter(Context context, ArrayList<SalesReturnProductModel> list) {
        this.context = context;
        this.list = list;
        this.isUpdating = false;
        this.common_class = new Common_Class(context);
    }

    public void setCalculateTotal(CalculateTotal calculateTotal) {
        this.calculateTotal = calculateTotal;
    }

    @NonNull
    @Override
    public SalesReturnProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesReturnProductAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.sales_return_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReturnProductAdapter.ViewHolder holder, int pos) {
        final SalesReturnProductModel model = list.get(pos);
        holder.productName.setText(model.getProductName());
        String matCode = "Mat Code: " + model.getMaterialCode();
        holder.materialCode.setText(matCode);
        String mRp = "MRP: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(model.getMRP());
        holder.MRP.setText(mRp);
        String raTe = "Rate: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(model.getRate());
        holder.rate.setText(raTe);
        holder.invUOM.setText(model.getInvUOM());
        holder.invQty.setText(String.valueOf(model.getInvQty()));
        holder.retUOM.setText(model.getRetUOM());
        holder.retQty.setText(String.valueOf(model.getRetQty()));
        String value = "Return Amount: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(model.getRetTotal());
        holder.retAmount.setText(value);
        if (model.getRetType().equalsIgnoreCase("")) {
            String select = "--- Select ---";
            holder.retType.setText(select);
        } else {
            holder.retType.setText(model.getRetType());
        }

        holder.retQtyMinus.setOnClickListener(v -> {
            int retQtyCount = list.get(holder.getAdapterPosition()).getRetQty();
            if (retQtyCount > 0) {
                int newQty = retQtyCount - 1;
                holder.retQty.setText(String.valueOf(newQty));
            }
        });

        holder.retQtyPlus.setOnClickListener(v -> {
            int newQty = list.get(holder.getAdapterPosition()).getRetQty() + 1;
            int invItemQty = (int) (list.get(holder.getAdapterPosition()).getInvQty() * list.get(holder.getAdapterPosition()).getInvConvFac());
            int retItemQty = (int) (newQty * list.get(holder.getAdapterPosition()).getRetConvFac());
            if (invItemQty >= retItemQty) {
                holder.retQty.setText(String.valueOf(newQty));
            } else {
                Toast.makeText(context, "Return quantity can't exceed the invoice quantity", Toast.LENGTH_SHORT).show();
            }
        });

        holder.retQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isUpdating) {
                    isUpdating = true;
                    String Qty = "0";
                    if (!s.toString().equals("")) {
                        Qty = s.toString();
                    }
                    int newQty = Integer.parseInt(Qty);
                    if (newQty != list.get(holder.getAdapterPosition()).getRetQty()) {
                        int invItemQty = (int) (list.get(holder.getAdapterPosition()).getInvQty() * list.get(holder.getAdapterPosition()).getInvConvFac());
                        int retItemQty = (int) (newQty * list.get(holder.getAdapterPosition()).getRetConvFac());
                        if (invItemQty >= retItemQty) {
                            list.get(holder.getAdapterPosition()).setRetQty(newQty);
                            double taxPercent = 0;
                            for (TaxModel taxModel : list.get(holder.getAdapterPosition()).getTaxList()) {
                                taxPercent += taxModel.getTaxVal();
                            }
                            double totalRate = common_class.formatDecimalToTwoDecimal(newQty * list.get(holder.getAdapterPosition()).getRetConvFac() * list.get(holder.getAdapterPosition()).getRate());
                            double totalTax = common_class.formatDecimalToTwoDecimal((taxPercent / 100 * totalRate));
                            double totalAmt = common_class.formatDecimalToSingleDecimal((totalRate + totalTax));
                            list.get(holder.getAdapterPosition()).setRetTotal(totalAmt);
                            list.get(holder.getAdapterPosition()).setRetTax(totalTax);
                            String currency = "Return Amount: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(totalAmt);
                            holder.retAmount.setText(currency);
                            if (calculateTotal != null) {
                                calculateTotal.onClick();
                            }
                            holder.retQty.setText(String.valueOf(newQty));
                            isUpdating = false;
                        } else {
                            holder.retQty.setText(String.valueOf(list.get(holder.getAdapterPosition()).getRetQty()));
                            isUpdating = false;
                        }
                    } else {
                        isUpdating = false;
                    }
                    holder.retQty.setSelection(holder.retQty.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface CalculateTotal {
        void onClick();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, materialCode, MRP, rate, invUOM, invQty, retUOM, retType, retAmount;
        EditText retQty;
        ImageView retQtyMinus, retQtyPlus;
        RelativeLayout rl_retType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.ProductName);
            materialCode = itemView.findViewById(R.id.MatCode);
            MRP = itemView.findViewById(R.id.MRP);
            rate = itemView.findViewById(R.id.Rate);
            invUOM = itemView.findViewById(R.id.UOM);
            invQty = itemView.findViewById(R.id.invQty);
            retUOM = itemView.findViewById(R.id.retUOM);
            retQty = itemView.findViewById(R.id.retQty);
            retType = itemView.findViewById(R.id.tv_retType);
            retAmount = itemView.findViewById(R.id.retAmount);
            retQtyMinus = itemView.findViewById(R.id.retQtyMns);
            retQtyPlus = itemView.findViewById(R.id.retQtyPlus);
            rl_retType = itemView.findViewById(R.id.rl_retType);
        }
    }
}
