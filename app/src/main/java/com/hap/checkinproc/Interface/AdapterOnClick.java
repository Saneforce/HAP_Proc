package com.hap.checkinproc.Interface;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public interface AdapterOnClick {

    default void onIntentClick(JsonObject item,int Name){

    };

    default void onIntentClick(JSONObject item, int Name){

    };
    default void onIntentClick(int Name){

    };
    default void CallMobile(String MobileNo){

    };

}
