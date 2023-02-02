package com.example.notebook.database;



import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notebook.dao.NoteDAO;
import com.example.notebook.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public static final String NAME = "noteDataBase";
    public abstract NoteDAO noteDao();

    private static NoteDatabase noteDatabaseInstance;
    public static synchronized NoteDatabase getInstance(Context context) {
        if (noteDatabaseInstance == null) {
            noteDatabaseInstance = Room.databaseBuilder(context,NoteDatabase.class,NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return noteDatabaseInstance;
    }

    // Database name to be used

}
