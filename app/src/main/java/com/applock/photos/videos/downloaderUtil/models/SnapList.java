package com.applock.photos.videos.downloaderUtil.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SnapList implements Serializable {
    @SerializedName("snapIndex")
    private long snapIndex;
    @SerializedName("snapId")
    private StoryID snapID;
    @SerializedName("snapMediaType")
    private long snapMediaType;
    @SerializedName("snapUrls")
    private SnapUrls snapUrls;
    @SerializedName("timestampInSec")
    private StoryID timestampInSEC;

    @SerializedName("snapIndex")
    public long getSnapIndex() {
        return snapIndex;
    }

    @SerializedName("snapIndex")
    public void setSnapIndex(long value) {
        this.snapIndex = value;
    }

    @SerializedName("snapId")
    public StoryID getSnapID() {
        return snapID;
    }

    @SerializedName("snapId")
    public void setSnapID(StoryID value) {
        this.snapID = value;
    }

    @SerializedName("snapMediaType")
    public long getSnapMediaType() {
        return snapMediaType;
    }

    @SerializedName("snapMediaType")
    public void setSnapMediaType(long value) {
        this.snapMediaType = value;
    }

    @SerializedName("snapUrls")
    public SnapUrls getSnapUrls() {
        return snapUrls;
    }

    @SerializedName("snapUrls")
    public void setSnapUrls(SnapUrls value) {
        this.snapUrls = value;
    }

    @SerializedName("timestampInSec")
    public StoryID getTimestampInSEC() {
        return timestampInSEC;
    }

    @SerializedName("timestampInSec")
    public void setTimestampInSEC(StoryID value) {
        this.timestampInSEC = value;
    }
}
