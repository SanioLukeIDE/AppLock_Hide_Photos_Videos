package com.applock.fingerprint.passwordlock.ui.ext;

import android.content.Context;

import com.applock.fingerprint.passwordlock.model.CommLockInfo;

import java.util.List;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context, boolean isSort);

        void loadLockAppInfo(Context context);

        void onDestroy();
    }
}
