package com.hap.checkinproc.SFA_Adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QPS_Modal {
    public QPS_Modal(String sNo, String requestNo, String gift, String bookingDate, String duration, String receivedDate, String Status) {
        this.sNo = sNo;
        this.requestNo = requestNo;
        this.gift = gift;
        this.bookingDate = bookingDate;
        this.duration = duration;
        this.receivedDate = receivedDate;
        this.Status = Status;
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

    @SerializedName("Status")
    @Expose
    private String Status;


    public QPS_Modal(String filePath, String fileName, String fileKey) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileKey = fileKey;
    }

    private String filePath;
    private String fileName;
    private String fileKey;


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


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
