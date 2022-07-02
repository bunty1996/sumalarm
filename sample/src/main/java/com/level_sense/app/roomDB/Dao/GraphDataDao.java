package com.level_sense.app.roomDB.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.level_sense.app.roomDB.model.DeviceDataModelDB;

import java.util.List;

@Dao
public interface GraphDataDao {
    //table graph_data_table
    @Insert
    void insert(DeviceDataModelDB deviceDataModelDB);

    @Query("SELECT * FROM graph_data_table ")
    List<DeviceDataModelDB> getAllData();

  /*  @Query("SELECT * FROM graph_data_table ")
    LiveData<List<DeviceDataModelDB>> getAllData();*/

    @Query("SELECT * FROM graph_data_table WHERE sensorId=:sensorId")
    LiveData<DeviceDataModelDB> getData(String sensorId);
    //LiveData<DeviceDataModelDB> getData(String sensorId);

    @Query("SELECT * FROM graph_data_table WHERE db_time =:db_time")
    LiveData<DeviceDataModelDB> getDBtime(String db_time);

    @Update
    void update(DeviceDataModelDB deviceDataModelDB);

    @Delete
    int delete(DeviceDataModelDB deviceDataModelDB);

    @Query("DELETE FROM graph_data_table")
    void deleteAll();

}
