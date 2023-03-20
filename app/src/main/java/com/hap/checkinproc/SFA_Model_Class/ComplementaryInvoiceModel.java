package com.hap.checkinproc.SFA_Model_Class;

public class ComplementaryInvoiceModel {
    String invoice, date, value;
    boolean isChecked;

    public ComplementaryInvoiceModel(String invoice, String date, String value, boolean isChecked) {
        this.invoice = invoice;
        this.date = date;
        this.value = value;
        this.isChecked = isChecked;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
