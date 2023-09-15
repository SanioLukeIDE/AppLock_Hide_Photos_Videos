package com.applock.fingerprint.passwordlock.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelInstaWithLogin implements Serializable {
    @SerializedName("items")
    private List<Item> items;
    @SerializedName("num_results")
    private long numResults;


    public ModelInstaWithLogin() {

    }

    @SerializedName("items")
    public List<Item> getItems() {
        return items;
    }

    @SerializedName("items")
    public void setItems(List<Item> value) {
        this.items = value;
    }

    @SerializedName("num_results")
    public long getNumResults() {
        return numResults;
    }

    @SerializedName("num_results")
    public void setNumResults(long value) {
        this.numResults = value;
    }

}


