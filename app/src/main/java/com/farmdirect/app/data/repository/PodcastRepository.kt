package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.Podcast
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PodcastRepository @Inject constructor() {
    suspend fun getPodcasts(token: String, category: String? = null): List<Podcast> {
        val response = RetrofitClient.apiService.getPodcasts("Bearer $token", category)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun subscribeToPodcast(token: String, podcastId: String): Boolean {
        return RetrofitClient.apiService.subscribeToPodcast("Bearer $token", mapOf("podcastId" to podcastId)).isSuccessful
    }
}
