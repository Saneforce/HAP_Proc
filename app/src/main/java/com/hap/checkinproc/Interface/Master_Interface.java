package com.hap.checkinproc.Interface;

import com.hap.checkinproc.Model_Class.Distributor_Master;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.Model_Class.Work_Type_Model;

import java.util.List;

public interface Master_Interface {

    void OnclickMasterType(java.util.List<Work_Type_Model> myDataset, int position, List<Distributor_Master> Distributor_Master, List<Route_Master> route_Master, int type);


}
