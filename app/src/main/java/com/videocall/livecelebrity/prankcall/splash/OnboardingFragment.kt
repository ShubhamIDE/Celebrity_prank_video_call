package com.videocall.livecelebrity.prankcall.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.adsmodule.api.adsModule.AdUtils
import com.adsmodule.api.adsModule.utils.Constants
import com.bumptech.glide.Glide
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses.AppOpenAds
import com.videocall.livecelebrity.prankcall.databinding.FragmentOnboardingBinding
import com.videocall.livecelebrity.prankcall.databinding.OnboardingItemBinding
import com.videocall.livecelebrity.prankcall.databinding.RvLangItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        binding.onboardingVP.adapter = OnboardingAdapter{
            if(it != 2){
                binding.onboardingVP.setCurrentItem(it+1, true)
            }
            else {
                AdUtils.showInterstitialAd(
                    Constants.adsResponseModel.interstitial_ads.adx,
                    AppOpenAds.activity
                ) { state_load: Boolean ->
                    findNavController().navigate(R.id.action_onboardingFragment_to_permissionFragment)
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            AdUtils.showBackPressAds(
                AppOpenAds.activity,
                Constants.adsResponseModel.app_open_ads.adx,
            ) { state_load: Boolean ->
                findNavController().navigateUp()
            }
        }

        binding.onboardingVP.doOnPreDraw {
            binding.onboardingVP.setCurrentItem(0, false)
        }

        return binding.root
    }
}


class OnboardingAdapter(
    val onNextClicked: (pos: Int) -> Unit
): RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>(){

    inner class OnboardingViewHolder(val binding: OnboardingItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = OnboardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding);
    }

    override fun getItemCount() = 3

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val bgImg = if(position == 0){
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_onboarding_1)
        }else if(position == 1){
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_onboarding_2)
        }else {
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_onboarding_scr3)
        }
        holder.binding.parent.background = bgImg

        val title = if(position == 0) holder.itemView.context.getString(R.string.call_any_celebrity)
        else if(position == 1) holder.itemView.context.getString(R.string.Celebrity_Video_call)
        else holder.itemView.context.getString(R.string.Celebrity_chat)

        val subtitle = if(position == 0) holder.itemView.context.getString(R.string.one_tap_calling_to_connect_with_your_favourite_ncelebrities)
        else if(position == 1) holder.itemView.context.getString(R.string.experience_face_to_face_calls_with_any_ncelebrity_you_want)
        else holder.itemView.context.getString(R.string.send_messages_and_fan_love_directly_to_nyour_beloved_celebrity)

        val btnNextTxtCol = if(position == 0) ContextCompat.getColor(holder.itemView.context, R.color.green)
        else if(position == 1) ContextCompat.getColor(holder.itemView.context, R.color.darkBlue)
        else ContextCompat.getColor(holder.itemView.context, R.color.red)

        holder.binding.tvTitle.text = title
        holder.binding.tvSubtitle.text = subtitle
        holder.binding.btnNext.setTextColor(btnNextTxtCol)

        holder.binding.btnNext.setOnClickListener {
            onNextClicked(position)
        }
    }
}