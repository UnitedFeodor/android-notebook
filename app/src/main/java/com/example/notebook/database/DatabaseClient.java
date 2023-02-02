package com.example.notebook.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private NoteDatabase noteDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        noteDatabase = Room.databaseBuilder(mCtx, NoteDatabase.class, "Notes.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public NoteDatabase getAppDatabase() {
        return noteDatabase;
    }
}