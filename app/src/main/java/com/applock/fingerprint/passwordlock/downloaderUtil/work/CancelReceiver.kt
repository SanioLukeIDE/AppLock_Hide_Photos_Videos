package com.applock.fingerprint.passwordlock.downloaderUtil.work

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.WorkManager
import com.applock.fingerprint.passwordlock.R

class CancelReceiver : BroadcastReceiver() {
    private val TAG = "CancelReceiver"

    @SuppressLint("SuspiciousIndentation")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Task Canceler called")

        if (intent == null) return
        val taskId = intent.getStringExtra("taskId")
        val notificationId = intent.getIntExtra("notificationId", 0)
        if (taskId.isNullOrEmpty()) return
//        val result = YoutubeDL.getInstance().destroyProcessById(taskId)
//        if (result) {
            Log.d(TAG, "Task (id:$taskId) was killed.")
            WorkManager.getInstance(context!!).cancelAllWorkByTag(taskId)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as
                        NotificationManager?
            Toast.makeText(context, R.string.cancel, Toast.LENGTH_LONG).show()
            notificationManager?.cancel(notificationId)
//        }
    }
}