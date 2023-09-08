package com.applock.photos.videos.downloaderUtil.utils;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.devname.youtubedl.android.YoutubeDL;
import com.devname.youtubedl.android.mapper.VideoFormat;
import com.devname.youtubedl.android.mapper.VideoInfo;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.applock.photos.videos.downloaderUtil.adapter.QualityBottomsheetAdapter;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.applock.photos.videos.R;
import com.applock.photos.videos.downloaderUtil.activity.GetLinkThroughWebView;
import com.applock.photos.videos.downloaderUtil.activity.SnapChatBulkStoryDownloader;
import com.applock.photos.videos.downloaderUtil.activity.TikTokDownloadCloudBypassWebview_method_2;
import com.applock.photos.videos.downloaderUtil.activity.TikTokDownloadCloudBypassWebview_method_3;
import com.applock.photos.videos.downloaderUtil.activity.TikTokDownloadCloudBypassWebview_method_4;
import com.applock.photos.videos.downloaderUtil.interfaces.RetrofitApiInterface;
import com.applock.photos.videos.downloaderUtil.models.DLDataParser;
import com.applock.photos.videos.downloaderUtil.models.Format;
import com.applock.photos.videos.downloaderUtil.models.Video;
import com.applock.photos.videos.downloaderUtil.service.RetrofitClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressLint("all")
public class DownloadVideosMain {

    private static final int DOWNLOAD_NOTIFICATION_ID = 231;

    public static Activity Mcontext;
    public static ProgressDialog pd;
    public static Dialog dialog;
    public static SharedPreferences prefs;
    public static Boolean fromService;
    public static String VideoUrl;
    static String Title;
    static LinearLayout mainLayout;
    static WindowManager windowManager2;
    static WindowManager.LayoutParams params;
    static View mChatHeadView;
    static ImageView img_dialog;
    static String myURLIS = "";
    static Dialog dialog_quality_allvids;
    static VideoInfo streamInfo;
    private static String newurl;
    static boolean isStartedFbDownload = false;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public static void Start(final Activity context, String url, Boolean service) {
        try {

            Mcontext = context;
            fromService = service;
            Log.i("LOGClipboard111111 clip", "work 2");

            try {
                url = iUtils.extractUrls(url).get(0);
            } catch (Exception ignored) {

            }

            myURLIS = url;
            if (!fromService) {
                pd = new ProgressDialog(context);
                pd.setMessage(Mcontext.getResources().getString(R.string.genarating_download_link));
                pd.setCancelable(false);
                pd.show();
            }

            if (url.contains("tiktok")) {

                if (!iUtils.isSocialMediaOn("tiktok")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                if (!((Activity) Mcontext).isFinishing()) {

                    Dialog dialog = new Dialog(Mcontext);

                    dialog.setContentView(R.layout.tiktok_optionselect_dialog);

                    String finalUrl1 = url;

                    Button methode0 = dialog.findViewById(R.id.dig_btn_met0);
                    Button methode1 = dialog.findViewById(R.id.dig_btn_met1);
                    Button methode2 = dialog.findViewById(R.id.dig_btn_met2);
                    Button methode3 = dialog.findViewById(R.id.dig_btn_met3);
                    Button methode4 = dialog.findViewById(R.id.dig_btn_met4);
                    Button methode5 = dialog.findViewById(R.id.dig_btn_met5);
                    Button methode6 = dialog.findViewById(R.id.dig_btn_met6);

                    Button dig_btn_cancel = dialog.findViewById(R.id.dig_btn_cancel);

                    String finalUrl2 = url;
                    methode0.setOnClickListener(v -> {
                        dialog.dismiss();
                        Executors.newSingleThreadExecutor().submit(() -> {
                            try {
//                                    Looper.prepare();

                                try {

                                    HttpURLConnection con = (HttpURLConnection) (new URL(finalUrl2).openConnection());

                                    con.setInstanceFollowRedirects(false);
                                    con.connect();
                                    int responseCode = con.getResponseCode();
                                    System.out.println(responseCode);
                                    String location = con.getHeaderField("Location");
                                    if (location != null) {
                                        System.out.println(location);
                                        System.out.println(location.split("/")[5]);
                                        System.out.println(location.split("/")[5].split("\\?")[0]);
                                    } else {
                                        location = finalUrl2;
                                        System.out.println(location.split("/")[5]);
                                        System.out.println(location.split("/")[5].split("\\?")[0]);
                                    }
                                    System.out.println("wojfdjhfdjh " + location);

                                    AndroidNetworking.get("https://api.tiktokv.com/aweme/v1/feed/?aweme_id=" + location.split("/")[5].split("\\?")[0])
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    System.out.println("wojfdjhfdjh " + response);

                                                    String matag, username = "";
                                                    try {

                                                        JSONObject jsonObject = new JSONObject(response.toString());

                                                        matag = jsonObject.getJSONArray("aweme_list").getJSONObject(0).getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").getString(2);

                                                        username = jsonObject.getJSONArray("aweme_list").getJSONObject(0).getJSONObject("author").getString("unique_id");
                                                        if (matag.contains("http")) {
                                                            System.out.println("wojfdjhfdjh " + matag);

                                                            DownloadFileMain.startDownloading(context, matag, "Tiktok_" + username + "_" + System.currentTimeMillis(), ".mp4");

                                                            dismissMyDialog();

                                                        } else {
                                                            dismissMyDialogErrortoast();
                                                        }

                                                    } catch (Exception e) {

                                                        dismissMyDialogErrortoast();
                                                    }

                                                }

                                                @Override /**/
                                                public void onError(ANError error) {
                                                    System.out.println("youtube4kdownloader wojfdjhfdjhtik yyyy " + error.getMessage());

                                                    AndroidNetworking.get("https://s6.youtube4kdownloader.com/ajax/getLinks.php?video=" + finalUrl2 + "&rand=TPMOUMSQRPUURSQ")
                                                            .setPriority(Priority.MEDIUM)
                                                            .addHeaders("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")
                                                            .addHeaders("Connection", "keep-alive")
                                                            .addHeaders("Content-type", "application/x-www-form-urlencoded")
                                                            .addHeaders("Origin", "https://youtube4kdownloader.com")
                                                            .addHeaders("Referer", "https://youtube4kdownloader.com/")
                                                            .addHeaders("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                                                            .build()
                                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {

                                                                    System.out.println("youtube4kdownloader wojfdjhfdjhtik yyyy " + response);

                                                                    String matag, myttt;
                                                                    try {
                                                                        dismissMyDialog();
                                                                        JSONObject jsonObject = new JSONObject(response.toString());

                                                                        JSONArray jsonaaa = jsonObject.getJSONObject("data").getJSONArray("av");
                                                                        matag = jsonaaa.getJSONObject(jsonaaa.length() - 1).getString("url");                                                                            //                                                                            try {
                                                                        //                                                                                matag = matag.substring(matag.indexOf("video_id=") + 9, matag.indexOf("&line="));
                                                                        //                                                                                System.out.println("wojfdjhfdjhtik youtube4kdownloader " + matag);
                                                                        //                                                                                myttt = "https://api2-16-h2.musical.ly/aweme/v1/play/?video_id=" + matag + "&vr_type=0&is_play_url=1&source=PackSourceEnum_PUBLISH&media_type=4&ratio=default&improve_bitrate=1";
                                                                        //                                                                            } catch (Exception e) {
                                                                        //                                                                                myttt = matag;
                                                                        //
                                                                        //                                                                            }
                                                                        myttt = matag;

                                                                        DownloadFileMain.startDownloading(context, myttt, "Tiktok_" + System.currentTimeMillis(), ".mp4");

                                                                    } catch (Exception e) {

                                                                        try {

                                                                            ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));

                                                                            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                                                            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                                                                            OkHttpClient client = new OkHttpClient.Builder()
                                                                                    .cookieJar(cookieJar)
                                                                                    .addInterceptor(logging)
                                                                                    .connectTimeout(10, TimeUnit.SECONDS)
                                                                                    .writeTimeout(10, TimeUnit.SECONDS)
                                                                                    .readTimeout(30, TimeUnit.SECONDS)
                                                                                    .build();

                                                                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                                                    .addFormDataPart("id", finalUrl2 + "")
                                                                                    .addFormDataPart("locale", "en")
                                                                                    .addFormDataPart("gc", "0")
                                                                                    .addFormDataPart("tt", "0")
                                                                                    .addFormDataPart("ts", "0")
                                                                                    .build();
                                                                            Request request = new Request.Builder()
                                                                                    .url("https://tiktokdownload.online/abc?url=dl")
                                                                                    .method("POST", body)
                                                                                    .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                                                                                    .addHeader("hx-current-url", "https://tiktokdownload.online/")
                                                                                    .addHeader("hx-request", "true")
                                                                                    .addHeader("hx-target", "target")
                                                                                    .addHeader("hx-trigger", "_gcaptcha_pt")
                                                                                    .addHeader("origin", "https://tiktokdownload.online")
                                                                                    .addHeader("referer", "https://tiktokdownload.online/")
                                                                                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
                                                                                    .build();

                                                                            Response response3 = client.newCall(request).execute();
                                                                            String res = Objects.requireNonNull(response3.body()).string();
                                                                            System.out.println("myresponseis111 temp res: " + res);

                                                                            Document document = Jsoup.parse(res);

                                                                            ArrayList<String> arrayListLinks = new ArrayList<>();

                                                                            Elements elements = document.getElementsByTag("a");

                                                                            //   Elements elements = document.select("a[]");
                                                                            for (Element element : elements) {

                                                                                if (!element.attr("href").contains("https://play.google.com") && !element.attr("href").contains("https://ssscdn.io/tiktokdownload/a/") && element.attr("href").contains("tiktokcdn") && element.attr("href").length() > 150) {
                                                                                    arrayListLinks.add(element.attr("href"));
                                                                                    System.out.println("myresponseis111 testing tiktok: " + element.attr("href"));
                                                                                }
                                                                            }

                                                                            dismissMyDialog();

                                                                            Thread.sleep(500);

                                                                            DownloadFileMain.startDownloading(context, arrayListLinks.get(0), "Tiktok_" + System.currentTimeMillis(), ".mp4");
                                                                        } catch (Exception e3) {
                                                                            System.out.println("myresponseis111 exp2 " + e3.getMessage());
                                                                            dismissMyDialogErrortoast();

                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onError(ANError error) {
                                                                    System.out.println("myresponseis111 exp2 " + error.getMessage());
                                                                    dismissMyDialogErrortoast();

                                                                }
                                                            });
                                                }
                                            });

                                } catch (Exception e1) {

                                    try {

                                        ClearableCookieJar cookieJar =
                                                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));

                                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                                        OkHttpClient client = new OkHttpClient.Builder()
                                                .cookieJar(cookieJar)
                                                .addInterceptor(logging)
                                                .connectTimeout(10, TimeUnit.SECONDS)
                                                .writeTimeout(10, TimeUnit.SECONDS)
                                                .readTimeout(30, TimeUnit.SECONDS)
                                                .build();

                                        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                .addFormDataPart("id", finalUrl2 + "")
                                                .addFormDataPart("locale", "en")
                                                .addFormDataPart("gc", "0")
                                                .addFormDataPart("tt", "0")
                                                .addFormDataPart("ts", "0")
                                                .build();
                                        Request request = new Request.Builder()
                                                .url("https://tiktokdownload.online/abc?url=dl")
                                                .method("POST", body)
                                                .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                                                .addHeader("hx-current-url", "https://tiktokdownload.online/")
                                                .addHeader("hx-request", "true")
                                                .addHeader("hx-target", "target")
                                                .addHeader("hx-trigger", "_gcaptcha_pt")
                                                .addHeader("origin", "https://tiktokdownload.online")
                                                .addHeader("referer", "https://tiktokdownload.online/")
                                                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
                                                .build();

                                        Response response3 = client.newCall(request).execute();
                                        String res = Objects.requireNonNull(response3.body()).string();
                                        System.out.println("myresponseis111 temp res: " + res);

                                        Document document = Jsoup.parse(res);

                                        ArrayList<String> arrayListLinks = new ArrayList<>();

                                        Elements elements = document.getElementsByTag("a");

                                        //   Elements elements = document.select("a[]");
                                        for (Element element : elements) {

                                            if (!element.attr("href").contains("https://play.google.com") && !element.attr("href").contains("https://ssscdn.io/tiktokdownload/a/") && element.attr("href").contains("tiktokcdn") && element.attr("href").length() > 150) {
                                                arrayListLinks.add(element.attr("href"));
                                                System.out.println("myresponseis111 testing tiktok: " + element.attr("href"));
                                            }
                                        }

                                        dismissMyDialog();

                                        Thread.sleep(500);

                                        DownloadFileMain.startDownloading(context, arrayListLinks.get(0), "Tiktok_" + System.currentTimeMillis(), ".mp4");
                                    } catch (Exception e3) {
                                        System.out.println("myresponseis111 exp2 " + e3.getMessage());
                                        dismissMyDialogErrortoast();

                                    }

                                }

                            } catch (Exception unused) {
                                System.out.println("myresponseis111 exp " + unused.getMessage());

                                dismissMyDialogErrortoast();
                            }

                        });

                    });

                    methode1.setVisibility(View.VISIBLE);
                    methode1.setOnClickListener(v -> {
                        dialog.dismiss();

                        System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);

                        dismissMyDialog();
                        Intent intent = new Intent(Mcontext, TikTokDownloadCloudBypassWebview_method_2.class);
                        intent.putExtra("myvidurl", finalUrl1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Mcontext.startActivity(intent);

                        //
                        //                    TikTokNewTestDownloader tikTokNewTestDownloader = new TikTokNewTestDownloader(Mcontext, finalUrl1);
                        //                    tikTokNewTestDownloader.DownloadVideo();
                    });

                    methode2.setOnClickListener(v -> {
                        dialog.dismiss();

                        System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);

                        Executors.newSingleThreadExecutor().submit(() -> {
                            try {
                                ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));

                                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                                OkHttpClient client = new OkHttpClient.Builder()
                                        .cookieJar(cookieJar)
                                        .addInterceptor(logging)
                                        .build();

                                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                        .addFormDataPart("url", finalUrl1)
                                        .build();
                                final String[] userAgent = {
                                        "Mozilla/5.0 (iPad; CPU OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/87.0.4280.77 Mobile/15E148 Safari/604.1"
                                };
                                ((Activity) Mcontext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        userAgent[0] = new WebView(Mcontext).getSettings().getUserAgentString();
                                        System.out.println("myresponseis111 " + userAgent[0]);
                                    }
                                });

                                Request request = new Request.Builder()
                                        .url("https://tikcd.com/en/video/info")
                                        .method("POST", body)
                                        .addHeader("origin", "https://tikcd.com")
                                        .addHeader("referer", "https://tikcd.com/")
                                        .addHeader("user-agent", "" + userAgent[0])
                                        .addHeader("x-requested-with", "XMLHttpRequest")
                                        .build();
                                Response response = client.newCall(request).execute();

                                if (response.code() == 200) {
                                    String urlisddd;
                                    String resss = Objects.requireNonNull(response.body()).string();

                                    Document document = Jsoup.parse(resss);

                                    System.out.println("myresponseis111 exp 888 " + resss);

                                    urlisddd = document.select("a").get(0).attr("href");

                                    System.out.println("myresponseis111 exp 888 urlisddd " + urlisddd);

                                    if (urlisddd != null && !urlisddd.equals("")) {

                                        dismissMyDialog();
                                        DownloadFileMain.startDownloading(context, urlisddd, "Tiktok_" + System.currentTimeMillis(), ".mp4");

                                    } else {
                                        dismissMyDialogErrortoast();
                                    }
                                } else {
                                    dismissMyDialogErrortoast();

                                }

