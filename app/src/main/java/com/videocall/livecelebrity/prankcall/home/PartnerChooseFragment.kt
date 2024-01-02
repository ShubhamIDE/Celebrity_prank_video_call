package com.videocall.livecelebrity.prankcall.home

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adsmodule.api.adsModule.utils.AdUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses1.LifeCycleOwner
import com.videocall.livecelebrity.prankcall.audio.AudioFragment
import com.videocall.livecelebrity.prankcall.databinding.FragmentPartnerChooseBinding
import com.videocall.livecelebrity.prankcall.databinding.RvPartnerItemBinding
import com.videocall.livecelebrity.prankcall.message.ChatsFragment
import com.videocall.livecelebrity.prankcall.splash.ItemOffsetDecoration
import com.videocall.livecelebrity.prankcall.splash.SplashScreenActivity
import com.videocall.livecelebrity.prankcall.utils.Celebrity
import com.videocall.livecelebrity.prankcall.video.VideoFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue


class PartnerChooseFragment : Fragment() {

    private lateinit var binding: FragmentPartnerChooseBinding
    private var job: Job = Job()
    private val partnersList = mutableListOf<Celebrity>()
    private lateinit var partnersAdapter: ChoosePartnerAdapter
    private val originalPartnersList = mutableListOf<Celebrity>()

