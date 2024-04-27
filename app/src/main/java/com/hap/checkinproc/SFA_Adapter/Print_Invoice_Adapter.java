package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.List;

public class Print_Invoice_Adapter extends RecyclerView.Adapter<Print_Invoice_Adapter.MyViewHolder> {
    Context context;
    List<Product_Details_Modal> mDate;
    String flag="";

    public Print_Invoice_Adapter(Context context, List<Product_Details_Modal> mDate,String flag) {
        this.flag=flag;
        this.context = context;
        this.mDate = mDate;
    }

    @NonNull
    @Override
    public Print_Invoice_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.print_invoice_recyclerview, null, false);
        return new Print_Invoice_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(Print_Invoice_Adapter.MyViewHolder holder, int position) {
        try {
            Product_Details_Modal pm=mDate.get(position);
            holder.productname.setText("" + pm.getName());
            holder.productqty.setText("" + pm.getQty());
            holder.productUOM.setText("" + pm.getUnitCode());
            holder.productrate.setText("" + new DecimalFormat("##0.00").format(pm.getRate()));
            holder.producttotal.setText("" + new DecimalFormat("##0.00").format(pm.getQty()*pm.getRate()));
            //holder.producttotal.setText("" + new DecimalFormat("##0.00").format(pm.getAmount()));

            if(flag.equalsIgnoreCase("PROJECTION")){
                holder.llUom.setVisibility(View.GONE);
                holder.llPrice.setVisibility(View.GONE);
                holder.llTot.setVisibility(View.GONE);
            }else if(flag.equalsIgnoreCase("Order")||flag.equalsIgnoreCase("INVOICE")||flag.equalsIgnoreCase("VANSALES")){
                holder.llUom.setVisibility(View.GONE);
                holder.llproductqtyPcs.setVisibility(View.VISIBLE);
                holder.llmrp.setVisibility(View.VISIBLE);
                holder.llhsncode.setVisibility(View.VISIBLE);
                holder.tvhsncode.setText(""+pm.getHSNCode());
                holder.tvmrp.setText(""+Double.parseDouble(pm.getMRP())*Double.parseDouble(pm.getConversionFactor()));
               // Log.e("pcs",""+pm.getQty()*Double.parseDouble(pm.getConversionFactor()));
                holder.tvproductqtyPcs.setText(""+(pm.getQty()*Double.parseDouble(pm.getConversionFactor())));
                String prodName=(pm.getDiscount()>0? pm.getName()+ "\n"+"(Discount Price Rs."+ new DecimalFormat("##0.00").format(pm.getDiscount())+")":pm.getName());
                holder.productname.setText("" +prodName);
               // holder.productname.setText("" + pm.getName()+ "\n"+"(Discount Price Rs."+ new DecimalFormat("##0.00").format(pm.getDiscount())+")");
            }else if(flag.equalsIgnoreCase("POS INVOICE")){
                holder.llUom.setVisibility(View.GONE);
                holder.llproductqtyPcs.setVisibility(View.VISIBLE);
                holder.llmrp.setVisibility(View.VISIBLE);
                holder.llhsncode.setVisibility(View.VISIBLE);
                double val = (100 + (pm.getTaxPer())) / 100;
                double rateValue = Double.parseDouble(new DecimalFormat("##0.00").format(pm.getRate() / val));
                double amtValue = Double.parseDouble(new DecimalFormat("##0.00").format(pm.getQty() * (pm.getRate() / val)));
                holder.tvmrp.setText(""+Double.parseDouble(pm.getMRP())*Double.parseDouble(pm.getConversionFactor()));
                String prodName=(pm.getDiscount()>0? pm.getName()+ "\n"+"(Discount Price Rs."+ new DecimalFormat("##0.00").format(pm.getDiscount())+")":pm.getName());
                holder.productname.setText("" +prodName);
                holder.productrate.setText("" + new DecimalFormat("##0.00").format(rateValue));
                holder.producttotal.setText("" + new DecimalFormat("##0.00").format(amtValue));
                holder.tvhsncode.setText(""+pm.getHSNCode());
                holder.tvproductqtyPcs.setText(""+(pm.getQty()*Double.parseDouble(pm.getConversionFactor())));

            }


//            holder.productname.setText("" + mDate.getJSONObject(position).getString("Product_Name"));
//            holder.productqty.setText("" + mDate.getJSONObject(position).getInt("Quantity"));
//            holder.productUOM.setText("" + mDate.getJSONObject(position).getString("UOM"));
//            holder.productrate.setText("" + new DecimalFormat("##0.00").format(mDate.getJSONObject(position).getDouble("Rate")));
//            holder.producttotal.setText("" + new DecimalFormat("##0.00").format(mDate.getJSONObject(position).getDouble("value")));
        } catch (Exception e) {
            Log.e("error",e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productname, productqty, productrate, producttotal, productUOM,tvhsncode,tvmrp,tvproductqtyPcs;
        LinearLayout llUom,llPrice,llTot,llproductqtyPcs,llmrp,llhsncode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productname = itemView.findViewById(R.id.productname);
            productqty = itemView.findViewById(R.id.productqty);
            productrate = itemView.findViewById(R.id.productrate);
            producttotal = itemView.findViewById(R.id.producttotal);
            productUOM = itemView.findViewById(R.id.productUom);
            tvhsncode=itemView.findViewById(R.id.tvhsncode);
            tvmrp=itemView.findViewById(R.id.tvmrp);
            tvproductqtyPcs=itemView.findViewById(R.id.tvproductqtyPcs);

            llUom=itemView.findViewById(R.id.llUOM);
            llPrice=itemView.findViewById(R.id.llPrice);
            llTot=itemView.findViewById(R.id.llTot);
            llproductqtyPcs=itemView.findViewById(R.id.llproductqtyPcs);
            llmrp=itemView.findViewById(R.id.llmrp);
            llhsncode=itemView.findViewById(R.id.llhsncode);



        }
    }
}