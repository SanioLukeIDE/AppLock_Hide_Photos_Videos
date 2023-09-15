package com.applock.fingerprint.passwordlock.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.applock.fingerprint.passwordlock.databinding.ViewOnboardScreenBinding;
import com.applock.fingerprint.passwordlock.model.DataModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

public class OnBoardingAdapter extends ListAdapter<DataModel, OnBoardingAdapter.ViewHolder> {

    public OnBoardingAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewOnboardScreenBinding binding = ViewOnboardScreenBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel model = getItem(position);

        Glide.with(holder.itemView.getContext()).load(model.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.image);

        holder.binding.title.setText(model.getName());
        holder.binding.desc.setText(model.getValue());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ViewOnboardScreenBinding binding;

        public ViewHolder(@NonNull ViewOnboardScreenBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static DiffUtil.ItemCallback<DataModel> diffCallback = new DiffUtil.ItemCallback<DataModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull DataModel oldItem, @NonNull DataModel newItem) {
            return Objects.equals(oldItem.getName(), newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DataModel oldItem, @NonNull DataModel newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };
}
