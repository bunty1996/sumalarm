
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Input1 {

    @SerializedName("label")
    private String mLabel;
    @SerializedName("value")
    private String mValue;

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
