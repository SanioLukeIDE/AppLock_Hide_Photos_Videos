package com.applock.photos.videos.downloaderUtil.models;


import com.google.gson.annotations.SerializedName;
import com.applock.photos.videos.downloaderUtil.models.Props;

import java.io.Serializable;

public class SnapChatStoryModel implements Serializable {
    @SerializedName("props")
    private Props props;

    @SerializedName("props")
    public Props getProps() {
        return props;
    }

    @SerializedName("props")
    public void setProps(Props value) {
        this.props = value;
    }
}
