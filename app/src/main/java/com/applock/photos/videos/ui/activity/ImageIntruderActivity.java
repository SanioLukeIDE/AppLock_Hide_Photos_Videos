package com.applock.photos.videos.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityImageIntruderBinding;
import com.applock.photos.videos.databinding.ViewIntruderImageBinding;
import com.applock.photos.videos.utils.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.Objects;

public class ImageIntruderActivity extends AppCompatActivity {

    ActivityImageIntruderBinding binding;
    ImageIntruderActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_intruder);
        activity = this;

        ImageIntruderAdapter adapter = new ImageIntruderAdapter();
        binding.recyclerView.setAdapter(adapter);

        String path = String.valueOf(getExternalFilesDir("Intruders"));
        Utility.getAllImagesFromFolderPath(path).observe(activity, data->{
            adapter.submitList(data);
        });

        binding.btnMore.setOnClickListener(this::showPopMenu);

    }

    private void showPopMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_settings, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(activity, MainSettingsActivity.class).putExtra("page", 1));
            }
            return true;
        });

        popupMenu.show();
    }

    private class ImageIntruderAdapter extends ListAdapter<String, ImageIntruderAdapter.Holder> {
        public ImageIntruderAdapter() {
            super(diffCallback);
        }

        @NonNull
        @Override
        public ImageIntruderAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewIntruderImageBinding imageBinding = ViewIntruderImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new Holder(imageBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageIntruderAdapter.Holder holder, int position) {
            File path = new File(getItem(position));
            String date = String.valueOf(path.getName().subSequence(10, 21));

            holder.binding.tvDate.setText(date);
            Glide.with(activity).load(path).apply(RequestOptions.circleCropTransform()).into(holder.binding.image);

        }
        public class Holder extends RecyclerView.ViewHolder {
            ViewIntruderImageBinding binding;
            public Holder(@NonNull ViewIntruderImageBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }

    DiffUtil.ItemCallback<String> diffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return Objects.equals(newItem, oldItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return Objects.equals(newItem, oldItem);
        }
    };
}