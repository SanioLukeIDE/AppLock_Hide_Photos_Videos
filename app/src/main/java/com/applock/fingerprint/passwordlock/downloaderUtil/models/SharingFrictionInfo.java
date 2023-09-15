package com.applock.fingerprint.passwordlock.downloaderUtil.models;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class SharingFrictionInfo implements Serializable {
    boolean should_have_sharing_friction;
    Object bloks_app_url;

    @SerializedName("should_have_sharing_friction")
    public boolean getShould_have_sharing_friction() {
        return this.should_have_sharing_friction;
    }

    public void setShould_have_sharing_friction(boolean should_have_sharing_friction) {
        this.should_have_sharing_friction = should_have_sharing_friction;
    }

    @SerializedName("bloks_app_url")
    public Object getBloks_app_url() {
        return this.bloks_app_url;
    }

    public void setBloks_app_url(Object bloks_app_url) {
        this.bloks_app_url = bloks_app_url;
    }
}
