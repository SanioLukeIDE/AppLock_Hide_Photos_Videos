package com.applock.fingerprint.passwordlock.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivityTmcBinding;
import com.applock.fingerprint.passwordlock.utils.Utility;

public class TMCActivity extends AppCompatActivity {

    ActivityTmcBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tmc);

        binding.btnAgree.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
        overridePendingTransition(0, 0);
    }


}