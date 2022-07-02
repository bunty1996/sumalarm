
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CellProviderModel {

    @SerializedName("id")
    private Long mId;
    @SerializedName("provider")
    private String mProvider;
    @SerializedName("providerCode")
    private String mProviderCode;
    @SerializedName("providerStatus")
    private Boolean mProviderStatus;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getProvider() {
        return mProvider;
    }

    public void setProvider(String provider) {
        mProvider = provider;
    }

    public String getProviderCode() {
        return mProviderCode;
    }

    public void setProviderCode(String providerCode) {
        mProviderCode = providerCode;
    }

    public Boolean getProviderStatus() {
        return mProviderStatus;
    }

    public void setProviderStatus(Boolean providerStatus) {
        mProviderStatus = providerStatus;
    }

}
