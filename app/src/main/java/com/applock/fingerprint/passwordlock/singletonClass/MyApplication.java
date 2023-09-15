package com.applock.fingerprint.passwordlock.singletonClass;

import static com.adsmodule.api.adsModule.retrofit.APICallHandler.callAppCountApi;
import static com.adsmodule.api.adsModule.utils.Constants.MAIN_BASE_URL;
import static com.applock.fingerprint.passwordlock.service.LockService.CHANNEL_ID;
import static com.applock.fingerprint.passwordlock.singletonClass.AppOpenAds.activityList;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM_FINISH;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_PACKAGE_NAME;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import com.adsmodule.api.adsModule.preferencesManager.AppPreferences;
import com.adsmodule.api.adsModule.retrofit.AdsDataRequestModel;
import com.adsmodule.api.adsModule.utils.Global;
import com.applock.fingerprint.passwordlock.room.AppDatabase;
import com.applock.fingerprint.passwordlock.room.AppLockDao;
import com.applock.fingerprint.passwordlock.ui.activity.GestureUnlockActivity;
import com.applock.fingerprint.passwordlock.utils.CommLockInfoManager;
import com.applock.fingerprint.passwordlock.utils.SharePreferences;

import org.litepal.LitePal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {

    private static MyApplication instance;
    private static SharePreferences preferences;
    private static CommLockInfoManager lockInfoManager;
    private static ExecutorService executorService;
    private static AppLockDao lockDao;

    public static AppLockDao getLockDao(){
        if (lockDao == null)
            lockDao = AppDatabase.getInstance(getInstance()).lockDao();
        return lockDao;
    }

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

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "My Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
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
        try {
            for (Activity activity : activityList) {
                if (activity != null && !clearAllWhiteList(activity)) {
                    activity.finish();
                    activityList.remove(activity);
                }
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean clearAllWhiteList(Activity activity) {
        return activity instanceof GestureUnlockActivity;
    }

}
