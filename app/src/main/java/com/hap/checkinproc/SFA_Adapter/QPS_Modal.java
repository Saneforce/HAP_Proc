package com.hap.checkinproc.SFA_Adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QPS_Modal {
    public QPS_Modal(String sNo, String requestNo, String gift, String bookingDate, String duration, String receivedDate) {
        this.sNo = sNo;
        this.requestNo = requestNo;
        this.gift = gift;
        this.bookingDate = bookingDate;
        this.duration = duration;
        this.receivedDate = receivedDate;
    }

    @SerializedName("id")
    @Expose
    private String sNo;

    @SerializedName("requestNo")
    @Expose
    private String requestNo;
    @SerializedName("gift")
    @Expose
    private String gift;
    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;
    @SerializedName("duration")
    @Expose
    private String duration;

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    @SerializedName("receivedDate")
    @Expose
    private String receivedDate;


}
