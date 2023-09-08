package com.applock.photos.videos.ui.fragments;

import static com.applock.photos.videos.utils.Utility.openPrivacyPolicy;
import static com.applock.photos.videos.utils.Utility.reviewDialog;
import static com.applock.photos.videos.utils.Utility.shareAppDialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.photos.videos.BuildConfig;
import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.FragmentNavigationDrawerBinding;
import com.applock.photos.videos.ui.activity.ImageIntruderActivity;
import com.applock.photos.videos.ui.activity.MainActivity;
import com.applock.photos.videos.ui.activity.MainSettingsActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import java.util.Objects;

public class NavigationDrawerFragment extends Fragment {

    FragmentNavigationDrawerBinding binding;
    MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_navigation_drawer, container, false);

        activity = (MainActivity) requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.btnAppLock.setOnClickListener(view -> {
            activity.closeDrawer();
            activity.startAppLock();
        });

        binding.btnHideApps.setOnClickListener(view -> {
            activity.closeDrawer();
            activity.startHideApps();
        });

        binding.btnVault.setOnClickListener(view -> {
            activity.closeDrawer();
            activity.initVault();
        });

        binding.btnStatusSaver.setOnClickListener(view -> {
            activity.closeDrawer();
            activity.startStatusSaver();
        });

        binding.navSetting.setOnClickListener(view -> {
            activity.closeDrawer();
            startActivity(new Intent(activity, MainSettingsActivity.class));
        });
        binding.btnIntruder.setOnClickListener(view -> {
            activity.closeDrawer();
            startActivity(new Intent(activity, ImageIntruderActivity.class));
        });

        binding.navPolicy.setOnClickListener(view -> {
            openPrivacyPolicy(activity);
        });

        binding.navShare.setOnClickListener(view -> {
            shareAppDialog(activity);
        });

        binding.navRate.setOnClickListener(view -> {
            reviewDialog(activity);
        });

    }


}