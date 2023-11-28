package com.videocall.livecelebrity.prankcall.message

import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adsmodule.api.adsModule.AdUtils
import com.adsmodule.api.adsModule.utils.Constants
import com.bumptech.glide.Glide
import com.flurry.sdk.p
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.SingletonClasses.AppOpenAds
import com.videocall.livecelebrity.prankcall.audio.AudioFragment
import com.videocall.livecelebrity.prankcall.databinding.CustomReceiveMsgBinding
import com.videocall.livecelebrity.prankcall.databinding.CustomSendMsgBinding
import com.videocall.livecelebrity.prankcall.databinding.FragmentChatsBinding
import com.videocall.livecelebrity.prankcall.databinding.HsvSuggestionItemBinding
import com.videocall.livecelebrity.prankcall.utils.Celebrity
import com.videocall.livecelebrity.prankcall.utils.ChatGptCompletions
import com.videocall.livecelebrity.prankcall.utils.ChatHistory
import com.videocall.livecelebrity.prankcall.utils.Msg
import com.videocall.livecelebrity.prankcall.utils.PreferenceManager
import com.videocall.livecelebrity.prankcall.video.VideoFragment
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
    private var addMsgJob: Job = Job()
    private val apiCallScope  = CoroutineScope(Dispatchers.IO + job)
    private lateinit var chatCompletion: ChatGptCompletions
    private var systemTyping = false
    private var suggestionsList = listOf(
        "What's an emoji that you think describes me, and one that describes you?",
        "What's your favorite movie, and how many times do you think you've seen it?",
        "If you could travel anywhere in the world right now, where would you go?",
        "Hey, want to try out that restaurant in town that just opened?",
        "What's the most embarrassing thing that happened to you in your recent memory?",
        "Who have you been friends with the longest and how did you meet?",
        "I have an extra ticket to this concert, do you want to come with me?",
        "I was just thinking about how much fun we have together. I am so relieved that I have you to keep me out of trouble! Where did you learn how to be such a great leader?",
        "I wanted to remind you how awesome you are! Let's catch up! What have you been up to?",
        "Pandemics are the worst, but you are the best! I have always admired your ability to look on the bright side. There has never been a better time to remind you of that! What's on your post-pandemic to-do list?"
    )

    companion object{
        var chatHistory: ChatHistory? = null
        lateinit var celebrity: Celebrity
        var fromHome = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)

        chatCompletion = ChatGptCompletions()
        preferenceManager = PreferenceManager(requireContext())
        binding.tvName.text = celebrity.name


        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                val chatList = preferenceManager.getChatsList()
                for(chat in chatList){
                    if(chat.id.equals(celebrity.profile_url)){
                        chatHistory = chat
                        break
                    }
                }

                if(chatHistory!=null){
                    msgList.clear()
                    msgList.addAll(chatHistory!!.msgList.reversed())
                    for(i in msgList.size-1 downTo 0){
                        val time = msgList[i].second.time
                        val msg = msgList[i].second.content
                        val type = msgList[i].first
                        withContext(Dispatchers.Main){
                            addMsgToScrollView(msg, type, time)
                        }
                    }
                }
                else {
                    chatHistory = ChatHistory(
                        id = celebrity.profile_url,
                        name = celebrity.name,
                        profession = celebrity.profession,
                        img = celebrity.profile_url,
                        msgList = arrayListOf(),
                        lastChatTime = Date()
                    )
                }

                if(chatHistory!!.msgList.isEmpty()){
                    binding.tvSuggestions.visibility = View.VISIBLE
                    binding.suggestionsHSV.visibility = View.VISIBLE
                    val list = suggestionsList.shuffled()
                    for(sugg in list){

                        val suggestionItem = HsvSuggestionItemBinding.inflate(LayoutInflater.from(requireContext()))
                        suggestionItem.tvSuggestion.text = sugg
                        withContext(Dispatchers.Main){
                            val layoutParams = LinearLayout.LayoutParams(450, WRAP_CONTENT)
                            layoutParams.setMargins(0, 0, 30, 0)
                            suggestionItem.root.layoutParams = layoutParams
                            binding.llSuggestions.addView(suggestionItem.root)

                            suggestionItem.root.setOnClickListener {
                                val time = getCurrentTimeIn12HourFormat()
                                addMsgToScrollView(sugg, 0, time)
                                msgList.add(Pair(0, Msg(sugg, time)))
                                addData()
                                getMessageFromSystem(sugg)
                                binding.tvSuggestions.visibility = View.GONE
                                binding.suggestionsHSV.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            AdUtils.showBackPressAds(
                AppOpenAds.activity,
                Constants.adsResponseModel.app_open_ads.adx,
            ) { state_load: Boolean ->
//                if (fromHome) {
//                    findNavController().popBackStack(R.id.homeFragment, false)
//                } else
                findNavController().navigateUp()
            }
        }

        binding.btnVideoCall.setOnClickListener {
            VideoFragment.celebrity = celebrity
            VideoFragment.fromHome = false
            AdUtils.showInterstitialAd(
                Constants.adsResponseModel.interstitial_ads.adx,
                AppOpenAds.activity
            ) { state_load: Boolean ->
                findNavController().navigate(R.id.action_chatsFragment_to_videoFragment)
            }
        }

        binding.ivCall.setOnClickListener {
            AudioFragment.celebrity = celebrity
            AudioFragment.fromHome = false
            AdUtils.showInterstitialAd(
                Constants.adsResponseModel.interstitial_ads.adx,
                AppOpenAds.activity
            ) { state_load: Boolean ->
                findNavController().navigate(R.id.action_chatsFragment_to_audioFragment)
            }
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

        requireActivity().onBackPressedDispatcher.addCallback {
            AdUtils.showBackPressAds(
                AppOpenAds.activity,
                Constants.adsResponseModel.app_open_ads.adx,
            ) { state_load: Boolean ->
//                if(fromHome){
//                    findNavController().popBackStack(R.id.homeFragment, false)
//                }
//                else
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    fun addData(){
        chatHistory!!.msgList = msgList
        addMsgJob.cancel()
        addMsgJob = lifecycleScope.launch {
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

            chatCompletion.getCompletion(chatHistory!!.name, msg){
                systemTyping = false
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

    override fun onDestroy() {
        super.onDestroy()
        fromHome = false
        chatHistory = null
    }
}