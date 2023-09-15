package com.applock.fingerprint.passwordlock.downloaderUtil.interfaces;

public interface VideoDownloader {

    String getVideoId(String link);

    void DownloadVideo();
}
