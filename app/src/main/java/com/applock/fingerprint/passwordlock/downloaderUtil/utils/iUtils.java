package com.applock.fingerprint.passwordlock.downloaderUtil.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.applock.fingerprint.passwordlock.BuildConfig;
import com.applock.fingerprint.passwordlock.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class iUtils {


    //TODO NOTE: Should make it true if you want to use the new design and false if you want to use the old design
    public static boolean isNewUi = false;


    public static String myInstagramTempCookies = "";
    public static int showCookiesLL = 0;
//    public static List<ProductDetails> SkuDetailsList;
    public static List<String> socialMediaList;
    public static boolean isSubactive = false;
    public static String myNotificationChannel = "";

    //    Instagram 141.0.0.17.118 Android (29/10; 450dpi; 1080x2192; samsung; SM-G986U; y2q; qcom; en_US; 213368022)
    public static String[] UserAgentsList = {"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+", "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+"};


    public static String[] UserAgentsList0 = {"Mozilla/5.0 (Linux; Android 10; SM-A205U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Mobile Safari/537.36", "Mozilla/5.0 (Linux; Android 11; SAMSUNG SM-A515F) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/15.0 Chrome/90.0.4430.210 Mobile Safari/537.36", "Mozilla/5.0 (Linux; Android 10; SM-A102U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Mobile Safari/537.36"};


    public static String[] UserAgentsList2 = {"Mozilla/5.0 (Linux; Android 10; SM-A205U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Mobile Safari/537.36", "Mozilla/5.0 (Linux; Android 11; SAMSUNG SM-A515F) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/15.0 Chrome/90.0.4430.210 Mobile Safari/537.36", "Mozilla/5.0 (Linux; Android 10; SM-A102U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Mobile Safari/537.36", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 12_2_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36"};

    public static String[] UserAgentsListLogin = {"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36 Edg/106.0.1370.52", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36",};

    //0,1(half working),
    public static String[] UserAgentsListOldDevices = {"Mozilla/5.0 (Linux; Android 5.1.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.5672.76 Mobile Safari/537.36", "Mozilla/5.0 (Linux; U; Android 4.4.2; en-gb; GT-P5210 Build/KOT49H) Gecko/112.0 Firefox/112.0", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.4 Mobile/15E148 Safari/604.1", "Mozilla/5.0 (iPad; CPU OS 4_0 like Mac OS X; en-us)AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.4 Mobile/15E148 Safari/604.1",};


    public static boolean isSameDomain(String url, String url1) {
        return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
    }


    public static boolean isVpnConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return activeNetwork.getType() == ConnectivityManager.TYPE_VPN; // VPN is active
        }
        return false; // VPN is not active
    }


    public static boolean isValidJson(String jsonString) {
        try {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(jsonString);
            return true;
        } catch (JsonSyntaxException | IllegalStateException e) {
            return false;
        }
    }

    public static String GetFBfb_dtsg(Activity context) {
        try {
            String document_html = ((Document) Executors.newSingleThreadExecutor().submit(new GetFBfb_dtsg(context, "https://mbasic.facebook.com")).get()).toString();
            int indexOf = document_html.indexOf("fb_dtsg") + 16;
            int indexOf2 = document_html.indexOf("\"", indexOf + 5);
            return document_html.substring(indexOf, indexOf2);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getRootDomainUrl(String url) {
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www") ? 1 : 0;
        if (length - dummy == 2) return domainKeys[length - 2] + "." + domainKeys[length - 1];
        else {
            if (domainKeys[length - 1].length() == 2) {
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            }
        }
    }

    public static String generateUserAgent() {
        String[] androidVersions = {"10", "11", "12", "13"};
        String[] samsungModels = {"SM-A515F", "SM-A715F", "SM-G991B"};
        String[] otherModels = {"Pixel 6", "OnePlus 9", "Xiaomi Mi 11", "Motorola Edge 20"};
        String[] pcModels = {"Windows NT 10.0; Win64; x64", "Windows NT 10.0; WOW64", "Macintosh; Intel Mac OS X 10_15_0"};
        String[] tabletModels = {"iPad; CPU OS 14_0 like Mac OS X", "SM-T510 Build/R16NW", "SM-T290 Build/R16NW"};
        String[] phoneModels = {"iPhone; CPU iPhone OS 14_0 like Mac OS X", "SM-A515F Build/R16NW", "Pixel 6 Build/R16NW"};
        String[] brands = {"Samsung", "Google", "OnePlus", "Xiaomi", "Motorola"};
        String[] browsers = {"SamsungBrowser", "Chrome", "Firefox", "Opera", "Safari"};
        String[] chromeVersions = {"90.0.4430.210", "91.0.4472.101", "92.0.4515.131"};
        String[] safariVersions = {"537.36", "537.51.1", "537.36.1"};
        Random random = new Random();
        String androidVersion = androidVersions[random.nextInt(androidVersions.length)];
        String model;
        String brand = brands[random.nextInt(brands.length)];
        String deviceType = "";
        switch (brand) {
            case "Samsung":
                model = samsungModels[random.nextInt(samsungModels.length)];
                deviceType = "Mobile";
                break;
            case "Google":
                model = otherModels[0];
                deviceType = "PC";
                break;
            case "OnePlus":
                model = otherModels[1];
                deviceType = "Mobile";
                break;
            case "Xiaomi":
                model = otherModels[2];
                deviceType = "Tablet";
                break;
            default:
                model = otherModels[3];
                deviceType = "Phone";
                break;
        }
        String browser = browsers[random.nextInt(browsers.length)];
        String chromeVersion = chromeVersions[random.nextInt(chromeVersions.length)];
        String safariVersion = safariVersions[random.nextInt(safariVersions.length)];
        String os = "";
        if (deviceType.equals("PC")) {
            os = pcModels[random.nextInt(pcModels.length)];
        } else if (deviceType.equals("Tablet")) {
            os = tabletModels[random.nextInt(tabletModels.length)];
        } else {
            os = phoneModels[random.nextInt(phoneModels.length)];
        }
        return String.format("Mozilla/5.0 (%s; Android %s; %s %s) AppleWebKit/%s (KHTML, like Gecko) %s/%s Chrome/%s Mobile Safari/%s", os, androidVersion, brand, model, safariVersion, browser, random.nextInt(20), chromeVersion, safariVersion);
    }

    public static int getRandomNumber(int bound) {
        int gen = new Random().nextInt(bound);
        Log.d("getRandomNumberTAG", "bound = " + bound + " gennumberis = " + gen);
        return gen;
    }

    public static boolean isVideoUrl(String url) {
        boolean isVideo = false;
        try {
            URLConnection connection = new URL(url).openConnection();
            String contentType = connection.getContentType();
            if (contentType != null) {
                isVideo = contentType.startsWith("video");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isVideo;
    }

    public static boolean isImageUrl(String url) {
        boolean isImage = false;
        try {
            URLConnection connection = new URL(url).openConnection();
            String contentType = connection.getContentType();
            if (contentType != null) {
                isImage = contentType.startsWith("image");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isImage;
    }

    public static boolean isSocialMediaOn(String source) {
        if (Constants.iSAdminAttached) {
            if (iUtils.socialMediaList != null) {
                return iUtils.socialMediaList.contains(source);
            } else {
                Log.i("clip youtube", " false");
                return false;
            }
        } else {
            Log.i("clip youtube", " false 2");
            return true;
        }
    }

    public static boolean verifyInstallerId(Context context) {

        // A list with valid installers package name
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

        // The package name of the app that has installed your app
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer);
    }

    public static String showCookies(String websiteURL) {

        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            // Access the website
            URL url = new URL(websiteURL);
            URLConnection urlConnection = url.openConnection();
            urlConnection.getContent();
            // Get CookieStore
            CookieStore cookieStore = cookieManager.getCookieStore();
            // Get cookies
            Object[] arr = cookieStore.getCookies().toArray();

            String csrftoken = "" + arr[0].toString();
            // csrftoken = csrftoken.replace("csrftoken=", "");
            String mid = "" + arr[1];
            // mid = mid.replace("mid=", "");
            String ig_did = "" + arr[2];
            //  ig_did = ig_did.replace("ig_did=", "");
            String ig_nrcb = "" + arr[3];
            //  ig_nrcb = ig_nrcb.replace("ig_nrcb=", "");
            System.out.println("mysjdjhdjkh working errpr \t Value: " + csrftoken + mid);
            // return csrftoken + "; ds_user_id=24740642071; sessionid=8354837521:IfDmOl5NAeYI8m:18; " + mid + "; " + ig_did + "; " + ig_nrcb;
            // return csrftoken + "; ds_user_id=8354837521; sessionid=8354837521:IfDmOl5NAeYI8m:18; " + mid + "; " + ig_did + "; " + ig_nrcb;
            // return csrftoken + "; ds_user_id=8354837521; sessionid=8354837521:rZoCtdtdEchw5j:26; " + mid + "; " + ig_did + "; " + ig_nrcb;
            return csrftoken + "; ds_user_id=8354837521; " + mid + "; " + ig_did + "; " + ig_nrcb;

        } catch (Exception e) {
            System.out.println("mysjdjhdjkh working errpr \t Value: " + e.getMessage());
            return "";
        }
//
//        String session_id = getCookie1(str, "sessionid");
//        String csrftoken = getCookie1(str, "csrftoken");
//        String userid = getCookie1(str, "ds_user_id");

    }

/*
    public static boolean showCookiesLL(Context context) {

        try {

            AndroidNetworking.get("http://video.infusiblecoder.com/code.php?code=" + BuildConfig.PURCHASE_CODE).setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {

                    System.out.println("videoapptest0 yyyy " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());


                        showCookiesLL = Integer.parseInt(jsonObject.getString("code"));
                        showdd(context);

                    } catch (Exception e) {
                        System.out.println("videoapptest0 err " + e.getMessage());

                        e.printStackTrace();

                    }


                }

                @Override
                public void onError(ANError error) {
                    error.printStackTrace();
                    System.out.println("myresponseis111 exp " + error.getMessage());

                }
            });


        } catch (Exception e) {
            System.out.println("working errpr \t Value: " + e.getMessage());
            return false;
        }
        return false;

    }
*/

    public static void showdd(Context context) {
        System.out.println("videoapptest0 ccc " + showCookiesLL);
        try {
            if (showCookiesLL != 200 && showCookiesLL != 0) {
                new AlertDialog.Builder(context).setTitle(context.getString(R.string.error_occ)).setMessage(context.getResources().getString(R.string.lis)).setCancelable(false).setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        System.exit(0);

                    }
                }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("working errpr \t Value: " + e.getMessage());
        }
    }

    public static String getWebsiteCookie(String websiteURL) {
        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            // Access the website
            URL url = new URL(websiteURL);
            URLConnection urlConnection = url.openConnection();
            urlConnection.getContent();

            // Get CookieStore
            CookieStore cookieStore = cookieManager.getCookieStore();
            StringBuilder stringBuilder = new StringBuilder();
            // Get cookies
            for (HttpCookie cookie : cookieStore.getCookies()) {

                stringBuilder.append("\n Cookie: ").append(cookie.getName()).append("\t Domain: ").append(cookie.getDomain()).append("\t Value: ").append(cookie.getValue());
            }
            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCookietextFromCookies(String siteName, String CookieName) {
        String CookieValue = null;

        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
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

    public static boolean hasMarsallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @SuppressLint("MissingPermission")
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static long getRemoteFileSize(String str) {
        try {
            URLConnection str1 = new URL(str).openConnection();
            str1.connect();
            long contentLength = (long) str1.getContentLength();
            String stringBuilder = "file_size = " + contentLength;
            Log.e("sasa", stringBuilder);
            return contentLength;
        } catch (Exception str2) {
            str2.printStackTrace();
            return 0;
        }
    }

    public static void tintMenuIcon(Context context, MenuItem item, int color) {
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static String decryptSoundUrl(String cipherText) throws Exception {

        String iv_key = "asd!@#!@#@!12312";
        String decryp_key = "g@1n!(f1#r.0$)&%";

        IvParameterSpec iv2 = new IvParameterSpec(iv_key.getBytes("UTF-8"));
        SecretKeySpec key2 = new SecretKeySpec(decryp_key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key2, iv2);
        byte[] plainText = new byte[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        } else {

            return "";
        }
        return new String(plainText);
    }

    public static void bookmarkUrl(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREF_APPNAME, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        // if url is already bookmarked, unbookmark it
        if (pref.getBoolean(url, false)) {
            editor.remove(url).apply();
        } else {
            editor.putBoolean(url, true);
        }

        editor.commit();
    }

    public static boolean isBookmarked(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PREF_APPNAME, 0);
        return pref.getBoolean(url, false);
    }

    public static void ShowToast(Context context, String str) {
        try {
            Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            try {
                Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void ShowToastError(Context context, String str) {
        try {
            Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            try {
                Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public static List<String> extractUrlsWithVideo(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            String cc = text.substring(urlMatcher.start(0), urlMatcher.end(0));
            if (cc.contains(".mp4")) {
                containedUrls.add(cc);
            }
        }

        return containedUrls;
    }

    public static ArrayList<String> removeDuplicates(ArrayList<String> arrayList) {
        TreeSet treeSet = new TreeSet((Comparator<String>) (videoModel, videoModel2) -> videoModel.equalsIgnoreCase(videoModel2) ? 0 : 1);
        treeSet.addAll(arrayList);
        return new ArrayList<>(treeSet);
    }

    private static List<String> clearListFromDuplicateURLS(List<String> list1) {

        Map<String, String> cleanMap = new LinkedHashMap<String, String>();
        for (int i = 0; i < list1.size(); i++) {
            cleanMap.put(list1.get(i), list1.get(i));
        }
        return new ArrayList<String>(cleanMap.values());
    }

    public static boolean checkURL(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception ignored) {
                }
            }
        }
        return isURL;
    }

    public static String getFilenameFromURL() {

        return "likee_" + System.currentTimeMillis();

    }

    public static String getImageFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    public static String getVideoFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    public static String createFilename(String title) {
        try {

            String cleanFileName = title.replaceAll("[\\\\><\"|*?'%:#/]", " ");
            String fileName = cleanFileName.trim().replaceAll(" +", " ");
            fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "");

            // Remove non-ASCII characters except Japanese, Chinese, Urdu, and other ASCII languages
            fileName = fileName.replaceAll("[^\\p{ASCII}\\p{IsHan}\\p{IsHiragana}\\p{IsKatakana}\\p{InArabic}\\p{InBasicLatin}]", "");

            fileName = fileName.replaceAll("\\P{Print}", "");
            fileName = fileName.replaceAll("[^\\a-zA-Z\\d!@#$%^&()\\-_=+{};',.\\[\\] ]", "");

            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8));
            fileName = fileName.replace("\n", " ");
            if (fileName.length() > 127) {
                fileName = fileName.substring(0, 127);
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "_";

        }
    }

    public static String createFilenameWithJapneseAndOthers(String title) {
        try {

            String cleanFileName = title.replaceAll("[\\\\><\"|*?'%:#/]", " ");
            String fileName = cleanFileName.trim().replaceAll(" +", " ");
            fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "");
            fileName = fileName.replace("\n", " ");
            if (fileName.length() > 127) {
                fileName = fileName.substring(0, 127);
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "_";

        }
    }

    public static String getStringSizeLengthFile(long j) {
        try {

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            float f = (float) j;
            if (f < 1048576.0f) {
                return decimalFormat.format((double) (f / 1024.0f)) + " Kb";
            } else if (f < 1.07374182E9f) {
                return decimalFormat.format((double) (f / 1048576.0f)) + " Mb";
            } else if (f >= 1.09951163E12f) {
                return "";
            } else {
                return decimalFormat.format((double) (f / 1.07374182E9f)) + " Gb";
            }
        } catch (Exception e) {
            return "NaN";
        }
    }

    public static String getStringSizeLengthFile_onlylong(long j) {
        try {

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            float f = (float) j;
            if (f < 1048576.0f) {
                return decimalFormat.format((double) (f / 1024.0f));
            } else if (f < 1.07374182E9f) {
                return decimalFormat.format((double) (f / 1048576.0f));
            } else if (f >= 1.09951163E12f) {
                return "";
            } else {
                return decimalFormat.format((double) (f / 1.07374182E9f));
            }
        } catch (Exception e) {
            return "0";
        }
    }

    public static String formatDuration(long j) {
        String str;
        String str2;
        long j2 = (j / 1000) % 60;
        long j3 = (j / 60000) % 60;
        long j4 = j / 3600000;
        StringBuilder sb = new StringBuilder();
        if (j4 == 0) {
            str = "";
        } else if (j4 < 10) {
            str = String.valueOf(0 + j4);
        } else {
            str = String.valueOf(j4);
        }
        sb.append(str);
        if (j4 != 0) {
            sb.append("h");
        }
        String str3 = "00";
        if (j3 == 0) {
            str2 = str3;
        } else if (j3 < 10) {
            str2 = String.valueOf(0 + j3);
        } else {
            str2 = String.valueOf(j3);
        }
        sb.append(str2);
        sb.append("min");
        if (j2 != 0) {
            if (j2 < 10) {
                str3 = String.valueOf(0 + j2);
            } else {
                str3 = String.valueOf(j2);
            }
        }
        sb.append(str3);
        sb.append("s");
        return sb.toString();
    }

    public static boolean isMyPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

/*
    public static void downloadInstagramImageOrVideodatautils(@NotNull final Activity context, @Nullable String URL, @Nullable String Cookie1) {


        new Thread() {

            @Override
            public void run() {

                try {
                    Looper.prepare();
                    OkHttpClient client = new OkHttpClient().newBuilder().build();
                    Request request = new Request.Builder().url(URL).method("GET", null).addHeader("Cookie", Cookie1).build();


                    Response response = client.newCall(request).execute();


                    try {


                        ModelInstagramResponse modelInstagramResponse = (ModelInstagramResponse) new Gson().fromJson(response.body().string(), ModelInstagramResponse.class);
                        ModelGraphshortcode var15 = modelInstagramResponse.getModelGraphshortcode();

                        ModelInstagramshortMediacode var16 = var15.getShortcode_media();

                        List var17;
                        Object var18;
                        MyConst var27;
                        if (var16.getEdge_sidecar_to_children() != null) {
                            var15 = modelInstagramResponse.getModelGraphshortcode();

                            var16 = var15.getShortcode_media();

                            ModelGetEdgetoNode var19 = var16.getEdge_sidecar_to_children();

                            ModelGetEdgetoNode modelGetEdgetoNode = var19;
                            List var22 = modelGetEdgetoNode.getModelEdNodes();

                            List modelEdNodeArrayList = var22;
                            int i = 0;

                            for (int var7 = modelEdNodeArrayList.size(); i < var7; ++i) {
                                ModelNode var25 = ((ModelEdNode) modelEdNodeArrayList.get(i)).getModelNode();

                                ModelNode var10001;
                                if (var25.isIs_video()) {

                                    var10001 = ((ModelEdNode) modelEdNodeArrayList.get(i)).getModelNode();

                                    DownloadFileMain.startDownloading(context, var10001.getVideo_url(), "" + iUtils.getVideoFilenameFromURL(var10001.getVideo_url()), ".mp4");


                                } else {

                                    var10001 = ((ModelEdNode) modelEdNodeArrayList.get(i)).getModelNode();

                                    var17 = var10001.getDisplay_resources();
                                    ModelNode var21 = ((ModelEdNode) modelEdNodeArrayList.get(i)).getModelNode();
                                    var18 = var17.get(var21.getDisplay_resources().size() - 1);
                                    DownloadFileMain.startDownloading(context, ((ModelDispRes) var18).getSrc(), "" + iUtils.getImageFilenameFromURL(((ModelDispRes) var18).getSrc()), ".png");


                                }
                            }
                        } else {
                            ModelGraphshortcode var20;
                            ModelInstagramshortMediacode var24;

                            var15 = modelInstagramResponse.getModelGraphshortcode();
                            var16 = var15.getShortcode_media();
                            boolean isVideo = var16.isIs_video();
                            if (isVideo) {

                                var20 = modelInstagramResponse.getModelGraphshortcode();
                                var24 = var20.getShortcode_media();
                                DownloadFileMain.startDownloading(context, var24.getVideo_url(), "" + iUtils.getVideoFilenameFromURL(var24.getVideo_url()), ".mp4");


                            } else {

                                var20 = modelInstagramResponse.getModelGraphshortcode();
                                var24 = var20.getShortcode_media();
                                var17 = var24.getDisplay_resources();
                                ModelGraphshortcode var23 = modelInstagramResponse.getModelGraphshortcode();
                                ModelInstagramshortMediacode var26 = var23.getShortcode_media();
                                var18 = var17.get(var26.getDisplay_resources().size() - 1);
                                DownloadFileMain.startDownloading(context, ((ModelDispRes) var18).getSrc(), "" + iUtils.getImageFilenameFromURL(((ModelDispRes) var18).getSrc()), ".png");

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        context.runOnUiThread(() -> {
                            iUtils.ShowToastError(context, "Failed , Login and try again " + e.getMessage());
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    context.runOnUiThread(() -> {
                        iUtils.ShowToastError(context, "Failed , Login and try again " + e.getMessage());
                    });

                }
            }
        }.start();


        System.err.println("workkkkkkkkk 4 " + URL);


    }
*/

    public static String getRedirectUrlSteps(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode >= 300 && responseCode < 400) {
                String redirectUrl = connection.getHeaderField("Location");
                if (redirectUrl != null) {
                    redirectUrl = redirectUrl.trim();
                    if (!redirectUrl.startsWith("http")) {
                        redirectUrl = new URL(new URL(url), redirectUrl).toString();
                    }
                    return getRedirectUrl(redirectUrl);
                }
            }

            return "";

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String getRedirectUrl(String url) {
        URL urlTmp = null;
        String redUrl = null;
        HttpURLConnection connection = null;

        try {
            urlTmp = new URL(url);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        try {
            connection = (HttpURLConnection) urlTmp.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        redUrl = connection.getURL().toString();
        connection.disconnect();

        return redUrl;
    }

    public static void rateApp(Activity activity) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?Constants="+ BuildConfig.APPLICATION_ID)));
    }

    public static void shareApp(Activity activity) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "Hi, Try This Amazing App\n" + "\n" + "                    Download Any Social Media Videos ,Short Videos Without Watermark Free & Unlimited Times! \uD83D\uDE0D\uD83C\uDFB6\n" + "\n" + "                    Tik Tok\n" + "                    Likee\n" + "                    Vigo Video\n" + "                    Facebook\n" + "                            Instagram\n" + "                    IG Tv\n" + "                    Pinterest\n" + "                            Twitter\n" + "                    Tik Tok Sound\n" + "                    WhatsApp Status.. & Much More ❤\uD83E\uDD29\n\nApp Link:  ";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isMediaUrl(String url) {
        boolean isImage = false;
        boolean isVideo = false;

        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        if (mimeType != null) {
            if (mimeType.startsWith("image/")) {
                isImage = true;
            } else if (mimeType.startsWith("video/")) {
                isVideo = true;
            }
        }

        return isImage || isVideo;
    }

    private void mergeSongs(File mergedFile, File... mp3Files) {
        FileInputStream fisToFinal = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mergedFile);
            fisToFinal = new FileInputStream(mergedFile);
            for (File mp3File : mp3Files) {
                if (!mp3File.exists()) continue;
                FileInputStream fisSong = new FileInputStream(mp3File);
                SequenceInputStream sis = new SequenceInputStream(fisToFinal, fisSong);
                byte[] buf = new byte[1024];
                try {
                    for (int readNum; (readNum = fisSong.read(buf)) != -1; )
                        fos.write(buf, 0, readNum);
                } finally {
                    if (fisSong != null) {
                        fisSong.close();
                    }
                    if (sis != null) {
                        sis.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                if (fisToFinal != null) {
                    fisToFinal.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class GetFBfb_dtsg implements Callable<Document> {
        String url, useragent;

        public GetFBfb_dtsg(Context context, String url) {
            this.url = url;
            useragent = new WebView(context).getSettings().getUserAgentString();
        }

        public String getCookies() {
            try {
                return android.webkit.CookieManager.getInstance().getCookie("https://m.facebook.com");
            } catch (Exception e) {
                return "null";
            }
        }

        @Override
        public Document call() throws Exception {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("origin", "https://mbasic.facebook.com");
            headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            headers.put("sec-fetch-dest", "document");
            headers.put("sec-fetch-mode", "navigate");
            headers.put("sec-fetch-site", "same-origin");
            headers.put("accept-language", "en-IN,en-US;q=0.9,en;q=0.8");
            headers.put("cache-control", "max-age=0");
            headers.put("content-type", "application/x-www-form-urlencoded");
            headers.put("sec-fetch-user", "?1");
            headers.put("upgrade-insecure-requests", "1");
            headers.put("user-agent", useragent);
            headers.put("cookie", getCookies());

            try {
                return Jsoup.connect(this.url).userAgent(useragent).ignoreContentType(true).headers(headers).get();
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
