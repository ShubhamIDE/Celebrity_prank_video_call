package com.videocall.livecelebrity.prankcall.utils

import java.util.Date

data class CallHistory(
    val id:String,
    val img: String,
    val name: String,
    var lastCallDateString:String,
    val audioUrl: String,
    val lastcallTime: Date
)

data class ChatHistory(
    val id: String,
    val img: String,
    val name: String,
    var msgList: ArrayList<Pair<Int, Msg>>,
    var lastChatTime: Date
)

data class Msg(
    val content: String,
    val time: String
)