package com.applock.fingerprint.passwordlock.downloaderUtil.models;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class DLDataParser {
    private String url;
    private List<Video> videos;

    public String getURL() {
        return url;
    }

    public void setURL(String value) {
        this.url = value;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> value) {
        this.videos = value;
    }
}
