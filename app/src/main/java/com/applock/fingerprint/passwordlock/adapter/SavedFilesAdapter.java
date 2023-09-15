package com.applock.fingerprint.passwordlock.adapter;

import static com.applock.fingerprint.passwordlock.utils.Utility.shareVideoFile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.applock.fingerprint.passwordlock.databinding.ViewStatusSaverBinding;
import com.applock.fingerprint.passwordlock.ui.activity.FullImageActivity;
import com.applock.fingerprint.passwordlock.ui.activity.PlayerActivity;
import com.applock.fingerprint.passwordlock.utils.Utility;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Objects;

public class SavedFilesAdapter extends ListAdapter<String, SavedFilesAdapter.Holder> {

    Context context;

    static DiffUtil.ItemCallback<String> diffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return Objects.equals(newItem, oldItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return Objects.equals(newItem, oldItem);
        }
    };

    OnFileDeleted fileDeleted;

    public SavedFilesAdapter(OnFileDeleted onFileDeleted) {
        super(diffCallback);
        fileDeleted = onFileDeleted;
    }

    @NonNull
    @Override
    public SavedFilesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewStatusSaverBinding binding = ViewStatusSaverBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedFilesAdapter.Holder holder, int position) {
        File file = new File(getItem(position));

        Glide.with(context).load(file).into(holder.binding.image);

        if (Utility.isVideoFile(file.getName())){
            holder.binding.imgPlay.setVisibility(View.VISIBLE);
        } else holder.binding.imgPlay.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (Utility.isVideoFile(file.getName())){
                context.startActivity(new Intent(context, PlayerActivity.class)
                        .putExtra("data", file.getPath()));
            } else {
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("myimgfile", file.getPath());
                context.startActivity(intent);
            }
        });

        holder.binding.btnShare.setOnClickListener(v -> {
            shareVideoFile(context, file.getPath());
        });
        holder.binding.btnDelete.setOnClickListener(v -> {
           if (file.exists())
               if (file.delete()){
                   Toast.makeText(context, "File deleted successfully.", Toast.LENGTH_SHORT).show();
                   fileDeleted.fileDeleted();
               }
        });

    }

    public interface OnFileDeleted{
        void fileDeleted();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        ViewStatusSaverBinding binding;
        public Holder(@NonNull ViewStatusSaverBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