    companion object{
        const val TYPE_KEY = "type_key"
        const val TYPE_MESSAGE = "Message"
        const val TYPE_AUDIO = "Audio"
        const val TYPE_VIDEO = "video"
        var selectedType: String = TYPE_MESSAGE
        val CELEB_BOLLYWOOD = "Bollywood"
        val CELEB_HOLLYWOOD = "Hollywood"
        var CELEBRITY_TYPE: String = CELEB_BOLLYWOOD

        lateinit var picasso: Picasso
        fun initializePicasso(context: Context) {
            if(::picasso.isInitialized) return
            picasso = Picasso.Builder(context)
                .memoryCache(LruCache(10 * 1024 * 1024)) // 10 MB memory cache
//                .diskCache(object : DiskCache.Factory {
//                    override fun build(): DiskCache? {
//                        val cacheDir = File(context.applicationContext.cacheDir, "picasso_cache")
//                        return DiskLruCacheWrapper.create(cacheDir, 50 * 1024 * 1024) // 50 MB disk cache
//                    }
//                })
                .build()

            Picasso.setSingletonInstance(picasso)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPartnerChooseBinding.inflate(inflater, container, false)

        binding.btnBackArrow.setOnClickListener {
            it.isClickable = false
            AdUtils.showBackPressAd(
                LifeCycleOwner.activity
            ) { state_load: Boolean ->
                it.isClickable = true
                findNavController().navigateUp()
            }
        }

        binding.searchEdtTxt.addTextChangedListener {
            if(it!=null){
                if(it.toString()!=""){
                    binding.ivSearch.visibility = View.GONE
                    binding.btnClear.visibility = View.VISIBLE
                }
                else {
                    binding.ivSearch.visibility = View.VISIBLE
                    binding.btnClear.visibility = View.GONE
                }
                filterList(it.toString())
            }else {
                binding.btnClear.visibility = View.GONE
            }
        }

        binding.btnClear.setOnClickListener {
            binding.searchEdtTxt.setText("")
        }

        if(CELEBRITY_TYPE == CELEB_BOLLYWOOD){
            SplashScreenActivity.celebrityListBollywood.observe(viewLifecycleOwner){
                if(it!=null && partnersList.isEmpty()){
                    partnersList.clear()
                    partnersList.addAll(it)
                    originalPartnersList.clear()
                    originalPartnersList.addAll(it)
                    partnersAdapter.notifyDataSetChanged()
                }
            }
        }
        else{
            SplashScreenActivity.celebrityListHollywood.observe(viewLifecycleOwner){
                if(it!=null && partnersList.isEmpty()){
                    partnersList.clear()
                    partnersList.addAll(it)
                    originalPartnersList.clear()
                    originalPartnersList.addAll(it)
                    partnersAdapter.notifyDataSetChanged()
                }
            }
        }


        originalPartnersList.addAll(partnersList)
        partnersAdapter =  ChoosePartnerAdapter(partnersList){
            if(selectedType == TYPE_MESSAGE){
                ChatsFragment.celebrity = partnersList[it]
                AdUtils.showInterstitialAd(
                    LifeCycleOwner.activity
                ) { state_load: Boolean ->
                    findNavController().navigate(R.id.action_partnerChooseFragment_to_chatsFragment)
                }
            }
            else if(selectedType == TYPE_AUDIO){
                AudioFragment.celebrity = partnersList[it]
                AdUtils.showInterstitialAd(
                    
                    LifeCycleOwner.activity
                ) { state_load: Boolean ->
                    findNavController().navigate(R.id.action_partnerChooseFragment_to_audioFragment)
                }
            }
            else{
                VideoFragment.celebrity = partnersList[it]
                AdUtils.showInterstitialAd(
                    
                    LifeCycleOwner.activity
                ) { state_load: Boolean ->
                    findNavController().navigate(R.id.action_partnerChooseFragment_to_videoFragment)
                }
            }
        }
        binding.rvPartners.adapter = partnersAdapter

        binding.rvPartners.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPartners.addItemDecoration(ItemOffsetDecoration(30))

        requireActivity().onBackPressedDispatcher.addCallback {
            AdUtils.showBackPressAd(
                LifeCycleOwner.activity,
                
            ) { state_load: Boolean ->
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    fun filterList(text: String){
        job.cancel()
        job = lifecycleScope.launch {
            delay(300)
            partnersList.clear()
            partnersList.addAll(originalPartnersList.filter {
                it.name.startsWith(text, ignoreCase = true) || it.profession.startsWith(text, ignoreCase = true)
            })
            partnersAdapter.notifyDataSetChanged()
        }
    }
}


class ChoosePartnerAdapter(
    private val personsList: List<Celebrity>,
    val onClick: (pos: Int) -> Unit
): RecyclerView.Adapter<ChoosePartnerAdapter.PersonVH>()
{
    private var jobQueue: Queue<Job> = LinkedList<Job>()
    private var scopeList: Queue<CoroutineScope> = LinkedList<CoroutineScope>()
    private var count = 0;
    private var firstTime = true;

    inner class PersonVH(val binding: RvPartnerItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonVH {
        val binding = RvPartnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonVH(binding);
    }

    override fun getItemCount() = personsList.size

    override fun onBindViewHolder(holder: PersonVH, position: Int) {
        count++
        if(count > 10){
            count = 0
            val job = jobQueue.poll()
            job?.cancel()
            val scope = scopeList.poll()
            scope?.cancel()
        }

        val person = personsList[holder.adapterPosition]
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        scope.launch {
           // delay(200)
            Glide
                .with(holder.binding.ivImg)
                .asBitmap()
                .load(person.profile_url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .override(400, 600)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        holder.binding.ivImg.setImageBitmap(resource)
                    }
                })

//            picasso.load(person.profile_url)
//                .resize(400, 600)
//                .placeholder(R.drawable.bg_blue)
//                .into(holder.binding.ivImg)
//            Glide.with(holder.binding.ivImg).load(person.profile_url).into(holder.binding.ivImg)
        }

        jobQueue.add(job)
        scopeList.add(scope)

        holder.binding.tvName.text = person.name
        holder.binding.tvProfession.text = person.profession
        holder.itemView.setOnClickListener {
            onClick(holder.adapterPosition)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        while(jobQueue.isNotEmpty()){
            val job = jobQueue.poll()
            job?.cancel()
        }
        while(scopeList.isNotEmpty()){
            val scope = scopeList.poll()
            scope?.cancel()
        }
    }
}