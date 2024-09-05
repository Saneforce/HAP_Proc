package com.hap.checkinproc.Model_Class;

public class PaymentModel {

    private  String billNo;
    private String billDate;
    private Double billedAmt;
    private Double pendingAmt;
    private  Double  Amt=0.0;
    private  String orderNo;
    private String payMode;
    private String payDate;
    private int spinPos;
    private Double balAmt;


    public PaymentModel(String bill_no, String bill_date, double billed_amt, double pending_amt, String orderNo) {
        this.billNo=bill_no;
        this.billDate=bill_date;
        this.billedAmt=billed_amt;
        this.pendingAmt=pending_amt;
        this.orderNo=orderNo;
        this.balAmt=pending_amt;
    }
    public PaymentModel(String bill_no, String bill_date, double billed_amt, double pending_amt, String orderNo, double balanceAmt) {
        this.billNo=bill_no;
        this.billDate=bill_date;
        this.billedAmt=billed_amt;
        this.pendingAmt=pending_amt;
        this.orderNo=orderNo;
        this.balAmt=balanceAmt;
    }
    public PaymentModel(String bill_no, String bill_date, double billed_amt, double pending_amt, double paid_amt) {
        this.billNo=bill_no;
        this.billDate=bill_date;
        this.billedAmt=billed_amt;
        this.pendingAmt=pending_amt;
       this.Amt=paid_amt;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public Double getBilledAmt() {
        return billedAmt;
    }

    public void setBilledAmt(Double billedAmt) {
        this.billedAmt = billedAmt;
    }

    public Double getPendingAmt() {
        return pendingAmt;
    }

    public void setPendingAmt(Double pendingAmt) {
        this.pendingAmt = pendingAmt;
    }

    public Double getAmt() {
        return Amt;
    }

    public void setAmt(Double amt) {
        Amt = amt;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayMode() { return payMode;}

    public void setPayMode(String payMode) { this.payMode = payMode;}

    public String getPayDate() {return payDate;}

    public void setPayDate(String payDate) {this.payDate = payDate;}

    public int getSpinPos() {return spinPos;}

    public void setSpinPos(int spinPos) {this.spinPos = spinPos;}
    public Double getBalAmt() {return balAmt;}

    public void setBalAmt(Double balAmt) {this.balAmt = balAmt;}
    @Override
    public String toString() {
        return "paymentmodel{" +
                "id=" + billNo +
                ", billamt=" + billedAmt +
                ", pendamt=" + pendingAmt +
                ", receiveamt='" + Amt + '\'' +
                ", balanceAmt'" + balAmt + '\'' +
                ", mode='" + payMode + '\'' +
                ", date=" + payDate +
                '}';
    }
}
