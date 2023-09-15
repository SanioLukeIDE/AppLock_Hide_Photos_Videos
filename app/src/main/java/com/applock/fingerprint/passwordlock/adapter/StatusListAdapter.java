package com.applock.fingerprint.passwordlock.adapter;

import static com.applock.fingerprint.passwordlock.adapter.OnBoardingAdapter.diffCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.interfaces.AppInterfaces;
import com.adsmodule.api.adsModule.utils.Constants;
import com.applock.fingerprint.passwordlock.databinding.ViewStatusSaversListBinding;
import com.applock.fingerprint.passwordlock.model.DataModel;
import com.applock.fingerprint.passwordlock.ui.activity.DownloaderActivity;
import com.applock.fingerprint.passwordlock.ui.activity.StatusSaverActivity;
import com.applock.fingerprint.passwordlock.utils.Utility;
import com.bumptech.glide.Glide;

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

        Glide.with(context).load(context.getDrawable(model.getIcon())).into(holder.binding.image);

        holder.binding.cardView.setCardBackgroundColor(context.getColor(model.getColor()));
        holder.binding.imgBackground.setBackgroundTintList(ColorStateList.valueOf(context.getColor(model.getColor())));

        holder.binding.tvName.setText(model.getName());

        if (holder.getAbsoluteAdapterPosition() == 2 || holder.getAbsoluteAdapterPosition() == 5){
            holder.binding.adsView0.setVisibility(View.VISIBLE);
        } else holder.binding.adsView0.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(view -> {
            if (holder.getAbsoluteAdapterPosition() == 0)
                Utility.nextActivity((Activity) context, StatusSaverActivity.class, false);
            else
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), (Activity) context, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        context.startActivity(new Intent(context, DownloaderActivity.class)
                                .putExtra("model", model));
                    }
                });
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
