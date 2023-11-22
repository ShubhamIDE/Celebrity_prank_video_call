package com.videocall.livecelebrity.prankcall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    companion object{
        const val ONBOARDING_SHOWN = "onboarding_shown1"
        const val ONBOARDING_SHARED_PREF_KEY = "onboarding_shared_pref"
        var LANG_CHANGED = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}