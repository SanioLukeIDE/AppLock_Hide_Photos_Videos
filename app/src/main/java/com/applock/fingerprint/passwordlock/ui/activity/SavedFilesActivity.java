package com.applock.fingerprint.passwordlock.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivitySavedFilesBinding;
import com.applock.fingerprint.passwordlock.ui.fragments.DownloadFilesFragment;
import com.applock.fingerprint.passwordlock.ui.fragments.WhatsAppFilesFragment;
import com.applock.fingerprint.passwordlock.utils.Utility;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SavedFilesActivity extends AppCompatActivity {

    ActivitySavedFilesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_files);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, true, this::setTabs).attach();

        binding.btnBack.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }


    private void setTabs(TabLayout.Tab tab, int position) {
        if (position == 0) {
            tab.setText("WhatsApp");
            tab.setIcon(R.drawable.ic_whatsapp);
            return;
        }
        tab.setText("Downloader");
        tab.setIcon(R.drawable.ic_download);
    }

    private static class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new WhatsAppFilesFragment();
            }
            return new DownloadFilesFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}