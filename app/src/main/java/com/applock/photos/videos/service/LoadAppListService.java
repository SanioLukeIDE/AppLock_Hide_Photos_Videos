package com.applock.photos.videos.service;

import static com.applock.photos.videos.utils.Const.APP_PACKAGE_NAME;
import static com.applock.photos.videos.utils.Const.LOCK_IS_INIT_DB;
import static com.applock.photos.videos.utils.Const.LOCK_IS_INIT_FAVITER;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.applock.photos.videos.model.CommLockInfo;
import com.applock.photos.videos.utils.CommLockInfoManager;
import com.applock.photos.videos.singletonClass.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadAppListService extends IntentService {

    private PackageManager mPackageManager;
    private CommLockInfoManager mLockInfoManager;
    long time = 0;

    public LoadAppListService() {
        super("LoadAppListService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPackageManager = getPackageManager();
        mLockInfoManager = new CommLockInfoManager(this);
    }


    @Override
    protected void onHandleIntent(Intent handleIntent) {
        checkData();
    }

    private void checkData() {
        time = System.currentTimeMillis();

        boolean isInitFavorite = MyApplication.getPreferences().getBoolean(LOCK_IS_INIT_FAVITER, false);
        boolean isInitDb = MyApplication.getPreferences().getBoolean(LOCK_IS_INIT_DB, false);

        if (!isInitFavorite) {
            MyApplication.getPreferences().putBoolean(LOCK_IS_INIT_FAVITER, true);
            initFavoriteApps();
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = mPackageManager.queryIntentActivities(intent, 0);
        if (isInitDb) {
            List<ResolveInfo> appList = new ArrayList<>();
            List<CommLockInfo> dbList = mLockInfoManager.getAllCommLockInfos();
            for (ResolveInfo resolveInfo : resolveInfoList) {
                if (!resolveInfo.activityInfo.packageName.equals(APP_PACKAGE_NAME) &&
                        !resolveInfo.activityInfo.packageName.equals("com.android.settings")) {
                    appList.add(resolveInfo);
                }
            }
            if (appList.size() > dbList.size()) {
                List<ResolveInfo> reslist = new ArrayList<>();
                HashMap<String, CommLockInfo> hashMap = new HashMap<>();
                for (CommLockInfo info : dbList) {
                    hashMap.put(info.getPackageName(), info);
                }
                for (ResolveInfo info : appList) {
                    if (!hashMap.containsKey(info.activityInfo.packageName)) {
                        reslist.add(info);
                    }
                }
                try {
                    if (reslist.size() != 0)
                        mLockInfoManager.instanceCommLockInfoTable(reslist);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (appList.size() < dbList.size()) {
                List<CommLockInfo> commlist = new ArrayList<>();
                HashMap<String, ResolveInfo> hashMap = new HashMap<>();
                for (ResolveInfo info : appList) {
                    hashMap.put(info.activityInfo.packageName, info);
                }
                for (CommLockInfo info : dbList) {
                    if (!hashMap.containsKey(info.getPackageName())) {
                        commlist.add(info);
                    }
                }
                if (commlist.size() != 0)
                    mLockInfoManager.deleteCommLockInfoTable(commlist);
            }
        } else {
            MyApplication.getPreferences().putBoolean(LOCK_IS_INIT_DB, true);
            try {
                mLockInfoManager.instanceCommLockInfoTable(resolveInfoList);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLockInfoManager = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void initFavoriteApps() {
        List<String> packageList = new ArrayList<>();

        packageList.add("com.whatsapp");
        packageList.add("com.android.mms");
        packageList.add("com.instagram.android");
        packageList.add("com.android.contacts");
        packageList.add("com.facebook.katana");
        packageList.add("com.facebook.orca");
        packageList.add("com.google.android.apps.nbu.files");
        packageList.add("com.sec.android.gallery3d");
        packageList.add("com.google.android.gm");
        packageList.add("com.sec.android.app.myfiles");
        packageList.add("com.android.chrome");
        packageList.add("com.google.android.youtube");
        packageList.add("com.google.android.apps.messaging");
        packageList.add("com.tencent.qq");
        packageList.add("com.android.dialer");
        packageList.add("com.twitter.android");

        MyApplication.getPreferences().setFavoriteApps(packageList);
    }
}