                                //                                    https://www.tiktok.com/@sidmr.rapper4/video/7041988986662915354

                            } catch (Throwable e) {
                                e.printStackTrace();
                                System.out.println("myresponseis111 exp " + e.getLocalizedMessage());

                                dismissMyDialogErrortoast();
                            }
                        });

                    });

                    methode3.setOnClickListener(v -> {
                        dialog.dismiss();

                        System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);

                        dismissMyDialog();
                        Intent intent = new Intent(Mcontext, TikTokDownloadCloudBypassWebview_method_3.class);
                        intent.putExtra("myvidurl", finalUrl1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Mcontext.startActivity(intent);

                    });

                    methode4.setOnClickListener(v -> {
                        dialog.dismiss();

                        System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);

                        dismissMyDialog();
                        Intent intent = new Intent(Mcontext, TikTokDownloadCloudBypassWebview_method_4.class);
                        intent.putExtra("myvidurl", finalUrl1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Mcontext.startActivity(intent);

                        //
                        //                    TikTokNewTestDownloader tikTokNewTestDownloader = new TikTokNewTestDownloader(Mcontext, finalUrl1);
                        //                    tikTokNewTestDownloader.DownloadVideo();
                    });

                    methode5.setVisibility(View.VISIBLE);
                    methode5.setOnClickListener(v -> {
                        dialog.dismiss();

                        System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);
                        callDlApiNew(finalUrl1);

                    });

                    methode6.setVisibility(View.GONE);
                    methode6.setOnClickListener(v -> {
                        dialog.dismiss();

                        try {

                            System.out.println("myurliss = " + finalUrl1);

                            Executors.newSingleThreadExecutor().submit(() -> {
                                try {
                                    Looper.prepare();

                                    ClearableCookieJar cookieJar =
                                            new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));

                                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                                    OkHttpClient client = new OkHttpClient.Builder()
                                            .cookieJar(cookieJar)
                                            .addInterceptor(logging)
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(30, TimeUnit.SECONDS)
                                            .build();

                                    MediaType mediaType = MediaType.parse("application/json");
                                    RequestBody body = RequestBody.create(mediaType, "{\"url\":\"" + finalUrl1 + "\"}");
                                    Request request = new Request.Builder()
                                            .url("https://ssyoutube.com/api/convert")
                                            .method("POST", body)
                                            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                                            .addHeader("x-requested-with", "XMLHttpRequest")
                                            .addHeader("Content-Type", "application/json")
                                            .build();
                                    Response response = client.newCall(request).execute();

                                    String ressff = Objects.requireNonNull(response.body()).string();
                                    System.out.println("myurliss resss = " + ressff);

                                    JSONObject document = new JSONObject(ressff);
                                    dismissMyDialog();

                                    String nametitle = "Tiktok_" +
                                            System.currentTimeMillis();

                                    DownloadFileMain.startDownloading(Mcontext, document.getJSONArray("url").getJSONObject(0).getString("url"), nametitle, ".mp4");

                                } catch (Exception e) {
                                    System.out.println("myurliss err dialog = " + e.getLocalizedMessage());

                                    e.printStackTrace();
                                    dismissMyDialogErrortoast();
                                }


                            });
                        } catch (Exception e) {
                            System.out.println("myurliss err dialog 4= " + e.getLocalizedMessage());

                            e.printStackTrace();
                            dismissMyDialogErrortoast();
                        }

                    });

                    dig_btn_cancel.setOnClickListener(v -> {
                        dialog.dismiss();
                        dismissMyDialog();
                    });

                    dialog.setCancelable(false);
                    dialog.show();

                }
                System.out.println("wojfdjhfdjh url=" + url);

            }
            else if (url.contains("snapchat.com")) {

                if (!iUtils.isSocialMediaOn("snapchat.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                try {
                    url = iUtils.extractUrls(url).get(0);

                    URI uri = new URI(url);
                    String urlwi = new URI(
                            uri.getScheme(),
                            uri.getAuthority(),
                            uri.getPath(),
                            null, // Ignore the query part of the input url
                            uri.getFragment()
                    ).toString();

                    System.out.println("snamppppp " + urlwi.trim());
                    dismissMyDialog();
                    if (urlwi.split("/").length > 5) {
                        Mcontext.startActivity(new Intent(Mcontext, SnapChatBulkStoryDownloader.class).putExtra("urlsnap", urlwi.substring(0, urlwi.lastIndexOf("/")).trim()));
                    } else {
                        Mcontext.startActivity(new Intent(Mcontext, SnapChatBulkStoryDownloader.class).putExtra("urlsnap", urlwi.trim()));

                    }

                } catch (Exception e) {
                    dismissMyDialogErrortoast();
                }

            }
            else if (url.contains("popcornflix")) {
                if (!iUtils.isSocialMediaOn("popcornflix")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                url = url.substring(url.lastIndexOf("/") + 1);

                System.out.println("fjhjfhjsdfsdhf " + url);

                AndroidNetworking.get("https://api.unreel.me/v2/sites/popcornflix/videos/" + url + "/play-url?__site=popcornflix&__source=web&embed=false&protocol=https&tv=false")
                        .addHeaders("User-Agent", "Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                System.out.println("fjhjfhjsdfsdhf " + response);

                                dismissMyDialog();
                                String matag;
                                try {

                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    matag = jsonObject.getString("url");
                                    System.out.println("wojfdjhfdjh " + matag);
                                    DownloadFileMain.startDownloading(context, matag, "Popcornflex_" + System.currentTimeMillis(), ".mp4");

                                } catch (Exception e) {
                                    dismissMyDialog();
                                    e.printStackTrace();
                                }

                            }

                            @Override /**/
                            public void onError(ANError error) {
                                dismissMyDialog();
                            }
                        });

            }
            else if (url.contains("veoh")) {

                if (!iUtils.isSocialMediaOn("veoh")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                }
                url = url.substring(url.lastIndexOf("/") + 1);

                System.out.println("fjhjfhjsdfsdhf " + url);

                AndroidNetworking.get("http://www.veoh.com/watch/getVideo/" + url)
                        .addHeaders("User-Agent", "Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                System.out.println("fjhjfhjsdfsdhf " + response);

                                dismissMyDialog();
                                String matag;
                                try {

                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    matag = jsonObject.getJSONObject("video").getJSONObject("src").getString("HQ");
                                    System.out.println("wojfdjhfdjh " + matag);
                                    DownloadFileMain.startDownloading(context, matag, "Veoh_" + System.currentTimeMillis(), ".mp4");

                                } catch (Exception e) {
                                    dismissMyDialog();
                                    e.printStackTrace();
                                }

                            }

                            @Override /**/
                            public void onError(ANError error) {
                                dismissMyDialog();
                            }
                        });

            }
            else if (url.contains("moj")) {
                //                https://mojapp.in/@officialrobin13/video/2791694826?referrer=TNuztD6-fgUv4B

                if (!iUtils.isSocialMediaOn("moj")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                try {
                    url = url.substring(url.lastIndexOf("/") + 1);

                    if (url.contains("?referrer=share")) {
                        url = url.substring(0, url.indexOf("?"));
                        System.out.println("fjhjfhjsdfsdhf 000=" + url + " size " + url.indexOf("?"));
                        System.out.println("fjhjfhjsdfsdhf 000=" + "https://moj-apis.sharechat.com/videoFeed?postId=" + url + "&firstFetch=true");
                    }

                    JSONObject jsonObject = new JSONObject("{\"appVersion\":83,\"bn\":\"broker1\",\"client\":\"android\",\"deviceId\":\"ebb088d29e7287b1\",\"message\":{\"adData\":{\"adsShown\":0,\"firstFeed\":false},\"deviceInfoKey\":\"OSyQoHJLJ4NsXPLyQePkAICh3Q0ih0bveFwm1KEV+vReMuldqo+mSyMjdhb4EeryKxk1ctAbYaDH\\\\nTI+PMRPZVYH5pBccAm7OT2uz69vmD/wPqGuSgWV2aVNMdM75DMb8NZn1JU2b1bo/oKs80baklsvx\\\\n1X7jrFPL6M5EDTdPDhs=\\\\n\",\"deviceInfoPayload\":\"M6g+6j6irhFT/H6MsQ/n/tEhCl7Z5QgtVfNKU8M90zTJHxqljm2263UkjRR9bRXAjmQFXXOTXJ25\\\\nOHRjV7L5Lw+tUCONoYfyUEzADihWfAiUgXJEcKePfZONbdXXuwGgOPeD0k4iSvI7JdzroRCScKXd\\\\n41CkmXFayPaRL9aqgAgs6kSoIncCWBU2gEXiX1lgPVvdmUzCZ+yi2hFA+uFOmv1MJ6dcFKKcpBM6\\\\nHSPIrGV+YtTyfd8nElx0kyUbE4xmjOuMrctkjnJkd2tMdxB8qOFKeYrcLzy4LZJNXyUmzs29XSE+\\\\nhsrMZib8fFPJhJZIyGCWqfWiURut4Bg5HxYhYhg3ejPxFjNyXxS3Ja+/pA+A0olt5Uia7ync/Gui\\\\n58tlDQ4SKPthCzGa1tCVN+2y/PW30+LM79t0ltJ/YrNZivQx4eEnszlM9nwmIuj5z5LPniQghA6x\\\\nrfQ8IqVUZfiitXj/Fr7UjKg1cs/Ajj8g4u/KooRvVkg9tMwWePtJFqrkk1+DU4cylnSEG3XHgfer\\\\nslrzj5NNZessMEi+4Nz0O2D+b8Y+RjqN6HqpwZPDHhZwjz0Iuj2nhZLgu1bgNJev5BwxAr8akDWv\\\\nvKsibrJS9auQOYVzbYZFdKMiBnh+WHq0qO2aW1akYWCha3ZsSOtsnyPnFC+1PnMbBv+FiuJmPMXg\\\\nSODFoRIXfxgA/qaiKBipS+kIyfaPxn6O1i6MOwejVuQiWdAPTO132Spx0cFtdyj2hX6wAMe21cSy\\\\n8rs3KQxiz+cq7Rfwzsx4wiaMryFunfwUwnauGwTFOW98D5j6oO8=\\\\n\",\"lang\":\"Hindi\",\"playEvents\":[{\"authorId\":\"18326559001\",\"networkBitrate\":1900000,\"initialBufferPercentage\":100,\"isRepost\":false,\"sg\":false,\"meta\":\"NotifPostId\",\"md\":\"Stream\",\"percentage\":24.68405,\"p\":\"91484006\",\"radio\":\"wifi\",\"r\":\"deeplink_VideoPlayer\",\"repeatCount\":0,\"timeSpent\":9633,\"duration\":15,\"videoStartTime\":3916,\"t\":1602255552820,\"clientType\":\"Android\",\"i\":79,\"appV\":83,\"sessionId\":\"72137847101_8863b3f5-ad2d-4d59-aa7c-cf1fb9ef32ea\"},{\"authorId\":\"73625124001\",\"networkBitrate\":1900000,\"initialBufferPercentage\":100,\"isRepost\":false,\"sg\":false,\"meta\":\"list2\",\"md\":\"Stream\",\"percentage\":17.766666,\"p\":\"21594412\",\"radio\":\"wifi\",\"r\":\"First Launch_VideoPlayer\",\"repeatCount\":0,\"tagId\":\"0\",\"tagName\":\"\",\"timeSpent\":31870,\"duration\":17,\"videoStartTime\":23509,\"t\":1602218215942,\"clientType\":\"Android\",\"i\":79,\"appV\":83,\"sessionId\":\"72137847101_db67c0c9-a267-4cec-a3c3-4c0fa4ea16e1\"}],\"r\":\"VideoFeed\"},\"passCode\":\"9e32d6145bfe53d14a0c\",\"resTopic\":\"response/user_72137847101_9e32d6145bfe53d14a0c\",\"userId\":\"72137847101\"}");

                    AndroidNetworking.post("https://moj-apis.sharechat.com/videoFeed?postId=" + url + "&firstFetch=true")
                            .addHeaders("X-SHARECHAT-USERID", "72137847101")
                            .addHeaders("X-SHARECHAT-SECRET", "9e32d6145bfe53d14a0c")
                            .addHeaders("APP-VERSION", "83")
                            .addHeaders("PACKAGE-NAME", "in.mohalla.video")
                            .addHeaders("DEVICE-ID", "ebb088d29e7287b1")
                            .addHeaders("CLIENT-TYPE", "Android:")
                            .addHeaders("Content-Type", "application/json; charset=UTF-8")
                            .addHeaders("Host", "moj-apis.sharechat.com")
                            .addHeaders("Connection", "Keep-Alive:")
                            .addHeaders("User-Agent", "okhttp/3.12.12app-version:83")
                            .addHeaders("cache-control", "no-cache")
                            .addHeaders("client-type", "Android")
                            .addHeaders("connection", "Keep-Alive")
                            .addHeaders("content-type", "application/json;charset=UTF-8")
                            .addHeaders("device-id", "ebb088d29e7287b1")
                            .addHeaders("host", "moj-apis.sharechat.com")
                            .addHeaders("package-name", "in.mohalla.video")
                            .addHeaders("postman-token", "37d59a7c-f247-3b70-ab3c-1dedf4079852")
                            .addHeaders("user-agent", "okhttp/3.12.12")
                            .addHeaders("x-sharechat-secret", "9e32d6145bfe53d14a0c")
                            .addHeaders("x-sharechat-userid", "72137847101")
                            .addJSONObjectBody(jsonObject)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    System.out.println("fjhjfhjsdfsdhf " + response);

                                    dismissMyDialog();
                                    String matag;
                                    try {

                                        JSONObject jsonObject = new JSONObject(response.toString());

                                        matag = jsonObject.getJSONObject("payload")
                                                .getJSONArray("d")
                                                .getJSONObject(0)
                                                .getString("compressedVideoUrl");

                                        System.out.println("wojfdjhfdjh " + matag);
                                        DownloadFileMain.startDownloading(context, matag, "Moj_" + System.currentTimeMillis(), ".mp4");

                                    } catch (Exception e) {
                                        dismissMyDialog();
                                        e.printStackTrace();
                                    }

                                }

                                @Override /**/
                                public void onError(ANError error) {
                                    dismissMyDialog();
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                    dismissMyDialog();
                }
            }
            else if (url.contains("fairtok")) {
                if (!iUtils.isSocialMediaOn("fairtok")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                try {
                    url = url.substring(url.lastIndexOf("/") + 1);

                    JSONObject jsonObject = new JSONObject("{\"device_fingerprint_id\":\"838529202513017602\",\"identity_id\":\"838529202537882206\",\"hardware_id\":\"ebb088d29e7287b1\",\"is_hardware_id_real\":true,\"brand\":\"samsung\",\"model\":\"SM-J200G\",\"screen_dpi\":240,\"screen_height\":960,\"screen_width\":540,\"wifi\":true,\"ui_mode\":\"UI_MODE_TYPE_NORMAL\",\"os\":\"Android\",\"os_version\":22,\"cpu_type\":\"armv7l\",\"build\":\"LMY47X.J200GDCU2ARL1\",\"locale\":\"en_GB\",\"connection_type\":\"wifi\",\"os_version_android\":\"5.1.1\",\"country\":\"GB\",\"language\":\"en\",\"local_ip\":\"192.168.43.18\",\"app_version\":\"1.19\",\"facebook_app_link_checked\":false,\"is_referrable\":0,\"debug\":false,\"update\":1,\"latest_install_time\":1601158937336,\"latest_update_time\":1601158937336,\"first_install_time\":1601158937336,\"previous_update_time\":1601158937336,\"environment\":\"FULL_APP\",\"android_app_link_url\":\"https:\\/\\/fairtok.app.link\\/" + url + "\",\"external_intent_uri\":\"https:\\/\\/fairtok.app.link\\/Y7ov2al149\",\"cd\":{\"mv\":\"-1\",\"pn\":\"com.fairtok\"},\"metadata\":{},\"advertising_ids\":{\"aaid\":\"094dfa1f-77cf-4f84-b373-2c15bf74f9d1\"},\"lat_val\":0,\"google_advertising_id\":\"094dfa1f-77cf-4f84-b373-2c15bf74f9d1\",\"instrumentation\":{\"v1\\/open-qwt\":\"0\"},\"sdk\":\"android5.0.1\",\"branch_key\":\"key_live_hjLYp0Wi3i6R1qQ1Lr51TlpcBvkxEp53\",\"retryNumber\":0}");

                    AndroidNetworking.post("https://api2.branch.io/v1/open")
                            .addHeaders("cache-control", "no-cache")

                            .addJSONObjectBody(jsonObject)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    System.out.println("fjhjfhjsdfsdhf " + response);

                                    dismissMyDialog();
                                    String matag;
                                    try {

                                        JSONObject jsonObject = new JSONObject(response.toString());

                                        matag = "https://bucket-fairtok.s3.ap-south-1.amazonaws.com/" + jsonObject.getString("post_video");

                                        System.out.println("wojfdjhfdjh " + matag);
                                        DownloadFileMain.startDownloading(context, matag, "Fairtok_" + System.currentTimeMillis(), ".mp4");

                                    } catch (Exception e) {
                                        dismissMyDialog();
                                        e.printStackTrace();
                                    }

                                }

                                @Override /**/
                                public void onError(ANError error) {
                                    dismissMyDialog();
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                    dismissMyDialog();
                }
            }
            else if (url.contains("videoclip.org")) {
                if (!iUtils.isSocialMediaOn("videoclip.org")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
            }
            else if (url.contains("funimate")) {
                if (!iUtils.isSocialMediaOn("funimate")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                CalldlApisDataData(url, true);
            }
            else if (url.contains("1tv.ru")) {
                if (!iUtils.isSocialMediaOn("1tv.ru")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
            }
            else if (url.contains("naver")) {
                if (!iUtils.isSocialMediaOn("naver")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                CalldlApisDataData(url, true);
            }
            else if (url.contains("gloria.tv")) {

                if (!iUtils.isSocialMediaOn("gloria.tv")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
            }
            else if (url.contains("vidcommons.org")) {

                if (!iUtils.isSocialMediaOn("vidcommons.org")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
            }
            else if (url.contains("media.ccc.de")) {

                if (!iUtils.isSocialMediaOn("media.ccc.de")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
            }
            else if (url.contains("vlive")) {

                if (!iUtils.isSocialMediaOn("vlive")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
            }
            else if (url.contains("blogspot.com")) {
                if (!iUtils.isSocialMediaOn("blogspot.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);

            }
            else if (url.contains("vimeo.com")) {
                if (!iUtils.isSocialMediaOn("vimeo.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                //https://api.m3u8-downloader.com/api/parse?url=https://vimeo.com/115074667

                 VimeoVideoDownloader downloader = new VimeoVideoDownloader(Mcontext, url);
                  downloader.DownloadVideo();
                CalldlApisDataData(url, true);

            }
            else if (url.contains("flickr") || url.contains("flic.kr")) {
                if (!iUtils.isSocialMediaOn("flickr") || !iUtils.isSocialMediaOn("flic.kr")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                // CallDailymotionData(url, true);
                CalldlApisDataData(url, true);

            }
            else if (url.contains("streamable")) {

                if (!iUtils.isSocialMediaOn("streamable")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                // CallDailymotionData(url, true);
                CalldlApisDataData(url, true);

            }
            else if (url.contains("vk.com")) {

                if (!iUtils.isSocialMediaOn("vk.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                CalldlApisDataData(url, false);

                //CallVKData(url, false);

            }
            else if (url.contains("soundcloud")) {

                if (!iUtils.isSocialMediaOn("soundcloud")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                // CallsoundData(url, false);

                if (Constants.showsoundcloud) {
                    url = url.replace("//m.", "//");

                    CalldlApisDataData(url, true);
                } else {
                    dismissMyDialogErrortoast();
                }

            }
            else if (url.contains("bandcamp")) {

                if (!iUtils.isSocialMediaOn("bandcamp")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                //TODO Has multiple video json array
                CalldlApisDataData(url, true);
                //   CallsoundData(url, false);

            }
            else if (url.contains("cocoscope")) {

                if (!iUtils.isSocialMediaOn("cocoscope")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                // CallsoundData(url, false);
                CalldlApisDataData(url, true);

            }
            else if (url.contains("vidlit")) {

                if (!iUtils.isSocialMediaOn("vidlit")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                // CallsoundData(url, false);
                CalldlApisDataData(url, true);

            }
            else if (url.contains("izlesene")) {

                if (!iUtils.isSocialMediaOn("izlesene")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                //CallsoundData(url, false);
                CalldlApisDataData(url, false);

            }
            else if (url.contains("linkedin")) {

                if (!iUtils.isSocialMediaOn("linkedin")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                //   CalldlApisDataData(url, false);
                new CalllinkedinData().execute(url);

            }
            else if (url.contains("kwai") || url.contains("kw.ai")) {
                if (!iUtils.isSocialMediaOn("kwai") || !iUtils.isSocialMediaOn("kw.ai")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                //https://v.kuaishou.com/N4D3Ci
                //http://s.kw.ai/p/BhhKA7HG
                CalldlApisDataData(url, false);

                //                KwaiDownloader kwaiDownloader = new KwaiDownloader(Mcontext, url);
                //                kwaiDownloader.DownloadVideo();

            }
            else if (url.contains("bitchute")) {
                if (!iUtils.isSocialMediaOn("bitchute")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }CalldlApisDataData(url, false);}
            else if (url.contains("ojoo")) {
                if (!iUtils.isSocialMediaOn("ojoo")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                CalldlApisDataData(url, false);

            }
            else if (url.contains("blogger")) {
                if (!iUtils.isSocialMediaOn("blogger")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                CalldlApisDataData(url, false);

            }
            else if (url.contains("break.com")) {
                if (!iUtils.isSocialMediaOn("break.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                CalldlApisDataData(url, false);

            }
            else if (url.contains("espn")) {
                if (!iUtils.isSocialMediaOn("espn")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);

            }
            else if (url.contains("coub")) {
                if (!iUtils.isSocialMediaOn("coub")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
                //            CoubDownloader coubDownloader = new CoubDownloader(Mcontext, url);
                //            coubDownloader.DownloadVideo();

            }
            else if (url.contains("ted.com")) {
                if (!iUtils.isSocialMediaOn("ted.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
            }
            else if (url.contains("fansubs.tv")) {
                if (!iUtils.isSocialMediaOn("fansubs.tv")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
            }
            else if (url.contains("twitch")) {
                if (!iUtils.isSocialMediaOn("twitch")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);

            }
            else if (url.contains("imgur.com")) {
                if (!iUtils.isSocialMediaOn("imgur.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                url = url.replace("//m.", "//");
                CalldlApisDataData(url, false);

            }
            else if (url.contains("rumble.com")) {
                if (!iUtils.isSocialMediaOn("rumble.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                CalldlApisDataData(url, false);

            }

            // TODO youtube from here
            // http://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=6s7NgJ_GlzM&format=json
            else if (url.contains("youtube.com") || url.contains("youtu.be")) {

                Log.i("LOGClipboard111111 testinggg ytd", " " + iUtils.isSocialMediaOn("youtube.com"));

                if (Constants.showyoutube) {

                    if (!iUtils.isSocialMediaOn("youtube.com") || !iUtils.isSocialMediaOn("youtu.be")) {
                        dismissMyDialogErrorToastForBlockedWebsitePanel();
                        return;
                    }

                    Log.i("LOGClipboard111111 clip", "work 3");
                    // getYoutubeDownloadUrl(url);
//                    CalldlApisDataData(url, true);
                    callDlApiNew(url);
                } else {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                }

            }
            else if (url.contains("sharechat.com")) {

                if (!iUtils.isSocialMediaOn("sharechat.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                Log.i("LOGClipboard111111 clip", "work 66666");
                try {
                    // new callGetShareChatData().execute(url);

                    url = iUtils.extractUrls(url).get(0);
                    System.out.println("subssssssss11 " + url);

                    int index = url.lastIndexOf('/') + 1;
                    url = url.substring(index);
                    if (url.contains("?")) {
                        url = url.split("[?]")[0];
                    }
                    System.out.println("subssssssss " + url);

                    JSONObject jsonObject = new JSONObject("{\"bn\":\"broker3\",\"userId\":644045091,\"passCode\":\"52859d76753457f8dcae\",\"client\":\"web\",\"message\":{\"key\":\"" + url + "\",\"ph\":\"" + url + "\"}}");
                    AndroidNetworking.post("https://apis.sharechat.com/requestType45")
                            .addJSONObjectBody(jsonObject)
                            .addHeaders("Content-Type", "application/json")
                            .addHeaders("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    dismissMyDialog();
                                    String matag;
                                    try {

                                        JSONObject jsonObject = new JSONObject(response.toString());

                                        boolean isvideo = true;
                                        try {
                                            matag = jsonObject.getJSONObject("payload").getJSONObject("d").getString("v");
                                            isvideo = true;
                                        } catch (Exception e) {
                                            matag = jsonObject.getJSONObject("payload").getJSONObject("d").getString("g");
                                            isvideo = false;
                                        }

                                        System.out.println("wojfdjhfdjh " + matag);
                                        DownloadFileMain.startDownloading(context, matag, "Sharechat_" + System.currentTimeMillis(), ((isvideo) ? ".mp4" : ".jpg"));

                                    } catch (Exception e) {

                                        e.printStackTrace();
                                        dismissMyDialog();
                                    }

                                }

                                @Override
                                public void onError(ANError error) {
                                    dismissMyDialog();
                                }
                            });

                } catch (Exception e) {
                    dismissMyDialog();
                }

            }
            else if (url.contains("roposo.com")) {

                if (!iUtils.isSocialMediaOn("roposo.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                Log.i("LOGClipboard111111 clip", "work 66666");

                new callGetRoposoData().execute(url);
                Log.i("LOGClipboard111111 clip", "work 1111111");

            }
            else if (url.contains("snackvideo") || url.contains("sck.io")) {

                if (!iUtils.isSocialMediaOn("snackvideo") || !iUtils.isSocialMediaOn("sck.io")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                //https://snackvideo.social/p/U8kje11Q

                System.out.println("myresponseis111 urlsss" + url);

                new callGetsnackvideoData().execute(url);

                //                if (url.contains("snackvideo.com")) {
                //                    new callGetSnackAppData().execute(url);
                //                } else if (url.contains("sck.io")) {
                //                    getSnackVideoData(url, Mcontext);
                //                }

            }
            else if (url.contains("facebook.com") || url.contains("fb.watch")) {

                if (!iUtils.isSocialMediaOn("facebook.com") || !iUtils.isSocialMediaOn("fb.watch")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

        /*
        TODO not working
        https://m.facebook.com/story.php?story_fbid=10160134899489468&id=506699467
        https://www.facebook.com/100004478714645/posts/2230800797079189/
        https://www.facebook.com/100002655463277/posts/4874514195980381/
        https://m.facebook.com/story.php?story_fbid=1881132202078276&id=104400186269172
        https://www.facebook.com/watch/?v=1403988643425724
        https://www.facebook.com/221895084488402/videos/1403988643425724/
        * */

                if (!((Activity) Mcontext).isFinishing()) {
                    Dialog dialog = new Dialog(Mcontext);

                    dialog.setContentView(R.layout.tiktok_optionselect_dialog);

                    String finalUrl1 = url;

                    Button methode0 = dialog.findViewById(R.id.dig_btn_met0);
                    Button methode1 = dialog.findViewById(R.id.dig_btn_met1);
                    Button methode2 = dialog.findViewById(R.id.dig_btn_met2);
                    Button methode3 = dialog.findViewById(R.id.dig_btn_met3);
                    Button methode4 = dialog.findViewById(R.id.dig_btn_met4);
                    Button methode5 = dialog.findViewById(R.id.dig_btn_met5);
                    Button methode6 = dialog.findViewById(R.id.dig_btn_met6);

                    methode5.setVisibility(View.VISIBLE);
                    methode6.setVisibility(View.VISIBLE);

                    Button dig_btn_cancel = dialog.findViewById(R.id.dig_btn_cancel);

                    methode0.setOnClickListener(v -> {
                        dialog.dismiss();

                        try {

                            System.out.println("myurliss = " + finalUrl1);

                            Executors.newSingleThreadExecutor().submit(() -> {
                                try {
                                    Looper.prepare();

                                    ClearableCookieJar cookieJar =
                                            new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));

                                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                                    OkHttpClient client = new OkHttpClient.Builder()
                                            .cookieJar(cookieJar)
                                            .addInterceptor(logging)
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(30, TimeUnit.SECONDS)
                                            .build();

                                    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                            .addFormDataPart("url", finalUrl1)
                                            .build();
                                    Request request = new Request.Builder()
                                            .url("https://snapsave.app/action.php")
                                            .method("POST", body)
                                            .build();
                                    Response response = client.newCall(request).execute();

                                    String ressff = Objects.requireNonNull(response.body()).string();
                                    System.out.println("myurliss resss = " + ressff);


                                    if (!ressff.equals("")) {
                                        String nametitle = "Facebook_" +
                                                System.currentTimeMillis();

                                        String inputString = ressff;
//                                                DownloadFileMain.startDownloading(Mcontext, document.getJSONObject("links").getString("hd"), nametitle, ".mp4");

                                        String targetString = "decodeURIComponent(escape(r))";
                                        String prefix = "console.log('Hello'+";
                                        String suffix = ")";

                                        String outputString = inputString.replace(targetString, prefix + targetString + suffix);
                                        System.out.println(outputString);

                                        Mcontext.runOnUiThread(() -> {
                                            WebView web = new WebView(Mcontext);
                                            web.getSettings().setJavaScriptEnabled(true);
                                            web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                                            web.getSettings().getAllowFileAccess();
                                            web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            web.getSettings().setDomStorageEnabled(true);
                                            web.getSettings().setAllowUniversalAccessFromFileURLs(true);
                                            web.getSettings().setMediaPlaybackRequiresUserGesture(false);
                                            CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
                                            int randomNumber = iUtils.getRandomNumber(iUtils.UserAgentsList0.length);

                                            try {
                                                web.getSettings().setUserAgentString(iUtils.UserAgentsList0[randomNumber]);
                                            } catch (Exception e) {
                                                web.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36");
                                            }

                                            web.setWebChromeClient(new WebChromeClient() {
                                                @Override
                                                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                                                    try {
                                                        Log.d("chromium-A-WebView", consoleMessage.message());
                                                        String decodedHtml = StringEscapeUtils.unescapeHtml4(consoleMessage.message());
                                                        isStartedFbDownload = false;
                                                        List<String> allurls = iUtils.extractUrls(decodedHtml);

                                                        for (int i = 0; i < allurls.size(); i++) {
                                                            boolean idVid = allurls.get(i).contains(".mp4");

                                                            if (idVid && !isStartedFbDownload) {
                                                                DownloadFileMain.startDownloading(Mcontext, allurls.get(i), nametitle, ".mp4");
                                                                isStartedFbDownload = true;
                                                            }
                                                        }


                                                    } catch (Exception e) {
                                                        dismissMyDialogErrortoast();
                                                        e.printStackTrace();
                                                    }

                                                    return true;

                                                }
                                            });
                                            web.evaluateJavascript("javascript:" + outputString, value -> {
                                                System.out.println("myvall=" + value);
                                            });
                                        });

                                        dismissMyDialog();
                                    } else {

                                        dismissMyDialogErrortoast();
                                    }

                                } catch (Exception e) {
                                    System.out.println("myurliss err dialog = " + e.getLocalizedMessage());

                                    e.printStackTrace();
                                    dismissMyDialogErrortoast();
                                }

                            });

                        } catch (Exception e) {
                            System.out.println("myurliss err dialog 4= " + e.getLocalizedMessage());

                            e.printStackTrace();
                            dismissMyDialogErrortoast();
                        }

                    });

                    methode1.setOnClickListener(v -> {
                        dialog.dismiss();

                        System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);

                        dismissMyDialog();
                        Intent intent = new Intent(Mcontext, GetLinkThroughWebView.class);
                        intent.putExtra("myurlis", finalUrl1);
                        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ((Activity) Mcontext).startActivityForResult(intent, 2);

                    });


                    methode2.setOnClickListener(v -> {
                        dialog.dismiss();

                        try {

                            System.out.println("myurliss = " + finalUrl1);
                            FbVideoDownloader fbVideoDownloader = new FbVideoDownloader(Mcontext, finalUrl1, 1);
                            fbVideoDownloader.DownloadVideo();

                        } catch (Exception e) {
                            System.out.println("myurliss err dialog 4= " + e.getLocalizedMessage());

                            e.printStackTrace();
                            dismissMyDialogErrortoast();
                        }

                    });

                    methode3.setOnClickListener(v -> {
                        dialog.dismiss();

                        FbVideoDownloader fbVideoDownloader = new FbVideoDownloader(Mcontext, finalUrl1, 3);
                        fbVideoDownloader.DownloadVideo();
                    });

                    methode4.setOnClickListener(v -> {
                        dialog.dismiss();

                        FbVideoDownloader fbVideoDownloader = new FbVideoDownloader(Mcontext, finalUrl1, 2);
                        fbVideoDownloader.DownloadVideo();
                    });

                    methode5.setOnClickListener(v -> {
                        dialog.dismiss();
                        callDlApiNew(finalUrl1);
                    });

                    //todo fix it
                    //                    methode5.setOnClickListener(new View.OnClickListener() {
                    //                        @Override
                    //                        public void onClick(View v) {
                    //                            dialog.dismiss();
                    //
                    //                            try {
                    //
                    //                                System.out.println("myurliss = " + finalUrl1);
                    //
                    //                                if (mytiktok_get_thread != null && mytiktok_get_thread.isAlive()) {
                    //                                    mytiktok_get_thread.stop();
                    //                                }
                    //                                mytiktok_get_thread = new Thread() {
                    //
                    //                                    @Override
                    //                                    public void run() {
                    //                                        super.run();
                    //                                        try {
                    //                                            Looper.prepare();
                    //
                    //                                            ClearableCookieJar cookieJar =
                    //                                                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));
                    //
                    //                                            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    //                                            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    //                                            OkHttpClient client = new OkHttpClient.Builder()
                    //                                                    .cookieJar(cookieJar)
                    //                                                    .addInterceptor(logging)
                    //                                                    .connectTimeout(10, TimeUnit.SECONDS)
                    //                                                    .writeTimeout(10, TimeUnit.SECONDS)
                    //                                                    .readTimeout(30, TimeUnit.SECONDS)
                    //                                                    .build();
                    //
                    //
                    //                                            Request request = new Request.Builder()
                    //                                                    .url(finalUrl1)
                    //                                                    .method("GET", null)
                    //                                                    .build();
                    //
                    //                                            Response response;
                    //
                    //                                            response = client.newCall(request).execute();
                    //
                    //                                            Document document = Jsoup.parse(Objects.requireNonNull(response.body()).string());
                    //                                            dismissMyDialog();
                    //
                    //                                            String unused = document.select("meta[property=\"og:video\"]").last().attr("content");
                    //                                            if (!unused.equals("")) {
                    //                                                String nametitle = "Facebook_" +
                    //                                                        System.currentTimeMillis();
                    //
                    //                                                DownloadFileMain.startDownloading(Mcontext, unused, nametitle, ".mp4");
                    //
                    //                                            }
                    //
                    //                                        } catch (Exception e) {
                    //                                            e.printStackTrace();
                    //                                            dismissMyDialogErrortoast();
                    //                                        }
                    //
                    //
                    //                                    }
                    //                                };
                    //                                mytiktok_get_thread.start();
                    //                            } catch (Exception e) {
                    //                                e.printStackTrace();
                    //                                dismissMyDialogErrortoast();
                    //                            }
                    //
                    //                        }
                    //                    });

                    methode6.setOnClickListener(v -> {
                        dialog.dismiss();

                        try {

                            System.out.println("myurliss = " + finalUrl1);

                            Executors.newSingleThreadExecutor().submit(() -> {
                                try {
                                    Looper.prepare();

                                    ClearableCookieJar cookieJar =
                                            new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));

                                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                                    OkHttpClient client = new OkHttpClient.Builder()
                                            .cookieJar(cookieJar)
                                            .addInterceptor(logging)
                                            .connectTimeout(10, TimeUnit.SECONDS)
                                            .writeTimeout(10, TimeUnit.SECONDS)
                                            .readTimeout(30, TimeUnit.SECONDS)
                                            .build();

                                    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                            .addFormDataPart("q", finalUrl1)
                                            .addFormDataPart("vt", "home")
                                            .addFormDataPart("v", "v2")
                                            .build();
                                    Request request = new Request.Builder()
                                            .url("https://x2download.com/api/ajaxSearch")
                                            .method("POST", body)
                                            .build();
                                    Response response = client.newCall(request).execute();

                                    String ressff = Objects.requireNonNull(response.body()).string();
                                    System.out.println("myurliss resss = " + ressff);

                                    JSONObject document = new JSONObject(ressff);
                                    dismissMyDialog();

                                    if (document.has("data") && !document.getString("data").equals("")) {
                                        String nametitle = "Facebook_" +
                                                System.currentTimeMillis();

                                        String inputString = document.getString("data");
//                                                DownloadFileMain.startDownloading(Mcontext, document.getJSONObject("links").getString("hd"), nametitle, ".mp4");

                                        String targetString = "decodeURIComponent(escape(r))";
                                        String prefix = "console.log('Hello'+";
                                        String suffix = ")";

                                        String outputString = inputString.replace(targetString, prefix + targetString + suffix);
                                        System.out.println(outputString);

                                        Mcontext.runOnUiThread(() -> {
                                            WebView web = new WebView(Mcontext);
                                            web.getSettings().setJavaScriptEnabled(true);
                                            web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                                            web.getSettings().getAllowFileAccess();
                                            web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                            web.getSettings().setDomStorageEnabled(true);
                                            web.getSettings().setAllowUniversalAccessFromFileURLs(true);
                                            web.getSettings().setMediaPlaybackRequiresUserGesture(false);
                                            CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
                                            int randomNumber = iUtils.getRandomNumber(iUtils.UserAgentsList0.length);

                                            try {
                                                web.getSettings().setUserAgentString(iUtils.UserAgentsList0[randomNumber]);
                                            } catch (Exception e) {
                                                web.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36");
                                            }
                                            web.setWebChromeClient(new WebChromeClient() {
                                                @Override
                                                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                                                    try {
                                                        Log.d("chromium-A-WebView", consoleMessage.message());
                                                        String decodedHtml = StringEscapeUtils.unescapeHtml4(consoleMessage.message());

                                                        List<String> allurls = iUtils.extractUrlsWithVideo(decodedHtml);


                                                        DownloadFileMain.startDownloading(Mcontext, allurls.get(0), nametitle, ".mp4");

                                                    } catch (Exception e) {
                                                        dismissMyDialogErrortoast();
                                                        e.printStackTrace();
                                                    }

                                                    return true;

                                                }
                                            });
                                            web.evaluateJavascript("javascript:" + outputString, value -> {
                                                System.out.println("myvall=" + value);
                                            });
                                        });


                                    } else {

                                        dismissMyDialogErrortoast();
                                    }

                                } catch (Exception e) {
                                    System.out.println("myurliss err dialog = " + e.getLocalizedMessage());

                                    e.printStackTrace();
                                    dismissMyDialogErrortoast();
                                }


                            });

                        } catch (Exception e) {
                            System.out.println("myurliss err dialog 4= " + e.getLocalizedMessage());

                            e.printStackTrace();
                            dismissMyDialogErrortoast();
                        }

                    });

                    dig_btn_cancel.setOnClickListener(v -> {
                        dialog.dismiss();
                        dismissMyDialog();
                    });

                    dialog.setCancelable(false);
                    dialog.show();
                }

            }
            else if (url.contains("instagram.com")) {
                if (!iUtils.isSocialMediaOn("instagram.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
//                new GetInstagramVideo().execute(url);
                callDlApiNew(url);

            }
            else if (url.contains("bilibili.com")) {
                if (!iUtils.isSocialMediaOn("bilibili.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                new callGetbilibiliAppData().execute(url);

            }
            else if (url.contains("mitron.tv")) {
                if (!iUtils.isSocialMediaOn("mitron.tv")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                new CallMitronData().execute(url);
            }
            else if (url.contains("josh")) {
                if (!iUtils.isSocialMediaOn("josh")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                new CallJoshData().execute(url);
            }
            else if (url.contains("kuaishou")) {
                if (!iUtils.isSocialMediaOn("kuaishou")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                if (url.contains("https://www.kuaishou.com/short-video/")) {
                    new CallkuaishouData().execute(url);
                } else {
                    if (pd != null && pd.isShowing() && !fromService && !((Activity) Mcontext).isFinishing()) {
                        pd.dismiss();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToast(Mcontext, "Please Use this url type \"https://www.kuaishou.com/short-video/\"");
                        });
                    }
                }


            }
            else if (url.contains("triller")) {
                if (!iUtils.isSocialMediaOn("triller")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                //  new CallTrillerData().execute(url);

                getTrillerData(url);

            }
            else if (url.contains("rizzle")) {
                if (!iUtils.isSocialMediaOn("rizzle")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                new CallRizzleData().execute(url);
            } /*else if (url.contains("solidfiles")) {
                if (!iUtils.isSocialMediaOn("solidfiles")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                SolidfilesDownloader solidfilesDownloader = new SolidfilesDownloader(Mcontext, url);
                solidfilesDownloader.DownloadVideo();

            }*/
            else if (url.contains("ifunny")) {
                if (!iUtils.isSocialMediaOn("ifunny")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                new CallIfunnyData().execute(url);
            }
            else if (url.contains("trell.co")) {
                if (!iUtils.isSocialMediaOn("trell.co")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                if (url.contains("?")) {
                    url = url.split("\\?")[0];
                }
                url = url.substring(url.indexOf("watch"));
                url = url.substring(url.indexOf("/") + 1);

                System.out.println("mytrellis " + url);

                AndroidNetworking.get("https://api.trell.co/api/v1/trail/" + url)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    dismissMyDialog();

                                    String html = response.toString();
                                    System.out.println("myresponseis111 html " + html);

                                    if (!html.equals("")) {
                                        VideoUrl = String.valueOf(new JSONObject(new JSONObject(html)
                                                .getJSONObject("result")
                                                .getJSONObject("trail")
                                                .getJSONArray("posts").get(0).toString())
                                                .get("video"));

                                        System.out.println("myresponseis111 exp991 " + VideoUrl);

                                        if (VideoUrl != null && !VideoUrl.equals("")) {
                                            try {

                                                String myurldocument = VideoUrl;

                                                String nametitle = "trellvideo_" +
                                                        System.currentTimeMillis();

                                                DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                                                //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                                                VideoUrl = "";
                                                //   binding.etText.setText(charSequence);

                                            } catch (Exception document2) {
                                                System.out.println("myresponseis111 exp1 " + document2.getMessage());
                                                dismissMyDialog();
                                                document2.printStackTrace();
                                                Mcontext.runOnUiThread(() -> iUtils.ShowToastError(Mcontext,
                                                        Mcontext.getResources().getString(R.string.somthing)
                                                ));
                                            }

                                            return;
                                        }
                                        return;
                                    }

                                    Mcontext.runOnUiThread(() -> {
                                        iUtils.ShowToastError(Mcontext,
                                                Mcontext.getResources().getString(R.string.somthing)
                                        );
                                    });
                                } catch (Exception unused) {
                                    System.out.println("myresponseis111 exp " + unused.getMessage());

                                    dismissMyDialog();
                                    Mcontext.runOnUiThread(() -> iUtils.ShowToastError(Mcontext,
                                            Mcontext.getResources().getString(R.string.somthing)
                                    ));
                                }

                            }

                            @Override
                            public void onError(ANError error) {
                                error.printStackTrace();
                                System.out.println("Apidata error " + error.getMessage());

                            }
                        });

            }
            else if (url.contains("boloindya.com")) {
                if (!iUtils.isSocialMediaOn("boloindya.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                new CallBoloindyaData().execute(url);
            }
            else if (url.contains("chingari")) {
                if (!iUtils.isSocialMediaOn("chingari")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CallchingariData(url);
            }
            else if (url.contains("dubsmash")) {
                if (!iUtils.isSocialMediaOn("dubsmash")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                new CalldubsmashData().execute(url);
            }
            else if (url.contains("bittube")) {
                if (!iUtils.isSocialMediaOn("bittube")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                String myurlis1 = url;
                if (myurlis1.contains(".tv")) {
                    String str = "/";
                    myurlis1 = myurlis1.split(str)[myurlis1.split(str).length - 1];
                    myurlis1 = "https://bittube.video/videos/watch/" +
                            myurlis1;
                }
                new CallgdriveData().execute(myurlis1);

            }
            else if (url.contains("drive.google.com") ||
                    url.contains("mp4upload") ||

                    url.contains("ok.ru") ||

                    url.contains("mediafire") ||
                    url.contains("gphoto") ||
                    url.contains("uptostream") ||

                    url.contains("fembed") ||
                    url.contains("cocoscope") ||
                    url.contains("sendvid") ||

                    url.contains("vivo") ||
                    url.contains("4shared")) {
                if (!iUtils.isSocialMediaOn("drive.google.com") ||
                        !iUtils.isSocialMediaOn("mp4upload") ||

                        !iUtils.isSocialMediaOn("ok.ru") ||

                        !iUtils.isSocialMediaOn("mediafire") ||
                        !iUtils.isSocialMediaOn("gphoto") ||
                        !iUtils.isSocialMediaOn("uptostream") ||

                        !iUtils.isSocialMediaOn("fembed") ||
                        !iUtils.isSocialMediaOn("cocoscope") ||
                        !iUtils.isSocialMediaOn("sendvid") ||

                        !iUtils.isSocialMediaOn("vivo") ||
                        !iUtils.isSocialMediaOn("fourShared")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                new CallgdriveData().execute(url);
            }
            else if (url.contains("hind")) {
                if (!iUtils.isSocialMediaOn("hind")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                new CallhindData().execute(url);
            }
            else if (url.contains("audioboom")) {
                if (!iUtils.isSocialMediaOn("audioboom")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                //                AudioboomDownloader audioboomDownloader = new AudioboomDownloader(Mcontext, url);
                //                audioboomDownloader.DownloadVideo();

                CalldlApisDataData(url, true);

            }
            else if (url.contains("zingmp3.vn")) {
                if (!iUtils.isSocialMediaOn("zingmp3.vn")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                CalldlApisDataData(url, true);

            }
            else if (url.contains("twitter.com")) {
                if (!iUtils.isSocialMediaOn("twitter.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                TwitterVideoDownloader downloader = new TwitterVideoDownloader(Mcontext, url);
                downloader.DownloadVideo();
                //  CallDailymotionData(url, true);
                //CalldlApisDataData(url, true);

            }
            //new
            //working
            else if (url.contains("gag.com")) {
                if (!iUtils.isSocialMediaOn("gag.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                //            https://9gag.com/gag/aXowVXz
                CalldlApisDataData(url, false);
                //new Call9gagData().execute(url);

            }
            else if (url.contains("buzzfeed.com")) {
                if (!iUtils.isSocialMediaOn("buzzfeed.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                dismissMyDialogErrortoast();
            }
            else if (url.contains("redd.it") || url.contains("reddit")) {

                if (!iUtils.isSocialMediaOn("redd.it") || !iUtils.isSocialMediaOn("reddit")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, true);
                // new CallRedditData().execute("https://redditsave.com/info?url=" + url);
                //  CallREditData(url, true);

            }
            else if (url.contains("mxtakatak")) {

                if (!iUtils.isSocialMediaOn("mxtakatak")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                String finalUrl3 = url;
                if (finalUrl3.contains("share.mxtakatak.com")) {
                    new Thread(() -> {
                        Looper.prepare();
                        HttpURLConnection con;
                        try {
                            con = (HttpURLConnection) (new URL(finalUrl3).openConnection());

                            con.setInstanceFollowRedirects(false);
                            con.connect();
                            int responseCode = con.getResponseCode();
                            System.out.println(responseCode);
                            String location = con.getHeaderField("Location");
                            System.out.println(location);

                            if (location != null && !location.equals("") && location.contains("https://www.mxtakatak.com/")) {

                                String urls = location.split("/")[5];
                                urls = urls.substring(0, urls.indexOf("?"));

                                String newuuu = "https://mxshorts.akamaized.net/video/" + urls + "/download/1/h264_high_720.mp4";

                                String nametitle = "Mxtaktak_" +
                                        System.currentTimeMillis();

                                DownloadFileMain.startDownloading(Mcontext, newuuu, nametitle, ".mp4");

                            }

                            dismissMyDialog();

                        } catch (Exception e) {
                            dismissMyDialogErrortoast();
                        }
                    }).start();
                } else {
                    try {

                        String urls = finalUrl3.split("/")[5];
                        urls = urls.substring(0, urls.indexOf("?"));

                        String newuuu = "https://mxshorts.akamaized.net/video/" + urls + "/download/1/h264_high_720.mp4";

                        String nametitle = "Mxtaktak_" +
                                System.currentTimeMillis();
                        dismissMyDialog();
                        DownloadFileMain.startDownloading(Mcontext, newuuu, nametitle, ".mp4");

                    } catch (Exception e) {
                        dismissMyDialogErrortoast();
                    }
                }

                //  new CallmxtaktakData().execute(url);

                //            AndroidNetworking.post("http://mxtakatakvideodownloader.shivjagar.co.in/MXTakatak-service.php")
                //                    .addBodyParameter("url", url)
                //                    .setPriority(Priority.MEDIUM)
                //                    .build()
                //                    .getAsJSONObject(new JSONObjectRequestListener() {
                //                        @Override
                //                        public void onResponse(JSONObject response) {
                //
                //                            dismissMyDialog();
                //                            String matag;
                //                            try {
                //
                //                                JSONObject jsonObject = new JSONObject(response.toString());
                //
                //                                matag = jsonObject.getString("videourl");
                //                                System.out.println("wojfdjhfdjh " + matag);
                //                                DownloadFileMain.startDownloading(context, matag, "Mxtakatak_" + System.currentTimeMillis(), ".mp4");
                //
                //
                //                            } catch (Exception e) {
                //                                matag = "";
                //
                //                                e.printStackTrace();
                //                               dismissMyDialog();
                //                            }
                //
                //
                //                        }
                //
                //                        @Override/**/
                //                        public void onError(ANError error) {
                //                           dismissMyDialog();
                //                        }
                //                    });

            }
            else if (url.contains("test.com")) {

                new CallgaanaData().execute(url);
                // CallsoundData(url, false);
                //  CalldlApisDataData(url, true);

            }
            else if (url.contains("gaana")) {
                if (!iUtils.isSocialMediaOn("gaana")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                String finalUrl = url;
                new Thread(() -> {
                    HttpURLConnection con;
                    try {
                        con = (HttpURLConnection) (new URL("https://tinyurl.com/f67p797b").openConnection());

                        con.setInstanceFollowRedirects(false);
                        con.connect();
                        int responseCode = con.getResponseCode();
                        System.out.println(responseCode);
                        String location = con.getHeaderField("Location");
                        System.out.println(location);

                        AndroidNetworking.post(location)
                                .addBodyParameter("url", finalUrl)
                                .addBodyParameter("weburl", "https://video.infusiblecoder.com/")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        dismissMyDialog();
                                        String matag;
                                        try {

                                            JSONObject jsonObject = new JSONObject(response.toString());

                                            matag = jsonObject.getJSONArray("songlinks").getJSONObject(0).getString("songurl");
                                            System.out.println("wojfdjhfdjh " + matag);
                                            DownloadFileMain.startDownloading(context, matag, "Gaana_" + System.currentTimeMillis(), ".mp3");

                                        } catch (Exception e) {

                                            e.printStackTrace();
                                            dismissMyDialog();
                                        }

                                    }

                                    @Override /**/
                                    public void onError(ANError error) {

                                        System.out.println("wojfdjhfdjh error = " + error.getMessage());

                                        dismissMyDialog();
                                    }
                                });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }
            else if (url.contains("douyin")) {
                if (!iUtils.isSocialMediaOn("douyin")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                //https://v.douyin.com/j8kDWWX/
                //https://www.douyin.com/video/7120185466233556260

                String finalUrl = url;

                new Thread(() -> {
                    HttpURLConnection con;
                    try {
                        String[] idis = new String[0];
                        if (!finalUrl.contains("/video/")) {
                            con = (HttpURLConnection) (new URL(finalUrl).openConnection());

                            con.setInstanceFollowRedirects(false);
                            con.connect();
                            int responseCode = con.getResponseCode();
                            System.out.println(responseCode);
                            String location = con.getHeaderField("Location");
                            System.out.println(location);

                            if (location != null && location.contains("?")) {
                                location = location.split("\\?")[0];
                            }
                            if (location != null && location.contains("/video/")) {
                                idis = location.split("/");
                            }
                            System.out.println(idis[idis.length - 1]);

                        } else {
                            idis = finalUrl.split("/");
                        }

                        AndroidNetworking.get("https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=" + idis[idis.length - 1])
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        dismissMyDialog();
                                        String matag;
                                        try {

                                            //                                $video_info["item_list"][0]["video"]["play_addr"]["url_list"][0];
                                            JSONObject jsonObject = new JSONObject(response.toString());

                                            JSONArray itemlist = jsonObject.getJSONArray("item_list");
                                            matag = itemlist.getJSONObject(0).getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").getString(0);

                                            matag = matag.replace("playwm", "play");
                                            System.out.println("wojfdjhfdjh " + matag);
                                            DownloadFileMain.startDownloading(context, matag, "Douyin_" + System.currentTimeMillis(), ".mp4");

                                        } catch (Exception e) {

                                            e.printStackTrace();
                                            dismissMyDialog();
                                        }

                                    }

                                    @Override /**/
                                    public void onError(ANError error) {
                                        dismissMyDialogErrortoast();
                                    }
                                });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }
            else if (url.contains("dailymotion") || url.contains("dai.ly")) {

                if (!iUtils.isSocialMediaOn("dailymotion") && !iUtils.isSocialMediaOn("dai.ly")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                DailyMotionDownloader mashableDownloader = new DailyMotionDownloader(Mcontext, url);
                mashableDownloader.DownloadVideo();
                Log.i("DailyMotionDownloader", "work 0");

                String mnnn = DailyMotionDownloader.getVideoId(url);
                Log.i("DailyMotionDownloader", "work 2 " + mnnn);
                //
                //                AndroidNetworking.get("https://api.1qvid.com/api/v1/videoInfo?videoId=" + mnnn + "&host=dailymotion")
                //                        //    .addHeaders("User-Agent", "Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36")
                //                        .setPriority(Priority.MEDIUM)
                //                        .build()
                //                        .getAsJSONArray(new JSONArrayRequestListener() {
                //                            @Override
                //                            public void onResponse(JSONArray response) {
                //                                Log.i("DailyMotionDownloader", "work 2");
                //
                //                                dismissMyDialog();
                //                                try {
                //
                //
                //                                    JSONArray jsonArray12 = new JSONArray(response.toString());
                //                                    JSONArray jsonArraye = jsonArray12.getJSONObject(0).getJSONArray("formats");
                //
                //                                    ArrayList<String> urls = new ArrayList<>();
                //                                    ArrayList<String> qualities = new ArrayList<>();
                //
                //
                //                                    for (int i = 0; i < jsonArraye.length(); i++) {
                //
                //                                        String urlis = jsonArraye.getJSONObject(i).getString("url");
                //                                        urls.add(urlis);
                //                                        String resolutionis = jsonArraye.getJSONObject(i).getString("formatNote");
                //                                        qualities.add(resolutionis);
                //
                //
                //                                    }
                //
                //                                    String[] arr = new String[qualities.size()];
                //                                    arr = qualities.toArray(arr);
                //
                //                                    new AlertDialog.Builder(Mcontext).setTitle("Quality!").setItems(arr, (dialogInterface, i) -> DownloadFileMain.startDownloading(Mcontext, urls.get(i), "Dailymotion_" + System.currentTimeMillis(), ".mp4")).setPositiveButton("OK", (dialogInterface, i) -> dismissMyDialog()).setCancelable(false).show();
                //
                //
                //                                } catch (Exception e) {
                //                                    Log.i("DailyMotionDownloader", "work 3" + e.getLocalizedMessage());
                //
                //                                }
                //                            }
                //
                //                            @Override
                //                            public void onError(ANError anError) {
                //                                dismissMyDialogErrortoast();
                //                                Log.i("DailyMotionDownloader", "work 4" + anError.getLocalizedMessage());
                //
                //                            }
                //                        });

            }
            else if (url.contains("imdb.com")) {
                if (!iUtils.isSocialMediaOn("imdb.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                // CalldlApisDataData(url, false);
                new CallIMDBData().execute(url);

            }
            else if (url.contains("camdemy")) {
                if (!iUtils.isSocialMediaOn("camdemy")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                CalldlApisDataData(url, false);

            }
            else if (url.contains("pinterest") || url.contains("pin.it")) {

                if (!iUtils.isSocialMediaOn("pinterest") && !iUtils.isSocialMediaOn("pin.it")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }

                System.out.println("sjdfjsdj 56 " + url);

                String finalUrl4 = url;
                new Thread(() -> {
                    try {
                        Looper.prepare();
                        Document document;
                        try {
                            String location = "";
                            if (finalUrl4.contains("pin.it")) {

                                String newsmallurl = "https://api.pinterest.com/url_shortener/" + finalUrl4.split("/")[3] + "/redirect/";

                                HttpURLConnection con = (HttpURLConnection) (new URL(newsmallurl).openConnection());

                                con.setInstanceFollowRedirects(false);
                                con.connect();
                                int responseCode = con.getResponseCode();
                                System.out.println(responseCode);
                                location = con.getHeaderField("Location");
                            } else {
                                location = finalUrl4;
                            }

                            if (location.contains("?")) {
                                System.out.println("myresponseis111 https://www.pinterest.com/pin/" + location.split("/")[4]);

                                document = Jsoup.connect("https://www.pinterest.com/pin/" + location.split("/")[4]).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36").get();

                            } else {
                                document = Jsoup.connect(location).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36").get();
                                System.out.println("myresponseis111 " + location);

                            }
                            System.out.println("myresponseis111 exp166 " + document);

                        } catch (Exception e) {
                            document = Jsoup.connect(finalUrl4).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36").get();

                        }
                        String data = "";

                        Elements elements = document.select("script");
                        for (Element element : elements) {
                            if (element.attr("id").equals("__PWS_DATA__")) {
                                //Save As you want to

                                System.out.println("myresponseis111 list_of_qualities" + element.html());
                                List<String> stringList = iUtils.extractUrls(element.html());
                                System.out.println("myresponseis111 lgfgf " + stringList.size());
                                TreeSet treeSet = new TreeSet();
                                for (int i = 0; i < stringList.size(); i++) {
                                    if (stringList.get(i).endsWith(".mp4")) {
                                        treeSet.add(stringList.get(i));
                                        System.out.println("myresponseis111 urlsnap  " + i + " " + stringList.get(i));

                                    }
                                }

                                List<String> stringListModified = new ArrayList<>(treeSet);
                                System.out.println("myresponseis111 lgfgf " + stringListModified.size());

                                DownloadFileMain.startDownloading(Mcontext, stringListModified.get(0), "Pinterest_" + System.currentTimeMillis(), ".mp4");

                            }
                        }
                        dismissMyDialog();

                    } catch (Throwable e) {
                        e.printStackTrace();
                        dismissMyDialogErrortoast();
                    }
                }).start();

            }
            else if (url.contains("tumblr.com")) {
                if (!iUtils.isSocialMediaOn("tumblr.com")) {
                    dismissMyDialogErrorToastForBlockedWebsitePanel();
                    return;
                }
                // new CalltumblerData().execute(url);
                CalldlApisDataData(url, true);
            }

            //TODO Till Here
            else {

                CalldlApisDataData(url, true);

                //dismissMyDialogErrortoast();
            }

            prefs = Mcontext.getSharedPreferences("AppConfig", MODE_PRIVATE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void getTrillerData(String url1) {

        //            if (url1.contains("v.triller")) {
        //
        //            } else {
        //
        //                newurl = "https://social.triller.co/v1.5/api/videos/" + url1.substring(url1.indexOf("/video/") + 7);
        //
        //            }
        new Thread(() -> {
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) (new URL(url1).openConnection());

                con.setInstanceFollowRedirects(false);
                con.connect();
                int responseCode = con.getResponseCode();
                System.out.println(responseCode);
                String location = con.getHeaderField("Location");
                System.out.println(location);

                newurl = "https://social.triller.co/v1.5/api/videos/" + location.substring(location.indexOf("/video/") + 7);

                System.out.println("mydnewurlis=" + newurl);

                AndroidNetworking.get(newurl)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("mydnewurlis res =" + response);

                                dismissMyDialog();
                                String matag;
                                try {

                                    matag = response.getJSONArray("videos").getJSONObject(0).getString("video_url");

                                    DownloadFileMain.startDownloading(Mcontext, matag, "Triller_" + System.currentTimeMillis(), ".mp4");

                                } catch (Exception e) {

                                    e.printStackTrace();
                                    dismissMyDialog();
                                }

                            }

                            @Override /**/
                            public void onError(ANError error) {

                                System.out.println("wojfdjhfdjh error = " + error.getMessage());

                                dismissMyDialog();
                            }
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private static void CallchingariData(String url) {
        try {
            String[] urlstr = url.split("=");

            AndroidNetworking.get("https://api.chingari.io/post/post_details/" + urlstr[1])
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            dismissMyDialog();
                            String matag = "";
                            try {
                                System.out.println("fjhjfhjsdfsdhf " + response);

                                JSONObject jsonObject = new JSONObject(response.toString());

                                JSONObject transcode = jsonObject.getJSONObject("data").getJSONObject("mediaLocation").getJSONObject("transcoded");

                                if (transcode.has("p1024")) {
                                    matag = transcode.getString("p1024");
                                } else if (transcode.has("p720")) {
                                    matag = transcode.getString("p720");

                                } else if (transcode.has("p480")) {
                                    matag = transcode.getString("p480");

                                }
                                matag = "https://media.chingari.io" + matag;
                                System.out.println("wojfdjhfdjh " + matag);
                                DownloadFileMain.startDownloading(Mcontext, matag, "Chingari_" + System.currentTimeMillis(), ".mp4");

                            } catch (Exception e) {
                                dismissMyDialog();
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError error) {
                            dismissMyDialog();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showdownload_progress(int timeelipsed, long filesize) {
        Thread thread = new Thread() {
            @Override
            public void run() {

                String filesize2 = iUtils.getStringSizeLengthFile(filesize);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Mcontext);

                String contentTitle = "Downloaded";
                Intent notifyIntent = new Intent();
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(Mcontext, DOWNLOAD_NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notificationBuilder = createNotificationBuilder("downloader_channel");
                notificationBuilder.setContentIntent(notifyPendingIntent);
                notificationBuilder.setTicker("Start downloading from the server");
                notificationBuilder.setOngoing(true);
                notificationBuilder.setAutoCancel(false);
                notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
                notificationBuilder.setContentTitle(contentTitle);
                //   notificationBuilder.setContentText("0%");
                //   notificationBuilder.setProgress(100, 0, false);
                notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());

                long total = 0;
                int count, tmpPercentage = 0;

                System.out.println("mobile-ffmpegusa prog :" + "time elipsed: " + iUtils.formatDuration(timeelipsed) + " downloaded : " + filesize2 + "%");
                notificationBuilder.setContentText("time elipsed: " + iUtils.formatDuration(timeelipsed) + " downloaded : " + filesize2);
                notificationBuilder.setProgress(100, timeelipsed, false);
                notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());

                notificationBuilder.setContentTitle(contentTitle);
                notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                notificationBuilder.setOngoing(false);
                notificationBuilder.setAutoCancel(true);
                //notificationBuilder.setContentText("0");
                // notificationBuilder.setProgress(0, 0, false);
                notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());

            }
        };

        thread.start();
    }

    private static NotificationCompat.Builder createNotificationBuilder(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = Mcontext.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = (NotificationManager) Mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        return new NotificationCompat.Builder(Mcontext, channelId);
    }

    public static void dismissMyDialog() {

        if (pd != null && pd.isShowing() && !fromService && !((Activity) Mcontext).isFinishing()) {
            pd.dismiss();
        }
    }

    public static void dismissMyDialogErrortoast() {

        if (pd != null && pd.isShowing() && !fromService && !((Activity) Mcontext).isFinishing()) {
            pd.dismiss();
            Mcontext.runOnUiThread(() -> {
                iUtils.ShowToastError(Mcontext,
                        Mcontext.getResources().getString(R.string.somthing)
                );
            });
        }
    }

    public static void dismissMyDialogErrorToastForBlockedWebsitePanel() {

        if (pd != null && pd.isShowing() && !fromService && !((Activity) Mcontext).isFinishing()) {
            pd.dismiss();
            Mcontext.runOnUiThread(() -> {
                iUtils.ShowToast(Mcontext,
                        Mcontext.getResources().getString(R.string.somthing_webiste_panele_block)
                );
            });
        }
    }

    public static void download(String url12) {
        String readLine;
        URL url = null;
        try {
            url = new URL(url12);

            Log.d("ThumbnailURL11111_1 ", url12);

            //        URLConnection openConnection = url.openConnection();
            //        openConnection.setRequestProperty("ModelUserInstagram-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            //       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));

            URL url1 = new URL(url12);
            URLConnection connection = url1.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            while ((readLine = bufferedReader.readLine()) != null) {
                //  readLine = bufferedReader.readLine();
                Log.d("ThumbnailURL11111_2  ", readLine);

                readLine = readLine.substring(readLine.indexOf("VideoObject"));
                String substring = readLine.substring(readLine.indexOf("thumbnailUrl") + 16);
                substring = substring.substring(0, substring.indexOf("\""));

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ThumbnailURL: ");
                stringBuilder.append(substring);

                Log.d("ThumbnailURL", substring);
                readLine = readLine.substring(readLine.indexOf("contentUrl") + 13);
                readLine = readLine.substring(0, readLine.indexOf("?"));
                stringBuilder = new StringBuilder();
                stringBuilder.append("ContentURL: ");
                stringBuilder.append(readLine);

                Log.d("ContentURL1111 thumb  ", substring);
                Log.d("ContentURL1111", stringBuilder.toString());

                if (readLine == null) {
                    break;
                }
            }
            bufferedReader.close();

        } catch (Exception e) {
            //            Log.d("ContentURL1111 errrr", e.getMessage());
            e.printStackTrace();
        }
        // new downloadFile().Downloading(Mcontext, URL, Title, ".mp4");
        //   new DownloadFileFromURL().execute(new String[]{readLine});
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void addButtonToMainLayouttest_allvideo(final String videoTitle, String ytfile, String video_title) {
        try {

            // Display some buttons and let the user choose the format
            Button btn = new Button(Mcontext);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8);
            btn.setLayoutParams(params);

            btn.setBackground(Mcontext.getResources().getDrawable(R.drawable.rect_round_gradient_12));
            btn.setTextColor(Color.WHITE);

            btn.setText(videoTitle);
            btn.setOnClickListener(v -> {

                if (windowManager2 != null) {
                    try {
                        windowManager2.removeView(mChatHeadView);
                    } catch (Exception e) {
                        Log.i("LOGClipboard111111", "error is " + e.getMessage());

                    }
                }

                if (videoTitle.equals("audio/mp4")) {
                    DownloadFileMain.startDownloading(Mcontext, ytfile, video_title + "_" + videoTitle, ".mp3");
                } else {
                    DownloadFileMain.startDownloading(Mcontext, ytfile, video_title + "_" + videoTitle, ".mp4");

                }
                dialog_quality_allvids.dismiss();
            });
            mainLayout.addView(btn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callSnackVideoResult(String URL, String shortKey, String os, String sig, String client_key) {

        RetrofitApiInterface apiService = RetrofitClient.getClient();

        Call<JsonObject> callResult = apiService.getsnackvideoresult(URL + "&" + shortKey + "&" + os + "&sig=" + sig + "&" + client_key);

        callResult.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {

                assert response.body() != null;
                VideoUrl = response.body().getAsJsonObject("photo").get("main_mv_urls").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();

                System.out.println("response1122334455worURL:   " + VideoUrl);

                if (!VideoUrl.equals("")) {

                    try {

                        dismissMyDialog();

                        String myurldocument = VideoUrl;

                        String nametitle = "snackvideo_" +
                                System.currentTimeMillis();

                        DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        document2.printStackTrace();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                System.out.println("response1122334455:   " + "Failed0 " + call);

                dismissMyDialog();

            }
        });

    }


    private static String[] splitDataToVideoAndAudio_dlapiformat(ArrayList<VideoFormat> videoList, RecyclerView recyclerView_video, RecyclerView recyclerView_audio, QualityBottomsheetAdapter qualityBottomsheetAdapter, String extractor, String title, String id, String url) {

        List<VideoFormat> videoList_sub = new ArrayList<>();
        List<VideoFormat> videoList_sub_video = new ArrayList<>();
        for (int i = 0; i < videoList.size(); i++) {


            System.out.println("fhnsdkjhffjhsfhsdfjksdfsdhfkjs vifro= " + videoList.get(i).getExt());

            String myext = videoList.get(i).getExt();
            if (myext.equals("m4a") ||
                    myext.equals("mp3") ||
                    myext.equals("wav") || (myext.equals("webm") && !videoList.get(i).getAcodec().equals("none"))) {
                videoList_sub.add(videoList.get(i));
            } else if (myext.equals("mp4") || myext.equals("mpeg") || (myext.equals("webm") && videoList.get(i).getAcodec().equals("none"))) {

                videoList_sub_video.add(videoList.get(i));

            }

        }

        Collections.reverse(videoList_sub_video);

        System.out.println("fhnsdkjhffjhsfhsdfjksdfsdhfkjs cideos list = " + videoList_sub_video.size());
        System.out.println("fhnsdkjhffjhsfhsdfjksdfsdhfkjs audios list = " + videoList_sub.size());

        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, extractor, false, videoList_sub_video, true, title, id, url);
        recyclerView_video.setAdapter(qualityBottomsheetAdapter);

        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, extractor, false, videoList_sub, true, title, id, url);
        recyclerView_audio.setAdapter(qualityBottomsheetAdapter);
        String[] dataSize = {"n", "n"};
        if (videoList_sub_video.size() > 0) {
            dataSize[0] = "y";
        }

        if (videoList_sub.size() > 0) {
            dataSize[1] = "y";
        }
        return dataSize;
    }


    private static void splitDataToVideoAndAudio_video(List<Video> videoList, RecyclerView recyclerView_video, RecyclerView recyclerView_audio, QualityBottomsheetAdapter qualityBottomsheetAdapter, String extractor) {

        List<Video> videoList_sub = new ArrayList<>();
        List<Video> videoList_sub_video = new ArrayList<>();
        for (int i = 0; i < videoList.size(); i++) {

            if (videoList.get(i).getProtocol().contains("http") && !videoList.get(i).getProtocol().contains("http_dash_segments") && !videoList.get(i).getURL().contains(".m3u8") && !videoList.get(i).getProtocol().contains("m3u8_native")) {

                System.out.println("fhnsdkjhffjhsfhsdfjksdfsdhfkjs vifro= " + videoList.get(i).getURL());

                if (videoList.get(i).getEXT().equals("m4a") ||
                        videoList.get(i).getEXT().equals("mp3") ||
                        videoList.get(i).getEXT().equals("wav")) {
                    videoList_sub.add(videoList.get(i));
                } else if (videoList.get(i).getEXT().equals("mp4") || videoList.get(i).getEXT().equals("mpeg")) {

                    videoList_sub_video.add(videoList.get(i));

                }
            }
        }

        Collections.reverse(videoList_sub_video);

        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, videoList_sub_video.get(0).getExtractor(), false, videoList_sub_video, true);
        recyclerView_video.setAdapter(qualityBottomsheetAdapter);

        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, videoList_sub.get(0).getExtractor(), false, videoList_sub, true);
        recyclerView_audio.setAdapter(qualityBottomsheetAdapter);

    }

    private static void splitDataToVideoAndAudio_format(List<Format> formatList, RecyclerView recyclerView_video, RecyclerView recyclerView_audio, QualityBottomsheetAdapter qualityBottomsheetAdapter, String extractor) {

        List<Format> formatList_sub = new ArrayList<>();
        List<Format> formatList_sub_video = new ArrayList<>();
        for (int i = 0; i < formatList.size(); i++) {

            if (formatList.get(i).getProtocol().contains("http") && !formatList.get(i).getProtocol().contains("http_dash_segments") && !formatList.get(i).getURL().contains(".m3u8") && !formatList.get(i).getProtocol().contains("m3u8_native")) {

                if (formatList.get(i).getAcodec() != null && !formatList.get(i).getAcodec().equals("none")) {

                    if (formatList.get(i).getEXT().equals("m4a") ||
                            formatList.get(i).getEXT().equals("mp3") ||
                            formatList.get(i).getEXT().equals("wav")) {
                        formatList_sub.add(formatList.get(i));
                    } else if (formatList.get(i).getEXT().equals("mp4") || formatList.get(i).getEXT().equals("unknown_video") || formatList.get(i).getEXT().equals("mpeg")) {

                        formatList_sub_video.add(formatList.get(i));

                    }
                } else {
                    System.out.println("reccccc VVKK 8 " + formatList.get(i).getEXT());

                    if (formatList.get(i).getEXT().equals("m4a") ||
                            formatList.get(i).getEXT().equals("mp3") ||
                            formatList.get(i).getEXT().equals("wav")) {
                        formatList_sub.add(formatList.get(i));
                    } else if (formatList.get(i).getEXT().equals("mp4") || formatList.get(i).getEXT().equals("unknown_video") || formatList.get(i).getEXT().equals("mpeg")) {
                        System.out.println("reccccc VVKK 9 " + formatList.get(i).getURL());

                        formatList_sub_video.add(formatList.get(i));

                    }

                    //                   formatList.get(i).setFormat("(no audio) " + formatList.get(i).getFormat());
                    formatList.get(i).setFormat("" + formatList.get(i).getFormat());

                }

            }
        }

        Collections.reverse(formatList_sub_video);

        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, formatList_sub_video, extractor, false);
        recyclerView_video.setAdapter(qualityBottomsheetAdapter);

        qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, formatList_sub, extractor, false);
        recyclerView_audio.setAdapter(qualityBottomsheetAdapter);

    }

    @Keep
    public static void CalldlApisDataData(String url, boolean hasQualityOption) {

        System.out.println("reccc " + url);
        callDlApiNew(url);

//        AndroidNetworking.get(DlApisUrl + url + "&flatten=True")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        System.out.println("reccccc VVKK0 " + response);
//                        // Log.e("myresponse ",response.toString());
//
//                        parseCalldlApisDataData(response, url, false);
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        System.out.println("reccccc VVKK0 error " + error.getErrorBody());
//                        callBackup(url);
//
//                    }
//                });

    }

    static void callM3u8Backup(String url) {
        AndroidNetworking.get("https://api.m3u8-downloader.com/api/parse?url=" + url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("reccccc VVKK1 " + response);
                        // Log.e("myresponse ",response.toString());

                        try {
                            JSONObject obj = new JSONObject();
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(0, response);
                            obj.put("url", "" + url);
                            obj.put("videos", jsonArray);
                            parseCalldlApisDataData(obj, url, true);
                        } catch (Exception e) {
                            dismissMyDialogErrortoast();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println("reccccc VVKK1 error " + error.getErrorBody());

                        dismissMyDialogErrortoast();

                    }
                });
    }


    static void callBackup(String url) {
        AndroidNetworking.get("http://199.192.20.82:9191/api/info?url=" + url + "&flatten=True")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("reccccc VVKK1 " + response);
                        // Log.e("myresponse ",response.toString());
                        parseCalldlApisDataData(response, url, true);
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println("reccccc VVKK1 error " + error.getErrorBody());

                        dismissMyDialogErrortoast();

                    }
                });
    }

    static void callDlApiNew(String url) {


//        ExecutorService executor = Executors.newSingleThreadExecutor();
//
//        Future<VideoInfo> futureResult = executor.submit(() -> {
//            // Perform some long running task here
//            // ...
//
//            // Return the result of the task
//            return YoutubeDL.getInstance().getInfo(url);
//        });
//
//// Shutdown the executor when done
//        executor.shutdown();
//
//
//            try {
//                VideoInfo result = futureResult.get();
//                System.out.println("Task result: " + result.getTitle());
//
//
//
//                parseCalldlApisLocalDataData(result, url);
//
//
//            } catch (Exception e) {
//                dismissMyDialogErrortoast();
//                e.printStackTrace();
//            }


        Executors.newSingleThreadExecutor().submit(() -> {

            try {

                streamInfo = YoutubeDL.getInstance().getInfo(url);

                Mcontext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseCalldlApisLocalDataData(streamInfo, url);

                    }
                });

            } catch (Throwable e) {
                dismissMyDialogErrortoast();

                System.err.println("YTDLP ERROR" + e.getLocalizedMessage());

            }
        });

    }

    @Keep
    public static void parseCalldlApisDataData(JSONObject response, String url, boolean isFromBackup) {
        try {

            try {
                if (!response.getString("error").equals("")) {
                    if (isFromBackup) {
                        dismissMyDialogErrortoast();
                    } else {
                        callBackup(url);
                    }
                }

            } catch (Exception e) {

                Gson gson = new Gson();

                DLDataParser gsonObj = gson.fromJson(response.toString(), DLDataParser.class);

                System.out.println("reccccc VVKK " + gsonObj.getURL());

                View view = LayoutInflater.from(Mcontext).inflate(R.layout.bottomsheet_quality_layout, null);

                Button btncancel_bottomsheet = view.findViewById(R.id.btncancel_bottomsheet);
                Button btnopen_bottomsheet = view.findViewById(R.id.btnopen_bottomsheet);
                TextView source_bottomsheet = view.findViewById(R.id.source_bottomsheet);
                TextView title_bottomsheet = view.findViewById(R.id.bottomsheet_title);
                TextView duration_bottomsheet = view.findViewById(R.id.bottomsheet_duration);
                ImageView thumb_bottomsheet = view.findViewById(R.id.bottomsheet_thumbnail);

                RecyclerView recyclerView = view.findViewById(R.id.recqualitybottomsheet);

                recyclerView.setLayoutManager(new LinearLayoutManager(Mcontext));

                RecyclerView recyclerView_audio = view.findViewById(R.id.recqualitybottomsheet_aud);
                recyclerView_audio.setHasFixedSize(true);
                recyclerView_audio.setLayoutManager(new LinearLayoutManager(Mcontext));

                QualityBottomsheetAdapter qualityBottomsheetAdapter = null;

                System.out.println("reccc lengthe iss= " + response.getJSONArray("videos").length());

                if (response.getJSONArray("videos").length() > 1) {

                    System.out.println("reccccc VVKK 0 ");

                    System.out.println("reccccc VVKK 1 ");

                    if (response.getJSONArray("videos").getJSONObject(0).has("protocol")) {

                        System.out.println("reccccc VVKK 2");

                        System.out.println("reccccc VVKK 3 ");

                        splitDataToVideoAndAudio_video(gsonObj.getVideos(), recyclerView, recyclerView_audio, qualityBottomsheetAdapter, gsonObj.getVideos().get(0).getExtractor());

                    }

                    BottomSheetDialog dialog = new BottomSheetDialog(Mcontext);

                    if (response.getJSONArray("videos").getJSONObject(0).has("extractor")) {
                        String styledText = "";

                        switch (gsonObj.getVideos().get(0).getExtractor()) {
                            case "youtube": {
                                styledText = "Source: <font color='red'>" + "YTD" + "</font>";

                                break;
                            }
                            case "soundcloud": {
                                styledText = "Source: <font color='red'>" + "SCloud" + "</font>";

                                break;
                            }
                            case "facebook": {
                                styledText = "Source: <font color='red'>" + "SocialF" + "</font>";

                                break;
                            }

                            default: {
                                styledText = "Source: <font color='red'>" + gsonObj.getVideos().get(0).getExtractor() + "</font>";

                                break;
                            }

                        }
                        source_bottomsheet.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                    }

                    if (response.getJSONArray("videos").getJSONObject(0).has("duration")) {

                        String mystring = gsonObj.getVideos().get(0).getDuration() + "";
                        String[] correctstring = mystring.split("\\.");

                        long hours = Long.parseLong(correctstring[0]) / 3600;
                        long minutes = (Long.parseLong(correctstring[0]) % 3600) / 60;
                        long seconds = Long.parseLong(correctstring[0]) % 60;

                        String DurationstyledText = "Duration: <font color='red'>" + String.format("%02d:%02d:%02d", hours, minutes, seconds) + "</font>";
                        duration_bottomsheet.setText(Html.fromHtml(DurationstyledText), TextView.BufferType.SPANNABLE);
                    }

                    if (response.getJSONArray("videos").getJSONObject(0).has("title")) {

                        System.out.println("reccccc VVKKtttt " + gsonObj.getVideos().get(0).getTitle());

                        String titletyledText = "Title: <font color='red'>" + String.format("%s", gsonObj.getVideos().get(0).getTitle()) + "</font>";
                        title_bottomsheet.setText(Html.fromHtml(titletyledText), TextView.BufferType.SPANNABLE);
                    }

                    if (response.getJSONArray("videos").getJSONObject(0).has("thumbnail")) {

                        Glide.with(Mcontext)
                                .load(gsonObj.getVideos().get(0).getThumbnail())
                                .into(thumb_bottomsheet);

                    }
                    // source_bottomsheet.setText(String.format("Source: %s", gsonObj.getVideos().get(0).getExtractor()));
                    btncancel_bottomsheet.setOnClickListener(v -> dialog.cancel());

                    btnopen_bottomsheet.setOnClickListener(v -> {
                        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
                        dialog.getBehavior().setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
                    });

                    dialog.setContentView(view);
                    dialog.show();

                    dismissMyDialog();

                } else {

                    System.out.println("reccccc VVKK 6 ");

                    if (response.getJSONArray("videos").getJSONObject(0).has("formats")) {
                        System.out.println("reccccc VVKK 7 ");

                        splitDataToVideoAndAudio_format(gsonObj.getVideos().get(0).getFormats(), recyclerView, recyclerView_audio, qualityBottomsheetAdapter, gsonObj.getVideos().get(0).getExtractor());

                    } else {
                        if (response.getJSONArray("videos").getJSONObject(0).has("protocol")) {

                            String ishttp = response.getJSONArray("videos").getJSONObject(0).getString("protocol");
                            if (ishttp.contains("http")) {
                                qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getURL(), gsonObj.getVideos().get(0).getExtractor(), true);
                                recyclerView.setAdapter(qualityBottomsheetAdapter);

                            }
                            //                                            else {
                            //                                                qualityBottomsheetAdapter = new QualityBottomsheetAdapter(Mcontext, gsonObj.getVideos().get(0).getURL(), gsonObj.getVideos().get(0).getExtractor(), true);
                            //                                                recyclerView.setAdapter(qualityBottomsheetAdapter);
                            //                                            }

                        }
                    }

                    if (!((Activity) Mcontext).isFinishing()) {

                        BottomSheetDialog dialog = new BottomSheetDialog(Mcontext);


                        if (response.getJSONArray("videos").getJSONObject(0).has("extractor")) {
                            String styledText = "";

                            switch (gsonObj.getVideos().get(0).getExtractor()) {
                                case "youtube": {
                                    styledText = "Source: <font color='red'>" + "YTD" + "</font>";

                                    break;
                                }
                                case "soundcloud": {
                                    styledText = "Source: <font color='red'>" + "SCloud" + "</font>";

                                    break;
                                }
                                case "facebook": {
                                    styledText = "Source: <font color='red'>" + "SocialF" + "</font>";

                                    break;
                                }

                                default: {
                                    styledText = "Source: <font color='red'>" + gsonObj.getVideos().get(0).getExtractor() + "</font>";

                                    break;
                                }

                            }
                            source_bottomsheet.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                        }


                        if (response.getJSONArray("videos").getJSONObject(0).has("duration")) {

                            String mystring = gsonObj.getVideos().get(0).getDuration() + "";
                            String[] correctstring = mystring.split("\\.");

                            long hours = Long.parseLong(correctstring[0]) / 3600;
                            long minutes = (Long.parseLong(correctstring[0]) % 3600) / 60;
                            long seconds = Long.parseLong(correctstring[0]) % 60;

                            String DurationstyledText = "Duration: <font color='red'>" + String.format("%02d:%02d:%02d", hours, minutes, seconds) + "</font>";
                            duration_bottomsheet.setText(Html.fromHtml(DurationstyledText), TextView.BufferType.SPANNABLE);
                        }

                        if (response.getJSONArray("videos").getJSONObject(0).has("title")) {

                            System.out.println("reccccc VVKKtttt " + gsonObj.getVideos().get(0).getTitle());

                            String titletyledText = "Title: <font color='red'>" + String.format("%s", gsonObj.getVideos().get(0).getTitle()) + "</font>";
                            title_bottomsheet.setText(Html.fromHtml(titletyledText), TextView.BufferType.SPANNABLE);
                        }

                        if (response.getJSONArray("videos").getJSONObject(0).has("thumbnail")) {

                            Glide.with(Mcontext)
                                    .load(gsonObj.getVideos().get(0).getThumbnail())
                                    .into(thumb_bottomsheet);

                        }
                        // source_bottomsheet.setText(String.format("Source: %s", gsonObj.getVideos().get(0).getExtractor()));
                        btncancel_bottomsheet.setOnClickListener(v -> dialog.cancel());

                        dialog.setContentView(view);
                        dialog.show();

                    }
                    dismissMyDialog();

                }

            }
        } catch (Exception str2) {
            // str2.printStackTrace();
            System.out.println("reccccc VVKK Error= " + str2);

            dismissMyDialogErrortoast();
        }

    }

    @Keep
    public static void parseCalldlApisLocalDataData(VideoInfo response, String url) {
        try {
            dismissMyDialog();
            System.out.println("reccccc VVKK " + response.getId());

            View view = LayoutInflater.from(Mcontext).inflate(R.layout.bottomsheet_quality_layout, null);
            String[] dataSize;
            Button btncancel_bottomsheet = view.findViewById(R.id.btncancel_bottomsheet);
            Button btnopen_bottomsheet = view.findViewById(R.id.btnopen_bottomsheet);
            TextView source_bottomsheet = view.findViewById(R.id.source_bottomsheet);
            TextView title_bottomsheet = view.findViewById(R.id.bottomsheet_title);
            TextView duration_bottomsheet = view.findViewById(R.id.bottomsheet_duration);
            ImageView thumb_bottomsheet = view.findViewById(R.id.bottomsheet_thumbnail);
            TextView bottomsheet_description = view.findViewById(R.id.bottomsheet_description);
            LinearLayout linAudios = view.findViewById(R.id.linbbb2);
            LinearLayout linVideos = view.findViewById(R.id.linbbb3);


            RecyclerView recyclerView = view.findViewById(R.id.recqualitybottomsheet);

            recyclerView.setLayoutManager(new LinearLayoutManager(Mcontext));

            RecyclerView recyclerView_audio = view.findViewById(R.id.recqualitybottomsheet_aud);
            recyclerView_audio.setHasFixedSize(true);
            recyclerView_audio.setLayoutManager(new LinearLayoutManager(Mcontext));

            QualityBottomsheetAdapter qualityBottomsheetAdapter = null;

            System.out.println("reccc lengthe iss= " + response.getFormats().size());


            System.out.println("reccccc VVKK 0 ");


            if (response.getFormats().size() > 0) {

                System.out.println("reccccc VVKK 2");

                System.out.println("reccccc VVKK 3 ");

                dataSize = splitDataToVideoAndAudio_dlapiformat(response.getFormats(), recyclerView, recyclerView_audio, qualityBottomsheetAdapter, response.getExtractor(), response.getTitle(), response.getId(), url);

                if (!dataSize[0].equals("y")) {
                    linVideos.setVisibility(View.GONE);
                }

                if (!dataSize[1].equals("y")) {
                    linAudios.setVisibility(View.GONE);
                }
            }

            BottomSheetDialog dialog = new BottomSheetDialog(Mcontext);

            if (!TextUtils.isEmpty(response.getDescription())) {

                String descriptiontext = "Description: " + String.format("%s", response.getDescription());
                bottomsheet_description.setText(Html.fromHtml(descriptiontext), TextView.BufferType.SPANNABLE);
            }
            if (!TextUtils.isEmpty(response.getExtractor())) {
                String styledText = "";

                switch (response.getExtractor()) {
                    case "youtube": {
                        styledText = "Source: <font color='red'>" + "YTD" + "</font>";

                        break;
                    }
                    case "soundcloud": {
                        styledText = "Source: <font color='red'>" + "SCloud" + "</font>";

                        break;
                    }
                    case "facebook": {
                        styledText = "Source: <font color='red'>" + "SocialF" + "</font>";

                        break;
                    }

                    default: {
                        styledText = "Source: <font color='red'>" + response.getExtractor() + "</font>";

                        break;
                    }

                }
                source_bottomsheet.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
            }

            if (response.getDuration() > 0) {

                String mystring = response.getDuration() + "";
                String[] correctstring = mystring.split("\\.");

                long hours = Long.parseLong(correctstring[0]) / 3600;
                long minutes = (Long.parseLong(correctstring[0]) % 3600) / 60;
                long seconds = Long.parseLong(correctstring[0]) % 60;

                String DurationstyledText = "Duration: <font color='red'>" + String.format("%02d:%02d:%02d", hours, minutes, seconds) + "</font>";
                duration_bottomsheet.setText(Html.fromHtml(DurationstyledText), TextView.BufferType.SPANNABLE);
            }

            if (!TextUtils.isEmpty(response.getTitle())) {

                System.out.println("reccccc VVKKtttt " + response.getTitle());

                String titletyledText = "Title: <font color='red'>" + String.format("%s", response.getTitle()) + "</font>";
                title_bottomsheet.setText(Html.fromHtml(titletyledText), TextView.BufferType.SPANNABLE);
            }

            System.out.println("reccccc VVKK thumb " + response.getThumbnail());


            if (!TextUtils.isEmpty(response.getThumbnail())) {

                Glide.with(Mcontext)
                        .load(response.getThumbnail())
                        .into(thumb_bottomsheet);

            }
            // source_bottomsheet.setText(String.format("Source: %s", gsonObj.getVideos().get(0).getExtractor()));
            btncancel_bottomsheet.setOnClickListener(v -> dialog.cancel());

            btnopen_bottomsheet.setOnClickListener(v -> {
                if (dialog.getBehavior().getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    dialog.getBehavior().setState(BottomSheetBehavior.STATE_COLLAPSED);
                    dialog.getBehavior().setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
                    btnopen_bottomsheet.animate().rotationBy(180f).start();
                } else {
                    dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
                    dialog.getBehavior().setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
                    btnopen_bottomsheet.animate().rotationBy(180f).start();
                }
            });

            dialog.setContentView(view);
            dialog.show();

            dismissMyDialog();


        } catch (Exception str2) {
            // str2.printStackTrace();
            System.out.println("reccccc VVKK Error= " + str2);

            dismissMyDialogErrortoast();
        }

    }

    private static class GetInstagramVideo extends AsyncTask<String, Void, Document> {
        Document doc;

        @Override
        protected Document doInBackground(String... urls) {

            System.out.println("mydahjsdgadashas2244  " + urls[0]);

            try {
                doc = Jsoup.connect(urls[0]).get();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error" + e.getMessage());
            }
            return doc;

        }

        protected void onPostExecute(Document result) {
            dismissMyDialog();

            try {
                String URL = result.select("meta[property=\"og:video\"]").last().attr("content");
                Title = result.title();
                System.out.println("mydahjsdgadashas  " + Title);

                DownloadFileMain.startDownloading(Mcontext, URL, Title + ".mp4", ".mp4");

            } catch (Exception e) {
                System.out.println("mydahjsdgadashas22  " + e.getMessage());
                e.printStackTrace();

                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }
    }

    private static class callGetRoposoData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.ShareChatDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues roposo_", "doInBackground: Error");
            }
            return this.ShareChatDoc;
        }

        protected void onPostExecute(Document document) {
            String charSequence = "";

            try {

                dismissMyDialog();

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                System.out.println("myresponseis111 html " + html);

                if (!html.equals("")) {
                    VideoUrl = new JSONObject(html)
                            .getJSONObject("props")
                            .getJSONObject("pageProps")
                            .getJSONObject("meta")
                            .getString("og:video");
                    if (VideoUrl != null && !VideoUrl.equals("")) {
                        try {

                            String nametitle = "roposo_" +
                                    System.currentTimeMillis() +
                                    ".mp4";

                            DownloadFileMain.startDownloading(Mcontext, VideoUrl, nametitle, ".mp4");

                            VideoUrl = charSequence;

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());
                            dismissMyDialog();
                            document2.printStackTrace();
                            Mcontext.runOnUiThread(() -> {
                                iUtils.ShowToastError(Mcontext,
                                        Mcontext.getResources().getString(R.string.somthing)
                                );
                            });
                        }

                        return;
                    }
                }

            } catch (Exception document22) {
                document22.printStackTrace();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
                dismissMyDialog();
            }
        }
    }

    public static class CallMitronData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            String str;
            try {
                String str2 = strArr[0];
                if (str2.contains("api.mitron.tv")) {
                    String[] split = str2.split("=");
                    str = "https://web.mitron.tv/video/" + split[split.length - 1];
                } else {
                    str = strArr[0];
                }
                this.RoposoDoc = Jsoup.connect(str).get();
            } catch (IOException e) {
                e.printStackTrace();
                dismissMyDialog();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            //   System.out.println("myresponseis111 " + document.html());

            try {

                dismissMyDialog();

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                if (!html.equals("")) {
                    this.VideoUrl = String.valueOf(new JSONObject(html)
                            .getJSONObject("props")
                            .getJSONObject("pageProps").getJSONObject("video").get("videoUrl"));
                    if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                        try {

                            String myurldocument = VideoUrl;

                            String nametitle = "mitron_" +
                                    System.currentTimeMillis();

                            DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                            //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                            VideoUrl = "";
                            //   binding.etText.setText(charSequence);

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());
                            dismissMyDialog();
                            document2.printStackTrace();
                            Mcontext.runOnUiThread(() -> {
                                iUtils.ShowToastError(Mcontext,
                                        Mcontext.getResources().getString(R.string.somthing)
                                );
                            });
                        }

                        return;
                    }
                    return;
                }
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CallIMDBData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                String str2 = strArr[0];

                this.RoposoDoc = Jsoup.connect(str2).get();
            } catch (IOException e) {
                e.printStackTrace();
                dismissMyDialog();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            //   System.out.println("myresponseis111 " + document.html());

            try {

                dismissMyDialog();

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();

                //  System.out.println("myressss " + html);
                if (!html.equals("")) {

                    JSONArray listOfUrls = new JSONObject(html)
                            .getJSONObject("props")
                            .getJSONObject("pageProps")
                            .getJSONObject("videoPlaybackData")
                            .getJSONObject("video")
                            .getJSONArray("playbackURLs");

                    ArrayList<String> arrayList = new ArrayList<>();
                    ArrayList<String> qualityArrayList = new ArrayList<>();

                    for (int i = 1; i < listOfUrls.length(); i++) {
                        System.out.println("myressss urls " + listOfUrls.getJSONObject(i).getString("url"));

                        arrayList.add(listOfUrls.getJSONObject(i).getString("url"));
                        qualityArrayList.add(listOfUrls.getJSONObject(i).getJSONObject("displayName").getString("value"));

                    }

                    //                            String myurldocument = VideoUrl;
                    //
                    //
                    //                            String nametitle = "mitron_" +
                    //                                    System.currentTimeMillis();
                    //
                    //                            DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");
                    //
                    //                            VideoUrl = "";

                    CharSequence[] charSequenceArr = qualityArrayList.toArray(new String[0]);
                    if (!Mcontext.isFinishing()) {
                        new AlertDialog.Builder(Mcontext).setTitle("Quality!")
                                .setItems(charSequenceArr, (dialogInterface, i) -> DownloadFileMain.startDownloading(Mcontext, arrayList.get(i), "IMDB_" + System.currentTimeMillis(), ".mp4"))
                                .setPositiveButton("OK", (dialogInterface, i) -> dismissMyDialog()).setCancelable(false).show();
                    }
                } else {
                    dismissMyDialogErrortoast();
                }
            } catch (Throwable f) {
                System.out.println("myresponseis111 exp " + f.getMessage());

                f.printStackTrace();
                dismissMyDialogErrortoast();

            }
        }

    }

    public static class CallJoshData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";
        String link = "";

        String getVideoIdjosh(String link) {

            if (link.contains("?")) {
                link = link.split("\\?")[0];
                link = (link.contains("video")) ? link.substring(link.indexOf("video")) : link.substring(link.indexOf("content"));
                link = link.substring(link.indexOf("/") + 1);
            } else {
                link = (link.contains("video")) ? link.substring(link.indexOf("video")) : link.substring(link.indexOf("content"));
                link = link.substring(link.indexOf("/") + 1);
            }
            return link;
        }

        //https://stream.coolfie.io/stream/prod-ugc-contents/0e19c40d6d17dd00/29c2e4ea77cd8119/0e19c40d6d17dd0029c2e4ea77cd8119_r4_wmj_480.mp4
        public Document doInBackground(String... strArr) {
            try {

                link = getVideoIdjosh(strArr[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {
            try {
                JSONObject snackvideoDoc = new JSONObject("{\"method\": \"GET\",\"url\": \"https://api-internal.myjosh.in/share/content/" + link + "\",\"platform\": \"PWA\"}");

                AndroidNetworking.post("https://share.myjosh.in/webview/apiwbody")
                        .addJSONObjectBody(snackvideoDoc)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("wojfdjhfdjh " + response);

                                try {
                                    dismissMyDialog();

                                    VideoUrl = response.getJSONObject("data").getString("mp4_url");

                                    String myurldocument = VideoUrl.replace("_r4_wmj_480.mp4", ".mp4");

                                    String nametitle = "joshvideo_" +
                                            System.currentTimeMillis();

                                    DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                                    //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                                    VideoUrl = "";
                                    //   binding.etText.setText(charSequence);

                                } catch (Exception document2) {
                                    System.out.println("myresponseis111 exp1 " + document2.getMessage());
                                    dismissMyDialog();
                                    document2.printStackTrace();
                                    Mcontext.runOnUiThread(() -> {
                                        iUtils.ShowToastError(Mcontext,
                                                Mcontext.getResources().getString(R.string.somthing)
                                        );
                                    });
                                }

                            }

                            @Override
                            public void onError(ANError error) {

                                error.printStackTrace();
                                dismissMyDialogErrortoast();
                            }
                        });

            } catch (Exception eee) {
                System.out.println("myresponseis111 exp " + eee.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CallRizzleData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {

                dismissMyDialog();

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                if (!html.equals("")) {
                    this.VideoUrl = String.valueOf(new JSONObject(html).getJSONObject("props").getJSONObject("pageProps").getJSONObject("post").getJSONObject("video").get("originalUrl"));
                    if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                        try {

                            String myurldocument = VideoUrl;

                            String nametitle = "rizzlevideo_" +
                                    System.currentTimeMillis();

                            DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                            //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                            VideoUrl = "";
                            //   binding.etText.setText(charSequence);

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());
                            dismissMyDialog();
                            document2.printStackTrace();
                            Mcontext.runOnUiThread(() -> {
                                iUtils.ShowToastError(Mcontext,
                                        Mcontext.getResources().getString(R.string.somthing)
                                );
                            });
                        }

                        return;
                    }
                    return;
                }
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CallIfunnyData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36").get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {

                dismissMyDialog();
                this.VideoUrl = document.select("meta[property=\"og:video:url\"]").last().attr("content");

                //                String html = document.select("script[class=\"js-media-template\"]").first().html();
                //                new Element(html);
                //                Matcher matcher = Pattern.compile("<video[^>]+poster\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>").matcher(html);
                //                while (matcher.find()) {
                //                    this.VideoUrl = matcher.group(1).replace("jpg", "mp4").replace("images", "videos").replace("_3", "_1");
                //                }
                if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                    try {

                        String myurldocument = VideoUrl;

                        String nametitle = "ifunnyvideo_" +
                                System.currentTimeMillis();

                        DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());
                        dismissMyDialog();
                        document2.printStackTrace();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }

                    return;
                }

                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CallBoloindyaData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {

                dismissMyDialog();

                Iterator it = document.getElementsByTag("script").iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Element element = (Element) it.next();
                    if (element.data().contains("videoFileCDN")) {
                        for (String str : element.data().split(StringUtils.LF)) {
                            if (str.contains("var videoFileCDN=\"https")) {
                                this.VideoUrl = str.split("=")[1]
                                        .replace("\"", "")
                                        .replace("\"", "")
                                        .replace(";", "");
                            }
                        }
                    }
                }
                if (this.VideoUrl.startsWith("//")) {
                    this.VideoUrl = "https:" + this.VideoUrl;
                }
                if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                    try {

                        String myurldocument = VideoUrl;

                        String nametitle = "Boloindyavideo_" +
                                System.currentTimeMillis();

                        DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());
                        dismissMyDialog();
                        document2.printStackTrace();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }

                    return;
                }

                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CallhindData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {
                dismissMyDialog();

                for (Element element : document.getElementsByTag("script")) {
                    if (element.data().contains("window.__STATE__")) {
                        String replace = element.html().replace("window.__STATE__", "").replace(";", "")
                                .replace("=", "");
                        this.VideoUrl = String.valueOf(new JSONObject(new JSONObject(new JSONArray("[" + replace + "]")
                                .get(0).toString()).getJSONObject("feed").getJSONArray("feed")
                                .get(0).toString()).get("download_media"));
                        if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                            try {

                                String myurldocument = VideoUrl;

                                String nametitle = "hindvideo_" +
                                        System.currentTimeMillis();

                                DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                                //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                                VideoUrl = "";
                                //   binding.etText.setText(charSequence);

                            } catch (Exception document2) {
                                System.out.println("myresponseis111 exp1 " + document2.getMessage());
                                dismissMyDialog();
                                document2.printStackTrace();
                                Mcontext.runOnUiThread(() -> {
                                    iUtils.ShowToastError(Mcontext,
                                            Mcontext.getResources().getString(R.string.somthing)
                                    );
                                });
                            }

                            return;
                        }
                    }
                }
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CalldubsmashData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {

                dismissMyDialog();

                this.VideoUrl = document.select("video").last().attr("src");
                if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                    try {

                        String myurldocument = VideoUrl;

                        String nametitle = "dubsmashvideo_" +
                                System.currentTimeMillis();

                        DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());
                        dismissMyDialog();
                        document2.printStackTrace();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }

                    return;
                }

                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CalltumblerData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {

                dismissMyDialog();
                // System.out.println("myresponseis111 exp166 " + document);
                //                System.out.println("myresponseis111 exp166 " + document.select("source[src]").first().attr("src"));
                //                System.out.println("myresponseis111 exp177 " + document.select("video[src]").first().attr("src"));

                this.VideoUrl = document.select("source").last().attr("src");

                System.out.println("myresponseis111 exp1 " + VideoUrl);

                if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                    try {

                        String myurldocument = VideoUrl;

                        String nametitle = "tumbler_" +
                                System.currentTimeMillis();

                        DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());
                        dismissMyDialog();
                        document2.printStackTrace();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }

                    return;
                }

                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CallRedditData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {

                dismissMyDialog();
                System.out.println("myresponseis111 exp166 " + document.select("a"));
                //System.out.println("myresponseis111 exp166 " + document.select("a[class=\"downloadbutton \"]").last().attr("src"));
                //                System.out.println("myresponseis111 exp177 " + document.select("video[src]").first().attr("src"));

                this.VideoUrl = document.select("a").get(10).attr("href");

                System.out.println("myresponseis111 exp1 " + VideoUrl);

                if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                    try {

                        String myurldocument = VideoUrl;

                        String nametitle = "Reddit_" +
                                System.currentTimeMillis();

                        DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());
                        dismissMyDialog();
                        document2.printStackTrace();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }

                    return;
                }

                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp12 " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CalllinkedinData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {

                dismissMyDialog();
                System.out.println("myresponseis111 exp166 linkedin" + document);
                //                System.out.println("myresponseis111 exp166 " + document.select("source[src]").first().attr("src"));
                //                System.out.println("myresponseis111 exp177 " + document.select("video[src]").first().attr("src"));

                this.VideoUrl = document.select("video").first().attr("data-sources");

                JSONArray jsonArray = new JSONArray(VideoUrl);
                System.out.println("myresponseis111 exp1 " + jsonArray.getJSONObject(0).getString("src"));

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(jsonArray.getJSONObject(0).getString("src"));
                arrayList.add(jsonArray.getJSONObject(1).getString("src"));

                CharSequence[] charSequenceArr = new CharSequence[arrayList.size()];

                charSequenceArr[0] = "Low quality";
                charSequenceArr[1] = "High quality";
                if (!Mcontext.isFinishing()) {
                    new AlertDialog.Builder(Mcontext).setTitle("Quality!").setItems(charSequenceArr, (dialogInterface, i) -> DownloadFileMain.startDownloading(Mcontext, arrayList.get(i), "Linkedin_" + System.currentTimeMillis(), ".mp4")).setPositiveButton("OK", (dialogInterface, i) -> dismissMyDialog()).setCancelable(false).show();
                }
                //                Mcontext.runOnUiThread(() -> {
                //                    iUtils.ShowToastError(      Mcontext,
                //                            Mcontext.getResources().getString(R.string.somthing)
                //                    );
                //                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CallgaanaData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                //   this.RoposoDoc = Jsoup.connect(strArr[0]).get();
                //   download_hlsganna("", ".mp4");

            } catch (Exception e) {

                System.out.println("jskdfhksdhfkshdfkhsdj " + e.getMessage());
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            dismissMyDialog();

        }
    }

    public static class CallgdriveData extends AsyncTask<String, Void, String> {
        String VideoUrl = "";
        LowCostVideo xGetter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.xGetter = new LowCostVideo(Mcontext);
            this.xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {
                public void onTaskCompleted(ArrayList<XModel> arrayList, boolean z) {
                    if (!z) {

                        System.out.println("myresponseis111 exp122 " + arrayList.get(0));

                        CallgdriveData.this.done(arrayList.get(0));

                    } else if (arrayList != null) {
                        System.out.println("myresponseis111 exp133 " + arrayList.get(0));

                        CallgdriveData.this.multipleQualityDialog(arrayList);
                    } else {

                        dismissMyDialog();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }
                }

                public void onError() {

                    dismissMyDialog();
                    Mcontext.runOnUiThread(() -> {
                        iUtils.ShowToastError(Mcontext,
                                Mcontext.getResources().getString(R.string.somthing)
                        );
                    });
                }
            });

        }

        public String doInBackground(String... strArr) {
            return strArr[0];
        }

        public void onPostExecute(String str) {
            System.out.println("myresponseis111 exp13344 " + str);

            if (xGetter != null) {
                this.xGetter.find(str);
                //   System.out.println("myresponseis111 exp13344 " + xGetter.find(str));

            } else {
                this.xGetter = new LowCostVideo(Mcontext);
                this.xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {
                    public void onTaskCompleted(ArrayList<XModel> arrayList, boolean z) {

                        System.out.println("myresponseis111 exp133 " + arrayList.get(0));

                        if (!z) {

                            System.out.println("myresponseis111 exp122 " + arrayList.get(0));

                            CallgdriveData.this.done(arrayList.get(0));

                        } else if (arrayList != null) {
                            System.out.println("myresponseis111 exp133 " + arrayList.get(0));

                            CallgdriveData.this.multipleQualityDialog(arrayList);
                        } else {

                            dismissMyDialog();
                            Mcontext.runOnUiThread(() -> {
                                iUtils.ShowToastError(Mcontext,
                                        Mcontext.getResources().getString(R.string.somthing)
                                );
                            });
                        }

                    }

                    public void onError() {

                        dismissMyDialog();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }
                });
                this.xGetter.find(str);
            }
        }

        public void multipleQualityDialog(final ArrayList<XModel> arrayList) {
            CharSequence[] charSequenceArr = new CharSequence[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                charSequenceArr[i] = arrayList.get(i).getQuality();
            }
            if (!Mcontext.isFinishing()) {
                new AlertDialog.Builder(Mcontext).setTitle("Quality!").setItems(charSequenceArr, (dialogInterface, i) -> CallgdriveData.this.done(arrayList.get(i))).setPositiveButton("OK", (dialogInterface, i) -> dismissMyDialog()).setCancelable(false).show();
            }
        }

        public void done(XModel xModel) {

            try {

                dismissMyDialog();

                this.VideoUrl = xModel.getUrl();
                if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                    try {

                        String myurldocument = VideoUrl;

                        String nametitle = "Allvideo_" +
                                System.currentTimeMillis();

                        DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());
                        dismissMyDialog();
                        document2.printStackTrace();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }

                    return;
                }

                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }

        }

    }

    public static class callGetsnackvideoData extends AsyncTask<String, Void, JSONObject> {
        JSONObject snackvideoDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            try {

                String photoId, authorId;
                String location = iUtils.getRedirectUrl(urls[0]);

                System.out.println(location);
                if (location.contains("?")) {
                    location = location.split("\\?")[0];

                }
                if (location != null) {

                    authorId = location.split("/")[4];
                    photoId = location.split("/")[5];
                } else {
                    location = urls[0];
                    authorId = location.split("/")[4];
                    photoId = location.split("/")[5];
                }

                snackvideoDoc = new JSONObject("{\"photoId\":\"" + photoId + "\",\"authorId\":\"" + authorId + "\"}");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return snackvideoDoc;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {

                AndroidNetworking.post("https://m.snackvideo.com/rest/o/w/photo/getUserHotPhoto?kpn=KWAI_BULLDOG")
                        .addJSONObjectBody(result)
                        .addHeaders("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36")
                        .addHeaders("x-csrf-token", "Acn4pAjr5lT5AT6OBjzyB9VP")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("wojfdjhfdjh " + response);

                                try {
                                    VideoUrl = response.getJSONArray("datas").getJSONObject(0).getString("mp4Url");

                                    System.out.println("myresponseis111 list_of_qualities" + VideoUrl);

                                    // getSnackVideoData(jSONObject.getString("shortUrl"), Mcontext);
                                    String nametitle = "snackvideo_" +
                                            System.currentTimeMillis();

                                    DownloadFileMain.startDownloading(Mcontext, VideoUrl, nametitle, ".mp4");
                                    VideoUrl = "";
                                    dismissMyDialog();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    dismissMyDialogErrortoast();
                                }

                            }

                            @Override
                            public void onError(ANError error) {

                                error.printStackTrace();
                                dismissMyDialogErrortoast();
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
                dismissMyDialogErrortoast();
            }
        }

    }

    private static class callGetbilibiliAppData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;
        private Iterator<Element> abk;

        callGetbilibiliAppData() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.ShareChatDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues roposo_", "doInBackground: Error");
            }
            return this.ShareChatDoc;
        }

        protected void onPostExecute(Document document) {
            String charSequence = "";

            try {

                ArrayList<String> mp4List = new ArrayList<>();
                ArrayList<String> qualitylist = new ArrayList<>();

                String data = "";

                Iterator<Element> documentitrator = document.select("script").iterator();

                do {
                    if (!documentitrator.hasNext()) {

                        break;
                    }
                    data = documentitrator.next().data();
                    Log.e("onP4342424te:datais ", data);

                } while (!data.contains("window.__playinfo__="));

                String stringbuil = data.substring(data.indexOf("{"), data.lastIndexOf("}"));

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(stringbuil);
                stringBuilder.append("}");

                Log.e("onPostbjnkjhoso_11 ", stringBuilder.toString());
                if (!document.equals("")) {
                    try {
                        JSONObject jSONObject = new JSONObject(stringBuilder.toString());
                        JSONObject datajSONObject = jSONObject.getJSONObject("data");
                        JSONObject dashjSONObject1 = datajSONObject.getJSONObject("dash");
                        JSONArray videojSONObject1 = dashjSONObject1.getJSONArray("video");

                        System.out.println("respossss112212121URL)) " + videojSONObject1.getJSONObject(0).getString("base_url"));

                        for (int i = 0; i < videojSONObject1.length(); i++) {

                            JSONObject jsonObject12 = videojSONObject1.getJSONObject(i);
                            mp4List.add(jsonObject12.getString("base_url"));
                            qualitylist.add(jsonObject12.getString("width"));

                            System.out.println("respossss112212121URL " + jsonObject12.getString("base_url"));

                        }

                        try {
                            JSONArray audiojSONObject1 = dashjSONObject1.getJSONArray("audio");
                            for (int i = 0; i < audiojSONObject1.length(); i++) {

                                JSONObject jsonObject12 = audiojSONObject1.getJSONObject(i);
                                mp4List.add(jsonObject12.getString("base_url"));
                                qualitylist.add(jsonObject12.getString("mime_type"));

                                System.out.println("respossss112212121URL " + jsonObject12.getString("base_url"));

                            }

                        } catch (Exception e) {
                            dismissMyDialog();
                        }

                        if (videojSONObject1.length() > 0) {
                            if (!((Activity) Mcontext).isFinishing()) {

                                dialog_quality_allvids = new Dialog(Mcontext);

                                dismissMyDialog();

                                windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);
                                LayoutInflater layoutInflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                mChatHeadView = layoutInflater.inflate(R.layout.dialog_quality_ytd, null);

                                dialog_quality_allvids.setContentView(mChatHeadView);

                                mainLayout = mChatHeadView.findViewById(R.id.linlayout_dialog);

                                img_dialog = mChatHeadView.findViewById(R.id.img_dialog);

                                mainLayout = dialog_quality_allvids.findViewById(R.id.linlayout_dialog);
                                img_dialog = dialog_quality_allvids.findViewById(R.id.img_dialog);

                                int size = 0;

                                try {
                                    DisplayMetrics displayMetrics = new DisplayMetrics();
                                    ((Activity) Mcontext).getWindowManager()
                                            .getDefaultDisplay()
                                            .getMetrics(displayMetrics);

                                    int height = displayMetrics.heightPixels;
                                    int width = displayMetrics.widthPixels;

                                    size = width / 2;

                                } catch (Exception e) {
                                    size = WindowManager.LayoutParams.WRAP_CONTENT;
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    params = new WindowManager.LayoutParams(
                                            size,
                                            WindowManager.LayoutParams.WRAP_CONTENT,
                                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                                                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                                                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                            PixelFormat.TRANSLUCENT);

                                } else {
                                    params = new WindowManager.LayoutParams(
                                            size,
                                            WindowManager.LayoutParams.WRAP_CONTENT,
                                            WindowManager.LayoutParams.TYPE_PHONE,
                                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                                                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                                                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                            PixelFormat.TRANSLUCENT);

                                }
                                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                                params.x = 0;
                                params.y = 100;

                                // mainLayout.setLayoutParams(params);

                                for (int i = 0; i < mp4List.size(); i++) {

                                    addButtonToMainLayouttest_allvideo(qualitylist.get(i), mp4List.get(i), "Bilibili_" + qualitylist.get(i) + "_" + System.currentTimeMillis());

                                }

                                img_dialog.setOnClickListener(v -> dialog_quality_allvids.dismiss());

                                dialog_quality_allvids.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                                dialog_quality_allvids.getWindow().setAttributes(params);
                                //  dialogquality.getWindow().setColorMode(Mcontext.getResources().getColor(R.color.colorAccent));

                                dialog_quality_allvids.show();

                                dialog_quality_allvids.show();
                            }
                        } else {

                            DownloadFileMain.startDownloading(Mcontext, mp4List.get(0), "Bilibili_" + System.currentTimeMillis(), ".mp4");

                            dismissMyDialog();
                        }

                    } catch (Exception document2) {
                        document2.printStackTrace();

                        System.out.println("respossss112212121qerrr " + document2.getMessage());

                        dismissMyDialog();
                        Mcontext.runOnUiThread(() -> {
                            iUtils.ShowToastError(Mcontext,
                                    Mcontext.getResources().getString(R.string.somthing)
                            );
                        });
                    }
                }

            } catch (Exception document22) {
                dismissMyDialog();
                document22.printStackTrace();
                System.out.println("respossss112212121qerrr " + document22.getMessage());

                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }
    }

    public static class Call9gagData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(strArr[0])
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();

                return this.RoposoDoc = Jsoup.parse(Objects.requireNonNull(response.body()).string());

            } catch (IOException e) {
                e.printStackTrace();
                return this.RoposoDoc;

            }
        }

        public void onPostExecute(Document document) {
            System.out.println("myresponseis111 exp12222 " + document);

            try {
                dismissMyDialog();

                Iterator it = document.getElementsByTag("script").iterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    if (element.data().contains("window.__STATE__")) {
                        String replace = element.html().replace("window.__STATE__", "").replace(";", "")
                                .replace("=", "");
                        this.VideoUrl = String.valueOf(new JSONObject(new JSONObject(new JSONArray("[" + replace + "]")
                                .get(0).toString()).getJSONObject("feed").getJSONArray("feed")
                                .get(0).toString()).get("download_media"));
                        if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                            try {

                                String myurldocument = VideoUrl;

                                String nametitle = "hindvideo_" +
                                        System.currentTimeMillis();

                                DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                                //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                                VideoUrl = "";
                                //   binding.etText.setText(charSequence);

                            } catch (Exception document2) {
                                System.out.println("myresponseis111 exp1 " + document2.getMessage());
                                dismissMyDialog();
                                document2.printStackTrace();
                                Mcontext.runOnUiThread(() -> {
                                    iUtils.ShowToastError(Mcontext,
                                            Mcontext.getResources().getString(R.string.somthing)
                                    );
                                });
                            }

                            return;
                        }
                    }
                }
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());

                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

    public static class CallkuaishouData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        private String getCookiesFromWebsite(String websiteUrl) {
            try {
                URL url = new URL(websiteUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get("Set-Cookie");
                if (cookiesHeader != null) {
                    StringBuilder cookies = new StringBuilder();
                    for (String cookie : cookiesHeader) {
                        cookies.append(cookie).append("; ");
                    }
                    return cookies.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }


        //https://www.kuaishou.com/f/X6eF2jLWKLzP2fb
        //https://www.kuaishou.com/short-video/3xm6hy7tre4tv2y?authorId=3xnd37wmjsye45u&streamSource=find&area=homexxbrilliant
        public Document doInBackground(String... strArr) {
            try {
                System.out.println("myresponseis111 mybodyhh1111>>> " + strArr[0]);

                String tempCookies = getCookiesFromWebsite("https://www.kuaishou.com/");

                ClearableCookieJar cookieJar =
                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .cookieJar(cookieJar)
                        .addInterceptor(logging)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();


                Request request = new Request.Builder()
                        .url(strArr[0])
                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                        .addHeader("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Cookie", tempCookies)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                        .build();

                Response response = client.newCall(request).execute();

                return this.RoposoDoc = Jsoup.parse(Objects.requireNonNull(response.body()).string());
            } catch (IOException e) {
                e.printStackTrace();
                return this.RoposoDoc;
            }
        }

        public void onPostExecute(Document document) {
            Log.d("", "myresponseis1115 exp12222 " + document.toString());

            try {
                dismissMyDialog();

                Iterator it = document.getElementsByTag("script").iterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    if (element.data().contains("window.__APOLLO_STATE__=")) {
                        String replace = element.html().replace("window.__APOLLO_STATE__=", "");
                        replace = replace.substring(0, replace.indexOf("{}};") + 3);

                        System.out.println("myresponseis111 replace  " + replace);

                        String sr = StringUtils.substringBetween(replace, "\"videoResource\"", "\"__typename\"");
                        sr = sr.substring(1, sr.length() - 1);
                        System.out.println("myresponseis111 str  " + sr);

                        this.VideoUrl = new JSONObject(sr)
                                .getJSONObject("json")
                                .getJSONObject("h264")
                                .getJSONArray("adaptationSet")
                                .getJSONObject(0)
                                .getJSONArray("representation")
                                .getJSONObject(0)
                                .getString("url");

                        this.VideoUrl = URLDecoder.decode(this.VideoUrl, "UTF-8");
                        System.out.println("myresponseis111 VideoUrl  " + this.VideoUrl);

                        if (this.VideoUrl != null && !this.VideoUrl.equals("")) {
                            try {

                                String myurldocument = VideoUrl;

                                String nametitle = "kuaishou_" +
                                        System.currentTimeMillis();

                                DownloadFileMain.startDownloading(Mcontext, myurldocument, nametitle, ".mp4");

                                //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                                VideoUrl = "";
                                //   binding.etText.setText(charSequence);

                            } catch (Exception document2) {
                                System.out.println("myresponseis111 exp1 " + document2.getMessage());
                                dismissMyDialog();
                                document2.printStackTrace();
                                Mcontext.runOnUiThread(() -> {
                                    iUtils.ShowToastError(Mcontext,
                                            Mcontext.getResources().getString(R.string.somthing)
                                    );
                                });
                            }

                            return;
                        }
                    }
                }
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());
                dismissMyDialog();
                Mcontext.runOnUiThread(() -> {
                    iUtils.ShowToastError(Mcontext,
                            Mcontext.getResources().getString(R.string.somthing)
                    );
                });
            }
        }

    }

}