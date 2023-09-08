package com.applock.photos.videos.downloaderUtil.interfaces;

public interface VideoDownloader {

    String getVideoId(String link);

    void DownloadVideo();
}
