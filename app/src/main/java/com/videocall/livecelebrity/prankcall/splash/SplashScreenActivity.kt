package com.videocall.livecelebrity.prankcall.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.adsmodule.api.adsModule.retrofit.APICallHandler
import com.adsmodule.api.adsModule.retrofit.AdsDataRequestModel
import com.adsmodule.api.adsModule.retrofit.AdsResponseModel
import com.adsmodule.api.adsModule.utils.Constants
import com.adsmodule.api.adsModule.utils.Global
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.videocall.livecelebrity.prankcall.MainActivity
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses.MyApplication
import com.videocall.livecelebrity.prankcall.databinding.ActivitySplashScreenBinding
import com.videocall.livecelebrity.prankcall.utils.Celebrity
import com.videocall.livecelebrity.prankcall.utils.LocaleHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    companion object{
        val celebrityListBollywood = MutableLiveData<ArrayList<Celebrity>>()
        val celebrityListHollywood = MutableLiveData<ArrayList<Celebrity>>()
        var baseUrl = ""
        var chatGptAccessToken = ""
        val gson = Gson()
        val INDUSTRY_BOLLYWOOD = "Bollywood"
        val INDUSTRY_HOLLYWOOD = "Hollywood"
        var youTubeBaseUrl = ""

        fun setUpList(){
            val data: JsonObject = Constants.adsResponseModel.extra_data_field.data
            if(Constants.adsResponseModel.extra_data_field.base_url!=null){
                baseUrl = Constants.adsResponseModel.extra_data_field.base_url
            }
            if(Constants.adsResponseModel.extra_data_field.chatGptAccessToken!=null){
                chatGptAccessToken = Constants.adsResponseModel.extra_data_field.chatGptAccessToken
            }
            if(Constants.adsResponseModel.extra_data_field.youtubeBaseUrl!=null){
                youTubeBaseUrl = Constants.adsResponseModel.extra_data_field.youtubeBaseUrl
            }
            val celebrityListJson = Constants.adsResponseModel.extra_data_field.celebrities_list
            if(celebrityListJson!=null){
                val cbListBollywood = arrayListOf<Celebrity>()
                val cbListHollywood = arrayListOf<Celebrity>()
                for(i in 0 until celebrityListJson.size){
                    val key = celebrityListJson[i]
                    if(key!=null){
                        val celeb = data[key].asJsonObject
                        if(celeb!=null){
                            val celebrity = gson.fromJson(celeb, Celebrity::class.java)
                            if(celebrity!=null){
                                celebrity.profile_url = baseUrl + celebrity.profile_url;
                                if(celebrity.industry == INDUSTRY_BOLLYWOOD) cbListBollywood.add(celebrity)
                                else cbListHollywood.add(celebrity)
                            }
                        }
                    }
                }
                celebrityListBollywood.postValue(cbListBollywood)
                celebrityListHollywood.postValue(cbListHollywood)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences(LanguageActivity.LANG_PREF, Context.MODE_PRIVATE)
        val locale = pref.getString(LocaleHelper.SELECTED_LANGUAGE, "en").toString()
        LocaleHelper.setLocale(this,  locale)

        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        Glide.with(this).load(R.drawable.call_loading_gif).into(binding.loadingGif)

        // ads call and checking whether activity restarted for language change
        Global.showLoader = false
        val sharedPref = this.getSharedPreferences(MainActivity.ONBOARDING_SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val onBoardingShown = sharedPref.getBoolean(MainActivity.ONBOARDING_SHOWN, false)

        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                if (MyApplication.getConnectionStatus().isConnectingToInternet) {
                    APICallHandler.callAdsApi(
                        this@SplashScreenActivity, Constants.MAIN_BASE_URL, AdsDataRequestModel(packageName, "")
                    ) { adsResponseModel: AdsResponseModel? ->
                        if (adsResponseModel != null) {

                            setUpList()

//                                AdUtils.showAppOpenAds(
//                                    Constants.adsResponseModel.getApp_open_ads().getAdx(),
//                                    activity
//                                ) { state_load: Boolean ->
//                                }

                            if(onBoardingShown){
                                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                                finish()
                            }
                            else {
                                startActivity(Intent(this@SplashScreenActivity, LanguageActivity::class.java))
                                finish()
                            }
                        }
                        else {
                            if(onBoardingShown){
                                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                                finish()
                            }
                            else {
                                startActivity(Intent(this@SplashScreenActivity, LanguageActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
                else {
                    if(onBoardingShown){
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                        finish()
                    }
                    else {
                        startActivity(Intent(this@SplashScreenActivity, LanguageActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}