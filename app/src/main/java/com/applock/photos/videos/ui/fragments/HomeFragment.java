package com.applock.photos.videos.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.photos.videos.R;
import com.applock.photos.videos.adapter.AppLockAdapter;
import com.applock.photos.videos.databinding.FragmentHomeBinding;
import com.applock.photos.videos.interfaces.AppsClickedInterface;
import com.applock.photos.videos.model.AppsModel;
import com.applock.photos.videos.model.CommLockInfo;
import com.applock.photos.videos.ui.activity.MainActivity;
import com.applock.photos.videos.ui.activity.ThemesActivity;
import com.applock.photos.videos.ui.ext.LockMainContract;
import com.applock.photos.videos.ui.ext.LockMainPresenter;
import com.applock.photos.videos.singletonClass.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements LockMainContract.View, AppsClickedInterface {

    FragmentHomeBinding binding;
    AppLockAdapter recommendedAdapter, adapter;
    MainActivity activity;
    LockMainPresenter mLockMainPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        activity = (MainActivity) requireActivity();

        recommendedAdapter = new AppLockAdapter(this);
        adapter = new AppLockAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recommendedRecyclerView.setAdapter(recommendedAdapter);

        mLockMainPresenter = new LockMainPresenter(this, requireContext());
        mLockMainPresenter.loadAppInfo(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.btnHideApps.setOnClickListener(view -> activity.startHideApps());
        binding.btnVault.setOnClickListener(view -> activity.initVault());
        binding.btnStatusSaver.setOnClickListener(view -> activity.startStatusSaver());


        binding.btnMenu.setOnClickListener(v -> {
            activity.drawerOpen();
        });
        binding.btnTheme.setOnClickListener(v -> {
            startActivity(new Intent(activity, ThemesActivity.class));
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                openCloseDialog();
            }
        });


    }

    private void openCloseDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure want to close this app?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            System.exit(0);
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }


    private void unlockData(List<CommLockInfo> list, List<CommLockInfo> lockedApps) {
        List<CommLockInfo> favApps = new ArrayList<>();
        List<String> strings = MyApplication.getPreferences().getFavoriteApps();

        binding.recommendedRecyclerView.setVisibility(View.VISIBLE);
        binding.tvRec.setVisibility(View.VISIBLE);
        binding.tvApps.setText("More Apps");

        for (CommLockInfo info : lockedApps){
            for (String s : strings){
                if (s.equals(info.getPackageName())){
                    favApps.add(info);
                }
            }
        }

        new Handler().post(()->{
            recommendedAdapter.submitList(favApps);
            adapter.submitList(list);
        });

    }

    private void lockedData(List<CommLockInfo> lockedApps) {
        binding.recommendedRecyclerView.setVisibility(View.GONE);
        binding.tvRec.setVisibility(View.GONE);
        binding.tvApps.setText("Locked Apps");
        new Handler().post(()-> {
            adapter.submitList(lockedApps);
        });
    }


    @Override
    public void onItemClicked(AppsModel model) {

    }

    @Override
    public void onItemClicked(CommLockInfo model) {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void loadAppInfoSuccess(List<CommLockInfo> list) {
        List<CommLockInfo> lockedApps = new ArrayList<>();
        List<CommLockInfo> unLockedApps = new ArrayList<>();

        for (CommLockInfo info : list){
            if (info.isLocked())
                lockedApps.add(info);
            else
                unLockedApps.add(info);
        }

        binding.group.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.rd_unlocked:
                    unlockData(unLockedApps, lockedApps);
                    break;
                case R.id.rd_locked:
                    lockedData(lockedApps);
                    break;
            }
        });

        binding.rdUnlocked.setChecked(true);
        unlockData(unLockedApps, lockedApps);

    }
}