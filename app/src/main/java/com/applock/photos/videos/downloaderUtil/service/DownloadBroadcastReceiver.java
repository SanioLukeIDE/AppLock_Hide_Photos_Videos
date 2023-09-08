package com.applock.photos.videos.downloaderUtil.service;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    public static boolean isFirstTimeDownload = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadId == -1)
                return;

            // query download status
            Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                    // download is successful
                    if (!isFirstTimeDownload) {
                        DownloadBroadcastReceiver.isFirstTimeDownload = true;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            MyFirebaseMessagingService.sendOreoNotification(context, context.getResources().getString(R.string.app_name), context.getResources().getString(R.string.yourdoncomple));
//                        } else {
//                            MyFirebaseMessagingService.sendNotification(context, context.getResources().getString(R.string.app_name), context.getResources().getString(R.string.yourdoncomple));
//                        }
                    }
                } else {
                    Log.d("error", "error");

                }
            } else {
                Log.d("error", "error");

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
