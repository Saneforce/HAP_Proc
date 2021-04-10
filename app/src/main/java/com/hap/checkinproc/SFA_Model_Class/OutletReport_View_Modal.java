package com.hap.checkinproc.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletReport_View_Modal {
    @SerializedName("No_Of_items")
    @Expose
    private String No_Of_items;
    @SerializedName("slno")
    @Expose
    private String slno;
    @SerializedName("OrderNo")
    @Expose
    private String orderNo;
    @SerializedName("Stockist_Code")
    @Expose
    private String stockistCode;
    @SerializedName("Trans_Sl_No")
    @Expose
    private String transSlNo;
    @SerializedName("Outlet_Code")
    @Expose
    private String outletCode;
    @SerializedName("sf_code")
    @Expose
    private String sfCode;
    @SerializedName("Order_Date")
    @Expose
    private String orderDate;
    @SerializedName("Order_Value")
    @Expose
    private Double orderValue;
    @SerializedName("Invoice_Flag")
    @Expose
    private String Invoice_Flag;
    @SerializedName("invoicevalues")
    @Expose
    private String invoicevalues;
    @SerializedName("NetAmount")
    @Expose
    private String NetAmount;
    @SerializedName("Discount_Amount")
    @Expose
    private String Discount_Amount;

    public String getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(String netAmount) {
        NetAmount = netAmount;
    }

    public String getDiscount_Amount() {
        return Discount_Amount;
    }

    public void setDiscount_Amount(String discount_Amount) {
        Discount_Amount = discount_Amount;
    }

    public String getInvoice_Flag() {
        return Invoice_Flag;
    }

    public String getInvoicevalues() {
        return invoicevalues;
    }

    public String getNo_Of_items() {
        return No_Of_items;
    }

    public void setNo_Of_items(String no_Of_items) {
        No_Of_items = no_Of_items;
    }

    public void setInvoicevalues(String invoicevalues) {
        this.invoicevalues = invoicevalues;
    }


    public void setInvoice_Flag(String invoice_Flag) {
        Invoice_Flag = invoice_Flag;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @SerializedName("Status")
    @Expose
    private String Status;

    public OutletReport_View_Modal(String no_Of_items, String slno, String orderNo, String stockistCode, String transSlNo, String outletCode, String sfCode, String orderDate, Double orderValue, String invoice_Flag, String invoicevalues, String netAmount, String discount_Amount, String status) {
        this.No_Of_items = no_Of_items;
        this.slno = slno;
        this.orderNo = orderNo;
        this.stockistCode = stockistCode;
        this.transSlNo = transSlNo;
        this.outletCode = outletCode;
        this.sfCode = sfCode;
        this.orderDate = orderDate;
        this.orderValue = orderValue;
        Invoice_Flag = invoice_Flag;
        this.invoicevalues = invoicevalues;
        this.NetAmount = netAmount;
        this.Discount_Amount = discount_Amount;
        this.Status = status;
    }

    public OutletReport_View_Modal(String slno, String orderNo, String stockistCode, String transSlNo, String outletCode, String sfCode, String orderDate, Double orderValue, String status) {
        this.slno = slno;
        this.orderNo = orderNo;
        this.stockistCode = stockistCode;
        this.transSlNo = transSlNo;
        this.outletCode = outletCode;
        this.sfCode = sfCode;
        this.orderDate = orderDate;
        this.orderValue = orderValue;
        Status = status;
    }

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStockistCode() {
        return stockistCode;
    }

    public void setStockistCode(String stockistCode) {
        this.stockistCode = stockistCode;
    }

    public String getTransSlNo() {
        return transSlNo;
    }

    public void setTransSlNo(String transSlNo) {
        this.transSlNo = transSlNo;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Double orderValue) {
        this.orderValue = orderValue;
    }
}

