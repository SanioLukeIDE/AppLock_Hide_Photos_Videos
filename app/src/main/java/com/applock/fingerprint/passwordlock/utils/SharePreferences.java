package com.applock.fingerprint.passwordlock.utils;

import static com.applock.fingerprint.passwordlock.utils.Const.FAVORITE_APPS_LIST;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.applock.fingerprint.passwordlock.model.AppsModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SharePreferences {

    private Context context;
    private SharedPreferences preferences;
    private String DEFAULT_APP_IMAGEDATA_DIRECTORY;
    private String lastImagePath = "";
    private final String PREFERENCE_whatsapp = "whatsapp";
    private final String PREFERENCE_whatsappbusiness = "whatsappbusiness";

    public SharePreferences(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        context = appContext;
    }

    public String getPREFERENCE_whatsapp() {
        return getString(PREFERENCE_whatsapp);
    }

    public String getPREFERENCE_whatsappbusiness() {
        return getString(PREFERENCE_whatsappbusiness);
    }

    public void setPREFERENCE_whatsappbusiness(String string) {
        putString(PREFERENCE_whatsappbusiness, string);
    }

    public void setPREFERENCE_whatsapp(String toString) {
        putString(PREFERENCE_whatsapp, toString);
    }

    public static boolean isNonNull(String val) {
        return val == null || val.trim().equalsIgnoreCase("") || val.trim().equalsIgnoreCase("null") || val.trim().equals("") || val.trim().equals("null");
    }

    public void setHideApps(String pk) {
        ArrayList<String> list = getListString("HideApps");
        list.add(pk);
        putListString("HideApps", list);
    }

    public void removeHideApps(String pk) {
        ArrayList<String> list = getListString("HideApps");
        list.removeIf(pk::equals);
        putListString("HideApps", list);
    }

    public List<String> getHideApps() {
        return getListString("HideApps");
    }

    public void setHiddenImages(String path) {
        ArrayList<String> list = getListString("hiddenImages");
        list.add(path);
        putListString("hiddenImages", list);
    }

    public void setFavoriteApps(List<String> list) {
        putListString(FAVORITE_APPS_LIST, (ArrayList<String>) list);
    }

    public List<String> getFavoriteApps() {
        return getListString(FAVORITE_APPS_LIST);
    }

    public void removeHiddenImage(String path) {
        ArrayList<String> list = getListString("hiddenImages");
        list.removeIf(path::equals);
        putListString("hiddenImages", list);
    }

    public List<String> getHiddenImages() {
        return getListString("hiddenImages");
    }

    public AppsModel StringToPOJO(String json) {
        return new Gson().fromJson(json, AppsModel.class);
    }

    public String POJOToString(AppsModel object) {
        return new Gson().toJson(object);
    }


    /**
     * Decodes the Bitmap from 'path' and returns it
     *
     * @param path image path
     * @return the Bitmap from 'path'
     */
    public Bitmap getImage(String path) {
        Bitmap bitmapFromPath = null;
        try {
            bitmapFromPath = BitmapFactory.decodeFile(path);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return bitmapFromPath;
    }


    /**
     * Returns the String path of the last saved image
     *
     * @return string path of the last saved image
     */
    public String getSavedImagePath() {
        return lastImagePath;
    }


    /**
     * Saves 'theBitmap' into folder 'theFolder' with the name 'theImageName'
     *
     * @param theFolder    the folder path dir you want to save it to e.g "DropBox/WorkImages"
     * @param theImageName the name you want to assign to the image file e.g "MeAtLunch.png"
     * @param theBitmap    the image you want to save as a Bitmap
     * @return returns the full path(file system address) of the saved image
     */
    public String putImage(String theFolder, String theImageName, Bitmap theBitmap) {
        if (theFolder == null || theImageName == null || theBitmap == null) return null;

        this.DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder;
        String mFullPath = setupFullPath(theImageName);

        if (!mFullPath.equals("")) {
            lastImagePath = mFullPath;
            saveBitmap(mFullPath, theBitmap);
        }

        return mFullPath;
    }


    /**
     * Saves 'theBitmap' into 'fullPath'
     *
     * @param fullPath  full absolute path of the image file e.g. "..Images/MeAtLunch.png"
     * @param theBitmap the image you want to save as a Bitmap
     * @return true if image was saved, false otherwise
     */
    public boolean putImageWithFullPath(String fullPath, Bitmap theBitmap) {
        return !(fullPath == null || theBitmap == null) && saveBitmap(fullPath, theBitmap);
    }

    /**
     * Creates the path for the image with name 'imageName' in DEFAULT_APP.. directory
     *
     * @param imageName name of the image
     * @return the full path of the image. If it failed to create directory, return empty string
     */
    private String setupFullPath(String imageName) {
        File mFolder = new File(context.getExternalFilesDir(null), DEFAULT_APP_IMAGEDATA_DIRECTORY);

        if (isExternalStorageReadable() && isExternalStorageWritable() && !mFolder.exists()) {
            if (!mFolder.mkdirs()) {
                Log.e("ERROR", "Failed to setup folder");
                return "";
            }
        }

        return mFolder.getPath() + '/' + imageName;
    }

    /**
     * Saves the Bitmap as a PNG file at path 'fullPath'
     *
     * @param fullPath path of the image file
     * @param bitmap   the image as a Bitmap
     * @return true if it successfully saved, false otherwise
     */
    private boolean saveBitmap(String fullPath, Bitmap bitmap) {
        if (fullPath == null || bitmap == null) return false;

        boolean fileCreated = false;
        boolean bitmapCompressed = false;
        boolean streamClosed = false;

        File imageFile = new File(fullPath);

        if (imageFile.exists()) if (!imageFile.delete()) return false;

        try {
            fileCreated = imageFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imageFile);
            bitmapCompressed = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
            bitmapCompressed = false;

        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                    streamClosed = true;

                } catch (IOException e) {
                    e.printStackTrace();
                    streamClosed = false;
                }
            }
        }

        return (fileCreated && bitmapCompressed && streamClosed);
    }

    // Getters

    /**
     * Get int value from SharedPreferences at 'key'. If key not found, return 0
     *
     * @param key SharedPreferences key
     * @return int value at 'key' or 0 if key not found
     */
    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Integers
     */
    public ArrayList<Integer> getListInt(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Integer> newList = new ArrayList<Integer>();

        for (String item : arrayToList)
            newList.add(Integer.parseInt(item));

        return newList;
    }

    /**
     * Get long value from SharedPreferences at 'key'. If key not found, return 0
     *
     * @param key SharedPreferences key
     * @return long value at 'key' or 0 if key not found
     */
    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    /**
     * Get float value from SharedPreferences at 'key'. If key not found, return 0
     *
     * @param key SharedPreferences key
     * @return float value at 'key' or 0 if key not found
     */
    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    /**
     * Get double value from SharedPreferences at 'key'. If exception thrown, return 0
     *
     * @param key SharedPreferences key
     * @return double value at 'key' or 0 if exception is thrown
     */
    public double getDouble(String key) {
        String number = getString(key);

        try {
            return Double.parseDouble(number);

        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Get parsed ArrayList of Double from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Double
     */
    public ArrayList<Double> getListDouble(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Double> newList = new ArrayList<Double>();

        for (String item : arrayToList)
            newList.add(Double.parseDouble(item));

        return newList;
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Longs
     */
    public ArrayList<Long> getListLong(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Long> newList = new ArrayList<Long>();

        for (String item : arrayToList)
            newList.add(Long.parseLong(item));

        return newList;
    }

    /**
     * Get String value from SharedPreferences at 'key'. If key not found, return ""
     *
     * @param key SharedPreferences key
     * @return String value at 'key' or "" (empty String) if key not found
     */
    public String getString(String key) {
        return preferences.getString(key, null);
    }

    public String getCustomString(String key, String val) {
        return preferences.getString(key, val);
    }

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    /**
     * Get boolean value from SharedPreferences at 'key'. If key not found, return false
     *
     * @param key SharedPreferences key
     * @return boolean value at 'key' or false if key not found
     */
    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, true);
    }

    /**
     * Get parsed ArrayList of Boolean from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Boolean
     */
    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList<String> myList = getListString(key);
        ArrayList<Boolean> newList = new ArrayList<Boolean>();

        for (String item : myList) {
            if (item.equals("true")) {
                newList.add(true);
            } else {
                newList.add(false);
            }
        }

        return newList;
    }


    public ArrayList<Object> getListObject(String key, Class<?> mClass) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<Object> objects = new ArrayList<Object>();

        for (String jObjString : objStrings) {
            Object value = gson.fromJson(jObjString, mClass);
            objects.add(value);
        }
        return objects;
    }


    public <T> T getObject(String key, Class<T> classOfT) {

        String json = getString(key);
        Object value = new Gson().fromJson(json, classOfT);
        if (value == null) throw new NullPointerException();
        return (T) value;
    }


    // Put methods

    /**
     * Put int value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value int value to be added
     */
    public void putInt(String key, int value) {
        checkForNullKey(key);
        preferences.edit().putInt(key, value).apply();
    }

    /**
     * Put ArrayList of Integer into SharedPreferences with 'key' and save
     *
     * @param key     SharedPreferences key
     * @param intList ArrayList of Integer to be added
     */
    public void putListInt(String key, ArrayList<Integer> intList) {
        checkForNullKey(key);
        Integer[] myIntList = intList.toArray(new Integer[intList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply();
    }

    /**
     * Put long value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value long value to be added
     */
    public void putLong(String key, long value) {
        checkForNullKey(key);
        preferences.edit().putLong(key, value).apply();
    }

    /**
     * Put ArrayList of Long into SharedPreferences with 'key' and save
     *
     * @param key      SharedPreferences key
     * @param longList ArrayList of Long to be added
     */
    public void putListLong(String key, ArrayList<Long> longList) {
        checkForNullKey(key);
        Long[] myLongList = longList.toArray(new Long[longList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myLongList)).apply();
    }

    /**
     * Put float value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value float value to be added
     */
    public void putFloat(String key, float value) {
        checkForNullKey(key);
        preferences.edit().putFloat(key, value).apply();
    }

    /**
     * Put double value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value double value to be added
     */
    public void putDouble(String key, double value) {
        checkForNullKey(key);
        putString(key, String.valueOf(value));
    }

    /**
     * Put ArrayList of Double into SharedPreferences with 'key' and save
     *
     * @param key        SharedPreferences key
     * @param doubleList ArrayList of Double to be added
     */
    public void putListDouble(String key, ArrayList<Double> doubleList) {
        checkForNullKey(key);
        Double[] myDoubleList = doubleList.toArray(new Double[doubleList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply();
    }

    /**
     * Put String value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value String value to be added
     */
    public void putString(String key, String value) {
        checkForNullKey(key);
        checkForNullValue(value);
        preferences.edit().putString(key, value).apply();
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     *
     * @param key        SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    /**
     * Put boolean value into SharedPreferences with 'key' and save
     *
     * @param key   SharedPreferences key
     * @param value boolean value to be added
     */
    public void putBoolean(String key, boolean value) {
        checkForNullKey(key);
        preferences.edit().putBoolean(key, value).apply();
    }

    /**
     * Put ArrayList of Boolean into SharedPreferences with 'key' and save
     *
     * @param key      SharedPreferences key
     * @param boolList ArrayList of Boolean to be added
     */
    public void putListBoolean(String key, ArrayList<Boolean> boolList) {
        checkForNullKey(key);
        ArrayList<String> newList = new ArrayList<String>();

        for (Boolean item : boolList) {
            if (item) {
                newList.add("true");
            } else {
                newList.add("false");
            }
        }

        putListString(key, newList);
    }

    /**
     * Put ObJect any type into SharedPrefrences with 'key' and save
     *
     * @param key SharedPreferences key
     * @param obj is the Object you want to put
     */
    public void putObject(String key, Object obj) {
        checkForNullKey(key);
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }

    public void putListObject(String key, List<Object> objArray) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (Object obj : objArray) {
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    /**
     * Remove SharedPreferences item with 'key'
     *
     * @param key SharedPreferences key
     */
    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    /**
     * Delete image file at 'path'
     *
     * @param path path of image file
     * @return true if it successfully deleted, false otherwise
     */
    public boolean deleteImage(String path) {
        return new File(path).delete();
    }


    /**
     * Clear SharedPreferences (remove everything)
     */
    public void clear() {
        preferences.edit().clear().apply();
    }

    /**
     * Retrieve all values from SharedPreferences. Do not modify collection return by method
     *
     * @return a Map representing a list of key/value pairs from SharedPreferences
     */
    public Map<String, ?> getAll() {
        return preferences.getAll();
    }


    /**
     * Register SharedPreferences change listener
     *
     * @param listener listener object of OnSharedPreferenceChangeListener
     */
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregister SharedPreferences change listener
     *
     * @param listener listener object of OnSharedPreferenceChangeListener to be unregistered
     */
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }


    /**
     * Check if external storage is writable or not
     *
     * @return true if writable, false otherwise
     */
    public static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Check if external storage is readable or not
     *
     * @return true if readable, false otherwise
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * Checks if an object exists in Memory
     *
     * @param key the pref key to check
     */
    public boolean objectExists(String key) {
        String gottenString = getString(key);
        return !gottenString.isEmpty();

    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     *
     * @param key the pref key to check
     */
    private void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     *
     * @param value the pref value to check
     */
    private void checkForNullValue(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
    }

//    public void setUserInfo(UserModel userModel) {
//        putObject("login_info", userModel);
//    }
//
//    public UserModel getUserInfo() {
//        return getObject("login_info", UserModel.class);
//    }


    public void setFirstLock(boolean value) {
        putBoolean("isFirstLock", value);
    }

    public boolean isFirstLock() {
        return getBoolean("isFirstLock");
    }


    public void setFCMToken(String token) {
        putString("FCM_token", token);
    }

    public String getFCMToken() {
        return getString("FCM_token");
    }

    public void setFirstRun(boolean value) {
        putBoolean("firstRun", value);
    }

    public boolean isFirstRun() {
        return getBoolean("firstRun");
    }

    public boolean isDarkTheme() {
        return getBoolean("NightMode");
    }

    public void setDarkTheme(boolean val) {
        putBoolean("NightMode", val);
    }

    public void setImageURL(String imgURL) {
        putString("imgURL", imgURL);
    }

    public String getImageURL() {
        return getString("imgURL");
    }

    public void setLockBackground(String imageURL) {
        putString("LockBackground", imageURL);
    }

    public String getLockBackground() {
        return getString("LockBackground");
    }

    public boolean getBoolean(String key, boolean bool) {
        return preferences.getBoolean(key, bool);
    }

    public boolean getFBoolean(String key) {
        return preferences.getBoolean(key, false);
    }


    public boolean isCalculatorLock() {
        return getBoolean("CalculatorLock", true);
    }
    public void setCalculatorLock(boolean value) {
        putBoolean("CalculatorLock", value);
    }
    public boolean isIntruderDetectorOnOff() {
        return getBoolean("IntruderDetectorOnOff", true);
    }
    public void setIntruderDetectorOnOff(boolean value) {
        putBoolean("IntruderDetectorOnOff", value);
    }

    public void setCalculatorLockPassword(String value) {
        putString("CalculatorLockPassword", value);
    }
    public String getCalculatorLockPassword() {
        return getCustomString("CalculatorLockPassword", "1234");
    }

    public int getAttemptFailedCount() {
        return preferences.getInt("AttemptFailedCount", 2);
    }

    public void setAttemptFailedCount(int count) {
        putInt("AttemptFailedCount", count);
    }
}
