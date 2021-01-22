package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModeOfTravel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("StEndNeed")
    @Expose
    private Integer stEndNeed;
    @SerializedName("DriverNeed")
    @Expose
    private Integer driverNeed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStEndNeed() {
        return stEndNeed;
    }

    public void setStEndNeed(Integer stEndNeed) {
        this.stEndNeed = stEndNeed;
    }

    public Integer getDriverNeed() {
        return driverNeed;
    }

    public void setDriverNeed(Integer driverNeed) {
        this.driverNeed = driverNeed;
    }

}
