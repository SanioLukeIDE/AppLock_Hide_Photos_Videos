package com.applock.fingerprint.passwordlock.ui.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.KEYGUARD_SERVICE;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.FragmentBiometricLockBinding;
import com.applock.fingerprint.passwordlock.ui.activity.MainActivity;
import com.applock.fingerprint.passwordlock.utils.BiometricUtils;


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
            new BiometricUtils(activity, isAuthenticate -> {
                if (isAuthenticate)
                    activity.startVault();
                else {
                    Toast.makeText(activity, "Failed to unlock.", Toast.LENGTH_SHORT).show();
                    activity.startAppLock();
                }
            });
        });
    }

    ActivityResultLauncher<Intent> openPin = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            authSuccess();
        } else {
            Toast.makeText(activity, "Failed to unlock.", Toast.LENGTH_SHORT).show();
            activity.startAppLock();
        }
    });


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}