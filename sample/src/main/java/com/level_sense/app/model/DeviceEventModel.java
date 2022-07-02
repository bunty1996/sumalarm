
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DeviceEventModel {
    @SerializedName("deviceID")
    private String mDeviceID;
    @SerializedName("event")
    private String mEvent;
    @SerializedName("eventTime")
    private String mEventTime;
    @SerializedName("id")
    private String mId;
    @SerializedName("logType")
    private String mLogType;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("to")
    private String mTo;

    public String getDeviceID() {
        return mDeviceID;
    }

    public void setDeviceID(String deviceID) {
        mDeviceID = deviceID;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(String event) {
        mEvent = event;
    }

    public String getEventTime() {
        return mEventTime;
    }

    public void setEventTime(String eventTime) {
        mEventTime = eventTime;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLogType() {
        return mLogType;
    }

    public void setLogType(String logType) {
        mLogType = logType;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getTo() {
        return mTo;
    }

    public void setTo(String to) {
        mTo = to;
    }

}
