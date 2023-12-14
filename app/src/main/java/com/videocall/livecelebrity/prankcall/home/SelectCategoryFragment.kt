package com.videocall.livecelebrity.prankcall.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.utils.AdUtils
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses1.LifeCycleOwner
import com.videocall.livecelebrity.prankcall.databinding.FragmentSelectCategoryBinding

class SelectCategoryFragment : Fragment() {

    private lateinit var binding: FragmentSelectCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectCategoryBinding.inflate(inflater, container, false)

        binding.cardHollywood.setOnClickListener {
            AdUtils.showInterstitialAd(
                
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                PartnerChooseFragment.CELEBRITY_TYPE = PartnerChooseFragment.CELEB_HOLLYWOOD
                findNavController().navigate(R.id.action_selectCategoryFragment_to_partnerChooseFragment)
            }
        }

        binding.cardBollywood.setOnClickListener {
            AdUtils.showInterstitialAd(
                
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                PartnerChooseFragment.CELEBRITY_TYPE = PartnerChooseFragment.CELEB_BOLLYWOOD
                findNavController().navigate(R.id.action_selectCategoryFragment_to_partnerChooseFragment)
            }
        }

        binding.ivMenu.setOnClickListener {
            AdUtils.showBackPressAd(
                LifeCycleOwner.activity,
                
            ) { state_load: Boolean ->
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            AdUtils.showBackPressAd(
                LifeCycleOwner.activity,
                
            ) { state_load: Boolean ->
                findNavController().navigateUp()
            }
        }

        return binding.root
    }
}