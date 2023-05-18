package com.applock.photos.videos.interfaces;

import com.applock.photos.videos.model.AppsModel;
import com.applock.photos.videos.model.CommLockInfo;
import com.applock.photos.videos.model.ImageFolder;

public interface AppsClickedInterface {

    void onItemClicked(AppsModel model);
    void onItemClicked(CommLockInfo model);

}
