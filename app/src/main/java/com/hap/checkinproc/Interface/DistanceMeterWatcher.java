package com.hap.checkinproc.Interface;

import org.json.JSONObject;

public interface DistanceMeterWatcher {
    void onKilometerChange(JSONObject KMDetails);
}
