package com.farmdirect.app.domain.model

data class Podcast(
    val id: String = "", val title: String = "", val description: String = "",
    val host: String = "", val category: String = "FARMING_TIPS", val rating: Double = 0.0,
    val subscribers: Int = 0, val totalEpisodes: Int = 0, val thumbnailUrl: String = "",
    val isSubscribed: Boolean = false
)

data class PodcastEpisode(val id: String = "", val title: String = "", val audioUrl: String = "", val duration: Int = 0, val isPlayed: Boolean = false)
