package com.farmdirect.app.domain.model

data class Message(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val receiverId: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class Chat(
    val id: String = "",
    val otherUserId: String = "",
    val otherUserName: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0
)

data class SendMessageRequest(val receiverId: String, val content: String)
