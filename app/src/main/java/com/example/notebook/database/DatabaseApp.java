package com.example.notebook.database;

import android.app.Application;

import androidx.room.Room;

public class DatabaseApp extends Application {

    NoteDatabase noteDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        // when upgrading versions, kill the original tables by using fallbackToDestructiveMigration()
        noteDatabase = Room.databaseBuilder(this, NoteDatabase.class, NoteDatabase.NAME).fallbackToDestructiveMigration().build();
    }

    public NoteDatabase getAppDatabase() {
        return noteDatabase;
    }

}