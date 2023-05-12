package com.applock.photos.videos.ui.fragments;

import static com.applock.photos.videos.utils.Utility.getAllImageFolders;
import static com.applock.photos.videos.utils.Utility.getAllVideoFolders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.photos.videos.R;
import com.applock.photos.videos.adapter.AlbumAdapter;
import com.applock.photos.videos.adapter.ContentAdapter;
import com.applock.photos.videos.databinding.FragmentVaultBinding;
import com.applock.photos.videos.interfaces.AlbumClickedInterface;
import com.applock.photos.videos.model.ImageFolder;
import com.applock.photos.videos.ui.activity.HiddenImagesActivity;
import com.applock.photos.videos.ui.activity.MainActivity;
import com.applock.photos.videos.utils.SharePreferences;

import java.util.ArrayList;

public class VaultFragment extends Fragment implements AlbumClickedInterface {

    FragmentVaultBinding binding;
    MainActivity activity;
    int i = 1;
    AlbumAdapter adapter;
    SharePreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vault, container, false);

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        activity = (MainActivity) requireActivity();

        preferences = new SharePreferences(requireContext());

        adapter = new AlbumAdapter(this);
        binding.allRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (i == 1) {
            adapter.submitList(getAllImageFolders(requireContext()));
            adapter.setType(i);
        }
        if (i == 2) {
            adapter.submitList(getAllVideoFolders(requireContext()));
            adapter.setType(i);
        }

        binding.btnAlbum.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            }
            if (i != 1) {
                binding.btnAlbum.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                binding.btnVideos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.allRecyclerView.post(() -> adapter.submitList(getAllImageFolders(requireContext())));
                i = 1;
                adapter.setType(i);
                binding.contentRecyclerView.setVisibility(View.GONE);
                binding.allRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        binding.btnVideos.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            }
            if (i != 2) {
                binding.btnAlbum.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnVideos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                binding.allRecyclerView.post(() -> adapter.submitList(getAllVideoFolders(requireContext())));
                i = 2;
                adapter.setType(i);
                binding.contentRecyclerView.setVisibility(View.GONE);
                binding.allRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        binding.btnBack.setOnClickListener(view -> {
            if (i == 3 || i == 4) {
                binding.contentRecyclerView.setVisibility(View.GONE);
                binding.allRecyclerView.setVisibility(View.VISIBLE);
                return;
            }
            activity.startAppLock();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (i == 3 || i == 4) {
                    binding.contentRecyclerView.setVisibility(View.GONE);
                    binding.allRecyclerView.setVisibility(View.VISIBLE);
                    return;
                }
                activity.startAppLock();
            }
        });

        binding.btnBack.setOnClickListener(view -> {
            if (i == 3 || i == 4) {
                binding.contentRecyclerView.setVisibility(View.GONE);
                binding.allRecyclerView.setVisibility(View.VISIBLE);
                return;
            }
            activity.startAppLock();
        });

        binding.btnMenu.setOnClickListener(this::showPopMenu);

    }

    private void showPopMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_hide_menu, popupMenu.getMenu());
        popupMenu.getMenu().getItem(1).setVisible(false);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_hidden) {
//                showHiddenItems();
                startActivity(new Intent(requireContext(), HiddenImagesActivity.class));
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


    @Override
    public void onItemClicked(ImageFolder data, int type) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.allRecyclerView.setVisibility(View.GONE);
        binding.contentRecyclerView.setVisibility(View.GONE);

        if (type == 1) {
            this.i = 3;
        }
        if (type == 2) {
            this.i = 4;
        }
        startFragment(data.getImagePaths());
    }

    private void startFragment(ArrayList<String> list) {
        ContentAdapter adapter = new ContentAdapter();
        binding.contentRecyclerView.setAdapter(adapter);
        binding.contentRecyclerView.post(() -> adapter.submitList(list));

        if (i == 3) adapter.setType(1);
        if (i == 4) adapter.setType(2);

        binding.contentRecyclerView.scrollToPosition(0);

        binding.progressBar.setVisibility(View.GONE);
        binding.allRecyclerView.setVisibility(View.GONE);
        binding.contentRecyclerView.setVisibility(View.VISIBLE);
    }
}