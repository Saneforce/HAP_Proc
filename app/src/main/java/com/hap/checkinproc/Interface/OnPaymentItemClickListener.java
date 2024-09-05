package com.hap.checkinproc.Interface;

import com.hap.checkinproc.Model_Class.PaymentModel;

public interface OnPaymentItemClickListener {
    void onClickProduct(int currentPosition, PaymentModel paymentModel , int type);
}
