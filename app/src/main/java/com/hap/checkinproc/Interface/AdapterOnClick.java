package com.hap.checkinproc.Interface;

public interface AdapterOnClick {

    void onIntentClick(int Name);
    default void CallMobile(String MobileNo){

    };

}
