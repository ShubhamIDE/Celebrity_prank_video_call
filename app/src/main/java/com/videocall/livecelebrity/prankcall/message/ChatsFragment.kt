package com.videocall.livecelebrity.prankcall.message

import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.flurry.sdk.p
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.audio.AudioFragment
import com.videocall.livecelebrity.prankcall.databinding.CustomReceiveMsgBinding
import com.videocall.livecelebrity.prankcall.databinding.CustomSendMsgBinding
import com.videocall.livecelebrity.prankcall.databinding.FragmentChatsBinding
import com.videocall.livecelebrity.prankcall.utils.ChatGptCompletions
import com.videocall.livecelebrity.prankcall.utils.ChatHistory
import com.videocall.livecelebrity.prankcall.utils.Msg
import com.videocall.livecelebrity.prankcall.utils.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private val msgList = arrayListOf<Pair<Int, Msg>>()
    private lateinit var preferenceManager: PreferenceManager
    private val job: Job = Job()
    private val apiCallScope  = CoroutineScope(Dispatchers.IO + job)
    private lateinit var chatCompletion: ChatGptCompletions
    private val personName = "Alia Bhatt"
    private var message = "What is your favourite movie"
    private var systemTyping = false

    companion object{
        var chatHistory: ChatHistory? = null
        val chatGptApiKey = "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"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)

        binding.tvName.text = personName

        chatHistory = ChatHistory(
            "1",
            "https://unsplash.com/photos/black-and-white-plane-flying-over-the-sea-during-daytime-rNqs9hM0U8I",
            "Alia Bhatt",
            arrayListOf(),
            Date()
        )
        chatCompletion = ChatGptCompletions()
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
            if(msg.trim()!="" && !systemTyping){
                binding.edtTxtMsgs.setText("")
                val time = getCurrentTimeIn12HourFormat()
                addMsgToScrollView(msg, 0, time)
                msgList.add(Pair(0, Msg(msg, time)))
                addData()
                getMessageFromSystem(msg)
            }
        }


        return binding.root
    }

    fun addData(){
        if(chatHistory!=null){
            chatHistory!!.msgList = msgList
            preferenceManager.addToChatsList(chatHistory!!)
        }
    }

    fun getMessageFromSystem(msg: String){
        apiCallScope.launch {
            systemTyping = true
            if(isAdded && !isDetached){
                withContext(Dispatchers.Main){
                    binding.tvTyping.visibility = View.VISIBLE
                    binding.edtTxtMsgs.hint = getString(R.string.please_wait_for_reply)
                    binding.edtTxtMsgs.isEnabled = false
                }
            }

            chatCompletion.getCompletion(personName, msg){
                systemTyping = false
                message = it
                if(isAdded && !isDetached){
                    val sendMsgView = CustomReceiveMsgBinding.inflate(LayoutInflater.from(requireContext()))
                    sendMsgView.tvMsg.text = it
                    val time = getCurrentTimeIn12HourFormat()
                    sendMsgView.tvTime.text = time
                    binding.edtTxtMsgs.hint = getString(R.string.type_something)
                    binding.edtTxtMsgs.isEnabled = true
                    binding.msgLL.addView(sendMsgView.root)
                    binding.tvTyping.visibility = View.GONE
                    msgList.add(Pair(1, Msg(it, time)))
                    addData()
                }
            }
        }
    }

    fun addMsgToScrollView(msg: String, type: Int, time: String){
        if(type == 0){
            val sendMsgView = CustomSendMsgBinding.inflate(LayoutInflater.from(requireContext()))
            sendMsgView.tvMsg.text = msg
            sendMsgView.tvTime.text = time
            binding.msgLL.addView(sendMsgView.root)
        }
        else {
            val sendMsgView = CustomReceiveMsgBinding.inflate(LayoutInflater.from(requireContext()))
            sendMsgView.tvMsg.text = msg
            sendMsgView.tvTime.text = time
            binding.msgLL.addView(sendMsgView.root)
        }
    }

    fun getCurrentTimeIn12HourFormat(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}