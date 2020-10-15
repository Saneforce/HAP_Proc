package com.hap.checkinproc.Activity.Util;

public class SelectionModel {
    String txt;
    boolean isClick;
    String code;

    public SelectionModel(String txt, boolean isClick) {
        this.txt = txt;
        this.isClick = isClick;
    }
    public SelectionModel(String txt, boolean isClick, String code) {
        this.txt = txt;
        this.isClick = isClick;
        this.code=code;
    }
    public SelectionModel(String txt, String code){
        this.txt=txt;
        this.code=code;
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

