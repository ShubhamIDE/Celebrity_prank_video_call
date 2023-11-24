package com.videocall.livecelebrity.prankcall.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.videocall.livecelebrity.prankcall.MainActivity
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentGetStartedBinding
import eightbitlab.com.blurview.RenderScriptBlur

class GetStartedFragment : Fragment() {

    private lateinit var binding: FragmentGetStartedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val sharedPref = requireContext().getSharedPreferences(MainActivity.ONBOARDING_SHARED_PREF_KEY, Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(MainActivity.ONBOARDING_SHOWN, true).apply()

        binding = FragmentGetStartedBinding.inflate(inflater, container,false)

        binding.bvRateUs.setupWith(binding.llRateUs, RenderScriptBlur(requireContext()))
            .setFrameClearDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.white_blur))
            .setBlurRadius(20f)

        binding.bvPP.setupWith(binding.llPP, RenderScriptBlur(requireContext()))
            .setFrameClearDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.white_blur))
            .setBlurRadius(20f)

        binding.bvShare.setupWith(binding.llShare, RenderScriptBlur(requireContext()))
            .setFrameClearDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.white_blur))
            .setBlurRadius(20f)

        binding.llRateUs.setOnClickListener {
            reviewDialog(requireActivity())
        }

        binding.llPP.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://picturesaentertainment.blogspot.com/p/privacy-policy-for-dual-wallpaper.html")
            startActivity(intent)
        }

        binding.llShare.setOnClickListener {
            shareApp(requireContext())
        }

        binding.llGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_homeFragment)
        }

        return binding.root
    }
}