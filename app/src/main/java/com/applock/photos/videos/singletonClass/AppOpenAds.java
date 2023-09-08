package com.applock.photos.videos.singletonClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.utils.Constants;
import com.applock.photos.videos.ui.activity.SplashActivity;

public class AppOpenAds implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    @SuppressLint("StaticFieldLeak")
    public static Activity currentActivity;
    MyApplication application;
    boolean isAdShowing;
    Bundle bundle;

    public AppOpenAds(MyApplication application) {
        this.application = application;
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        isAdShowing = false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentActivity = activity;
        bundle = savedInstanceState;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (Constants.adsResponseModel != null && Constants.adsResponseModel.isShow_ads()) {
            if (!isAdShowing && currentActivity != null && (!currentActivity.getClass().getName().equals(SplashActivity.class.getName()))) {
                isAdShowing = true;

                AdUtils.showAppOpenAds(Constants.adsResponseModel.getApp_open_ads().getAdx(), currentActivity, state_load -> {
                    isAdShowing = false;
                });

            } else {
                isAdShowing = false;
            }
        }

    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }


}
