package com.level_sense.app.roomDB.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "graph_value_table")

public class GraphModelDB implements Serializable {
    @ForeignKey(entity = DeviceDataModelDB.class,
            parentColumns = "sensorId",
            childColumns = "sensor_Id",
            onDelete = ForeignKey.CASCADE)

    @SerializedName("value")
    @ColumnInfo(name = "value")
    @Expose
    private String value;


    @ColumnInfo(name = "timeStamp")
    @SerializedName("timeStamp")
    @Expose
    private Long timeStamp;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long idd;

    @NonNull
    @ColumnInfo(name = "sensor_Id")
    @SerializedName("sensorId")
    @Expose
    private String sensorId;


    public GraphModelDB(String value, Long timeStamp, String sensorId) {
        this.value = value;
        this.timeStamp = timeStamp;
        this.sensorId = sensorId;
    }

    @NonNull
    public Long getIdd() {
        return idd;
    }

    public void setIdd(@NonNull Long idd) {
        this.idd = idd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
}
