package com.videocall.livecelebrity.prankcall.utils

import java.util.Date

data class CallHistory(
    val id:String,
    val img: String,
    val name: String,
    val audioUrl: String,
    val lastcallTime: Date
)

data class ChatHistory(
    val id: String,
    val img: String,
    val name: String,
    var msgList: ArrayList<Pair<Int, Msg>>,
    var lastChatTime: Date,
    val profession: String = "",
)

data class Msg(
    val content: String,
    val time: String
)

data class Celebrity(
    val name: String,
    val profession: String,
    var profile_url: String,
    val audio_url_list: ArrayList<String>,
    val video_url_list: ArrayList<String>,
    val industry: String
)