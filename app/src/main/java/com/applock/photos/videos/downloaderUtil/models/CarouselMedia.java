package com.applock.photos.videos.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CarouselMedia implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("media_type")
    private int mediaType;
    @SerializedName("image_versions2")
    private ImageVersions2 imageVersions2;
    @SerializedName("original_width")
    private long originalWidth;
    @SerializedName("original_height")
    private long originalHeight;
    @SerializedName("pk")
    private double pk;
    @SerializedName("carousel_parent_id")
    private String carouselParentid;
    @SerializedName("can_see_insights_as_brand")
    private Boolean canSeeInsightsAsBrand;
    @SerializedName("is_commercial")
    private Boolean isCommercial;
    @SerializedName("commerciality_status")
    private String commercialityStatus;
    @SerializedName("sharing_friction_info")
    private SharingFrictionInfo sharingFrictionInfo;
    @SerializedName("video_versions")
    private List<VideoVersion> videoVersions;
    @SerializedName("video_duration")
    private Double videoDuration;
    @SerializedName("video_dash_manifest")
    private String videoDashManifest;
    @SerializedName("video_codec")
    private String videoCodec;

    public CarouselMedia() {
    }

    @SerializedName("video_versions")
    public List<VideoVersion> getVideoVersions() {
        return videoVersions;
    }

    @SerializedName("video_versions")
    public void setVideoVersions(List<VideoVersion> value) {
        this.videoVersions = value;
    }

    @SerializedName("video_duration")
    public Double getVideoDuration() {
        return videoDuration;
    }

    @SerializedName("video_duration")
    public void setVideoDuration(Double value) {
        this.videoDuration = value;
    }


    @SerializedName("video_dash_manifest")
    public String getVideoDashManifest() {
        return videoDashManifest;
    }

    @SerializedName("video_dash_manifest")
    public void setVideoDashManifest(String value) {
        this.videoDashManifest = value;
    }

    @SerializedName("video_codec")
    public String getVideoCodec() {
        return videoCodec;
    }

    @SerializedName("video_codec")
    public void setVideoCodec(String value) {
        this.videoCodec = value;
    }


    @SerializedName("id")
    public String getid() {
        return id;
    }

    @SerializedName("id")
    public void setid(String value) {
        this.id = value;
    }

    @SerializedName("media_type")
    public int getMediaType() {
        return mediaType;
    }

    @SerializedName("media_type")
    public void setMediaType(int value) {
        this.mediaType = value;
    }

    @SerializedName("image_versions2")
    public ImageVersions2 getImageVersions2() {
        return imageVersions2;
    }

    @SerializedName("image_versions2")
    public void setImageVersions2(ImageVersions2 value) {
        this.imageVersions2 = value;
    }

    @SerializedName("original_width")
    public long getOriginalWidth() {
        return originalWidth;
    }

    @SerializedName("original_width")
    public void setOriginalWidth(long value) {
        this.originalWidth = value;
    }

    @SerializedName("original_height")
    public long getOriginalHeight() {
        return originalHeight;
    }

    @SerializedName("original_height")
    public void setOriginalHeight(long value) {
        this.originalHeight = value;
    }

    @SerializedName("pk")
    public double getPk() {
        return pk;
    }

    @SerializedName("pk")
    public void setPk(double value) {
        this.pk = value;
    }

    @SerializedName("carousel_parent_id")
    public String getCarouselParentid() {
        return carouselParentid;
    }

    @SerializedName("carousel_parent_id")
    public void setCarouselParentid(String value) {
        this.carouselParentid = value;
    }

    @SerializedName("can_see_insights_as_brand")
    public Boolean getCanSeeInsightsAsBrand() {
        return canSeeInsightsAsBrand;
    }

    @SerializedName("can_see_insights_as_brand")
    public void setCanSeeInsightsAsBrand(Boolean value) {
        this.canSeeInsightsAsBrand = value;
    }

    @SerializedName("is_commercial")
    public Boolean getIsCommercial() {
        return isCommercial;
    }

    @SerializedName("is_commercial")
    public void setIsCommercial(Boolean value) {
        this.isCommercial = value;
    }

    @SerializedName("commerciality_status")
    public String getCommercialityStatus() {
        return commercialityStatus;
    }

    @SerializedName("commerciality_status")
    public void setCommercialityStatus(String value) {
        this.commercialityStatus = value;
    }

    @SerializedName("sharing_friction_info")
    public SharingFrictionInfo getSharingFrictionInfo() {
        return sharingFrictionInfo;
    }

    @SerializedName("sharing_friction_info")
    public void setSharingFrictionInfo(SharingFrictionInfo value) {
        this.sharingFrictionInfo = value;
    }
}
