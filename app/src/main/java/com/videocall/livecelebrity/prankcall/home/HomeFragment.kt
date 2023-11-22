package com.videocall.livecelebrity.prankcall.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.toolbarCard,
            R.string.drawer_open, R.string.drawer_close)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setToolbarNavigationClickListener {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        }

        binding.ivMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        }

        binding.drawerLayout.setDrawerListener(toggle)
        toggle.syncState()
        binding.navView.bringToFront()

        binding.clVideoCall.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_partnerChooseFragment)
        }

        binding.clCallHistory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_callLogFragment)
        }

        binding.clAudio.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_partnerChooseFragment)
        }

        binding.clMsg.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_partnerChooseFragment)
        }

        return binding.root
    }
}