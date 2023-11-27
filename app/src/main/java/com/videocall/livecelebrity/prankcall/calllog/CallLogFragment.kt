package com.videocall.livecelebrity.prankcall.calllog

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.audio.AudioFragment
import com.videocall.livecelebrity.prankcall.databinding.FragmentCallLogBinding
import com.videocall.livecelebrity.prankcall.databinding.RvChatItemBinding
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
                if(SplashScreenActivity.celebrityList.value!=null){
                    ChatsFragment.chatHistory = chatsHistoryList[it]
                    var celebrity: Celebrity? = null
                    for(celeb in SplashScreenActivity.celebrityList.value!!){
                        if(celeb.profile_url == chatsHistoryList[it].img){
                            celebrity = celeb
                            break;
                        }
                    }
                    if(celebrity!=null){
                        ChatsFragment.celebrity = celebrity
                        findNavController().navigate(R.id.action_callLogFragment_to_chatsFragment)
                    }
                }
            })

        audioAdapter = CallLogRVAdapter(chatsHistoryList, audioCallHistory, videoCallHistory, TYPE_AUDIO,
            onCallClicked = {
                if(SplashScreenActivity.celebrityList.value!=null){
                    AudioFragment.audioHistory = audioCallHistory[it]
                    var celebrity: Celebrity? = null
                    for(celeb in SplashScreenActivity.celebrityList.value!!){
                        if(celeb.profile_url == audioCallHistory[it].img){
                            celebrity = celeb
                            break;
                        }
                    }
                    if(celebrity!=null){
                        AudioFragment.celebrity = celebrity!!
                        findNavController().navigate(R.id.action_callLogFragment_to_audioFragment)
                    }
                }
            },
            onVideoCallClicked = {},
            onItemClick = {})

        videoAdapter = CallLogRVAdapter(chatsHistoryList, audioCallHistory, videoCallHistory, TYPE_VIDEO,
            onCallClicked = {},
            onVideoCallClicked = {
                if(SplashScreenActivity.celebrityList.value!=null){
                    VideoFragment.videoHistory = videoCallHistory[it]
                    var celebrity: Celebrity? = null
                    for(celeb in SplashScreenActivity.celebrityList.value!!){
                        if(celeb.profile_url == videoCallHistory[it].img){
                            celebrity = celeb
                            break;
                        }
                    }
                    if(celebrity!=null){
                        VideoFragment.celebrity = celebrity!!
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

        binding.llChat.setOnClickListener {
            binding.rvAudioCalls.visibility = View.GONE
            binding.rvVideoCalls.visibility = View.GONE
            binding.rvChats.visibility = View.VISIBLE
            it.alpha = 1.0f
            binding.llAudio.alpha = 0.4f
            binding.llVideo.alpha = 0.4f
            binding.tvTitle.text = getString(R.string.chat)
        }

        binding.llAudio.setOnClickListener {
            binding.rvAudioCalls.visibility = View.VISIBLE
            binding.rvVideoCalls.visibility = View.GONE
            binding.rvChats.visibility = View.GONE
            it.alpha = 1.0f
            binding.llChat.alpha = 0.4f
            binding.llVideo.alpha = 0.4f
            binding.tvTitle.text = getString(R.string.audio_call)
        }

        binding.llVideo.setOnClickListener {
            binding.rvAudioCalls.visibility = View.GONE
            binding.rvVideoCalls.visibility = View.VISIBLE
            binding.rvChats.visibility = View.GONE
            it.alpha = 1.0f
            binding.llAudio.alpha = 0.4f
            binding.llChat.alpha = 0.4f
            binding.tvTitle.text = getString(R.string.video_call)
        }

        binding.btnBackArrow.setOnClickListener {
            findNavController().navigateUp()
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
            findNavController().navigateUp()
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
            Glide.with(holder.binding.ivLogo).load("https://images.unsplash.com/photo-1591154669695-5f2a8d20c089?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D").into(holder.binding.ivLogo)
            holder.binding.tvName.text = history.name
            if(history.msgList.isNotEmpty()){
                holder.binding.tvMessage.text = history.msgList[history.msgList.size-1].second.content
            }

            holder.binding.msgCount.text = history.msgList.size.toString()

            holder.binding.cardMsgCount.visibility = View.VISIBLE
            holder.binding.ivCall.visibility = View.GONE
            holder.binding.ivVideoCall.visibility = View.GONE
        }
        else if(type == TYPE_AUDIO){

            val history = audioList[holder.adapterPosition]
            Glide.with(holder.binding.ivLogo).load(history.img).into(holder.binding.ivLogo)
            val formattedString = formatDate(history.lastcallTime)
            holder.binding.tvMessage.text = formattedString
            holder.binding.tvName.text = history.name
            holder.binding.cardMsgCount.visibility = View.GONE
            holder.binding.ivCall.visibility = View.VISIBLE
            holder.binding.ivVideoCall.visibility = View.GONE
        }
        else {
            val history = videoList[holder.adapterPosition]
            Glide.with(holder.binding.ivLogo).load(history.img).into(holder.binding.ivLogo)
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