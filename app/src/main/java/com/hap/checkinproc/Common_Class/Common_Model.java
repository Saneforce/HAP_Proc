package com.hap.checkinproc.Common_Class;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public class Common_Model {
    private String name;
    private String id;
    private String flag;

    private JsonObject jsonObject;
    private JSONObject JSONobject;

    private String address;
    private String phone;

    String checkouttime;
    boolean ExpNeed;
    boolean isSelected;
    private Integer Pho;
    private String cont;

    private Integer MaxDays;


    public Common_Model(String name, String id, String flag, String address, String phone) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.address = address;
        this.phone = phone;
    }

    public Common_Model(String name, String id, String flag, String address, Integer phone) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.address = address;
        this.Pho = phone;
    }

    public Common_Model(String name, String id, String flag, String address, String phone, String cont) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.address = address;
        this.phone = phone;
        this.cont = cont;
    }

    public Common_Model(String id, String name, String flag, String checkouttime, Boolean ExpNeed) {
        this.id = id;
        this.name = name;
        this.flag = flag;
        this.checkouttime = checkouttime;
        this.ExpNeed = ExpNeed;
    }

    public Common_Model(String id, String name, JsonObject jsonObject) {
        this.id = id;
        this.name = name;
        this.jsonObject = jsonObject;
    }

    public Common_Model(String id, String name, JSONObject jsonObject) {
        this.id = id;
        this.name = name;
        this.JSONobject = jsonObject;
    }

    public Common_Model(String id, String name, String flag, String checkouttime) {
        this.id = id;
        this.name = name;
        this.flag = flag;
        this.checkouttime = checkouttime;
    }

    public Common_Model(String name, String id, boolean isSelected) {
        this.name = name;
        this.id = id;
        this.isSelected = isSelected;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public void setCheckouttime(String checkouttime) {
        this.checkouttime = checkouttime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Common_Model(String id, String name, String flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    public Common_Model(String id, String name, String flag, Integer MaxDays) {
        this.id = id;
        this.name = name;
        this.flag = flag;
        this.MaxDays = MaxDays;
    }

    public Common_Model(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Common_Model(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getFlag() {
        return flag;
    }


    public Integer getMaxDays() {
        return MaxDays;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public boolean getExpNeed() {
        return ExpNeed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPho() {
        return Pho;
    }

    public void setPho(Integer pho) {
        Pho = pho;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public JSONObject getJSONObject() {
        return JSONobject;
    }
}
