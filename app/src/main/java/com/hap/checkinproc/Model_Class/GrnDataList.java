package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import java.util.List;

public class GrnDataList {
    @SerializedName("Data")
    @Expose
    private List<OutletReport_View_Modal> data = null;

    public List<OutletReport_View_Modal> getData() {
        return data;
    }

    public void setData(List<OutletReport_View_Modal> data) {
        this.data = data;
    }
}
