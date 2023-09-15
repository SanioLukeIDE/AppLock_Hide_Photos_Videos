package com.applock.fingerprint.passwordlock.ui.activity;

import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_AUTO_RECORD_PIC;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM_FINISH;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM_LOCK_MAIN_ACITVITY;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM_SETTING;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_FROM_UNLOCK;
import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_PACKAGE_NAME;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivityGestureSelfUnlockBinding;
import com.applock.fingerprint.passwordlock.libs.LockPatternUtils;
import com.applock.fingerprint.passwordlock.libs.LockPatternView;
import com.applock.fingerprint.passwordlock.libs.LockPatternViewPattern;
import com.applock.fingerprint.passwordlock.service.CameraService;
import com.applock.fingerprint.passwordlock.utils.CommLockInfoManager;
import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;

public class GestureSelfUnlockActivity extends AppCompatActivity {

    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private String actionFrom;
    private String pkgName;
    private CommLockInfoManager mManager;
    ActivityGestureSelfUnlockBinding binding;
    private int wrongBiometricCount = 0;

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
                switch (actionFrom) {
                    case LOCK_FROM_LOCK_MAIN_ACITVITY:
                        Intent intent = new Intent(GestureSelfUnlockActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case LOCK_FROM_FINISH:
                        mManager.unlockCommApplication(pkgName);
                        finish();
                        break;
                    case LOCK_FROM_SETTING:
//                        startActivity(new Intent(GestureSelfUnlockActivity.this, LockSettingActivity.class));
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        finish();
                        break;
                    case LOCK_FROM_UNLOCK:
                        mManager.setIsUnLockThisApp(pkgName, true);
                        mManager.unlockCommApplication(pkgName);
                        sendBroadcast(new Intent(GestureUnlockActivity.FINISH_UNLOCK_THIS_APP));
                        finish();
                        break;
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
                }
                if (mFailedPatternAttemptsSinceLastTimeout >= 3) {
                    if (MyApplication.getPreferences().getBoolean(LOCK_AUTO_RECORD_PIC, false)) {

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
