
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DeviceConfigModel {

    @SerializedName("configKey")
    private String mConfigKey;
    @SerializedName("configVal")
    private String mConfigVal;
    @SerializedName("id")
    private String mId;

    public String getConfigKey() {
        return mConfigKey;
    }

    public void setConfigKey(String configKey) {
        mConfigKey = configKey;
    }

    public String getConfigVal() {
        return mConfigVal;
    }

    public void setConfigVal(String configVal) {
        mConfigVal = configVal;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

}
