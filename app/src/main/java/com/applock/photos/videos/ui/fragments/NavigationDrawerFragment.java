package com.applock.photos.videos.ui.fragments;

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
import com.applock.photos.videos.ui.activity.MainActivity;
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

        String link = "https://play.google.com/store/apps/details?id=com.instag.caption.hashtag.genrator";
        String link1 = "https://play.google.com/store/apps/details?id=com.photosnap.pfpeditor.instamaker";

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

        binding.navPolicy.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://invotechgirge.blogspot.com/p/privacy-policy_8.html"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.navShare.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Looking to take your Instagram game to the next level? Look no further than " + getString(R.string.app_name) + "!!\n" +
                    "With our multiple editing features you'll be able to create stunning content that will help you stand out on the platform. \n" +
                    "Download " + getString(R.string.app_name) + " today and elevate your Instagram presence!\n" +
                    "\nJoin now with the link: \nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        binding.navRate.setOnClickListener(view -> {
            reviewDialog();
        });

    }

    private void reviewDialog() {
        ReviewManager manager = ReviewManagerFactory.create(requireActivity());
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(requireActivity(), reviewInfo);
                flow.addOnCompleteListener(task1 -> {

                });
            } else {
                // There was some problem, log or handle the error code.
                Log.e("reviewDialog: ", Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        });

    }

}