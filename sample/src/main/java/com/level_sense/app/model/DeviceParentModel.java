
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class DeviceParentModel {

    @SerializedName("deviceList")
    private ArrayList<DeviceModel> mDeviceList;
    @SerializedName("success")
    private Boolean mSuccess;

    public ArrayList<DeviceModel> getDeviceList() {
        return mDeviceList;
    }

    public void setDeviceList(ArrayList<DeviceModel> deviceList) {
        mDeviceList = deviceList;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }

}
