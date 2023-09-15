package com.applock.fingerprint.passwordlock.downloaderUtil.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.applock.fingerprint.passwordlock.R
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.Constants.directoryInstaShoryDirectorydownload_audio
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.Constants.directoryInstaShoryDirectorydownload_images
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.Constants.directoryInstaShoryDirectorydownload_videos
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.iUtils
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.iUtils.createFilenameWithJapneseAndOthers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DownloadWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager?

    override suspend fun doWork(): Result {

        val url = inputData.getString(urlKey)!!
        val name = createFilenameWithJapneseAndOthers(inputData.getString(nameKey)!!)
        println("myfilepath nameeAFTER = $name")
        val formatId = inputData.getString(formatIdKey)!!
        val acodec = inputData.getString(acodecKey)
        val vcodec = inputData.getString(vcodecKey)
        val taskId = inputData.getString(taskIdKey)!!
        val ext = inputData.getString(ext)!!

        createNotificationChannel()
        val notificationId = id.hashCode()
        val notification = NotificationCompat.Builder(
            applicationContext,
            iUtils.myNotificationChannel
        )
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(name)
            .setSound(null)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setContentText(applicationContext.getString(R.string.don_start))
            .build()

        val foregroundInfo = ForegroundInfo(notificationId, notification)
        setForeground(foregroundInfo)

//        val request = YoutubeDLRequest(url)


        try {
            val file_v = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + directoryInstaShoryDirectorydownload_videos
            )
            if (!file_v.exists()) {
                file_v.mkdir()
            }
            val file_i = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + directoryInstaShoryDirectorydownload_images
            )
            if (!file_i.exists()) {
                file_i.mkdir()
            }
            val file_a = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + directoryInstaShoryDirectorydownload_audio
            )
            if (!file_a.exists()) {
                file_a.mkdir()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

        var mypath: String = ""
        var myseleext = ""

        myseleext = ext
        println("myfilepath ext = $ext")

        when (ext) {
            "png", "jpg", "gif", "jpeg" -> {
                mypath =
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_DOWNLOADS + directoryInstaShoryDirectorydownload_images
            }

            "mp4", "webm" -> {
                mypath =
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_DOWNLOADS + directoryInstaShoryDirectorydownload_videos

            }

            "mp3", "m4a", "wav" -> {
                mypath =
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_DOWNLOADS + directoryInstaShoryDirectorydownload_audio
            }
        }


        val tmpFile = withContext(Dispatchers.IO) {
            File.createTempFile("allvideodd", null, applicationContext.externalCacheDir)
        }
        tmpFile.delete()
        tmpFile.mkdir()
        tmpFile.deleteOnExit()

        println("myfilepath folder = " + tmpFile.absolutePath)

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel =
                notificationManager?.getNotificationChannel(iUtils.myNotificationChannel)
            if (notificationChannel == null) {
                val channelName = applicationContext.getString(R.string.app_name)
                notificationChannel = NotificationChannel(
                    iUtils.myNotificationChannel,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
                notificationChannel.description =
                    channelName
                notificationManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    companion object {
        const val urlKey = "url"
        const val nameKey = "name"
        const val formatIdKey = "formatId"
        const val acodecKey = "acodec"
        const val vcodecKey = "vcodec"
        const val taskIdKey = "taskId"
        const val ext = "ext"
    }
}