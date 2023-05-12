package com.applock.photos.videos.adapter;

import static com.applock.photos.videos.adapter.OnBoardingAdapter.diffCallback;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.applock.photos.videos.databinding.ViewStatusSaversListBinding;
import com.applock.photos.videos.model.DataModel;
import com.applock.photos.videos.ui.activity.StatusSaverElementActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class StatusListAdapter extends ListAdapter<DataModel, StatusListAdapter.ViewHolder> {

    Context context;

    public StatusListAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public StatusListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewStatusSaversListBinding binding = ViewStatusSaversListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusListAdapter.ViewHolder holder, int position) {
        DataModel model = getItem(position);

        Glide.with(context).load(model.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.image);

        holder.binding.cardView.setCardBackgroundColor(context.getColor(model.getColor()));
        holder.binding.imgBackground.setBackgroundTintList(ColorStateList.valueOf(context.getColor(model.getColor())));

        holder.binding.tvName.setText(model.getName());

        holder.itemView.setOnClickListener(view -> {
            context.startActivity(new Intent(context, StatusSaverElementActivity.class)
                    .putExtra("model", model));
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewStatusSaversListBinding binding;

        public ViewHolder(@NonNull ViewStatusSaversListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
