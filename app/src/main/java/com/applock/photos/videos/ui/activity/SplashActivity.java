package com.applock.photos.videos.ui.activity;

import static com.applock.photos.videos.utils.Utility.setFullScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.applock.photos.videos.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setFullScreen(this);

        new Handler().postDelayed(()->{
            startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class));
            finish();
        }, 1500);

    }
}