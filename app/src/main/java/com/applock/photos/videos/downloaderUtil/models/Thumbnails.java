package com.applock.photos.videos.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Thumbnails implements Serializable {
    @SerializedName("video_length")
    private double videoLength;
    @SerializedName("thumbnail_width")
    private long thumbnailWidth;
    @SerializedName("thumbnail_height")
    private long thumbnailHeight;
    @SerializedName("thumbnail_duration")
    private double thumbnailDuration;
    @SerializedName("sprite_urls")
    private List<String> spriteUrls;
    @SerializedName("thumbnails_per_row")
    private long thumbnailsPerRow;
    @SerializedName("total_thumbnail_num_per_sprite")
    private long totalThumbnailNumPerSprite;
    @SerializedName("max_thumbnails_per_sprite")
    private long maxThumbnailsPerSprite;
    @SerializedName("sprite_width")
    private long spriteWidth;
    @SerializedName("sprite_height")
    private long spriteHeight;
    @SerializedName("rendered_width")
    private long renderedWidth;
    @SerializedName("file_size_kb")
    private long fileSizekb;

    public Thumbnails() {
    }

    @SerializedName("video_length")
    public double getVideoLength() {
        return videoLength;
    }

    @SerializedName("video_length")
    public void setVideoLength(double value) {
        this.videoLength = value;
    }

    @SerializedName("thumbnail_width")
    public long getThumbnailWidth() {
        return thumbnailWidth;
    }

    @SerializedName("thumbnail_width")
    public void setThumbnailWidth(long value) {
        this.thumbnailWidth = value;
    }

    @SerializedName("thumbnail_height")
    public long getThumbnailHeight() {
        return thumbnailHeight;
    }

    @SerializedName("thumbnail_height")
    public void setThumbnailHeight(long value) {
        this.thumbnailHeight = value;
    }

    @SerializedName("thumbnail_duration")
    public double getThumbnailDuration() {
        return thumbnailDuration;
    }

    @SerializedName("thumbnail_duration")
    public void setThumbnailDuration(double value) {
        this.thumbnailDuration = value;
    }

    @SerializedName("sprite_urls")
    public List<String> getSpriteUrls() {
        return spriteUrls;
    }

    @SerializedName("sprite_urls")
    public void setSpriteUrls(List<String> value) {
        this.spriteUrls = value;
    }

    @SerializedName("thumbnails_per_row")
    public long getThumbnailsPerRow() {
        return thumbnailsPerRow;
    }

    @SerializedName("thumbnails_per_row")
    public void setThumbnailsPerRow(long value) {
        this.thumbnailsPerRow = value;
    }

    @SerializedName("total_thumbnail_num_per_sprite")
    public long getTotalThumbnailNumPerSprite() {
        return totalThumbnailNumPerSprite;
    }

    @SerializedName("total_thumbnail_num_per_sprite")
    public void setTotalThumbnailNumPerSprite(long value) {
        this.totalThumbnailNumPerSprite = value;
    }

    @SerializedName("max_thumbnails_per_sprite")
    public long getMaxThumbnailsPerSprite() {
        return maxThumbnailsPerSprite;
    }

    @SerializedName("max_thumbnails_per_sprite")
    public void setMaxThumbnailsPerSprite(long value) {
        this.maxThumbnailsPerSprite = value;
    }

    @SerializedName("sprite_width")
    public long getSpriteWidth() {
        return spriteWidth;
    }

    @SerializedName("sprite_width")
    public void setSpriteWidth(long value) {
        this.spriteWidth = value;
    }

    @SerializedName("sprite_height")
    public long getSpriteHeight() {
        return spriteHeight;
    }

    @SerializedName("sprite_height")
    public void setSpriteHeight(long value) {
        this.spriteHeight = value;
    }

    @SerializedName("rendered_width")
    public long getRenderedWidth() {
        return renderedWidth;
    }

    @SerializedName("rendered_width")
    public void setRenderedWidth(long value) {
        this.renderedWidth = value;
    }

    @SerializedName("file_size_kb")
    public long getFileSizekb() {
        return fileSizekb;
    }

    @SerializedName("file_size_kb")
    public void setFileSizekb(long value) {
        this.fileSizekb = value;
    }
}
