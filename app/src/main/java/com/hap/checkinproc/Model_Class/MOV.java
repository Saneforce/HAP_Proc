
package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MOV {

    @SerializedName("MorderSum")
    @Expose
    private Object morderSum;

    public Object getMorderSum() {
        return morderSum;
    }

    public void setMorderSum(Object morderSum) {
        this.morderSum = morderSum;
    }

}
