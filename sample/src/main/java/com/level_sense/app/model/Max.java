
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Max {

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

    public void setLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public Boolean getSelected() {
        return mSelected;
    }

    public void setSelected(Boolean mSelected) {
        this.mSelected = mSelected;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }



   /* public Long getLabel() {
        return mLabel;
    }
    public void setLabel(Long label) {
        mLabel = label;
    }
    public Boolean getSelected() {
        return mSelected;
    }
    public void setSelected(Boolean selected) {
        mSelected = selected;
    }
    public Long getValue() {
        return mValue;
    }
    public void setValue(Long value) {
        mValue = value;
    }*/
}
