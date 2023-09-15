package com.applock.fingerprint.passwordlock.downloaderUtil.utils;

import static com.applock.fingerprint.passwordlock.downloaderUtil.utils.DownloadVideosMain.fromService;
import static com.applock.fingerprint.passwordlock.downloaderUtil.utils.DownloadVideosMain.pd;

import android.app.Activity;
import android.os.Looper;
import android.webkit.URLUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.downloaderUtil.interfaces.VideoDownloader;

import org.json.JSONObject;

import java.util.Date;

public class TwitterVideoDownloader implements VideoDownloader {

    private final Activity context;
    private final String VideoURL;
    private String VideoTitle;

    public TwitterVideoDownloader(Activity context, String videoURL) {
        this.context = context;
        VideoURL = videoURL;
    }

    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }


    @Override
    public String getVideoId(String link) {
        if (link.contains("?")) {
            link = link.split("\\?")[0];
        }
        link = link.substring(link.indexOf("status"));
        link = link.substring(link.indexOf("/") + 1);
        return link;
    }

    @Override
    public void DownloadVideo() {
        AndroidNetworking.post("https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php")
                .addBodyParameter("id", getVideoId(VideoURL))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.e("Hello", response.toString());
                            String URL = response.toString();
                            if (URL.contains("url")) {
                                URL = URL.substring(URL.indexOf("url"));
                                URL = URL.substring(ordinalIndexOf(URL, "\"", 1) + 1, ordinalIndexOf(URL, "\"", 2));
                                if (URL.contains("\\")) {
                                    URL = URL.replace("\\", "");
                                }
                                //Log.e("HelloURL",URL);
                                if (URLUtil.isValidUrl(URL)) {

                                    if (pd != null && pd.isShowing() && !fromService && !((Activity) context).isFinishing()) {
                                        pd.dismiss();
                                    }

                                    if (VideoTitle == null || VideoTitle.equals("")) {
                                        if (URL.contains(".mp4")) {
                                            VideoTitle = "TwitterVideo" + new Date() + ".mp4";
                                        } else {
                                            VideoTitle = "TwitterImage" + new Date() + ".jpg";
                                        }


                                    } else {
                                        if (URL.contains(".mp4")) {
                                            VideoTitle = VideoTitle + ".mp4";
                                        } else {
                                            VideoTitle = VideoTitle + ".jpg";

                                        }
                                    }
                                    if (Looper.myLooper() == null)
                                        Looper.prepare();

                                    if (URL.contains(".mp4")) {

                                        DownloadFileMain.startDownloading(context, URL, VideoTitle, ".mp4");
                                    } else {
                                        DownloadFileMain.startDownloading(context, URL, VideoTitle, ".jpg");

                                    }
                                } else {

                                    if (pd != null && pd.isShowing() && !fromService && !((Activity) context).isFinishing()) {
                                        pd.dismiss();
                                        context.runOnUiThread(() -> {
                                            iUtils.ShowToastError(context,
                                                    context.getResources().getString(R.string.somthing)
                                            );
                                        });
                                    }
                                    if (Looper.myLooper() == null)
                                        Looper.prepare();
                                    context.runOnUiThread(() -> {
                                        iUtils.ShowToastError(context,
                                                "No Video Found"
                                        );
                                    });
                                    Looper.loop();
                                }
                            } else {

                                if (pd != null && pd.isShowing() && !fromService && !((Activity) context).isFinishing()) {
                                    pd.dismiss();
                                    context.runOnUiThread(() -> {
                                        iUtils.ShowToastError(context,
                                                context.getResources().getString(R.string.somthing)
                                        );
                                    });

                                }
                                if (Looper.myLooper() == null)
                                    Looper.prepare();
                                context.runOnUiThread(() -> {
                                    iUtils.ShowToastError(context,
                                            "No Video Found"
                                    );
                                });
                                Looper.loop();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (pd != null && pd.isShowing() && !fromService && !((Activity) context).isFinishing()) {
                                pd.dismiss();
                                context.runOnUiThread(() -> {
                                    iUtils.ShowToastError(context,
                                            context.getResources().getString(R.string.somthing)
                                    );
                                });

                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (Looper.myLooper() == null)
                            Looper.prepare();
                        Looper.loop();

                        if (pd != null && pd.isShowing() && !fromService && !((Activity) context).isFinishing()) {
                            pd.dismiss();
                            context.runOnUiThread(() -> {
                                iUtils.ShowToastError(context,
                                        context.getResources().getString(R.string.somthing)
                                );
                            });
                        }

                    }
                });
    }
}
