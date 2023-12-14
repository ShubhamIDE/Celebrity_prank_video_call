package com.videocall.livecelebrity.prankcall.splash

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.utils.AdUtils
import com.videocall.livecelebrity.prankcall.MainActivity
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses1.LifeCycleOwner
import com.videocall.livecelebrity.prankcall.databinding.FragmentTermsOfUseBinding

class TermsOfUseFragment : Fragment() {

    private lateinit var binding: FragmentTermsOfUseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermsOfUseBinding.inflate(inflater, container, false)

        val sharedPref = requireContext().getSharedPreferences(MainActivity.ONBOARDING_SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val onBoardingShown = sharedPref.getBoolean(MainActivity.ONBOARDING_SHOWN, false)
        if(onBoardingShown){
            findNavController().navigate(R.id.action_termsOfUseFragment_to_getStartedFragment)
        }

        binding.btnAgree.setOnClickListener {
            AdUtils.showInterstitialAd(
                
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                findNavController().navigate(R.id.action_termsOfUseFragment_to_onboardingFragment)
            }
        }

        binding.btnPrivacy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://pixarllwallstudios.blogspot.com/p/privacy-policy.html")
            startActivity(intent)
        }

        binding.btnTmc.setOnClickListener {
            AdUtils.showInterstitialAd(
                
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                findNavController().navigate(R.id.action_termsOfUseFragment_to_tcsFragment)
            }
        }

        binding.btnBackArrow.setOnClickListener {
            requireActivity().finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }

        return binding.root
    }
}