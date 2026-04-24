package com.farmdirect.app.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.ChatRepository
import com.farmdirect.app.domain.model.Chat
import com.farmdirect.app.domain.model.Message
import com.farmdirect.app.domain.model.SendMessageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val preferences: FarmDirectPreferences,
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats = _chats.asStateFlow()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun loadChats() {
        viewModelScope.launch {
            val token = preferences.authToken.firstOrNull() ?: ""
            _chats.value = chatRepository.getChats(token)
        }
    }

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            val token = preferences.authToken.firstOrNull() ?: ""
            _messages.value = chatRepository.getMessages(token, chatId)
        }
    }

    fun sendMessage(receiverId: String, content: String) {
        viewModelScope.launch {
            val token = preferences.authToken.firstOrNull() ?: ""
            chatRepository.sendMessage(token, SendMessageRequest(receiverId, content))
        }
    }
}
