package com.hap.checkinproc.SFA_Model_Class;

import java.util.ArrayList;

public class FreezerStatusModel {
    String freezerGroup, freezerGroupID, freezerStatus, freezerStatusID, freezerMake, freezerTagNo, freezerCapacity, freezerCapacityID, expectedSalesValue, depositAmount;
    int freezerStatusCnvQty;
    ArrayList<String> photoList;

    public FreezerStatusModel(String freezerGroup, String freezerGroupID, String freezerStatus, String freezerStatusID, String freezerMake, String freezerTagNo, String freezerCapacity, String freezerCapacityID, String expectedSalesValue, String depositAmount, int freezerStatusCnvQty, ArrayList<String> photoList) {
        this.freezerGroup = freezerGroup;
        this.freezerGroupID = freezerGroupID;
        this.freezerStatus = freezerStatus;
        this.freezerStatusID = freezerStatusID;
        this.freezerMake = freezerMake;
        this.freezerTagNo = freezerTagNo;
        this.freezerCapacity = freezerCapacity;
        this.freezerCapacityID = freezerCapacityID;
        this.expectedSalesValue = expectedSalesValue;
        this.depositAmount = depositAmount;
        this.freezerStatusCnvQty = freezerStatusCnvQty;
        this.photoList = photoList;
    }

    public String getFreezerGroup() {
        return freezerGroup;
    }

    public void setFreezerGroup(String freezerGroup) {
        this.freezerGroup = freezerGroup;
    }

    public String getFreezerGroupID() {
        return freezerGroupID;
    }

    public void setFreezerGroupID(String freezerGroupID) {
        this.freezerGroupID = freezerGroupID;
    }

    public String getFreezerStatus() {
        return freezerStatus;
    }

    public void setFreezerStatus(String freezerStatus) {
        this.freezerStatus = freezerStatus;
    }

    public String getFreezerStatusID() {
        return freezerStatusID;
    }

    public void setFreezerStatusID(String freezerStatusID) {
        this.freezerStatusID = freezerStatusID;
    }

    public String getFreezerMake() {
        return freezerMake;
    }

    public void setFreezerMake(String freezerMake) {
        this.freezerMake = freezerMake;
    }

    public String getFreezerTagNo() {
        return freezerTagNo;
    }

    public void setFreezerTagNo(String freezerTagNo) {
        this.freezerTagNo = freezerTagNo;
    }

    public String getFreezerCapacity() {
        return freezerCapacity;
    }

    public void setFreezerCapacity(String freezerCapacity) {
        this.freezerCapacity = freezerCapacity;
    }

    public String getFreezerCapacityID() {
        return freezerCapacityID;
    }

    public void setFreezerCapacityID(String freezerCapacityID) {
        this.freezerCapacityID = freezerCapacityID;
    }

    public String getExpectedSalesValue() {
        return expectedSalesValue;
    }

    public void setExpectedSalesValue(String expectedSalesValue) {
        this.expectedSalesValue = expectedSalesValue;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getFreezerStatusCnvQty() {
        return freezerStatusCnvQty;
    }

    public void setFreezerStatusCnvQty(int freezerStatusCnvQty) {
        this.freezerStatusCnvQty = freezerStatusCnvQty;
    }

    public ArrayList<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<String> photoList) {
        this.photoList = photoList;
    }
}
