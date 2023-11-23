package com.videocall.livecelebrity.prankcall.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceManager(val mContext: Context) {
    private var sharedPref: SharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        const val AUDIO_LIST_KEY = "AudioListKey"
        const val VIDEO_LIST_KEY = "VideoListKey"
        const val CHAT_LIST_KEY = "ChatListKey"
        const val SHARED_PREF = "DataSharedPref"
    }

    fun getAudioList(): ArrayList<CallHistory>{
        val audioListJson = sharedPref.getString(AUDIO_LIST_KEY, "")
        if(audioListJson == "") return arrayListOf()
        val typeToken = object : TypeToken<ArrayList<CallHistory>>(){}.type
        val audioList: ArrayList<CallHistory> = gson.fromJson(audioListJson, typeToken)
        return audioList
    }

    fun addToAudioList(audioHistory: CallHistory){
        val audioListJson = sharedPref.getString(AUDIO_LIST_KEY, "")
        if(audioListJson == ""){
            sharedPref.edit().putString(AUDIO_LIST_KEY, gson.toJson(arrayListOf(audioHistory))).apply()
        }
        else{
            val typeToken = object : TypeToken<ArrayList<CallHistory>>(){}.type
            val audioList: ArrayList<CallHistory> = gson.fromJson(audioListJson, typeToken)
            audioList.add(audioHistory)
            sharedPref.edit().putString(AUDIO_LIST_KEY, gson.toJson(audioList)).apply()
        }
    }

    fun getVideoCallList(): ArrayList<CallHistory>{
        val videoListJson = sharedPref.getString(VIDEO_LIST_KEY, "")
        if(videoListJson == "") return arrayListOf()
        val typeToken = object : TypeToken<ArrayList<CallHistory>>(){}.type
        val videoList: ArrayList<CallHistory> = gson.fromJson(videoListJson, typeToken)
        return videoList
    }

    fun addToVideoList(videoHistory: CallHistory){
        val videoListJson = sharedPref.getString(VIDEO_LIST_KEY, "")
        if(videoListJson == ""){
            sharedPref.edit().putString(VIDEO_LIST_KEY, gson.toJson(arrayListOf(videoHistory))).apply()
        }
        else{
            val typeToken = object : TypeToken<ArrayList<CallHistory>>(){}.type
            val videoList: ArrayList<CallHistory> = gson.fromJson(videoListJson, typeToken)
            videoList.add(videoHistory)
            sharedPref.edit().putString(VIDEO_LIST_KEY, gson.toJson(videoList)).apply()
        }
    }

    fun getChatsList(): ArrayList<ChatHistory>{
        val chatsJson = sharedPref.getString(CHAT_LIST_KEY, "")
        if(chatsJson == "") return arrayListOf()
        val typeToken = object : TypeToken<ArrayList<ChatHistory>>(){}.type
        val chatsList: ArrayList<ChatHistory> = gson.fromJson(CHAT_LIST_KEY, typeToken)
        return chatsList
    }

    fun setChatList(newChat: ChatHistory, oldChat: ChatHistory){
        val chatsJson = sharedPref.getString(CHAT_LIST_KEY, "")
        val typeToken = object : TypeToken<ArrayList<ChatHistory>>(){}.type
        val chatsList: ArrayList<ChatHistory> = gson.fromJson(chatsJson, typeToken)
        chatsList.remove(oldChat)
        chatsList.add(newChat)
        sharedPref.edit().putString(CHAT_LIST_KEY, gson.toJson(chatsList)).apply()
    }

    fun addToChatsList(chat: ChatHistory){
        val chatsJson = sharedPref.getString(CHAT_LIST_KEY, "")
        if(chatsJson == ""){
            sharedPref.edit().putString(CHAT_LIST_KEY, gson.toJson(arrayListOf(chat))).apply()
        }
        else {
            val typeToken = object : TypeToken<ArrayList<ChatHistory>>(){}.type
            val chatsList: ArrayList<ChatHistory> = gson.fromJson(chatsJson, typeToken)
            chatsList.add(chat)
            sharedPref.edit().putString(CHAT_LIST_KEY, gson.toJson(chatsList)).apply()
        }
    }
}