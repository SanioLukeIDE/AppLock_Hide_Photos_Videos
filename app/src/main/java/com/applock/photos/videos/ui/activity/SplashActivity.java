package com.applock.photos.videos.ui.activity;

import static com.applock.photos.videos.utils.Const.APP_PACKAGE_NAME;
import static com.applock.photos.videos.utils.Const.LOCK_FROM;
import static com.applock.photos.videos.utils.Const.LOCK_FROM_LOCK_MAIN_ACITVITY;
import static com.applock.photos.videos.utils.Const.LOCK_PACKAGE_NAME;
import static com.applock.photos.videos.utils.Const.LOCK_STATE;
import static com.applock.photos.videos.utils.Utility.setFullScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.applock.photos.videos.R;
import com.applock.photos.videos.service.LoadAppListService;
import com.applock.photos.videos.service.LockService;
import com.applock.photos.videos.singletonClass.MyApplication;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setFullScreen(this);

        startService(new Intent(getApplicationContext(), LoadAppListService.class));
        if (MyApplication.getPreferences().getFBoolean(LOCK_STATE)) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(getApplicationContext(), LockService.class));
            } else*/ startService(new Intent(getApplicationContext(), LockService.class));
        }

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