package com.hap.checkinproc.SFA_Model_Class;

public class ModelApproveOutlets {
    String customerName;
    String customerID;
    String customerMobile;
    String customerAddress;
    String listedDrCode;



    String customerCreatedDate;

    public ModelApproveOutlets(String customerName, String customerID, String customerMobile, String customerAddress, String listedDrCode,String customerCreatedDate) {
        this.customerName = customerName;
        this.customerID = customerID;
        this.customerMobile = customerMobile;
        this.customerAddress = customerAddress;
        this.listedDrCode = listedDrCode;
        this.customerCreatedDate=customerCreatedDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getListedDrCode() {
        return listedDrCode;
    }

    public void setListedDrCode(String listedDrCode) {
        this.listedDrCode = listedDrCode;
    }

    public String getCustomerCreatedDate() {return customerCreatedDate;}

    public void setCustomerCreatedDate(String customerCreatedDate) {this.customerCreatedDate = customerCreatedDate;}
}
