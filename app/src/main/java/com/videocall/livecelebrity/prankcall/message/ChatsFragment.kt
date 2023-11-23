package com.videocall.livecelebrity.prankcall.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.flurry.sdk.p
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.CustomReceiveMsgBinding
import com.videocall.livecelebrity.prankcall.databinding.CustomSendMsgBinding
import com.videocall.livecelebrity.prankcall.databinding.FragmentChatsBinding
import com.videocall.livecelebrity.prankcall.utils.ChatHistory
import com.videocall.livecelebrity.prankcall.utils.Msg
import com.videocall.livecelebrity.prankcall.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private val msgList = arrayListOf<Pair<Int, Msg>>()
    private lateinit var preferenceManager: PreferenceManager

    companion object{
        var chatHistory: ChatHistory? = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)

        preferenceManager = PreferenceManager(requireContext())

        if(chatHistory!=null){
            msgList.addAll(chatHistory!!.msgList)
            for(i in msgList.size-1 downTo 0){
                val time = msgList[i].second.time
                val msg = msgList[i].second.content
                val type = msgList[i].first
                addMsgToScrollView(msg, type, time)
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnVideoCall.setOnClickListener {
            findNavController().navigate(R.id.action_chatsFragment_to_videoFragment)
        }

        binding.ivCall.setOnClickListener {
            findNavController().navigate(R.id.action_chatsFragment_to_audioFragment)
        }

        binding.btnSendMsg.setOnClickListener {
            val msg = binding.edtTxtMsgs.text.toString()
            if(msg.trim()!=""){
                val time = getCurrentTimeIn12HourFormat()
                addMsgToScrollView(msg, 0, time)
            }
        }

        return binding.root
    }

    fun addMsgToScrollView(msg: String, type: Int, time: String){
        if(type == 0){
            val sendMsgView = CustomSendMsgBinding.inflate(LayoutInflater.from(requireContext()))
            sendMsgView.tvMsg.text = msg
            sendMsgView.tvTime.text = time
            binding.msgLL.addView(sendMsgView.root)
            msgList.add(Pair(0, Msg(msg, time)))
        }
        else {
            val sendMsgView = CustomReceiveMsgBinding.inflate(LayoutInflater.from(requireContext()))
            sendMsgView.tvMsg.text = msg
            sendMsgView.tvTime.text = time
            binding.msgLL.addView(sendMsgView.root)
            msgList.add(Pair(1, Msg(msg, time)))
        }
    }

    fun getCurrentTimeIn12HourFormat(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(chatHistory!=null){
            chatHistory!!.lastChatTime = Calendar.getInstance().time
            if(!chatHistory!!.msgList.isEmpty()){
                val oldChat = chatHistory
                chatHistory!!.msgList = msgList
                preferenceManager.setChatList(chatHistory!!, oldChat!!)
            }
            else{
                chatHistory!!.msgList = msgList
                preferenceManager.addToChatsList(chatHistory!!)
            }
        }
    }
}