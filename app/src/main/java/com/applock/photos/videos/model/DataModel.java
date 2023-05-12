package com.applock.photos.videos.model;

import java.io.Serializable;

public class DataModel implements Serializable {

    private String name, value;
    private int icon, color;
    private boolean isPremium;

    public DataModel(String name, String value, int icon, int color) {
        this.name = name;
        this.value = value;
        this.icon = icon;
        this.color = color;
    }

    public DataModel(String name, String value, int icon, boolean isPremium) {
        this.name = name;
        this.value = value;
        this.icon = icon;
        this.isPremium = isPremium;
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
