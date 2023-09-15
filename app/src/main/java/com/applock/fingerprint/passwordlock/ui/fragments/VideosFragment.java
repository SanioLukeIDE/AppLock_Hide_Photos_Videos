package com.applock.fingerprint.passwordlock.ui.fragments;

import static com.applock.fingerprint.passwordlock.utils.Utility.getAllVideoFolders;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.adapter.AlbumAdapter;
import com.applock.fingerprint.passwordlock.adapter.ContentAdapter;
import com.applock.fingerprint.passwordlock.databinding.FragmentAlbumsBinding;
import com.applock.fingerprint.passwordlock.databinding.FragmentVideosBinding;
import com.applock.fingerprint.passwordlock.interfaces.AlbumClickedInterface;
import com.applock.fingerprint.passwordlock.model.ImageFolder;
import com.applock.fingerprint.passwordlock.ui.activity.MainActivity;
import com.applock.fingerprint.passwordlock.utils.SharePreferences;

import java.util.ArrayList;

public class VideosFragment extends Fragment implements AlbumClickedInterface {

    FragmentAlbumsBinding binding;
    AlbumAdapter adapter;
    MainActivity activity;
    SharePreferences preferences;
    int i = 2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_albums, container, false);

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

        activity = (MainActivity) requireActivity();

        preferences = new SharePreferences(activity);

        adapter = new AlbumAdapter(this);
        binding.allRecyclerView.setAdapter(adapter);

        adapter.submitList(getAllVideoFolders(requireContext()));
        adapter.setType(i);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               backPressed(activity);
            }
        });


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

    public void backPressed(MainActivity activity) {
        if (i == 3 || i == 4) {
            binding.contentRecyclerView.setVisibility(View.GONE);
            binding.allRecyclerView.setVisibility(View.VISIBLE);
            i=2;
            return;
        }
        activity.startAppLock();
    }
}