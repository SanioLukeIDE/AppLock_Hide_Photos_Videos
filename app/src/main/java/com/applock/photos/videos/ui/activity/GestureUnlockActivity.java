package com.applock.photos.videos.ui.activity;

import static com.applock.photos.videos.utils.Const.LOCK_CURR_MILLISENCONS;
import static com.applock.photos.videos.utils.Const.LOCK_FROM;
import static com.applock.photos.videos.utils.Const.LOCK_FROM_FINISH;
import static com.applock.photos.videos.utils.Const.LOCK_FROM_LOCK_MAIN_ACITVITY;
import static com.applock.photos.videos.utils.Const.LOCK_LAST_LOAD_PKG_NAME;
import static com.applock.photos.videos.utils.Const.LOCK_PACKAGE_NAME;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityGestureUnlockBinding;
import com.applock.photos.videos.libs.LockPatternUtils;
import com.applock.photos.videos.libs.LockPatternView;
import com.applock.photos.videos.libs.LockPatternViewPattern;
import com.applock.photos.videos.libs.UnLockMenuPopWindow;
import com.applock.photos.videos.service.CameraService;
import com.applock.photos.videos.service.LockService;
import com.applock.photos.videos.utils.CommLockInfoManager;
import com.applock.photos.videos.utils.LockUtil;
import com.applock.photos.videos.singletonClass.MyApplication;

import java.util.List;


public class GestureUnlockActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIconMore;
    private LockPatternView mLockPatternView;
    private ImageView mUnLockIcon, mBgLayout, mAppLogo;
    private TextView mUnLockText, mUnlockFailTip, mAppLabel;
    private RelativeLayout mUnLockLayout;

    private PackageManager packageManager;
    private String pkgName;
    private String actionFrom;
    private LockPatternUtils mLockPatternUtils;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private CommLockInfoManager mLockInfoManager;
    private UnLockMenuPopWindow mPopWindow;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureUnlockReceiver mGestureUnlockReceiver;
    private ApplicationInfo appInfo;
    public static final String FINISH_UNLOCK_THIS_APP = "finish_unlock_this_app";

    private Drawable iconDrawable;
    private String appLabel;

    ActivityGestureUnlockBinding binding;
    private int wrongBiometricCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gesture_unlock);

        mUnLockLayout = (RelativeLayout) findViewById(R.id.unlock_layout);
        mIconMore = (ImageView) findViewById(R.id.btn_more);
        mLockPatternView = (LockPatternView) findViewById(R.id.unlock_lock_view);
        mUnLockIcon = (ImageView) findViewById(R.id.unlock_icon);
        mBgLayout = (ImageView) findViewById(R.id.bg_layout);
        mUnLockText = (TextView) findViewById(R.id.unlock_text);
        mUnlockFailTip = (TextView) findViewById(R.id.unlock_fail_tip);

        mAppLogo = (ImageView) findViewById(R.id.app_logo);
        mAppLabel = (TextView) findViewById(R.id.app_label);

        pkgName = getIntent().getStringExtra(LOCK_PACKAGE_NAME);
        actionFrom = getIntent().getStringExtra(LOCK_FROM);
        packageManager = getPackageManager();

        binding.unlockLayout.setBackgroundResource(MyApplication.getPreferences().getLockBackground());

        mLockInfoManager = new CommLockInfoManager(this);
        mPopWindow = new UnLockMenuPopWindow(this, pkgName, true);

        initLayoutBackground();
        initLockPatternView();


        mGestureUnlockReceiver = new GestureUnlockReceiver();
        IntentFilter filter = new IntentFilter();
        //  filter.addAction(UnLockMenuPopWindow.UPDATE_LOCK_VIEW);
        filter.addAction(FINISH_UNLOCK_THIS_APP);
        registerReceiver(mGestureUnlockReceiver, filter);

        mIconMore.setOnClickListener(this);
    }

    private void initLayoutBackground() {
        try {
            appInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (appInfo != null) {
                iconDrawable = packageManager.getApplicationIcon(appInfo);
                appLabel = packageManager.getApplicationLabel(appInfo).toString();
                mUnLockIcon.setImageDrawable(iconDrawable);
                mUnLockText.setText(appLabel);
                mUnlockFailTip.setText(getString(R.string.password_gestrue_tips));
                final Drawable icon = packageManager.getApplicationIcon(appInfo);
                mUnLockLayout.setBackgroundDrawable(icon);
                mUnLockLayout.getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                mUnLockLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                                mUnLockLayout.buildDrawingCache();
                                Bitmap bmp = LockUtil.drawableToBitmap(icon, mUnLockLayout);
                                LockUtil.blur(GestureUnlockActivity.this, LockUtil.big(bmp), mUnLockLayout);
                                return true;
                            }
                        });
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void initLockPatternView() {
        mLockPatternView.setLineColorRight(0x80ffffff);
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);

        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                if (mLockPatternUtils.checkPattern(pattern)) {
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    if (actionFrom.equals(LOCK_FROM_LOCK_MAIN_ACITVITY)) {
                        startActivity(new Intent(GestureUnlockActivity.this, MainActivity.class));
                        finish();
                    } else {
                        MyApplication.getPreferences().putLong(LOCK_CURR_MILLISENCONS, System.currentTimeMillis());
                        MyApplication.getPreferences().putString(LOCK_LAST_LOAD_PKG_NAME, pkgName);

                        Intent intent = new Intent(LockService.UNLOCK_ACTION);
                        intent.putExtra(LockService.LOCK_SERVICE_LASTTIME, System.currentTimeMillis());
                        intent.putExtra(LockService.LOCK_SERVICE_LASTAPP, pkgName);
                        sendBroadcast(intent);

                        mLockInfoManager.unlockCommApplication(pkgName);
                        finish();
                    }
                } else {
                    wrongBiometricCount++;
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);

                    if (MyApplication.getPreferences().getAttemptFailedCount() == wrongBiometricCount){
                        if (MyApplication.getPreferences().isIntruderDetectorOnOff()){
                            startService(new Intent(getApplicationContext(), CameraService.class));
                            wrongBiometricCount = 0;
                        }
                    }

                    if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                        mFailedPatternAttemptsSinceLastTimeout++;
                        int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                        if (retry >= 0) {

                        }
                    }

                    if (mFailedPatternAttemptsSinceLastTimeout >= 3) {
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    } else {
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    }
                }
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    @Override
    public void onBackPressed() {
        if (actionFrom.equals(LOCK_FROM_FINISH)) {
            LockUtil.goHome(this);
        } else if (actionFrom.equals(LOCK_FROM_LOCK_MAIN_ACITVITY)) {
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_more:
                mPopWindow.showAsDropDown(mIconMore);
                break;
        }
    }

    private class GestureUnlockReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            if (action.equals(UnLockMenuPopWindow.UPDATE_LOCK_VIEW)) {
//                mLockPatternView.initRes();
//            } else
            if (action.equals(FINISH_UNLOCK_THIS_APP)) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGestureUnlockReceiver);
    }
}
