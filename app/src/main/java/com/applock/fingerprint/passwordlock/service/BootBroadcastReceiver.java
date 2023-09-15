package com.applock.fingerprint.passwordlock.service;

import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_STATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LoadAppListService.class));
        if (MyApplication.getPreferences().getFBoolean(LOCK_STATE)) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, LockService.class));
            } else*/ context.startService(new Intent(context, LockService.class));
        }
    }
}
