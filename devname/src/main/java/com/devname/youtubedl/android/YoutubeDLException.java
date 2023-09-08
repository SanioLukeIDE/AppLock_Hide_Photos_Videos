/*
 * *
 *  * Created by Syed Usama Ahmad on 3/4/23, 12:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/4/23, 12:48 AM
 *
 */

package com.devname.youtubedl.android;

public class YoutubeDLException extends Exception {

    public YoutubeDLException(String message) {
        super(message);
    }

    public YoutubeDLException(String message, Throwable e) {
        super(message, e);
    }

    public YoutubeDLException(Throwable e) {
        super(e);
    }

}
