package com.videocall.livecelebrity.prankcall.calllog

import android.app.Person
import android.os.Bundle
import android.provider.CallLog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentCallLogBinding
import com.videocall.livecelebrity.prankcall.databinding.RvChatItemBinding
import com.videocall.livecelebrity.prankcall.databinding.RvPartnerItemBinding
import com.videocall.livecelebrity.prankcall.home.ChoosePartnerAdapter
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment.Companion.TYPE_AUDIO
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment.Companion.TYPE_MESSAGE
import com.videocall.livecelebrity.prankcall.home.PartnerChooseFragment.Companion.TYPE_VIDEO

class CallLogFragment : Fragment() {

    private lateinit var binding: FragmentCallLogBinding
    private lateinit var chatsAdapter: CallLogRVAdapter
    private lateinit var audioAdapter: CallLogRVAdapter
    private lateinit var videoAdapter: CallLogRVAdapter

    private val historyList = listOf(
        PersonCallHistory(
            R.drawable.ic_launcher_background,
            "Name 1",
            "Hello there how are you",
            "Today 5:37 pm"
        ),
        PersonCallHistory(
            R.drawable.ic_launcher_background,
            "Name 1",
            "Hello there how are you",
            "Today 5:37 pm"
        ),
        PersonCallHistory(
            R.drawable.ic_launcher_background,
            "Name 1",
            "Hello there how are you",
            "Today 5:37 pm"
        ),
        PersonCallHistory(
            R.drawable.ic_launcher_background,
            "Name 1",
            "Hello there how are you",
            "Today 5:37 pm"
        ),
        PersonCallHistory(
            R.drawable.ic_launcher_background,
            "Name 1",
            "Hello there how are you",
            "Today 5:37 pm"
        ),
        PersonCallHistory(
            R.drawable.ic_launcher_background,
            "Name 1",
            "Hello there how are you",
            "Today 5:37 pm"
        ),
        PersonCallHistory(
            R.drawable.ic_launcher_background,
            "Name 1",
            "Hello there how are you",
            "Today 5:37 pm"
        ),
        PersonCallHistory(
            R.drawable.ic_launcher_background,
            "Name 1",
            "Hello there how are you",
            "Today 5:37 pm"
        ),
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallLogBinding.inflate(inflater, container, false)

        chatsAdapter = CallLogRVAdapter(historyList, TYPE_MESSAGE,
            onCallClicked = {

            },
            onVideoCallClicked = {

            },
            onItemClick = {

            })

        audioAdapter = CallLogRVAdapter(historyList, TYPE_AUDIO,
            onCallClicked = {

            },
            onVideoCallClicked = {

            },
            onItemClick = {

            })

        videoAdapter = CallLogRVAdapter(historyList, TYPE_VIDEO,
            onCallClicked = {

            },
            onVideoCallClicked = {

            },
            onItemClick = {

            })

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
    private val personsList: List<PersonCallHistory>,
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

    override fun getItemCount() = personsList.size

    override fun onBindViewHolder(holder: HistoryVH, position: Int) {
        val history = personsList[holder.adapterPosition]
        Glide.with(holder.binding.ivLogo).load(history.img).into(holder.binding.ivLogo)
        holder.binding.tvName.text = history.name
        if(type == TYPE_MESSAGE){
            holder.binding.cardMsgCount.visibility = View.VISIBLE
            holder.binding.ivCall.visibility = View.GONE
            holder.binding.ivVideoCall.visibility = View.GONE
            holder.binding.tvMessage.text = history.msg
            holder.binding.msgCount.text = history.msgCount.toString()
        }
        else if(type == TYPE_AUDIO){
            holder.binding.cardMsgCount.visibility = View.GONE
            holder.binding.ivCall.visibility = View.VISIBLE
            holder.binding.ivVideoCall.visibility = View.GONE
            holder.binding.tvMessage.text = history.lastCall
        }
        else {
            holder.binding.cardMsgCount.visibility = View.GONE
            holder.binding.ivCall.visibility = View.GONE
            holder.binding.ivVideoCall.visibility = View.VISIBLE
            holder.binding.tvMessage.text = history.lastCall
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