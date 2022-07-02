package com.level_sense.app.roomDB.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.level_sense.app.roomDB.Dao.GraphDataDao;
import com.level_sense.app.roomDB.Dao.GraphValueDao;
import com.level_sense.app.roomDB.model.DeviceDataModelDB;
import com.level_sense.app.roomDB.model.GraphModelDB;

@Database(entities = {DeviceDataModelDB.class, GraphModelDB.class},version = 1)
public abstract class GraphRoomDatabase extends RoomDatabase {

    public abstract GraphDataDao graphDataDao();
    public abstract GraphValueDao graphValueDao();

    private static volatile GraphRoomDatabase graphRoomInstance;

    public static GraphRoomDatabase getDatabase(final Context context) {
        if (graphRoomInstance == null) {
            synchronized (GraphRoomDatabase.class) {
                if (graphRoomInstance == null) {
                    graphRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            GraphRoomDatabase.class, "graph_abc_database")
                            .build();
                }
            }
        }
        return graphRoomInstance;
    }
}
