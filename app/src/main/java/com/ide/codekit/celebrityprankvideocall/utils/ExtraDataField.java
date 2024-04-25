package com.ide.codekit.celebrityprankvideocall.utils;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ExtraDataField {
    public class Data{
    }
    String base_url;
    String youtubeBaseUrl;
    ArrayList<String> celebrities_list;
    JsonObject data;
    String chatGptAccessToken;

    public String getChatGptAccessToken() {
        return chatGptAccessToken;
    }

    public void setChatGptAccessToken(String chatGptAccessToken) {
        this.chatGptAccessToken = chatGptAccessToken;
    }

    public String getBase_url() {
        return this.base_url;
    }
    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getYoutubeBaseUrl() {
        return youtubeBaseUrl;
    }

    public void setYoutubeBaseUrl(String youtubeBaseUrl) {
        this.youtubeBaseUrl = youtubeBaseUrl;
    }

    public ArrayList<String> getCelebrities_list() {
        return this.celebrities_list;
    }
    public void setCelebrities_list(ArrayList<String> celebrities_list) {
        this.celebrities_list = celebrities_list;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

}
