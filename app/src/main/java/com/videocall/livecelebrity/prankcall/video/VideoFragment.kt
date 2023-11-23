package com.videocall.livecelebrity.prankcall.video

import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
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
import androidx.navigation.fragment.findNavController
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentVideoBinding
import com.videocall.livecelebrity.prankcall.utils.CallHistory
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

    companion object{
        var videoHistory: CallHistory? = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)


        videoHistory?.let{
            //TODO play yt video and the start the timer
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