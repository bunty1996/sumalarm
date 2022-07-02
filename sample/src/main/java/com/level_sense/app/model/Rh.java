
package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Rh {

    @SerializedName("max")
    private ArrayList<Max> mMax;
    @SerializedName("min")
    private ArrayList<Min> mMin;

    public ArrayList<Max> getMax() {
        return mMax;
    }

    public void setMax(ArrayList<Max> max) {
        mMax = max;
    }

    public ArrayList<Min> getMin() {
        return mMin;
    }

    public void setMin(ArrayList<Min> min) {
        mMin = min;
    }

}
