package com.videocall.livecelebrity.prankcall.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentVideoBinding
import com.videocall.livecelebrity.prankcall.utils.CallHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class VideoFragment : Fragment() {

    private lateinit var binding: FragmentVideoBinding
    private var imageCapture: ImageCapture? = null
    private var currentCameraSelector: CameraSelector? = null
    var cameraProvider: ProcessCameraProvider? = null
    private var cameraExecutor: ExecutorService? = null
    var preview: Preview? = null
    var videoOn = true;
    var baseUrl = "https://www.youtube.com/watch?v="

    companion object{
        var videoHistory: CallHistory? = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        Glide.with(requireContext()).load(R.drawable.call_loading_gif).into(binding.ivLoading)

        videoHistory?.let{
            val iFrameOptions = IFramePlayerOptions.Builder().controls(0).rel(0).ivLoadPolicy(1).ccLoadPolicy(1).build()
            lifecycle.addObserver(binding.videoView)

            binding.videoView.matchParent()
            binding.videoView.initialize(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    binding.loaderCl.visibility = View.VISIBLE
                    youTubePlayer.loadVideo("Z5VjmLhnoJU", 0F)
                    youTubePlayer.mute()
                    youTubePlayer.setLoop(true)
                    lifecycleScope.launch {
                        delay(4000)
                        withContext(Dispatchers.Main){
                            binding.loaderCl.visibility = View.GONE
                            youTubePlayer.unMute()
                        }
                    }
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    super.onStateChange(youTubePlayer, state)
                    if (state === PlayerState.ENDED) {
                        binding.loaderCl.setVisibility(View.VISIBLE)
                        youTubePlayer.loadVideo("Z5VjmLhnoJU", 0f)
                        youTubePlayer.mute()
                        youTubePlayer.setLoop(true)
                        lifecycleScope.launch {
                            delay(4000)
                            withContext(Dispatchers.Main){
                                binding.loaderCl.visibility = View.GONE
                                youTubePlayer.unMute()
                            }
                        }
                    }
                }
            }, true, iFrameOptions)

        }

        startCamera(CameraSelector.DEFAULT_BACK_CAMERA);
        cameraExecutor = Executors.newSingleThreadExecutor();

        binding.btnSwitch.setOnClickListener {
            switchCamera()
        }

        binding.btnBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEndCall.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnVideo.setOnClickListener {
            videoOn = !videoOn
            if(videoOn){
                binding.btnVideo.setImageResource(R.drawable.ic_video_call_on)
                binding.cameraPreviewView.visibility = View.VISIBLE
            }
            else {
                binding.btnVideo.setImageResource(R.drawable.ic_video_call_off)
                binding.cameraPreviewView.visibility = View.GONE
            }
        }

        return binding.root
    }

    private fun switchCamera() {
        currentCameraSelector = if (currentCameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera(currentCameraSelector!!)
    }

    private fun startCamera(cameraSelector: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                cameraProvider = cameraProviderFuture.get()

                // Preview
                preview = Preview.Builder().build()
                preview!!.setSurfaceProvider(binding.cameraPreviewView.getSurfaceProvider())
                imageCapture = ImageCapture.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_16_9).build()
                val recorder = Recorder.Builder()
                    .setQualitySelector(QualitySelector.from(Quality.FHD)).build()
                // Select back camera as a default
                currentCameraSelector = cameraSelector

                // Unbind use cases before rebinding
                cameraProvider!!.unbindAll()

                // Bind use cases to camera
                // can't bind more than 4 use cases it throws error based on camera hardware hence the condition
                cameraProvider!!.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                )
                //  if(openForImage){
//                    cameraProvider.bindToLifecycle(this, currentCameraSelector, preview, imageCapture);
//
//                }
//                // if not open for camera than we don't need imageCapture use case instead we are using imageAnalysis use case
//                else cameraProvider.bindToLifecycle(this, currentCameraSelector, preview, videoCapture);
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }
}