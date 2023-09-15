package com.applock.fingerprint.passwordlock.ui.fragments;

import static com.applock.fingerprint.passwordlock.utils.Utility.openPrivacyPolicy;
import static com.applock.fingerprint.passwordlock.utils.Utility.reviewDialog;
import static com.applock.fingerprint.passwordlock.utils.Utility.shareAppDialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.FragmentNavigationDrawerBinding;
import com.applock.fingerprint.passwordlock.ui.activity.ImageIntruderActivity;
import com.applock.fingerprint.passwordlock.ui.activity.MainActivity;
import com.applock.fingerprint.passwordlock.ui.activity.MainSettingsActivity;

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

        binding.btnIntruder.setOnClickListener(view -> {
            activity.closeDrawer();
            startActivity(new Intent(activity, ImageIntruderActivity.class));
        });

        binding.navPolicy.setOnClickListener(view -> {
            activity.closeDrawer();
            openPrivacyPolicy(activity);
        });

        binding.navShare.setOnClickListener(view -> {
            activity.closeDrawer();
            shareAppDialog(activity);
        });

        binding.navRate.setOnClickListener(view -> {
            activity.closeDrawer();
            reviewDialog(activity);
        });
        binding.navSetting.setOnClickListener(view -> {
            activity.closeDrawer();
            startActivity(new Intent(activity, MainSettingsActivity.class));
        });

    }


}