package com.videocall.livecelebrity.prankcall.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentGetStartedBinding
import eightbitlab.com.blurview.RenderScriptBlur

class GetStartedFragment : Fragment() {

    private lateinit var binding: FragmentGetStartedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        return binding.root
    }
}