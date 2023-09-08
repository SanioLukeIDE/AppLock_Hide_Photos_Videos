package com.applock.photos.videos.downloaderUtil.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.applock.photos.videos.downloaderUtil.models.ModelGraphshortcode;

import java.io.Serializable;

@Keep
public class ModelInstagramResponse implements Serializable {

    @SerializedName("graphql")
    private ModelGraphshortcode modelGraphshortcode;

    public ModelGraphshortcode getModelGraphshortcode() {
        return modelGraphshortcode;
    }

    public void setModelGraphshortcode(ModelGraphshortcode modelGraphshortcode) {
        this.modelGraphshortcode = modelGraphshortcode;
    }
}
