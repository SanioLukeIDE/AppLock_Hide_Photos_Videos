package com.applock.fingerprint.passwordlock.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivityPlayerBinding;

public class PlayerActivity extends AppCompatActivity {

    ActivityPlayerBinding binding;
    PlayerActivity activity;
    ExoPlayer exoPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        activity = PlayerActivity.this;

        exoPlayer = new ExoPlayer.Builder(activity).build();
        String data = getIntent().getStringExtra("data");

        MediaItem mediaItem = MediaItem.fromUri(data);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();


    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);

        binding.playerView.setPlayer(exoPlayer);

        binding.btnBack.setOnClickListener(view -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        exoPlayer.release();
        finish();
        overridePendingTransition(0, 0);

    }

}