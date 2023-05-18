package com.applock.photos.videos.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.applock.photos.videos.ui.activity.GestureUnlockActivity;

import org.litepal.LitePal;

import java.util.List;

public class MyApp extends Application {

    private static MyApp instance;
    private static SharePreferences preferences;
    private static CommLockInfoManager lockInfoManager;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        LitePal.initialize(this);

    }

    public static SharePreferences getPreferences(){
        if (preferences == null)
            preferences = new SharePreferences(getInstance());
        return preferences;
    }

    public static CommLockInfoManager getLockInfoManager(){
        if (lockInfoManager == null)
            lockInfoManager = new CommLockInfoManager(getInstance());
        return lockInfoManager;
    }

    public static synchronized MyApp getInstance(){
        return instance;
    }

    public void clearAllActivity() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
        if (appTasks != null) {
            for (ActivityManager.AppTask appTask : appTasks) {
//                ActivityManager.RecentTaskInfo recentTaskInfo = appTask.getTaskInfo();
                if (appTask.getTaskInfo().baseActivity != null) {
                    appTask.finishAndRemoveTask();
                }
            }
        }
    }

}
