package com.applock.photos.videos.downloaderUtil.utils;

import static com.applock.photos.videos.downloaderUtil.utils.DownloadVideosMain.Mcontext;
import static com.applock.photos.videos.downloaderUtil.utils.DownloadVideosMain.fromService;
import static com.applock.photos.videos.downloaderUtil.utils.DownloadVideosMain.pd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.CookieManager;

import androidx.annotation.Keep;
import androidx.appcompat.app.AlertDialog;

import com.applock.photos.videos.R;
import com.applock.photos.videos.downloaderUtil.models.VideoModel;
import com.applock.photos.videos.downloaderUtil.interfaces.VideoDownloader;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Keep
public class FbVideoDownloader implements VideoDownloader {

    public static String downlink;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    String matag;
    int myselmethode = 0;
    private final String VideoURL;
    private String VideoTitle;

    public FbVideoDownloader(Context context, String videoURL, int method) {
        this.context = context;
        VideoURL = videoURL;
        myselmethode = method;
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
        return link;
    }

    @Override
    public void DownloadVideo() {

        try {
            downlink = VideoURL;


            switch (myselmethode) {

//                case 0: {
//                    downlink = downlink.contains("www.facebook") ? downlink.replace("www.facebook", "m.facebook") : downlink;
//                    downlink = downlink.contains("story.php") ? downlink : downlink + "?refsrc=https%3A%2F%2Fm.facebook.com%2F&refid=28&_rdr";
//
//                    new Fbwatch_myown().execute();
//
//                    break;
//                }
                case 1: {
                    new Fbwatch_getfvid().execute();
                    break;
                }
                case 2: {
                    new Fbwatch_fbdown().execute();
                    break;
                }
                case 3: {
                    new Fbwatch_myown_version2().execute();
                    break;
                }
            }
        } catch (Exception e) {

            if (!fromService && pd != null) {

                pd.dismiss();
            }

            new Handler(Looper.getMainLooper()).post(() -> iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing)));
        }

    }

    public static class Fbwatch_myown_version2 extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrlSD = "";
        String VideoUrlHD = "";

        public String getCookies() {
            try {
                return CookieManager.getInstance().getCookie("https://m.facebook.com");

            } catch (Exception unused2) {
                return "";
            }
        }

        boolean isValidLink(String link) {
            return link.contains("https") && !link.contains(" ");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            downlink = downlink.replace("https://m.", "https://www.").replace("https://mbasic.", "https://www.");
        }

        public Document doInBackground(String... strArr) {
            try {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                hashMap.put("accept-language", "en-IN,en-US;q=0.9,en;q=0.8");
                hashMap.put("cache-control", "max-age=0");
                hashMap.put("origin", "https://www.facebook.com");
                hashMap.put("referer", "https://www.facebook.com");
                hashMap.put("sec-fetch-dest", "document");
                hashMap.put("sec-ch-ua", " Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100");
                hashMap.put("sec-fetch-mode", "navigate");
                hashMap.put("sec-ch-ua-mobile", "?0");
                hashMap.put("sec-ch-ua-platform", "macOS");
                hashMap.put("sec-fetch-site", "same-origin");
                hashMap.put("sec-fetch-user", "?1");
                hashMap.put("upgrade-insecure-requests", "1");
                hashMap.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
                hashMap.put("cookie", getCookies());
                String uag = "";
                int rand_int1 = iUtils.getRandomNumber(2);

                if (rand_int1 == 0) {
                    uag = "Mozilla/5.0 (Linux; Android 5.0.2; SAMSUNG SM-G925F Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/4.0 Chrome/44.0.2403.133 Mobile Safari/537.36";
                } else {
                    uag = "Mozilla/5.0 (Linux; U; Android 4.1.1; en-gb; Build/KLP) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30";
                }


                this.RoposoDoc = Jsoup.connect(downlink)
                        .headers(hashMap)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
                        .get();

            } catch (Exception ignored) {


            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            try {

                if (!fromService && pd != null) {

                    pd.dismiss();
                }
//                System.out.println("myresponseis111 exp166 " + document);


                String responsePage = document.toString();
                int i = 0;
                try {
                    int indexOf = responsePage.indexOf("playable_url\"") + 15;
                    String replaceAll = responsePage.substring(indexOf, responsePage.indexOf("\"", indexOf)).replaceAll("\\\\/", "/").replaceAll("\\\\u0025", "%");
                    if (isValidLink(replaceAll)) {

                        VideoUrlSD = replaceAll;

                        i = 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

                try {
                    int indexOf2 = responsePage.indexOf("playable_url_quality_hd\":\"") + 26;
                    String replaceAll2 = responsePage.substring(indexOf2, responsePage.indexOf("\"", indexOf2)).replaceAll("\\\\/", "/").replaceAll("\\\\u0025", "%");
                    if (isValidLink(replaceAll2)) {
                        VideoUrlHD = replaceAll2;
                        i++;
                    }
                } catch (Exception ignored) {
                }

                if (i != 0) {

                    System.out.println("myresponseis111 fbvideo " + VideoUrlSD);
                    System.out.println("myresponseis111 fbvideo_HD  " + VideoUrlHD);


                    DownloadFileMain.startDownloading(Mcontext, !(VideoUrlHD.equals("")) ? VideoUrlHD : VideoUrlSD, "Facebook_" + (!(VideoUrlHD.equals("")) ? "HD" : "SD") + "_" + System.currentTimeMillis(), ".mp4");


                    downlink = "";


                } else {
                    downlink = "";

                    if (!fromService && pd != null) {

                        pd.dismiss();
                    }

                    new Handler(Looper.getMainLooper()).post(() -> iUtils.ShowToastError(Mcontext, Mcontext.getResources().getString(R.string.somthing)));

                }

            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());
                downlink = "";

                if (!fromService && pd != null) {

                    pd.dismiss();
                }

                new Handler(Looper.getMainLooper()).post(() -> iUtils.ShowToastError(Mcontext, Mcontext.getResources().getString(R.string.somthing)));
            }
        }


    }

    public static class Fbwatch_tiktok extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect("https://vm.tiktok.com/ZSQTnNWu/")
                        .header("Accept", "*/*")
                        .userAgent("PostmanRuntime/7.26.8")
                        .get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {
            boolean isSecon = false;

            try {

                if (!fromService && pd != null) {

                    pd.dismiss();
                }
                System.out.println("myresponseis111 exp166 " + document);

                String data = "";

                Elements elements = document.select("script");
                for (Element element : elements) {
                    if (element.attr("type").equals("application/ld+json")) {


                        JSONObject obj = new JSONObject(element.html());

                        System.out.println("myresponseis111 list_of_qualities" + obj);


                        String replaceString = obj.getString("contentUrl");
                        System.out.println("myresponseis111 list_of_qualities" + replaceString);
                        DownloadFileMain.startDownloading(Mcontext, replaceString, "Facebook_" + System.currentTimeMillis(), ".mp4");

                        downlink = "";

                    }
                }


            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());
                downlink = "";

                if (!fromService && pd != null) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    private class Fbwatch_fbdown extends AsyncTask<Void, Boolean, Boolean> {

        String title;
        ArrayList<VideoModel> removeDuplicatesvalues = new ArrayList<>();


        CharSequence[] charSequenceArr = {context.getString(R.string.hdlink) + "", context.getString(R.string.sdlink) + ""};

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Document doc = Jsoup.connect("https://www.getfvid.com/downloader")
                        .data("url", downlink)
                        .header("accept-encoding", "gzip, deflate, br")
                        .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
                        .header("content-type", "application/x-www-form-urlencoded")
                        .header("origin", "https://www.getfvid.com")
                        .header("referer", "https://www.getfvid.com/")
                        .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                        .timeout(100 * 1000)
                        .post();

                String atag = doc.select("a").get(9).attr("href");

                Log.e("TextLink", atag);

                matag = atag;
//                }
                return true;
            } catch (Exception e) {

                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {

            if (aVoid) {
                try {
                    if (!fromService && pd != null) {

                        pd.dismiss();
                    }

                    DownloadFileMain.startDownloading(context, matag, "Facebook_" + System.currentTimeMillis(), ".mp4");


                } catch (Exception e) {

                    if (!fromService && pd != null) {

                        pd.dismiss();
                    }

                    new Handler(Looper.getMainLooper()).post(() -> iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing)));
                }

            } else {
                if (!fromService && pd != null) {

                    pd.dismiss();
                }

                new Handler(Looper.getMainLooper()).post(() -> iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing)));
            }
        }
    }

    private class Fbwatch_getfvid extends AsyncTask<Void, Boolean, Boolean> {

        String title;
        ArrayList<VideoModel> removeDuplicatesvalues = new ArrayList<>();


        CharSequence[] charSequenceArr = {context.getString(R.string.hdlink) + "", context.getString(R.string.sdlink) + ""};

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {


            try {

                Document doc = Jsoup.connect("https://downvideo.net/download.php")
                        .data("URL", downlink)
                        .data("token", "2c17c6393771ee3048ae34d6b380c5ecz")
                        .timeout(100 * 1000)
                        .post();


                String myhd9 = doc.select("a").get(4).attr("href");
                String myhd = doc.select("a").get(5).attr("href");

                Log.e("TextLink myrrr 0.5 ", myhd);
                Log.e("TextLink myrrr 0 ", myhd9);


                VideoModel videoModel2 = new VideoModel();
                videoModel2.setUrl(myhd);
                removeDuplicatesvalues.add(videoModel2);

                VideoModel videoModel1 = new VideoModel();
                videoModel1.setUrl(myhd9);
                removeDuplicatesvalues.add(videoModel1);


                return true;


//                Element link = doc.body().getElementById("sdlink");
//                String atag = link.select("a").first().attr("href");// 10,11
//                // atag = URLDecoder.decode(atag, "UTF-8");
//                Log.e("TextLink", atag);
//
//                matag = atag;
//                }
                //  Log.e("Main12346", matag);
            } catch (Exception e) {


                return false;

            }
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {

            if (aVoid) {
                try {
                    if (!fromService && pd != null) {

                        pd.dismiss();
                    }
                    //   new downloadFile().Downloading(context, matag, "Facebook_" + System.currentTimeMillis(), ".mp4");


                        new AlertDialog.Builder(context).setTitle(context.getString(R.string.select_quality))
                                .setItems(charSequenceArr, (dialogInterface, i) -> DownloadFileMain.startDownloading(context, removeDuplicatesvalues.get(i).getUrl(), "Facebook_" + System.currentTimeMillis(), ".mp4")).setPositiveButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss()).setCancelable(false).show();

                } catch (Exception e) {

                    if (!fromService && pd != null) {

                        pd.dismiss();
                    }

//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//
//                        }
//                    });
                }


            } else {
                try {
                    if (!fromService && pd != null) {

                        pd.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
//
//                    }
//                });
            }
        }
    }


}
