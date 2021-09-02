package com.hap.checkinproc.Interface;


import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

public interface UpdateResponseUI {


    void onLoadFilterData(java.util.List<Retailer_Modal_List> retailer_modal_list);

    void onLoadTodayOrderList(java.util.List<OutletReport_View_Modal> outletReportViewModals);

    void onLoadDataUpdateUI(String apiDataResponse);

}