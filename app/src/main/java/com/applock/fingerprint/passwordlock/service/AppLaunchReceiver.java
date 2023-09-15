package com.applock.fingerprint.passwordlock.service;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class AppLaunchReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if the action is a package added or replaced event
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction()) ||
                Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {

            String packageName = intent.getData().getEncodedSchemeSpecificPart();

            // Check if the package name matches your app's package name
            if (packageName != null && packageName.equals(context.getPackageName())) {
                // Your app has been launched

                // Check for permission to access package usage stats
                if (hasUsageStatsPermission(context)) {
                    Toast.makeText(context, "Your app has been launched.", Toast.LENGTH_SHORT).show();
                } else {
                    // Request the permission or inform the user to grant it
                    Toast.makeText(context, "Your app has been launched, but PACKAGE_USAGE_STATS permission is not granted.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return (mode == AppOpsManager.MODE_ALLOWED);
    }
}
