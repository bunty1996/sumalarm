
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class StateListModel implements Serializable {

    @SerializedName("countryId")
    private Long mCountryId;
    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;

    public Long getCountryId() {
        return mCountryId;
    }

    public void setCountryId(Long countryId) {
        mCountryId = countryId;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
