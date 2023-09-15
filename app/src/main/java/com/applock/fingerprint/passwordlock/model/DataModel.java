package com.applock.fingerprint.passwordlock.model;

import java.io.Serializable;

public class DataModel implements Serializable {

    private String name, value, url, extraURL;
    private int icon, color;
    private boolean isPremium;

    public DataModel(String name, String url, String extraURL, int icon, int color) {
        this.name = name;
        this.value = value;
        this.icon = icon;
        this.color = color;
        this.url = url;
        this.extraURL = extraURL;
    }

    public DataModel(String name, String value, int icon, boolean isPremium) {
        this.name = name;
        this.value = value;
        this.icon = icon;
        this.isPremium = isPremium;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtraURL() {
        return extraURL;
    }

    public void setExtraURL(String extraURL) {
        this.extraURL = extraURL;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
