package com.hap.checkinproc.Interface;

import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

public interface UpdateResponseUI {
    void onLoadDataUpdateUI(String apiDataResponse, String key);
    default void onErrorData(String msg){}
}