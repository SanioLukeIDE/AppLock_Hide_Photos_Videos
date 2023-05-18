package com.applock.photos.videos.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ViewAppsListBinding;
import com.applock.photos.videos.interfaces.AppsClickedInterface;
import com.applock.photos.videos.model.AppsModel;
import com.applock.photos.videos.model.CommLockInfo;
import com.applock.photos.videos.ui.fragments.HideAppsFragment;
import com.applock.photos.videos.utils.CommLockInfoManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.Objects;

public class AppLockAdapter extends ListAdapter<CommLockInfo, AppLockAdapter.ViewHolder> {

    Context context;
    AppsClickedInterface clickedInterface;
    PackageManager packageManager;
    CommLockInfoManager mLockInfoManager;

    public AppLockAdapter(AppsClickedInterface appsClickedInterface) {
        super(diffCallback);
        clickedInterface = appsClickedInterface;
    }


    @NonNull
    @Override
    public AppLockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        packageManager = context.getPackageManager();
        mLockInfoManager = new CommLockInfoManager(context);
        ViewAppsListBinding binding = ViewAppsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppLockAdapter.ViewHolder holder, int position) {
        CommLockInfo model = getItem(position);


        holder.binding.hLayout.setVisibility(View.VISIBLE);

        Glide.with(context).load(packageManager.getApplicationIcon(model.getAppInfo()))
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.hLogo);

        holder.binding.hAppName.setText(model.getAppName());

        if (!model.isLocked()) {
            holder.binding.hLock.setImageResource(R.drawable.ic_lock_grey);
        } else
            holder.binding.hLock.setImageResource(R.drawable.ic_lock_gradient);


        holder.itemView.setOnClickListener(view -> {
            if (model.isLocked()){
                model.setLocked(false);
                mLockInfoManager.unlockCommApplication(model.getPackageName());
                holder.binding.hLock.setImageResource(R.drawable.ic_lock_grey);
            } else {
                model.setLocked(true);
                mLockInfoManager.unlockCommApplication(model.getPackageName());
                holder.binding.hLock.setImageResource(R.drawable.ic_lock_gradient);
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ViewAppsListBinding binding;

        public ViewHolder(@NonNull ViewAppsListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static DiffUtil.ItemCallback<CommLockInfo> diffCallback = new DiffUtil.ItemCallback<CommLockInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull CommLockInfo oldItem, @NonNull CommLockInfo newItem) {
            return Objects.equals(oldItem.getAppName(), newItem.getAppName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CommLockInfo oldItem, @NonNull CommLockInfo newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };
}
