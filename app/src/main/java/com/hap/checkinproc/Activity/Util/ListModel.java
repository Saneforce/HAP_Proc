package com.hap.checkinproc.Activity.Util;

public class ListModel {
    String formid,formName,formTable,targetForm,formType;

    public ListModel(String formid, String formName, String formTable, String targetForm, String formType) {
        this.formid = formid;
        this.formName = formName;
        this.formTable = formTable;
        this.targetForm = targetForm;
        this.formType = formType;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormTable() {
        return formTable;
    }

    public void setFormTable(String formTable) {
        this.formTable = formTable;
    }

    public String getTargetForm() {
        return targetForm;
    }

    public void setTargetForm(String targetForm) {
        this.targetForm = targetForm;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }
}
