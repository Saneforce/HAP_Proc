package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VanInvTransactionModel {
    @SerializedName("InvoiceNo")
    private String invoiceNo;

    @SerializedName("InvoiceDate")
    private String invoiceDate;

    @SerializedName("InvoiceVal")
    private double invoiceVal;

    @SerializedName("transactionList")
    private List<Transaction> transactionList;

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public double getInvoiceVal() {
        return invoiceVal;
    }

    public void setInvoiceVal(double invoiceVal) {
        this.invoiceVal = invoiceVal;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }




    public class Transaction {

        @SerializedName("RecAmt")
        private double recAmt;

        @SerializedName("PayDate")
        private String payDate;

        @SerializedName("PayMode")
        private String payMode;

        @SerializedName("BalanceAmt")
        private double balanceAmt;

        public double getRecAmt() {
            return recAmt;
        }

        public void setRecAmt(double recAmt) {
            this.recAmt = recAmt;
        }

        public String getPayDate() {
            return payDate;
        }

        public void setPayDate(String payDate) {
            this.payDate = payDate;
        }

        public String getPayMode() {
            return payMode;
        }

        public void setPayMode(String payMode) {
            this.payMode = payMode;
        }

        public double getBalanceAmt() {
            return balanceAmt;
        }

        public void setBalanceAmt(double balanceAmt) {
            this.balanceAmt = balanceAmt;
        }




    }

}
