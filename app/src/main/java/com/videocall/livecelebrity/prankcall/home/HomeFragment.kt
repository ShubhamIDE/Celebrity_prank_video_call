package com.videocall.livecelebrity.prankcall.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.utils.AdUtils
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses1.LifeCycleOwner
import com.videocall.livecelebrity.prankcall.audio.AudioFragment
import com.videocall.livecelebrity.prankcall.databinding.FragmentHomeBinding
import com.videocall.livecelebrity.prankcall.message.ChatsFragment
import com.videocall.livecelebrity.prankcall.video.VideoFragment

class HomeFragment : Fragment() {

    companion object{
        lateinit var binding: FragmentHomeBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.toolbarCard,
            R.string.drawer_open, R.string.drawer_close)
        toggle.isDrawerIndicatorEnabled = false
//        toggle.setToolbarNavigationClickListener {
//            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
//                binding.drawerLayout.closeDrawer(GravityCompat.START);
//            } else {
//                binding.drawerLayout.openDrawer(GravityCompat.START);
//            }
//        }

        binding.ivMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        }

        binding.drawerLayout.setDrawerListener(toggle)
        toggle.syncState()
        binding.navView.bringToFront()

        binding.clVideoCall.setOnClickListener {
            it.isClickable = false
            VideoFragment.fromHome = true
            PartnerChooseFragment.selectedType = PartnerChooseFragment.TYPE_VIDEO
            AdUtils.showInterstitialAd(
                
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                it.isClickable = true
                findNavController().navigate(R.id.action_homeFragment_to_selectCategoryFragment)
            }
        }

        binding.clCallHistory.setOnClickListener {
            it.isClickable = false
            AdUtils.showInterstitialAd(
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                it.isClickable = true
                findNavController().navigate(R.id.action_homeFragment_to_callLogFragment)
            }
        }

        binding.clAudio.setOnClickListener {
            it.isClickable = false
            AudioFragment.fromHome = true
            PartnerChooseFragment.selectedType = PartnerChooseFragment.TYPE_AUDIO
            AdUtils.showInterstitialAd(
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                it.isClickable = true
                findNavController().navigate(R.id.action_homeFragment_to_selectCategoryFragment)
            }
        }

        binding.clMsg.setOnClickListener {
            it.isClickable = false
            ChatsFragment.fromHome = true
            PartnerChooseFragment.selectedType = PartnerChooseFragment.TYPE_MESSAGE
            AdUtils.showInterstitialAd(
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                it.isClickable = true
                findNavController().navigate(R.id.action_homeFragment_to_selectCategoryFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            if(binding.drawerLayout.isDrawerVisible(GravityCompat.START)){
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            else {
                AdUtils.showBackPressAd(
                    LifeCycleOwner.activity,
                    
                ) { state_load: Boolean ->
                    findNavController().navigateUp()
                }
            }
        }

        return binding.root
    }
}