package com.hap.checkinproc.SFA_Model_Class;

public class ModelPendingOrder {
    String title, address, mobile, title2, orderID, date, products, total, OutletId;

    public ModelPendingOrder(String title, String address, String mobile, String title2, String orderID, String date, String products, String total, String OutletId) {
        this.title = title;
        this.address = address;
        this.mobile = mobile;
        this.title2 = title2;
        this.orderID = orderID;
        this.date = date;
        this.products = products;
        this.total = total;
        this.OutletId = OutletId;
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

    public String getOutletId() {
        return OutletId;
    }
}
