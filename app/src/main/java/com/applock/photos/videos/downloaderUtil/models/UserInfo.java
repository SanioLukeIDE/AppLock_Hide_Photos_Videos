package com.applock.photos.videos.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserInfo implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("displayName")
    private String displayName;
    @SerializedName("snapcodeImageUrl")
    private String snapcodeImageURL;

    @SerializedName("username")
    public String getUsername() {
        return username;
    }

    @SerializedName("username")
    public void setUsername(String value) {
        this.username = value;
    }

    @SerializedName("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @SerializedName("displayName")
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    @SerializedName("snapcodeImageUrl")
    public String getSnapcodeImageURL() {
        return snapcodeImageURL;
    }

    @SerializedName("snapcodeImageUrl")
    public void setSnapcodeImageURL(String value) {
        this.snapcodeImageURL = value;
    }

}
