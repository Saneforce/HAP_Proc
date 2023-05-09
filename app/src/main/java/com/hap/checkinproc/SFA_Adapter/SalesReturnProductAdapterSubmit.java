package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
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

import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.HAPApp;
import com.hap.checkinproc.SFA_Model_Class.SalesReturnProductModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SalesReturnProductAdapterSubmit extends RecyclerView.Adapter<SalesReturnProductAdapterSubmit.ViewHolder> {
    Context context;
    ArrayList<SalesReturnProductModel> list;

    public SalesReturnProductAdapterSubmit(Context context, ArrayList<SalesReturnProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SalesReturnProductAdapterSubmit.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesReturnProductAdapterSubmit.ViewHolder(LayoutInflater.from(context).inflate(R.layout.sales_return_product_item_two, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReturnProductAdapterSubmit.ViewHolder holder, int position) {
        SalesReturnProductModel model = list.get(holder.getAdapterPosition());
        holder.productName.setText(model.getProductName());
        holder.materialCode.setText("" + model.getMaterialCode());
        holder.MRP.setText(HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(model.getMRP()));
        holder.rate.setText(HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(model.getRate()));
        holder.invUOM.setText(model.getInvUOM());
        holder.invQty.setText(String.valueOf(model.getInvQty()));
        holder.retUOM.setText(model.getRetUOM());
        holder.retQty.setText(String.valueOf(model.getRetQty()));
        String value = HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(model.getRetTotal());
        holder.retAmount.setText(value);

        if (model.getRetType().equalsIgnoreCase("")) {
            holder.retType.setText("--- Select ---");
        } else {
            holder.retType.setText(model.getRetType());
        }

        holder.retUOM.setOnClickListener(v -> {

            /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);

            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);

            title.setText("Select UOM");
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UOMAdapter adapter = new UOMAdapter(context, model.getList());
            recyclerView1.setAdapter(adapter);
            AlertDialog dialog = builder.create();

            adapter.setItemSelected((model1, position1) -> {
                holder.retUOM.setText(model1.getUOM_Nm());
                holder.retQty.setText("");

                model.setRetUOM(model1.getUOM_Nm());
                model.setRetConvFac(model1.getCnvQty());
                model.setRetQty(0);

                dialog.dismiss();
            });

            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();*/

        });

        holder.rl_retType.setOnClickListener(v -> {

            /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);

            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);

            title.setText("Select Return Type");
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            ArrayList<String> list = new ArrayList<>();
            list.add("Saleable");
            list.add("Damaged");
            ReturnTypeAdapter adapter = new ReturnTypeAdapter(context, list);
            recyclerView1.setAdapter(adapter);
            AlertDialog dialog = builder.create();

            adapter.setItemSelected((data) -> {
                holder.retType.setText(data);
                model.setRetType(data);
                dialog.dismiss();
            });

            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();*/

        });

        holder.retQtyMinus.setOnClickListener(v -> {
            int retQtyCount = model.getRetQty();
            if (retQtyCount > 0) {
                int newQty = retQtyCount - 1;
                setTotal(holder, model, newQty);
            }
        });

        holder.retQtyPlus.setOnClickListener(v -> {
            int newQty = model.getRetQty() + 1;
            int invItemQty = (int) (model.getInvQty() * model.getInvConvFac());
            int retItemQty = (int) (newQty * model.getRetConvFac());
            if (invItemQty >= retItemQty) {
                setTotal(holder, model, newQty);
            } else {
                Toast.makeText(context, "Return quantity can't exceed the invoice quantity", Toast.LENGTH_SHORT).show();
            }
        });

         /*holder.retQty.setOnClickListener(v -> {
             holder.retQty.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                 }

                 @Override
                 public void onTextChanged(CharSequence s, int start, int before, int count) {
                     try {
                         String data = s.toString();
                         if (!data.equals("")) {
                             int invItemQty = (int) (model.getInvQty() * model.getInvConvFac());
                             int retItemQty = (int) (Integer.parseInt(data) * model.getRetConvFac());
                             if (invItemQty >= retItemQty) {
                                 if (model.getRetQty() != Integer.parseInt(data)) {
                                     setTotal(holder, model, Integer.parseInt(data));
                                 }
                             }
                         } else {
                             setTotal(holder, model, 0);
                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }

                 @Override
                 public void afterTextChanged(Editable s) {

                 }
             });
         });*/
    }

    private void setTotal(ViewHolder holder, SalesReturnProductModel model, int newQty) {
        model.setRetQty(newQty);
        double totalAmt = newQty * model.getRetConvFac() * model.getRate();
        model.setRetTotal(totalAmt);
        holder.retQty.setText(String.valueOf(newQty));
        String currency = "Return Amount: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(totalAmt);
        holder.retAmount.setText(currency);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, materialCode, MRP, rate, invUOM, invQty, retUOM, retType, retAmount, retQty;
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
