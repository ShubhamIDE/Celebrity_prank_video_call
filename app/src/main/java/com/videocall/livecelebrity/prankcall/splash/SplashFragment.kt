package com.videocall.livecelebrity.prankcall.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.AdUtils
import com.adsmodule.api.adsModule.retrofit.APICallHandler
import com.adsmodule.api.adsModule.retrofit.AdsDataRequestModel
import com.adsmodule.api.adsModule.retrofit.AdsResponseModel
import com.adsmodule.api.adsModule.utils.Constants
import com.adsmodule.api.adsModule.utils.Global
import com.bumptech.glide.Glide
import com.videocall.livecelebrity.prankcall.MainActivity
import com.videocall.livecelebrity.prankcall.MainActivity.Companion.ONBOARDING_SHARED_PREF_KEY
import com.videocall.livecelebrity.prankcall.MainActivity.Companion.ONBOARDING_SHOWN
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses.MyApplication
import com.videocall.livecelebrity.prankcall.databinding.FragmentSplashBinding
import com.videocall.livecelebrity.prankcall.utils.LocaleHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root

        Glide.with(this).load(R.drawable.call_loading_gif).into(binding.loadingGif)

        if(MainActivity.LANG_CHANGED){
            MainActivity.LANG_CHANGED = false;
            findNavController().navigate(R.id.action_splashFragment_to_termsOfUseFragment)
        }
        else {
            // ads call and checking whether activity restarted for language change
            Global.showLoader = false
            val sharedPref = requireContext().getSharedPreferences(ONBOARDING_SHARED_PREF_KEY, Context.MODE_PRIVATE)
            val onBoardingShown = sharedPref.getBoolean(ONBOARDING_SHOWN, false)

            lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    if (MyApplication.getConnectionStatus().isConnectingToInternet) {
                        APICallHandler.callAdsApi(
                            activity, Constants.MAIN_BASE_URL, AdsDataRequestModel(requireActivity().packageName, "")
                        ) { adsResponseModel: AdsResponseModel? ->
                            if (adsResponseModel != null) {
//                            AdUtils.showAppOpenAds(
//                                Constants.adsResponseModel.getApp_open_ads().getAdx(),
//                                activity
//                            ) { state_load: Boolean ->
//                            }
                                if(onBoardingShown){
                                    lifecycleScope.launch {
                                        withContext(Dispatchers.Main){
                                            findNavController().navigate(R.id.action_splashFragment_to_getStartedFragment)
                                        }
                                    }
                                }
                                else {
                                    LocaleHelper.langCalledFromSplash = true
                                    lifecycleScope.launch {
                                        withContext(Dispatchers.Main){
                                            startActivity(Intent(requireContext(), LanguageActivity::class.java))
                                            requireActivity().finish()
                                        }
                                    }
                                }
                            }
                            else {
                                if(onBoardingShown){
                                    lifecycleScope.launch {
                                        withContext(Dispatchers.Main){
                                            findNavController().navigate(R.id.action_splashFragment_to_getStartedFragment)
                                        }
                                    }
                                }
                                else {
                                    lifecycleScope.launch {
                                        withContext(Dispatchers.Main){
                                            startActivity(Intent(requireContext(), LanguageActivity::class.java))
                                            requireActivity().finish()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if(onBoardingShown){
                            lifecycleScope.launch {
                                withContext(Dispatchers.Main){
                                    findNavController().navigate(R.id.action_splashFragment_to_getStartedFragment)
                                }
                            }
                        }
                        else {
                            LocaleHelper.langCalledFromSplash = true
                            lifecycleScope.launch {
                                withContext(Dispatchers.Main){
                                    startActivity(Intent(requireContext(), LanguageActivity::class.java))
                                    requireActivity().finish()
                                }
                            }
                        }
                    }
                }
            }
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        Global.showLoader = true
    }
}