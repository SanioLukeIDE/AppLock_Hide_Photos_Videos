package com.applock.photos.videos.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Highlight implements Serializable {
    @SerializedName("storyType")
    private long storyType;
    @SerializedName("snapList")
    private List<SnapList> snapList;
    @SerializedName("storyId")
    private StoryID storyID;
    @SerializedName("storyTitle")
    private StoryID storyTitle;
    @SerializedName("thumbnailUrl")
    private StoryID thumbnailURL;
    @SerializedName("storyTapId")
    private String storyTapID;

    @SerializedName("storyType")
    public long getStoryType() {
        return storyType;
    }

    @SerializedName("storyType")
    public void setStoryType(long value) {
        this.storyType = value;
    }

    @SerializedName("snapList")
    public List<SnapList> getSnapList() {
        return snapList;
    }

    @SerializedName("snapList")
    public void setSnapList(List<SnapList> value) {
        this.snapList = value;
    }

    @SerializedName("storyId")
    public StoryID getStoryID() {
        return storyID;
    }

    @SerializedName("storyId")
    public void setStoryID(StoryID value) {
        this.storyID = value;
    }

    @SerializedName("storyTitle")
    public StoryID getStoryTitle() {
        return storyTitle;
    }

    @SerializedName("storyTitle")
    public void setStoryTitle(StoryID value) {
        this.storyTitle = value;
    }

    @SerializedName("thumbnailUrl")
    public StoryID getThumbnailURL() {
        return thumbnailURL;
    }

    @SerializedName("thumbnailUrl")
    public void setThumbnailURL(StoryID value) {
        this.thumbnailURL = value;
    }

    @SerializedName("storyTapId")
    public String getStoryTapID() {
        return storyTapID;
    }

    @SerializedName("storyTapId")
    public void setStoryTapID(String value) {
        this.storyTapID = value;
    }
}
