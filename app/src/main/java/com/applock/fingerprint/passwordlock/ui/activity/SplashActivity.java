package com.applock.fingerprint.passwordlock.ui.activity;

import static com.adsmodule.api.adsModule.retrofit.APICallHandler.callAdsApi;
import static com.applock.fingerprint.passwordlock.singletonClass.AppOpenAds.currentActivity;
import static com.applock.fingerprint.passwordlock.utils.Const.APP_PACKAGE_NAME;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM_LOCK_MAIN_ACITVITY;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_PACKAGE_NAME;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_STATE;
import static com.applock.fingerprint.passwordlock.utils.Utility.setFullScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.retrofit.AdsDataRequestModel;
import com.adsmodule.api.adsModule.utils.Constants;
import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.service.LoadAppListService;
import com.applock.fingerprint.passwordlock.service.LockService;
import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;
import com.applock.fingerprint.passwordlock.utils.Utility;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setFullScreen(this);

        startService(new Intent(getApplicationContext(), LoadAppListService.class));
        if (MyApplication.getPreferences().getFBoolean(LOCK_STATE)) {
            startService(new Intent(getApplicationContext(), LockService.class));
        }

        if (Utility.isInternetConnected(getApplicationContext())) {
            callAdsApi(SplashActivity.this, Constants.MAIN_BASE_URL, new AdsDataRequestModel(getPackageName(), ""), adsResponseModel -> {
                if (adsResponseModel != null) {
                    AdUtils.showAppOpenAds(Constants.adsResponseModel.getApp_open_ads().getAdx(), currentActivity, (state_load) -> {
                        beginActivity();
                    });
                } else beginActivity();
            });
        } else
            beginActivity();

    }

    private void beginActivity(){
        new Handler().postDelayed(() -> {
            if (!MyApplication.getPreferences().isFirstLock()) {
                Intent intent = new Intent(getApplicationContext(), GestureSelfUnlockActivity.class);
                intent.putExtra(LOCK_PACKAGE_NAME, APP_PACKAGE_NAME);
                intent.putExtra(LOCK_FROM, LOCK_FROM_LOCK_MAIN_ACITVITY);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class));
                finish();
            }
        }, 1500);

    }

}