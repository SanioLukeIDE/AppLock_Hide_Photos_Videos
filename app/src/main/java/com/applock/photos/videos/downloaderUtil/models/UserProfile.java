package com.applock.photos.videos.downloaderUtil.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserProfile implements Serializable {
    @SerializedName("$case")
    private String userProfileCase;
    @SerializedName("userInfo")
    private UserInfo userInfo;
    @SerializedName("publicProfileInfo")
    private PublicProfileInfo publicProfileInfo;

    @SerializedName("$case")
    public String getUserProfileCase() {
        return userProfileCase;
    }

    @SerializedName("$case")
    public void setUserProfileCase(String value) {
        this.userProfileCase = value;
    }


    @SerializedName("userInfo")
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @SerializedName("userInfo")
    public void setUserInfo(UserInfo value) {
        this.userInfo = value;
    }

    @SerializedName("publicProfileInfo")
    public PublicProfileInfo getPublicProfileInfo() {
        return publicProfileInfo;
    }

    @SerializedName("publicProfileInfo")
    public void setPublicProfileInfo(PublicProfileInfo value) {
        this.publicProfileInfo = value;
    }
}
