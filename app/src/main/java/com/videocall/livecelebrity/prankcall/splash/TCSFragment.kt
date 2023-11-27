package com.videocall.livecelebrity.prankcall.splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.AdUtils
import com.adsmodule.api.adsModule.utils.Constants
import com.videocall.livecelebrity.prankcall.MainActivity
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses.AppOpenAds
import com.videocall.livecelebrity.prankcall.databinding.FragmentTcsBinding
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment

class TCSFragment : Fragment() {

    private lateinit var binding: FragmentTcsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTcsBinding.inflate(inflater, container, false)
        binding.btnBackArrow.setOnClickListener {
            AdUtils.showInterstitialAd(
                Constants.adsResponseModel.interstitial_ads.adx,
                AppOpenAds.activity
            ) { state_load: Boolean ->
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            AdUtils.showInterstitialAd(
                Constants.adsResponseModel.interstitial_ads.adx,
                AppOpenAds.activity
            ) { state_load: Boolean ->
                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}