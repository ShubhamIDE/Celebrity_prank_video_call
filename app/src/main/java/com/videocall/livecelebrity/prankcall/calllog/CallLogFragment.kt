package com.videocall.livecelebrity.prankcall.calllog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adsmodule.api.adsModule.utils.AdUtils
import com.squareup.picasso.Picasso
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses1.LifeCycleOwner
import com.videocall.livecelebrity.prankcall.audio.AudioFragment
import com.videocall.livecelebrity.prankcall.databinding.FragmentCallLogBinding
import com.videocall.livecelebrity.prankcall.databinding.RvChatItemBinding
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment.Companion.TYPE_AUDIO
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment.Companion.TYPE_MESSAGE
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment.Companion.TYPE_VIDEO
import com.videocall.livecelebrity.prankcall.message.ChatsFragment
import com.videocall.livecelebrity.prankcall.splash.SplashScreenActivity
import com.videocall.livecelebrity.prankcall.utils.CallHistory
import com.videocall.livecelebrity.prankcall.utils.Celebrity
import com.videocall.livecelebrity.prankcall.utils.ChatHistory
import com.videocall.livecelebrity.prankcall.utils.PreferenceManager
import com.videocall.livecelebrity.prankcall.video.VideoFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CallLogFragment : Fragment() {

    private lateinit var binding: FragmentCallLogBinding
    private lateinit var chatsAdapter: CallLogRVAdapter
    private lateinit var audioAdapter: CallLogRVAdapter
    private lateinit var videoAdapter: CallLogRVAdapter

    private lateinit var audioCallHistory: ArrayList<CallHistory>
    private lateinit var videoCallHistory: ArrayList<CallHistory>
    private lateinit var chatsHistoryList: ArrayList<ChatHistory>

    private lateinit var preferenceManager: PreferenceManager
    private var job: Job = Job()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallLogBinding.inflate(inflater, container, false)

        preferenceManager = PreferenceManager(requireContext())

        audioCallHistory = preferenceManager.getAudioList()
        videoCallHistory = preferenceManager.getVideoCallList()
        chatsHistoryList = preferenceManager.getChatsList()

        audioCallHistory.reverse()
        videoCallHistory.reverse()
        chatsHistoryList.reverse()

        chatsAdapter = CallLogRVAdapter(chatsHistoryList, audioCallHistory, videoCallHistory, TYPE_MESSAGE,
            onCallClicked = {},
            onVideoCallClicked = {},
            onItemClick = {
                var celebrity: Celebrity? = null
                if(SplashScreenActivity.celebrityListBollywood.value!=null) {
                    ChatsFragment.chatHistory = chatsHistoryList[it]
                    for (celeb in SplashScreenActivity.celebrityListBollywood.value!!) {
                        if (celeb.profile_url == chatsHistoryList[it].img) {
                            celebrity = celeb
                            break;
                        }
                    }
                }
                if(celebrity == null && SplashScreenActivity.celebrityListHollywood.value!=null){
                    ChatsFragment.chatHistory = chatsHistoryList[it]
                    for (celeb in SplashScreenActivity.celebrityListHollywood.value!!) {
                        if (celeb.profile_url == chatsHistoryList[it].img) {
                            celebrity = celeb
                            break;
                        }
                    }
                }
                if(celebrity!=null){
                    ChatsFragment.celebrity = celebrity
                    AdUtils.showInterstitialAd(

                        LifeCycleOwner.activity
                    ) { state_load: Boolean ->
                        findNavController().navigate(R.id.action_callLogFragment_to_chatsFragment)
                    }
                }
            })

        audioAdapter = CallLogRVAdapter(chatsHistoryList, audioCallHistory, videoCallHistory, TYPE_AUDIO,
            onCallClicked = {
                var celebrity: Celebrity? = null
                if(SplashScreenActivity.celebrityListBollywood.value!=null) {
                    AudioFragment.audioHistory = audioCallHistory[it]
                    for (celeb in SplashScreenActivity.celebrityListBollywood.value!!) {
                        if (celeb.profile_url == audioCallHistory[it].img) {
                            celebrity = celeb
                            break;
                        }
                    }
                }
                if(celebrity == null && SplashScreenActivity.celebrityListHollywood.value!=null) {
                    AudioFragment.audioHistory = audioCallHistory[it]
                    for (celeb in SplashScreenActivity.celebrityListHollywood.value!!) {
                        if (celeb.profile_url == audioCallHistory[it].img) {
                            celebrity = celeb
                            break;
                        }
                    }
                }
                if(celebrity!=null){
                    AudioFragment.celebrity = celebrity!!
                    AdUtils.showInterstitialAd(

                        LifeCycleOwner.activity
                    ) { state_load: Boolean ->
                        findNavController().navigate(R.id.action_callLogFragment_to_audioFragment)
                    }
                }
            },
            onVideoCallClicked = {},
            onItemClick = {})

        videoAdapter = CallLogRVAdapter(chatsHistoryList, audioCallHistory, videoCallHistory, TYPE_VIDEO,
            onCallClicked = {},
            onVideoCallClicked = {
                var celebrity: Celebrity? = null
                if(SplashScreenActivity.celebrityListBollywood.value!=null) {
                    VideoFragment.videoHistory = videoCallHistory[it]
                    for (celeb in SplashScreenActivity.celebrityListBollywood.value!!) {
                        if (celeb.profile_url == videoCallHistory[it].img) {
                            celebrity = celeb
                            break;
                        }
                    }
                }
                if(celebrity == null && SplashScreenActivity.celebrityListHollywood.value!=null) {
                    VideoFragment.videoHistory = videoCallHistory[it]
                    for (celeb in SplashScreenActivity.celebrityListHollywood.value!!) {
                        if (celeb.profile_url == videoCallHistory[it].img) {
                            celebrity = celeb
                            break;
                        }
                    }
                }
                if(celebrity!=null){
                    VideoFragment.celebrity = celebrity!!
                    AdUtils.showInterstitialAd(

                        LifeCycleOwner.activity
                    ) { state_load: Boolean ->
                        findNavController().navigate(R.id.action_callLogFragment_to_videoFragment)
                    }
                }
            },
            onItemClick = {})

        binding.rvVideoCalls.adapter = videoAdapter
        binding.rvVideoCalls.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvChats.adapter = chatsAdapter
        binding.rvChats.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvAudioCalls.adapter = audioAdapter
        binding.rvAudioCalls.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if(chatsHistoryList.isEmpty()){
            binding.tvNoChatsFound.visibility = View.VISIBLE
            binding.btnStart.visibility = View.VISIBLE
            binding.tvNoChatsFound.text = getString(R.string.no_chats_found)
            binding.btnStart.text = getString(R.string.start_a_new_chat)
            binding.rvChats.visibility = View.GONE
        }
        else {
            binding.tvNoChatsFound.visibility = View.GONE
            binding.btnStart.visibility = View.GONE
            binding.rvChats.visibility = View.VISIBLE
        }

        binding.llChat.setOnClickListener {
            binding.rvAudioCalls.visibility = View.GONE
            binding.rvVideoCalls.visibility = View.GONE
            binding.rvChats.visibility = View.VISIBLE
            it.alpha = 1.0f
            binding.llAudio.alpha = 0.4f
            binding.llVideo.alpha = 0.4f
            binding.tvTitle.text = getString(R.string.chat)

            if(chatsHistoryList.isEmpty()){
                binding.tvNoChatsFound.visibility = View.VISIBLE
                binding.btnStart.visibility = View.VISIBLE
                binding.tvNoChatsFound.text = getString(R.string.no_chats_found)
                binding.btnStart.text = getString(R.string.start_a_new_chat)
                binding.rvChats.visibility = View.GONE
            }
            else {
                binding.tvNoChatsFound.visibility = View.GONE
                binding.btnStart.visibility = View.GONE
                binding.rvChats.visibility = View.VISIBLE
            }

        }

        binding.llAudio.setOnClickListener {
            binding.rvAudioCalls.visibility = View.VISIBLE
            binding.rvVideoCalls.visibility = View.GONE
            binding.rvChats.visibility = View.GONE
            it.alpha = 1.0f
            binding.llChat.alpha = 0.4f
            binding.llVideo.alpha = 0.4f
            binding.tvTitle.text = getString(R.string.audio_call)
            if(audioCallHistory.isEmpty()){
                binding.tvNoChatsFound.visibility = View.VISIBLE
                binding.btnStart.visibility = View.VISIBLE
                binding.tvNoChatsFound.text = getString(R.string.no_audio_call_history_found)
                binding.btnStart.text = getString(R.string.start_a_call)
                binding.rvAudioCalls.visibility = View.GONE
            }
            else {
                binding.tvNoChatsFound.visibility = View.GONE
                binding.btnStart.visibility = View.GONE
                binding.rvAudioCalls.visibility = View.VISIBLE
            }
        }

        binding.llVideo.setOnClickListener {
            binding.rvAudioCalls.visibility = View.GONE
            binding.rvVideoCalls.visibility = View.VISIBLE
            binding.rvChats.visibility = View.GONE
            it.alpha = 1.0f
            binding.llAudio.alpha = 0.4f
            binding.llChat.alpha = 0.4f
            binding.tvTitle.text = getString(R.string.video_call)
            if(videoCallHistory.isEmpty()){
                binding.tvNoChatsFound.visibility = View.VISIBLE
                binding.btnStart.visibility = View.VISIBLE
                binding.tvNoChatsFound.text = getString(R.string.no_video_call_history_found)
                binding.btnStart.text = getString(R.string.start_a_video_call)
                binding.rvVideoCalls.visibility = View.GONE
            }
            else {
                binding.tvNoChatsFound.visibility = View.GONE
                binding.btnStart.visibility = View.GONE
                binding.rvVideoCalls.visibility = View.VISIBLE
            }
        }

        binding.btnBackArrow.setOnClickListener {
            it.isClickable = false
            AdUtils.showBackPressAd(
                LifeCycleOwner.activity,
            ) { state_load: Boolean ->
                it.isClickable = true
                    findNavController().navigateUp()
            }
        }

        binding.btnStart.setOnClickListener {
            it.isClickable = false
            if(binding.llAudio.alpha == 1.0f){
                AdUtils.showInterstitialAd(
                    LifeCycleOwner.activity
                ) { state_load: Boolean ->
                    AudioFragment.fromHome = true
                    it.isClickable = true
                    PartnerChooseFragment.selectedType = PartnerChooseFragment.TYPE_AUDIO
                    findNavController().navigate(R.id.action_callLogFragment_to_selectCategoryFragment)
                }
            }else if(binding.llChat.alpha == 1.0f){
                AdUtils.showInterstitialAd(
                    LifeCycleOwner.activity
                ) { state_load: Boolean ->
                    ChatsFragment.fromHome = true
                    it.isClickable = true
                    PartnerChooseFragment.selectedType = PartnerChooseFragment.TYPE_MESSAGE
                    findNavController().navigate(R.id.action_callLogFragment_to_selectCategoryFragment)
                }
            }
            else {
                AdUtils.showInterstitialAd(
                    LifeCycleOwner.activity
                ) { state_load: Boolean ->
                    VideoFragment.fromHome = true
                    it.isClickable = true
                    PartnerChooseFragment.selectedType = PartnerChooseFragment.TYPE_VIDEO
                    findNavController().navigate(R.id.action_callLogFragment_to_selectCategoryFragment)
                }
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
            if(binding.rvChats.visibility == View.VISIBLE){
                chatsHistoryList.clear()
                chatsHistoryList.addAll(preferenceManager.getChatsList().filter {
                    it.name.startsWith(text, ignoreCase = true)
                })
                chatsAdapter.notifyDataSetChanged()
            }
            else if(binding.rvAudioCalls.visibility == View.VISIBLE){
                audioCallHistory.clear()
                audioCallHistory.addAll(preferenceManager.getAudioList().filter {
                    it.name.startsWith(text, ignoreCase = true)
                })
                audioAdapter.notifyDataSetChanged()
            }
            else {
                videoCallHistory.clear()
                videoCallHistory.addAll(preferenceManager.getVideoCallList().filter {
                    it.name.startsWith(text, ignoreCase = true)
                })
                videoAdapter.notifyDataSetChanged()
            }
        }
    }
}

