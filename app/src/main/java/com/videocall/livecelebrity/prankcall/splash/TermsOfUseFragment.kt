package com.videocall.livecelebrity.prankcall.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        }

        binding.btnPrivacy.setOnClickListener {

        }

        binding.btnTmc.setOnClickListener {

        }


        return binding.root
    }
}