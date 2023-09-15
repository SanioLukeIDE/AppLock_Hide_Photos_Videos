package com.applock.fingerprint.passwordlock.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.applock.fingerprint.passwordlock.model.CommLockInfo;

@Database(entities = {CommLockInfo.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract AppLockDao lockDao();

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "LockApp").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
