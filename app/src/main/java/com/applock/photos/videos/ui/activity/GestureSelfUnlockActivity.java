package com.applock.photos.videos.ui.activity;

import static com.applock.photos.videos.utils.Const.LOCK_AUTO_RECORD_PIC;
import static com.applock.photos.videos.utils.Const.LOCK_FROM;
import static com.applock.photos.videos.utils.Const.LOCK_FROM_FINISH;
import static com.applock.photos.videos.utils.Const.LOCK_FROM_LOCK_MAIN_ACITVITY;
import static com.applock.photos.videos.utils.Const.LOCK_FROM_SETTING;
import static com.applock.photos.videos.utils.Const.LOCK_FROM_UNLOCK;
import static com.applock.photos.videos.utils.Const.LOCK_PACKAGE_NAME;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityGestureSelfUnlockBinding;
import com.applock.photos.videos.libs.LockPatternUtils;
import com.applock.photos.videos.libs.LockPatternView;
import com.applock.photos.videos.libs.LockPatternViewPattern;
import com.applock.photos.videos.libs.UnLockMenuPopWindow;
import com.applock.photos.videos.utils.CommLockInfoManager;
import com.applock.photos.videos.utils.MyApp;
import com.applock.photos.videos.utils.SystemBarHelper;

import java.util.List;

public class GestureSelfUnlockActivity extends AppCompatActivity {

    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private String actionFrom;
    private String pkgName;
    private CommLockInfoManager mManager;
    ActivityGestureSelfUnlockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gesture_self_unlock);

        mLockPatternView = (LockPatternView) findViewById(R.id.unlock_lock_view);

        mManager = new CommLockInfoManager(this);
        pkgName = getIntent().getStringExtra(LOCK_PACKAGE_NAME);
        actionFrom = getIntent().getStringExtra(LOCK_FROM);

        initLockPatternView();

    }

    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);

        mPatternViewPattern.setPatternListener(pattern -> {
            if (mLockPatternUtils.checkPattern(pattern)) {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                if (actionFrom.equals(LOCK_FROM_LOCK_MAIN_ACITVITY)) {
                    Intent intent = new Intent(GestureSelfUnlockActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else if (actionFrom.equals(LOCK_FROM_FINISH)) {
                    mManager.unlockCommApplication(pkgName);
                    finish();
                } else if (actionFrom.equals(LOCK_FROM_SETTING)) {
//                        startActivity(new Intent(GestureSelfUnlockActivity.this, LockSettingActivity.class));
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        finish();
                } else if (actionFrom.equals(LOCK_FROM_UNLOCK)) {
                    mManager.setIsUnLockThisApp(pkgName, true);
                    mManager.unlockCommApplication(pkgName);
                    sendBroadcast(new Intent(GestureUnlockActivity.FINISH_UNLOCK_THIS_APP));
                    finish();
                }
            } else {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                    mFailedPatternAttemptsSinceLastTimeout++;
                    int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                }
                if (mFailedPatternAttemptsSinceLastTimeout >= 3) {
                    if (MyApp.getPreferences().getBoolean(LOCK_AUTO_RECORD_PIC, false)) {

                    }
                }
                if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {

                } else {
                    mLockPatternView.postDelayed(mClearPatternRunnable, 500);
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


}
