package com.videocall.livecelebrity.prankcall.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment() {

    private lateinit var binding: FragmentPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionBinding.inflate(inflater, container, false)

        binding.btnGrantPerm.setOnClickListener {
            findNavController().navigate(R.id.action_permissionFragment_to_getStartedFragment)
        }

        return binding.root
    }
}