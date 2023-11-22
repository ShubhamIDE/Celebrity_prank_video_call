package com.videocall.livecelebrity.prankcall.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.CustomSendMsgBinding
import com.videocall.livecelebrity.prankcall.databinding.FragmentChatsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnVideoCall.setOnClickListener {

        }

        binding.ivCall.setOnClickListener {

        }

        binding.btnSendMsg.setOnClickListener {
            val msg = binding.edtTxtMsgs.text.toString()
            if(msg.trim()!=""){
                addMsgToScrollView(msg)
            }
        }

        return binding.root
    }

    fun addMsgToScrollView(msg: String){
        val sendMsgView = CustomSendMsgBinding.inflate(LayoutInflater.from(requireContext()))
        sendMsgView.tvMsg.text = msg
        val time = getCurrentTimeIn12HourFormat()
        sendMsgView.tvTime.text = time
        binding.msgLL.addView(sendMsgView.root)
    }

    fun getCurrentTimeIn12HourFormat(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}