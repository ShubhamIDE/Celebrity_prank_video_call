package com.adsmodule.api.adsModule.retrofit;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AdsResponseModel {

    private String url;
    private AppOpenAdsDTO app_open_ads = new AppOpenAdsDTO();
    private BannerAdsDTO banner_ads = new BannerAdsDTO();
    private NativeAdsDTO native_ads = new NativeAdsDTO();
    private InterstitialAdsDTO interstitial_ads = new InterstitialAdsDTO();
    private RewardedAdsDTO rewarded_ads = new RewardedAdsDTO();
    private MobileStickyAdsDTO mobile_sticky_ads = new MobileStickyAdsDTO();
    private CollapsibleAdsDTO collapsible_ads = new CollapsibleAdsDTO();
    private CustomAdsJsonDTO custom_ads_json = new CustomAdsJsonDTO();
    private ExtraDataFieldDTO extra_data_field = new ExtraDataFieldDTO();
    private String app_name = "";
    private String package_name;
    private boolean show_ads;
    private String ads_open_type;
    private int ads_count;
    private String version_name;
    private String button_bg;
    private String button_text_color;
    private String common_text_color;
    private String ads_bg;
    private String backpress_ads_type;
    private int backpress_count;

    public String getAds_sequence_type() {
        return ads_sequence_type;
    }

    public void setAds_sequence_type(String ads_sequence_type) {
        this.ads_sequence_type = ads_sequence_type;
    }

    private String ads_sequence_type;

    public String getBackPress_ads_type() {
        return backpress_ads_type;
    }

    public void setBackPress_ads_type(String backPress_ads_type) {
        this.backpress_ads_type = backPress_ads_type;
    }

    public int getBackPress_count() {
        return backpress_count;
    }

    public void setBackPress_count(int backPress_count) {
        this.backpress_count = backPress_count;
    }

    public String getButton_bg() {
        return button_bg;
    }

    public void setButton_bg(String button_bg) {
        this.button_bg = button_bg;
    }

    public String getCommon_text_color() {
        return common_text_color;
    }

    public void setCommon_text_color(String common_text_color) {
        this.common_text_color = common_text_color;
    }

    public String getButton_text_color() {
        return button_text_color;
    }

    public void setButton_text_color(String button_text_color) {
        this.button_text_color = button_text_color;
    }

    public String getAd_bg() {
        return ads_bg;
    }

    public void setAd_bg(String ad_bg) {
        this.ads_bg = ad_bg;
    }


    public String getMonetize_platform() {
        return monetize_platform;
    }

    public void setMonetize_platform(String monetize_platform) {
        this.monetize_platform = monetize_platform;
    }
    @SerializedName(value = "monetize_platform", alternate = {"testads_platform"})
    String monetize_platform;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AppOpenAdsDTO getApp_open_ads() {
        return app_open_ads;
    }

    public void setApp_open_ads(AppOpenAdsDTO app_open_ads) {
        this.app_open_ads = app_open_ads;
    }

    public BannerAdsDTO getBanner_ads() {
        return banner_ads;
    }

    public void setBanner_ads(BannerAdsDTO banner_ads) {
        this.banner_ads = banner_ads;
    }

    public NativeAdsDTO getNative_ads() {
        return native_ads;
    }

    public void setNative_ads(NativeAdsDTO native_ads) {
        this.native_ads = native_ads;
    }

    public InterstitialAdsDTO getInterstitial_ads() {
        return interstitial_ads;
    }

    public void setInterstitial_ads(InterstitialAdsDTO interstitial_ads) {
        this.interstitial_ads = interstitial_ads;
    }

    public RewardedAdsDTO getRewarded_ads() {
        return rewarded_ads;
    }

    public void setRewarded_ads(RewardedAdsDTO rewarded_ads) {
        this.rewarded_ads = rewarded_ads;
    }

    public MobileStickyAdsDTO getMobile_sticky_ads() {
        return mobile_sticky_ads;
    }

    public void setMobile_sticky_ads(MobileStickyAdsDTO mobile_sticky_ads) {
        this.mobile_sticky_ads = mobile_sticky_ads;
    }

    public CollapsibleAdsDTO getCollapsible_ads() {
        return collapsible_ads;
    }

    public void setCollapsible_ads(CollapsibleAdsDTO collapsible_ads) {
        this.collapsible_ads = collapsible_ads;
    }

    public CustomAdsJsonDTO getCustom_ads_json() {
        return custom_ads_json;
    }

    public void setCustom_ads_json(CustomAdsJsonDTO custom_ads_json) {
        this.custom_ads_json = custom_ads_json;
    }

    public ExtraDataFieldDTO getExtra_data_field() {
        return extra_data_field;
    }

    public void setExtra_data_field(ExtraDataFieldDTO extra_data_field) {
        this.extra_data_field = extra_data_field;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public boolean isShow_ads() {
        return show_ads;
    }

    public void setShow_ads(boolean show_ads) {
        this.show_ads = show_ads;
    }

    public String getAds_open_type() {
        return ads_open_type;
    }

    public void setAds_open_type(String ads_open_type) {
        this.ads_open_type = ads_open_type;
    }

    public int getAds_count() {
        return ads_count;
    }

    public void setAds_count(int ads_count) {
        this.ads_count = ads_count;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public static class AppOpenAdsDTO {
        private String Admob = "";
        private String Facebook = "";
        private String Adx = "";

        public String getAdmob() {
            return Admob;
        }

        public void setAdmob(String admob) {
            this.Admob = admob;
        }

        public String getFacebook() {
            return Facebook;
        }

        public void setFacebook(String Facebook) {
            this.Facebook = Facebook;
        }

        public String getAdx() {
            return Adx;
        }

        public void setAdx(String adx) {
            this.Adx = adx;
        }
    }

    public static class BannerAdsDTO {
        private String Admob = "";
        private String Facebook = "";
        private String Adx = "";

        public String getAdmob() {
            return Admob;
        }

        public void setAdmob(String admob) {
            this.Admob = admob;
        }

        public String getFacebook() {
            return Facebook;
        }

        public void setFacebook(String Facebook) {
            this.Facebook = Facebook;
        }

        public String getAdx() {
            return Adx;
        }

        public void setAdx(String adx) {
            this.Adx = adx;
        }
    }

    public static class NativeAdsDTO {
        private String Admob = "";
        private String Facebook = "";
        private String Adx = "";

        public String getAdmob() {
            return Admob;
        }

        public void setAdmob(String admob) {
            this.Admob = admob;
        }

        public String getFacebook() {
            return Facebook;
        }

        public void setFacebook(String Facebook) {
            this.Facebook = Facebook;
        }

        public String getAdx() {
            return Adx;
        }

        public void setAdx(String adx) {
            this.Adx = adx;
        }
    }

    public static class InterstitialAdsDTO {
        private String Admob = "";
        private String Facebook = "";
        private String Adx = "";

        public String getAdmob() {
            return Admob;
        }

        public void setAdmob(String admob) {
            this.Admob = admob;
        }

        public String getFacebook() {
            return Facebook;
        }

        public void setFacebook(String Facebook) {
            this.Facebook = Facebook;
        }

        public String getAdx() {
            return Adx;
        }

        public void setAdx(String adx) {
            this.Adx = adx;
        }
    }

    public static class RewardedAdsDTO {
        private String Admob = "";
        private String Facebook = "";
        private String Adx = "";

        public String getAdmob() {
            return Admob;
        }

        public void setAdmob(String admob) {
            this.Admob = admob;
        }

        public String getFacebook() {
            return Facebook;
        }

        public void setFacebook(String Facebook) {
            this.Facebook = Facebook;
        }

        public String getAdx() {
            return Adx;
        }

        public void setAdx(String adx) {
            this.Adx = adx;
        }
    }

    public static class CollapsibleAdsDTO {
        private String Admob = "";
        private String Facebook = "";
        private String Adx = "";

        public String getAdmob() {
            return Admob;
        }

        public void setAdmob(String admob) {
            this.Admob = admob;
        }

        public String getFacebook() {
            return Facebook;
        }

        public void setFacebook(String Facebook) {
            this.Facebook = Facebook;
        }

        public String getAdx() {
            return Adx;
        }

        public void setAdx(String adx) {
            this.Adx = adx;
        }
    }

    public static class CustomAdsJsonDTO {
    }

    public static class ExtraDataFieldDTO {
        // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
        public class Category{
            public String getCategory() {
                return this.category; }
            public void setCategory(String category) {
                this.category = category; }
            String category;
            public String getImage() {
                return this.image; }
            public void setImage(String image) {
                this.image = image; }
            String image;
            public ArrayList<String> getIds() {
                return this.ids; }
            public void setIds(ArrayList<String> ids) {
                this.ids = ids; }
            ArrayList<String> ids;
        }

        public class Color{
            public String getColor_name() {
                return this.color_name; }
            public void setColor_name(String color_name) {
                this.color_name = color_name; }
            String color_name;
            public String getColor() {
                return this.color; }
            public void setColor(String color) {
                this.color = color; }
            String color;
            public ArrayList<String> getIds() {
                return this.ids; }
            public void setIds(ArrayList<String> ids) {
                this.ids = ids; }
            ArrayList<String> ids;
        }

        public class Data{
        }

            public String getBase_url() {
                return this.base_url; }
            public void setBase_url(String base_url) {
                this.base_url = base_url; }

        public String getMyBaseUrl() {
            return this.dualWpUrl; }
        public void setMyBaseUrl(String base_url) {
            this.dualWpUrl = base_url; }
            String base_url;
        String dualWpUrl;
            public ArrayList<String> getTrending() {
                return this.trending; }
            public void setTrending(ArrayList<String> trending) {
                this.trending = trending; }
            ArrayList<String> trending;

        ArrayList<String> trending_dual;

        public ArrayList<String> getTrending_dual() {
            return this.trending_dual;
        }
        public void setTrending_dual(ArrayList<String> trending_dual) {
            this.trending_dual = trending_dual; }



            ArrayList<String> dual_wp_lock;
            ArrayList<String> dual_wp_home;

            public ArrayList<String> getDual_wp_lock() {
                return this.dual_wp_lock;
            }
            public void setDual_wp_lock(ArrayList<String> dual_wp_lock) {
                this.dual_wp_lock = dual_wp_lock; }

            public ArrayList<String> getDual_wp_home() {
                return this.dual_wp_home;
            }
            public void setDual_wp_home(ArrayList<String> dual_wp_home) {
                this.dual_wp_home = dual_wp_home; }

            public ArrayList<String> getRecently_uploaded() {
                return this.recently_uploaded; }
            public void setRecently_uploaded(ArrayList<String> recently_uploaded) {
                this.recently_uploaded = recently_uploaded; }
            ArrayList<String> recently_uploaded;
            public ArrayList<String> getTrending_page() {
                return this.trending_page; }
            public void setTrending_page(ArrayList<String> trending_page) {
                this.trending_page = trending_page; }
            ArrayList<String> trending_page;
            public ArrayList<Category> getCategories() {
                return this.categories; }
            public void setCategories(ArrayList<Category> categories) {
                this.categories = categories; }
            ArrayList<Category> categories;
            public ArrayList<Color> getColors() {
                return this.colors; }
            public void setColors(ArrayList<Color> colors) {
                this.colors = colors; }
            ArrayList<Color> colors;
            JsonObject data;

            public JsonObject getData() {
                return data;
            }

            public void setData(JsonObject data) {
                this.data = data;
            }



    }

    private class MobileStickyAdsDTO {
        private String Admob = "";
        private String Facebook = "";
        private String Adx = "";

        public String getAdmob() {
            return Admob;
        }

        public void setAdmob(String admob) {
            this.Admob = admob;
        }

        public String getFacebook() {
            return Facebook;
        }

        public void setFacebook(String Facebook) {
            this.Facebook = Facebook;
        }

        public String getAdx() {
            return Adx;
        }

        public void setAdx(String adx) {
            this.Adx = adx;
        }
    }
}
