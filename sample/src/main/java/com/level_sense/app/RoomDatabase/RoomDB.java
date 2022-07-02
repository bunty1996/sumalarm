package com.level_sense.app.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = Note.class,version = 1)
public abstract class RoomDB extends RoomDatabase {
    public abstract NoteDao noteDao();
    public static volatile RoomDB noteroominstance;
    public static  RoomDB getDatabase(final Context context){
         if(noteroominstance==null){
             noteroominstance= Room.databaseBuilder(context.getApplicationContext(),RoomDB.class,"note_database").build();
         }
         return noteroominstance;
     }
}
