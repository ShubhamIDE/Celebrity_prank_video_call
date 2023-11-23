package com.videocall.livecelebrity.prankcall.calllog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.videocall.livecelebrity.prankcall.utils.CallHistory
import com.videocall.livecelebrity.prankcall.utils.ChatHistory
import com.videocall.livecelebrity.prankcall.utils.PreferenceManager
import com.videocall.livecelebrity.prankcall.video.VideoFragment

class CallLogFragment : Fragment() {

    private lateinit var binding: FragmentCallLogBinding
    private lateinit var chatsAdapter: CallLogRVAdapter
    private lateinit var audioAdapter: CallLogRVAdapter
    private lateinit var videoAdapter: CallLogRVAdapter

    private lateinit var audioCallHistory: ArrayList<CallHistory>
    private lateinit var videoCallHistory: ArrayList<CallHistory>
    private lateinit var chatsHistoryList: ArrayList<ChatHistory>

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallLogBinding.inflate(inflater, container, false)

        preferenceManager = PreferenceManager(requireContext())

        audioCallHistory = preferenceManager.getAudioList()
        videoCallHistory = preferenceManager.getVideoCallList()
        chatsHistoryList = preferenceManager.getChatsList()

        chatsAdapter = CallLogRVAdapter(chatsHistoryList, audioCallHistory, videoCallHistory, TYPE_MESSAGE,
            onCallClicked = {},
            onVideoCallClicked = {},
            onItemClick = {
                ChatsFragment.chatHistory = chatsHistoryList[it]
                findNavController().navigate(R.id.action_callLogFragment_to_chatsFragment)
            })

        audioAdapter = CallLogRVAdapter(chatsHistoryList, audioCallHistory, videoCallHistory, TYPE_AUDIO,
            onCallClicked = {
                AudioFragment.audioHistory = audioCallHistory[it]
                findNavController().navigate(R.id.action_callLogFragment_to_audioFragment)
            },
            onVideoCallClicked = {},
            onItemClick = {})

        videoAdapter = CallLogRVAdapter(chatsHistoryList, audioCallHistory, videoCallHistory, TYPE_VIDEO,
            onCallClicked = {},
            onVideoCallClicked = {
                VideoFragment.videoHistory = videoCallHistory[it]
                findNavController().navigate(R.id.action_callLogFragment_to_videoFragment)
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
        }

        binding.llAudio.setOnClickListener {
            binding.rvAudioCalls.visibility = View.VISIBLE
            binding.rvVideoCalls.visibility = View.GONE
            binding.rvChats.visibility = View.GONE
            it.alpha = 1.0f
            binding.llChat.alpha = 0.4f
            binding.llVideo.alpha = 0.4f
        }

        binding.llVideo.setOnClickListener {
            binding.rvAudioCalls.visibility = View.GONE
            binding.rvVideoCalls.visibility = View.VISIBLE
            binding.rvChats.visibility = View.GONE
            it.alpha = 1.0f
            binding.llAudio.alpha = 0.4f
            binding.llChat.alpha = 0.4f
        }

        return binding.root
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
            Glide.with(holder.binding.ivLogo).load(history.img).into(holder.binding.ivLogo)
            holder.binding.tvName.text = history.name
            holder.binding.tvMessage.text = history.msgList[history.msgList.size-1].second.content
            holder.binding.msgCount.text = history.msgList.size.toString()

            holder.binding.cardMsgCount.visibility = View.VISIBLE
            holder.binding.ivCall.visibility = View.GONE
            holder.binding.ivVideoCall.visibility = View.GONE
        }
        else if(type == TYPE_AUDIO){

            val history = audioList[holder.adapterPosition]
            Glide.with(holder.binding.ivLogo).load(history.img).into(holder.binding.ivLogo)
            holder.binding.tvMessage.text = history.lastCallDateString

            holder.binding.cardMsgCount.visibility = View.GONE
            holder.binding.ivCall.visibility = View.VISIBLE
            holder.binding.ivVideoCall.visibility = View.GONE
        }
        else {

            val history = videoList[holder.adapterPosition]
            Glide.with(holder.binding.ivLogo).load(history.img).into(holder.binding.ivLogo)
            holder.binding.tvMessage.text = history.lastCallDateString

            holder.binding.cardMsgCount.visibility = View.GONE
            holder.binding.ivCall.visibility = View.GONE
            holder.binding.ivVideoCall.visibility = View.VISIBLE
            holder.binding.tvMessage.text = history.lastCallDateString
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

data class PersonCallHistory(
    val img: Int,
    val name: String,
    val msg: String,
    val lastCall: String,
    val msgCount: Int = 100
)