package com.videocall.livecelebrity.prankcall.utils

import com.videocall.livecelebrity.prankcall.splash.SplashScreenActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class ChatGptCompletions {
    private lateinit var chatApi: ChatApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // Adjust timeout duration as needed
                    .readTimeout(30, TimeUnit.SECONDS) // Adjust timeout duration as needed
                    .build()
            )
            .build()

        chatApi = retrofit.create(ChatApi::class.java)
    }

    fun getCompletion(personName: String, prompt: String, onResponse: (msg: String) -> Unit) {
        val body = createRequestBody(personName, prompt)
        val authToken = "Bearer ${SplashScreenActivity.chatGptAccessToken}"
        val call = chatApi.getChatCompletions(authToken, body)
        call.enqueue(object : Callback<CompletionResponse> {
            override fun onResponse(call: Call<CompletionResponse>, response: Response<CompletionResponse>) {
                val msg = response.body()?.choices?.get(0)?.message?.content
                msg?.let{onResponse(it)}
            }

            override fun onFailure(call: Call<CompletionResponse>, t: Throwable) {}
        })
    }

    fun createRequestBody(personName: String, prompt: String): RequestBody {
        val mediaType = "application/json".toMediaType()
        val requestBodyString = """
        {
            "model": "gpt-3.5-turbo",
            "messages": [
                {
                    "role": "user",
                    "content": "Can you talk as if you are $personName and give replies in not more than 10 lines"
                },
                {
                    "role": "system",
                    "content": "Sure I will talk like $personName and provide details accordingly"
                },
                 {
                    "role": "user",
                    "content": "$prompt"
                }
            ]
        }
        """
        return requestBodyString.toRequestBody(mediaType)
    }

}


interface ChatApi {
    @Headers(
        "Content-Type: application/json",
//        "Cookie: __cf_bm=HQNR3lqVj4fFaQ7Pd84F5GFAijqJDA94eowKmAUibBs-1700809592-0-AZvV0lUysIXwYihNu0zX+ZlnawhDxXJDiHY5ugPfSzhDdEf9kaRDoQwdYKwfKoZAa2NWMyhAyYiaaSRDx9dzb0Y=; _cfuvid=cF.qri.W7ebj73LF_L6r1mRB5EWuJgbWGxyUfVufxfA-1700809592571-0-604800000"
    )
    @POST("v1/chat/completions")
    fun getChatCompletions(
        @Header("Authorization") authToken: String,
        @Body body: RequestBody
    ): Call<CompletionResponse>
}

data class CompletionResponse(
    val id: String,
    val choices: Array<ChoiceResponse>
)

data class ChoiceResponse(
    val index: Long,
    val message: Message,
    val finish_reason: String
)

data class Message(
    val role: String,
    val content: String
)