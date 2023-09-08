package com.applock.photos.videos.downloaderUtil.models;

import androidx.annotation.Keep;

@Keep
public class HTTPHeaders {
    private String accept;
    private String acceptCharset;
    private String acceptEncoding;
    private String acceptLanguage;
    private String cookie;
    private String referer;
    private String userAgent;

    public String getAccept() {
        return accept;
    }

    public void setAccept(String value) {
        this.accept = value;
    }

    public String getAcceptCharset() {
        return acceptCharset;
    }

    public void setAcceptCharset(String value) {
        this.acceptCharset = value;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String value) {
        this.acceptEncoding = value;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String value) {
        this.acceptLanguage = value;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String value) {
        this.cookie = value;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String value) {
        this.referer = value;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String value) {
        this.userAgent = value;
    }
}
