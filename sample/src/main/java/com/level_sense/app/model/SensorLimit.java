package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SensorLimit {

  /*@SerializedName("currentValue")
    private String mCurrentValue;
    @SerializedName("email")
    private Long mEmail;
    @SerializedName("enabled")
    private Long mEnabled;
    @SerializedName("id")
    private String mId;
    @SerializedName("isAlarm")
    private Boolean mIsAlarm;
    @SerializedName("lcl")
    private Long mLcl;
    @SerializedName("relay")
    private Long mRelay;
    @SerializedName("sensorDisplayName")
    private String mSensorDisplayName;
    @SerializedName("sensorDisplayUnits")
    private String mSensorDisplayUnits;
    @SerializedName("sensorId")
    private String mSensorId;
    @SerializedName("sensorSlug")
    private String mSensorSlug;
    @SerializedName("siren")
    private Long mSiren;
    @SerializedName("text")
    private Long mText;
    @SerializedName("ucl")
    private Long mUcl;
    @SerializedName("voice")
    private Long mVoice;*/

    @SerializedName("currentValue")
    private String mCurrentValue;



    //either 1 or 0
    @SerializedName("email")
    private String mEmail;
    @SerializedName("enabled")
    private String mEnabled;
    @SerializedName("id")
    private String mId;
    @SerializedName("isAlarm")
    private Boolean mIsAlarm;
    @SerializedName("lcl")
    private String mLcl;
    @SerializedName("relay")
    private String mRelay;
    @SerializedName("sensorDisplayName")
    private String mSensorDisplayName;
    @SerializedName("sensorDisplayUnits")
    private String mSensorDisplayUnits;
    @SerializedName("sensorId")
    private String mSensorId;
    @SerializedName("sensorSlug")
    private String mSensorSlug;
    @SerializedName("siren")
    private String mSiren;
    @SerializedName("text")
    private String mText;
    @SerializedName("ucl")
    private String mUcl;
    @SerializedName("voice")
    private String mVoice;

    @SerializedName("rawValue")
    private String rawValue;

    public String getCurrentValue() {
        return mCurrentValue;
    }

    public void setCurrentValue(String currentValue) {
        mCurrentValue = currentValue;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getEnabled() {
        return mEnabled;
    }

    public void setEnabled(String enabled) {
        mEnabled = enabled;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Boolean getIsAlarm() {
        return mIsAlarm;
    }

    public void setIsAlarm(Boolean isAlarm) {
        mIsAlarm = isAlarm;
    }

    public String getLcl() {
        return mLcl;
    }

    public void setLcl(String lcl) {
        mLcl = lcl;
    }

    public String getRelay() {
        return mRelay;
    }

    public void setRelay(String relay) {
        mRelay = relay;
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
    public int getSensorIntId() {
        return Integer.parseInt(mSensorId);
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

    public String getSiren() {
        return mSiren;
    }

    public void setSiren(String siren) {
        mSiren = siren;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getUcl() {
        return mUcl;
    }

    public void setUcl(String ucl) {
        mUcl = ucl;
    }

    public String getVoice() {
        return mVoice;
    }

    public void setVoice(String voice) {
        mVoice = voice;
    }

    public String getRawValue() {
        return rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }
}
