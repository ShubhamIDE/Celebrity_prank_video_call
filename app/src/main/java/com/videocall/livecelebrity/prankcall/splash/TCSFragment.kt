package com.videocall.livecelebrity.prankcall.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.utils.AdUtils
import com.videocall.livecelebrity.prankcall.SingletonClasses1.LifeCycleOwner
import com.videocall.livecelebrity.prankcall.databinding.FragmentTcsBinding

class TCSFragment : Fragment() {

    private lateinit var binding: FragmentTcsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTcsBinding.inflate(inflater, container, false)
        binding.btnBackArrow.setOnClickListener {
            AdUtils.showInterstitialAd(
                
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            AdUtils.showInterstitialAd(
                
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}