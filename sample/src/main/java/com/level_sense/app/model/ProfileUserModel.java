package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

public class ProfileUserModel {
    @SerializedName("address")
    private String mAddress;
    @SerializedName("city")
    private String mCity;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("creationDateAt")
    private String mCreationDateAt;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("firstName")
    private String mFirstName;
    @SerializedName("id")
    private String mId;
    @SerializedName("lastName")
    private String mLastName;
    @SerializedName("portalId")
    private String mPortalId;
    @SerializedName("sensorDisplayUnits")
    private String mSensorDisplayUnits;
    @SerializedName("state")
    private String mState;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("userStatus")
    private Boolean mUserStatus;
    @SerializedName("zipcode")
    private String mZipcode;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getCreationDateAt() {
        return mCreationDateAt;
    }

    public void setCreationDateAt(String creationDateAt) {
        mCreationDateAt = creationDateAt;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
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

    public String getPortalId() {
        return mPortalId;
    }

    public void setPortalId(String portalId) {
        mPortalId = portalId;
    }

    public String getSensorDisplayUnits() {
        return mSensorDisplayUnits;
    }

    public void setSensorDisplayUnits(String sensorDisplayUnits) {
        mSensorDisplayUnits = sensorDisplayUnits;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public Boolean getUserStatus() {
        return mUserStatus;
    }

    public void setUserStatus(Boolean userStatus) {
        mUserStatus = userStatus;
    }

    public String getZipcode() {
        return mZipcode;
    }

    public void setZipcode(String zipcode) {
        mZipcode = zipcode;
    }

}
