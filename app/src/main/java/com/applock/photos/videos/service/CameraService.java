package com.applock.photos.videos.service;

import static com.applock.photos.videos.singletonClass.AppOpenAds.currentActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CameraService extends Service {

    ProcessCameraProvider cameraProvider;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCameraPreview();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraProvider != null) cameraProvider.unbindAll();
    }

    private void capturePhoto() {
        File photoFile = createPhotoFile();
        if (photoFile != null) {
            ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).setMetadata(new ImageCapture.Metadata()).build();

            imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    // Apply the filter or background to the captured photo
                    Toast.makeText(getApplicationContext(), "Intruder Detected.", Toast.LENGTH_SHORT).show();
                    onDestroy();
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    Log.e("CameraActivity", "Error capturing photo: " + exception.getMessage(), exception);
                }
            });
        }
    }

    private File createPhotoFile() {
        String timeStamp = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(System.currentTimeMillis());
//        long timeStamp = System.currentTimeMillis();
        File storageDir = getExternalFilesDir("Intruders");
        File photoFile;
        try {
            photoFile = File.createTempFile("Intruders_" + timeStamp, ".jpeg", storageDir);
            return photoFile;
        } catch (Exception e) {
            Log.e("CameraActivity", "Error creating photo file", e);
        }
        return null;
    }

    private void startCameraPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                previewCamera(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(this));
    }

    @SuppressLint("RestrictedApi")
    private void previewCamera(ProcessCameraProvider cameraProvider) {
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        Preview preview = new Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3).build();
        imageCapture = new ImageCapture.Builder().build();

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) currentActivity, cameraSelector, preview, imageCapture);

        capturePhoto();
    }

}
