
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DeviceDataModel implements Serializable {
    public List<DeviceEventModel> getEventList() {
        return eventList;
    }

    @SerializedName("LIST")
    private List<DeviceEventModel> eventList;

    @SerializedName("data")
    private List<GraphModel> mData;
    @SerializedName("maxValue")
    private double mMax;
    @SerializedName("minValue")
    private double mMin;
    @SerializedName("sensorDisplayName")
    private String mSensorDisplayName;
    @SerializedName("sensorDisplayUnits")
    private String mSensorDisplayUnits;
    @SerializedName("sensorId")
    private String mSensorId;
    @SerializedName("sensorSlug")
    private String mSensorSlug;

    @SerializedName("value")
    private String value;

   /* @SerializedName("minValue")
    private String valueMin;
    @SerializedName("maxValue")
    private String valueMax;*/

   @SerializedName("samplePeriod")
    private String samplePeriod;
    @SerializedName("points")
    private String points;
    @SerializedName("timeStamp")
    private String timeStamp;
    @SerializedName("timeStampAt")
    private String timeStampAt;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

  /*  public String getValueMin() {
        return valueMin;
    }

    public void setValueMin(String valueMin) {
        this.valueMin = valueMin;
    }

    public String getValueMax() {
        return valueMax;
    }

    public void setValueMax(String valueMax) {
        this.valueMax = valueMax;
    }*/

    public String getSamplePeriod() {
        return samplePeriod;
    }

    public void setSamplePeriod(String samplePeriod) {
        this.samplePeriod = samplePeriod;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStampAt() {
        return timeStampAt;
    }

    public void setTimeStampAt(String timeStampAt) {
        this.timeStampAt = timeStampAt;
    }

    public List<GraphModel> getData() {
        return mData;
    }

    public void setData(List<GraphModel> data) {
        mData = data;
    }

    public double getMax() {
        return mMax;
    }

    public void setMax(double max) {
        mMax = max;
    }

    public double getMin() {
        return mMin;
    }

    public void setMin(double min) {
        mMin = min;
    }

    public String getSensorDisplayName() {
        return mSensorDisplayName;
    }

    public void setSensorDisplayName(String sensorDisplayName) {
        mSensorDisplayName = sensorDisplayName;
    }

    public String getSensorDisplayUnits() {
        return mSensorDisplayUnits;
    }

    public void setSensorDisplayUnits(String sensorDisplayUnits) {
        mSensorDisplayUnits = sensorDisplayUnits;
    }

    public String getSensorId() {
        return mSensorId;
    }

    public void setSensorId(String sensorId) {
        mSensorId = sensorId;
    }

    public String getSensorSlug() {
        return mSensorSlug;
    }

    public void setSensorSlug(String sensorSlug) {
        mSensorSlug = sensorSlug;
    }

}
