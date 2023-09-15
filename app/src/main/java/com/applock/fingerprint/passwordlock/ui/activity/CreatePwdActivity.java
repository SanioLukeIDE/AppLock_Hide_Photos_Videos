package com.applock.fingerprint.passwordlock.ui.activity;

import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_STATE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivityCreatePwdBinding;
import com.applock.fingerprint.passwordlock.libs.LockPatternUtils;
import com.applock.fingerprint.passwordlock.libs.LockPatternView;
import com.applock.fingerprint.passwordlock.libs.LockPatternViewPattern;
import com.applock.fingerprint.passwordlock.model.LockStage;
import com.applock.fingerprint.passwordlock.service.LockService;
import com.applock.fingerprint.passwordlock.ui.ext.GestureCreateContract;
import com.applock.fingerprint.passwordlock.ui.ext.GestureCreatePresenter;
import com.applock.fingerprint.passwordlock.utils.SharePreferences;
import com.applock.fingerprint.passwordlock.utils.SystemBarHelper;

import java.util.List;

public class CreatePwdActivity extends AppCompatActivity implements View.OnClickListener, GestureCreateContract.View {

    private TextView mLockTip;
    private LockPatternView mLockPatternView;
    private TextView mBtnReset;
    private LockStage mUiStage = LockStage.Introduction;
    protected List<LockPatternView.Cell> mChosenPattern = null; //密码
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureCreatePresenter mGestureCreatePresenter;
    private RelativeLayout mTopLayout;

    ActivityCreatePwdBinding binding;
    SharePreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_pwd);

        mLockPatternView = (LockPatternView) findViewById(R.id.lock_pattern_view);
        mLockTip = (TextView) findViewById(R.id.lock_tip);
        mBtnReset = (TextView) findViewById(R.id.btn_reset);
//        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
//        mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this), 0, 0);

        preferences = new SharePreferences(getApplicationContext());

        mGestureCreatePresenter = new GestureCreatePresenter(this, this);
        initLockPatternView();

        mBtnReset.setOnClickListener(this);

    }

    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                mGestureCreatePresenter.onPatternDetected(pattern, mChosenPattern, mUiStage);
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                setStepOne();
                break;
        }
    }

    private void setStepOne() {
        mGestureCreatePresenter.updateStage(LockStage.Introduction);
        mLockTip.setText(getString(R.string.lock_recording_intro_header));
    }

    private void gotoLockMainActivity() {
        preferences.putBoolean(LOCK_STATE, true);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, LockService.class));
        } else*/ startService(new Intent(getApplicationContext(), LockService.class));
        preferences.setFirstLock(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @Override
    public void updateUiStage(LockStage stage) {
        mUiStage = stage;
    }


    @Override
    public void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern) {
        this.mChosenPattern = mChosenPattern;
    }


    @Override
    public void updateLockTip(String text, boolean isToast) {
        mLockTip.setText(text);
    }


    @Override
    public void setHeaderMessage(int headerMessage) {
        mLockTip.setText(headerMessage);
    }

    @Override
    public void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode) {
        if (patternEnabled) {
            mLockPatternView.enableInput();
        } else {
            mLockPatternView.disableInput();
        }
        mLockPatternView.setDisplayMode(displayMode);
    }


    @Override
    public void Introduction() {
        clearPattern();
    }

    @Override
    public void HelpScreen() {

    }

    @Override
    public void ChoiceTooShort() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    @Override
    public void moveToStatusTwo() {

    }


    @Override
    public void clearPattern() {
        mLockPatternView.clearPattern();
    }


    @Override
    public void ConfirmWrong() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }


    @Override
    public void ChoiceConfirmed() {
        mLockPatternUtils.saveLockPattern(mChosenPattern);
        clearPattern();
        gotoLockMainActivity();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGestureCreatePresenter.onDestroy();
    }
}
