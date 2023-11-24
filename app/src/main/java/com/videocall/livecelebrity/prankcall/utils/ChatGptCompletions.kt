package com.videocall.livecelebrity.prankcall.utils

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class ChatGptCompletions {
    private lateinit var chatApi: ChatApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        chatApi = retrofit.create(ChatApi::class.java)
    }

    fun getCompletion(personName: String, prompt: String) {
        val body = createRequestBody(personName, prompt)
        val call = chatApi.getChatCompletions(body)
        call.enqueue(object : Callback<CompletionResponse> {
            override fun onResponse(call: Call<CompletionResponse>, response: Response<CompletionResponse>) {
                // Handle successful response here
                Log.d("chatResponse", response.body().toString())
            }

            override fun onFailure(call: Call<CompletionResponse>, t: Throwable) {
                // Handle failure here
                Log.d("chatResponse", call.toString())
                Log.d("chatResponse", t.message.toString())
            }
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
                    "content": "Can you talk as if you are $personName"
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
        "Authorization: Bearer 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",
        "Cookie: __cf_bm=HQNR3lqVj4fFaQ7Pd84F5GFAijqJDA94eowKmAUibBs-1700809592-0-AZvV0lUysIXwYihNu0zX+ZlnawhDxXJDiHY5ugPfSzhDdEf9kaRDoQwdYKwfKoZAa2NWMyhAyYiaaSRDx9dzb0Y=; _cfuvid=cF.qri.W7ebj73LF_L6r1mRB5EWuJgbWGxyUfVufxfA-1700809592571-0-604800000"
    )
    @POST("v1/chat/completions")
    fun getChatCompletions(@Body body: RequestBody): Call<CompletionResponse>
}

data class CompletionResponse(
    val id: String,
    val choices: List<ChoiceResponse>
)

data class ChoiceResponse(
    val index: Long,
    val message: List<Message>,
    val finish_reason: String
)

data class Message(
    val role: String,
    val content: String
)