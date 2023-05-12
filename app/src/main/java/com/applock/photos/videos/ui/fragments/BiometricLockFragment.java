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

import java.util.concurrent.Executor;


public class BiometricLockFragment extends Fragment {

    FragmentBiometricLockBinding binding;
    BiometricManager biometricManager;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    Executor executor;
    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBiometricLockBinding.inflate(inflater, container, false);

        activity = (MainActivity) requireActivity();

        biometricManager = BiometricManager.from(requireContext());
        executor = ContextCompat.getMainExecutor(requireContext());

        switch (biometricManager.canAuthenticate(DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                Intent enrollIntent = new Intent();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, DEVICE_CREDENTIAL);
                }
                startActivityForResult(enrollIntent, 1);
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                break;
        }

        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
//                Log.e("onAuthenticationError: ", errorCode + " " + errString);
                if (errorCode == 14) authSuccess();
                else {
                    activity.startAppLock();
                    Toast.makeText(requireContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                authSuccess();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(requireContext(), "Authentication failed!", Toast.LENGTH_SHORT).show();
                biometricPrompt.authenticate(promptInfo);
            }
        });

        try {
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(getString(R.string.biometric_title))
                    .setDescription(getString(R.string.biometric_desc))
                    .setAllowedAuthenticators(BIOMETRIC_WEAK | DEVICE_CREDENTIAL)
                    .build();
        } catch (Exception e) {
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(getString(R.string.biometric_title))
                    .setDescription(getString(R.string.biometric_desc))
                    .setDeviceCredentialAllowed(true) // for below API 28
                    .build();
        }


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
            activity.startVault();

//            biometricPrompt.authenticate(promptInfo);
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