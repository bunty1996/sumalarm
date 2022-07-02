package com.level_sense.app.roomDB.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.level_sense.app.model.DeviceDataModel;

import java.io.Serializable;

@Entity(tableName = "graph_data_table")
public class DeviceDataModelDB implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    @SerializedName("sensorId")
    @ColumnInfo(name = "sensorId")
    @Expose
    private String sensorId;

    @Expose
    @SerializedName("sensorSlug")
    @ColumnInfo(name = "sensorSlug")
    private String sensorSlug;

    @SerializedName("sensorDisplayName")
    @ColumnInfo(name = "sensorDisplayName")
    @Expose
    private String sensorDisplayName;

    @SerializedName("sensorDisplayUnits")
    @ColumnInfo(name = "sensorDisplayUnits")
    @Expose
    private String sensorDisplayUnits;

    @SerializedName("min")
    @ColumnInfo(name = "min")
    @Expose
    private double min;


    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @SerializedName("max")
    @ColumnInfo(name = "max")
    @Expose
    private double max;

    // get database store time
    @ColumnInfo(name = "db_time")
    @Expose
    private Long db_time;

    //get today and week data
    @ColumnInfo(name = "graph_schema")
    @Expose
    private String graph_schema;

    public Long getDb_time() {
        return db_time;
    }

    public void setDb_time(Long db_time) {
        this.db_time = db_time;
    }

    public String getGraph_schema() {
        return graph_schema;
    }

    public void setGraph_schema(String graph_schema) {
        this.graph_schema = graph_schema;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public DeviceDataModelDB() {
    }

    public DeviceDataModelDB(Long db_time, DeviceDataModel model) {
        this.sensorId = model.getSensorId();
        this.sensorSlug = model.getSensorSlug();
        this.sensorDisplayName = model.getSensorDisplayName();
        this.sensorDisplayUnits = model.getSensorDisplayUnits();
        this.min = model.getMin();
        this.max = model.getMin();
        this.db_time= db_time;

       /* for (int i = 0; i < model.getData().size(); i++) {

            new GraphModelDB(model.getData().get(i).getValue(), model.getData().get(i).getTimeStamp(), sensorId);

        }
*/
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorSlug() {
        return sensorSlug;
    }

    public void setSensorSlug(String sensorSlug) {
        this.sensorSlug = sensorSlug;
    }

    public String getSensorDisplayName() {
        return sensorDisplayName;
    }

    public void setSensorDisplayName(String sensorDisplayName) {
        this.sensorDisplayName = sensorDisplayName;
    }

    public String getSensorDisplayUnits() {
        return sensorDisplayUnits;
    }

    public void setSensorDisplayUnits(String sensorDisplayUnits) {
        this.sensorDisplayUnits = sensorDisplayUnits;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }


}
