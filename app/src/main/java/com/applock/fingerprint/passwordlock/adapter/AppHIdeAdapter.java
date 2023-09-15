package com.applock.fingerprint.passwordlock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.ViewAppsListBinding;
import com.applock.fingerprint.passwordlock.interfaces.AppsClickedInterface;
import com.applock.fingerprint.passwordlock.model.AppsModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.Objects;

public class AppHIdeAdapter extends ListAdapter<AppsModel, AppHIdeAdapter.ViewHolder> {

    Context context;
    int type;
    boolean isLock;
    AppsClickedInterface clickedInterface;


    public AppHIdeAdapter(boolean isLock, AppsClickedInterface appsClickedInterface) {
        super(diffCallback);
        this.isLock = isLock;
        clickedInterface = appsClickedInterface;
    }

    public void setType(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public AppHIdeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewAppsListBinding binding = ViewAppsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppHIdeAdapter.ViewHolder holder, int position) {
        AppsModel model = getItem(position);

        if (isLock){
            holder.binding.hLayout.setVisibility(View.VISIBLE);

            Glide.with(context).load(model.getIcon())
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.hLogo);

            holder.binding.hAppName.setText(model.getAppName());

            if (type == 1){
                holder.binding.hLock.setImageResource(R.drawable.ic_lock_grey);
            } else holder.binding.hLock.setImageResource(R.drawable.ic_lock_gradient);
        } else {
            holder.binding.vLayout.setVisibility(View.VISIBLE);

            Glide.with(context).load(model.getIcon())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.vLogo);

            holder.binding.vAppName.setText(model.getAppName());

        }

        holder.itemView.setOnClickListener(view -> clickedInterface.onItemClicked(model));

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

    static DiffUtil.ItemCallback<AppsModel> diffCallback = new DiffUtil.ItemCallback<AppsModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull AppsModel oldItem, @NonNull AppsModel newItem) {
            return Objects.equals(oldItem.getAppName(), newItem.getAppName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull AppsModel oldItem, @NonNull AppsModel newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };
}
