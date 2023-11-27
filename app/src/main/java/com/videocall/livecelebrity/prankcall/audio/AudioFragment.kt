package com.videocall.livecelebrity.prankcall.audio

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentAudioBinding
import com.videocall.livecelebrity.prankcall.utils.CallHistory
import com.videocall.livecelebrity.prankcall.utils.Celebrity
import com.videocall.livecelebrity.prankcall.utils.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class AudioFragment : Fragment() {

    private lateinit var binding: FragmentAudioBinding
    private lateinit var mediaPlayer: MediaPlayer
    var holdOn = false;
    var speakerOn = false
    var muteOn = false;
    var lastTime = 0L
    private var duratoinJob: Job = Job()
    private lateinit var preferenceManager: PreferenceManager

    companion object{
        var audioHistory: CallHistory? = null
        lateinit var celebrity: Celebrity
        var fromHome = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioBinding.inflate(inflater, container, false)

        preferenceManager = PreferenceManager(requireContext())

        audioHistory = CallHistory(
            id = celebrity.profile_url,
            img = celebrity.profile_url,
            name = celebrity.name,
            audioUrl = celebrity.audio_url_list[Random.nextInt(celebrity.audio_url_list.size)],
            lastcallTime = Date()
        )

        preferenceManager.addToAudioList(audioHistory!!)

        binding.tvName.text = audioHistory!!.name
        Glide.with(requireContext()).load(audioHistory!!.img).into(binding.ivImg)

        lifecycleScope.launch {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer.setDataSource(audioHistory!!.audioUrl)
                mediaPlayer.prepare()
                mediaPlayer.start()
                duratoinJob = lifecycleScope.launch {
                    withContext(Dispatchers.Main){
                        while (isActive){
                            delay(1000)
                            lastTime+=1000
                            val time = formatTimeFromLong(lastTime)
                            binding.tvDuratoin.text = time
                        }
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.ivHold.setOnClickListener {
            holdOn = !holdOn
            if(holdOn){
                binding.ivHold.setImageResource(R.drawable.ic_hold_selected)
                binding.ivMute.alpha = 0.4f
                mediaPlayer.pause()
                duratoinJob.cancel()
            }else {
                binding.ivHold.setImageResource(R.drawable.ic_hold_unselected)
                binding.ivMute.alpha = 1f
                mediaPlayer.start()
                duratoinJob = lifecycleScope.launch {
                    withContext(Dispatchers.Main){
                        while (isActive){
                            delay(1000)
                            lastTime+=1000
                            val time = formatTimeFromLong(lastTime)
                            binding.tvDuratoin.text = time
                        }
                    }
                }
            }
        }

        binding.ivMute.setOnClickListener {
            if(binding.ivMute.alpha == 1f){
                muteOn = !muteOn
                if(muteOn){
                    binding.ivMute.setImageResource(R.drawable.ic_mute_selected)
                }
                else {
                    binding.ivMute.setImageResource(R.drawable.ic_mute_unselected)
                }
            }
        }

        binding.ivSpeaker.setOnClickListener {
            speakerOn = !speakerOn
            if(speakerOn){
                binding.ivSpeaker.setImageResource(R.drawable.ic_speaker_selected)
            }else{
                binding.ivSpeaker.setImageResource(R.drawable.ic_speaker_unselected)
            }
        }

        binding.btnEndCall.setOnClickListener {
            if(fromHome){
                findNavController().popBackStack(R.id.homeFragment, false)
            }
            else findNavController().navigateUp()
        }

        binding.btnBackArrow.setOnClickListener {
            if(fromHome){
                findNavController().popBackStack(R.id.homeFragment, false)
            }
            else findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            if(fromHome){
                findNavController().popBackStack(R.id.homeFragment, false)
            }
            else findNavController().navigateUp()
        }

        return binding.root
    }

    fun formatTimeFromLong(timeInMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        fromHome = false
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}