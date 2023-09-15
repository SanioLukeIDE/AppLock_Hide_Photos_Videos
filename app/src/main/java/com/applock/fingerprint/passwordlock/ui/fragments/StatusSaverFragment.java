package com.applock.fingerprint.passwordlock.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.adapter.StatusListAdapter;
import com.applock.fingerprint.passwordlock.databinding.FragmentStatusSaverBinding;
import com.applock.fingerprint.passwordlock.model.DataModel;
import com.applock.fingerprint.passwordlock.ui.activity.MainActivity;
import com.applock.fingerprint.passwordlock.ui.activity.SavedFilesActivity;
import com.applock.fingerprint.passwordlock.utils.SharePreferences;

import java.util.ArrayList;
import java.util.List;

public class StatusSaverFragment extends Fragment {

    FragmentStatusSaverBinding binding;
    MainActivity activity;
    SharePreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_status_saver, container, false);

        activity = (MainActivity) requireActivity();

        preferences = new SharePreferences(requireContext());

        StatusListAdapter adapter = new StatusListAdapter();
        binding.recyclerView.setAdapter(adapter);
        adapter.submitList(getSites());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                activity.startAppLock();
            }
        });

        binding.btnBack.setOnClickListener(view -> activity.startAppLock());
        binding.btnDownloadFiles.setOnClickListener(view -> startActivity(new Intent(activity, SavedFilesActivity.class)));

    }

    public List<DataModel> getSites(){
        List<DataModel> list = new ArrayList<>();

        list.add(new DataModel("WhatsApp", "", "", R.drawable.img_whatsapp, R.color.whatsapp_color));
        list.add(new DataModel("Instagram", "instagram.com", "", R.drawable.img_instagram, R.color.insta_color));
        list.add(new DataModel("Facebook", "facebook.com", "fb.watch", R.drawable.img_fb, R.color.fb_color));
        list.add(new DataModel("Twitter", "twitter.com", "", R.drawable.img_twitter, R.color.twitter_color));
        list.add(new DataModel("Tik Tok", "tiktok", "", R.drawable.img_tik_tok, R.color.tiktok_color));
        list.add(new DataModel("Snapchat", "snapchat.com", "", R.drawable.img_snapchat, R.color.snap_color));
        list.add(new DataModel("Chingari", "chingari", "", R.drawable.ic_chingari, R.color.chingari_color));

        return list;
    }

}