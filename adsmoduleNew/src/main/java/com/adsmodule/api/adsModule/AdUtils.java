package com.adsmodule.api.adsModule;

import static com.adsmodule.api.adsModule.utils.Global.isListNull;
import static com.adsmodule.api.adsModule.utils.Global.sout;
import static com.adsmodule.api.adsModule.utils.StringUtils.CheckEqualIgnoreCase;
import static com.adsmodule.api.adsModule.utils.StringUtils.isNull;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.adsmodule.api.R;
import com.adsmodule.api.adsModule.interfaces.AppInterfaces;
import com.adsmodule.api.adsModule.utils.ConnectionDetector;
import com.adsmodule.api.adsModule.utils.Constants;
import com.adsmodule.api.adsModule.utils.Global;
import com.adsmodule.api.adsModule.utils.StringUtils;
import com.facebook.ads.Ad;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AdUtils {

    @SuppressLint("StaticFieldLeak")
    private static ConnectionDetector cd;
    //TODO AD COUNTERS----------------------------------------------    //
    private static int interstitialHitCounter = 0;
    private static int nativeHitCounter = 0;
    private static int appOpenHitCounter = 0;
    private static int rewardAdHitCounter = 0;
    private static int bannerAdHitCounter = 0;
    private static int collapsibleAdHitCounter = 0;
    //TODO AD COUNTERS----------------------------------------------     //

    //TODO PRECACHE ADS----------------------------------------------     //
    private static InterstitialAd precacheInterstitialAd;
    private static NativeAd precacheNativeAd;
    private static AppOpenAd precacheAppOpenAd;
    private static RewardedAd precacheRewardAd;

    // TODO INTERSTITIAL AD IMPLEMENTATION--------------------------------------------------------------------------------------------------------------
    public static void buildInterstitialAdCache(String interstitialAdId, Activity activity) {
        precacheInterstitialAd = null;
        cd = new ConnectionDetector(activity);
        if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads() && !isNull(interstitialAdId)) {
            if (Constants.hitCounter == Constants.adsResponseModel.getAds_count()) {
                Constants.hitCounter = 0;
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(activity, interstitialAdId, adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        precacheInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        if (!isListNull(Constants.platformList)) {
                            switch (interstitialHitCounter) {
                                case 0:
                                    interstitialHitCounter++;
                                    if (Constants.platformList.contains(Constants.ADMOB))
                                        buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdmob(), activity);
                                    break;
                                case 1:
                                    interstitialHitCounter++;
                                    if (Constants.platformList.contains(Constants.FACEBOOK))
                                        buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getFacebook(), activity);
                                    break;
                                case 2:
                                    buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
                                    interstitialHitCounter = 0;
                                    break;
                            }
                        } else {
                            interstitialHitCounter = 0;
                        }

                    }
                });
            }
        }
    }

    public static void showInterstitialAd(String interstitialAd, Activity activity, AppInterfaces.InterstitialADInterface interstitialADInterface) {
        interstitialHitCounter = 0;
        if (!Constants.isFixed) {
            cd = new ConnectionDetector(activity);
            Collections.shuffle(Constants.platformList);
            if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
                Global.showAlertProgressDialog(activity);
                if (!isListNull(Constants.platformList)) {
                    if (Constants.platformList.get(0).equals(Constants.ADX) || Constants.platformList.get(0).equals(Constants.ADMOB)) {
                        if (Constants.hitCounter == Constants.adsResponseModel.getAds_count()) {
                            Constants.hitCounter = 0;
                            AdRequest adRequest = new AdRequest.Builder().build();
                            if (precacheInterstitialAd == null) {
                                InterstitialAd.load(activity, interstitialAd, adRequest, new InterstitialAdLoadCallback() {
                                    @Override
                                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                        Global.hideAlertProgressDialog();
                                        loadInterstitialAd(activity, interstitialADInterface, interstitialAd);
                                    }

                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        Global.hideAlertProgressDialog();
                                        if (!isListNull(Constants.platformList)) {
                                            switch (interstitialHitCounter) {
                                                case 0:
                                                    interstitialHitCounter++;
                                                    if (Constants.platformList.contains(Constants.ADMOB))
                                                        showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdmob(), activity, interstitialADInterface);
                                                    break;
                                                case 1:
                                                    interstitialHitCounter++;
                                                    if (Constants.platformList.contains(Constants.FACEBOOK))
                                                        showFacebookInterstitial(activity, Constants.adsResponseModel.getInterstitial_ads().getFacebook(), interstitialADInterface);
                                                    break;
                                                case 2:
                                                    interstitialHitCounter = 0;
                                                    buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
                                                    Global.hideAlertProgressDialog();
                                                    interstitialADInterface.adLoadState(false);
                                                    break;
                                            }
                                        } else {
                                            interstitialHitCounter = 0;
                                            Global.hideAlertProgressDialog();
                                            interstitialADInterface.adLoadState(false);
                                        }
                                    }
                                });
                            } else {
                                loadInterstitialAd(activity, interstitialADInterface, precacheInterstitialAd);
                            }

                        } else {
                            Global.hideAlertProgressDialog();
                            interstitialADInterface.adLoadState(false);
                            Constants.hitCounter++;
                        }
                    } else {
                        if (!isListNull(Constants.platformList)) {
                            if (Constants.platformList.contains(Constants.ADMOB)) {
                                showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdmob(), activity, interstitialADInterface);
                            } else if (Constants.platformList.contains(Constants.FACEBOOK)) {
                                showFacebookInterstitial(activity, Constants.adsResponseModel.getInterstitial_ads().getFacebook(), interstitialADInterface);
                            } else {
                                buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
                                Global.hideAlertProgressDialog();
                                interstitialADInterface.adLoadState(false);
                            }
                        } else {
                            interstitialHitCounter = 0;
                            Global.hideAlertProgressDialog();
                            interstitialADInterface.adLoadState(false);
                        }

                    }
                } else {
                    interstitialHitCounter = 0;
                    Global.hideAlertProgressDialog();
                    interstitialADInterface.adLoadState(false);
                   /* AdRequest adRequest = new AdRequest.Builder().build();
                    InterstitialAd.load(activity, interstitialAd, adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            Global.hideAlertProgressDialog();
                            loadInterstitialAd(activity, interstitialADInterface, interstitialAd);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            if (!isListNull(Constants.platformList)) {
                                switch (interstitialHitCounter) {
                                    case 0:
                                        interstitialHitCounter++;
                                        if (Constants.platformList.contains(Constants.ADMOB))
                                            showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdmob(), activity, interstitialADInterface);
                                        break;
                                    case 1:
                                        interstitialHitCounter++;
                                        if (Constants.platformList.contains(Constants.FACEBOOK))
                                            showFacebookInterstitial(activity, Constants.adsResponseModel.getInterstitial_ads().getFacebook(), interstitialADInterface);
                                        break;
                                    case 2:
                                        interstitialHitCounter = 0;
                                        buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
                                        Global.hideAlertProgressDialog();
                                        interstitialADInterface.adLoadState(false);
                                        break;
                                }
                            } else {

                            }
                        }
                    });*/
                }
            } else {
                Global.hideAlertProgressDialog();
                interstitialADInterface.adLoadState(false);
            }
        } else {
            if (Constants.interstitialPlatformList.isEmpty())
                Constants.interstitialPlatformList.addAll(Constants.platformList);
            FixedSequenceAdUtils.showInterstitialAd(activity, interstitialADInterface);
        }
    }

    static void loadInterstitialAd(Activity activity, AppInterfaces.InterstitialADInterface interStitialADInterface, InterstitialAd interstitialAd) {
        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                @Override
                public void onAdDismissedFullScreenContent() {

//                    interStitialADInterface.adLoadState(true);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                    interStitialADInterface.adLoadState(false);
                }

                @Override
                public void onAdShowedFullScreenContent() {

                }
            });
            Global.hideAlertProgressDialog();
            interStitialADInterface.adLoadState(true);
            buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
            interstitialAd.show(activity);
        } else {
            Global.hideAlertProgressDialog();
            interStitialADInterface.adLoadState(false);
        }
    }

    public static void showFacebookInterstitial(Activity activity, String facebookAdId, AppInterfaces.InterstitialADInterface interstitialADInterface) {
        if (!isNull(facebookAdId)) {
            com.facebook.ads.InterstitialAd facebookInterstitialAd = new com.facebook.ads.InterstitialAd(activity, facebookAdId);
            facebookInterstitialAd.loadAd(facebookInterstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    Global.sout("Face ad displayed >>> ", adError);
                    Global.hideAlertProgressDialog();
                    interstitialADInterface.adLoadState(false);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Global.hideAlertProgressDialog();
                    interstitialADInterface.adLoadState(true);
                    facebookInterstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            }).build());
        } else {
            Global.hideAlertProgressDialog();
            interstitialADInterface.adLoadState(false);
        }
    }
    // TODO INTERSTITIAL AD IMPLEMENTATION--------------------------------------------------------------------------------------------------------------


    // TODO NATIVE AD IMPLEMENTATION--------------------------------------------------------------------------------------------------------------
    public static void buildNativeCache(String nativeAd, Activity activity) {
        cd = new ConnectionDetector(activity);
        precacheNativeAd = null;
        if (!isNull(nativeAd)) {
            if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
                try {
                    AdLoader adLoader = new AdLoader.Builder(activity, nativeAd).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                            precacheNativeAd = nativeAd;
                        }
                    }).withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                            Global.sout("Native failed for ad>>>>>>>>>>>>>>>>>" + nativeHitCounter, adError.toString());
                            if (!isListNull(Constants.platformList)) {
                                switch (nativeHitCounter) {
                                    case 0:
                                        nativeHitCounter++;
                                        if (Constants.platformList.contains(Constants.ADMOB))
                                            buildNativeCache(Constants.adsResponseModel.getNative_ads().getAdmob(), activity);
                                        break;
                                    case 1:
                                        nativeHitCounter++;
                                        break;
                                }
                            } else {
                                nativeHitCounter = 0;
                            }

                        }
                    }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
                    adLoader.loadAd(new AdRequest.Builder().build());
                } catch (Exception e) {
                    Global.sout("native ads exception>>", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }


    }

    public static void showNativeAd(Activity activity, String nativeAd, CardView adContainer, String adType, Drawable imageID) {
        nativeHitCounter = 0;
        cd = new ConnectionDetector(activity);
        if (!Constants.isFixed) {
            Collections.shuffle(Constants.platformList);
            if (imageID != null) {
                adContainer.addView(Global.getDefaultImage(activity, imageID));
            }
            if (!isListNull(Constants.platformList) && cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
                if (Constants.platformList.get(0).equals(Constants.ADX) || Constants.platformList.get(0).equals(Constants.ADMOB)) {
                    if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
                        try {
                            if (precacheNativeAd != null) {
                                try {
                                    NativeAdView unifiedNativeAdView;
                                    if (CheckEqualIgnoreCase(adType, "full")) {
                                        unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.full_native_ad_param, (ViewGroup) null);
                                    } else if (CheckEqualIgnoreCase(adType, "small")) {
                                        unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.small_native_ad, (ViewGroup) null);
                                    } else {
                                        unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.medium_native_ad, (ViewGroup) null);
                                    }

                                    populateUnifiedNativeAdView(activity, precacheNativeAd, unifiedNativeAdView, adType);
                                    adContainer.removeAllViews();
                                    adContainer.setCardBackgroundColor(Color.parseColor(!isNull(Constants.adsResponseModel.getAd_bg()) ? Constants.adsResponseModel.getAd_bg() : "#FFFFFF"));
                                    adContainer.addView(unifiedNativeAdView);
                                    adContainer.setVisibility(View.VISIBLE);
                                    buildNativeCache(Constants.adsResponseModel.getNative_ads().getAdx(), activity);
                                } catch (Exception e2) {
                                    buildNativeCache(Constants.adsResponseModel.getNative_ads().getAdx(), activity);
                                    e2.printStackTrace();
                                }

                            } else {
                                AdLoader adLoader = new AdLoader.Builder(activity, nativeAd).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                                    @Override
                                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {

                                        NativeAdView unifiedNativeAdView;
                                        try {
                                            if (CheckEqualIgnoreCase(adType, "full")) {
                                                unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.full_native_ad_param, (ViewGroup) null);
                                            } else if (CheckEqualIgnoreCase(adType, "small")) {
                                                unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.small_native_ad, (ViewGroup) null);
                                            } else {
                                                unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.medium_native_ad, (ViewGroup) null);
                                            }

                                            populateUnifiedNativeAdView(activity, nativeAd, unifiedNativeAdView, adType);
                                            adContainer.removeAllViews();
                                            adContainer.setCardBackgroundColor(Color.parseColor(!isNull(Constants.adsResponseModel.getAd_bg()) ? Constants.adsResponseModel.getAd_bg() : "#FFFFFF"));
                                            adContainer.addView(unifiedNativeAdView);
                                            adContainer.setVisibility(View.VISIBLE);

                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                }).withAdListener(new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                        sout("Native failed for ad>>>>>>>>>>>>>>>>>" + nativeHitCounter, adError.toString());
                                        if (!isListNull(Constants.platformList)) {
                                            switch (nativeHitCounter) {
                                                case 0:
                                                    nativeHitCounter++;
                                                    if (Constants.platformList.contains(Constants.ADMOB))
                                                        showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdmob(), adContainer, adType, imageID);
                                                    break;
                                                case 1:
                                                    nativeHitCounter++;
                                                    if (Constants.platformList.contains(Constants.FACEBOOK))
                                                        showFacebookNativeAd(activity, Constants.adsResponseModel.getNative_ads().getFacebook(), adType, adContainer, new AppInterfaces.FacebookInterface() {
                                                            @Override
                                                            public void facebookAdState(boolean isLoaded) {
                                                                if (!isLoaded) {
                                                                    try {
                                                                        if (imageID != null) {
                                                                            adContainer.removeAllViews();
                                                                            adContainer.addView(Global.getDefaultImage(activity, imageID));
                                                                        }
                                                                    } catch (Exception e) {
                                                                        sout("facebook native ads exception>>", e.getLocalizedMessage());
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    break;
                                            }
                                        }

                                    }
                                }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
                                adLoader.loadAd(new AdRequest.Builder().build());
                            }
                        } catch (Exception e) {
                            Global.sout("native ads exception>>", e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    } else {
                        if (imageID != null) {
                            adContainer.removeAllViews();
                            adContainer.addView(Global.getDefaultImage(activity, imageID));
                        } else adContainer.setVisibility(View.GONE);
                    }
                } else {
                    if (!isListNull(Constants.platformList) && Constants.adsResponseModel.isShow_ads()) {
                        if (Constants.platformList.contains(Constants.ADMOB))
                            showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdmob(), adContainer, adType, imageID);
                        else {
                            showFacebookNativeAd(activity, Constants.adsResponseModel.getNative_ads().getFacebook(), adType, adContainer, new AppInterfaces.FacebookInterface() {
                                @Override
                                public void facebookAdState(boolean isLoaded) {
                                    if (!isLoaded) {
                                        try {
                                            if (imageID != null) {
                                                adContainer.removeAllViews();
                                                adContainer.addView(Global.getDefaultImage(activity, imageID));
                                            }
                                        } catch (Exception e) {
                                            Global.sout("facebook native ads exception>>", e.getLocalizedMessage());
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        if (imageID != null) {
                            adContainer.removeAllViews();
                            adContainer.addView(Global.getDefaultImage(activity, imageID));
                        } else adContainer.setVisibility(View.GONE);

                    }

                }

            } else {
                if (imageID != null) {
                    adContainer.removeAllViews();
                    adContainer.addView(Global.getDefaultImage(activity, imageID));
                } else adContainer.setVisibility(View.GONE);
            }
        } else {
            if (Constants.nativePlatformList.isEmpty())
                Constants.nativePlatformList.addAll(Constants.platformList);
            FixedSequenceAdUtils.showNativeAd(activity, adContainer, adType, imageID);
        }
    }

    public static void populateUnifiedNativeAdView(Activity activity, NativeAd unifiedNativeAd, NativeAdView unifiedNativeAdView, String adType) {
        if (CheckEqualIgnoreCase(adType, "full")) {
            unifiedNativeAdView.setMediaView(unifiedNativeAdView.findViewById(R.id.ad_media));
        }
        unifiedNativeAdView.setHeadlineView(unifiedNativeAdView.findViewById(R.id.ad_headline));
        unifiedNativeAdView.setBodyView(unifiedNativeAdView.findViewById(R.id.ad_body));
        unifiedNativeAdView.setCallToActionView(unifiedNativeAdView.findViewById(R.id.ad_call_to_action));
        unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(R.id.ad_app_icon));
        unifiedNativeAdView.setPriceView(unifiedNativeAdView.findViewById(R.id.ad_price));
        unifiedNativeAdView.setStarRatingView(unifiedNativeAdView.findViewById(R.id.ad_stars));
        unifiedNativeAdView.setStoreView(unifiedNativeAdView.findViewById(R.id.ad_store));
        unifiedNativeAdView.setAdvertiserView(unifiedNativeAdView.findViewById(R.id.ad_advertiser));
        ((TextView) Objects.requireNonNull(unifiedNativeAdView.getHeadlineView())).setText(unifiedNativeAd.getHeadline());
        ((TextView) unifiedNativeAdView.getHeadlineView()).setTextColor(!isNull(Constants.adsResponseModel.getCommon_text_color()) ? Color.parseColor(Constants.adsResponseModel.getCommon_text_color()) : activity.getResources().getColor(R.color.black));
        if (unifiedNativeAd.getBody() == null) {
            Objects.requireNonNull(unifiedNativeAdView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            //TODO sets color to headline button -->
            Objects.requireNonNull(unifiedNativeAdView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) unifiedNativeAdView.getBodyView()).setText(unifiedNativeAd.getBody());
            ((TextView) unifiedNativeAdView.getBodyView()).setTextColor(!isNull(Constants.adsResponseModel.getCommon_text_color()) ? Color.parseColor(Constants.adsResponseModel.getCommon_text_color()) : activity.getResources().getColor(R.color.black));
        }

        if (unifiedNativeAd.getCallToAction() == null) {
            Objects.requireNonNull(unifiedNativeAdView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(unifiedNativeAdView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) unifiedNativeAdView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            //TODO sets color to install button -->
            unifiedNativeAdView.getCallToActionView().setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constants.adsResponseModel.getButton_bg())));
            ((Button) unifiedNativeAdView.getCallToActionView()).setTextColor(!isNull(Constants.adsResponseModel.getButton_text_color()) ? Color.parseColor(Constants.adsResponseModel.getButton_text_color()) : activity.getResources().getColor(R.color.white));
        }

        if (unifiedNativeAd.getIcon() == null) {
            Objects.requireNonNull(unifiedNativeAdView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(unifiedNativeAdView.getIconView())).setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
            unifiedNativeAdView.getIconView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getPrice() == null) {
            Objects.requireNonNull(unifiedNativeAdView.getPriceView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(unifiedNativeAdView.getPriceView()).setVisibility(View.GONE);
            ((TextView) unifiedNativeAdView.getPriceView()).setText(unifiedNativeAd.getPrice());
            ((TextView) unifiedNativeAdView.getPriceView()).setTextColor(!isNull(Constants.adsResponseModel.getCommon_text_color()) ? Color.parseColor(Constants.adsResponseModel.getCommon_text_color()) : activity.getResources().getColor(R.color.black));

        }
        if (unifiedNativeAd.getStore() == null) {
            Objects.requireNonNull(unifiedNativeAdView.getStoreView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(unifiedNativeAdView.getStoreView()).setVisibility(View.GONE);
            ((TextView) unifiedNativeAdView.getStoreView()).setText(unifiedNativeAd.getStore());
            ((TextView) unifiedNativeAdView.getStoreView()).setTextColor(!isNull(Constants.adsResponseModel.getCommon_text_color()) ? Color.parseColor(Constants.adsResponseModel.getCommon_text_color()) : activity.getResources().getColor(R.color.black));
        }

        if (unifiedNativeAd.getStarRating() == null) {
            Objects.requireNonNull(unifiedNativeAdView.getStarRatingView()).setVisibility(View.VISIBLE);
        } else {
            ((RatingBar) Objects.requireNonNull(unifiedNativeAdView.getStarRatingView())).setRating(unifiedNativeAd.getStarRating().floatValue());
            unifiedNativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((RatingBar) unifiedNativeAdView.getStarRatingView()).setProgressBackgroundTintList(!isNull(Constants.adsResponseModel.getButton_bg()) ? ColorStateList.valueOf(Color.parseColor(Constants.adsResponseModel.getButton_bg())) : ColorStateList.valueOf(activity.getColor(R.color.black)));
                ((RatingBar) unifiedNativeAdView.getStarRatingView()).setProgressTintList(!isNull(Constants.adsResponseModel.getButton_bg()) ? ColorStateList.valueOf(Color.parseColor(Constants.adsResponseModel.getButton_bg())) : ColorStateList.valueOf(activity.getColor(R.color.black)));
            }
        }

        if (unifiedNativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(unifiedNativeAdView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(unifiedNativeAdView.getAdvertiserView())).setText(unifiedNativeAd.getAdvertiser());
            unifiedNativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) unifiedNativeAdView.getAdvertiserView()).setTextColor(!isNull(Constants.adsResponseModel.getCommon_text_color()) ? Color.parseColor(Constants.adsResponseModel.getCommon_text_color()) : activity.getResources().getColor(R.color.black));
        }
        if (CheckEqualIgnoreCase(adType, "medium")) {
            unifiedNativeAdView.getBodyView().setVisibility(View.VISIBLE);
            unifiedNativeAdView.getIconView().setVisibility(View.GONE);
            unifiedNativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        unifiedNativeAdView.setNativeAd(unifiedNativeAd);

    }

    public static void showFacebookNativeAd(Activity activity, String facebookAdId, String adType, CardView adContainer, AppInterfaces.FacebookInterface facebookInterface) {
        com.facebook.ads.NativeAd faceBookNativeAd = new com.facebook.ads.NativeAd(activity, facebookAdId);
        faceBookNativeAd.loadAd(faceBookNativeAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                facebookInterface.facebookAdState(true);
                inflateFaceBookAd(activity, adType, (com.facebook.ads.NativeAd) ad, adContainer);
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                facebookInterface.facebookAdState(false);
                System.out.println(adError);
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        }).build());
    }

    private static void inflateFaceBookAd(Activity activity, String adType, com.facebook.ads.NativeAd facebookAd, CardView adContainer) {
        try {
            adContainer.removeAllViews();
//        adContainer.setCardBackgroundColor(Color.parseColor(Constants.adsResponseModel.getAd_bg()));
            adContainer.setVisibility(View.VISIBLE);
            facebookAd.unregisterView();

            LayoutInflater inflater = LayoutInflater.from(activity);
            View view;
            if (CheckEqualIgnoreCase(adType, "full")) {
                view = inflater.inflate(R.layout.ads_nb_fb_full, null);
            } else {
                view = inflater.inflate(R.layout.ads_nb_fb, null);
            }

            adContainer.addView(view);
            NativeAdLayout nativeAdLayout = view.findViewById(R.id.nativview);
            LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
            AdOptionsView adOptionsView = new AdOptionsView(activity, facebookAd, nativeAdLayout);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);
            TextView nativeAdCallToAction = view.findViewById(R.id.nb_ad_call_to_action);

            TextView nativeAdTitle = view.findViewById(R.id.native_ad_title);
//        nativeAdTitle.setTextColor(Color.parseColor(Constants.adsResponseModel.getCommon_text_color()));

            TextView nativeAdSocialContext = view.findViewById(R.id.native_ad_social_context);
//        nativeAdSocialContext.setTextColor(Color.parseColor(Constants.adsResponseModel.getCommon_text_color()));
            com.facebook.ads.MediaView nativeAdIconView = view.findViewById(R.id.native_icon_view);

            nativeAdCallToAction.setText(facebookAd.getAdCallToAction());

//        nativeAdCallToAction.setTextColor(Color.parseColor(Constants.adsResponseModel.getButton_text_color()));
            nativeAdCallToAction.setVisibility(facebookAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);

            nativeAdTitle.setText(facebookAd.getAdvertiserName());
//        nativeAdTitle.setTextColor(Color.parseColor(Constants.adsResponseModel.getCommon_text_color()));

            nativeAdSocialContext.setText(facebookAd.getAdBodyText());
//        nativeAdSocialContext.setTextColor(Color.parseColor(Constants.adsResponseModel.getCommon_text_color()));

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);
            clickableViews.add(nativeAdIconView);
            clickableViews.add(nativeAdSocialContext);
            facebookAd.registerViewForInteraction(view, nativeAdIconView, clickableViews);
        } catch (Exception e) {
            sout("FaceBoook ad issue", e.getLocalizedMessage());
        }

    }
    // TODO NATIVE AD IMPLEMENTATION--------------------------------------------------------------------------------------------------------------

    // TODO APPOPEN ADS--------------------------------------------------------------------------------------------------------------
    public static void buildAppOpenAdCache(Activity activity, String appOpenAdId) {
        precacheAppOpenAd = null;
        ConnectionDetector cd = new ConnectionDetector(activity);
        if (!isNull(Constants.adsResponseModel.getApp_open_ads().getAdx()) && Constants.adsResponseModel != null && cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
            if (StringUtils.CheckEqualIgnoreCase(Constants.adsResponseModel.getAds_open_type(), Constants.IS_APP_OPEN_ADS)) {
                AdRequest adRequest = new AdRequest.Builder().build();
                AppOpenAd.load(activity, appOpenAdId, adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {

                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        precacheAppOpenAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        if (!isListNull(Constants.platformList)) {
                            switch (appOpenHitCounter) {
                                case 0:
                                    appOpenHitCounter++;
                                    if (Constants.platformList.contains(Constants.ADMOB))
                                        buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getAdmob());
                                    break;
                                case 1:
                                    appOpenHitCounter++;
                                    if (Constants.platformList.contains(Constants.FACEBOOK))
                                        buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getFacebook());
                                    break;
                                case 2:
                                    appOpenHitCounter = 0;
                                    break;
                            }
                        }

                        Global.printLog("adsLoading Failed ==", loadAdError.getMessage());
                        // Handle the error.
                    }

                });
            } else {
                buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
            }
        }

    }

    public static void showAppOpenAds(String appOpenAd, Activity activity, AppInterfaces.AppOpenADInterface appOpenADInterface) {
        appOpenHitCounter = 0;
        if (!Constants.isFixed) {
            ConnectionDetector cd = new ConnectionDetector(activity);
            Collections.shuffle(Constants.platformList);
            if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
                if (StringUtils.CheckEqualIgnoreCase(Constants.adsResponseModel.getAds_open_type(), Constants.IS_APP_OPEN_ADS)) {
                    Global.showAlertProgressDialog(activity);
                    if (!Constants.platformList.isEmpty()) {
                        if (Constants.platformList.get(0).equals(Constants.ADX) || Constants.platformList.get(0).equals(Constants.ADMOB)) {
                            if (precacheAppOpenAd != null) {
                                showAdIfAvailable(precacheAppOpenAd, activity, appOpenADInterface);
                            } else {

                                AdRequest adRequest = new AdRequest.Builder().build();
                                AppOpenAd.load(activity, appOpenAd, adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {

                                    @Override
                                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                                        Global.hideAlertProgressDialog();
                                        showAdIfAvailable(ad, activity, appOpenADInterface);
                                    }

                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        Global.hideAlertProgressDialog();
                                        if (!isListNull(Constants.platformList)) {
                                            switch (appOpenHitCounter) {
                                                case 0:
                                                    appOpenHitCounter++;
                                                    if (Constants.platformList.contains(Constants.ADMOB))
                                                        showAppOpenAds(Constants.adsResponseModel.getApp_open_ads().getAdmob(), activity, appOpenADInterface);
                                                    break;
                                                case 1:
                                                    appOpenHitCounter++;
                                                    if (Constants.platformList.contains(Constants.FACEBOOK))
                                                        showAppOpenAds(Constants.adsResponseModel.getApp_open_ads().getFacebook(), activity, appOpenADInterface);
                                                    break;
                                                case 2:
                                                    appOpenHitCounter = 0;
                                                    Global.hideAlertProgressDialog();
                                                    buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getAdx());
                                                    appOpenADInterface.appOpenAdState(false);
                                                    break;
                                            }
                                        } else {
                                            appOpenHitCounter = 0;
                                            Global.hideAlertProgressDialog();
                                            appOpenADInterface.appOpenAdState(false);
                                        }

                                        Global.printLog("adsLoading Failed", loadAdError.getMessage());
                                        // Handle the error.
                                    }

                                });
                            }
                        } else {
                            showFacebookInterstitial(activity, Constants.adsResponseModel.getInterstitial_ads().getFacebook(), new AppInterfaces.InterstitialADInterface() {
                                @Override
                                public void adLoadState(boolean isLoaded) {
                                    appOpenADInterface.appOpenAdState(isLoaded);
                                }
                            });
                        }
                    } else {
                        AdRequest adRequest = new AdRequest.Builder().build();
                        AppOpenAd.load(activity, Constants.adsResponseModel.getApp_open_ads().getAdx(), adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {

                            @Override
                            public void onAdLoaded(@NonNull AppOpenAd ad) {
                                showAdIfAvailable(ad, activity, appOpenADInterface);
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                appOpenHitCounter = 0;
                                Global.hideAlertProgressDialog();
                                appOpenADInterface.appOpenAdState(false);
                            }

                        });
                    }


                } else {
                    Global.hideAlertProgressDialog();
                    showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, appOpenADInterface::appOpenAdState);
                }
            } else {
                Global.hideAlertProgressDialog();
                appOpenADInterface.appOpenAdState(false);
            }
        } else {
            if (Constants.appOpenPlatformList.isEmpty())
                Constants.appOpenPlatformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
            FixedSequenceAdUtils.showAppOpenAds(activity, appOpenADInterface);
        }

    }

    public static void showAdIfAvailable(AppOpenAd appopenAd, Activity activity, AppInterfaces.AppOpenADInterface appOpenADInterface) {
        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                // Set the reference to null so isAdAvailable() returns false.
                appOpenADInterface.appOpenAdState(true);
                Global.hideAlertProgressDialog();
                buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getAdx());
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                appOpenADInterface.appOpenAdState(false);
                buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getAdx());
                Global.hideAlertProgressDialog();
            }

            @Override
            public void onAdShowedFullScreenContent() {
            }
        };
        appopenAd.setFullScreenContentCallback(fullScreenContentCallback);
        appopenAd.show(activity);

    }
    //TODO APPOPEN ADS--------------------------------------------------------------------------------------------------------------


    //TODO REWARD ADS--------------------------------------------------------------------------------------------------------------
    public static void buildRewardAdCache(String rewardAd, Activity activity) {
        cd = new ConnectionDetector(activity);
        if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads() && !isNull(rewardAd)) {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(activity, rewardAd, adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    if (!isListNull(Constants.platformList)) {
                        switch (rewardAdHitCounter) {
                            case 0:
                                rewardAdHitCounter++;
                                if (Constants.platformList.contains(Constants.ADMOB))
                                    buildRewardAdCache(Constants.adsResponseModel.getRewarded_ads().getAdmob(), activity);
                                break;
                            case 1:
                                rewardAdHitCounter++;
                                if (Constants.platformList.contains(Constants.FACEBOOK))
                                    buildRewardAdCache(Constants.adsResponseModel.getRewarded_ads().getFacebook(), activity);
                                break;
                            case 2:
                                rewardAdHitCounter = 0;
                                buildRewardAdCache(Constants.adsResponseModel.getRewarded_ads().getAdx(), activity);
                                break;

                        }
                    } else {
                        rewardAdHitCounter = 0;
                    }
                    Global.sout("RewardedAdLoadCallback: ", loadAdError.toString());

                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    precacheRewardAd = rewardedAd;

                }

            });
        }
    }

    public static void showRewardAd(String rewardAd, Activity activity, AppInterfaces.RewardedAd rewardedAdInterface) {
        cd = new ConnectionDetector(activity);
        if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads() && !isNull(rewardAd)) {
            if (precacheRewardAd != null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedAd.load(activity, rewardAd, adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Global.sout("RewardedAdLoadCallback: ", loadAdError.toString());
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        rewardedAd.show(activity, new OnUserEarnedRewardListener() {

                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                //TODO user earned reward --Not to implement now--
                            }
                        });
                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                            @Override
                            public void onAdShowedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                Global.sout("onAdFailedToShowFullScreenContent: ", adError.toString());
                                if (!isListNull(Constants.platformList)) {
                                    switch (rewardAdHitCounter) {
                                        case 0:
                                            rewardAdHitCounter++;
                                            if (Constants.platformList.contains(Constants.ADMOB))
                                                showRewardAd(Constants.adsResponseModel.getRewarded_ads().getAdmob(), activity, rewardedAdInterface);
                                            break;
                                        case 1:
                                            rewardAdHitCounter++;
                                            if (Constants.platformList.contains(Constants.FACEBOOK))
                                                showRewardAd(Constants.adsResponseModel.getRewarded_ads().getFacebook(), activity, rewardedAdInterface);
                                            break;
                                        case 2:
                                            rewardedAdInterface.rewardState(false);
                                            break;

                                    }
                                } else {
                                    rewardedAdInterface.rewardState(false);
                                }


                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                rewardedAdInterface.rewardState(false);
                            }
                        });
                    }

                });
            } else {

                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedAd.load(activity, rewardAd, adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Global.sout("RewardedAdLoadCallback: ", loadAdError.toString());
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        rewardedAd.show(activity, new OnUserEarnedRewardListener() {

                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                //TODO user earned reward --Not to implement now--
                            }
                        });
                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                            @Override
                            public void onAdShowedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                Global.sout("onAdFailedToShowFullScreenContent: ", adError.toString());
                                if (!isListNull(Constants.platformList)) {
                                    switch (rewardAdHitCounter) {
                                        case 0:
                                            rewardAdHitCounter++;
                                            if (Constants.platformList.contains(Constants.ADMOB))
                                                showRewardAd(Constants.adsResponseModel.getRewarded_ads().getAdmob(), activity, rewardedAdInterface);
                                            break;
                                        case 1:
                                            rewardAdHitCounter++;
                                            if (Constants.platformList.contains(Constants.FACEBOOK))
                                                showRewardAd(Constants.adsResponseModel.getRewarded_ads().getFacebook(), activity, rewardedAdInterface);
                                            break;
                                        case 2:
                                            buildRewardAdCache(Constants.adsResponseModel.getRewarded_ads().getAdx(), activity);
                                            rewardedAdInterface.rewardState(false);
                                            break;

                                    }
                                } else {
                                    rewardedAdInterface.rewardState(false);
                                }


                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                rewardedAdInterface.rewardState(false);
                            }
                        });
                    }

                });
            }
        } else {
            rewardedAdInterface.rewardState(false);
        }
    }
    //TODO REWARD ADS--------------------------------------------------------------------------------------------------------------

    //TODO BANNER ADS--------------------------------------------------------------------------------------------------------------
    public static void showBannerAd(String bannerAdID, LinearLayout adContainer, Activity activity) {
        cd = new ConnectionDetector(activity);
        if (!isNull(bannerAdID)) {
            if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
                AdView mAdView = new AdView(activity);
                if (cd.isConnectingToInternet()) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    try {
                        mAdView.setVisibility(View.VISIBLE);
                        mAdView.setEnabled(true);
                        mAdView.setAdSize(AdSize.BANNER);
                        mAdView.setAdUnitId(bannerAdID);
                        adContainer.setVisibility(View.VISIBLE);
                        mAdView.setAdListener(new AdListener() {
                            public void onAdFailedToLoad(int i) {
                                if (!isListNull(Constants.platformList)) {
                                    switch (bannerAdHitCounter) {
                                        case 0:
                                            bannerAdHitCounter++;
                                            if (Constants.platformList.contains(Constants.ADMOB))
                                                showBannerAd(Constants.adsResponseModel.getBanner_ads().getAdmob(), adContainer, activity);
                                            break;
                                        case 1:
                                            bannerAdHitCounter++;
                                            if (Constants.platformList.contains(Constants.FACEBOOK))
                                                showBannerAd(Constants.adsResponseModel.getBanner_ads().getFacebook(), adContainer, activity);
                                            break;
                                        case 2:
                                            adContainer.setVisibility(View.GONE);
                                            break;
                                    }
                                } else {
                                    adContainer.setVisibility(View.GONE);
                                }

                                Global.sout("Banner ad failed >>>>>>>>>>>>>>> ", i + "");
                            }

                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            public void onAdLoaded() {
                            }

                            public void onAdOpened() {
                            }

                            public void onAdClosed() {
                            }
                        });

                        mAdView.loadAd(adRequest);
                        adContainer.removeAllViews();
                        adContainer.setPadding(5, 5, 5, 5);
                        adContainer.addView(mAdView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                mAdView.setVisibility(View.GONE);
                adContainer.setVisibility(View.GONE);
                adContainer.removeAllViews();
            }
        } else {
            adContainer.setVisibility(View.GONE);
            adContainer.removeAllViews();
        }
    }
    //TODO BANNER ADS--------------------------------------------------------------------------------------------------------------


    //TODO COLLAPSIBLE ADS--------------------------------------------------------------------------------------------------------------
    public static void showCollapsibleAd(String collapsibleAdId, Activity activity, LinearLayout adContainer, AppInterfaces.CollapsibleAdInterface collapsibleAdInterface) {
        try {
            AdView adView = new AdView(activity);
            AdSize customAdSize2 = new AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT);
            adView.setAdSize(customAdSize2);
            adView.setAdUnitId(collapsibleAdId);
            Bundle extras = new Bundle();
            extras.putString("collapsible", "top");
            AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    if (!isListNull(Constants.platformList)) {
                        switch (collapsibleAdHitCounter) {
                            case 0:
                                collapsibleAdHitCounter++;
                                if (Constants.platformList.contains(Constants.ADMOB))
                                    showCollapsibleAd(Constants.adsResponseModel.getBanner_ads().getAdmob(), activity, adContainer, collapsibleAdInterface);
                                break;
                            case 1:
                                collapsibleAdHitCounter++;
                                if (Constants.platformList.contains(Constants.FACEBOOK))
                                    showCollapsibleAd(Constants.adsResponseModel.getBanner_ads().getFacebook(), activity, adContainer, collapsibleAdInterface);
                                break;
                            case 2:
                                collapsibleAdInterface.setAdState(false);
                                adContainer.setVisibility(View.GONE);
                                break;
                        }
                    } else {
                        collapsibleAdHitCounter = 0;
                        adContainer.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    adContainer.setGravity(Gravity.TOP);
                    collapsibleAdInterface.setAdState(true);
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    adContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    adContainer.setGravity(Gravity.BOTTOM);
                    collapsibleAdInterface.setAdState(false);
                }
            });

            adView.loadAd(adRequest);
            adContainer.addView(adView);

            adContainer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Global.sout("Collapsible ad exception >>>>>>>>>>>>>> ", e.getLocalizedMessage());
        }
    }
    //TODO COLLAPSIBLE ADS--------------------------------------------------------------------------------------------------------------

    //TODO BackPressAds --------------------------------------------------------------------------------------------------------------
    public static void showBackPressAds(Activity activity, String appopenAd, AppInterfaces.AppOpenADInterface appOpenADInterface) {
        appOpenHitCounter = 0;
        if (!Constants.isFixed) {
            ConnectionDetector cd = new ConnectionDetector(activity);
            if (Constants.BACKPRESS_COUNT == Constants.adsResponseModel.getBackPress_count()) {
                Constants.BACKPRESS_COUNT = 0;
                if (Constants.adsResponseModel != null && cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
                    if (StringUtils.CheckEqualIgnoreCase(Constants.adsResponseModel.getBackPress_ads_type(), Constants.BACKPRESS_AD_TYPE)) {
                        Global.showAlertProgressDialog(activity);
                        if (precacheAppOpenAd != null) {
                            showAdIfAvailable(precacheAppOpenAd, activity, appOpenADInterface);
                        } else {
                            AdRequest adRequest = new AdRequest.Builder().build();
                            AppOpenAd.load(activity, appopenAd, adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {

                                @Override
                                public void onAdLoaded(@NonNull AppOpenAd ad) {
                                    Global.hideAlertProgressDialog();
                                    showAdIfAvailable(ad, activity, appOpenADInterface);
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    Global.hideAlertProgressDialog();
                                    if (!isListNull(Constants.platformList)) {
                                        switch (appOpenHitCounter) {
                                            case 0:
                                                appOpenHitCounter++;
                                                if (Constants.platformList.contains(Constants.ADMOB))
                                                    showAppOpenAds(Constants.adsResponseModel.getApp_open_ads().getAdmob(), activity, appOpenADInterface);
                                                break;
                                            case 1:
                                                appOpenHitCounter++;
                                                if (Constants.platformList.contains(Constants.FACEBOOK))
                                                    showAppOpenAds(Constants.adsResponseModel.getApp_open_ads().getFacebook(), activity, appOpenADInterface);
                                                break;
                                            case 2:
                                                appOpenHitCounter = 0;
                                                Global.hideAlertProgressDialog();
                                                buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getAdx());
                                                appOpenADInterface.appOpenAdState(false);
                                                break;
                                        }
                                    } else {
                                        appOpenHitCounter = 0;
                                        Global.hideAlertProgressDialog();
                                        appOpenADInterface.appOpenAdState(false);
                                    }

                                    Global.printLog("adsLoading Failed", loadAdError.getMessage());
                                    // Handle the error.
                                }

                            });
                        }


                    } else {
                        Global.hideAlertProgressDialog();
                        showBackInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
                            appOpenADInterface.appOpenAdState(isLoaded);
                        });
                    }
                } else {
                    Global.hideAlertProgressDialog();
                    appOpenADInterface.appOpenAdState(false);
                }
            } else {
                Constants.BACKPRESS_COUNT++;
                appOpenADInterface.appOpenAdState(false);
            }
        } else {
            if (Constants.backPressAdPlatformList.isEmpty())
                Constants.backPressAdPlatformList.addAll(Constants.platformList);
            FixedSequenceAdUtils.showBackPressAd(activity, appOpenADInterface);
        }

    }

    public static void showBackInterstitialAd(String interstitialAd, Activity activity, AppInterfaces.InterstitialADInterface interstitialADInterface) {
        interstitialHitCounter = 0;
        cd = new ConnectionDetector(activity);
        Collections.shuffle(Constants.interstitialPlatformList);
        if (cd.isConnectingToInternet() && Constants.adsResponseModel.isShow_ads()) {
            Global.showAlertProgressDialog(activity);
            AdRequest adRequest = new AdRequest.Builder().build();
            if (Constants.platformList.get(0).equals(Constants.ADX) || Constants.platformList.get(0).equals(Constants.ADMOB)) {
                if (precacheInterstitialAd == null) {
                    InterstitialAd.load(activity, interstitialAd, adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            Global.hideAlertProgressDialog();
                            loadInterstitialAd(activity, interstitialADInterface, interstitialAd);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            if (!isListNull(Constants.platformList)) {
                                switch (interstitialHitCounter) {
                                    case 0:
                                        interstitialHitCounter++;
                                        if (Constants.platformList.contains(Constants.ADMOB))
                                            showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdmob(), activity, interstitialADInterface);
                                        break;
                                    case 1:
                                        interstitialHitCounter++;
                                        if (Constants.platformList.contains(Constants.FACEBOOK))
                                            showFacebookInterstitial(activity, Constants.adsResponseModel.getInterstitial_ads().getFacebook(), interstitialADInterface);
                                        break;
                                    case 2:
                                        interstitialHitCounter = 0;
                                        buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
                                        Global.hideAlertProgressDialog();
                                        interstitialADInterface.adLoadState(false);
                                        break;
                                }
                            } else {
                                interstitialHitCounter = 0;
                                Global.hideAlertProgressDialog();
                                interstitialADInterface.adLoadState(false);
                            }
                        }
                    });
                } else {
                    loadInterstitialAd(activity, interstitialADInterface, precacheInterstitialAd);
                }
            } else if (Constants.platformList.get(0).equals(Constants.FACEBOOK)) {
                showFacebookInterstitial(activity, Constants.adsResponseModel.getInterstitial_ads().getFacebook(), interstitialADInterface);
            }


        } else {
            Global.hideAlertProgressDialog();
            interstitialADInterface.adLoadState(false);
        }
    }
    //TODO BackPressAds --------------------------------------------------------------------------------------------------------------
}