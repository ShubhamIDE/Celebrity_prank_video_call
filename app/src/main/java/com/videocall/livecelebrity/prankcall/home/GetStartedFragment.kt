package com.videocall.livecelebrity.prankcall.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.utils.AdUtils
import com.videocall.livecelebrity.prankcall.MainActivity
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses1.LifeCycleOwner
import com.videocall.livecelebrity.prankcall.databinding.ExitDialogBinding
import com.videocall.livecelebrity.prankcall.databinding.FragmentGetStartedBinding
import eightbitlab.com.blurview.RenderScriptBlur
import java.util.Objects


class GetStartedFragment : Fragment() {

    private lateinit var binding: FragmentGetStartedBinding
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val sharedPref = requireContext().getSharedPreferences(MainActivity.ONBOARDING_SHARED_PREF_KEY, Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(MainActivity.ONBOARDING_SHOWN, true).apply()

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

        binding.llRateUs.setOnClickListener {
            reviewDialog(requireActivity())
        }

        binding.llPP.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://pixarllwallstudios.blogspot.com/p/privacy-policy.html")
            startActivity(intent)
        }

        binding.llShare.setOnClickListener {
            shareApp(requireContext())
        }

        binding.llGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_homeFragment)
//            AdUtils.showInterstitialAd(
//
//                LifeCycleOwner.activity
//            ) { state_load: Boolean ->
//                findNavController().navigate(R.id.action_getStartedFragment_to_homeFragment)
//            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            if(!::dialog.isInitialized || !dialog.isShowing){
                showExitDialog()
            }
        }

        return binding.root
    }

    fun showExitDialog(){
        val builder = AlertDialog.Builder(requireActivity())
        val exitDialogBinding: ExitDialogBinding = ExitDialogBinding.inflate(
            LayoutInflater.from(requireActivity()),
            null,
            false
        )
        builder.setView(exitDialogBinding.getRoot())
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        Objects.requireNonNull(dialog.window)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        exitDialogBinding.llStars.setOnClickListener {
            reviewDialog(requireActivity())
        }
        exitDialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        exitDialogBinding.btnExit.setOnClickListener {
            dialog.dismiss()
            requireActivity().finish()
        }
    }
}