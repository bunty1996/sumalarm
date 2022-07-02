package com.level_sense.app.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Query ("SELECT * FROM graphdata Where type=:type")
    List<Note> getNotes(String type);

    @Query("DELETE FROM graphdata ")
    void deleteAll();
}
