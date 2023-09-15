package com.applock.fingerprint.passwordlock.service;

import static com.applock.fingerprint.passwordlock.utils.Const.APP_PACKAGE_NAME;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_AUTO_SCREEN;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_AUTO_SCREEN_TIME;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_CURR_MILLISENCONS;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM_FINISH;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_LAST_LOAD_PKG_NAME;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_PACKAGE_NAME;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_STATE;
import static com.applock.fingerprint.passwordlock.utils.SharePreferences.isNonNull;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;
import com.applock.fingerprint.passwordlock.ui.activity.GestureUnlockActivity;
import com.applock.fingerprint.passwordlock.utils.CommLockInfoManager;
import com.applock.fingerprint.passwordlock.utils.SharePreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class LockService extends IntentService {

    public static final String UNLOCK_ACTION = "UNLOCK_ACTION";
    public static final String LOCK_SERVICE_LASTTIME = "LOCK_SERVICE_LASTTIME";
    public static final String LOCK_SERVICE_LASTAPP = "LOCK_SERVICE_LASTAPP";
    public static final String CHANNEL_ID = "MyServiceChannel";
    private static final String TAG = "LockService";
    private static final int NOTIFICATION_ID = 1;
    public static boolean isActionLock = false;
    public boolean threadIsTerminate = false;
    public String savePkgName;
    boolean isAppLocked = false;
    SharePreferences preferences;
    private long lastUnlockTimeSeconds = 0;
    private String lastUnlockPackageName = "";
    private boolean lockState;
    private ServiceReceiver mServiceReceiver;
    private CommLockInfoManager mLockInfoManager;
    private ActivityManager activityManager;

    public LockService() {
        super("LockService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        checkData();
        startForeground(NOTIFICATION_ID, buildNotification());
    }

    private Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name) + " Service")
                .setContentText("Running in the background")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setCategory(NotificationCompat.CATEGORY_SERVICE);

        return builder.build();
    }

    private void checkData() {
        while (threadIsTerminate) {
            String packageName = getLauncherTopApp(LockService.this);

            if (lockState && !inWhiteList(packageName) && !TextUtils.isEmpty(packageName)) {
                boolean isLockOffScreenTime = preferences.getBoolean(LOCK_AUTO_SCREEN_TIME, false);
                boolean isLockOffScreen = preferences.getBoolean(LOCK_AUTO_SCREEN, false);
                savePkgName = preferences.getCustomString(LOCK_LAST_LOAD_PKG_NAME, "");

                Log.e(TAG, "lastUnlockPackageName: " + lastUnlockPackageName);
                Log.e(TAG, "packageName: " + packageName);
                Log.e(TAG, "mLockInfoManager: " + mLockInfoManager.isLockedPackageName(packageName));
                if (!lastUnlockPackageName.equals(packageName) && mLockInfoManager.isLockedPackageName(packageName)) {
                    passwordLock(packageName);
                    isAppInBackground(getApplicationContext(), "first");
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

    public String getLauncherTopApp(Context context) {
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

    public void isAppInBackground(Context context, String unlockAction) {
        Log.e("isAppInBackground: ", lastUnlockPackageName + "  -->  " + unlockAction);
        long currentTime = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, currentTime - 5000, currentTime);

        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            SortedMap<Long, UsageStats> sortedMap = new TreeMap<>();
            for (UsageStats usageStats : usageStatsList) {
                sortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }

            if (!sortedMap.isEmpty()) {
                String currentPackageName = sortedMap.get(sortedMap.lastKey()).getPackageName();
                if (!isNonNull(lastUnlockPackageName) && !currentPackageName.equals(lastUnlockPackageName)) {
                    lastUnlockPackageName = "";
                    return;
                }
                lastUnlockPackageName = currentPackageName;
            }
        }
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

        Intent intent = new Intent(getApplicationContext(), GestureUnlockActivity.class);
        intent.putExtra(LOCK_PACKAGE_NAME, packageName);
        intent.putExtra(LOCK_FROM, LOCK_FROM_FINISH);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadIsTerminate = false;
        unregisterReceiver(mServiceReceiver);
    }

    public class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Objects.equals(action, UNLOCK_ACTION)) {
//            lastUnlockPackageName = intent.getStringExtra(LOCK_SERVICE_LASTAPP);
                isAppInBackground(getApplicationContext(), UNLOCK_ACTION);
                String savePkgName = preferences.getString(LOCK_LAST_LOAD_PKG_NAME);
                lastUnlockTimeSeconds = intent.getLongExtra(LOCK_SERVICE_LASTTIME, lastUnlockTimeSeconds);
                preferences.putLong(LOCK_CURR_MILLISENCONS, System.currentTimeMillis());
                mLockInfoManager.lockCommApplication(savePkgName);
            }
        }
    }
}
