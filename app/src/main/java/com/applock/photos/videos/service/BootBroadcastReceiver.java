package com.applock.photos.videos.service;

import static com.applock.photos.videos.utils.Const.LOCK_STATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.applock.photos.videos.utils.MyApp;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LoadAppListService.class));
        if (MyApp.getPreferences().getBoolean(LOCK_STATE, false)) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, LockService.class));
            } else*/ context.startService(new Intent(context, LockService.class));
        }
    }
}
