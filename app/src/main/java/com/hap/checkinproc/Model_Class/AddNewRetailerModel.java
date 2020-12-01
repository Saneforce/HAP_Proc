package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddNewRetailerModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("SPSQl")
    @Expose
    private Object sPSQl;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getSPSQl() {
        return sPSQl;
    }

    public void setSPSQl(Object sPSQl) {
        this.sPSQl = sPSQl;
    }
}
