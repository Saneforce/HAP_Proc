package com.hap.checkinproc.SFA_Model_Class;

public class ModelPendingOrder {
    String title;
    String address;
    String mobile;
    String title2;
    String orderID;
    String date;
    String products;
    String total;
    String OutletCode;
    String disTotal;
    String outletTyp;

    public ModelPendingOrder(String title, String address, String mobile, String title2, String orderID, String date, String products, String total) {
        this.title = title;
        this.address = address;
        this.mobile = mobile;
        this.title2 = title2;
        this.orderID = orderID;
        this.date = date;
        this.products = products;
        this.total = total;

    }
    public ModelPendingOrder(String ouletcode,String title, String address, String mobile, String title2, String orderID, String date, String products, String total,String distotal,String outletTyp) {
        this.title = title;
        this.address = address;
        this.mobile = mobile;
        this.title2 = title2;
        this.orderID = orderID;
        this.date = date;
        this.products = products;
        this.total = total;
        this.OutletCode=ouletcode;
        this.disTotal=distotal;
        this.outletTyp=outletTyp;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getTitle2() {
        return title2;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getDate() {
        return date;
    }

    public String getProducts() {
        return products;
    }

    public String getTotal() {
        return total;
    }

    public String getOutletCode() { return OutletCode; }

    public void setOutletCode(String outletCode) { OutletCode = outletCode;}

    public String getDisTotal() {return disTotal;}

    public void setDisTotal(String disTotal) {this.disTotal = disTotal;}
    public String getOutletTyp() {return outletTyp;}

    public void setOutletTyp(String outletTyp) {this.outletTyp = outletTyp;}

}
