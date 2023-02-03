package com.example.notebook.dao;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notebook.model.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Query("SELECT * FROM note WHERE noteId = :id")
    Note getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllNotes(List<Note> notes);


    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
    @Query("DELETE FROM note")
    public void nukeTable();
}
