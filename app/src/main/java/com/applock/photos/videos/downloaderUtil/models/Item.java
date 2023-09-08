package com.applock.photos.videos.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {
    @SerializedName("taken_at")
    private long takenAt;
    @SerializedName("pk")
    private double pk;
    @SerializedName("id")
    private String id;
    @SerializedName("media_type")
    private int mediaType;
    @SerializedName("code")
    private String code;
    @SerializedName("client_cache_key")
    private String clientCacheKey;
    @SerializedName("filter_type")
    private long filterType;
    @SerializedName("is_unified_video")
    private boolean isUnifiedVideo;
    @SerializedName("next_max_id")
    private double nextMaxid;
    @SerializedName("inline_composer_display_condition")
    private String inlineComposerDisplayCondition;
    @SerializedName("title")
    private String title;
    @SerializedName("product_type")
    private String productType;
    @SerializedName("nearly_complete_copyright_match")
    private boolean nearlyCompleteCopyrightMatch;
    @SerializedName("thumbnails")
    private Thumbnails thumbnails;
    @SerializedName("igtv_exists_in_viewer_series")
    private boolean igtvExistsInViewerSeries;
    @SerializedName("is_post_live")
    private boolean isPostLive;
    @SerializedName("image_versions2")
    private ImageVersions2 imageVersions2;
    @SerializedName("video_codec")
    private String videoCodec;
    @SerializedName("number_of_qualities")
    private long numberOfQualities;
    @SerializedName("video_versions")
    private List<VideoVersion> videoVersions;
    @SerializedName("has_audio")
    private boolean hasAudio;
    @SerializedName("video_duration")
    private double videoDuration;
    @SerializedName("carousel_media_count")
    private long carouselMediaCount;
    @SerializedName("carousel_media")
    private List<CarouselMedia> carouselMedia;
    @SerializedName("user")
    private UsersInsta user;

    public Item() {
    }


    public List<CarouselMedia> getCarouselMedia() {
        return carouselMedia;
    }

    public void setCarouselMedia(List<CarouselMedia> value) {
        this.carouselMedia = value;
    }

    public long getCarouselMediaCount() {
        return carouselMediaCount;
    }

    public void setCarouselMediaCount(long value) {
        this.carouselMediaCount = value;
    }

    public long getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(long value) {
        this.takenAt = value;
    }

    public UsersInsta getUser() {
        return user;
    }

    public void setUser(UsersInsta user) {
        this.user = user;
    }

    public double getPk() {
        return pk;
    }

    public void setPk(double value) {
        this.pk = value;
    }

    public String getid() {
        return id;
    }

    public void setid(String value) {
        this.id = value;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int value) {
        this.mediaType = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String value) {
        this.code = value;
    }

    public String getClientCacheKey() {
        return clientCacheKey;
    }

    public void setClientCacheKey(String value) {
        this.clientCacheKey = value;
    }

    public long getFilterType() {
        return filterType;
    }

    public void setFilterType(long value) {
        this.filterType = value;
    }

    public boolean getIsUnifiedVideo() {
        return isUnifiedVideo;
    }

    public void setIsUnifiedVideo(boolean value) {
        this.isUnifiedVideo = value;
    }

    public double getNextMaxid() {
        return nextMaxid;
    }

    public void setNextMaxid(double value) {
        this.nextMaxid = value;
    }

    public String getInlineComposerDisplayCondition() {
        return inlineComposerDisplayCondition;
    }

    public void setInlineComposerDisplayCondition(String value) {
        this.inlineComposerDisplayCondition = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String value) {
        this.productType = value;
    }

    public boolean getNearlyCompleteCopyrightMatch() {
        return nearlyCompleteCopyrightMatch;
    }

    public void setNearlyCompleteCopyrightMatch(boolean value) {
        this.nearlyCompleteCopyrightMatch = value;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnails value) {
        this.thumbnails = value;
    }

    public boolean getIgtvExistsInViewerSeries() {
        return igtvExistsInViewerSeries;
    }

    public void setIgtvExistsInViewerSeries(boolean value) {
        this.igtvExistsInViewerSeries = value;
    }

    public boolean getIsPostLive() {
        return isPostLive;
    }

    public void setIsPostLive(boolean value) {
        this.isPostLive = value;
    }

    public ImageVersions2 getImageVersions2() {
        return imageVersions2;
    }

    public void setImageVersions2(ImageVersions2 value) {
        this.imageVersions2 = value;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String value) {
        this.videoCodec = value;
    }

    public long getNumberOfQualities() {
        return numberOfQualities;
    }

    public void setNumberOfQualities(long value) {
        this.numberOfQualities = value;
    }

    public List<VideoVersion> getVideoVersions() {
        return videoVersions;
    }

    public void setVideoVersions(List<VideoVersion> value) {
        this.videoVersions = value;
    }

    public boolean getHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(boolean value) {
        this.hasAudio = value;
    }

    public double getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(double value) {
        this.videoDuration = value;
    }
}
