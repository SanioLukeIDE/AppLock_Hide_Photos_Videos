package com.applock.fingerprint.passwordlock.downloaderUtil.models;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Video {
    private long commentCount;
    private String description;
    private String displayID;
    private String duration;
    private String ext;
    private String extractor;
    private String extractorKey;
    private String format;
    private String formatID;
    private List<Format> formats;
    private long height;
    private HTTPHeaders httpHeaders;
    private String id;
    private Object playlist;
    private Object playlistIndex;
    private String protocol;
    private String thumbnail;
    private List<Thumbnail> thumbnails;
    private double timestamp;
    private String title;

    private String url;


    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long value) {
        this.commentCount = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDisplayID() {
        return displayID;
    }

    public void setDisplayID(String value) {
        this.displayID = value;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String value) {
        this.duration = value;
    }

    public String getEXT() {
        return ext;
    }

    public void setEXT(String value) {
        this.ext = value;
    }

    public String getExtractor() {
        return extractor;
    }

    public void setExtractor(String value) {
        this.extractor = value;
    }

    public String getExtractorKey() {
        return extractorKey;
    }

    public void setExtractorKey(String value) {
        this.extractorKey = value;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String value) {
        this.format = value;
    }

    public String getFormatID() {
        return formatID;
    }

    public void setFormatID(String value) {
        this.formatID = value;
    }

    public List<Format> getFormats() {
        return formats;
    }

    public void setFormats(List<Format> value) {
        this.formats = value;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long value) {
        this.height = value;
    }

    public HTTPHeaders getHTTPHeaders() {
        return httpHeaders;
    }

    public void setHTTPHeaders(HTTPHeaders value) {
        this.httpHeaders = value;
    }

    public String getID() {
        return id;
    }

    public void setID(String value) {
        this.id = value;
    }


    public Object getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Object value) {
        this.playlist = value;
    }

    public Object getPlaylistIndex() {
        return playlistIndex;
    }

    public void setPlaylistIndex(Object value) {
        this.playlistIndex = value;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String value) {
        this.protocol = value;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String value) {
        this.thumbnail = value;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<Thumbnail> value) {
        this.thumbnails = value;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long value) {
        this.timestamp = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }


    public String getURL() {
        return url;
    }

    public void setURL(String value) {
        this.url = value;
    }


}
