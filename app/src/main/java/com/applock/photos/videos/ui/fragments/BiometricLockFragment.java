package com.applock.photos.videos.ui.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.KEYGUARD_SERVICE;
import static android.icu.text.DateTimePatternGenerator.PatternInfo.OK;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;
import static com.applock.photos.videos.utils.Utility.setFullScreen;

import android.annotation.SuppressLint;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.FragmentBiometricLockBinding;
import com.applock.photos.videos.ui.activity.MainActivity;
import com.applock.photos.videos.utils.BiometricUtils;

import java.util.concurrent.Executor;


public class BiometricLockFragment extends Fragment {

    FragmentBiometricLockBinding binding;

    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBiometricLockBinding.inflate(inflater, container, false);

        activity = (MainActivity) requireActivity();

        return binding.getRoot();

    }

    private void authSuccess() {
        activity.startVault();
    }

    @Override
    public void onResume() {
        super.onResume();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                activity.startAppLock();
            }
        });

        binding.btnBack.setOnClickListener(view1 -> {
            activity.startAppLock();
        });

        KeyguardManager km = (KeyguardManager) requireContext().getSystemService(KEYGUARD_SERVICE);

        binding.btnPin.setOnClickListener(view -> {
            if (km.isKeyguardSecure()) {
                Intent authIntent = km.createConfirmDeviceCredentialIntent(getString(R.string.biometric_title), getString(R.string.biometric_desc));
                openPin.launch(authIntent);
            }
        });

        binding.btnScan.setOnClickListener(view1 -> {
            new BiometricUtils(activity, isAuthenticate -> activity.startVault());
        });
    }

    ActivityResultLauncher<Intent> openPin = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            authSuccess();
        } else activity.startAppLock();
    });


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}