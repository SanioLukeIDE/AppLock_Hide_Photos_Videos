package com.applock.photos.videos.ui.activity;

import static com.applock.photos.videos.utils.Utility.getAllDisableApps;
import static com.applock.photos.videos.utils.Utility.hideApp;
import static com.applock.photos.videos.utils.Utility.hideUnHideApps;
import static com.applock.photos.videos.utils.Utility.unHideApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.applock.photos.videos.R;
import com.applock.photos.videos.adapter.AppHIdeAdapter;
import com.applock.photos.videos.adapter.AppLockAdapter;
import com.applock.photos.videos.adapter.ContentAdapter;
import com.applock.photos.videos.databinding.ActivityHiddenImagesBinding;
import com.applock.photos.videos.databinding.DialogHideAppBinding;
import com.applock.photos.videos.interfaces.AppsClickedInterface;
import com.applock.photos.videos.model.AppsModel;
import com.applock.photos.videos.model.CommLockInfo;
import com.applock.photos.videos.utils.SharePreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class HiddenImagesActivity extends AppCompatActivity implements AppsClickedInterface {

    ActivityHiddenImagesBinding binding;
    SharePreferences preferences;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hidden_images);

        preferences = new SharePreferences(getApplicationContext());

        type = getIntent().getIntExtra("type", 0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.btnBack.setOnClickListener(view -> finish());

       if (type == 0){
           ContentAdapter adapter = new ContentAdapter();
           binding.contentRecyclerView.setAdapter(adapter);

           if (preferences.getHiddenImages().size() > 0) {
               adapter.submitList(preferences.getHiddenImages());
               binding.tvNot.setVisibility(View.GONE);
               binding.contentRecyclerView.setVisibility(View.VISIBLE);
           } else {
               binding.tvNot.setVisibility(View.VISIBLE);
               binding.contentRecyclerView.setVisibility(View.GONE);
           }

           adapter.setType(1);
       }
       if (type == 1){
           AppHIdeAdapter adapter = new AppHIdeAdapter(false, this);
           binding.contentRecyclerView.setAdapter(adapter);

           List<AppsModel> appsModels = getAllDisableApps(getApplicationContext());

           if (appsModels.size() > 0) {
               adapter.submitList(appsModels);
               binding.tvNot.setVisibility(View.GONE);
               binding.contentRecyclerView.setVisibility(View.VISIBLE);
           } else {
               binding.tvNot.setVisibility(View.VISIBLE);
               binding.contentRecyclerView.setVisibility(View.GONE);
           }

           adapter.setType(1);
       }

    }

    @Override
    public void onItemClicked(AppsModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HiddenImagesActivity.this);
        DialogHideAppBinding appBinding = DialogHideAppBinding.inflate(LayoutInflater.from(getApplicationContext()));
        builder.setView(appBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        appBinding.btnHide.setText("Unhide App");
        appBinding.btnHide.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_lock_open), null);


        Glide.with(getApplicationContext()).load(model.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(appBinding.logo);

        appBinding.tvDesc.setText("Import "+model.getAppName()+" App and Unhide the application.");

        appBinding.btnHide.setOnClickListener(view -> {
            try {
                unHideApp(getApplicationContext(), model.getComponentName());
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", model.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
            dialog.dismiss();

        });

    }

    @Override
    public void onItemClicked(CommLockInfo model) {

    }
}