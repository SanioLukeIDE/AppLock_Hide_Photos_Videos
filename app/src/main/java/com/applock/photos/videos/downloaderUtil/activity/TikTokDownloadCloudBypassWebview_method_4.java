package com.applock.photos.videos.downloaderUtil.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityTikTokDownloadWebviewBinding;
import com.applock.photos.videos.downloaderUtil.utils.iUtils;
import com.applock.photos.videos.downloaderUtil.utils.DownloadFileMain;

public class TikTokDownloadCloudBypassWebview_method_4 extends AppCompatActivity {


    public static Handler handler;
    static String myvidintenturlis = "";
    private static ValueCallback<Uri[]> mUploadMessageArr;
    String TAG = "whatsapptag";
    boolean isdownloadstarted = false;
    ProgressDialog progressDialog;
    private ActivityTikTokDownloadWebviewBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityTikTokDownloadWebviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.tool12);

        InitHandler();

        try {


            progressDialog = new ProgressDialog(TikTokDownloadCloudBypassWebview_method_4.this);
            progressDialog.setMessage(getString(R.string.nodeifittakeslonger));
            progressDialog.show();
        } catch (Exception e) {
            Log.d("error", "" + e.getLocalizedMessage());

        }
        if (getIntent().getStringExtra("myvidurl") != null && !getIntent().getStringExtra("myvidurl").equals("")) {
            myvidintenturlis = getIntent().getStringExtra("myvidurl");
        }
        binding.opentiktok.setOnClickListener(view1 -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiktok.com/"));

                intent.setPackage("com.zhiliaoapp.musically");

                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                iUtils.ShowToast(TikTokDownloadCloudBypassWebview_method_4.this, "Tiktok not Installed");
            }
        });


        binding.webViewscan.clearFormData();
        binding.webViewscan.getSettings().setSaveFormData(true);
        // binding.webViewscan.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
        binding.webViewscan.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        // binding.webViewscan.setWebChromeClient(new webChromeClients());
        binding.webViewscan.setWebViewClient(new MyBrowser());

        binding.webViewscan.getSettings().setAllowFileAccess(true);
        binding.webViewscan.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        binding.webViewscan.getSettings().setJavaScriptEnabled(true);
        binding.webViewscan.getSettings().setDefaultTextEncodingName("UTF-8");
        binding.webViewscan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        binding.webViewscan.getSettings().setDatabaseEnabled(true);
        binding.webViewscan.getSettings().setBuiltInZoomControls(false);
        binding.webViewscan.getSettings().setSupportZoom(true);
        binding.webViewscan.getSettings().setUseWideViewPort(true);
        binding.webViewscan.getSettings().setDomStorageEnabled(true);
        binding.webViewscan.getSettings().setAllowFileAccess(true);
        binding.webViewscan.getSettings().setLoadWithOverviewMode(true);
        binding.webViewscan.getSettings().setLoadsImagesAutomatically(true);
        binding.webViewscan.getSettings().setBlockNetworkImage(false);
        binding.webViewscan.getSettings().setBlockNetworkLoads(false);
        binding.webViewscan.getSettings().setLoadWithOverviewMode(true);


        binding.webViewscan.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });
        binding.webViewscan.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {

            String nametitle = "Tiktok_video_" +
                    System.currentTimeMillis();

            DownloadFileMain.startDownloading(TikTokDownloadCloudBypassWebview_method_4.this, url, nametitle, ".mp4");


        });

        binding.webViewscan.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && binding.progressBar.getVisibility() == View.GONE) {
                    binding.progressBar.setVisibility(View.VISIBLE);

                }

                binding.progressBar.setProgress(progress);
                if (progress == 100) {
                    binding.progressBar.setVisibility(View.GONE);

                }
            }
        });

