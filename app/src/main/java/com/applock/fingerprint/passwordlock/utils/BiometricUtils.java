package com.applock.fingerprint.passwordlock.utils;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.service.CameraService;
import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;

import java.util.concurrent.Executor;

public class BiometricUtils {

    public int wrongBiometricCount = 0;
    BiometricManager biometricManager;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    Executor executor;

    public BiometricUtils(Activity activity, BiometricResult biometricResult) {
        biometricManager = BiometricManager.from(activity);
        executor = ContextCompat.getMainExecutor(activity);

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
                activity.startActivityForResult(enrollIntent, 1);
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                break;
        }

        biometricPrompt = new BiometricPrompt((FragmentActivity) activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
//                Log.e("onAuthenticationError: ", errorCode + " " + errString);
                if (errorCode == 14) biometricResult.authSuccess(true);
                else {
                    Toast.makeText(activity, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                biometricResult.authSuccess(true);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                wrongBiometricCount++;
                Toast.makeText(activity, "Authentication failed!", Toast.LENGTH_SHORT).show();
                biometricPrompt.authenticate(promptInfo);

                if (MyApplication.getPreferences().getAttemptFailedCount() == wrongBiometricCount) {
                    if (MyApplication.getPreferences().isIntruderDetectorOnOff()) {
                        activity.startService(new Intent(activity, CameraService.class));
                        wrongBiometricCount = 0;
                    }
                }

            }
        });

        try {
            promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle(activity.getString(R.string.biometric_title))
                    .setDescription(activity.getString(R.string.biometric_desc))
                    .setAllowedAuthenticators(BIOMETRIC_WEAK | DEVICE_CREDENTIAL).build();
        } catch (Exception e) {
            promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle(activity.getString(R.string.biometric_title))
                    .setDescription(activity.getString(R.string.biometric_desc)).setDeviceCredentialAllowed(true) // for below API 28
                    .build();
        }

        biometricPrompt.authenticate(promptInfo);
    }

    public interface BiometricResult {
        void authSuccess(boolean isAuthenticate);
    }

}
