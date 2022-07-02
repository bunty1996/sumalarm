
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ContactListModel {

    @SerializedName("cellProvider")
    private String mCellProvider;
    @SerializedName("defaultStatus")
    private Boolean mDefaultStatus;
    @SerializedName("email")
    private String mEmail;
@SerializedName("emailActive")
    private Boolean mEmailActive;
    @SerializedName("enableStatus")
    private Boolean mEnableStatus;
    @SerializedName("firstName")
    private String mFirstName;
    @SerializedName("id")
    private String mId;
    @SerializedName("lastName")
    private String mLastName;
    @SerializedName("mobile")
    private String mMobile;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @SerializedName("countryId")
    private String countryId;

    @SerializedName("dialCode")
    private String mobileCode;
    @SerializedName("smsActive")
    private Boolean mSmsActive;
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("voiceActive")
    private Boolean mVoiceActive;

    String servicePrivederName;

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }

    public String getServicePrivederName() {
        return servicePrivederName;
    }

    public void setServicePrivederName(String servicePrivederName) {
        this.servicePrivederName = servicePrivederName;
    }

    public String getCellProvider() {
        return mCellProvider;
    }

    public void setCellProvider(String cellProvider) {
        mCellProvider = cellProvider;
    }

    public Boolean getDefaultStatus() {
        return mDefaultStatus;
    }

    public void setDefaultStatus(Boolean defaultStatus) {
        mDefaultStatus = defaultStatus;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Boolean getEmailActive() {
        return mEmailActive;
    }

    public void setEmailActive(Boolean emailActive) {
        mEmailActive = emailActive;
    }

    public Boolean getEnableStatus() {
        return mEnableStatus;
    }

    public void setEnableStatus(Boolean enableStatus) {
        mEnableStatus = enableStatus;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public Boolean getSmsActive() {
        return mSmsActive;
    }

    public void setSmsActive(Boolean smsActive) {
        mSmsActive = smsActive;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public Boolean getVoiceActive() {
        return mVoiceActive;
    }

    public void setVoiceActive(Boolean voiceActive) {
        mVoiceActive = voiceActive;
    }

}
