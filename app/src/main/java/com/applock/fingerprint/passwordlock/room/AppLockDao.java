package com.applock.fingerprint.passwordlock.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.applock.fingerprint.passwordlock.model.CommLockInfo;

import java.util.List;

@Dao
public interface AppLockDao {

    @Insert(entity = CommLockInfo.class, onConflict = OnConflictStrategy.REPLACE)
    void insertData(CommLockInfo commLockInfo);

    @Update
    void updateData(CommLockInfo commLockInfo);

    @Query("SELECT * FROM lockInfo WHERE isFavoriteApp=1")
    LiveData<List<CommLockInfo>> getFavourite();

    @Query("SELECT * FROM lockInfo WHERE isFavoriteApp=0")
    LiveData<List<CommLockInfo>> getOtherApps();

    @Query("SELECT packageName FROM lockInfo WHERE packageName=:packageName")
    boolean isExists(String packageName);


}
