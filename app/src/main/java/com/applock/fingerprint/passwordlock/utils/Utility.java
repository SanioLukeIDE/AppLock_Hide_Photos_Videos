package com.applock.fingerprint.passwordlock.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.applock.fingerprint.passwordlock.BuildConfig;
import com.applock.fingerprint.passwordlock.R;
import com.applock.fingerprint.passwordlock.databinding.DialogExitBinding;
import com.applock.fingerprint.passwordlock.model.AppsModel;
import com.applock.fingerprint.passwordlock.model.CommLockInfo;
import com.applock.fingerprint.passwordlock.model.ImageFolder;
import com.applock.fingerprint.passwordlock.model.LockThemeModel;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Utility {

    public static final String privacyLink = "https://framefusionfilms.blogspot.com/p/privacy-policy.html";

    public static boolean isInternetConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(1);
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    return true;
                }
                return networkInfo2 != null && networkInfo2.isConnectedOrConnecting();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void openCloseDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        DialogExitBinding binding = DialogExitBinding.inflate(LayoutInflater.from(activity));
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        binding.btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            activity.finishAffinity();
        });
        binding.btnRate.setOnClickListener(v -> {
            dialog.dismiss();
            reviewDialog(activity);
        });
        binding.btnClose.setOnClickListener(v -> dialog.dismiss());

    }


    public static List<LockThemeModel> readThemeJsonFromRaw(Context context) {
        List<LockThemeModel> lockThemeModels = new ArrayList<>();

        try {
            Resources resources = context.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.themes);
            Scanner scanner = new Scanner(inputStream);

            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                LockThemeModel model = new Gson().fromJson(object.toString(), LockThemeModel.class);
                lockThemeModels.add(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        lockThemeModels.sort((t1, t2) -> t1.getTitle().toLowerCase().compareToIgnoreCase(t2.getTitle().toLowerCase()));

        return lockThemeModels;
    }

    public static void openPrivacyPolicy(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://invotechgirge.blogspot.com/p/privacy-policy_8.html"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void reviewDialog(Activity activity) {
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

    public static void shareAppDialog(Activity activity) {
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

    public static void OpenApp(Context context, String Package) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(Package);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            setToast(context, "App Not Available.");
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
                String className = appInfo.name;
                for (String val : preferences.getHideApps()) {
                    if (val.equals(packageName)) {
                        AppsModel app = new AppsModel(appName, packageName, appIcon);
                        app.setClassName(className);
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

            for (String s : preferences.getHideApps()) {
                if (packageName.equals(s)) {
                    preferences.removeHideApps(packageName);
                }
            }

            if (!context.getPackageName().equals(packageName)) {
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

        AsyncTask.execute(() -> {
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

            data.postValue(imagePaths);
        });

        return data;
    }

    public static boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName);
        return extension != null && (extension.equalsIgnoreCase("webp") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp"));
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

    public static LiveData<List<String>> getAllVideosFromFolder(Context context) {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        List<String> videoPaths = new ArrayList<>();

        // Define the columns you want to retrieve
        String[] projection = {MediaStore.Video.Media.DATA};

        // Define the selection query
        String folderPath = "/storage/emulated/0/Download/All Video Downloader";

        String selection = MediaStore.Video.Media.DATA + " like ?";
        String[] selectionArgs = new String[]{"%" + folderPath + "%"};

        // Query the media store for videos matching the folder path
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null) {

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

            while (cursor.moveToNext()) {
                String videoPath = cursor.getString(columnIndex);
                videoPaths.add(videoPath);
            }

            cursor.close();
            AsyncTask.execute(() -> data.postValue(videoPaths));

        }

        return data;
    }

    public static LiveData<List<String>> getAllImagesVideosFromFolderPath(String path) {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        List<String> imagePaths = new ArrayList<>();

        AsyncTask.execute(() -> {
            File folder = new File(path);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && (isImageFile(file.getName()) || isVideoFile(file.getName()))) {
                            String imagePath = file.getAbsolutePath();
                            imagePaths.add(imagePath);
                        }
                    }
                }
            }

            data.postValue(imagePaths);
        });

        return data;
    }

    public static boolean isVideoFile(String fileName) {
        String[] videoExtensions = {".gif", ".mp4", ".mkv", ".avi", ".mov", ".flv", ".wmv", ".webm", ".mpg", ".mpeg", ".3gp", ".m4v", ".mts", ".m2ts", ".vob", ".rm", ".rmvb", ".asf", ".ts", ".m2t", ".m2v", ".mpe", ".divx"};

        for (String extension : videoExtensions) {
            if (fileName.toLowerCase().endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    public static void shareVideoFile(Context context, String filePath) {
        File file = new File(filePath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, file.getName());
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
        context.startActivity(Intent.createChooser(shareIntent, "Share Video"));
    }

/*
    private List<LockThemeModel> getList(int page) {
        List<LockThemeModel> list = new ArrayList<>();

        String mainLink = "https://raw.githubusercontent.com/brijesh-ide/AppLock_Hide_Photos_Videos/master/themes/";
        String abstractLink = mainLink + "img_abstract.png", animals = mainLink + "img_animals.png", nature = mainLink + "img_nature.png", minimal = mainLink + "img_minimal.png", plants = mainLink + "img_plants.png", art = mainLink + "img_art.png", portrait = mainLink + "img_potrait.png", space = mainLink + "img_space.png", architect = mainLink + "img_architect.png", food = mainLink + "img_food.png", business = mainLink + "img_business.png", interior = mainLink + "img_interior.png";


        if (page == 0) {
            list.add(new LockThemeModel("Animals", animals, false));
            list.add(new LockThemeModel("Abstract", abstractLink, false));
            list.add(new LockThemeModel("Nature", nature, false));
            list.add(new LockThemeModel("Minimal", minimal, false));
            list.add(new LockThemeModel("Plants", plants, false));
            list.add(new LockThemeModel("Art", art, false));
            list.add(new LockThemeModel("Portrait", portrait, false));
            list.add(new LockThemeModel("Space", space, false));
            list.add(new LockThemeModel("Architect", architect, false));
            list.add(new LockThemeModel("Food", food, false));
            list.add(new LockThemeModel("Business", business, false));
            list.add(new LockThemeModel("Interior", interior, false));
        }
        if (page == 1) {
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/cheetah-sitting-big-cat-feline-162313.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/farmer-man-shepherd-dog-162520.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/free-photo-of-a-black-dog-in-close-up-shot.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/leopard-animal-cat-cheetah-51129.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/monkey-mandril-africa-baboon-38280.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/panthera-tigris-altaica-tiger-siberian-amurtiger-162203.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-carlos-spitzer-17811.jpg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-1025586.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-1170986.webp", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-1420405.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-1454786.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-1669860.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-1670413.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-1804976.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-1805164.webp", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-1912176.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-2023384.webp", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-2100047.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-2152399.webp", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-2255564.webp", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-2313396.webp", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-2795879.webp", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-3010291.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-3162755.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-3281127.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-3550969.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-3777622.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-4577819.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-5731788.webp", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-674318.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-68594.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-704454.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/pexels-photo-7709902.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/swiss-shepherd-dog-dog-pet-portrait-46505.jpeg", false));
            list.add(new LockThemeModel("Animals", mainLink + "wallpaper/ANIMAL/yemen-chameleon-chamaeleo-calyptratus-chameleon-reptile-62289.jpeg", false));
        }
        if (page == 2) {
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-1029611.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-1114883.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-1198828.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-1493226.jpeg", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-1544946.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-1762973.jpeg", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-1910231.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-1934846.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-1998479.jpeg", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-2088207.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-2215609.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-2276928.jpeg", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-2333293.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-2892606.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-3357695.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-3371103.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-3435272.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-367903.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-4068339.jpeg", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-4585184.webp", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-5278835.jpeg", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-5480787.jpeg", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-6194972.jpeg", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-6331049.jpeg", false));
            list.add(new LockThemeModel("Abstract", mainLink + "wallpaper/ABSTRACT/pexels-photo-68556.webp", false));
        }
        if (page == 3) {
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/free-photo-of-a-house-on-a-hill-between-trees-in-the-countryside.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/free-photo-of-clouds-over-mountains.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/free-photo-of-water-around-rocks-on-shore.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-1011302.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-1144687.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-12879014.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-14934612.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-1671325.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-16948299.webp", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-1761279.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-1784577.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-1785493.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-1894350.webp", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-2097103.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-2469122.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-2724664.webp", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-3329292.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-3331094.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-3464632.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-3560044.webp", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-3621344.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-4220967.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-4591254.webp", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-4666748.webp", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-4962458.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-5117913.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-5282269.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-5345029.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-5708073.webp", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-5850083.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-6590699.jpeg", false));
            list.add(new LockThemeModel("Nature", mainLink + "wallpaper/NATURE/pexels-photo-6843561.webp", false));
        }
        if (page == 4) {
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-1557183.jpeg", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-1671643.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-1772123.jpeg", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-2027697.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-2280171.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-2394446.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-2417608.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-2438213.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-2439595.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-2456270.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-2742725.jpeg", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-2868847.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-3062948.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-3094224.jpeg", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-3112019.jpeg", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-3143922.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-325812.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-3283576.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-3499176.jpeg", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-3750893.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-4046718.jpeg", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-4065624.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-4065906.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-4207776.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-4207892.jpeg", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-4210811.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-5238645.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-912413.webp", false));
            list.add(new LockThemeModel("Minimal", mainLink + "wallpaper/MINIMAL/pexels-photo-940298.webp", false));
        }
        if (page == 5) {
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-305827.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-797793.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-912413.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-1141792.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-1207978.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-1261016.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-1494067.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-1732411.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-1863526.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-1999579.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-2090641.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-2253844.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-2560900.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-2845269.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-3139367.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-3192175.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-3283453.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-3377252.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-3578393.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-4099358.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-4374576.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-4505447.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-4599906.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-4621640.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-5137558.jpeg", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-5748730.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-7354448.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-7558445.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-7690862.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-7897455.webp", false));
            list.add(new LockThemeModel("Plants", mainLink + "wallpaper/PLANTS/pexels-photo-9414266.jpeg", false));
        }
        if (page == 6) {
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-179747.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-270873.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-297494.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-587958.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-616849.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-699466.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-721287.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-917594.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-971546.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-1570264.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-1579708.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-1646953.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-1671643.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-1727658.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-1762851.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-1998479.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-2067178.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-2086361.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-2088203.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-2090484.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-2807495.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-2884867.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-3094218.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-3095769.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-3246665.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-3354675.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-3660035.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-3683209.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-3893650.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-4483093.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-4585185.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-6538894.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-7168823.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-7883486.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-8843689.webp", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-11942721.jpeg", false));
            list.add(new LockThemeModel("Art", mainLink + "wallpaper/ART/pexels-photo-15032623.webp", false));
        }
        if (page == 7) {
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-1858490.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-2531734.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-2598024.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-2709385.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-2739792.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-2853198.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-2922301.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3048715.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3482526.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3533228.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3586798.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3728598.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3748399.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3805874.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3867132.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3886285.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-3907595.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4156467.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4192655.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4244305.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4298629.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4505269.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4728655.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4754648.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4757976.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4926674.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4927361.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4956618.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-4966995.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-5119214.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-5160850.jpeg", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-5273717.webp", false));
            list.add(new LockThemeModel("Portrait", mainLink + "wallpaper/PORTRAIT/pexels-photo-6151982.webp", false));
        }
        if (page == 8) {
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-42148.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-355925.webp", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-355938.webp", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-544268.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-731649.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-783944.webp", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-816608.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-920689.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-1098012.webp", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-1274260.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-1302434.webp", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-1591252.webp", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-1906658.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-2098427.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-2113554.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-2376991.webp", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-2387877.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-2644734.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-2670898.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-2680270.webp", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-2753432.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-2873671.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-3110354.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-3114462.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-3222255.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-3536526.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-3680319.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-3934512.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/pexels-photo-4967858.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/kazakhstan-soyuz-rocket-space-65704.jpeg", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/space-shuttle-start-discovery-spaceport-87080.webp", false));
            list.add(new LockThemeModel("Space", mainLink + "wallpaper/SPACE/space-shuttle-atlantis-liftoff-mission-rocket-39698.webp", false));
        }
        if (page == 9) {
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-273661.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-986728.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-1088828.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-1608429.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-2119236.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-2506211.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-2884867.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-3769162.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-3779187.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-3779814.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-3779817.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-4219151.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-4219155.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-4219524.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-4219527.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-4968580.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-5292199.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-5292201.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-5582599.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-5584052.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-5641872.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-5641876.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-6044258.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-6044265.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-6283214.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-6322371.jpeg", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-6592547.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-6615196.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-8293645.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-8960933.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-8960992.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-9511923.webp", false));
            list.add(new LockThemeModel("Architect", mainLink + "wallpaper/ARCHITECT/pexels-photo-9512012.webp", false));
        }
        if (page == 10) {
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-323682.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-397913.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-541216.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-604969.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-806457.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-842571.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-916925.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-1109197.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-1410235.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-1435735.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-1633525.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-1639556.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-1813505.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-1998920.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-2067396.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-2097090.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-2454533.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-2641886.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-2983099.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-3304057.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-3338497.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-3338681.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-3642030.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-3754300.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-3789885.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-7441761.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-10897655.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-15564188.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/pexels-photo-15913452.webp", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/free-photo-of-food-restaurant-spoon-drinks.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/free-photo-of-breakfast-on-bed-in-mountain-chalet.jpeg", false));
            list.add(new LockThemeModel("Food", mainLink + "wallpaper/FOOD/free-photo-of-pizzas-on-the-table-at-the-restaurant.jpeg", false));
        }
        if (page == 11) {
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-133021.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-164666.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-244133.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-264512.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-273671.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-322335.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-331989.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-756484.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-1181214.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-1181292.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-1181312.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-1181391.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-1181651.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-1251849.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-1400249.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-1546329.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2103944.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2148216.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2199190.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2637579.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2637581.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2889488.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2901581.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2911260.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2912583.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-2977513.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-3184287.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-3747446.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-9546837.jpeg", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-16051533.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-17392394.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-17459762.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-17739178.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/pexels-photo-17795616.webp", false));
            list.add(new LockThemeModel("Business", mainLink + "wallpaper/BUSINESS/free-photo-of-close-up-of-woman-sitting-with-laptop.jpeg", false));
        }
        if (page == 12) {
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-210464.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-447592.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-1125136.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-1148955.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-1321290.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-1457847.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-1874726.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-2079249.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-2208891.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-2227832.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-2343468.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-2442904.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-3932930.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-3951746.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-4352247.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-4450337.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-4906249.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-5490356.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-5570224.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-5794012.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-5824901.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-5825398.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-5825576.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-5942742.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-6021895.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-6434622.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-7563521.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-8210482.jpeg", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-11112251.webp", false));
            list.add(new LockThemeModel("Interior", mainLink + "wallpaper/INTERIOR/pexels-photo-15756564.webp", false));
        }

        return list;
    }
*/


}
