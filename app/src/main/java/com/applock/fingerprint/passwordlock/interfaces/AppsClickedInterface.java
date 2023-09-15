package com.applock.fingerprint.passwordlock.interfaces;

import com.applock.fingerprint.passwordlock.model.AppsModel;
import com.applock.fingerprint.passwordlock.model.CommLockInfo;

public interface AppsClickedInterface {

    void onItemClicked(AppsModel model);
    void onItemClicked(CommLockInfo model);

}
