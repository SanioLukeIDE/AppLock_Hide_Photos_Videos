package com.applock.photos.videos.ui.fragments;

import static com.applock.photos.videos.utils.Utility.getInstalledApps;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.photos.videos.R;
import com.applock.photos.videos.adapter.AppLockAdapter;
import com.applock.photos.videos.databinding.FragmentHomeBinding;
import com.applock.photos.videos.interfaces.AppsClickedInterface;
import com.applock.photos.videos.model.AppsModel;
import com.applock.photos.videos.ui.activity.MainActivity;

public class HomeFragment extends Fragment implements AppsClickedInterface {

    FragmentHomeBinding binding;
    AppLockAdapter recommendedAdapter, adapter;
    MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        activity = (MainActivity) requireActivity();

        recommendedAdapter = new AppLockAdapter(true, this);
        adapter = new AppLockAdapter(true, this);
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

        binding.rdUnlocked.setChecked(true);
        unlockData();

        binding.group.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.rd_unlocked:
                    unlockData();
                    break;
                case R.id.rd_locked:
                    lockedData();
                    break;
            }
        });

        binding.btnMenu.setOnClickListener(v -> {
            activity.drawerOpen();
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


    private void unlockData() {
        adapter.setType(1);
        recommendedAdapter.setType(1);

        recommendedAdapter.submitList(getInstalledApps(requireContext(), true));
        adapter.submitList(getInstalledApps(requireContext(),false));

    }

    private void lockedData() {
        adapter.setType(2);
        recommendedAdapter.setType(2);

        recommendedAdapter.submitList(getInstalledApps(requireContext(), true));
        adapter.submitList(getInstalledApps(requireContext(),false));

    }


    @Override
    public void onItemClicked(AppsModel model) {

    }
}