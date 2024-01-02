package com.videocall.livecelebrity.prankcall

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adsmodule.api.adsModule.utils.Globals
import com.dualwallpaper.livehd.wallpaper.utils.NetworkViewModel
import com.videocall.livecelebrity.prankcall.databinding.ActivityMainBinding
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment.Companion.picasso

class MainActivity : AppCompatActivity() {

    private lateinit var networkViewModel: NetworkViewModel
    private lateinit var binding: ActivityMainBinding

    companion object{
        val MAINACTIVITY_INTENT: String = "com.videocall.livecelebrity.prankcall.MainActivityIntent"
        const val ONBOARDING_SHOWN = "onboarding_shown2"
        const val ONBOARDING_SHARED_PREF_KEY = "onboarding_shared_pref"
        var LANG_CHANGED = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PartnerChooseFragment.initializePicasso(this)

        val intentFilter = IntentFilter()
        intentFilter.addAction(MAINACTIVITY_INTENT)
        val receiver = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                finish()
            }
        }

        ContextCompat.registerReceiver(this, receiver , intentFilter, ContextCompat.RECEIVER_EXPORTED)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        networkViewModel = NetworkViewModel(this)
        if (!Globals.isConnectingToInternet(this)) {
            binding.noInternetCl.visibility = View.VISIBLE
        }

        networkViewModel.isConnected.observeForever {
            if(it==null || !it){
                binding.noInternetCl.visibility = View.VISIBLE
            }
            else if(it){
                binding.noInternetCl.visibility = View.GONE
            }
        }

        binding.noInternetCl.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }
        })


    }
}