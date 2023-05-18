package com.applock.photos.videos.utils;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.applock.photos.videos.R;

import java.io.File;

public class Utils {
    public static Dialog customDialog;
    private static Context context;

    public static String RootDirectoryFacebook = "/StatusSaver/Facebook/";
    public static String RootDirectoryInsta = "/StatusSaver/Insta/";
    public static String RootDirectoryTikTok = "/StatusSaver/TikTok/";
    public static String RootDirectoryTwitter = "/StatusSaver/Twitter/";
    public static String RootDirectoryShareChat = "/StatusSaver/ShareChat/";
    public static final String ROOTDIRECTORYCHINGARI = "/StatusSaver/Chingari/";
    public static final File ROOTDIRECTORYJOSHSHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Josh");
    public static final File ROOTDIRECTORYCHINGARISHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Chingari");
    public static final File ROOTDIRECTORYMITRONSHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Mitron");
    public static final File ROOTDIRECTORYMXSHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Mxtakatak");
    public static final File ROOTDIRECTORYMOJSHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Moj");
    public static File RootDirectoryFacebookShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Facebook");
    public static File RootDirectoryInstaShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Insta");
    public static File RootDirectoryTikTokShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/TikTok");
    public static File RootDirectoryTwitterShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Twitter");
    public static File RootDirectoryShareChatShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/ShareChat");
    public static Uri wa_path = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses");


    public static Uri wa_business_path = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses");
    public static Uri wa_gb_path = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GBWhatsApp/Media/.Statuses");

    public static Uri wa_businnes_status_uri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business%2FMedia%2F.Statuses");


    public static Uri wa_status_uri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses");

    public static final String WA_BUSINESS_PACKAGE_NAME = "com.whatsapp.w4b";
    public static final String WA_PACKAGE_NAME = "com.whatsapp";

  public static File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_PICTURES + "/WPTools/WhatsAppStatusSaverr");
    public static File RootDirectoryLikeeShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Likee");
   // public static String RootDirectoryInsta = "/StatusSaver/Insta/";

    public static String PrivacyPolicyUrl = "http://codingdunia.com/ccprojects/statussaver/privacy_policy.html";
    public static String TikTokUrl = "http://codingdunia.com/ccprojects/tiktok/api/getContent/";
    public Utils(Context _mContext) {
        context = _mContext;
    }

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isWhatsAppInstall(Context context) {
        context.getPackageManager();
        try {
            boolean bl = Utils.isAppInstalled(context, WA_PACKAGE_NAME);
            return bl;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static void infoDialog(Context context, String title, String msg) {
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(msg)
                .setPositiveButton(context.getResources().getString(R.string.ok),
                        (dialog, which) -> dialog.dismiss()).create().show();
    }

    public static boolean isAppInstalled(Context context, String string2) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN", null);
            for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
                boolean bl = resolveInfo.activityInfo.packageName.equals((Object) string2);
                if (!bl) continue;
                return true;
            }
            return false;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean isBusinessWhatsAppInstall(Context context) {
        try {
            boolean bl = Utils.isAppInstalled(context, WA_BUSINESS_PACKAGE_NAME);
            return bl;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }


    public static void createFileFolder() {
        if (!RootDirectoryWhatsappShow.exists()) {
            RootDirectoryWhatsappShow.mkdirs();
        }
    }

    public static void showProgressDialog(Activity activity) {
        System.out.println("Show");
        if (customDialog != null) {
            customDialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mView = inflater.inflate(R.layout.progress_dialog, null);
        customDialog.setCancelable(false);
        customDialog.setContentView(mView);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.show();
        }
    }

    public static void hideProgressDialog(Activity activity) {
        System.out.println("Hide");
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void startDownload(String downloadPath, String destinationPath, Context context, String FileName) {
        setToast(context, "Download Started");
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle(FileName+""); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,destinationPath+FileName);  // Storage directory path
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
        try {
            MediaScannerConnection.scanFile(context, new String[]{new File(DIRECTORY_DOWNLOADS + "/" + destinationPath + FileName).getAbsolutePath()},
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.equals("")) || (s.length() == 0) || (s.equalsIgnoreCase("null")) || (s.equalsIgnoreCase("0"));
    }



    public static void shareImage(Context context, String filePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_txt));
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, "", null);
            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_image_via)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void shareImageVideoOnWhatsapp(Context context, String filePath, boolean isVideo) {
        Uri imageUri = Uri.parse(filePath);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        if (isVideo) {
            shareIntent.setType("video/*");
        }else {
            shareIntent.setType("image/*");
        }
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (Exception e) {
            Utils.setToast(context,"Whtasapp not installed.");
        }
    }

    public static void shareVideo(Context context, String filePath) {
        Uri mainUri = Uri.parse(filePath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found to open this file.", Toast.LENGTH_LONG).show();
        }
    }

    public static void OpenApp(Context context,String Package) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(Package);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            setToast(context,"App Not Available.");
        }
    }

}