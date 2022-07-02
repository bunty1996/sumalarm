
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class DeviceModel implements Serializable {

    @SerializedName("alarmSent")
    private Long mAlarmSent;
    @SerializedName("alarmSilence")
    private Long mAlarmSilence;
    @SerializedName("capSenseMax")
    private Long mCapSenseMax;
    @SerializedName("capSenseMinOffset")
    private Long mCapSenseMinOffset;
    @SerializedName("checkinFailCount")
    private Long mCheckinFailCount;
    @SerializedName("deviceFirmware")
    private String mDeviceFirmware;
    @SerializedName("deviceSerialNumber")
    private String mDeviceSerialNumber;
    @SerializedName("deviceState")
    private Long mDeviceState;
    @SerializedName("displayName")
    private String mDisplayName;
    @SerializedName("entAccount")
    private Object mEntAccount;
    @SerializedName("id")
    private String mId;
    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("messageSendDate")
    private Long mMessageSendDate;
    @SerializedName("pcbNumber")
    private String mPcbNumber;
    @SerializedName("portalId")
    private String mPortalId;
    @SerializedName("pumpCalibrateCycles")
    private Long mPumpCalibrateCycles;
    @SerializedName("pumpCalibrateMinSum")
    private Long mPumpCalibrateMinSum;
    @SerializedName("pumpCalibrateSum")
    private Long mPumpCalibrateSum;
    @SerializedName("qcSerialNo")
    private String mQcSerialNo;
    @SerializedName("relayState")
    private Long mRelayState;
    @SerializedName("sirenState")
    private Long mSirenState;
    @SerializedName("lastCheckinTime")
    private String lastCheckinTime;
    @SerializedName("deviceSubscriptionDate")
    private String deviceSubscriptionDate;
    @SerializedName("online")
    private String online;
    @SerializedName("deviceType")
    private String deviceType;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public ArrayList<DeviceConfigModel> getDeviceConfigList() {
        return deviceConfigList;
    }

    public void setDeviceConfigList(ArrayList<DeviceConfigModel> deviceConfigList) {
        this.deviceConfigList = deviceConfigList;
    }

    @SerializedName("deviceConfig")
    ArrayList<DeviceConfigModel> deviceConfigList = new ArrayList<>();

    public ArrayList<DeviceDataModel> getDeviceDatList() {
        return deviceDatList;
    }

    public void setDeviceDatList(ArrayList<DeviceDataModel> deviceDatList) {
        this.deviceDatList = deviceDatList;
    }

    @SerializedName("deviceData")
    ArrayList<DeviceDataModel> deviceDatList = new ArrayList<>();

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @SerializedName("mac")
    private String mac;


    public String getLastCheckinTime() {
        return lastCheckinTime;
    }

    public void setLastCheckinTime(String lastCheckinTime) {
        this.lastCheckinTime = lastCheckinTime;
    }

    public String getDeviceSubscriptionDate() {
        return deviceSubscriptionDate;
    }

    public void setDeviceSubscriptionDate(String deviceSubscriptionDate) {
        this.deviceSubscriptionDate = deviceSubscriptionDate;
    }

    public Long getAlarmSent() {
        return mAlarmSent;
    }

    public void setAlarmSent(Long alarmSent) {
        mAlarmSent = alarmSent;
    }

    public Long getAlarmSilence() {
        return mAlarmSilence;
    }

    public void setAlarmSilence(Long alarmSilence) {
        mAlarmSilence = alarmSilence;
    }

    public Long getCapSenseMax() {
        return mCapSenseMax;
    }

    public void setCapSenseMax(Long capSenseMax) {
        mCapSenseMax = capSenseMax;
    }

    public Long getCapSenseMinOffset() {
        return mCapSenseMinOffset;
    }

    public void setCapSenseMinOffset(Long capSenseMinOffset) {
        mCapSenseMinOffset = capSenseMinOffset;
    }

    public Long getCheckinFailCount() {
        return mCheckinFailCount;
    }

    public void setCheckinFailCount(Long checkinFailCount) {
        mCheckinFailCount = checkinFailCount;
    }

    public String getDeviceFirmware() {
        return mDeviceFirmware;
    }

    public void setDeviceFirmware(String deviceFirmware) {
        mDeviceFirmware = deviceFirmware;
    }

    public String getDeviceSerialNumber() {
        return mDeviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        mDeviceSerialNumber = deviceSerialNumber;
    }

    public Long getDeviceState() {
        return mDeviceState;
    }

    public void setDeviceState(Long deviceState) {
        mDeviceState = deviceState;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public Object getEntAccount() {
        return mEntAccount;
    }

    public void setEntAccount(Object entAccount) {
        mEntAccount = entAccount;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public Long getMessageSendDate() {
        return mMessageSendDate;
    }

    public void setMessageSendDate(Long messageSendDate) {
        mMessageSendDate = messageSendDate;
    }

    public Object getPcbNumber() {
        return mPcbNumber;
    }

    public void setPcbNumber(String pcbNumber) {
        mPcbNumber = pcbNumber;
    }

    public String getPortalId() {
        return mPortalId;
    }

    public void setPortalId(String portalId) {
        mPortalId = portalId;
    }

    public Long getPumpCalibrateCycles() {
        return mPumpCalibrateCycles;
    }

    public void setPumpCalibrateCycles(Long pumpCalibrateCycles) {
        mPumpCalibrateCycles = pumpCalibrateCycles;
    }

    public Long getPumpCalibrateMinSum() {
        return mPumpCalibrateMinSum;
    }

    public void setPumpCalibrateMinSum(Long pumpCalibrateMinSum) {
        mPumpCalibrateMinSum = pumpCalibrateMinSum;
    }

    public Long getPumpCalibrateSum() {
        return mPumpCalibrateSum;
    }

    public void setPumpCalibrateSum(Long pumpCalibrateSum) {
        mPumpCalibrateSum = pumpCalibrateSum;
    }

    public String getQcSerialNo() {
        return mQcSerialNo;
    }

    public void setQcSerialNo(String qcSerialNo) {
        mQcSerialNo = qcSerialNo;
    }

    public Long getRelayState() {
        return mRelayState;
    }

    public void setRelayState(Long relayState) {
        mRelayState = relayState;
    }

    public Long getSirenState() {
        return mSirenState;
    }

    public void setSirenState(Long sirenState) {
        mSirenState = sirenState;
    }

}
