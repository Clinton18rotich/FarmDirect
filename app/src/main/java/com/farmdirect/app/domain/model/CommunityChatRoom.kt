package com.farmdirect.app.domain.model

data class CommunityChatRoom(
    val id: String = "",
    val name: String = "",
    val topic: String = "",
    val description: String = "",
    val emoji: String = "💬",
    val memberCount: Int = 0,
    val onlineCount: Int = 0,
    val isJoined: Boolean = false,
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val isAnnouncement: Boolean = false,
    val tags: List<String> = emptyList()
)

val defaultChatRooms = listOf(
    CommunityChatRoom("1", "Maize Farmers Kenya", "🌽", "Connect with maize farmers across Kenya", memberCount = 1234, onlineCount = 89),
    CommunityChatRoom("2", "Dairy Farmers Hub", "🐄", "Share tips on dairy farming and milk production", memberCount = 892, onlineCount = 56),
    CommunityChatRoom("3", "Organic Farming", "🌿", "Discuss organic farming methods and certification", memberCount = 567, onlineCount = 34),
    CommunityChatRoom("4", "Farm Machinery", "🚜", "Buy, sell, and discuss farm equipment", memberCount = 445, onlineCount = 23),
    CommunityChatRoom("5", "Market Prices Today", "💰", "Real-time price updates from major markets", memberCount = 2100, onlineCount = 145, isAnnouncement = true),
    CommunityChatRoom("6", "Weather Watch", "🌤️", "Weather updates and farming advisories", memberCount = 987, onlineCount = 67, isAnnouncement = true),
    CommunityChatRoom("7", "Poultry Keepers", "🐔", "Everything about chicken farming", memberCount = 678, onlineCount = 45),
    CommunityChatRoom("8", "Young Farmers", "🌟", "Youth in agriculture - opportunities and innovation", memberCount = 543, onlineCount = 78),
    CommunityChatRoom("9", "Women in Agriculture", "👩‍🌾", "Empowering women farmers across Africa", memberCount = 789, onlineCount = 56),
    CommunityChatRoom("10", "Export Ready", "🌍", "For farmers ready to export their produce", memberCount = 234, onlineCount = 15)
)
