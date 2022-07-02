package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

public class GraphModel {
    @SerializedName("timeStamp")
    private Long mTimeStamp;
    @SerializedName("value")
    private String mValue;

    public Long getTimeStamp() {
        return mTimeStamp;
    }

    public Long getTimeInHours() {
        return mTimeStamp;
    }

    public Long getTimeInDays() {
        return mTimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        mTimeStamp = timeStamp;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
