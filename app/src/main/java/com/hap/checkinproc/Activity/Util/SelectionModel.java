package com.hap.checkinproc.Activity.Util;

import java.util.ArrayList;

public class SelectionModel {
    String txt;
    boolean isClick;
    String code,value,img_url,tmp_url;
    ArrayList<SelectionModel> array=new ArrayList<>();


    public SelectionModel(String txt, boolean isClick) {
        this.txt = txt;
        this.isClick = isClick;
    }
    public SelectionModel(String txt, boolean isClick, String code) {
        this.txt = txt;
        this.isClick = isClick;
        this.code=code;
    }
    public SelectionModel(String txt,ArrayList<SelectionModel> array){
        this.txt = txt;
        this.array=array;
    }
    public SelectionModel(String txt, String value, String code,String img_url,String tmp_url) {
        this.txt = txt;
        this.value = value;
        this.code=code;
        this.img_url=img_url;
        this.tmp_url=tmp_url;
    }
    public SelectionModel(String txt, String value, String code,String img_url,ArrayList<SelectionModel> array) {
        this.txt = txt;
        this.value = value;
        this.code=code;
        this.img_url=img_url;
        this.array=array;
    }
    public SelectionModel(String txt, String code){
        this.txt=txt;
        this.code=code;
    }

    public ArrayList<SelectionModel> getArray() {
        return array;
    }

    public void setArray(ArrayList<SelectionModel> array) {
        this.array = array;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTmp_url() {
        return tmp_url;
    }

    public void setTmp_url(String tmp_url) {
        this.tmp_url = tmp_url;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SelectionModel(boolean isClick){
        this.isClick=isClick;
    }

    @Override
    public boolean equals(Object obj) {
        SelectionModel dt = (SelectionModel)obj;

        if(String.valueOf(dt.isClick).equals(String.valueOf(isClick))) return true;

        return false;
    }
}

