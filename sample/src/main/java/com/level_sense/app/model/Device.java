
package com.level_sense.app.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Device {

    @SerializedName("deviceSerialNumber")
    private String mDeviceSerialNumber;
    @SerializedName("displayName")
    private String mDisplayName;
    @SerializedName("id")
    private String mId;

    @Nullable
    @SerializedName("pumpCycle")
    private String pumpCycle;
    @SerializedName("sensorLimit")
    private ArrayList<SensorLimit> mSensorLimit;
    @SerializedName("sensorLimitMeta")
    private SensorLimitMeta mSensorLimitMeta;

    @SerializedName("deviceConfig")
    private ArrayList<DeviceConfigModel> deviceConfig = null;
    @SerializedName("deviceConfigMeta")
    private DeviceConfigMeta deviceConfigMeta;

    public String getPumpCycle() {
        return pumpCycle;
    }

    public void setPumpCycle(String pumpCycle) {
        this.pumpCycle = pumpCycle;
    }


    public String getDeviceSerialNumber() {
        return mDeviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        mDeviceSerialNumber = deviceSerialNumber;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public ArrayList<SensorLimit> getSensorLimit() {
        return mSensorLimit;
    }

    public void setSensorLimit(ArrayList<SensorLimit> sensorLimit) {
        mSensorLimit = sensorLimit;
    }

    public SensorLimitMeta getSensorLimitMeta() {
        return mSensorLimitMeta;
    }

    public void setSensorLimitMeta(SensorLimitMeta sensorLimitMeta) {
        mSensorLimitMeta = sensorLimitMeta;
    }

    public String getmDeviceSerialNumber() {
        return mDeviceSerialNumber;
    }

    public void setmDeviceSerialNumber(String mDeviceSerialNumber) {
        this.mDeviceSerialNumber = mDeviceSerialNumber;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public ArrayList<SensorLimit> getmSensorLimit() {
        return mSensorLimit;
    }

    public void setmSensorLimit(ArrayList<SensorLimit> mSensorLimit) {
        this.mSensorLimit = mSensorLimit;
    }

    public SensorLimitMeta getmSensorLimitMeta() {
        return mSensorLimitMeta;
    }

    public void setmSensorLimitMeta(SensorLimitMeta mSensorLimitMeta) {
        this.mSensorLimitMeta = mSensorLimitMeta;
    }

    public ArrayList<DeviceConfigModel> getDeviceConfig() {
        return deviceConfig;
    }

    public void setDeviceConfig(ArrayList<DeviceConfigModel> deviceConfig) {
        this.deviceConfig = deviceConfig;
    }

    public DeviceConfigMeta getDeviceConfigMeta() {
        return deviceConfigMeta;
    }

    public void setDeviceConfigMeta(DeviceConfigMeta deviceConfigMeta) {
        this.deviceConfigMeta = deviceConfigMeta;
    }
}
