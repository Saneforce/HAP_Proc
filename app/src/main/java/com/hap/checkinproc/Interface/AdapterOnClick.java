package com.hap.checkinproc.Interface;

import com.google.gson.JsonObject;

public interface AdapterOnClick {

    default void onIntentClick(JsonObject item,int Name){

    };
    default void onIntentClick(int Name){

    };
    default void CallMobile(String MobileNo){

    };

}
