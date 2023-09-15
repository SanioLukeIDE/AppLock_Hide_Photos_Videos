package com.applock.fingerprint.passwordlock.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;

import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.Constants;
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.FilePathUtility;
import com.applock.fingerprint.passwordlock.downloaderUtil.utils.iUtils;
import com.applock.fingerprint.passwordlock.model.WAStoryModel;
import com.applock.fingerprint.passwordlock.ui.activity.FullImageActivity;
import com.applock.fingerprint.passwordlock.ui.activity.PlayerActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class WhatsappStoryAdapter extends RecyclerView.Adapter<WhatsappStoryAdapter.ViewHolder> {

    private final Activity context;
    private final ArrayList<Object> filesList;

    public WhatsappStoryAdapter(Activity context, ArrayList<Object> filesList) {
        this.context = context;
        this.filesList = filesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row_statussaver, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //    int viewType = getItemViewType(position);
        try {
            final WAStoryModel files = (WAStoryModel) filesList.get(position);
            //final Uri uri = Uri.parse(files.getUri().toString());
            holder.userName.setText(files.getName());
            if (files.getUri().toString().endsWith(".mp4")) {
                holder.playIcon.setVisibility(View.VISIBLE);
            } else {
                holder.playIcon.setVisibility(View.INVISIBLE);
            }

            Log.e("Errorisnewis", files.getUri().toString());
            Glide.with(context)
                    .load(files.getUri())
                    .into(holder.savedImage);

            holder.savedImage.setOnClickListener(v -> {
                if (files.getUri().toString().endsWith(".mp4")) {
                    if (!files.getUri().toString().contains(".gif")) {

                        context.startActivity(new Intent(context, PlayerActivity.class)
                                .putExtra("data", files.getPath()));

                    } else {
                        iUtils.ShowToast(context, context.getString(R.string.preview_not));

                    }

                } else {
                    try {

                        Intent intent = new Intent(context, FullImageActivity.class);
                        intent.putExtra("myimgfile", files.getPath());
                        context.startActivity(intent);

                    } catch (Exception e) {
                        iUtils.ShowToast(context, context.getResources().getString(R.string.somth_video_wrong));
                        Log.e("Errorisnewis", e.getMessage());
                    }
                }

            });


            holder.linlayoutclick.setOnClickListener(v -> {
                checkFolder();

                final String path = files.getPath();

                if (Build.VERSION.SDK_INT >= 30) {
                    DocumentFile fromTreeUri1 = DocumentFile.fromSingleUri(context, Uri.parse(path));
                    String despath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME;

                    try {

                        FilePathUtility.moveFile(context, Objects.requireNonNull(fromTreeUri1).getUri().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    context.runOnUiThread(() -> {
                        String[] strings = files.getFilename().split(".Statuses/");
                        iUtils.ShowToast(context, context.getString(R.string.saved_to) + despath + strings[1]);
                    });
                } else {
                    final File file = new File(path);

                    String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME;

                    File destFile = new File(destPath);
                    try {
                        FileUtils.copyFileToDirectory(file, destFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    context.runOnUiThread(() -> {
                        iUtils.ShowToast(context, context.getString(R.string.saved_to) + destPath + files.getFilename()
                        );
                    });

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        } else  {
            Log.e("Folder", "Already Created");
        }
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView savedImage;
        ImageView playIcon;
        LinearLayout linlayoutclick;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.profileUserName);
            savedImage = itemView.findViewById(R.id.mainImageView);
            playIcon = itemView.findViewById(R.id.playButtonImage);
            linlayoutclick = itemView.findViewById(R.id.linlayoutclick);
        }
    }
}
