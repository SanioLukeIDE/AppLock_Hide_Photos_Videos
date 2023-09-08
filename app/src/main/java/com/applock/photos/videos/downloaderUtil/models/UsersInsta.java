/*
 * *
 *  * Created by Syed Usama Ahmad on 1/24/23, 5:00 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 1/24/23, 4:54 PM
 *
 */

package com.applock.photos.videos.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsersInsta implements Serializable {

    @SerializedName("pk")
    private String pk;

    @SerializedName("username")
    private String username;
    @SerializedName("profile_pic_url")
    private String profile_pic_url;

    public UsersInsta() {
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getUsername() {
        return username;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