//            Cloudflare cf = new Cloudflare(TikTokDownloadCloudBypassWebview_method_4.this, "https://ttdownloader.com/?url=" + myvidintenturlis);
//            //   cf.setUser_agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
//            cf.setUser_agent(binding.webViewscan.getSettings().getUserAgentString());
//            cf.setCfCallback(new CfCallback() {
//                @Override
//                public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
//
//                    binding.webViewscan.loadUrl(newUrl);
//
//
//                }
//
//                @Override
//                public void onFail(int code, String msg) {
        // Toast.makeText(TikTokDownloadCloudBypassWebview_method_4.this, "" + msg, Toast.LENGTH_SHORT).show();
        binding.webViewscan.loadUrl("https://ttdownloader.com/");

//                }
//            });
//            cf.getCookies();

    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001) {
            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(i2, intent));
            mUploadMessageArr = null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = true;
        if (keyCode == 4) {
            try {
                if (binding.webViewscan.canGoBack()) {
                    binding.webViewscan.goBack();
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finishAndDismiss();
        z = super.onKeyDown(keyCode, event);
        return z;
    }

    @SuppressLint({"WrongConstant"})
    public void onBackPressed() {

        finishAndDismiss();
        super.onBackPressed();

    }

    public void onPause() {
        super.onPause();
        try {
            binding.webViewscan.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            binding.webViewscan.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStop() {
        try {
            binding.webViewscan.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    public boolean onSupportNavigateUp() {
        finishAndDismiss();
        return true;
    }

    @SuppressLint({"HandlerLeak"})
    private void InitHandler() {
        handler = new btnInitHandlerListner();
    }

    @SuppressLint("HandlerLeak")
    private class btnInitHandlerListner extends Handler {
        @SuppressLint({"SetTextI18n"})
        public void handleMessage(Message msg) {
        }
    }

    private class webChromeClients extends WebChromeClient {
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.e("CustomClient", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }

    private class MyBrowser extends WebViewClient {
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            binding.progressBar.setVisibility(View.VISIBLE);
            Log.e(TAG, "binding.progressBar");
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "binding.progressBar GONE");
            binding.progressBar.setVisibility(View.GONE);


            String jsscript = "javascript:(function() { "

                    + "document.getElementById(\"url\").value ='" + myvidintenturlis + "';"
                    + "document.getElementById(\"submit\").click();"
                    //    + "await new Promise(resolve => setTimeout(resolve, 3000)); "
                    //  + "javascript:document.getElementsByClassName(\"pure-button pure-button-primary is-center u-bl dl-button download_link without_watermark_direct\").click(); "
                    + "})();";

            view.evaluateJavascript(jsscript, new ValueCallback() {
                public void onReceiveValue(Object obj) {
                    Log.e(TAG, "binding.progressBar reciveing data " + obj.toString());

                    try {


                        Handler handler1 = new Handler();

                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Log.e(TAG, "binding.progressBar reciveing data executed 1");


                                //    binding.webViewscan.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('audio')[0].getAttribute(\"src\"));");


                                view.evaluateJavascript("javascript:document.getElementsByClassName('download-link')[0].getAttribute('href')", obj -> {
                                    Log.e(TAG, "binding.progressBar reciveing data download " + obj);
                                    if (obj != null && obj.contains("http")) {
                                        Log.e(TAG, "binding.progressBar reciveing data http " + obj);

                                        handler1.removeCallbacksAndMessages(null);

                                        if (!isdownloadstarted) {
                                            DownloadFileMain.startDownloading(TikTokDownloadCloudBypassWebview_method_4.this, obj, "Tiktok_" + System.currentTimeMillis(), ".mp4");
                                            isdownloadstarted = true;
                                        }

                                        //  startActivity(new Intent(TikTokDownloadWebview.this,MainActivity.class));
                                        finishAndDismiss();
                                    }


                                });

                                handler1.postDelayed(this, 2000);

                            }
                        }, 2000);
                    } catch (Exception e) {

                        finishAndDismiss();
                    }
                }
            });



        }
    }

    void finishAndDismiss() {
        try {
            if (progressDialog != null && progressDialog.isShowing() && !isFinishing()) {
                progressDialog.dismiss();
            }
            finish();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
