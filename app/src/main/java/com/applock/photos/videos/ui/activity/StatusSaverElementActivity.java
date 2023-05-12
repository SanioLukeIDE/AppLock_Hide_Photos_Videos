package com.applock.photos.videos.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityStatusSaverElementBinding;
import com.applock.photos.videos.model.DataModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class StatusSaverElementActivity extends AppCompatActivity {

    ActivityStatusSaverElementBinding binding;
    DataModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_status_saver_element);

        model = (DataModel) getIntent().getSerializableExtra("model");

        binding.title.setText(model.getName());
        Glide.with(getApplicationContext()).load(model.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.image);

    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.btnBack.setOnClickListener(view -> finish());

    }
}