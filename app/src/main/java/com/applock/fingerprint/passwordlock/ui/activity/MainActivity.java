package com.applock.fingerprint.passwordlock.ui.activity;

import static com.applock.fingerprint.passwordlock.utils.Const.LOCK_STATE;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.interfaces.AppInterfaces;
import com.adsmodule.api.adsModule.utils.Constants;
import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivityMainBinding;
import com.applock.fingerprint.passwordlock.databinding.DialogPermissionBinding;
import com.applock.fingerprint.passwordlock.service.AppLaunchReceiver;
import com.applock.fingerprint.passwordlock.service.LoadAppListService;
import com.applock.fingerprint.passwordlock.service.LockService;
import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;
import com.applock.fingerprint.passwordlock.ui.fragments.BiometricLockFragment;
import com.applock.fingerprint.passwordlock.ui.fragments.HideAppsFragment;
import com.applock.fingerprint.passwordlock.ui.fragments.HomeFragment;
import com.applock.fingerprint.passwordlock.ui.fragments.StatusSaverFragment;
import com.applock.fingerprint.passwordlock.ui.fragments.VaultFragment;
import com.applock.fingerprint.passwordlock.utils.LockUtil;
import com.applock.fingerprint.passwordlock.utils.SharePreferences;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    ActivityMainBinding binding;
    ActionBarDrawerToggle toggle;
    int val;
    SharePreferences preferences;

    public void requestPermissions() {
        if (!Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, PERMISSIONS_REQUEST_CODE);
        } /*else if (!hasUsageStatsPermission()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, PERMISSIONS_REQUEST_CODE);
        }*/
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

//        requestPermissions();
        preferences = new SharePreferences(getApplicationContext());

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        MyApplication.getPreferences().setFirstRun(false);
        // Check if the overlay permission is not already granted
        if (!Settings.canDrawOverlays(this)) {
            // If not granted, request the permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 11);
        }

        if (MyApplication.getPreferences().getLockBackground() == null)
            MyApplication.getPreferences().setLockBackground(getString(R.string.default_theme));

    }

    public void drawerOpen() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else binding.drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (val == 1) {
            startVault();
        }
        if (val == 2) {
            startStatusSaver();
        }
        if (val == 3) {
            startHideApps();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        startService(new Intent(this, LoadAppListService.class));
        startService(new Intent(this, AppLaunchReceiver.class));
        if (MyApplication.getPreferences().getFBoolean(LOCK_STATE)) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(getApplicationContext(), LockService.class));
            } else*/
            startService(new Intent(getApplicationContext(), LockService.class));
        }

        if (preferences.isFirstLock()) {
            showDialog();
        }

        binding.navView.bringToFront();

        binding.navView.setNavigationItemSelectedListener(item -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        binding.rdGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rd_app_lock:
                    val = 0;
                    startAppLock();
                    break;

                case R.id.rd_vault:
                    initVault();
                    break;

                case R.id.rd_status_saver:
                    val = 2;
                    startStatusSaver();
                    break;

                case R.id.rd_hide_apps:
                    val = 3;
                    startHideApps();
                    break;

            }
        });

    }


    private void showDialog() {
        if (!LockUtil.isStatAccessPermissionSet(MainActivity.this) && LockUtil.isNoOption(MainActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            DialogPermissionBinding permissionBinding = DialogPermissionBinding.inflate(LayoutInflater.from(getApplicationContext()));
            builder.setView(permissionBinding.getRoot());
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if (!dialog.isShowing()) dialog.show();
            permissionBinding.btnPermission.setOnClickListener(view -> {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent, 1);
                dialog.dismiss();
            });
        } else {
            gotoCreatePwdActivity();
        }
    }

    private void gotoCreatePwdActivity() {
        Intent intent = new Intent(MainActivity.this, CreatePwdActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public void closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void initVault() {
        startFragment(new BiometricLockFragment());
        binding.rdGroup.setVisibility(View.GONE);

    }

    public void startAppLock() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                startFragment(new HomeFragment());
                binding.rdAppLock.setText("App Lock");
                binding.rdVault.setText("");
                binding.rdStatusSaver.setText("");
                binding.rdHideApps.setText("");
                binding.rdAppLock.setChecked(true);
                binding.rdGroup.setVisibility(View.VISIBLE);
            }
        });


    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    public void startVault() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                val = 1;
                binding.rdVault.setChecked(true);
                startFragment(new VaultFragment());

                binding.rdAppLock.setText("");
                binding.rdVault.setText("Vault");
                binding.rdStatusSaver.setText("");
                binding.rdHideApps.setText("");

                binding.rdGroup.setVisibility(View.VISIBLE);
            }
        });



    }

    public void startStatusSaver() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {

                binding.rdStatusSaver.setChecked(true);
                startFragment(new StatusSaverFragment());

                binding.rdAppLock.setText("");
                binding.rdVault.setText("");
                binding.rdStatusSaver.setText("Status Saver");
                binding.rdHideApps.setText("");
            }
        });


    }

    public void startHideApps() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                binding.rdHideApps.setChecked(true);
                startFragment(new HideAppsFragment());
                binding.rdAppLock.setText("");
                binding.rdVault.setText("");
                binding.rdStatusSaver.setText("");
                binding.rdHideApps.setText("Hide");
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }
}