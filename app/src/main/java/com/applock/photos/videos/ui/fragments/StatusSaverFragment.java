package com.applock.photos.videos.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.photos.videos.R;
import com.applock.photos.videos.adapter.StatusListAdapter;
import com.applock.photos.videos.databinding.FragmentStatusSaverBinding;
import com.applock.photos.videos.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class StatusSaverFragment extends Fragment {

    FragmentStatusSaverBinding binding;
    StatusListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_status_saver, container, false);

        adapter = new StatusListAdapter();
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.submitList(getSites());

    }

    public List<DataModel> getSites(){
        List<DataModel> list = new ArrayList<>();

        list.add(new DataModel("Instagram", "", R.drawable.img_instagram, R.color.insta_color));
        list.add(new DataModel("WhatsApp", "", R.drawable.img_whatsapp, R.color.whatsapp_color));
        list.add(new DataModel("Facebook", "", R.drawable.img_fb, R.color.fb_color));
        list.add(new DataModel("Twitter", "", R.drawable.img_twitter, R.color.twitter_color));
        list.add(new DataModel("Tik Tok", "", R.drawable.img_tik_tok, R.color.tiktok_color));
        list.add(new DataModel("Snapchat", "", R.drawable.img_snapchat, R.color.snap_color));
        list.add(new DataModel("Chingari", "", R.drawable.ic_chingari, R.color.chingari_color));

        return list;
    }

}