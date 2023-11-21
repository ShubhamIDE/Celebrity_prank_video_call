package com.adsmodule.api.adsModule.retrofit;


import static com.adsmodule.api.adsModule.utils.Global.checkAppVersion;
import static com.adsmodule.api.adsModule.utils.Global.isNull;

import android.app.Activity;

import androidx.annotation.NonNull;

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
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    counterInterface.getEmptyInterface();
                    Global.sout("Counts api response >>>>>>>>>>> ", response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
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
            public void onResponse(@NonNull Call<AdsResponseModel> call, @NonNull Response<AdsResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Constants.adsResponseModel = response.body();
                    Constants.hitCounter = Constants.adsResponseModel.getAds_count();
                    Constants.BACKPRESS_COUNT = Constants.adsResponseModel.getBackPress_count();
                    if (!isNull(Constants.adsResponseModel.getMonetize_platform())) {
                        Constants.platformList.addAll(Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(",")));
                        Constants.isFixed = StringUtils.CheckEqualIgnoreCase(Constants.adsResponseModel.getAds_sequence_type(), Constants.FIXED);

                        if (Constants.platformList.isEmpty()) Constants.platformList.add("Adx");

                        if (Constants.isFixed) {
                            Constants.fixedPlatformList.addAll(Constants.platformList);
                            Constants.nativePlatformList.addAll(Constants.platformList);
                            Constants.interstitialPlatformList.addAll(Constants.platformList);
                            Constants.appOpenPlatformList.addAll(Constants.platformList);
                            Constants.backPressAdPlatformList.addAll(Constants.platformList);
                        }
                    }
                    if (checkAppVersion(Constants.adsResponseModel.getVersion_name(), activity)) {
                        Global.showUpdateAppDialog(activity, isUpdated -> {
                            adDataInterface.getAdData(response.body());
                        });
                    } else {
                        AdUtils.buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getAdx());
                        AdUtils.buildNativeCache(Constants.adsResponseModel.getNative_ads().getAdx(), activity);
                        AdUtils.buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
                        AdUtils.buildRewardAdCache(Constants.adsResponseModel.getRewarded_ads().getAdx(), activity);
                        adDataInterface.getAdData(response.body());
                    }


                } else {
                    adDataInterface.getAdData(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AdsResponseModel> call, @NonNull Throwable t) {
                adDataInterface.getAdData(null);
            }
        });
    }
}
