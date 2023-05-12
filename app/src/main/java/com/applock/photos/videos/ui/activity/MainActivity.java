package com.applock.photos.videos.ui.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.RadioGroup;


import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityMainBinding;
import com.applock.photos.videos.ui.fragments.BiometricLockFragment;
import com.applock.photos.videos.ui.fragments.HideAppsFragment;
import com.applock.photos.videos.ui.fragments.HomeFragment;
import com.applock.photos.videos.ui.fragments.NavigationDrawerFragment;
import com.applock.photos.videos.ui.fragments.StatusSaverFragment;
import com.applock.photos.videos.ui.fragments.VaultFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ActionBarDrawerToggle toggle;
    int val;

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    public void requestPermissions() {
        if (!Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, PERMISSIONS_REQUEST_CODE);
        } else if (!hasUsageStatsPermission()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, PERMISSIONS_REQUEST_CODE);
        }
    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            requestPermissions();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        val = getIntent().getIntExtra("val", 0);

        requestPermissions();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CHANGE_COMPONENT_ENABLED_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CHANGE_COMPONENT_ENABLED_STATE, Manifest.permission.SYSTEM_ALERT_WINDOW }, 11);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    public void drawerOpen() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else
            binding.drawerLayout.openDrawer(GravityCompat.START);
    }


    @Override
    protected void onResume() {
        super.onResume();

        binding.navView.bringToFront();

        binding.navView.setNavigationItemSelectedListener(item -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        if (val == 1) {
            startVault();
            val = 0;
        }
        if (val == 3) {
            startHideApps();
            val = 0;
        }

        binding.rdGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rd_app_lock:
                    startAppLock();
                    break;

                case R.id.rd_vault:
                    initVault();
                    break;

                case R.id.rd_status_saver:
                    startStatusSaver();
                    break;

                case R.id.rd_hide_apps:
                    startHideApps();
                    break;

            }
        });

    }

    public void closeDrawer(){
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void initVault() {
        startFragment(new BiometricLockFragment());
        binding.rdGroup.setVisibility(View.GONE);

    }

    public void startAppLock() {
        startFragment(new HomeFragment());
        binding.rdAppLock.setText("App Lock");
        binding.rdVault.setText("");
        binding.rdStatusSaver.setText("");
        binding.rdHideApps.setText("");
        binding.rdAppLock.setChecked(true);
        binding.rdGroup.setVisibility(View.VISIBLE);

    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    public void startVault() {
        binding.rdVault.setChecked(true);
        startFragment(new VaultFragment());

        binding.rdAppLock.setText("");
        binding.rdVault.setText("Vault");
        binding.rdStatusSaver.setText("");
        binding.rdHideApps.setText("");

        binding.rdGroup.setVisibility(View.VISIBLE);

    }

    public void startStatusSaver() {
        startFragment(new StatusSaverFragment());

        binding.rdAppLock.setText("");
        binding.rdVault.setText("");
        binding.rdStatusSaver.setText("Status Saver");
        binding.rdHideApps.setText("");

        binding.rdStatusSaver.setChecked(true);

    }

    public void startHideApps() {
        startFragment(new HideAppsFragment());
        binding.rdAppLock.setText("");
        binding.rdVault.setText("");
        binding.rdStatusSaver.setText("");
        binding.rdHideApps.setText("Hide");

        binding.rdHideApps.setChecked(true);

    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }
}