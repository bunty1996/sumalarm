package com.level_sense.app.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Query("SELECT * FROM graph_table")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM graph_table WHERE id=:noteId")
    LiveData<Note> getNote(String noteId);

}
