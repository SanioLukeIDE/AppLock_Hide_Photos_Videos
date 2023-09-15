
package com.applock.fingerprint.passwordlock.ui.activity;

import static com.applock.fingerprint.passwordlock.utils.Utility.nextActivity;
import static com.applock.fingerprint.passwordlock.utils.Utility.privacyLink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivityTmcuseBinding;
import com.applock.fingerprint.passwordlock.utils.Utility;

public class TMCUseActivity extends AppCompatActivity {

    ActivityTmcuseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tmcuse);


    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.btnPrivacy.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(privacyLink));
            startActivity(intent);
        });

        binding.btnTmc.setOnClickListener(view -> {
            nextActivity(TMCUseActivity.this, TMCActivity.class, false);
            overridePendingTransition(0, 0);
        });

        binding.btnAgree.setOnClickListener(view -> {
            nextActivity(TMCUseActivity.this, PermissionsActivity.class, true);
            overridePendingTransition(0, 0);
        });

    }

}