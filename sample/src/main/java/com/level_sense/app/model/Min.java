
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Min {
    /*@SerializedName("label")
        private Long mLabel;
        @SerializedName("selected")
        private Boolean mSelected;
        @SerializedName("value")
        private Long mValue;*/

    @SerializedName("label")
    private String mLabel;
    @SerializedName("selected")
    private Boolean mSelected;
    @SerializedName("value")
    private String mValue;

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public Boolean getSelected() {
        return mSelected;
    }

    public void setSelected(Boolean selected) {
        mSelected = selected;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
