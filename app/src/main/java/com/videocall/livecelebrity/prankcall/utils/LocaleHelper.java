package com.videocall.livecelebrity.prankcall.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LocaleHelper {
    public static final String SELECTED_LANGUAGE = "appLanguage";
    static Locale myLocale;
    static String currentLang;
    public static boolean langActivityCalled = false;
    public static boolean langCalledFromSplash = false;
    public static boolean langActivityBackPressed = false;
    public static boolean langActivityNextPressed = false;

    public static boolean langCalledFromHomeBottomNav = false;
    public static void setLocale(Context context, String localeName) {
        //persist(context, activity,  localeName);
        if(localeName == "pt-rBR"){
            myLocale = new Locale("pt" , "BR");
        }
        else if(localeName == "nl-rBE"){
            myLocale = new Locale("nl" , "BE");
        }
        else myLocale = new Locale(localeName);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
    private static void persist(Context context, AppCompatActivity activity , String language) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECTED_LANGUAGE, language);
//        editor.apply();

        SharedPreferences preferences1 = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    // the method is used update the language of application by creating
    // object of inbuilt Locale class and passing language argument to it
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }


    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}
