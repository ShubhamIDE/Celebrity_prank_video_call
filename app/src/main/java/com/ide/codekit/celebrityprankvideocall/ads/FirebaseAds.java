package com.ide.codekit.celebrityprankvideocall.ads;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ide.codekit.celebrityprankvideocall.R;

import java.util.Objects;

public class FirebaseAds {
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseRemoteConfigSettings configSettings;
    public static boolean isUpdated = false;
    private static Dialog waitDialog;

    // Call this inside Singleton file
    public FirebaseAds(Application app){

        MobileAds.initialize(app);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        configSettings = new FirebaseRemoteConfigSettings.Builder()
                // Todo: Enable This if you want to set interval time
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        // This file will contain all the default parameters
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

    }

    // For Fetching Data From Firebase
    public void configureFetch(onFetchRemoteConfig onFetchRemoteConfig){
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if(task.isSuccessful()){
                            isUpdated = task.getResult();
                        }
                        onFetchRemoteConfig.onFetch(isUpdated);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onFetchRemoteConfig.onFetch(false);
                    }
                });
    }



    // Interstitial Ads
    public static void showInterstitialAds(Activity activity, onAdsResponse onAdsResponse){

        showWaitDialog(activity);
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        if(firebaseRemoteConfig.getBoolean("show_ads")){
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(activity, firebaseRemoteConfig.getString("interstitial_ad"), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    hideWaitDialog();
                    onAdsResponse.onAds(false);
                }

                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            onAdsResponse.onAds(true);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            onAdsResponse.onAds(false);

                        }
                    });
                    hideWaitDialog();
                    interstitialAd.show(activity);
                }
            });
        }else{
            hideWaitDialog();
            onAdsResponse.onAds(false);
        }
    }


    // AppOpen Ads
    public static void showAppOpenAd(Activity activity, onAdsResponse onAdsResponse){
        showWaitDialog(activity);
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        if(firebaseRemoteConfig.getBoolean("show_ads")){
            AdRequest adRequest = new AdRequest.Builder().build();
            AppOpenAd.load(activity, firebaseRemoteConfig.getString("app_open_ad"), adRequest, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    hideWaitDialog();
                    onAdsResponse.onAds(false);

                }

                @Override
                public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                    super.onAdLoaded(appOpenAd);
                    appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            onAdsResponse.onAds(true);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            onAdsResponse.onAds(false);
                        }
                    });

                    appOpenAd.show(activity);
                    hideWaitDialog();
                }
            });
        }else{
            onAdsResponse.onAds(false);
        }

    }


    // Reward Ads
    public static void showRewardAd(Activity activity , onAdsResponse onAdsResponse){
        showWaitDialog(activity);
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        if(firebaseRemoteConfig.getBoolean("show_ads")){
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(activity, firebaseRemoteConfig.getString("rewarded_ad"), adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    hideWaitDialog();
                    onAdsResponse.onAds(false);
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    super.onAdLoaded(rewardedAd);
                    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            onAdsResponse.onAds(true);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            onAdsResponse.onAds(false);
                        }
                    });

                    hideWaitDialog();
                    rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            onAdsResponse.onAds(true);
                        }
                    });

                }
            });
        }else{
            hideWaitDialog();
            onAdsResponse.onAds(false);
        }
    }


    // Banner Ads
    public static void showBannerAd(Activity activity, CardView container){
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        if(firebaseRemoteConfig.getBoolean("show_ads")){
//            AdRequest adRequest = new AdRequest.Builder().build();
//            AdView bannerView = new AdView(activity);
//            bannerView.setAdSize(AdSize.BANNER);
//            bannerView.setAdUnitId(firebaseRemoteConfig.getString("banner_ad"));
//            AdRequest.Builder adRequestBuilder = new AdRequest.Builder(); // AdRequest Builder
//            container.addView(bannerView); // AdView is being added to the MaterialCardView which acts as container
//            bannerView.loadAd(adRequestBuilder.build());
//        }
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView bannerView = new AdView(activity);
        bannerView.setAdSize(AdSize.BANNER);
        bannerView.setAdUnitId(firebaseRemoteConfig.getString("banner_ad"));
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder(); // AdRequest Builder
        container.addView(bannerView); // AdView is being added to the MaterialCardView which acts as container
        bannerView.loadAd(adRequestBuilder.build());
    }


    // Native Ad
    public static void showNativeAd(Activity activity, CardView container){
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        if(firebaseRemoteConfig.getBoolean("show_ads")){
            AdLoader adLoader = new AdLoader.Builder(activity, firebaseRemoteConfig.getString("native_ad"))
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                            loadNativeAd(nativeAd, activity, container);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                            Log.e("addfirebase", "NativeAds: Ads Load Failed");
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    private static void loadNativeAd(NativeAd nativeAd, Activity activity, CardView container) {
        NativeAdView unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.full_native_ad, (ViewGroup) null);
        populateNativeAd(nativeAd, unifiedNativeAdView);
        container.removeAllViews();
        container.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        container.addView(unifiedNativeAdView);
        container.setVisibility(View.VISIBLE);
    }

    public static void populateNativeAd(NativeAd nativeAd, NativeAdView nativeAdView) {
        // Populating/Displaying the Native Ads into the Container/CardView (from the XML Layout)
            nativeAdView.setMediaView(nativeAdView.findViewById(R.id.ad_media));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_app_icon));
        nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_stars));
        nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        ((TextView) Objects.requireNonNull(nativeAdView.getHeadlineView())).setText(nativeAd.getHeadline());
        ((TextView) nativeAdView.getHeadlineView()).setTextColor(Color.BLACK);
        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(nativeAdView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            //TODO sets color to headline button -->
            Objects.requireNonNull(nativeAdView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getBodyView()).setText(nativeAd.getBody());
            ((TextView) nativeAdView.getBodyView()).setTextColor(Color.BLACK);
        }

        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(nativeAdView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
            //TODO sets color to install button -->
            nativeAdView.getCallToActionView().setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#288fba")));
            ((Button) nativeAdView.getCallToActionView()).setTextColor(Color.WHITE);
        }

        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(nativeAdView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(nativeAdView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(nativeAdView.getPriceView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getPriceView()).setVisibility(View.GONE);
            ((TextView) nativeAdView.getPriceView()).setText(nativeAd.getPrice());
            ((TextView) nativeAdView.getPriceView()).setTextColor(Color.BLACK);

        }
        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(nativeAdView.getStoreView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(nativeAdView.getStoreView()).setVisibility(View.GONE);
            ((TextView) nativeAdView.getStoreView()).setText(nativeAd.getStore());
            ((TextView) nativeAdView.getStoreView()).setTextColor(Color.BLACK);
        }

        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(nativeAdView.getStarRatingView()).setVisibility(View.VISIBLE);
        } else {
            ((RatingBar) Objects.requireNonNull(nativeAdView.getStarRatingView())).setRating(nativeAd.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
            ((RatingBar) nativeAdView.getStarRatingView()).setProgressBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            ((RatingBar) nativeAdView.getStarRatingView()).setProgressTintList(ColorStateList.valueOf(Color.BLACK));
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(nativeAdView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(nativeAdView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getAdvertiserView()).setTextColor(Color.BLACK);
        }

        nativeAdView.setNativeAd(nativeAd);

    }


    // Dialog
    private static void showWaitDialog(Activity activity) {
        if(!activity.getComponentName().getClassName().equals("com.ide.codekit.celebrityprankvideocall.splash.SplashScreenActivity")){
            waitDialog = new Dialog(activity);
            LottieAnimationView lottieView = new LottieAnimationView(activity);
            lottieView.setAnimation(R.raw.lottie_loading);
            lottieView.playAnimation();
            lottieView.loop(true);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpToPx(activity, 100), dpToPx(activity, 100));
            waitDialog.setContentView(lottieView, params);
            waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            waitDialog.setCancelable(false);
            waitDialog.show();
        }
    }

    private static void hideWaitDialog(){
        if(waitDialog != null && waitDialog.isShowing()){
            waitDialog.dismiss();
        }
    }

    // Interfaces
    public interface onFetchRemoteConfig{
        void onFetch(boolean isUpdated);
    }

    public interface onAdsResponse{
        void onAds(boolean isAds);
    }


    // Utils
    public static int dpToPx(Activity app, int dp) {
        DisplayMetrics displayMetrics = app.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }



}

