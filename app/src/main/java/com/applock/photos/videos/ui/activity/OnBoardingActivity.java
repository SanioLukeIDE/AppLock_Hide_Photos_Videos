package com.applock.photos.videos.ui.activity;

import static com.applock.photos.videos.utils.Utility.setFullScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.applock.photos.videos.R;
import com.applock.photos.videos.adapter.OnBoardingAdapter;
import com.applock.photos.videos.databinding.ActivityOnBoardingBinding;
import com.applock.photos.videos.model.DataModel;
import com.applock.photos.videos.utils.MyApp;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {

    ActivityOnBoardingBinding binding;
    OnBoardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding);
        setFullScreen(this);

        adapter = new OnBoardingAdapter();
        adapter.submitList(getBoardList());
        binding.viewPager.setAdapter(adapter);

        setOnBoardingIndicator();
        setCurrentOnBoardingIndicators(0);

    }

    private List<DataModel> getBoardList() {
        List<DataModel> list = new ArrayList<>();

        list.add(new DataModel(getString(R.string.title1), getString(R.string.desc1), R.drawable.img_onboard_1, false));
        list.add(new DataModel(getString(R.string.title2), getString(R.string.desc2), R.drawable.img_onboard_2, false));
        list.add(new DataModel(getString(R.string.title3), getString(R.string.desc3), R.drawable.img_onboard_3, false));
        list.add(new DataModel(getString(R.string.title4), getString(R.string.desc4), R.drawable.img_onboard_4, false));

        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnBoardingIndicators(position);
            }
        });

        binding.btnNext.setOnClickListener(view -> {
            if (binding.viewPager.getCurrentItem() + 1 < adapter.getItemCount()) {
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            } else {
                MyApp.getPreferences().setFirstRun(false);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

    }

    private void setOnBoardingIndicator() {
        ImageView[] indicators = new ImageView[adapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot));
            indicators[i].setLayoutParams(layoutParams);
            binding.dotsLayout.addView(indicators[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCurrentOnBoardingIndicators(int index) {
        int childCount = binding.dotsLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.dotsLayout.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot_selected));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dot));
            }
        }

        if (index == 3) {
            binding.next.setVisibility(View.GONE);
            binding.tvStart.setVisibility(View.VISIBLE);
        } else {
            binding.next.setVisibility(View.VISIBLE);
            binding.tvStart.setVisibility(View.GONE);
        }

    }

}