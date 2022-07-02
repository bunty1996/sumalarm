package com.level_sense.app.roomDB.Dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.level_sense.app.roomDB.model.GraphModelDB;

import java.util.List;
@Dao
public interface GraphValueDao {
    //table graph_value_table
    @Insert
    void insert(GraphModelDB graphModelDB);

    @Query("SELECT * FROM graph_value_table")
    LiveData<List<GraphModelDB>> getAllValue();

    @Query("SELECT * FROM graph_value_table WHERE sensor_Id=:sensorId")
   /*public*/ List<GraphModelDB> getValueDB(String sensorId);

   /*@Query("SELECT * FROM graph_value_table WHERE sensor_Id=:sensorId")
    LiveData<GraphModelDB> getValueDB(String sensorId);*/

    @Update
    void update(GraphModelDB graphModelDB);

    @Delete
    int delete(GraphModelDB graphModelDB);

    @Query("DELETE FROM graph_value_table")
    void deleteAll();
}
