package com.videocall.livecelebrity.prankcall.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import com.adsmodule.api.adsModule.AdUtils
import com.adsmodule.api.adsModule.utils.Constants
import com.flurry.android.FlurryAgent
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses.AppOpenAds
import com.videocall.livecelebrity.prankcall.databinding.FragmentNavViewBinding
import com.videocall.livecelebrity.prankcall.splash.LanguageActivity
import eightbitlab.com.blurview.BuildConfig

class NavViewFragment : Fragment() {

    private lateinit var binding: FragmentNavViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavViewBinding.inflate(inflater, container, false)

        binding.llLang.setOnClickListener {
            LanguageActivity.langChange = true
            AdUtils.showInterstitialAd(
                Constants.adsResponseModel.interstitial_ads.adx,
                AppOpenAds.activity
            ) { state_load: Boolean ->
                requireActivity().startActivity(Intent(requireContext(), LanguageActivity::class.java))
            }
            HomeFragment.binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.llPP.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://pixarllwallstudios.blogspot.com/p/privacy-policy.html")
            startActivity(intent)
            HomeFragment.binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.llShare.setOnClickListener {
            shareApp(requireContext())
            HomeFragment.binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.llRateUs.setOnClickListener {
            reviewDialog(requireActivity())
            HomeFragment.binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        return binding.root
    }
}


fun shareApp(context: Context) {
    FlurryAgent.logEvent("Share app is clicked")
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.putExtra(
        Intent.EXTRA_SUBJECT, "${context.getString(R.string.app_name)} ${context.getString(R.string.txt_app_invitattion)}"
    )
    intent.putExtra(
        Intent.EXTRA_TEXT,
        "\n${context.getString(R.string.txt_download)}" + "\nhttps://play.google.com/store/apps/details?id=" + context.packageName
    )
    intent.type = "text/plain"
    context.startActivity(intent)
}

fun reviewDialog(activity: Activity?) {
    FlurryAgent.logEvent("Review is clicked")
    val manager = if (BuildConfig.DEBUG) FakeReviewManager(activity) else ReviewManagerFactory.create(
        activity!!
    )
    val request = manager.requestReviewFlow()
    request.addOnCompleteListener { task: Task<ReviewInfo?> ->
        if (task.isSuccessful) {
            val reviewInfo = task.result
            val flow = manager.launchReviewFlow(
                activity!!,
                reviewInfo!!
            )
            flow.addOnCompleteListener { task1: Task<Void?>? -> }
        }
    }
}