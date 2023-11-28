package com.videocall.livecelebrity.prankcall.splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.AdUtils
import com.adsmodule.api.adsModule.utils.Constants
import com.videocall.livecelebrity.prankcall.MainActivity
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses.AppOpenAds
import com.videocall.livecelebrity.prankcall.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment() {

    private lateinit var binding: FragmentPermissionBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionBinding.inflate(inflater, container, false)
        val permsArray = Array(1){""}
        permsArray[0] = android.Manifest.permission.CAMERA
        binding.btnGrantPerm.setOnClickListener {
            launcher.launch(permsArray)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            AdUtils.showBackPressAds(
                AppOpenAds.activity,
                Constants.adsResponseModel.app_open_ads.adx,
            ) { state_load: Boolean ->
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        val granted = it.all { it.value }
        if(granted){
//            AdUtils.showInterstitialAd(
//                Constants.adsResponseModel.interstitial_ads.adx,
//                AppOpenAds.activity
//            ) { state_load: Boolean ->
//                findNavController().navigate(R.id.action_permissionFragment_to_getStartedFragment)
//            }
            findNavController().navigate(R.id.action_permissionFragment_to_getStartedFragment)
        }
        else {
            Toast.makeText(requireContext(), "All permissions are required by the app", Toast.LENGTH_SHORT).show()
        }
    }
}