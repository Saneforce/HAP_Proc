package com.hap.checkinproc.Interface;

public interface UpdateResponseUI {
    void onLoadDataUpdateUI(String apiDataResponse, String key);
    default void onErrorData(String msg){}
}