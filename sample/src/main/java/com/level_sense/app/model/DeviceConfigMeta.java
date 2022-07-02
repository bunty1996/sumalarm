
package com.level_sense.app.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DeviceConfigMeta {

    @SerializedName("delayList")
    @Expose
    private List<Delay> delayList = null;

    public List<Delay> getDelayList() {
        return delayList;
    }

    public void setDelayList(List<Delay> delayList) {
        this.delayList = delayList;
    }

}
