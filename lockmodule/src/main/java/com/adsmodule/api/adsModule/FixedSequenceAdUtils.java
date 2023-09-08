package com.adsmodule.api.adsModule;

import static com.adsmodule.api.adsModule.AdUtils.loadInterstitialAd;
import static com.adsmodule.api.adsModule.AdUtils.populateUnifiedNativeAdView;
import static com.adsmodule.api.adsModule.AdUtils.showFacebookInterstitial;
import static com.adsmodule.api.adsModule.AdUtils.showFacebookNativeAd;
import static com.adsmodule.api.adsModule.utils.Global.sout;
import static com.adsmodule.api.adsModule.utils.StringUtils.CheckEqualIgnoreCase;
import static com.adsmodule.api.adsModule.utils.StringUtils.isNull;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.adsmodule.api.R;
import com.adsmodule.api.adsModule.interfaces.AppInterfaces;
import com.adsmodule.api.adsModule.utils.ConnectionDetector;
import com.adsmodule.api.adsModule.utils.Constants;
import com.adsmodule.api.adsModule.utils.Global;
import com.adsmodule.api.adsModule.utils.StringUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

public class FixedSequenceAdUtils {

    /*TODO--------------------------------------------------------- FIXED SEQUENCE NATIVE ADS---------------------------------------------------------*/
    public static void showNativeAd(Activity activity, CardView adContainer, String adType, Drawable imageID) {
        ConnectionDetector cd = new ConnectionDetector(activity);
        if (Constants.adsResponseModel.isShow_ads() && cd.isConnectingToInternet()) {
            if (Constants.nativePlatformList.get(0).equals(Constants.ADX)) {
                sout("AD Type", "NATIVE ADX");
                if (imageID != null) {
                    adContainer.addView(Global.getDefaultImage(activity, imageID));
                }
                AdLoader adLoader = new AdLoader.Builder(activity, Constants.adsResponseModel.getNative_ads().getAdx()).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {

                        NativeAdView unifiedNativeAdView;
                        try {
                            if (CheckEqualIgnoreCase(adType, "full")) {
                                unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.full_native_ad_param, null);
                            } else if (CheckEqualIgnoreCase(adType, "small")) {
                                unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.small_native_ad, null);
                            } else {
                                unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.medium_native_ad, null);
                            }
                            unifiedNativeAdView.isHardwareAccelerated();
                            populateUnifiedNativeAdView(activity, nativeAd, unifiedNativeAdView, adType);
                            adContainer.removeAllViews();
                            adContainer.setCardBackgroundColor(Color.parseColor(!isNull(Constants.adsResponseModel.getAd_bg()) ? Constants.adsResponseModel.getAd_bg() : "#FFFFFF"));
                            adContainer.addView(unifiedNativeAdView);
                            adContainer.setVisibility(View.VISIBLE);
                            Constants.nativePlatformList.remove(0);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        Constants.nativePlatformList.remove(0);
                        showNativeAd(activity, adContainer, adType, imageID);
                    }
                }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
                adLoader.loadAd(new AdRequest.Builder().build());

            } else if (CheckEqualIgnoreCase(Constants.nativePlatformList.get(0), Constants.ADMOB)) {
                sout("AD Type", "NATIVE ADMOB");
                AdLoader adLoader = new AdLoader.Builder(activity, Constants.adsResponseModel.getNative_ads().getAdmob()).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {

                        NativeAdView unifiedNativeAdView;
                        try {
                            if (CheckEqualIgnoreCase(adType, "full")) {
                                unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.full_native_ad_param, null);
                            } else if (CheckEqualIgnoreCase(adType, "small")) {
                                unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.small_native_ad, null);
                            } else {
                                unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.medium_native_ad, null);
                            }
                            unifiedNativeAdView.isHardwareAccelerated();
                            populateUnifiedNativeAdView(activity, nativeAd, unifiedNativeAdView, adType);
                            adContainer.removeAllViews();
                            adContainer.setCardBackgroundColor(Color.parseColor(!isNull(Constants.adsResponseModel.getAd_bg()) ? Constants.adsResponseModel.getAd_bg() : "#FFFFFF"));
                            adContainer.addView(unifiedNativeAdView);
                            adContainer.setVisibility(View.VISIBLE);
                            Constants.nativePlatformList.remove(0);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        Constants.nativePlatformList.remove(0);
                        showNativeAd(activity, adContainer, adType, imageID);
                    }
                }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
                adLoader.loadAd(new AdRequest.Builder().build());

            } else if (Constants.nativePlatformList.get(0).equals(Constants.FACEBOOK)) {
                sout("AD Type", "NATIVE FACEBOOK");
                showFacebookNativeAd(activity, Constants.adsResponseModel.getNative_ads().getFacebook(), adType, adContainer, new AppInterfaces.FacebookInterface() {
                    @Override
                    public void facebookAdState(boolean isLoaded) {
                        Constants.nativePlatformList.remove(0);
                        if (!isLoaded) {
                            adContainer.removeAllViews();
                            adContainer.addView(Global.getDefaultImage(activity, imageID));
                        }
                    }
                });
            }
        } else {
            if (imageID != null) {
                adContainer.setVisibility(View.VISIBLE);
                adContainer.addView(Global.getDefaultImage(activity, imageID));
            } else {
                adContainer.setVisibility(View.GONE);
            }
        }
    }
    /*TODO--------------------------------------------------------- FIXED SEQUENCE NATIVE ADS---------------------------------------------------------*/


    /*TODO--------------------------------------------------------- FIXED SEQUENCE INTERSTITIAL ADS---------------------------------------------------------*/
    public static void showInterstitialAd(Activity activity, AppInterfaces.InterstitialADInterface interstitialADInterface) {
        ConnectionDetector cd = new ConnectionDetector(activity);
        if (Constants.adsResponseModel.isShow_ads() && cd.isConnectingToInternet()) {
            Global.showAlertProgressDialog(activity);
            if (CheckEqualIgnoreCase(Constants.interstitialPlatformList.get(0), Constants.ADX)) {
                sout("AD Type", "INTERSTITIAL ADX");
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(activity, Constants.adsResponseModel.getInterstitial_ads().getAdx(), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Constants.interstitialPlatformList.remove(0);
                        Global.hideAlertProgressDialog();
                        loadInterstitialAd(activity, interstitialADInterface, interstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Global.hideAlertProgressDialog();
                        Constants.interstitialPlatformList.remove(0);
                        showInterstitialAd(activity, interstitialADInterface);
                    }
                });
            } else if (CheckEqualIgnoreCase(Constants.interstitialPlatformList.get(0), Constants.ADMOB)) {
                sout("AD Type", "INTERSTITIAL ADMOB");
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(activity, Constants.adsResponseModel.getInterstitial_ads().getAdmob(), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Constants.interstitialPlatformList.remove(0);
                        Global.hideAlertProgressDialog();
                        loadInterstitialAd(activity, interstitialADInterface, interstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Global.hideAlertProgressDialog();
                        Constants.interstitialPlatformList.remove(0);
                        showInterstitialAd(activity, interstitialADInterface);
                    }
                });
            } else if (CheckEqualIgnoreCase(Constants.interstitialPlatformList.get(0), Constants.FACEBOOK)) {
                sout("AD Type", "INTERSTITIAL FACEBOOK");
                showFacebookInterstitial(activity, Constants.adsResponseModel.getInterstitial_ads().getFacebook(), new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        Global.hideAlertProgressDialog();
                        Constants.interstitialPlatformList.remove(0);
                        interstitialADInterface.adLoadState(isLoaded);
                    }
                });
            }

        } else {
            Global.hideAlertProgressDialog();
            interstitialADInterface.adLoadState(false);
        }
    }
    /*TODO--------------------------------------------------------- FIXED SEQUENCE INTERSTITIAL ADS---------------------------------------------------------*/


    /*TODO--------------------------------------------------------- FIXED SEQUENCE APP OPEN ADS---------------------------------------------------------*/
    public static void showAppOpenAds(Activity activity, AppInterfaces.AppOpenADInterface appOpenADInterface) {
        ConnectionDetector cd = new ConnectionDetector(activity);
        if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
            if (CheckEqualIgnoreCase(Constants.adsResponseModel.getAds_open_type(), Constants.IS_APP_OPEN_ADS)) {
                Global.showAlertProgressDialog(activity);
                if (CheckEqualIgnoreCase(Constants.appOpenPlatformList.get(0), Constants.ADX)) {
                    sout("AD Type", "APPOPEN ADX");
                    AdRequest adRequest = new AdRequest.Builder().build();
                    AppOpenAd.load(activity, Constants.adsResponseModel.getApp_open_ads().getAdx(), adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {

                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            Constants.appOpenPlatformList.remove(0);
                            showAdIfAvailable(ad, activity, appOpenADInterface);
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            showAppOpenAds(activity, appOpenADInterface);
                            Global.hideAlertProgressDialog();
                            appOpenADInterface.appOpenAdState(false);
                            Global.printLog("adsLoading Failed", loadAdError.getMessage());
                            Constants.appOpenPlatformList.remove(0);
                            // Handle the error.
                        }

                    });
                } else if (CheckEqualIgnoreCase(Constants.appOpenPlatformList.get(0), Constants.ADMOB)) {
                    sout("AD Type", "APPOPEN ADMOB");
                    AdRequest adRequest = new AdRequest.Builder().build();
                    AppOpenAd.load(activity, Constants.adsResponseModel.getApp_open_ads().getAdmob(), adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {

                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            Constants.appOpenPlatformList.remove(0);
                            showAdIfAvailable(ad, activity, appOpenADInterface);
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            showAppOpenAds(activity, appOpenADInterface);
                            Global.hideAlertProgressDialog();
                            appOpenADInterface.appOpenAdState(false);
                            Global.printLog("adsLoading Failed", loadAdError.getMessage());
                            Constants.appOpenPlatformList.remove(0);
                            // Handle the error.
                        }

                    });

                } else if (CheckEqualIgnoreCase(Constants.appOpenPlatformList.get(0), Constants.FACEBOOK)) {
                    sout("AD Type", "APPOPEN FACEBOOK");
                    showFacebookInterstitial(activity, Constants.adsResponseModel.getApp_open_ads().getFacebook(), isLoaded -> {
                        Constants.appOpenPlatformList.remove(0);
                        appOpenADInterface.appOpenAdState(isLoaded);
                    });
                }
            } else {
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, appOpenADInterface::appOpenAdState);
            }
        } else {
            Global.hideAlertProgressDialog();
            appOpenADInterface.appOpenAdState(false);
        }
    }

    public static void showAdIfAvailable(AppOpenAd appopenAd, Activity activity, AppInterfaces.AppOpenADInterface appOpenADInterface) {
        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                // Set the reference to null so isAdAvailable() returns false.
                appOpenADInterface.appOpenAdState(true);
                Global.hideAlertProgressDialog();

            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                appOpenADInterface.appOpenAdState(false);
                Global.hideAlertProgressDialog();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                //appOpenADInterface.appOpenAdState(true);
            }
        };
        appopenAd.setFullScreenContentCallback(fullScreenContentCallback);
        appopenAd.show(activity);

    }

    /*TODO--------------------------------------------------------- FIXED SEQUENCE APP OPEN ADS---------------------------------------------------------*/


    /*TODO--------------------------------------------------------- FIXED SEQUENCE BACKPRESS ADS---------------------------------------------------------*/
    public static void showBackPressAd(Activity activity, AppInterfaces.AppOpenADInterface appOpenADInterface) {
        ConnectionDetector cd = new ConnectionDetector(activity);
        if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
            if (Constants.BACKPRESS_COUNT == Constants.adsResponseModel.getBackpress_count()) {
                Constants.BACKPRESS_COUNT = 0;
                if (CheckEqualIgnoreCase(Constants.backPressAdPlatformList.get(0), Constants.ADX)) {
                    if (StringUtils.CheckEqualIgnoreCase(Constants.adsResponseModel.getBackpress_ads_type(), Constants.BACKPRESS_AD_TYPE)) {
                        Global.showAlertProgressDialog(activity);
                        AdRequest adRequest = new AdRequest.Builder().build();
                        AppOpenAd.load(activity, Constants.adsResponseModel.getApp_open_ads().getAdx(), adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {

                            @Override
                            public void onAdLoaded(AppOpenAd ad) {
                                Constants.interstitialPlatformList.remove(0);
                                showAdIfAvailable(ad, activity, appOpenADInterface);
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError loadAdError) {
                                Constants.interstitialPlatformList.remove(0);
                                Global.hideAlertProgressDialog();
                                appOpenADInterface.appOpenAdState(false);
                            }

                        });
                    } else {
                        Global.hideAlertProgressDialog();
                        FixedSequenceAdUtils.showInterstitialAd(activity, isLoaded -> {
                            appOpenADInterface.appOpenAdState(isLoaded);
                        });
                    }
                } else if (CheckEqualIgnoreCase(Constants.backPressAdPlatformList.get(0), Constants.FACEBOOK)) {
                    AdUtils.showFacebookInterstitial(activity, Constants.adsResponseModel.getApp_open_ads().getFacebook(), isLoaded -> {
                        appOpenADInterface.appOpenAdState(isLoaded);
                    });
                } else if (CheckEqualIgnoreCase(Constants.backPressAdPlatformList.get(0), Constants.ADMOB)) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    AppOpenAd.load(activity, Constants.adsResponseModel.getApp_open_ads().getAdmob(), adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {

                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            Constants.interstitialPlatformList.remove(0);
                            showAdIfAvailable(ad, activity, appOpenADInterface);
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Constants.interstitialPlatformList.remove(0);
                            Global.hideAlertProgressDialog();
                            appOpenADInterface.appOpenAdState(false);
                        }

                    });
                }

            } else {
                Constants.BACKPRESS_COUNT++;
                appOpenADInterface.appOpenAdState(false);
            }
        } else {
            appOpenADInterface.appOpenAdState(false);
        }

    }

    /*TODO--------------------------------------------------------- FIXED SEQUENCE BACKPRESS ADS---------------------------------------------------------*/

}
