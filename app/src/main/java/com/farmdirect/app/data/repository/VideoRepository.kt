package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.VideoContent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor() {
    suspend fun getVideoFeed(token: String, page: Int = 1): List<VideoContent> {
        val response = RetrofitClient.apiService.getVideoFeed("Bearer $token", page)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun likeVideo(token: String, videoId: String): Boolean {
        return RetrofitClient.apiService.likeVideo("Bearer $token", videoId).isSuccessful
    }
}
