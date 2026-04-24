package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.Livestock
import com.farmdirect.app.domain.model.PostLivestockRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LivestockRepository @Inject constructor() {
    suspend fun getLivestock(token: String, animalType: String? = null): List<Livestock> {
        val response = RetrofitClient.apiService.getLivestock("Bearer $token", animalType)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun postLivestock(token: String, request: PostLivestockRequest): Livestock? {
        val response = RetrofitClient.apiService.postLivestock("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
}
