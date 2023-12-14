package com.videocall.livecelebrity.prankcall.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.utils.AdUtils
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses1.LifeCycleOwner
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
            AdUtils.showBackPressAd(
                LifeCycleOwner.activity,
                
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
//                
//                LifeCycleOwner.activity
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