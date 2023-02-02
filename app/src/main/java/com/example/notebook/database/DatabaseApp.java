package com.example.notebook.database;

import android.app.Application;

import androidx.room.Room;

public class DatabaseApp extends Application {

    AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        // when upgrading versions, kill the original tables by using fallbackToDestructiveMigration()
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.NAME).fallbackToDestructiveMigration().build();
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

}