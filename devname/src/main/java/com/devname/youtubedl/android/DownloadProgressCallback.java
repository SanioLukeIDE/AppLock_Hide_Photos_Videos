/*
 * *
 *  * Created by Syed Usama Ahmad on 3/4/23, 12:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/4/23, 12:48 AM
 *
 */

package com.devname.youtubedl.android;

public interface DownloadProgressCallback {
    void onProgressUpdate(float progress, long etaInSeconds, String line);
}
