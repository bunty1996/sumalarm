
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;


public class ProfileModel {

    @SerializedName("success")
    private Boolean mSuccess;
    @SerializedName("user")
    private ProfileUserModel mUser;

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }

    public ProfileUserModel getUser() {
        return mUser;
    }

    public void setUser(ProfileUserModel user) {
        mUser = user;
    }

}
