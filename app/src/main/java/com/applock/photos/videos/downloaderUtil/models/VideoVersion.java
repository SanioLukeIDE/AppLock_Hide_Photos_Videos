package com.applock.photos.videos.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoVersion implements Serializable {
    @SerializedName("type")
    private long type;
    @SerializedName("width")
    private long width;
    @SerializedName("height")
    private long height;
    @SerializedName("url")
    private String url;
    @SerializedName("id")
    private String id;


    public VideoVersion() {
    }

    @SerializedName("type")
    public long getType() {
        return type;
    }

    @SerializedName("type")
    public void setType(long value) {
        this.type = value;
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

    @SerializedName("id")
    public String getid() {
        return id;
    }

    @SerializedName("id")
    public void setid(String value) {
        this.id = value;
    }
}
