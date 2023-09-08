package com.applock.photos.videos.downloaderUtil.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FilePathUtility {

    private Context context;

    public static boolean moveFile(Context context, String sourceFile) {
        String[] strings = sourceFile.split(".Statuses%2F");
        String despath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME + strings[1];
        Uri destUri = Uri.fromFile(new File(despath));
        try {
            InputStream is = context.getContentResolver().openInputStream(Uri.parse(sourceFile));
            OutputStream os = context.getContentResolver().openOutputStream(destUri, "w");
            byte[] buffer = new byte[1024];
            while (true) {
                int read = is.read(buffer);
                int length = read;
                if (read > 0) {
                    os.write(buffer, 0, length);
                } else {
                    is.close();
                    os.flush();
                    os.close();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(destUri);
                    context.sendBroadcast(intent);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKatorUp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatorUp && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }

        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }


    public static FilePathUtility getInstance() {
        return new FilePathUtility();
    }

    public FilePathUtility with(Context context) {
        this.context = context;
        return this;
    }

    /**
     * Create new media uri.
     */
    public Uri create(String directory, String filename, String mimetype) {

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();

        //Set filename, if you don't system automatically use current timestamp as name
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);

        //Set mimetype if you want
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimetype);

        //To create folder in Android directories use below code
        //pass your folder path here, it will create new folder inside directory
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directory);
        }

        //pass new ContentValues() for no values.
        //Specified uri will save object automatically in android specified directories.
        //ex. MediaStore.Images.Media.EXTERNAL_CONTENT_URI will save object into android Pictures directory.
        //ex. MediaStore.Videos.Media.EXTERNAL_CONTENT_URI will save object into android Movies directory.
        //if content values not provided, system will automatically add values after object was written.
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    /**
     * Delete file.
     * <p>
     * If {@link ContentResolver} failed to delete the file, use trick,
     * SDK version is >= 29(Q)? use {@link SecurityException} and again request for delete.
     * SDK version is >= 30(R)? use .
     */
    public void delete(ActivityResultLauncher<IntentSenderRequest> launcher, Uri uri) {

        ContentResolver contentResolver = context.getContentResolver();

        try {

            //delete object using resolver
            contentResolver.delete(uri, null, null);

        } catch (SecurityException e) {

            PendingIntent pendingIntent = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                ArrayList<Uri> collection = new ArrayList<>();
                collection.add(uri);
                pendingIntent = MediaStore.createDeleteRequest(contentResolver, collection);

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                //if exception is recoverable then again send delete request using intent
                if (e instanceof RecoverableSecurityException) {
                    RecoverableSecurityException exception = (RecoverableSecurityException) e;
                    pendingIntent = exception.getUserAction().getActionIntent();
                }
            }

            if (pendingIntent != null) {
                IntentSender sender = pendingIntent.getIntentSender();
                IntentSenderRequest request = new IntentSenderRequest.Builder(sender).build();
                launcher.launch(request);
            }
        }
    }

    /**
     * Rename file.
     *
     * @param uri    - filepath.
     * @param rename - the name you want to replace with original.
     */
    public void rename(Uri uri, String rename) {

        //create content values with new name and update
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, rename);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.getContentResolver().update(uri, contentValues, null);
        }
    }

    /**
     * Duplicate file.
     *
     * @param uri - filepath.
     */
    public Uri duplicate(Uri uri) {

        ContentResolver contentResolver = context.getContentResolver();

        Uri output = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());

        String input = getPathFromUri(uri);

        try (InputStream inputStream = new FileInputStream(input)) { //input stream

            OutputStream out = contentResolver.openOutputStream(output); //output stream

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len); //write input file data to output file
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    /**
     * Get file path from uri.
     */
    @SuppressLint("Range")
    private String getPathFromUri(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        String text = null;

        if (cursor.moveToNext()) {
            text = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        }

        cursor.close();

        return text;
    }


    public static long getFilePathToMediaID(String songPath, Context context) {
        long id = 0;
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Audio.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = {MediaStore.Audio.Media._ID};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                id = Long.parseLong(cursor.getString(idIndex));
            }
        }

        return id;
    }


    public static void deleteAndroid10andABOVE(Activity activity, String path, String status) {
        File tempFile = new File(path);
        long mediaID = getFilePathToMediaID(tempFile.getAbsolutePath(), activity);
        Uri Uri_one;
        List<Uri> uris = new ArrayList<>();
        if (status.equals("v")) {
            Uri_one = ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), mediaID);


        } else if (status.equals("i")){
            Uri_one = ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), mediaID);


        } else {
            Uri_one = ContentUris.withAppendedId(MediaStore.Audio.Media.getContentUri("external"), mediaID);


        }
        uris.add(Uri_one);
        requestDeletePermission(activity, uris);
    }

    private static void requestDeletePermission(Activity activity, List<Uri> uriList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            PendingIntent pi = MediaStore.createDeleteRequest(activity.getContentResolver(), uriList);

            try {
                activity.startIntentSenderForResult(pi.getIntentSender(), 123, null, 0, 0,
                        0);
            } catch (IntentSender.SendIntentException e) {
                Log.d("error", "" + e.getLocalizedMessage());

            }
        }
    }


}
