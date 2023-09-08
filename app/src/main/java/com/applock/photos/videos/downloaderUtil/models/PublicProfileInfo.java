package com.applock.photos.videos.downloaderUtil.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PublicProfileInfo implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("title")
    private String title;
    @SerializedName("snapcodeImageUrl")
    private String snapcodeImageURL;
    @SerializedName("badge")
    private long badge;
    @SerializedName("categoryStringId")
    private String categoryStringID;
    @SerializedName("subcategoryStringId")
    private String subcategoryStringID;
    @SerializedName("subscriberCount")
    private String subscriberCount;
    @SerializedName("bio")
    private String bio;
    @SerializedName("websiteUrl")
    private String websiteURL;
    @SerializedName("profilePictureUrl")
    private String profilePictureURL;
    @SerializedName("address")
    private String address;
    @SerializedName("hasCuratedHighlights")
    private boolean hasCuratedHighlights;
    @SerializedName("hasSpotlightHighlights")
    private boolean hasSpotlightHighlights;
    @SerializedName("mutableName")
    private String mutableName;
    @SerializedName("publisherType")
    private String publisherType;
    @SerializedName("squareHeroImageUrl")
    private String squareHeroImageURL;
    @SerializedName("primaryColor")
    private String primaryColor;

    @SerializedName("username")
    public String getUsername() {
        return username;
    }

    @SerializedName("username")
    public void setUsername(String value) {
        this.username = value;
    }

    @SerializedName("title")
    public String getTitle() {
        return title;
    }

    @SerializedName("title")
    public void setTitle(String value) {
        this.title = value;
    }

    @SerializedName("snapcodeImageUrl")
    public String getSnapcodeImageURL() {
        return snapcodeImageURL;
    }

    @SerializedName("snapcodeImageUrl")
    public void setSnapcodeImageURL(String value) {
        this.snapcodeImageURL = value;
    }

    @SerializedName("badge")
    public long getBadge() {
        return badge;
    }

    @SerializedName("badge")
    public void setBadge(long value) {
        this.badge = value;
    }

    @SerializedName("categoryStringId")
    public String getCategoryStringID() {
        return categoryStringID;
    }

    @SerializedName("categoryStringId")
    public void setCategoryStringID(String value) {
        this.categoryStringID = value;
    }

    @SerializedName("subcategoryStringId")
    public String getSubcategoryStringID() {
        return subcategoryStringID;
    }

    @SerializedName("subcategoryStringId")
    public void setSubcategoryStringID(String value) {
        this.subcategoryStringID = value;
    }

    @SerializedName("subscriberCount")
    public String getSubscriberCount() {
        return subscriberCount;
    }

    @SerializedName("subscriberCount")
    public void setSubscriberCount(String value) {
        this.subscriberCount = value;
    }

    @SerializedName("bio")
    public String getBio() {
        return bio;
    }

    @SerializedName("bio")
    public void setBio(String value) {
        this.bio = value;
    }

    @SerializedName("websiteUrl")
    public String getWebsiteURL() {
        return websiteURL;
    }

    @SerializedName("websiteUrl")
    public void setWebsiteURL(String value) {
        this.websiteURL = value;
    }

    @SerializedName("profilePictureUrl")
    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    @SerializedName("profilePictureUrl")
    public void setProfilePictureURL(String value) {
        this.profilePictureURL = value;
    }

    @SerializedName("address")
    public String getAddress() {
        return address;
    }

    @SerializedName("address")
    public void setAddress(String value) {
        this.address = value;
    }

    @SerializedName("hasCuratedHighlights")
    public boolean getHasCuratedHighlights() {
        return hasCuratedHighlights;
    }

    @SerializedName("hasCuratedHighlights")
    public void setHasCuratedHighlights(boolean value) {
        this.hasCuratedHighlights = value;
    }

    @SerializedName("hasSpotlightHighlights")
    public boolean getHasSpotlightHighlights() {
        return hasSpotlightHighlights;
    }

    @SerializedName("hasSpotlightHighlights")
    public void setHasSpotlightHighlights(boolean value) {
        this.hasSpotlightHighlights = value;
    }

    @SerializedName("mutableName")
    public String getMutableName() {
        return mutableName;
    }

    @SerializedName("mutableName")
    public void setMutableName(String value) {
        this.mutableName = value;
    }

    @SerializedName("publisherType")
    public String getPublisherType() {
        return publisherType;
    }

    @SerializedName("publisherType")
    public void setPublisherType(String value) {
        this.publisherType = value;
    }

    @SerializedName("squareHeroImageUrl")
    public String getSquareHeroImageURL() {
        return squareHeroImageURL;
    }

    @SerializedName("squareHeroImageUrl")
    public void setSquareHeroImageURL(String value) {
        this.squareHeroImageURL = value;
    }

    @SerializedName("primaryColor")
    public String getPrimaryColor() {
        return primaryColor;
    }

    @SerializedName("primaryColor")
    public void setPrimaryColor(String value) {
        this.primaryColor = value;
    }
}
