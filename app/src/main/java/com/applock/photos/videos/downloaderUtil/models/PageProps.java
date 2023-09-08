package com.applock.photos.videos.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PageProps implements Serializable {
    @SerializedName("userProfile")
    private UserProfile userProfile;
    @SerializedName("curatedHighlights")
    private List<Highlight> curatedHighlights;
    @SerializedName("spotlightHighlights")
    private List<Highlight> spotlightHighlights;

    @SerializedName("userProfile")
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @SerializedName("userProfile")
    public void setUserProfile(UserProfile value) {
        this.userProfile = value;
    }

    @SerializedName("curatedHighlights")
    public List<Highlight> getCuratedHighlights() {
        return curatedHighlights;
    }

    @SerializedName("curatedHighlights")
    public void setCuratedHighlights(List<Highlight> value) {
        this.curatedHighlights = value;
    }

    @SerializedName("spotlightHighlights")
    public List<Highlight> getSpotlightHighlights() {
        return spotlightHighlights;
    }

    @SerializedName("spotlightHighlights")
    public void setSpotlightHighlights(List<Highlight> value) {
        this.spotlightHighlights = value;
    }
}
