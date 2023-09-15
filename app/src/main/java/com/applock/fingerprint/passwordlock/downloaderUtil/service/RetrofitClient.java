package com.applock.fingerprint.passwordlock.downloaderUtil.service;

import androidx.annotation.Keep;

import com.applock.fingerprint.passwordlock.downloaderUtil.interfaces.RetrofitApiInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Keep
public class RetrofitClient {
    public static String BASE_URL = "https://www.instagram.com/";
    private static RetrofitApiInterface retrofit;

    public static RetrofitApiInterface getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(RetrofitApiInterface.class);
        }
        return retrofit;
    }


}
