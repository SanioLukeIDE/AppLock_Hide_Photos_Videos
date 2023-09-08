package com.applock.photos.videos.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Candidate implements Serializable {
    @SerializedName("width")
    private long width;
    @SerializedName("height")
    private long height;
    @SerializedName("url")
    private String url;

    public Candidate() {
    }

    @SerializedName("width")
    public long getWidth() {
        return width;
    }

    @SerializedName("width")
    public void setWidth(long value) {
        this.width = value;
    }

    @SerializedName("height")
    public long getHeight() {
        return height;
    }

    @SerializedName("height")
    public void setHeight(long value) {
        this.height = value;
    }

    @SerializedName("url")
    public String geturl() {
        return url;
    }

    @SerializedName("url")
    public void seturl(String value) {
        this.url = value;
    }
}
