package com.applock.fingerprint.passwordlock.ui.activity;

import static com.applock.fingerprint.passwordlock.utils.Utility.getShimmer;
import static com.applock.fingerprint.passwordlock.utils.Utility.setFullScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivityPreviewBinding;
import com.applock.fingerprint.passwordlock.utils.SharePreferences;
import com.applock.fingerprint.passwordlock.utils.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class PreviewActivity extends AppCompatActivity {

    ActivityPreviewBinding binding;
    String path;
    File file;
    SharePreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview);
        setFullScreen(this);

        preferences = new SharePreferences(getApplicationContext());

        path = getIntent().getStringExtra("path");
        file = new File(path);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int imageHeight = options.outHeight;

        binding.image.setScaleType(imageHeight > 600 ? ImageView.ScaleType.CENTER_CROP : ImageView.ScaleType.FIT_CENTER);

        if (file.getName().startsWith(".")) {
            binding.btnHide.setText("Unhide");
            binding.btnHide.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_lock_open), null, null, null);
        }

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.btnBack.setOnClickListener(v -> onBackPressed());

        Glide.with(getApplicationContext())
                .load(path)
                .placeholder(getShimmer())
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }

        binding.btnDelete.setOnClickListener(view -> {
            if (file.exists()) {
                file.delete();
                Toast.makeText(this, "File deleted successfully.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .putExtra("val", 1));
                finish();
            }
        });


        binding.btnHide.setOnClickListener(view -> {
            if (file.exists()) {
                if (file.getName().startsWith(".")) {
                    File renamedFile = new File(file.getParent(), file.getName().substring(1));
                    if (file.renameTo(renamedFile)) {
                        // File was successfully unhidden
                        preferences.removeHiddenImage(file.getPath());
                        Toast.makeText(this, "File removed from hidden vault successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                .putExtra("val", 1));
                        finish();
                    } else {
                        // Failed to un-hide file
                        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    boolean success = file.renameTo(new File(file.getParentFile(), "." + file.getName()));
                    if (success) {
                        preferences.setHiddenImages(file.getParentFile() + "/." + file.getName());
                        Toast.makeText(this, "File added to hidden vault successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                .putExtra("val", 1));
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }
}