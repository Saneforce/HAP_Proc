package com.hap.checkinproc.SFA_Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity_Hap.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Interface.OnPaymentItemClickListener;
import com.hap.checkinproc.Model_Class.PaymentModel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.VanSalPaymentActivity;
import com.hap.checkinproc.SFA_Activity.VanSalePaymentNewActivity;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder>{
    Context context;
    List<PaymentModel> modelList;
    double rec_amt=0;
    int collectPaymentId=0;
    NumberFormat formatter = new DecimalFormat("##0.00");
    OnPaymentItemClickListener onPaymentItemClickListener;
    private DatePickerDialog fromDatePickerDialog;
    public static final int RECEIVE_AMT= 12;
    public static final int PAY_DATE = 7;
    public static final int PAY_MODE = 10;
    public void setCollectPaymentId(int collectPaymentId) {this.collectPaymentId = collectPaymentId;}

    public void setPaymentList(List<PaymentModel>paymentModelList){
        modelList=paymentModelList;
        notifyDataSetChanged();
    }


    public PaymentAdapter(Context context, List<PaymentModel> paymentModelList, OnPaymentItemClickListener onPaymentItemClickListener1){
        this.context=context;
        modelList=paymentModelList;
        onPaymentItemClickListener=onPaymentItemClickListener1;

    }
    public double getRec_amt() {return rec_amt;}

    public void setRec_amt(double rec_amt) {this.rec_amt = rec_amt;}

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_payment,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PaymentModel paymentModel=modelList.get(position);

        if(collectPaymentId==1){
            holder.tv_amt_label.setVisibility(View.GONE);
            holder.et_amt.setVisibility(View.GONE);
            holder.tv_paid_amt_label.setVisibility(View.VISIBLE);
            holder.tv_paid_amt.setVisibility(View.VISIBLE);
            holder.tv_paid_amt.setText(String.valueOf(formatter.format(paymentModel.getAmt())));
        }else{
            holder.tv_amt_label.setVisibility(View.VISIBLE);
            holder.et_amt.setVisibility(View.VISIBLE);
            holder.tv_paid_amt_label.setVisibility(View.GONE);
            holder.tv_paid_amt.setVisibility(View.GONE);

        }
        //Log.e("scasdccccdddd"+position,""+modelList.get(position).toString());
        holder.tv_bill_no.setText(paymentModel.getBillNo());
        holder.tv_bill_date.setText(paymentModel.getBillDate());
        holder.tv_billed_amt.setText(String.valueOf(paymentModel.getBilledAmt()));
        holder.tv_pending_amt.setText(String.valueOf(formatter.format(paymentModel.getPendingAmt())));
        Log.e(paymentModel.getBillNo(),""+paymentModel.getAmt());

        holder.tv_pay_mode.setText(paymentModel.getPayMode());
        if(paymentModel.getPayDate()!=null) {
            holder.tv_pay_date.setText(Common_Class.GetDateOneFormatToOther(paymentModel.getPayDate()));
        }
        holder.tv_balance_amt.setText(String.valueOf(formatter.format(paymentModel.getBalAmt())));


        holder.et_amt.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {
              try {
                  if (!editable.toString().isEmpty()) {
                      double receiveAmt = Double.parseDouble(editable.toString());
                      if (Double.parseDouble(holder.tv_pending_amt.getText().toString()) >= receiveAmt) {
                          modelList.get(holder.getAdapterPosition()).setAmt(receiveAmt);
                          double balAmt = modelList.get(holder.getAdapterPosition()).getPendingAmt() - receiveAmt;

                          modelList.get(position).setBalAmt(Double.valueOf(balAmt));
                          holder.tv_balance_amt.setText("" + formatter.format(balAmt));
                          holder.tv_pay_date.setText(Common_Class.GetDatewothouttimeNew());
                          modelList.get(position).setPayDate(Common_Class.GetDatewothouttime());

                      } else {
                          Toast.makeText(context, "Enter less than or equal to pending amount", Toast.LENGTH_SHORT).show();
                          holder.et_amt.setText("");
                          holder.tv_balance_amt.setText(String.valueOf(formatter.format(modelList.get(position).getPendingAmt())));
                          holder.tv_pay_date.setText("");
                          modelList.get(position).setBalAmt(0.0);
                          modelList.get(position).setPayDate("");
                      }
                  }
              }catch (Exception e){
                  Log.e("fcgvhbjnk",e.getMessage());
              }
          }
      });
        if(paymentModel.getAmt()>0) {
            holder.et_amt.setText(String.valueOf(formatter.format(paymentModel.getAmt())));
        }
        Log.e("scasdcccceeee"+position,""+modelList.get(position).toString());

       /* List<String> list = new ArrayList<String>();
        list.add("Cash");
        list.add("UPI");
        list.add("Cheque");

        holder.sp_pay_mode.setAdapter(new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, list));
        holder.sp_pay_mode.setSelection(modelList.get(position).getSpinPos());
        holder.sp_pay_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
              paymentModel.setPayMode(item);
              paymentModel.setSpinPos(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                paymentModel.setPayMode("Nil");
            }
        });*/
        holder.tv_pay_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPaymentItemClickListener.onClickProduct(holder.getAdapterPosition(),paymentModel,PAY_MODE);
            }
        });
        holder.tv_pay_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showDatePickerDialog(holder.tv_pay_date,paymentModel);
                int day, month, year;
                if (!holder.tv_pay_date.getText().toString().equals("")) {
                    String[] dateArray = holder.tv_pay_date.getText().toString().split("-");
                    day = Integer.parseInt(dateArray[0]);
                    month = Integer.parseInt(dateArray[1]) - 1;
                    year = Integer.parseInt(dateArray[2]);
                } else {
                    Calendar c = Calendar.getInstance();

                    day = c.get(Calendar.DAY_OF_MONTH);
                    month = c.get(Calendar.MONTH);
                    year = c.get(Calendar.YEAR);
                }
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String _pickedDate = _date + "-" + _month + "-" + _year;
                        Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                        // currentDate = _date +"/"+_month+"/"+_year;
                        String pay_date = _year + "-" + _month + "-" + _date;
                        holder.tv_pay_date.setText(_pickedDate);
                        paymentModel.setPayDate(pay_date);

                    }
                }, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

                Calendar calendarmin = Calendar.getInstance();
                String[] dateParts = paymentModel.getBillDate().split("/");
                String minyear = dateParts[2];
                String minmonth = dateParts[1];
                String minday = dateParts[0];
                calendarmin.set(Integer.parseInt(minyear), Integer.parseInt(minmonth) - 1, Integer.parseInt(minday));
                dialog.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bill_no,tv_bill_date,tv_billed_amt,tv_pending_amt,tv_amt_label,tv_paid_amt,tv_paid_amt_label,tv_pay_date,tv_pay_mode,tv_balance_amt;
        EditText et_amt;
        LinearLayout layout;
       // Spinner sp_pay_mode;


        public MyViewHolder(View itemView) {
            super(itemView);


            tv_bill_no=itemView.findViewById(R.id.tv_bill_no);
            tv_bill_date=itemView.findViewById(R.id.tv_bill_date);
            tv_billed_amt=itemView.findViewById(R.id.tv_billed_Amt);
            tv_pending_amt=itemView.findViewById(R.id.tv_pending_Amt);
            et_amt=itemView.findViewById(R.id.et_amt);
            tv_amt_label=itemView.findViewById(R.id.tv_amt_label);
            tv_paid_amt=itemView.findViewById(R.id.tv_paid_Amt);
            tv_paid_amt_label=itemView.findViewById(R.id.tv_paid_amt_label);
            tv_pay_date=itemView.findViewById(R.id.tv_pay_date);
           // sp_pay_mode=itemView.findViewById(R.id.sp_pay_mode);
            tv_pay_mode=itemView.findViewById(R.id.tv_pay_mode);
            tv_balance_amt=itemView.findViewById(R.id.tv_balance_amt);
        }
    }
    void showDatePickerDialog(TextView textView,PaymentModel paymentModel) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context.getApplicationContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String date = ("" + year + "-" + month + "-" + dayOfMonth);
                paymentModel.setPayDate(date);
                String datenew=(""+dayOfMonth+"-"+month+"-"+year);
                textView.setText("Date : " +datenew);


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public PaymentModel getPaymentData(int position){

        Log.e("scasdc",""+modelList.get(position).toString());
        Log.e("scasdcccc",""+modelList.toString());
        return modelList.get(position) ;
    }
    public void updateCurrentObject(PaymentModel paymentModel) {

        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i).getBillNo().equals(paymentModel.getBillNo())) {
                modelList.set(i, paymentModel);
            }
        }
        PaymentAdapter.this.notifyDataSetChanged();
    }

    /*public void setRecAmt(Double recAmt){
        double rcamt=recAmt;
        printUsrLog("")
        for (int i=0;i<modelList.size();i++){
            if(modelList.get(i).getPendingAmt()<rcamt){
                double amt=rcamt-modelList.get(i).getPendingAmt();
                modelList.get(i).setAmt(modelList.get(i).getPendingAmt());
                rcamt=amt;
            }else{
                modelList.get(i).setAmt(recAmt);
            }
        }
    }*/
}
