package com.applock.fingerprint.passwordlock.ui.activity;

import static com.applock.fingerprint.passwordlock.downloaderUtil.utils.SharedPrefsForInstagram.context;
import static com.applock.fingerprint.passwordlock.utils.Utility.openPrivacyPolicy;
import static com.applock.fingerprint.passwordlock.utils.Utility.reviewDialog;
import static com.applock.fingerprint.passwordlock.utils.Utility.shareAppDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.interfaces.AppInterfaces;
import com.adsmodule.api.adsModule.utils.Constants;
import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivityMainSettingsBinding;
import com.applock.fingerprint.passwordlock.databinding.DialogSortBinding;
import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;
import com.applock.fingerprint.passwordlock.utils.SharePreferences;
import com.applock.fingerprint.passwordlock.utils.Utility;

public class MainSettingsActivity extends AppCompatActivity {

    ActivityMainSettingsBinding binding;
    SharePreferences sharePreferences;
    MainSettingsActivity activity;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_settings);
        activity = MainSettingsActivity.this;

        sharePreferences = MyApplication.getPreferences();

        page = getIntent().getIntExtra("page", 0);

        if (page == 0){
            binding.main.setVisibility(View.VISIBLE);
        } else binding.intruder.setVisibility(View.VISIBLE);

        binding.switchCalculator.setChecked(sharePreferences.isCalculatorLock());
        binding.switchDetectBreak.setChecked(sharePreferences.isIntruderDetectorOnOff());

        setAttempt();
        setBtnEnabled();

        binding.btnShare.setOnClickListener(view -> {
            shareAppDialog(activity);
        });

        binding.btnRateUs.setOnClickListener(view -> {
            reviewDialog(activity);
        });

        binding.btnPrivacy.setOnClickListener(view -> {
            openPrivacyPolicy(activity);
        });

        binding.switchCalculator.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharePreferences.setCalculatorLock(isChecked);
            setBtnEnabled();
        });
        binding.switchDetectBreak.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharePreferences.setIntruderDetectorOnOff(isChecked);
        });

        binding.btnModifyPassword.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, new AppInterfaces.InterstitialADInterface() {
                @Override
                public void adLoadState(boolean isLoaded) {
                    startActivity(new Intent(activity, CalculatorLockActivity.class).putExtra("change", true));
                }
            });

        });

        binding.btnTmc.setOnClickListener(v -> {
            Utility.nextActivity(activity, TMCActivity.class, false);
        });

        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnFailed.setOnClickListener(v -> openAttemptDialog());

    }

    private void setAttempt() {
        binding.tvAttempts.setText(sharePreferences.getAttemptFailedCount()+" failed attempts");
    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(activity);
    }

    private void setBtnEnabled() {
        binding.btnModifyPassword.setClickable(sharePreferences.isCalculatorLock());
        binding.btnModifyPassword.setEnabled(sharePreferences.isCalculatorLock());
        if (sharePreferences.isCalculatorLock()){
            binding.btnModifyPassword.setTextColor(getColor(R.color.black));
            binding.btnModifyPassword.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.dark_grey)));
        } else {
            binding.btnModifyPassword.setTextColor(getColor(R.color.grey));
            binding.btnModifyPassword.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.grey)));
        }
    }

    private void openAttemptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        DialogSortBinding sortBinding = DialogSortBinding.inflate(LayoutInflater.from(activity));
        builder.setView(sortBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        int pos = sharePreferences.getAttemptFailedCount();
        sortBinding.attemptLayout.setVisibility(View.VISIBLE);

        switch (pos){
            case 1:
                sortBinding.rbAttempt1.setChecked(true);
                break;
            case 2:
                sortBinding.rbAttempt2.setChecked(true);
                break;
            case 3:
                sortBinding.rbAttempt3.setChecked(true);
                break;
            case 4:
                sortBinding.rbAttempt4.setChecked(true);
                break;
        }

        sortBinding.btnClose.setOnClickListener(view -> dialog.dismiss());
        sortBinding.rgAttempt.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id){
                case R.id.rb_attempt1:
                    sharePreferences.setAttemptFailedCount(1);
                    break;
                case R.id.rb_attempt2:
                    sharePreferences.setAttemptFailedCount(2);
                    break;
                case R.id.rb_attempt3:
                    sharePreferences.setAttemptFailedCount(3);
                    break;
                case R.id.rb_attempt4:
                    sharePreferences.setAttemptFailedCount(4);
                    break;
            }
        });

        sortBinding.btnDone.setOnClickListener(view -> {
            dialog.dismiss();
            setAttempt();
        });
    }
}