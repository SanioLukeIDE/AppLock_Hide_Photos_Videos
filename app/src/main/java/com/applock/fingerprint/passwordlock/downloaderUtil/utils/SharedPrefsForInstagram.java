package com.applock.fingerprint.passwordlock.downloaderUtil.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.applock.fingerprint.passwordlock.downloaderUtil.models.ModelInstagramPref;

public class SharedPrefsForInstagram {
    public static String PREFERENCE = "Allvideodownloaderinstaprefs";
    public static String PREFERENCE_item = "instadata";
    public static String PREFERENCE_SESSIONID = "session_id";
    public static String PREFERENCE_USERID = "user_id";
    public static String PREFERENCE_COOKIES = "Cookies";
    public static String PREFERENCE_CSRF = "csrf";
    public static String PREFERENCE_ISINSTAGRAMLOGEDIN = "IsInstaLogin";
    public static Context context;

    private static SharedPrefsForInstagram instance;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;

    public SharedPrefsForInstagram() {
    }

    public SharedPrefsForInstagram(Context context) {
        SharedPrefsForInstagram.context = context;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }

    public static SharedPrefsForInstagram getInstance() {
        return instance;


    }


    public void setPreference(ModelInstagramPref map) {

        Gson gson = new Gson();

        String hashMapString = gson.toJson(map);

        editor = sharedPreference.edit();
        editor.putString(PREFERENCE_item, hashMapString);
        editor.apply();
    }

    public ModelInstagramPref getPreference() {
        try {
            Gson gson = new Gson();
            String storedHashMapString = sharedPreference.getString(PREFERENCE_item, null);

            boolean isValid = iUtils.isValidJson(storedHashMapString);
            System.out.println("validjsonis = " + isValid + "____ " + storedHashMapString);

            if (isValid) {
                return gson.fromJson(storedHashMapString, ModelInstagramPref.class);
            } else {
                return new ModelInstagramPref("", "", "", "", "false");

            }
        } catch (Exception e) {
//            e.printStackTrace();
            return new ModelInstagramPref("", "", "", "", "false");
        }
    }

    public void clearSharePrefs() {


        Gson gson = new Gson();
        String hashMapString = gson.toJson(new ModelInstagramPref("", "", "", "", "false"));

        editor = sharedPreference.edit();
        editor.putString(PREFERENCE_item, hashMapString);
        editor.apply();

    }


}
