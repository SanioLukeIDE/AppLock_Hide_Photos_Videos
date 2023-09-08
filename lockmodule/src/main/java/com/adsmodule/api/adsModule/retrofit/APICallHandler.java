package com.adsmodule.api.adsModule.retrofit;


import static com.adsmodule.api.adsModule.utils.Global.checkAppVersion;
import static com.adsmodule.api.adsModule.utils.Global.isNull;

import android.app.Activity;
import android.content.Intent;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.interfaces.AppInterfaces;
import com.adsmodule.api.adsModule.utils.Constants;
import com.adsmodule.api.adsModule.utils.Global;
import com.adsmodule.api.adsModule.utils.StringUtils;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APICallHandler {

    // App download count api
    public static void callAppCountApi(String baseURL, AdsDataRequestModel requestModel, AppInterfaces.ApiInterface counterInterface) {
        PostApiInterface apiInterface = RetroFit_APIClient.getInstance().getClient(baseURL).create(PostApiInterface.class);
        Call<String> call = apiInterface.registerAppCount(requestModel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    counterInterface.getEmptyInterface();
                    Global.sout("Counts api response >>>>>>>>>>> ", response);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*TODO nothing to handle here*/
            }
        });
    }

    // Ads api calls

    public static void callAdsApi(Activity activity, String baseURL, AdsDataRequestModel requestModel, AppInterfaces.AdDataInterface adDataInterface) {
        PostApiInterface apiInterface = RetroFit_APIClient.getInstance().getClient(baseURL).create(PostApiInterface.class);
        Call<AdsResponseModel> call = apiInterface.getAdsData(requestModel);
        call.enqueue(new Callback<AdsResponseModel>() {
            @Override
            public void onResponse(Call<AdsResponseModel> call, Response<AdsResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Constants.adsResponseModel = response.body();
                    Constants.hitCounter = Constants.adsResponseModel.getAds_count();
                    Constants.BACKPRESS_COUNT = Constants.adsResponseModel.getBackpress_count();
                    if (!isNull(Constants.adsResponseModel.getMonetize_platform())) {
                        Constants.platformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                        Constants.isFixed = StringUtils.CheckEqualIgnoreCase(Constants.adsResponseModel.getAds_sequence_type(), Constants.FIXED);
                        if (Constants.isFixed) {
                            Constants.fixedPlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                            Constants.nativePlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                            Constants.interstitialPlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                            Constants.appOpenPlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                            Constants.backPressAdPlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                        }
                    }
                    if (checkAppVersion(Constants.adsResponseModel.getVersion_name(), activity)) {
                        Global.showUpdateAppDialog(activity);
                    } else {
                        AdUtils.buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getAdx());
                        AdUtils.buildNativeCache(Constants.adsResponseModel.getNative_ads().getAdx(), activity);
                        AdUtils.buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
                        AdUtils.buildRewardAdCache(Constants.adsResponseModel.getRewarded_ads().getAdx(), activity);


                    }
                    adDataInterface.getAdData(response.body());


                } else {
                    adDataInterface.getAdData(null);
                }
            }

            @Override
            public void onFailure(Call<AdsResponseModel> call, Throwable t) {
                callOptionalAdsApi(activity, Constants.OPTIONAL_BASE_URL, requestModel, adDataInterface);
            }
        });
    }

    private static void callOptionalAdsApi(Activity activity, String optionalBaseUrl, AdsDataRequestModel requestModel, AppInterfaces.AdDataInterface adDataInterface) {
        PostApiInterface apiInterface = RetroFit_APIClient.getInstance().getClient(optionalBaseUrl).create(PostApiInterface.class);
        Call<AdsResponseModel> call = apiInterface.getAdsData(requestModel);
        call.enqueue(new Callback<AdsResponseModel>() {
            @Override
            public void onResponse(Call<AdsResponseModel> call, Response<AdsResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Constants.adsResponseModel = response.body();
                    Constants.hitCounter = Constants.adsResponseModel.getAds_count();
                    Constants.BACKPRESS_COUNT = Constants.adsResponseModel.getBackpress_count();
                    Constants.adsResponseModel.setMonetize_platform("Adx,Facebook");
                    if (!isNull(Constants.adsResponseModel.getMonetize_platform())) {
                        Constants.platformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                        Constants.isFixed = StringUtils.CheckEqualIgnoreCase(Constants.adsResponseModel.getAds_sequence_type(), Constants.FIXED);
                        if (Constants.isFixed) {
                            Constants.fixedPlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                            Constants.nativePlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                            Constants.interstitialPlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                            Constants.appOpenPlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                            Constants.backPressAdPlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                        }
                    }
                    if (checkAppVersion(Constants.adsResponseModel.getVersion_name(), activity)) {
                        Global.showUpdateAppDialog(activity);
                    } else {
                        AdUtils.buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getAdx());
                        AdUtils.buildNativeCache(Constants.adsResponseModel.getNative_ads().getAdx(), activity);
                        AdUtils.buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
                        AdUtils.buildRewardAdCache(Constants.adsResponseModel.getRewarded_ads().getAdx(), activity);


                    }
                    adDataInterface.getAdData(response.body());
                } else {
                    adDataInterface.getAdData(null);
                }
            }

            @Override
            public void onFailure(Call<AdsResponseModel> call, Throwable t) {
                adDataInterface.getAdData(null);
            }
        });
    }
}
