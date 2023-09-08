package com.applock.photos.videos.service;

import static com.applock.photos.videos.utils.Const.APP_PACKAGE_NAME;
import static com.applock.photos.videos.utils.Const.LOCK_APART_MILLISENCONS;
import static com.applock.photos.videos.utils.Const.LOCK_AUTO_SCREEN;
import static com.applock.photos.videos.utils.Const.LOCK_AUTO_SCREEN_TIME;
import static com.applock.photos.videos.utils.Const.LOCK_CURR_MILLISENCONS;
import static com.applock.photos.videos.utils.Const.LOCK_FROM;
import static com.applock.photos.videos.utils.Const.LOCK_FROM_FINISH;
import static com.applock.photos.videos.utils.Const.LOCK_LAST_LOAD_PKG_NAME;
import static com.applock.photos.videos.utils.Const.LOCK_PACKAGE_NAME;
import static com.applock.photos.videos.utils.Const.LOCK_STATE;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.applock.photos.videos.R;
import com.applock.photos.videos.ui.activity.GestureUnlockActivity;
import com.applock.photos.videos.utils.CommLockInfoManager;
import com.applock.photos.videos.singletonClass.MyApplication;
import com.applock.photos.videos.utils.SharePreferences;

import java.util.ArrayList;
import java.util.List;

public class LockService extends IntentService {


    public LockService() {
        super("LockService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean threadIsTerminate = false;

    public static final String UNLOCK_ACTION = "UNLOCK_ACTION";
    public static final String LOCK_SERVICE_LASTTIME = "LOCK_SERVICE_LASTTIME";
    public static final String LOCK_SERVICE_LASTAPP = "LOCK_SERVICE_LASTAPP";

    private long lastUnlockTimeSeconds = 0;
    private String lastUnlockPackageName = "";

    private boolean lockState;

    private ServiceReceiver mServiceReceiver;
    private CommLockInfoManager mLockInfoManager;
    private ActivityManager activityManager;

    public static boolean isActionLock = false;
    public String savePkgName;
    SharePreferences preferences;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "MyServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = new SharePreferences(getApplicationContext());

        lockState = preferences.getFBoolean(LOCK_STATE);
        mLockInfoManager = new CommLockInfoManager(this);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        mServiceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(UNLOCK_ACTION);
        registerReceiver(mServiceReceiver, filter);

        threadIsTerminate = true;

        createNotificationChannel();


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        checkData();
//        startForeground(NOTIFICATION_ID, buildNotification());
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

    private Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Service")
                .setContentText("Running in the background")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setCategory(NotificationCompat.CATEGORY_SERVICE);

        return builder.build();
    }

    private void checkData() {
        while (threadIsTerminate) {
            String packageName = getLauncherTopApp(LockService.this, activityManager);

            if (lockState && !inWhiteList(packageName) && !TextUtils.isEmpty(packageName)) {
                boolean isLockOffScreenTime = preferences.getBoolean(LOCK_AUTO_SCREEN_TIME, false);
                boolean isLockOffScreen = preferences.getBoolean(LOCK_AUTO_SCREEN, false);
                savePkgName = preferences.getCustomString(LOCK_LAST_LOAD_PKG_NAME, "");
                if (isLockOffScreenTime && !isLockOffScreen) {
                    long time = preferences.getLong(LOCK_CURR_MILLISENCONS);
                    long leaverTime = preferences.getLong(LOCK_APART_MILLISENCONS);
                    if (!TextUtils.isEmpty(savePkgName)) {
                        if (!TextUtils.isEmpty(packageName)) {
                            if (!savePkgName.equals(packageName)) {
                                if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                                    boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                                    if (!isSetUnLock) {
                                        if (System.currentTimeMillis() - time > leaverTime) {
                                            mLockInfoManager.lockCommApplication(savePkgName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (isLockOffScreenTime && isLockOffScreen) {
                    long time = preferences.getLong(LOCK_CURR_MILLISENCONS);
                    long leaverTime = preferences.getLong(LOCK_APART_MILLISENCONS);
                    if (!TextUtils.isEmpty(savePkgName)) {
                        if (!TextUtils.isEmpty(packageName)) {
                            if (!savePkgName.equals(packageName)) {
                                if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                                    boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                                    if (!isSetUnLock) {
                                        if (System.currentTimeMillis() - time > leaverTime) {
                                            mLockInfoManager.lockCommApplication(savePkgName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (!isLockOffScreenTime && isLockOffScreen) {
                    if (!TextUtils.isEmpty(savePkgName)) {
                        if (!TextUtils.isEmpty(packageName)) {
                            if (!savePkgName.equals(packageName)) {
                                isActionLock = false;
                                if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                                    boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                                    if (!isSetUnLock) {
                                        mLockInfoManager.lockCommApplication(savePkgName);
                                    }
                                }
                            } else {
                                isActionLock = true;
                            }
                        }
                    }
                }

                if (!isLockOffScreenTime && !isLockOffScreen) {
                    if (!TextUtils.isEmpty(savePkgName)) {
                        if (!TextUtils.isEmpty(packageName)) {
                            if (!savePkgName.equals(packageName)) {
                                if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                                    boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                                    if (!isSetUnLock) {
                                        mLockInfoManager.lockCommApplication(savePkgName);
                                    }
                                }
                            }
                        }
                    }
                }

                if (mLockInfoManager.isLockedPackageName(packageName)) {
                    passwordLock(packageName);
                    continue;
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean inWhiteList(String packageName) {
        return packageName.equals(APP_PACKAGE_NAME) || packageName.equals("com.android.settings");
    }


    public class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            boolean isLockOffScreen = preferences.getBoolean(LOCK_AUTO_SCREEN, false);
            boolean isLockOffScreenTime = preferences.getBoolean(LOCK_AUTO_SCREEN_TIME, false);

            switch (action) {
                case UNLOCK_ACTION:
                    lastUnlockPackageName = intent.getStringExtra(LOCK_SERVICE_LASTAPP);
                    lastUnlockTimeSeconds = intent.getLongExtra(LOCK_SERVICE_LASTTIME, lastUnlockTimeSeconds);
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    preferences.putLong(LOCK_CURR_MILLISENCONS, System.currentTimeMillis());
                    if (!isLockOffScreenTime && isLockOffScreen) {
                        String savePkgName = preferences.getCustomString(LOCK_LAST_LOAD_PKG_NAME, "");
                        if (!TextUtils.isEmpty(savePkgName)) {
                            if (isActionLock) {
                                mLockInfoManager.lockCommApplication(lastUnlockPackageName);
                            }
                        }
                    }
                    break;
            }
        }
    }

    public String getLauncherTopApp(Context context, ActivityManager activityManager) {
        UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10000;
        String result = "";
        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                result = event.getPackageName();
            }
        }
        if (!TextUtils.isEmpty(result)) {
            return result;
        }
        return "";
    }


    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    private void passwordLock(String packageName) {
        MyApplication.getInstance().clearAllActivity();

        Intent intent = new Intent(this, GestureUnlockActivity.class);

        intent.putExtra(LOCK_PACKAGE_NAME, packageName);
        intent.putExtra(LOCK_FROM, LOCK_FROM_FINISH);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        threadIsTerminate = false;
        unregisterReceiver(mServiceReceiver);
    }*/
}
