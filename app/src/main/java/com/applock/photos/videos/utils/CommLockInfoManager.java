package com.applock.photos.videos.utils;


import static com.applock.photos.videos.utils.Const.APP_PACKAGE_NAME;
import static com.applock.photos.videos.utils.Utility.clearRepeatCommLockInfo;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.applock.photos.videos.model.CommLockInfo;
import com.applock.photos.videos.model.FavoriteInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CommLockInfoManager {

    private PackageManager mPackageManager;
    Context mContext;

    public CommLockInfoManager(Context mContext) {
        this.mContext = mContext;
        mPackageManager = mContext.getPackageManager();
    }


    public synchronized List<CommLockInfo> getAllCommLockInfos() {
        List<CommLockInfo> commLockInfos = DataSupport.findAll(CommLockInfo.class);
        commLockInfos.sort(commLockInfoComparator);
        return commLockInfos;
    }

    public synchronized void deleteCommLockInfoTable(List<CommLockInfo> commLockInfos) {
        for (CommLockInfo info : commLockInfos) {
            DataSupport.deleteAll(CommLockInfo.class, "packageName = ?", info.getPackageName());
        }
    }

    public synchronized void instanceCommLockInfoTable(List<ResolveInfo> resolveInfos) throws PackageManager.NameNotFoundException {
        List<CommLockInfo> list = new ArrayList<>();

        for (ResolveInfo resolveInfo : resolveInfos) {
            boolean isFavoriteApp = isHasFavoriteAppInfo(resolveInfo.activityInfo.packageName);
            CommLockInfo commLockInfo = new CommLockInfo(resolveInfo.activityInfo.packageName, false, isFavoriteApp);
            String appName = String.valueOf(resolveInfo.loadLabel(mPackageManager));
            if (!commLockInfo.getPackageName().equals(APP_PACKAGE_NAME) && !commLockInfo.getPackageName().equals("com.android.settings") && !commLockInfo.getPackageName().equals("com.google.android.googlequicksearchbox")) {
                commLockInfo.setLocked(isFavoriteApp);
                commLockInfo.setAppName(appName);
                commLockInfo.setSetUnLock(false);

                list.add(commLockInfo);
            }
        }
        list = clearRepeatCommLockInfo(list);
        list.sort((t1, t2) -> t1.getAppName().toLowerCase().compareToIgnoreCase(t2.getAppName().toLowerCase()));

        DataSupport.saveAll(list);
    }


    public boolean isHasFavoriteAppInfo(String packageName) {
        List<String> list = MyApp.getPreferences().getFavoriteApps();
        for (String name : list){
            if (packageName.equals(name))
                return true;
        }
        return false;
    }


    public void lockCommApplication(String packageName) {
        updateLockStatus(packageName, true);
    }

    public void unlockCommApplication(String packageName) {
        updateLockStatus(packageName, false);
    }

    public void updateLockStatus(String packageName, boolean isLock) {
        ContentValues values = new ContentValues();
        values.put("isLocked", isLock);
        DataSupport.updateAll(CommLockInfo.class, values, "packageName = ?", packageName);
    }

    public boolean isSetUnLock(String packageName) {
        List<CommLockInfo> lockInfos = DataSupport.where("packageName = ?", packageName).find(CommLockInfo.class);
        for (CommLockInfo commLockInfo : lockInfos) {
            if (commLockInfo.isSetUnLock()) {
                return true;
            }
        }
        return false;
    }

    public boolean isLockedPackageName(String packageName) {
        List<CommLockInfo> lockInfos = DataSupport.where("packageName = ?", packageName).find(CommLockInfo.class);
        for (CommLockInfo commLockInfo : lockInfos) {
            if (commLockInfo.isLocked()) {
                return true;
            }
        }
        return false;
    }

    public List<CommLockInfo> queryBlurryList(String appName) {
        return DataSupport.where("appName like ?", "%" + appName + "%").find(CommLockInfo.class);
    }

    public void setIsUnLockThisApp(String packageName, boolean isSetUnLock) {
        ContentValues values = new ContentValues();
        values.put("isSetUnLock", isSetUnLock);
        DataSupport.updateAll(CommLockInfo.class, values, "packageName = ?", packageName);
    }


    private Comparator commLockInfoComparator = (lhs, rhs) -> {
        CommLockInfo leftCommLockInfo = (CommLockInfo) lhs;
        CommLockInfo rightCommLockInfo = (CommLockInfo) rhs;

        if (leftCommLockInfo.isFavoriteApp()
                && !leftCommLockInfo.isLocked()
                && !rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            return -1;
        } else if (leftCommLockInfo.isFavoriteApp()
                && leftCommLockInfo.isLocked()
                && !rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            return -1;
        } else if (leftCommLockInfo.isFavoriteApp()
                && leftCommLockInfo.isLocked()
                && rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            if (leftCommLockInfo.getAppInfo() != null
                    && rightCommLockInfo.getAppInfo() != null)
                return 1;
            else
                return 0;
        } else if (!leftCommLockInfo.isFavoriteApp()
                && leftCommLockInfo.isLocked()
                && !rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            return -1;
        } else if (leftCommLockInfo.isFavoriteApp()
                && leftCommLockInfo.isLocked()
                && !rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            return -1;
        } else if (leftCommLockInfo.isFavoriteApp()
                && leftCommLockInfo.isLocked()
                && rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            if (leftCommLockInfo.getAppInfo() != null
                    && rightCommLockInfo.getAppInfo() != null)
                return 1;
            else
                return 0;
        } else if (!leftCommLockInfo.isFavoriteApp()
                && !leftCommLockInfo.isLocked()
                && rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            return 1;
        } else if (leftCommLockInfo.isFavoriteApp()
                && !leftCommLockInfo.isLocked()
                && rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            if (leftCommLockInfo.getAppInfo() != null
                    && rightCommLockInfo.getAppInfo() != null)
                return 1;
            else
                return 0;
        } else if (leftCommLockInfo.isFavoriteApp()
                && leftCommLockInfo.isLocked()
                && rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            if (leftCommLockInfo.getAppInfo() != null
                    && rightCommLockInfo.getAppInfo() != null)
                return 1;
            else
                return 0;
        } else if (!leftCommLockInfo.isFavoriteApp()
                && !leftCommLockInfo.isLocked()
                && !rightCommLockInfo.isFavoriteApp()
                && rightCommLockInfo.isLocked()) {
            return 1;
        } else if (!leftCommLockInfo.isFavoriteApp()
                && !leftCommLockInfo.isLocked()
                && rightCommLockInfo.isFavoriteApp()
                && rightCommLockInfo.isLocked()) {
            return 1;
        } else if (!leftCommLockInfo.isFavoriteApp()
                && leftCommLockInfo.isLocked()
                && rightCommLockInfo.isFavoriteApp()
                && rightCommLockInfo.isLocked()) {
            return 1;
        } else if (!leftCommLockInfo.isFavoriteApp()
                && !leftCommLockInfo.isLocked()
                && !rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            if (leftCommLockInfo.getAppInfo() != null
                    && rightCommLockInfo.getAppInfo() != null)
                return 1;
            else
                return 0;
        } else if (leftCommLockInfo.isFavoriteApp()
                && leftCommLockInfo.isLocked()
                && rightCommLockInfo.isFavoriteApp()
                && rightCommLockInfo.isLocked()) {
            if (leftCommLockInfo.getAppInfo() != null
                    && rightCommLockInfo.getAppInfo() != null)
                return 1;
            else
                return 0;
        } else if (!leftCommLockInfo.isFavoriteApp()
                && !leftCommLockInfo.isLocked()
                && rightCommLockInfo.isFavoriteApp()
                && rightCommLockInfo.isLocked()) {
            return 1;
        } else if (!leftCommLockInfo.isFavoriteApp()
                && leftCommLockInfo.isLocked()
                && !rightCommLockInfo.isFavoriteApp()
                && !rightCommLockInfo.isLocked()) {
            return -1;
        }
        return 0;
    };

}
