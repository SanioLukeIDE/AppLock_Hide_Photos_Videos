package com.applock.fingerprint.passwordlock.ui.ext;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.applock.fingerprint.passwordlock.service.LockService.CHANNEL_ID;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_STATE;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.service.LoadAppListService;
import com.applock.fingerprint.passwordlock.service.LockService;
import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;

public class BackupWorker extends Worker {

    private static final String TAG = "BackupWorker";

    public BackupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static boolean isServiceRunning(String serviceName, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public Result doWork() {

        boolean isRunning = isServiceRunning(LockService.class.getName(), getApplicationContext());
        Intent serviceIntent = new Intent(getApplicationContext(), LockService.class);
        if (!isRunning) {
            getApplicationContext().startService(serviceIntent);
        }

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1100, buildNotification());

        return Result.success();
    }

    private Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).setContentTitle(getApplicationContext().getString(R.string.app_name) + " Service").setContentText("Running in the background").setSmallIcon(R.mipmap.ic_launcher_round).setPriority(NotificationCompat.PRIORITY_DEFAULT).setVisibility(NotificationCompat.VISIBILITY_SECRET).setCategory(NotificationCompat.CATEGORY_SERVICE);

        return builder.build();
    }


    public void startService() {
        getApplicationContext().startService(new Intent(getApplicationContext(), LoadAppListService.class));
        if (MyApplication.getPreferences().getFBoolean(LOCK_STATE)) {
            Intent serviceIntent = new Intent(getApplicationContext(), LockService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplicationContext().startForegroundService(serviceIntent);
            } else {
                getApplicationContext().startService(serviceIntent);
            }
        }
    }

}