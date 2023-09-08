package com.applock.photos.videos.ui.fragments;

import static com.applock.photos.videos.utils.Utility.getAllInstalledApps;
import static com.applock.photos.videos.utils.Utility.hideApp;

import android.app.AlertDialog;
import android.app.role.RoleManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.applock.photos.videos.R;
import com.applock.photos.videos.adapter.AppHIdeAdapter;
import com.applock.photos.videos.databinding.DialogHideAppBinding;
import com.applock.photos.videos.databinding.FragmentHideAppsBinding;
import com.applock.photos.videos.interfaces.AppsClickedInterface;
import com.applock.photos.videos.model.AppsModel;
import com.applock.photos.videos.model.CommLockInfo;
import com.applock.photos.videos.ui.activity.CalculatorLockActivity;
import com.applock.photos.videos.ui.activity.HiddenImagesActivity;
import com.applock.photos.videos.ui.activity.MainActivity;
import com.applock.photos.videos.utils.BiometricUtils;
import com.applock.photos.videos.singletonClass.MyApplication;
import com.applock.photos.videos.utils.SharePreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class HideAppsFragment extends Fragment implements AppsClickedInterface {

    FragmentHideAppsBinding binding;
    int i = 1;
    MainActivity activity;
    AppHIdeAdapter adapter;
    SharePreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hide_apps, container, false);

        activity = (MainActivity) requireActivity();

        preferences = new SharePreferences(requireContext());

        adapter = new AppHIdeAdapter(false, this);
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.post(() -> adapter.submitList(getAllInstalledApps(requireContext())));

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                activity.startAppLock();
            }
        });

        binding.btnBack.setOnClickListener(view -> activity.startAppLock());


        binding.btnSocial.setOnClickListener(view -> {
            if (i != 1) {
                binding.btnSocial.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                binding.btnCommunication.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnPhoto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnEntertainment.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                i = 1;
            }
        });

        binding.btnCommunication.setOnClickListener(view -> {
            if (i != 2) {
                binding.btnSocial.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnCommunication.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                binding.btnPhoto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnEntertainment.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                i = 2;
            }
        });

        binding.btnPhoto.setOnClickListener(view -> {
            if (i != 3) {
                binding.btnSocial.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnCommunication.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnPhoto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                binding.btnEntertainment.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                i = 3;
            }
        });

        binding.btnEntertainment.setOnClickListener(view -> {
            if (i != 4) {
                binding.btnSocial.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnCommunication.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnPhoto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                binding.btnEntertainment.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                i = 4;
            }
        });

        binding.btnMore.setOnClickListener(this::showPopMenu);

    }

    private void showPopMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_hide_menu, popupMenu.getMenu());
        popupMenu.getMenu().getItem(0).setVisible(false);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_hidden_apps) {
                if (MyApplication.getPreferences().isCalculatorLock()) {
                    startActivity(new Intent(activity, CalculatorLockActivity.class).putExtra("hiddenPage", 1));
                } else {
                    new BiometricUtils(activity, isAuthenticate -> startActivity(new Intent(activity, HiddenImagesActivity.class).putExtra("type", 1)));
                }
            }
            return true;
        });

        popupMenu.show();
    }

    @Override
    public void onItemClicked(AppsModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        DialogHideAppBinding appBinding = DialogHideAppBinding.inflate(LayoutInflater.from(activity));
        builder.setView(appBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Glide.with(activity).load(model.getIcon()).diskCacheStrategy(DiskCacheStrategy.ALL).into(appBinding.logo);

        appBinding.tvDesc.setText("Import " + model.getAppName() + " App and Hide the application.");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            RoleManager roleManager = activity.getSystemService(RoleManager.class);
            if (roleManager.isRoleAvailable(RoleManager.ROLE_ASSISTANT)) {
                Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_ASSISTANT);
                startActivityForResult(intent, 1);
            }
        }
        appBinding.btnHide.setOnClickListener(view -> {
            if (MyApplication.getPreferences().isCalculatorLock()) {
                startActivity(new Intent(activity, CalculatorLockActivity.class).putExtra("isHide", true).putExtra("package", model.getPackageName()).putExtra("name", model.getComponentName()));
            } else {
                new BiometricUtils(activity, isAuthenticate -> {
                    authSuccess(model);
                });
            }
            dialog.dismiss();
        });

    }

    private void authSuccess(AppsModel model) {
        try {
            hideApp(activity, model.getComponentName());
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", model.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
//                String s = e.getLocalizedMessage();
//                Log.e( "onItemClicked: ", s);
//                activity.requestPermissions();
        }
        preferences.setHideApps(model.getPackageName());
    }


    @Override
    public void onItemClicked(CommLockInfo model) {

    }
}