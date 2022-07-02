package com.level_sense.app.RoomDatabase;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "graphdata")
public class Note {
    @PrimaryKey
    @NonNull
    private String type;
    @NonNull
    @ColumnInfo(name = "graph")
     private String data;


    public Note(@NonNull String type, @NonNull String data) {
        this.type = type;
        this.data = data;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getData() {
        return data;
    }

}
