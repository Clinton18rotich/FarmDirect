package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.Chat
import com.farmdirect.app.domain.model.Message
import com.farmdirect.app.domain.model.SendMessageRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor() {
    suspend fun getChats(token: String): List<Chat> {
        val response = RetrofitClient.apiService.getChats("Bearer $token")
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun getMessages(token: String, chatId: String): List<Message> {
        val response = RetrofitClient.apiService.getMessages("Bearer $token", chatId)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun sendMessage(token: String, request: SendMessageRequest): Message? {
        val response = RetrofitClient.apiService.sendMessage("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
}
