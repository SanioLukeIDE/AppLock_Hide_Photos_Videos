package com.applock.fingerprint.passwordlock.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.adapter.SavedFilesAdapter;
import com.applock.fingerprint.passwordlock.databinding.FragmentDownloadFilesBinding;
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.Constants;
import com.applock.fingerprint.passwordlock.ui.activity.SavedFilesActivity;
import com.applock.fingerprint.passwordlock.utils.Utility;

public class DownloadFilesFragment extends Fragment {

    FragmentDownloadFilesBinding binding;
    SavedFilesActivity activity;
    SavedFilesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_download_files, container, false);

        activity = (SavedFilesActivity) requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter = new SavedFilesAdapter(new SavedFilesAdapter.OnFileDeleted() {
            @Override
            public void fileDeleted() {
                setAdapter();
            }
        });
        setAdapter();
        binding.recyclerView.setAdapter(adapter);

    }

    private void setAdapter() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.directoryInstaShoryDirectorydownload;
        Utility.getAllImagesVideosFromFolderPath(path).observe(getViewLifecycleOwner(), data->{
            if (data.size() > 0){
                adapter.submitList(data);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.tvNot.setVisibility(View.GONE);
            } else {
                binding.recyclerView.setVisibility(View.GONE);
                binding.tvNot.setVisibility(View.VISIBLE);
            }
        });

    }
}