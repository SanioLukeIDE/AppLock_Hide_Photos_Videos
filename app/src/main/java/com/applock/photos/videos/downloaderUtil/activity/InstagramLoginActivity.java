package com.applock.photos.videos.downloaderUtil.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.applock.photos.videos.R;
import com.applock.photos.videos.databinding.ActivityInstagramLoginBinding;
import com.applock.photos.videos.downloaderUtil.models.ModelInstagramPref;
import com.applock.photos.videos.downloaderUtil.utils.SharedPrefsForInstagram;
import com.applock.photos.videos.downloaderUtil.utils.iUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InstagramLoginActivity extends AppCompatActivity {

    private ActivityInstagramLoginBinding binding;
    Map<String, String> extraHeaders = new HashMap<String, String>();
    int randomnumber = 0;

    //challenge url
//    https://i.instagram.com/challenge/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstagramLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.logintoinstatitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        randomnumber = iUtils.getRandomNumber(iUtils.UserAgentsListLogin.length);
        extraHeaders.put("x-requested-with", "XMLHttpRequest");
        extraHeaders.put("HTTP_X-Requested-With", "com.android.chrome");
        LoadPage();
        binding.swipeRefreshLayout.setOnRefreshListener(this::LoadPage);

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPage() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.clearCache(true);


        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true);
        binding.webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        binding.webView.getSettings().setDatabaseEnabled(true);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setAllowFileAccess(true);
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setBlockNetworkImage(false);
        binding.webView.getSettings().setBlockNetworkLoads(false);
        binding.webView.getSettings().setLoadWithOverviewMode(true);

        CookieSyncManager.createInstance(InstagramLoginActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();


        try {

            binding.webView.getSettings().setUserAgentString(iUtils.UserAgentsListLogin[randomnumber] + "");

        } catch (Exception e) {
            System.out.println("dsakdjasdjasd " + e.getMessage());

            binding.webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36");

        }


        binding.webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("chromium-A-INSTA", consoleMessage.message());
                return true;

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                getWindow().setTitle(title); //Set Activity tile to page title.
            }

            public void onProgressChanged(WebView view, int progress) {
                binding.swipeRefreshLayout.setRefreshing(progress != 100);
            }
        });

        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.loadUrl("http://www.instagram.com/accounts/login", extraHeaders);


    }


    public String getCookie(String siteName, String CookieName) {
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        if (cookies != null && !cookies.isEmpty()) {
            String[] temp = cookies.split(";");
            for (String ar1 : temp) {
                if (ar1.contains(CookieName)) {
                    String[] temp1 = ar1.split("=");
                    CookieValue = temp1[1];
                    break;
                }
            }
        }
        return CookieValue;
    }

    private class MyWebViewChromeClient extends WebChromeClient {


        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            if (newProgress >= 100) {

                String str = view.getUrl();
                String cookies = CookieManager.getInstance().getCookie(str);

                try {
                    String session_id = getCookie(str, "sessionid");
                    String csrftoken = getCookie(str, "csrftoken");
                    String userid = getCookie(str, "ds_user_id");
                    if (session_id != null && csrftoken != null && userid != null) {


                        ModelInstagramPref instagramPref = new ModelInstagramPref(session_id, userid, cookies, csrftoken, "true");
                        SharedPrefsForInstagram sharedPrefsForInstagram = new SharedPrefsForInstagram(InstagramLoginActivity.this);

                        sharedPrefsForInstagram.setPreference(instagramPref);


                        view.destroy();
                        Intent intent = new Intent();
                        intent.putExtra("result", "result");
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView1, String url) {
            try {


                webView1.getSettings().setUserAgentString(iUtils.UserAgentsListLogin[randomnumber] + "");
            } catch (Exception e) {
                System.out.println("dsakdjasdjasd " + e.getMessage());
                webView1.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36 Edg/106.0.1370.52");
            }

            webView1.loadUrl(url, extraHeaders);
            return true;
        }

        @Override
        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }


        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);

            String cookies = CookieManager.getInstance().getCookie(str);

            try {
                String session_id = getCookie(str, "sessionid");
                String csrftoken = getCookie(str, "csrftoken");
                String userid = getCookie(str, "ds_user_id");
                if (session_id != null && csrftoken != null && userid != null) {


                    ModelInstagramPref instagramPref = new ModelInstagramPref(session_id, userid, cookies, csrftoken, "true");
                    SharedPrefsForInstagram sharedPrefsForInstagram = new SharedPrefsForInstagram(InstagramLoginActivity.this);

                    sharedPrefsForInstagram.setPreference(instagramPref);


                    webView.destroy();
                    Intent intent = new Intent();
                    intent.putExtra("result", "result");
                    setResult(RESULT_OK, intent);
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
    }
}