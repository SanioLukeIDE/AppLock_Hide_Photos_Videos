package com.applock.fingerprint.passwordlock.downloaderUtil.models;

import androidx.annotation.Keep;

@Keep
public class DownloaderOptions {
    private long httpChunkSize;

    public long getHTTPChunkSize() {
        return httpChunkSize;
    }

    public void setHTTPChunkSize(long value) {
        this.httpChunkSize = value;
    }
}
