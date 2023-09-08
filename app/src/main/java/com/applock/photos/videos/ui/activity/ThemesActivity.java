package com.applock.photos.videos.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityThemesBinding;
import com.applock.photos.videos.databinding.ViewThemeCardBinding;
import com.applock.photos.videos.model.LockThemeModel;
import com.applock.photos.videos.singletonClass.MyApplication;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThemesActivity extends AppCompatActivity {

    ActivityThemesBinding binding;
    int page;
    ThemesActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_themes);
        activity = ThemesActivity.this;

        page = getIntent().getIntExtra("page", 0);

        ThemeAdapter adapter = new ThemeAdapter();
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.post(()->adapter.submitList(getList()));

    }

    private List<LockThemeModel> getList() {
        List<LockThemeModel> list = new ArrayList<>();

        String abstractLink = "",
                animals = "",
                nature = "",
                minimal = "",
                plants = "",
                art = "",
                portrait = "https://raw.githubusercontent.com/brijesh-ide/HDVideoPlayer/master/img_potrait.png",
                space = "",
                architect = "",
                food = "",
                business = "",
                interior = "";


        if (page == 0){
            list.add(new LockThemeModel(1, "Animals", animals, false));
            list.add(new LockThemeModel(2, "Abstract", abstractLink, false));
            list.add(new LockThemeModel(3, "Nature", nature, false));
            list.add(new LockThemeModel(4, "Minimal", minimal, false));
            list.add(new LockThemeModel(5, "Plants", plants, false));
            list.add(new LockThemeModel(6, "Art", art, false));
            list.add(new LockThemeModel(7, "Portrait", portrait, false));
            list.add(new LockThemeModel(8, "Space", space, false));
            list.add(new LockThemeModel(9, "Architect", architect, false));
            list.add(new LockThemeModel(10, "Food", food, false));
            list.add(new LockThemeModel(11, "Business", business, false));
            list.add(new LockThemeModel(12, "Interior", interior, false));
        }
        /*if (page == 0){
            list.add(new LockThemeModel(1, "Animals", R.drawable.img_animals, false));
            list.add(new LockThemeModel(2, "Abstract", R.drawable.img_abstract, false));
            list.add(new LockThemeModel(3, "Nature", R.drawable.img_nature, false));
            list.add(new LockThemeModel(4, "Minimal", R.drawable.img_minimal, false));
            list.add(new LockThemeModel(5, "Plants", R.drawable.img_plants, false));
            list.add(new LockThemeModel(6, "Art", R.drawable.img_art, false));
            list.add(new LockThemeModel(7, "Portrait", R.drawable.img_potrait, false));
            list.add(new LockThemeModel(8, "Space", R.drawable.img_space, false));
            list.add(new LockThemeModel(9, "Architect", R.drawable.img_architect, false));
            list.add(new LockThemeModel(10, "Food", R.drawable.img_food, false));
            list.add(new LockThemeModel(11, "Business", R.drawable.img_business, false));
            list.add(new LockThemeModel(12, "Interior", R.drawable.img_interior, false));
        }*/ else {
//        if (page == 2){
            list.add(new LockThemeModel(2, "", R.drawable.theme1, false));
            list.add(new LockThemeModel(2, "", R.drawable.theme2, false));
            list.add(new LockThemeModel(2, "", R.drawable.theme3, false));
            list.add(new LockThemeModel(2, "", R.drawable.theme6, false));
        }

        return list;
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

            Glide.with(activity).load(page == 0 ? model.getImageURL() : getDrawable(model.getBgImage())).into(holder.binding.image);

            holder.itemView.setOnClickListener(v -> {
                if (page == 0)
                    startActivity(new Intent(activity, ThemesActivity.class).putExtra("page", model.getId()));
                else applyDialog(model);
            });
            holder.binding.lockPatternView.setOnClickListener(v -> {
                if (page == 0)
                    startActivity(new Intent(activity, ThemesActivity.class).putExtra("page", model.getId()));
                else applyDialog(model);
            });


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

    private void applyDialog(LockThemeModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ViewThemeCardBinding cardBinding = ViewThemeCardBinding.inflate(LayoutInflater.from(activity));
        builder.setView(cardBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Glide.with(activity).load(getDrawable(model.getBgImage())).into(cardBinding.dImage);
        cardBinding.dialog.setVisibility(View.VISIBLE);
        cardBinding.btnApply.setVisibility(View.VISIBLE);

        cardBinding.btnApply.setOnClickListener(v -> {
            MyApplication.getPreferences().setLockBackground(model.getBgImage());
            Toast.makeText(activity, "Theme applied successfully.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

    }

    DiffUtil.ItemCallback<LockThemeModel> diffCallback = new DiffUtil.ItemCallback<LockThemeModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull LockThemeModel oldItem, @NonNull LockThemeModel newItem) {
            return Objects.equals(oldItem.getBgImage(), newItem.getBgImage());
        }

        @Override
        public boolean areContentsTheSame(@NonNull LockThemeModel oldItem, @NonNull LockThemeModel newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };
}