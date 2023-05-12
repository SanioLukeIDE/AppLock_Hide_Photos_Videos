package com.applock.photos.videos.adapter;

import static com.applock.photos.videos.utils.Utility.getShimmer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.applock.photos.videos.databinding.ViewAlbumListBinding;
import com.applock.photos.videos.model.ImageFolder;
import com.applock.photos.videos.ui.activity.PreviewActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.Objects;

public class ContentAdapter extends ListAdapter<String, ContentAdapter.ViewHolder> {

    Context context;
    int type;

    public ContentAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewAlbumListBinding binding = ViewAlbumListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentAdapter.ViewHolder holder, int position) {
        String path = getItem(position);

  /*      Bitmap bitmap = null;
        if (type == 2){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            bitmap = retriever.getFrameAtTime(100000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        }*/

        Glide.with(context)
                .load(path)
                .placeholder(getShimmer())
                .thumbnail( 0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.image);

        holder.itemView.setOnClickListener(view -> {
            if (type == 1)
                context.startActivity(new Intent(context, PreviewActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("path", path));

            if (type == 2) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                intent.setDataAndType(Uri.parse(path), "video/*");
                context.startActivity(Intent.createChooser(intent, "Open with"));
            }

        });

    }

    public void setType(int type) {
        this.type = type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewAlbumListBinding binding;
        public ViewHolder(@NonNull ViewAlbumListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.layout.setVisibility(View.GONE);
        }
    }

    static DiffUtil.ItemCallback<String> diffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return Objects.equals(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

}
