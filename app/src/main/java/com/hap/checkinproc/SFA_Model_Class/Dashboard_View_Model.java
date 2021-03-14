package com.hap.checkinproc.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dashboard_View_Model {

    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("CountAll")
    @Expose
    private String CountAll;
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCountAll() {
        return CountAll;
    }

    public void setCountAll(String countAll) {
        CountAll = countAll;
    }


}
