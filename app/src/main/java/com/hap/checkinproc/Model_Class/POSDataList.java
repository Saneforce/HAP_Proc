package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import java.util.List;

public class POSDataList {
    @SerializedName("Data")
    @Expose
    private List<Product_Details_Modal> data = null;

    public List<Product_Details_Modal> getData() {
        return data;
    }

    public void setData(List<Product_Details_Modal> data) {
        this.data = data;
    }
}
