package com.videocall.livecelebrity.prankcall.audio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentAudioBinding

class AudioFragment : Fragment() {

    private lateinit var binding: FragmentAudioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioBinding.inflate(inflater, container, false)

        binding.ivHold.setOnClickListener {
            binding.ivHold.setImageResource(R.drawable.ic_hold_selected)
            binding.ivMute.setImageResource(R.drawable.ic_mute_unselected)
            binding.ivSpeaker.setImageResource(R.drawable.ic_speaker_unselected)
        }

        binding.ivMute.setOnClickListener {
            binding.ivHold.setImageResource(R.drawable.ic_hold_unselected)
            binding.ivMute.setImageResource(R.drawable.ic_mute_selected)
            binding.ivSpeaker.setImageResource(R.drawable.ic_speaker_unselected)
        }

        binding.ivSpeaker.setOnClickListener {
            binding.ivHold.setImageResource(R.drawable.ic_hold_unselected)
            binding.ivMute.setImageResource(R.drawable.ic_mute_unselected)
            binding.ivSpeaker.setImageResource(R.drawable.ic_speaker_selected)
        }

        binding.btnEndCall.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}