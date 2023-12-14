package com.videocall.livecelebrity.prankcall.splash

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adsmodule.api.adsModule.utils.AdUtils
import com.bumptech.glide.Glide
import com.videocall.livecelebrity.prankcall.MainActivity
import com.videocall.livecelebrity.prankcall.MainActivity.Companion.MAINACTIVITY_INTENT
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses1.LifeCycleOwner
import com.videocall.livecelebrity.prankcall.databinding.ActivityLanguageBinding
import com.videocall.livecelebrity.prankcall.databinding.RvLangItemBinding
import com.videocall.livecelebrity.prankcall.utils.LocaleHelper

class LanguageActivity : AppCompatActivity(), LanguageClickListener {

    private val localList = listOf("en", "hi", "es", "fr"
        , "pt-rBR", "ko", "ru", "ja", "ar")
    private var locale = "en"
    private var langSelectedPos = 0
    private val languageList = mutableListOf<String>()
    private lateinit var binding: ActivityLanguageBinding

    companion object{
        const val LANG_PREF = "lang_pref"
        var calledFromMain = false
        var langChange = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calledFromMain = true

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences(LANG_PREF , Context.MODE_PRIVATE)
        locale = pref.getString(LocaleHelper.SELECTED_LANGUAGE, "en").toString()
        langSelectedPos = localList.indexOf(locale)

        languageList.add(getString(R.string.english));
        languageList.add(getString(R.string.hindi));
        languageList.add(getString(R.string.spanish));
        languageList.add(getString(R.string.french));
        languageList.add(getString(R.string.portuguese));
        languageList.add(getString(R.string.korean));
        languageList.add(getString(R.string.russian));
        languageList.add(getString(R.string.japanese));
        languageList.add(getString(R.string.arabic));

        binding.rvLanguages.adapter = LanguageAdapter(languageList, this, langSelectedPos);
        binding.rvLanguages.layoutManager = GridLayoutManager(this, 3)
        binding.rvLanguages.addItemDecoration(ItemOffsetDecoration(20))

        binding.btnNext.setOnClickListener {
            MainActivity.LANG_CHANGED = true
            with(pref.edit()){
                putString(LocaleHelper.SELECTED_LANGUAGE , locale)
            }.commit()
            LocaleHelper.setLocale(this, locale)
            AdUtils.showInterstitialAd(
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                calledFromMain = false
                if(langChange){
                    langChange = false
                    val local = Intent()
                    local.action = MAINACTIVITY_INTENT
                    sendBroadcast(local)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    override fun onItemClicked(position: Int) {
        locale = localList[position]
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}



class LanguageAdapter(
    private val languageList: List<String>,
    val onClickListener: LanguageClickListener,
    var lastSelectedPosition : Int = 0
): RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>(){

    private val langFlagList = listOf(
        R.drawable.ic_flag_english,
        R.drawable.ic_flag_hindi,
        R.drawable.ic_flag_spanish,
        R.drawable.ic_flag_french,
        R.drawable.ic_flag_portuguese,
        R.drawable.ic_flag_korean,
        R.drawable.ic_flag_russian,
        R.drawable.ic_flag_japanese,
        R.drawable.ic_flag_arabic
    )

    inner class LanguageViewHolder(val binding: RvLangItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = RvLangItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding);
    }

    override fun getItemCount() = languageList.size

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        Glide.with(holder.binding.ivFlag).load(langFlagList[position]).into(holder.binding.ivFlag);
        if(position == lastSelectedPosition){
            holder.itemView.setBackgroundResource(R.drawable.bg_lang_selected)
            holder.binding.tvLangName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.darkBlue))
        }
        else {
            holder.itemView.setBackgroundResource(R.drawable.bg_lang_card)
            holder.binding.tvLangName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }
        holder.binding.tvLangName.text = languageList[position]
        holder.itemView.setOnClickListener {
            val p = lastSelectedPosition
            lastSelectedPosition = position
            notifyItemChanged(p)
            notifyItemChanged(lastSelectedPosition)
            onClickListener.onItemClicked(position)
        }
    }
}

interface LanguageClickListener {
    fun onItemClicked(position: Int);
}

class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {
    constructor(
        context: Context,
        @DimenRes itemOffsetId: Int
    ) : this(context.resources.getDimensionPixelSize(itemOffsetId))

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[mItemOffset, mItemOffset, mItemOffset] = mItemOffset
    }
}