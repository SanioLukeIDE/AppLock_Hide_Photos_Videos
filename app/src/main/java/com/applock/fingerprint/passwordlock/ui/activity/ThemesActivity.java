package com.applock.fingerprint.passwordlock.ui.activity;

import static com.applock.fingerprint.passwordlock.utils.Utility.readThemeJsonFromRaw;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.interfaces.AppInterfaces;
import com.adsmodule.api.adsModule.utils.Constants;
import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ActivityThemesBinding;
import com.applock.fingerprint.passwordlock.databinding.ViewThemeCardBinding;
import com.applock.fingerprint.passwordlock.model.LockThemeModel;
import com.applock.fingerprint.passwordlock.singletonClass.MyApplication;
import com.applock.fingerprint.passwordlock.utils.Utility;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ThemesActivity extends AppCompatActivity {

    ActivityThemesBinding binding;
    int page;
    ThemesActivity activity;
    DiffUtil.ItemCallback<LockThemeModel> diffCallback = new DiffUtil.ItemCallback<LockThemeModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull LockThemeModel oldItem, @NonNull LockThemeModel newItem) {
            return Objects.equals(oldItem.getImageURL(), newItem.getImageURL());
        }

        @Override
        public boolean areContentsTheSame(@NonNull LockThemeModel oldItem, @NonNull LockThemeModel newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_themes);
        activity = ThemesActivity.this;

        page = getIntent().getIntExtra("page", 0);

        List<LockThemeModel> list = readThemeJsonFromRaw(activity);

        ThemeAdapter adapter = new ThemeAdapter();
        binding.recyclerView.setAdapter(adapter);

        Collections.shuffle(list);

        binding.recyclerView.post(() -> adapter.submitList(page == 0 ? list : list.get(page-1).getThemes()));

        binding.btnBack.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }


    private void applyDialog(LockThemeModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ViewThemeCardBinding cardBinding = ViewThemeCardBinding.inflate(LayoutInflater.from(activity));
        builder.setView(cardBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Glide.with(activity).load(model.getImageURL()).into(cardBinding.dImage);
        cardBinding.dialog.setVisibility(View.VISIBLE);
        cardBinding.btnApply.setVisibility(View.VISIBLE);

        cardBinding.btnApply.setOnClickListener(v -> {
            MyApplication.getPreferences().setLockBackground(model.getImageURL());
            Toast.makeText(activity, "Theme applied successfully.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

    }

    private class ThemeAdapter extends ListAdapter<LockThemeModel, ThemeAdapter.Holder> {
        public ThemeAdapter() {
            super(diffCallback);
        }

        @NonNull
        @Override
        public ThemeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewThemeCardBinding cardBinding = ViewThemeCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new Holder(cardBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ThemeAdapter.Holder holder, int position) {
            LockThemeModel model = getItem(position);

            holder.binding.lockPatternView.setVisibility(page != 0 ? View.VISIBLE : View.GONE);
            holder.binding.tvName.setVisibility(page == 0 ? View.VISIBLE : View.GONE);
            holder.binding.tvName.setText(model.getTitle());

            Glide.with(activity).load(model.getImageURL()).into(holder.binding.image);

            holder.itemView.setOnClickListener(v -> {
                openActivity(holder.getBindingAdapterPosition(), model);
            });
            holder.binding.lockPatternView.setOnClickListener(v -> {
                openActivity(holder.getBindingAdapterPosition(), model);
            });


        }

        private void openActivity(int position, LockThemeModel model) {
            if (page == 0)
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        startActivity(new Intent(activity, ThemesActivity.class).putExtra("page", position + 1));
                    }
                });
            else applyDialog(model);
        }

        public class Holder extends RecyclerView.ViewHolder {
            ViewThemeCardBinding binding;

            public Holder(@NonNull ViewThemeCardBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
                binding.main.setVisibility(View.VISIBLE);
            }
        }
    }
}