package com.applock.photos.videos.downloaderUtil.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityGetLinkThroughWebviewBinding;
import com.applock.photos.videos.downloaderUtil.models.VideoModel;
import com.applock.photos.videos.downloaderUtil.utils.DownloadFileMain;
import com.applock.photos.videos.downloaderUtil.utils.iUtils;

import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetLinkThroughWebView extends AppCompatActivity {
    String url = "";
    ProgressDialog progressDialog;
    boolean isOnetime = false;
    Handler handler;
    Runnable runnable;
    private ArrayList<VideoModel> videoModelArrayList;
    private ActivityGetLinkThroughWebviewBinding binding;


    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetLinkThroughWebviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(GetLinkThroughWebView.this);
        progressDialog.setMessage(getString(R.string.nodeifittakeslonger));
//        progressDialog.show();

        url = "https://audiomack.com/lightskinkeisha/song/fdh";
        videoModelArrayList = new ArrayList<>();
        handler = new Handler();

        if (getIntent().hasExtra("myurlis")) {
            url = getIntent().getStringExtra("myurlis");


            if (url.contains("ok.ru")) {
                try {
                    url = "https://dirpy.com/studio?url=" + URLEncoder.encode(url, String.valueOf(StandardCharsets.UTF_8));
                } catch (Exception ignored) {
                }
            }


            if (url.contains("audiomack")) {

                String[] urlarray = url.split("/");
                System.out.println("length ksdjjfsdfsd 6" + urlarray[5]);
                System.out.println("length ksdjjfsdfsd 5" + urlarray[3]);
                url = "https://audiomack.com/embed/song/" + urlarray[3] + "/" + urlarray[5] + "?background=1";

                System.out.println("length ksdjjfsdfsd 77 =" + url);
            }


        } else {
            url = "https://audiomack.com/embed/song/lightskinkeisha/fdh?background=1";
        }

        binding.browser.clearCache(true);


        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.browser, true);
        binding.browser.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        CookieSyncManager.createInstance(GetLinkThroughWebView.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        binding.browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        binding.browser.getSettings().setJavaScriptEnabled(true);
        binding.browser.getSettings().getAllowFileAccess();
        binding.browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.browser.getSettings().setDomStorageEnabled(true);
        binding.browser.getSettings().setAllowUniversalAccessFromFileURLs(true);
        binding.browser.getSettings().setMediaPlaybackRequiresUserGesture(false);


//        binding.browser.getSettings().setPluginState(WebSettings.PluginState.ON);


//        binding.browser.getSettings().setUserAgentString(binding.browser.getSettings().getUserAgentString());

        int randomnumber = iUtils.getRandomNumber(iUtils.UserAgentsList0.length);

        try {
            binding.browser.getSettings().setUserAgentString(iUtils.UserAgentsList0[randomnumber]);

        } catch (Exception e) {
            binding.browser.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36");
        }


        if (url.contains("facebook") || url.contains("fb.watch")) {

            int randomnumber1 = iUtils.getRandomNumber(iUtils.UserAgentsListOldDevices.length);

            try {
                binding.browser.getSettings().setUserAgentString(iUtils.UserAgentsListOldDevices[randomnumber1]);
                Log.d("TAG :", "Getlinkthrough_useragent: " + iUtils.UserAgentsListOldDevices[randomnumber1]);
            } catch (Exception e) {
                binding.browser.getSettings().setUserAgentString(iUtils.UserAgentsListOldDevices[0]+"");
            }

        }

        binding.browser.getSettings().setSupportMultipleWindows(true);


        binding.browser.setWebViewClient(new WebViewClient() {


//            @SuppressLint("WebViewClientOnReceivedSslError")
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed(); // Ignore SSL certificate errors
//            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);


                final String viewUrl = view.getUrl();

                if ((url.contains("facebook.com") || url.contains("fb.watch")))
                    Log.d("TAG :", "onLoadResourceFB url: " + url);

                if (url.contains("instagram")) {

                    Log.d("TAG :", "onLoadResource url: " + url);


                    if (url.contains(".mp4")) {
                        Log.d("TAG", "onLoadResource: view url" + url);
                        String title = view.getTitle();
                        Log.d("TAG", "onLoadResource: title" + title);

                        VideoModel videoModelImage = new VideoModel();
                        videoModelImage.setUrl(url);
                        videoModelImage.setTitle(title);
                        videoModelImage.setType(".mp4");

                        videoModelArrayList.add(videoModelImage);
                    }

                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        binding.browser.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                getWindow().setTitle(title); //Set Activity tile to page title.
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
            }


            @Override
            public void onProgressChanged(WebView view1, int newProgress) {


                if (newProgress >= 100) {


                    runnable = () -> {

                        try {
                            url = view1.getUrl();

                            if (url.contains("audiomack")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('audio')[0].getAttribute(\"src\"));");
                            } else if ((url.contains("facebook.com") || url.contains("fb.watch"))) {

                                view1.evaluateJavascript("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()", value -> {
                                    Log.d("TAG :", "videoModelArrayList Video Played: " + value);

                                    binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");
                                });
                            } else if (url.contains("tiki")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                            } else if (url.contains("instagram")) {

                                view1.evaluateJavascript("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()", value -> {
                                    Log.d("TAG :", "videoModelArrayList Video Played: " + value);


                                    if (videoModelArrayList != null && videoModelArrayList.size() > 0) {

                                        if (handler != null && runnable != null) {
                                            handler.removeCallbacks(runnable);
                                        }

                                        for (int i = 0; i < videoModelArrayList.size(); i++) {
                                            Log.d("TAG :", "videoModelArrayList urls: " + videoModelArrayList.get(i).getUrl());

                                            DownloadFileMain.startDownloading(GetLinkThroughWebView.this, videoModelArrayList.get(i).getUrl(), "Instagram_Web_" + System.currentTimeMillis(), videoModelArrayList.get(i).getType());
                                        }

                                        setIntentResultData(true);
                                    }
                                });


//                                else {
//                                    setIntentResultData(false);
//                                }

                            } else if (url.contains("ok.ru")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('source')[0].getAttribute(\"src\"));");

                            } else if (url.contains("zili")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                            } else if (url.contains("bemate")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                            } else if (url.contains("byte.co")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[1].getAttribute(\"src\"));");

                            } else if (url.contains("vidlit")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('source')[0].getAttribute(\"src\"));");

                            } else if (url.contains("veer.tv")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                            } else if (url.contains("fthis.gr")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('source')[0].getAttribute(\"src\"));");

                            } else if (url.contains("fw.tv") || url.contains("firework.tv")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('source')[0].getAttribute(\"src\"));");

                            } else if (url.contains("traileraddict")) {

                                binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");

                            } else {
                                setIntentResultData(false);
                            }

                            handler.postDelayed(runnable, 3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                            setIntentResultData(false);

                        }
                    };

                    handler.postDelayed(runnable, 2000);


                }

            }
        });

        binding.browser.loadUrl(url);

        webViewLightDark();
    }


    private void webViewLightDark() {

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    WebSettingsCompat.setForceDark(binding.browser.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                }
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    WebSettingsCompat.setForceDark(binding.browser.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
                }
                break;

            default:
                //
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }

    }


    public void setIntentResultData(boolean isDone) {


        if (!isFinishing() && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }

        if (!isDone) {
            iUtils.ShowToast(GetLinkThroughWebView.this, getResources().getString(R.string.somthing));
        }
        Intent intent = new Intent();
        setResult(2, intent);
        finish();

    }

    public class ParseM3u8 extends AsyncTask<Object, Integer, ArrayList<VideoModel>> {
        private final JSONObject emp = null;
        String DailyMotionUrl;
        private Document doc;
        private FrameLayout dwn_lyt;
        private Elements scriptElements;

        ParseM3u8(String str) {
            this.DailyMotionUrl = str;
        }

        protected ArrayList<VideoModel> doInBackground(Object... objArr) {
            String str = "TAG";
            ArrayList<VideoModel> arrayList = new ArrayList<>();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.DailyMotionUrl).openConnection();
                httpURLConnection.setConnectTimeout(60000);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                HashMap hashMap = null;
                Pattern compile = Pattern.compile("\\d+");
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    } else if (readLine.equals("#EXTM3U")) {
                        hashMap = new HashMap();
                    } else if (readLine.contains("#EXT-X-STREAM-INF")) {
                        Matcher matcher = compile.matcher(readLine);
                        matcher.find();
                        if (hashMap != null) {
                            hashMap.put(readLine, Integer.parseInt(Objects.requireNonNull(matcher.group(0))));
                            VideoModel videoModel = new VideoModel();
                            if (readLine.contains("PROGRESSIVE-URI")) {
                                String[] split = readLine.split(",");
                                String[] split2 = split[3].split("=");
                                String str2 = split[0];
                                split = split[5].split("=");
                                //videoModel.setName(Uri.parse(split[1].substring(1)).getLastPathSegment());
                                videoModel.setQuality(split2[1].replace("\"", ""));
                                videoModel.setUrl(split[1].substring(1));
                                String stringBuilder = "PROGRESSIVE-URI: " +
                                        split[1].substring(1);
                                Log.e(str, stringBuilder);
                                videoModel.setSize(String.valueOf(iUtils.getRemoteFileSize(split[1].substring(1))));
                                arrayList.add(videoModel);
                            }
                        }
                    }
                }
                bufferedReader.close();
            } catch (Exception objArr2) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("doInBackground: ");
                stringBuilder2.append(objArr2.getMessage());
                objArr2.printStackTrace();
            }
            return arrayList;
        }


        public ArrayList<VideoModel> removeDuplicates(ArrayList<VideoModel> arrayList) {
            TreeSet treeSet = new TreeSet(new Comparator<VideoModel>() {
                public int compare(VideoModel videoModel, VideoModel videoModel2) {
                    return videoModel.getQuality().equalsIgnoreCase(videoModel2.getQuality()) ? 0 : 1;
                }
            });
            treeSet.addAll(arrayList);
            return new ArrayList<>(treeSet);
        }


        protected void onPostExecute(ArrayList<VideoModel> arrayList) {
            super.onPostExecute(arrayList);
            if (arrayList != null) {

            }
            GetLinkThroughWebView.this.videoModelArrayList = this.removeDuplicates(arrayList);
        }
    }

    class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void showHTML(final String myurl, final String html) {
            try {
                progressDialog.dismiss();

                System.out.println("myhtml res =" + html);
                if (!isOnetime) {
                    isOnetime = true;

                    handler.removeCallbacks(runnable);

                    if (myurl.contains("audiomack")) {

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Audiomack_" + System.currentTimeMillis(), ".mp3");

                    } else if (myurl.contains("tiki")) {
                        String jj = "";
                        if (html.contains("_4.mp4")) {
                            jj = html.replace("_4.mp4", ".mp4");
                        } else {
                            jj = html;
                        }

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, jj, "tikiapp_" + System.currentTimeMillis(), ".mp4");

                    } else if (myurl.contains("zili")) {

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Zilivideo_" + System.currentTimeMillis(), ".mp4");

                    } else if (myurl.contains("instagram")) {

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Instagram_web_" + System.currentTimeMillis(), ".mp4");

                    } else if ((myurl.contains("facebook.com") || myurl.contains("fb.watch"))) {

                        System.out.println("facebookids urls is " + html);

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Facebook_" + System.currentTimeMillis(), ".mp4");

                    } else if (myurl.contains("ok.ru")) {

                        System.out.println("myhtml resbefd =" + html);

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "OKRU_" + System.currentTimeMillis(), ".mp4");

                    } else if (myurl.contains("bemate")) {

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Bemate_" + System.currentTimeMillis(), ".mp4");

                    } else if (myurl.contains("byte.co")) {

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Byte_" + System.currentTimeMillis(), ".mp4");

                    } else if (myurl.contains("vidlit")) {

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Vidlit_" + System.currentTimeMillis(), ".mp4");

                    } else if (myurl.contains("veer.tv")) {

                        String jj = html.replace("&amp;", "&");

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, jj, "Veer_" + System.currentTimeMillis(), ".mp4");
                    } else if (myurl.contains("fthis.gr")) {

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Fthis_" + System.currentTimeMillis(), ".mp4");

                    } else if (myurl.contains("fw.tv") || myurl.contains("firework.tv")) {

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Firework_" + System.currentTimeMillis(), ".mp4");

                    } else if (myurl.contains("traileraddict")) {

                        DownloadFileMain.startDownloading(GetLinkThroughWebView.this, html, "Traileraddict_" + System.currentTimeMillis(), ".mp4");

                    } else {

                        setIntentResultData(false);

                    }

                    System.out.println("htmlissss vid_url=" + html + " url=" + myurl);

                    setIntentResultData(true);

                } else {
                    if (handler != null && runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                setIntentResultData(false);

            }
        }
    }

}