package com.applock.photos.videos.downloaderUtil.models;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Format {
    private String acodec;
    private String audio_ext;
    private Object asr;
    private DownloaderOptions downloaderOptions;
    private String ext;
    private long filesize;
    private String format;
    private String formatID;
    private String formatNote;
    private HTTPHeaders httpHeaders;
    private String playerURL;
    private String protocol;
    private String url;
    private String vcodec;

    private String container;
    private String fragmentBaseURL;
    private List<FragmentModelVideo> fragments;
    private String language;
    private String manifestURL;

    public String getAcodec() {
        return acodec;
    }

    public String getAudio_ext() {
        return audio_ext;
    }

    public void setAudio_ext(String audio_ext) {
        this.audio_ext = audio_ext;
    }

    public Object getAsr() {
        return asr;
    }

    public void setAsr(Object asr) {
        this.asr = asr;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public HTTPHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(HTTPHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAcodec(String value) {
        this.acodec = value;
    }

    public Object getASR() {
        return asr;
    }

    public void setASR(Object value) {
        this.asr = value;
    }

    public void setASR(long value) {
        this.asr = value;
    }

    public DownloaderOptions getDownloaderOptions() {
        return downloaderOptions;
    }

    public void setDownloaderOptions(DownloaderOptions value) {
        this.downloaderOptions = value;
    }

    public String getEXT() {
        return ext;
    }

    public void setEXT(String value) {
        this.ext = value;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long value) {
        this.filesize = value;
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

    public String getFormatNote() {
        return formatNote;
    }

    public void setFormatNote(String value) {
        this.formatNote = value;
    }

    public HTTPHeaders getHTTPHeaders() {
        return httpHeaders;
    }

    public void setHTTPHeaders(HTTPHeaders value) {
        this.httpHeaders = value;
    }

    public String getPlayerURL() {
        return playerURL;
    }

    public void setPlayerURL(String value) {
        this.playerURL = value;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String value) {
        this.protocol = value;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String value) {
        this.url = value;
    }

    public String getVcodec() {
        return vcodec;
    }

    public void setVcodec(String value) {
        this.vcodec = value;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String value) {
        this.container = value;
    }


    public String getFragmentBaseURL() {
        return fragmentBaseURL;
    }

    public void setFragmentBaseURL(String value) {
        this.fragmentBaseURL = value;
    }

    public List<FragmentModelVideo> getFragments() {
        return fragments;
    }

    public void setFragments(List<FragmentModelVideo> value) {
        this.fragments = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String value) {
        this.language = value;
    }

    public String getManifestURL() {
        return manifestURL;
    }

    public void setManifestURL(String value) {
        this.manifestURL = value;
    }


}


