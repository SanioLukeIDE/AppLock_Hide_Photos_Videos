package com.applock.fingerprint.passwordlock.adapter;

import static com.applock.fingerprint.passwordlock.utils.Utility.getShimmer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.applock.fingerprint.passwordlock.databinding.ViewAlbumListBinding;
import com.applock.fingerprint.passwordlock.interfaces.AlbumClickedInterface;
import com.applock.fingerprint.passwordlock.model.ImageFolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

public class AlbumAdapter extends ListAdapter<ImageFolder, AlbumAdapter.ViewHolder> {

    Context context;
    AlbumClickedInterface clickedInterface;
    int type ;

    public AlbumAdapter(AlbumClickedInterface albumClickedInterface) {
        super(diffCallback);
        clickedInterface = albumClickedInterface;
    }

    public void setType(int type){
        this.type = type;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewAlbumListBinding binding = ViewAlbumListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        ImageFolder data = getItem(position);

        Glide.with(context)
                .load(data.getImagePath())
                .placeholder(getShimmer())
                .thumbnail( 0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.image);

        holder.binding.tvName.setText(data.getBucketName());
        holder.binding.tvCount.setText(data.getImageCount()+"");

        holder.itemView.setOnClickListener(view -> clickedInterface.onItemClicked(data, type));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewAlbumListBinding binding;
        public ViewHolder(@NonNull ViewAlbumListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    static DiffUtil.ItemCallback<ImageFolder> diffCallback = new DiffUtil.ItemCallback<ImageFolder>() {
        @Override
        public boolean areItemsTheSame(@NonNull ImageFolder oldItem, @NonNull ImageFolder newItem) {
            return Objects.equals(oldItem.getBucketId(), newItem.getBucketId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ImageFolder oldItem, @NonNull ImageFolder newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

}
