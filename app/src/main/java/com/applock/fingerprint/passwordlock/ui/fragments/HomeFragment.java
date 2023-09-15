package com.applock.fingerprint.passwordlock.ui.fragments;

import static com.applock.fingerprint.passwordlock.utils.Utility.openCloseDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.adapter.AppLockAdapter;
import com.applock.fingerprint.passwordlock.databinding.FragmentHomeBinding;
import com.applock.fingerprint.passwordlock.interfaces.AppsClickedInterface;
import com.applock.fingerprint.passwordlock.model.AppsModel;
import com.applock.fingerprint.passwordlock.model.CommLockInfo;
import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;
import com.applock.fingerprint.passwordlock.ui.activity.MainActivity;
import com.applock.fingerprint.passwordlock.ui.activity.ThemesActivity;
import com.applock.fingerprint.passwordlock.ui.ext.LockMainContract;
import com.applock.fingerprint.passwordlock.ui.ext.LockMainPresenter;
import com.applock.fingerprint.passwordlock.utils.Utility;

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

        mLockMainPresenter = new LockMainPresenter(this, requireContext());
        mLockMainPresenter.loadAppInfo(requireContext());

        recommendedAdapter = new AppLockAdapter(this, mLockMainPresenter);
        adapter = new AppLockAdapter(this, mLockMainPresenter);
        binding.recyclerView.setAdapter(adapter);
        binding.recommendedRecyclerView.setAdapter(recommendedAdapter);

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
            Utility.nextActivity(activity, ThemesActivity.class, false);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                openCloseDialog(activity);
            }
        });


    }


    private void unlockData(List<CommLockInfo> list, List<CommLockInfo> lockedApps) {
        List<CommLockInfo> favApps = new ArrayList<>();
        List<CommLockInfo> restApps = new ArrayList<>();
        List<String> strings = MyApplication.getPreferences().getFavoriteApps();

        binding.recommendedRecyclerView.setVisibility(View.VISIBLE);
        binding.tvRec.setVisibility(View.VISIBLE);
        binding.tvApps.setText("More Apps");

        MyApplication.getExecutorService().execute(()->{
            favApps.clear();
            restApps.clear();
            for(CommLockInfo info:list){
                if(info.isFavoriteApp()){
                    favApps.add(info);
                }else{
                    restApps.add(info);
                }
            }
            for(CommLockInfo info : lockedApps){
                if(strings.contains(info.getPackageName())){
                    favApps.add(info);
                }
            }
        });

        new Handler().post(() -> {
            recommendedAdapter.submitList(favApps);
            adapter.submitList(restApps);
        });

    }

    private void lockedData(List<CommLockInfo> lockedApps) {
        binding.recommendedRecyclerView.setVisibility(View.GONE);
        binding.tvRec.setVisibility(View.GONE);
        binding.tvApps.setText("Locked Apps");
        new Handler().post(() -> {
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

        for (CommLockInfo info : list) {
            if (info.isLocked())
                lockedApps.add(info);
            else
                unLockedApps.add(info);
        }

        binding.group.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rd_unlocked:
                    unlockData(unLockedApps, lockedApps);
                    break;
                case R.id.rd_locked:
                    lockedData(lockedApps);
                    break;
            }
//            mLockMainPresenter.loadAppInfo(activity);
        });

        binding.rdUnlocked.setChecked(true);
        unlockData(unLockedApps, lockedApps);

    }
}