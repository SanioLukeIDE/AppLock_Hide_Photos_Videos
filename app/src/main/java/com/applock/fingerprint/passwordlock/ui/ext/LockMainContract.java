package com.applock.fingerprint.passwordlock.ui.ext;

import android.content.Context;

import com.applock.fingerprint.passwordlock.model.CommLockInfo;

import java.util.List;


public interface LockMainContract {
    interface View extends BaseView<Presenter> {

        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context);

        void searchAppInfo(String search, LockMainPresenter.ISearchResultListener listener);

        void onDestroy();
    }
}
