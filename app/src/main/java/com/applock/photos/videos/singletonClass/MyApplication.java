package com.applock.photos.videos.singletonClass;

import static com.adsmodule.api.adsModule.retrofit.APICallHandler.callAppCountApi;
import static com.adsmodule.api.adsModule.utils.Constants.MAIN_BASE_URL;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.adsmodule.api.adsModule.preferencesManager.AppPreferences;
import com.adsmodule.api.adsModule.retrofit.AdsDataRequestModel;
import com.adsmodule.api.adsModule.utils.Global;
import com.applock.photos.videos.utils.CommLockInfoManager;
import com.applock.photos.videos.utils.SharePreferences;

import org.litepal.LitePal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {

    private static MyApplication instance;
    private static SharePreferences preferences;
    private static CommLockInfoManager lockInfoManager;
    private static ExecutorService executorService;

    public static ExecutorService getExecutorService() {
        if (executorService == null) executorService = Executors.newFixedThreadPool(8);
        return executorService;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        LitePal.initialize(this);

        AppPreferences preferences = new AppPreferences(instance);
        if (preferences.isFirstRun()) {
            callAppCountApi(MAIN_BASE_URL, new AdsDataRequestModel(instance.getPackageName(), Global.getDeviceId(instance)), () -> {
                preferences.setFirstRun(false);
            });
        }
        new AppOpenAds(instance);
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

    public static synchronized MyApplication getInstance(){
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
