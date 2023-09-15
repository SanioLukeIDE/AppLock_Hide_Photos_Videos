package com.applock.fingerprint.passwordlock.downloaderUtil.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoryID implements Serializable {
    @SerializedName("value")
    private String value;

    @SerializedName("value")
    public String getValue() {
        return value;
    }

    @SerializedName("value")
    public void setValue(String value) {
        this.value = value;
    }
}
