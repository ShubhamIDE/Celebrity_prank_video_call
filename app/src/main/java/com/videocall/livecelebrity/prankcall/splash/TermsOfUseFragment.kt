package com.videocall.livecelebrity.prankcall.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.videocall.livecelebrity.prankcall.MainActivity
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentTermsOfUseBinding

class TermsOfUseFragment : Fragment() {

    private lateinit var binding: FragmentTermsOfUseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermsOfUseBinding.inflate(inflater, container, false)

        binding.btnAgree.setOnClickListener {
            findNavController().navigate(R.id.action_termsOfUseFragment_to_onboardingFragment)
        }

        binding.btnPrivacy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://picturesaentertainment.blogspot.com/p/privacy-policy-for-dual-wallpaper.html")
            startActivity(intent)
        }

        binding.btnTmc.setOnClickListener {
            findNavController().navigate(R.id.action_termsOfUseFragment_to_tcsFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireContext().startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }

        return binding.root
    }
}