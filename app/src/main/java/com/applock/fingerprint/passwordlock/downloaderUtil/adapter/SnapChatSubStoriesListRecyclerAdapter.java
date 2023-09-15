package com.applock.fingerprint.passwordlock.downloaderUtil.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.applock.fingerprint.passwordlock.ui.activity.FullImageActivity;
import com.applock.fingerprint.passwordlock.ui.activity.PlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.applock.fingerprint.passwordlock.BuildConfig;
import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.iUtils;
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.DownloadFileMain;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;


public class SnapChatSubStoriesListRecyclerAdapter extends RecyclerView.Adapter<SnapChatSubStoriesListRecyclerAdapter.MyViewHolder> {
    public List<String> list;
    Activity context;

    public SnapChatSubStoriesListRecyclerAdapter(Activity context, List<String> list2) {
        this.list = list2;
        this.context = context;
    }

    @NotNull
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_snapchat_story, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final String FBStory = this.list.get(i);
        System.out.println("hsdfhjdsfsdfsdfAdapter " + FBStory);

        ((RequestBuilder) Glide.with(context).load(FBStory).transform((Transformation<Bitmap>[]) new Transformation[]{new CenterCrop(), new RoundedCorners(15)})).into(myViewHolder.thumb);
        if (FBStory.contains(".80") || FBStory.contains(".1034") || FBStory.contains(".1244") || FBStory.contains(".111") || FBStory.contains(".27")) {
            myViewHolder.play.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.play.setVisibility(View.GONE);
        }
        myViewHolder.parent.setOnClickListener(view -> SnapChatSubStoriesListRecyclerAdapter.this.openFile(FBStory));

        myViewHolder.option.setVisibility(View.GONE);

        myViewHolder.download.setVisibility(View.VISIBLE);

        myViewHolder.download.setOnClickListener(view -> SnapChatSubStoriesListRecyclerAdapter.this.startDownloadProcess(FBStory));
    }

    public void share(String str) {
        Uri uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, new File(str));
        if (str.toLowerCase().contains(".jpg")) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Intent createChooser = Intent.createChooser(intent, "Share Image using");
                createChooser.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(createChooser);
            } catch (ActivityNotFoundException unused) {
                context.runOnUiThread(() -> {
                    iUtils.ShowToastError(context, "No application found to open this file.");
                });

            }
        } else if (str.toLowerCase().contains(".mp4")) {
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("video/*");
            intent2.putExtra("android.intent.extra.STREAM", uriForFile);
            intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Intent createChooser2 = Intent.createChooser(intent2, "Share Video using");
                createChooser2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(createChooser2);
            } catch (ActivityNotFoundException unused2) {
                context.runOnUiThread(() -> {
                    iUtils.ShowToastError(context, "No application found to open this file."
                    );
                });
            }
        }
    }

    public void startDownloadProcess(final String FBStory) {
        if (FBStory.contains(".80") || FBStory.contains(".1034") || FBStory.contains(".1244") || FBStory.contains(".111") || FBStory.contains(".27")) {
            String titl = "snapchat__video_" + System.currentTimeMillis();

            DownloadFileMain.startDownloading(
                    context,
                    FBStory,
                    titl,
                    ".mp4");
            return;
        }
        String titl = "snapchat__image_" + System.currentTimeMillis();

        System.out.println("myalldatais = " + FBStory);
        DownloadFileMain.startDownloading(
                context,
                FBStory,
                titl,
                ".png");
    }

    public void openFile(String FBStory) {
        Intent intent = new Intent(context, PlayerActivity.class);
        if (FBStory.contains(".80") || FBStory.contains(".1034") | FBStory.contains(".1244") || FBStory.contains(".27") || FBStory.contains(".111")) {

            System.out.println("hsdfhjdsfsdfsdf " + FBStory);

            intent.putExtra("data", FBStory);
            intent.putExtra("name", FBStory);
        } else {
            intent = new Intent(context, FullImageActivity.class);

            intent.putExtra("myimgfile", FBStory);
            intent.putExtra("isfbimage", "true");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public int getItemCount() {
        List<String> list2 = this.list;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View parent;
        public TextView count;
        ImageView download;
        ImageView option;

        ImageView play;
        ImageView thumb;

        public MyViewHolder(View view) {
            super(view);
            this.parent = view;

            count = view.findViewById(R.id.count);
            download = view.findViewById(R.id.download);
            option = view.findViewById(R.id.option);

            play = view.findViewById(R.id.play);
            thumb = view.findViewById(R.id.image);
        }
    }
}
