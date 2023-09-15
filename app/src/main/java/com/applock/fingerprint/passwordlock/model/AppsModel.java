package com.applock.fingerprint.passwordlock.model;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppsModel implements Serializable {

    private String appName, packageName, versionName, versionCode, className;
    private Drawable iconString;
    private boolean isLocked, isRecommended;

    public AppsModel(String appName, String packageName, Drawable icon) {
        this.appName = appName;
        this.packageName = packageName;
        this.iconString = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Drawable getIcon() {
        return iconString;
    }

    public void setIcon(Drawable icon) {
        this.iconString = icon;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public ComponentName getComponentName() {
        return new ComponentName(packageName, className);
    }
}

