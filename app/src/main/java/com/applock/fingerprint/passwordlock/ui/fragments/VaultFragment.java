package com.applock.fingerprint.passwordlock.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.FragmentVaultBinding;
import com.applock.fingerprint.passwordlock.ui.activity.HiddenImagesActivity;
import com.applock.fingerprint.passwordlock.ui.activity.MainActivity;
import com.applock.fingerprint.passwordlock.utils.Utility;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class VaultFragment extends Fragment {

    FragmentVaultBinding binding;
    MainActivity activity;
    int i = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vault, container, false);

        activity = (MainActivity) requireActivity();

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(activity);
        binding.viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, true, this::setTabs).attach();

        return binding.getRoot();
    }

    private void setTabs(TabLayout.Tab tab, int position) {
        if (position == 0) {
            tab.setText("Albums");
            tab.setIcon(R.drawable.ic_albums_outline);
            return;
        }
        tab.setText("Videos");
        tab.setIcon(R.drawable.ic_video);
    }


    @Override
    public void onResume() {
        super.onResume();

        binding.btnBack.setOnClickListener(view -> {
            if (binding.viewPager.getCurrentItem() == 0){
                new AlbumsFragment().backPressed(activity);
            } else new VideosFragment().backPressed(activity);
        });

        binding.btnMenu.setOnClickListener(this::showPopMenu);

    }

    private void showPopMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_hide_menu, popupMenu.getMenu());
        popupMenu.getMenu().getItem(1).setVisible(false);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_hidden) {
                Utility.nextActivity(activity, HiddenImagesActivity.class, false);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


    private static class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new AlbumsFragment();
            }
            return new VideosFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}