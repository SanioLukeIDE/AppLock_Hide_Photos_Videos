package com.applock.photos.videos.downloaderUtil.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SnapUrls implements Serializable {
    @SerializedName("mediaUrl")
    private String mediaURL;
    @SerializedName("mediaPreviewUrl")
    private StoryID mediaPreviewURL;

    @SerializedName("mediaUrl")
    public String getMediaURL() {
        return mediaURL;
    }

    @SerializedName("mediaUrl")
    public void setMediaURL(String value) {
        this.mediaURL = value;
    }

    @SerializedName("mediaPreviewUrl")
    public StoryID getMediaPreviewURL() {
        return mediaPreviewURL;
    }

    @SerializedName("mediaPreviewUrl")
    public void setMediaPreviewURL(StoryID value) {
        this.mediaPreviewURL = value;
    }
}
