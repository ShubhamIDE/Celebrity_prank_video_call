package com.videocall.livecelebrity.prankcall.SingletonClasses;

import static com.adsmodule.api.adsModule.retrofit.APICallHandler.callAppCountApi;

import android.app.Application;
import android.util.Log;

import com.adsmodule.api.adsModule.preferencesManager.AppPreferences;
import com.adsmodule.api.adsModule.retrofit.AdsDataRequestModel;
import com.adsmodule.api.adsModule.utils.ConnectionDetector;
import com.adsmodule.api.adsModule.utils.Constants;
import com.adsmodule.api.adsModule.utils.Global;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryPerformance;


public class MyApplication extends Application {

    static AppPreferences preferences;
    private static MyApplication app;
    private static ConnectionDetector cd;

    public static AppPreferences getPreferences() {
        if (preferences == null)
            preferences = new AppPreferences(app);
        return preferences;
    }

    public static ConnectionDetector getConnectionStatus() {
        if (cd == null) {
            cd = new ConnectionDetector(app);
        }
        return cd;
    }


    public static synchronized MyApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        AppPreferences preferences = new AppPreferences(app);
        if (preferences.isFirstRun()) {
            callAppCountApi(Constants.MAIN_BASE_URL, new AdsDataRequestModel(app.getPackageName(), Global.getDeviceId(app)), () -> {
                preferences.setFirstRun(false);
            });
        }

        new AppOpenAds(app);


        new FlurryAgent.Builder()
                .withDataSaleOptOut(false)
                .withCaptureUncaughtExceptions(true)
                .withIncludeBackgroundSessionsInMetrics(true)
                .withLogLevel(Log.DEBUG)
                .withPerformanceMetrics(FlurryPerformance.ALL)
                .build(this, "86WWNQK6HY28VNRN7X3Z");

    }
}
