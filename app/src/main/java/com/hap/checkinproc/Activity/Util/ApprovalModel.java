package com.hap.checkinproc.Activity.Util;

public class ApprovalModel {
    String name,dates,firstdata,seconddata;

    public ApprovalModel(String name, String dates, String firstdata, String seconddata) {
        this.name = name;
        this.dates = dates;
        this.firstdata = firstdata;
        this.seconddata = seconddata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getFirstdata() {
        return firstdata;
    }

    public void setFirstdata(String firstdata) {
        this.firstdata = firstdata;
    }

    public String getSeconddata() {
        return seconddata;
    }

    public void setSeconddata(String seconddata) {
        this.seconddata = seconddata;
    }
}
