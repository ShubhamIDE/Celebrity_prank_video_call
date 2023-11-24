package com.videocall.livecelebrity.prankcall.audio

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentAudioBinding
import com.videocall.livecelebrity.prankcall.utils.CallHistory
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

class AudioFragment : Fragment() {

    private lateinit var binding: FragmentAudioBinding
    private lateinit var mediaPlayer: MediaPlayer
    var holdOn = false;
    var speakerOn = false
    var muteOn = false;
    var lastTime = 0L
    private var duratoinJob: Job = Job()

    companion object{
        var audioHistory: CallHistory? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioBinding.inflate(inflater, container, false)

        audioHistory = CallHistory(
            "1",
            "https://unsplash.com/photos/black-and-white-plane-flying-over-the-sea-during-daytime-rNqs9hM0U8I",
            "Alia Bhatt",
            "Today 5:30 PM",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            Date()
        )
        binding.tvName.text = audioHistory!!.name
        Glide.with(requireContext()).load(audioHistory!!.img).into(binding.ivImg)

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
            mediaPlayer.stop()
            mediaPlayer.release()
            findNavController().navigateUp()
        }

        binding.btnBackArrow.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.release()
            findNavController().navigateUp()
        }

        return binding.root
    }

    fun formatTimeFromLong(timeInMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}