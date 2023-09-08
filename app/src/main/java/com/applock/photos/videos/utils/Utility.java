package com.applock.photos.videos.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.applock.photos.videos.BuildConfig;
import com.applock.photos.videos.R;
import com.applock.photos.videos.model.AppsModel;
import com.applock.photos.videos.model.CommLockInfo;
import com.applock.photos.videos.model.ImageFolder;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Utility {

    public static void openPrivacyPolicy(Activity activity){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://invotechgirge.blogspot.com/p/privacy-policy_8.html"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void reviewDialog(Activity activity){
        ReviewManager manager = ReviewManagerFactory.create(activity);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                flow.addOnCompleteListener(task1 -> {

                });
            } else {
                // There was some problem, log or handle the error code.
                Log.e("reviewDialog: ", Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        });

    }

    public static void shareAppDialog(Activity activity){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Looking to take your Instagram game to the next level? Look no further than " + activity.getString(R.string.app_name) + "!!\n" +
                "With our multiple editing features you'll be able to create stunning content that will help you stand out on the platform. \n" +
                "Download " + activity.getString(R.string.app_name) + " today and elevate your Instagram presence!\n" +
                "\nJoin now with the link: \nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }

    public static ShimmerDrawable getShimmer() {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(1200) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.9f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setShape(Shimmer.Shape.LINEAR)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        return shimmerDrawable;
    }

    public static void setFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = activity.getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void hideApp(Context context, ComponentName componentName) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED, PackageManager.DONT_KILL_APP);
        } else {
            context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
//        context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public static void nextActivity(Activity activity, Class<?> className, boolean... isFinish) {
//        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            if (isFinish[0]) {
                activity.startActivity(new Intent(activity, className).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                activity.finish();
            } else
                activity.startActivity(new Intent(activity, className).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        });
    }

    public static void nextDataActivity(Activity activity, Class<?> className, Object... data) {
//        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            activity.startActivity(new Intent(activity, className).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("data", data));
//        });
    }

    public static void finishActivity(Activity activity) {
//        AdUtils.showBackPressAds(activity, Constants.adsResponseModel.getApp_open_ads().getAdx(), isLoaded -> {
            activity.finish();
//        });
    }

    public static void unHideApp(Context context, ComponentName componentName) {
        context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public static void hideUnHideApps(Context context, String packageName, boolean enabled) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(packageName, packageName + ".MainActivity");
        int newState = enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        pm.setComponentEnabledSetting(componentName, newState, PackageManager.DONT_KILL_APP);
    }

    public static ArrayList<ImageFolder> getAllVideoFolders(Context context) {
        ArrayList<ImageFolder> imageFolders = new ArrayList<>();
        HashMap<String, ImageFolder> folderMap = new HashMap<>();

        String[] projection = {MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.DATA};
        String sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " ASC";

        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);

        if (cursor != null) {
            int bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID);
            int bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
            int dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN);
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

            while (cursor.moveToNext()) {
                String bucketId = cursor.getString(bucketIdColumn);
                String bucketName = cursor.getString(bucketNameColumn);
                String dateTaken = cursor.getString(dateTakenColumn);
                String imagePath = cursor.getString(dataColumn);

                ImageFolder imageFolder = folderMap.get(bucketId);
                if (imageFolder == null) {
                    imageFolder = new ImageFolder(bucketId, bucketName, imagePath, dateTaken, 1, new ArrayList<>());
                    folderMap.put(bucketId, imageFolder);
                } else {
                    imageFolder.setImageCount(imageFolder.getImageCount() + 1);
                }
                imageFolder.addImagePath(imagePath);
            }
            cursor.close();
        }

        imageFolders.addAll(folderMap.values());
        return imageFolders;
    }

    public static void OpenApp(Context context,String Package) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(Package);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            setToast(context,"App Not Available.");
        }
    }

    public static List<CommLockInfo> clearRepeatCommLockInfo(List<CommLockInfo> lockInfos) {
        HashMap<String, CommLockInfo> hashMap = new HashMap<>();
        for (CommLockInfo lockInfo : lockInfos) {
            if (!hashMap.containsKey(lockInfo.getPackageName())) {
                hashMap.put(lockInfo.getPackageName(), lockInfo);
            }
        }
        List<CommLockInfo> commLockInfos = new ArrayList<>();
        for (HashMap.Entry<String, CommLockInfo> entry : hashMap.entrySet()) {
            commLockInfos.add(entry.getValue());
        }
        return commLockInfos;
    }

    public static List<AppsModel> getAllDisableApps(Context context) {
        List<AppsModel> disabledAppsList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        SharePreferences preferences = new SharePreferences(context);

        for (ApplicationInfo appInfo : applicationInfoList) {
            if (!appInfo.enabled) {
                Drawable appIcon = packageManager.getApplicationIcon(appInfo);
                String appName = (String) packageManager.getApplicationLabel(appInfo);
                String packageName = appInfo.packageName;
                for (String val : preferences.getHideApps()) {
                    if (val.equals(packageName)) {
                        AppsModel app = new AppsModel(appName, packageName, appIcon);
                        disabledAppsList.add(app);
                    }
                }
            }
        }

        return disabledAppsList;
    }

    public static List<AppsModel> getAllInstalledApps(Context context) {
        List<AppsModel> list = new ArrayList<>();
        SharePreferences preferences = new SharePreferences(context);

        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> info = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : info) {

            // Get package name
            String packageName = resolveInfo.activityInfo.packageName;

            // Get application label (name)
            String label = String.valueOf(resolveInfo.loadLabel(packageManager));
            // Get application icon (logo)
            Drawable icon = resolveInfo.loadIcon(packageManager);

            String className = resolveInfo.activityInfo.name;

           for (String s : preferences.getHideApps()){
               if (packageName.equals(s)){
                   preferences.removeHideApps(packageName);
               }
           }

           if (!context.getPackageName().equals(packageName)){
               AppsModel model = new AppsModel(label, packageName, icon);
               model.setClassName(className);
               list.add(model);
           }

        }

        list.sort((t1, t2) -> t1.getAppName().toLowerCase().compareToIgnoreCase(t2.getAppName().toLowerCase()));

        return list;
    }

    public static boolean isAppDisabled(Context context, String packageName) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
            int enabled = context.getPackageManager().getApplicationEnabledSetting(packageName);
            return enabled == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                    enabled == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static ArrayList<ImageFolder> getAllImageFolders(Context context) {
        ArrayList<ImageFolder> imageFolders = new ArrayList<>();
        HashMap<String, ImageFolder> folderMap = new HashMap<>();

        String[] projection = {MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA};
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " ASC";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder);

        if (cursor != null) {
            int bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            int bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            while (cursor.moveToNext()) {
                String bucketId = cursor.getString(bucketIdColumn);
                String bucketName = cursor.getString(bucketNameColumn);
                String dateTaken = cursor.getString(dateTakenColumn);
                String imagePath = cursor.getString(dataColumn);

                ImageFolder imageFolder = folderMap.get(bucketId);
                if (imageFolder == null) {
                    imageFolder = new ImageFolder(bucketId, bucketName, imagePath, dateTaken, 1, new ArrayList<>());
                    folderMap.put(bucketId, imageFolder);
                } else {
                    imageFolder.setImageCount(imageFolder.getImageCount() + 1);
                }
                imageFolder.addImagePath(imagePath);
            }

            cursor.close();
        }

        imageFolders.addAll(folderMap.values());
        return imageFolders;
    }

    public static LiveData<List<String>> getAllImagesFromFolderPath(String folderPath) {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        List<String> imagePaths = new ArrayList<>();

//        String folderPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + directoryInstaShoryDirectorydownload_images;

        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isImageFile(file.getName())) {
                        String imagePath = file.getAbsolutePath();
                        imagePaths.add(imagePath);
                    }
                }
            }
        }

        AsyncTask.execute(() -> data.postValue(imagePaths));

        return data;
    }

    public static boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName);
        return extension != null && (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp"));
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }


    public static List<AppsModel> getInstalledApps(Context context, boolean isRecommended) {
        List<AppsModel> list = new ArrayList<>();

        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> info = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : info) {
            // Get package name
            String packageName = resolveInfo.activityInfo.packageName;
            // Get application label (name)
            String label = String.valueOf(resolveInfo.loadLabel(packageManager));
            // Get application icon (logo)
            Drawable icon = resolveInfo.loadIcon(packageManager);

            boolean val = label.equals("WhatsApp") || label.equals("Facebook") || label.equals("Instagram") || label.equals("Twitter") || label.equals("Files") || label.equals("Chrome");
            if (isRecommended && (val)) {
                list.add(new AppsModel(label, packageName, icon));
            }
            if (!isRecommended) {
                if (!val) list.add(new AppsModel(label, packageName, icon));
            }
        }

//        list.sort(Comparator.comparing(AppsModel::getAppName));
        list.sort((t1, t2) -> t1.getAppName().toLowerCase().compareToIgnoreCase(t2.getAppName().toLowerCase()));

        return list;
    }
}
