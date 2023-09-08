package com.applock.photos.videos.ui.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityFullImageBinding;
import com.applock.photos.videos.utils.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class FullImageActivity extends AppCompatActivity {

    ActivityFullImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_image);

        String filepath = getIntent().getStringExtra("myimgfile");

        Glide.with(this)
                .load(filepath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageviewFullimg);


    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);

        binding.btnBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
        overridePendingTransition(0, 0);

    }

}