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
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Toast;

import com.applock.photos.videos.model.AppsModel;
import com.applock.photos.videos.model.CommLockInfo;
import com.applock.photos.videos.model.ImageFolder;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Utility {

    public static void hideApp(Context context, ComponentName componentName) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED, PackageManager.DONT_KILL_APP);
        } else {
            context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
//        context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
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

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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

            Log.e(label, label+" --> getAllInstalledApps: ---> "+packageName+ "     "+className);

           for (String s : preferences.getHideApps()){
               if (packageName.equals(s)){
                   preferences.removeHideApps(packageName);
               }
           }

            AppsModel model = new AppsModel(label, packageName, icon);
            model.setClassName(className);
            list.add(model);

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
