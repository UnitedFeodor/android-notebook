package com.example.notebook.database;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notebook.dao.NoteDAO;
import com.example.notebook.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDAO noteDao();

    // Database name to be used
    public static final String NAME = "AppDatabase";
}
