package com.farmdirect.app.domain.model

data class VideoContent(
    val id: String = "", val creatorName: String = "", val title: String = "",
    val description: String = "", val videoUrl: String = "", val thumbnailUrl: String = "",
    val views: Int = 0, val likes: Int = 0, val category: String = "FARM_TOUR",
    val createdAt: Long = System.currentTimeMillis()
)

data class UploadVideoRequest(val title: String, val description: String, val category: String, val videoUri: String, val thumbnailUri: String)
