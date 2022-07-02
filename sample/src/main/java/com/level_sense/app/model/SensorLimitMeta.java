package com.level_sense.app.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class SensorLimitMeta {
    @SerializedName("cap_sense")
    private String mCapSense;
    @SerializedName("input1")
    private ArrayList<Input1> mInput1;
    @SerializedName("input2")
    private ArrayList<Input2> mInput2;
    @SerializedName("rh")
    private Rh mRh;
    @SerializedName("tempc")
    private Tempc mTempc;

    ArrayList<String> minlist;
    ArrayList<String> maxlist;

    public String getCapSense() {
        return mCapSense;
    }

    public void setCapSense(String capSense) {
        mCapSense = capSense;
    }

    public ArrayList<Input1> getInput1() {
        return mInput1;
    }

    public void setInput1(ArrayList<Input1> input1) {
        mInput1 = input1;
    }

    public ArrayList<Input2> getInput2() {
        return mInput2;
    }

    public void setInput2(ArrayList<Input2> input2) {
        mInput2 = input2;
    }

    public Rh getRh() {
        return mRh;
    }

    public void setRh(Rh rh) {
        mRh = rh;
    }

    public Tempc getTempc() {
        return mTempc;
    }

    public void setTempc(Tempc tempc) {
        mTempc = tempc;
    }

}