class CallLogRVAdapter(
    private val chatsList: List<ChatHistory>,
    private val audioList: List<CallHistory>,
    private val videoList: List<CallHistory>,
    val type: String,
    val onItemClick: (pos: Int) -> Unit,
    val onCallClicked: (pos: Int) -> Unit,
    val onVideoCallClicked: (pos: Int) -> Unit
): RecyclerView.Adapter<CallLogRVAdapter.HistoryVH>()
{

    inner class HistoryVH(val binding: RvChatItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryVH {
        val binding = RvChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryVH(binding);
    }

    override fun getItemCount() = if(type == TYPE_MESSAGE) {
        chatsList.size
    }else if(type == TYPE_AUDIO) audioList.size
    else videoList.size

    override fun onBindViewHolder(holder: HistoryVH, position: Int) {
        if(type == TYPE_MESSAGE){
            val history = chatsList[holder.adapterPosition]

            Picasso.get().load(history.img)
                .resize(500, 500)
                .placeholder(R.drawable.bg_blue)
                .into(holder.binding.ivLogo)


            holder.binding.tvName.text = history.name
            if(history.msgList.isNotEmpty()){
                holder.binding.tvMessage.text = history.msgList[history.msgList.size-1].second.content
            }

            holder.binding.msgCount.visibility = View.GONE
            holder.binding.msgCount.text = history.msgList.size.toString()

            holder.binding.cardMsgCount.visibility = View.VISIBLE
            holder.binding.ivCall.visibility = View.GONE
            holder.binding.ivVideoCall.visibility = View.GONE
        }
        else if(type == TYPE_AUDIO){
            val history = audioList[holder.adapterPosition]

            Picasso.get().load(history.img)
                .resize(500, 500)
                .placeholder(R.drawable.bg_blue)
                .into(holder.binding.ivLogo)

            val formattedString = formatDate(history.lastcallTime)
            holder.binding.tvMessage.text = formattedString
            holder.binding.tvName.text = history.name
            holder.binding.cardMsgCount.visibility = View.GONE
            holder.binding.ivCall.visibility = View.VISIBLE
            holder.binding.ivVideoCall.visibility = View.GONE
        }
        else {
            val history = videoList[holder.adapterPosition]

            Picasso.get().load(history.img)
                .resize(500, 500)
                .placeholder(R.drawable.bg_blue)
                .into(holder.binding.ivLogo)

            val formattedString = formatDate(history.lastcallTime)
            holder.binding.tvMessage.text = formattedString
            holder.binding.tvName.text = history.name
            holder.binding.cardMsgCount.visibility = View.GONE
            holder.binding.ivCall.visibility = View.GONE
            holder.binding.ivVideoCall.visibility = View.VISIBLE
        }
        holder.binding.ivCall.setOnClickListener {
            onCallClicked(holder.adapterPosition)
        }
        holder.binding.ivVideoCall.setOnClickListener {
            onVideoCallClicked(holder.adapterPosition)
        }
        holder.itemView.setOnClickListener {
            onItemClick(holder.adapterPosition)
        }
    }
}


fun formatDate(date: Date): String {
    val calendar = Calendar.getInstance()
    calendar.time = date

    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DATE, -1)

    val sdfTime = SimpleDateFormat("h:mm a", Locale.getDefault())
    val sdfDate = SimpleDateFormat("d MMM", Locale.getDefault())

    return when {
        calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) ->
            "Today ${sdfTime.format(date)}"
        calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) ->
            "Yesterday ${sdfTime.format(date)}"
        else -> "${sdfDate.format(date)} ${sdfTime.format(date)}"
    }
}

data class PersonCallHistory(
    val img: Int,
    val name: String,
    val msg: String,
    val lastCall: String,
    val msgCount: Int = 100
)